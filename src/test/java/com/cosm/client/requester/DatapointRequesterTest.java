package com.cosm.client.requester;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.cosm.client.CosmConfig;
import com.cosm.client.CosmConfig.AcceptedMediaType;
import com.cosm.client.model.Datapoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class DatapointRequesterTest
{
	private static final String datapointId = "2013-02-03T00:00:00.000000Z";
	private static final String feedId = "97684";
	private static final String datastreamId = "Light_Level";
	private static final String datapointId2 = "2013-01-01T00:00:00.000000Z";

	private DatapointRequester fr;
	private Datapoint datapoint1;
	private ObjectMapper mapper;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() throws Exception
	{
		CosmConfig.getInstance().setApiKey("QYz6Tgyg4f2kmo1S9ffFJV8XoKiSAKx1RW40UkNyS1R1UT0g");
		fr = new DatapointRequester();
		mapper = new ObjectMapper();
		// datapoint1 =
		// Fixjure.of(Datapoint.class).from(YamlSource.newYamlResource("datapoint1.yaml")).create();
	}

	@After
	public void tearDown() throws Exception
	{
		CosmConfig.getInstance().reset();
		fr = null;
	}

	@Test
	public void testCreate()
	{
		DatapointRequester fr = new DatapointRequester();
		Datapoint toCreate = new Datapoint();
		toCreate.setAt(datapointId);
		toCreate.setValue("121");

		try
		{
			String s = fr.create(feedId, datastreamId, toCreate);
		} catch (HttpException e)
		{
			fail("failed on requesting to create a datapoint");
		}
	}

	@Test
	public void testCreateMultiple()
	{
		DatapointRequester fr = new DatapointRequester();
		Datapoint toCreate1 = new Datapoint();
		toCreate1.setAt(datapointId2);
		toCreate1.setValue("111");

		Datapoint toCreate2 = new Datapoint();
		toCreate2.setAt("2013-02-02T00:00:00.000000Z");
		toCreate2.setValue("222");
		try
		{
			String s = fr.create(feedId, datastreamId, toCreate1, toCreate2);
		} catch (HttpException e)
		{
			fail("failed on requesting to create multiple datapoints");
		}
	}

	@Test
	public void testJSONAcceptHeader()
	{
		try
		{
			CosmConfig.getInstance().setResponseMedia(AcceptedMediaType.json);
			String retval = fr.get(feedId, datastreamId, datapointId);
			Datapoint retObj = mapper.readValue(retval, Datapoint.class);
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datapoint");
		} catch (IOException e)
		{
			fail("response is not a valid json");
		}
	}

	@Test
	public void testXMLAcceptHeader()
	{
		ObjectMapper mapper = new XmlMapper();

		try
		{
			CosmConfig.getInstance().setResponseMedia(AcceptedMediaType.xml);
			String retval = fr.get(feedId, datastreamId, datapointId);
			Datapoint retObj = mapper.readValue(retval, Datapoint.class);
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datapoint");
		} catch (IOException e)
		{
			fail("response is not a valid xml");
		}
	}

	@Test
	public void testCSVAcceptHeader()
	{
		CsvMapper mapper = new CsvMapper();

		try
		{
			CosmConfig.getInstance().setResponseMedia(AcceptedMediaType.csv);
			String retval = fr.get(feedId, datastreamId, datapointId);
			Datapoint retObj = mapper.readValue(retval, Datapoint.class);
			// assert that retval is csv
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datapoint");
		} catch (IOException e)
		{
			fail("response is not a valid csv");
		}
	}

	@Test
	public void testGet()
	{
		try
		{
			String retval = fr.get(feedId, datastreamId, datapointId);
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datapoint");
		}
	}

	@Test
	public void testGetWithParams()
	{
		DatapointRequester fr = new DatapointRequester();
		try
		{
			String s = fr.get(feedId, datastreamId, "2012-04-01T00:00:00.000000Z", "2013-04-01T00:00:00.000000Z", 86400);
		} catch (HttpException e)
		{
			fail("failed on requesting to get datapoints with parameters");
		}
	}

	@Test
	public void testUpdate()
	{
		DatapointRequester fr = new DatapointRequester();
		Datapoint toUpdate = new Datapoint();
		toUpdate.setAt(datapointId);
		toUpdate.setValue("555");

		try
		{
			fr.update(feedId, datastreamId, toUpdate);
		} catch (HttpException e)
		{
			fail("failed on requesting to update a datapoint");
		}
	}

	@Test
	public void testDelete()
	{
		DatapointRequester fr = new DatapointRequester();
		try
		{
			fr.delete(feedId, datastreamId, datapointId2);
		} catch (HttpException e)
		{
			fail("failed on requesting to delete a datapoint");
		}
	}
}
