package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.procialize.mrgeApp20.Adapter.SponsorAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.EventInfoFetch;
import com.procialize.mrgeApp20.GetterSetter.EventList;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.SponsorsList;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.KeyboardUtility;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.Utility.Utility;
import com.procialize.mrgeApp20.util.GetUserActivityReport;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_EVENT_LOGO_PATH;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;

public class EventInfoActivity extends AppCompatActivity implements OnMapReadyCallback {


    ImageView logoIv;
    TextView nameTv, dateTv, cityTv, event_desc;
    View view;
    LatLng position;
    MarkerOptions options;
    SessionManager sessionManager;
    String token;
    ProgressBar progressbar;
    String event_info_display_map = "0", event_info_description = "0";
    List<EventSettingList> eventSettingLists;
    SupportMapFragment fm;
    ImageView back;
    LinearLayout linMap;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    RelativeLayout relative_head;
    List<SponsorsList> sponsorList;
    String filePath,eventLogoPath;
    LinearLayout linShare;
    RecyclerView rv_sponsors;
    private APIService mAPIService;
    private GoogleMap map;
    private Date d2, d1;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private DBHelper dbHelper;
    private List<EventList> eventList;
    private List<EventList> eventDBList;
    private List<SponsorsList> sponsorDBList;
    Boolean isVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventinfo_for_logo);
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        Intent intent=getIntent();
//        eventid=intent.getStringExtra("eventId");
//        eventnamestr=intent.getStringExtra("eventnamestr");
        cd = new ConnectionDetector(EventInfoActivity.this);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.activetab), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();
                KeyboardUtility.hideSoftKeyboard(EventInfoActivity.this);

                finish();
            }
        });

        ImageView notificationlogoIv = findViewById(R.id.notificationlogoIv);
        notificationlogoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(main);
                //finish();
            }
        });


        SharedPreferences prefs = EventInfoActivity.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        relative_head = findViewById(R.id.relative_head);
        sessionManager = new SessionManager(this);

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Event Info", token);
        eventSettingLists = sessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

        try {
            setNotification(EventInfoActivity.this, MrgeHomeActivity.tv_notification, MrgeHomeActivity.ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cd = new ConnectionDetector(this);
        mAPIService = ApiUtils.getAPIService();
        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);
        logoIv = findViewById(R.id.logoIv);
        nameTv = findViewById(R.id.nameTv);
        dateTv = findViewById(R.id.dateTv);
        cityTv = findViewById(R.id.cityTv);
        rv_sponsors = findViewById(R.id.rv_sponsors);
        linShare = findViewById(R.id.linShare);

        linShare.setBackgroundColor(Color.parseColor(colorActive));
        linShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, nameTv.getText().toString());
                share.putExtra(Intent.EXTRA_TEXT, "I am attending " + nameTv.getText().toString() + " using the MRGE app. Check out mrge.in for more");//event_desc.getText().toString());

                startActivity(Intent.createChooser(share, "Share Event Info!"));

            }
        });

        event_desc = findViewById(R.id.event_desc);
        view = findViewById(R.id.view);
        progressbar = findViewById(R.id.progressbar);
        back = findViewById(R.id.back);
        linMap = findViewById(R.id.linMap);
        db = dbHelper.getWritableDatabase();
        // fm = ( SupportMapFragment ) EventInfoActivity.this.getSupportFragmentManager().findFragmentById(R.id.map);
        fm = ( SupportMapFragment ) getSupportFragmentManager().findFragmentById(R.id.map);


        fm.getMapAsync(this);


        TextView header = (TextView) findViewById(R.id.event_info_heading);
        header.setTextColor(Color.parseColor(colorActive));
        //  nameTv.setTextColor(Color.parseColor(colorActive));


        RelativeLayout layoutTop = (RelativeLayout) findViewById(R.id.layoutTop);
        // layoutTop.setBackgroundColor(Color.parseColor(colorActive));


        //if (event_info_display_map.equalsIgnoreCase("1")) {
        event_desc.setMovementMethod(new ScrollingMovementMethod());
        //event_desc.setMaxLines(20);
        event_desc.setVerticalScrollBarEnabled(true);
       /* } else {

        }*/

        if (cd.isConnectingToInternet()) {
            fetchEventInfo(token, eventid);
        }

        db = dbHelper.getReadableDatabase();
        sponsorDBList = dbHelper.getSponsorList();
        if (!cd.isConnectingToInternet()) {
            if (sponsorDBList.size() != 0) {
                String sponsor_filePath = prefs.getString("sponsor_filePath", "");
                SponsorAdapter sponsorAdapter1 = new SponsorAdapter(EventInfoActivity.this, sponsorDBList, sponsor_filePath);
                RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(EventInfoActivity.this, 3);
                rv_sponsors.setNestedScrollingEnabled(false);
                rv_sponsors.setLayoutManager(mLayoutManager1);
                rv_sponsors.setItemAnimator(new DefaultItemAnimator());
                rv_sponsors.setAdapter(sponsorAdapter1);
            }
        }
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(EventInfoActivity.this, token,
                eventid,
                ApiConstant.pageVisited,
                "48",
                "");
        getUserActivityReport.userActivityReport();
    }


    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

           /* if (eventSettingLists.get(i).getFieldName().equals("event_info_display_map")) {
                event_info_display_map = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("event_info_description")) {
                event_info_description = eventSettingLists.get(i).getFieldValue();
            }
*/
            if (eventSettingLists.get(i).getFieldName().equals("event_details")) {
                if (eventSettingLists.get(i).getSub_menuList() != null) {
                    if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                        for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                            if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("event_info_display_map")) {
                                event_info_display_map = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("event_info_description")) {
                                event_info_description = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }

                        }
                    }
                }
            }
        }
    }


    public void fetchEventInfo(String token, String eventid) {
        // showProgress();
        mAPIService.EventInfoFetch(token, eventid).enqueue(new Callback<EventInfoFetch>() {
            @Override
            public void onResponse(Call<EventInfoFetch> call, Response<EventInfoFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    //  dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(EventInfoActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventInfoFetch> call, Throwable t) {
                //dismissProgress();
                Toast.makeText(EventInfoActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showResponse(final Response<EventInfoFetch> response) {

        // specify an adapter (see also next example)

        if (response.body().getEventList().isEmpty()) {

            sponsorList = response.body().getSponsor_list();
            filePath = response.body().getSponsor_file_path();
            eventLogoPath = response.body().getEvent_logo_url_path();





            SharedPreferences prefs = EventInfoActivity.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("sponsor_filePath", filePath);
            editor.putString(KEY_EVENT_LOGO_PATH, eventLogoPath);
            editor.commit();

            SponsorAdapter sponsorAdapter = new SponsorAdapter(EventInfoActivity.this, sponsorList, filePath);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(EventInfoActivity.this, 3);
            rv_sponsors.setLayoutManager(mLayoutManager);
            rv_sponsors.setItemAnimator(new DefaultItemAnimator());
            rv_sponsors.setAdapter(sponsorAdapter);

            db = dbHelper.getReadableDatabase();
            eventDBList = dbHelper.getEventListDetail();
            sponsorDBList = dbHelper.getSponsorList();
            if (eventDBList.size() != 0) {
                String startTime = "", endTime = "";
                SimpleDateFormat sdf = new SimpleDateFormat(ApiConstant.dateformat + " HH:mm");
                String currentDateandTime = sdf.format(new Date());
                try {
                    if (eventDBList.get(0).getEventStart().equals("null") && eventDBList.get(0).getEventStart() != null && !eventDBList.get(0).getEventStart().isEmpty()) {
                        startTime = currentDateandTime;
                    } else {
                        startTime = eventDBList.get(0).getEventStart();
                    }

                    if (eventDBList.get(0).getEventEnd().equals("null") && eventDBList.get(0).getEventEnd() != null && eventDBList.get(0).getEventEnd().isEmpty()) {
                        endTime = currentDateandTime;
                    } else {
                        endTime = eventDBList.get(0).getEventEnd();
                    }
                } catch (Exception e) {
                    startTime = currentDateandTime;
                    endTime = currentDateandTime;
                }

                // SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

                SimpleDateFormat formatter = null;

                String formate1 = ApiConstant.dateformat;
                String formate2 = ApiConstant.dateformat1;

                if (Utility.isValidFormat(formate1, startTime, Locale.UK)) {
                    formatter = new SimpleDateFormat(ApiConstant.dateformat);
                } else if (Utility.isValidFormat(formate2, startTime, Locale.UK)) {
                    formatter = new SimpleDateFormat(ApiConstant.dateformat1);
                }
                try {
                    d1 = formatter.parse(startTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long millisecondsStart = d1.getTime();

                try {
                    d2 = formatter.parse(endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long millisecondsEnd = d2.getTime();

                SimpleDateFormat formatter1 = new SimpleDateFormat("dd MMM yyyy");

                String finalStartTime = formatter1.format(new Date(millisecondsStart));
                String finalEndTime = formatter1.format(new Date(millisecondsEnd));

                try {
                    nameTv.setText(eventDBList.get(0).getEventName());
                    if (finalStartTime.equalsIgnoreCase(finalEndTime)) {
                        dateTv.setText(finalStartTime);

                    } else {
                        dateTv.setText(finalStartTime + " - " + finalEndTime);
                    }
                    cityTv.setText(eventDBList.get(0).getEventCity());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {

                    event_desc.setText(eventDBList.get(0).getEventLocation() + "\n\n" + eventDBList.get(0).getEventDescription());
                    //event_desc.setText(eventDBList.get(0).getEventDescription());
                    event_desc.setVisibility(View.VISIBLE);
                    //event_desc.setText("\n" + eventDBList.get(0).getEventDescription());

                    SharedPreferences prefs1 = EventInfoActivity.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String logoImg = prefs1.getString("logoImg", "");
                    String eventLogoPath = prefs1.getString(KEY_EVENT_LOGO_PATH, "");

                    String image_final_url = /*ApiConstant.imgURL + "uploads/app_logo/"*/eventLogoPath +logoImg;

                    Glide.with(this).load(image_final_url)
                            .placeholder(R.drawable.profilepic_placeholder)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).circleCrop().centerCrop()
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    logoIv.setImageResource(R.drawable.app_icon);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    logoIv.setImageResource(R.drawable.app_icon);
                                    return false;
                                }
                            }).into(logoIv);

                   /* Glide.with(this).load(image_final_url)
                            .apply(RequestOptions.skipMemoryCacheOf(true)).circleCrop()
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).circleCrop()
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    logoIv.setImageResource(R.drawable.app_icon);
                                    return true;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            }).into(logoIv);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }

                linMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                   /* String label = "ABC Label";
                    String uriBegin = "geo:" + response.body().getEventList().get(0).getEventLatitude() + "," + response.body().getEventList().get(0).getEventLongitude();
                    String query = response.body().getEventList().get(0).getEventLatitude() + "," + response.body().getEventList().get(0).getEventLatitude() + "(" + label + ")";
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    startActivity(intent);*/
                        String label = eventDBList.get(0).getEventName();
                        String strUri = "http://maps.google.com/maps?q=loc:" + eventDBList.get(0).getEventLatitude() + "," + eventDBList.get(0).getEventLongitude() + " (" + label + ")";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));

                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                        startActivity(intent);


                    }
                });

/*
                try {
                    if (map != null && event_info_display_map.equalsIgnoreCase("1")) {

                        fm.getView().setVisibility(View.VISIBLE);
                        position = new LatLng(Double.parseDouble(eventDBList.get(0).getEventLatitude()), Double.parseDouble(eventDBList.get(0).getEventLongitude()));

                        CameraUpdate updatePosition1 = CameraUpdateFactory.newLatLng(position);

                        map.moveCamera(updatePosition1);

                        // Instantiating MarkerOptions class
                        options = new MarkerOptions();

                        // Setting position for the MarkerOptions
                        options.position(position);

                        // Setting title for the MarkerOptions


                        // Setting snippet for the MarkerOptions
                        options.snippet(eventDBList.get(0).getEventLocation());


                        // Adding Marker on the Google Map
                        map.addMarker(options);


                        moveToCurrentLocation(position);
                    } else {
                        fm.getView().setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                }
*/


            } else {
                /*setContentView(R.layout.activity_empty_view);
                ImageView imageView = findViewById(R.id.back);
                TextView text_empty = findViewById(R.id.text_empty);
                final ImageView headerlogoIv1 = findViewById(R.id.headerlogoIv);
                Util.logomethod(this, headerlogoIv1);
                text_empty.setText("No Data Found");
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });*/
            }
            if (sponsorDBList.size() != 0) {
                SponsorAdapter sponsorAdapter1 = new SponsorAdapter(EventInfoActivity.this, sponsorDBList, filePath);
                RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(EventInfoActivity.this, 3);
                rv_sponsors.setNestedScrollingEnabled(false);
                rv_sponsors.setLayoutManager(mLayoutManager1);
                rv_sponsors.setItemAnimator(new DefaultItemAnimator());
                rv_sponsors.setAdapter(sponsorAdapter1);
            }
        } else {
            if (response.body().getStatus().equalsIgnoreCase("success")) {
                eventList = response.body().getEventList();
                dbHelper.clearEventListTable();
                dbHelper.clearSponsorTable();
                dbHelper.insertEventInfo(eventList, db);

                String startTime = "", endTime = "";
                SimpleDateFormat sdf = new SimpleDateFormat(ApiConstant.dateformat + " HH:mm");
                String currentDateandTime = sdf.format(new Date());
                try {
                    if (response.body().getEventList().get(0).getEventStart().equals("null") && response.body().getEventList().get(0).getEventStart() != null && !response.body().getEventList().get(0).getEventStart().isEmpty()) {
                        startTime = currentDateandTime;
                    } else {
                        startTime = response.body().getEventList().get(0).getEventStart();
                    }

                    if (response.body().getEventList().get(0).getEventEnd().equals("null") && response.body().getEventList().get(0).getEventEnd() != null && response.body().getEventList().get(0).getEventEnd().isEmpty()) {
                        endTime = currentDateandTime;
                    } else {
                        endTime = response.body().getEventList().get(0).getEventEnd();
                    }
                } catch (Exception e) {
                    startTime = currentDateandTime;
                    endTime = currentDateandTime;
                }

                sponsorList = response.body().getSponsor_list();

                dbHelper.insertSponsorInfo(sponsorList, db);
                filePath = response.body().getSponsor_file_path();


                SharedPreferences prefs = EventInfoActivity.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("sponsor_filePath", filePath);
                editor.putString(KEY_EVENT_LOGO_PATH, eventLogoPath);
                editor.commit();

                SponsorAdapter sponsorAdapter = new SponsorAdapter(EventInfoActivity.this, sponsorList, filePath);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(EventInfoActivity.this, 3);
                rv_sponsors.setNestedScrollingEnabled(false);
                rv_sponsors.setLayoutManager(mLayoutManager);
                rv_sponsors.setItemAnimator(new DefaultItemAnimator());
                rv_sponsors.setAdapter(sponsorAdapter);

                // SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

                SimpleDateFormat formatter = null;

                String formate1 = ApiConstant.dateformat;
                String formate2 = ApiConstant.dateformat1;

                if (Utility.isValidFormat(formate1, startTime, Locale.UK)) {
                    formatter = new SimpleDateFormat(ApiConstant.dateformat);
                } else if (Utility.isValidFormat(formate2, startTime, Locale.UK)) {
                    formatter = new SimpleDateFormat(ApiConstant.dateformat1);
                }
                try {
                    d1 = formatter.parse(startTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long millisecondsStart = d1.getTime();

                try {
                    d2 = formatter.parse(endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long millisecondsEnd = d2.getTime();

                SimpleDateFormat formatter1 = new SimpleDateFormat("dd MMM yyyy");

                String finalStartTime = formatter1.format(new Date(millisecondsStart));
                String finalEndTime = formatter1.format(new Date(millisecondsEnd));

                try {
                    nameTv.setText(response.body().getEventList().get(0).getEventName());
                    if (finalStartTime.equalsIgnoreCase(finalEndTime)) {
                        dateTv.setText(finalStartTime);

                    } else {
                        dateTv.setText(finalStartTime + " - " + finalEndTime);
                    }
                    cityTv.setText(response.body().getEventList().get(0).getEventCity());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    if (event_info_description.equalsIgnoreCase("1") && response.body().getEventList().get(0).getEventDescription() != null) {

                        event_desc.setVisibility(View.VISIBLE);
//                        eventvenu.setVisibility(View.VISIBLE);
                        // view.setVisibility(View.VISIBLE);

                    } else {
                        event_desc.setVisibility(View.GONE);
//                        eventvenu.setVisibility(View.GONE);
                        //   view.setVisibility(View.GONE);
                    }


                    SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String logoImg = prefs1.getString("logoImg", "");
                    String eventLogoPath = prefs1.getString(KEY_EVENT_LOGO_PATH, "");

                    event_desc.setText(StringEscapeUtils.unescapeJava(response.body().getEventList().get(0).getEventLocation() + "\n\n" + response.body().getEventList().get(0).getEventDescription()));
                    // event_desc.setText(response.body().getEventList().get(0).getEventDescription());
                    String image_final_url = /*ApiConstant.imgURL + "uploads/app_logo/"*/eventLogoPath + response.body().getEventList().get(0).getLogo();

//                Glide.with(getApplicationContext()).load(image_final_url).into(logoIv).onLoadStarted(getDrawable(R.drawable.logo));
                    Glide.with(this).load(image_final_url)
                            .placeholder(R.drawable.profilepic_placeholder)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).circleCrop().centerCrop()
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    logoIv.setImageResource(R.drawable.app_icon);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    logoIv.setImageResource(R.drawable.app_icon);
                                    return false;
                                }
                            }).into(logoIv);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                linMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String label = response.body().getEventList().get(0).getEventName();
                        String strUri = "http://maps.google.com/maps?q=loc:" + response.body().getEventList().get(0).getEventLatitude() + "," + response.body().getEventList().get(0).getEventLongitude() + " (" + label + ")";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));

                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                        startActivity(intent);


                    }
                });


                try {
                    if (map != null && event_info_display_map.equalsIgnoreCase("1")) {

                        fm.getView().setVisibility(View.VISIBLE);
                        position = new LatLng(Double.parseDouble(response.body().getEventList().get(0).getEventLatitude()), Double.parseDouble(response.body().getEventList().get(0).getEventLongitude()));

                        CameraUpdate updatePosition1 = CameraUpdateFactory.newLatLng(position);

                        map.moveCamera(updatePosition1);

                        // Instantiating MarkerOptions class
                        options = new MarkerOptions();

                        // Setting position for the MarkerOptions
                        options.position(position);

                        // Setting title for the MarkerOptions


                        // Setting snippet for the MarkerOptions
                        options.snippet(response.body().getEventList().get(0).getEventLocation());


                        // Adding Marker on the Google Map
                        map.addMarker(options);


                        moveToCurrentLocation(position);
                    } else {
                        fm.getView().setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                }
            }

        }
    }


    private void moveToCurrentLocation(LatLng currentLocation) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        if (cd.isConnectingToInternet()) {
            fetchEventInfo(token, eventid);
        } else {
            db = dbHelper.getReadableDatabase();

            eventDBList = dbHelper.getEventListDetail();


            if (eventDBList.size() != 0) {
                String startTime = "", endTime = "";
                SimpleDateFormat sdf = new SimpleDateFormat(ApiConstant.dateformat + " HH:mm");
                String currentDateandTime = sdf.format(new Date());
                try {
                    if (eventDBList.get(0).getEventStart().equals("null") && eventDBList.get(0).getEventStart() != null && !eventDBList.get(0).getEventStart().isEmpty()) {
                        startTime = currentDateandTime;
                    } else {
                        startTime = eventDBList.get(0).getEventStart();
                    }

                    if (eventDBList.get(0).getEventEnd().equals("null") && eventDBList.get(0).getEventEnd() != null && eventDBList.get(0).getEventEnd().isEmpty()) {
                        endTime = currentDateandTime;
                    } else {
                        endTime = eventDBList.get(0).getEventEnd();
                    }
                } catch (Exception e) {
                    startTime = currentDateandTime;
                    endTime = currentDateandTime;
                }


                // SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

                SimpleDateFormat formatter = null;

                String formate1 = ApiConstant.dateformat;
                String formate2 = ApiConstant.dateformat1;

                if (Utility.isValidFormat(formate1, startTime, Locale.UK)) {
                    formatter = new SimpleDateFormat(ApiConstant.dateformat);
                } else if (Utility.isValidFormat(formate2, startTime, Locale.UK)) {
                    formatter = new SimpleDateFormat(ApiConstant.dateformat1);
                }
                try {
                    d1 = formatter.parse(startTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long millisecondsStart = d1.getTime();

                try {
                    d2 = formatter.parse(endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long millisecondsEnd = d2.getTime();

                SimpleDateFormat formatter1 = new SimpleDateFormat("dd MMM yyyy");

                String finalStartTime = formatter1.format(new Date(millisecondsStart));
                String finalEndTime = formatter1.format(new Date(millisecondsEnd));

                try {
                    nameTv.setText(eventDBList.get(0).getEventName());
                    if (finalStartTime.equalsIgnoreCase(finalEndTime)) {
                        dateTv.setText(finalStartTime);

                    } else {
                        dateTv.setText(finalStartTime + " - " + finalEndTime);
                    }
                    cityTv.setText(eventDBList.get(0).getEventCity());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    if (event_info_description.equalsIgnoreCase("1") && eventDBList.get(0).getEventDescription() != null) {

                        event_desc.setVisibility(View.VISIBLE);
//                        eventvenu.setVisibility(View.VISIBLE);
//                        view.setVisibility(View.VISIBLE);

                    } else {
                        // event_desc.setVisibility(View.GONE);
//                        eventvenu.setVisibility(View.GONE);
                        //    view.setVisibility(View.GONE);
                    }

                    String eventDescription = eventDBList.get(0).getEventDescription();
                    event_desc.setText(eventDBList.get(0).getEventLocation() + "\n\n" + eventDBList.get(0).getEventDescription());
                    // event_desc.setText(eventDescription);
                    SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String logoImg = prefs1.getString("logoImg", "");
                    String eventLogoPath = prefs1.getString(KEY_EVENT_LOGO_PATH, "");
                    String image_final_url = /*ApiConstant.imgURL + "uploads/app_logo/"*/eventLogoPath + eventDBList.get(0).getLogo();

//                Glide.with(getApplicationContext()).load(image_final_url).into(logoIv).onLoadStarted(getDrawable(R.drawable.logo));
                    Glide.with(this).load(image_final_url)
                            .placeholder(R.drawable.profilepic_placeholder)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).circleCrop().centerCrop()
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    logoIv.setImageResource(R.drawable.app_icon);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    logoIv.setImageResource(R.drawable.app_icon);
                                    return false;
                                }
                            }).into(logoIv);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                linMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                   /* String label = "ABC Label";
                    String uriBegin = "geo:" + response.body().getEventList().get(0).getEventLatitude() + "," + response.body().getEventList().get(0).getEventLongitude();
                    String query = response.body().getEventList().get(0).getEventLatitude() + "," + response.body().getEventList().get(0).getEventLatitude() + "(" + label + ")";
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    startActivity(intent);*/
                        String label = eventDBList.get(0).getEventName();
                        String strUri = "http://maps.google.com/maps?q=loc:" + eventDBList.get(0).getEventLatitude() + "," + eventDBList.get(0).getEventLongitude() + " (" + label + ")";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));

                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                        startActivity(intent);


                    }
                });


                try {
                    if (map != null && event_info_display_map.equalsIgnoreCase("1")) {

                        fm.getView().setVisibility(View.VISIBLE);
                        position = new LatLng(Double.parseDouble(eventDBList.get(0).getEventLatitude()), Double.parseDouble(eventDBList.get(0).getEventLongitude()));

                        CameraUpdate updatePosition1 = CameraUpdateFactory.newLatLng(position);

                        map.moveCamera(updatePosition1);

                        // Instantiating MarkerOptions class
                        options = new MarkerOptions();

                        // Setting position for the MarkerOptions
                        options.position(position);

                        // Setting title for the MarkerOptions


                        // Setting snippet for the MarkerOptions
                        options.snippet(eventDBList.get(0).getEventLocation());


                        // Adding Marker on the Google Map
                        map.addMarker(options);


                        moveToCurrentLocation(position);
                    } else {
                        fm.getView().setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                }


            } else {

               /* setContentView(R.layout.activity_empty_view);
                ImageView imageView = findViewById(R.id.back);
                TextView text_empty = findViewById(R.id.text_empty);
                final ImageView headerlogoIv1 = findViewById(R.id.headerlogoIv);
                Util.logomethod(this, headerlogoIv1);
                text_empty.setText("No Data Found");
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });*/
            }


        }


    }

    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
    }

    public void dismissProgress() {
        if (progressbar.getVisibility() == View.VISIBLE) {
            progressbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        //   overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        isVisible = true;
        JzvdStd.releaseAllVideos();

    }

}
