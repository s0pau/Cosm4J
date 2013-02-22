package com.cosm.client.requester;

import java.io.IOException;

public class CosmClientException extends RuntimeException
{
	Exception wrapped;

	public CosmClientException(String msg, IOException e)
	{
		super(msg);
		this.wrapped = e;
	}

	public Throwable getCause()
	{
		return wrapped == null ? null : wrapped.getCause();
	}

	public String getLocalisedMessage()
	{
		return wrapped == null ? null : wrapped.getLocalizedMessage();
	}
}
