package com.cosm.client.requester;

import com.cosm.client.model.Datastream;
import com.cosm.client.requester.RequestHandler.RequestMethod;

/**
 * Class for making request for datastream from COSM API
 * 
 * {@see https ://cosm.com/docs/v2/datastream/}
 * 
 * @author s0pau
 * 
 */
public class DatastreamRequester
{
	private final RequestHandler requestHandler = RequestHandler.make();

	public String create(String feedId, Datastream... toCreate) throws HttpException
	{
		return requestHandler.doRequest(RequestMethod.POST, getResourcesPath(feedId), toCreate);
	}

	public String get(String feedId, String dataStreamId) throws HttpException
	{
		return requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId, dataStreamId));
	}

	public void update(String feedId, Datastream toUpdate) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.PUT, getResourcePath(feedId, toUpdate.getId()), toUpdate);
	}

	public void delete(String feedId, String dataStreamId) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.DELETE, getResourcePath(feedId, dataStreamId));
	}

	/**
	 * @param feedId
	 * @param dataStreamId
	 * @return the restful path to a specifc datastream resource, which can then
	 *         be appended to a base path for a complete uri
	 */
	private String getResourcePath(String feedId, String dataStreamId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getResourcesPath(feedId)).append("/").append(dataStreamId);
		return sb.toString();
	}

	/**
	 * @param feedId
	 * @return the restful path to a all datastream resource for the given feed
	 *         and datastream, which can then be appended to a base path for a
	 *         complete uri
	 */
	private String getResourcesPath(String feedId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("feeds").append("/").append(feedId).append("/").append("datastreams");
		return sb.toString();
	}
}
