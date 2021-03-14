package com.thinkgenie.news.ReadFullArtcle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.thinkgenie.news.Activities.BottomNavigationActivity;
import com.thinkgenie.news.Activities.NotifictaionsActivity;
import com.thinkgenie.news.Activities.ProfileActivity;
import com.thinkgenie.news.Banner.NewsGalleryAdapter;
import com.thinkgenie.news.Banner.NewsGalleryModel;
import com.thinkgenie.news.Helpers.SessionManager;
import com.thinkgenie.news.R;
import com.thinkgenie.news.consstrains.Constarins;
import com.thinkgenie.news.databinding.ActivityReadFullArticleBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class ReadFullArticleActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    ActivityReadFullArticleBinding activityReadFullArticleBinding;
    String newsTypeId,user_name,tag_name;
    int newstype_id;

    private List<NewsGalleryModel> newsGalleryModelList;
    private ViewPager mPager;
    private int currentPage;

    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_read_full_article);
        activityReadFullArticleBinding = DataBindingUtil.setContentView(this, R.layout.activity_read_full_article);
        newsTypeId = getIntent().getStringExtra("newsTypeId");

        Log.e("newtypeId","newstype"+newsTypeId);
        sessionManager = new SessionManager(ReadFullArticleActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        user_name = user.get(SessionManager.KEY_NAME);
        tag_name = user.get(SessionManager.KEY_TAGNAME);
       // newstype_id = user.get(SessionManager.KEY_NEWSINPUTID);
        getSliderImages();
        activityReadFullArticleBinding.newsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReadFullArticleActivity.this, BottomNavigationActivity.class);
                startActivity(intent);
            }
        });
        activityReadFullArticleBinding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReadFullArticleActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        }); activityReadFullArticleBinding.notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReadFullArticleActivity.this, NotifictaionsActivity.class);
                startActivity(intent);
            }
        });

        getData();

    }

    private void getData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.readFullArticle + "newsTypeId="+newsTypeId + "&htype=d", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String id = jsonObject2.getString("id");
                        String title = jsonObject2.getString("title");
                        String date = jsonObject2.getString("date");
                        String description = jsonObject2.getString("description");
                        activityReadFullArticleBinding.title.setText(title);
                        activityReadFullArticleBinding.descrptionTv.setText(description);
                        activityReadFullArticleBinding.personName.setText(user_name);
                        activityReadFullArticleBinding.personTagname.setText(tag_name);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void getSliderImages() {
        RequestQueue requestQueue = Volley.newRequestQueue(ReadFullArticleActivity.this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constarins.readFullArticle + "newsTypeId="+newsTypeId + "&htype=d",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("newsgallery","newgallery"+response);
                        newsGalleryModelList = new ArrayList<>();
                        try {
                            JSONArray jsonArray_res=new JSONArray(response);
                            for (int i = 0; i < jsonArray_res.length(); i++)
                            {
                                JSONObject object = jsonArray_res.getJSONObject(i);

                                if(object.has("NewsGallery") && !object.isNull("NewsGallery") ){
                                JSONArray jsonArray_newsgallery=object.getJSONArray("NewsGallery");


                                for (int j = 0; j< jsonArray_newsgallery.length(); j++) {
                                    JSONObject newsgalleryObjec = jsonArray_newsgallery.getJSONObject(j);
                                    String id = newsgalleryObjec.getString("id");
                                    String nid = newsgalleryObjec.getString("nid");
                                    String contenturl = newsgalleryObjec.getString("contenturl");

                                    NewsGalleryModel newsGalleryModel = new NewsGalleryModel();
                                    newsGalleryModel.setContenturl(contenturl);

                                    newsGalleryModelList.add(newsGalleryModel);

                                }

                                }else {

                                }

                            }

                            } catch (JSONException jsonException) {
                            jsonException.printStackTrace();

                        }
                        if (newsGalleryModelList.size() > 0) {

                            mPager = (ViewPager) findViewById(R.id.pager);
                            mPager.setAdapter(new NewsGalleryAdapter(ReadFullArticleActivity.this, newsGalleryModelList));
//                            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                            activityReadFullArticleBinding.indicator.setViewPager(mPager);

                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == newsGalleryModelList.size()) {
                                        currentPage = 0;
                                    }
                                    mPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 5000, 5000);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}