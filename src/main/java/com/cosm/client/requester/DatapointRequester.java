package com.cosm.client.requester;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.model.Datapoint;
import com.cosm.client.requester.RequestHandler.RequestMethod;

/**
 * Class for making requests for datapoint(s) from COSM API
 * 
 * @see https://cosm.com/docs/v2/datapoint/
 * 
 * @author s0pau
 * 
 */
public class DatapointRequester
{
	private final RequestHandler<Datapoint> requestHandler = RequestHandler.make();

	public Datapoint create(int feedId, String dataStreamId, Datapoint toCreate) throws HttpException
	{
		create(feedId, dataStreamId, new Datapoint[] { toCreate });
		return toCreate;
	}

	public Collection<Datapoint> create(int feedId, String dataStreamId, Datapoint... toCreate) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.POST, getResourcesPath(feedId, dataStreamId), toCreate);
		return Arrays.asList(toCreate);
	}

	public Datapoint get(int feedId, String dataStreamId, String datapointAt) throws HttpException
	{
		Response<Datapoint> response = requestHandler.doRequest(RequestMethod.GET,
				getResourcePath(feedId, dataStreamId, datapointAt));
		return response.getBodyAsObject(Datapoint.class);
	}

	public Collection<Datapoint> get(int feedId, String dataStreamId, String startAt, String endAt, int samplingInterval)
			throws HttpException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", startAt);
		params.put("end", endAt);
		params.put("interval", samplingInterval);

		Response<Datapoint> response = requestHandler.doRequest(RequestMethod.GET, getParentResourcePath(feedId, dataStreamId),
				params);
		return response.getBodyAsObjects(Datapoint.class);
	}

	public Datapoint update(int feedId, String dataStreamId, Datapoint toUpdate) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.PUT, getResourcePath(feedId, dataStreamId, toUpdate.getAt()), toUpdate);
		return toUpdate;
	}

	public void delete(int feedId, String dataStreamId, String datapointAt) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.DELETE, getResourcePath(feedId, dataStreamId, datapointAt));
	}

	public void deleteMultiple(int feedId, String dataStreamId, String startAt) throws HttpException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", startAt);

		requestHandler.doRequest(RequestMethod.DELETE, getResourcesPath(feedId, dataStreamId), params);
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
