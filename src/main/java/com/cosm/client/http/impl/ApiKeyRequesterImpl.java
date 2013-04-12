package com.cosm.client.http.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.http.DefaultRequestHandler;
import com.cosm.client.http.DefaultRequestHandler.HttpMethod;
import com.cosm.client.http.Response;
import com.cosm.client.http.api.ApiKeyRequester;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.ApiKey;

/**
 * Class for making request for apikey from COSM API
 * 
 * {@link https ://cosm.com/docs/v2/keys/}
 * 
 * 
 * @author s0pau
 * 
 */
public class ApiKeyRequesterImpl implements ApiKeyRequester
{
	private static final String RESOURCES_PATH = "keys";

	@Override
	public ApiKey create(ApiKey toCreate) throws HttpException
	{
		Response<ApiKey> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.POST, getResourcesPath(), toCreate);
		String id = response.getIdFromResponse();
		return get(id);
	}

	@Override
	public ApiKey get(String apiKey) throws HttpException, ParseToObjectException
	{
		Response<ApiKey> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcePath(apiKey));
		return response.getBodyAsObject(ApiKey.class);
	}

	@Override
	public Collection<ApiKey> getByFeedId(int feedId) throws HttpException, ParseToObjectException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("feed_id", feedId);

		Response<ApiKey> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcesPath(), params);
		return response.getBodyAsObjects(ApiKey.class);
	}

	@Override
	public void delete(String apiKey) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.DELETE, getResourcePath(apiKey));
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
