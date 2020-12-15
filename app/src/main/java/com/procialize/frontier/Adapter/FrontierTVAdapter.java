package com.procialize.frontier.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.procialize.frontier.GetterSetter.FrontierTV;
import com.procialize.frontier.R;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.Context.MODE_PRIVATE;

public class FrontierTVAdapter extends RecyclerView.Adapter<FrontierTVAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive,picpath;
    private List<FrontierTV> frontiertv;
    private Context context;
    private FrontierTVAdapter.FrontierTVAdapterListner listener;

    public FrontierTVAdapter(Context context, List<FrontierTV> speakerList,String picpath, FrontierTVAdapter.FrontierTVAdapterListner listener) {
        this.frontiertv = speakerList;
        this.listener = listener;
        this.context = context;
        this.picpath = picpath;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public FrontierTVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frontierspecial_item, parent, false);

        return new FrontierTVAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FrontierTVAdapter.MyViewHolder holder, int position) {
        final FrontierTV survey = frontiertv.get(position);
        int colorInt = Color.parseColor(colorActive);
        AtomicInteger color2 = new AtomicInteger(Color.parseColor("#ffffff"));

//        ColorStateList csl = ColorStateList.valueOf(color2);
//        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
//        DrawableCompat.setTintList(drawable, csl);
//        holder.ic_rightarrow.setImageDrawable(drawable);


        holder.text_name.setText(survey.getTitle());
        Glide.with(context).load(picpath/*ApiConstant.selfieimage*/ + survey.getThumb_image())
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                return false;
            }
        }).into(holder.img_back);


    }

    @Override
    public int getItemCount() {
        return frontiertv.size();
    }

    public interface FrontierTVAdapterListner {
        void onContactSelected(FrontierTV survey,String path);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text_name;
        ImageView img_back;

        public MyViewHolder(View view) {
            super(view);
            img_back = view.findViewById(R.id.img_back);
            text_name = view.findViewById(R.id.text_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(frontiertv.get(getAdapterPosition()),picpath);
                }
            });
        }
    }
}