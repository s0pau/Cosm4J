package com.cosm.client.requester.exceptions;

import com.cosm.client.CosmClientException;
import com.sun.jersey.api.client.ClientResponse;

/**
 * Exception condition when making HTTP requests to COSM API
 * 
 * @author s0pau
 * 
 */
public class HttpException extends CosmClientException
{
	private ClientResponse response = null;

	public HttpException(String msg, ClientResponse response)
	{
		super(msg);
		this.response = response;
	}

	public String getLocalizedMessage()
	{
		StringBuilder sb = new StringBuilder(getMessage());
		sb.append(" Status code: ").append(response.getStatus()).append(".");
		if (response != null && response.hasEntity())
		{
			sb.append(" Reason: ").append(response.getEntity(String.class)).append(".");
		}
		return sb.toString();
	}

	public int getStatusCode()
	{
		return response.getStatus();
	}
}
