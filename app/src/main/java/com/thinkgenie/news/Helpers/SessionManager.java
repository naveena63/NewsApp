package com.thinkgenie.news.Helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.thinkgenie.news.Login.LoginActivity;

import java.util.HashMap;

public class SessionManager {


    SharedPreferences pref;

    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Newsapp_thinkg";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_USER_DP = "DP";
    public static final String KEY_NAME = "name";
    public static final String KEY_TAGNAME = "tagname";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_USERID = "userid";
    public static final String KEY_USERID1 = "userid1";
    public static final String KEY_PINCODE = "pincode";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_NEWSINPUTID = "inputid";
    public static final String KEY_state = "er";
    public static final String KEY_distruict = "inpufetid";
    public static final String KEY_mandal = "ee";
    public static final String KEY_village = "inpuwwtid";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(int id, String name, String tagname, String mobile, String dp, String pincode, String address,String state,String district,String village,String mandal) {
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PHONE, mobile);
        editor.putInt(KEY_USERID, id);
       // editor.putInt(KEY_NEWSINPUTID, newsInputid);
        editor.putString(KEY_TAGNAME, tagname);
        editor.putString(KEY_USER_DP, dp);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_state, state);
        editor.putString(KEY_distruict, district);
        editor.putString(KEY_mandal, mandal);
        editor.putString(KEY_village, village);

        editor.commit();
    }  public void createNewsSession(int newsInputid) {

        editor.putInt(KEY_NEWSINPUTID, newsInputid);


        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USER_DP, pref.getString(KEY_USER_DP, " "));
        user.put(KEY_NAME, pref.getString(KEY_NAME, " "));
        user.put(KEY_TAGNAME, pref.getString(KEY_TAGNAME,  " "));
//;        user.put(KEY_USERID, String.valueOf(pref.getInt(KEY_USERID, 0)));
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, " "));
        user.put(KEY_PINCODE, pref.getString(KEY_PINCODE, " "));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, " "));
        user.put(KEY_state, pref.getString(KEY_state, " "));
        user.put(KEY_mandal, pref.getString(KEY_mandal, " "));
        user.put(KEY_distruict, pref.getString(KEY_distruict, " "));
        user.put(KEY_village, pref.getString(KEY_village, " "));
       // user.put(KEY_NEWSINPUTID, (pref.getString(KEY_NEWSINPUTID, "")));

        return user;
    }

    public boolean isLoggedin() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void useridstoreset(String user){
        editor.putString(KEY_USERID1,user);
        editor.commit();

    }
    public String useridstoreget(){
       return pref.getString(KEY_USERID1,"");
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

}
