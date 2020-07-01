package com.procialize.mrgeApp20.Gallery.Video.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.GetterSetter.FirstLevelFilter;
import com.procialize.mrgeApp20.GetterSetter.GalleryList;
import com.procialize.mrgeApp20.R;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_FOLDER_VIDEO_PATH;
import static com.procialize.mrgeApp20.Utility.Util.getYoutubeVideoIdFromUrl;

/**
 * Created by Naushad on 10/31/2017.
 */

public class SwipeVideoAdapter extends RecyclerView.Adapter<SwipeVideoAdapter.MyViewHolder> {


    private List<FirstLevelFilter> filtergallerylists;
    private Context context;
    private SwipeVideoAdapterListner listener;
    private SparseBooleanArray selectedItems;

    String MY_PREFS_NAME = "ProcializeInfo";

    TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                {
                    //No need to implement.
                }
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                {
                    //No need to implement.
                }
            }
    };


    public SwipeVideoAdapter(Context context, List<FirstLevelFilter> filtergallerylists, SwipeVideoAdapterListner listener) {

        this.filtergallerylists = filtergallerylists;
        this.listener = listener;
        this.context = context;
        selectedItems = new SparseBooleanArray(filtergallerylists.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.swappingvideo_row, parent, false);
        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FirstLevelFilter galleryList = filtergallerylists.get(position);
        //holder.nameTv.setText(galleryList.getTitle());


        if (galleryList.getFileName().contains("youtu")) {
            String id = getYoutubeVideoIdFromUrl(galleryList.getFileName());

            String url = "https://img.youtube.com/vi/" + id + "/default.jpg";

            Glide.with(holder.imageIv).load(url)
                    .placeholder(R.drawable.gallery_placeholder)
                    .apply(RequestOptions.skipMemoryCacheOf(false)).centerCrop()
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.imageIv);
        } else {

            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String picPath = prefs.getString(KEY_FOLDER_VIDEO_PATH,"");
            Glide.with(context)
                    .load(/*ApiConstant.folderimage*/picPath + galleryList.getVideo_thumb())
                    .placeholder(R.drawable.gallery_placeholder)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).centerCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.imageIv);
/*
            holder.videoView.setUp(ApiConstant.folderimage + galleryList.getFileName(), ""
                    , JzvdStd.SCREEN_NORMAL);
            Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);*/
        }
    }

    @Override
    public int getItemCount() {
        return filtergallerylists.size();
    }

    public interface SwipeVideoAdapterListner {
        void onContactSelected(FirstLevelFilter filtergallerylists);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //public TextView nameTv;
        public LinearLayout mainLL;
        public ImageView imageIv;
        public RelativeLayout myBackground;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            //nameTv = view.findViewById(R.id.nameTv);
            imageIv = view.findViewById(R.id.imageIv);
            mainLL = view.findViewById(R.id.mainLL);
            progressBar = view.findViewById(R.id.progressBar);
            myBackground = view.findViewById(R.id.myBackground);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(filtergallerylists.get(getAdapterPosition()));
                }
            });
        }
    }
}