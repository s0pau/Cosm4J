package com.cosm.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

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
	public enum IFCClassification
	{
		BASIC_SI("basicSI"),
		DERIVED_SI("derivedSI"),
		CONVERSION_BASED_UNITS("conservationBasedUnits"),
		DERIVED_UNITS("derivedUnits"),
		CONTEXT_DEPENDENT_UNITS("contextDependentUnits");

		private String jsonVal;

		private IFCClassification(String jsonVal)
		{
			this.jsonVal = jsonVal;
		}

		@JsonValue
		public String getJsonVal()
		{
			return jsonVal;
		}
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
	// @JsonProperty("type") TODO putting this on the member doesnt seem to have
	// effect for enum, investigate if feature needs to be switched on
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

	@JsonProperty("type")
	public IFCClassification getUnitType()
	{
		return unitType;
	}

	public void setUnitType(IFCClassification unitType)
	{
		this.unitType = unitType;
	}
}
