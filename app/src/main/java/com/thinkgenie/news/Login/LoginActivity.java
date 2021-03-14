package com.thinkgenie.news.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
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
import com.thinkgenie.news.OTP.OtpActivity;
import com.thinkgenie.news.OTP.OtpResponse;
import com.thinkgenie.news.R;
import com.thinkgenie.news.Utilities.Utility;
import com.thinkgenie.news.consstrains.Constarins;
import com.thinkgenie.news.databinding.ActivityLoginBinding;
import com.thinkgenie.news.interfaces.EActivity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements EActivity {
    ActivityLoginBinding loginBinding;
    RequestQueue requestQueue;
    private String mobileNumber;
    private int MY_SOCKET_TIMEOUT_MS = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        loginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNumber = loginBinding.phoneNumberEt.getText().toString().trim();
                if (mobileNumber.isEmpty()) {
                    showSnackBar("Please enter mobile number", 2);
                } else if (!(mobileNumber.startsWith("9") || mobileNumber.startsWith("8") || mobileNumber.startsWith("7") ||
                        mobileNumber.startsWith("6") && mobileNumber.length() == 10)) {
                    showSnackBar("Please enter valid mobile number", 3);
                } else  {
                    // sendOTPtoPhoneRequest();
                    sendSms();

                }

            }
        });
    }



    private void sendSms() {

        if (!Utility.isNetworkAvailable(this)) {
            showSnackBar(Constarins.PLEASE_CHECK_INTERNET, 2);
            return;
        }
        Utility.showProgress(this);

        StringRequest stringRequestForOTP = new StringRequest(Request.Method.POST, Constarins.OTP + "mobile=" + mobileNumber,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utility.hideProgress(LoginActivity.this);
                        OtpResponse otpResponse = new Gson().fromJson(response.toString(), OtpResponse.class);

                        if (otpResponse.getErrorMessage().equals("Success")) {

                            String otp = otpResponse.getMessageOTP();
                            Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                            intent.putExtra("MessageOTP", otp);
                            intent.putExtra("mobile", mobileNumber);
                            startActivity(intent);
                            showSnackBar(otpResponse.getErrorMessage(), 2);
                            Log.e("otp Response", "otp response " + response);
                        }
                        else{
                            showSnackBar("failed", 2);
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress(LoginActivity.this);

                showSnackBar(Constarins.SOMETHING_WENT_WRONG_IN_SERVER, 3);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        stringRequestForOTP.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequestForOTP);

    }


    @Override
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(this, loginBinding.coordinator, snackBarText, type);

    }

    @Override
    public Activity activity() {
        return this;
    }


}