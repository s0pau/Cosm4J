package com.cosm.client.http.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cosm.client.AppConfig;
import com.cosm.client.http.TestUtil;
import com.cosm.client.http.api.ApiKeyRequester;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.ApiKey;
import com.cosm.client.model.Permission;
import com.cosm.client.model.Permission.AccessMethod;
import com.cosm.client.model.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ApiKeyRequesterTest
{
	private static int FEED_ID = 123;

	private ApiKeyRequester requester;
	private ApiKey apiKey1;
	private ApiKey apiKey2;

	@Before
	public void setUp() throws Exception
	{
		TestUtil.loadDefaultTestConfig();

		// apiKey1 fixture
		apiKey1 = new ApiKey();
		apiKey1.setLabel("Simple Test Key");
		apiKey1.setPrivateAccess(true);

		List<AccessMethod> am1 = new ArrayList<>();
		am1.add(AccessMethod.get);
		Permission p1 = new Permission(null, am1, null);
		List<Permission> permissions1 = new ArrayList<>();
		permissions1.add(p1);
		apiKey1.setPermissions(permissions1);

		// apiKey2 fixture
		apiKey2 = new ApiKey();
		apiKey2.setLabel("Magic Test Key");
		apiKey2.setPrivateAccess(true);

		List<AccessMethod> am2 = new ArrayList<>();
		am2.add(AccessMethod.get);
		List<Resource> resources = new ArrayList<>();
		Resource r2 = new Resource(FEED_ID, null);
		resources.add(r2);
		Permission p2 = new Permission("66.66.66.66", am2, resources);

		List<AccessMethod> am3 = new ArrayList<>();
		am3.add(AccessMethod.put);
		am3.add(AccessMethod.post);
		am3.add(AccessMethod.delete);
		Permission p3 = new Permission(null, am3, null);

		List<Permission> permissions2 = new ArrayList<>();
		permissions2.add(p2);
		permissions2.add(p3);
		apiKey2.setPermissions(permissions2);

		requester = new ApiKeyRequesterImpl();
		apiKey1 = requester.create(apiKey1);
	}

	@After
	public void tearDown() throws Exception
	{
		// tear down only if it were ever persisted, i.e., id != null
		if (apiKey1.getApiKey() != null)
		{
			tearDownFixture(apiKey1.getApiKey());
		}
		if (apiKey2.getApiKey() != null)
		{
			tearDownFixture(apiKey2.getApiKey());
		}
		AppConfig.getInstance().reload();
		requester = null;
	}

	private void tearDownFixture(String fixtureId)
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
			ApiKey retval = requester.create(apiKey2);

			// update fields returned from creation before comparing all fields
			assertTrue(retval.getApiKey() != null);
			apiKey2.setApiKey(retval.getApiKey());

			assertTrue(apiKey2.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to create a apiKey");
		}
	}

	@Test
	public void testJSONAcceptHeaderAndConverstion()
	{
		try
		{
			ApiKey retval = requester.get(apiKey1.getApiKey());
			assertTrue(apiKey1.memberEquals(retval));
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
		try
		{
			ApiKey retval = requester.get(apiKey1.getApiKey());
			assertTrue(apiKey1.memberEquals(retval));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a apiKey");
		}
	}

	@Test
	public void testGetByFeedId()
	{
		try
		{
			Collection<ApiKey> retval = requester.getByFeedId(FEED_ID);
			assertTrue(retval.size() == 2);
			assertTrue(retval.contains(apiKey1));
			assertTrue(retval.contains(apiKey2));
		} catch (HttpException e)
		{
			fail("failed on requesting to get a apiKey");
		}
	}

	@Test
	public void testDelete()
	{
		try
		{
			requester.delete(apiKey1.getApiKey());
		} catch (HttpException e)
		{
			fail("failed on requesting to delete a apiKey");
		}

		try
		{
			requester.get(apiKey1.getApiKey());
			fail("should not have been able to retrieve deleted apiKey");
		} catch (HttpException e)
		{
			// pass
		}
	}
}
