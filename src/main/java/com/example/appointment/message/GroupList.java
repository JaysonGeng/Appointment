
//Ⱥ���б��洢����Ⱥ���Group

package com.example.appointment.message;



import java.util.ArrayList;
import java.util.List;

public class GroupList {
	public List<GroupInfo> groupList = new ArrayList<GroupInfo>();
	
	//�����ʺŲ����û�
	public GroupInfo get(long num)
	{
		for(GroupInfo a:groupList)
		{
			if(a.number==num)
			{
				return a;
			}
		}
		return null;
	}
}
