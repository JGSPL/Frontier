package com.procialize.mrgeApp20.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.BuildConfig;
import com.procialize.mrgeApp20.CustomTools.PicassoTrustAll;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.GetterSetter.Analytic;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.GetterSetter.ProfileSave;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.Utility.Utility;
import com.procialize.mrgeApp20.util.GetUserActivityReport;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_EVENT_LIST_LOGO_PATH;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_EVENT_PROFILE_PATH;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_PROFILE_PIC_PATH;
import static com.procialize.mrgeApp20.Session.SessionManager.MY_PREFS_NAME;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.Utility.Utility.setgradientDrawable;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class ProfileActivity extends AppCompatActivity {

    public static final int RequestPermissionCode = 8;
    public static String logoImg = "";
    Button savebtn;
    SessionManager sessionManager;
    EditText Etfirstname, Etlastname, Etdesignation, Etcompany, Etdescription, Etcity, Etcountry, Etmobile, Etemail;
    ProgressBar progressView;
    ImageView profileIV;
    File file;
    String edit_profile, edit_profile_pic, edit_profile_naame, edit_profile_designation, edit_profile_company,
            edit_profile_location, edit_profile_mobile, edit_profile_email;
    List<EventSettingList> eventSettingLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, eventback;
    String eventnamestr;
    String profilepic;
    String api_token, colorActive;
    TextView txt_upload;
    ImageView headerlogoIv;
    RelativeLayout linear_upload;
    String MY_PREFS_LOGIN = "ProcializeLogin";
   /* TextInputLayout input_layout_firstname, input_layout_lastname, input_layout_designation, input_layout_company,
            input_layout_mobile, input_layout_desc, input_layout_city, input_layout_emailid, input_layout_country;*/
    RelativeLayout relative;
    ProgressDialog progressDialog;
    String mCurrentPhotoPath = "";
    UCrop.Options options;
    LinearLayout linear;
    String imgname, background;
    private APIService mAPIService;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ConnectionDetector cd;
    RelativeLayout rl_update_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "");
        eventnamestr = prefs.getString("eventnamestr", "");
        logoImg = prefs.getString("logoImg", "");
        colorActive = prefs.getString("colorActive", "");
        eventback = prefs.getString("eventback", "");
        SharedPreferences.Editor editorlogin = getSharedPreferences(MY_PREFS_LOGIN, MODE_PRIVATE).edit();
        editorlogin.putString("loginfirst", "1").commit();

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();


        api_token = user.get(SessionManager.KEY_TOKEN);

        crashlytics("Profile Page", api_token);
        firbaseAnalytics(this,"Profile Page", api_token);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        Intent intent=getIntent();
//        eventid=intent.getStringExtra("eventId");
//        eventnamestr=intent.getStringExtra("eventnamestr");
        cd = new ConnectionDetector(ProfileActivity.this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);


        headerlogoIv = findViewById(R.id.headerlogoIv);
        linear = findViewById(R.id.linear);
        Util.logomethod1(this, headerlogoIv);


        mAPIService = ApiUtils.getAPIService();
        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String profilePic = prefs1.getString(KEY_EVENT_PROFILE_PATH,"");
        String eventLogo = prefs1.getString(KEY_EVENT_LIST_LOGO_PATH,"");
        PicassoTrustAll.getInstance(ProfileActivity.this)
                .load(/*ApiConstant.eventpic*/eventLogo + eventback)
                .into(new com.squareup.picasso.Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                              try {
                                  String root = Environment.getExternalStorageDirectory().toString();
                                  File myDir = new File(root + "/"+ApiConstant.folderName);

                                  if (!myDir.exists()) {
                                      myDir.mkdirs();
                                  }

                                  String name = imgname + ".jpg";
                                  myDir = new File(myDir, name);
                                  FileOutputStream out = new FileOutputStream(myDir);
                                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                  out.flush();
                                  out.close();
                              } catch (Exception e) {
                                  // some action
                              }
                          }

                          @Override
                          public void onBitmapFailed(Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                );

        try {
//            ContextWrapper cw = new ContextWrapper(MrgeHomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();

            PicassoTrustAll.getInstance(ProfileActivity.this)
                    .load(eventLogo/*ApiConstant.eventpic*/ + eventback)
                    .into(new com.squareup.picasso.Target() {
                              @Override
                              public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                  try {
                                      String root = Environment.getExternalStorageDirectory().toString();
                                      File myDir = new File(root + "/"+ApiConstant.folderName);

                                      if (!myDir.exists()) {
                                          myDir.mkdirs();
                                      }

                                      String name = imgname + ".jpg";
                                      myDir = new File(myDir, name);
                                      FileOutputStream out = new FileOutputStream(myDir);
                                      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                      out.flush();
                                      out.close();
                                  } catch (Exception e) {
                                      // some action
                                  }
                              }

                              @Override
                              public void onBitmapFailed(Drawable errorDrawable) {
                              }

                              @Override
                              public void onPrepareLoad(Drawable placeHolderDrawable) {
                              }
                          }
                    );

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

        }

        try {

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


//        } else {
//
//        }

        if (cd.isConnectingToInternet()) {
            fetchProfileDetail(api_token, eventid);
            SubmitAnalytics(api_token, eventid, "", "", "EditProfile");

        } else {
            initializeView();
            Toast.makeText(this, "Device not connected to internet", Toast.LENGTH_SHORT).show();
        }


        options = new UCrop.Options();

        options.setToolbarColor(Color.parseColor(colorActive));
        options.setCropFrameColor(Color.parseColor(colorActive));
        options.setLogoColor(Color.parseColor(colorActive));
        options.setActiveWidgetColor(Color.parseColor(colorActive));
        options.setLogoColor(Color.parseColor(colorActive));
        options.setToolbarTitle("Edit Profile");

        options.setToolbarCancelDrawable(R.color.transperent);


       /* if (CheckingPermissionIsEnabledOrNot()) {
//            Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }

        // If, If permission is not enabled then else condition will execute.
        else {

            //Calling method to enable permission.
            RequestMultiplePermission();

        }*/

        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this, api_token,
                eventid,
                ApiConstant.pageVisited,
                "41",
                "");
        getUserActivityReport.userActivityReport();
    }

    private void initializeView() {


//        sessionManager = new SessionManager(this);


        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        String lname = user.get(SessionManager.KEY_LNAME);

        // designation
        String designation = user.get(SessionManager.KEY_DESIGNATION);

        // company
        String company = user.get(SessionManager.KEY_COMPANY);

        //description
        String description = user.get(SessionManager.KEY_DESCRIPTION);

        //mobile
        String mobile = user.get(SessionManager.KEY_MOBILE);

        //city
        String city = user.get(SessionManager.KEY_CITY);

        //country
        String country = user.get(SessionManager.KEY_COUNTRY);

        //profilepic
        profilepic = user.get(SessionManager.KEY_PIC);

        //email
        String email = user.get(SessionManager.KEY_EMAIL);


        //Log.d("Profile Pic", profilepic);
        eventSettingLists = SessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

/*
        input_layout_firstname = findViewById(R.id.input_layout_firstname);
        input_layout_lastname = findViewById(R.id.input_layout_lastname);
        input_layout_designation = findViewById(R.id.input_layout_designation);
        input_layout_company = findViewById(R.id.input_layout_company);
        input_layout_mobile = findViewById(R.id.input_layout_mobile);
        input_layout_desc = findViewById(R.id.input_layout_desc);
        input_layout_city = findViewById(R.id.input_layout_city);
        input_layout_emailid = findViewById(R.id.input_layout_emailid);
        input_layout_country = findViewById(R.id.input_layout_country);*/
        relative = findViewById(R.id.relative);


        Etfirstname = findViewById(R.id.Etfirstname);
        Etlastname = findViewById(R.id.Etlastname);
        Etdesignation = findViewById(R.id.Etdesignation);
        Etcompany = findViewById(R.id.Etcompany);
        Etdescription = findViewById(R.id.Etdescription);
        Etcity = findViewById(R.id.Etcity);
        Etcountry = findViewById(R.id.Etcountry);
        Etmobile = findViewById(R.id.Etmobile);
        Etemail = findViewById(R.id.Etemail);

        savebtn = findViewById(R.id.savebtn);
        txt_upload = findViewById(R.id.txt_upload);
        rl_update_pic = findViewById(R.id.rl_update_pic);
        linear_upload = findViewById(R.id.linear_upload);

        TextView header = findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));

        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------


        RelativeLayout layoutTop = findViewById(R.id.layoutTop);
      //  layoutTop.setBackgroundColor(Color.parseColor(colorActive));

        GradientDrawable shape = setgradientDrawable(5, colorActive);
        savebtn.setBackground(shape);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        String colorActive = prefs.getString("colorActive", "");

      /*  GradientDrawable drawable = (GradientDrawable)savebtn.getBackground();
        drawable.setColor(Color.parseColor(colorActive));*/

        txt_upload.setTextColor(Color.parseColor(colorActive));


        profileIV = findViewById(R.id.profileIV);
        progressView = findViewById(R.id.progressView);


        if (designation != null && edit_profile_designation.equalsIgnoreCase("1")) {
            Etdesignation.setText(designation);
            Etdesignation.setVisibility(View.VISIBLE);
           // input_layout_designation.setVisibility(View.VISIBLE);
        } else {
            if (designation != null) {
                Etdesignation.setText(designation);
                Etdesignation.setVisibility(View.VISIBLE);
               // input_layout_designation.setVisibility(View.VISIBLE);

            }
            Etdesignation.setVisibility(View.GONE);
           // input_layout_designation.setVisibility(View.GONE);
        }

        if (description != null) {
            Etdescription.setText(description);
            Etdescription.setVisibility(View.GONE);
         //   input_layout_desc.setVisibility(View.GONE);
        } else {
            if (description != null) {
                Etdescription.setText(description);
                Etdescription.setVisibility(View.GONE);
              //  input_layout_desc.setVisibility(View.GONE);
            }
            Etdescription.setVisibility(View.GONE);
          //  input_layout_desc.setVisibility(View.GONE);
        }

        if (city != null && edit_profile_location.equalsIgnoreCase("1")) {
            Etcity.setText(city);
            Etcity.setVisibility(View.VISIBLE);
          //  input_layout_city.setVisibility(View.VISIBLE);
        } else {
            if (city != null) {
                Etcity.setText(city);
                Etcity.setVisibility(View.VISIBLE);
              //  input_layout_city.setVisibility(View.VISIBLE);
            }
            Etcity.setVisibility(View.GONE);
           // input_layout_city.setVisibility(View.GONE);
        }

        if (country != null && edit_profile_location.equalsIgnoreCase("1") && !country.isEmpty()) {
            Etcountry.setText(country);
            Etcountry.setVisibility(View.VISIBLE);
            //input_layout_country.setVisibility(View.VISIBLE);
        } else {
            if (country != null) {
                Etcountry.setText(country);
                Etcountry.setVisibility(View.VISIBLE);
                //input_layout_country.setVisibility(View.VISIBLE);
            }
            Etcountry.setVisibility(View.GONE);
            //input_layout_country.setVisibility(View.GONE);

        }

        if (mobile != null && edit_profile_mobile.equalsIgnoreCase("1")) {
            Etmobile.setText(mobile);
            Etmobile.setVisibility(View.VISIBLE);
         //   input_layout_mobile.setVisibility(View.VISIBLE);
        } else {
            if (mobile != null) {
                Etmobile.setText(mobile);
                Etmobile.setVisibility(View.VISIBLE);
         //       input_layout_mobile.setVisibility(View.VISIBLE);
            }
            Etmobile.setVisibility(View.GONE);
         //   input_layout_mobile.setVisibility(View.GONE);

        }

        if (company != null && edit_profile_company.equalsIgnoreCase("1")) {
            Etcompany.setText(company);
            Etcompany.setVisibility(View.VISIBLE);
        //    input_layout_company.setVisibility(View.VISIBLE);

        } else {

            if (company != null) {
                Etcompany.setText(company);
                Etcompany.setVisibility(View.VISIBLE);
         //       input_layout_company.setVisibility(View.VISIBLE);

            }

            Etcompany.setVisibility(View.GONE);
         //   input_layout_company.setVisibility(View.GONE);
        }

        try {
            if (name != null && edit_profile_naame.equalsIgnoreCase("1")) {

                Etfirstname.setText(name);
                Etlastname.setText(lname);
                Etfirstname.setVisibility(View.VISIBLE);
                Etlastname.setVisibility(View.VISIBLE);
          /*      input_layout_firstname.setVisibility(View.VISIBLE);
                input_layout_lastname.setVisibility(View.VISIBLE);*/

            } else {
                if (name != null) {
                    Etfirstname.setText(name);
                    Etlastname.setText(lname);
                    Etfirstname.setVisibility(View.VISIBLE);
                    Etlastname.setVisibility(View.VISIBLE);
                /*    input_layout_firstname.setVisibility(View.VISIBLE);
                    input_layout_lastname.setVisibility(View.VISIBLE);*/
                }
                Etfirstname.setVisibility(View.GONE);
                Etlastname.setVisibility(View.GONE);
             /*   input_layout_firstname.setVisibility(View.GONE);
                input_layout_lastname.setVisibility(View.GONE);*/

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (email != null && edit_profile_email.equalsIgnoreCase("1")) {
            Etemail.setText(email);
            Etemail.setVisibility(View.VISIBLE);
           /* input_layout_emailid.setVisibility(View.VISIBLE);*/
        } else {
            if (email != null) {
                Etemail.setText(email);
              //  Etemail.setVisibility(View.VISIBLE);
             /*   input_layout_emailid.setVisibility(View.VISIBLE);*/

            }
            Etemail.setVisibility(View.GONE);
          /*  input_layout_emailid.setVisibility(View.GONE);*/
        }


        if (edit_profile_pic.equalsIgnoreCase("1")) {
            profileIV.setVisibility(View.VISIBLE);
            //txt_upload.setVisibility(View.VISIBLE);
            rl_update_pic.setVisibility(View.VISIBLE);
            relative.setVisibility(View.VISIBLE);
            if (profilepic != null) {
                SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String profilePicPath  = prefs1.getString(KEY_PROFILE_PIC_PATH,"");
                Glide.with(this).load(profilePicPath/*ApiConstant.profilepic*/ + profilepic)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).circleCrop()
                        .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        profileIV.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(profileIV);
            } else {
               // profileIV.setImageResource(R.drawable.profilepic_placeholder);
                progressView.setVisibility(View.GONE);
            }
        } else {
            if (profilepic != null) {
                SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String profilePicPath  = prefs1.getString(KEY_PROFILE_PIC_PATH,"");
                Glide.with(this).load(profilePicPath/*ApiConstant.profilepic*/ + profilepic)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).circleCrop()
                        .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        profileIV.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(profileIV);
            }
            relative.setVisibility(View.GONE);
            profileIV.setVisibility(View.GONE);
            //txt_upload.setVisibility(View.GONE);
            rl_update_pic.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);
        }

        linear_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckingPermissionIsEnabledOrNot()) {
                    selectImage();
                } else {
                    RequestMultiplePermission();
                }
            }
        });

        txt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckingPermissionIsEnabledOrNot()) {
                    selectImage();
                } else {
                    RequestMultiplePermission();
                }
                // selectImage();

            }
        });
        rl_update_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckingPermissionIsEnabledOrNot()) {
                    selectImage();
                } else {
                    RequestMultiplePermission();
                }
                // selectImage();

            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Etfirstname = findViewById(R.id.Etfirstname);
                Etlastname = findViewById(R.id.Etlastname);
                Etdesignation = findViewById(R.id.Etdesignation);
                Etcompany = findViewById(R.id.Etcompany);
                Etdescription = findViewById(R.id.Etdescription);
                Etcity = findViewById(R.id.Etcity);
                Etcountry = findViewById(R.id.Etcountry);
                Etmobile = findViewById(R.id.Etmobile);
                Etemail = findViewById(R.id.Etemail);

                /*if (Etemail.getVisibility() == View.VISIBLE && Etemail.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter Email Id", Toast.LENGTH_SHORT).show();
                } else */if (Etfirstname.getVisibility() == View.VISIBLE && Etfirstname.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter First Name", Toast.LENGTH_SHORT).show();
                }
                else if (Etlastname.getVisibility() == View.VISIBLE && Etlastname.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
                } else if (Etdesignation.getVisibility() == View.VISIBLE && Etdesignation.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter Designation", Toast.LENGTH_SHORT).show();
                } else if (Etcompany.getVisibility() == View.VISIBLE && Etcompany.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter Company Name", Toast.LENGTH_SHORT).show();
                } /*else if (Etmobile.getVisibility() == View.VISIBLE && Etmobile.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }*/ else if (Etcity.getVisibility() == View.VISIBLE && Etcity.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter City Name", Toast.LENGTH_SHORT).show();
                } else {
                    if (profilepic == null) {
                        if (file == null) {
                            Toast.makeText(ProfileActivity.this, "Please upload profile image", Toast.LENGTH_SHORT).show();
                        } else {
                            saveProfile();
                        }
                    } else {
                        saveProfile();
                    }
                }
            }
        });

    }

    public void fetchProfileDetail(String token, String eventid) {

        RequestBody token1 = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody eventid1 = RequestBody.create(MediaType.parse("text/plain"), eventid);
//        showProgress();
        mAPIService.fetchProfileDetail(token1, eventid1).enqueue(new Callback<ProfileSave>() {
            @Override
            public void onResponse(Call<ProfileSave> call, Response<ProfileSave> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        sessionManager.logoutUser();
                        Intent main = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(main);
                        finish();

                    } else {

                        showResponse(response);
                    }
//                    dismissProgress();
//                    showResponse(response);
                } else {


//                    dismissProgress();
                    try {
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ProfileSave> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();

            }
        });
    }

    public void showResponse(Response<ProfileSave> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getUserData().equals(null))) {

                try {
                    String id = response.body().getUserData().getAttendeeId();
                    String name = response.body().getUserData().getFirstName();
                    String company = response.body().getUserData().getCompanyName();
                    String designation = response.body().getUserData().getDesignation();
                    String pic = response.body().getUserData().getProfilePic();
                    String lastname = response.body().getUserData().getLastName();
                    String city = response.body().getUserData().getCity();
                    String mobno = response.body().getUserData().getMobile();
                    String email = response.body().getUserData().getEmail();
                    String country = response.body().getUserData().getCountry();
                    String description = response.body().getUserData().getDescription();
                    String attendee_status = response.body().getUserData().getAttendee_status();
                    String exhibitor_status = response.body().getUserData().getExhibitor_status();
                    String exhibitor_id = response.body().getUserData().getExhibitor_id();
                    String profilePicPath = response.body().getProfile_pic_url_path();

                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString(KEY_PROFILE_PIC_PATH,profilePicPath);
                    edit.putString("buddy_tc_accepted",response.body().getUserData().getBuddy_accept_terms());
                    edit.commit();

                    SharedPreferences.Editor pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                    pref.clear();

                    sessionManager.createProfileSession(id,name, company, designation, pic, lastname, city, description, country, email, mobno, attendee_status, exhibitor_id, exhibitor_status);

                    initializeView();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {

            }
            if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                sessionManager.logoutUser();
                Intent main = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(main);
                finish();

            }
        } else {
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {
        //edit_profile_pic,edit_profile_name,edit_profile_designation,edit_profile_location
        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile")) {
                edit_profile = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_pic")) {
                edit_profile_pic = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_naame")) {
                edit_profile_naame = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_name")) {
                edit_profile_naame = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_designation")) {
                edit_profile_designation = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_company")) {
                edit_profile_company = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_location")) {
                edit_profile_location = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_mobile")) {
                edit_profile_mobile = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_email")) {
                edit_profile_email = eventSettingLists.get(i).getFieldValue();
            }
        }
    }


    public void saveProfile() {

        Showprogress(this, "Loading.......");

        HashMap<String, String> user = sessionManager.getUserDetails();

        final String api_token = user.get(SessionManager.KEY_TOKEN);
        String firstname = Etfirstname.getText().toString();
        String lastname = Etlastname.getText().toString();
        String description = Etdescription.getText().toString();
        String designation = Etdesignation.getText().toString();
        String city = Etcity.getText().toString();
        String country = Etcountry.getText().toString();
        String mobile = Etmobile.getText().toString();
        String companyname = Etcompany.getText().toString();

        MultipartBody.Part body = null;
        if (file != null) {

            if (!file.equals("")) {
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                body = MultipartBody.Part.createFormData("profile_pic", file.getName(), reqFile);
                SharedPreferences.Editor editor = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                editor.putString("profile", String.valueOf(file)).commit();
//          editor.putString("loginfirst","1");
                editor.apply();
            }
        } else {
            SharedPreferences pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE);
            String prof_pic = pref.getString("profile", "");
            // String prof_pic = user.get(SessionManager.KEY_PIC);

            if (prof_pic != null) {
                if (!prof_pic.equals("")) {
                    file = new File(prof_pic);
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                    body = MultipartBody.Part.createFormData("profile_pic", file.getName(), reqFile);
                }
            } else {
//                file = new File(profilepic);
//                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), profilepic);
//                body = MultipartBody.Part.createFormData("profile_pic", profilepic, reqFile);
            }
//            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), profilepic);
//            body = MultipartBody.Part.createFormData("profile_pic", profilepic, reqFile);
        }

        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), api_token);
        RequestBody fstname = RequestBody.create(MediaType.parse("text/plain"), firstname);
        RequestBody lstname = RequestBody.create(MediaType.parse("text/plain"), lastname);
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody cit = RequestBody.create(MediaType.parse("text/plain"), city);
        RequestBody countr = RequestBody.create(MediaType.parse("text/plain"), country);
        RequestBody mob = RequestBody.create(MediaType.parse("text/plain"), mobile);
        RequestBody cmp = RequestBody.create(MediaType.parse("text/plain"), companyname);
        RequestBody des = RequestBody.create(MediaType.parse("text/plain"), designation);
        RequestBody typ = RequestBody.create(MediaType.parse("text/plain"), "A");
        RequestBody evid = RequestBody.create(MediaType.parse("text/plain"), eventid);


        if (body == null) {

            mAPIService.ProfileSave(token, fstname, lstname, desc, cit, countr,
                    mob, typ, des, evid, cmp).enqueue(new Callback<ProfileSave>() {
                @Override
                public void onResponse(Call<ProfileSave> call, Response<ProfileSave> response) {
                    try {

                        Dismissprogress();
                        if (response.body().getStatus().equals("success")) {
                            Log.i("hit", "post submitted to API." + response.body().toString());

                            String id = response.body().getUserData().getAttendeeId();
                            String name = response.body().getUserData().getFirstName();
                            String company = response.body().getUserData().getCompanyName();
                            String designation = response.body().getUserData().getDesignation();
                            String pic = response.body().getUserData().getProfilePic();
                            String lastname = response.body().getUserData().getLastName();
                            String city = response.body().getUserData().getCity();
                            String mobno = response.body().getUserData().getMobile();
                            String email = response.body().getUserData().getEmail();
                            String country = response.body().getUserData().getCountry();
                            String description = response.body().getUserData().getDescription();
                            String attendee_type = response.body().getUserData().getAttendee_status();
                            String exhibitor_id = response.body().getUserData().getExhibitor_id();
                            String exhibitor_status = response.body().getUserData().getExhibitor_status();

                            SharedPreferences.Editor pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                            pref.clear();

                            sessionManager.createProfileSession(id,name, company, designation, pic, lastname, city, description, country, email, mobno, attendee_type, exhibitor_id, exhibitor_status);
//                      initializeView();

                            Intent home = new Intent(getApplicationContext(), MrgeHomeActivity.class);
                            startActivity(home);
                            finish();

                            SubmitAnalytics(api_token, eventid, "", "", "EditProfileSubmit");
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
//                        Intent home = new Intent(getApplicationContext(), MrgeHomeActivity.class);
//                        home.putExtra("eventId", eventid);
//                        home.putExtra("eventnamestr", eventnamestr);
//                        startActivity(home);
//                        finish();
                        }
                    } catch (Exception e) {
                        Dismissprogress();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ProfileSave> call, Throwable t) {
                    Dismissprogress();
                    Log.e("hit", "Low network or no network");
                    // Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mAPIService.ProfileSave(token, fstname, lstname, desc, cit, countr,
                    mob, typ, des, evid, cmp, body).enqueue(new Callback<ProfileSave>() {
                @Override
                public void onResponse(Call<ProfileSave> call, Response<ProfileSave> response) {
                    try {
                        Dismissprogress();
                        if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                            sessionManager.logoutUser();
                            Intent main = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(main);
                            finish();

                        }
                        if (response.body().getStatus().equals("success")) {
                            Log.i("hit", "post submitted to API." + response.body().toString());

                            String id = response.body().getUserData().getAttendeeId();
                            String name = response.body().getUserData().getFirstName();
                            String company = response.body().getUserData().getCompanyName();
                            String designation = response.body().getUserData().getDesignation();
                            String pic = response.body().getUserData().getProfilePic();
                            String lastname = response.body().getUserData().getLastName();
                            String city = response.body().getUserData().getCity();
                            String mobno = response.body().getUserData().getMobile();
                            String email = response.body().getUserData().getEmail();
                            String country = response.body().getUserData().getCountry();
                            String description = response.body().getUserData().getDescription();
                            String attendee_status = response.body().getUserData().getAttendee_status();
                            String exhibitor_id = response.body().getUserData().getExhibitor_id();
                            String exhibitor_status = response.body().getUserData().getExhibitor_status();

                            SharedPreferences.Editor pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                            pref.clear();

                            sessionManager.createProfileSession(id,name, company, designation, pic, lastname, city, description, country, email, mobno, attendee_status, exhibitor_id, exhibitor_status);
//                      initializeView();

                            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.putString("buddy_tc_accepted",response.body().getUserData().getBuddy_accept_terms());
                            edit.commit();

                            Intent home = new Intent(getApplicationContext(), MrgeHomeActivity.class);
                            startActivity(home);
                            finish();

                            SubmitAnalytics(api_token, eventid, "", "", "EditProfileSubmit");
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
//                        Intent home = new Intent(getApplicationContext(), MrgeHomeActivity.class);
//                        home.putExtra("eventId", eventid);
//                        home.putExtra("eventnamestr", eventnamestr);
//                        startActivity(home);
//                        finish();
                        }
                    } catch (Exception e) {
                        Dismissprogress();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ProfileSave> call, Throwable t) {
                    Dismissprogress();
                    Log.e("hit", "Low network or no network");
                    //Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ProfileActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_FILE);

    }

    private void cameraIntent() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CAMERA);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID+".android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);

                /*Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);

                String compressedImagePath = compressImage(picturePath);
//                appDelegate.setPostImagePath(compressedImagePath);

                file = new File(compressedImagePath);

                // PicassoTrustAll.getInstance(this).load(compressedImagePath).into(post_thumbnail);
                Glide.with(this).load(compressedImagePath)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).circleCrop()
                        .into(profileIV);


                // PicassoTrustAll.getInstance(PostActivity.this).load(compressedImagePath)
                // .into(post_thumbnail);

                Toast.makeText(ProfileActivity.this, "Image selected",
                        Toast.LENGTH_SHORT).show();

                cursor.close();*/
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(data);
                onSelectFromCropResult(resultUri);
            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
            }
        } else {
            if (profilepic != null) {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String profilePicPath  = prefs.getString(KEY_PROFILE_PIC_PATH,"");

                Glide.with(this).load(profilePicPath/*ApiConstant.profilepic*/ + profilepic)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).circleCrop()
                        .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        profileIV.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(profileIV);
            }
            file = null;
            if (CheckingPermissionIsEnabledOrNot()) {
                selectImage();
            } else {
                RequestMultiplePermission();
            }
            //selectImage();

        }
    }


    private void onCaptureImageResult(Intent data) {
        try {
            Uri tempUri;
            if (data != null) {
                if (data.getData() != null) {
                    Bitmap thumbnail = ( Bitmap ) data.getExtras().get("data");

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    tempUri = getImageUri(getApplicationContext(), thumbnail);

                    UCrop.of(tempUri, Uri.parse(destination.getAbsolutePath()))
                            .withAspectRatio(2, 2).withOptions(options)
                            .withMaxResultSize(200, 200)
                            .start(this);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    file = new File(getRealPathFromURI(tempUri));
                } else {
                    file = new File(mCurrentPhotoPath);

                    tempUri = Uri.fromFile(file);

                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    UCrop.of(tempUri, Uri.parse(destination.getAbsolutePath()))
                            .withAspectRatio(2, 2).withOptions(options)
                            .withMaxResultSize(200, 200)
                            .start(this);
                }
            } else {
                file = new File(mCurrentPhotoPath);

                tempUri = Uri.fromFile(file);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                UCrop.of(tempUri, Uri.parse(destination.getAbsolutePath()))
                        .withAspectRatio(2, 2).withOptions(options)
                        .withMaxResultSize(200, 200)
                        .start(this);
            }

//        profileIV.setImageURI(tempUri);

            Glide.with(this).load(tempUri)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).circleCrop()
                    .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    progressView.setVisibility(View.GONE);
                    profileIV.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressView.setVisibility(View.GONE);
                    return false;
                }
            }).into(profileIV);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), bm);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                file = new File(getRealPathFromURI(tempUri));

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                UCrop.of(tempUri, Uri.parse(destination.getAbsolutePath()))
                        .withAspectRatio(2, 2).withOptions(options)
                        .withMaxResultSize(200, 200)
                        .start(this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Glide.with(this).load(bm)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).circleCrop()
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressView.setVisibility(View.GONE);
                profileIV.setImageResource(R.drawable.profilepic_placeholder);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressView.setVisibility(View.GONE);
                return false;
            }
        }).into(profileIV);
//        profileIV.setImageBitmap(bm);
    }

    private void onSelectFromCropResult(Uri tempUri) {

        if (tempUri != null) {

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            file = new File(tempUri.getPath());

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Glide.with(this).load(tempUri)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).circleCrop()
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressView.setVisibility(View.GONE);
                profileIV.setImageResource(R.drawable.profilepic_placeholder);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressView.setVisibility(View.GONE);
                return false;
            }
        }).into(profileIV);
//        profileIV.setImageBitmap(bm);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(getApplicationContext(), MrgeHomeActivity.class);
        startActivity(home);
        finish();
    }

    @Override
    protected void onResume() {
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]
                {
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readstoragepermjission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writestoragepermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && readstoragepermjission && writestoragepermission) {
                        selectImage();
//                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String
            target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

                    // Toast.makeText(ProfileActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
                // Toast.makeText(ProfileActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void Showprogress(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();
    }


    private void Dismissprogress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        float maxHeight = 916.0f;
        float maxWidth = 712.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = ( int ) (imgRatio * actualWidth);
                actualHeight = ( int ) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = ( int ) (imgRatio * actualHeight);
                actualWidth = ( int ) maxWidth;
            } else {
                actualHeight = ( int ) maxHeight;
                actualWidth = ( int ) maxWidth;
            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / ( float ) options.outWidth;
        float ratioY = actualHeight / ( float ) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();

        try {
            out = new FileOutputStream(filename);

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round(( float ) height
                    / ( float ) reqHeight);
            final int widthRatio = Math.round(( float ) width / ( float ) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null,
                null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

}
