package com.cosm.client.http.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cosm.client.AppConfig;
import com.cosm.client.http.TestUtil;
import com.cosm.client.http.api.DatapointRequester;
import com.cosm.client.http.api.DatastreamRequester;
import com.cosm.client.http.api.FeedRequester;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Datapoint;
import com.cosm.client.model.Datastream;
import com.cosm.client.model.Feed;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DatapointRequesterTest
{
	private static int feedId;
	private static String datastreamId;

	private DatapointRequester requester;
	private Datapoint datapoint1;
	private Datapoint datapoint2;

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		// setup a general purpose feed set up for testing all it's children
		Feed feed = TestUtil.getObjectMapper().readValue(new FileInputStream(new File(TestUtil.fixtureUri + "feed1.json")),
				Feed.class);
		FeedRequester fRequester = new FeedRequesterImpl();
		feed = fRequester.create(feed);

		Datastream datastream = TestUtil.getObjectMapper().readValue(
				new FileInputStream(new File(TestUtil.fixtureUri + "datastream1.json")), Datastream.class);
		DatastreamRequester dsRequester = new DatastreamRequesterImpl();
		dsRequester.create(feed.getId(), datastream);

		feedId = feed.getId();
		datastreamId = datastream.getId();
	}

	@AfterClass
	public static void tearDownClass()
	{
		FeedRequester fRequester = new FeedRequesterImpl();
		fRequester.delete(feedId);

		// DatastreamRequester dsRequester = new DatastreamRequesterImpl();
		// dsRequester.delete(feedId, datastreamId);
	}

	@Before
	public void setUp() throws Exception
	{
		TestUtil.loadDefaultTestConfig();
		ObjectMapper mapper = TestUtil.getObjectMapper();

		datapoint1 = mapper.readValue(new FileInputStream(new File(TestUtil.fixtureUri + "datapoint1.json")), Datapoint.class);
		datapoint2 = mapper.readValue(new FileInputStream(new File(TestUtil.fixtureUri + "datapoint2.json")), Datapoint.class);

		requester = new DatapointRequesterImpl();
	}

	@After
	public void tearDown() throws Exception
	{
		tearDownFixture(datapoint1.getAt());
		tearDownFixture(datapoint2.getAt());
		AppConfig.getInstance().reload();
		requester = null;
	}

	private void tearDownFixture(String fixtureId)
	{
		try
		{
			requester.delete(feedId, datastreamId, fixtureId);
		} catch (HttpException e)
		{
			// NOT_FOUND is ok as the test ran could have not created/deleted it
			if (HttpStatus.SC_NOT_FOUND != e.getStatusCode())
			{
				throw e;
			}
		}
	}

	@Test
	public void testCreate()
	{
		try
		{
			Datapoint retval = requester.create(feedId, datastreamId, datapoint2);
			assertTrue(datapoint2.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to create a datapoint");
		}
	}

	@Test
	public void testCreateMultiple()
	{
		try
		{
			Collection<Datapoint> retval = requester.create(feedId, datastreamId, datapoint1, datapoint2);
			assertEquals(2, retval.size());
			assertTrue(retval.contains(datapoint1));
			assertTrue(retval.contains(datapoint2));
		} catch (HttpException e)
		{
			fail("failed on requesting to create multiple datapoints");
		}
	}

	@Test
	public void testJSONAcceptHeaderAndConverstion()
	{
		requester.create(feedId, datastreamId, datapoint1);

		try
		{
			Datapoint retval = requester.get(feedId, datastreamId, datapoint1.getAt());
			assertTrue(datapoint1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datapoint");
		} catch (ParseToObjectException e)
		{
			fail("response is not a valid json");
		}
	}

	@Test
	public void testGet()
	{
		requester.create(feedId, datastreamId, datapoint1);

		try
		{
			Datapoint retval = requester.get(feedId, datastreamId, datapoint1.getAt());
			assertTrue(datapoint1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datapoint");
		}
	}

	@Test
	public void testUpdate()
	{
		requester.create(feedId, datastreamId, datapoint1);
		datapoint1.setValue("555");

		try
		{
			requester.update(feedId, datastreamId, datapoint1);
			Datapoint retval = requester.update(feedId, datastreamId, datapoint1);
			assertTrue(datapoint1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to update a datapoint");
		}
	}

	@Test
	public void testDelete()
	{
		requester.create(feedId, datastreamId, datapoint1);

		try
		{
			requester.delete(feedId, datastreamId, datapoint1.getAt());
		} catch (HttpException e)
		{
			fail("failed on requesting to delete a datapoint");
		}

		try
		{
			requester.get(feedId, datastreamId, datapoint1.getAt());
			fail("should not be able to get deleted datapoint");
		} catch (HttpException e)
		{
			// pass
		}
	}
}
