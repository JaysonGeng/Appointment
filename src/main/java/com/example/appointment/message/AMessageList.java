
//�洢�������Ϣ�б����

package com.example.appointment.message;


import java.util.ArrayList;
import java.util.List;

public class AMessageList {

	public List<AMessage> messageList;
	public String listname="";//�б�����
	
	public AMessageList(String name)
	{
		messageList = new ArrayList<AMessage>();
		listname=name;
	}
	
	//ȡ�б�Ķ���Ԫ��
	public AMessage getTop()
	{
		int j=messageList.size();
		if(j>0)
			return messageList.get(messageList.size()-1);
		else
			return null;
	}
}
