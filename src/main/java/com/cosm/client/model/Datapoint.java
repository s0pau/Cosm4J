package com.cosm.client.model;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Datapoint resource/model
 * 
 * @see https://cosm.com/docs/v2/datapoint/
 * 
 * @author s0pau
 * 
 */
@JsonRootName(value = "datapoint")
// @XmlRootElement(name = "datapoints")
public class Datapoint implements CosmObject
{
	/**
	 * id of the datapoint
	 */
	String at;
	String value;

	public String getAt()
	{
		return at;
	}

	public void setAt(String at)
	{
		this.at = at;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}
