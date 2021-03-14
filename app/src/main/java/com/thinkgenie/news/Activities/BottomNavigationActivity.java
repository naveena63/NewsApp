package com.thinkgenie.news.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thinkgenie.news.BottomNavigationViewEx;
import com.thinkgenie.news.compose.ComposeFragment;
import com.thinkgenie.news.LiveStreaming.LiveStreamFragment;
import com.thinkgenie.news.Location.LocationFragment;
import com.thinkgenie.news.NewsFragment.NewsFragment;
import com.thinkgenie.news.R;
import com.thinkgenie.news.databinding.ActivityBottomNavigationBinding;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationActivity extends AppCompatActivity  {
    private ActivityBottomNavigationBinding activityBottomNavigationBinding;
    BottomNavigationViewEx bnve;
    private List<Fragment> fragments;
    private VpAdapter adapter;
    ViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_bottom_navigation);
         bnve=findViewById(R.id.bnve);
         vp=findViewById(R.id.vp);
       // activityBottomNavigationBinding = DataBindingUtil.setContentView(this, R.layout.activity_bottom_navigation);
        initData();
        initEvent();
        initView();
        if(getIntent().getStringExtra("location")!=null){
            vp.setCurrentItem(vp.getCurrentItem() + 1, true);

        }
    }
        private void initData() {
            fragments = new ArrayList<>(4);

            // create music fragment and add it
            NewsFragment newsFragment = new NewsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", "news");
            newsFragment.setArguments(bundle);

            // create backup fragment and add it
            LocationFragment locationFragment = new LocationFragment();
            bundle = new Bundle();
            bundle.putString("title", "location");
            locationFragment.setArguments(bundle);

            // create friends fragment and add it
            LiveStreamFragment liveNewsFragment = new LiveStreamFragment();
            bundle = new Bundle();
            bundle.putString("title", "livenews");
            liveNewsFragment.setArguments(bundle);

            // create friends fragment and add it
            ComposeFragment composeFragment = new ComposeFragment();
            bundle = new Bundle();
            bundle.putString("title", "compose");
            composeFragment.setArguments(bundle);

            // add to fragments for adapter
            fragments.add(newsFragment);
            fragments.add(locationFragment);
            fragments.add(liveNewsFragment);
            fragments.add(composeFragment);
        }

        private void initView() {
            bnve.enableItemShiftingMode(false);
            bnve.enableShiftingMode(false);
            bnve.enableAnimation(false);

            // set adapter
            adapter = new VpAdapter(getSupportFragmentManager(), fragments);
            vp.setAdapter(adapter);
        }

        /**
         * set listeners
         */
        private void initEvent() {
            // set listener to change the current item of view pager when click bottom nav item
            bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                private int previousPosition = -1;

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int position = 0;
                    switch (item.getItemId()) {
                        case R.id.i_music:
                            position = 0;
                            break;
                        case R.id.i_backup:
                            position = 1;
                            break;
                        case R.id.i_favor:
                            position = 2;
                            break;
                        case R.id.i_visibility:
                            position = 3;
                            break;

                    }
                    if (previousPosition != position) {
                        vp.setCurrentItem(position, false);
                        previousPosition = position;
                        Log.i("sjdx", "-----bnve-------- previous item:" + bnve.getCurrentItem() + " current item:" + position + " ------------------");
                    }

                    return true;
                }
            });

            // set listener to change the current checked item of bottom nav when scroll view pager
            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    Log.i("mxa", "-----ViewPager-------- previous item:" + bnve.getCurrentItem() + " current item:" + position + " ------------------");
                    if (position >= 2)// 2 is center
                        position++;// if page is 2, need set bottom item to 3, and the same to 3 -> 4
                    bnve.setCurrentItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }

        private static class VpAdapter extends FragmentPagerAdapter {
            private List<Fragment> data;

            public VpAdapter(FragmentManager fm, List<Fragment> data) {
                super(fm);
                this.data = data;
            }

            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Fragment getItem(int position) {
                return data.get(position);
            }
        }

    @Override
    public void onBackPressed() {
        finish();
    }
}