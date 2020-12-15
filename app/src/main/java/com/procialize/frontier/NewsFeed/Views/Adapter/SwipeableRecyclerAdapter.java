package com.procialize.frontier.NewsFeed.Views.Adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.GetterSetter.news_feed_media;
import com.procialize.frontier.NewsFeed.Views.Activity.ImageMultipleActivity;
import com.procialize.frontier.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;


public class SwipeableRecyclerAdapter extends RecyclerView.Adapter<SwipeableRecyclerAdapter.MyViewHolder> {
    public ImageView myImage;
    ProgressBar progressBar;
    public JzvdStd videoview;
    public TextView name;
    String MY_PREFS_NAME = "ProcializeInfo";
    ImageView imgplay, thumbimg;
    private List<String> images;
    private List<String> thumbImages;
    private LayoutInflater inflater;
    List<com.procialize.frontier.GetterSetter.news_feed_media> news_feed_media = new ArrayList<>();
    private Context context;
    private ConnectionDetector cd;    // View Holder class which


    // extends RecyclerView.ViewHolder
    public class  MyViewHolder extends RecyclerView.ViewHolder {

        // Text View
        TextView textView;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyViewHolder(View view)
        {
            super(view);

            cd = new ConnectionDetector(context);

            myImage = view.findViewById(R.id.image);
            videoview = view.findViewById(R.id.videoview);
            name = view.findViewById(R.id.name);
            imgplay = view.findViewById(R.id.imgplay);
            progressBar = view.findViewById(R.id.progressbar);

        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public SwipeableRecyclerAdapter(Context context, List<String> images, List<String> thumbImages, List<news_feed_media> news_feed_media) {
        this.context = context;
        this.images = images;
        this.thumbImages = thumbImages;
        this.news_feed_media = news_feed_media;
        inflater = LayoutInflater.from(context);

    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                     int viewType)
    {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slide_multiple, parent, false);

        return new MyViewHolder(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyViewHolder holder,
                                 final int position)
    {
        final String firstLevelFilter = images.get(position);
        final String thumbImage = thumbImages.get(position);

        if ((firstLevelFilter.contains(".png") || firstLevelFilter.contains(".jpg") || firstLevelFilter.contains(".jpeg") || firstLevelFilter.contains(".gif"))) {
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
          /*  videoview.setUp(firstLevelFilter
                    , JzvdStd.SCREEN_WINDOW_NORMAL, "");*/
            videoview.setUp(firstLevelFilter,""
                    , JzvdStd.SCREEN_NORMAL);
                      /*HttpProxyCacheServer proxy = getProxy(context);
            String proxyUrl = proxy.getProxyUrl(firstLevelFilter);
            videoview.setUp(proxyUrl , JzvdStd.SCREEN_WINDOW_NORMAL, "");*/

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
                        edit.putExtra("media_list", (Serializable) news_feed_media);
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
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return news_feed_media.size();
    }
}