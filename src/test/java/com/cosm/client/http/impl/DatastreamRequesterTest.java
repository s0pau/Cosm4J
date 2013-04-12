package com.cosm.client.http.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cosm.client.CosmConfig;
import com.cosm.client.http.TestUtil;
import com.cosm.client.http.api.DatapointRequester;
import com.cosm.client.http.api.DatastreamRequester;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Datapoint;
import com.cosm.client.model.Datastream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class DatastreamRequesterTest
{
	private static final int feedId = TestUtil.TEST_FEED_ID;
	private static final String datastreamId1 = "test_stream_1";
	private static final String datastreamId2 = "test_stream_2";

	private DatastreamRequester requester;
	private Datastream datastream1;
	private Datastream datastream2;
	private ObjectMapper mapper;

	@Before
	public void setUp() throws Exception
	{
		TestUtil.loadDefaultTestConfig();
		mapper = TestUtil.getObjectMapper();

		datastream1 = mapper.readValue(new FileInputStream(new File(TestUtil.fixtureUri + "datastream1.json")), Datastream.class);
		datastream2 = mapper.readValue(new FileInputStream(new File(TestUtil.fixtureUri + "datastream2.json")), Datastream.class);

		requester = new DatastreamRequesterImpl();
	}

	@After
	public void tearDown() throws Exception
	{
		tearDownFixture(datastreamId1);
		tearDownFixture(datastreamId2);
		CosmConfig.getInstance().reload();
		requester = null;
	}

	private void tearDownFixture(String fixtureId)
	{
		try
		{
			requester.delete(feedId, fixtureId);
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
			Datastream retval = requester.create(feedId, datastream2);

			// set fields updated by create so we can compare all other fields
			assertTrue(retval.getUpdatedAt() != null);
			datastream2.setUpdatedAt(retval.getUpdatedAt());

			assertTrue(datastream2.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to create a datastream");
		}
	}

	@Test
	public void testCreateMultiple()
	{
		try
		{
			Collection<Datastream> retval = requester.create(feedId, datastream1, datastream2);
			assertEquals(2, retval.size());
			assertTrue(retval.contains(datastream1));
			assertTrue(retval.contains(datastream2));
		} catch (HttpException e)
		{
			fail("failed on requesting to create multiple datastreams");
		}
	}

	@Test
	public void testJSONAcceptHeaderAndConverstion()
	{
		datastream1 = requester.create(feedId, datastream1);

		try
		{
			Datastream retval = requester.get(feedId, datastreamId1);
			assertTrue(datastream1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datastream");
		} catch (ParseToObjectException e)
		{
			fail("response is not a valid json");
		}
	}

	@Ignore("unable to deserialise EEML atm")
	@Test
	public void testXMLAcceptHeaderAndConverstion()
	{
		ObjectMapper mapper = new XmlMapper();
	}

	@Ignore("unable to deserialise CSV atm")
	@Test
	public void testCSVAcceptHeaderAndConverstion()
	{
		CsvMapper mapper = new CsvMapper();
	}

	@Test
	public void testGet()
	{
		datastream1 = requester.create(feedId, datastream1);

		try
		{
			Datastream retval = requester.get(feedId, datastreamId1);
			assertTrue(datastream1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datastream");
		}
	}

	@Test
	public void testGetHistoryWithDatapoints()
	{
		Datapoint datapoint1 = null;
		Datapoint datapoint2 = null;
		try
		{
			datapoint1 = mapper
					.readValue(new FileInputStream(new File(TestUtil.fixtureUri + "datapoint1.json")), Datapoint.class);
			datapoint2 = mapper
					.readValue(new FileInputStream(new File(TestUtil.fixtureUri + "datapoint1.json")), Datapoint.class);

			datastream1 = requester.create(feedId, datastream1);
			DatapointRequester dpRequester = new DatapointRequesterImpl();
			dpRequester.create(feedId, datastream1.getId(), datapoint1);
			dpRequester.create(feedId, datastream1.getId(), datapoint2);
		} catch (Exception e)
		{
			fail(String.format("fail to set up test, %s", e.getLocalizedMessage()));
		}

		try
		{
			Datastream retval = requester.getHistoryWithDatapoints(feedId, datastream1.getId(), "2012-02-02T00:00:00.000000Z",
					"2013-02-03T00:00:00.000000Z", 86400);
			assertEquals(1, retval.getDatapoints().size());

			assertEquals(datastream1.getId(), retval.getId());

			assertEquals(1, retval.getDatapoints().size());
			Datapoint dp = (Datapoint) retval.getDatapoints().toArray()[0];
			assertEquals(datapoint1.getAt(), dp.getAt());
			assertEquals(datapoint1.getValue(), dp.getValue());
		} catch (HttpException e)
		{
			fail("failed on requesting to get datapoints with parameters");
		}
	}

	@Test
	public void testUpdate()
	{
		datastream1 = requester.create(feedId, datastream1);
		datastream1.setValue("666");

		try
		{
			Datastream retval = requester.update(feedId, datastream1);
			assertTrue(datastream1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to update a datastream");
		}
	}

	@Test
	public void testDelete()
	{
		datastream1 = requester.create(feedId, datastream1);

		try
		{
			requester.delete(feedId, datastreamId1);
		} catch (HttpException e)
		{
			fail("failed on requesting to delete a datastream");
		}

		try
		{
			requester.get(feedId, datastreamId1);
			fail("should not be able to get deleted datasteram");
		} catch (HttpException e)
		{
			// pass
		}
	}
}
