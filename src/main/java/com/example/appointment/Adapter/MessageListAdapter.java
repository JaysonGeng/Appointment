package com.example.appointment.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointment.R;
import com.example.appointment.chart.ChartActivity;
import com.example.appointment.core.ImApp;
import com.example.appointment.group.GroupChart;
import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageType;
import com.example.appointment.message.ContactInfoList;
import com.example.appointment.message.GroupInfo;
import com.example.appointment.message.GroupList;
import com.example.appointment.message.ThreadUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

/**
 * 消息列表的调试器
 * Created by MichaelOD on 2017/12/23.
 */

public class MessageListAdapter extends ArrayAdapter<AMessage> {
    ImApp app;

    //	传入上下文与集合
    public MessageListAdapter(Context context, List<AMessage> objects, ImApp app) {
        super(context, 0, objects);
        this.app = app;
    }

    class ViewHolder {
        ImageView icon;
        TextView title;
        TextView desc;
        TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AMessage info = getItem(position);// 从集合中取得数据
        // listView的优化
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(),
                    R.layout.view_item_contact, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        // 默认头像
        holder.icon.setImageResource(R.drawable.touxiang_send);

        // 如果是自己登录的账号，则显示自己，否则显示账号
        // 获得保存在application中的自己登录的账号
        String name = "", content = "";
        if (info.type.equals(AMessageType.MSG_TYPE_CHAT_P2P)) {
            if (info.fromName.equals(app.getMyName())) {
                String newBuddyListJson = app.getBuddyListJson();
                Gson gson = new Gson();
                ContactInfoList newList = gson.fromJson(
                        newBuddyListJson, ContactInfoList.class);
                name = newList.get(info.to).name;
                content = info.content;
            } else
                name = info.fromName;
            content = info.content;
        } else if (info.type.equals(AMessageType.MSG_TYPE_CHAT_ROOM)) {
            String newGroupListJson = app.getGroupListJson();
            Gson gson = new Gson();
            GroupList newList = gson.fromJson(
                    newGroupListJson, GroupList.class);
            for (GroupInfo a : newList.groupList) {
                if (a.number == info.to)
                    name = a.name;
            }
            content = info.content;
        }
        //好友消息
        else if (info.type.equals(AMessageType.MSG_TYPE_ADDFRIEND)) {
            String y[] = info.fromName.split("add");
            name = y[0] + "请求添加您为好友";
        }

        holder.title.setText("" + name);

        holder.desc.setText(content);
        holder.time.setText(info.sendTime);
        return convertView;
    }
}
