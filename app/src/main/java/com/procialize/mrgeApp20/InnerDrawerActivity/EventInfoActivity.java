package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.EventInfoFetch;
import com.procialize.mrgeApp20.GetterSetter.EventList;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.Utility.Utility;

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

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;

public class EventInfoActivity extends FragmentActivity implements OnMapReadyCallback {


    ImageView logoIv;
    TextView nameTv, dateTv, cityTv, event_desc;
    View view;
    LatLng position;
    MarkerOptions options;
    SessionManager sessionManager;
    String token;
    ProgressBar progressbar;
    String event_info_display_map, event_info_description;
    List<EventSettingList> eventSettingLists;
    SupportMapFragment fm;
    ImageView back;
    LinearLayout linMap;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    RelativeLayout relative_head;
    private APIService mAPIService;
    private GoogleMap map;
    private Date d2, d1;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private DBHelper dbHelper;
    private List<EventList> eventList;
    private List<EventList> eventDBList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventinfo2);
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        Toolbar toolbar = findViewById(R.id.toolbar);


        headerlogoIv = findViewById(R.id.headerlogoIv);
        relative_head = findViewById(R.id.relative_head);
        Util.logomethod(this, headerlogoIv);
        sessionManager = new SessionManager(this);

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            relative_head.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            relative_head.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
        token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Event Info", token);
        eventSettingLists = sessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }
        cd = new ConnectionDetector(EventInfoActivity.this);
        mAPIService = ApiUtils.getAPIService();
        dbHelper = new DBHelper(EventInfoActivity.this);
        sessionManager = new SessionManager(EventInfoActivity.this);

        procializeDB = new DBHelper(EventInfoActivity.this);
        db = procializeDB.getWritableDatabase();

        fm = ( SupportMapFragment ) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);

        logoIv = findViewById(R.id.logoIv);
        nameTv = findViewById(R.id.nameTv);
        dateTv = findViewById(R.id.dateTv);
        cityTv = findViewById(R.id.cityTv);

        event_desc = findViewById(R.id.event_desc);
        view = findViewById(R.id.view);
        progressbar = findViewById(R.id.progressbar);
        back = findViewById(R.id.back);
        linMap = findViewById(R.id.linMap);

        TextView header = ( TextView ) findViewById(R.id.event_info_heading);
        header.setTextColor(Color.parseColor(colorActive));
        nameTv.setTextColor(Color.parseColor(colorActive));


        RelativeLayout layoutTop = ( RelativeLayout ) findViewById(R.id.layoutTop);
        layoutTop.setBackgroundColor(Color.parseColor(colorActive));


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (event_info_display_map.equalsIgnoreCase("1")) {
            event_desc.setMovementMethod(new ScrollingMovementMethod());
            event_desc.setMaxLines(20);
            event_desc.setVerticalScrollBarEnabled(true);
        } else {

        }
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("event_info_display_map")) {
                event_info_display_map = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("event_info_description")) {
                event_info_description = eventSettingLists.get(i).getFieldValue();
            }

        }
    }


    public void fetchEventInfo(String token, String eventid) {
        showProgress();
        mAPIService.EventInfoFetch(token, eventid).enqueue(new Callback<EventInfoFetch>() {
            @Override
            public void onResponse(Call<EventInfoFetch> call, Response<EventInfoFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventInfoFetch> call, Throwable t) {
                dismissProgress();
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showResponse(final Response<EventInfoFetch> response) {

        // specify an adapter (see also next example)

        if (response.body().getEventList().isEmpty()) {
            db = procializeDB.getReadableDatabase();

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
                        view.setVisibility(View.VISIBLE);

                    } else {
                        event_desc.setVisibility(View.GONE);
//                        eventvenu.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                    }

                    event_desc.setText(eventDBList.get(0).getEventLocation() + "\n\n" + eventDBList.get(0).getEventDescription());
                    String image_final_url = ApiConstant.imgURL + "uploads/app_logo/" + eventDBList.get(0).getLogo();

//                Glide.with(getApplicationContext()).load(image_final_url).into(logoIv).onLoadStarted(getDrawable(R.drawable.logo));
                    Glide.with(getApplicationContext()).load(image_final_url)
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                            logoIv.setImageResource(R.drawable.profilepic_placeholder);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

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

                setContentView(R.layout.activity_empty_view);
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
                });
            }
        } else {
            if (response.body().getStatus().equalsIgnoreCase("success")) {
                eventList = response.body().getEventList();
                procializeDB.clearEventListTable();
                procializeDB.insertEventInfo(eventList, db);

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
                        view.setVisibility(View.VISIBLE);

                    } else {
                        event_desc.setVisibility(View.GONE);
//                        eventvenu.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                    }

                    event_desc.setText(response.body().getEventList().get(0).getEventLocation() + "\n\n" + response.body().getEventList().get(0).getEventDescription());
                    String image_final_url = ApiConstant.imgURL + "uploads/app_logo/" + response.body().getEventList().get(0).getLogo();

//                Glide.with(getApplicationContext()).load(image_final_url).into(logoIv).onLoadStarted(getDrawable(R.drawable.logo));
                    Glide.with(getApplicationContext()).load(image_final_url)
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                            logoIv.setImageResource(R.drawable.profilepic_placeholder);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            return false;
                        }
                    }).into(logoIv).onLoadStarted(this.getDrawable(R.drawable.profilepic_placeholder));
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
            db = procializeDB.getReadableDatabase();

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
                        view.setVisibility(View.VISIBLE);

                    } else {
                        event_desc.setVisibility(View.GONE);
//                        eventvenu.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                    }

                    event_desc.setText(eventDBList.get(0).getEventLocation() + "\n\n" + eventDBList.get(0).getEventDescription());
                    String image_final_url = ApiConstant.imgURL + "uploads/app_logo/" + eventDBList.get(0).getLogo();

//                Glide.with(getApplicationContext()).load(image_final_url).into(logoIv).onLoadStarted(getDrawable(R.drawable.logo));
                    Glide.with(getApplicationContext()).load(image_final_url)
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                            logoIv.setImageResource(R.drawable.profilepic_placeholder);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

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

                setContentView(R.layout.activity_empty_view);
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
                });
            }

            try {
                LinearLayout linear_head = findViewById(R.id.linear_head);
                File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
                Resources res = getResources();
                Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
                BitmapDrawable bd = new BitmapDrawable(res, bitmap);
                linear_head.setBackgroundDrawable(bd);

                Log.e("PATH", String.valueOf(mypath));
            } catch (Exception e) {
                e.printStackTrace();
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
    protected void onResume() {
        //   overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }
}
