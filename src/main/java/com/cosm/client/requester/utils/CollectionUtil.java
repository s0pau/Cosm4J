package com.cosm.client.requester.utils;

import java.util.Collection;

public class CollectionUtil
{
	public static <T extends Object> boolean deepEquals(Collection<T> one, Collection<T> two)
	{
		if (!ObjectUtil.nullCheckEquals(one, two))
		{
			return false;
		}

		if (one == null)
		{
			return true;
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
