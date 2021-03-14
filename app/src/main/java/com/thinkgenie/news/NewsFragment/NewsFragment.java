
package com.thinkgenie.news.NewsFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinkgenie.news.Activities.NotifictaionsActivity;
import com.thinkgenie.news.Activities.ProfileActivity;

import com.thinkgenie.news.Apimanger.Apimanger;
import com.thinkgenie.news.Apimanger.VolleyInterface;

import com.thinkgenie.news.FillProfile.FillProfileActivity;
import com.thinkgenie.news.FillProfile.VillageAdapter;
import com.thinkgenie.news.FillProfile.VillageResponse;
import com.thinkgenie.news.Helpers.SessionManager;
import com.thinkgenie.news.R;
import com.thinkgenie.news.Utilities.Utility;
import com.thinkgenie.news.consstrains.Constarins;
import com.thinkgenie.news.interfaces.EActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thinkgenie.news.Utilities.Apicontant.GET;

public class NewsFragment extends Fragment {
    View view;
    NewsAdapter newsAdapter;
    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;
    ArrayList<NewsModel> newsdataList;
    NewsModel newsModel;
    ImageView search_iv;
    EditText search_et;
    ImageView notifications, profile;
    TextView news_toolbarTv, news_type, filters, favorites, no_packages_available;
    NewsTypePopupAdapter newsTypePopupAdapter;
    MyFavoritesPopupAdapter myFavoritesPopupAdapter;
    ArrayList<Newstypemodel> newstypeList;
    ArrayList<MyFavoritesModel> myFavList;
    int newsTypeId;
    Newstypemodel newstypemodel;
    MyFavoritesModel myFavoritesModel;
    List fav_popuplist = new ArrayList<>();
    String isview = "few";
    RecyclerView news_recyclerview, recyclerView_popup;
    public static boolean isSignedIn = false;
    Apimanger apimanger;
    SessionManager sessionManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_news, container, false);
        sessionManager = new SessionManager(getContext());
        news_recyclerview = view.findViewById(R.id.news_recyclerview);
        news_toolbarTv = view.findViewById(R.id.news_toolbarTv);
        notifications = view.findViewById(R.id.notifications);
        news_type = view.findViewById(R.id.news_type);
        favorites = view.findViewById(R.id.fav);
        filters = view.findViewById(R.id.filters);
        search_et = view.findViewById(R.id.search_et);
        search_iv = view.findViewById(R.id.search_iv);
        profile = view.findViewById(R.id.profile);
        no_packages_available = view.findViewById(R.id.no_packages_available);

        apimanger = new Apimanger(getActivity());

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);
        news_recyclerview.setLayoutManager(manager);
        news_recyclerview.setAdapter(newsAdapter);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NotifictaionsActivity.class);
                startActivity(intent);
            }
        });
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                favoritesPopmenu();
            }
        });
        filters.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                initiatePopupWindow();

            }

        });
        news_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsTypePopmenu();

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        newsDataApi("");
        return view;
    }

    private void newsDataApi(String typ) {
        String type;
        if (!typ.equals("")) {
            type = "?newsTypeId=" + typ + "&htype=d";
        } else {
            type = "";
        }
        if (!Utility.isNetworkAvailable(getActivity())) {
            Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        Utility.showProgress(getActivity());
        newsdataList = new ArrayList<>();
        Map<String, String> news = new HashMap<String, String>();
        apimanger.Request(GET, Constarins.NEWS + type, news, new VolleyInterface() {
            @Override
            public void onSuccess(String result) {
                Log.e("wwwwww", result);
                String reslt = result;
                try {
                    Utility.hideProgress(getActivity());
                    JSONArray jsonArray = new JSONArray(result);

                    Log.e("wwwwww2222", result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        newsModel = new NewsModel();
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String id = jsonObject2.getString("id");
                        String title = jsonObject2.getString("title");
                        String date = jsonObject2.getString("date");
                        newsTypeId = jsonObject2.getInt("newstypeid");
                        sessionManager.createNewsSession(newsTypeId);
                        Log.e("wwwwww3333", id + "   " + title + "   " + date + "   " + date);
                        newsModel.setId(Integer.valueOf(jsonObject2.getString("id")));

                        newsModel.setTitle(jsonObject2.getString("title"));
                        newsModel.setDate(jsonObject2.getString("date"));
                        newsModel.setDescription(jsonObject2.getString("description"));
                        newsModel.setArticletypeid(Integer.valueOf(jsonObject2.getString("articletypeid")));
                        //  newsModel.setname(Integer.valueOf(jsonObject2.getString("name")));
                        newsModel.setVillageid(Integer.valueOf(jsonObject2.getString("villageid")));
                        newsModel.setNewstypeid(Integer.valueOf(jsonObject2.getString("newstypeid")));
                        newsModel.setStatus(Boolean.valueOf(jsonObject2.getString("status")));
                        newsModel.setCreatedon(jsonObject2.getString("createdon"));
                        newsModel.setCreatedby(Integer.valueOf(jsonObject2.getString("createdby")));
                        newsModel.setModifiedby(Integer.valueOf(jsonObject2.getString("modifiedby")));
                        newsModel.setModifiedon(jsonObject2.getString("modifiedon"));
                        newsdataList.add(newsModel);
                    }
                    newsAdapter = new NewsAdapter(newsdataList, getContext());
                    news_recyclerview.setAdapter(newsAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, int statusCode) {
                Log.e("wwwwww error", error);
                Utility.hideProgress(getActivity());
                no_packages_available.setText("Something Went Wrong");
            }
        });

    }

    //filters popup menu
    private PopupWindow initiatePopupWindow() {

        try {

            mInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.fliters_menu_layout, null);
            final TextView textview_fliter = (TextView) layout.findViewById(R.id.textview_fliter);
            final TextView textview_fliter_one = (TextView) layout.findViewById(R.id.textview_fliter_one);
            final TextView textview_fliter_two = (TextView) layout.findViewById(R.id.textview_fliter_two);
            final TextView textview_fliter_three = (TextView) layout.findViewById(R.id.textview_fliter_three);

            final ImageView rightmark_iv = (ImageView) layout.findViewById(R.id.rightmark_iv);
            final ImageView rightmark_iv_1 = (ImageView) layout.findViewById(R.id.rightmark_iv_one);
            final ImageView rightmark_iv_2 = (ImageView) layout.findViewById(R.id.rightmark_iv_two);
            final ImageView rightmark_iv_3 = (ImageView) layout.findViewById(R.id.rightmark_iv_three);
            RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.NewsContents, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            String id = jsonObject.getString("id");
                            if (id.equals("1")) {
                                textview_fliter.setText(name);
                            }
                            if (id.equals("2")) {
                                textview_fliter_one.setText(name);

                            }
                            if (id.equals("3")) {
                                textview_fliter_two.setText(name);

                            }
                            if (id.equals("4")) {
                                textview_fliter_three.setText(name);

                            }

                            textview_fliter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (rightmark_iv.getVisibility() == View.VISIBLE) {

                                        rightmark_iv.setVisibility(view.INVISIBLE);
                                    } else {
                                        rightmark_iv.setVisibility(view.VISIBLE);
                                    }
                                    newsDataApi(String.valueOf(newsTypeId));
                                   // newsAdapter.refreshCartAmount(getContext());


                                }

                            });
                            textview_fliter_one.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (rightmark_iv_1.getVisibility() == View.VISIBLE) {

                                        rightmark_iv_1.setVisibility(view.INVISIBLE);
                                    } else {
                                        rightmark_iv_1.setVisibility(view.VISIBLE);
                                    }
                                    newsDataApi(String.valueOf(newsTypeId));
                                    // getData();
                                }
                            });
                            textview_fliter_two.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (rightmark_iv_2.getVisibility() == View.VISIBLE) {

                                        rightmark_iv_2.setVisibility(view.INVISIBLE);
                                    } else {
                                        rightmark_iv_2.setVisibility(view.VISIBLE);
                                    }
                                    newsDataApi(String.valueOf(newsTypeId));
                                    //   getData();
                                }
                            });
                            textview_fliter_three.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (rightmark_iv_3.getVisibility() == View.VISIBLE) {

                                        rightmark_iv_3.setVisibility(view.INVISIBLE);
                                    } else {
                                        rightmark_iv_3.setVisibility(view.VISIBLE);
                                    }
                                    newsDataApi(String.valueOf(newsTypeId));
                                    //   getData();
                                }
                            });

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utility.hideProgress(getActivity());
                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(stringRequest);


            layout.measure(View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
            Drawable background = getResources().getDrawable(android.R.drawable.alert_light_frame);
            mDropdown.setBackgroundDrawable(background);
            mDropdown.showAsDropDown(filters, 5, 5);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;

    }


    public void favoritesPopmenu() {
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.fav_popup_layout, viewGroup, false);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancelIv = dialogView.findViewById(R.id.close_popup);
        cancelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        recyclerView_popup = dialogView.findViewById(R.id.recyclerView_popup);
        news_toolbarTv = dialogView.findViewById(R.id.news_toolbarTv);
        news_toolbarTv.setText("My favourites");

        Map<String, String> news = new HashMap<String, String>();
        myFavList = new ArrayList<>();
        apimanger.Request(GET, Constarins.MyFavorites, news, new VolleyInterface() {
            @Override
            public void onSuccess(String result) {
                String reslt = result;
                try {
                    JSONArray jsonArraytype = new JSONArray(result);

                    for (int z = 0; z < jsonArraytype.length(); z++) {
                        myFavoritesModel = new MyFavoritesModel();
                        JSONObject jsonObject2 = jsonArraytype.getJSONObject(z);
                        // myFavoritesModel.setId(jsonObject2.getString("id"));
                        myFavoritesModel.setName(jsonObject2.getString("name"));


                        myFavList.add(myFavoritesModel);


                    }

                    myFavoritesPopupAdapter = new MyFavoritesPopupAdapter(myFavList, getContext());
                    RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);
                    recyclerView_popup.setLayoutManager(manager);
                    recyclerView_popup.setAdapter(myFavoritesPopupAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String error, int statusCode) {

            }
        });

        alertDialog.show();
//        Rect displayRectangle = new Rect();
//        Window window = getActivity().getWindow();
//        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
//        ViewGroup viewGroup = view.findViewById(android.R.id.content);
//        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.news_popup_layout, viewGroup, false);
//        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
//        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
//        builder.setView(dialogView);
//        final AlertDialog alertDialog = builder.create();
//        ImageView cancelIv = dialogView.findViewById(R.id.close_popup);
//        cancelIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//        recyclerView_popup = dialogView.findViewById(R.id.recyclerView_popup);
//        news_toolbarTv = dialogView.findViewById(R.id.news_toolbarTv);
//        news_toolbarTv.setText(" Favorite lists");
//        myFavoritesPopupAdapter = new MyFavoritesPopupAdapter(fav_popuplist, getContext());
//        myFavoritesPopupAdapter.notifyDataSetChanged();
//        myFavoritesPopupAdapter.setHasStableIds(false);
//        RecyclerView.LayoutManager manager2 = new GridLayoutManager(getContext(), 1);
//        recyclerView_popup.setLayoutManager(manager2);
//        recyclerView_popup.setAdapter(myFavoritesPopupAdapter);
//        StateDataModel data = new StateDataModel("My local news");
//        fav_popuplist.add(data);
//        data = new StateDataModel("News from my fav places");
//        fav_popuplist.add(data);
//        data = new StateDataModel("My states news");
//        fav_popuplist.add(data);
//        data = new StateDataModel("My political news");
//        fav_popuplist.add(data);
//        data = new StateDataModel("Entertainment  i follow");
//        fav_popuplist.add(data);
//        data = new StateDataModel("Favorite");
//        fav_popuplist.add(data);
//
//
//        alertDialog.show();

    }


    public void newsTypePopmenu() {
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.news_popup_layout, viewGroup, false);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancelIv = dialogView.findViewById(R.id.close_popup);
        cancelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        recyclerView_popup = dialogView.findViewById(R.id.recyclerView_popup);
        news_toolbarTv = dialogView.findViewById(R.id.news_toolbarTv);
        news_toolbarTv.setText(" News Type");

        Map<String, String> news = new HashMap<String, String>();
        newstypeList = new ArrayList<>();
        apimanger.Request(GET, Constarins.NEWSTYPES, news, new VolleyInterface() {
            @Override
            public void onSuccess(String result) {
                String reslt = result;
                try {
                    JSONArray jsonArraytype = new JSONArray(result);

                    for (int z = 0; z < jsonArraytype.length(); z++) {
                        newstypemodel = new Newstypemodel();
                        JSONObject jsonObject2 = jsonArraytype.getJSONObject(z);
                        newstypemodel.setId(jsonObject2.getString("id"));
                        newstypemodel.setName(jsonObject2.getString("type"));


                        newstypeList.add(newstypemodel);


                    }

                    newsTypePopupAdapter = new NewsTypePopupAdapter(newstypeList);
                    RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 2);
                    recyclerView_popup.setLayoutManager(manager);
                    recyclerView_popup.setAdapter(newsTypePopupAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String error, int statusCode) {

            }
        });

        alertDialog.show();
    }
}
