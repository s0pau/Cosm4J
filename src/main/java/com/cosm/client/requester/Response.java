package com.cosm.client.requester;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.model.ConnectedObject;
import com.cosm.client.requester.exceptions.ParseToObjectException;
import com.cosm.client.requester.utils.ParserUtil;

/**
 * Wrapper to decouple response implementation from the rest of the library
 * 
 * @author s0pau
 * 
 */
public class Response<T extends ConnectedObject>
{
	public static final String HEADER_NEW_OBJ_URI = "Location";

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
	private Map<String, Object> headers;
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

	public Map<String, Object> getHeaders()
	{
		return headers;
	}

	public Object getHeaders(String key)
	{
		return headers.get(key);
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

	/**
	 * @return the id of object as indicated in the headers, e.g. for Feed and
	 *         Trigger; null if no such header is found.
	 */
	public Integer getIdFromResponse()
	{
		Collection<String> headerVal = (Collection<String>) getHeaders(HEADER_NEW_OBJ_URI);
		if (headerVal.isEmpty())
		{
			return null;
		}

		String feedUrlStr = (String) headerVal.toArray()[0];
		String[] tokens = feedUrlStr.split("/");
		return Integer.valueOf(tokens[tokens.length - 1]);
	}
}
