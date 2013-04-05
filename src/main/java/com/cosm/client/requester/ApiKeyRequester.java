package com.cosm.client.requester;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.model.ApiKey;
import com.cosm.client.requester.RequestHandler.RequestMethod;
import com.cosm.client.requester.exceptions.HttpException;
import com.cosm.client.requester.exceptions.ParseToObjectException;

/**
 * Class for making request for apikey from COSM API
 * 
 * {@link https ://cosm.com/docs/v2/keys/}
 * 
 * 
 * @author s0pau
 * 
 */
public class ApiKeyRequester
{
	private static final String RESOURCES_PATH = "keys";
	private final RequestHandler requestHandler = RequestHandler.make();

	/**
	 * @param toCreate
	 *            apikey to be created over the API
	 * @return the apikey that was passed in, on successful operation
	 * @throws HttpException
	 *             if failed to create apikey over the API
	 */
	public ApiKey create(ApiKey toCreate) throws HttpException
	{
		Response<ApiKey> response = requestHandler.doRequest(RequestMethod.POST, getResourcesPath(), toCreate);
		String id = response.getIdFromResponse();
		return get(id);
	}

	/**
	 * @param apiKey
	 *            the id of the apiKey to be retrieved
	 * @return a apiKey object parsed from the json returned from the API
	 * @throws HttpException
	 *             if failed to get apiKey over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to apiKey
	 */
	public ApiKey get(String apiKey) throws HttpException, ParseToObjectException
	{
		Response<ApiKey> response = requestHandler.doRequest(RequestMethod.GET, getResourcePath(apiKey));
		return response.getBodyAsObject(ApiKey.class);
	}

	/**
	 * 
	 * @param triggerId
	 *            the id of the trigger to be retrieved
	 * @param feedId
	 * @return a collection of trigger objects matching the params, parsed from
	 *         the json returned from the API
	 * @throws HttpException
	 *             if failed to get trigger over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to trigger
	 */
	public Collection<ApiKey> getByFeedId(int feedId) throws HttpException, ParseToObjectException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("feed_id", feedId);

		Response<ApiKey> response = requestHandler.doRequest(RequestMethod.GET, getResourcesPath(), params);
		return response.getBodyAsObjects(ApiKey.class);
	}

	/**
	 * @param apiKey
	 *            the id of the apikey to be deleted over the API
	 * @throws HttpException
	 *             if failed to delete the apikey over the API
	 */
	public void delete(String apiKey) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.DELETE, getResourcePath(apiKey));
	}

	/**
	 * @param apiKey
	 * @return the restful path to a specifc ApiKey resource, which can then be
	 *         appended to a base path for a complete uri
	 */
	private String getResourcePath(String apiKey)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(RESOURCES_PATH).append("/").append(apiKey);
		return sb.toString();
	}

	/**
	 * @return the restful path to a all apikey resources, which can then be
	 *         appended to a base path for a complete uri
	 */
	private String getResourcesPath()
	{
		return RESOURCES_PATH;
	}
}
