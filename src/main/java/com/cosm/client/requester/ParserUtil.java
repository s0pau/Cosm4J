package com.cosm.client.requester;

import java.io.IOException;

import com.cosm.client.model.CosmObject;
import com.cosm.client.model.Datapoint;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ParserUtil
{
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
	static <T extends CosmObject> String toJson(ObjectMapper objectMapper, boolean isUpdate, T... models)
	{
		String json = null;

		try
		{
			if (isUpdate)
			{
				if (models.length == 1)
				{
					T model = models[0];

					json = objectMapper.writeValueAsString(model);
					JsonNode nodes = objectMapper.readTree(json);

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

					json = objectMapper.writeValueAsString(nodes);
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
				json = objectMapper.writer().withRootName(rootName).writeValueAsString(models);
			}

		} catch (IOException e)
		{
			throw new CosmClientException("Cannot parse model to object", e);
		}
		
		return json;
	}

}
