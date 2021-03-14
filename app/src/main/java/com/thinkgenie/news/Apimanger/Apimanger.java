package com.thinkgenie.news.Apimanger;

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thinkgenie.news.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Apimanger {
    private Activity activity;
    public Apimanger(Activity activity) {
        this.activity = activity;
    }

    public void RequestWithObject(int Method, String url, JSONObject Incomingparams, VolleyInterface volleyInterface) {
        Log.e("eeeeee",Incomingparams.toString());
        // initialize RequestQueue
        RequestQueue mRequestQueue = Volley.newRequestQueue(activity);





        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method, url,Incomingparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        volleyInterface.onSuccess(response.toString());
                        Log.e("loginobject###",""+response.toString());


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("QWERTY", error.toString());
                parseVolleyError(error, volleyInterface);

            }

        });
        mRequestQueue.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                // Log.d("errorvaloyyer", ":::" + error);

            }
        });
    }

    private void parseVolleyError(VolleyError error, VolleyInterface volleyInterface) {
        try {
            if (error instanceof TimeoutError) {
                volleyInterface.onError(activity.getResources().getString(R.string.please_try_again), 0);
            } else {
                //  Log.i("QWERTY", "::::::"+error.networkResponse.data.length);
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {

                    if (error.networkResponse.data.length != 0) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        JSONObject data = new JSONObject(responseBody);
                        Log.i("QWERTY", data.toString());
                        String message = data.getString("Message");
//                        String statusCode = data.getString("statusCode");
           //             volleyInterface.onError(message, Integer.parseInt(statusCode));

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void Request(int Method, String url, Map<String, String> Incomingparams, VolleyInterface volleyInterface) {

        // initialize RequestQueue
        RequestQueue mRequestQueue = Volley.newRequestQueue(activity);

//        if (mRequestQueue != null) {
//            mRequestQueue.cancelAll(this);
//        }
        //  widgets.progressDialog(activity);
        Log.e("loginobject###",""+url);


        StringRequest mStringRequest = new StringRequest(Method,  url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    volleyInterface.onSuccess(response);
                Log.e("loginobject###",""+response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                parseVolleyError(error, volleyInterface);
                //     widgets.dismissProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params = Incomingparams;
                return params;
            }


        };
//        mStringRequest.setTag(Apicalled);
        mRequestQueue.add(mStringRequest);
    }


}
