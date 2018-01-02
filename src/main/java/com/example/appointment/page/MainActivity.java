package com.example.appointment.page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointment.Adapter.UserAdapter;
import com.example.appointment.Dao.ActivityCollector;
import com.example.appointment.Dao.OnClickView;
import com.example.appointment.Entity.User;
import com.example.appointment.R;
import com.example.appointment.View.ChangeInformationActivity;
import com.example.appointment.View.CommitActivity;
import com.example.appointment.View.FeedbackActivity;
import com.example.appointment.View.PersonalInformationActivity;
import com.example.appointment.View.SearchActivity;
import com.example.appointment.View.SettingActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private LinearLayout stage1;
    private RelativeLayout transition;
    private FrameLayout stage2;
    private FrameLayout stage3;
    private TextView username_show;
    private TextView activity;
    private TextView friend;
    private TextView chatting;
    private TextView activity_checked;
    private TextView friend_checked;
    private TextView chatting_checked;
    private TextView all_activity_switch;
    private TextView my_activity_switch;
    private NavigationView navView;
    private FloatingActionButton commit;
    private WebView all_activity;
    private WebView my_activity;
    private RecyclerView friend_list;
    private CircleImageView icon;
    private MediaPlayer player;
    private List<User> userList = new ArrayList<>();
    private UserAdapter userAdapter;
    private long firstClickBack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initView(){

        toolbar = findViewById(R.id.toolbar_Main);
        username_show = findViewById(R.id.username_text_view_Main);
        mDrawerLayout = findViewById(R.id.drawer_layout_Main);
        stage1 = findViewById(R.id.stage1_linear_layout_Main);
        transition = findViewById(R.id.transition_relative_layout_Main);
        stage2 = findViewById(R.id.stage2_frame_layout_Main);
        stage3 = findViewById(R.id.stage3_frame_layout_Main);
        navView = findViewById(R.id.nav_view_Main);
        commit = findViewById(R.id.commit_floating_button_Main);
        chatting =  findViewById(R.id.ll_tab1_message);
        friend =  findViewById(R.id.ll_tab1_friend);
        activity =  findViewById(R.id.ll_tab1_activity);
        activity_checked = findViewById(R.id.activity_list_checked_text_view_Main);
        friend_checked = findViewById(R.id.friend_list_checked_text_view_Main);
        chatting_checked = findViewById(R.id.chatting_list_checked_text_view_Main);
        all_activity_switch = findViewById(R.id.all_activities_text_view_Main);
        my_activity_switch = findViewById(R.id.my_activities_text_view_Main);
        friend_list = findViewById(R.id.friend_list_recycler_view_Main);
        icon = findViewById(R.id.icon_image_view_Main);

    }

    private void initEvent(){

        player= MediaPlayer.create(MainActivity.this, R.raw.alpha);
        player.start();

        ActivityCollector.addActivity(this);

        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setStartOffset(300);
        alphaAnimation.setDuration(2000);
        animationSet.addAnimation(alphaAnimation);
        transition.setAnimation(animationSet);

        //显示用户名
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = pref.getString("account","");
        username_show.setText(username);

//        //加载工具栏
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.selffunctionicon);
//        }

        //侧滑菜单item的点击事件
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent = new Intent();
                switch (item.getItemId()){
                    case R.id.nav_information:
                        intent.setClass(MainActivity.this,PersonalInformationActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_setting:
                        intent.setClass(MainActivity.this,SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_feedback:
                        intent.setClass(MainActivity.this,FeedbackActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_administrator:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        //加载RecyclerView
        initUsers();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friend_list.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(userList);
        friend_list.setAdapter(userAdapter);

        //设置各控件的点击事件
        commit.setOnClickListener(this);
        activity.setOnClickListener(this);
        friend.setOnClickListener(this);
        chatting.setOnClickListener(this);
        all_activity_switch.setOnClickListener(this);
        my_activity_switch.setOnClickListener(this);
        icon.setOnClickListener(this);

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
                intent.setClass(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;

    }

    @Override
    public void onClick(View view){

        Intent intent =new Intent();
        switch (view.getId()){
            case R.id.commit_floating_button_Main:
                intent.setClass(MainActivity.this,CommitActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_tab1_activity:
                OnClickView.click_small(activity,activity_checked);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        friend.setVisibility(View.VISIBLE);
                        chatting.setVisibility(View.VISIBLE);
                        friend_checked.setVisibility(View.GONE);
                        chatting_checked.setVisibility(View.GONE);
                    }
                },500);

                stage1.setVisibility(View.VISIBLE);
                stage2.setVisibility(View.GONE);
                stage3.setVisibility(View.GONE);
                break;
            case R.id.ll_tab1_friend:
                OnClickView.click_small(friend,friend_checked);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.setVisibility(View.VISIBLE);
                        chatting.setVisibility(View.VISIBLE);
                        activity_checked.setVisibility(View.GONE);
                        chatting_checked.setVisibility(View.GONE);
                    }
                },500);

                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.VISIBLE);
                stage3.setVisibility(View.GONE);
                break;
            case R.id.ll_tab1_message:
                OnClickView.click_small(chatting,chatting_checked);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.setVisibility(View.VISIBLE);
                        friend.setVisibility(View.VISIBLE);
                        activity_checked.setVisibility(View.GONE);
                        friend_checked.setVisibility(View.GONE);
                    }
                },500);

                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.GONE);
                stage3.setVisibility(View.VISIBLE);
                break;
            case R.id.all_activities_text_view_Main:
                all_activity.setVisibility(View.VISIBLE);
                my_activity.setVisibility(View.GONE);
                all_activity_switch.setTextColor(Color.BLUE);
                my_activity_switch.setTextColor(Color.BLACK);
                break;
            case R.id.my_activities_text_view_Main:
                all_activity.setVisibility(View.GONE);
                my_activity.setVisibility(View.VISIBLE);
                all_activity_switch.setTextColor(Color.BLACK);
                my_activity_switch.setTextColor(Color.BLUE);
                break;
            case R.id.icon_image_view_Main:
                intent.setClass(MainActivity.this,ChangeInformationActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //"替换碎片"方法
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.show_frame_layout_Main,fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //删除该活动
        ActivityCollector.removeActivity(this);
        player.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            long secondClickBack = System.currentTimeMillis();
            if (secondClickBack - firstClickBack > 1500){
                Toast.makeText(MainActivity.this,"再次点击返回以退出",Toast.LENGTH_SHORT).show();
                firstClickBack = secondClickBack;
                return true;
            }else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }

    //更新朋友列表数据
    private void initUsers(){
        userList.clear();
        userList.add(new User("abc",1,"男","123","","123",R.drawable.loginbackground));
    }

}
