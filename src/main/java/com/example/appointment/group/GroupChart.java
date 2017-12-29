package com.example.appointment.group;

/**群组聊天的类
 * Created by MichaelOD on 2017/12/26.
 */


import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointment.Adapter.ChartMessageAdapter;
import com.example.appointment.R;
import com.example.appointment.core.AConnection;
import com.example.appointment.core.ImApp;
import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageList;
import com.example.appointment.message.AMessageType;
import com.example.appointment.message.ThreadUtils;
import com.example.appointment.page.Main2;

public class GroupChart extends Activity {

	private TextView title;
	private ListView listView;
	private EditText input;
	private ImApp app;
	private Button send;
	private ImageView emoji1;
	private ImageView emoji2;
	private ImageView emoji3;
	private ImageView emoji4;
	private ImageView emoji5;
	private ChartMessageAdapter adapter;
	// 这是点击的用户，也就是你要发消息给谁
	private String toNick;// 要发送给谁
	private long toAccount;// 要发送的账号
	private long fromAccount;// 我的账号，我要跟谁聊天
	private String inputStr;// 聊天内容
	AMessageList j;
	public static FragmentActivity u;
	// 聊天消息的集合

	// 点击发送按钮的时候，获得输入框中的内容，将内容封装到messages集合中，聊调消息是一个QQMessageJava对象,一定包含四个字段
	public void send(View view) {
		inputStr = input.getText().toString().trim();
		// 清空输入框
		input.setText("");
		final AMessage msg = new AMessage();
		if ("".equals(inputStr)) {
			Toast.makeText(getApplicationContext(), "不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		msg.type = AMessageType.MSG_TYPE_CHAT_ROOM;
		msg.from = fromAccount;
		msg.fromName = app.getMyName()+"("+app.getMyNumber()+")";
		msg.to = toAccount;
		msg.content = inputStr;
		msg.fromAvatar = R.mipmap.ic_launcher;

		j.messageList.add(msg);
		// 数据集合有了，创建适配器
		// 刷新消息
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

		// 展示到最新发送的消息处
		if (j.messageList.size() > 0) {
			listView.setSelection(j.messageList.size() - 1);
		}

		// 发送消息到服务器--子线程
		ThreadUtils.runInSubThread(new Runnable() {

			public void run() {
				try {
					app.getMyConn().sendMessage(msg);
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

	//接收消息，使用监听器
	private AConnection.OnMessageListener listener = new AConnection.OnMessageListener() {

		public void onReveive(final AMessage msg) {
			// 注意onReveive是子线程，更新界面一定要在主线程中
			ThreadUtils.runInUiThread(new Runnable() {

				public void run() {

					// 服务器返回回来的消息
					if (AMessageType.MSG_TYPE_CHAT_ROOM.equals(msg.type)) {
						j.messageList.add(msg);// 把消息加到消息集合中，这是最新的消息
						// 刷新消息
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						}
						// 展示到最新发送的消息出
						if (j.messageList.size() > 0) {
							listView.setSelection(j.messageList.size() - 1);
						}

					}

				}
			});

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.groupchart);
//		MainActivity.t=this;

		title=(TextView)findViewById(R.id.title2);
		listView=(ListView)findViewById(R.id.listview2);
		input=(EditText)findViewById(R.id.input2);
		send=(Button)findViewById(R.id.send2);
		emoji1=(ImageView)findViewById(R.id.emoji1);
		emoji2=(ImageView)findViewById(R.id.emoji2);
		emoji3=(ImageView)findViewById(R.id.emoji3);
		emoji4=(ImageView)findViewById(R.id.emoji4);
		emoji5=(ImageView)findViewById(R.id.emoji5);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				send(v);
			}
		});
		emoji1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final AMessage msg = new AMessage();
				

				msg.type = AMessageType.MSG_TYPE_CHAT_ROOM;
				msg.from = fromAccount;
				msg.fromName = app.getMyName()+"("+app.getMyNumber()+")";
				msg.content="[Emoji]";
				msg.to = toAccount;
				msg.emoji = "e01";
				msg.fromAvatar = R.mipmap.ic_launcher;

				j.messageList.add(msg);
				// 数据集合有了，创建适配器
				// 刷新消息
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}

				// 展示到最新发送的消息处
				if (j.messageList.size() > 0) {
					listView.setSelection(j.messageList.size() - 1);
				}

				// 发送消息到服务器--子线程
				ThreadUtils.runInSubThread(new Runnable() {

					public void run() {
						try {
							app.getMyConn().sendMessage(msg);
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
		});
emoji2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final AMessage msg = new AMessage();
				

				msg.type = AMessageType.MSG_TYPE_CHAT_ROOM;
				msg.from = fromAccount;
				msg.content="[Emoji]";
				msg.fromName = app.getMyName()+"("+app.getMyNumber()+")";
				msg.to = toAccount;
				msg.emoji = "e02";
				msg.fromAvatar = R.mipmap.ic_launcher;

				j.messageList.add(msg);
				// 数据集合有了，创建适配器
				// 刷新消息
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}

				// 展示到最新发送的消息处
				if (j.messageList.size() > 0) {
					listView.setSelection(j.messageList.size() - 1);
				}

				// 发送消息到服务器--子线程
				ThreadUtils.runInSubThread(new Runnable() {

					public void run() {
						try {
							app.getMyConn().sendMessage(msg);
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
		});
emoji3.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		final AMessage msg = new AMessage();
		

		msg.type = AMessageType.MSG_TYPE_CHAT_ROOM;
		msg.from = fromAccount;
		msg.fromName = app.getMyName()+"("+app.getMyNumber()+")";
		msg.content="[Emoji]";
		msg.to = toAccount;
		msg.emoji = "e03";
		msg.fromAvatar = R.mipmap.ic_launcher;

		j.messageList.add(msg);
		// 数据集合有了，创建适配器
		// 刷新消息
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

		// 展示到最新发送的消息处
		if (j.messageList.size() > 0) {
			listView.setSelection(j.messageList.size() - 1);
		}

		// 发送消息到服务器--子线程
		ThreadUtils.runInSubThread(new Runnable() {

			public void run() {
				try {
					app.getMyConn().sendMessage(msg);
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
});
emoji4.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		final AMessage msg = new AMessage();
		

		msg.type = AMessageType.MSG_TYPE_CHAT_ROOM;
		msg.from = fromAccount;
		msg.fromName = app.getMyName()+"("+app.getMyNumber()+")";
		msg.content="[:DDDDDDD]";
		msg.to = toAccount;
		msg.emoji = "e04";
		msg.fromAvatar = R.mipmap.ic_launcher;

		j.messageList.add(msg);
		// 数据集合有了，创建适配器
		// 刷新消息
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

		// 展示到最新发送的消息处
		if (j.messageList.size() > 0) {
			listView.setSelection(j.messageList.size() - 1);
		}

		// 发送消息到服务器--子线程
		ThreadUtils.runInSubThread(new Runnable() {

			public void run() {
				try {
					app.getMyConn().sendMessage(msg);
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
});
emoji5.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		final AMessage msg = new AMessage();
		

		msg.type = AMessageType.MSG_TYPE_CHAT_ROOM;
		msg.from = fromAccount;
		msg.fromName = app.getMyName()+"("+app.getMyNumber()+")";
		msg.content="[董叔的微笑]";
		msg.to = toAccount;
		msg.emoji = "e05";
		msg.fromAvatar = R.mipmap.ic_launcher;

		j.messageList.add(msg);
		// 数据集合有了，创建适配器
		// 刷新消息
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

		// 展示到最新发送的消息处
		if (j.messageList.size() > 0) {
			listView.setSelection(j.messageList.size() - 1);
		}

		// 发送消息到服务器--子线程
		ThreadUtils.runInSubThread(new Runnable() {

			public void run() {
				try {
					app.getMyConn().sendMessage(msg);
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
});
		Main2.on2=true;
		app = (ImApp) getApplication();
		// 开启监听
		app.getMyConn().addOnMessageListener(listener);
		// 聊天的界面是复杂的listView。发送消息的条目是item_chat_send.xml布局，接收到的消息现实的条目是item_chat_receive.xml布局
		// 获得从上一个界面获取的账号与昵称
		Intent intent = getIntent();
		toNick = intent.getStringExtra("nick");
		toAccount = intent.getLongExtra("account", 0);
		
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		//文本显示的位置在EditText的最上方  
		input.setGravity(Gravity.TOP);
		//改变默认的单行模式  
		input.setSingleLine(false);  
		//水平滚动设置为False  
		input.setHorizontallyScrolling(false);

		title.setText(toNick);
		fromAccount = app.getMyNumber();// 我的账户
		
		boolean check=false;
		for(AMessageList a:app.getList())
		{
			if(a.listname.equals(toNick))
			{
				j=a;
				check=true;
				break;
			}
		}
		if(!check)
		{
			j=new AMessageList(toNick);
			app.getList().add(j);
		}

		adapter = new ChartMessageAdapter(this, j.messageList);
		listView.setAdapter(adapter);
	}

	protected void onStart(){
		super.onStart();
		if (j.messageList.size() > 0) {
			listView.setSelection(j.messageList.size() - 1);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Main2.on2=false;
		app.getMyConn().removeOnMessageListener(listener);
		if(j.getTop()==null)
			app.getList().remove(j);
	}
}
