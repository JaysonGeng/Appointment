package com.example.appointment.page;
//消息列表的页面


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
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
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class TabThree extends Fragment {


    WebView webView;
    Context t;
    private long userid;
    boolean check = false;

    public TabThree(Context context) {
        super();
        t = context;

    }

    private ImApp app;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabthree, container, false);
    }

    @Override

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 数据保存在application中
        app = (ImApp) getActivity().getApplication();
        webView =getActivity().findViewById(R.id.web_tabthree);
        userid = app.getMyNumber();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://39.107.228.179:8080/Blog1_war/?userid=" + userid);

    }




    public enum ContentPage {
        Item1(0),
        Item2(1);

        public static final int SIZE = 2;
        private final int position;

        ContentPage(int pos) {
            position = pos;
        }

        public static ContentPage getPage(int position) {
            switch (position) {
                case 0:
                    return Item1;
                case 1:
                    return Item2;
                default:
                    return Item1;
            }
        }

        public int getPosition() {
            return position;
        }
    }
}
 