package com.cosm.client.requester;

import java.util.HashMap;
import java.util.Map;

import com.cosm.client.model.Feed;

/**
 * Feed's allowable filter parameters' key-value
 * 
 * @author s0pau
 */
class FeedFilterParam
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