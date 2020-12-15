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

import com.procialize.frontier.GetterSetter.SurveyList;
import com.procialize.frontier.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Naushad on 10/31/2017.
 */

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<SurveyList> surveyLists;
    private Context context;
    private FeedBackAdapterListner listener;

    public FeedBackAdapter(Context context, List<SurveyList> speakerList, FeedBackAdapterListner listener) {
        this.surveyLists = speakerList;
        this.listener = listener;
        this.context = context;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedbackrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SurveyList survey = surveyLists.get(position);
        int colorInt = Color.parseColor(colorActive);
        int color2 = Color.parseColor("#ffffff");

        ColorStateList csl = ColorStateList.valueOf(color2);
        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.ic_rightarrow.setImageDrawable(drawable);


        holder.nameTv.setText(survey.getName());
//        holder.mainLL.setBackgroundColor(colorInt);

    }

    @Override
    public int getItemCount() {
        return surveyLists.size();
    }

    public interface FeedBackAdapterListner {
        void onContactSelected(SurveyList survey);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        ImageView ic_rightarrow;
        LinearLayout mainLL;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            ic_rightarrow = view.findViewById(R.id.ic_rightarrow);
            mainLL = view.findViewById(R.id.mainLL);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(surveyLists.get(getAdapterPosition()));
                }
            });
        }
    }
}