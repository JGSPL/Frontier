package com.procialize.mrgeApp20.BuddyList.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.BuddyList.DataModel.chat_list;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Utility.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class LiveChatAdapter extends BaseAdapter {

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

    public LiveChatAdapter(Context context, List<chat_list> directQuestionLists, String att_id) {
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


    @Override
    public int getCount() {
        return directQuestionLists.size();
    }

    @Override
    public Object getItem(int position) {
        return directQuestionLists.get(position);
    }

   /* @Override
    public int getViewTypeCount() {
        return getCount();
    }*/

    @Override
    public int getViewTypeCount() {
        //if (getCount() > 0) {
            return directQuestionLists.size();//getCount();
        /*} else {
            return super.getViewTypeCount();
        }*/
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        chat_list question;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_row,
                    null);

            holder = new ViewHolder();

            holder.QaTv = convertView.findViewById(R.id.QaTv);
            holder.AnsTv = convertView.findViewById(R.id.AnsTv);
            holder.ReviewTv = convertView.findViewById(R.id.ReviewTv);
            holder.linAns = convertView.findViewById(R.id.linAns);
            holder.linQues = convertView.findViewById(R.id.linQues);
            holder.dateNTv = convertView.findViewById(R.id.dateNTv);

            holder.dateATv = convertView.findViewById(R.id.dateATv);
            question = directQuestionLists.get(position);


            //if (question.geta()!=null && QA_reply_question.equalsIgnoreCase("1")) {
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
        return convertView;
    }


    public static class ViewHolder {
        public TextView QaTv, AnsTv, ReviewTv, dateNTv, dateATv;
        LinearLayout linAns, linQues;
    }

}

