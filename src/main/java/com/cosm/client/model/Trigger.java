package com.cosm.client.model;

import java.net.URI;

import com.cosm.client.requester.utils.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@JsonProperty("trigger_type")
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

	@JsonIgnore
	private transient Datastream datastream;

	@JsonIgnore
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

	public String getFeedId()
	{
		return feedId;
	}

	public void setFeedId(String feedId)
	{
		this.feedId = feedId;
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

	public String getLogin()
	{
		return login;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	public String getNotifiedAt()
	{
		return notifiedAt;
	}

	public void setNotifiedAt(String notifiedAt)
	{
		this.notifiedAt = notifiedAt;
	}

	@JsonIgnore
	@Override
	public String getIdString()
	{
		return String.valueOf(id);
	}

	@Override
	public boolean memberEquals(ConnectedObject obj)
	{
		if (!equals(obj))
		{
			return false;
		}

		Trigger other = (Trigger) obj;

		if (!ObjectUtil.nullCheckEquals(this.type, other.type))
		{
			return false;
		}

		if (!ObjectUtil.nullCheckEquals(this.datastreamId, other.datastreamId))
		{
			return false;
		}

		if (!ObjectUtil.nullCheckEquals(this.feedId, other.feedId))
		{
			return false;
		}

		if (!ObjectUtil.nullCheckEquals(this.login, other.login))
		{
			return false;
		}

		if (!ObjectUtil.nullCheckEquals(this.notifiedAt, other.notifiedAt))
		{
			return false;
		}

		if (!ObjectUtil.nullCheckEquals(this.thresholdValue, other.thresholdValue))
		{
			return false;
		}

		if (!ObjectUtil.nullCheckEquals(this.url, other.url))
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
