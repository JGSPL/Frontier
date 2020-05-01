package com.procialize.mrgeApp20.Adapter;

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
import com.procialize.mrgeApp20.CustomTools.MyJZVideoPlayerStandard;
import com.procialize.mrgeApp20.CustomTools.ScaledImageView;
import com.procialize.mrgeApp20.R;

import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class SwipemultiPagerAdapter extends PagerAdapter {

    String MY_PREFS_NAME = "ProcializeInfo";
    private List<String> images;
    private LayoutInflater inflater;
    private Context context;
    ScaledImageView myImage;
    public ImageView imgplay, thumbimg;
    public MyJZVideoPlayerStandard videoview;
    public TextView name;
    List<com.procialize.mrgeApp20.GetterSetter.news_feed_media> news_feed_media;

    public SwipemultiPagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);

    }

    public SwipemultiPagerAdapter(Context context, List<String> images,    List<com.procialize.mrgeApp20.GetterSetter.news_feed_media> news_feed_media) {
        this.context = context;
        this.images = images;
        this.news_feed_media = news_feed_media;
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
        View myImageLayout = inflater.inflate(R.layout.slide_multiple, view, false);

        final String firstLevelFilter = images.get(position);


        myImage = myImageLayout.findViewById(R.id.image);
        videoview = myImageLayout.findViewById(R.id.videoview);
        name = myImageLayout.findViewById(R.id.name);
        imgplay = myImageLayout.findViewById(R.id.imgplay);


        final ProgressBar progressBar = myImageLayout.findViewById(R.id.progressbar);

        if ((firstLevelFilter.contains(".png") || firstLevelFilter.contains(".jpg") || firstLevelFilter.contains(".jpeg")  || firstLevelFilter.contains(".gif"))) {
            myImage.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);
//            card_video.setVisibility(View.GONE);
            imgplay.setVisibility(View.GONE);
//            thumbimg.setVisibility(View.GONE);
            JZVideoPlayer.goOnPlayOnPause();
            Glide.with(context).load(firstLevelFilter)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
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
        } else if (firstLevelFilter.contains(".mp4")) {
            myImage.setVisibility(View.GONE);
            videoview.setVisibility(View.VISIBLE);
//            card_video.setVisibility(View.VISIBLE);
            imgplay.setVisibility(View.GONE);
//            thumbimg.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            videoview.setUp(firstLevelFilter
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");


            Glide.with(videoview).load(firstLevelFilter).into(videoview.thumbImageView);
//            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//
//                    mp.setLooping(true);
//                    videoview.start();
//                }
//            });
////            Glide.with(videoview.getContext()).load(ApiConstant.newsfeedwall + firstLevelFilter).into(thumbimg);
//            imgplay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    videoview.setVisibility(View.VISIBLE);
////                    card_video.setVisibility(View.VISIBLE);
////                    thumbimg.setVisibility(View.GONE);
//
//                }
//            });

        }


        name.setText(firstLevelFilter);

//        myImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(cd.isConnectingToInternet()) {
//                    if(news_feed_media.size()>0) {
//
//                        Intent edit = new Intent(context, ImageMultipleActivity.class);
//                        edit.putExtra("position", position);
//                        edit.putExtra("media_list", (Serializable) news_feed_media);
//                        context.startActivity(edit);
//                    }
//                }
//            }
//        });

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}
