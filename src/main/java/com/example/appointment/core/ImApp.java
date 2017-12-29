
//程序的的长连接，整个客户端的核心组件

package com.example.appointment.core;

import android.app.Application;


import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageList;
import com.example.appointment.message.AMessageType;
import com.example.appointment.message.ContactInfo;
import com.example.appointment.message.ContactInfoList;
import com.example.appointment.message.GroupInfo;
import com.example.appointment.message.GroupList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ImApp extends Application {
	private AConnection myConn;// 长连接
	private long myNumber;// 用户的登录账号
	private String buddyListJson;// 好友列表的json串
	private String groupListJson;// 群组列表的json串
	private String planListJson;// 活动列表的json串
	private String myname;
	private String mypassword;
	private List<AMessageList> list = new ArrayList<AMessageList>();
	private boolean online=true;

	//长连接方法
	public AConnection getMyConn() {
		return myConn;
	}

	public void setMyConn(AConnection myConn) {
		this.myConn = myConn;
	}

	//帐号方法
	public long getMyNumber() {
		return myNumber;
	}

	public void setMyNumber(long myNumber) {
		this.myNumber = myNumber;
	}

	//密码方法
	public String getMyPassword() {
		return mypassword;
	}

	public void setMyPassword(String Password) {
		this.mypassword = Password;
	}

	//获取自己的连接状态
	public boolean getstate() {
		return online;
	}

	public void setstate(boolean h) {
		this.online = h;
	}

	//获取昵称
	public String getMyName() {
		return myname;
	}

	public void setMyName(String myName) {
		this.myname = myName;
	}

	//获取好友列表
	public String getBuddyListJson() {
		return buddyListJson;
	}

	public void setBuddyListJson(String buddyListJson) {
		this.buddyListJson = buddyListJson;
	}

	//获取群组列表
	public String getGroupListJson() {
		return groupListJson;
	}

	public void setGroupListJson(String groupListJson) {
		this.groupListJson = groupListJson;
	}

	//获取活动列表
	public String getPlanListJson() {
		return planListJson;
	}

	public void setPlanListJson(String planListJson) {
		this.planListJson = planListJson;
	}

	//添加消息到消息列表
	public void addMessage(AMessage message) {
		if(message.type.equals(AMessageType.MSG_TYPE_CHAT_P2P)){
			boolean check=false;
			long num;

			String name="";
			String newBuddyListJson = this.getBuddyListJson();
			Gson gson = new Gson();
			ContactInfoList newList = gson.fromJson(
					newBuddyListJson, ContactInfoList.class);
			if(message.to==myNumber)
				num=message.from;
			else
				num=message.to;
			for(ContactInfo a:newList.buddyList)
			{
				if(a.number==num)
				{
					name=a.name;
					break;
				}
			}

			for(AMessageList a:list)
			{
				//查找消息列表，如果存在这样的列表名就加到该表中
				//没有就新建一个列表，以此实现QQ的消息列表功能，同类消息存储在一起
				//群组和好友消息同理
				if(a.listname.equals(name))
				{
					check=true;
					a.messageList.add(message);
					break;
				}
			}
			if(!check)
			{
				AMessageList re=new AMessageList(name);
				re.messageList.add(message);
				list.add(re);
			}}
		else if(message.type.equals(AMessageType.MSG_TYPE_CHAT_ROOM)){
			boolean check=false;
			String name="";
			String newGroupListJson = this.getGroupListJson();
			Gson gson = new Gson();
			GroupList newList = gson.fromJson(
					newGroupListJson, GroupList.class);
			for(GroupInfo a:newList.groupList)
			{
				if(a.number==message.to)
				{
					name=a.name;
					break;
				}
			}
			for(AMessageList a:list)
			{
				if(a.listname.equals(name))
				{
					check=true;
					a.messageList.add(message);
					break;
				}
			}
			if(!check)
			{
				AMessageList re=new AMessageList(name);
				re.messageList.add(message);
				list.add(re);
			}}
		else if(message.type.equals(AMessageType.MSG_TYPE_ADDFRIEND)){
			boolean check=false;
			for(AMessageList a:list)
			{
				if(a.listname.equals(message.fromName))
				{
					check=true;
					break;
				}
			}
			if(!check)
			{
				AMessageList re=new AMessageList(message.fromName);
				re.messageList.add(message);
				list.add(re);
			}}
	}

	//清理掉一个列表
	public void clearMessage(String listname)
	{
		for(AMessageList a:list)
		{
			if(a.listname.equals(listname))
			{
				list.remove(a);
				break;
			}
		}
	}

	//获取消息列表
	public AMessageList getList(String name)
	{
		for(AMessageList r:list)
		{
			if(r.listname.equals(name))
			{
				return r;
			}
		}
		return null;
	}

	//获取整个列表集
	public List<AMessageList> getList() {
		return list;
	}

	//清空整个列表集
	public void clearList() {
		list.clear();
	}
}
