package com.example.appointment.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
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
import com.example.appointment.core.ImApp;
import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageType;
import com.example.appointment.message.ThreadUtils;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private RelativeLayout user_agreement;
    private RelativeLayout our_information;
    private Button logout;
    ImApp app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        app = (ImApp) getApplication();
        initView();
        initEvent();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar_Setting);
        user_agreement = findViewById(R.id.user_agreement_relative_layout_Setting);
        our_information = findViewById(R.id.our_information_relative_layout_Setting);
        logout = findViewById(R.id.logout_button_Setting);
    }

    private void initEvent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        //加载工具栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        user_agreement.setOnClickListener(this);
        our_information.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    //加载toolbar菜单文件
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_general, menu);
        return true;
    }

    //设置工具栏按钮的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();

        switch (view.getId()) {
            case R.id.user_agreement_relative_layout_Setting:
                intent.setClass(SettingActivity.this, UserAgreementShowActivity.class);
                startActivity(intent);
                break;
            case R.id.our_information_relative_layout_Setting:
                intent.setClass(SettingActivity.this, OurInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.logout_button_Setting:
                editor.putBoolean("auto_login", false);

                editor.apply();
                ActivityCollector.finishAll();
                finish();
            default:
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // activity销毁的时候取消监听

        ThreadUtils.runInSubThread(new Runnable() {
            public void run() {
                try {
                    AMessage msg = new AMessage();
                    msg.type = AMessageType.MSG_TYPE_LOGOUT;
                    msg.from = app.getMyNumber();
                    app.getMyConn().sendMessage(msg);
                    app.getMyConn().disConnect();
                    app.clearList();
                } catch (Exception e) {
                    finish();
                }
            }
        });
        //删除该活动
        ActivityCollector.removeActivity(this);
    }
}
