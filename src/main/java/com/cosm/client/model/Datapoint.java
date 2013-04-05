package com.cosm.client.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.cosm.client.utils.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@XmlRootElement(name = "datapoints")
public class Datapoint implements ConnectedObject
{
	/**
	 * id of the datapoint
	 */
	@JsonInclude(Include.NON_NULL)
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

	@JsonIgnore
	@Override
	public boolean memberEquals(ConnectedObject obj)
	{
		if (!equals(obj))
		{
			return false;
		}

		Datapoint other = (Datapoint) obj;
		if (!ObjectUtil.nullCheckEquals(this.value, other.value))
		{
			return false;
		}

		return true;
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

		if (!(obj instanceof Datapoint))
		{
			return false;
		}

		Datapoint other = (Datapoint) obj;

		if (!ObjectUtil.nullCheckEquals(this.at, other.at))
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int retval = 1;
		retval += (at == null ? 0 : at.hashCode() * 37);
		return retval;
	}
}
