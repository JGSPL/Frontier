package com.procialize.mrgeApp20.NewsFeed.Views.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import com.procialize.mrgeApp20.CustomTools.MyJzvdStd;
import com.procialize.mrgeApp20.R;

import java.util.LinkedHashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import cn.jzvd.JzvdStd;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class SwipeMultimediaDetailsAdapter extends PagerAdapter {

    public PhotoView myImage;
    public ImageView imgplay, thumbimg;
    public MyJzvdStd videoview;
    public TextView name;
    String MY_PREFS_NAME = "ProcializeInfo";
    private List<String> images;
    private List<String> imagesThumb;
    private LayoutInflater inflater;
    private Context context;


    public SwipeMultimediaDetailsAdapter(Context context, List<String> images, List<String> imagesThumb) {
        this.context = context;
        this.images = images;
        this.imagesThumb = imagesThumb;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(( View ) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide_multiple_details, view, false);

        final String firstLevelFilter = images.get(position);
        final String thumbImage = imagesThumb.get(position);


        myImage = myImageLayout.findViewById(R.id.image);
        videoview = myImageLayout.findViewById(R.id.videoview);
        name = myImageLayout.findViewById(R.id.name);
        imgplay = myImageLayout.findViewById(R.id.imgplay);
        final ProgressBar progressBar = myImageLayout.findViewById(R.id.progressbar);

        if ((firstLevelFilter.contains(".png") || firstLevelFilter.contains(".jpg") || firstLevelFilter.contains(".jpeg") || firstLevelFilter.contains("gif"))) {
            myImage.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);
//            card_video.setVisibility(View.GONE);
            imgplay.setVisibility(View.GONE);
//            thumbimg.setVisibility(View.GONE);
            JzvdStd.goOnPlayOnPause();
            if (firstLevelFilter.contains("gif")) {
                progressBar.setVisibility(View.GONE);
                Glide.with(videoview).load(firstLevelFilter).into(myImage);
            } else {
                Glide.with(context).load(firstLevelFilter)
                        .placeholder(R.drawable.placeholder)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(myImage);
            }
        } else if (firstLevelFilter.contains(".mp4")) {
            myImage.setVisibility(View.GONE);
            videoview.setVisibility(View.VISIBLE);
//            card_video.setVisibility(View.VISIBLE);
            imgplay.setVisibility(View.GONE);
//            thumbimg.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            LinkedHashMap map = new LinkedHashMap();

          /*  videoview.setUp(firstLevelFilter
                    , JzvdStd.SCREEN_NORMAL, "");*/
            videoview.setUp(firstLevelFilter,""
                    , JzvdStd.SCREEN_NORMAL);

            MyJzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);

            Glide.with(videoview).load(thumbImage).into(videoview.thumbImageView);
         }


        name.setText(firstLevelFilter);

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}
