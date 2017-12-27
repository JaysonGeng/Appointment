
//活动列表，存储各个活动的APlan

package com.example.appointment.message;



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
