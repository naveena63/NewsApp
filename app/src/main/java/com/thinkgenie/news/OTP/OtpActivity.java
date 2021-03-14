package com.thinkgenie.news.OTP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;
import com.thinkgenie.news.Activities.BottomNavigationActivity;
import com.thinkgenie.news.FillProfile.FillProfileActivity;
import com.thinkgenie.news.Helpers.SessionManager;

import com.thinkgenie.news.Login.LoginActivity;
import com.thinkgenie.news.Login.LoginResponse;
import com.thinkgenie.news.R;
import com.thinkgenie.news.Utilities.Utility;
import com.thinkgenie.news.consstrains.Constarins;
import com.thinkgenie.news.databinding.ActivityOtpBinding;
import com.thinkgenie.news.interfaces.EActivity;

import java.util.HashMap;
import java.util.Map;

import static com.thinkgenie.news.consstrains.Constarins.MyPREFERENCES;
import static com.thinkgenie.news.consstrains.Constarins.USERLOGIN;

public class OtpActivity extends AppCompatActivity implements EActivity {
    ActivityOtpBinding activityOtpBinding;
    RequestQueue requestQueue;
    SharedPreferences sharedpreferences;
    private final int MY_SOCKET_TIMEOUT_MS = 15000;
    String mobilenumber, MessageOTP;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOtpBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
      //  MessageOTP = getIntent().getStringExtra("MessageOTP");
        mobilenumber = getIntent().getStringExtra("mobile");
        Log.e("mobile_number", "mobile" + mobilenumber);

    //    Log.e("Otp", "otp" + MessageOTP);
        activityOtpBinding.createAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        sessionManager = new SessionManager(getApplicationContext());
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        activityOtpBinding.submitOtpBtn.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(USERLOGIN, "logindone");
            editor.apply();
            String et_otp = activityOtpBinding.etOtp.getText().toString().trim();
            if (et_otp.isEmpty()) {
                Toast.makeText(OtpActivity.this, "Please enter Otp", Toast.LENGTH_SHORT).show();
                // showSnackBar("Please enter Otp", 2);
            } else {

                sendOTPtoPhoneRequest();


            }


        });
    }

    private void sendOTPtoPhoneRequest() {


        if (!Utility.isNetworkAvailable(this)) {
            showSnackBar(Constarins.PLEASE_CHECK_INTERNET, 2);
            return;
        }
        Utility.showProgress(this);
        StringRequest stringRequestForOTP = new StringRequest(Request.Method.POST, Constarins.LOGIN_URL + "mobile=" + mobilenumber,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utility.hideProgress(OtpActivity.this);
                        LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
                        Log.i("otprepsonse", "otpresponse" + response);
                        if (loginResponse.getStatus().equals(true)) {
                            Long user_id = loginResponse.getId();
                            String tagname = loginResponse.getTagname();
                            Log.e("Login usdreid", "Login usreid " + user_id);
                            sessionManager.useridstoreset(String.valueOf(user_id));

//                                 if (user_id.equals("id")) {
//
//                                     Intent intent = new Intent(OtpActivity.this, BottomNavigationActivity.class);
//                                     intent.putExtra("mobile", mobilenumber);
//                                     startActivity(intent);
//                                 }
//                                 else{
//                                     Intent intent = new Intent(OtpActivity.this, FillProfileActivity.class);
//                                     intent.putExtra("mobile", mobilenumber);
//                                     startActivity(intent);
//                                 }


                        } else {
                            Intent intent = new Intent(OtpActivity.this, FillProfileActivity.class);
                            intent.putExtra("mobile", mobilenumber);
                            startActivity(intent);
                             Toast.makeText(OtpActivity.this, "success", Toast.LENGTH_SHORT).show();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress(OtpActivity.this);
                showSnackBar(Constarins.SOMETHING_WENT_WRONG_IN_SERVER, 3);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobilenumber);
                return params;
            }
        };
        stringRequestForOTP.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequestForOTP);

    }

    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(this, activityOtpBinding.coordinator, snackBarText, type);

    }

    @Override
    public Activity activity() {
        return this;
    }


}