
//��б��洢�������APlan

package com.example.appointment.message;



import java.util.ArrayList;
import java.util.List;

public class APlanList {
	public List<APlan> planList = new ArrayList<APlan>();

	//�����ʺŲ��һ
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
