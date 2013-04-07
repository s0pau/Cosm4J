package com.cosm.client.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.cosm.client.CosmConfig;
import com.cosm.client.CosmConfig.AcceptedMediaType;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.exception.RequestInvalidException;
import com.cosm.client.http.util.ParserUtil;
import com.cosm.client.model.ConnectedObject;
import com.cosm.client.utils.StringUtil;

/**
 * Handler for building and making requests, uses {@link DefaultResponseHandler}
 * to process the response
 * 
 * @author s0pau
 */
public class DefaultRequestHandler
{
	private static final String HEADER_KEY_API = "X-ApiKey";
	private static final String HEADER_USER_AGENT = "User Agent";
	private static final String COSM_USER_AGENT = "Cosm-Java-Lib/1.0";

	private String baseURI;

	private DefaultHttpClient httpClient;

	private static DefaultRequestHandler instance;

	public enum HttpMethod
	{
		DELETE, GET, POST, PUT;
	}

	private DefaultRequestHandler()
	{
		// singleton
	}

	private DefaultRequestHandler(String baseURI)
	{
		this.baseURI = baseURI;
	}

	public static DefaultRequestHandler getInstance()
	{
		if (instance == null)
		{
			instance = new DefaultRequestHandler(CosmConfig.getInstance().getBaseURI());
		}
		return instance;
	}

	public <T extends ConnectedObject> Response<T> doRequest(HttpMethod requestMethod, String appPath)
	{
		return doRequest(requestMethod, appPath, (Map<String, Object>) null);
	}

	public <T extends ConnectedObject> Response<T> doRequest(HttpMethod requestMethod, String appPath, T... bodyObjects)
	{
		return doRequest(requestMethod, appPath, null, bodyObjects);
	}

	public <T extends ConnectedObject> Response<T> doRequest(HttpMethod requestMethod, String appPath, Map<String, Object> params)
	{
		return doRequest(requestMethod, appPath, params, null);
	}

	/**
	 * * Make the request to Cosm API and return the response string
	 * 
	 * @param <T extends ConnectedObject>
	 * 
	 * @param requestMethod
	 *            http request methods
	 * @param appPath
	 *            restful app path
	 * @param bodyObjects
	 *            objects to be parsed as body for api call
	 * @param params
	 *            key-value of params for api call
	 * 
	 * @return response string
	 */

	private <T extends ConnectedObject> Response<T> doRequest(HttpMethod requestMethod, String appPath,
			Map<String, Object> params, T... bodyObjects)
	{
		Response<T> response = null;
		HttpRequestBase request = buildRequest(requestMethod, appPath, params, bodyObjects);

		try
		{
			DefaultResponseHandler<T> responseHandler = new DefaultResponseHandler<>();
			response = getClient().execute(request, responseHandler);
		} catch (HttpException e)
		{
			throw e;
		} catch (IOException e)
		{
			throw new HttpException("Http request did not return successfully.", e);
		} catch (RuntimeException e)
		{
			// release resources manually on unexpected exceptions
			request.abort();
			throw new HttpException("Http request did not return successfully.", e);
		}

		return response;
	}

	private DefaultHttpClient getClient()
	{
		if (httpClient == null)
		{
			httpClient = HttpClientBuilder.getInstance().getHttpClient();
		}
		return httpClient;
	}

	private String concatParams(String apiCall, Map<String, Object> params)
	{
		StringBuilder retval = new StringBuilder(apiCall);
		if (params != null && !params.isEmpty())
		{
			String paramStr = toParamString(params);
			if (paramStr != null)
			{
				retval.append('?').append(toParamString(params));
			}
		}
		return retval.toString();
	}

	/**
	 * @param params
	 *            parameters where if the value is a list, the values will be
	 *            turned into a comma delimited string
	 * @return comma delimited param string containing the key-value pair of the
	 *         map given, any entry with null value is going to be dropped;
	 *         empty string if no params has valid values
	 */
	private String toParamString(Map<String, Object> params)
	{
		if (params == null || params.isEmpty())
		{
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean isFirstToken = true;
		for (Entry<String, Object> entry : params.entrySet())
		{
			Object value = entry.getValue();

			String delimited = StringUtil.toString(value);
			if (delimited != null)
			{
				if (!isFirstToken)
				{
					sb.append("&");
				}
				sb.append(entry.getKey()).append("=").append(delimited);
			}
		}

		return sb.toString();
	}

	private <T extends ConnectedObject> HttpRequestBase buildRequest(HttpMethod requestMethod, String appPath,
			Map<String, Object> params, T... bodyObjects)
	{
		AcceptedMediaType mediaType = CosmConfig.getInstance().getResponseMediaType();

		HttpRequestBase request = null;
		switch (requestMethod)
		{
		case DELETE:
			request = new HttpDelete();
			break;

		case GET:
			request = new HttpGet();
			break;

		case POST:
			request = new HttpPost();
			StringEntity postEntity = getEntity(false, bodyObjects);
			((HttpPost) request).setEntity(postEntity);
			break;

		case PUT:
			request = new HttpPut();
			StringEntity putEntity = getEntity(true, bodyObjects);
			((HttpPut) request).setEntity(putEntity);
			break;

		default:
			return null;
		}

		String uriStr = baseURI.concat(appPath);
		if (HttpMethod.GET == requestMethod)
		{
			uriStr = uriStr.concat(".").concat(mediaType.name());
		}
		uriStr = concatParams(uriStr, params);

		try
		{
			request.setURI(new URI(uriStr));
		} catch (URISyntaxException e)
		{
			throw new RequestInvalidException("Invalid URI requested.", e);
		}

		request.addHeader("accept", mediaType.getMediaType());
		request.addHeader(HEADER_KEY_API, CosmConfig.getInstance().getApiKey());
		request.addHeader(HEADER_USER_AGENT, COSM_USER_AGENT);

		return request;
	}

	private <T extends ConnectedObject> StringEntity getEntity(boolean isUpdate, T... bodyObjects)
	{
		AcceptedMediaType mediaType = CosmConfig.getInstance().getResponseMediaType();
		String json = ParserUtil.toJson(isUpdate, bodyObjects);

		StringEntity body = null;
		try
		{
			body = new StringEntity(json);
			body.setContentType(mediaType.getMediaType());
		} catch (UnsupportedEncodingException e)
		{
			throw new RequestInvalidException("Unable to encode json string for making request.", e);
		}

		return body;
	}
}
