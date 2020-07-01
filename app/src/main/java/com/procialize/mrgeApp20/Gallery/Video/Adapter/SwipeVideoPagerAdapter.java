package com.procialize.mrgeApp20.Gallery.Video.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.GetterSetter.FirstLevelFilter;
import com.procialize.mrgeApp20.R;

import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_DOCUMENTS_PIC_PATH;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_FOLDER_VIDEO_PATH;
import static com.procialize.mrgeApp20.Utility.Util.getYoutubeVideoIdFromUrl;

/**
 * Created by gauravnaik309 on 01/03/18.
 */

public class SwipeVideoPagerAdapter extends PagerAdapter {

    String MY_PREFS_NAME = "ProcializeInfo";
    String colorActive, img;
    onItemClick listener;
    private List<FirstLevelFilter> images;
    private LayoutInflater inflater;
    private Context context;
    private ConnectionDetector cd;

    public SwipeVideoPagerAdapter(Context context, List<FirstLevelFilter> images, onItemClick listener) {
        this.context = context;
        this.listener = listener;
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
        View myImageLayout = inflater.inflate(R.layout.slide_video, view, false);
        cd = new ConnectionDetector(context);
        final FirstLevelFilter firstLevelFilter = images.get(position);

        try {
            img = firstLevelFilter.getFileName().substring(58, 60);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JzvdStd video_view = myImageLayout.findViewById(R.id.video_view);
        TextView name = myImageLayout.findViewById(R.id.name);

        RelativeLayout rl_image = myImageLayout.findViewById(R.id.rl_image);
        ImageView iv_thumb = myImageLayout.findViewById(R.id.iv_thumb);
        ImageView iv_video_play = myImageLayout.findViewById(R.id.iv_video_play);

        iv_video_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickListener(position);
            }
        });

        if (firstLevelFilter.getFileName().contains("youtu")) {
            video_view.setVisibility(View.GONE);
            rl_image.setVisibility(View.VISIBLE);
            String id = getYoutubeVideoIdFromUrl(firstLevelFilter.getFileName());

            String url = "https://img.youtube.com/vi/" + id + "/default.jpg";

            Glide.with(context).load(url)
                    .placeholder(R.drawable.gallery_placeholder)
                    .apply(RequestOptions.skipMemoryCacheOf(false)).centerCrop()
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(iv_thumb);
        } else {
            video_view.setVisibility(View.VISIBLE);
            rl_image.setVisibility(View.GONE);

            name.setTextColor(Color.parseColor(colorActive));
            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String picPath = prefs.getString(KEY_FOLDER_VIDEO_PATH,"");

            Glide.with(context)
                    .load(/*ApiConstant.folderimage*/picPath + firstLevelFilter.getVideo_thumb())
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


            video_view.setUp(/*ApiConstant.folderimage*/picPath + firstLevelFilter.getFileName(), ""
                    , JzvdStd.SCREEN_NORMAL);
            Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);
            name.setText(firstLevelFilter.getTitle());
        }
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public interface onItemClick {
        public void onClickListener(int pos);
    }


}