package com.cosm.client.requester.exceptions;

import com.cosm.client.CosmClientException;

/**
 * Exception condition when parsing requests to {@link CosmObject}
 * 
 * @author spau
 * 
 */
public class ParseToObjectException extends CosmClientException
{
	public ParseToObjectException(String msg, Throwable e)
	{
		super(msg, e);
	}

	public ParseToObjectException(String msg)
	{
		super(msg);
	}
}
