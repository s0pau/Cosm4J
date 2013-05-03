package com.cosm.client.model;

import com.cosm.client.utils.CollectionUtil;

/**
 * Interface for all resources that can be requested from the API directly. This
 * is super class of all resource objects (in restful API terms) or model object
 * (in MVC terms).
 * 
 * @author s0pau
 * 
 */
public interface DomainObject
{
	/**
	 * @param other
	 * @return true, if all member fields are equal by equal() method
	 *         (collections are considered equal if the members of the
	 *         collection's equal() returns true using
	 *         {@link CollectionUtil#equal(java.util.Collection, java.util.Collection)}
	 *         ; false otherwise
	 */
	boolean memberEquals(DomainObject other);

	/**
	 * @return the id of this object
	 */
	Object getId();
}
