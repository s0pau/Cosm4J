package com.cosm.client.http.util.exception;

import com.cosm.client.CosmClientException;
import com.cosm.client.model.DomainObject;

/**
 * Exception condition when parsing to {@link DomainObject}
 * 
 * @author s0pau
 * 
 */
public class ParseToObjectException extends CosmClientException
{
	private static final long serialVersionUID = 7105379283948908356L;

	public ParseToObjectException(String msg, Throwable e)
	{
		super(msg, e);
	}

	public ParseToObjectException(String msg)
	{
		super(msg);
	}
}