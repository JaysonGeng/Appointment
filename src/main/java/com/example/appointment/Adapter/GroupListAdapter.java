package com.example.appointment.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appointment.R;
import com.example.appointment.core.ImApp;
import com.example.appointment.message.GroupInfo;

import java.util.List;

/**
 * Created by MichaelOD on 2017/12/23.
 */

public class GroupListAdapter extends ArrayAdapter<GroupInfo> {

    ImApp app;

    //	 传入上下文与集合
    public GroupListAdapter(Context context, List<GroupInfo> objects, ImApp app) {
        super(context, 0, objects);
        this.app=app;
    }

    class ViewHolder {
        ImageView icon;
        TextView title;
        TextView desc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupInfo info = getItem(position);// 从集合中取得数据
        // listView的优化
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(),
                    R.layout.view_item_contact, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 赋值
        if (info.avatar == 0) {
            // 默认头像
            holder.icon.setImageResource(R.mipmap.ic_launcher);
        } else {
            holder.icon.setImageResource(info.avatar);
        }

        holder.title.setText(info.name + "");
        holder.desc.setText(info.describe);

        return convertView;
    }
}
