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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.frontier.GetterSetter.TravelList;
import com.procialize.frontier.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Naushad on 10/31/2017.
 */

public class MyTravelAdapter extends RecyclerView.Adapter<MyTravelAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<TravelList> travelLists;
    private Context context;
    private MyTravelAdapterListner listener;

    public MyTravelAdapter(Context context, List<TravelList> travelList, MyTravelAdapterListner listener) {
        this.travelLists = travelList;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_detail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TravelList travel = travelLists.get(position);

        holder.linTicket.setBackgroundColor(Color.parseColor(colorActive));
        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.ic_rightarrow.setImageDrawable(drawable);

        holder.nameTv.setText(travel.getTitle());

        if (travel.getType().equalsIgnoreCase("video")) {
            holder.imgTvel.setBackgroundResource(R.drawable.travel_video);
        } else {
            holder.imgTvel.setBackgroundResource(R.drawable.tickets);

        }
    }

    @Override
    public int getItemCount() {
        return travelLists.size();
    }

    public interface MyTravelAdapterListner {
        void onContactSelected(TravelList travel);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        ImageView ic_rightarrow, imgTvel;
        LinearLayout linTicket;


        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            ic_rightarrow = view.findViewById(R.id.ic_rightarrow);
            linTicket = view.findViewById(R.id.linTicket);

            imgTvel = view.findViewById(R.id.imgTvel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(travelLists.get(getAdapterPosition()));
                }
            });
        }
    }
}