package com.procialize.mrgeApp20.Engagement.Adapter;

import android.content.Context;
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
import com.procialize.mrgeApp20.GetterSetter.SelfieList;
import com.procialize.mrgeApp20.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

/**
 * Created by Naushad on 10/31/2017.
 */

public class SwipeImageSelfieAdapter extends RecyclerView.Adapter<SwipeImageSelfieAdapter.MyViewHolder> {


    private List<SelfieList> filtergallerylists;
    private Context context;
    private SwipeImageSelfieAdapterListner listener;


    public SwipeImageSelfieAdapter(Context context, List<SelfieList> filtergallerylists, SwipeImageSelfieAdapterListner listener) {

        this.filtergallerylists = filtergallerylists;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.swappingselfie_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SelfieList galleryList = filtergallerylists.get(position);
        try {
            holder.nameTv.setText(StringEscapeUtils.unescapeJava(galleryList.getTitle()));

        }catch (IllegalArgumentException e){
            e.printStackTrace();

        }

        Glide.with(context).load(ApiConstant.selfieimage + galleryList.getFileName())
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).listener(new RequestListener<Drawable>() {
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
        }).into(holder.imageIv).onLoadStarted(context.getDrawable(R.drawable.gallery_placeholder));

    }

    @Override
    public int getItemCount() {
        return filtergallerylists.size();
    }

    public interface SwipeImageSelfieAdapterListner {
        void onContactSelected(SelfieList filtergallerylists);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public LinearLayout mainLL;
        public ImageView imageIv;
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
                    listener.onContactSelected(filtergallerylists.get(getAdapterPosition()));
                }
            });
        }
    }
}