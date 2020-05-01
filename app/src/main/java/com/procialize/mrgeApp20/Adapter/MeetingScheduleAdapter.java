package com.procialize.mrgeApp20.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.Fragments.MeetingScheduleFragment;
import com.procialize.mrgeApp20.GetterSetter.DeleteSelfie;
import com.procialize.mrgeApp20.GetterSetter.Exhibitor_Meeting_Request_List;
import com.procialize.mrgeApp20.GetterSetter.NotificationListExhibitorFetch;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MeetingScheduleAdapter extends RecyclerView.Adapter<MeetingScheduleAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive, eventid, accesstoken, exhibitor_id;
    private List<Exhibitor_Meeting_Request_List> travelLists;
    private List<Exhibitor_Meeting_Request_List> travelLists1 = new ArrayList<>();
    private Context context;
    private MeetingScheduleAdapter.MeetingScheduleAdapterListner listener;
    APIService mAPIService;
    SessionManager session;
    HashMap<String, String> user;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    MeetingScheduleAdapter messageAdapter;

    public MeetingScheduleAdapter(Context context, List<Exhibitor_Meeting_Request_List> travelList, MeetingScheduleAdapter.MeetingScheduleAdapterListner listener) {
        this.travelLists = travelList;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        eventid = prefs.getString("eventid", "");
        session = new SessionManager(context);
        user = session.getUserDetails();
        // token
        accesstoken = user.get(SessionManager.KEY_TOKEN);
        exhibitor_id = user.get(SessionManager.EXHIBITOR_ID);
        mAPIService = ApiUtils.getAPIService();
        dbHelper = new DBHelper(context);

        procializeDB = new DBHelper(context);
        db = procializeDB.getWritableDatabase();

    }

    @Override
    public MeetingScheduleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meetingitem, parent, false);


        return new MeetingScheduleAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MeetingScheduleAdapter.MyViewHolder holder, final int position) {
        final Exhibitor_Meeting_Request_List travel = travelLists.get(position);


        holder.nameTv.setText(travel.getFirst_name() + " " + travel.getLast_name());
        holder.nameTv.setTextColor(Color.parseColor(colorActive));


        if (travel.getProfile_pic() != null) {

            Glide.with(context).load(ApiConstant.profilepic + travel.getProfile_pic())
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
            Date date1 = formatter.parse(travel.getMeeting_date_time());

            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");

            String date = originalFormat.format(date1);

            DateFormat originalFormat1 = new SimpleDateFormat("HH:mm aa", Locale.UK);

            String date2 = originalFormat1.format(date1);

            holder.txt_date.setText(date);
            holder.txt_time.setText(date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formattercr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = formattercr.parse(travel.getCreated());

            DateFormat originalFormat = new SimpleDateFormat("dd MMM HH:mm");

            String date = originalFormat.format(date1);


            holder.created_date.setText(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.message.setText(StringEscapeUtils.unescapeJava(travel.getDescription()));

        if (travel.getStatus().equalsIgnoreCase("0")) {
            holder.btn_accept.setVisibility(View.VISIBLE);
            holder.btn_reject.setVisibility(View.VISIBLE);
            holder.view1.setVisibility(View.VISIBLE);
            holder.txt_status.setVisibility(View.GONE);
        }
        if (travel.getStatus().equalsIgnoreCase("1")) {
            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_reject.setVisibility(View.GONE);
            holder.view1.setVisibility(View.GONE);
            holder.txt_status.setVisibility(View.VISIBLE);
            holder.txt_status.setText("Accepted");
        }
        if (travel.getStatus().equalsIgnoreCase("2")) {
            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_reject.setVisibility(View.GONE);
            holder.view1.setVisibility(View.GONE);
            holder.txt_status.setVisibility(View.VISIBLE);
            holder.txt_status.setText("Rejected");
        }

        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApproveRejectMeetingRequest(eventid, accesstoken, travel.getId(), exhibitor_id, "1", position);

                holder.btn_accept.setVisibility(View.GONE);
                holder.btn_reject.setVisibility(View.GONE);
                holder.view1.setVisibility(View.GONE);
                holder.txt_status.setVisibility(View.VISIBLE);
                holder.txt_status.setText("Accepted");

            }
        });

        holder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApproveRejectMeetingRequest(eventid, accesstoken, travel.getId(), exhibitor_id, "2", position);

                holder.btn_accept.setVisibility(View.GONE);
                holder.btn_reject.setVisibility(View.GONE);
                holder.view1.setVisibility(View.GONE);
                holder.txt_status.setVisibility(View.VISIBLE);
                holder.txt_status.setText("Rejected");
            }
        });

    }

    @Override
    public int getItemCount() {
        return travelLists.size();
    }

    public interface MeetingScheduleAdapterListner {
        void onContactSelected(Exhibitor_Meeting_Request_List travel);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, designationTv, txt_date, txt_time, message, txt_status, created_date;
        ImageView profileIV;
        ProgressBar progressView;
        Button btn_accept, btn_reject;
        View view1;


        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            progressView = view.findViewById(R.id.progressView);
            profileIV = view.findViewById(R.id.profileIV);
            txt_date = view.findViewById(R.id.txt_date);
            txt_time = view.findViewById(R.id.txt_time);
            message = view.findViewById(R.id.message);
            designationTv = view.findViewById(R.id.designationTv);
            view1 = view.findViewById(R.id.view);
            txt_status = view.findViewById(R.id.txt_status);
            created_date = view.findViewById(R.id.created_date);

            btn_accept = view.findViewById(R.id.btn_accept);
            btn_reject = view.findViewById(R.id.btn_reject);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(travelLists.get(getAdapterPosition()));
                }
            });
        }
    }


    public void ApproveRejectMeetingRequest(final String eventid, String token, String meeting_id, final String exhibitor_id, String meeting_status, final int position) {

        mAPIService.ApproveRejectMeetingRequest(token, eventid, meeting_id, exhibitor_id, meeting_status).enqueue(new Callback<DeleteSelfie>() {
            @Override
            public void onResponse(Call<DeleteSelfie> call, Response<DeleteSelfie> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    NotificationListExhibitorFetch(eventid, accesstoken, exhibitor_id, position);

                } else {
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteSelfie> call, Throwable t) {

            }
        });
    }

    public void NotificationListExhibitorFetch(String eventid, String token, String exhibitor_id, final int position) {

        mAPIService.NotificationListExhibitorFetch(token, eventid, exhibitor_id).enqueue(new Callback<NotificationListExhibitorFetch>() {
            @Override
            public void onResponse(Call<NotificationListExhibitorFetch> call, Response<NotificationListExhibitorFetch> response) {

                if (response.isSuccessful()) {

                    dbHelper.clearExNotificationTable();
                    dbHelper.clearEXMeetingTable();
                    dbHelper.clearNotificationTable();
                    dbHelper.insertExNotificationList(response.body().getExhibitor_notification_list(), db);
                    dbHelper.insertNotificationList(response.body().getNotification_list(), db);
                    dbHelper.insertMeetingList(response.body().getExhibitor_meeting_request_list(), db);

                    travelLists1.clear();
                    travelLists1 = dbHelper.getMeetingDetails();

                    travelLists.clear();
                    travelLists = travelLists1;

                } else {

                }
            }

            @Override
            public void onFailure(Call<NotificationListExhibitorFetch> call, Throwable t) {

            }
        });
    }

    public void update(View view) {
        messageAdapter = new MeetingScheduleAdapter(context, travelLists, new MeetingScheduleFragment());
        messageAdapter.notifyDataSetChanged();
        messageAdapter.getItemCount();

    }
}
