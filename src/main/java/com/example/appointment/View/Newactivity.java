package com.example.appointment.View;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;


import com.example.appointment.R;

public class Newactivity extends AppCompatActivity {

    private WebView webView;
    private ImageView back;
    private long userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newactivity);
        back = findViewById(R.id.back_newactivity);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        userid = intent.getLongExtra("userid", 0);

        webView = findViewById(R.id.web_newactivity);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://39.107.228.179:8080/Blog1_war/NewEvent?userid=" + userid);

    }

}
