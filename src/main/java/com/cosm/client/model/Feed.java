package com.cosm.client.model;

import java.net.URI;
import java.util.Collection;

import com.cosm.client.requester.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Feed resource/model
 * 
 * @see https://cosm.com/docs/v2/feed/
 * 
 * @author s0pau
 * 
 */
@JsonRootName(value = "")
public class Feed implements CosmObject<Feed>
{
	public enum Status
	{
		frozen, live
	}

	private Integer id;
	private String title;
	private String description;

	@JsonProperty("updated")
	private String updatedAt;

	@JsonProperty("created")
	private String createdAt;

	/**
	 * A link to the creator of the feed
	 */
	@JsonProperty("creator")
	private URI creatorUri;

	@JsonProperty("feed")
	private URI feedUri;

	private Status status;

	// private String email;
	// private URI icon;

	@JsonProperty("website")
	private URI website;

	private Collection<String> tags;

	private Location location;

	// TODO where is this on the API doc?
	@JsonProperty("private")
	private boolean isPrivate;

	// TODO where is this on the API doc?
	// @JsonProperty("owner")
	// private String login;

	private Collection<Datastream> datastreams;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getUpdatedAt()
	{
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt)
	{
		this.updatedAt = updatedAt;
	}

	public String getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(String createdAt)
	{
		this.createdAt = createdAt;
	}

	public URI getCreatorUri()
	{
		return creatorUri;
	}

	public void setCreatorUri(URI creatorUri)
	{
		this.creatorUri = creatorUri;
	}

	public URI getFeedUri()
	{
		return feedUri;
	}

	public void setFeedUri(URI feedUri)
	{
		this.feedUri = feedUri;
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	public URI getWebsite()
	{
		return website;
	}

	public void setWebsite(URI website)
	{
		this.website = website;
	}

	public Collection<String> getTags()
	{
		return tags;
	}

	public void setTags(Collection<String> tags)
	{
		this.tags = tags;
	}

	public Location getLocation()
	{
		return location;
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}

	public boolean isPrivate()
	{
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate)
	{
		this.isPrivate = isPrivate;
	}

	public Collection<Datastream> getDatastreams()
	{
		return datastreams;
	}

	public void setDatastreams(Collection<Datastream> datastreams)
	{
		this.datastreams = datastreams;
	}

	@JsonIgnore
	@Override
	public String getIdString()
	{
		return String.valueOf(id);
	}

	@Override
	public boolean memberEquals(Feed other)
	{
		if (!equals(other))
		{
			return false;
		}

		if (!CollectionUtil.nullCheckEquals(this.getTitle(), other.getTitle()))
		{
			return false;
		}

		if (!CollectionUtil.nullCheckEquals(this.getDescription(), other.getDescription()))
		{
			return false;
		}

		if (!CollectionUtil.nullCheckEquals(this.getUpdatedAt(), other.getUpdatedAt()))
		{
			return false;
		}

		if (!CollectionUtil.nullCheckEquals(this.getCreatedAt(), other.getCreatedAt()))
		{
			return false;
		}

		if (!CollectionUtil.nullCheckEquals(this.getCreatorUri(), other.getCreatorUri()))
		{
			return false;
		}

		if (!CollectionUtil.nullCheckEquals(this.getFeedUri(), other.getFeedUri()))
		{
			return false;
		}

		if (!CollectionUtil.nullCheckEquals(this.getStatus(), other.getStatus()))
		{
			return false;
		}

		if (!CollectionUtil.nullCheckEquals(this.getWebsite(), other.getWebsite()))
		{
			return false;
		}

		if (!CollectionUtil.memberEquals(this.getTags(), other.getTags()))
		{
			return false;
		}

		// TODO
		// if (!CollectionUtil.memberEquals(this.getLocation(),
		// other.getLocation()))
		// {
		// return false;
		// }

		if (this.isPrivate != other.isPrivate)
		{
			return false;
		}

		if (!CollectionUtil.memberEquals(this.getDatastreams(), other.getDatastreams()))
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

		if (!(obj instanceof Feed))
		{
			return false;
		}

		Feed other = (Feed) obj;

		if (id != other.id)
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int retval = 1;
		retval += this.id.hashCode() * 37;
		return retval;
	}
}
