package com.example.appointment.group;

/**群组信息的类
 * Created by MichaelOD on 2017/12/26.
 */


import java.io.IOException;

import com.example.appointment.R;
import com.example.appointment.core.ImApp;
import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageType;
import com.example.appointment.message.GroupList;
import com.example.appointment.message.ThreadUtils;
import com.example.appointment.page.Main2;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GroupMessage extends Activity
{
	private LinearLayout groupmain;
	private TextView groupname;
	private RelativeLayout groupnumber_bar;
	private TextView groupnumber_text;
	private RelativeLayout groupdescribe_bar;
	private TextView groupdescribe_text;
	private RelativeLayout group_other_bar;
	private Button button;
	ImApp app;
	private String name;
	private long toAccount;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.groupmessage);
		groupmain=(LinearLayout)findViewById(R.id.groupmain);
		groupname=(TextView)findViewById(R.id.groupname);
		groupnumber_bar=(RelativeLayout)findViewById(R.id.groupnumber_bar);
		groupnumber_text=(TextView)findViewById(R.id.groupnumber_text);
		groupdescribe_bar=(RelativeLayout)findViewById(R.id.groupdescribe_bar);
		groupdescribe_text=(TextView)findViewById(R.id.groupdescribe_text);
		group_other_bar=(RelativeLayout)findViewById(R.id.group_other_bar);
		button=(Button)findViewById(R.id.group_button);
//		MainActivity.t=this;
		app = (ImApp) getApplication();
		
		Intent intent = getIntent();
		toAccount = intent.getLongExtra("account", 0);
		String newGroupListJson = app.getGroupListJson();
		Gson gson = new Gson();
		GroupList newList = gson.fromJson(newGroupListJson, GroupList.class);
		name=newList.get(toAccount).name;
		groupname.setText(name);
		groupnumber_text.setText(""+toAccount);
		groupdescribe_text.setText(newList.get(toAccount).describe);
		Main2.on3=true;
		
		//根据不同的状态显示按钮不同的文字
		if(newList.get(toAccount).member.contains(""+app.getMyNumber()))
		{
			button.setText("发送消息");
			
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(GroupMessage.this,
							GroupChart.class);
					// 将账号和个性签名带到下一个activity
					intent.putExtra("account", toAccount);
					intent.putExtra("nick", name);
					startActivity(intent);
				}
			});
			
		}
		else
		{
			button.setText("添加群组");
			
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final AMessage msg = new AMessage();
					msg.type = AMessageType.MSG_TYPE_ADDGROUP;
					msg.from = app.getMyNumber();
					msg.to=toAccount;
					ThreadUtils.runInSubThread(new Runnable() {
						public void run() {
					try {
						app.getMyConn().sendMessage(msg);
						ThreadUtils.runInUiThread(new Runnable() {
							public void run() {
								Toast.makeText(getApplicationContext(), "添加成功", 0).show();
							}
						});
					} catch (IOException e) {
						ThreadUtils.runInUiThread(new Runnable() {
							public void run() {
								Toast.makeText(getApplicationContext(), "出现问题，请重试！", 0).show();
							}
						});
					}
						}
					});
				}
			});

		}
	}
	protected void onDestroy() {
		super.onDestroy();
		Main2.on3=false;
	}
}
