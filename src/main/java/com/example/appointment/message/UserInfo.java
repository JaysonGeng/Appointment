package com.example.appointment.message;


import com.example.appointment.R;

/**可以放在好友列表的用户类
 * Created by MichaelOD on 2017/12/23.
 */
public class UserInfo
{
    public String name=null;
    public String sign=null;
    public String studentId;
    public String sex;
    public String campus;
    public String mail_address;
    public int avatar=0;
    public long number=0;
    public String groupInfo=null;
    public String online=null;
    //构造方法把ContactInfo生成一个UserInfo
    public UserInfo(ContactInfo a,String inf) {
        super();
        this.name = a.name;
        this.sign = a.sign;
        this.number = a.number;
//        this.studentId=a.studentId;
//        this.sex=a.sex;
//        this.campus=a.campus;
//        this.mail_address=a.mail_address;

        if(a.avatar==0)
            this.avatar=R.mipmap.ic_launcher;
        else
            this.avatar = a.avatar;
        this.groupInfo =inf;
        this.online=a.online;

    }

}
