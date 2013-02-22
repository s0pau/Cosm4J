package com.cosm.client.model;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Datastream resource/model
 * 
 * @see https://cosm.com/docs/v2/datastream/
 * 
 * @author s0pau
 * 
 */
@JsonRootName(value = "datastream")
public class Datastream implements CosmObject
{
	private String id;

	@JsonProperty("at")
	private String updatedAt;

	/**
	 * Current datapoint's value // TODO somewhere said this should be read
	 * only?
	 */
	@JsonProperty("current_value")
	private String value;

	/**
	 * Min of all datapoint's value since the last reset
	 */
	@JsonProperty("min_value")
	private String minValue;

	/**
	 * Max of all datapoint's value since the last reset
	 */
	@JsonProperty("max_value")
	private String maxValue;

	private Collection<String> tags;

	private Unit unit;

	private Collection<Datapoint> datapoints;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUpdatedAt()
	{
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt)
	{
		this.updatedAt = updatedAt;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getMinValue()
	{
		return minValue;
	}

	public void setMinValue(String minValue)
	{
		this.minValue = minValue;
	}

	public String getMaxValue()
	{
		return maxValue;
	}

	public void setMaxValue(String maxValue)
	{
		this.maxValue = maxValue;
	}

	public Collection<String> getTags()
	{
		return tags;
	}

	public void setTags(Collection<String> tags)
	{
		this.tags = tags;
	}

	public Unit getUnit()
	{
		return unit;
	}

	public void setUnit(Unit unit)
	{
		this.unit = unit;
	}

	public Collection<Datapoint> getDatapoints()
	{
		return datapoints;
	}

	public void setDatapoints(Collection<Datapoint> datapoints)
	{
		this.datapoints = datapoints;
	}
}
