package com.cosm.client.http;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.cosm.client.http.impl.ApiKeyRequesterTest;
import com.cosm.client.http.impl.DatapointRequesterTest;
import com.cosm.client.http.impl.DatastreamRequesterTest;
import com.cosm.client.http.impl.FeedRequesterTest;
import com.cosm.client.http.impl.TriggerRequesterTest;

@RunWith(Suite.class)
@SuiteClasses({ DatapointRequesterTest.class, DatastreamRequesterTest.class, FeedRequesterTest.class, TriggerRequesterTest.class,
		ApiKeyRequesterTest.class })
public class HttpClientTestSuite
{
}
