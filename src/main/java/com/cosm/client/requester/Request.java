package com.cosm.client.requester;

import java.util.Collection;
import java.util.Map;

import com.cosm.client.model.CosmObject;
import com.cosm.client.requester.RequestHandler.RequestMethod;

public class Request<T extends CosmObject>
{
	RequestMethod requestMethod;
	String app;
	Map<String, Object> params;
	Collection<T> body;

	public RequestMethod getRequestMethod()
	{
		return requestMethod;
	}

	public void setRequestMethod(RequestMethod requestMethod)
	{
		this.requestMethod = requestMethod;
	}

	public String getApp()
	{
		return app;
	}

	public void setApp(String app)
	{
		this.app = app;
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
