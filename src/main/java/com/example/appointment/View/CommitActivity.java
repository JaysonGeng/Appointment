package com.example.appointment.View;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appointment.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private Uri imageUri;
    private ImageView activity_picture;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private String imagePath;

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
        activity_picture = findViewById(R.id.activity_picture_show_image_view_Commit);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(CommitActivity.this);
                dialog.setTitle("选择图片");
                dialog.setMessage("拍照或从手机相册中选择");
                dialog.setCancelable(true);
                dialog.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                        try{
                            if(outputImage.exists()){
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        if(Build.VERSION.SDK_INT>=24){
                            imageUri = FileProvider.getUriForFile(CommitActivity.this,"com.example.appointment.fileprovider",outputImage);
                        }else {
                            imageUri = Uri.fromFile(outputImage);
                        }
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,TAKE_PHOTO);
                    }
                });
                dialog.setNegativeButton("从手机相册中选择", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(ContextCompat.checkSelfPermission(CommitActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(CommitActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }else {
                            openAlbum();
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.commit_button_Commit:
                if(isInputValid()){
                    //
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("image",imagePath);
                    editor.apply();
                    Toast.makeText(CommitActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
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
                if(imagePath != null){
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
                    Toast.makeText(CommitActivity.this,"请选择一张图片",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        activity_picture.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageBeforeKitKat(data);
                    }else {

                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permission,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(CommitActivity.this,"没有相册权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null ,null );
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                imagePath = path;
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            activity_picture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(CommitActivity.this,"图片获取失败",Toast.LENGTH_SHORT).show();
        }
    }

}
