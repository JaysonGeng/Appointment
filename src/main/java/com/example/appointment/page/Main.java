package com.example.appointment.page;
//程序的登陆后真正的的主界面




import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


import com.google.gson.Gson;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends FragmentActivity implements View.OnClickListener{

    private DrawerLayout drawerLayout;
    private List<ContentModel> list;
    private ContentAdapter adapter;
    private ImageView leftMenu,rightMenu;
    private ListView listView;
    private FragmentManager fm;
    private LinearLayout ll_tab1_message;
    private LinearLayout ll_tab1_friend;
    private LinearLayout ll_tab1_activity;

    private TextView message;
    private TextView friend;
    private TextView activity;
    private TextView maintext;
    private TextView usersign;
    private LinearLayout user_message;
    ImApp app;
    public int h=R.id.ll_tab1_message;//目前显示的页面变量
    public static boolean on1=false; //私聊界面开启的开关
    public static boolean on2=false; //群聊界面开启的开关
    public static boolean on3=false; //其他界面开启的开关
    private boolean exit=false;		//退出的开关

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        leftMenu = (ImageView) findViewById(R.id.leftmenu);
        rightMenu = (ImageView) findViewById(R.id.rightmenu);
        user_message = (LinearLayout) findViewById(R.id.user_message);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        fm = getSupportFragmentManager();
        ChartActivity.u=this;
        GroupChart.u=this;

        ll_tab1_message = (LinearLayout) findViewById(R.id.ll_tab1_message);
        ll_tab1_friend = (LinearLayout) findViewById(R.id.ll_tab1_friend);
        ll_tab1_activity = (LinearLayout) findViewById(R.id.ll_tab1_activity);
        message = (TextView) findViewById(R.id.tv_message);
        friend = (TextView) findViewById(R.id.tv_friend);
        activity = (TextView) findViewById(R.id.tv_activity);
        maintext = (TextView) findViewById(R.id.maintext);
        message.setTextColor(getResources().getColor(R.color.blue));

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
                    while(!exit)
                    {
                        AMessage msg = new AMessage();
                        msg.type = AMessageType.MSG_TYPE_ONLINE;
                        msg.from=app.getMyNumber();
                        app.getMyConn().sendMessage(msg);
                        Thread.sleep(3000);
                    }
                } catch (IOException e) {
                    if(!exit){
                        ThreadUtils.runInUiThread(new Runnable() {

                            public void run() {
                                app.setstate(false);
                                Intent p=new Intent(Main.this,MainActivity.class);
                                Toast.makeText(getBaseContext(), "您的网络出现异常，请重新登录！", 0).show();
                                startActivity(p);
                                finish();
                            }
                        });}
                }catch(Exception e){
                    Toast.makeText(getBaseContext(), "抱歉，程序出现意外异常！", 0).show();
                }
            }
        });

        TextView textView=(TextView)findViewById(R.id.username);
        textView.setText(app.getMyName());
        usersign=(TextView)findViewById(R.id.user_sign);
        String newBuddyListJson = app.getBuddyListJson();
        Gson gson = new Gson();
        ContactInfoList newList = gson.fromJson(newBuddyListJson, ContactInfoList.class);
        usersign.setText(newList.get(app.getMyNumber()).sign);

        //默认加载第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content,new TabOne(Main.this))
                .commit();

        //以下这段给ListView赋值
        listView = (ListView) findViewById(R.id.left_listview);
        initData();
        adapter = new ContentAdapter(this, list);
        listView.setAdapter(adapter);

        //调出左边菜单的按钮设置
        leftMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        user_message.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main.this, UserMessage.class);
                intent.putExtra("number", app.getMyNumber());
                startActivity(intent);
            }
        });

        rightMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openOptionsMenu();
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

//			每次点击之后，就用相应的Fragment替换主界面的LinearLayout，
//			当然，替换完成之后要记得关闭左边的侧拉菜单，传入的参数为Gravity.LEFT表示关闭左边的侧拉菜单，。

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                FragmentTransaction bt = fm.beginTransaction();
                switch ((int) id) {
                    case 1:
                        Intent intent = new Intent(Main.this,UserMessage.class);
                        // 将账号和个性签名带到下一个activity
                        intent.putExtra("number", app.getMyNumber());
                        startActivity(intent);
                        break;
                    case 6:
                        startActivity(new Intent(Main.this, Settings.class));
                        break;

                    default:
                        break;
                }
                bt.commit();
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

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
                                if(!on3 && !on1 && !on2)
                                {
                                    if(h==R.id.ll_tab1_friend)
                                    {
                                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.content,new TabThree(Main.this));
                                        transaction.commit();
                                    }}
                                String newBuddyListJson = app.getBuddyListJson();
                                Gson gson = new Gson();
                                ContactInfoList newList = gson.fromJson(newBuddyListJson, ContactInfoList.class);
                                usersign.setText(newList.get(app.getMyNumber()).sign);
                            }
                        });

                    }
                    else if (AMessageType.MSG_TYPE_CHAT_P2P.equals(msg.type))
                    {
                        ThreadUtils.runInUiThread(new Runnable() {

                            public void run() {
                                if(!on1)
                                {
                                    app.addMessage(msg);
                                    if (h==R.id.ll_tab1_message && !on2 && !on3)
                                    {
                                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.content,new TabOne(Main.this));
                                        transaction.commit();
                                    }
                                }
                            }
                        });

                    }
                    else if (AMessageType.MSG_TYPE_CHAT_ROOM.equals(msg.type))
                    {
                        ThreadUtils.runInUiThread(new Runnable() {

                            public void run() {
                                if(!on2)
                                {
                                    app.addMessage(msg);
                                    if (h==R.id.ll_tab1_message && !on1 && !on3)
                                    {
                                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.content,new TabOne(Main.this));
                                        transaction.commit();
                                    }
                                }
                            }
                        });

                    }
                    else if (AMessageType.MSG_TYPE_ADDFRIEND.equals(msg.type))
                    {
                        ThreadUtils.runInUiThread(new Runnable() {

                            public void run() {
                                app.addMessage(msg);
                                if (h==R.id.ll_tab1_message && !on2 && !on1 && !on3)
                                {
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.content,new TabOne(Main.this));
                                    transaction.commit();
                                }
                            }
                        });

                    }else if (AMessageType.MSG_TYPE_GROUPLIST.equals(msg.type)) {
                        app.setGroupListJson(msg.content);
                    }else if (AMessageType.MSG_TYPE_PLANLIST.equals(msg.type)) {
                        app.setPlanListJson(msg.content);
                    }
                }
            });
        }
    };

    //底部按钮切换的点击监听器
    public void onClick(View view) {
        setTabImageNormal();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        h=view.getId();
        switch (h) {
            case R.id.ll_tab1_message:
                transaction.replace(R.id.content,new TabOne(Main.this));
                message.setTextColor(getResources().getColor(R.color.blue));
                maintext.setText("消息");
                transaction.commit();

                break;
            case R.id.ll_tab1_friend:
                transaction.replace(R.id.content,new TabThree(Main.this));
                friend.setTextColor(getResources().getColor(R.color.blue));
                maintext.setText("好友");
                transaction.commit();

                break;
            case R.id.ll_tab1_activity:
                Intent w=new Intent(this,Plan.class);
                startActivity(w);
                break;
            default:
                break;
        }
    }

    //将底部按钮设置回正常
    public void setTabImageNormal(){

        int normalColor = getResources().getColor(R.color.black);
        message.setTextColor(normalColor);
        friend.setTextColor(normalColor);
        activity.setTextColor(normalColor);
    }

    //设置侧拉菜单
    private void initData() {
        list = new ArrayList<ContentModel>();

        //初始化左侧菜单的选项
        list.add(new ContentModel(R.drawable.doctoradvice2, "查看资料", 1));
        list.add(new ContentModel(R.drawable.infusion_selected, "未解锁装备槽", 2));
        list.add(new ContentModel(R.drawable.mypatient_selected, "未解锁武器槽", 3));
        list.add(new ContentModel(R.drawable.mywork_selected, "未解锁护甲槽", 4));
        list.add(new ContentModel(R.drawable.nursingcareplan2, "未解锁护符槽", 5));
        list.add(new ContentModel(R.drawable.personal_selected, "设置", 6));
    }

    public void openOptionsMenu() {
        super.openOptionsMenu();
    }

    //设置单机菜单键弹出的菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);	//getMenuInflater用于从xml中读取菜单定义转化为视图
        setIconEnable(menu,true);		//用于显示图标
        return true;
    }

    //设置菜单的选项
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch(id)
        {
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                return true;
            case R.id.exit2:
                ThreadUtils.runInSubThread(new Runnable() {
                    public void run() {
                        try {
                            AMessage msg = new AMessage();
                            msg.type=AMessageType.MSG_TYPE_LOGOUT;
                            msg.from=app.getMyNumber();
                            app.getMyConn().sendMessage(msg);
                            exit=true;
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //设置使菜单图标可见
    private void setIconEnable(Menu menu, boolean enable)  //用于显示图标
    {
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);

            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);

        } catch (Exception e)
        {
            ThreadUtils.runInUiThread(new Runnable() {

                public void run() {
                    Toast.makeText(getBaseContext(), "显示图标出现问题！", 0).show();
                }
            });

        }
    }

    //按返回键的确认窗口
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
                                    msg.type=AMessageType.MSG_TYPE_LOGOUT;
                                    msg.from=app.getMyNumber();
                                    app.getMyConn().sendMessage(msg);
                                    exit=true;
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


    protected void onDestroy() {
        super.onDestroy();
        // activity销毁的时候取消监听
        if(!exit){
            ThreadUtils.runInSubThread(new Runnable() {
                public void run() {
                    try {
                        AMessage msg = new AMessage();
                        msg.type=AMessageType.MSG_TYPE_LOGOUT;
                        msg.from=app.getMyNumber();
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
    protected void onStart(){
        super.onStart();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (h) {
            case R.id.ll_tab1_message:
                transaction.replace(R.id.content,new TabOne(Main.this));
                message.setTextColor(getResources().getColor(R.color.blue));
                maintext.setText("消息");
                transaction.commit();

                break;
            case R.id.ll_tab1_friend:
                transaction.replace(R.id.content,new TabThree(Main.this));
                friend.setTextColor(getResources().getColor(R.color.blue));
                maintext.setText("好友");
                transaction.commit();

                break;
            default:
                break;
        }
    }
}
