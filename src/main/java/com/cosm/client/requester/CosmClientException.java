package com.cosm.client.requester;

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
