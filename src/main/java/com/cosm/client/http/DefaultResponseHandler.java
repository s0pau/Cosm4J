package com.cosm.client.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.log4j.Logger;

import com.cosm.client.CosmClientException;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.model.ConnectedObject;

/**
 * Class for handling the http response
 * 
 * @author s0pau
 * @param <T>
 * 
 */
public class DefaultResponseHandler<T extends ConnectedObject> implements ResponseHandler<Response<T>>
{
	private static Logger log = Logger.getLogger(DefaultResponseHandler.class);

	@Override
	public Response<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException
	{
		if (!isHttpStatusOK(response.getStatusLine().getStatusCode()))
		{
			// skip operation on parsing response unless success
			throw new HttpException("Http response status indicates unsuccessful operation", response);
		}

		log.info(String.format("Handling response [%s]", response.getStatusLine().getStatusCode()));

		Response<T> retval = new Response<>();

		StringBuilder bodyBuilder = null;
		try (InputStream contentStream = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(contentStream)))
		{
			bodyBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null)
			{
				bodyBuilder.append(line + "\n");
			}
		} catch (IOException e)
		{
			throw new CosmClientException("Unable to parse response", e);
		}

		retval.setStatusCode(response.getStatusLine().getStatusCode());
		retval.setBody(bodyBuilder.toString());

		Map<String, Object> headers = new HashMap<>();
		for (Header header : response.getAllHeaders())
		{
			headers.put(header.getName(), header.getValue());
		}
		retval.setHeaders(headers);

		return retval;
	}

	private boolean isHttpStatusOK(int statusCode)
	{
		if (statusCode >= 300)
		{
			return false;
		}

		return true;
	}
}
