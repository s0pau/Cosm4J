package com.cosm.client.http.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.http.DefaultRequestHandler;
import com.cosm.client.http.DefaultRequestHandler.HttpMethod;
import com.cosm.client.http.Response;
import com.cosm.client.http.api.DatapointRequester;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Datapoint;

/**
 * Class for making requests for datapoint(s) from COSM API
 * 
 * {@link https ://cosm.com/docs/v2/datapoint/}
 * 
 * @author s0pau
 * 
 */
public class DatapointRequesterImpl implements DatapointRequester
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cosm.client.http.DatapointResource#create(int, java.lang.String,
	 * com.cosm.client.model.Datapoint)
	 */
	@Override
	public Datapoint create(int feedId, String dataStreamId, Datapoint toCreate) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.POST, getResourcesPath(feedId, dataStreamId), toCreate);
		return toCreate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cosm.client.http.DatapointResource#create(int, java.lang.String,
	 * com.cosm.client.model.Datapoint)
	 */
	@Override
	public Collection<Datapoint> create(int feedId, String dataStreamId, Datapoint... toCreate) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.POST, getResourcesPath(feedId, dataStreamId), toCreate);
		return Arrays.asList(toCreate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cosm.client.http.DatapointResource#get(int, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Datapoint get(int feedId, String dataStreamId, String datapointAt) throws HttpException, ParseToObjectException
	{
		Response<Datapoint> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET,
				getResourcePath(feedId, dataStreamId, datapointAt));
		return response.getBodyAsObject(Datapoint.class);
	}

	@Override
	public Datapoint update(int feedId, String dataStreamId, Datapoint toUpdate) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.PUT, getResourcePath(feedId, dataStreamId, toUpdate.getAt()), toUpdate);
		return toUpdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cosm.client.http.DatapointResource#delete(int, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void delete(int feedId, String dataStreamId, String datapointAt) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.DELETE, getResourcePath(feedId, dataStreamId, datapointAt));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cosm.client.http.DatapointResource#deleteMultiple(int,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteMultiple(int feedId, String dataStreamId, String startAt) throws HttpException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", startAt);

		DefaultRequestHandler.getInstance().doRequest(HttpMethod.DELETE, getResourcesPath(feedId, dataStreamId), params);
	}

	/**
	 * @param feedId
	 * @param dataStreamId
	 * @param datapointAt
	 *            of the datapoint
	 * @return the restful path to a specifc datapoint resource, which can then
	 *         be appended to a base path for a complete uri
	 */
	private String getResourcePath(int feedId, String dataStreamId, String datapointAt)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getResourcesPath(feedId, dataStreamId)).append("/").append(datapointAt);
		return sb.toString();
	}

	/**
	 * @param feedId
	 * @param dataStreamId
	 * @return the restful path to a all datapoints resource for the given feed
	 *         and datastream, which can then be appended to a base path for a
	 *         complete uri
	 */
	private String getResourcesPath(int feedId, String dataStreamId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("feeds").append("/").append(feedId);
		sb.append("/").append("datastreams").append("/").append(dataStreamId);
		sb.append("/").append("datapoints");
		return sb.toString();
	}

	/**
	 * @param feedId
	 * @param dataStreamId
	 * @return the restful path to a the datastream, which can then be appended
	 *         to a base path for a complete uri
	 */
	private String getParentResourcePath(int feedId, String dataStreamId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("feeds").append("/").append(feedId);
		sb.append("/").append("datastreams").append("/").append(dataStreamId);
		return sb.toString();
	}
}
