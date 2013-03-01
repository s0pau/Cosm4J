package com.cosm.client.model;

import java.util.Collection;

import com.cosm.client.requester.utils.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Datastream implements ConnectedObject<Datastream>
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

	@JsonIgnore
	@Override
	public String getIdString()
	{
		return id;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (this == obj)
		{
			return true;
		}

		if (!(obj instanceof Datastream))
		{
			return false;
		}

		Datastream other = (Datastream) obj;

		if (!ObjectUtil.nullCheckEquals(this.id, other.id))
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int retval = 1;
		retval += (id == null ? 0 : id.hashCode() * 37);
		return retval;
	}

	public boolean memberEquals(Datastream other)
	{
		if (!equals(other))
		{
			return false;
		}

		if (!ObjectUtil.nullCheckEquals(this.updatedAt, other.updatedAt))
		{
			return false;
		}

		if (!ObjectUtil.nullCheckEquals(this.value, other.value))
		{
			return false;
		}

		if (!ObjectUtil.nullCheckEquals(this.minValue, other.minValue))
		{
			return false;
		}

		if (!ObjectUtil.nullCheckEquals(this.maxValue, other.maxValue))
		{
			return false;
		}

		if (!ObjectUtil.memberEquals(this.getTags(), other.getTags()))
		{
			return false;
		}

		if (!ObjectUtil.nullCheckEquals(this.getUnit(), other.getUnit()))
		{
			return false;
		}

		if (!ObjectUtil.memberEquals(this.getDatapoints(), other.getDatapoints()))
		{
			return false;
		}

		return true;
	}
}
