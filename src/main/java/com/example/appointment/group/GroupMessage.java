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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GroupMessage extends AppCompatActivity {

	private Toolbar toolbar;
	private CollapsingToolbarLayout toolbarLayout;
	private TextView groupdescribe_text;
	private TextView groupname_text;
	private Button button;
	ImApp app;
	private String name;
	private long toAccount;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.groupmessage);
		toolbar = findViewById(R.id.toolbar_GroupMessage);
		toolbarLayout = findViewById(R.id.collapsing_toolbar_GroupMessage);
		groupdescribe_text=(TextView)findViewById(R.id.groupdescribe_text);
		groupname_text = findViewById(R.id.usernumber_text);
		button = findViewById(R.id.send_message_button_GroupMessage);
//		MainActivity.t=this;
		app = (ImApp) getApplication();

		//加载工具栏
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null){
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		Intent intent = getIntent();
		toAccount = intent.getLongExtra("account", 0);
		String newGroupListJson = app.getGroupListJson();
		Gson gson = new Gson();
		GroupList newList = gson.fromJson(newGroupListJson, GroupList.class);
		name=newList.get(toAccount).name;
		toolbarLayout.setTitle(name);
		groupname_text.setText(toAccount+"");
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
								Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
							}
						});
					} catch (IOException e) {
						ThreadUtils.runInUiThread(new Runnable() {
							public void run() {
								Toast.makeText(getApplicationContext(), "出现问题，请重试！", Toast.LENGTH_SHORT).show();
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

	//加载toolbar菜单文件
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.toolbar_general,menu);
		return true;
	}

	//设置工具栏按钮的点击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			case android.R.id.home:
				finish();
				break;
			default:
				break;
		}
		return true;
	}
}
