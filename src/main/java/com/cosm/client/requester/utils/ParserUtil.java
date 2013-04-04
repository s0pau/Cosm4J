package com.cosm.client.requester.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.internal.matchers.IsCollectionContaining;

import com.cosm.client.model.ApiKey;
import com.cosm.client.model.ConnectedObject;
import com.cosm.client.model.Datapoint;
import com.cosm.client.model.Datastream;
import com.cosm.client.model.Feed;
import com.cosm.client.model.Trigger;
import com.cosm.client.requester.exceptions.ParseToObjectException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ParserUtil
{
	private static ObjectMapper objectMapper;

	// TODO temp
	enum ParseArg
	{
		feed(Feed.class, null, false, null, false),
		datastream(Datastream.class, null, true, "datastreams", true),
		datapoint(Datapoint.class, null, false, "datapoints", true),
		trigger(Trigger.class, null, false, null, false),
		apikey(ApiKey.class, "key", false, "key", false);

		private Class objClass;
		private String updateRoot;
		private boolean isUpdateHasId;
		private String createRoot;
		private boolean isCreateHasId;

		ParseArg(Class objClass, String updateRoot, boolean isUpdateHasId, String createRoot, boolean isCreateHasId)
		{
			this.objClass = objClass;
			this.updateRoot = updateRoot;
			this.isUpdateHasId = isUpdateHasId;
			this.createRoot = createRoot;
			this.isCreateHasId = isCreateHasId;
		}

		public Class getObjClass()
		{
			return objClass;
		}

		public String getUpdateRoot()
		{
			return updateRoot;
		}

		public boolean isUpdateHasId()
		{
			return isUpdateHasId;
		}

		public String getCreateRoot()
		{
			return createRoot;
		}

		public boolean isCreateHasId()
		{
			return isCreateHasId;
		}

		public static ParseArg valueOf(ConnectedObject obj)
		{
			for (ParseArg arg : ParseArg.values())
			{
				if (obj.getClass() == arg.objClass)
				{
					return arg;
				}
			}

			return null;
		}
	}

	/**
	 * Get the list of model objects and create json with root name if there's
	 * more than 1, strip the id node if there's only 1.
	 * 
	 * @param isUpdate
	 *            TODO
	 * @param models
	 * 
	 * @return json string suitable for Cosm API consumption
	 * @throws ParseToObjectException
	 *             if unable to completely parse from model to json or if models
	 *             is empty or null
	 */
	public static <T extends ConnectedObject> String toJson(boolean isUpdate, T... models)
	{
		if (models == null || models.length == 0)
		{
			throw new ParseToObjectException("No model to parse to object");
		}

		if (models.length > 1 && (models[0] instanceof Feed) && (models[0] instanceof Trigger))
		{
			throw new UnsupportedOperationException(String.format("Bulk operation is not currently supported for %s", models[0]
					.getClass().getSimpleName()));
		}

		ParseArg arg = ParseArg.valueOf(models[0]);

		// TODO strip nodes that has a null key?
		String json = null;

		try
		{
			// ADD ROOT
			// Setting SerializationConfig.Feature.WRAP_ROOT_VALUE at mapper
			// did not read annotated label properly, use withRootName
			if (isUpdate && arg.updateRoot != null)
			{
				json = getObjectMapper().writer().withRootName(arg.updateRoot).writeValueAsString(models);
			} else if (!isUpdate && arg.createRoot != null)
			{
				json = getObjectMapper().writer().withRootName(arg.createRoot).writeValueAsString(models);
			} else
			{
				T model = models[0];
				json = getObjectMapper().writeValueAsString(model);
			}

			// STRIP IDs
			if ((isUpdate && !arg.isUpdateHasId) || (!isUpdate && !arg.isCreateHasId))
			{
				JsonNode nodes = getObjectMapper().readTree(json);

				// FIXME Very dodgy way to strip the node that
				// represents id because UPDATE doesn't like the full json
				// object, at least do this with some @Id annotation
				if (models[0] instanceof Datapoint)
				{
					((ObjectNode) nodes).remove("at");
				} else
				{
					((ObjectNode) nodes).remove("id");
				}

				json = getObjectMapper().writeValueAsString(nodes);
			}
		} catch (IOException e)
		{
			throw new ParseToObjectException("Cannot parse model to object", e);
		}

		return json;
	}

	public static <T extends ConnectedObject> Collection<T> toConnectedObjects(String body, Class elementType)
	{
		Collection<T> objs;

		try
		{
			CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, elementType);
			objs = getObjectMapper().readValue(body, collectionType);
		} catch (IOException e)
		{
			throw new ParseToObjectException(String.format("Unable to parse [%s] to %s.", body, elementType), e);
		}
		return objs;
	}

	static <T extends ConnectedObject> T toConnectedObject(String body, Class elementType)
	{
		T obj;
		try
		{
			obj = (T) getObjectMapper().readValue(body, elementType);
		} catch (IOException e)
		{
			throw new ParseToObjectException(String.format("Unable to parse [%s] to %s.", body, elementType), e);
		}
		return obj;
	}

	public static ObjectMapper getObjectMapper()
	{
		if (objectMapper == null)
		{
			ObjectMapper retval = new ObjectMapper();
			// retval.configure(SerializationFeature.WRAP_ROOT_VALUE, true);

			retval.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			retval.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper = retval;
		}
		return objectMapper;
	}
}
