package com.example.appointment.page;
//消息列表的页面

import java.util.ArrayList;
import java.util.List;

import com.example.appointment.Adapter.MessageListAdapter;
import com.example.appointment.R;
import com.example.appointment.core.ImApp;
import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("ValidFragment")
public class TabOne extends Fragment {
    Context t;

    public TabOne(Context context) {
        super();
        t = context;
    }

    //listView的使用分三步，获得listView容器，封装集合数据，写数据适配器加载listView条目的布局
    RecyclerView listView;
    // listView的集合
    private List<AMessage> infos = new ArrayList<>();
    private ImApp app;
    private MessageListAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabone, container, false);
    }

    @Override

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = getActivity().findViewById(R.id.listview);

        ImageView noMessage = view.findViewById(R.id.no_message);

        // 数据保存在application中
        app = (ImApp) getActivity().getApplication();

        for (AMessageList a : app.getList())
            infos.add(a.getTop());
        // buddyList是一个集合，把buddyList集合里的东西全部添加进infos
        if(infos.size()!=0){
            noMessage.setVisibility(View.GONE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);
        adapter = new MessageListAdapter(infos);
        listView.setAdapter(adapter);


    }
}
