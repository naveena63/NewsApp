package com.thinkgenie.news.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.thinkgenie.news.FillProfile.FillProfileActivity;
import com.thinkgenie.news.Helpers.SessionManager;
import com.thinkgenie.news.Login.LoginActivity;
import com.thinkgenie.news.R;

import java.io.StringWriter;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    TextView profile_toolbar, username_tv,tagName_tv,mobileNum_tv,address_tv,pincode_tv;
    TextView logout,edit_tv;

    private SessionManager sessionManager;
    String profile_pic, user_name, tag_name, mobile, user_id,pincode,address,state,district,mandal,village;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_toolbar = findViewById(R.id.profile_toolbar);
        username_tv = findViewById(R.id.username_tv);
        tagName_tv = findViewById(R.id.tagName_tv);
        mobileNum_tv = findViewById(R.id.mobileNum_tv);
        address_tv = findViewById(R.id.address_tv);
        pincode_tv = findViewById(R.id.pincode_tv);
        edit_tv = findViewById(R.id.edit);

        logout = findViewById(R.id.logout);

       String user_name_in=getIntent().getStringExtra("user_name");

        sessionManager = new SessionManager(ProfileActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        profile_pic = user.get(SessionManager.KEY_USER_DP);
        user_name = user.get(SessionManager.KEY_NAME);
        tag_name = user.get(SessionManager.KEY_TAGNAME);
        mobile = user.get(SessionManager.KEY_PHONE);
        pincode = user.get(SessionManager.KEY_PINCODE);
        address = user.get(SessionManager.KEY_ADDRESS);
        state = user.get(SessionManager.KEY_state);
        district = user.get(SessionManager.KEY_distruict);
        mandal = user.get(SessionManager.KEY_mandal);
        village = user.get(SessionManager.KEY_village);

        username_tv.setText(user_name);
        tagName_tv.setText(tag_name);
        mobileNum_tv.setText(mobile);
        pincode_tv.setText(pincode);
        address_tv.setText(address);


edit_tv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(ProfileActivity.this, FillProfileActivity.class);
        startActivity(intent);
    }
});
        profile_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, BottomNavigationActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logoutUser();
                finish();
            }
        });


    }
}