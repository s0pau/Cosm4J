package com.cosm.client.requester;

import java.util.Arrays;
import java.util.Collection;

import com.cosm.client.model.Datastream;
import com.cosm.client.requester.RequestHandler.RequestMethod;
import com.cosm.client.requester.exceptions.HttpException;
import com.cosm.client.requester.exceptions.ParseToObjectException;

/**
 * Class for making request for datastream from COSM API
 * 
 * {@link https ://cosm.com/docs/v2/datastream/}
 * 
 * 
 * @author s0pau
 * 
 */
public class DatastreamRequester
{
	private final RequestHandler requestHandler = RequestHandler.make();

	/**
	 * 
	 * @param feedId
	 *            parent of the datastream
	 * @param toCreate
	 *            datastream to be created datapoint to be created over the API
	 * @return the datastream that was passed in, on successful operation
	 * @throws HttpException
	 *             if failed to create datastream over the API
	 */
	public Datastream create(int feedId, Datastream toCreate) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.POST, getResourcesPath(feedId), toCreate);
		return toCreate;
	}

	public Collection<Datastream> create(int feedId, Datastream... toCreate) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.POST, getResourcesPath(feedId), toCreate);
		return Arrays.asList(toCreate);
	}

	/**
	 * @param feedId
	 *            parent of the datastream
	 * @param dataStreamId
	 *            the id of the datastream to be retrieved
	 * @return a datastream object parsed from the json returned from the API
	 * @throws HttpException
	 *             if failed to get datastream over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to datastream
	 */
	public Datastream get(int feedId, String dataStreamId) throws HttpException, ParseToObjectException
	{
		Response<Datastream> response = requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId, dataStreamId));
		return response.getBodyAsObject(Datastream.class);
	}

	/**
	 * 
	 * @param feedId
	 *            parent of the datastream
	 * @param toUpdate
	 *            datastream to be updated over the API
	 * @return the datastream that was passed in, on successful operation
	 * @throws HttpException
	 *             if failed to create datastream over the API
	 */
	public Datastream update(int feedId, Datastream toUpdate) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.PUT, getResourcePath(feedId, toUpdate.getId()), toUpdate);
		return toUpdate;
	}

	public void delete(int feedId, String dataStreamId) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.DELETE, getResourcePath(feedId, dataStreamId));
	}

	/**
	 * @param feedId
	 * @param dataStreamId
	 * @return the restful path to a specifc datastream resource, which can then
	 *         be appended to a base path for a complete uri
	 */
	private String getResourcePath(int feedId, String dataStreamId)
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
	private String getResourcesPath(int feedId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("feeds").append("/").append(String.valueOf(feedId)).append("/").append("datastreams");
		return sb.toString();
	}
}
