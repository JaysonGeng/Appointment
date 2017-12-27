package com.example.appointment.View;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appointment.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText origin_password;
    private EditText new_password;
    private EditText new_password_again;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initView();
        initEvent();
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar_ChangePassword);
        origin_password = findViewById(R.id.origin_password_edit_text_ChangePassword);
        new_password = findViewById(R.id.new_password_edit_text_ChangePassword);
        new_password_again = findViewById(R.id.new_password_again_edit_text_ChangePassword);
        change = findViewById(R.id.change_button_ChangePassword);
    }

    private void initEvent(){
        //加载工具栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String origin_password_content = origin_password.getText().toString();
                String new_password_content = new_password.getText().toString();
                String new_password_again_content = new_password_again.getText().toString();
                Toast.makeText(ChangePasswordActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(ChangePasswordActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
}
