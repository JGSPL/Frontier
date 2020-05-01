package com.procialize.mrgeApp20.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.GetterSetter.Agenda;
import com.procialize.mrgeApp20.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AgendaListAdapter extends BaseAdapter {
    Date d1, d2;
    ApiConstant constant = new ApiConstant();
    private Activity activity;
    private LayoutInflater inflater;
    private List<Agenda> agendaList;

    public AgendaListAdapter(Activity activity, List<Agenda> agendaList) {
        this.activity = activity;
        this.agendaList = agendaList;
    }

    @Override
    public int getCount() {
        return agendaList.size();
    }

    @Override
    public Object getItem(int location) {
        return agendaList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Agenda getAttendeeFromList(int position) {
        return agendaList.get(position);
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.agenda_list_row, null);

            holder = new ViewHolder();

            holder.agenda_time_slot = convertView
                    .findViewById(R.id.agenda_time_slot);
            holder.agenda_title = convertView
                    .findViewById(R.id.agenda_title);

            holder.agenda_desc = convertView
                    .findViewById(R.id.agenda_desc);

            Typeface typeFace = Typeface.createFromAsset(activity.getAssets(),
                    "fonts/DINPro-Regular.ttf");

            holder.agenda_time_slot.setTypeface(typeFace);
            holder.agenda_title.setTypeface(typeFace);
            holder.agenda_desc.setTypeface(typeFace);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//		String startTime = agendaList.get(position).getSession_start_time();
//		String endTime = agendaList.get(position).getSession_end_time();

//		Log.e("time",endTime);
        // SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat f = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");

//		try {
//			d1 = f.parse(startTime);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		long millisecondsStart = d1.getTime();
//
//		try {
//			d2 = f.parse(endTime);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		long millisecondsEnd = d2.getTime();
//
//		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
//		SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mm a");
//
//
//		String finalStartTime = formatter.format(new Date(millisecondsStart));
//		String finalEndTime = formatter1.format(new Date(millisecondsEnd));

//		holder.agenda_time_slot.setText(finalStartTime+" - "+finalEndTime);
//		holder.agenda_title.setText(agendaList.get(position).getSession_name());
//		holder.agenda_desc.setText(agendaList.get(position).getSession_description());

        return convertView;
    }

    static class ViewHolder {
        TextView agenda_time_slot, agenda_title, attendee_comp_name,
                agenda_desc;

    }

}
