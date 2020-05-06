package com.procialize.mrgeApp20.NewsFeed.Views.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.procialize.mrgeApp20.CustomTools.MyJZVideoPlayerStandard;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.GetterSetter.news_feed_media;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.NewsFeed.Views.Activity.ImageMultipleActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class SwipeMultimediaAdapter extends PagerAdapter {

    public ImageView myImage;
    public MyJZVideoPlayerStandard videoview;
    public TextView name;
    String MY_PREFS_NAME = "ProcializeInfo";
    ImageView imgplay, thumbimg;
    private List<String> images;
    private List<String> thumbImages;
    private LayoutInflater inflater;
    List<com.procialize.mrgeApp20.GetterSetter.news_feed_media> news_feed_media = new ArrayList<>();
    private Context context;
    private ConnectionDetector cd;

    public SwipeMultimediaAdapter(Context context, List<String> images, List<String> thumbImages, List<news_feed_media> news_feed_media) {
        this.context = context;
        this.images = images;
        this.thumbImages = thumbImages;
        this.news_feed_media = news_feed_media;
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
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.slide_multiple, view, false);
        cd = new ConnectionDetector(context);
        final String firstLevelFilter = images.get(position);
        final String thumbImage = thumbImages.get(position);

        myImage = myImageLayout.findViewById(R.id.image);
        videoview = myImageLayout.findViewById(R.id.videoview);
        name = myImageLayout.findViewById(R.id.name);
        imgplay = myImageLayout.findViewById(R.id.imgplay);

        final ProgressBar progressBar = myImageLayout.findViewById(R.id.progressbar);

        if ((firstLevelFilter.contains(".png") || firstLevelFilter.contains(".jpg") || firstLevelFilter.contains(".jpeg") || firstLevelFilter.contains(".gif"))) {
            myImage.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);
//            card_video.setVisibility(View.GONE);
            imgplay.setVisibility(View.GONE);
//            thumbimg.setVisibility(View.GONE);
            JZVideoPlayer.goOnPlayOnPause();

            if (firstLevelFilter.contains("gif")) {
                progressBar.setVisibility(View.GONE);
                Glide.with(videoview).load(firstLevelFilter).into(myImage);
            } else {
                Glide.with(context)
                        .load(firstLevelFilter)
                        .placeholder(R.drawable.gallery_placeholder)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).centerCrop()
                        .listener(new RequestListener<Drawable>() {
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
            JZVideoPlayerStandard.se
            videoview.setUp(firstLevelFilter
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");

                      /*HttpProxyCacheServer proxy = getProxy(context);
            String proxyUrl = proxy.getProxyUrl(firstLevelFilter);
            videoview.setUp(proxyUrl , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");*/

            Glide.with(videoview)
                    .load(thumbImage)
                    .placeholder(R.drawable.gallery_placeholder)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).centerCrop()
                    .listener(new RequestListener<Drawable>() {
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
                    }).into(videoview.thumbImageView);
            //Glide.with(videoview).load(firstLevelFilter).into(videoview.thumbImageView);
        }

        name.setText(firstLevelFilter);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cd.isConnectingToInternet()) {
                    if(news_feed_media.size()>0) {

                        Intent edit = new Intent(context, ImageMultipleActivity.class);
                        edit.putExtra("position", position);
                        edit.putExtra("media_list", ( Serializable ) news_feed_media);
                        context.startActivity(edit);
                    }
                }
            }
        });

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if (news_feed_media.size() > 0) {

                        Intent edit = new Intent(context, ImageMultipleActivity.class);
                        edit.putExtra("position", position);
                        edit.putExtra("media_list", ( Serializable ) news_feed_media);
                        context.startActivity(edit);
                    }
                }
            }
        });

        videoview.thumbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd.isConnectingToInternet()) {
                    if (news_feed_media.size() > 0) {

                        Intent edit = new Intent(context, ImageMultipleActivity.class);
                        edit.putExtra("position", position);
                        edit.putExtra("media_list", ( Serializable ) news_feed_media);
                        context.startActivity(edit);
                    }
                }
            }
        });

        view.addView(myImageLayout, 0);

        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}