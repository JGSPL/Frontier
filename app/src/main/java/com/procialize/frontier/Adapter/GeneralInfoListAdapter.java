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

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.frontier.GetterSetter.InfoList;
import com.procialize.frontier.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class GeneralInfoListAdapter extends RecyclerView.Adapter<GeneralInfoListAdapter.MyViewHolder> {

    List<InfoList> infoList;
    Context context;
    GeneralInfoListener listener;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;


    public GeneralInfoListAdapter(Context context, List<InfoList> infoList, GeneralInfoListener listener) {
        this.context = context;
        this.infoList = infoList;
        this.listener = listener;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public GeneralInfoListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.general_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final InfoList infonew = infoList.get(position);
        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.ic_rightarrow.setImageDrawable(drawable);


        holder.txt_info.setText(infonew.getName());
        holder.txt_info.setTextColor(Color.parseColor(colorActive));


    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public interface GeneralInfoListener {
        void onContactSelected(InfoList firstLevelFilter);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_info;
        public View layout;
        public ImageView ic_rightarrow;

        public MyViewHolder(View view) {
            super(view);
            layout = view;
            txt_info = view.findViewById(R.id.txt_info);
            ic_rightarrow = view.findViewById(R.id.ic_rightarrow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onContactSelected(infoList.get(getAdapterPosition()));

                }
            });

        }
    }
}