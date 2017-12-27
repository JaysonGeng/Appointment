
//存储传输的信息列表的类

package com.example.appointment.message;


import java.util.ArrayList;
import java.util.List;
/**
 * Created by MichaelOD on 2017/12/22.
 */
public class AMessageList {

	public List<AMessage> messageList;
	public String listname="";//列表名称
	
	public AMessageList(String name)
	{
		messageList = new ArrayList<AMessage>();
		listname=name;
	}
	
	//取列表的顶部元素
	public AMessage getTop()
	{
		int j=messageList.size();
		if(j>0)
			return messageList.get(messageList.size()-1);
		else
			return null;
	}
}
