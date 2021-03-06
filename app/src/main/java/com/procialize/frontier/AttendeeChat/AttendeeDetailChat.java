package com.procialize.frontier.AttendeeChat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.BuddyList.DataModel.FetchSendRequest;
import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.DbHelper.DBHelper;
import com.procialize.frontier.GetterSetter.AttendeeList;
import com.procialize.frontier.GetterSetter.EventSettingList;
import com.procialize.frontier.GetterSetter.UserData;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_ATTENDEE_PIC_PATH;

public class AttendeeDetailChat extends AppCompatActivity {

    public static final int RequestPermissionCode = 8;
    String attendeeid, city, country, company, designation, description,
            totalrating, name, profile, mobile, buddy_status;
    TextView tvname, tvcompany, tvdesignation, tvcity, tvmob,  tv_description;
    Dialog myDialog;
    APIService mAPIService;
    SessionManager sessionManager;
    String apikey;
    ImageView profileIV;
    String attendee_company, attendee_location, attendee_mobile, attendee_design, attendee_savecontact, attendeemsg;
    List<EventSettingList> eventSettingLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive, eventnamestr;
    UserData userData;
    String getattendee;
    EditText posttextEt;
    View viewtwo, viewthree, viewone, viewtfour, viewtfive;
    ProgressDialog progressDialog;
    ImageView headerlogoIv;
    // RelativeLayout linear;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<AttendeeList> attendeeList;
    private List<AttendeeList> attendeesDBList;
    private DBHelper dbHelper;
    private RelativeLayout layoutTop;
    TextView txtRemove;
    ImageView imgBuddy;
    TextView saveContact;
    LinearLayout linsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendeed_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
      //  toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });
        headerlogoIv = findViewById(R.id.headerlogoIv);
        // Util.logomethod(this, headerlogoIv);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        eventnamestr = prefs.getString("eventnamestr", "");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dbHelper = new DBHelper(AttendeeDetailChat.this);
        db = dbHelper.getWritableDatabase();

        db = dbHelper.getReadableDatabase();

        userData = dbHelper.getUserDetails();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // token


        mAPIService = ApiUtils.getBuddyAPIService();
        sessionManager = new SessionManager(AttendeeDetailChat.this);

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
        } catch (Exception e) {
            e.printStackTrace();
        }


        tvname = findViewById(R.id.tvname);
        tvcompany = findViewById(R.id.tvcompany);
        tvdesignation = findViewById(R.id.tvdesignation);
        tv_description = findViewById(R.id.tv_description);
        tvcity = findViewById(R.id.tvcity);
        profileIV = findViewById(R.id.profileIV);
        posttextEt = findViewById(R.id.posttextEt);
        viewtwo = findViewById(R.id.viewtwo);
        viewthree = findViewById(R.id.viewthree);
        viewone = findViewById(R.id.viewone);
        viewtfour = findViewById(R.id.viewtfour);
        viewtfive = findViewById(R.id.viewtfive);
        txtRemove = findViewById(R.id.txtRemove);
        saveContact = findViewById(R.id.saveContact);
        linsave = findViewById(R.id.linsave);
        imgBuddy = findViewById(R.id.imgBuddy);
        //linear = findViewById(R.id.linear);
        tvmob = findViewById(R.id.tvmob);
        layoutTop = findViewById(R.id.layoutTop);
        TextView title = findViewById(R.id.title);
        title.setTextColor(Color.parseColor(colorActive));
        tv_description.setMovementMethod(new ScrollingMovementMethod());

        LinearLayout linear = findViewById(R.id.linear);
        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/"+ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));

        }

        txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccectRejectMethod(eventid,apikey,attendeeid,"4");

            }
        });
        if(buddy_status!=null) {

            if (buddy_status.equalsIgnoreCase("send_request")) {
                saveContact.setText("Add to buddy list");
                imgBuddy.setVisibility(View.VISIBLE);
                txtRemove.setVisibility(View.GONE);

            } else if (buddy_status.equalsIgnoreCase("friends")) {
                saveContact.setText("Added to buddy list");
                linsave.setBackgroundColor(Color.parseColor(colorActive));
                saveContact.setBackgroundColor(Color.parseColor(colorActive));
                saveContact.setTextColor(Color.parseColor("#ffffff"));
                imgBuddy.setVisibility(View.GONE);
                txtRemove.setVisibility(View.VISIBLE);

            } else if (buddy_status.equalsIgnoreCase("request_sent")) {
                saveContact.setText("Request sent");
                linsave.setBackgroundColor(Color.parseColor(colorActive));
                saveContact.setBackgroundColor(Color.parseColor(colorActive));
                saveContact.setTextColor(Color.parseColor("#ffffff"));
                imgBuddy.setVisibility(View.GONE);
                txtRemove.setVisibility(View.GONE);

            }else if (buddy_status.equalsIgnoreCase("request_received")) {
                saveContact.setText("Request sent");
                linsave.setBackgroundColor(Color.parseColor(colorActive));
                saveContact.setBackgroundColor(Color.parseColor(colorActive));
                saveContact.setTextColor(Color.parseColor("#ffffff"));
                imgBuddy.setVisibility(View.GONE);
                txtRemove.setVisibility(View.GONE);

            }
        }

        linsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buddy_status.equalsIgnoreCase("send_request")){
                    AddBuddy(apikey,eventid,attendeeid);


                }
            }
        });




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



        try {
            if (designation.equalsIgnoreCase("N A")) {
                tvdesignation.setVisibility(View.GONE);
                viewtwo.setVisibility(View.GONE);
                viewone.setVisibility(View.GONE);

            } else if (designation != null /*&& attendee_design.equalsIgnoreCase("1")*/) {
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

            } else if (city != null/* && attendee_location.equalsIgnoreCase("1")*/) {
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
            SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String picPath =  prefs1.getString(KEY_ATTENDEE_PIC_PATH,"");
            Glide.with(this).load(/*ApiConstant.profilepic*/picPath + profile).circleCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            profileIV.setImageResource(R.drawable.profilepic_placeholder);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(profileIV);
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
        ActivityCompat.requestPermissions(AttendeeDetailChat.this, new String[]
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

                        Toast.makeText(AttendeeDetailChat.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }


    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("attendee_company")) {
                attendee_company = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("attendee_location")) {
                attendee_location = eventSettingLists.get(i).getFieldValue();
            }  else if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("attendee_design")) {
                attendee_design = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("attendee_designation")) {
                attendee_design = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("attendee_message")) {
                attendeemsg = eventSettingLists.get(i).getFieldValue();
            }
        }
    }
    public void AccectRejectMethod(String eventid, String toke, String Buddy_id, String response) {
        mAPIService.removeBuddy(eventid,toke, Buddy_id).enqueue(new Callback<FetchSendRequest>() {
            @Override
            public void onResponse(Call<FetchSendRequest> call, Response<FetchSendRequest> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    showResponseBuddy(response);
                } else {

                    Toast.makeText(AttendeeDetailChat.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchSendRequest> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(AttendeeDetailChat.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void showResponseBuddy(Response<FetchSendRequest> response) {

        if (response.body().getStatus().equalsIgnoreCase("success")) {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            //fetchFeed(token, eventid);
            linsave.setBackgroundResource(R.drawable.button_background);
            saveContact.setBackgroundColor(Color.parseColor("#ffffff"));
            saveContact.setTextColor(Color.parseColor(colorActive));
            saveContact.setText("Add to buddy list");
            buddy_status= "send_request";

            imgBuddy.setVisibility(View.VISIBLE);

        } else {

            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    public void AddBuddy( String token,String eventid,String messId) {
        mAPIService.sendFriendRequest(token, eventid, messId).enqueue(new Callback<FetchSendRequest>() {
            @Override
            public void onResponse(Call<FetchSendRequest> call, Response<FetchSendRequest> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    Buddyresponse(response);
                } else {


                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchSendRequest> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();


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


}
