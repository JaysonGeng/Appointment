
//���ڰ�java��ת��Ϊxml�ļ���json����������Ϣ�洢��ĸ��࣬�ǳ�����Ϣ����ĺ���


package com.example.appointment.message;


import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

public class ProtocalObj {
	public String toXml() {
		XStream x = new XStream();
		x.alias(this.getClass().getSimpleName(), this.getClass());
		return x.toXML(this);// ������ת����xml����
	}
	public Object fromXml(String xml) {
		XStream x = new XStream();
		x.alias(this.getClass().getSimpleName(), this.getClass());
		return x.fromXML(xml);
	}
	public String toJson() {
		Gson g = new Gson();
		return g.toJson(this);
	}
	public Object fromJson(String json) {
		Gson g = new Gson();
		return g.fromJson(json, this.getClass());
	}
}