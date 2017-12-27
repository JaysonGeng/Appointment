
//传输信息的类型

package com.example.appointment.message;

/**
 * Created by MichaelOD on 2017/12/22.
 */

public class AMessageType {

	public static final String MSG_TYPE_ZHUCE = "zhuce";				// 注册
	public static final String MSG_TYPE_USERSIGN = "sign";				//用户修改签名
	public static final String MSG_TYPE_NEWGROUP = "newgroup";			//新建群组
	public static final String MSG_TYPE_NEWPLAN = "newplan";			//新建活动
	public static final String MSG_TYPE_LOGIN = "login";				// 登录
	public static final String MSG_TYPE_LOGOUT = "logout";				// 退出
	public static final String MSG_TYPE_CHAT_P2P = "chatp2p";			// 私聊
	public static final String MSG_TYPE_CHAT_ROOM = "chatroom";			// 群聊
	public static final String MSG_TYPE_ONLINE = "online";				// 上线
	public static final String MSG_TYPE_SUCCESS = "success";			//成功
	public static final String MSG_TYPE_BUDDYLIST = "buddylist";		// 好友列表
	public static final String MSG_TYPE_GROUPLIST = "grouplist";		//群组列表
	public static final String MSG_TYPE_PLANLIST = "planlist";			//活动列表
	public static final String MSG_TYPE_ADDFRIEND= "addfriend";			//添加好友
	public static final String MSG_TYPE_ADDGROUP= "addgroup";			//添加群组
	public static final String MSG_TYPE_ADDPLAN= "addplan";				//添加活动
	public static final String MSG_TYPE_PLANPOSITION= "planposition";	//修改活动地址
	public static final String MSG_TYPE_FAIL = "fail";					// 失败

}
