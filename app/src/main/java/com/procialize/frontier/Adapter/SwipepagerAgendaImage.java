package com.procialize.frontier.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.CustomTools.PicassoTrustAll;
import com.procialize.frontier.GetterSetter.AgendaMediaList;
import com.procialize.frontier.R;

import java.util.List;

import cn.jzvd.JzvdStd;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_AGENDA_MEDIA_PIC_PATH;


/**
 * Created by Rahul on 13-06-2018.
 */

public class SwipepagerAgendaImage extends PagerAdapter {

    public static JzvdStd videoplayer;
    ApiConstant constant;
    String image_url, thumb_image_url;
    String deviceMan;
    private List<AgendaMediaList> images;
    private LayoutInflater inflater;
    private Context context;
    // public static EMVideoView videoplayer;
    // ProgressDialog pDialog;
    String MY_PREFS_NAME = "ProcializeInfo",picPath="";

    public SwipepagerAgendaImage(Context context, List<AgendaMediaList> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);

        deviceMan = Build.MANUFACTURER;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        picPath = prefs.getString(KEY_AGENDA_MEDIA_PIC_PATH,"");

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
        View myImageLayout = inflater.inflate(R.layout.slide_agenda, view, false);
        constant = new ApiConstant();


        AgendaMediaList firstLevelFilter = images.get(position);

        ImageView myImage = myImageLayout.findViewById(R.id.image1);


        videoplayer = myImageLayout.findViewById(R.id.videoplayerAgenda);
        //videoplayer = (EMVideoView)myImageLayout. findViewById(R.id.videoplayerAgenda);
        WebView videoWebView = myImageLayout.findViewById(R.id.videoWebView);

        //TextView name = (TextView) myImageLayout.findViewById(R.id.name);
        final ProgressBar progressBar = myImageLayout.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);


        image_url = picPath/*ApiConstant.GALLERY_IMAGE*/
                + firstLevelFilter.getMedia_name();
        thumb_image_url =picPath/* ApiConstant.GALLERY_IMAGE*/
                + firstLevelFilter.getMedia_thumbnail();

        String imageType = firstLevelFilter.getMedia_type();


        if (imageType.equalsIgnoreCase("Video") || imageType.contains(".mp4") || imageType.contains(".MOV") || imageType.contains(".mov")) {
            myImage.setVisibility(View.GONE);


            if (deviceMan.equalsIgnoreCase("Vivo")
                    || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {


                videoplayer.setVisibility(View.GONE);
                videoWebView.setVisibility(View.VISIBLE);

                videoWebView.setWebViewClient(new WebViewClient());
                videoWebView.getSettings().setJavaScriptEnabled(true);
                videoWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                videoWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    videoWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
                }
                videoWebView.setWebChromeClient(new WebChromeClient());
                videoWebView.loadUrl(image_url);
//                PicassoTrustAll.getInstance(context)
//                        .load(image_url)
//                        .into(videoplayer.thumbImageView);
                Glide.with(context).load(image_url).into(videoplayer.thumbImageView);
            }
            videoplayer.setVisibility(View.VISIBLE);
            videoWebView.setVisibility(View.GONE);

     /*       videoplayer.setUp(image_url
                    , JzvdStd.SCREEN_WINDOW_LIST, "");*/

            videoplayer.setUp(image_url,""
                    , JzvdStd.SCREEN_NORMAL);
            Glide.with(context).load(image_url).into(videoplayer.thumbImageView);
//            PicassoTrustAll.getInstance(context)
//                    .load(image_url)
//                    .into(videoplayer.thumbImageView);
            /*AgendaFolderFragment.mViewPager.stopAutoScroll();*/
            //setupVideoView(image_url);
            //videoplayer.setPreviewImage();

        } else {

            /*try {

                if (videoplayer != null) {
                    if (videoplayer.isCurrentPlay()) {
                        videoplayer.onStatePause();
                    }

                }
                videoplayer.release();

            } catch (Exception e) {
                e.printStackTrace();
            }*/

            myImage.setVisibility(View.VISIBLE);
            videoplayer.setVisibility(View.GONE);
            videoWebView.setVisibility(View.GONE);
            PicassoTrustAll.getInstance(context)
                    .load(image_url)
                    .placeholder(R.drawable.gallery_placeholder)
                    .into(myImage);
            /*Glide.with(context).load(image_url)
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
            }).into(myImage).onLoadStarted(context.getDrawable(R.drawable.gallery_placeholder));*/

           /* AgendaFolderFragment.mViewPager.startAutoScroll();
            AgendaFolderFragment.mViewPager.setInterval(5000);
            AgendaFolderFragment.mViewPager.setStopScrollWhenTouch(true);
            AgendaFolderFragment.mViewPager.setCycle(true);*/

        }




       /* Glide.with(context).load(image_url)
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
        }).into(myImage).onLoadStarted(context.getDrawable(R.drawable.gallery_placeholder));*/

        // name.setText(StringEscapeUtils.unescapeJava(firstLevelFilter.getName()));

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }





/*
    private void setupVideoView(String videoUrl) {
        videoplayer.setVisibility(View.VISIBLE);
        AgendaFolderFragment.mViewPager.stopAutoScroll();

        videoplayer.setOnPreparedListener(this);

        //For now we just picked an arbitrary item to play.  More can be found at
        //https://archive.org/details/more_animation
        videoplayer.setVideoURI(Uri.parse(videoUrl));

        //pDialog = new ProgressDialog(context);
        // Set progressbar title
        // Set progressbar message
        //pDialog.setMessage("Buffering...");
        // Show progressbar
       // pDialog.show();


        videoplayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion() {
                videoplayer.release();
               // videoplayer.reset();
               // videoplayer.restart();
                */
/*AgendaFolderFragment.mViewPager.startAutoScroll();
                AgendaFolderFragment.mViewPager.setInterval(5000);
                AgendaFolderFragment.mViewPager.setStopScrollWhenTouch(true);
                AgendaFolderFragment.mViewPager.setCycle(true);*//*

            }
        });

        videoplayer.setOnBufferUpdateListener(new OnBufferUpdateListener() {
            @Override
            public void onBufferingUpdate(int percent) {
                AgendaFolderFragment.mViewPager.stopAutoScroll();

            }
        });

        videoplayer.getVideoControls().setFitsSystemWindows(true);


    }
*/

}
