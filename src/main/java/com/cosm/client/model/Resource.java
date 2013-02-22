package com.cosm.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Resource - is the {@link Feed} and/or {@link Datastream} that a
 * {@link Permission} maps to.
 * 
 * @author s0pau
 * 
 */
public class Resource
{
	@JsonProperty("feed_id")
	private String feedId;

	@JsonProperty("datastream_id")
	private String datastreamId;

	private transient Feed feed;
	private transient Datastream datastream;

	public String getFeedId()
	{
		return feedId;
	}

	public void setFeedId(String feedId)
	{
		this.feedId = feedId;
	}

	public String getDatastreamId()
	{
		return datastreamId;
	}

	public void setDatastreamId(String datastreamId)
	{
		this.datastreamId = datastreamId;
	}

}
