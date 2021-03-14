package com.thinkgenie.news.compose;

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

public class SelectNewstypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<SelectNewstypemodel> newstypemodelsList;
    private OnNewsTypeClickListener onNewsTypeClickListener;


    public SelectNewstypeAdapter(Context context, List<SelectNewstypemodel> newstypemodelsList) {
        this.context = context;
        this.newstypemodelsList = newstypemodelsList;
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
            countryViewHolder.tvName.setText(newstypemodelsList.get(position).getType());
            countryViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    countryViewHolder.tvName.setText(newstypemodelsList.get(position).getType());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return newstypemodelsList.size();
    }

    public void setonNewsTypeClickListener(OnNewsTypeClickListener onNewsTypeClickListener) {
        this.onNewsTypeClickListener = onNewsTypeClickListener;
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

                    if (onNewsTypeClickListener != null)
                        onNewsTypeClickListener.onNewsTypeClickListener(newstypemodelsList.get(getAdapterPosition()));

                }
            });
        }
    }

    public interface OnNewsTypeClickListener {
        void onNewsTypeClickListener(SelectNewstypemodel filternames);
    }
}
