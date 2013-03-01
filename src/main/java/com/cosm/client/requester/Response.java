package com.cosm.client.requester;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.model.ConnectedObject;

/**
 * Wrapper to decouple response implementation from the rest of the library
 * 
 * @author s0pau
 * 
 */
public class Response<T extends ConnectedObject>
{

	/**
	 * Didn't want to import the whole Http library for just the codes.
	 * 
	 * @author s0pau
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

		public static HttpStatus valueOf(int code)
		{
			for (HttpStatus status : HttpStatus.values())
			{
				if (status.code == code)
				{
					return status;
				}
			}
			return null;
		}
	}

	private HttpStatus statusCode;
	private Map<String, String> headers;
	private String body;

	public HttpStatus getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(int statusCode)
	{
		this.statusCode = HttpStatus.valueOf(statusCode);
	}

	public void setStatusCode(HttpStatus statusCode)
	{
		this.statusCode = statusCode;
	}

	public Map<String, String> getHeaders()
	{
		return headers;
	}

	/**
	 * Shallow cope the collection of headers
	 * 
	 * @param headers
	 */
	public void setHeaders(Map headers)
	{
		this.headers = new HashMap<>();
		this.headers.putAll(headers);
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public T getBodyAsObject(Class returnType) throws ParseToObjectException
	{
		Collection<T> models = ParserUtil.toConnectedObjects(body, returnType);
		if (!models.isEmpty())
		{
			return (T) (models.toArray())[0];
		}

		return null;
	}

	public Collection<T> getBodyAsObjects(Class returnType) throws ParseToObjectException
	{
		return ParserUtil.toConnectedObjects(body, returnType);
	}
}