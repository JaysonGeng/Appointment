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

import com.bumptech.glide.Glide;
import com.example.appointment.Entity.User;
import com.example.appointment.R;
import com.example.appointment.View.MainActivity;
import com.example.appointment.View.UserDetailActivity;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUser;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView userIcon;
        TextView username;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            userIcon = view.findViewById(R.id.icon_image_view_user_item);
            username = view.findViewById(R.id.username_text_view_user_item);
        }
    }

    public UserAdapter(List<User> userList){
        mUser = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                User user = mUser.get(position);
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("username",user.getName());
                intent.putExtra("student_id",user.getStudentId());
                intent.putExtra("nickname",user.getNickname());
                intent.putExtra("sex",user.getSex());
                intent.putExtra("campus",user.getCampus());
                intent.putExtra("icon",user.getIcon());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        User user = mUser.get(position);
        holder.username.setText(user.getNickname());
        Glide.with(mContext).load(user.getIcon()).into(holder.userIcon);
    }

    @Override
    public int getItemCount(){
        return mUser.size();
    }

}
