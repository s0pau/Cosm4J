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
public class TriggerRequesterImpl extends AbstractRequester<Integer, Trigger> implements TriggerRequester
{
	private static final String RESOURCES_PATH = "triggers";

	@Override
	public Collection<Trigger> getByFeedId(int feedId) throws HttpException, ParseToObjectException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("feed_id", feedId);

		Response<Trigger> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcesPath(), params);
		return response.getBodyAsObjects(Trigger.class);
	}

	/**
	 * @param triggerId
	 * @return the restful path to a specifc trigger resource, which can then be
	 *         appended to a base path for a complete uri
	 */
	@Override
	protected String getResourcePath(Integer triggerId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getResourcesPath()).append("/").append(String.valueOf(triggerId));
		return sb.toString();
	}

	/**
	 * @return the restful path to a all trigger resources, which can then be
	 *         appended to a base path for a complete uri
	 */
	@Override
	protected String getResourcesPath()
	{
		return RESOURCES_PATH;
	}

	@Override
	protected Class getObjectClass()
	{
		return Trigger.class;
	}

	@Override
	protected Integer getCreatedId(Response<Trigger> response)
	{
		return Integer.valueOf(response.getIdFromResponse());
	}
}
