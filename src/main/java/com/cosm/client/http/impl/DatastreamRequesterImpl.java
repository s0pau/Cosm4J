package com.cosm.client.http.impl;

import java.util.Arrays;
import java.util.Collection;

import com.cosm.client.http.DefaultRequestHandler;
import com.cosm.client.http.DefaultRequestHandler.HttpMethod;
import com.cosm.client.http.Response;
import com.cosm.client.http.api.DatastreamRequester;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Datastream;

/**
 * Class for making request for datastream from COSM API
 * 
 * {@link https ://cosm.com/docs/v2/datastream/}
 * 
 * 
 * @author s0pau
 * 
 */
public class DatastreamRequesterImpl implements DatastreamRequester
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cosm.client.http.DatastreamResource#create(int,
	 * com.cosm.client.model.Datastream)
	 */
	@Override
	public Datastream create(int feedId, Datastream toCreate) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.POST, getResourcesPath(feedId), toCreate);
		return toCreate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cosm.client.http.DatastreamResource#create(int,
	 * com.cosm.client.model.Datastream)
	 */
	@Override
	public Collection<Datastream> create(int feedId, Datastream... toCreate) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.POST, getResourcesPath(feedId), toCreate);
		return Arrays.asList(toCreate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cosm.client.http.DatastreamResource#get(int, java.lang.String)
	 */
	@Override
	public Datastream get(int feedId, String dataStreamId) throws HttpException, ParseToObjectException
	{
		Response<Datastream> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcePath(feedId, dataStreamId));
		return response.getBodyAsObject(Datastream.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cosm.client.http.DatastreamResource#update(int,
	 * com.cosm.client.model.Datastream)
	 */
	@Override
	public Datastream update(int feedId, Datastream toUpdate) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.PUT, getResourcePath(feedId, toUpdate.getId()), toUpdate);
		return toUpdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cosm.client.http.DatastreamResource#delete(int,
	 * java.lang.String)
	 */
	@Override
	public void delete(int feedId, String dataStreamId) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.DELETE, getResourcePath(feedId, dataStreamId));
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
