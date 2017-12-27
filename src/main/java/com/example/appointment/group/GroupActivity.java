
//群组界面的类

package com.example.appointment.group;

/**
 * Created by MichaelOD on 2017/12/26.
 */


import com.example.appointment.Adapter.GroupListAdapter;
import com.example.appointment.R;
import com.example.appointment.core.ImApp;
import com.example.appointment.message.GroupInfo;
import com.example.appointment.message.GroupList;
import com.example.appointment.page.Main;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends Activity
{
	ListView list;
	private Button newgroup;
	private List<GroupInfo> infos = new ArrayList<GroupInfo>();
	private ImApp app;
	private GroupListAdapter adapter;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.group);
		list=(ListView)findViewById(R.id.grouplist);
		newgroup=(Button)findViewById(R.id.newgroup_button);
		Main.on3=true;
		
		//创建群组
  		newgroup.setOnClickListener(new OnClickListener() {

  			@Override
  			public void onClick(View v) {
				Intent intent = new Intent(GroupActivity.this,
						NewGroup.class);
				startActivity(intent);
				finish();
  			}
  		});
  				
		app = (ImApp) getApplication();
		String groupListJson = app.getGroupListJson();
		Gson gson = new Gson();
		GroupList alist = gson.fromJson(groupListJson,
				GroupList.class);
		for(GroupInfo a:alist.groupList)
		{
			if(a.member.contains(""+app.getMyNumber()))
				infos.add(a);
		}
		
		adapter = new GroupListAdapter(getBaseContext(), infos,app);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				// 获得当前点击条目的信息.包含账号和昵称
				GroupInfo info = infos.get(position);
				// 不能跟自己聊天

					Intent intent = new Intent(GroupActivity.this,
							GroupMessage.class);
					// 将账号和个性签名带到下一个activity
					intent.putExtra("account", info.number);
					startActivity(intent);
			}
		});
	}
	protected void onDestroy()
	{
		super.onDestroy();
		Main.on3=false;
	}
}
