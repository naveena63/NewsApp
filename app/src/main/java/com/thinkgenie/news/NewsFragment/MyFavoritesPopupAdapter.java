package com.thinkgenie.news.NewsFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkgenie.news.R;

import java.util.List;

public class MyFavoritesPopupAdapter extends RecyclerView.Adapter<MyFavoritesPopupAdapter.MyViewHolder> {
    List<MyFavoritesModel> myFavoritesModelList;
Context context;
    public MyFavoritesPopupAdapter(List<MyFavoritesModel> myFavoritesModelList, Context context) {
        this.myFavoritesModelList = myFavoritesModelList;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.favorites_popup_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.favName.setText(myFavoritesModelList.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return myFavoritesModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView favName;


        public MyViewHolder(View itemView) {
            super(itemView);
            favName = itemView.findViewById(R.id.fav_list_text);
        }
    }
}