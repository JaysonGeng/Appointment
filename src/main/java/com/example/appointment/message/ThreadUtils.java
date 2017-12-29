package com.example.appointment.message;


import android.os.Handler;

/**线程切换工具，用于切换UI线程和网络线程
 * Created by MichaelOD on 2017/12/23.
 */
public class ThreadUtils {

	//网络线程
	public static void runInSubThread(Runnable r) {
		new Thread(r).start();
	}

	private static Handler handler = new Handler();

	//UI线程
	public static void runInUiThread(Runnable r) {
		handler.post(r);
	}
}
