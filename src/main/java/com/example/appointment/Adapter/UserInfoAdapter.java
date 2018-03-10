package com.example.appointment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appointment.chart.UserMessage;
import com.example.appointment.core.ImApp;

import java.util.ArrayList;
import java.util.HashMap;

/**用户信息适配器
 * Created by MichaelOD on 2017/12/23.
 */

public class UserInfoAdapter extends BaseExpandableListAdapter{

    private Context context=null;
    private ArrayList<HashMap<String,Object>> groupData=null;
    int groupLayout=0;
    private String[] groupFrom=null;
    private int[] groupTo=null;
    private ArrayList<ArrayList<HashMap<String,Object>>> childData=null;
    int childLayout=0;
    private String[] childFrom=null;
    private int[] childTo=null;
    ImApp app;

    public UserInfoAdapter(Context context, ArrayList<HashMap<String, Object>> groupData,
                           int groupLayout, String[] groupFrom, int[] groupTo,
                           ArrayList<ArrayList<HashMap<String, Object>>> childData, int childLayout,
                           String[] childFrom, int[] childTo,ImApp app) {
        super();
        this.context = context;
        this.groupData = groupData;
        this.groupLayout = groupLayout;
        this.groupFrom = groupFrom;
        this.groupTo = groupTo;
        this.childData = childData;
        this.childLayout = childLayout;
        this.childFrom = childFrom;
        this.childTo = childTo;
        this.app = app;
    }

    @Override
    public Object getChild(int arg0, int arg1) {

        return null;
    }

    //     position与id一样，都是从0开始计数的，
    //     	这里返回的id也是从0开始计数的
    @Override
    public long getChildId(int groupPosition, int childPosition) {

        long id=0;
        for(int i=0;i<groupPosition; i++){
            id+=childData.size();
        }
        id+=childPosition;
        return id;
    }

    //ChildViewHolder内部类
    class ChildViewHolder{
        ImageButton userImage=null;
        TextView userName=null;
        TextView userSign=null;
        TextView userState=null;
        TextView userNumber=null;
    }

    //头像点击事件监听类
    class ImageClickListener implements View.OnClickListener {

        ChildViewHolder holder=null;
        public ImageClickListener(ChildViewHolder holder){
            this.holder=holder;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,UserMessage.class);
            // 将账号和个性签名带到下一个activity
            intent.putExtra("number", Long.parseLong((String) holder.userNumber.getText()));
            context.startActivity(intent);
        }

    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ChildViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(childLayout,null);
            //感觉这里需要把root设置成ViewGroup 对象

            //  ERROR!!这里不能把null换成parent，否则会出现异常退出，原因不太确定，可能是inflate方法获得的这个item的View
            //  并不属于某个控件组，所以使用默认值null即可
            holder=new ChildViewHolder();
            holder.userImage=(ImageButton)convertView.findViewById(childTo[0]);
            holder.userName=(TextView)convertView.findViewById(childTo[1]);
            holder.userSign=(TextView)convertView.findViewById(childTo[2]);
            holder.userState=(TextView)convertView.findViewById(childTo[3]);
            holder.userNumber=(TextView)convertView.findViewById(childTo[4]);
            convertView.setTag(holder);
        }
        else{
            holder=(ChildViewHolder)convertView.getTag();
        }

        holder.userImage.setBackgroundResource((Integer)(childData.get(groupPosition).get(childPosition).get(childFrom[0])));
        holder.userName.setText(childData.get(groupPosition).get(childPosition).get(childFrom[1]).toString());
        holder.userSign.setText(childData.get(groupPosition).get(childPosition).get(childFrom[2]).toString());
        holder.userState.setText(childData.get(groupPosition).get(childPosition).get(childFrom[3]).toString());
        holder.userNumber.setText(childData.get(groupPosition).get(childPosition).get(childFrom[4]).toString());
        holder.userImage.setOnClickListener(new ImageClickListener(holder));

        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {

        return childData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return null;
    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }


    class GroupViewHolder{
        ImageView image=null;
        TextView groupName=null;
        TextView childCount=null;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupViewHolder holder=null;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(groupLayout, null);
            holder=new GroupViewHolder();
            holder.image=(ImageView)convertView.findViewById(groupTo[0]);
            holder.groupName=(TextView)convertView.findViewById(groupTo[1]);
            holder.childCount=(TextView)convertView.findViewById(groupTo[2]);
            convertView.setTag(holder);
        }
        else{
            holder=(GroupViewHolder)convertView.getTag();
        }

        int[] groupIndicator=(int[])groupData.get(groupPosition).get(groupFrom[0]);
        holder.image.setBackgroundResource(groupIndicator[isExpanded?1:0]);
        holder.groupName.setText(groupData.get(groupPosition).get(groupFrom[1]).toString());
        holder.childCount.setText(groupData.get(groupPosition).get(groupFrom[2]).toString());

        return convertView;
        //不要在适配器中调用适配器的内部方法，不然会出现奇怪的异常
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
