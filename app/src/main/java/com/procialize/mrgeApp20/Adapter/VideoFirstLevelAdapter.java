package com.procialize.mrgeApp20.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.GetterSetter.FirstLevelFilter;
import com.procialize.mrgeApp20.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Utility.Util.getYoutubeVideoIdFromUrl;

/**
 * Created by Naushad on 10/31/2017.
 */

public class VideoFirstLevelAdapter extends RecyclerView.Adapter<VideoFirstLevelAdapter.MyViewHolder> {

    String videoId;
    String colorActive;
    String MY_PREFS_NAME = "ProcializeInfo";
    private List<FirstLevelFilter> videoLists;
    private Context context;
    private VideoFirstLevelAdapterListner listener;

    public VideoFirstLevelAdapter(Context context, List<FirstLevelFilter> galleryLists, VideoFirstLevelAdapterListner listener) {
        this.videoLists = galleryLists;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_gallery_first_level, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FirstLevelFilter videoList = videoLists.get(position);

        holder.nameTv.setText(videoList.getTitle());
        holder.nameTv.setTextColor(Color.parseColor(colorActive));
        if (videoList.getFileName().contains("youtu")) {

            String CurrentString = videoList.getFileName();
//                String[] separated = CurrentString.split("/");
//
//                String id = separated[1];
            String id = getYoutubeVideoIdFromUrl(videoList.getFileName());

            String url = "https://img.youtube.com/vi/" + id + "/default.jpg";


            Glide.with(context).load(url)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
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


            Glide.with(context).load(ApiConstant.folderimage + videoList.getVideo_thumb())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
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

//            Bitmap bitmap;
//            try {
//                bitmap = retriveVideoFrameFromVideo(ApiConstant.folderimage + videoList.getFileName());
//                if (bitmap != null) {
//                    bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, false);
//                    holder.imageIv.setImageBitmap(bitmap);
//                    holder.progressBar.setVisibility(View.GONE);
//                }
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }


        }


    }

    @Override
    public int getItemCount() {
        return videoLists.size();
    }


    public interface VideoFirstLevelAdapterListner {
        void onContactSelected(FirstLevelFilter firstLevelFilter);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public ImageView imageIv;
        public LinearLayout mainLL;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            imageIv = view.findViewById(R.id.imageIv);
            mainLL = view.findViewById(R.id.mainLL);
            progressBar = view.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(videoLists.get(getAdapterPosition()));
                }
            });
        }
    }


}