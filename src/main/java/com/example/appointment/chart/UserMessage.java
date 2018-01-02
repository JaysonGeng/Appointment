
//用户的信息界面

package com.example.appointment.chart;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointment.R;
import com.example.appointment.core.ImApp;
import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageType;
import com.example.appointment.message.ContactInfoList;
import com.example.appointment.message.ThreadUtils;
import com.example.appointment.page.Main2;
import com.google.gson.Gson;

import java.io.IOException;

public class UserMessage extends Activity {
	private LinearLayout usermain;
	private TextView username;
	private RelativeLayout usernumber_bar;
	private TextView usernumber_text;
	private RelativeLayout usersign_bar;
	private TextView usersign_text;
	private RelativeLayout other_bar;
	private TextView other_text;
	private Button button;
	long num;
	ImApp app;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usermessage);
		usermain=(LinearLayout)findViewById(R.id.usermain);
		username=(TextView)findViewById(R.id.username);
		usernumber_bar=(RelativeLayout)findViewById(R.id.usernumber_bar);
		usernumber_text=(TextView)findViewById(R.id.usernumber_text);
		usersign_bar=(RelativeLayout)findViewById(R.id.usersign_bar);
		usersign_text=(TextView)findViewById(R.id.usersign_text);
		other_bar=(RelativeLayout)findViewById(R.id.other_bar);
		button=(Button)findViewById(R.id.user_button);
//		MainActivity.t=this;
		Main2.on3=true;
		app = (ImApp) getApplication();
		
		Intent intent = getIntent();
		num = intent.getLongExtra("number", 0);
		String newBuddyListJson = app.getBuddyListJson();
		Gson gson = new Gson();
		ContactInfoList newList = gson.fromJson(newBuddyListJson, ContactInfoList.class);
		username.setText(newList.get(num).name);
		usernumber_text.setText(""+num);
		usersign_text.setText(newList.get(num).sign);
		
		//根据不同的状态显示按钮不同的文字
		if(num==app.getMyNumber())
		{	
			button.setText("修改签名");
	  		button.setOnClickListener(new OnClickListener() {

	  			@Override
	  			public void onClick(View v) {
					ThreadUtils.runInUiThread(new Runnable() {

						public void run() {	
							final EditText edt=new EditText(UserMessage.this);
							edt.setMinLines(1);
							new AlertDialog.Builder(UserMessage.this)
							.setTitle("请输入新的签名")
							.setIcon(R.mipmap.ic_launcher)
							.setView(edt)
							.setPositiveButton("确定",new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {

									ThreadUtils.runInSubThread(new Runnable() {

										public void run() {
											try {
												AMessage msg=new AMessage();
												msg.type= AMessageType.MSG_TYPE_USERSIGN;
												msg.from=app.getMyNumber();
												msg.content=edt.getText().toString();
												app.getMyConn().sendMessage(msg);
												ThreadUtils.runInUiThread(new Runnable() {
													public void run() {
														Toast.makeText(getApplicationContext(), "修改完成！", Toast.LENGTH_SHORT).show();
													}
												});
												finish();
											} catch (IOException e) {
												ThreadUtils.runInUiThread(new Runnable() {
													public void run() {
														Toast.makeText(getApplicationContext(), "您的网络连接出现问题！", Toast.LENGTH_SHORT).show();
													}
												});
											}
										}
									});
								}
							})
							.setNegativeButton("取消",null)
							.show();
						}
					});
	  			}
	  		});
		}
		else if(newList.get(num).groupInfo.contains(""+app.getMyNumber()))
		{
			button.setText("发送消息");
			
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(UserMessage.this,ChartActivity.class);
					// 将账号和个性签名带到下一个activity
					intent.putExtra("account", ""+num);
					intent.putExtra("nick", username.getText());
					UserMessage.this.startActivity(intent);
				}
			});
			
		}
		else
		{
			button.setText("加为好友");
			
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					final AMessage msg = new AMessage();
					final EditText edt=new EditText(UserMessage.this);
					edt.setMinLines(1);
					new AlertDialog.Builder(UserMessage.this)
					.setTitle("请输入分组信息")
					.setIcon(R.mipmap.ic_launcher)
					.setView(edt)
					.setPositiveButton("确定",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							msg.content=edt.getText().toString();
							msg.type = AMessageType.MSG_TYPE_ADDFRIEND;
							msg.from = app.getMyNumber();
							msg.fromName=app.getMyName()+"add";
							msg.fromAvatar=0;
							msg.to=num;
							ThreadUtils.runInSubThread(new Runnable() {
								public void run() {

							try {
								app.getMyConn().sendMessage(msg);

								ThreadUtils.runInUiThread(new Runnable() {
									public void run() {
										Toast.makeText(getApplicationContext(), "请求已发送", Toast.LENGTH_SHORT).show();
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
					})
					.show();
										
				}
			});
		}
	}
	protected void onDestroy() {
		super.onDestroy();
		Main2.on3=false;
	}
}
