package com.example.appointment.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.appointment.R;

public class UserAgreementActivity extends AppCompatActivity {

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);

        initView();
        initEvent();
    }

    private void initView(){
        radioGroup = findViewById(R.id.choice_radio_group_UserAgreement);
    }

    private void initEvent(){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.BLACK);
        }

        SharedPreferences preferences = getSharedPreferences("choice", Context.MODE_PRIVATE);
        int choice = preferences.getInt("choice",0);
        if(choice==R.id.accept_radio_button_UserAgreement){
            Intent intent = new Intent();
            intent.setClass(UserAgreementActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int choice = group.getCheckedRadioButtonId();
                switch (choice){
                    case R.id.accept_radio_button_UserAgreement:
                        SharedPreferences preferences = getSharedPreferences("choice", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("choice", R.id.accept_radio_button_UserAgreement);
                        editor.apply();
                        Intent intent = new Intent();
                        intent.setClass(UserAgreementActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.decline_radio_button_UserAgreement:
                        finish();
                        break;
                }
            }
        });
    }
}
