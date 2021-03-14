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

public class MandalAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<MandalResponse> mandalResponseList;

    private OnMandalCliclListener onMandalCliclListener;

    public MandalAdapter(Context context, List<MandalResponse> mandalResponseList) {
        this.context = context;
        this.mandalResponseList = mandalResponseList;
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
            countryViewHolder.tvName.setText(mandalResponseList.get(position).getName());
            countryViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    countryViewHolder.tvName.setText(mandalResponseList.get(position).getName());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mandalResponseList.size();
    }


    public void setOnMandalCliclListener(OnMandalCliclListener onMandalCliclListener) {
        this.onMandalCliclListener = onMandalCliclListener;
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

                    if (onMandalCliclListener != null)
                        onMandalCliclListener.onMandalClickListener(mandalResponseList.get(getAdapterPosition()));


                }
            });


        }
    }
    public interface OnMandalCliclListener {
        void onMandalClickListener(MandalResponse mandalResponse);
    }

}
