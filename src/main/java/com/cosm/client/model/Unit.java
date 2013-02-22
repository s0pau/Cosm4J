package com.cosm.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Unit model
 * 
 * @author s0pau
 * 
 */
public class Unit
{
	/**
	 * http://www.eeml.org/#units
	 */
	enum IFCClassification
	{
		BASIC_SI, DERIVED_SI, CONVERSION_BASED_UNITS, DERIVED_UNITS, CONTEXT_DEPENDENT_UNITS;
	}

	/**
	 * This is the unit of the datastream, e.g. Celsius
	 */
	private String label;

	/**
	 * Symbolic representation of this unit - e.g. C is the symbol for Celcius
	 */
	private String symbol;

	/**
	 * This attribute is supported according to the API but is very much unused,
	 * consider deprecated.
	 */
	@Deprecated
	@JsonProperty("type")
	private IFCClassification unitType;

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public IFCClassification getUnitType()
	{
		return unitType;
	}

	public void setUnitType(IFCClassification unitType)
	{
		this.unitType = unitType;
	}
}
