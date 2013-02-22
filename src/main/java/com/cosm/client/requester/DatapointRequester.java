package com.cosm.client.requester;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpException;

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
	private final RequestHandler requestHandler = RequestHandler.make();

	public String create(String feedId, String dataStreamId, Datapoint... toCreate) throws HttpException
	{
		return requestHandler.doRequest(RequestMethod.POST, getResourcesPath(feedId, dataStreamId), toCreate);
	}

	public String get(String feedId, String dataStreamId, String datapointAt) throws HttpException
	{
		return requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId, dataStreamId, datapointAt));
	}

	public String get(String feedId, String dataStreamId, String startAt, String endAt, int samplingInterval)
			throws HttpException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", startAt);
		params.put("end", endAt);
		params.put("interval", samplingInterval);

		return requestHandler.doRequest(RequestMethod.GET, getParentResourcePath(feedId, dataStreamId), params);
	}

	public void update(String feedId, String dataStreamId, Datapoint toUpdate) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.PUT, getResourcePath(feedId, dataStreamId, toUpdate.getAt()), toUpdate);
	}

	public void delete(String feedId, String dataStreamId, String datapointAt) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.DELETE, getResourcePath(feedId, dataStreamId, datapointAt));
	}

	public void deleteMultiple(String feedId, String dataStreamId, String startAt) throws HttpException
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
	private String getResourcePath(String feedId, String dataStreamId, String datapointAt)
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
	private String getResourcesPath(String feedId, String dataStreamId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("/").append("feeds").append("/").append(feedId);
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
	private String getParentResourcePath(String feedId, String dataStreamId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("/").append("feeds").append("/").append(feedId);
		sb.append("/").append("datastreams").append("/").append(dataStreamId);
		return sb.toString();
	}

}
