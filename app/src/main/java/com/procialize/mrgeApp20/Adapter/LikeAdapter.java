package com.procialize.mrgeApp20.Adapter;

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
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sneha on 10/31/2017.
 */

public class LikeAdapter  extends RecyclerView.Adapter<LikeAdapter.MyViewHolder> {

    private List<AttendeeList> attendeeListList;
    private Context context;
    //    private LikeAdapterListner listener;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, dateTv;
        public ImageView imageIv, iv_reaction;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            imageIv = view.findViewById(R.id.imageIv);
            iv_reaction = view.findViewById(R.id.iv_reaction);
            dateTv = view.findViewById(R.id.dateTv);


        }
    }


    public LikeAdapter(Context context, List<AttendeeList> attendeeListList) {
        this.attendeeListList = attendeeListList;
//        this.listener=listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.likelist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        AttendeeList attendeeList = attendeeListList.get(position);

        if (attendeeListList.get(position).getProfilePic() != null) {
            Glide.with(context).load(ApiConstant.profilepic + attendeeListList.get(position).getProfilePic()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.imageIv.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(holder.imageIv);
        } else {
            holder.imageIv.setImageResource(R.drawable.profilepic_placeholder);
        }

       // holder.nameTv.setTextColor(Color.parseColor(colorActive));
        holder.nameTv.setText(attendeeList.getFirstName() + " " + attendeeList.getLastName());

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM HH:mm");
        try {
            Date startdate = originalFormat.parse(attendeeList.getCreated());
            String startdatestr = targetFormat.format(startdate);
            //holder.dateTv.setText(startdatestr);
            holder.dateTv.setText(attendeeList.getReaction());

            if (attendeeList.getReaction().equals("0"))
                holder.iv_reaction.setImageDrawable(context.getResources().getDrawable(R.drawable.like_0));
            else if (attendeeList.getReaction().equals("1"))
                holder.iv_reaction.setImageDrawable(context.getResources().getDrawable(R.drawable.love_1));
            else if (attendeeList.getReaction().equals("2"))
                holder.iv_reaction.setImageDrawable(context.getResources().getDrawable(R.drawable.smile_2));
            else if (attendeeList.getReaction().equals("3"))
                holder.iv_reaction.setImageDrawable(context.getResources().getDrawable(R.drawable.haha_3));
            else if (attendeeList.getReaction().equals("4"))
                holder.iv_reaction.setImageDrawable(context.getResources().getDrawable(R.drawable.wow_4));
            else if (attendeeList.getReaction().equals("5"))
                holder.iv_reaction.setImageDrawable(context.getResources().getDrawable(R.drawable.sad_5));
            else if (attendeeList.getReaction().equals("6"))
                holder.iv_reaction.setImageDrawable(context.getResources().getDrawable(R.drawable.angry_6));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return attendeeListList.size();
    }


}