package com.cosm.client.http.util.exception;

import com.cosm.client.CosmClientException;
import com.cosm.client.model.ConnectedObject;

/**
 * Exception condition when parsing from {@link ConnectedObject}
 * 
 * @author s0pau
 * 
 */
public class ParseFromObjectException extends CosmClientException
{
	private static final long serialVersionUID = 1417537723548831992L;

	public ParseFromObjectException(String msg, Throwable e)
	{
		super(msg, e);
	}

	public ParseFromObjectException(String msg)
	{
		super(msg);
	}
}