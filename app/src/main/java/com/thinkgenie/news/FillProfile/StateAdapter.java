package com.thinkgenie.news.FillProfile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkgenie.news.R;

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<StateDataResponse> stateDataResponseList;
    private StateAdapter.OnStateClickListener onStateClickListener;


    public StateAdapter(Context context, List<StateDataResponse> stateDataResponseList) {
        this.context = context;
        this.stateDataResponseList = stateDataResponseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.state_dropdown_custom_layout;
        return new StateAdapter.CountryViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CountryViewHolder) {
            StateAdapter.CountryViewHolder countryViewHolder = (CountryViewHolder) holder;
            countryViewHolder.tvName.setText(stateDataResponseList.get(position).getName());
            countryViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    countryViewHolder.tvName.setText(stateDataResponseList.get(position).getName());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return stateDataResponseList.size();
    }

    public void setonStateClickListener(OnStateClickListener onStateClickListener) {
        this.onStateClickListener = onStateClickListener;
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

                    if (onStateClickListener != null)
                        onStateClickListener.onStateClickListener(stateDataResponseList.get(getAdapterPosition()));


                }
            });


        }
    }

    public interface OnStateClickListener {
        void onStateClickListener(StateDataResponse filternames);
    }


}
