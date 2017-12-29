package com.example.appointment.page;
//消息列表的页面


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appointment.Adapter.MessageListAdapter;
import com.example.appointment.R;
import com.example.appointment.chart.ChartActivity;
import com.example.appointment.core.ImApp;
import com.example.appointment.group.GroupChart;
import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageList;
import com.example.appointment.message.AMessageType;
import com.example.appointment.message.ContactInfoList;
import com.example.appointment.message.GroupInfo;
import com.example.appointment.message.GroupList;
import com.example.appointment.message.ThreadUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class TabThree extends Fragment {
    Context t;
    boolean check = false;

    public TabThree(Context context) {
        super();
        t = context;

    }

    //listView的使用分三步，获得listView容器，封装集合数据，写数据适配器加载listView条目的布局
    ListView listView;
    WebView webView;
    // listView的集合
    private List<AMessage> infos = new ArrayList<AMessage>();
    private ImApp app;
    private MessageListAdapter adapter;
    AMessage info;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabthree, container, false);
    }

    @Override

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = (WebView) getActivity().findViewById(R.id.web_view);
        // 数据保存在application中
        app = (ImApp) getActivity().getApplication();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.baidu.com");

    }
}
 