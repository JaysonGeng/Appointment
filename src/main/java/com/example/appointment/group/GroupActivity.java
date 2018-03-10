package com.example.appointment.group;

/**群组界面的类
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

	private RecyclerView list;
	private Toolbar toolbar;
	private Button newgroup;
	private List<GroupInfo> infos = new ArrayList<>();
	private ImApp app;
	private GroupListAdapter adapter;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.group);
		toolbar = findViewById(R.id.toolbar_Group);
		list = findViewById(R.id.grouplist);

		Main.on3=true;

		//加载工具栏
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null){
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

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

		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		list.setLayoutManager(layoutManager);
		adapter = new GroupListAdapter(infos);
		list.setAdapter(adapter);
	}
	protected void onDestroy() {
		super.onDestroy();
		Main.on3=false;
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
