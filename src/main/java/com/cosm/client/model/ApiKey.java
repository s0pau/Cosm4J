package com.cosm.client.model;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * ApiKey resource/model
 * 
 * @author s0pau
 * 
 */
@JsonRootName("key")
public class ApiKey implements CosmObject
{
	/**
	 * Actual string of the api key
	 */
	@JsonProperty("api_key")
	private String apiKey;

	/**
	 * Name of the api key
	 */
	private String label;

	// String expiresAt;

	@JsonProperty("private_access")
	private boolean isPrivateAccess;

	/**
	 * Mappings to {@link Resource}, if not empty, this api key is mapped to
	 * specific {@link feed}/{@link datastream}
	 */
	private Collection<Permission> permissions;

	public String getApiKey()
	{
		return apiKey;
	}

	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public boolean isPrivateAccess()
	{
		return isPrivateAccess;
	}

	public void setPrivateAccess(boolean isPrivateAccess)
	{
		this.isPrivateAccess = isPrivateAccess;
	}

	public Collection<Permission> getPermissions()
	{
		return permissions;
	}

	public void setPermissions(Collection<Permission> permissions)
	{
		this.permissions = permissions;
	}

	@JsonIgnore
	@Override
	public String getIdString()
	{
		return apiKey;
	}
}
