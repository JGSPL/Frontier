package com.procialize.mrgeApp20.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.Activity.AttendeeDetailActivity;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.NotificationList;
import com.procialize.mrgeApp20.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Naushad on 10/31/2017.//preeti
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<NotificationList> notificationLists;
    private Context context;
    private NotificationAdapterListner listener;
    String substring;
    private List<AttendeeList> attendeeDBList;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public NotificationAdapter(Context context, List<NotificationList> notificationLists, NotificationAdapterListner listener) {
        this.notificationLists = notificationLists;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

        procializeDB = new DBHelper(context);
        db = procializeDB.getWritableDatabase();
        dbHelper = new DBHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NotificationList notificationList = notificationLists.get(position);

        holder.notiLin.setBackgroundColor(Color.parseColor(colorActive));
       holder.nameTv.setTextColor(Color.parseColor(colorActive));

        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
       /* Drawable drawable = DrawableCompat.wrap(holder.arrowIv.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.arrowIv.setImageDrawable(drawable);

        Drawable drawable1 = DrawableCompat.wrap(holder.arrowIv.getDrawable());
        DrawableCompat.setTintList(drawable1, csl);
        holder.arrowIvmsg.setImageDrawable(drawable1);*/


        if (notificationList.getNotificationContent() != null) {
            if (notificationList.getNotificationContent().contains("gif")) {
                holder.messageTV.setVisibility(View.GONE);
                holder.gifiv.setVisibility(View.VISIBLE);
               // holder.messageTV.setText("GIF");

                    Glide.with(context).load(notificationList.getNotificationContent())
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(holder.gifiv);
            } else {
                holder.messageTV.setVisibility(View.VISIBLE);
                if (notificationList.getMedia_file() != null) {
                    holder.gifiv.setVisibility(View.VISIBLE);
                    Glide.with(context).load(notificationList.getMedia_file())
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(holder.gifiv);
                } else{
                    holder.gifiv.setVisibility(View.GONE);
            }
                try{
                holder.testdata.setText(StringEscapeUtils.unescapeJava(notificationList.getNotificationContent()));
                }catch (IllegalArgumentException e){
                    e.printStackTrace();

                }
                final SpannableStringBuilder stringBuilder = new SpannableStringBuilder(holder.testdata.getText());
                if (notificationList.getNotificationContent() != null) {
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
                                            substring = substring.substring(index + 1, substring.length());


                                            stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            stringBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


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
                                                    intent.putExtra("mobile", attendeeDBList.get(0).getMobile());
                                                    intent.putExtra("buddy_status", attendeeDBList.get(0).getBuddy_status());

                                                    context.startActivity(intent);
                                                }
                                            }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            stringBuilder.replace(start, end + 1, substring);
                                            holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                            holder.messageTV.setMovementMethod(LinkMovementMethod.getInstance());
                                            holder.messageTV.setText(stringBuilder);
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
                                            substring = substring.substring(index + 1, substring.length());


                                            stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            stringBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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
                                                    intent.putExtra("mobile", attendeeDBList.get(0).getMobile());
                                                    intent.putExtra("buddy_status", attendeeDBList.get(0).getBuddy_status());

                                                    context.startActivity(intent);
                                                }
                                            }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                            stringBuilder.replace(start, end + 1, substring);
                                            holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                            holder.messageTV.setMovementMethod(LinkMovementMethod.getInstance());

                                            holder.messageTV.setText(stringBuilder);

                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    holder.messageTV.setText(stringBuilder);
                } else {
                    holder.messageTV.setVisibility(View.GONE);
                }
            }
        }

        if (notificationList.getNotificationType().equalsIgnoreCase("Like")) {
            holder.txt_msg.setText("Liked Your Post");
            holder.messageTV.setVisibility(View.GONE);
            String lName = notificationList.getAttendeeLastName();if (notificationList.getMedia_file() != null) {
                holder.gifiv.setVisibility(View.VISIBLE);
                Glide.with(context).load(notificationList.getMedia_file())
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(holder.gifiv);
            } else{
                holder.gifiv.setVisibility(View.GONE);
            }

            if (lName != null) {
                holder.nameTv.setText(notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            } else {
                holder.nameTv.setText(notificationList.getAttendeeFirstName());

            }
        } else if (notificationList.getNotificationType().equalsIgnoreCase("Cmnt")) {
            holder.txt_msg.setText("commented on your post");
            String lName = notificationList.getAttendeeLastName();
            if (notificationList.getMedia_file() != null && !(notificationList.getMedia_file().equalsIgnoreCase(""))&&
            !(notificationList.getMedia_file().isEmpty())) {
                holder.gifiv.setVisibility(View.VISIBLE);
                Glide.with(context).load(notificationList.getMedia_file())
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(holder.gifiv);
            } else{
                holder.gifiv.setVisibility(View.GONE);
            }
            if (lName != null) {
                holder.nameTv.setText(notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            } else {
                holder.nameTv.setText(notificationList.getAttendeeFirstName());

            }
        } else if (notificationList.getNotificationType().equalsIgnoreCase("Post")) {
            holder.txt_msg.setText("");
            holder.txt_msg.setVisibility(View.GONE);
            String lName = notificationList.getAttendeeLastName();
            if (notificationList.getMedia_file() != null && !(notificationList.getMedia_file().equalsIgnoreCase(""))&&
                    !(notificationList.getMedia_file().isEmpty())) {
                holder.gifiv.setVisibility(View.VISIBLE);
                Glide.with(context).load(notificationList.getMedia_file())
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(holder.gifiv);
            } else{
                holder.gifiv.setVisibility(View.GONE);
            }
            if (lName != null) {
                holder.nameTv.setText(notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            } else {
                holder.nameTv.setText(notificationList.getAttendeeFirstName());

            }
        } else if (notificationList.getNotificationType().equalsIgnoreCase("T")) {
            holder.txt_msg.setText("");
            holder.txt_msg.setVisibility(View.GONE);
            holder.gifiv.setVisibility(View.GONE);

            String lName = notificationList.getAttendeeLastName();
            if (lName != null) {
                holder.nameTv.setText(notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            } else {
                holder.nameTv.setText(notificationList.getAttendeeFirstName());

            }
        } else if (notificationList.getNotificationType().equalsIgnoreCase("Quiz")) {
            holder.txt_msg.setText("");
            holder.txt_msg.setVisibility(View.GONE);
            String lName = notificationList.getAttendeeLastName();
            holder.gifiv.setVisibility(View.GONE);

            if (lName != null) {
                holder.nameTv.setText(notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            } else {
                holder.nameTv.setText(notificationList.getAttendeeFirstName());

            }
        } else {
            if (notificationList.getMedia_file() != null && !(notificationList.getMedia_file().equalsIgnoreCase(""))&&
                    !(notificationList.getMedia_file().isEmpty())) {
                holder.gifiv.setVisibility(View.VISIBLE);
                Glide.with(context).load(notificationList.getMedia_file())
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(holder.gifiv);
            } else{
                holder.gifiv.setVisibility(View.GONE);
            }

            String lName = notificationList.getAttendeeLastName();
            if (lName != null) {
                holder.nameTv.setText(notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            } else {
                holder.nameTv.setText(notificationList.getAttendeeFirstName());

            }

        }


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = formatter.parse(notificationList.getNotificationDate());

            DateFormat originalFormat = new SimpleDateFormat("dd MMMM yyyy hh:mm a", Locale.UK);

            String date = originalFormat.format(date1);

            holder.dataTv.setText(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (notificationList.getProfilePic() != null) {

            Glide.with(context).load(ApiConstant.profilepic + notificationList.getProfilePic())
                    .apply(RequestOptions.skipMemoryCacheOf(true)).circleCrop().centerCrop()
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

        if (notificationList.getNotificationType().equalsIgnoreCase("Cmnt")) {
            holder.txt_msg.setVisibility(View.VISIBLE);
            holder.arrowIv.setVisibility(View.VISIBLE);
            holder.arrowIvmsg.setVisibility(View.GONE);
            holder.ivtype.setImageResource(R.drawable.notifycoment);
            holder.arrowIv.setVisibility(View.VISIBLE);
            holder.arrowIv.setImageResource(R.drawable.ic_rightarrow);
            if(notificationList.getMedia_file().equalsIgnoreCase("")){
                holder.gifiv.setVisibility(View.GONE);

            }
        } else if (notificationList.getNotificationType().equalsIgnoreCase("Like")) {
            holder.txt_msg.setVisibility(View.VISIBLE);
            holder.arrowIv.setVisibility(View.VISIBLE);
            if(notificationList.getMedia_file().equalsIgnoreCase("")){
                holder.gifiv.setVisibility(View.GONE);

            }
            holder.arrowIvmsg.setVisibility(View.GONE);

            holder.ivtype.setImageResource(R.drawable.notifylike);
            holder.arrowIv.setImageResource(R.drawable.ic_rightarrow);
        } else if (notificationList.getNotificationType().equalsIgnoreCase("Msg")) {
            holder.txt_msg.setVisibility(View.VISIBLE);
            holder.arrowIvmsg.setVisibility(View.VISIBLE);
            if(notificationList.getMedia_file().equalsIgnoreCase("")){
                holder.gifiv.setVisibility(View.GONE);

            }
            holder.ivtype.setImageResource(R.drawable.notifymessage);
            holder.arrowIv.setVisibility(View.GONE);
            holder.arrowIvmsg.setImageResource(R.drawable.messageiv);
           // holder.arrowIvmsg.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);

        } else if (notificationList.getNotificationType().equalsIgnoreCase("Quiz")) {
            holder.txt_msg.setVisibility(View.GONE);
            holder.arrowIv.setVisibility(View.VISIBLE);
            holder.gifiv.setVisibility(View.GONE);

            holder.arrowIvmsg.setVisibility(View.GONE);

            holder.ivtype.setImageResource(R.drawable.notifyadmin);
            holder.arrowIv.setImageResource(R.drawable.ic_rightarrow);
        } else if (notificationList.getNotificationType().equalsIgnoreCase("Post")) {
            holder.txt_msg.setVisibility(View.GONE);
            holder.arrowIv.setVisibility(View.VISIBLE);
            holder.arrowIvmsg.setVisibility(View.GONE);
            holder.ivtype.setImageResource(R.drawable.notifyadmin);
            holder.arrowIv.setImageResource(R.drawable.ic_rightarrow);
        } else if (notificationList.getNotificationType().equalsIgnoreCase("T")) {
            holder.txt_msg.setVisibility(View.GONE);
            holder.arrowIv.setVisibility(View.VISIBLE);
            holder.gifiv.setVisibility(View.GONE);

            holder.arrowIvmsg.setVisibility(View.GONE);

            holder.ivtype.setImageResource(R.drawable.notifylike);
            holder.arrowIv.setImageResource(R.drawable.ic_rightarrow);

        } else if (notificationList.getNotificationType().equalsIgnoreCase("Live_poll")) {
            holder.txt_msg.setVisibility(View.GONE);
            holder.arrowIv.setVisibility(View.VISIBLE);
            holder.gifiv.setVisibility(View.GONE);

            holder.arrowIvmsg.setVisibility(View.GONE);

            holder.ivtype.setImageResource(R.drawable.notifylike);
            holder.arrowIv.setImageResource(R.drawable.ic_rightarrow);

        } else {
            holder.txt_msg.setVisibility(View.GONE);
            holder.arrowIv.setVisibility(View.GONE);
            holder.arrowIvmsg.setVisibility(View.GONE);
            holder.arrowIvmsg.setVisibility(View.GONE);
            if(notificationList.getMedia_file().equalsIgnoreCase("")){
                holder.gifiv.setVisibility(View.GONE);

            }

            holder.ivtype.setImageResource(R.drawable.notifyadmin);
            holder.arrowIv.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return notificationLists.size();
    }

    public interface NotificationAdapterListner {
        void onContactSelected(NotificationList notification, Context context);

        void onReplyClick(NotificationList notification, Context context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, dataTv, messageTV, txt_msg, testdata;
        public ImageView profileIv, gifiv;
        Button replyBtn;
        ImageView arrowIv, ivtype, arrowIvmsg;
        LinearLayout notiLin;
        private ProgressBar progressView;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            dataTv = view.findViewById(R.id.dataTv);
            messageTV = view.findViewById(R.id.messageTV);
            txt_msg = view.findViewById(R.id.txt_msg);

            replyBtn = view.findViewById(R.id.replyBtn);

            arrowIv = view.findViewById(R.id.arrowIv);
            arrowIvmsg = view.findViewById(R.id.arrowIvmsg);
            ivtype = view.findViewById(R.id.ivtype);

            profileIv = view.findViewById(R.id.profileIV);
            gifiv = view.findViewById(R.id.gifiv);
            testdata = view.findViewById(R.id.testdata);

            progressView = view.findViewById(R.id.progressView);
            notiLin = view.findViewById(R.id.notiLin);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(notificationLists.get(getAdapterPosition()), context);
                }
            });

            arrowIvmsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onReplyClick(notificationLists.get(getAdapterPosition()), context);
                }
            });
        }
    }
}