
//描述活动信息的类

package com.example.appointment.message;



public class APlan extends ProtocalObj {
	public long number;			// 活动号码
	public String name = "";	// 活动名称
	public String position="";	//活动地点
	public String describe="";	//活动介绍
	public int avatar;			// 头像(未用)
	public String member="";	//参与成员的帐号
	public String time="";		//活动时间(本版本并不完善)
}
