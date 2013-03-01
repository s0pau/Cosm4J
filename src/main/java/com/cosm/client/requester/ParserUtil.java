package com.cosm.client.requester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.cosm.client.model.ConnectedObject;
import com.cosm.client.model.Datapoint;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ParserUtil
{
	private static ObjectMapper objectMapper;

	/**
	 * Get the list of model objects and create json with root name if there's
	 * more than 1, strip the id node if there's only 1.
	 * 
	 * @param isUpdate
	 *            if true, strip the id node; else add the root node
	 * @param models
	 * @return json string suitable for Cosm API consumption
	 * @throws CosmClientException
	 *             if unable to completely parse from model to json
	 */
	static <T extends ConnectedObject> String toJson(boolean isUpdate, T... models)
	{
		// TODO strip nodes that has a null key
		String json = null;

		try
		{
			if (isUpdate)
			{
				if (models.length == 1)
				{
					T model = models[0];

					json = getObjectMapper().writeValueAsString(model);
					JsonNode nodes = getObjectMapper().readTree(json);

					// FIXME Very dodgy way to strip the node that
					// represents id because UPDATE doesn't like the full json
					// object
					if (model instanceof Datapoint)
					{
						((ObjectNode) nodes).remove("at");
					} else
					{
						((ObjectNode) nodes).remove("id");
					}

					json = getObjectMapper().writeValueAsString(nodes);
				} else
				{
					throw new UnsupportedOperationException("Bulk updates are not currently supported");
				}
			} else
			{
				// Setting SerializationConfig.Feature.WRAP_ROOT_VALUE for
				// mapper did not read the annotation value label, instead it
				// sets root value to "<classname>[]". FIXME
				String rootName = models[0].getClass().getSimpleName().toLowerCase().concat("s");
				json = getObjectMapper().writer().withRootName(rootName).writeValueAsString(models);
			}

		} catch (IOException e)
		{
			throw new ParseToObjectException("Cannot parse model to object", e);
		}

		return json;
	}

	static <T extends ConnectedObject> Collection<T> toConnectedObjects(String body, Class elementType)
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

	private static ObjectMapper getObjectMapper()
	{
		if (objectMapper == null)
		{
			ObjectMapper retval = new ObjectMapper();
			// retval.configure(SerializationFeature.WRAP_ROOT_VALUE, true);

			retval.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			retval.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			objectMapper = retval;
		}
		return objectMapper;
	}
}
