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

public class DistirctAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<DistrictResponse> districtResponseList;
    private DistirctAdapter.OnDistrictClickListener onDistrictClickListener;


    public DistirctAdapter(Context context, List<DistrictResponse> districtResponseList) {
        this.context = context;
        this.districtResponseList = districtResponseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.state_dropdown_custom_layout;
        return new DistirctAdapter.CountryViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DistirctAdapter.CountryViewHolder) {
            DistirctAdapter.CountryViewHolder countryViewHolder = (DistirctAdapter.CountryViewHolder) holder;
            countryViewHolder.tvName.setText(districtResponseList.get(position).getName());
            countryViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    countryViewHolder.tvName.setText(districtResponseList.get(position).getName());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return districtResponseList.size();
    }

    public void setOnDistrictClickListener(OnDistrictClickListener onStateClickListener) {
        this.onDistrictClickListener = onStateClickListener;
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

                    if (onDistrictClickListener != null)
                        onDistrictClickListener.onDistrictClickListener(districtResponseList.get(getAdapterPosition()));


                }
            });


        }
    }

    public interface OnDistrictClickListener {
        void onDistrictClickListener(DistrictResponse districtnmaes);
    }

}