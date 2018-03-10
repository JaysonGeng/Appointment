package com.example.appointment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appointment.R;
import com.example.appointment.group.GroupMessage;
import com.example.appointment.message.GroupInfo;

import java.util.List;

/**
 * 群组列表的调试器
 * Created by MichaelOD on 2017/12/23.
 */

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    private List<GroupInfo> mGroupList;

    private Context mContext;

    //	 传入上下文与集合
    public GroupListAdapter(List<GroupInfo> objects) {
        mGroupList = objects;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView desc;
        CardView cardView;

        private ViewHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.icon);
            title = view.findViewById(R.id.title);
            desc = view.findViewById(R.id.desc);
            cardView = view.findViewById(R.id.CardView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_item_contact, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                // 获得当前点击条目的信息.包含账号和昵称
                GroupInfo info = mGroupList.get(position);
                // 不能跟自己聊天
                Intent intent = new Intent(mContext, GroupMessage.class);
                // 将账号和个性签名带到下一个activity
                intent.putExtra("account", info.number);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GroupInfo info = mGroupList.get(position);
        holder.title.setText(info.name);
        // 赋值

        holder.icon.setImageResource(R.drawable.touxiang_send);

        holder.desc.setText(info.describe);
    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }


}
