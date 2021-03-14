package com.thinkgenie.news.FillProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.gson.reflect.TypeToken;
import com.thinkgenie.news.Activities.BottomNavigationActivity;
import com.thinkgenie.news.Activities.ProfileActivity;
import com.thinkgenie.news.Apimanger.Apimanger;
import com.thinkgenie.news.Apimanger.VolleyInterface;
import com.thinkgenie.news.Helpers.SessionManager;
import com.thinkgenie.news.R;
import com.thinkgenie.news.Utilities.SharedPreference;
import com.thinkgenie.news.Utilities.Utility;
import com.thinkgenie.news.consstrains.Constarins;
import com.thinkgenie.news.databinding.ActivityFillProfileBinding;
import com.thinkgenie.news.interfaces.EActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.thinkgenie.news.Utilities.Apicontant.POST;
import static com.thinkgenie.news.consstrains.Constarins.MyPREFERENCES;
import static com.thinkgenie.news.consstrains.Constarins.MyProfile;
import static com.thinkgenie.news.consstrains.Constarins.USERLOGIN;
import static com.thinkgenie.news.consstrains.Constarins.prfoileDone;

public class FillProfileActivity extends AppCompatActivity implements EActivity {

    ActivityFillProfileBinding fillProfileBinding;
    private static final int MY_SOCKET_TIMEOUT_MS = 15000;
    RequestQueue requestQueue;
    private Dialog dialog;
    StateAdapter stateAdapter;
    List<StateDataResponse> stateDataResponseList;
    private static final int PICK_IMAGE_REQUEST = 0;
    private Uri mImageUri;
    Bitmap bitmapImage;

    private CircleImageView profileImage;
    //    String stateId;
//    String districtId;
//    String mandalId;
    String path, encodedImage, defaulImage, stateName, districtName, mandalName, villageName;
    Integer languageID, stateId, districtId, mandalId, villageId;
    DistirctAdapter districtAdapter;
    List<DistrictResponse> districtResponseList;
    VillageAdapter villageAdapter;
    List<VillageResponse> villageResponseList;

    MandalAdapter mandalAdapter;
    List<MandalResponse> mandalResponseList;
    LanguageAdapter languageAdapter;
    List<LanguageResposne> languageResposneList;
    String fullName, tagName, pincode, mobileNumber, state, district, mandal, village, langugage;
    String name,
            mobile,
            email,
            password,
            tagname,
            languageid,
            confirmcode,
            status,
            imageurl,
            isverified,
            usertype, stateid,
            districtid,
            mandalid,
            villageid,
            pincodes;
    Apimanger apimanger;
    public static JSONObject dataobjcam;
    SharedPreferences sharedPreference;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_fill_profile);

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreference = getSharedPreferences(MyProfile, Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);
        apimanger = new Apimanger(this);
        mobileNumber = getIntent().getStringExtra("mobile");

        profileImage = findViewById(R.id.profile_image);

        sessionManager = new SessionManager(FillProfileActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();

        fullName = user.get(SessionManager.KEY_NAME);
        tagName = user.get(SessionManager.KEY_TAGNAME);
        mobile = user.get(SessionManager.KEY_PHONE);
        pincode = user.get(SessionManager.KEY_PINCODE);
//
//      fillProfileBinding.fullName.setText(fullName);
//
//      fillProfileBinding.tagName.setText(tagName);
//
//      fillProfileBinding.pincode.setText(pincode);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        fillProfileBinding.relativeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateDropdown();
            }
        });
        fillProfileBinding.relativeDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                districtDropDown();
            }
        });
        fillProfileBinding.relativeMandal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mandalDropDown();
            }
        });
        fillProfileBinding.languageRelatve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageDropdown();
            }
        });
        fillProfileBinding.relativeVillage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                villageDropdown();
            }
        });
        fillProfileBinding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelect();
            }
        });
        fillProfileBinding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName = fillProfileBinding.fullName.getText().toString().trim();
                tagName = fillProfileBinding.tagName.getText().toString().trim();
                pincode = fillProfileBinding.pincode.getText().toString().trim();
                state = fillProfileBinding.selectState.getText().toString().trim();
                district = fillProfileBinding.selectDistrict.getText().toString().trim();
                mandal = fillProfileBinding.selectMandal.getText().toString().trim();
                village = fillProfileBinding.selectVillage.getText().toString().trim();
                langugage = fillProfileBinding.selectLanguage.getText().toString().trim();

                if (fullName.isEmpty()) {
                    showSnackBar("Please enter username", 2);
                } else if (langugage.equals(getResources().getString(R.string.language))) {
                    showSnackBar("Please Select Language", 2);
                } else {
                    dataobjcam = new JSONObject();
                    try {
                        dataobjcam.put("id", "12");
                        dataobjcam.put("usertype", 2);
                        dataobjcam.put("name", fullName);
                        dataobjcam.put("tagname", tagName);
                        dataobjcam.put("email", "");
                        dataobjcam.put("mobile", mobileNumber);
                        dataobjcam.put("password", "");
                        dataobjcam.put("confirmcode", "");
                        dataobjcam.put("status", "true");
                        dataobjcam.put("imageurl", defaulImage);

                        dataobjcam.put("isverified", "true");
                        dataobjcam.put("stateid", stateId);
                        dataobjcam.put("districtid", districtId);
                        dataobjcam.put("mandalid", mandalId);
                        dataobjcam.put("villageid", villageId);
                        dataobjcam.put("languageid", languageID);
                        dataobjcam.put("pincode", pincode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    saveProfile(dataobjcam);
                }

            }
        });
    }

    private void saveProfile(JSONObject datajson) {
        if (!Utility.isNetworkAvailable(this)) {
            showSnackBar(Constarins.PLEASE_CHECK_INTERNET, 2);
            return;
        }

        Utility.showProgress(this);
        apimanger.RequestWithObject(POST, Constarins.profile_saving, datajson, new VolleyInterface() {
            @Override
            public void onSuccess(String result) {
                Log.e("fillprofileresponse", result);
                Utility.hideProgress(FillProfileActivity.this);
                try {
                    SharedPreferences.Editor editor = sharedPreference.edit();
                    editor.putString(prfoileDone, "profileDone");
                    editor.commit();
                    int user_id = datajson.getInt("id");
                    Log.e("user_id","usreid"+user_id);
                    name = datajson.getString("name");

                    email = datajson.getString("email");
                    password = datajson.getString("password");
                    tagname = datajson.getString("tagname");
                    if (datajson.has("languageid")) {
                        languageid = datajson.getString("languageid");

                    }
                    confirmcode = datajson.getString("confirmcode");
                    status = datajson.getString("status");

                    if (datajson.has("imageurl")) {
                        imageurl = datajson.getString("imageurl");

                    } if (datajson.has("mobile")) {
                        mobile = datajson.getString("mobile");
                    }

                    isverified = datajson.getString("isverified");
                    usertype = datajson.getString("usertype");
                    if (datajson.has("stateid")) {
                        stateid = datajson.getString("stateid");

                    }
                    if (datajson.has("districtid")) {
                        districtid = datajson.getString("districtid");

                    }
                    if (datajson.has("mandalid")) {
                        mandalid = datajson.getString("mandalid");

                    }
                    if (datajson.has("villageid")) {
                        villageid = datajson.getString("villageid");

                    }
                    if (datajson.has("pincode")) {
                        pincodes = datajson.getString("pincode");

                    }


                    sessionManager.createLoginSession(user_id, name, tagname, mobile, imageurl, pincodes, stateName ,stateName,districtName,mandalName,villageName);
                    Intent intent = new Intent(FillProfileActivity.this, BottomNavigationActivity.class);

                    intent.putExtra("user_name", name);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, int statusCode) {
                Utility.hideProgress(FillProfileActivity.this);

            }
        });


    }

    private void villageDropdown() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.state_dropdownlist);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER_HORIZONTAL;
        window.setAttributes(wlp);
        dialog.show();
        dialog.setCancelable(false);
        TextView tv_warning = dialog.findViewById(R.id.tv_warning);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        final RecyclerView rcvStates = dialog.findViewById(R.id.rcvStates);
        TextView tv_nodata = dialog.findViewById(R.id.tv_nodata);

        tv_warning.setText("Select your Village");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvStates.setLayoutManager(layoutManager);
        if (!Utility.isNetworkAvailable(this)) {
            showSnackBar(Constarins.PLEASE_CHECK_INTERNET, 2);
            return;
        }
        Utility.showProgress(this);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.villages_dropdown + "mandalid=" + mandalId + "&htype=d", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.hideProgress(FillProfileActivity.this);
                Log.e("village Repsonse", "" + response);
                villageResponseList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<VillageResponse>>() {
                }.getType();
                List<VillageResponse> apiListvillage = gson.fromJson(response.toString(), listType);
                villageResponseList.addAll(apiListvillage);

                if (villageResponseList.size() > 0) {
                    villageAdapter = new VillageAdapter(FillProfileActivity.this, villageResponseList);
                    rcvStates.setAdapter(villageAdapter);
                    tv_nodata.setVisibility(View.GONE);
                    villageAdapter.setOnVillageClickListener(new VillageAdapter.onVillageClickListener() {
                        @Override
                        public void onVillageClickListner(VillageResponse villageResponse) {
                            villageId = villageResponse.getId();
                            Log.e("villageId", "" + villageId);
                            villageName = villageResponse.getName();
                            fillProfileBinding.selectVillage.setText(villageName);
                            dialog.dismiss();
                        }
                    });
                } else {
                    tv_nodata.setText("No Data Available");
                    tv_nodata.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress(FillProfileActivity.this);
                showSnackBar(Constarins.SOMETHING_WENT_WRONG_IN_SERVER, 3);
            }
        });
        requestQueue.add(stringRequest);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            }
        });
    }

    private void languageDropdown() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.state_dropdownlist);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER_HORIZONTAL;
        window.setAttributes(wlp);
        dialog.show();
        dialog.setCancelable(false);
        TextView tv_warning = dialog.findViewById(R.id.tv_warning);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        final RecyclerView rcvStates = dialog.findViewById(R.id.rcvStates);
        tv_warning.setText("Select your Language");
        TextView tv_nodata = dialog.findViewById(R.id.tv_nodata);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvStates.setLayoutManager(layoutManager);
        if (!Utility.isNetworkAvailable(this)) {
            showSnackBar(Constarins.PLEASE_CHECK_INTERNET, 2);
            return;
        }
        Utility.showProgress(this);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.language_dropdown, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.hideProgress(FillProfileActivity.this);
                Log.e("Language Repsonse", "" + response);
                languageResposneList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<LanguageResposne>>() {
                }.getType();
                List<LanguageResposne> apiListlanguagr = gson.fromJson(response.toString(), listType);
                languageResposneList.addAll(apiListlanguagr);
                if (languageResposneList.size() > 0) {
                    languageAdapter = new LanguageAdapter(FillProfileActivity.this, languageResposneList);
                    rcvStates.setAdapter(languageAdapter);
                    tv_nodata.setVisibility(View.GONE);
                    languageAdapter.setOnLanguageClickListner(new LanguageAdapter.onLanguageClickListner() {
                        @Override
                        public void onLanguageClickListner(LanguageResposne languageResposne) {
                            languageID = languageResposne.getId();
                            Log.e("languagId", "" + languageID);
                            String languageName = languageResposne.getName();
                            fillProfileBinding.selectLanguage.setText(languageName);
                            dialog.dismiss();
                        }
                    });
                } else {
                    tv_nodata.setText("No Data Available");
                    tv_nodata.setVisibility(View.VISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress(FillProfileActivity.this);
                showSnackBar(Constarins.SOMETHING_WENT_WRONG_IN_SERVER, 3);
            }
        });
        requestQueue.add(stringRequest);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            }
        });
    }

    private void mandalDropDown() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.state_dropdownlist);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER_HORIZONTAL;
        window.setAttributes(wlp);
        dialog.show();
        dialog.setCancelable(false);
        TextView tv_warning = dialog.findViewById(R.id.tv_warning);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        final RecyclerView rcvStates = dialog.findViewById(R.id.rcvStates);
        TextView tv_nodata = dialog.findViewById(R.id.tv_nodata);
        tv_warning.setText("Select your Mandal");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvStates.setLayoutManager(layoutManager);
        if (!Utility.isNetworkAvailable(this)) {
            showSnackBar(Constarins.PLEASE_CHECK_INTERNET, 2);
            return;
        }
        Utility.showProgress(this);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.mandal_dropdown + "districtsid=" + districtId + "&htype=d", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.hideProgress(FillProfileActivity.this);
                Log.e("mandal Repsonse", "" + response);
                mandalResponseList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<MandalResponse>>() {
                }.getType();
                List<MandalResponse> apiList2 = gson.fromJson(response.toString(), listType);
                mandalResponseList.addAll(apiList2);
                if (mandalResponseList.size() > 0) {
                    mandalAdapter = new MandalAdapter(FillProfileActivity.this, mandalResponseList);
                    rcvStates.setAdapter(mandalAdapter);
                    tv_nodata.setVisibility(View.GONE);
                    mandalAdapter.setOnMandalCliclListener(new MandalAdapter.OnMandalCliclListener() {
                        @Override
                        public void onMandalClickListener(MandalResponse mandalResponse) {
                            mandalId = mandalResponse.getId();
                            Log.e("distirctId", "" + stateId);
                            mandalName = mandalResponse.getName();
                            fillProfileBinding.selectMandal.setText(mandalName);
                            dialog.dismiss();
                        }
                    });
                } else {
                    tv_nodata.setText("No Data Available");
                    tv_nodata.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress(FillProfileActivity.this);
                showSnackBar(Constarins.SOMETHING_WENT_WRONG_IN_SERVER, 3);
            }
        });
        requestQueue.add(stringRequest);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            }
        });
    }

    private void districtDropDown() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.state_dropdownlist);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER_HORIZONTAL;
        window.setAttributes(wlp);
        dialog.show();
        dialog.setCancelable(false);
        TextView tv_warning = dialog.findViewById(R.id.tv_warning);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        final RecyclerView rcvDistrict = dialog.findViewById(R.id.rcvStates);
        TextView tv_nodata = dialog.findViewById(R.id.tv_nodata);
        tv_warning.setText("Select your District");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvDistrict.setLayoutManager(layoutManager);
        if (!Utility.isNetworkAvailable(this)) {
            showSnackBar(Constarins.PLEASE_CHECK_INTERNET, 2);
            return;
        }
        Utility.showProgress(this);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.district_dropdown + "stateid=" + stateId + "&htype=d", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.hideProgress(FillProfileActivity.this);
                Log.e("district Repsonse", "" + response);
                districtResponseList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<DistrictResponse>>() {
                }.getType();
                List<DistrictResponse> apiList1 = gson.fromJson(response.toString(), listType);
                districtResponseList.addAll(apiList1);
                if (districtResponseList.size() > 0) {
                    districtAdapter = new DistirctAdapter(FillProfileActivity.this, districtResponseList);
                    rcvDistrict.setAdapter(districtAdapter);
                    tv_nodata.setVisibility(View.GONE);
                    districtAdapter.setOnDistrictClickListener(new DistirctAdapter.OnDistrictClickListener() {
                        @Override
                        public void onDistrictClickListener(DistrictResponse districtnmaes) {
                            districtId = districtnmaes.getId();
                            Log.e("distirctId", "" + stateId);
                            districtName = districtnmaes.getName();
                            fillProfileBinding.selectDistrict.setText(districtName);
                            dialog.dismiss();
                        }
                    });
                } else {
                    tv_nodata.setText("No Data Available");
                    tv_nodata.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress(FillProfileActivity.this);
                showSnackBar(Constarins.SOMETHING_WENT_WRONG_IN_SERVER, 3);
            }
        });
        requestQueue.add(stringRequest);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            }
        });
    }

    private void stateDropdown() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.state_dropdownlist);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER_HORIZONTAL;
        window.setAttributes(wlp);
        dialog.show();
        dialog.setCancelable(false);
        TextView tv_warning = dialog.findViewById(R.id.tv_warning);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        final RecyclerView rcvStates = dialog.findViewById(R.id.rcvStates);
        TextView tv_nodata = dialog.findViewById(R.id.tv_nodata);
        tv_warning.setText("Select your State");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvStates.setLayoutManager(layoutManager);
        if (!Utility.isNetworkAvailable(this)) {
            showSnackBar(Constarins.PLEASE_CHECK_INTERNET, 2);
            return;
        }
        Utility.showProgress(this);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.State_dropdown, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.hideProgress(FillProfileActivity.this);
                Log.e("stateResponse1", "" + response);
                stateDataResponseList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<StateDataResponse>>() {
                }.getType();

                List<StateDataResponse> apiList = gson.fromJson(response.toString(), listType);
                stateDataResponseList.addAll(apiList);
                if (stateDataResponseList.size() > 0) {
                    stateAdapter = new StateAdapter(FillProfileActivity.this, stateDataResponseList);
                    rcvStates.setAdapter(stateAdapter);
                    tv_nodata.setVisibility(View.GONE);
                    stateAdapter.setonStateClickListener(new StateAdapter.OnStateClickListener() {
                        @Override
                        public void onStateClickListener(StateDataResponse filternames) {
                            stateId = filternames.getId();
                            Log.e("stateId", String.valueOf(stateId));
                            stateName = filternames.getName();
                            fillProfileBinding.selectState.setText(stateName);
                            dialog.dismiss();
                        }
                    });
                } else {
                    tv_nodata.setText("No Data Available");
                    tv_nodata.setVisibility(View.VISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress(FillProfileActivity.this);
                showSnackBar(Constarins.SOMETHING_WENT_WRONG_IN_SERVER, 3);
            }
        });
        requestQueue.add(stringRequest);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            }
        });
    }

    public void imageSelect() {
        permissionsCheck();
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void permissionsCheck() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a image.
                // The Intent's data Uri identifies which item was selected.
                if (data != null) {

                    // This is the key line item, URI specifies the name of the data
                    mImageUri = data.getData();
                    path = mImageUri.toString();
                    File myFile = new File(FillProfileActivity.this.getFilesDir().getAbsolutePath());
                    defaulImage = myFile.getAbsolutePath();
                    Log.e("profuileImahge", String.valueOf(defaulImage));
                    try {
                        bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    } catch (Exception e) {
                        //handle exception
                    }
                    profileImage.buildDrawingCache();
                    Bitmap bmap = profileImage.getDrawingCache();
                    encodedImage = getEncoded64ImageStringFromBitmap(bitmapImage);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("image", String.valueOf(mImageUri));
                    editor.commit();

                    // Sets the ImageView with the Image URI
                    profileImage.setImageURI(mImageUri);
                    profileImage.invalidate();


                }
            }
        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    @Override
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(this, fillProfileBinding.coordinator, snackBarText, type);
    }

    @Override
    public Activity activity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}