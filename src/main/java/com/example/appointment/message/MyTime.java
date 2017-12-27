
//获取当前时间的类

package com.example.appointment.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by MichaelOD on 2017/12/22.
 */
public class MyTime {

	public static String geTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formate = new SimpleDateFormat("MM-dd HH:mm:ss");
		return formate.format(date);
	}

	public static String geTime(Long time) {
		Date date = new Date(time);
		SimpleDateFormat formate = new SimpleDateFormat("MM-dd HH:mm:ss");
		return formate.format(date);
	}

	public static Long geTime(String dateString) throws ParseException {
		SimpleDateFormat formate = new SimpleDateFormat("MM-dd HH:mm:ss");
		return formate.parse(dateString).getTime();
	}
}
