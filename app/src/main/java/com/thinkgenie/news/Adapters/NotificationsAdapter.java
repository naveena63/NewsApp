package com.thinkgenie.news.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkgenie.news.Model.NotifictaionsModel;
import com.thinkgenie.news.R;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {
        List<NotifictaionsModel> notifictaionsModelList;
        Context context;
public NotificationsAdapter(List<NotifictaionsModel> notifictaionsModelList,Context context) {
        this.notifictaionsModelList = notifictaionsModelList;
        this.context=context;
        }
@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.notifications_custom_layout, viewGroup, false);
        return new MyViewHolder(itemView);
        }
@Override
public void onBindViewHolder(MyViewHolder viewHolder, int i) {

    viewHolder.name.setText(notifictaionsModelList.get(i).getName());
    viewHolder.likePostText.setText(notifictaionsModelList.get(i).getLikePostText());
    viewHolder.timing.setText(notifictaionsModelList.get(i).getTiming());


        }
@Override
public int getItemCount() {
        return notifictaionsModelList.size();
        }
public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView name,likePostText,timing;

    public MyViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.person_name);
        likePostText = itemView.findViewById(R.id.likd_text);
        timing = itemView.findViewById(R.id.time_text);


    }
}
}