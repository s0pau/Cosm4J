package com.cosm.client.http;

import java.util.Collection;
import java.util.Map;

import com.cosm.client.http.DefaultRequestHandler.HttpMethod;
import com.cosm.client.model.ConnectedObject;

/**
 * Wrapper to encapsulating headers, params and body needed for building a
 * request for T
 * 
 * @param <T>
 *            T extends ConnectedObject
 * 
 * @author s0pau
 * 
 */
public class Request<T extends ConnectedObject>
{
	HttpMethod httpMethod;
	Map<String, Object> params;
	Collection<T> body;

	public HttpMethod getHttpMethod()
	{
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod)
	{
		this.httpMethod = httpMethod;
	}

	public Map<String, Object> getParams()
	{
		return params;
	}

	public void setParams(Map<String, Object> params)
	{
		this.params = params;
	}

	public Collection<T> getBody()
	{
		return body;
	}

	public void setBody(Collection<T> body)
	{
		this.body = body;
	}
}
