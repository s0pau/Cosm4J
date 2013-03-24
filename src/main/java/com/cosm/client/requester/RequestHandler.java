package com.cosm.client.requester;

import java.util.Map;
import java.util.Map.Entry;

import com.cosm.client.CosmConfig;
import com.cosm.client.CosmConfig.AcceptedMediaType;
import com.cosm.client.model.ConnectedObject;
import com.cosm.client.requester.exceptions.HttpException;
import com.cosm.client.requester.utils.ParserUtil;
import com.cosm.client.requester.utils.StringUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * Handler for building and making requests
 * 
 * @author s0pau
 */
public class RequestHandler<T extends ConnectedObject>
{
	private static final String HEADER_KEY_API = "X-ApiKey";
	private static final String HEADER_USER_AGENT = "User Agent";
	// TODO share properties between this and maven
	private static final String COSM_USER_AGENT = "Cosm-Java-Lib/1.0";

	private String baseURI;

	private Client httpClient;

	public enum RequestMethod
	{
		DELETE, GET, POST, PUT;
	}

	public RequestHandler(String baseURI)
	{
		this.baseURI = baseURI;
	}

	public static RequestHandler make()
	{
		return new RequestHandler(CosmConfig.getInstance().getBaseURI());
	}

	public Response<T> doRequest(RequestMethod requestMethod, String appPath)
	{
		return doRequest(requestMethod, appPath, (Map<String, Object>) null);
	}

	public Response<T> doRequest(RequestMethod requestMethod, String appPath, T... objects)
	{
		return doRequest(requestMethod, appPath, null, objects);
	}

	public Response<T> doRequest(RequestMethod requestMethod, String appPath, Map<String, Object> params)
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
	 * @param body
	 *            body for api call
	 * @param params
	 *            key-value of params for api call
	 * 
	 * @return response string
	 */

	private Response<T> doRequest(RequestMethod requestMethod, String appPath, Map<String, Object> params, T... body)
	{
		AcceptedMediaType mediaType = CosmConfig.getInstance().getResponseMediaType();

		String apiUri = appPath;
		if (RequestMethod.GET == requestMethod)
		{
			apiUri = apiUri.concat(".").concat(mediaType.name());
		}
		apiUri = concatParams(apiUri, params);

		ClientResponse response = null;

		try
		{
			WebResource service = getClient().resource(baseURI);
			WebResource.Builder b = service.path(apiUri).accept(mediaType.getMediaType())
					.header(HEADER_KEY_API, CosmConfig.getInstance().getApiKey()).header(HEADER_USER_AGENT, COSM_USER_AGENT);

			if (RequestMethod.DELETE == requestMethod)
			{
				response = b.delete(ClientResponse.class);
			} else if (RequestMethod.GET == requestMethod)
			{
				response = b.get(ClientResponse.class);
			} else if (RequestMethod.POST == requestMethod)
			{
				String json = ParserUtil.toJson(body);
				response = b.post(ClientResponse.class, json);
			} else if (RequestMethod.PUT == requestMethod)
			{
				String json = ParserUtil.toJson(body);
				response = b.put(ClientResponse.class, json);
			}
		} catch (UniformInterfaceException e)
		{
			throw new HttpException("Http request did not return successfully.", e.getResponse());
		}

		if (!isHttpStatusOK(response.getStatus()))
		{
			throw new HttpException("Http response status indicates unsuccessful operation.", response);
		}

		return toResponse(requestMethod, appPath, response, body);
	}

	private Response<T> toResponse(RequestMethod requestMethod, String appPath, ClientResponse response, T... body)
	{
		Response<T> retval = new Response<T>();
		retval.setStatusCode(response.getStatus());
		retval.setBody(response.getEntity(String.class));
		retval.setHeaders(response.getHeaders());
		return retval;
	}

	private Client getClient()
	{
		if (httpClient == null)
		{
			ClientConfig config = new DefaultClientConfig();
			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
			httpClient = Client.create(config);
			httpClient.addFilter(new LoggingFilter(System.out));
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

	private boolean isHttpStatusOK(int statusCode)
	{
		if (statusCode >= 300)
		{
			return false;
		}

		return true;
	}
}
