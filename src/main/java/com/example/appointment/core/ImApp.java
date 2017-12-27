
//����ĵĳ����ӣ������ͻ��˵ĺ������

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
	private AConnection myConn;// ������
	private long myNumber;// �û��ĵ�¼�˺�
	private String buddyListJson;// �����б��json��
	private String groupListJson;// Ⱥ���б��json��
	private String planListJson;// ��б��json��
	private String myname;
	private String mypassword;
	private List<AMessageList> list = new ArrayList<AMessageList>();
	private boolean online=true;

	//�����ӷ���
	public AConnection getMyConn() {
		return myConn;
	}

	public void setMyConn(AConnection myConn) {
		this.myConn = myConn;
	}

	//�ʺŷ���
	public long getMyNumber() {
		return myNumber;
	}

	public void setMyNumber(long myNumber) {
		this.myNumber = myNumber;
	}
		
	//���뷽��
	public String getMyPassword() {
		return mypassword;
	}

	public void setMyPassword(String Password) {
		this.mypassword = Password;
	}
	
	//��ȡ�Լ�������״̬
	public boolean getstate() {
		return online;
	}

	public void setstate(boolean h) {
		this.online = h;
	}

	//��ȡ�ǳ�
	public String getMyName() {
		return myname;
	}

	public void setMyName(String myName) {
		this.myname = myName;
	}
	
	//��ȡ�����б�
	public String getBuddyListJson() {
		return buddyListJson;
	}

	public void setBuddyListJson(String buddyListJson) {
		this.buddyListJson = buddyListJson;
	}
	
	//��ȡȺ���б�
	public String getGroupListJson() {
		return groupListJson;
	}

	public void setGroupListJson(String groupListJson) {
		this.groupListJson = groupListJson;
	}
	
	//��ȡ��б�
	public String getPlanListJson() {
		return planListJson;
	}

	public void setPlanListJson(String planListJson) {
		this.planListJson = planListJson;
	}

	//�����Ϣ����Ϣ�б�
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
			//������Ϣ�б���������������б����ͼӵ��ñ���
			//û�о��½�һ���б��Դ�ʵ��QQ����Ϣ�б��ܣ�ͬ����Ϣ�洢��һ��
			//Ⱥ��ͺ�����Ϣͬ��
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

	//�����һ���б�
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
	
	//��ȡ��Ϣ�б�
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
	
	//��ȡ�����б�
	public List<AMessageList> getList() {
		return list;
	}
	
	//��������б�
	public void clearList() {
		list.clear();
	}
}
