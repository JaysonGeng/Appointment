package com.example.appointment.View;

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

import com.example.appointment.R;

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
                finish();
                break;
        }
    }
}
