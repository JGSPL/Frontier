package com.procialize.mrgeApp20.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;

import com.procialize.mrgeApp20.CustomTools.CircleDisplay;
import com.procialize.mrgeApp20.GetterSetter.QuizFolder;
import com.procialize.mrgeApp20.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class QuizFolderAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<QuizFolder> quizList;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;

    public QuizFolderAdapter(Activity activity, List<QuizFolder> quizList) {
        this.activity = activity;
        this.quizList = quizList;
        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public int getCount() {
        return quizList.size();
    }

    @Override
    public Object getItem(int location) {
        return quizList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public QuizFolder getQuestionIdFromList(int position) {
        return quizList.get(position);
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }


   /* @Override
    public int getViewTypeCount() {
        return getCount();
    }*/

    @Override
    public int getViewTypeCount() {
        if (getCount() > 0) {
            return getCount();
        } else {
            return super.getViewTypeCount();
        }
    }



    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.quiz_row, null);

            holder = new ViewHolder();

            // holder.video_preview_img = (ImageView) convertView
            // .findViewById(R.id.video_preview_img);

            holder.quiz_title_txt = (TextView) convertView
                    .findViewById(R.id.video_title_txt);
            holder.video_status = (TextView) convertView
                    .findViewById(R.id.video_status);

            holder.relative = (RelativeLayout) convertView
                    .findViewById(R.id.relative);
            holder.textViewTime = (TextView) convertView
                    .findViewById(R.id.textViewTime);



            holder.progressBarCircle = (ProgressBar) convertView.findViewById(R.id.progressBarCircle);


            holder.linQuiz = (LinearLayout)convertView.findViewById(R.id.linQuiz);

            if(position==0){
                holder.relative.setVisibility(View.VISIBLE);
            }else{
                holder.relative.setVisibility(View.GONE);

            }



            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.quiz_title_txt.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getFolder_name()));
        }catch (IllegalArgumentException e){
            e.printStackTrace();

        }
        if(quizList.get(position).getAnswered().equalsIgnoreCase("0")){
            holder.progressBarCircle.setVisibility(View.INVISIBLE);
            holder.textViewTime.setVisibility(View.INVISIBLE);
            holder.video_status.setText("Tap to start");
        }else {
            holder.progressBarCircle.setVisibility(View.VISIBLE);
            holder.textViewTime.setVisibility(View.VISIBLE);
            holder.video_status.setText("Completed");

            holder.textViewTime.setTextColor(Color.parseColor(colorActive));
            holder.textViewTime.setText(Integer.parseInt(quizList.get(position).getTotal_correct())+"/"+
                    Integer.parseInt(quizList.get(position).getTotal_quiz()));

            holder.progressBarCircle.setMax(Integer.parseInt(quizList.get(position).getTotal_quiz()));
            if(quizList.get(position).getTotal_correct().equalsIgnoreCase("0")){
                holder.progressBarCircle.setProgress(0);

            }else {

            holder.progressBarCircle.setProgress(Integer.parseInt(quizList.get(position).getTotal_correct()));
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView quiz_title_txt,video_status,textViewTime;
        LinearLayout linQuiz;
        RelativeLayout relative;
        ProgressBar progressBarCircle;

    }


}
