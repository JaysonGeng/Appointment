package com.example.appointment.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointment.Dao.OnClickView;
import com.example.appointment.R;
import com.example.appointment.core.AConnection;
import com.example.appointment.core.ImApp;
import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageType;
import com.example.appointment.message.ContactInfoList;
import com.example.appointment.message.ThreadUtils;
import com.example.appointment.page.MainActivity;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private CheckBox rememberPass;
    private CheckBox autoLogin;
    private TextView register;
    private TextView forget_password;
    private Button login;
    private EditText usernameInputting;
    private EditText passwordInputting;
    private LinearLayout content;
    private LinearLayout transition;
    private AlphaAnimation alphaAnimation;
    private TranslateAnimation translateAnimation;
    private AnimationSet animationSet;
    private String username;
    private String password;
    AConnection conn;
    ImApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
        ThreadUtils.runInSubThread(new Runnable() {
            public void run() {
                try {
                    //这里的IP地址一定要注意改成电脑的地址
                    conn = new AConnection("192.168.43.153", 8080);// Socket
                    conn.connect();// 建立连接
                    // 建立连接之后，将监听器添加到连接里面
                    conn.addOnMessageListener(listener);
                } catch (Exception e) {
                    ThreadUtils.runInUiThread(new Runnable() {

                        public void run() {
                            Toast.makeText(getBaseContext(), "网络连接故障！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void initView() {
        autoLogin = findViewById(R.id.auto_login_checkbox_Login);
        register = findViewById(R.id.register_text_view_Login);
        forget_password = findViewById(R.id.forget_password_text_view_Login);
        login = findViewById(R.id.login_button_Login);
        usernameInputting = findViewById(R.id.username_edit_text_Login);
        passwordInputting = findViewById(R.id.password_edit_text_Login);
        rememberPass = findViewById(R.id.remember_password_checkbox_Login);
        content = findViewById(R.id.linear_layout_Login);
        transition = findViewById(R.id.transition_linear_layout_Login);
    }

    private void initEvent() {
        animationSet = new AnimationSet(true);
        alphaAnimation = new AlphaAnimation(0, 1);
        translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setStartOffset(300);
        translateAnimation.setDuration(1500);
        translateAnimation.setStartOffset(300);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        content.startAnimation(animationSet);

        //实现记住密码和自动登录功能
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);
        boolean isAutoLogin = pref.getBoolean("auto_login", false);
        if (isAutoLogin) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        if (isRemember) {
            String username = pref.getString("username", "");
            String password = pref.getString("password", "");
            usernameInputting.setText(username);
            passwordInputting.setText(password);
            rememberPass.setChecked(true);
        }

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent();
        final SharedPreferences.Editor editor = pref.edit();

        switch (view.getId()) {
            case R.id.login_button_Login:
                //按钮动画
                OnClickView.click_big(login);
                //获取EditView中的内容
                username = usernameInputting.getText().toString();
                password = passwordInputting.getText().toString();


                ThreadUtils.runInSubThread(new Runnable() {
                    public void run() {
                        try {
                            AMessage msg = new AMessage();
                            msg.type = AMessageType.MSG_TYPE_LOGIN;
                            msg.content = username + "#" + password;//帐号和密码用#分隔
                            conn.sendMessage(msg);
                        } catch (Exception e) {
                            ThreadUtils.runInUiThread(new Runnable() {

                                public void run() {
                                    Toast.makeText(getBaseContext(), "出现异常，请检查后重试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });


                //记住密码
                if (rememberPass.isChecked()) {
                    editor.putBoolean("remember_password", true);
                    editor.putString("username", username);
                    editor.putString("password", password);
                } else editor.putBoolean("remember_password", false);
                //自动登录
                if (autoLogin.isChecked()) {
                    editor.putBoolean("auto_login", true);
                } else editor.putBoolean("auto_login", false);
                editor.putString("account", username);
                editor.apply();

                break;
            case R.id.register_text_view_Login:
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forget_password_text_view_Login:
                intent.setClass(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // activity销毁的时候取消监听
        conn.removeOnMessageListener(listener);
    }


    private AConnection.OnMessageListener listener = new AConnection.OnMessageListener() {

        public void onReveive(final AMessage msg) {
            // 接收服务器返回的结果.处理数据的显示,运行在主线程中
            ThreadUtils.runInUiThread(new Runnable() {

                public void run() {
                    if (AMessageType.MSG_TYPE_BUDDYLIST.equals(msg.type)) {
                        // 登录成功，返回的数据是好友列表
                        // 有用的信息是content的json串
                        //星空效果动画
                        animationSet = new AnimationSet(true);
                        alphaAnimation = new AlphaAnimation(1, 0);
                        alphaAnimation.setDuration(1600);
                        animationSet.addAnimation(alphaAnimation);
                        transition.startAnimation(animationSet);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                transition.setVisibility(View.GONE);
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        }, 1000);
                        // 将连接conn保存到application中，作为一个长连接存在，这样在其他的activity，server中都能拿到这个连接，保证了项目连接的唯一性
                        // 新建一个application类，给出get，set方法，使用application可以在整个项目中共享数据
                        app = (ImApp) getApplication();
                        //保存一个长连接
                        app.setMyConn(conn);
                        // 保存好友列表的json串
                        app.setBuddyListJson(msg.content);
                        Gson gson = new Gson();
                        ContactInfoList Alist = gson.fromJson(msg.content,
                                ContactInfoList.class);
                        // 保存当前登录用户的账号
                        app.setMyNumber(Long.parseLong(username));

                        app.setMyName(msg.fromName);
                        app.setMyPassword(password);
                        // 打开主页
                        app.setstate(true);

                    } else if (AMessageType.MSG_TYPE_GROUPLIST.equals(msg.type)) {
                        app.setGroupListJson(msg.content);
                    } else if (AMessageType.MSG_TYPE_PLANLIST.equals(msg.type)) {
                        app.setPlanListJson(msg.content);
                        finish();
                    } else if (AMessageType.MSG_TYPE_FAIL.equals(msg.type)) {
                        Toast.makeText(getBaseContext(), msg.content, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "出现异常，请检查后重试", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
}
