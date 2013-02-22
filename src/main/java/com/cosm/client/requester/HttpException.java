package com.cosm.client.requester;

import com.sun.jersey.api.client.ClientResponse;

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
