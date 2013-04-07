package com.cosm.client.http.exception;

import com.cosm.client.CosmClientException;

/**
 * Exception condition when constructing requests
 * 
 * @author s0pau
 * 
 */
public class RequestInvalidException extends CosmClientException
{
	public RequestInvalidException(String msg, Throwable e)
	{
		super(msg, e);
	}
}