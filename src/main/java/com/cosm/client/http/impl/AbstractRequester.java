package com.cosm.client.http.impl;

import com.cosm.client.http.DefaultRequestHandler;
import com.cosm.client.http.DefaultRequestHandler.HttpMethod;
import com.cosm.client.http.Response;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.DomainObject;

/**
 * Base request implementation for basic CRUD operations and hooks.
 * 
 * @author s0pau
 * 
 */
public abstract class AbstractRequester<I extends Object, T extends DomainObject>
{
	public T create(T toCreate) throws HttpException
	{
		Response<T> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.POST, getResourcesPath(), toCreate);
		I id = (I) (toCreate.getId() == null ? getCreatedId(response) : toCreate.getId());
		return get(id);
	}

	public T get(I id) throws HttpException, ParseToObjectException
	{
		Response<T> response = DefaultRequestHandler.getInstance().doRequest(HttpMethod.GET, getResourcePath(id));
		return response.getBodyAsObject(getObjectClass());
	}

	public void delete(I id) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.DELETE, getResourcePath(id));
	}

	public T update(T toUpdate) throws HttpException
	{
		DefaultRequestHandler.getInstance().doRequest(HttpMethod.PUT, getResourcePath((I) toUpdate.getId()), toUpdate);
		return toUpdate;
	}

	/**
	 * Override this method if the id provided in the response after create.
	 * 
	 * @param response
	 * @return id from the headers of response, in the correct type
	 */
	protected I getCreatedId(Response<T> response)
	{
		return null;
	}

	/**
	 * @return the restful path to resources, e.g. http://url/resources
	 */
	abstract protected String getResourcesPath();

	/**
	 * @return the restful path to a resource given the id, e.g.
	 *         http://url/resources/id
	 */
	abstract protected String getResourcePath(I id);

	/**
	 * @return the class of object handled by this requester
	 */
	abstract protected Class getObjectClass();
}
