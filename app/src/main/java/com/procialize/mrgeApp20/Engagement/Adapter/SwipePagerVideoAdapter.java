package com.procialize.mrgeApp20.Engagement.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.GetterSetter.FirstLevelFilter;
import com.procialize.mrgeApp20.GetterSetter.SelfieList;
import com.procialize.mrgeApp20.GetterSetter.VideoContest;
import com.procialize.mrgeApp20.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by gauravnaik309 on 01/03/18.
 */

public class SwipePagerVideoAdapter extends PagerAdapter {

    String MY_PREFS_NAME = "ProcializeInfo";
    String colorActive;
    private List<VideoContest> images;
    private LayoutInflater inflater;
    private Context context;

    public SwipePagerVideoAdapter(Context context, List<VideoContest> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

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
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide_selfie_video, view, false);

        VideoContest firstLevelFilter = images.get(position);

        JzvdStd video_view = myImageLayout.findViewById(R.id.video_view);
        TextView name = myImageLayout.findViewById(R.id.name);
        final ProgressBar progressBar = myImageLayout.findViewById(R.id.progressbar);

        try {
            Glide.with(context)
                    .load(ApiConstant.selfievideo + firstLevelFilter.getThumbName())
                    .placeholder(R.drawable.gallery_placeholder)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).centerCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(video_view.thumbImageView);

            video_view.setUp(ApiConstant.selfievideo + firstLevelFilter.getFileName(), ""
                    , JzvdStd.SCREEN_NORMAL);
            Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);
        }catch (Exception e)
        {e.printStackTrace();}
        try{
        name.setText(StringEscapeUtils.unescapeJava(firstLevelFilter.getTitle()));
        }catch (IllegalArgumentException e){
            e.printStackTrace();

        }
        name.setTextColor(Color.parseColor(colorActive));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}