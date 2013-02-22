package com.cosm.client.requester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cosm.client.CosmConfig;
import com.cosm.client.CosmConfig.AcceptedMediaType;
import com.cosm.client.model.Datapoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class DatapointRequesterTest
{
	private static final String feedId = "97684";
	private static final String datastreamId = "Light_Level";
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
		// Set up with default config, i.e. JSON as main MIME type for most test
		// unless otherwise specified.
		CosmConfig.getInstance().setApiKey("QYz6Tgyg4f2kmo1S9ffFJV8XoKiSAKx1RW40UkNyS1R1UT0g");
		requester = new DatapointRequester();
		mapper = new ObjectMapper();

		String fixtureUri = "src/test/res";
		datapoint1 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/datapoint1.json")), Datapoint.class);
		datapoint2 = mapper.readValue(new FileInputStream(new File(fixtureUri + "/datapoint2.json")), Datapoint.class);

		datapoint1_JSON = getStringFromFile(fixtureUri + "/datapoint1.json");
		datapoint2_JSON = getStringFromFile(fixtureUri + "/datapoint2.json");

		DatapointRequester fr = new DatapointRequester();
		fr.create(feedId, datastreamId, datapoint1);
	}

	private String getStringFromFile(String filePath)
	{
		try
		{
			FileInputStream fileStream = null;
			try
			{
				fileStream = new FileInputStream(new File(filePath));
				FileChannel fileChannel = fileStream.getChannel();
				MappedByteBuffer bb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
				return Charset.defaultCharset().decode(bb).toString();
			} finally
			{
				fileStream.close();
			}
		} catch (IOException io)
		{
			throw new RuntimeException(io);
		}
	}

	@After
	public void tearDown() throws Exception
	{
		tearDownFixture(datapointId1);
		tearDownFixture(datapointId2);
		CosmConfig.getInstance().reset();
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
			String retval = requester.create(feedId, datastreamId, datapoint2);
			assertEquals(retval, "http://api.cosm.com/v2/feeds/" + feedId + "/datastreams/" + datastreamId + "/datapoints/" + datapointId2);
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
			String retval = requester.create(feedId, datastreamId, datapoint1, datapoint2);
			assertTrue(retval.contains("http://api.cosm.com/v2/feeds/" + feedId + "/datastreams/" + datastreamId + "/datapoints/" + datapointId1));
			assertTrue(retval.contains("http://api.cosm.com/v2/feeds/" + feedId + "/datastreams/" + datastreamId + "/datapoints/" + datapointId2));
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
			CosmConfig.getInstance().setResponseMedia(AcceptedMediaType.json);
			String retval = requester.get(feedId, datastreamId, datapointId1);
			Datapoint retObj = mapper.readValue(retval, Datapoint.class);
			assertEqualObjects(retObj, datapoint1);
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datapoint");
		} catch (IOException e)
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
			String retval = requester.get(feedId, datastreamId, datapointId1);
			Datapoint retObj = mapper.readValue(retval, Datapoint.class);
			// assertEqualToFixture(retObj);
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datapoint");
		} catch (IOException e)
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
			String retval = requester.get(feedId, datastreamId, datapointId1);
			Datapoint retObj = mapper.readValue(retval, Datapoint.class);
			// assertEqualToFixture(retObj);
		} catch (HttpException e)
		{
			fail("failed on requesting to get a datapoint");
		} catch (IOException e)
		{
			fail("response is not a valid csv");
		}
	}

	private void assertEqualObjects(Datapoint retObj, Datapoint fixtureObj)
	{
		assertEquals(fixtureObj.getAt(), retObj.getAt());
		assertEquals(fixtureObj.getValue(), retObj.getValue());
	}

	@Test
	public void testGet()
	{
		try
		{
			String retval = requester.get(feedId, datastreamId, datapointId1);
			// assertEquals(datapoint1_JSON, retval);
			assertTrue(retval.contains("\"at\":\"" + datapointId1 + "\""));
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
			String retval = requester.get(feedId, datastreamId, "2012-04-01T00:00:00.000000Z", "2013-01-02T00:00:00.000000Z",
					86400);
			assertTrue(retval.contains("\"at\":\"" + datapointId1 + "\""));
			assertTrue(!retval.contains("\"at\":\"" + datapointId2 + "\""));
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
			String retval = requester.update(feedId, datastreamId, datapoint1);
			assertEquals(retval, "1");
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
			String retval = requester.delete(feedId, datastreamId, datapointId1);
			assertEquals(retval, "1");
		} catch (HttpException e)
		{
			fail("failed on requesting to delete a datapoint");
		}
	}
}
