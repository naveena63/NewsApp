package com.thinkgenie.news.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thinkgenie.news.Login.LoginActivity;
import com.thinkgenie.news.OTP.OtpActivity;
import com.thinkgenie.news.R;

public class SignupActivity extends AppCompatActivity {
Button next_btn;
TextView goto_login_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        next_btn=findViewById(R.id.next_btn);
        goto_login_tv=findViewById(R.id.goto_login_tv);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupActivity.this, OtpActivity.class);
                startActivity(intent);
            }
        });goto_login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}