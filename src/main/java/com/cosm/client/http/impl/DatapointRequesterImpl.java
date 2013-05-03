package com.cosm.client.http.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.http.DefaultRequestHandler;
import com.cosm.client.http.DefaultRequestHandler.HttpMethod;
import com.cosm.client.http.api.DatapointRequester;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.model.Datapoint;

/**
 * Class for making requests for datapoint(s) from COSM API
 * 
 * {@link https ://cosm.com/docs/v2/datapoint/}
 * 
 * @author s0pau
 * 
 */
public class DatapointRequesterImpl extends AbstractRequester<String, Datapoint> implements DatapointRequester
{
	private int feedId;
	private String dataStreamId;

	public DatapointRequesterImpl(int feedId, String dataStreamId)
	{
		this.feedId = feedId;
		this.dataStreamId = dataStreamId;
	}

	@Override
	public Collection<Datapoint> create(Datapoint... toCreate) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.POST, getResourcesPath(), toCreate);
		return Arrays.asList(toCreate);
	}

	@Override
	public void deleteMultiple(String startAt) throws HttpException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", startAt);

		DefaultRequestHandler.getInstance().doRequest(HttpMethod.DELETE, getResourcesPath(), params);
	}

	@Override
	protected String getResourcePath(String datapointAt)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getResourcesPath()).append("/").append(datapointAt);
		return sb.toString();
	}

	@Override
	protected String getResourcesPath()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("feeds").append("/").append(feedId);
		sb.append("/").append("datastreams").append("/").append(dataStreamId);
		sb.append("/").append("datapoints");
		return sb.toString();
	}

	@Override
	protected Class getObjectClass()
	{
		return Datapoint.class;
	}
}
