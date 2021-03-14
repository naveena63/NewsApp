package com.thinkgenie.news.compose;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.thinkgenie.news.R;
import com.thinkgenie.news.interfaces.customButtonListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.ViewHolderRecycler> {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<Bitmap> mData = new ArrayList<>();
    customButtonListener customListner;

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    public RecyclerImageAdapter(Context context, ArrayList<Bitmap> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    public ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_image_item, parent, false);
        return new ViewHolderRecycler(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolderRecycler holder, final int position) {

        holder.imgUserImage.setImageBitmap(mData.get(position));
        holder.imgUserImage.setOnClickListener(v -> {
            mData.remove(position);
            notifyDataSetChanged();
            if (customListner != null) {
                customListner.onButtonClickListner(position,mData.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        CircleImageView imgUserImage;
        ImageView imgDeleteImage;

        public ViewHolderRecycler(View itemView) {
            super(itemView);
            imgUserImage = itemView.findViewById(R.id.imgUserImage);
            imgDeleteImage = itemView.findViewById(R.id.imgDeleteImage);
        }
    }
}