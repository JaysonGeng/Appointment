package com.example.appointment.page;
//程序的登陆后真正的的主界面


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.example.appointment.Adapter.ContentModel;
import com.example.appointment.Adapter.UserAdapter;
import com.example.appointment.Entity.User;
import com.example.appointment.R;
import com.example.appointment.View.LoginActivity;
import com.example.appointment.chart.ChartActivity;
import com.example.appointment.core.AConnection.OnMessageListener;
import com.example.appointment.core.ImApp;
import com.example.appointment.group.GroupChart;
import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageType;
import com.example.appointment.message.ContactInfoList;
import com.example.appointment.message.ThreadUtils;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by MichaelOD on 2017/12/27.
 */
public class Main2 extends FragmentActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private List<ContentModel> list;

    private ImageView leftMenu, rightMenu;
    private FragmentManager fm;
    private LinearLayout ll_tab1_message;
    private LinearLayout ll_tab1_friend;
    private LinearLayout ll_tab1_activity;

    private TextView maintext;
    private TextView usersign;
    private LinearLayout user_message;
    ImApp app;
    public int h;//目前显示的页面变量
    public static boolean on1 = false; //私聊界面开启的开关
    public static boolean on2 = false; //群聊界面开启的开关
    public static boolean on3 = false; //其他界面开启的开关
    private boolean exit = false;        //退出的开关
    private ListView listView;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private LinearLayout stage1;
    private RelativeLayout transition;
    private FrameLayout stage2;
    private FrameLayout stage3;
    private TextView username_show;
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

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main2);

        leftMenu = (ImageView) findViewById(R.id.leftmenu);
        rightMenu = (ImageView) findViewById(R.id.rightmenu);
        h = R.id.ll_tab1_message;
        fm = getSupportFragmentManager();
        ChartActivity.u = this;
        GroupChart.u = this;

        ll_tab1_message = (LinearLayout) findViewById(R.id.ll_tab1_message);
        ll_tab1_friend = (LinearLayout) findViewById(R.id.ll_tab1_friend);
        ll_tab1_activity = (LinearLayout) findViewById(R.id.ll_tab1_activity);

        maintext = (TextView) findViewById(R.id.maintext);


        navView = findViewById(R.id.nav_view_Main);
        mDrawerLayout = findViewById(R.id.drawer_layout_Main);
        icon = findViewById(R.id.icon_image_view_Main);
        username_show = findViewById(R.id.username_text_view_Main);

        ll_tab1_message.setOnClickListener(this);
        ll_tab1_friend.setOnClickListener(this);
        ll_tab1_activity.setOnClickListener(this);


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
                                Intent p = new Intent(Main2.this, LoginActivity.class);
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


//        player= MediaPlayer.create(Main2.this, R.raw.alpha);
//        player.start();
//
//        ActivityCollector.addActivity(this);
//
//        AnimationSet animationSet = new AnimationSet(true);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
//        alphaAnimation.setStartOffset(300);
//        alphaAnimation.setDuration(2000);
//        animationSet.addAnimation(alphaAnimation);
//        transition.setAnimation(animationSet);
//
////        //显示用户名
////        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
////        String username = pref.getString("account","");
////        username_show.setText(username);
//        username_show.setText(app.getMyName());


//        TextView textView=(TextView)findViewById(R.id.username);
//        textView.setText(app.getMyName());
//        usersign=(TextView)findViewById(R.id.user_sign);
        String newBuddyListJson = app.getBuddyListJson();
        Gson gson = new Gson();
        ContactInfoList newList = gson.fromJson(newBuddyListJson, ContactInfoList.class);
//        usersign.setText(newList.get(app.getMyNumber()).sign);

        //默认加载第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, new TabOne(Main2.this))
                .commit();


//        //调出左边菜单的按钮设置
//        leftMenu.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(Gravity.LEFT);
//            }
//        });
//
//        user_message.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(Main2.this, UserMessage.class);
//                intent.putExtra("number", app.getMyNumber());
//                startActivity(intent);
//            }
//        });
//
//        rightMenu.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                openOptionsMenu();
//            }
//        });

//        listView.setOnItemClickListener(new OnItemClickListener() {
//
////			每次点击之后，就用相应的Fragment替换主界面的LinearLayout，
////			当然，替换完成之后要记得关闭左边的侧拉菜单，传入的参数为Gravity.LEFT表示关闭左边的侧拉菜单，。
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                FragmentTransaction bt = fm.beginTransaction();
//                switch ((int) id) {
//                    case 1:
//                        Intent intent = new Intent(Main2.this,UserMessage.class);
//                        // 将账号和个性签名带到下一个activity
//                        intent.putExtra("number", app.getMyNumber());
//                        startActivity(intent);
//
//                        break;
//                    case 6:
////                        startActivity(new Intent(Main2.this, Settings.class));
//                        break;
//
//                    default:
//                        break;
//                }
//                bt.commit();
//                drawerLayout.closeDrawer(Gravity.LEFT);
//            }
//        });
//


    }

    //主界面消息监听器
    private OnMessageListener listener = new OnMessageListener() {

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
                                        transaction.replace(R.id.content, new TabTwo(Main2.this));
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
                                        transaction.replace(R.id.content, new TabOne(Main2.this));
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
                                        transaction.replace(R.id.content, new TabOne(Main2.this));
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
                                    transaction.replace(R.id.content, new TabOne(Main2.this));
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
            case R.id.ll_tab1_message:
                transaction.replace(R.id.content, new TabOne(Main2.this));

                maintext.setText("消息");
                transaction.commit();

                break;
            case R.id.ll_tab1_friend:
                transaction.replace(R.id.content, new TabTwo(Main2.this));
                maintext.setText("好友");
                transaction.commit();
                break;
            case R.id.ll_tab1_activity:
//                Intent w=new Intent(this,Plan.class);
//                startActivity(w);
                break;
            default:
                break;
        }
    }

    public void openOptionsMenu() {
        super.openOptionsMenu();
    }


    //    //设置使菜单图标可见
//    private void setIconEnable(Menu menu, boolean enable)  //用于显示图标
//    {
//        try
//        {
//            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
//            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
//            m.setAccessible(true);
//
//            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
//            m.invoke(menu, enable);
//
//        } catch (Exception e)
//        {
//            ThreadUtils.runInUiThread(new Runnable() {
//
//                public void run() {
//                    Toast.makeText(getBaseContext(), "显示图标出现问题！", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//    }
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确定要退出吗？")
                .setIcon(android.R.drawable.ic_menu_help)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
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
                                    }
                                    app.clearList();
                                    finish();
                                } catch (Exception e) {
                                    finish();
                                }
                            }
                        });

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }
//    //按返回键的确认窗口
//    @Override
//    public boolean onKeyDown(int keyCode,KeyEvent event){
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            long secondClickBack = System.currentTimeMillis();
//            if (secondClickBack - firstClickBack > 1500){
//                Toast.makeText(Main2.this,"再次点击返回以退出",Toast.LENGTH_SHORT).show();
//                firstClickBack = secondClickBack;
//                return true;
//            }else {
//                finish();
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode,event);
//    }


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
    }

    //切回时刷新页面
    protected void onStart() {
        super.onStart();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (h) {
            case R.id.ll_tab1_message:
                transaction.replace(R.id.content, new TabOne(Main2.this));
                maintext.setText("消息");
                transaction.commit();

                break;
            case R.id.ll_tab1_friend:
                transaction.replace(R.id.content, new TabTwo(Main2.this));

                maintext.setText("好友");
                transaction.commit();

                break;
            default:
                break;
        }
    }
}
