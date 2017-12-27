
//群组列表，存储各个群组的Group

package com.example.appointment.message;



import java.util.ArrayList;
import java.util.List;
/**
 * Created by MichaelOD on 2017/12/22.
 */
public class GroupList {
	public List<GroupInfo> groupList = new ArrayList<GroupInfo>();
	
	//根据帐号查找用户
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
