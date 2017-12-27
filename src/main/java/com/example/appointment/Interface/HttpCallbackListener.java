package com.example.appointment.Interface;

/**
 * Created by 李骏涛 on 2017/12/25.
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
