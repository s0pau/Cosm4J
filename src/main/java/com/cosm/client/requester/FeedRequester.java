package com.cosm.client.requester;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.model.Feed;
import com.cosm.client.requester.RequestHandler.RequestMethod;

/**
 * Class for making request for datapoint from COSM API
 * 
 * {@see https ://cosm.com/docs/v2/feed/}
 * 
 * @author s0pau
 * 
 */
public class FeedRequester
{
	private final RequestHandler requestHandler = RequestHandler.make();

	public Collection<Feed> create(Feed... toCreate) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.POST, getResourcesPath(), toCreate);
		return Arrays.asList(toCreate);
	}

	public Feed get(int feedId) throws HttpException
	{
		Response<Feed> response = requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId));
		return response.getBodyAsObject(Feed.class);
	}

	public Collection<Feed> get(int feedId, String isShowUser, String... dataStreamIds) throws HttpException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("datastreams", Arrays.asList(dataStreamIds));
		params.put("show_user", isShowUser);

		Response<Feed> response = requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId), params);
		return response.getBodyAsObjects(Feed.class);
	}

	// TODO can this location based params be also in filterParam?
	public Collection<Feed> getByLocation(int feedId, String latitude, String longitude, Double distance, String distanceUnits)
			throws HttpException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("lat", latitude);
		params.put("lon", longitude);
		params.put("distance", distance);
		params.put("distance_unit", distanceUnits);

		Response<Feed> response = requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId), params);
		return response.getBodyAsObjects(Feed.class);
	}

	public Collection<Feed> get(int feedId, FilterParam filterParam) throws HttpException
	{
		Response<Feed> response = requestHandler
				.doRequest(RequestMethod.GET, getResourcePath(feedId), filterParam.getParamsMap());
		return response.getBodyAsObjects(Feed.class);
	}

	public Collection<Feed> get(int feedId, String startAt, String endAt, int samplingInterval) throws HttpException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", startAt);
		params.put("end", endAt);
		params.put("interval", samplingInterval);

		Response<Feed> response = requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId), params);

		return response.getBodyAsObjects(Feed.class);
	}

	// TODO getMobileFeed(), "waypoints" etc

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
	 * @return the restful path to a specifc datastream resource, which can then
	 *         be appended to a base path for a complete uri
	 */
	private String getResourcePath(int feedId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getResourcesPath()).append("/").append(feedId);
		return sb.toString();
	}

	/**
	 * @return the restful path to a all datastream resource for the given feed
	 *         and datastream, which can then be appended to a base path for a
	 *         complete uri
	 */
	private String getResourcesPath()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("feeds");
		return sb.toString();
	}

	static final class FilterParam
	{
		enum OrderBy
		{
			CreatedAt("created_at"), RetrievedAt("reterived_at"), Relevance("relevance");

			private String queryValue;

			private OrderBy(String queryValue)
			{
				this.queryValue = queryValue;
			}

			public String getQueryValue()
			{
				return queryValue;
			}
		}

		private Integer onPage;

		private Integer pageSize;

		private Boolean summaryOnly;

		private String textSearch;

		private String tag;

		private String creator;

		private String unitLabel;
		Feed.Status status;
		OrderBy orderBy;
		Boolean showUser;

		Map<String, Object> getParamsMap()
		{
			Map<String, Object> params = new HashMap<>();

			params.put("page", onPage);
			params.put("per_page", pageSize);
			params.put("content", summaryOnly == null ? null : (summaryOnly ? "summary" : "full"));
			params.put("q", textSearch);
			params.put("tag", tag);
			params.put("user", creator);
			params.put("units", unitLabel);
			params.put("status", status);
			params.put("order", orderBy == null ? null : orderBy.getQueryValue());
			params.put("show_user", showUser);

			return params;
		}
	}
}
