package com.cosm.client.requester;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.model.Datastream;
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

	public String create(Feed... toCreate) throws HttpException
	{
		return requestHandler.doRequest(RequestMethod.POST, getResourcesPath(), toCreate);
	}

	public String get(String feedId) throws HttpException
	{
		return requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId));
	}

	public String get(String feedId, String isShowUser, String... dataStreamIds) throws HttpException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("datastreams", Arrays.asList(dataStreamIds));
		params.put("show_user", isShowUser);

		return requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId), params);
	}

	// TODO can this location based params be also in filterParam?
	public String getByLocation(String feedId, String latitude, String longitude, Double distance, String distanceUnits)
			throws HttpException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("lat", latitude);
		params.put("lon", longitude);
		params.put("distance", distance);
		params.put("distance_unit", distanceUnits);

		return requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId), params);
	}

	public String get(String feedId, FilterParam filterParam) throws HttpException
	{
		return requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId), filterParam.getParamsMap());
	}

	// TODO what is version?
	// TODO there is a "waypoints" element for mobile feed? What for?
	public String get(String feedId, String startAt, String endAt, int samplingInterval) throws HttpException
	{
		Map<String, Object> params = new HashMap<>();
		params.put("start", startAt);
		params.put("end", endAt);
		params.put("interval", samplingInterval);

		return requestHandler.doRequest(RequestMethod.GET, getResourcePath(feedId), params);
	}

	public void update(Datastream toUpdate) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.PUT, getResourcePath(toUpdate.getId()), toUpdate);
	}

	public void delete(String feedId) throws HttpException
	{
		requestHandler.doRequest(RequestMethod.DELETE, getResourcePath(feedId));
	}

	/**
	 * @param feedId
	 * @return the restful path to a specifc datastream resource, which can then
	 *         be appended to a base path for a complete uri
	 */
	private String getResourcePath(String feedId)
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
