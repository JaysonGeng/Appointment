package com.example.appointment.page;
//消息列表的页面


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class TabOne extends Fragment {
    Context t;
    boolean check = false;

    public TabOne(Context context) {
        super();
        t = context;

    }

    //listView的使用分三步，获得listView容器，封装集合数据，写数据适配器加载listView条目的布局
    ListView listView;
    // listView的集合
    private List<AMessage> infos = new ArrayList<AMessage>();
    private ImApp app;
    private MessageListAdapter adapter;
    AMessage info;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabone, container, false);
    }

    @Override

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) getActivity().findViewById(R.id.listview);

        // 数据保存在application中
        app = (ImApp) getActivity().getApplication();

        for (AMessageList a : app.getList())
            infos.add(a.getTop());
        // buddyList是一个集合，把buddyList集合里的东西全部添加进infos
        adapter = new MessageListAdapter(getActivity(), infos, app);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 获得当前点击条目的信息.包含账号和昵称
                info = infos.get(position);
                // 不能跟自己聊天

                // 将账号和个性签名带到下一个activity
                String name = "";
                long num = 0;
                if (info.type.equals(AMessageType.MSG_TYPE_CHAT_P2P)) {
                    if (info.from == app.getMyNumber())
                        num = info.to;
                    else
                        num = info.from;
                    if (info.fromName.equals(app.getMyName())) {
                        String newBuddyListJson = app.getBuddyListJson();
                        Gson gson = new Gson();
                        ContactInfoList newList = gson.fromJson(
                                newBuddyListJson, ContactInfoList.class);
                        name = newList.get(info.to).name;

                    } else
                        name = info.fromName;
                    Intent intent = new Intent(t,
                            ChartActivity.class);
                    intent.putExtra("account", "" + num);
                    intent.putExtra("nick", name);
                    startActivity(intent);
                } else if (info.type.equals(AMessageType.MSG_TYPE_CHAT_ROOM)) {
                    num = info.to;
                    String newGroupListJson = app.getGroupListJson();
                    Gson gson = new Gson();
                    GroupList newList = gson.fromJson(
                            newGroupListJson, GroupList.class);
                    for (GroupInfo a : newList.groupList) {
                        if (a.number == info.to)
                            name = a.name;
                    }

                    Intent intent = new Intent(t,
                            GroupChart.class);
                    intent.putExtra("account", num);
                    intent.putExtra("nick", name);
                    startActivity(intent);
                } else if (info.type.equals(AMessageType.MSG_TYPE_ADDFRIEND)) {

                    ThreadUtils.runInUiThread(new Runnable() {

                        public void run() {
                            if (!check) {
                                String y[] = info.fromName.split("add");
                                new AlertDialog.Builder(t).setTitle("好友请求")
                                        .setMessage(y[0] + "请求添加您为好友")
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                final EditText edt = new EditText(t);
                                                edt.setMinLines(1);
                                                new AlertDialog.Builder(t)
                                                        .setTitle("请输入分组信息")
                                                        .setIcon(R.mipmap.ic_launcher)
                                                        .setView(edt)
                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                info.content = info.from + "/" + info.content + "#" + info.to + "/" + edt.getText().toString();
                                                                ThreadUtils.runInSubThread(new Runnable() {

                                                                                               public void run() {
                                                                                                   try {
                                                                                                       app.getMyConn().sendMessage(info);
                                                                                                   } catch (IOException e) {
                                                                                                       Toast.makeText(t, "很抱歉出现异常！", Toast.LENGTH_SHORT).show();
                                                                                                   }
                                                                                               }
                                                                                           }
                                                                );
                                                                Toast.makeText(t, "确认成功！", Toast.LENGTH_SHORT).show();
                                                                app.clearMessage(info.fromName);
                                                            }
                                                        })
                                                        .show();
                                            }
                                        })

                                        .setNegativeButton("忽略", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                ThreadUtils.runInSubThread(new Runnable() {

                                                    public void run() {
                                                        try {
                                                            info.content = "refuse";
                                                            app.getMyConn().sendMessage(info);
                                                        } catch (IOException e) {
                                                            Toast.makeText(t, "很抱歉出现异常！", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                app.clearMessage(info.fromName);
                                            }
                                        })
                                        .show();
                                check = true;
                            }
                        }
                    });
                }

            }
        });

    }
}
 