
//�߳��л����ߣ������л�UI�̺߳������߳�

package com.example.appointment.message;


import android.os.Handler;

public class ThreadUtils {

	//�����߳�
	public static void runInSubThread(Runnable r) {
		new Thread(r).start();
	}

	private static Handler handler = new Handler();

	//UI�߳�
	public static void runInUiThread(Runnable r) {
		handler.post(r);
	}
}
