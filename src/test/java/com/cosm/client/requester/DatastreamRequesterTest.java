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
import com.cosm.client.CosmConfig.AcceptedMediaType;
import com.cosm.client.model.Datastream;
import com.cosm.client.requester.Response.HttpStatus;
import com.cosm.client.requester.exceptions.HttpException;
import com.cosm.client.requester.exceptions.ParseToObjectException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class DatastreamRequesterTest
{
	private static final int feedId = 109;

	private static final String datastreamId1 = "test_stream_1";
	private static final String datastreamId2 = "test_stream_2";
	private static final String datastreamId3 = "test_stream_3";
	private static final String datastreamId_bad = "stream - bogus";

	private DatastreamRequester requester;
	private ObjectMapper mapper;
	private Datastream datastream1;
	private Datastream datastream2;
	private String datastream1_JSON;
	private String datastream2_JSON;

	@Before
	public void setUp() throws Exception
	{
		TestUtil.loadDefaultTestConfig();
		mapper = new ObjectMapper();

		String fixtureUri = "src/test/res";
		datastream1 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/datastream1.json")), Datastream.class);
		datastream2 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/datastream2.json")), Datastream.class);

		datastream1_JSON = TestUtil.getStringFromFile(fixtureUri + "/datastream1.json");
		datastream2_JSON = TestUtil.getStringFromFile(fixtureUri + "/datastream2.json");

		requester = new DatastreamRequester();
		requester.create(feedId, datastream1);
	}

	@After
	public void tearDown() throws Exception
	{
		tearDownFixture(datastreamId1);
		tearDownFixture(datastreamId2);
		tearDownFixture(datastreamId3);
		CosmConfig.getInstance().reset();
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
			Datastream retval = requester.create(feedId, datastream2);
			assertEquals(retval, datastream2);
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
			CosmConfig.getInstance().setResponseMedia(AcceptedMediaType.json);
			Datastream retval = requester.get(feedId, datastreamId1);
			assertEquals(datastream1, retval);
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

		try
		{
			CosmConfig.getInstance().setResponseMedia(AcceptedMediaType.xml);
			Datastream retval = requester.get(feedId, datastreamId1);
			assertEquals(datastream1, retval);
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datastream");
		} catch (ParseToObjectException e)
		{
			fail("response is not a valid xml");
		}
	}

	@Ignore("unable to deserialise CSV atm")
	@Test
	public void testCSVAcceptHeaderAndConverstion()
	{
		CsvMapper mapper = new CsvMapper();

		try
		{
			CosmConfig.getInstance().setResponseMedia(AcceptedMediaType.csv);
			Datastream retval = requester.get(feedId, datastreamId1);
			// assertEqualToFixture(retObj);
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datastream");
		} catch (ParseToObjectException e)
		{
			fail("response is not a valid csv");
		}
	}

	@Test
	public void testGet()
	{
		try
		{
			Datastream retval = requester.get(feedId, datastreamId1);
			assertEquals(datastream1, retval);
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
			assertEquals(datastream1, retval);
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
	}
}
