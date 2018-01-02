package com.example.appointment.View;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appointment.R;

import java.util.Calendar;
import java.util.regex.Pattern;

public class CommitActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private EditText title;
    private EditText year;
    private EditText month;
    private EditText day;
    private EditText place;
    private EditText detail;
    private ImageView select_photo;
    private Button commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit);

        initView();
        initEvent();
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar_Commit);
        title = findViewById(R.id.activity_title_edit_text_Commit);
        year = findViewById(R.id.year_edit_text_Commit);
        month = findViewById(R.id.month_edit_text_Commit);
        day = findViewById(R.id.day_edit_text_Commit);
        place = findViewById(R.id.activity_place_edit_text_Commit);
        detail = findViewById(R.id.detail_edit_text_Commit);
        select_photo = findViewById(R.id.select_photo_image_view_Commit);
        commit = findViewById(R.id.commit_button_Commit);
    }

    private void initEvent(){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.BLACK);
        }

        //加载工具栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        select_photo.setOnClickListener(this);
        commit.setOnClickListener(this);
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

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.select_photo_image_view_Commit:
                break;
            case R.id.commit_button_Commit:
                if(isInputValid()){
                    //
                    finish();
                }
                break;
        }
    }

    private boolean isInputValid(){
        boolean a = Pattern.matches("^[\u4e00-\u9fa50-9a-zA-Z]{2,}$",title.getText().toString());
        boolean b = !place.getText().toString().equals("");
        if(a){
            if (b){
                try {
                    int y = Integer.parseInt(year.getText().toString());
                    int m = Integer.parseInt(month.getText().toString());
                    int d = Integer.parseInt(day.getText().toString());
                    return isDateValid(y,m,d);
                }catch (NumberFormatException e){
                    Toast.makeText(CommitActivity.this,"日期输入有误",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(CommitActivity.this,"请输入活动地点",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(CommitActivity.this,"活动名不规范",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isDateValid(int y,int m,int d){
        Calendar now = Calendar.getInstance();
        if(y>now.get(Calendar.YEAR)){
            if(y==now.get(Calendar.YEAR)+1){
                if(m>0&&m<13){
                    if (d>0&&d<32){
                        return true;
                    }else {
                        Toast.makeText(CommitActivity.this,"日期输入有误",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Toast.makeText(CommitActivity.this,"日期输入有误",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(CommitActivity.this,"日期过于遥远",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            if (y==now.get(Calendar.YEAR)){
                if(m>0&&m<13){
                    if(m>now.get(Calendar.MONTH)){
                        if (d>0&&d<32){
                            return true;
                        }else {
                            Toast.makeText(CommitActivity.this,"日期输入有误",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }else {
                        if(m==now.get(Calendar.MONTH)){
                            if(d>=now.get(Calendar.DATE)&&d<32){
                                return true;
                            }else {
                                Toast.makeText(CommitActivity.this,"日期输入有误",Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }else {
                            Toast.makeText(CommitActivity.this,"日期输入有误",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                }else {
                    Toast.makeText(CommitActivity.this,"日期输入有误",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(CommitActivity.this,"日期输入有误",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

}
