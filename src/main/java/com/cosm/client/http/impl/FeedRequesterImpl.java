package com.cosm.client.http.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.http.DefaultRequestHandler;
import com.cosm.client.http.DefaultRequestHandler.HttpMethod;
import com.cosm.client.http.Response;
import com.cosm.client.http.api.FeedRequester;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Feed;

/**
 * Class for making request for feed from COSM API
 * 
 * {@link https ://cosm.com/docs/v2/feed/}
 * 
 * @author s0pau
 * 
 */
public class FeedRequesterImpl implements FeedRequester
{
	private static final String RESOURCES_PATH = "feeds";

	@Override
	public Feed create(Feed toCreate) throws HttpException
	{
		Response<Feed> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.POST, getResourcesPath(), toCreate);
		int feedId = Integer.valueOf(response.getIdFromResponse());
		return get(feedId);
	}

	@Override
	public Feed get(int feedId) throws HttpException, ParseToObjectException

	{
		Response<Feed> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcePath(feedId));
		return response.getBodyAsObject(Feed.class);
	}

	@Override
	public Feed getHistoryWithDatastreams(Boolean isShowUser, String... dataStreamIds) throws HttpException, ParseToObjectException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("datastreams", Arrays.asList(dataStreamIds));
		params.put("show_user", isShowUser == null ? false : isShowUser);

		Response<Feed> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcesPath(), params);
		return response.getBodyAsObject(Feed.class);
	}

	@Override
	public Collection<Feed> getByLocation(Double latitude, Double longitude, Double distance, String distanceUnits)
			throws HttpException, ParseToObjectException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("lat", latitude);
		params.put("lon", longitude);
		params.put("distance", distance);
		params.put("distance_unit", distanceUnits);

		Response<Feed> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcesPath(), params);
		return response.getBodyAsObjects(Feed.class);
	}

	@Override
	public Collection<Feed> get(Map<String, Object> filterParam) throws HttpException, ParseToObjectException
	{
		Response<Feed> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcesPath(), filterParam);
		return response.getBodyAsObjects(Feed.class);
	}

	@Override
	public Feed getHistory(int feedId, String startAt, String endAt, int samplingInterval) throws HttpException,
			ParseToObjectException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", startAt);
		params.put("end", endAt);
		params.put("interval", samplingInterval);

		Response<Feed> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcePath(feedId), params);

		return response.getBodyAsObject(Feed.class);
	}

	@Override
	public Feed update(Feed toUpdate) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.PUT, getResourcePath(toUpdate.getId()), toUpdate);
		return toUpdate;
	}

	@Override
	public void delete(int feedId) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.DELETE, getResourcePath(feedId));
	}

	/**
	 * @param feedId
	 * @return the restful path to a specifc feed resource, which can then be
	 *         appended to a base path for a complete uri
	 */
	private String getResourcePath(int feedId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getResourcesPath()).append("/").append(String.valueOf(feedId));
		return sb.toString();
	}

	/**
	 * @return the restful path to a all feeds, which can then be appended to a
	 *         base path for a complete uri
	 */
	private String getResourcesPath()
	{
		return RESOURCES_PATH;
	}
}
