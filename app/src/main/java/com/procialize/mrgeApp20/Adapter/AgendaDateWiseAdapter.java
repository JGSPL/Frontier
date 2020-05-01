package com.procialize.mrgeApp20.Adapter;

import android.content.Context;
import android.content.Intent;
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

import com.procialize.mrgeApp20.Activity.AgendaDetailActivity;
import com.procialize.mrgeApp20.GetterSetter.AgendaList;
import com.procialize.mrgeApp20.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class AgendaDateWiseAdapter extends RecyclerView.Adapter<AgendaDateWiseAdapter.MyViewHolder> {

    String date = "";
    //    private AgendaAdapter.AgendaAdapterListner listener;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<AgendaList> agendaLists;
    private Context context;
    private AgendaAdapterListner listener;


    public AgendaDateWiseAdapter(Context context, List<AgendaList> agendaLists, AgendaAdapterListner listener) {
        this.agendaLists = agendaLists;
        this.context = context;
        this.listener = listener;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agenda_date_wise, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return agendaLists.size();
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AgendaList agenda = agendaLists.get(position);

        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.ic_rightarrow.setImageDrawable(drawable);

        holder.nameTv.setText(agenda.getSessionName());
        holder.nameTv.setTextColor(Color.parseColor(colorActive));
        holder.descriptionTv.setText(agenda.getSessionDescription());


        try {

            SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.UK);
            SimpleDateFormat targetFormat = new SimpleDateFormat("HH:mm");

            Date startdate = originalFormat.parse(agenda.getSessionStartTime());
            Date enddate = originalFormat.parse(agenda.getSessionEndTime());
//            String timestart = String.valueOf(android.text.format.DateFormat.format("EEEE", startdate));
//            String timeend = String.valueOf(android.text.format.DateFormat.format("EEEE", enddate));
            String startdatestr = targetFormat.format(startdate);
            String enddatestr = targetFormat.format(enddate);


            holder.dateTv.setText(startdatestr + " - " + enddatestr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agendadetail = new Intent(context, AgendaDetailActivity.class);

                agendadetail.putExtra("id", agenda.getSessionId());
                agendadetail.putExtra("date", agenda.getSessionDate());
                agendadetail.putExtra("name", agenda.getSessionName());
                agendadetail.putExtra("description", agenda.getSessionDescription());
                agendadetail.putExtra("starttime", agenda.getSessionStartTime());
                agendadetail.putExtra("endtime", agenda.getSessionEndTime());
                context.startActivity(agendadetail);
            }
        });
    }


    public interface AgendaAdapterListner {
        void onContactSelecteddate(AgendaList agendaList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, dateTv, descriptionTv, tvheading;
        public LinearLayout mainLL;
        public ImageView ic_rightarrow;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            dateTv = view.findViewById(R.id.dateTv);
            descriptionTv = view.findViewById(R.id.descriptionTv);
            tvheading = view.findViewById(R.id.tvheading);
            ic_rightarrow = view.findViewById(R.id.ic_rightarrow);


            mainLL = view.findViewById(R.id.mainLL);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
