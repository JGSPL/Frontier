package com.procialize.frontier.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;

import com.procialize.frontier.GetterSetter.TravelList;
import com.procialize.frontier.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyTravelAdapterList extends BaseAdapter {
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<TravelList> travelLists;
    private Context context;
    private MyTravelAdapterListner listener;
    private LayoutInflater inflater;


    public MyTravelAdapterList(Context context, List<TravelList> travelList, MyTravelAdapterListner listener) {
        this.travelLists = travelList;
        this.listener = listener;
        this.context = context;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public int getCount() {
        return travelLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final TravelList travel = travelLists.get(position);
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.travel_detail, null);

            holder = new ViewHolder();

            Display dispDefault = ((WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            int totalwidth = dispDefault.getWidth();


            holder.nameTv = convertView.findViewById(R.id.nameTv);
            holder.ic_rightarrow = convertView.findViewById(R.id.ic_rightarrow);
            holder.linTicket = convertView.findViewById(R.id.linTicket);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.linTicket.setBackgroundColor(Color.parseColor(colorActive));
        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.ic_rightarrow.setImageDrawable(drawable);


        holder.nameTv.setText(travel.getTitle());
        holder.nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactSelected(travelLists.get(position));
            }
        });

        return convertView;
    }

    public interface MyTravelAdapterListner {
        void onContactSelected(TravelList travel);
    }

    static class ViewHolder {
        public TextView nameTv;

        ImageView ic_rightarrow;
        LinearLayout linTicket;
    }
}
