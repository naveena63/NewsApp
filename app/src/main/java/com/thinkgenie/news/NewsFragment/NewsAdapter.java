package com.thinkgenie.news.NewsFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.thinkgenie.news.Activities.BottomNavigationActivity;
import com.thinkgenie.news.ReadFullArtcle.ReadFullArticleActivity;
import com.thinkgenie.news.R;

import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    List<NewsModel> newsModelList;
    Context context;

    public NewsAdapter(List<NewsModel> newsModelList, Context context) {
        this.newsModelList = newsModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_news_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {


        viewHolder.news_headline_tv.setText(newsModelList.get(i).getTitle());
        //viewHolder.timing_newspost.setText(newsModelList.get(i).getTiming_newspost());
String newsId= String.valueOf((newsModelList.get(i).getNewstypeid()));
        viewHolder.newsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReadFullArticleActivity.class);
                intent.putExtra("newsTypeId", newsId);

                context.startActivity(intent);
            }

        });

        viewHolder.settings_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, viewHolder.settings_iv);
                //inflating menu from xml resource
                popup.inflate(R.menu.popup_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.report_offensive:
                                Toast.makeText(context, "Report Offensive Clicked", Toast.LENGTH_SHORT).show();
                                return true;
//

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }

        });

    }
    public void refreshCartAmount(Context context) {
        Intent intent = new Intent(context, BottomNavigationActivity.class);

        context.startActivity(intent);
        ((Activity) context).finish();
    }
    @Override
    public int getItemCount() {
        return newsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView news_headline_tv, report_offensive, timing_newspost, read_full_article;
        ImageView settings_iv;
        LinearLayout newsLinear;

        public MyViewHolder(View itemView) {
            super(itemView);
            news_headline_tv = itemView.findViewById(R.id.news_headline_tv);
            timing_newspost = itemView.findViewById(R.id.timing_newspost);
            read_full_article = itemView.findViewById(R.id.read_full_article);
            settings_iv = itemView.findViewById(R.id.settings_iv);
            newsLinear = itemView.findViewById(R.id.linear_news);

        }
    }

}