
//�洢�û����༯��

package com.example.appointment.message;


import java.util.ArrayList;
import java.util.List;

public class ContactInfoList {
	public List<ContactInfo> buddyList = new ArrayList<ContactInfo>();

	//�����ʺŲ����û�
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
