package com.thinkgenie.news.FillProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkgenie.news.R;

import java.util.List;

public class VillageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<VillageResponse> villageResponseList;

 private onVillageClickListener onVillageClickListener;

    public VillageAdapter(Context context, List<VillageResponse> villageResponseList) {
        this.context = context;
        this.villageResponseList = villageResponseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.state_dropdown_custom_layout;
        return new CountryViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CountryViewHolder) {
            CountryViewHolder countryViewHolder = (CountryViewHolder) holder;
            countryViewHolder.tvName.setText(villageResponseList.get(position).getName());
            countryViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    countryViewHolder.tvName.setText(villageResponseList.get(position).getName());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return villageResponseList.size();
    }


    public void setOnVillageClickListener(onVillageClickListener onVillageClickListener) {
        this.onVillageClickListener = onVillageClickListener;
    }


    public class CountryViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        LinearLayout linearLayout;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            linearLayout = itemView.findViewById(R.id.linearLayout);


            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (onVillageClickListener != null)
                        onVillageClickListener.onVillageClickListner(villageResponseList.get(getAdapterPosition()));


                }
            });


        }
    }
    public interface onVillageClickListener {
        void onVillageClickListner(VillageResponse villageResponse);
    }

}