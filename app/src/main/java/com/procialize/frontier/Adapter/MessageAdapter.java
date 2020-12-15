package com.procialize.frontier.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.procialize.frontier.DbHelper.DBHelper;
import com.procialize.frontier.GetterSetter.AttendeeList;
import com.procialize.frontier.GetterSetter.Exhibitor_Notification_List;
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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<Exhibitor_Notification_List> travelLists;
    private Context context;
    private MessageAdapterListner listener;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private List<AttendeeList> attendeeDBList;

    public MessageAdapter(Context context, List<Exhibitor_Notification_List> travelList, MessageAdapterListner listener) {
        this.travelLists = travelList;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        procializeDB = new DBHelper(context);
        dbHelper = new DBHelper(context);
        db = procializeDB.getReadableDatabase();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Exhibitor_Notification_List travel = travelLists.get(position);

        holder.linTicket.setBackgroundColor(Color.parseColor(colorActive));
        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.ic_rightarrow.setImageDrawable(drawable);

        holder.nameTv.setText(travel.getAttendeeFirstName() + " " + travel.getAttendeeLastName());
        holder.nameTv.setTextColor(Color.parseColor(colorActive));


        if (travel.getProfilePic() != null) {
            SharedPreferences prefs1 = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String picPath =  prefs1.getString(KEY_ATTENDEE_PIC_PATH,"");
            Glide.with(context).load(/*ApiConstant.profilepic*/picPath + travel.getProfilePic())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.progressView.setVisibility(View.GONE);
                    holder.profileIV.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.progressView.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.profileIV).onLoadStarted(context.getDrawable(R.drawable.profilepic_placeholder));

        } else {
            holder.progressView.setVisibility(View.GONE);

        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = formatter.parse(travel.getNotificationDate());

            DateFormat originalFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.UK);

            String date = originalFormat.format(date1);

            holder.dataTv.setText(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.messageTV.setVisibility(View.VISIBLE);
        holder.messageTV.setText(StringEscapeUtils.unescapeJava(travel.getNotificationContent()));

        holder.ic_rightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendeeDBList = dbHelper.getAttendeeDetailsId(travel.getAttendeeId());
                if (attendeeDBList.size() > 0) {

                    Intent attendeetail = new Intent(context, AttendeeDetailActivity.class);
                    attendeetail.putExtra("id", travel.getAttendeeId());
                    attendeetail.putExtra("name", travel.getAttendeeFirstName() + " " + travel.getAttendeeLastName());
                    attendeetail.putExtra("city", attendeeDBList.get(0).getCity());
                    attendeetail.putExtra("country", attendeeDBList.get(0).getCountry());
                    attendeetail.putExtra("company", travel.getCompanyName());
                    attendeetail.putExtra("designation", travel.getDesignation());
                    attendeetail.putExtra("description", attendeeDBList.get(0).getDescription());
                    attendeetail.putExtra("profile", travel.getProfilePic());
                    attendeetail.putExtra("buddy_status", attendeeDBList.get(0).getBuddy_status());
                    context.startActivity(attendeetail);
                } else {
                    Intent attendeetail = new Intent(context, AttendeeDetailActivity.class);
                    attendeetail.putExtra("id", travel.getAttendeeId());
                    attendeetail.putExtra("name", travel.getAttendeeFirstName() + " " + travel.getAttendeeLastName());
                    attendeetail.putExtra("city", "");
                    attendeetail.putExtra("country", "");
                    attendeetail.putExtra("company", travel.getCompanyName());
                    attendeetail.putExtra("designation", travel.getDesignation());
                    attendeetail.putExtra("description", "");
                    attendeetail.putExtra("profile", travel.getProfilePic());

                    context.startActivity(attendeetail);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return travelLists.size();
    }

    public interface MessageAdapterListner {
        void onContactSelected(Exhibitor_Notification_List travel);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, txt_msg, dataTv, messageTV;
        ImageView ic_rightarrow, imgTvel, profileIV;
        LinearLayout linTicket;
        ProgressBar progressView;


        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            ic_rightarrow = view.findViewById(R.id.ic_rightarrow);
            progressView = view.findViewById(R.id.progressView);
            linTicket = view.findViewById(R.id.notiLin);
            profileIV = view.findViewById(R.id.profileIV);
            dataTv = view.findViewById(R.id.dataTv);
            messageTV = view.findViewById(R.id.messageTV);

            imgTvel = view.findViewById(R.id.ic_rightarrow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(travelLists.get(getAdapterPosition()));
                }
            });
        }
    }
}
