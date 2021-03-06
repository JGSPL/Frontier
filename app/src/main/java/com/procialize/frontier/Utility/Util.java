package com.procialize.frontier.Utility;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.frontier.Activity.ProfileActivity;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.MergeMain.MrgeHomeActivity;
import com.procialize.frontier.R;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.frontier.MergeMain.MrgeHomeActivity.ll_notification_count_drawer;
import static com.procialize.frontier.MergeMain.MrgeHomeActivity.notificationCountFilter;
import static com.procialize.frontier.MergeMain.MrgeHomeActivity.notificationCountReciever;
import static com.procialize.frontier.MergeMain.MrgeHomeActivity.tv_notification_drawer;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_EVENT_LIST_LOGO_PATH;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_EVENT_PROFILE_PATH;


public class Util {


    public static void setTextViewDrawableColor(TextView textView, String color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    public static void logomethod(final Context context, final ImageView headerlogoIv) {
        String logoHeader = MrgeHomeActivity.logoImg;
        if ((logoHeader.equalsIgnoreCase("")) || logoHeader == null) {
            headerlogoIv.setImageResource(R.drawable.frontierlogo);

        } else {
            String MY_PREFS_NAME = "ProcializeInfo";
            SharedPreferences prefs1 = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String profilePic = prefs1.getString(KEY_EVENT_PROFILE_PATH, "");
            String eventLogo = prefs1.getString(KEY_EVENT_LIST_LOGO_PATH, "");
/*
            Glide.with(context).load(*/
            /*ApiConstant.imgURL + "uploads/app_logo/"*//*
eventLogo + MrgeHomeActivity.logoImg).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    headerlogoIv.setImageResource(R.drawable.header_logo);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(headerlogoIv);
*/

            Glide.with(context).load(
                    R.drawable.frontierlogo)
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            headerlogoIv.setImageResource(R.drawable.frontierlogo);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(headerlogoIv);

        }
    }

    public static void logomethodwithText(final Context context, boolean isTextHeader, String hearderText, TextView tv_header, ImageView header_logo) {
        if (isTextHeader) {
            tv_header.setVisibility(View.VISIBLE);
            header_logo.setVisibility(View.GONE);
            tv_header.setText(hearderText);
        } else {
            tv_header.setVisibility(View.GONE);
            header_logo.setVisibility(View.VISIBLE);
            String MY_PREFS_NAME = "ProcializeInfo";
            SharedPreferences prefs1 = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

            String profilePic = prefs1.getString(KEY_EVENT_PROFILE_PATH, "");
            String eventLogo = prefs1.getString(KEY_EVENT_LIST_LOGO_PATH, "");
            /*Glide.with(context).load(*//*ApiConstant.imgURL + "uploads/app_logo/"*//*eventLogo + MrgeHomeActivity.logoImg).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    headerlogoIv.setImageResource(R.drawable.header_logo);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(header_logo);*/
            Glide.with(context).load(
                    /*ApiConstant.imgURL + "uploads/app_logo/"*/
//                    eventLogo + MrgeHomeActivity.logoImg
                    R.drawable.frontierlogo)
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            header_logo.setImageResource(R.drawable.frontierlogo);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(header_logo);
        }
    }

    public static void setNotification(Context context, TextView tv_notification, LinearLayout ll_notification_count) {
        try {
            notificationCountReciever = new MrgeHomeActivity.NotificationCountReciever();
            notificationCountFilter = new IntentFilter(ApiConstant.BROADCAST_ACTION_FOR_NOTIFICATION_COUNT);
            LocalBroadcastManager.getInstance(context).registerReceiver(notificationCountReciever, notificationCountFilter);


            SharedPreferences prefs1 = context.getSharedPreferences("ProcializeInfo", MODE_PRIVATE);
            String notificationCount = prefs1.getString("notificationCount", "");
            tv_notification.setText(notificationCount);
            tv_notification_drawer.setText(notificationCount);

            if (notificationCount.equalsIgnoreCase("0")) {
                tv_notification.setVisibility(View.GONE);
                ll_notification_count.setVisibility(View.GONE);

                tv_notification_drawer.setVisibility(View.GONE);
                ll_notification_count_drawer.setVisibility(View.GONE);
            } else {
                tv_notification.setVisibility(View.VISIBLE);
                ll_notification_count.setVisibility(View.VISIBLE);

                tv_notification_drawer.setVisibility(View.VISIBLE);
                ll_notification_count_drawer.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logomethodwithText_bk(final Context context, TextView txtHeader, String HeaderText) {
        //MrgeHomeActivity.headerlogoIv.setVisibility(View.GONE);
        //MrgeHomeActivity.txtMainHeader.setVisibility(View.VISIBLE);
        String logoHeader = MrgeHomeActivity.logoImg;
        txtHeader.setText(HeaderText);
    }


    public static void logomethod1(final Context context, final ImageView headerlogoIv) {
        String logoHeader = ProfileActivity.logoImg;


        if ((logoHeader.equalsIgnoreCase("")) || logoHeader == null) {
            headerlogoIv.setImageResource(R.drawable.frontierlogo);

        } else {


            Glide.with(context).load(R.drawable.frontierlogo).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    headerlogoIv.setImageResource(R.drawable.frontierlogo);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(headerlogoIv);
        }
    }

/*
    public static void logomethod(final Context context, final ImageView headerlogoIv)
    {
        Glide.with(context).load("http://www.procialize.info/uploads/app_logo/logo-testing.png").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                headerlogoIv.setImageResource(R.drawable.splashlogo);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(headerlogoIv);
    }
*/


    public static String getYoutubeVideoIdFromUrl(String inUrl) {
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return inUrl.substring(inUrl.lastIndexOf("/") + 1);
        }
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(inUrl);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


}
