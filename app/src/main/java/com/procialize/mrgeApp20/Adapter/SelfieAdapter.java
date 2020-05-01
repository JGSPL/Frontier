package com.procialize.mrgeApp20.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Naushad on 10/31/2017.
 */

public class SelfieAdapter extends RecyclerView.Adapter<SelfieAdapter.MyViewHolder> {


    public List<SelfieList> selfieList;
    String colorActive;
    String MY_PREFS_NAME = "ProcializeInfo";
    private Context context;
    private SelfieAdapterListner listener;


    public SelfieAdapter(Context context, List<SelfieList> selfieList, SelfieAdapterListner listener) {

        this.selfieList = selfieList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selfierow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SelfieList galleryList = selfieList.get(position);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

        holder.dataTv.setText(StringEscapeUtils.unescapeJava(galleryList.getTitle()));
//        holder.dataTv.setTextColor(Color.parseColor(colorActive));
        holder.countTv.setText(galleryList.getTotalLikes());
//        holder.countTv.setTextColor(Color.parseColor(colorActive));


        holder.progressBar.setVisibility(View.GONE);

//        PicassoTrustAll.getInstance(context)
//                .load(ApiConstant.selfieimage + galleryList.getFileName())
//                .fit()
//                .placeholder(R.drawable.gallery_placeholder)
//                .into(holder.imageIv);
        Glide.with(context).load(ApiConstant.selfieimage + galleryList.getFileName())
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).listener(new RequestListener<Drawable>() {
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

        holder.moreIV.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);

        if (galleryList.getLikeFlag().equals("1")) {


            holder.likeIv.setImageResource(R.drawable.ic_afterlike);
            holder.likeIv.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.likeIv.setImageResource(R.drawable.ic_like);
            holder.likeIv.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

        }

    }

    @Override
    public int getItemCount() {
        return selfieList.size();
    }

    public interface SelfieAdapterListner {
        void onContactSelected(SelfieList selfieList, ImageView imageView);


        void onLikeListener(View v, SelfieList selfieList, int position, TextView count, ImageView likeIv);

        void onMoreListner(View v, SelfieList selfieList, int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dataTv, countTv;
        public LinearLayout mainLL;
        public ImageView imageIv, likeIv, moreIV;
        private ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            dataTv = view.findViewById(R.id.dataTv);
            countTv = view.findViewById(R.id.countTv);
            imageIv = view.findViewById(R.id.imageIv);
            likeIv = view.findViewById(R.id.likeIv);
            moreIV = view.findViewById(R.id.moreIV);
            mainLL = view.findViewById(R.id.mainLL);

            progressBar = view.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(selfieList.get(getAdapterPosition()), imageIv);
                }
            });

            likeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLikeListener(v, selfieList.get(getAdapterPosition()), getAdapterPosition(), countTv, likeIv);
                }
            });

            moreIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMoreListner(v, selfieList.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}