package com.procialize.mrgeApp20.AttendeeChat.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class AttendeeChatAdapterRecycler extends RecyclerView.Adapter<AttendeeChatAdapterRecycler.MyViewHolder> {

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

    public AttendeeChatAdapterRecycler(Context context, List<chat_list> directQuestionLists, String att_id) {
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (directQuestionLists.get(position).getReceiver_id().equalsIgnoreCase(att_id)) {
            return 1;
        } else if (directQuestionLists.get(position).getSender_id().equalsIgnoreCase(att_id)) {
            return 2;
        } else {
            return 0;
        }
        // return position;
    }

    @NonNull
    @Override
    public AttendeeChatAdapterRecycler.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_row, parent, false);
      /*  View view;
        if (viewType == 1) { // for call layout
            view = LayoutInflater.from(context).inflate(R.layout.chat_row1, parent, false);
            return new ChatRowViewHolder(view);

        } else { // for email layout
            view = LayoutInflater.from(context).inflate(R.layout.chat_row, parent, false);
            return new ChatRow1ViewHolder(view);
        }*/
        return new MyViewHolder(itemView);
    }

   /* @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        chat_list question = directQuestionLists.get(position);
        if (getItemViewType(position) == 1) {
            ((ChatRowViewHolder) holder).setChatRowDetails(question);
        } else {
            ((ChatRow1ViewHolder) holder).setChatRow1Details(question);
        }
    }*/

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        chat_list question = directQuestionLists.get(position);
        if (getItemViewType(position) == 1) {
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
        } else if (getItemViewType(position) == 2) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return directQuestionLists.size();
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

/*    class ChatRowViewHolder extends RecyclerView.ViewHolder {

        private TextView AnsTv;
        private TextView dateATv;

        ChatRowViewHolder(@NonNull View itemView) {
            super(itemView);
            AnsTv = itemView.findViewById(R.id.AnsTv);
            dateATv = itemView.findViewById(R.id.dateATv);
        }

        private void setChatRowDetails(chat_list chat_list1) {
            try {
                AnsTv.setText(StringEscapeUtils.unescapeJava(chat_list1.getMessage()));
            } catch (IllegalArgumentException e) {

            }
            try {
                if (chat_list1.getTimestamp() != null) {
                    SimpleDateFormat formatter = null;

                    String formate1 = ApiConstant.dateformat;
                    String formate2 = ApiConstant.dateformat1;

                    if (Utility.isValidFormat(formate1, chat_list1.getTimestamp(), Locale.UK)) {
                        formatter = new SimpleDateFormat(ApiConstant.dateformat);
                    } else if (Utility.isValidFormat(formate2, chat_list1.getTimestamp(), Locale.UK)) {
                        formatter = new SimpleDateFormat(ApiConstant.dateformat1);
                    }

                    try {
                        Date date1 = formatter.parse(chat_list1.getTimestamp());

                        //DateFormat originalFormat = new SimpleDateFormat("dd MMM , HH:mm", Locale.UK);
                        DateFormat originalFormat = new SimpleDateFormat("HH:mm", Locale.UK);

                        String date = originalFormat.format(date1);

                        dateATv.setText(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
            }
        }

    }

    class ChatRow1ViewHolder extends RecyclerView.ViewHolder {

        private TextView QaTv;
        private TextView dateNTv;

        ChatRow1ViewHolder(@NonNull View itemView) {
            super(itemView);
            QaTv = itemView.findViewById(R.id.QaTv);
            dateNTv = itemView.findViewById(R.id.dateNTv);
        }

        private void setChatRow1Details(chat_list chat_list1) {
            try {
                QaTv.setText(StringEscapeUtils.unescapeJava(chat_list1.getMessage()));
            } catch (IllegalArgumentException e) {

            }
            try {
                //holder.nameTv.setText(question.get());
                if (chat_list1.getTimestamp() != null) {
                    SimpleDateFormat formatter = null;
                    String formate1 = ApiConstant.dateformat;
                    String formate2 = ApiConstant.dateformat1;

                    if (Utility.isValidFormat(formate1, chat_list1.getTimestamp(), Locale.UK)) {
                        formatter = new SimpleDateFormat(ApiConstant.dateformat);
                    } else if (Utility.isValidFormat(formate2, chat_list1.getTimestamp(), Locale.UK)) {
                        formatter = new SimpleDateFormat(ApiConstant.dateformat1);
                    }

                    try {
                        Date date1 = formatter.parse(chat_list1.getTimestamp());

                        //DateFormat originalFormat = new SimpleDateFormat("dd MMM , HH:mm", Locale.UK);
                        DateFormat originalFormat = new SimpleDateFormat("HH:mm", Locale.UK);

                        String date = originalFormat.format(date1);

                        dateNTv.setText(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {

            }
        }
    }*/
}

