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

/**消息列表的调试器
 * Created by MichaelOD on 2017/12/23.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private Context mContext;

    private List<AMessage> mMessageList;

    private ImApp app;

    private boolean check = false;

    //	传入上下文与集合
    public MessageListAdapter(List<AMessage> objects) {
        mMessageList = objects;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView title;
        TextView desc;
        TextView time;
        CardView cardView;

        private ViewHolder(View view){
            super(view);
            icon = view.findViewById(R.id.icon);
            title = view.findViewById(R.id.title);
            desc = view.findViewById(R.id.desc);
            time = view.findViewById(R.id.time);
            cardView = view.findViewById(R.id.CardView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_item_contact,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        Activity activity = (Activity) mContext;
        app = (ImApp) activity.getApplication();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                // 获得当前点击条目的信息.包含账号和昵称
                int position = holder.getAdapterPosition();
                final AMessage info = mMessageList.get(position+1);
                // 不能跟自己聊天
                // 将账号和个性签名带到下一个activity
                String name = "";
                long num;
                switch (info.type){
                    case AMessageType.MSG_TYPE_CHAT_P2P:
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
                        intent = new Intent(mContext, ChartActivity.class);
                        intent.putExtra("account", "" + num);
                        intent.putExtra("nick", name);
                        mContext.startActivity(intent);
                        break;
                    case AMessageType.MSG_TYPE_CHAT_ROOM:
                        num = info.to;
                        String newGroupListJson = app.getGroupListJson();
                        Gson gson = new Gson();
                        GroupList newList = gson.fromJson(
                                newGroupListJson, GroupList.class);
                        for (GroupInfo a : newList.groupList) {
                            if (a.number == info.to)
                                name = a.name;
                        }
                        intent = new Intent(mContext, GroupChart.class);
                        intent.putExtra("account", num);
                        intent.putExtra("nick", name);
                        mContext.startActivity(intent);
                        break;
                    case AMessageType.MSG_TYPE_ADDFRIEND:
                        ThreadUtils.runInUiThread(new Runnable() {
                            public void run() {
                                if (!check) {
                                    String y[] = info.fromName.split("add");
                                    new AlertDialog.Builder(mContext).setTitle("好友请求")
                                            .setMessage(y[0] + "请求添加您为好友")
                                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    final EditText edt = new EditText(mContext);
                                                    edt.setMinLines(1);
                                                    new AlertDialog.Builder(mContext)
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
                                                                                                           Toast.makeText(mContext, "很抱歉出现异常！", Toast.LENGTH_SHORT).show();
                                                                                                       }
                                                                                                   }
                                                                                               }
                                                                    );
                                                                    Toast.makeText(mContext, "确认成功！", Toast.LENGTH_SHORT).show();
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
                                                                Toast.makeText(mContext, "很抱歉出现异常！", Toast.LENGTH_SHORT).show();
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
                        break;
                    default:
                        break;
                }
            }
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        AMessage info = mMessageList.get(position);
        if (info.fromAvatar == 0) {
            // 默认头像
            holder.icon.setImageResource(R.mipmap.ic_launcher);
        } else {
            holder.icon.setImageResource(info.fromAvatar);
        }
        String name = "", content = "";
        switch (info.type){
            case AMessageType.MSG_TYPE_CHAT_P2P:
                if (info.fromName.equals(app.getMyName())) {
                    String newBuddyListJson = app.getBuddyListJson();
                    Gson gson = new Gson();
                    ContactInfoList newList = gson.fromJson(newBuddyListJson, ContactInfoList.class);
                    name = newList.get(info.to).name;
                } else
                    name = info.fromName;
                content = info.content;
                break;
            case AMessageType.MSG_TYPE_CHAT_ROOM:
                String newGroupListJson = app.getGroupListJson();
                Gson gson = new Gson();
                GroupList newList = gson.fromJson(newGroupListJson, GroupList.class);
                for (GroupInfo a : newList.groupList) {
                    if (a.number == info.to)
                        name = a.name;
                }
                content = info.content;
                break;
            case AMessageType.MSG_TYPE_ADDFRIEND:
                String y[] = info.fromName.split("add");
                name = y[0] + "请求添加您为好友";
                break;
            default:
                    break;
        }
        holder.title.setText(name);
        holder.desc.setText(content);
        holder.time.setText(info.sendTime);
    }

    @Override
    public int getItemCount(){
        return mMessageList.size();
    }

}
