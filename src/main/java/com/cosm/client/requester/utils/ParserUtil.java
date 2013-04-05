package com.cosm.client.requester.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.cosm.client.model.ApiKey;
import com.cosm.client.model.ConnectedObject;
import com.cosm.client.model.Datapoint;
import com.cosm.client.model.Datastream;
import com.cosm.client.model.Feed;
import com.cosm.client.model.Trigger;
import com.cosm.client.requester.exceptions.ParseToObjectException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ParserUtil
{
	private static ObjectMapper objectMapper;

	// TODO temp
	enum ParseArg
	{
		feed(Feed.class, null, false, null, false, "id"),
		datastream(Datastream.class, null, true, "datastreams", true, "id"),
		datapoint(Datapoint.class, null, false, "datapoints", true, "at"),
		trigger(Trigger.class, null, false, null, false, "id"),
		apikey(ApiKey.class, "key", false, "key", false, "api_key");

		private Class objClass;
		private String updateRoot;
		private boolean isUpdateHasId;
		private String createRoot;
		private boolean isCreateHasId;
		private String idName;

		ParseArg(Class objClass, String updateRoot, boolean isUpdateHasId, String createRoot, boolean isCreateHasId, String idName)
		{
			this.objClass = objClass;
			this.updateRoot = updateRoot;
			this.isUpdateHasId = isUpdateHasId;
			this.createRoot = createRoot;
			this.isCreateHasId = isCreateHasId;
			this.idName = idName;
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

		public String getIdName()
		{
			return idName;
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
	 * Get the list of model objects and create json as expected by the API.
	 * 
	 * @param isUpdate
	 *            parse object to body suitable for updates if true; for
	 *            creates, otherwise
	 * @param models
	 *            models to be parsed to body
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

			// ApiKey needs to be wrapped in a root node without the array
			// container, hack the standards!
			if (arg.objClass == ApiKey.class)
			{
				int start = json.indexOf("\"key\":[") + 6;
				int end = json.lastIndexOf("}") - 1;
				StringBuilder sb = new StringBuilder(json);
				sb.setCharAt(start, ' ');
				sb.setCharAt(end, ' ');
				json = sb.toString();
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

	public static <T extends ConnectedObject> T toConnectedObject(String body, Class elementType)
	{
		T obj;

		if (ApiKey.class.equals(elementType))
		{
			// ApiKey comes wrapped in a root node without the array container,
			// hackily remove the hack so it can be parsed by standard parsers
			int start = body.indexOf("\"key\":") + 6;
			int end = body.lastIndexOf("}");
			body = body.substring(start, end);
		}

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
			retval.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			retval.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			retval.configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, false);
			retval.configure(SerializationFeature.EAGER_SERIALIZER_FETCH, false);
			objectMapper = retval;
		}
		return objectMapper;
	}
}
