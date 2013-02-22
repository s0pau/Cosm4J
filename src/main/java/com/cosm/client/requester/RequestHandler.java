package com.cosm.client.requester;

import java.util.Map;
import java.util.Map.Entry;

import com.cosm.client.CosmConfig;
import com.cosm.client.CosmConfig.AcceptedMediaType;
import com.cosm.client.model.CosmObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
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
public class RequestHandler
{
	private static final String HEADER_KEY_API = "X-ApiKey";
	private String baseURI;

	private Client httpClient;
	private ObjectMapper objectMapper;

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

	public <T extends CosmObject> String doRequest(RequestMethod requestMethod, String appPath)
	{
		return doRequest(requestMethod, appPath, (Map<String, Object>) null);
	}

	public <T extends CosmObject> String doRequest(RequestMethod requestMethod, String appPath, T... objects)
	{
		return doRequest(requestMethod, appPath, null, objects);
	}

	public <T extends CosmObject> String doRequest(RequestMethod requestMethod, String appPath, Map<String, Object> params)
	{
		return doRequest(requestMethod, appPath, params, null);
	}

	/**
	 * * Make the request to Cosm API and return the response string
	 * 
	 * @param <T>
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
	private <T extends CosmObject> String doRequest(RequestMethod requestMethod, String appPath, Map<String, Object> params,
			T... body)
	{

		// TODO user agent
		AcceptedMediaType mediaType = CosmConfig.getInstance().getResponseMedia();

		String apiCall = appPath;
		if (RequestMethod.GET == requestMethod)
		{
			apiCall = apiCall.concat(".").concat(mediaType.name());
		}

		apiCall = concatParams(apiCall, params);

		WebResource service = getClient().resource(baseURI);
		WebResource.Builder b = service.path(apiCall).accept(mediaType.getMediaType())
				.header(HEADER_KEY_API, CosmConfig.getInstance().getApiKey());

		String response = null;
		if (RequestMethod.DELETE == requestMethod)
		{
			response = b.delete(String.class);
		} else if (RequestMethod.GET == requestMethod)
		{
			response = b.get(String.class);
		} else if (RequestMethod.POST == requestMethod)
		{
			String json = ParserUtil.toJson(getObjectMapper(), false, body);
			b.post(json);
		} else if (RequestMethod.PUT == requestMethod)
		{
			String json = ParserUtil.toJson(getObjectMapper(), true, body);
			b.put(json);
		}

		return response;
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

	private ObjectMapper getObjectMapper()
	{
		if (objectMapper == null)
		{
			ObjectMapper retval = new ObjectMapper();

			JacksonXmlModule module = new JacksonXmlModule();
			module.setDefaultUseWrapper(false);

			AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
			AnnotationIntrospector secondary = new JaxbAnnotationIntrospector();
			AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);
			retval.getDeserializationConfig().setAnnotationIntrospector(pair);
			retval.getSerializationConfig().setAnnotationIntrospector(pair);

			// retval.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
			// retval.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			objectMapper = retval;
		}
		return objectMapper;
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
}
