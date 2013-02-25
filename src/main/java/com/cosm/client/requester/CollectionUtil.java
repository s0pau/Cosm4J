package com.cosm.client.requester;

import java.util.Collection;

public class CollectionUtil
{

	public static boolean equal(Collection<? extends Object> tags, Collection<? extends Object> tags2)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean memberEquals(Collection<? extends Object> tags, Collection<? extends Object> tags2)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public static <O extends Object> boolean nullCheckEquals(O one, O two)
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
