package com.example.appointment.View;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appointment.R;

public class UserDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView name_show;
    private TextView sex_show;
    private TextView campus_show;
    private ImageView icon_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        initView();
        initEvent();
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar_UserDetail);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_UserDetail);
        name_show = findViewById(R.id.name_text_view_UserDetail);
        sex_show = findViewById(R.id.sex_text_view_UserDetail);
        campus_show = findViewById(R.id.campus_text_view_UserDetail);
        icon_show = findViewById(R.id.user_icon_UserDetail);
    }

    private void initEvent(){
        //加载工具栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //接收参数
        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname") ;
        String username = intent.getStringExtra("username");
        String sex = intent.getStringExtra("sex");
        String campus = intent.getStringExtra("campus");
        int icon = intent.getIntExtra("icon",R.drawable.generallogo);

        collapsingToolbarLayout.setTitle(nickname);
        name_show.setText(username);
        sex_show.setText(sex);
        campus_show.setText(campus);
        icon_show.setImageResource(icon);
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
