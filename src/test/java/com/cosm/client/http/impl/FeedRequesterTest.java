package com.cosm.client.http.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cosm.client.CosmConfig;
import com.cosm.client.http.TestUtil;
import com.cosm.client.http.api.FeedRequester;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Feed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class FeedRequesterTest
{
	private FeedRequester requester;
	private ObjectMapper mapper;
	private Feed feed1;
	private Feed feed2;

	@Before
	public void setUp() throws Exception
	{
		TestUtil.loadDefaultTestConfig();
		mapper = TestUtil.getObjectMapper();

		String fixtureUri = "src/test/res";

		feed1 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/feed1.json")), Feed.class);
		feed2 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/feed2.json")), Feed.class);

		requester = new FeedRequesterImpl();
		feed1 = requester.create(feed1);
	}

	@After
	public void tearDown() throws Exception
	{
		// if id is null, it was never persisted
		if (feed1.getId() != null)
		{
			tearDownFixture(feed1.getId());
		}

		if (feed2.getId() != null)
		{
			tearDownFixture(feed2.getId());
		}
		CosmConfig.getInstance().reload();
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
			Feed retval = requester.create(feed2);

			// update fields returned from creation before comparing all fields
			feed2.setId(retval.getId());

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
			Feed retval = requester.get(feed1.getId());
			assertTrue(feed1.memberEquals(retval));
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
			Feed retval = requester.get(feed1.getId());
			feed1.setUpdatedAt(retval.getUpdatedAt());
			assertTrue(feed1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a feed");
		}
	}

	@Test
	@Ignore
	public void testGetByDatastreams()
	{
		try
		{
			Collection<Feed> retval = requester.get(true, "test_feed-stream10");
			assertTrue(retval.size() == 1);
			assertTrue(retval.contains(feed1));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a feed");
		}

	}

	@Test
	@Ignore
	public void testGetByLocation()
	{
		try
		{
			Collection<Feed> retval = requester.getByLocation(feed2.getLocation().getLatitiude(), feed2.getLocation()
					.getLongitute(), null, null);
			assertTrue(retval.size() == 1);
			assertTrue(retval.contains(feed2));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a feed");
		}
	}

	@Test
	@Ignore
	public void testGetByParams()
	{
		try
		{
			Map<String, Object> params = null; // TODO
			Collection<Feed> retval = requester.get(params);
			assertTrue(retval.size() == 1);
			assertTrue(retval.contains(feed2));
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
			assertTrue(feed1.memberEquals(retval));
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

		try
		{
			requester.get(feed1.getId());
			fail("should not be able to get deleted feed");
		} catch (HttpException e)
		{
			// pass
		}
	}
}
