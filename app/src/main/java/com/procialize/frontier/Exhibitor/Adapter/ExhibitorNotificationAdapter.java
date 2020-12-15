package com.procialize.frontier.Exhibitor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import com.procialize.frontier.Activity.AttendeeDetailActivity;
import com.procialize.frontier.NewsFeed.Views.Activity.CommentActivity;
import com.procialize.frontier.NewsFeed.Views.Activity.LikeDetailActivity;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.DbHelper.DBHelper;
import com.procialize.frontier.GetterSetter.AttendeeList;
import com.procialize.frontier.GetterSetter.NewsFeedList;
import com.procialize.frontier.GetterSetter.NotificationList;
import com.procialize.frontier.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_ATTENDEE_PIC_PATH;

public class ExhibitorNotificationAdapter extends RecyclerView.Adapter<ExhibitorNotificationAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<NotificationList> notificationLists;
    private Context context;
    private ExhibitorNotificationAdapter.ExhibitorNotificationAdapterListner listener;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private List<AttendeeList> attendeeDBList;
    private List<NewsFeedList> newsfeedsDBList;

    public ExhibitorNotificationAdapter(Context context, List<NotificationList> notificationLists, ExhibitorNotificationAdapter.ExhibitorNotificationAdapterListner listener) {
        this.notificationLists = notificationLists;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        procializeDB = new DBHelper(context);
        dbHelper = new DBHelper(context);
        db = procializeDB.getReadableDatabase();
    }

    @Override
    public ExhibitorNotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);

        return new ExhibitorNotificationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ExhibitorNotificationAdapter.MyViewHolder holder, int position) {
        final NotificationList notificationList = notificationLists.get(position);

        holder.notiLin.setBackgroundColor(Color.parseColor(colorActive));
        holder.nameTv.setTextColor(Color.parseColor(colorActive));

        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.arrowIv.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.arrowIv.setImageDrawable(drawable);

        Drawable drawable1 = DrawableCompat.wrap(holder.arrowIv.getDrawable());
        DrawableCompat.setTintList(drawable1, csl);
        holder.arrowIvmsg.setImageDrawable(drawable1);


        if (notificationList.getNotificationContent() != null) {
            if (notificationList.getNotificationContent().contains("gif")) {
                holder.messageTV.setVisibility(View.VISIBLE);
                holder.gifiv.setVisibility(View.GONE);
                holder.messageTV.setText("GIF");
//
//                    Glide.with(context).load(notificationList.getNotificationContent())
//                            .apply(RequestOptions.skipMemoryCacheOf(true))
//                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//
//                            return true;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            return false;
//                        }
//                    }).into(holder.gifiv);

            } else {
                holder.messageTV.setVisibility(View.VISIBLE);
                holder.gifiv.setVisibility(View.GONE);
                holder.messageTV.setText(StringEscapeUtils.unescapeJava(notificationList.getNotificationContent()));
            }
        }

        if (notificationList.getNotificationType().equalsIgnoreCase("Msg")) {
            holder.txt_msg.setText("Sent You Message");
            String lName = notificationList.getAttendeeLastName();
            if (lName != null) {
                holder.nameTv.setText(notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            } else {
                holder.nameTv.setText(notificationList.getAttendeeFirstName());

            }
        } else if (notificationList.getNotificationType().equalsIgnoreCase("Like")) {
            holder.txt_msg.setText("Liked Your Post");
            holder.messageTV.setVisibility(View.GONE);
            String lName = notificationList.getAttendeeLastName();
            if (lName != null) {
                holder.nameTv.setText(notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            } else {
                holder.nameTv.setText(notificationList.getAttendeeFirstName());

            }
        } else if (notificationList.getNotificationType().equalsIgnoreCase("Cmnt")) {
            holder.txt_msg.setText("commented on your post");
            String lName = notificationList.getAttendeeLastName();
            if (lName != null) {
                holder.nameTv.setText(notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            } else {
                holder.nameTv.setText(notificationList.getAttendeeFirstName());

            }
        } else {
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

            DateFormat originalFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.UK);

            String date = originalFormat.format(date1);

            holder.dataTv.setText(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (notificationList.getProfilePic() != null) {
            SharedPreferences prefs1 = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String picPath =  prefs1.getString(KEY_ATTENDEE_PIC_PATH,"");
            Glide.with(context).load(/*ApiConstant.profilepic*/picPath + notificationList.getProfilePic())
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

        if (notificationList.getNotificationType().equalsIgnoreCase("Cmnt")) {
//            holder.replyBtn.setVisibility(View.VISIBLE);
            holder.arrowIv.setVisibility(View.VISIBLE);
            holder.arrowIvmsg.setVisibility(View.GONE);
            holder.ivtype.setImageResource(R.drawable.notifycoment);
            holder.arrowIv.setVisibility(View.VISIBLE);
            holder.arrowIv.setImageResource(R.drawable.ic_rightarrow);
        } else if (notificationList.getNotificationType().equalsIgnoreCase("Like")) {
//            holder.replyBtn.setVisibility(View.VISIBLE);
            holder.arrowIv.setVisibility(View.VISIBLE);
            holder.arrowIvmsg.setVisibility(View.GONE);
            holder.ivtype.setImageResource(R.drawable.notifylike);
            holder.arrowIv.setImageResource(R.drawable.ic_rightarrow);
        } else if (notificationList.getNotificationType().equalsIgnoreCase("Msg")) {
//            holder.replyBtn.setVisibility(View.VISIBLE);
            //holder.arrowIv.setVisibility(View.VISIBLE);
            holder.arrowIvmsg.setVisibility(View.VISIBLE);
            holder.ivtype.setImageResource(R.drawable.notifymessage);
            holder.arrowIv.setVisibility(View.GONE);
            holder.arrowIvmsg.setImageResource(R.drawable.messageiv);
            holder.arrowIvmsg.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);

        } else {
//            holder.replyBtn.setVisibility(View.GONE);
            holder.arrowIv.setVisibility(View.GONE);
            holder.arrowIvmsg.setVisibility(View.GONE);
            holder.arrowIvmsg.setVisibility(View.GONE);
            holder.ivtype.setImageResource(R.drawable.notifyadmin);
            holder.arrowIv.setVisibility(View.GONE);
        }


        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsfeedsDBList = dbHelper.getNewsFeedLikeandComment(notificationList.getNotificationPostId());
                if (notificationList.getNotificationType() != null) {
                    procializeDB = new DBHelper(context);
                    db = procializeDB.getReadableDatabase();
                    dbHelper = new DBHelper(context);
                    db = procializeDB.getReadableDatabase();

                    newsfeedsDBList = dbHelper.getNewsFeedLikeandComment(notificationList.getNotificationPostId());
                    if (notificationList.getNotificationType().equalsIgnoreCase("Cmnt")) {


                        Intent comment = new Intent(context, CommentActivity.class);
                        comment.putExtra("feedid", notificationList.getNotificationPostId());


                        comment.putExtra("noti_type", "Notification");
                        try {
                            float width = Float.parseFloat(newsfeedsDBList.get(0).getWidth());
                            float height = Float.parseFloat(newsfeedsDBList.get(0).getHeight());

                            float p1 = height / width;
                            comment.putExtra("heading", newsfeedsDBList.get(0).getPostStatus());
                            comment.putExtra("company", newsfeedsDBList.get(0).getCompanyName());
                            comment.putExtra("fname", newsfeedsDBList.get(0).getFirstName());
                            comment.putExtra("lname", newsfeedsDBList.get(0).getLastName());
                            comment.putExtra("profilepic", newsfeedsDBList.get(0).getProfilePic());
                            comment.putExtra("Likes", newsfeedsDBList.get(0).getTotalLikes());
                            comment.putExtra("Comments", newsfeedsDBList.get(0).getTotalComments());
                            comment.putExtra("designation", newsfeedsDBList.get(0).getDesignation());
                            comment.putExtra("Likeflag", newsfeedsDBList.get(0).getLikeFlag());
                            comment.putExtra("date", newsfeedsDBList.get(0).getPostDate());
                            comment.putExtra("type", newsfeedsDBList.get(0).getType());

                            comment.putExtra("AspectRatio", p1);
                            if (newsfeedsDBList.get(0).getType().equalsIgnoreCase("Image")) {
                                comment.putExtra("url", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getMediaFile());
                            } else if (newsfeedsDBList.get(0).getType().equalsIgnoreCase("Video")) {
                                comment.putExtra("videourl", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getMediaFile());
                                comment.putExtra("thumbImg", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getThumbImage());
                            }
                            if (newsfeedsDBList.get(0).getType().equalsIgnoreCase("Gif")) {
                                comment.putExtra("url", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getMediaFile());
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        context.startActivity(comment);


                    } else if (notificationList.getNotificationType().equalsIgnoreCase("Msg")) {

                        attendeeDBList = dbHelper.getAttendeeDetailsId(notificationList.getAttendeeId());
                        if (attendeeDBList.size() > 0) {
                            Intent attendeetail = new Intent(context, AttendeeDetailActivity.class);
                            attendeetail.putExtra("id", notificationList.getAttendeeId());
                            attendeetail.putExtra("name", notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
                            attendeetail.putExtra("city", attendeeDBList.get(0).getCity());
                            attendeetail.putExtra("country", attendeeDBList.get(0).getCountry());
                            attendeetail.putExtra("company", notificationList.getCompanyName());
                            attendeetail.putExtra("designation", notificationList.getDesignation());
                            attendeetail.putExtra("description", attendeeDBList.get(0).getDescription());
                            attendeetail.putExtra("profile", notificationList.getProfilePic());
                            context.startActivity(attendeetail);
                        } else {
                            Intent attendeetail = new Intent(context, AttendeeDetailActivity.class);
                            attendeetail.putExtra("id", notificationList.getAttendeeId());
                            attendeetail.putExtra("name", notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
                            attendeetail.putExtra("city", "");
                            attendeetail.putExtra("country", "");
                            attendeetail.putExtra("company", notificationList.getCompanyName());
                            attendeetail.putExtra("designation", notificationList.getDesignation());
                            attendeetail.putExtra("description", "");
                            attendeetail.putExtra("profile", notificationList.getProfilePic());


//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
                            context.startActivity(attendeetail);
                        }


                    } else if (notificationList.getNotificationType().equalsIgnoreCase("Like")) {

                        Intent likedetail = new Intent(context, LikeDetailActivity.class);
                        likedetail.putExtra("feedid", notificationList.getNotificationPostId());
                        likedetail.putExtra("type", notificationList.getNotificationType());

                        likedetail.putExtra("noti_type", "Notification");
                        try {
                            float width = Float.parseFloat(newsfeedsDBList.get(0).getWidth());
                            float height = Float.parseFloat(newsfeedsDBList.get(0).getHeight());

                            float p1 = height / width;
                            likedetail.putExtra("heading", newsfeedsDBList.get(0).getPostStatus());
                            likedetail.putExtra("company", newsfeedsDBList.get(0).getCompanyName());
                            likedetail.putExtra("fname", newsfeedsDBList.get(0).getFirstName());
                            likedetail.putExtra("lname", newsfeedsDBList.get(0).getLastName());
                            likedetail.putExtra("profilepic", newsfeedsDBList.get(0).getProfilePic());
                            likedetail.putExtra("Likes", newsfeedsDBList.get(0).getTotalLikes());
                            likedetail.putExtra("Comments", newsfeedsDBList.get(0).getTotalComments());
                            likedetail.putExtra("designation", newsfeedsDBList.get(0).getDesignation());
                            likedetail.putExtra("Likeflag", newsfeedsDBList.get(0).getLikeFlag());
                            likedetail.putExtra("date", newsfeedsDBList.get(0).getPostDate());

                            likedetail.putExtra("AspectRatio", p1);
                            if (newsfeedsDBList.get(0).getType().equalsIgnoreCase("Image")) {
                                likedetail.putExtra("url", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getMediaFile());
                            } else if (newsfeedsDBList.get(0).getType().equalsIgnoreCase("Gif")) {
                                likedetail.putExtra("url", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getMediaFile());
                            } else if (newsfeedsDBList.get(0).getType().equalsIgnoreCase("Video")) {
                                likedetail.putExtra("videourl", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getMediaFile());
                                likedetail.putExtra("thumbImg", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getThumbImage());
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        context.startActivity(likedetail);

                    }
                }
            }
        });

        holder.arrowIvmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendeeDBList = dbHelper.getAttendeeDetailsId(notificationList.getAttendeeId());
                if (attendeeDBList.size() > 0) {

                    Intent attendeetail = new Intent(context, AttendeeDetailActivity.class);
                    attendeetail.putExtra("id", notificationList.getAttendeeId());
                    attendeetail.putExtra("name", notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
                    attendeetail.putExtra("city", attendeeDBList.get(0).getCity());
                    attendeetail.putExtra("country", attendeeDBList.get(0).getCountry());
                    attendeetail.putExtra("company", notificationList.getCompanyName());
                    attendeetail.putExtra("designation", notificationList.getDesignation());
                    attendeetail.putExtra("description", attendeeDBList.get(0).getDescription());
                    attendeetail.putExtra("profile", notificationList.getProfilePic());
                    attendeetail.putExtra("mobile", attendeeDBList.get(0).getMobile());


//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
                    context.startActivity(attendeetail);
                } else {
                    Intent attendeetail = new Intent(context, AttendeeDetailActivity.class);
                    attendeetail.putExtra("id", notificationList.getAttendeeId());
                    attendeetail.putExtra("name", notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
                    attendeetail.putExtra("city", "");
                    attendeetail.putExtra("country", "");
                    attendeetail.putExtra("company", notificationList.getCompanyName());
                    attendeetail.putExtra("designation", notificationList.getDesignation());
                    attendeetail.putExtra("description", "");
                    attendeetail.putExtra("profile", notificationList.getProfilePic());
                    attendeetail.putExtra("mobile", "");

//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
                    context.startActivity(attendeetail);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return notificationLists.size();
    }

    public interface ExhibitorNotificationAdapterListner {
        void onContactSelected(NotificationList notification, Context context);

        void onReplyClick(NotificationList notification, Context context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, dataTv, messageTV, txt_msg;
        public ImageView profileIv, gifiv;
        Button replyBtn;
        ImageView arrowIv, ivtype, arrowIvmsg;
        LinearLayout notiLin, mainLL;
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

            progressView = view.findViewById(R.id.progressView);
            notiLin = view.findViewById(R.id.notiLin);
            mainLL = view.findViewById(R.id.mainLL);

//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // send selected contact in callback
////                    listener.onContactSelected(notificationLists.get(getAdapterPosition()),context);
//
//
//                }
//            });
//
//            arrowIvmsg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onReplyClick(notificationLists.get(getAdapterPosition()), context);
//                }
//            });
        }
    }
}
