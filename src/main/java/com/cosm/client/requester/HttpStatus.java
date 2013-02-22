package com.cosm.client.requester;

/**
 * Didn't want to import the whole Http library for just the codes.
 * 
 * @author spau
 * 
 */
public enum HttpStatus
{
	NOT_FOUND(404), FORBIDDEN(403);

	private int code;

	private HttpStatus(int errorCode)
	{
		this.code = errorCode;
	}

	public int getCode()
	{
		return code;
	}
}