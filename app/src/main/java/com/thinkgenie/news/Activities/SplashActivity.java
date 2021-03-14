package com.thinkgenie.news.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.thinkgenie.news.FillProfile.FillProfileActivity;
import com.thinkgenie.news.Login.LoginActivity;
import com.thinkgenie.news.R;

import static com.thinkgenie.news.consstrains.Constarins.MyPREFERENCES;
import static com.thinkgenie.news.consstrains.Constarins.MyProfile;
import static com.thinkgenie.news.consstrains.Constarins.USERLOGIN;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences,sharedpreferences1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences1 = getSharedPreferences(MyProfile, Context.MODE_PRIVATE);
        try {
            //android O fix bug orientation
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        goToNextScreen();
    }

    private void goToNextScreen() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(SplashActivity.this, BottomNavigationActivity.class);
//                       startActivity(intent);

                if (sharedpreferences.getString(USERLOGIN, null) == null) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (sharedpreferences1.getString("profileDone","0").equals("profileDone")){
                        Intent intent = new Intent(SplashActivity.this, BottomNavigationActivity.class);
                        startActivity(intent);
                        finish();
                    }else
                    {
                        Intent intent = new Intent(SplashActivity.this, FillProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        }, 1500);
    }
}