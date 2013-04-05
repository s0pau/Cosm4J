package com.cosm.client.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.apache.http.HttpStatus;

import com.cosm.client.CosmConfig;
import com.cosm.client.http.exception.HttpException;
import com.cosm.client.http.util.ParserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil
{
	public static final int TEST_FEED_ID = 929;// 97684;

	public static String getStringFromFile(String filePath)
	{
		try
		{
			FileInputStream fileStream = null;
			try
			{
				fileStream = new FileInputStream(new File(filePath));
				FileChannel fileChannel = fileStream.getChannel();
				MappedByteBuffer bb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
				return Charset.defaultCharset().decode(bb).toString();
			} finally
			{
				fileStream.close();
			}
		} catch (IOException io)
		{
			throw new RuntimeException(io);
		}
	}

	public static void loadDefaultTestConfig()
	{
		CosmConfig.getInstance().reload();
	}

	public static ObjectMapper getObjectMapper()
	{
		return ParserUtil.getObjectMapper();
	}
}
