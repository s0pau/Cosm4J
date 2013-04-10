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
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Datapoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class DatapointRequesterTest
{
	private static final int feedId = TestUtil.TEST_FEED_ID;
	private static final String datastreamId = "stream_id2";
	private static final String datapointId1 = "2013-01-01T00:00:00.000000Z";
	private static final String datapointId2 = "2013-02-02T00:00:00.000000Z";

	private DatapointRequester requester;
	private Datapoint datapoint1;
	private Datapoint datapoint2;

	@Before
	public void setUp() throws Exception
	{
		TestUtil.loadDefaultTestConfig();
		ObjectMapper mapper = TestUtil.getObjectMapper();

		datapoint1 = mapper.readValue(new FileInputStream(new File(TestUtil.fixtureUri + "datapoint1.json")), Datapoint.class);
		datapoint2 = mapper.readValue(new FileInputStream(new File(TestUtil.fixtureUri + "datapoint2.json")), Datapoint.class);

		requester = new DatapointRequesterImpl();
		requester.create(feedId, datastreamId, datapoint1);
	}

	@After
	public void tearDown() throws Exception
	{
		tearDownFixture(datapointId1);
		tearDownFixture(datapointId2);
		CosmConfig.getInstance().reload();
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
		// covered in setup... perhaps not needed here
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
		try
		{
			Datapoint retval = requester.get(feedId, datastreamId, datapointId1);
			assertTrue(datapoint1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datapoint");
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
		try
		{
			Datapoint retval = requester.get(feedId, datastreamId, datapointId1);
			assertTrue(datapoint1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datapoint");
		}
	}

	@Test
	public void testGetWithParams()
	{
		try
		{
			Collection<Datapoint> retval = requester.get(feedId, datastreamId, "2012-04-01T00:00:00.000000Z",
					"2013-01-02T00:00:00.000000Z", 86400);

			assertEquals(1, retval.size());
			assertTrue(retval.contains(datapoint1));
			assertTrue(!retval.contains(datapoint2));
		} catch (HttpException e)
		{
			fail("failed on requesting to get datapoints with parameters");
		}
	}

	@Test
	public void testUpdate()
	{
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
		try
		{
			requester.delete(feedId, datastreamId, datapointId1);
		} catch (HttpException e)
		{
			fail("failed on requesting to delete a datapoint");
		}

		try
		{
			requester.get(feedId, datastreamId, datapointId1);
			fail("should not be able to get deleted datapoint");
		} catch (HttpException e)
		{
			// pass
		}
	}
}
