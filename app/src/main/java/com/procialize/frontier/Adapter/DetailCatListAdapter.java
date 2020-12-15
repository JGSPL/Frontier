package com.procialize.frontier.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.frontier.GetterSetter.EventSettingList;
import com.procialize.frontier.GetterSetter.ExhiCatDetailList;
import com.procialize.frontier.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DetailCatListAdapter extends RecyclerView.Adapter<DetailCatListAdapter.MyViewHolder> {

    List<EventSettingList> eventSettingLists;
    String attendee_design, attendee_company, attendee_location, attendee_mobile, attendee_save_contact;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<ExhiCatDetailList> attendeeLists;
    private Context context;
    private List<ExhiCatDetailList> attendeeListFiltered;
    private DetailCatListAdapter.MyTravelAdapterListner listener;


    public DetailCatListAdapter(Context context, List<ExhiCatDetailList> attendeeLists, DetailCatListAdapter.MyTravelAdapterListner listener) {
        this.attendeeLists = attendeeLists;
        this.attendeeListFiltered = attendeeLists;
        this.context = context;
        this.listener = listener;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public DetailCatListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.excatlist_item, parent, false);

        return new DetailCatListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DetailCatListAdapter.MyViewHolder holder, int position) {
        final ExhiCatDetailList travel = attendeeListFiltered.get(position);

        holder.txt_cat_name.setText(travel.getName());
        holder.txt_cat_name.setTextColor(Color.parseColor(colorActive));
        ShapeDrawable sd = new ShapeDrawable();

        // Specify the shape of ShapeDrawable
        sd.setShape(new RectShape());

        // Specify the border color of shape
        sd.getPaint().setColor(Color.parseColor(colorActive));

        // Set the border width
        sd.getPaint().setStrokeWidth(10f);

        // Specify the style is a Stroke
        sd.getPaint().setStyle(Paint.Style.STROKE);

        // Finally, add the drawable background to TextView
        holder.txt_cat_name.setBackground(sd);

    }

    @Override
    public int getItemCount() {
        return attendeeListFiltered.size();
    }

    public interface MyTravelAdapterListner {
        void onContactSelected(ExhiCatDetailList travel);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_cat_name;


        public MyViewHolder(View view) {
            super(view);
            txt_cat_name = view.findViewById(R.id.txt_cat_name);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(attendeeListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
}
