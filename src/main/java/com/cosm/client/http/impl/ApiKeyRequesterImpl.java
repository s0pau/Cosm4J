package com.cosm.client.http.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.http.DefaultRequestHandler;
import com.cosm.client.http.DefaultRequestHandler.HttpMethod;
import com.cosm.client.http.Response;
import com.cosm.client.http.api.ApiKeyRequester;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.exception.RequestInvalidException;
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
public class ApiKeyRequesterImpl extends AbstractRequester<String, ApiKey> implements ApiKeyRequester
{
	private static final String RESOURCES_PATH = "keys";

	@Override
	public ApiKey update(ApiKey toUpdate) throws HttpException
	{
		throw new RequestInvalidException("This operation is currently unsupported.");
	}

	@Override
	public Collection<ApiKey> getByFeedId(int feedId) throws HttpException, ParseToObjectException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("feed_id", feedId);

		Response<ApiKey> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcesPath(), params);
		return response.getBodyAsObjects(ApiKey.class);
	}

	protected String getResourcePath(String apiKey)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(RESOURCES_PATH).append("/").append(apiKey);
		return sb.toString();
	}

	protected String getResourcesPath()
	{
		return RESOURCES_PATH;
	}

	protected Class getObjectClass()
	{
		return ApiKey.class;
	}

	@Override
	protected String getCreatedId(Response<ApiKey> response)
	{
		return response.getIdFromResponse();
	}
}
