package com.cosm.client.http.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import com.cosm.client.http.TestUtil;
import com.cosm.client.model.Datapoint;
import com.cosm.client.model.Datastream;
import com.cosm.client.model.Feed;
import com.cosm.client.model.Feed.Status;
import com.cosm.client.model.Location;
import com.cosm.client.model.Location.Domain;

public class ParserUtilTest
{
	@Test
	public void testToConnectedObject()
	{
		String datapoint2 = TestUtil.getStringFromFile("datapoint2.json");
		Datapoint retval = ParserUtil.toConnectedObject(datapoint2, Datapoint.class);
		assertEquals("2013-02-02T00:00:00.000000Z", retval.getAt());
		assertEquals("222", retval.getValue());
	}

	@Test
	public void testListFeedsToConnectedObjects()
	{
		String feeds = TestUtil.getStringFromFile("listFeeds.json");
		Collection<Feed> retval = ParserUtil.toConnectedObjects(feeds, Feed.class);
		try
		{
			assertEquals(2, retval.size());

			Object[] fList = retval.toArray();
			Feed feed1 = (Feed) fList[0];
			Feed feed2 = (Feed) fList[1];

			// feed1
			assertEquals(new URI("http://api.testsite.com/v2/feeds/11111.json"), feed1.getFeedUri());
			assertEquals("Test Feed - ONE", feed1.getTitle());
			assertEquals(Status.live, feed1.getStatus());
			assertEquals(new URI("https://testsite.com/users/JohnDoe"), feed1.getCreatorUri());
			assertEquals("2010-06-08T09:30:21.472927Z", feed1.getUpdatedAt());
			assertEquals("2010-05-03T23:43:01.238734Z", feed1.getCreatedAt());
			
			Location location = new Location();
			location.setDomain(Domain.physical);
			assertEquals(location, feed1.getLocation());

			assertEquals(2, feed1.getDatastreams().size());
			Object[] dsList = feed1.getDatastreams().toArray();
			Datastream ds1 = new Datastream();
			ds1.setId("test_feed-stream10");
			ds1.setMaxValue("10000.0");
			ds1.setTags(Arrays.asList("humidity"));
			ds1.setValue("111");
			ds1.setMinValue("-10.0");
			ds1.setUpdatedAt("2010-07-02T10:21:57.101496Z");
			assertEquals(ds1, dsList[0]);
			
			Datastream ds2 = new Datastream();
			ds2.setId("test_feed-stream11");
			ds2.setMaxValue("10000.0");
			ds2.setTags(Arrays.asList("humidity"));
			ds2.setValue("222");
			ds2.setMinValue("-10.0");
			ds2.setUpdatedAt("2010-07-02T10:21:57.176209Z");
			assertEquals(ds2, dsList[1]);

			// feed2
			assertEquals(new URI("http://api.testsite.com/v2/feeds/22.json"), feed2.getFeedUri());
			assertEquals("Test Feed - Title 2", feed2.getTitle());
			assertEquals(2, feed2.getDatastreams().size());
			dsList = feed2.getDatastreams().toArray();
			ds1 = (Datastream) dsList[0];
			ds2 = (Datastream) dsList[1];
			assertEquals("test_feed-stream20", ds1.getId());
			assertEquals("test_feed-stream21", ds2.getId());
		} catch (Exception e)
		{
			fail("Error while comparing response");
		}
	}
}
