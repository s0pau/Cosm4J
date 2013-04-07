package com.cosm.client.http.exception;

import org.apache.http.HttpResponse;

import com.cosm.client.CosmClientException;

/**
 * Exception condition when executing HTTP requests to API
 * 
 * @author s0pau
 * 
 */
public class HttpException extends CosmClientException
{
	private HttpResponse response = null;

	public HttpException(String msg, HttpResponse response)
	{
		super(msg);
		this.response = response;
	}

	public HttpException(String msg, Exception e)
	{
		super(msg, e);
	}

	public String getLocalizedMessage()
	{
		if (response == null)
		{
			return super.getLocalizedMessage();
		}

		StringBuilder sb = new StringBuilder(getMessage());
		sb.append(" Status code: ").append(response.getStatusLine().getStatusCode()).append(".");
		if (response != null && response.getEntity() != null)
		{
			sb.append(" Reason: ").append(response.getEntity().toString()).append(".");
		}
		return sb.toString();
	}

	public int getStatusCode()
	{
		return response == null ? 0 : response.getStatusLine().getStatusCode();
	}
}
