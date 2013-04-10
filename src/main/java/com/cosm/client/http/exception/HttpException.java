package com.cosm.client.http.exception;

import com.cosm.client.CosmClientException;
import com.cosm.client.utils.StringUtil;

/**
 * Exception condition when executing HTTP requests to API
 * 
 * @author s0pau
 * 
 */
public class HttpException extends CosmClientException
{
	private Integer statusCode;
	private String errorDetail;

	public HttpException(String msg, int statusCode, String errorDetail)
	{
		super(msg);
		this.statusCode = statusCode;
		this.errorDetail = errorDetail;
	}

	public HttpException(String msg, Exception e)
	{
		super(msg, e);
	}

	public String getLocalizedMessage()
	{
		if (statusCode == null)
		{
			return super.getLocalizedMessage();
		}

		StringBuilder sb = new StringBuilder(getMessage());
		sb.append("[Status code: ").append(statusCode).append(".");
		if (!StringUtil.isNullOrEmpty(errorDetail))
		{
			sb.append("; Reason: ").append(errorDetail);
		}
		sb.append("]");
		return sb.toString();
	}

	public int getStatusCode()
	{
		return statusCode == null ? 0 : statusCode;
	}
}
