package com.cosm.client.http.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.http.DefaultRequestHandler;
import com.cosm.client.http.DefaultRequestHandler.HttpMethod;
import com.cosm.client.http.Response;
import com.cosm.client.http.api.TriggerRequester;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Trigger;

/**
 * Class for making request for trigger from COSM API
 * 
 * {@link https ://cosm.com/docs/v2/trigger/}
 * 
 * @author s0pau
 * 
 */
public class TriggerRequesterImpl implements TriggerRequester
{
	private static final String RESOURCES_PATH = "triggers";

	@Override
	public Trigger create(Trigger toCreate) throws HttpException
	{
		Response<Trigger> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.POST, getResourcesPath(), toCreate);
		int triggerId = Integer.valueOf(response.getIdFromResponse());
		return get(triggerId);
	}

	@Override
	public Trigger get(int triggerId) throws HttpException, ParseToObjectException
	{
		Response<Trigger> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcePath(triggerId));
		return response.getBodyAsObject(Trigger.class);
	}

	@Override
	public Collection<Trigger> getByFeedId(int feedId) throws HttpException, ParseToObjectException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("feed_id", feedId);

		Response<Trigger> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcesPath(), params);
		return response.getBodyAsObjects(Trigger.class);
	}

	@Override
	public Trigger update(Trigger toUpdate) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.PUT, getResourcePath(toUpdate.getId()), toUpdate);
		return toUpdate;
	}

	@Override
	public void delete(int triggerId) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.DELETE, getResourcePath(triggerId));
	}

	/**
	 * @param triggerId
	 * @return the restful path to a specifc trigger resource, which can then be
	 *         appended to a base path for a complete uri
	 */
	private String getResourcePath(int triggerId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getResourcesPath()).append("/").append(String.valueOf(triggerId));
		return sb.toString();
	}

	/**
	 * @return the restful path to a all trigger resources, which can then be
	 *         appended to a base path for a complete uri
	 */
	private String getResourcesPath()
	{
		return RESOURCES_PATH;
	}
}