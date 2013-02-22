package com.cosm.client.model;

import java.util.Collection;

import com.cosm.client.requester.RequestHandler.RequestMethod;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Permission model, accessible from {@link ApiKey}
 * 
 * @author spau
 * 
 */
public class Permission
{
	@JsonProperty("access_methods")
	private Collection<String> accessMethodIds;

	@JsonProperty("source_ip")
	private String sourceIp;
	// private String referer;
	// private Integer minimumInterval;
	// private String label;
	private Collection<Resource> resources;

	private transient Collection<RequestMethod> accessMethods;

	public Collection<String> getAccessMethodIds()
	{
		return accessMethodIds;
	}

	public void setAccessMethodIds(Collection<String> accessMethodIds)
	{
		this.accessMethodIds = accessMethodIds;
	}

	public String getSourceIp()
	{
		return sourceIp;
	}

	public void setSourceIp(String sourceIp)
	{
		this.sourceIp = sourceIp;
	}

	public Collection<Resource> getResources()
	{
		return resources;
	}

	public void setResources(Collection<Resource> resources)
	{
		this.resources = resources;
	}
}
