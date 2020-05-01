package com.procialize.mrgeApp20.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.mrgeApp20.GetterSetter.Forecast;
import com.procialize.mrgeApp20.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Naushad on 10/31/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<Forecast> forecastList;
    private Context context;
    private WeatherAdapterListner listener;

    public WeatherAdapter(Context context, List<Forecast> forecastList, WeatherAdapterListner listener) {
        this.forecastList = forecastList;
        this.listener = listener;
        this.context = context;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weatherrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Forecast forecast = forecastList.get(position);

        char tmp = 0x00B0;


        int min_temp = (forecast.getLow() - 32) * 5 / 9;
        int max_temp = (forecast.getHigh() - 32) * 5 / 9;


        holder.maxTv.setText(String.valueOf(max_temp) + tmp + "");
        holder.minTv.setText(String.valueOf(min_temp) + tmp + "");
        holder.mainLL.setBackgroundColor(Color.parseColor(colorActive));

        holder.typev.setText(forecast.getText());

//        if (forecast.getText().equalsIgnoreCase("Thunderstorms"))
//        {
//            holder.imageIv.setImageResource(R.drawable.thunderwhite);
//        }else if (forecast.getText().equalsIgnoreCase("Mostly Cloudy"))
//        {
//            holder.imageIv.setImageResource(R.drawable.cleanwhite);
//        }else if (forecast.getText().equalsIgnoreCase("Cloudy"))
//        {
//            holder.imageIv.setImageResource(R.drawable.cloudywhite);
//        }else
//        {
//            holder.imageIv.setImageResource(R.drawable.sunnywhite);
//        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM");
        SimpleDateFormat mymonthFormat = new SimpleDateFormat("EEE");

        Date date = new Date();
        try {
            date = dateFormat.parse(forecast.getDate());
            System.out.println("Date is: " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String datestr = myFormat.format(date);
        String month = mymonthFormat.format(date);

        holder.dateTv.setText(datestr);
        holder.nameTv.setText(forecast.getDay());
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public interface WeatherAdapterListner {
        void onContactSelected(Forecast forecast);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, dateTv, maxTv, minTv, typev;
        public ImageView imageIv;
        public LinearLayout mainLL;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            dateTv = view.findViewById(R.id.dateTv);
            maxTv = view.findViewById(R.id.maxTv);
            minTv = view.findViewById(R.id.minTv);
            typev = view.findViewById(R.id.typev);
            mainLL = view.findViewById(R.id.mainLL);

            imageIv = view.findViewById(R.id.imageIv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(forecastList.get(getAdapterPosition()));
                }
            });
        }
    }
}