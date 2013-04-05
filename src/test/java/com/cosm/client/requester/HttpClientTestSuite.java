package com.cosm.client.requester;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DatapointRequesterTest.class, DatastreamRequesterTest.class, FeedRequesterTest.class, TriggerRequesterTest.class,
		ApiKeyRequesterTest.class })
public class HttpClientTestSuite
{
}
