package com.cosm.client.requester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cosm.client.CosmConfig;
import com.cosm.client.model.Datapoint;
import com.cosm.client.requester.Response.HttpStatus;
import com.cosm.client.requester.exceptions.HttpException;
import com.cosm.client.requester.exceptions.ParseToObjectException;
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
	private ObjectMapper mapper;
	private Datapoint datapoint1;
	private Datapoint datapoint2;
	private String datapoint1_JSON;
	private String datapoint2_JSON;

	@Before
	public void setUp() throws Exception
	{
		TestUtil.loadDefaultTestConfig();
		mapper = TestUtil.getObjectMapper();

		String fixtureUri = "src/test/res";
		datapoint1 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/datapoint1.json")), Datapoint.class);
		datapoint2 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/datapoint2.json")), Datapoint.class);

		datapoint1_JSON = TestUtil.getStringFromFile(fixtureUri + "/datapoint1.json");
		datapoint2_JSON = TestUtil.getStringFromFile(fixtureUri + "/datapoint2.json");

		requester = new DatapointRequester();
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
			// requester.delete(feedId, datastreamId, fixtureId);
		} catch (HttpException e)
		{
			// NOT_FOUND is ok as the test ran could have not created/deleted it
			if (HttpStatus.NOT_FOUND.getCode() != e.getStatusCode())
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
			assertEquals(retval, datapoint2);
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
			assertEquals(datapoint1, retval);
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
			assertEquals(datapoint1, retval);
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
			assertEquals(datapoint1, retval);
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
	}
}
