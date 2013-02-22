package com.cosm.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Marker interface for all resources that can be requested from Cosm API
 * directly
 * 
 * @author s0pau
 * 
 */
public interface CosmObject
{
	String getIdString();
}
