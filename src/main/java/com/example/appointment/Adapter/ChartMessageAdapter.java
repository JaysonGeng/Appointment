package com.example.appointment.Adapter;



import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appointment.R;
import com.example.appointment.core.ImApp;
import com.example.appointment.message.AMessage;

import java.util.List;

/**聊天信息的调试器
 * Created by MichaelOD on 2017/12/23.
 */

public class ChartMessageAdapter extends ArrayAdapter<AMessage> {
    ImApp app;

    public ChartMessageAdapter(Context context, List<AMessage> objects) {
        super(context, 0, objects);
        Activity activity = (Activity) context;
        app = (ImApp) activity.getApplication();
    }

    //*根据集合中的position位置，返回不同的值，代表不同的布局 0代表自己发送的消息 1代表接收到的消息
    @Override
    public int getItemViewType(int position) {// 这个方法是给getView用得
        AMessage msg = getItem(position);
        // 消息来自谁，如果消息来自我自己，说明是我发送的
        if (msg.from == app.getMyNumber()) {
            // 我自己的消息，发送
            if (msg.emoji.equals("")) {
                return 0;
            } else {
                return 2;
            }
        } else {
            if (msg.emoji.equals("")) {
                return 1;
            } else {
                return 3;
            }
        }
    }

    //	两种布局
    @Override
    public int getViewTypeCount() {

        return 4;
    }

    class ViewHolder {
        TextView time;
        TextView name;
        TextView content;
        ImageView head;
        ImageView emoji;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        if (0 == type) {
            // 发送的布局
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(),
                        R.layout.item_chat_send, null);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.name = (TextView) convertView
                        .findViewById(R.id.name);
                holder.content = (TextView) convertView
                        .findViewById(R.id.content);
                holder.head = (ImageView) convertView.findViewById(R.id.head);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 设置值
            AMessage msg = getItem(position);
            holder.time.setText(msg.sendTime);
            holder.name.setText(msg.fromName);
            holder.head.setImageResource(msg.fromAvatar);
            holder.content.setText(msg.content);
            return convertView;

        }
        if (2 == type) {
            // 发送的布局
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(),
                        R.layout.item_chat_sendemoji, null);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.name = (TextView) convertView
                        .findViewById(R.id.name);
                holder.emoji = (ImageView) convertView
                        .findViewById(R.id.emoji);
                holder.head = (ImageView) convertView.findViewById(R.id.head);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 设置值
            AMessage msg = getItem(position);
            holder.time.setText(msg.sendTime);
            holder.name.setText(msg.fromName);
            holder.head.setImageResource(msg.fromAvatar);

            int emojinum = 0;
            if (msg.emoji.equals("e01")) {
                emojinum = 1;
            }
            if (msg.emoji.equals("e02")) {
                emojinum = 2;
            }
            if (msg.emoji.equals("e03")) {
                emojinum = 3;
            }
            if (msg.emoji.equals("e04")) {
                emojinum = 4;
            }
            if (msg.emoji.equals("e05")) {
                emojinum = 5;
            }
            switch (emojinum) {
                case 1:
                    holder.emoji.setImageResource(R.drawable.e01);
                    break;
                case 2:
                    holder.emoji.setImageResource(R.drawable.e02);
                    break;
                case 3:
                    holder.emoji.setImageResource(R.drawable.e03);
                    break;
                case 4:
                    holder.emoji.setImageResource(R.drawable.e04);
                    break;
                case 5:
                    holder.emoji.setImageResource(R.drawable.e05);
                    break;
                default:
                    break;
            }
            return convertView;

        }
        if (type == 1) {
            // 接收的布局
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(),
                        R.layout.item_chat_receive, null);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.content = (TextView) convertView
                        .findViewById(R.id.content);
                holder.name = (TextView) convertView
                        .findViewById(R.id.name);
                holder.head = (ImageView) convertView.findViewById(R.id.head);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 设置值
            AMessage msg = getItem(position);
            holder.head.setImageResource(msg.fromAvatar);
            holder.time.setText(msg.sendTime);
            holder.name.setText(msg.fromName);
            holder.content.setText(msg.content);

            return convertView;
        } else {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(),
                        R.layout.item_chat_receiveemoji, null);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.emoji = (ImageView) convertView
                        .findViewById(R.id.emoji);
                holder.name = (TextView) convertView
                        .findViewById(R.id.name);
                holder.head = (ImageView) convertView.findViewById(R.id.head);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 设置值
            AMessage msg = getItem(position);
            holder.head.setImageResource(msg.fromAvatar);
            holder.time.setText(msg.sendTime);
            holder.name.setText(msg.fromName);

            int emojinum = 0;
            if (msg.emoji.equals("e01")) {
                emojinum = 1;
            }
            if (msg.emoji.equals("e02")) {
                emojinum = 2;
            }
            if (msg.emoji.equals("e03")) {
                emojinum = 3;
            }
            if (msg.emoji.equals("e04")) {
                emojinum = 4;
            }
            if (msg.emoji.equals("e05")) {
                emojinum = 5;
            }
            switch (emojinum) {
                case 1:
                    holder.emoji.setImageResource(R.drawable.e01);
                    break;
                case 2:
                    holder.emoji.setImageResource(R.drawable.e02);
                    break;
                case 3:
                    holder.emoji.setImageResource(R.drawable.e03);
                    break;
                case 4:
                    holder.emoji.setImageResource(R.drawable.e04);
                    break;
                case 5:
                    holder.emoji.setImageResource(R.drawable.e05);
                    break;
                default:
                    break;
            }

            return convertView;

        }

    }
}
