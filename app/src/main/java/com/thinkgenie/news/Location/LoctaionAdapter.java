package com.thinkgenie.news.Location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkgenie.news.R;

import java.util.List;

public class LoctaionAdapter extends RecyclerView.Adapter<LoctaionAdapter.ViewHolder> {

    private Context context;
    private List<SavedLocationModel> loctaionAdapterList;

    LoctaionAdapter.SelectedList callback;

    public interface SelectedList {
        public void selectList();
    }
    public LoctaionAdapter(List<SavedLocationModel> loctaionAdapterList) {
        this.loctaionAdapterList = loctaionAdapterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.custom_saved_location, parent, false);


        return new LoctaionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.locationName.setText(loctaionAdapterList.get(position).getName());
        if (callback != null) {
            callback.selectList();
        }
    }

    @Override
    public int getItemCount() {
        return loctaionAdapterList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView locationName;


        ViewHolder(View itemView) {
            super(itemView);

            locationName = itemView.findViewById(R.id.location_name);


        }

    }
}