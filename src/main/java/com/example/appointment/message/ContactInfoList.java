
//存储用户的类集合

package com.example.appointment.message;


import java.util.ArrayList;
import java.util.List;
/**
 * Created by MichaelOD on 2017/12/22.
 */
public class ContactInfoList {
	public List<ContactInfo> buddyList = new ArrayList<ContactInfo>();

	//根据帐号查找用户
	public ContactInfo get(long num)
	{
		for(ContactInfo a:buddyList)
		{
			if(a.number==num)
			{
				return a;
			}
		}
		return null;
	}
}
