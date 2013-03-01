package com.cosm.client.requester;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import com.cosm.client.CosmConfig;

public class TestUtil
{
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
		CosmConfig.getInstance().setApiKey("EI-h5T4VzXt9eQlIGYVhoMyisnySAKxXYU0zbjF5dGtYZz0g");
		CosmConfig.getInstance().setBaseURI("http://api.dds-1537376.192.168.60.179.xip.io/v2/");
	}
}
