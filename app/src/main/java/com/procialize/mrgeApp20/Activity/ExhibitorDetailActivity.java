package com.procialize.mrgeApp20.Activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.tabs.TabLayout;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.Fragments.ContactFragment;
import com.procialize.mrgeApp20.Fragments.DetailFragment;
import com.procialize.mrgeApp20.Fragments.DocumentFragment;
import com.procialize.mrgeApp20.GetterSetter.DeleteExhibitorBrochure;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorAttendeeList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorBrochureList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorCatList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorDataList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorList;
import com.procialize.mrgeApp20.GetterSetter.QRPost;
import com.procialize.mrgeApp20.GetterSetter.SendMessagePost;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExhibitorDetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    TabLayout tabs;
    ProgressBar progressBar;
    Adapter adapter;
    TextView txt_categoryname;
    ImageView img_category, headerlogoIv;
    Button button_sendmsg, button_meeting, btn_edit;
    String exhibitor_id, exhibitor_status;
    public static String allowedMessage, address, allowedsetupMeeting, ExhiCatId, Exhi_id, ExhiLogo, ExhiName, StallNum,
            TileImage, CatName, accesstoken, eventid, colorActive;
    SharedPreferences prefs;
    LinearLayout linear;
    HashMap<String, String> user;
    String MY_PREFS_NAME = "ProcializeInfo";
    SessionManager session;
    private APIService mAPIService;
    private List<ExhibitorCatList> ExhibitorCatList = new ArrayList<>();
    private List<ExhibitorDataList> ExhibitorDataList = new ArrayList<>();
    public static List<ExhibitorAttendeeList> ExhibitorAttendeeList = new ArrayList<>();
    public static List<ExhibitorBrochureList> ExhibitorBrochureList = new ArrayList<>();
    Dialog myDialog;
    String formatdate;
    final long[] time = new long[1];
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    LinearLayout linearmain;
    TextView customTab1, customTab2, customTab3;
    String catcnt;
    String MY_PREFS_CATEGORY = "categorycnt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitor_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        SharedPreferences prefs8 = getSharedPreferences(MY_PREFS_CATEGORY, MODE_PRIVATE);
        catcnt = prefs8.getString("categorycnt", "");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();


            }
        });

        dbHelper = new DBHelper(ExhibitorDetailActivity.this);

        procializeDB = new DBHelper(ExhibitorDetailActivity.this);
        db = procializeDB.getWritableDatabase();
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        session = new SessionManager(this);
        user = session.getUserDetails();
        // token
        accesstoken = user.get(SessionManager.KEY_TOKEN);
        exhibitor_id = user.get(SessionManager.EXHIBITOR_ID);
        exhibitor_status = user.get(SessionManager.EXHIBITOR_STATUS);
        address = getIntent().getExtras().getString("address");
        allowedMessage = getIntent().getExtras().getString("allowedMessage");
        allowedsetupMeeting = getIntent().getExtras().getString("allowedsetupMeeting");
        ExhiCatId = getIntent().getExtras().getString("ExhiCatId");
        Exhi_id = getIntent().getExtras().getString("Exhi_id");
        ExhiLogo = getIntent().getExtras().getString("ExhiLogo");
        ExhiName = getIntent().getExtras().getString("ExhiName");
        StallNum = getIntent().getExtras().getString("StallNum");
        TileImage = getIntent().getExtras().getString("TileImage");
        CatName = getIntent().getExtras().getString("CatName");
        mAPIService = ApiUtils.getAPIService();

        ExhibitorAnalyticsSubmit(eventid, accesstoken, "exhibitor_detail", "0", Exhi_id);
        sendExhiList(eventid, accesstoken);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        button_sendmsg = (Button) findViewById(R.id.button_sendmsg);
        button_meeting = (Button) findViewById(R.id.button_meeting);
        txt_categoryname = (TextView) findViewById(R.id.txt_categoryname);
        img_category = (ImageView) findViewById(R.id.img_category);
        linear = (LinearLayout) findViewById(R.id.linear);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        linearmain = (LinearLayout) findViewById(R.id.linearmain);

        try {

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linearmain.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linearmain.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        button_meeting.setBackgroundColor(Color.parseColor(colorActive));
        button_sendmsg.setBackgroundColor(Color.parseColor(colorActive));

        ShapeDrawable sd = new ShapeDrawable();
        // Specify the shape of ShapeDrawable
        sd.setShape(new RectShape());
        // Specify the border color of shape
        sd.getPaint().setColor(Color.parseColor(colorActive));
        // Set the border width
        sd.getPaint().setStrokeWidth(10f);
        // Specify the style is a Stroke
        sd.getPaint().setStyle(Paint.Style.STROKE);
        // Finally, add the drawable background to TextView
        btn_edit.setBackground(sd);
        btn_edit.setTextColor(Color.parseColor(colorActive));
        txt_categoryname.setTextColor(Color.parseColor(colorActive));

        if (exhibitor_status.equalsIgnoreCase("1")) {
            if (exhibitor_id.equalsIgnoreCase(Exhi_id)) {
                btn_edit.setVisibility(View.VISIBLE);
                linear.setVisibility(View.GONE);
                button_meeting.setVisibility(View.GONE);
                button_sendmsg.setVisibility(View.GONE);
            } else {
                btn_edit.setVisibility(View.GONE);
                linear.setVisibility(View.VISIBLE);
                button_meeting.setVisibility(View.VISIBLE);
                button_sendmsg.setVisibility(View.VISIBLE);
            }
        }

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExhibitorDetailActivity.this, EditExhibitorActivity.class);
                intent.putExtra("Tileimage", ExhibitorDetailActivity.TileImage);
                intent.putExtra("Logomage", ExhibitorDetailActivity.ExhiLogo);
                intent.putExtra("exhid", ExhibitorDetailActivity.Exhi_id);
                intent.putExtra("exhname", ExhibitorDetailActivity.ExhiName);
                startActivity(intent);
                finish();
            }
        });

        if (CatName.equalsIgnoreCase("All Category")) {
            txt_categoryname.setText(ExhiName);

        } else {
            txt_categoryname.setText(CatName + "  " + ExhiName);
        }


        if (allowedMessage.equalsIgnoreCase("0")) {
            button_sendmsg.setVisibility(View.GONE);
        } else {
            button_sendmsg.setVisibility(View.VISIBLE);
        }

        if (allowedsetupMeeting.equalsIgnoreCase("0")) {
            button_meeting.setVisibility(View.GONE);
        } else {
            button_meeting.setVisibility(View.VISIBLE);
        }

        if (allowedMessage.equalsIgnoreCase("0") && allowedsetupMeeting.equalsIgnoreCase("0")) {
            linear.setVisibility(View.GONE);
            button_meeting.setVisibility(View.GONE);
            button_sendmsg.setVisibility(View.GONE);
        }

        button_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge(ExhiName);
            }
        });

        button_sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showmessagedialouge(ExhiName);
            }
        });


        if (TileImage != null) {


            Glide.with(this).load(ApiConstant.exhiilogo + TileImage)
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    // holder.progressBar.setVisibility(View.GONE);
                    img_category.setImageResource(R.drawable.tile);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    //  holder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(img_category);

        }


        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabs = (TabLayout) findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setTabMode(TabLayout.GRAVITY_CENTER);
        setupTabLayout();
        viewPager.setCurrentItem(0);
        tabs.getTabAt(0).getCustomView().setBackgroundColor(Color.parseColor(colorActive));
        customTab1.setTextColor(Color.parseColor("#ffffff"));
        customTab2.setTextColor(Color.parseColor("#000000"));
        customTab3.setTextColor(Color.parseColor("#000000"));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabs.getTabCount(); i++) {
                    if (i == position) {
                        tabs.getTabAt(i).getCustomView().setBackgroundColor(Color.parseColor(colorActive));
//                        tabs.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                    } else {
                        tabs.getTabAt(i).getCustomView().setBackgroundColor(Color.parseColor("#ffffff"));
//                        tabs.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    }
                }

                if (tabs.getSelectedTabPosition() == 0) {
                    customTab1.setTextColor(Color.parseColor("#ffffff"));
                    customTab2.setTextColor(Color.parseColor("#000000"));
                    customTab3.setTextColor(Color.parseColor("#000000"));
                } else if (tabs.getSelectedTabPosition() == 1) {
                    customTab2.setTextColor(Color.parseColor("#ffffff"));
                    customTab1.setTextColor(Color.parseColor("#000000"));
                    customTab3.setTextColor(Color.parseColor("#000000"));
                } else if (tabs.getSelectedTabPosition() == 2) {
                    customTab3.setTextColor(Color.parseColor("#ffffff"));
                    customTab1.setTextColor(Color.parseColor("#000000"));
                    customTab2.setTextColor(Color.parseColor("#000000"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }

    private void setupTabLayout() {

        customTab1 = (TextView) LayoutInflater.from(ExhibitorDetailActivity.this)
                .inflate(R.layout.custom_tab_layout, null);
        customTab2 = (TextView) LayoutInflater.from(ExhibitorDetailActivity.this)
                .inflate(R.layout.custom_tab_layout, null);
        customTab3 = (TextView) LayoutInflater.from(ExhibitorDetailActivity.this)
                .inflate(R.layout.custom_tab_layout, null);
        customTab1.setText("Details");
        tabs.getTabAt(0).setCustomView(customTab1);
        customTab2.setText("Contact");
        tabs.getTabAt(1).setCustomView(customTab2);
        customTab3.setText("Documents");
        tabs.getTabAt(2).setCustomView(customTab3);


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new DetailFragment(), "Details");
        adapter.addFragment(new ContactFragment(), "Contact");
        adapter.addFragment(new DocumentFragment(), "Documents");
        viewPager.setAdapter(adapter);
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void sendExhiList(String event_id, String token) {
        mAPIService.ExhibitorFetch(event_id, token).enqueue(new Callback<ExhibitorList>() {
            @Override
            public void onResponse(Call<ExhibitorList> call, Response<ExhibitorList> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        Intent main = new Intent(ExhibitorDetailActivity.this, LoginActivity.class);
                        startActivity(main);

                    } else {
                        showResponse(response);
                    }
                } else {
                    Toast.makeText(ExhibitorDetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<ExhibitorList> call, Throwable t) {
                Toast.makeText(ExhibitorDetailActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ExhibitorAnalyticsSubmit(String event_id, String token, String analytic_type, String target_id, String exhibitor_id) {
        mAPIService.ExhibitorAnalyticsSubmit(event_id, token, analytic_type, target_id, exhibitor_id).enqueue(new Callback<DeleteExhibitorBrochure>() {
            @Override
            public void onResponse(Call<DeleteExhibitorBrochure> call, Response<DeleteExhibitorBrochure> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        Intent main = new Intent(ExhibitorDetailActivity.this, LoginActivity.class);
                        startActivity(main);

                    } else {
//                        Toast.makeText(ExhibitorDetailActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    Toast.makeText(ExhibitorDetailActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<DeleteExhibitorBrochure> call, Throwable t) {
//                Toast.makeText(ExhibitorDetailActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showResponse(Response<ExhibitorList> response) {

        if (response.body().getStatus().equals("success")) {

            ExhibitorDataList = response.body().getExhibitorDataList();
            ExhibitorCatList = response.body().getExhibitorCatList();
            ExhibitorAttendeeList = response.body().getExhibitorAttendeeList();
            ExhibitorBrochureList = response.body().getExhibitorBrochureList();

            dbHelper.clearEXattendeeTable();
            dbHelper.clearEXbrocherTable();
            dbHelper.insertExAttendeeList(ExhibitorAttendeeList, db);
            dbHelper.insertExBrocherList(ExhibitorBrochureList, db);

        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showratedialouge(String name) {

        myDialog = new Dialog(ExhibitorDetailActivity.this);
        myDialog.setContentView(R.layout.setup_meeting);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        myDialog.setCancelable(false);
        myDialog.show();

        final ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);
        final TextView txt_date = myDialog.findViewById(R.id.txt_date);
        final TextView txt_time = myDialog.findViewById(R.id.txt_time);
        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final ImageView img_date = myDialog.findViewById(R.id.img_date);
        final ImageView img_time = myDialog.findViewById(R.id.img_time);
        final EditText etmsg = myDialog.findViewById(R.id.etmsg);
        final Button submit = myDialog.findViewById(R.id.submit);
        LinearLayout linear = myDialog.findViewById(R.id.linear);
        TextView exh_title = myDialog.findViewById(R.id.exh_title);
        final TimePickerDialog[] picker = new TimePickerDialog[1];

        linear.setBackgroundColor(Color.parseColor(colorActive));
        submit.setBackgroundColor(Color.parseColor(colorActive));
        exh_title.setTextColor(Color.parseColor(colorActive));
        exh_title.setText(name);

        img_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker[0] = new TimePickerDialog(ExhibitorDetailActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                txt_time.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker[0].show();
            }
        });
        img_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View dialogView = View.inflate(ExhibitorDetailActivity.this, R.layout.setupmeeting, null);
                final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(ExhibitorDetailActivity.this).create();

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {

                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
//                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth());
//                                timePicker.getCurrentHour(),
//                                timePicker.getCurrentMinute());
                        datePicker.setMinDate(new Date().getTime());
                        int selectyear = datePicker.getYear();
                        int selectmonth = datePicker.getMonth();
                        int selectday = datePicker.getDayOfMonth();
                        int selecttime = 0;
                        int selecthour = 0;
//                        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
//                            selecttime = timePicker.getMinute();
//                            selecthour = timePicker.getHour();
//                        } else {
//                            selecttime = timePicker.getCurrentMinute();
//                            selecthour = timePicker.getCurrentHour();
//
//                        }
                        int seconds = calendar.get(Calendar.SECOND);

                        Date mDate = new GregorianCalendar(selectyear, selectmonth, selectday, selecthour, selecttime).getTime();
                        if (mDate.getTime() <= calendar.getTimeInMillis()) {
//                            int hour = hourOfDay % 12;
                        } else {
                            Toast.makeText(dialogView.getContext(), "Invalid Date", Toast.LENGTH_LONG).show();
                        }


                        formatdate = (String) android.text.format.DateFormat.format("yyyy-MM-dd", mDate);
                        String date = (String) android.text.format.DateFormat.format("yyyy-MM-dd", mDate);
                        txt_date.setText(date);
                        time[0] = calendar.getTimeInMillis();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 250 - s.length();
                counttv.setText(count + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = txt_date.getText().toString() + " " + txt_time.getText().toString();
                if (etmsg.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter message", Toast.LENGTH_SHORT).show();
                } else if (txt_date.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Select Date", Toast.LENGTH_SHORT).show();
                } else if (txt_time.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Select Time", Toast.LENGTH_SHORT).show();
                } else {
                    SendExhibitorMeetingRequest(accesstoken, eventid, Exhi_id, date, etmsg.getText().toString());
                }
            }
        });

    }

    private void showmessagedialouge(String name) {

        myDialog = new Dialog(ExhibitorDetailActivity.this);
        myDialog.setContentView(R.layout.sendmsg);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        myDialog.setCancelable(false);
        myDialog.show();

        final ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);

        final TextView counttv = myDialog.findViewById(R.id.counttv);

        final EditText etmsg = myDialog.findViewById(R.id.etmsg);
        final Button submit = myDialog.findViewById(R.id.ratebtn);
        LinearLayout linear = myDialog.findViewById(R.id.diatitle);
//        TextView exh_title = myDialog.findViewById(R.id.exh_title);
//        final TimePickerDialog[] picker = new TimePickerDialog[1];

        linear.setBackgroundColor(Color.parseColor(colorActive));
        submit.setBackgroundColor(Color.parseColor(colorActive));
//        exh_title.setTextColor(Color.parseColor(colorActive));
//        exh_title.setText(name);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 250 - s.length();
                counttv.setText(count + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
                ExhibitorAttendeeList = dbHelper.getExAttendeeList();

                String attendee_id = ExhibitorAttendeeList.get(0).getAttendee_id();

                if (etmsg.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Message", Toast.LENGTH_SHORT).show();
                } else {
                    ExhibitorSendMessage(accesstoken, eventid, Exhi_id, StringEscapeUtils.escapeJava(etmsg.getText().toString()), attendee_id, "");
                }


            }
        });

    }

    public void SendExhibitorMeetingRequest(String accesstoken, String eventid, String Exhi_id, String date, String desc) {

        mAPIService.SendExhibitorMeetingRequest(accesstoken, eventid, Exhi_id, date, desc).enqueue(new Callback<QRPost>() {
            @Override
            public void onResponse(Call<QRPost> call, Response<QRPost> response) {

                myDialog.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QRPost> call, Throwable t) {
                myDialog.cancel();
                Log.e("hit", "Low network or no network");
                Log.e("hit", t.getMessage());
                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
//
            }
        });
    }

    public void ExhibitorSendMessage(String accesstoken, String eventid, String Exhi_id, String message_text, String target_attendee_id, String target_attendee_type) {

        mAPIService.ExhibitorSendMessage(accesstoken, eventid, Exhi_id, message_text, target_attendee_id, target_attendee_type).enqueue(new Callback<SendMessagePost>() {
            @Override
            public void onResponse(Call<SendMessagePost> call, Response<SendMessagePost> response) {

                myDialog.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendMessagePost> call, Throwable t) {
                myDialog.cancel();
                Log.e("hit", "Low network or no network");
                Log.e("hit", t.getMessage());
                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
//
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
    }
}

