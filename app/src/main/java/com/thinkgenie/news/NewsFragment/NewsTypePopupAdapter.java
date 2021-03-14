package com.thinkgenie.news.NewsFragment;


import android.graphics.Color;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.thinkgenie.news.R;

import java.util.List;

public class NewsTypePopupAdapter extends RecyclerView.Adapter<NewsTypePopupAdapter.MyViewHolder> {
    private int lastSelectedPosition = -1;
    List<Newstypemodel> stateDataModelList;

    public NewsTypePopupAdapter(List<Newstypemodel> stateDataModelList) {
        this.stateDataModelList = stateDataModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_newstypepopup_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Newstypemodel model = stateDataModelList.get(position);
        holder.newsTypeName.setText(stateDataModelList.get(position).getName());
        holder.newsTypeName.setText(model.getName());
        holder.itemView.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
        holder.newsTypeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check whether you selected an item

                if (lastSelectedPosition > 0) {
                    stateDataModelList.get(lastSelectedPosition).setSelected(false);
                }

                model.setSelected(!model.isSelected());
                holder.itemView.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);

                // store last selected item position

                lastSelectedPosition = holder.getAdapterPosition();
            }
        });
    }


    @Override
    public int getItemCount() {
        return stateDataModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView newsTypeName;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            newsTypeName = itemView.findViewById(R.id.news_headline_tv);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}