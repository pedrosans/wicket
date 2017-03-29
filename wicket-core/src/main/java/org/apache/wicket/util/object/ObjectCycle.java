package org.apache.wicket.util.object;

import org.apache.wicket.model.IDetachable;

public class ObjectCycle
{

	public static void detach(Object detachable)
	{
		if (detachable instanceof IDetachable)
		{
			((IDetachable)detachable).detach();
		}
	}
}
