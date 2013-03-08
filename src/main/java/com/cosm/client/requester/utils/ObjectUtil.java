package com.cosm.client.requester.utils;

import java.util.Collection;

import com.cosm.client.model.ConnectedObject;

public class ObjectUtil
{
	public static <T extends ConnectedObject> boolean deepEquals(Collection<T> one, Collection<T> two)
	{
		if (!CollectionUtil.deepEquals(one, two))
		{
			return false;
		}

		int matchedCounts = 0;
		int i = 1;
		int quitEarlyThreshold = (int) Math.round(one.size() / 2 + 0.5);
		for (T obj1 : one)
		{
			if (i >= quitEarlyThreshold && matchedCounts < quitEarlyThreshold)
			{
				// optimisation - quit early over half of the collection objects
				// does not match
				return false;
			}

			for (T obj2 : two)
			{
				if (obj1.memberEquals((ConnectedObject) two))
				{
					matchedCounts++;
				}
			}
			i++;
		}

		return matchedCounts == one.size();
	}

	public static <T extends Object> boolean nullCheckEquals(T one, T two)
	{
		if (one == null)
		{
			if (two == null)
			{
				return true;
			}
		} else if (one.equals(two))
		{
			return true;
		}

		return false;
	}
}
