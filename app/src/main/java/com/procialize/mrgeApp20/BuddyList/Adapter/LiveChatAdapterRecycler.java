package com.procialize.mrgeApp20.BuddyList.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.BuddyList.DataModel.chat_list;
import com.procialize.mrgeApp20.CustomTools.ClickableViewPager;
import com.procialize.mrgeApp20.CustomTools.ScaledImageView;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.jzvd.JzvdStd;

import static android.content.Context.MODE_PRIVATE;

public class LiveChatAdapterRecycler extends RecyclerView.Adapter<LiveChatAdapterRecycler.MyViewHolder> {

    String token, message;
    String QA_like_question, QA_reply_question;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    String size = "5";
    private List<chat_list> directQuestionLists;
    private Context context;
    private String speakername;
    private APIService mAPIService;
    private LayoutInflater inflater;
    private String att_id;

    public LiveChatAdapterRecycler(Context context, List<chat_list> directQuestionLists, String att_id) {
        this.directQuestionLists = directQuestionLists;
        if (size.equalsIgnoreCase(String.valueOf(directQuestionLists.size()))) {
            //  ActivityBuddyChat.chat_id = directQuestionLists.get(0).getId();
        }

        //Collections.reverse(directQuestionLists);

        this.context = context;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        this.att_id = att_id;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView QaTv, AnsTv, ReviewTv, dateNTv, dateATv;
        LinearLayout linAns, linQues;

        public MyViewHolder(View convertView) {
            super(convertView);



            QaTv = convertView.findViewById(R.id.QaTv);
            AnsTv = convertView.findViewById(R.id.AnsTv);
            ReviewTv = convertView.findViewById(R.id.ReviewTv);
            linAns = convertView.findViewById(R.id.linAns);
            linQues = convertView.findViewById(R.id.linQues);
            dateNTv = convertView.findViewById(R.id.dateNTv);
            dateATv = convertView.findViewById(R.id.dateATv);
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

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        chat_list question = directQuestionLists.get(position);
        if (question.getReceiver_id().equalsIgnoreCase(att_id)) {
            try {
                holder.AnsTv.setText(StringEscapeUtils.unescapeJava(question.getMessage()));
            } catch (IllegalArgumentException e) {

            }
            try {
                holder.linAns.setVisibility(View.VISIBLE);
                holder.ReviewTv.setVisibility(View.GONE);
                holder.linQues.setVisibility(View.GONE);
                if (question.getTimestamp() != null) {
                    SimpleDateFormat formatter = null;

                    String formate1 = ApiConstant.dateformat;
                    String formate2 = ApiConstant.dateformat1;

                    if (Utility.isValidFormat(formate1, question.getTimestamp(), Locale.UK)) {
                        formatter = new SimpleDateFormat(ApiConstant.dateformat);
                    } else if (Utility.isValidFormat(formate2, question.getTimestamp(), Locale.UK)) {
                        formatter = new SimpleDateFormat(ApiConstant.dateformat1);
                    }

                    try {
                        Date date1 = formatter.parse(question.getTimestamp());

                        //DateFormat originalFormat = new SimpleDateFormat("dd MMM , HH:mm", Locale.UK);
                        DateFormat originalFormat = new SimpleDateFormat("HH:mm", Locale.UK);

                        String date = originalFormat.format(date1);

                        holder.dateATv.setText(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
            }

        } else if (question.getSender_id().equalsIgnoreCase(att_id)) {
            try {
                holder.QaTv.setText(StringEscapeUtils.unescapeJava(question.getMessage()));
            } catch (IllegalArgumentException e) {

            }
            try {
                //holder.nameTv.setText(question.get());
                if (question.getTimestamp() != null) {
                    SimpleDateFormat formatter = null;

                    String formate1 = ApiConstant.dateformat;
                    String formate2 = ApiConstant.dateformat1;

                    if (Utility.isValidFormat(formate1, question.getTimestamp(), Locale.UK)) {
                        formatter = new SimpleDateFormat(ApiConstant.dateformat);
                    } else if (Utility.isValidFormat(formate2, question.getTimestamp(), Locale.UK)) {
                        formatter = new SimpleDateFormat(ApiConstant.dateformat1);
                    }

                    try {
                        Date date1 = formatter.parse(question.getTimestamp());

                        //DateFormat originalFormat = new SimpleDateFormat("dd MMM , HH:mm", Locale.UK);
                        DateFormat originalFormat = new SimpleDateFormat("HH:mm", Locale.UK);

                        String date = originalFormat.format(date1);

                        holder.dateNTv.setText(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                holder.AnsTv.setVisibility(View.GONE);
                holder.ReviewTv.setVisibility(View.GONE);
                holder.linAns.setVisibility(View.GONE);
                holder.linQues.setVisibility(View.VISIBLE);
            } catch (Exception e) {

            }
        } else {
            try {
                holder.AnsTv.setVisibility(View.GONE);
                holder.ReviewTv.setVisibility(View.GONE);
                holder.linAns.setVisibility(View.GONE);
                holder.linQues.setVisibility(View.GONE);
            }catch (Exception e)
            {e.printStackTrace();}
        }
    }

    @Override
    public int getItemCount() {
        return directQuestionLists.size();
    }
}

