package com.example.appointment.View;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.appointment.Dao.OnClickView;
import com.example.appointment.R;

public class LogoActivity extends AppCompatActivity{

    private ImageView logo;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        initView();
        initEvent();
    }

    private void initView(){
        logo = findViewById(R.id.logo_image_view_Logo);
    }

    private void initEvent(){

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        player= MediaPlayer.create(LogoActivity.this, R.raw.logo2);
        player.start();

        OnClickView.logo(logo);

        //设置欢迎页在打开应用6秒后消失
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                player.stop();
                Intent intent = new Intent();
                intent.setClass(LogoActivity.this,UserAgreementActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        },6000);
    }
}
