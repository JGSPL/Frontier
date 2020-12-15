package com.procialize.frontier.Adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.frontier.GetterSetter.SponsorsList;
import com.procialize.frontier.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.MyViewHolder> {

    private List<SponsorsList> sponsorsLists;
    private Context context;
    private String filePath;

    String MY_PREFS_NAME = "ProcializeInfo";
    private SponsorAdapterListner listener;

    public SponsorAdapter(Context context, List<SponsorsList> sponsorsLists,String filePath, SponsorAdapterListner listener) {
       this.sponsorsLists = sponsorsLists;
       this.filePath = filePath;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public SponsorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sponsor_row, parent, false);

        return new SponsorAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SponsorAdapter.MyViewHolder holder, int position) {
        final SponsorsList sponsorsList = sponsorsLists.get(position);

        if (sponsorsList.getLogo() != null) {


            String file = filePath + sponsorsList.getLogo();

            holder.tv_name.setText(sponsorsList.getName());
            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String colorActive = prefs.getString("colorActive", "");
            holder.tv_name.setTextColor(Color.parseColor(colorActive));
            Glide.with(context).load(file)
                    .placeholder(R.drawable.profilepic_placeholder)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progress_bar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progress_bar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.iv_sponsor_logo);


            holder.iv_sponsor_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onContactSelected(sponsorsList,context);
                }
            });
           /* Glide.with(context).load(file)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                            holder.progress_bar.setVisibility(View.GONE);
                            holder.iv_sponsor_logo.setImageResource(R.drawable.profilepic_placeholder);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progress_bar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.iv_sponsor_logo);*/

        } else {
            holder.progress_bar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return sponsorsLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_sponsor_logo;
        TextView tv_name;
        ProgressBar progress_bar;

        public MyViewHolder(View view) {
            super(view);
            iv_sponsor_logo = view.findViewById(R.id.iv_sponsor_logo);
            progress_bar = view.findViewById(R.id.progress_bar);
            tv_name = view.findViewById(R.id.tv_name);
        }
    }

    public interface SponsorAdapterListner {
        void onContactSelected(SponsorsList sponsorsList, Context context);

    }
}