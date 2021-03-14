package com.thinkgenie.news.Banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.thinkgenie.news.R;
import com.thinkgenie.news.consstrains.Constarins;

import java.util.List;

public class NewsGalleryAdapter extends PagerAdapter {
    private List<NewsGalleryModel> images;
    private LayoutInflater inflater;
    private Context context;


    public NewsGalleryAdapter(Context context, List<NewsGalleryModel> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        final NewsGalleryModel newsGalleryModel = images.get(position);
        View myImageLayout = inflater.inflate(R.layout.news_gallery_layout, view, false);
        final ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image_newsgallery);
        final TextView textView = myImageLayout.findViewById(R.id.textLabel);
        textView.setText(newsGalleryModel.getContenturl());
        Log.e("image", "image==>  " + newsGalleryModel.getContenturl());
String imagepath=Constarins.BASE_URL+ newsGalleryModel.getContenturl();
        //added Piccasso for memory cache...
        Picasso.get().load(imagepath).into(myImage);
Log.i("imagepath","imagepth"+imagepath);

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}