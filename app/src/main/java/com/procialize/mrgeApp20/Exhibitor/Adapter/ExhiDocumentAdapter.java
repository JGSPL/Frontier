package com.procialize.mrgeApp20.Exhibitor.Adapter;

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

import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorBrochureList;
import com.procialize.mrgeApp20.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ExhiDocumentAdapter extends RecyclerView.Adapter<ExhiDocumentAdapter.MyViewHolder> {

    List<EventSettingList> eventSettingLists;
    String attendee_design, attendee_company, attendee_location, attendee_mobile, attendee_save_contact;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<ExhibitorBrochureList> attendeeLists;
    private Context context;
    private List<ExhibitorBrochureList> attendeeListFiltered;
    private MyTravelAdapterListner listener;


    public ExhiDocumentAdapter(Context context, List<ExhibitorBrochureList> attendeeLists, MyTravelAdapterListner listener) {
        this.attendeeLists = attendeeLists;
        this.attendeeListFiltered = attendeeLists;
        this.context = context;
        this.listener = listener;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exhi_document, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ExhibitorBrochureList travel = attendeeListFiltered.get(position);

        holder.linTicket.setBackgroundColor(Color.parseColor(colorActive));
        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.ic_rightarrow.setImageDrawable(drawable);

        holder.nameTv.setText(StringEscapeUtils.unescapeJava(travel.getBrochure_title()));

        if (travel.getFile_type().equalsIgnoreCase("video")) {
            holder.imgTvel.setBackgroundResource(R.drawable.video_ic);
        } else if (travel.getFile_type().equalsIgnoreCase("image")) {
            holder.imgTvel.setBackgroundResource(R.drawable.image_icon);
        } else {
            holder.imgTvel.setBackgroundResource(R.drawable.pdf_icon);

        }


    }

    @Override
    public int getItemCount() {
        return attendeeListFiltered.size();
    }

    public interface MyTravelAdapterListner {
        void onContactSelected(ExhibitorBrochureList travel);
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
                    listener.onContactSelected(attendeeListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
}