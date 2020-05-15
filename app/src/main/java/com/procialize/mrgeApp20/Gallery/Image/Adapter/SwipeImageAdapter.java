package com.procialize.mrgeApp20.Gallery.Image.Adapter;

import android.content.Context;
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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.GetterSetter.FirstLevelFilter;
import com.procialize.mrgeApp20.R;

import java.util.List;

/**
 * Created by Naushad on 10/31/2017.
 */

public class SwipeImageAdapter extends RecyclerView.Adapter<SwipeImageAdapter.MyViewHolder> {


    private List<FirstLevelFilter> filtergallerylists;
    private Context context;
    private SwipeImageAdapterListner listener;
    private SparseBooleanArray selectedItems;


    public SwipeImageAdapter(Context context, List<FirstLevelFilter> filtergallerylists, SwipeImageAdapterListner listener) {

        this.filtergallerylists = filtergallerylists;
        this.listener = listener;
        this.context = context;
        selectedItems = new SparseBooleanArray(filtergallerylists.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.swappinggallery_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FirstLevelFilter galleryList = filtergallerylists.get(position);
        holder.nameTv.setText(galleryList.getTitle());
        Glide.with(context).load(galleryList.getFileName())
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
        }).into(holder.imageIv).onLoadStarted(context.getDrawable(R.drawable.gallery_placeholder));

    }

    @Override
    public int getItemCount() {
        return filtergallerylists.size();
    }

    public interface SwipeImageAdapterListner {
        void onContactSelected(FirstLevelFilter filtergallerylists);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public LinearLayout mainLL;
        public ImageView imageIv;
        public RelativeLayout myBackground;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
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