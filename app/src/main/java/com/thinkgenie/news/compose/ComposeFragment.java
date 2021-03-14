package com.thinkgenie.news.compose;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinkgenie.news.Apimanger.Apimanger;
import com.thinkgenie.news.Apimanger.VolleyInterface;
import com.thinkgenie.news.FillProfile.DistirctAdapter;
import com.thinkgenie.news.FillProfile.DistrictResponse;
import com.thinkgenie.news.FillProfile.LanguageAdapter;
import com.thinkgenie.news.FillProfile.LanguageResposne;
import com.thinkgenie.news.FillProfile.MandalAdapter;
import com.thinkgenie.news.FillProfile.MandalResponse;
import com.thinkgenie.news.FillProfile.StateAdapter;
import com.thinkgenie.news.FillProfile.StateDataResponse;
import com.thinkgenie.news.FillProfile.VillageAdapter;
import com.thinkgenie.news.FillProfile.VillageResponse;
import com.thinkgenie.news.Helpers.SessionManager;
import com.thinkgenie.news.R;
import com.thinkgenie.news.Utilities.Apicontant;
import com.thinkgenie.news.Utilities.Utility;
import com.thinkgenie.news.Utilities.UtilityPermission;
import com.thinkgenie.news.Volley.VolleyMultipartRequest;
import com.thinkgenie.news.consstrains.Constarins;
import com.thinkgenie.news.interfaces.customButtonListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ComposeFragment extends Fragment implements customButtonListener {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    private static final int VIDEO_CAPTURE = 102;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 103;
    private static String fileName = null;
    LocationCallback locationCallback;
    LatLng mycordinates;

    int newstypeID, districtId, mandalId, villageId, stateId, languageID;
    private Dialog dialog;
    RequestQueue requestQueue;
    SelectNewstypeAdapter newstypeAdapter;
    List<SelectNewstypemodel> newstypemodelsList;

    SessionManager sessionManager;
    View view;
    View includedLayout;
    RadioButton currentLoc_rb, selectLoc_rb, newsWithText_rb1, newsWithAudio_rb2, newsWithVideo_rb3, liveStreaming_rb4;
    LinearLayout linear_1, linear_2, linear_3, select_location_linear, select_currentloc_linear;
    TextView news_type_tv, news_type_rb2, language_tv_rb2, record_news_tv, news_type_rb3, language_tv_rb3, upload_video_tv, take_video_tv;
    RelativeLayout news_type_rl, add_image_rl, select_state_rl, select_district_rl, select_mandal_rl, select_village_rl;
    RelativeLayout audio_news_type_rl, language_tv_rl, upload_image_rl, record_news_rl;
    RelativeLayout video_news_type_rl, video_language_rl, upload_video_rl, take_video_rl;
    EditText state_tv, district_tv, mandal_tv, village_tv, headline_tv, descrption_tv;
    ImageView state_dpimg, district_dpimg, mandal_dpimg, village_dpimg;
    String fetch_state = "", fetch_district = "", fetch_mandal = "", fetch_city = "", stateName, districtName, mandalName, villageName,
            push_state = "", push_district = "", push_mandal = "", push_village = "", push_newswithtype = "",
            push_newstype = "", push_headline = "", push_description = "";
    String push_audionewstype = "", push_language = "";
    String push_videonewstype = "", push_videolanguage = "", push_upload_video = "", push_take_video = "";
    String sess_name, sess_tagname, sess_phone, sess_userid;
    Button submit_btn, submit_btn_2, submit_btn_3;
    ProgressDialog progressDialog;
    RadioGroup selected_newsgroup;
    RecyclerView _recyclerView_images;
    RecyclerImageAdapter mAdapter;

    ArrayList<Bitmap> userImageList = new ArrayList<>();
    ArrayList<File> userFileImageList = new ArrayList<>();

    private static final int SELECT_FILE = 1;
    private String userChoosenTask;
    Bitmap imageBitmap = null;
    int addImagname, remove_pos;
    List<StateDataResponse> stateDataResponseList;
    List<DistrictResponse> districtResponseList;
    List<MandalResponse> mandalResponseList;
    List<VillageResponse> villageResponseList;
    List<LanguageResposne> languageResposneList;
    StateAdapter stateAdapter;
    DistirctAdapter districtAdapter;
    MandalAdapter mandalAdapter;
    VillageAdapter villageAdapter;
    LanguageAdapter languageAdapter;
    Apimanger apimanger;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.compose_fragment_layout, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        sessionManager = new SessionManager(getContext());
        apimanger = new Apimanger(getActivity());

        currentLoc_rb = view.findViewById(R.id.currentLoc_rb);
        selectLoc_rb = view.findViewById(R.id.selectLoc_rb);
        select_location_linear = view.findViewById(R.id.select_location_linear);
//        select_currentloc_linear = view.findViewById(R.id.select_currentloc_linear);
        includedLayout = view.findViewById(R.id.currentLoc_layout);

        linear_1 = view.findViewById(R.id.linear_rb1);
        linear_2 = view.findViewById(R.id.linear_rb2);
        linear_3 = view.findViewById(R.id.linear_rb3);

        selected_newsgroup = view.findViewById(R.id.radioGroup1);
        newsWithText_rb1 = view.findViewById(R.id.newsWithText);
        newsWithAudio_rb2 = view.findViewById(R.id.newsWithAudio);
        newsWithVideo_rb3 = view.findViewById(R.id.newsWithVideo);
        liveStreaming_rb4 = view.findViewById(R.id.liveStreaming);

        state_tv = view.findViewById(R.id.select_state);
        district_tv = view.findViewById(R.id.select_district);
        mandal_tv = view.findViewById(R.id.select_mandal);
        village_tv = view.findViewById(R.id.select_village);

        state_dpimg = view.findViewById(R.id.state_dpimg);
        district_dpimg = view.findViewById(R.id.district_dpimg);
        mandal_dpimg = view.findViewById(R.id.mandal_dpimg);
        village_dpimg = view.findViewById(R.id.village_dpimg);

        news_type_tv = view.findViewById(R.id.news_type);
        news_type_rl = view.findViewById(R.id.news_type_rl);
        add_image_rl = view.findViewById(R.id.add_image_rl);
        _recyclerView_images = view.findViewById(R.id.recyclerView_images);

        audio_news_type_rl = view.findViewById(R.id.audio_news_type_rl);
        language_tv_rl = view.findViewById(R.id.language_tv_rl);
        upload_image_rl = view.findViewById(R.id.upload_image_rl);
        news_type_rb2 = view.findViewById(R.id.news_type_rb2);
        language_tv_rb2 = view.findViewById(R.id.language_tv_rb2);
        record_news_rl = view.findViewById(R.id.record_news_rl);
        record_news_tv = view.findViewById(R.id.record_news_tv);

        video_news_type_rl = view.findViewById(R.id.video_news_type_rl);
        video_language_rl = view.findViewById(R.id.video_language_rl);
        upload_video_rl = view.findViewById(R.id.upload_video_rl);
        news_type_rb3 = view.findViewById(R.id.news_type_rb3);
        language_tv_rb3 = view.findViewById(R.id.language_tv_rb3);
        upload_video_tv = view.findViewById(R.id.upload_video_tv);
        take_video_rl = view.findViewById(R.id.take_video_rl);
        take_video_tv = view.findViewById(R.id.take_video_tv);

        select_state_rl = view.findViewById(R.id.select_state_rl);
        select_district_rl = view.findViewById(R.id.select_district_rl);
        select_mandal_rl = view.findViewById(R.id.select_mandal_rl);
        select_village_rl = view.findViewById(R.id.select_village_rl);

        headline_tv = view.findViewById(R.id.headline_tv);
        descrption_tv = view.findViewById(R.id.descrption_tv);

        submit_btn = view.findViewById(R.id.submit_btn);
        submit_btn_2 = view.findViewById(R.id.submit_btn_2);
        submit_btn_3 = view.findViewById(R.id.submit_btn_3);

        HashMap<String, String> user = sessionManager.getUserDetails();

        sess_name = user.get(SessionManager.KEY_NAME);
        sess_tagname = user.get(SessionManager.KEY_TAGNAME);
        sess_phone = user.get(SessionManager.KEY_PHONE);
        sess_userid = user.get(SessionManager.KEY_USERID1);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NotNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
//                Log.e("maps callback--", locationResult.toString());
            }
        };

        if (currentLoc_rb.isChecked()) {
            Log.e("checked", "yes");
            currentloc_ui();
        } else {
            Log.e("checked", "no");
            selectloca_ui();
        }

        currentLoc_rb.setOnClickListener(view -> currentloc_ui());

        selectLoc_rb.setOnClickListener(view -> selectloca_ui());

        select_state_rl.setOnClickListener(v -> {
            if (selectLoc_rb.isChecked()) {
                dialog = new Dialog(getContext());
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
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                rcvStates.setLayoutManager(layoutManager);
                if (!Utility.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getContext(), Constarins.PLEASE_CHECK_INTERNET, Toast.LENGTH_LONG).show();
                    return;
                }
                Utility.showProgress(getActivity());
                requestQueue = Volley.newRequestQueue(getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.State_dropdown, response -> {
                    Utility.hideProgress(getActivity());
                    Log.e("stateResponse1", "" + response);
                    stateDataResponseList = new ArrayList<>();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<StateDataResponse>>() {
                    }.getType();

                    List<StateDataResponse> apiList = gson.fromJson(response, listType);
                    stateDataResponseList.addAll(apiList);
                    if (stateDataResponseList.size() > 0) {
                        stateAdapter = new StateAdapter(getContext(), stateDataResponseList);
                        rcvStates.setAdapter(stateAdapter);
                        tv_nodata.setVisibility(View.GONE);
                        stateAdapter.setonStateClickListener(filternames -> {
                            stateId = filternames.getId();
                            Log.e("stateId", String.valueOf(stateId));
                            stateName = filternames.getName();
                            state_tv.setText(stateName);
                            dialog.dismiss();
                        });
                    } else {
                        tv_nodata.setText("No Data Available");
                        tv_nodata.setVisibility(View.VISIBLE);
                    }

                }, error -> {
                    Utility.hideProgress(getActivity());
                    Toast.makeText(getContext(), Constarins.SOMETHING_WENT_WRONG_IN_SERVER, Toast.LENGTH_LONG).show();
                });
                requestQueue.add(stringRequest);
                ivClose.setOnClickListener(view -> {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    dialog.dismiss();
                });
            }
        });

        select_district_rl.setOnClickListener(v -> {
            if (selectLoc_rb.isChecked()) {
                dialog = new Dialog(getContext());
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
                tv_warning.setText("Select your District");
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                rcvStates.setLayoutManager(layoutManager);
                if (!Utility.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getContext(), Constarins.PLEASE_CHECK_INTERNET, Toast.LENGTH_LONG).show();
                    return;
                }
                Utility.showProgress(getActivity());
                requestQueue = Volley.newRequestQueue(getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.district_dropdown + "stateid=" + stateId + "&htype=d", response -> {
                    Utility.hideProgress(getActivity());
                    Log.e("district Repsonse", "" + response);
                    districtResponseList = new ArrayList<>();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<DistrictResponse>>() {
                    }.getType();
                    List<DistrictResponse> apiList1 = gson.fromJson(response, listType);
                    districtResponseList.addAll(apiList1);
                    if (districtResponseList.size() > 0) {
                        districtAdapter = new DistirctAdapter(getContext(), districtResponseList);
                        rcvStates.setAdapter(districtAdapter);
                        tv_nodata.setVisibility(View.GONE);
                        districtAdapter.setOnDistrictClickListener(districtnmaes -> {
                            districtId = districtnmaes.getId();
                            Log.e("distirctId", "" + stateId);
                            districtName = districtnmaes.getName();
                            district_tv.setText(districtName);
                            dialog.dismiss();
                        });
                    } else {
                        tv_nodata.setText("No Data Available");
                        tv_nodata.setVisibility(View.VISIBLE);
                    }

                }, error -> {
                    Utility.hideProgress(getActivity());
                    Toast.makeText(getContext(), Constarins.SOMETHING_WENT_WRONG_IN_SERVER, Toast.LENGTH_LONG).show();
                });
                requestQueue.add(stringRequest);
                ivClose.setOnClickListener(view -> {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    dialog.dismiss();
                });
            }
        });

        select_mandal_rl.setOnClickListener(v -> {
            if (selectLoc_rb.isChecked()) {
                dialog = new Dialog(getContext());
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
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                rcvStates.setLayoutManager(layoutManager);
                if (!Utility.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getContext(), Constarins.PLEASE_CHECK_INTERNET, Toast.LENGTH_LONG).show();
                    return;
                }
                Utility.showProgress(getActivity());
                requestQueue = Volley.newRequestQueue(getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.mandal_dropdown + "districtsid=" + districtId + "&htype=d", response -> {
                    Utility.hideProgress(getActivity());
                    Log.e("mandal Repsonse", "" + response);
                    mandalResponseList = new ArrayList<>();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<MandalResponse>>() {
                    }.getType();
                    List<MandalResponse> apiList2 = gson.fromJson(response, listType);
                    mandalResponseList.addAll(apiList2);
                    if (mandalResponseList.size() > 0) {
                        mandalAdapter = new MandalAdapter(getContext(), mandalResponseList);
                        rcvStates.setAdapter(mandalAdapter);
                        tv_nodata.setVisibility(View.GONE);
                        mandalAdapter.setOnMandalCliclListener(mandalResponse -> {
                            mandalId = mandalResponse.getId();
                            Log.e("distirctId", "" + stateId);
                            mandalName = mandalResponse.getName();
                            mandal_tv.setText(mandalName);
                            dialog.dismiss();
                        });
                    } else {
                        tv_nodata.setText("No Data Available");
                        tv_nodata.setVisibility(View.VISIBLE);
                    }

                }, error -> {
                    Utility.hideProgress(getActivity());
                    Toast.makeText(getContext(), Constarins.SOMETHING_WENT_WRONG_IN_SERVER, Toast.LENGTH_LONG).show();
                });
                requestQueue.add(stringRequest);
                ivClose.setOnClickListener(view -> {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    dialog.dismiss();
                });
            }
        });

        select_village_rl.setOnClickListener(v -> {
            if (selectLoc_rb.isChecked()) {
                dialog = new Dialog(getContext());
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
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                rcvStates.setLayoutManager(layoutManager);
                if (!Utility.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getContext(), Constarins.PLEASE_CHECK_INTERNET, Toast.LENGTH_LONG).show();
                    return;
                }
                Utility.showProgress(getActivity());
                requestQueue = Volley.newRequestQueue(getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.villages_dropdown + "mandalid=" + mandalId + "&htype=d", response -> {
                    Utility.hideProgress(getActivity());
                    Log.e("village Repsonse", "" + response);
                    villageResponseList = new ArrayList<>();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<VillageResponse>>() {
                    }.getType();
                    List<VillageResponse> apiListvillage = gson.fromJson(response, listType);
                    villageResponseList.addAll(apiListvillage);

                    if (villageResponseList.size() > 0) {
                        villageAdapter = new VillageAdapter(getContext(), villageResponseList);
                        rcvStates.setAdapter(villageAdapter);
                        tv_nodata.setVisibility(View.GONE);
                        villageAdapter.setOnVillageClickListener((VillageAdapter.onVillageClickListener) villageResponse -> {
                            villageId = villageResponse.getId();
                            Log.e("villageId", "" + villageId);
                            villageName = villageResponse.getName();
                            village_tv.setText(villageName);
                            dialog.dismiss();
                        });
                    } else {
                        tv_nodata.setText("No Data Available");
                        tv_nodata.setVisibility(View.VISIBLE);
                    }

                }, error -> {
                    Utility.hideProgress(getActivity());
                    Toast.makeText(getContext(), Constarins.SOMETHING_WENT_WRONG_IN_SERVER, Toast.LENGTH_LONG).show();
                });
                requestQueue.add(stringRequest);
                ivClose.setOnClickListener(view -> {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    dialog.dismiss();
                });
            }
        });

//        ----------------- News with text Start--------------------

        newsWithText_rb1.setOnClickListener(view -> {
            linear_1.setVisibility(View.VISIBLE);
            linear_2.setVisibility(View.GONE);
            linear_3.setVisibility(View.GONE);
            newsWithText_rb1.setChecked(true);
            newsWithAudio_rb2.setChecked(false);
            newsWithVideo_rb3.setChecked(false);
            liveStreaming_rb4.setChecked(false);
        });

        _recyclerView_images.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        _recyclerView_images.setLayoutManager(gridLayoutManager);

        mAdapter = new RecyclerImageAdapter(getContext(), userImageList);
        _recyclerView_images.setAdapter(mAdapter);

        add_image_rl.setOnClickListener(v -> {
            if (userImageList.size() < 3) {
                selectImage();
            } else {
                Toast.makeText(getContext(), "can't upload more than 3 Images", Toast.LENGTH_LONG).show();
            }
        });

        news_type_rl.setOnClickListener(v -> {

            dialog = new Dialog(requireContext());
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
            tv_warning.setText("Select News Type");
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            rcvStates.setLayoutManager(layoutManager);
            if (!Utility.isNetworkAvailable(requireActivity())) {
                Toast.makeText(getContext(), Constarins.PLEASE_CHECK_INTERNET, Toast.LENGTH_LONG).show();
                return;
            }
            Utility.showProgress(requireActivity());
            requestQueue = Volley.newRequestQueue(requireContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.NEWSTYPES, response -> {
                Utility.hideProgress(requireActivity());
                Log.e("newstypes Repsonse", "" + response);
                newstypemodelsList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<SelectNewstypemodel>>() {
                }.getType();
                List<SelectNewstypemodel> apiListlanguagr = gson.fromJson(response, listType);
                newstypemodelsList.addAll(apiListlanguagr);
                newstypeAdapter = new SelectNewstypeAdapter(getContext(), newstypemodelsList);
                rcvStates.setAdapter(newstypeAdapter);
                newstypeAdapter.setonNewsTypeClickListener(newstypemodelsList -> {
                    newstypeID = newstypemodelsList.getId();
                    String newsTypeName = newstypemodelsList.getType();
                    Log.e("newstypeID", "" + newstypeID + " " + newsTypeName);
                    news_type_tv.setText(newsTypeName);
                    dialog.dismiss();
                });

            }, error -> {
                Utility.hideProgress(getActivity());
                Toast.makeText(getContext(), Constarins.SOMETHING_WENT_WRONG_IN_SERVER, Toast.LENGTH_LONG).show();
            });
            requestQueue.add(stringRequest);

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            ivClose.setOnClickListener(view -> {
                InputMethodManager imm1 = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm1.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            });
        });

        submit_btn.setOnClickListener(v -> {
            Log.e("submit", "yes");
            if (newsWithText_rb1.isChecked()) {
                push_newswithtype = newsWithText_rb1.getText().toString();
                post_NewswithText();
            } else if (newsWithAudio_rb2.isChecked()) {
                push_newswithtype = newsWithAudio_rb2.getText().toString();
                post_NewswithText();
            } else if (newsWithVideo_rb3.isChecked()) {
                push_newswithtype = newsWithVideo_rb3.getText().toString();
                post_NewswithText();
            } else if (liveStreaming_rb4.isChecked()) {
                push_newswithtype = liveStreaming_rb4.getText().toString();
                post_NewswithText();
            }
        });

//        ----------------- News with text End--------------------

//        ----------------- News with Audio Start--------------------

        newsWithAudio_rb2.setOnClickListener(view -> {
            linear_1.setVisibility(View.GONE);
            linear_2.setVisibility(View.VISIBLE);
            linear_3.setVisibility(View.GONE);
            newsWithText_rb1.setChecked(false);
            newsWithVideo_rb3.setChecked(false);
            newsWithAudio_rb2.setChecked(true);
            liveStreaming_rb4.setChecked(false);

            audio_news_type_rl.setVisibility(View.VISIBLE);
            language_tv_rl.setVisibility(View.VISIBLE);
            news_type_rb2.setText("News Type");
            language_tv_rb2.setText("Language");
            record_news_tv.setText("Record News");
            news_type_rb2.setError(null);
            language_tv_rb2.setError(null);

        });

        audio_news_type_rl.setOnClickListener(v -> {
            dialog = new Dialog(requireContext());
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
            tv_warning.setText("Select News Type");
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            rcvStates.setLayoutManager(layoutManager);
            if (!Utility.isNetworkAvailable(requireActivity())) {
                Toast.makeText(getContext(), Constarins.PLEASE_CHECK_INTERNET, Toast.LENGTH_LONG).show();
                return;
            }
            Utility.showProgress(requireActivity());
            requestQueue = Volley.newRequestQueue(requireContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.NEWSTYPES, response -> {
                Utility.hideProgress(requireActivity());
                Log.e("newstypes Repsonse", "" + response);
                newstypemodelsList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<SelectNewstypemodel>>() {
                }.getType();
                List<SelectNewstypemodel> apiListlanguagr = gson.fromJson(response, listType);
                newstypemodelsList.addAll(apiListlanguagr);
                newstypeAdapter = new SelectNewstypeAdapter(getContext(), newstypemodelsList);
                rcvStates.setAdapter(newstypeAdapter);
                newstypeAdapter.setonNewsTypeClickListener(newstypemodelsList -> {
                    newstypeID = newstypemodelsList.getId();
                    String newsTypeName = newstypemodelsList.getType();
                    Log.e("newstypeID", "" + newstypeID + " " + newsTypeName);
                    news_type_rb2.setText(newsTypeName);
                    dialog.dismiss();
                });

            }, error -> {
                Utility.hideProgress(getActivity());
                Toast.makeText(getContext(), Constarins.SOMETHING_WENT_WRONG_IN_SERVER, Toast.LENGTH_LONG).show();
            });
            requestQueue.add(stringRequest);

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            ivClose.setOnClickListener(view -> {
                InputMethodManager imm1 = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm1.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            });
        });

        language_tv_rl.setOnClickListener(v -> {
            dialog = new Dialog(getContext());
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
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rcvStates.setLayoutManager(layoutManager);
            if (!Utility.isNetworkAvailable(requireActivity())) {
                Toast.makeText(getContext(), Constarins.PLEASE_CHECK_INTERNET, Toast.LENGTH_LONG).show();
                return;
            }
            Utility.showProgress(requireActivity());
            requestQueue = Volley.newRequestQueue(requireContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.language_dropdown, response -> {
                Utility.hideProgress(requireActivity());
                Log.e("Language Repsonse", "" + response);
                languageResposneList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<LanguageResposne>>() {
                }.getType();
                List<LanguageResposne> apiListlanguagr = gson.fromJson(response, listType);
                languageResposneList.addAll(apiListlanguagr);
                if (languageResposneList.size() > 0) {
                    languageAdapter = new LanguageAdapter(getContext(), languageResposneList);
                    rcvStates.setAdapter(languageAdapter);
                    tv_nodata.setVisibility(View.GONE);
                    languageAdapter.setOnLanguageClickListner(languageResposne -> {
                        languageID = languageResposne.getId();
                        Log.e("languagId", "" + languageID);
                        String languageName = languageResposne.getName();
                        language_tv_rb2.setText(languageName);
                        dialog.dismiss();
                    });
                } else {
                    tv_nodata.setText("No Data Available");
                    tv_nodata.setVisibility(View.VISIBLE);
                }
            }, error -> {
                Utility.hideProgress(getActivity());
                Toast.makeText(getContext(), Constarins.SOMETHING_WENT_WRONG_IN_SERVER, Toast.LENGTH_LONG).show();
            });
            requestQueue.add(stringRequest);
            ivClose.setOnClickListener(view -> {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            });
        });

        record_news_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckVoiceRecorderPermissions()) {

                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_audio_recorder);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    wlp.gravity = Gravity.CENTER_HORIZONTAL;
                    window.setAttributes(wlp);
                    dialog.show();
                    dialog.setCancelable(false);
                    TextView r_filename = dialog.findViewById(R.id.car_filename);
                    TextView r_start = dialog.findViewById(R.id.car_start);
                    TextView r_play = dialog.findViewById(R.id.car_play);
                    TextView r_stop = dialog.findViewById(R.id.car_stop);
                    Chronometer r_timer = dialog.findViewById(R.id.car_timer);
                    Button record_ok = dialog.findViewById(R.id.record_ok);
                    ImageView ivClose = dialog.findViewById(R.id.ivClose);

                    r_play.setEnabled(false);
                    r_stop.setEnabled(false);
                    r_start.setEnabled(true);
                    record_ok.setEnabled(false);

                    r_start.setOnClickListener(v1 -> {

                        fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString();
                        fileName += "_audiorecord.3gp";

                        setupmediaRecorder();

                        try {
                            mRecorder.prepare();
                            mRecorder.start();
                            r_filename.setText(fileName);
                            r_timer.setBase(SystemClock.elapsedRealtime());
                            r_timer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        r_start.setEnabled(false);
                        r_play.setEnabled(false);
                        r_stop.setEnabled(true);
                        record_ok.setEnabled(false);

                        Toast.makeText(getActivity(), "Recording...", Toast.LENGTH_SHORT).show();
                    });

                    r_stop.setOnClickListener(v12 -> {
                        if (mRecorder != null) {
                            mRecorder.stop();
                            mRecorder.release();
                            mRecorder = null;
                            r_timer.stop();
                        }
                        r_play.setEnabled(true);
                        r_stop.setEnabled(false);
                        r_start.setEnabled(true);
                        record_ok.setEnabled(true);

                        if (mPlayer != null) {
                            mPlayer.stop();
                            mPlayer.release();
                            mPlayer = null;
                            setupmediaRecorder();
                        }
                    });

                    r_play.setOnClickListener(v13 -> {
                        r_play.setEnabled(false);
                        r_stop.setEnabled(true);
                        r_start.setEnabled(false);

                        mPlayer = new MediaPlayer();
                        try {
                            mPlayer.setDataSource(fileName);
                            mPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mPlayer.start();
                        Toast.makeText(getActivity(), "Playing...", Toast.LENGTH_SHORT).show();
                    });

                    ivClose.setOnClickListener(view -> {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        record_news_tv.setText("Record News");
                        dialog.dismiss();
                    });

                    record_ok.setOnClickListener(v14 -> {
                        record_news_tv.setText(fileName);
                        dialog.dismiss();
                    });

                } else {
                    RequestPermissions();
                }
            }
        });

        upload_image_rl.setOnClickListener(v -> selectaudiofile());

        submit_btn_2.setOnClickListener(v -> {
            Log.e("submit", "yes");
            if (newsWithText_rb1.isChecked()) {
                push_newswithtype = newsWithText_rb1.getText().toString();
                post_NewswithAudio();
            } else if (newsWithAudio_rb2.isChecked()) {
                push_newswithtype = newsWithAudio_rb2.getText().toString();
                post_NewswithAudio();
            } else if (newsWithVideo_rb3.isChecked()) {
                push_newswithtype = newsWithVideo_rb3.getText().toString();
                post_NewswithAudio();
            } else if (liveStreaming_rb4.isChecked()) {
                push_newswithtype = liveStreaming_rb4.getText().toString();
                post_NewswithAudio();
            }
        });

//        ----------------- News with Audio End--------------------

//        ----------------- News with Video Start--------------------

        newsWithVideo_rb3.setOnClickListener(view -> {
            linear_1.setVisibility(View.GONE);
            linear_2.setVisibility(View.GONE);
            linear_3.setVisibility(View.VISIBLE);
            newsWithText_rb1.setChecked(false);
            newsWithAudio_rb2.setChecked(false);
            newsWithVideo_rb3.setChecked(true);
            liveStreaming_rb4.setChecked(false);

            video_news_type_rl.setVisibility(View.VISIBLE);
            video_language_rl.setVisibility(View.VISIBLE);
            upload_video_rl.setVisibility(View.VISIBLE);
            take_video_rl.setVisibility(View.VISIBLE);
            news_type_rb3.setText("News Type");
            language_tv_rb3.setText("Language");
            upload_video_tv.setText("Upload Video");
            take_video_tv.setText("Take Video");
            news_type_rb3.setError(null);
            language_tv_rb3.setError(null);

        });

        video_news_type_rl.setOnClickListener(v -> {
            dialog = new Dialog(requireContext());
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
            tv_warning.setText("Select News Type");
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            rcvStates.setLayoutManager(layoutManager);
            if (!Utility.isNetworkAvailable(requireActivity())) {
                Toast.makeText(getContext(), Constarins.PLEASE_CHECK_INTERNET, Toast.LENGTH_LONG).show();
                return;
            }
            Utility.showProgress(requireActivity());
            requestQueue = Volley.newRequestQueue(requireContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.NEWSTYPES, response -> {
                Utility.hideProgress(requireActivity());
                Log.e("newstypes Repsonse", "" + response);
                newstypemodelsList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<SelectNewstypemodel>>() {
                }.getType();
                List<SelectNewstypemodel> apiListlanguagr = gson.fromJson(response, listType);
                newstypemodelsList.addAll(apiListlanguagr);
                newstypeAdapter = new SelectNewstypeAdapter(getContext(), newstypemodelsList);
                rcvStates.setAdapter(newstypeAdapter);
                newstypeAdapter.setonNewsTypeClickListener(newstypemodelsList -> {
                    newstypeID = newstypemodelsList.getId();
                    String newsTypeName = newstypemodelsList.getType();
                    Log.e("newstypeID", "" + newstypeID + " " + newsTypeName);
                    news_type_rb3.setText(newsTypeName);
                    dialog.dismiss();
                });

            }, error -> {
                Utility.hideProgress(getActivity());
                Toast.makeText(getContext(), Constarins.SOMETHING_WENT_WRONG_IN_SERVER, Toast.LENGTH_LONG).show();
            });
            requestQueue.add(stringRequest);

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            ivClose.setOnClickListener(view -> {
                InputMethodManager imm1 = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm1.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            });
        });

        video_language_rl.setOnClickListener(v -> {
            dialog = new Dialog(getContext());
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
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rcvStates.setLayoutManager(layoutManager);
            if (!Utility.isNetworkAvailable(requireActivity())) {
                Toast.makeText(getContext(), Constarins.PLEASE_CHECK_INTERNET, Toast.LENGTH_LONG).show();
                return;
            }
            Utility.showProgress(requireActivity());
            requestQueue = Volley.newRequestQueue(requireContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.language_dropdown, response -> {
                Utility.hideProgress(requireActivity());
                Log.e("Language Repsonse", "" + response);
                languageResposneList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<LanguageResposne>>() {
                }.getType();
                List<LanguageResposne> apiListlanguagr = gson.fromJson(response, listType);
                languageResposneList.addAll(apiListlanguagr);
                if (languageResposneList.size() > 0) {
                    languageAdapter = new LanguageAdapter(getContext(), languageResposneList);
                    rcvStates.setAdapter(languageAdapter);
                    tv_nodata.setVisibility(View.GONE);
                    languageAdapter.setOnLanguageClickListner(languageResposne -> {
                        languageID = languageResposne.getId();
                        Log.e("languagId", "" + languageID);
                        String languageName = languageResposne.getName();
                        language_tv_rb3.setText(languageName);
                        dialog.dismiss();
                    });
                } else {
                    tv_nodata.setText("No Data Available");
                    tv_nodata.setVisibility(View.VISIBLE);
                }
            }, error -> {
                Utility.hideProgress(getActivity());
                Toast.makeText(getContext(), Constarins.SOMETHING_WENT_WRONG_IN_SERVER, Toast.LENGTH_LONG).show();
            });
            requestQueue.add(stringRequest);
            ivClose.setOnClickListener(view -> {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            });
        });

        upload_video_rl.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            startActivityForResult(intent, 3);
        });

        take_video_rl.setOnClickListener(v -> {
            boolean result1 = UtilityPermission.checkVideoPermission(getContext());
            if (result1) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, VIDEO_CAPTURE);
            }
        });

        submit_btn_3.setOnClickListener(v -> {
//            if (!hasCamera()) {
            Log.e("submit", "yes");
            if (newsWithText_rb1.isChecked()) {
                push_newswithtype = newsWithText_rb1.getText().toString();
                post_NewswithVideo();
            } else if (newsWithAudio_rb2.isChecked()) {
                push_newswithtype = newsWithAudio_rb2.getText().toString();
                post_NewswithVideo();
            } else if (newsWithVideo_rb3.isChecked()) {
                push_newswithtype = newsWithVideo_rb3.getText().toString();
                post_NewswithVideo();
            } else if (liveStreaming_rb4.isChecked()) {
                push_newswithtype = liveStreaming_rb4.getText().toString();
                post_NewswithVideo();
            }
//            }else {
//                Log.e("submit","no");
//            }
        });

//        ----------------- News with Video End--------------------

        liveStreaming_rb4.setOnClickListener(view -> {
            linear_1.setVisibility(View.GONE);
            linear_2.setVisibility(View.GONE);
            linear_3.setVisibility(View.GONE);
            newsWithText_rb1.setChecked(false);
            newsWithAudio_rb2.setChecked(false);
            newsWithVideo_rb3.setChecked(false);
            liveStreaming_rb4.setChecked(true);
        });

        return view;
    }

//        ----------------- News with Video Functions Start--------------------

    private boolean validate_newswithVideo() {
        boolean valid2 = true;

        push_state = state_tv.getText().toString().trim();
        push_district = district_tv.getText().toString().trim();
        push_mandal = mandal_tv.getText().toString().trim();
        push_village = village_tv.getText().toString().trim();
        push_videonewstype = news_type_rb3.getText().toString().trim();
        push_videolanguage = language_tv_rb3.getText().toString().trim();
        push_upload_video = upload_video_tv.getText().toString().trim();
        push_take_video = take_video_tv.getText().toString().trim();

        Log.e("selectedradioNews--", push_newswithtype);

        if (push_state.isEmpty() || push_state.equalsIgnoreCase("Select State")) {
            Toast.makeText(getContext(), "Please Select State", Toast.LENGTH_LONG).show();
            valid2 = false;
            fetch_location();
        }

        if (push_district.isEmpty() || push_district.equalsIgnoreCase("Select District")) {
            Toast.makeText(getContext(), "Please Select District", Toast.LENGTH_LONG).show();
            valid2 = false;
        }

        if (push_village.isEmpty() || push_village.equalsIgnoreCase("Select Village")) {
            Toast.makeText(getContext(), "Please Select District", Toast.LENGTH_LONG).show();
            valid2 = false;
        }

        if (push_videonewstype.isEmpty() || push_videonewstype.equalsIgnoreCase("News Type")) {
            news_type_rb3.setError("Please Select News Type");
            valid2 = false;
        } else {
            news_type_rb3.setError(null);
        }

        if (push_videolanguage.isEmpty() || push_videolanguage.equalsIgnoreCase("Language")) {
            language_tv_rb3.setError("Please Select Language");
            valid2 = false;
        } else {
            language_tv_rb3.setError(null);
        }

        if ((push_take_video.isEmpty() || push_take_video.equalsIgnoreCase("Take Video")) && (push_upload_video.isEmpty() || push_upload_video.equalsIgnoreCase("Upload Video"))) {
            Log.e("er", "vid");
            Log.e("push_take_video", push_take_video);
            Log.e("push_upload_video", push_upload_video);
            Toast.makeText(getContext(), "Please Upload or Record Video", Toast.LENGTH_LONG).show();
            valid2 = false;
        } else {
            Log.e("push_take_video", push_take_video);
            Log.e("push_upload_video", push_upload_video);
            Log.e("er", "else vid");
        }

        return valid2;
    }

    private void post_NewswithVideo() {
        if (validate_newswithVideo()) {

            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Uploading Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateTime = simpleDateFormat.format(calendar.getTime());

            JSONObject dataobjcam = new JSONObject();
            try {
                dataobjcam.put("id", 0);
                dataobjcam.put("title", push_headline);
                dataobjcam.put("date", dateTime);
                dataobjcam.put("description", push_description);
                dataobjcam.put("articletypeid", 1);
                dataobjcam.put("newstypeid", 3);
                dataobjcam.put("villageid", 1);
                dataobjcam.put("userid", 5);
                dataobjcam.put("status", true);
                dataobjcam.put("createdby", 1);
                dataobjcam.put("createdon", dateTime);
                dataobjcam.put("modifiedby", 1);
                dataobjcam.put("modifiedon", dateTime);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("error exception", e.toString());
                progressDialog.dismiss();
            }

            if (!Utility.isNetworkAvailable(getActivity())) {
                Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            apimanger.RequestWithObject(Apicontant.POST, Constarins.NEWS, dataobjcam, new VolleyInterface() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String get_nid = jsonObject.getString("id");
                        Log.e("NewsTextresponse", result);
                        progressDialog.dismiss();
                        if (imageBitmap != null) {
                            uploadVideomedia(get_nid, Uri.parse(push_upload_video));
                        } else {
                            Toast.makeText(getContext(), "Successfully uploaded news", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error-", e.toString());
                    }
                }

                @Override
                public void onError(String error, int statusCode) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void uploadVideomedia(String nid, Uri videobit) {

        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Uploading Media...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constarins.mediaupload,
                response -> {
                    System.out.println("Success 2222222 " + response);
                    Log.e("image response", "" + response);
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), response.toString() + " Success - ", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                    } else {
                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Log.e("Error Status", status);
                            Log.e("Error Message", message);

                            if (networkResponse.statusCode == 404) {
                                errorMessage = "Resource not found";
                            } else if (networkResponse.statusCode == 401) {
                                errorMessage = message + " Please login again";
                            } else if (networkResponse.statusCode == 400) {
                                errorMessage = message + " Check your inputs";
                            } else if (networkResponse.statusCode == 500) {
                                errorMessage = message + " Something is getting wrong";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("catch--", errorMessage);
                        }
                    }
                    Log.i("Error", errorMessage);
                    System.out.println(" 22222 " + error.getMessage());
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
//                    params.put("imageurl", new DataPart(imagename + ".png", getFileDataFromDrawable(videobit)));
//                    Log.e("imageurl", "" + getFileDataFromDrawable(videobit));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> imageParams = new HashMap<>();

                imageParams.put("ClientDocs", "ClientDocs");
                imageParams.put("nid", nid);
                imageParams.put("contentid", "1");

                Log.d("image uploading", ":::::" + imageParams);
                return imageParams;
            }
        };

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(volleyMultipartRequest);
    }

//        ----------------- News with Video Functions End--------------------

//        ----------------- News with Audio Functions Start--------------------

    private boolean validate_newswithAudio() {
        boolean valid1 = true;

        push_state = state_tv.getText().toString().trim();
        push_district = district_tv.getText().toString().trim();
        push_mandal = mandal_tv.getText().toString().trim();
        push_village = village_tv.getText().toString().trim();
        push_audionewstype = news_type_rb2.getText().toString().trim();
        push_language = language_tv_rb2.getText().toString().trim();

        Log.e("selectedradioNews--", push_newswithtype);

        if (push_state.isEmpty() || push_state.equalsIgnoreCase("Select State")) {
            Toast.makeText(getContext(), "Please Select State", Toast.LENGTH_LONG).show();
            valid1 = false;
            fetch_location();
        }

        if (push_district.isEmpty() || push_district.equalsIgnoreCase("Select District")) {
            Toast.makeText(getContext(), "Please Select District", Toast.LENGTH_LONG).show();
            valid1 = false;
        }

        if (push_village.isEmpty() || push_village.equalsIgnoreCase("Select Village")) {
            Toast.makeText(getContext(), "Please Select District", Toast.LENGTH_LONG).show();
            valid1 = false;
        }

        if (push_audionewstype.isEmpty() || push_audionewstype.equalsIgnoreCase("News Type")) {
            news_type_rb2.setError("Please Select News Type");
            valid1 = false;
        } else {
            news_type_rb2.setError(null);
        }

        if (push_language.isEmpty() || push_language.equalsIgnoreCase("Language")) {
            language_tv_rb2.setError("Please Select Language");
            valid1 = false;
        } else {
            language_tv_rb2.setError(null);
        }

        return valid1;
    }

    private void post_NewswithAudio() {
        if (validate_newswithAudio()) {

            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Uploading Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateTime = simpleDateFormat.format(calendar.getTime());

            JSONObject dataobjcam = new JSONObject();
            try {
                dataobjcam.put("id", 0);
                dataobjcam.put("title", push_headline);
                dataobjcam.put("date", dateTime);
                dataobjcam.put("description", push_description);
                dataobjcam.put("articletypeid", 1);
                dataobjcam.put("newstypeid", 2);
                dataobjcam.put("villageid", 1);
                dataobjcam.put("userid", 5);
                dataobjcam.put("status", true);
                dataobjcam.put("createdby", 1);
                dataobjcam.put("createdon", dateTime);
                dataobjcam.put("modifiedby", 1);
                dataobjcam.put("modifiedon", dateTime);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("error exception", e.toString());
                progressDialog.dismiss();
            }

            if (!Utility.isNetworkAvailable(getActivity())) {
                Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            apimanger.RequestWithObject(Apicontant.POST, Constarins.NEWS, dataobjcam, new VolleyInterface() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String get_nid = jsonObject.getString("id");
                        Log.e("NewsTextresponse", result);
                        progressDialog.dismiss();
                        if (imageBitmap != null) {
//                            uploadmedia(get_nid, imageBitmap);
                        } else {
                            Toast.makeText(getContext(), "Successfully uploaded news", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error-", e.toString());
                    }
                }

                @Override
                public void onError(String error, int statusCode) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void selectaudiofile() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);
    }


    private void setupmediaRecorder() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(fileName);
    }

    private boolean CheckVoiceRecorderPermissions() {
        int result = ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

//        ----------------- News with Audio Functions End--------------------

//        ----------------- News with Text Functions Start--------------------

    private boolean validate_newswithtext() {
        boolean valid = true;

        push_state = state_tv.getText().toString().trim();
        push_district = district_tv.getText().toString().trim();
        push_mandal = mandal_tv.getText().toString().trim();
        push_village = village_tv.getText().toString().trim();
        push_newstype = news_type_tv.getText().toString().trim();
        push_headline = headline_tv.getText().toString().trim();
        push_description = descrption_tv.getText().toString().trim();

        Log.e("selectedradioNews--", push_newswithtype);

        if (push_state.isEmpty() || push_state.equalsIgnoreCase("Select State")) {
            Toast.makeText(getContext(), "Please Select State", Toast.LENGTH_LONG).show();
            valid = false;
            fetch_location();
        }

        if (push_district.isEmpty() || push_district.equalsIgnoreCase("Select District")) {
            Toast.makeText(getContext(), "Please Select District", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (push_village.isEmpty() || push_village.equalsIgnoreCase("Select Village")) {
            Toast.makeText(getContext(), "Please Select District", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (push_newstype.isEmpty() || push_newstype.equalsIgnoreCase("News Type")) {
            Toast.makeText(getContext(), "Please Select News Type", Toast.LENGTH_LONG).show();
            valid = false;
        } else if (userImageList.size() == 0) {
            Toast.makeText(getContext(), "Please add atleast 1 image", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (push_headline.isEmpty()) {
            headline_tv.setError("Please Fill Headline");
            valid = false;
        } else {
            headline_tv.setError(null);
        }

        if (push_description.isEmpty()) {
            descrption_tv.setError("Please Fill Description");
            valid = false;
        } else {
            descrption_tv.setError(null);
        }

        return valid;
    }

    private void post_NewswithText() {
        if (validate_newswithtext()) {

            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Uploading Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateTime = simpleDateFormat.format(calendar.getTime());

            JSONObject dataobjcam = new JSONObject();
            try {
                dataobjcam.put("id", 0);
                dataobjcam.put("title", push_headline);
                dataobjcam.put("date", dateTime);
                dataobjcam.put("description", push_description);
                dataobjcam.put("articletypeid", 1);
                dataobjcam.put("newstypeid", 1);
                dataobjcam.put("villageid", 1);
                dataobjcam.put("userid", 5);
                dataobjcam.put("status", true);
                dataobjcam.put("createdby", 1);
                dataobjcam.put("createdon", dateTime);
                dataobjcam.put("modifiedby", 1);
                dataobjcam.put("modifiedon", dateTime);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("error exception", e.toString());
                progressDialog.dismiss();
            }

            if (!Utility.isNetworkAvailable(getActivity())) {
                Toast.makeText(getContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            apimanger.RequestWithObject(Apicontant.POST, Constarins.NEWS, dataobjcam, new VolleyInterface() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String get_nid = jsonObject.getString("id");
                        Log.e("NewsTextresponse", result);
                        progressDialog.dismiss();
                        if (imageBitmap != null) {
                            uploadmedia(get_nid, imageBitmap);
                        } else {
                            Toast.makeText(getContext(), "Successfully uploaded news", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error-", e.toString());
                    }
                }

                @Override
                public void onError(String error, int statusCode) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadmedia(String nid, Bitmap imgbit) {

        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Uploading Media...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constarins.mediaupload,
                response -> {
                    System.out.println("Success 2222222 " + response);
                    Log.e("image response", "" + response);
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), response.toString() + " Success - ", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                    } else {
                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Log.e("Error Status", status);
                            Log.e("Error Message", message);

                            if (networkResponse.statusCode == 404) {
                                errorMessage = "Resource not found";
                            } else if (networkResponse.statusCode == 401) {
                                errorMessage = message + " Please login again";
                            } else if (networkResponse.statusCode == 400) {
                                errorMessage = message + " Check your inputs";
                            } else if (networkResponse.statusCode == 500) {
                                errorMessage = message + " Something is getting wrong";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Log.e("catch--", errorMessage);
                        }
                    }
                    Log.i("Error", errorMessage);
                    System.out.println(" 22222 " + error.getMessage());
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                for (int i = 0, j = 1; i < userImageList.size(); i++, j++) {
                    long imagename = System.currentTimeMillis();
                    params.put("imageurl[i]", new DataPart(imagename + ".png", getFileDataFromDrawable(imgbit)));
                    Log.e("imageurl", "" + getFileDataFromDrawable(imgbit));
                }
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> imageParams = new HashMap<>();

                imageParams.put("ClientDocs", "ClientDocs");
                imageParams.put("nid", nid);
                imageParams.put("contentid", "1");

                Log.d("image uploading", ":::::" + imageParams);
                return imageParams;
            }
        };

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(volleyMultipartRequest);
    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {
            boolean result = UtilityPermission.checkPermission(getContext());

            if (items[item].equals("Choose from Library")) {
                userChoosenTask = "Choose from Library";
                if (result)
                    galleryIntent();
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void onSelectFromGalleryResult(Intent data) {
        imageBitmap = null;
        if (data != null) {
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                addImagname = userImageList.size() + 1;
                if (imageBitmap != null) {
                    userImageList.add(imageBitmap);
                    mAdapter.notifyDataSetChanged();
                    _recyclerView_images.scrollToPosition(userImageList.size() - 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String imagetostring(Bitmap imageBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

//        ----------------- News with Text Functions End--------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    onSelectFromGalleryResult(data);
                }
                break;

            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    Log.e("audio file--", String.valueOf(uri));
                    Log.e("audio file--", uri.getPath());
                }
                break;

            case 3:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    upload_video_tv.setText(uri.getPath());
                    take_video_tv.setText("Take Video");
                    take_video_rl.setVisibility(View.GONE);
                    Log.e("upload video file--", String.valueOf(uri));
                    Log.e("upload video file--", uri.getPath());
                }
                break;
            case VIDEO_CAPTURE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Uri videoUri = data.getData();
                        take_video_tv.setText(videoUri.getPath());
                        upload_video_tv.setText("Upload Video");
                        upload_video_rl.setVisibility(View.GONE);
                        Log.e("take video file--", String.valueOf(videoUri));
                        Log.e("take video file--", videoUri.getPath());
                        Toast.makeText(getActivity(), "Video saved to:\n" + videoUri, Toast.LENGTH_LONG).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getActivity(), "Video recording cancelled.", Toast.LENGTH_LONG).show();
                        break;
                }
            case LOCATION_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getActivity(), "Grant", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getActivity(), "Grant", Toast.LENGTH_SHORT).show();
                        break;
                }
        }
    }

    private void currentloc_ui() {

        includedLayout.setVisibility(View.VISIBLE);
        newsWithText_rb1.setVisibility(View.VISIBLE);
        select_state_rl.setClickable(false);
        select_district_rl.setClickable(false);
        select_mandal_rl.setClickable(false);
        select_village_rl.setClickable(false);

        state_dpimg.setVisibility(View.GONE);
        district_dpimg.setVisibility(View.GONE);
        mandal_dpimg.setVisibility(View.GONE);
        village_dpimg.setVisibility(View.GONE);

        state_tv.setText("");
        district_tv.setText("");
        mandal_tv.setText("");
        village_tv.setText("");

        state_tv.setEnabled(false);
        district_tv.setEnabled(false);
        mandal_tv.setEnabled(false);
        village_tv.setEnabled(false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        fetch_location();
    }

    @SuppressLint("SetTextI18n")
    private void selectloca_ui() {
        includedLayout.setVisibility(View.VISIBLE);

        select_state_rl.setClickable(true);
        select_district_rl.setClickable(true);
        select_mandal_rl.setClickable(true);
        select_village_rl.setClickable(true);

        state_dpimg.setVisibility(View.VISIBLE);
        district_dpimg.setVisibility(View.VISIBLE);
        mandal_dpimg.setVisibility(View.VISIBLE);
        village_dpimg.setVisibility(View.VISIBLE);

        state_tv.setText("Select State");
        district_tv.setText("Select District");
        mandal_tv.setText("Select Mandal");
        village_tv.setText("Select Village");

        state_tv.setEnabled(true);
        district_tv.setEnabled(true);
        mandal_tv.setEnabled(true);
        village_tv.setEnabled(true);
    }

    private void fetch_location() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e("loca", "1");
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
//                return;
            } else {
                Log.e("loca", "2");
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(5000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.e("loca", "3");
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    Task<Location> task1 = fusedLocationProviderClient.getLastLocation();
                    task1.addOnSuccessListener(location -> {
                        if (location != null) {
                            Log.e("loca", "4");
                            currentLocation = location;
                            mycordinates = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            getcityname(mycordinates);
                        } else {
                            Log.e("loca", "5");
                        }
                    });
                }
                Log.e("loca", "6");
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
            Log.e("loca", "7");
//            Task<Location> task = fusedLocationProviderClient.getLastLocation();
//            task.addOnSuccessListener(location -> {
//                if (location != null) {
//                    Log.e("loca", "8");
//                    currentLocation = location;
//                    mycordinates = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                    getcityname(mycordinates);
//                } else {
//                    Log.e("loca", "9");
//                }
//            });
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, id) -> {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("loca", "0000");
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            fetch_location();
        }

        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
//        if (requestCode == LOCATION_REQUEST_CODE) {
//            switch ()
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.e("loca", "111");
//                Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.e("loca", "222");
//                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private void getcityname(LatLng mycordinates) {
        Log.e("loca", "10");
        try {
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(mycordinates.latitude, mycordinates.longitude, 1);
            Log.e("Compose fragment", addresses.toString());

            if (addresses.get(0).getSubAdminArea() != null) {
                fetch_district = addresses.get(0).getSubAdminArea() + "";
            }

            if (addresses.get(0).getLocality() != null) {
                fetch_city = addresses.get(0).getLocality() + "";
            }

            if (addresses.get(0).getAdminArea() != null) {
                fetch_state = addresses.get(0).getAdminArea() + "";
            }

            state_tv.setText(fetch_state);
            district_tv.setText(fetch_district);
            village_tv.setText(fetch_city);
            Log.e("village--", fetch_city);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onButtonClickListner(int position, int listsize) {
        userFileImageList.remove(position);
        remove_pos = position + 1;
//        Utils.deleteFilesInFolder(this, "uploadimages", remove_pos);
    }
}