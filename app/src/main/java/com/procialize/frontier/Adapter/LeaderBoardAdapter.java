package com.procialize.frontier.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.GetterSetter.LeaderBoard;
import com.procialize.frontier.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rahul on 23-10-2018.
 */

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder> {

    private List<LeaderBoard> leadLists;
    private Context context;
    String colorActive;
    String MY_PREFS_NAME = "ProcializeInfo";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPoints, tvName;
        public ImageView ivpic, star_icon;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvPoints = (TextView) view.findViewById(R.id.tvPoints);
            ivpic = (ImageView) view.findViewById(R.id.ivpic);
            star_icon = (ImageView) view.findViewById(R.id.star_icon);

        }
    }


    public LeaderBoardAdapter(Context context, List<LeaderBoard> docList) {
        this.leadLists = docList;
        this.context = context;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final LeaderBoard leaderBoard = leadLists.get(position);
        holder.tvName.setText(leaderBoard.getFirst_name() + " " + leaderBoard.getLast_name());
        holder.tvPoints.setText(leaderBoard.getPoint());

        holder.tvName.setTextColor(Color.parseColor(colorActive));
        int colorInt = Color.parseColor(colorActive);
        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.star_icon.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.star_icon.setImageDrawable(drawable);

        if (leaderBoard.getProfile_pic() != null) {
            Glide.with(context).load(ApiConstant.Leaderboard_IMAGE + leaderBoard.getProfile_pic()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.ivpic.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(holder.ivpic);
        } else {
            holder.ivpic.setImageResource(R.drawable.profilepic_placeholder);
        }

    }

    @Override
    public int getItemCount() {
        return leadLists.size();
    }


}
