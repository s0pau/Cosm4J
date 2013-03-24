package com.cosm.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import com.cosm.client.requester.utils.StringUtil;

/**
 * Config for accessing Cosm API.
 * 
 * @author s0pau
 */
public class CosmConfig
{
	private static CosmConfig instance;

	private static final String DEFAULT_BASE_URI = "http://api.cosm.com/v2/";
	private static final AcceptedMediaType DEFAULT_ACCEPTED_MEDIA_TYPE = AcceptedMediaType.json;
	private static final String CONFIG_FILE_NAME = "config.properties";

	/**
	 * The apiKey used for authenticating API calls.
	 */
	private String apiKey;

	/**
	 * The base uri for all API calls as defined in properties file, defaults to
	 * {@link DEFAULT_BASE_URI) if unspecified.
	 */
	private String baseUri;

	/**
	 * The media type for the body as defined in properties file, defaults to
	 * {@link DEFAULT_ACCEPTED_MEDIA_TYPE) if unspecified.
	 */
	private AcceptedMediaType responseMediaType = DEFAULT_ACCEPTED_MEDIA_TYPE;

	private final Properties prop = new Properties();

	public enum AcceptedMediaType
	{
		json(MediaType.APPLICATION_JSON_TYPE), xml(MediaType.APPLICATION_XML_TYPE), csv(MediaType.TEXT_HTML_TYPE);

		private MediaType mediaType;

		private AcceptedMediaType(MediaType mediaType)
		{
			this.mediaType = mediaType;
		}

		public MediaType getMediaType()
		{
			return mediaType;
		}
	}

	private CosmConfig()
	{
		// singleton
		loadProperties();
	}

	/**
	 * Load config from properties file.
	 */
	private void loadProperties()
	{
		try
		{
			InputStream in = new FileInputStream(new File(getClass().getProtectionDomain().getCodeSource().getLocation()
					.toString().substring(6).concat(CONFIG_FILE_NAME)));

			prop.load(in);

			baseUri = prop.getProperty("api.baseUri", DEFAULT_BASE_URI);

			String responseMediaProperty = prop.getProperty("api.responseMedia");
			if (!StringUtil.isNullOrEmpty(responseMediaProperty))
			{
				responseMediaType = AcceptedMediaType.valueOf(responseMediaProperty);
			}
			responseMediaType = responseMediaType == null ? DEFAULT_ACCEPTED_MEDIA_TYPE : responseMediaType;

			apiKey = prop.getProperty("api.key");
		} catch (IOException ex)
		{
			throw new CosmClientException("Unable to load config file.");
		}
	}

	/**
	 * @return singleton instance
	 */
	public static CosmConfig getInstance()
	{
		if (instance == null)
		{
			instance = new CosmConfig();
		}
		return instance;
	}

	public String getApiKey()
	{
		return apiKey;
	}

	public AcceptedMediaType getResponseMediaType()
	{
		return responseMediaType;
	}

	public String getBaseURI()
	{
		return baseUri;
	}

	/**
	 * Reloads the properties from config file
	 */
	public void reload()
	{
		loadProperties();
	}
}
