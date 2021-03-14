package com.thinkgenie.news.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreference {
    //keys
    public static final String mobile_number = "mobilenumber";
    public static final String fcm_token = "FCM_Token";
    public static final String login = "LOGIN";
    public static final String TOKEN="TOKEN";
    public static final String LOGO="LOGO";

    public static final String LOGINSTATUS="LOGINSTATUS";

    public static final String REGI_STATUS="REGI_STATUS";
    public static final String NAV_PAGE="NAV_PAGE";
    public static final String DOC_PAGE="DOC_PAGE";

    public static final String PROFILE_PERCENTAGE="PROFILE_PERCENTAGE";

    public static String getStringPreference(Context context, String key) {
        String value = null;
        if (context != null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (preferences != null) {
                value = preferences.getString(key, null);
            }
        }

        return value;
    }

    public static boolean setStringPreference(Context context, String key, String value) {
        if (context != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (preferences != null && !TextUtils.isEmpty(key)) {
                if (value == null && key.equals("Profilepic")) {
                    return true;
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(key, value);
                return editor.commit();
            }
        }

        return false;
    }


    public static void saveArrayList(Context context, String list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }

    public static ArrayList<String> getArrayList(Context context, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

}
