
//活动列表，存储各个活动的APlan

package com.example.appointment.message;


/**
 * Created by MichaelOD on 2017/12/22.
 */
import java.util.ArrayList;
import java.util.List;

public class APlanList {
	public List<APlan> planList = new ArrayList<APlan>();

	//根据帐号查找活动
	public APlan get(long num)
	{
		for(APlan a:planList)
		{
			if(a.number==num)
			{
				return a;
			}
		}
		return null;
	}
}
