
//���Է��ں����б���û���

package com.example.appointment.message;


import com.example.appointment.R;

public class UserInfo
{
    public String name=null;
    public String sign=null;
    public int avatar=0;
    public long number=0;
    public String groupInfo=null;
    public String online=null;
    //���췽����ContactInfo����һ��UserInfo
    public UserInfo(ContactInfo a,String inf) {
        super();
        this.name = a.name;
        this.sign = a.sign;
        this.number = a.number;
        if(a.avatar==0)
//        	this.avatar= R.drawable.ic_launcher;
//        else
//        	this.avatar = a.avatar;
		this.groupInfo =inf;
		this.online=a.online;
         
    }
    
}
