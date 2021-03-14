package com.thinkgenie.news.Location;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.thinkgenie.news.Apimanger.Apimanger;
import com.thinkgenie.news.Apimanger.VolleyInterface;
import com.thinkgenie.news.FillProfile.DistirctAdapter;
import com.thinkgenie.news.FillProfile.DistrictResponse;
import com.thinkgenie.news.FillProfile.FillProfileActivity;
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
import com.thinkgenie.news.Utilities.Utility;
import com.thinkgenie.news.consstrains.Constarins;
import com.thinkgenie.news.databinding.ActivityCreateNewLocBinding;
import com.thinkgenie.news.interfaces.EActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thinkgenie.news.Utilities.Apicontant.POST;

public class CreateNewLocActivity extends AppCompatActivity implements EActivity {
ActivityCreateNewLocBinding activityCreateNewLocBinding;
    private static final int MY_SOCKET_TIMEOUT_MS = 15000;
    RequestQueue requestQueue;
    private Dialog dialog;
    StateAdapter stateAdapter;

    List<StateDataResponse> stateDataResponseList;
    private static final int PICK_IMAGE_REQUEST = 0;
    Integer languageID, stateId, districtId, mandalId, villageId;
    DistirctAdapter districtAdapter;
    List<DistrictResponse> districtResponseList;
    VillageAdapter villageAdapter;
    List<VillageResponse> villageResponseList;
    AlertDialog.Builder builder;
    MandalAdapter mandalAdapter;
    List<MandalResponse> mandalResponseList;
    LanguageAdapter languageAdapter;
    List<LanguageResposne> languageResposneList;
    String  pincode,state,district,mandal,village,user_id_loc,stateName,fav_loc_name;
    Apimanger apimanger;
     JSONObject dataobj;
     SessionManager sessionManager;

    CreateNewLocActivity.refreshData callback;
    public interface refreshData {
        public void onRefreshList();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityCreateNewLocBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_new_loc);
        sessionManager=new SessionManager(getApplicationContext());

        user_id_loc = sessionManager.useridstoreget();


        builder = new AlertDialog.Builder(this);
        apimanger=new Apimanger(this);
        activityCreateNewLocBinding.relativeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateDropdown();

            }
        });
        activityCreateNewLocBinding.relativeDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                districtDropDown();
            }
        });
        activityCreateNewLocBinding.relativeMandal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mandalDropDown();
            }
        });

        activityCreateNewLocBinding.relativeVillage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                villageDropdown();
            }
        });
        activityCreateNewLocBinding.btnCreateLoctn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onRefreshList();
                    finish();
                }
                pincode = activityCreateNewLocBinding.pincode.getText().toString().trim();
                state = activityCreateNewLocBinding.selectState.getText().toString().trim();
                district = activityCreateNewLocBinding.selectDistrict.getText().toString().trim();
                mandal = activityCreateNewLocBinding.selectMandal.getText().toString().trim();

                fav_loc_name=activityCreateNewLocBinding.favLocEt.getText().toString().trim();
                if (pincode.isEmpty()) {
                    showSnackBar("Please enter pincode", 2);
                } else if (state.equals("State")) {
                    showSnackBar("Please Select state", 2);
                }else if (district.equals("District")) {
                    showSnackBar("Please Select District", 2);
                }else if (mandal.equals("Mandal")) {
                    showSnackBar("Please Select Mandal", 2);
                }
                else if (fav_loc_name.isEmpty())
                {
                    showSnackBar("Please enter Fav Location Name", 2);
                }
                else {
                    builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);


                    //Setting message manually and performing action on button click
                    builder.setMessage("Do you want add this location to favorites ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent=new Intent(CreateNewLocActivity.this,BottomNavigationActivity.class);
                                    intent.putExtra("location","location");
                                    startActivity(intent);
//                               Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
//                                        Toast.LENGTH_SHORT).show();
                                    dataobj=new JSONObject();

                                    try {
                                        dataobj.put("id", 1);
                                        dataobj.put("name", fav_loc_name);
                                        dataobj.put("userid", user_id_loc);
                                        Log.i("userid","userid"+user_id_loc);
                                        dataobj.put("status", "true");
                                        dataobj.put("villageid", String.valueOf(villageId));
                                        dataobj.put("createdby", 1);
                                        dataobj.put("createdon", "2021-02-28");
                                        dataobj.put("modifiedby", 1);
                                        dataobj.put("modifiedon", "2021-02-28");
                                        dataobj.put("pincode", pincode);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    addtoFav(dataobj);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("");
                    alert.show();
                }
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
        tv_warning.setText("Select your Village");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvStates.setLayoutManager(layoutManager);
        if (!Utility.isNetworkAvailable(this)) {
            showSnackBar(Constarins.PLEASE_CHECK_INTERNET, 2);
            return;
        }
        Utility.showProgress(this);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.villages_dropdown +mandalId + "htype=d", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.hideProgress(CreateNewLocActivity.this);
                Log.e("village Repsonse", "" + response);
                villageResponseList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<VillageResponse>>() {
                }.getType();
                List<VillageResponse> apiListvillage = gson.fromJson(response.toString(), listType);
                villageResponseList.addAll(apiListvillage);
                villageAdapter = new VillageAdapter(CreateNewLocActivity.this, villageResponseList);
                rcvStates.setAdapter(villageAdapter);
                villageAdapter.setOnVillageClickListener(new VillageAdapter.onVillageClickListener() {
                    @Override
                    public void onVillageClickListner(VillageResponse villageResponse) {
                        villageId = villageResponse.getId();
                        Log.e("villageId", "" + villageId);
                        String villageName = villageResponse.getName();
                        activityCreateNewLocBinding.selectVillage.setText(villageName);
                        dialog.dismiss();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress(CreateNewLocActivity.this);
                showSnackBar(Constarins.SOMETHING_WENT_WRONG_IN_SERVER, 3);
            }
        });
        requestQueue.add(stringRequest);

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
        tv_warning.setText("Select your Mandal");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvStates.setLayoutManager(layoutManager);
        if (!Utility.isNetworkAvailable(this)) {
            showSnackBar(Constarins.PLEASE_CHECK_INTERNET, 2);
            return;
        }
        Utility.showProgress(this);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.mandal_dropdown + districtId + "htype=d", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.hideProgress(CreateNewLocActivity.this);
                Log.e("mandal Repsonse", "" + response);
                mandalResponseList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<MandalResponse>>() {
                }.getType();
                List<MandalResponse> apiList2 = gson.fromJson(response.toString(), listType);
                mandalResponseList.addAll(apiList2);
                mandalAdapter = new MandalAdapter(CreateNewLocActivity.this, mandalResponseList);
                rcvStates.setAdapter(mandalAdapter);
                mandalAdapter.setOnMandalCliclListener(new MandalAdapter.OnMandalCliclListener() {
                    @Override
                    public void onMandalClickListener(MandalResponse mandalResponse) {
                        mandalId = mandalResponse.getId();
                        Log.e("distirctId", "" + stateId);
                        String mandalName = mandalResponse.getName();
                        activityCreateNewLocBinding.selectMandal.setText(mandalName);
                        dialog.dismiss();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress(CreateNewLocActivity.this);
                showSnackBar(Constarins.SOMETHING_WENT_WRONG_IN_SERVER, 3);
            }
        });
        requestQueue.add(stringRequest);

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
        tv_warning.setText("Select your District");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvDistrict.setLayoutManager(layoutManager);
        if (!Utility.isNetworkAvailable(this)) {
            showSnackBar(Constarins.PLEASE_CHECK_INTERNET, 2);
            return;
        }
        Utility.showProgress(this);
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.district_dropdown + stateId + "htype=d", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.hideProgress(CreateNewLocActivity.this);
                Log.e("district Repsonse", "" + response);
                districtResponseList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<DistrictResponse>>() {
                }.getType();
                List<DistrictResponse> apiList1 = gson.fromJson(response.toString(), listType);
                districtResponseList.addAll(apiList1);
                districtAdapter = new DistirctAdapter(CreateNewLocActivity.this, districtResponseList);
                rcvDistrict.setAdapter(districtAdapter);
                districtAdapter.setOnDistrictClickListener(new DistirctAdapter.OnDistrictClickListener() {
                    @Override
                    public void onDistrictClickListener(DistrictResponse districtnmaes) {
                        districtId = districtnmaes.getId();
                        Log.e("distirctId", "" + stateId);
                        String districtName = districtnmaes.getName();
                        activityCreateNewLocBinding.selectDistrict.setText(districtName);
                        dialog.dismiss();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress(CreateNewLocActivity.this);
                showSnackBar(Constarins.SOMETHING_WENT_WRONG_IN_SERVER, 3);
            }
        });
        requestQueue.add(stringRequest);

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
                Utility.hideProgress(CreateNewLocActivity.this);
                Log.e("stateResponse1", "" + response);
                stateDataResponseList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<StateDataResponse>>() {
                }.getType();

                List<StateDataResponse> apiList = gson.fromJson(response.toString(), listType);
                stateDataResponseList.addAll(apiList);
                stateAdapter = new StateAdapter(CreateNewLocActivity.this, stateDataResponseList);
                rcvStates.setAdapter(stateAdapter);
                stateAdapter.setonStateClickListener(new StateAdapter.OnStateClickListener() {
                    @Override
                    public void onStateClickListener(StateDataResponse filternames) {
                        stateId = filternames.getId();
                        Log.e("stateId", String.valueOf(stateId));
                         stateName = filternames.getName();
                        activityCreateNewLocBinding.selectState.setText(stateName);
                        dialog.dismiss();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress(CreateNewLocActivity.this);
                showSnackBar(Constarins.SOMETHING_WENT_WRONG_IN_SERVER, 3);
            }
        });
        requestQueue.add(stringRequest);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.dismiss();
            }
        });
    }

public void addtoFav(JSONObject dataobject124){
    if (!Utility.isNetworkAvailable(this)) {
        showSnackBar(Constarins.PLEASE_CHECK_INTERNET, 2);
        return;
    }

    Utility.showProgress(this);
    apimanger.RequestWithObject(POST, Constarins.add_loc_fav, dataobject124, new VolleyInterface() {
        @Override
        public void onSuccess(String result) {
            Log.e("ccccccccc",result);
            Utility.hideProgress(CreateNewLocActivity.this);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
//                        String id = jsonObject.getString("id");
//                        String name = jsonObject.getString("name");
//                        String mobile = jsonObject.getString("mobile");
//                        String email = jsonObject.getString("email");
//                        String password = jsonObject.getString("password");
//                        String tagname = jsonObject.getString("tagname");
//                        String languageid = jsonObject.getString("languageid");
//                        String confirmcode = jsonObject.getString("confirmcode");
//                        String status = jsonObject.getString("status");
//                        String imageurl = jsonObject.getString("imageurl");
//                        String isverified = jsonObject.getString("isverified");
//                        String usertype = jsonObject.getString("usertype");
//                        String stateid = jsonObject.getString("stateid");
//                        String districtid = jsonObject.getString("districtid");
//                        String mandalid = jsonObject.getString("mandalid");
//                        String villageid = jsonObject.getString("villageid");
//                        String pincode = jsonObject.getString("pincode");
                        Log.i("fav", "" + result);
                        Toast.makeText(CreateNewLocActivity.this, "location Successfully added to facvorites", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


        @Override
        public void onError(String error, int statusCode) {
            Utility.hideProgress(CreateNewLocActivity.this);
            Log.e("ccccccccc",error);

        }
    });

}
    @Override
    public void showSnackBar(String snackBarText, int type) {
        Utility.showSnackBar(CreateNewLocActivity.this, activityCreateNewLocBinding.coordinator, snackBarText, type);
    }

    @Override
    public Activity activity() {
        return this;
    }
}