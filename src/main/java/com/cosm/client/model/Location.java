package com.cosm.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Location model, accessible from {@link Feed}.
 * 
 * @author s0pau
 * 
 */
@JsonRootName("location")
class Location
{
	@JsonRootName("exposure")
	enum Exposure
	{
		indoor, outdoor
	}

	@JsonRootName("disposition")
	enum Disposition
	{
		fixed, mobile
	}

	@JsonRootName("domain")
	enum Domain
	{
		physical, virtual
	}

	private String name;

	@JsonProperty("lat")
	private double latitiude;

	@JsonProperty("lon")
	private double longitute;

	@JsonProperty("ele")
	private double elevation;

	private Exposure exposure;

	private Domain domain;

	private Disposition disposition;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public double getLatitiude()
	{
		return latitiude;
	}

	public void setLatitiude(double latitiude)
	{
		this.latitiude = latitiude;
	}

	public double getLongitute()
	{
		return longitute;
	}

	public void setLongitute(double longitute)
	{
		this.longitute = longitute;
	}

	public double getElevation()
	{
		return elevation;
	}

	public void setElevation(double elevation)
	{
		this.elevation = elevation;
	}

	public Exposure getExposure()
	{
		return exposure;
	}

	public void setExposure(Exposure exposure)
	{
		this.exposure = exposure;
	}

	public Domain getDomain()
	{
		return domain;
	}

	public void setDomain(Domain domain)
	{
		this.domain = domain;
	}

	public Disposition getDisposition()
	{
		return disposition;
	}

	public void setDisposition(Disposition disposition)
	{
		this.disposition = disposition;
	}
}