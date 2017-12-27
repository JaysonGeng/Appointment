
//存储传输的信息的类

package com.example.appointment.message;


public class AMessage extends ProtocalObj 
{
	public String type = AMessageType.MSG_TYPE_CHAT_P2P;// 信息类型
	public long from = 0;// 发送者 帐号
	public String fromName = "";// 昵称
	public int fromAvatar = 1;// 头像(本版本暂未用到)
	public long to = 0; // 接收者 帐号
	public String content = ""; // 消息的内容
	public String sendTime = MyTime.geTime(); // 发送时间
	public String emoji="";
}
