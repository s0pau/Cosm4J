package com.cosm.client.model;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Trigger resource/model.
 * 
 * @see https://cosm.com/docs/v2/trigger/
 * 
 * @author s0pau
 * 
 */
public class Trigger implements ConnectedObject
{

	/**
	 * Types of triggers available
	 * 
	 * @see https://cosm.com/docs/v2/trigger/
	 * 
	 */
	public enum TriggerType
	{
		onGreaterThan("gt"),
		onGreaterThanOrEqual("gte"),
		onLessThan("lt"),
		onLessThanOrEqual("lte"),
		onEqual("eq"),
		onChange("change"),
		onNoUpdates("frozen"),
		onUpdateResumes("live");

		private final String attributeName;

		TriggerType(String attributeName)
		{
			this.attributeName = attributeName;
		}

		@JsonValue
		String getAttributeName()
		{
			return attributeName;
		}
	}

	private Integer id;
	private TriggerType type;
	@JsonProperty("stream_id")
	private String datastreamId;

	@JsonProperty("environment_id")
	private String feedId;

	@JsonProperty("user")
	private String login;

	// private String description;

	@JsonProperty("notified_at")
	private String notifiedAt;

	@JsonProperty("threshold_value")
	private Double thresholdValue;

	/**
	 * Url of where the trigger posts to
	 */
	private URI url;

	private transient Datastream datastream;
	private transient Feed feed;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getDatastreamId()
	{
		return datastreamId;
	}

	public void setDatastreamId(String datastreamId)
	{
		this.datastreamId = datastreamId;
	}

	public Double getThresholdValue()
	{
		return thresholdValue;
	}

	public void setThresholdValue(Double thresholdValue)
	{
		this.thresholdValue = thresholdValue;
	}

	public TriggerType getType()
	{
		return type;
	}

	public void setType(TriggerType type)
	{
		this.type = type;
	}

	public URI getUrl()
	{
		return url;
	}

	public void setUrl(URI url)
	{
		this.url = url;
	}

	public String getNotifiedAt()
	{
		return notifiedAt;
	}

	public void setNotifiedAt(String notifiedAt)
	{
		this.notifiedAt = notifiedAt;
	}

	@Override
	public String getIdString()
	{
		return String.valueOf(id);
	}

	@Override
	public boolean memberEquals(ConnectedObject other)
	{
		// TODO Auto-generated method stub
		return false;
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

		if (!(obj instanceof Trigger))
		{
			return false;
		}

		Trigger other = (Trigger) obj;

		if (this.id != other.id)
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
}
