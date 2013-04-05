package com.cosm.client.http.impl;

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
import com.cosm.client.http.api.TriggerResource;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.impl.TriggerRequester;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.Trigger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class TriggerRequesterTest
{
	private TriggerResource requester;
	private ObjectMapper mapper;
	private Trigger trigger1;
	private Trigger trigger2;
	private String trigger1_JSON;
	private String trigger2_JSON;

	@Before
	public void setUp() throws Exception
	{
		TestUtil.loadDefaultTestConfig();
		mapper = TestUtil.getObjectMapper();

		String fixtureUri = "src/test/res";

		trigger1 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/trigger1.json")), Trigger.class);
		trigger2 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/trigger2.json")), Trigger.class);

		trigger1_JSON = TestUtil.getStringFromFile(fixtureUri + "/trigger1.json");
		trigger2_JSON = TestUtil.getStringFromFile(fixtureUri + "/trigger2.json");

		requester = new TriggerRequester();
		trigger1 = requester.create(trigger1);
	}

	@After
	public void tearDown() throws Exception
	{
		// tear down only if it were ever persisted, i.e., id != null
		if (trigger1.getId() != null)
		{
			tearDownFixture(trigger1.getId());
		}
		if (trigger2.getId() != null)
		{
			tearDownFixture(trigger2.getId());
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
			Trigger retval = requester.create(trigger2);
			
			// update fields that returned after creation before comparing every other field
			assertTrue(retval.getId() != null);
			trigger2.setId(retval.getId());
			assertTrue(retval.getLogin() != null);
			trigger2.setLogin(retval.getLogin());
			assertTrue(retval.getNotifiedAt() != null);
			trigger2.setNotifiedAt(retval.getNotifiedAt());
			
			assertTrue(trigger2.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to create a trigger");
		}
	}

	@Test
	public void testJSONAcceptHeaderAndConverstion()
	{
		try
		{
			Trigger retval = requester.get(trigger1.getId());
			assertTrue(trigger1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a trigger");
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
			Trigger retval = requester.get(trigger1.getId());
			assertTrue(trigger1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a trigger");
		}
	}

	@Test
	public void testGetByFeedId()
	{
		trigger2 = requester.create(trigger2);
		try
		{
			Collection<Trigger> retval = requester.getByFeedId(TestUtil.TEST_FEED_ID);
			assertTrue(retval.size() == 2);
			assertTrue(retval.contains(trigger1));
			assertTrue(retval.contains(trigger2));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a trigger");
		}
	}

	@Test
	public void testUpdate()
	{
		trigger1.setThresholdValue(6.66d);

		try
		{
			Trigger retval = requester.update(trigger1);
			assertTrue(trigger1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to update a trigger");
		}
	}

	@Test
	public void testDelete()
	{
		try
		{
			requester.delete(trigger1.getId());
		} catch (HttpException e)
		{
			fail("failed on requesting to delete a trigger");
		}
	}
}
