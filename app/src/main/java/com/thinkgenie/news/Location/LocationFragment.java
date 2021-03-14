package com.thinkgenie.news.Location;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.thinkgenie.news.Activities.NotifictaionsActivity;
import com.thinkgenie.news.Activities.ProfileActivity;
import com.thinkgenie.news.FillProfile.FillProfileActivity;
import com.thinkgenie.news.FillProfile.VillageAdapter;
import com.thinkgenie.news.FillProfile.VillageResponse;
import com.thinkgenie.news.Helpers.SessionManager;
import com.thinkgenie.news.R;
import com.thinkgenie.news.Utilities.Utility;
import com.thinkgenie.news.consstrains.Constarins;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LocationFragment extends Fragment {
    private RecyclerView recyclerView_locat;
    LoctaionAdapter loctaionAdapter;
    SavedLocationModel savedLocationModel;
    List<SavedLocationModel> savedLocationModelList;
    ImageView notifications, profile;
    TextView no_data;
    LinearLayout saved_lcocations;
    Button btn_createLoctn;
    View view;
    SessionManager sessionManager;
    String user_id_loc;


public static CreateNewLocActivity.refreshData refreshList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_location, container, false);


        recyclerView_locat = view.findViewById(R.id.recylerview);
        notifications = view.findViewById(R.id.notifications);
        no_data = view.findViewById(R.id.no_data);
        sessionManager = new SessionManager(getContext());
        user_id_loc = sessionManager.useridstoreget();

        Log.i("userid_locfragment",""+user_id_loc);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_locat.setLayoutManager(layoutManager);

        refreshList = this::refreshList;
        saved_lcocations = view.findViewById(R.id.saved_locations_linear);
        profile = view.findViewById(R.id.profile);

        btn_createLoctn = view.findViewById(R.id.btn_createLoctn);
        btn_createLoctn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateNewLocActivity.class);
                startActivity(intent);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NotifictaionsActivity.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });


        savedLocation();

        return view;
    }

    private void refreshList() {
        savedLocationModelList = new ArrayList<>();
        Utility.showProgress(getActivity());
        savedLocation();
    }

    private void savedLocation() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.saved_location + "userid="+ user_id_loc + "htype=d", new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                savedLocationModelList = new ArrayList<>();
                try {
                    Log.i("respons", "" + response);
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject memberOject = jsonArray.getJSONObject(i);
                        savedLocationModel = new SavedLocationModel();
                        savedLocationModel.setName(memberOject.getString("name"));
                        savedLocationModelList.add(savedLocationModel);
                    }
                    if (savedLocationModelList.size() > 0) {
                        loctaionAdapter = new LoctaionAdapter(savedLocationModelList);
                        recyclerView_locat.setAdapter(loctaionAdapter);
                        no_data.setVisibility(View.GONE);

                    } else {
                        no_data.setText("No Data Available");
                        no_data.setVisibility(View.VISIBLE);
                    }
                    loctaionAdapter = new LoctaionAdapter(savedLocationModelList);
                    recyclerView_locat.setAdapter(loctaionAdapter);
                    loctaionAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        savedLocation();
        super.onResume();
    }


}
