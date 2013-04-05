package com.cosm.client.requester;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.model.Feed;
import com.cosm.client.requester.RequestHandler.RequestMethod;
import com.cosm.client.requester.exceptions.HttpException;
import com.cosm.client.requester.exceptions.ParseToObjectException;
import com.cosm.client.requester.utils.StringUtil;

/**
 * Class for making request for feed from COSM API
 * 
 * {@link https ://cosm.com/docs/v2/feed/}
 * 
 * @author s0pau
 * 
 */
public class FeedRequester
{
	private static final String RESOURCES_PATH = "feeds";
	private final RequestHandler requestHandler = RequestHandler.make();

	/**
	 * @param toCreate
	 *            the feed to be created over the API
	 * @return the feed created, on successful operation
	 * @throws HttpException
	 *             if failed to create feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to feed
	 */
	public Feed create(Feed toCreate) throws HttpException
	{
		Response<Feed> response = requestHandler.doRequest(RequestMethod.POST, getResourcesPath(), toCreate);
		int feedId = Integer.valueOf(response.getIdFromResponse());
		return get(feedId);
	}

	// Unsupported:
	// public Collection<Feed> create(Feed... toCreate)

	/**
	 * @param feedId
	 *            the id of the feed to be retrieved
	 * @return a feed object parsed from the json returned from the API
	 * @throws HttpException
	 *             if failed to get feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to feed
	 */
	public Feed get(int feedId) throws HttpException, ParseToObjectException

	{
		Response<Feed> response = requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId));
		return response.getBodyAsObject(Feed.class);
	}

	/**
	 * 
	 * @param feedId
	 *            the id of the feed to be retrieved
	 * @param isShowUser
	 * @param dataStreamIds
	 * @return a collection of feed objects matching the params, parsed from the
	 *         json returned from the API
	 * @throws HttpException
	 *             if failed to get feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to feed
	 */
	public Collection<Feed> get(int feedId, String isShowUser, String... dataStreamIds) throws HttpException,
			ParseToObjectException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("datastreams", Arrays.asList(dataStreamIds));
		params.put("show_user", isShowUser);

		Response<Feed> response = requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId), params);
		return response.getBodyAsObjects(Feed.class);
	}

	// TODO can this location based params be also in filterParam?
	/**
	 * 
	 * @param feedId
	 * @param latitude
	 * @param longitude
	 * @param distance
	 * @param distanceUnits
	 * @return a collection of feed objects matching the params, parsed from the
	 *         json returned from the API
	 * @throws HttpException
	 *             if failed to get feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to feed
	 */
	public Collection<Feed> getByLocation(int feedId, String latitude, String longitude, Double distance, String distanceUnits)
			throws HttpException, ParseToObjectException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("lat", latitude);
		params.put("lon", longitude);
		params.put("distance", distance);
		params.put("distance_unit", distanceUnits);

		Response<Feed> response = requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId), params);
		return response.getBodyAsObjects(Feed.class);
	}

	/**
	 * @param filterParam
	 * @return a collection of feed objects matching the parameters given,
	 *         parsed from the json returned from the API
	 * @throws HttpException
	 *             if failed to get feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to feed
	 */
	public Collection<Feed> get(FeedFilterParam filterParam) throws HttpException, ParseToObjectException
	{
		Response<Feed> response = requestHandler.doRequest(RequestMethod.GET, getResourcesPath(), filterParam.getParamsMap());
		return response.getBodyAsObjects(Feed.class);
	}

	/**
	 * 
	 * @param feedId
	 * @param startAt
	 * @param endAt
	 * @param samplingInterval
	 * @return a feed with history on datastreams and datapoints that matches
	 *         the given parameters
	 * @throws HttpException
	 *             if failed to get feed over the API
	 * @throws ParseToObjectException
	 *             if failed to parse the returned json to feed
	 */
	public Feed getHistory(int feedId, String startAt, String endAt, int samplingInterval) throws HttpException,
			ParseToObjectException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", startAt);
		params.put("end", endAt);
		params.put("interval", samplingInterval);

		Response<Feed> response = requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId), params);

		return response.getBodyAsObject(Feed.class);
	}

	// TODO getMobileFeed(), "waypoints" etc

	/**
	 * @param toUpdate
	 *            the feed to be updated over the API
	 * @return the feed that was passed in, on successful operation
	 * @throws HttpException
	 *             if failed to get feed over the API
	 */
	public Feed update(Feed toUpdate) throws HttpException
	{
		Response<Feed> response = requestHandler.doRequest(RequestMethod.PUT, getResourcePath(toUpdate.getId()), toUpdate);
		return toUpdate;
	}

	public void delete(int feedId) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.DELETE, getResourcePath(feedId));
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
