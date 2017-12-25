package com.example.appointment.Entity;


public class User {

    private String name;
    private int studentId;
    private String sex;
    private String campus;
    private String mail_address;
    private String nickname;
    private int icon;

    public User(String name,int studentId,String sex,String campus,String mail_address,String nickname,int icon){
        this.name = name;
        this.studentId = studentId;
        this.sex = sex;
        this.campus = campus;
        this.mail_address = mail_address;
        this.nickname = nickname;
        this.icon = icon;
    }

    public String getName(){
        return name;
    }


    public int getStudentId(){
        return studentId;
    }


    public String getSex(){
        return sex;
    }

    public String getCampus(){
        return campus;
    }

    public String getMail_address(){
        return mail_address;
    }

    public void setMail_address(String mail_address){
        this.mail_address = mail_address;
    }

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public int getIcon(){
        return icon;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

}
