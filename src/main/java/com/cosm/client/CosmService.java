package com.cosm.client;

import com.cosm.client.http.api.ApiKeyRequester;
import com.cosm.client.http.api.DatapointRequester;
import com.cosm.client.http.api.DatastreamRequester;
import com.cosm.client.http.api.FeedRequester;
import com.cosm.client.http.api.TriggerRequester;
import com.cosm.client.http.impl.ApiKeyRequesterImpl;
import com.cosm.client.http.impl.DatapointRequesterImpl;
import com.cosm.client.http.impl.DatastreamRequesterImpl;
import com.cosm.client.http.impl.FeedRequesterImpl;
import com.cosm.client.http.impl.TriggerRequesterImpl;

/**
 * Cosm Service Locator <br/>
 * <br/>
 * For example: to create a feed: CosmService.instance().feed().create(<Feed
 * object>);
 * 
 * @author s0pau
 */
public class CosmService
{
	private static CosmService instance;

	private CosmService()
	{
	}

	public static CosmService instance()
	{
		if (instance == null)
		{
			instance = new CosmService();
		}
		return instance;
	}

	public FeedRequester feed()
	{
		return new FeedRequesterImpl();
	}

	public DatastreamRequester datastream(Integer feedId)
	{
		return new DatastreamRequesterImpl(feedId);
	}

	public DatapointRequester datapoint(Integer feedId, String datastreamId)
	{
		return new DatapointRequesterImpl(feedId, datastreamId);
	}

	public ApiKeyRequester apiKey()
	{
		return new ApiKeyRequesterImpl();

	}

	public TriggerRequester trigger()
	{
		return new TriggerRequesterImpl();
	}
}
