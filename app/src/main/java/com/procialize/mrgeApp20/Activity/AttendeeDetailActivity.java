package com.procialize.mrgeApp20.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.AttendeeChat.AttendeeChatActivity;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchChatList;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchSendRequest;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.SendMessagePost;
import com.procialize.mrgeApp20.GetterSetter.UserData;
import com.procialize.mrgeApp20.InnerDrawerActivity.NotificationActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.util.GetUserActivityReport;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.Utility.Utility.setgradientDrawable;


public class AttendeeDetailActivity extends AppCompatActivity {

    public static final int RequestPermissionCode = 8;
    String attendeeid, city, country, company, designation,
            description, totalrating, name, profile, mobile, buddy_status, page_status;
    TextView tvname, tvcompany, tvdesignation, tvcity, tvmob, attendeetitle, tv_description;
    TextView sendbtn;
    Dialog myDialog;
    APIService mAPIService, mBuddyAPIService;
    SessionManager sessionManager;
    String apikey;
    ImageView profileIV;
    ProgressBar progressBar, progressBarmain;
    String attendee_company, attendee_location, attendee_mobile, attendee_design, attendee_savecontact = "1", attendeemsg;
    List<EventSettingList> eventSettingLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive, eventnamestr;
    UserData userData;
    String getattendee;
    EditText posttextEt;
    View viewtwo, viewthree, viewone, viewtfour, viewtfive;
    ProgressDialog progressDialog;
    LinearLayout linearsaveandsend;
    LinearLayout linMsg, linsave;
    ImageView headerlogoIv;
    TextView saveContact;
    RelativeLayout linear;
    ImageView imgBuddy;
    LinearLayout ll_notification_count;
    TextView tv_notification;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<AttendeeList> attendeeList;
    private List<AttendeeList> attendeesDBList;
    private DBHelper dbHelper;
    private RelativeLayout layoutTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_detail);
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        cd = new ConnectionDetector(this);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        eventnamestr = prefs.getString("eventnamestr", "");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dbHelper = new DBHelper(AttendeeDetailActivity.this);
        db = dbHelper.getWritableDatabase();

        db = dbHelper.getReadableDatabase();

        userData = dbHelper.getUserDetails();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // token


        mAPIService = ApiUtils.getAPIService();
        mBuddyAPIService = ApiUtils.getBuddyAPIService();
        sessionManager = new SessionManager(AttendeeDetailActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);
        getattendee = user.get(SessionManager.KEY_ID);


        eventSettingLists = SessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

        try {
            attendeeid = getIntent().getExtras().getString("id");
            name = getIntent().getExtras().getString("name");
            city = getIntent().getExtras().getString("city");
            country = getIntent().getExtras().getString("country");
            company = getIntent().getExtras().getString("company");
            designation = getIntent().getExtras().getString("designation");
            description = getIntent().getExtras().getString("description");
            totalrating = getIntent().getExtras().getString("totalrating");
            profile = getIntent().getExtras().getString("profile");
            mobile = getIntent().getExtras().getString("mobile");
            buddy_status = getIntent().getExtras().getString("buddy_status");
            // page_status = getIntent().getExtras().getString("page_status");

        } catch (Exception e) {
            e.printStackTrace();
        }


        tvname = findViewById(R.id.tvname);
        attendeetitle = findViewById(R.id.attendeetitle);
        tvcompany = findViewById(R.id.tvcompany);
        tvdesignation = findViewById(R.id.tvdesignation);
        tv_description = findViewById(R.id.tv_description);
        tvcity = findViewById(R.id.tvcity);
        profileIV = findViewById(R.id.profileIV);
        progressBar = findViewById(R.id.progressBar);
        progressBarmain = findViewById(R.id.progressBarmain);
        posttextEt = findViewById(R.id.posttextEt);
        viewtwo = findViewById(R.id.viewtwo);
        viewthree = findViewById(R.id.viewthree);
        viewone = findViewById(R.id.viewone);
        viewtfour = findViewById(R.id.viewtfour);
        viewtfive = findViewById(R.id.viewtfive);
        linearsaveandsend = findViewById(R.id.linearsaveandsend);
        saveContact = findViewById(R.id.saveContact);
        linear = findViewById(R.id.linear);
        tvmob = findViewById(R.id.tvmob);
        layoutTop = findViewById(R.id.layoutTop);
        linMsg = findViewById(R.id.linMsg);
        linsave = findViewById(R.id.linsave);
        imgBuddy = findViewById(R.id.imgBuddy);
        tv_description.setMovementMethod(new ScrollingMovementMethod());

        if (buddy_status.equalsIgnoreCase("send_request")) {
            saveContact.setText("Add to buddy list");
            imgBuddy.setVisibility(View.VISIBLE);

        } else if (buddy_status.equalsIgnoreCase("friends")) {
            saveContact.setText("Added to buddy list");
            linsave.setBackgroundColor(Color.parseColor(colorActive));
            saveContact.setBackgroundColor(Color.parseColor(colorActive));
            saveContact.setTextColor(Color.parseColor("#ffffff"));
            imgBuddy.setVisibility(View.GONE);


        } else if (buddy_status.equalsIgnoreCase("request_sent")) {
            saveContact.setText("Request sent");
            linsave.setBackgroundColor(Color.parseColor(colorActive));
            saveContact.setBackgroundColor(Color.parseColor(colorActive));
            saveContact.setTextColor(Color.parseColor("#ffffff"));
            imgBuddy.setVisibility(View.GONE);

        }


        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buddy_status.equalsIgnoreCase("send_request")) {

                    AddBuddy(apikey, eventid, attendeeid);
                } else {
                    Toast.makeText(getApplicationContext(), "Already added", Toast.LENGTH_SHORT).show();

                }

            }
        });


        // attendeetitle.setTextColor(Color.parseColor(colorActive));
        // tvname.setTextColor(Color.parseColor(colorActive));
        sendbtn = findViewById(R.id.sendMsg);

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/" + ApiConstant.folderName + "/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));

        }
        ImageView notificationlogoIv = findViewById(R.id.notificationlogoIv);
        notificationlogoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(main);
                finish();
            }
        });

        final GradientDrawable shape = setgradientDrawable(5, colorActive);
        final GradientDrawable shapelayout = setgradientDrawable(10, colorActive);
        final GradientDrawable shapeunactive = setgradientDrawable(5, "#4D4D4D");
        final GradientDrawable shapeunactivelayout = setgradientDrawable(10, "#4D4D4D");
//
        // linsave.setBackground(shapelayout);
//        linMsg.setBackground(shapelayout);
//        sendbtn.setBackground(shape);
        //  saveContact.setBackground(shape);
//        posttextEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    if (posttextEt.getText().toString().length() > 0) {
//
//                        String msg = StringEscapeUtils.escapeJava(posttextEt.getText().toString());
//                        PostMesssage(eventid, msg, apikey, attendeeid);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Enter Message", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                return false;
//            }
//        });
//        sendMsg.set
        linMsg.setEnabled(false);
        linsave.setEnabled(false);

        TextView txtcount = findViewById(R.id.txtcount);
        final TextWatcher txwatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start,
                                          int count, int after) {
                int tick = start + after;
                if (tick < 128) {
                    int remaining = 500 - tick;
                    // txtcount1.setText(String.valueOf(remaining));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                //txtcount.setText(String.valueOf(150 - s.length()) + "/");
                txtcount.setText(String.valueOf(s.length()));

                if (s.length() > 0) {
                    linMsg.setEnabled(true);
                    linMsg.setBackground(shapelayout);
                    sendbtn.setBackground(shape);
                } else {
                    linMsg.setEnabled(false);
//                    linsave.setEnabled(false);
                    linMsg.setBackground(shapeunactivelayout);
//                    linsave.setBackground(shapeunactivelayout);
                    sendbtn.setBackground(shapeunactive);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                System.out.print("Hello");
            }
        };

        posttextEt.addTextChangedListener(txwatcher);


        linMsg.setVisibility(View.GONE);
        if (attendeeid.equalsIgnoreCase(getattendee)) {
            linMsg.setVisibility(View.GONE);
            linearsaveandsend.setVisibility(View.GONE);
        } else {
            linMsg.setVisibility(View.VISIBLE);
            linearsaveandsend.setVisibility(View.VISIBLE);
        }

        // LinearLayout linCoounter = findViewById(R.id.linCoounter);
        if (attendeemsg.equalsIgnoreCase("1")) {
            linMsg.setVisibility(View.VISIBLE);
            posttextEt.setVisibility(View.VISIBLE);
            // linCoounter.setVisibility(View.VISIBLE);
        } else {
            linMsg.setVisibility(View.GONE);
            posttextEt.setVisibility(View.GONE);
            // linCoounter.setVisibility(View.GONE);

        }

        /*if (attendee_savecontact.equalsIgnoreCase("1")) {
            linsave.setVisibility(View.VISIBLE);
        } else {
            linsave.setVisibility(View.GONE);
        }*/

        linsave.setVisibility(View.VISIBLE);

        if (name.equalsIgnoreCase("N A")) {
            tvname.setVisibility(View.GONE);
        } else if (name != null) {
            tvname.setText(name);
        } else {
            tvname.setVisibility(View.GONE);
        }

        try {
            if (company.equalsIgnoreCase("N A")) {
                tvcompany.setVisibility(View.GONE);
                viewthree.setVisibility(View.GONE);
            } else if (company != null && attendee_company.equalsIgnoreCase("1")) {
                if (company.equalsIgnoreCase("")) {
                    tvcompany.setVisibility(View.GONE);
                    viewthree.setVisibility(View.GONE);
                } else if (company.equalsIgnoreCase(" ")) {
                    tvcompany.setVisibility(View.GONE);
                    viewthree.setVisibility(View.GONE);
                } else {
                    tvcompany.setText(company);
                }

            } else {
                tvcompany.setVisibility(View.GONE);
                viewthree.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvcompany.setVisibility(View.GONE);
            viewthree.setVisibility(View.GONE);
        }

        tvmob.setVisibility(View.GONE);
        viewtfour.setVisibility(View.GONE);
/*
        try {
            if (attendee_mobile.equalsIgnoreCase("N A")) {
                tvmob.setVisibility(View.GONE);
                viewtfour.setVisibility(View.GONE);
            } else if (mobile != null && attendee_mobile.equalsIgnoreCase("1")) {
                if (mobile.equalsIgnoreCase("")) {
                    tvmob.setVisibility(View.GONE);
                    viewtfour.setVisibility(View.GONE);
                } else if (mobile.equalsIgnoreCase(" ")) {
                    tvmob.setVisibility(View.GONE);
                    viewtfour.setVisibility(View.GONE);
                } else {
                    tvmob.setText(mobile);
                }


            } else {
                tvmob.setVisibility(View.GONE);
                viewtfour.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvmob.setVisibility(View.GONE);
            viewtfour.setVisibility(View.GONE);
        }
*/


        try {
            if (designation.equalsIgnoreCase("N A")) {
                tvdesignation.setVisibility(View.GONE);
                viewtwo.setVisibility(View.GONE);
                viewone.setVisibility(View.GONE);

            } else if (designation != null && attendee_design.equalsIgnoreCase("1")) {
                if (designation.equalsIgnoreCase("")) {
                    tvdesignation.setVisibility(View.GONE);
                    viewtwo.setVisibility(View.GONE);
                    viewone.setVisibility(View.GONE);
                } else if (designation.equalsIgnoreCase(" ")) {
                    tvdesignation.setVisibility(View.GONE);
                    viewtwo.setVisibility(View.GONE);
                    viewone.setVisibility(View.GONE);
                } else {
                    tvdesignation.setText(designation);
                }
            } else {
                tvdesignation.setVisibility(View.GONE);
                viewtwo.setVisibility(View.GONE);
                viewone.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            tvdesignation.setVisibility(View.GONE);
            viewone.setVisibility(View.GONE);
            e.printStackTrace();
        }

        try {
            if (city.equalsIgnoreCase("N A")) {
                tvcity.setVisibility(View.GONE);
                viewtfour.setVisibility(View.GONE);

            } else if (city != null && attendee_location.equalsIgnoreCase("1")) {
                if (city.equalsIgnoreCase("")) {
                    tvcity.setVisibility(View.GONE);
                    viewtfour.setVisibility(View.GONE);

                } else if (city.equalsIgnoreCase(" ")) {
                    tvcity.setVisibility(View.GONE);
                    viewtfour.setVisibility(View.GONE);

                } else {
                    tvcity.setText(city);
                }
            } else {
                tvcity.setVisibility(View.GONE);
                viewtfour.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            tvcity.setVisibility(View.GONE);
            viewtfour.setVisibility(View.GONE);

            e.printStackTrace();
        }

        try {
            if (!(description == null)) {
                if (description.equalsIgnoreCase("")) {
                    viewtfive.setVisibility(View.GONE);
                    tv_description.setVisibility(View.GONE);
                } else {
                    tv_description.setText(description);
                }
            } else {
                if (description.equalsIgnoreCase("")) {
                    viewtfive.setVisibility(View.GONE);
                    tv_description.setVisibility(View.GONE);
                } else {
                    tv_description.setText(description);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            viewtfive.setVisibility(View.GONE);
            tv_description.setVisibility(View.GONE);
        }


        if (profile != null) {
            Glide.with(this).load(ApiConstant.profilepic + profile).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    profileIV.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(profileIV);
        } else {
            progressBar.setVisibility(View.GONE);
        }


        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagealert();
            }
        });
        linMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    if (posttextEt.getText().toString().length() > 0) {

                        String msg = StringEscapeUtils.escapeJava(posttextEt.getText().toString());
                        linMsg.setEnabled(false);
                        linMsg.setClickable(false);
                        //PostMesssage(eventid, msg, apikey, attendeeid);
                        PostChat(eventid, apikey, attendeeid, msg);
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter Message", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AttendeeDetailActivity.this, "No internet connection..!", Toast.LENGTH_SHORT).show();
                }
            }
        });



/*
        linsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBuddy(eventid,apikey,attendeeid);

            }
        });
*/

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------
        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this, apikey,
                eventid,
                ApiConstant.pageVisited,
                "10",
                "");
        getUserActivityReport.userActivityReport();
    }

    private void PostChat(final String eventid, final String token, String budd_id, String msg) {
        mAPIService.eventLiveChat(eventid, token, budd_id, msg).enqueue(new Callback<FetchChatList>() {
            @Override
            public void onResponse(Call<FetchChatList> call, Response<FetchChatList> response) {

                if (response.isSuccessful()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    showResponsePost(response);
                    // UserChatHistory(eventid,token,attendeeid,"1");

                } else {

                    Toast.makeText(AttendeeDetailActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<FetchChatList> call, Throwable t) {
                Toast.makeText(AttendeeDetailActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showResponsePost(Response<FetchChatList> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            // page = response.body().getData_pages();

            if (!(response.body().getChatList().isEmpty())) {

                Intent attendeetail = new Intent(AttendeeDetailActivity.this, AttendeeChatActivity.class);
                attendeetail.putExtra("id", attendeeid);
                attendeetail.putExtra("name", name);
                attendeetail.putExtra("city", city);
                attendeetail.putExtra("country", country);
                attendeetail.putExtra("company", company);
                attendeetail.putExtra("designation", designation);
                attendeetail.putExtra("description", description);
                attendeetail.putExtra("profile", profile);
                attendeetail.putExtra("mobile", mobile);
                attendeetail.putExtra("buddy_status", buddy_status);

//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
                startActivity(attendeetail);
                finish();

            } else {

            }

        } else {

        }
    }


    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CONTACTS);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(AttendeeDetailActivity.this, new String[]
                {
                        WRITE_CONTACTS,
                        READ_CONTACTS
                }, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {
                    boolean readContactPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeContactpermjission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (readContactPermission && writeContactpermjission) {
//                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(AttendeeDetailActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }


    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {
            if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                    if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_company")) {
                        attendee_company = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                    } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_location")) {
                        attendee_location = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                    } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_mobile")) {
                        attendee_mobile = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                    }/*else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_save_contact")) {
                        attendee_savecontact = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                        attendee_savecontact = "1";

                    }*/ else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_designation")) {
                        attendee_design = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                    } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_message")) {
                        attendeemsg = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                    }

                }
            }

        }
    }


    private void showratedialouge() {

        myDialog = new Dialog(AttendeeDetailActivity.this);
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);

        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);

        nametv.setText("To " + name);

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


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etmsg.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    PostMesssage(eventid, msg, apikey, attendeeid);
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void AddBuddy(String token, String eventid, String messId) {
        showProgress();
        mBuddyAPIService.sendFriendRequest(token, eventid, messId).enqueue(new Callback<FetchSendRequest>() {
            @Override
            public void onResponse(Call<FetchSendRequest> call, Response<FetchSendRequest> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    dismissProgress();
                    Buddyresponse(response);
                } else {


                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchSendRequest> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();

            }
        });
    }


    private void Buddyresponse(Response<FetchSendRequest> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {
            Log.e("post", "success");
//            myDialog.dismiss();
            linsave.setBackgroundColor(Color.parseColor(colorActive));
            saveContact.setBackgroundColor(Color.parseColor(colorActive));
            saveContact.setTextColor(Color.parseColor("#ffffff"));
            saveContact.setEnabled(false);
            linsave.setEnabled(false);
            imgBuddy.setVisibility(View.GONE);

            saveContact.setText("Request sent");
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        } else {
            Log.e("post", "fail");
//            myDialog.dismiss();
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }


    public void PostMesssage(String eventid, String msg, String token, String attendeeid) {
        showProgress();
//        showProgress();
        mAPIService.SendMessagePost(token, eventid, msg, attendeeid, "").enqueue(new Callback<SendMessagePost>() {
            @Override
            public void onResponse(Call<SendMessagePost> call, Response<SendMessagePost> response) {

                if (response.isSuccessful()) {
                    dismissProgress();
                    linMsg.setEnabled(true);
                    linMsg.setClickable(true);
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    posttextEt.setText("");
                    DeletePostresponse(response);
//                    Intent intent = new Intent(AttendeeDetailActivity.this, HomeActivity.class);
//                    startActivity(intent);
//                    finish();
                } else {
                    dismissProgress();
//                    dismissProgress();
                    linMsg.setEnabled(true);
                    linMsg.setClickable(true);
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendMessagePost> call, Throwable t) {
                dismissProgress();
                linMsg.setEnabled(true);
                linMsg.setClickable(true);
                Log.e("hit", "Low network or no network");
//                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
//                dismissProgress();
            }
        });
    }


    private void DeletePostresponse(Response<SendMessagePost> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {
            Log.e("post", "success");
//            myDialog.dismiss();
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        } else {
            Log.e("post", "fail");
//            myDialog.dismiss();
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    public void addToContactList(Context context, String strDisplayName, String strNumber) {

        // Get android phone contact content provider uri.
        //Uri addContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        // Below uri can avoid java.lang.UnsupportedOperationException: URI: content://com.android.contacts/data/phones error.
        Uri addContactsUri = ContactsContract.Data.CONTENT_URI;

        // Add an empty contact and get the generated id.
        long rowContactId = getRawContactId();

        // Add contact name data.
        insertContactDisplayName(addContactsUri, rowContactId, strDisplayName);

        insertContactNotes(addContactsUri, rowContactId, strDisplayName);

        insertContactPhoneNumber(addContactsUri, rowContactId, strNumber, strDisplayName);

        Toast.makeText(getApplicationContext(), "Contact saved successfully", Toast.LENGTH_LONG).show();

        finish();


    }

    public void showProgress() {
        if (progressBarmain.getVisibility() == View.GONE) {
            progressBarmain.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progressBarmain.getVisibility() == View.VISIBLE) {
            progressBarmain.setVisibility(View.GONE);
        }
    }


    // Insert newly created contact display name.
    private void insertContactDisplayName(Uri addContactsUri, long rawContactId, String displayName) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);

        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);

        // Put contact display name value.
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);

        //Notes
        contentValues.put(ContactsContract.CommonDataKinds.Note.NOTE, "Met At " + eventnamestr);


        getContentResolver().insert(addContactsUri, contentValues);
    }

    private void insertContactNotes(Uri addContactsUri, long rawContactId, String displayName) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);

        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);

        //Notes
        contentValues.put(ContactsContract.CommonDataKinds.Note.NOTE, "Met At " + eventnamestr);

        getContentResolver().insert(addContactsUri, contentValues);
    }


    private void insertContactPhoneNumber(Uri addContactsUri, long rawContactId, String phoneNumber, String strDisplayName) {
        // Create a ContentValues object.
        ContentValues contentValues = new ContentValues();

        // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);

        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

        // Put phone number value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);

        // Calculate phone type by user selection.
        int phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

//        if("home".equalsIgnoreCase(phoneTypeStr))
//        {
//            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
//        }else if("mobile".equalsIgnoreCase(phoneTypeStr))
//        {
//            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
//        }else if("work".equalsIgnoreCase(phoneTypeStr))
//        {
//            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
//        }
        // Put phone type value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType);

        // Insert new contact data into phone contact list.
        getContentResolver().insert(addContactsUri, contentValues);


    }

    private long getRawContactId() {
        // Inser an empty contact.
        ContentValues contentValues = new ContentValues();
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        // Get the newly created contact raw id.
        long ret = ContentUris.parseId(rawContactUri);
        return ret;
    }


    public void imagealert() {
        final Dialog dialog = new Dialog(AttendeeDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
                .setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setContentView(R.layout.imagepopulayout);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
//        String imgae = dbManager.GetimageUrl(datamodel.get(position).getProdcutid());
        String imageUrl = ApiConstant.profilepic + profile;
        Picasso.with(AttendeeDetailActivity.this).load(imageUrl).into(image);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }


}
