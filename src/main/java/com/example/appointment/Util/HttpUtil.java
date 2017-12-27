package com.example.appointment.Util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    private static final String URL_ADDRESS = "http://www.baidu.com";

    public static void sendOkHttpRequest(String address,RequestBody requestBody,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL_ADDRESS+address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
