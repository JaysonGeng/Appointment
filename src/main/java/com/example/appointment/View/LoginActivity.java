package com.example.appointment.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointment.Dao.OnClickView;
import com.example.appointment.R;
import com.example.appointment.Util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initEvent();
    }

    private void initView(){
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

    private void initEvent(){
        animationSet = new AnimationSet(true);
        alphaAnimation = new AlphaAnimation(0,1);
        translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setStartOffset(300);
        translateAnimation.setDuration(1500);
        translateAnimation.setStartOffset(300);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        content.startAnimation(animationSet);

        //实现记住密码和自动登录功能
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password",false);
        boolean isAutoLogin = pref.getBoolean("auto_login",false);
        if(isAutoLogin){
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
        if(isRemember){
            String username = pref.getString("username","");
            String password = pref.getString("password","");
            usernameInputting.setText(username);
            passwordInputting.setText(password);
            rememberPass.setChecked(true);
        }

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){

        Intent intent =new Intent();
        final SharedPreferences.Editor editor = pref.edit();

        switch (view.getId()){
            case R.id.login_button_Login:
                OnClickView.click_big(login);
                String username = usernameInputting.getText().toString();
                String password = passwordInputting.getText().toString();
                final RequestBody requestBody = new FormBody.Builder()
                        .add("username",username)
                        .add("password",password)
                        .add("","login")
                        .build();
                HttpUtil.sendOkHttpRequest("/Appointment/LoginServlet",requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(LoginActivity.this,"服务器连接失败",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        switch (responseData){
                            case "Y":
                                editor.putBoolean("valid",true);
                                editor.apply();
                                break;
                            case "N":
                                editor.putBoolean("valid",false);
                                editor.apply();
                                break;
                            default:
                        }
                    }
                });

                if(pref.getBoolean("valid",true)){
                    if (rememberPass.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("username",username);
                        editor.putString("password",password);
                    }
                    else editor.putBoolean("remember_password",false);
                    if(autoLogin.isChecked()){
                        editor.putBoolean("auto_login",true);
                    }
                    else editor.putBoolean("auto_login",false);
                    editor.putString("account",username);
                    editor.apply();
                    animationSet = new AnimationSet(true);
                    alphaAnimation = new AlphaAnimation(1,0);
                    alphaAnimation.setDuration(1600);
                    animationSet.addAnimation(alphaAnimation);
                    transition.startAnimation(animationSet);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            transition.setVisibility(View.GONE);
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                    },1600);
                }
                else {
                    Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_text_view_Login:
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forget_password_text_view_Login:
                intent.setClass(LoginActivity.this,FindPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }
}
