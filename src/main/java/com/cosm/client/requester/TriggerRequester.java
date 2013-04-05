package com.cosm.client.requester;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.model.Trigger;
import com.cosm.client.requester.RequestHandler.RequestMethod;
import com.cosm.client.requester.exceptions.HttpException;
import com.cosm.client.requester.exceptions.ParseToObjectException;

/**
 * Class for making request for trigger from COSM API
 * 
 * {@link https ://cosm.com/docs/v2/trigger/}
 * 
 * @author s0pau
 * 
 */
public class TriggerRequester
{
	private static final String RESOURCES_PATH = "triggers";
	private final RequestHandler requestHandler = RequestHandler.make();

	/**
	 * @param toCreate
	 *            the trigger to be created over the API
	 * @return the trigger that was passed in with the id field updated, on
	 *         successful operation
	 * @throws HttpException
	 *             if failed to create feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to trigger
	 */
	public Trigger create(Trigger toCreate) throws HttpException
	{
		Response<Trigger> response = requestHandler.doRequest(RequestMethod.POST, getResourcesPath(), toCreate);
		int triggerId = Integer.valueOf(response.getIdFromResponse());
		return get(triggerId);
	}

	/**
	 * @param triggerId
	 *            the id of the trigger to be retrieved
	 * @return a feed object parsed from the json returned from the API
	 * @throws HttpException
	 *             if failed to get trigger over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to trigger
	 */
	public Trigger get(int triggerId) throws HttpException, ParseToObjectException
	{
		Response<Trigger> response = requestHandler.doRequest(RequestMethod.GET, getResourcePath(triggerId));
		return response.getBodyAsObject(Trigger.class);
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
	public Collection<Trigger> getByFeedId(int feedId) throws HttpException, ParseToObjectException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("feed_id", feedId);

		Response<Trigger> response = requestHandler.doRequest(RequestMethod.GET, getResourcesPath(), params);
		return response.getBodyAsObjects(Trigger.class);
	}

	/**
	 * @param toUpdate
	 *            the trigger to be updated over the API
	 * @return the trigger that was passed in, on successful operation
	 * @throws HttpException
	 *             if failed to get trigger over the API
	 */
	public Trigger update(Trigger toUpdate) throws HttpException
	{
		Response<Trigger> response = requestHandler.doRequest(RequestMethod.PUT, getResourcePath(toUpdate.getId()), toUpdate);
		return toUpdate;
	}

	/**
	 * @param triggerId
	 *            the id of the trigger to be deleted over the API
	 * @throws HttpException
	 *             if failed to delete the trigger over the API
	 */
	public void delete(int triggerId) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.DELETE, getResourcePath(triggerId));
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
