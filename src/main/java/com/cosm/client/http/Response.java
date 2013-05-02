package com.cosm.client.http;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cosm.client.http.util.ParserUtil;
import com.cosm.client.http.util.exception.ParseToObjectException;
import com.cosm.client.model.DomainObject;

/**
 * Wrapper to decouple response implementation from the rest of the library
 * 
 * @author s0pau
 * 
 */
public class Response<T extends DomainObject>
{
	public static final String HEADER_NEW_OBJ_URI = "Location";

	private int statusCode;
	private Map<String, String> headers;
	private String body;

	public int getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}

	public Map<String, String> getHeaders()
	{
		return headers;
	}

	public String getHeaders(String key)
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
		return ParserUtil.toConnectedObject(body, returnType);
	}

	public Collection<T> getBodyAsObjects(Class returnType) throws ParseToObjectException
	{
		return ParserUtil.toConnectedObjects(body, returnType);
	}

	/**
	 * @return the id of object as indicated in the headers, e.g. for Feed and
	 *         Trigger; null if no such header is found.
	 */
	public String getIdFromResponse()
	{
		String feedUrlStr = getHeaders(HEADER_NEW_OBJ_URI);
		String[] tokens = feedUrlStr.split("/");
		return tokens[tokens.length - 1];
	}
}
