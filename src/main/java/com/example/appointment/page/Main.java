package com.example.appointment.page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointment.Dao.ActivityCollector;
import com.example.appointment.Dao.OnClickView;
import com.example.appointment.R;
import com.example.appointment.View.CommitActivity;
import com.example.appointment.View.FeedbackActivity;
import com.example.appointment.View.LoginActivity;
import com.example.appointment.View.PersonalInformationActivity;
import com.example.appointment.View.SearchActivity;
import com.example.appointment.View.SettingActivity;
import com.example.appointment.chart.ChartActivity;
import com.example.appointment.chart.UserMessage;
import com.example.appointment.core.AConnection;
import com.example.appointment.core.ImApp;
import com.example.appointment.group.GroupChart;
import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageType;
import com.example.appointment.message.ContactInfoList;
import com.example.appointment.message.ThreadUtils;
import com.google.gson.Gson;

import java.io.IOException;

public class Main extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FragmentManager fm;
    private TextView message;
    private TextView friend;
    private TextView activity;
    private TextView activity_checked;
    private TextView friend_checked;
    private TextView chatting_checked;
    private NavigationView navView;
    private LinearLayout transition;
    private FloatingActionButton commit;
    private TextView usersign;
    ImApp app;
    public int h = R.id.ll_tab1_message;//目前显示的页面变量
    public static boolean on1 = false; //私聊界面开启的开关
    public static boolean on2 = false; //群聊界面开启的开关
    public static boolean on3 = false; //其他界面开启的开关
    private boolean exit = false;        //退出的开关
    private long firstClickBack = 0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        ActivityCollector.addActivity(this);

        transition = findViewById(R.id.fragment_layout);

        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setStartOffset(300);
        alphaAnimation.setDuration(2000);
        animationSet.addAnimation(alphaAnimation);
        transition.setAnimation(animationSet);

        drawerLayout =  findViewById(R.id.drawerlayout);
        toolbar = findViewById(R.id.toolbar_Main);
        fm = getSupportFragmentManager();
        ChartActivity.u = this;
        GroupChart.u = this;

        message =  findViewById(R.id.ll_tab1_message);
        friend =  findViewById(R.id.ll_tab1_friend);
        activity =  findViewById(R.id.ll_tab1_activity);
        activity_checked = findViewById(R.id.activity_list_checked_text_view_Main);
        friend_checked = findViewById(R.id.friend_list_checked_text_view_Main);
        chatting_checked = findViewById(R.id.chatting_list_checked_text_view_Main);
        commit = findViewById(R.id.commit_floating_button_Main);

        message.setOnClickListener(this);
        friend.setOnClickListener(this);
        activity.setOnClickListener(this);
        commit.setOnClickListener(this);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.BLACK);
        }

        //加载工具栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.selffunctionicon);
        }

        // 数据保存在application中
        app = (ImApp) getApplication();
        // 获取长连接，往长连接里添加监听,时刻监听服务器返回来的消息,如果有消息到达，就执行onReceive
        app.getMyConn().addOnMessageListener(listener);

        //心跳包线程开启
        ThreadUtils.runInSubThread(new Runnable() {
            public void run() {
                try {
                    while (!exit) {
                        AMessage msg = new AMessage();
                        msg.type = AMessageType.MSG_TYPE_ONLINE;
                        msg.from = app.getMyNumber();
                        app.getMyConn().sendMessage(msg);
                        Thread.sleep(3000);
                    }
                } catch (IOException e) {
                    if (!exit) {
                        ThreadUtils.runInUiThread(new Runnable() {

                            public void run() {
                                app.setstate(false);
                                Intent p = new Intent(Main.this, LoginActivity.class);
                                Toast.makeText(getBaseContext(), "您的网络出现异常，请重新登录！", Toast.LENGTH_SHORT).show();
                                startActivity(p);
                                finish();
                            }
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "抱歉，程序出现意外异常！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView textView = findViewById(R.id.username);
        textView.setText(app.getMyName());
        usersign = findViewById(R.id.user_sign);
        navView = findViewById(R.id.nav_view_Main);
        String newBuddyListJson = app.getBuddyListJson();
        Gson gson = new Gson();
        ContactInfoList newList = gson.fromJson(newBuddyListJson, ContactInfoList.class);
        usersign.setText(newList.get(app.getMyNumber()).sign);

        //默认加载第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, new TabOne(Main.this))
                .commit();


        navView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main.this, UserMessage.class);
                intent.putExtra("number", app.getMyNumber());
                startActivity(intent);
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent = new Intent();
                switch (item.getItemId()){
                    case R.id.nav_information:
                        intent.setClass(Main.this,PersonalInformationActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_setting:
                        intent.setClass(Main.this,SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_feedback:
                        intent.setClass(Main.this,FeedbackActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_administrator:
                        Toast.makeText(Main.this,"功能暂未开放",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

}

    //主界面消息监听器
    private AConnection.OnMessageListener listener = new AConnection.OnMessageListener() {

        public void onReveive(final AMessage msg) {
            // 接收服务器返回的结果.处理数据的显示,运行在主线程中
            ThreadUtils.runInSubThread(new Runnable() {

                public void run() {
                    if (AMessageType.MSG_TYPE_BUDDYLIST.equals(msg.type)) {

                        // 如有服务器返回好友列表，则说明有好友上线\
                        // 有好友上线// 新上线好友的信息json串
                        String newBuddyListJson = msg.content;
                        app.setBuddyListJson(newBuddyListJson);
                        ThreadUtils.runInUiThread(new Runnable() {

                            public void run() {
                                if (!on3 && !on1 && !on2) {
                                    if (h == R.id.ll_tab1_friend) {
                                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.content, new TabTwo(Main.this));
                                        transaction.commit();
                                    }
                                }
                                String newBuddyListJson = app.getBuddyListJson();
                                Gson gson = new Gson();
                                ContactInfoList newList = gson.fromJson(newBuddyListJson, ContactInfoList.class);
                                usersign.setText(newList.get(app.getMyNumber()).sign);
                            }
                        });

                    } else if (AMessageType.MSG_TYPE_CHAT_P2P.equals(msg.type)) {
                        ThreadUtils.runInUiThread(new Runnable() {

                            public void run() {
                                if (!on1) {
                                    app.addMessage(msg);
                                    if (h == R.id.ll_tab1_message && !on2 && !on3) {
                                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.content, new TabOne(Main.this));
                                        transaction.commit();
                                    }
                                }
                            }
                        });

                    } else if (AMessageType.MSG_TYPE_CHAT_ROOM.equals(msg.type)) {
                        ThreadUtils.runInUiThread(new Runnable() {

                            public void run() {
                                if (!on2) {
                                    app.addMessage(msg);
                                    if (h == R.id.ll_tab1_message && !on1 && !on3) {
                                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.content, new TabOne(Main.this));
                                        transaction.commit();
                                    }
                                }
                            }
                        });
                    } else if (AMessageType.MSG_TYPE_ADDFRIEND.equals(msg.type)) {
                        ThreadUtils.runInUiThread(new Runnable() {

                            public void run() {
                                app.addMessage(msg);
                                if (h == R.id.ll_tab1_message && !on2 && !on1 && !on3) {
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.content, new TabOne(Main.this));
                                    transaction.commit();
                                }
                            }
                        });

                    } else if (AMessageType.MSG_TYPE_GROUPLIST.equals(msg.type)) {
                        app.setGroupListJson(msg.content);
                    } else if (AMessageType.MSG_TYPE_PLANLIST.equals(msg.type)) {
                        app.setPlanListJson(msg.content);
                    }
                }
            });
        }
    };

    //底部按钮切换的点击监听器
    public void onClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        h = view.getId();
        switch (h) {
            case R.id.commit_floating_button_Main:
                Intent intent = new Intent();
                intent.setClass(Main.this,CommitActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_tab1_message:
                OnClickView.click_small(message,chatting_checked);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.setVisibility(View.VISIBLE);
                        friend.setVisibility(View.VISIBLE);
                        activity_checked.setVisibility(View.GONE);
                        friend_checked.setVisibility(View.GONE);
                    }
                },500);
                transaction.replace(R.id.content, new TabOne(Main.this));
                toolbar.setTitle("消息");
                transaction.commit();
                break;
            case R.id.ll_tab1_friend:
                OnClickView.click_small(friend,friend_checked);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.setVisibility(View.VISIBLE);
                        message.setVisibility(View.VISIBLE);
                        activity_checked.setVisibility(View.GONE);
                        chatting_checked.setVisibility(View.GONE);
                    }
                },500);
                transaction.replace(R.id.content, new TabTwo(Main.this));
                toolbar.setTitle("好友");
                transaction.commit();
                break;
            case R.id.ll_tab1_activity:
                OnClickView.click_small(activity,activity_checked);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        friend.setVisibility(View.VISIBLE);
                        message.setVisibility(View.VISIBLE);
                        friend_checked.setVisibility(View.GONE);
                        chatting_checked.setVisibility(View.GONE);
                    }
                },500);
                transaction.replace(R.id.content, new TabThree(Main.this));
                toolbar.setTitle("活动");
                transaction.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            long secondClickBack = System.currentTimeMillis();
            if (secondClickBack - firstClickBack > 1500){
                Toast.makeText(Main.this,"  再次点击返回以退出   ",Toast.LENGTH_SHORT).show();
                firstClickBack = secondClickBack;
                return true;
            }else {
                ThreadUtils.runInSubThread(new Runnable() {
                    public void run() {
                        try {
                            AMessage msg = new AMessage();
                            msg.type = AMessageType.MSG_TYPE_LOGOUT;
                            msg.from = app.getMyNumber();
                            app.getMyConn().sendMessage(msg);
                            exit = true;
                            try {
                                app.getMyConn().disConnect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            app.clearList();
                            finish();
                        } catch (Exception e) {
                            finish();
                        }
                    }
                });
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }

    protected void onDestroy() {
        super.onDestroy();
        // activity销毁的时候取消监听
        if (!exit) {
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
        }
        //删除该活动
        ActivityCollector.removeActivity(this);
    }

    //切回时刷新页面
    protected void onStart() {
        super.onStart();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (h) {
            case R.id.ll_tab1_message:
                OnClickView.click_small(message,chatting_checked);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.setVisibility(View.VISIBLE);
                        friend.setVisibility(View.VISIBLE);
                        activity_checked.setVisibility(View.GONE);
                        friend_checked.setVisibility(View.GONE);
                    }
                },500);
                transaction.replace(R.id.content, new TabOne(Main.this));
                toolbar.setTitle("消息");
                transaction.commit();
                break;
            case R.id.ll_tab1_friend:
                OnClickView.click_small(friend,friend_checked);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.setVisibility(View.VISIBLE);
                        message.setVisibility(View.VISIBLE);
                        activity_checked.setVisibility(View.GONE);
                        chatting_checked.setVisibility(View.GONE);
                    }
                },500);
                transaction.replace(R.id.content, new TabTwo(Main.this));
                toolbar.setTitle("好友");
                transaction.commit();
                break;
            case R.id.ll_tab1_activity:
                OnClickView.click_small(activity,activity_checked);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        friend.setVisibility(View.VISIBLE);
                        message.setVisibility(View.VISIBLE);
                        friend_checked.setVisibility(View.GONE);
                        chatting_checked.setVisibility(View.GONE);
                    }
                },500);
                transaction.replace(R.id.content, new TabThree(Main.this));
                toolbar.setTitle("活动");
                transaction.commit();
                break;

            default:
                break;
        }
    }

    //加载toolbar菜单文件
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_main,menu);
        return true;
    }

    //设置工具栏按钮的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.search:
                Intent intent =new Intent();
                intent.setClass(Main.this,SearchActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;

    }

}