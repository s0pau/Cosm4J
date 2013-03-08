package com.cosm.client.requester.utils;

import java.util.Collection;

public class CollectionUtil
{
	public static boolean deepEquals(Collection one, Collection two)
	{
		if (!ObjectUtil.nullCheckEquals(one, two))
		{
			return false;
		}

		if (one.size() != two.size())
		{
			return false;
		}

		if (one.containsAll(two))
		{
			return true;
		}

		return false;
	}
}
