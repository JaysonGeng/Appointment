package com.example.appointment.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.appointment.Dao.ActivityCollector;
import com.example.appointment.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private RelativeLayout change_password;
    private RelativeLayout user_agreement;
    private RelativeLayout our_information;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        initEvent();
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar_Setting);
        change_password  =findViewById(R.id.change_password_relative_layout_Setting);
        user_agreement = findViewById(R.id.user_agreement_relative_layout_Setting);
        our_information = findViewById(R.id.our_information_relative_layout_Setting);
        logout = findViewById(R.id.logout_button_Setting);
    }

    private void initEvent(){
        //加载工具栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        change_password.setOnClickListener(this);
        user_agreement.setOnClickListener(this);
        our_information.setOnClickListener(this);
        logout.setOnClickListener(this);
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

    @Override
    public void onClick(View view){
        Intent intent = new Intent();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();

        switch(view.getId()){
            case R.id.change_password_relative_layout_Setting:
                intent.setClass(SettingActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.user_agreement_relative_layout_Setting:
                intent.setClass(SettingActivity.this,UserAgreementShowActivity.class);
                startActivity(intent);
                break;
            case R.id.our_information_relative_layout_Setting:
                intent.setClass(SettingActivity.this,OurInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.logout_button_Setting:
                editor.putBoolean("auto_login",false);
                editor.apply();
                ActivityCollector.finishAll();
                finish();
            default:
        }
    }
}
