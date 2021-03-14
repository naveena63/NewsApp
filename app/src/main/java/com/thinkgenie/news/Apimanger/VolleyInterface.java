package com.thinkgenie.news.Apimanger;

public interface VolleyInterface {
    void onSuccess(String result);
    void onError(String error, int statusCode);

}