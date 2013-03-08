package com.cosm.client;

/**
 * Parent exception from which all exception classes in this library extends.
 * 
 * @author s0pau
 * 
 */
public class CosmClientException extends RuntimeException
{
	public CosmClientException(String msg, Throwable e)
	{
		super(msg, e);
	}

	public CosmClientException(String msg)
	{
		super(msg);
	}

	@Override
	public String getLocalizedMessage()
	{
		return String.format("%s: exception: %s", super.getMessage(), super.getCause() == null ? "" : super.getCause()
				.getLocalizedMessage());
	}

	@Override
	public synchronized Throwable getCause()
	{
		return super.getCause();
	}
}