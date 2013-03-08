package com.cosm.client.requester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cosm.client.CosmConfig;
import com.cosm.client.CosmConfig.AcceptedMediaType;
import com.cosm.client.model.Feed;
import com.cosm.client.requester.Response.HttpStatus;
import com.cosm.client.requester.exceptions.HttpException;
import com.cosm.client.requester.exceptions.ParseToObjectException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class FeedRequesterTest
{
	private FeedRequester requester;
	private ObjectMapper mapper;
	private Feed feed1;
	private Feed feed2;
	private Feed feedTooMinimal;
	private String feed1_JSON;
	private String feed2_JSON;

	@Before
	public void setUp() throws Exception
	{
		TestUtil.loadDefaultTestConfig();
		mapper = TestUtil.getObjectMapper();

		String fixtureUri = "src/test/res";

		feed1 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/feed1.json")), Feed.class);
		feed2 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/feed2.json")), Feed.class);
		feedTooMinimal = mapper.readValue(new FileInputStream(new File(fixtureUri + "/feed_title_only.json")), Feed.class);

		feed1_JSON = TestUtil.getStringFromFile(fixtureUri + "/feed1.json");
		feed2_JSON = TestUtil.getStringFromFile(fixtureUri + "/feed2.json");

		requester = new FeedRequester();
		feed1 = requester.create(feed1);
	}

	@After
	public void tearDown() throws Exception
	{
		tearDownFixture(feed1.getId());
		tearDownFixture(feed2.getId());
		tearDownFixture(feedTooMinimal.getId());
		CosmConfig.getInstance().reset();
		requester = null;
	}

	private void tearDownFixture(int fixtureId)
	{
		try
		{
			requester.delete(fixtureId);
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
			Feed retval = requester.create(feed2);
			assertEquals(retval, feed2);
		} catch (HttpException e)
		{
			fail("failed on requesting to create a feed");
		}
	}

	@Test
	public void testJSONAcceptHeaderAndConverstion()
	{
		try
		{
			CosmConfig.getInstance().setResponseMedia(AcceptedMediaType.json);
			Feed retval = requester.get(feed1.getId());
			assertEquals(feed1, retval);
		} catch (HttpException e)
		{
			fail("failed on requesting to get a feed");
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
			Feed retval = requester.get(feed1.getId());
			assertEquals(feed1, retval);
		} catch (HttpException e)
		{
			fail("failed on requesting to get a feed");
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
			Feed retval = requester.get(feed1.getId());
			// assertEqualToFixture(retObj);
		} catch (HttpException e)
		{
			fail("failed on requesting to get a feed");
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
			Feed retval = requester.get(feed1.getId());
			assertEquals(feed1, retval);
		} catch (HttpException e)
		{
			fail("failed on requesting to get a feed");
		}
	}

	@Test
	public void testUpdate()
	{
		feed1.setDescription("unit test feed description");

		try
		{
			requester.update(feed1);
			Feed retval = requester.update(feed1);
			assertEquals(feed1, retval);
		} catch (HttpException e)
		{
			fail("failed on requesting to update a feed");
		}
	}

	@Test
	public void testDelete()
	{
		try
		{
			requester.delete(feed1.getId());
		} catch (HttpException e)
		{
			fail("failed on requesting to delete a feed");
		}
	}
}
