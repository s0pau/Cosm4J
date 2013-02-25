package com.cosm.client.model;

import com.cosm.client.requester.CollectionUtil;

/**
 * Marker interface for all resources that can be requested from Cosm API
 * directly
 * 
 * @author s0pau
 * 
 */
public interface CosmObject<T>
{
	String getIdString();

	/**
	 * @param other
	 * @return true, if all member fields are equal by equal() method (collections are considered equal if the members of the collection's equal() returns true using
	 *         {@link CollectionUtil#equal(java.util.Collection, java.util.Collection)}
	 *         ; false otherwise
	 */
	boolean memberEquals(T other);
}
