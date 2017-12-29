package com.example.appointment.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appointment.R;
import com.example.appointment.Util.HttpUtil;
import com.example.appointment.core.AConnection;
import com.example.appointment.core.AConnection.OnMessageListener;
import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageType;
import com.example.appointment.message.ThreadUtils;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



public class RegisterActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private List<String> campus_list = new ArrayList<>();
    private List<String> sex_list = new ArrayList<>();
    private Toolbar toolbar;
    private EditText student_id;
    private String username;
    private String passwd;
    private EditText name;
    private EditText password;
    private EditText password_again;
    private EditText mailbox_address;
    private EditText nickname;
    private Spinner campus;
    private Spinner sex;
    private String txt;
    private ArrayAdapter<String> campus_adapter;
    private ArrayAdapter<String> sex_adapter;
    AConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        campus_list.add("");
        campus_list.add("中心校区");
        campus_list.add("软件园校区");
        campus_list.add("兴隆山校区");
        campus_list.add("洪家楼校区");
        campus_list.add("千佛山校区");
        campus_list.add("趵突泉校区");
        sex_list.add("");
        sex_list.add("男");
        sex_list.add("女");
        campus_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, campus_list);
        campus_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, sex_list);
        sex_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        initView();
        initEvent();

        ThreadUtils.runInSubThread(new Runnable() {
            public void run() {
                try {
                    //这里的IP地址一定要注意改成电脑的地址
                    conn = new AConnection("202.194.15.234", 8088);// Socket
                    conn.connect();// 建立连接
                    // 建立连接之后，将监听器添加到连接里面
                    conn.addOnMessageListener(listener);
                }catch(Exception e)
                {
                    ThreadUtils.runInUiThread(new Runnable() {

                        public void run() {
                            Toast.makeText(getBaseContext(), "网络连接故障！", Toast.LENGTH_SHORT).show();                        }
                    });
                }
            }
        });

    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar_Register);
        student_id = findViewById(R.id.student_id_edit_text_Register);
        name = findViewById(R.id.name_edit_text_Register) ;
        password = findViewById(R.id.password_edit_text_Register);
        password_again = findViewById(R.id.password_again_edit_text_Register);
        mailbox_address = findViewById(R.id.mailbox_address_edit_text_Register);
        campus = findViewById(R.id.campus_spinner_Register);
        sex = findViewById(R.id.sex_spinner_Register);
        nickname = findViewById(R.id.nickname_edit_text_Register);
    }

    private void initEvent(){
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.putString("campus","");
        editor.putString("sex","");
        editor.apply();

        //加载工具栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        campus.setAdapter(campus_adapter);
        campus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String campus = campus_list.get(position);
                editor = pref.edit();
                editor.putString("campus",campus);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                editor = pref.edit();
                editor.putString("campus",null);
                editor.apply();
            }
        });

        sex.setAdapter(sex_adapter);
        sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String sex = sex_list.get(position);
                editor = pref.edit();
                editor.putString("sex",sex);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                editor = pref.edit();
                editor.putString("sex",null);
                editor.apply();
            }
        });
    }

    //加载toolbar菜单文件
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_register,menu);
        return true;
    }

    //设置工具栏按钮的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.finish:
                if (isinputvalid()){
                    if(ispasswordconsistent()){
                        username = name.getText().toString();
                        passwd = password.getText().toString();  //账号密码

                            ThreadUtils.runInSubThread(new Runnable() {
                                public void run() {
                                    try {
                                        AMessage msg = new AMessage();
                                        msg.type = AMessageType.MSG_TYPE_ZHUCE;
                                        msg.content = username + "#" + passwd;
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

                    }
                    else {
                        Toast.makeText(RegisterActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private boolean isinputvalid(){
        boolean a = Pattern.matches("^[0-9]{5,20}$",student_id.getText().toString());
        boolean b = Pattern.matches("^[\u4e00-\u9fa5]{2,}$",name.getText().toString());
        boolean c = !pref.getString("campus","").equals("");
        boolean d = !pref.getString("sex","").equals("");
        boolean e = !nickname.getText().toString().equals("");
        boolean f = !password.getText().toString().equals("");
        boolean g = Pattern.matches("^([a-zA-Z0-9]+[-|_|\\.]?)+[a-zA-Z0-9]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",mailbox_address.getText().toString());
        if(a){
            if(b){
                if(c){
                    if(d){
                        if(e){
                            if(f){
                                if(g){
                                    return true;
                                }else {
                                    Toast.makeText(RegisterActivity.this,"邮箱格式不正确",Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            }else {
                                Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }else {
                            Toast.makeText(RegisterActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }else {
                        Toast.makeText(RegisterActivity.this,"请选择你的性别",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Toast.makeText(RegisterActivity.this,"请选择你的校区",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(RegisterActivity.this,"姓名填写有误",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(RegisterActivity.this,"学号填写有误",Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private boolean ispasswordconsistent(){
        return password_again.getText().toString().equals(password.getText().toString());

    }

    protected void onDestroy() {
        super.onDestroy();
        // activity销毁的时候取消监听
        conn.removeOnMessageListener(listener);
    }

    private OnMessageListener listener = new OnMessageListener() {

        public void onReveive(final AMessage msg) {

            ThreadUtils.runInUiThread(new Runnable() {

                public void run() {
                    if (AMessageType.MSG_TYPE_SUCCESS.equals(msg.type)) {
                        txt="注册成功！您的帐号为： "+msg.content;
                        Toast.makeText(RegisterActivity.this,txt,Toast.LENGTH_LONG).show();
                        try{
                            conn.disConnect();
                            finish();
                        }
                        catch(IOException e){
                            Toast.makeText(getBaseContext(), "出现异常，请检查后后重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    };

}
