package com.cosm.client;

import javax.ws.rs.core.MediaType;

public class CosmConfig
{
	private static CosmConfig instance;

	private static final String DEFAULT_BASE_URI = "http://api.cosm.com/v2/";
	private static final AcceptedMediaType DEFAULT_ACCEPTED_MEDIA_TYPE = AcceptedMediaType.json;

	private String apiKey;

	/**
	 * The media type for the body. Defaults to JSON if not specified.
	 */
	private AcceptedMediaType responseMedia = DEFAULT_ACCEPTED_MEDIA_TYPE;

	/**
	 * Defaults to Cosm V2 Api
	 */
	private String baseUri = DEFAULT_BASE_URI;

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

	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}

	public AcceptedMediaType getResponseMedia()
	{
		return responseMedia;
	}

	public void setResponseMedia(AcceptedMediaType responseMedia)
	{
		this.responseMedia = responseMedia;
	}

	public void setBaseURI(String baseUri)
	{
		this.baseUri = baseUri;
	}

	public String getBaseURI()
	{
		return baseUri;
	}

	public void reset()
	{
		apiKey = null;
		responseMedia = DEFAULT_ACCEPTED_MEDIA_TYPE;
		baseUri = DEFAULT_BASE_URI;
	}
}
