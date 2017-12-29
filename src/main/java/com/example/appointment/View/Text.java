package com.example.appointment.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.appointment.R;

/**
 * Created by MichaelOD on 2017/12/26.
 */

public class Text extends Activity
{
    private TextView message;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        message=(TextView)findViewById(R.id.text_content);

        //获得Intent
        Intent intent = this.getIntent();
        //从Intent获得额外信息，设置为TextView的文本
        message.setText(intent.getStringExtra("message"));
    }
}