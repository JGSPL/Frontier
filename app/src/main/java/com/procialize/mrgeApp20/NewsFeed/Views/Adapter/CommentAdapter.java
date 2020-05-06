package com.procialize.mrgeApp20.NewsFeed.Views.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.CommentDataList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Utility.Utility;
import com.procialize.mrgeApp20.NewsFeed.Views.Activity.AttendeeDetailActivity;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Naushad on 10/31/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    public List<CommentDataList> commentLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    String flag;
    private Context context;
    private CommentAdapterListner listener;
    String substring;
    private List<AttendeeList> attendeeDBList;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public CommentAdapter(Context context, List<CommentDataList> commentLists, CommentAdapterListner listener, String flag) {
        this.commentLists = commentLists;
        this.listener = listener;
        this.context = context;
        this.flag = flag;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

        procializeDB = new DBHelper(context);
        db = procializeDB.getWritableDatabase();
        dbHelper = new DBHelper(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commentlistingrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CommentDataList comment = commentLists.get(position);
      //  holder.nameTv.setTextColor(Color.parseColor(colorActive));

//        if (flag.equalsIgnoreCase("Notification")) {
//            holder.moreIv.setVisibility(View.GONE);
//        } else {
//            holder.moreIv.setVisibility(View.VISIBLE);
//        }

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        holder.nameTv.setText(comment.getFirstName() + " " + comment.getLastName());

        try {
//            Date date1 = formatter.parse(comment.getCreated());
//
//            DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.ENGLISH);
//
//            String date = originalFormat.format(date1);
//
//            holder.dateTv.setText(date);


            SimpleDateFormat formatter = null;

            String formate1 = ApiConstant.dateformat;
            String formate2 = ApiConstant.dateformat1;

            if (Utility.isValidFormat(formate1, comment.getCreated(), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat);
            } else if (Utility.isValidFormat(formate2, comment.getCreated(), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat1);
            }

            Date date1 = formatter.parse(comment.getCreated());

            DateFormat originalFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.UK);

            String date = originalFormat.format(date1);

            holder.dateTv.setText(date);


            Log.e("date", commentLists.size() + "");

        } catch (ParseException e) {
            e.printStackTrace();
        }
//


        if (comment.getProfilePic() != null) {
            Glide.with(context).load(ApiConstant.profilepic + comment.getProfilePic())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.progressView.setVisibility(View.GONE);
                    holder.profileIv.setImageResource(R.drawable.profilepic_placeholder);

                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.progressView.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.profileIv).onLoadStarted(context.getDrawable(R.drawable.profilepic_placeholder));
        } else {
            holder.progressView.setVisibility(View.GONE);

        }

        if (comment.getComment().contains("gif")) {
            holder.gifIV.setVisibility(View.VISIBLE);
//            holder.commentTv.setVisibility(View.GONE);
            holder.progressViewgif.setVisibility(View.VISIBLE);
            holder.commentTv.setText("GIF");
            holder.commentTv.setVisibility(View.GONE);
            Glide.with(context)
                    .load(commentLists.get(position).getComment())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressViewgif.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressViewgif.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.gifIV);


        } else {
            holder.progressViewgif.setVisibility(View.GONE);
            holder.commentTv.setVisibility(View.VISIBLE);
            holder.gifIV.setVisibility(View.GONE);


//            holder.commentTv.setText(StringEscapeUtils.unescapeJava(comment.getComment()));


            holder.testdata.setText(StringEscapeUtils.unescapeJava(comment.getComment()));

            final SpannableStringBuilder stringBuilder = new SpannableStringBuilder(holder.testdata.getText());
            if (comment.getComment() != null) {

                holder.commentTv.setVisibility(View.VISIBLE);
//                    holder.wallNotificationText.setText(getEmojiFromString(notificationImageStatus));
                int flag = 0;
                for (int i = 0; i < stringBuilder.length(); i++) {
                    String sample = stringBuilder.toString();
                    if ((stringBuilder.charAt(i) == '<')) {
                        try {
                            String text = "<";
                            String text1 = ">";

                            if (flag == 0) {
                                int start = sample.indexOf(text, i);
                                int end = sample.indexOf(text1, i);

                                Log.v("Indexes of", "Start : " + start + "," + end);
                                try {
                                    substring = sample.substring(start, end + 1);
                                    Log.v("String names: ", substring);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                if (substring.contains("<")) {
                                    if (sample.contains(substring)) {
                                        substring = substring.replace("<", "");
                                        substring = substring.replace(">", "");
                                        int index = substring.indexOf("^");
//                                    substring = substring.replace("^", "");
                                        final String attendeeid = substring.substring(0, index);
                                        substring = substring.substring(index+1, substring.length());


                                        stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                                        stringBuilder.setSpan(new ClickableSpan() {
                                            @Override
                                            public void onClick(View widget) {
                                                attendeeDBList = dbHelper.getAttendeeDetailsId(attendeeid);
                                                Intent intent = new Intent(context, AttendeeDetailActivity.class);
                                                intent.putExtra("id", attendeeDBList.get(0).getAttendeeId());
                                                intent.putExtra("name", attendeeDBList.get(0).getFirstName() + " " + attendeeDBList.get(0).getLastName());
                                                intent.putExtra("city", attendeeDBList.get(0).getCity());
                                                intent.putExtra("country", attendeeDBList.get(0).getCountry());
                                                intent.putExtra("company", attendeeDBList.get(0).getCompanyName());
                                                intent.putExtra("designation", attendeeDBList.get(0).getDesignation());
                                                intent.putExtra("description", attendeeDBList.get(0).getDescription());
                                                intent.putExtra("profile", attendeeDBList.get(0).getProfilePic());
                                                context.startActivity(intent);
                                            }
                                        }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        stringBuilder.replace(start, end + 1, substring);
                                        holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                        holder.commentTv.setMovementMethod(LinkMovementMethod.getInstance());
                                        holder.commentTv.setText(stringBuilder);
                                        flag = 1;
//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        attendees.setComment(substring);
                                    }
                                }
                            } else {

                                int start = sample.indexOf(text, i);
                                int end = sample.indexOf(text1, i);

                                Log.v("Indexes of", "Start : " + start + "," + end);
                                try {
                                    substring = sample.substring(start, end + 1);
                                    Log.v("String names: ", substring);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (substring.contains("<")) {
                                    if (sample.contains(substring)) {
                                        substring = substring.replace("<", "");
                                        substring = substring.replace(">", "");
                                        int index = substring.indexOf("^");
//                                    substring = substring.replace("^", "");
                                        final String attendeeid = substring.substring(0, index);
                                        substring = substring.substring(index+1, substring.length());


                                        stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        stringBuilder.setSpan(new ClickableSpan() {
                                            @Override
                                            public void onClick(View widget) {
                                                attendeeDBList = dbHelper.getAttendeeDetailsId(attendeeid);
                                                Intent intent = new Intent(context, AttendeeDetailActivity.class);
                                                intent.putExtra("id", attendeeDBList.get(0).getAttendeeId());
                                                intent.putExtra("name", attendeeDBList.get(0).getFirstName() + " " + attendeeDBList.get(0).getLastName());
                                                intent.putExtra("city", attendeeDBList.get(0).getCity());
                                                intent.putExtra("country", attendeeDBList.get(0).getCountry());
                                                intent.putExtra("company", attendeeDBList.get(0).getCompanyName());
                                                intent.putExtra("designation", attendeeDBList.get(0).getDesignation());
                                                intent.putExtra("description", attendeeDBList.get(0).getDescription());
                                                intent.putExtra("profile", attendeeDBList.get(0).getProfilePic());
                                                context.startActivity(intent);
                                            }
                                        }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        stringBuilder.replace(start, end + 1, substring);
                                        holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                        holder.commentTv.setMovementMethod(LinkMovementMethod.getInstance());

                                        holder.commentTv.setText(stringBuilder);


//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        attendees.setComment(substring);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                holder.commentTv.setText(stringBuilder);
            } else {
                holder.commentTv.setVisibility(View.GONE);
            }



        }
    }

    @Override
    public int getItemCount() {
        return commentLists.size();
    }

    public interface CommentAdapterListner {

        void onContactSelected(CommentDataList comment);

        void onMoreSelected(CommentDataList comment, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, commentTv, dateTv,testdata;
        public ImageView profileIv;
        public ProgressBar progressView, progressViewgif;
        public ImageView moreIv;
        public LinearLayout textcommentContainer;
        public ImageView gifIV;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            commentTv = view.findViewById(R.id.commentTv);
            dateTv = view.findViewById(R.id.dateTv);

            profileIv = view.findViewById(R.id.profileIV);
            gifIV = view.findViewById(R.id.gifIV);

            moreIv = view.findViewById(R.id.moreIv);

            progressView = view.findViewById(R.id.progressView);
            progressViewgif = view.findViewById(R.id.progressViewgif);
            textcommentContainer = view.findViewById(R.id.textcommentContainer);
            testdata = view.findViewById(R.id.testdata);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(commentLists.get(getAdapterPosition()));
                }
            });

            moreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMoreSelected(commentLists.get(getAdapterPosition()), getAdapterPosition());
                }
            });

        }
    }
}