package com.cosm.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.cosm.client.utils.StringUtil;

/**
 * Config for application behaviour and accessing Cosm API.
 * 
 * @author s0pau
 */
public class AppConfig
{
	private static AppConfig instance;

	private static final String DEFAULT_BASE_URI = "api.cosm.com/v2/";
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

	private Integer connectionTimeout;

	private Integer socketTimeout;

	private final Properties prop = new Properties();

	public enum AcceptedMediaType
	{
		json("application/json"), xml("application/json"), csv("text/html");

		private String contentType;

		private AcceptedMediaType(String contentType)
		{
			this.contentType = contentType;
		}

		public String getMediaType()
		{
			return contentType;
		}
	}

	private AppConfig()
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
			String fileName = null;
			if (System.getProperty("os.name").contains("Windows"))
			{
				fileName = getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(6)
						.concat(CONFIG_FILE_NAME);
			} else
			{
				fileName = getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(5)
						.concat(CONFIG_FILE_NAME);
			}
			InputStream in = new FileInputStream(new File(fileName));

			prop.load(in);

			baseUri = prop.getProperty("api.baseUri", DEFAULT_BASE_URI);

			String responseMediaProperty = prop.getProperty("api.responseMedia");
			if (!StringUtil.isNullOrEmpty(responseMediaProperty))
			{
				responseMediaType = AcceptedMediaType.valueOf(responseMediaProperty);
			}
			responseMediaType = responseMediaType == null ? DEFAULT_ACCEPTED_MEDIA_TYPE : responseMediaType;

			apiKey = prop.getProperty("api.key");

			if (prop.getProperty("http.connectionTimeout") != null)
			{
				connectionTimeout = Integer.valueOf(prop.getProperty("http.connectionTimeout"));
			}

			if (prop.getProperty("http.socketTimeout") != null)
			{
				socketTimeout = Integer.valueOf(prop.getProperty("http.socketTimeout"));
			}
		} catch (IOException ex)
		{
			throw new CosmClientException("Unable to load config propoerties", ex);
		}
	}

	/**
	 * @return singleton instance
	 */
	public static AppConfig getInstance()
	{
		if (instance == null)
		{
			instance = new AppConfig();
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

	public Integer getSocketTimeout()
	{
		return socketTimeout;
	}

	public Integer getConnectionTimeout()
	{
		return connectionTimeout;
	}

	/**
	 * Reloads the properties from config file
	 */
	public void reload()
	{
		loadProperties();
	}
}
