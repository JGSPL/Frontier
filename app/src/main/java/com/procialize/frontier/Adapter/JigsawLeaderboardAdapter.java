package com.procialize.frontier.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.procialize.frontier.GetterSetter.JgsawPuzzle;
import com.procialize.frontier.R;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.Context.MODE_PRIVATE;

public class JigsawLeaderboardAdapter extends RecyclerView.Adapter<JigsawLeaderboardAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive,picpath;
    private List<JgsawPuzzle> puzzle;
    private Context context;
    private JigsawLeaderboardAdapter.JigsawLeaderboardAdapterListner listener;

    public JigsawLeaderboardAdapter(Context context, List<JgsawPuzzle> puzzle,String picpath) {
        this.puzzle = puzzle;
        this.context = context;
        this.picpath = picpath;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public JigsawLeaderboardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderbord_item, parent, false);

        return new JigsawLeaderboardAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final JigsawLeaderboardAdapter.MyViewHolder holder, int position) {
        final JgsawPuzzle jgpuzzle = puzzle.get(position);


//        ColorStateList csl = ColorStateList.valueOf(color2);
//        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
//        DrawableCompat.setTintList(drawable, csl);
//        holder.ic_rightarrow.setImageDrawable(drawable);


        holder.tvName.setText(jgpuzzle.getFirst_name()+" "+jgpuzzle.getLast_name());
        holder.tvPoints.setText(jgpuzzle.getSolved_time());
        Glide.with(context).load(picpath/*ApiConstant.selfieimage*/ + jgpuzzle.getProfile_pic())
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
        }).into(holder.ivpic);


    }

    @Override
    public int getItemCount() {
        return puzzle.size();
    }

    public interface JigsawLeaderboardAdapterListner {
        void onContactSelected(JgsawPuzzle survey,String path);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPoints,tvName;
        ImageView ivpic;

        public MyViewHolder(View view) {
            super(view);
            tvPoints = view.findViewById(R.id.tvPoints);
            tvName = view.findViewById(R.id.tvName);
            ivpic = view.findViewById(R.id.ivpic);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // send selected contact in callback
//                    listener.onContactSelected(puzzle.get(getAdapterPosition()),picpath);
//                }
//            });
        }
    }
}