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
import com.cosm.client.http.api.DatastreamRequester;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Datastream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class DatastreamRequesterTest
{
	private static final int feedId = TestUtil.TEST_FEED_ID;

	private static final String datastreamId1 = "test_stream_1";
	private static final String datastreamId2 = "test_stream_2";
	private static final String datastreamId3 = "test_stream_3";
	private static final String datastreamId_bad = "stream - bogus";

	private DatastreamRequester requester;
	private ObjectMapper mapper;
	private Datastream datastream1;
	private Datastream datastream2;

	@Before
	public void setUp() throws Exception
	{
		TestUtil.loadDefaultTestConfig();
		mapper = TestUtil.getObjectMapper();

		String fixtureUri = "src/test/res";
		datastream1 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/datastream1.json")), Datastream.class);
		datastream2 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/datastream2.json")), Datastream.class);

		requester = new DatastreamRequesterImpl();
		datastream1 = requester.create(feedId, datastream1);
	}

	@After
	public void tearDown() throws Exception
	{
		tearDownFixture(datastreamId1);
		tearDownFixture(datastreamId2);
		tearDownFixture(datastreamId3);
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
			assertTrue(datastream2.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to create a datastream");
		}
	}

	@Test
	public void testCreateMultiple()
	{
		Datastream datastream3 = new Datastream();
		datastream3.setId(datastreamId3);
		datastream3.setValue("333");

		try
		{
			Collection<Datastream> retval = requester.create(feedId, datastream2, datastream3);
			assertEquals(2, retval.size());
			assertTrue(retval.contains(datastream2));
			assertTrue(retval.contains(datastream3));
		} catch (HttpException e)
		{
			fail("failed on requesting to create multiple datastreams");
		}
	}

	@Test
	public void testJSONAcceptHeaderAndConverstion()
	{
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
	public void testUpdate()
	{
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
