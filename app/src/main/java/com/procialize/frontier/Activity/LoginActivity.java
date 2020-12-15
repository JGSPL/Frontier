package com.procialize.frontier.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.BuildConfig;
import com.procialize.frontier.CustomTools.PicassoTrustAll;
import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.GetterSetter.EventListing;
import com.procialize.frontier.GetterSetter.Forgot;
import com.procialize.frontier.GetterSetter.Login;
import com.procialize.frontier.GetterSetter.UserEventList;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

import cn.jzvd.JzvdStd;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_EVENT_LIST_LOGO_PATH;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_EVENT_PROFILE_PATH;
import static com.procialize.frontier.util.CommonFunction.crashlytics;
import static com.procialize.frontier.util.CommonFunction.firbaseAnalytics;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private Dialog passcodeDialog;
    public static final int RequestPermissionCode = 8;
    Boolean emailbool = false, passwordbool = false;
    ProgressBar progressBar2;
    Button loginbtn, createaccbtn;
    String emailid, password;
    ImageView headerlogoIv;
    Dialog myDialog;
    CheckBox chk_box;
    LinearLayout linearLayout2;
    RelativeLayout rel2;
    ImageView imgBack;
    EditText login_otp;
    Button otp_submit_btn, otp_resubmit_btn;
    String passwordOtp, mobilenumber, accessToken;
    private APIService mAPIService;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String MY_PREFS_CATEGORY = "categorycnt";
    String eventnamestr, gcmRegID;
    private String logoImg = "", colorActive = "", background = "";
    SessionManager session;
    String platform, device, os_version, app_version;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String imgname;
    UserEventList eventList;
    String url;
    ConnectionDetector connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        session = new SessionManager(getApplicationContext());

        mAPIService = ApiUtils.getAPIService();
        Fabric.with(this, new Crashlytics());
        underLineTextview();
        initializeView();
        platform = "android";
        device = Build.MODEL;
        os_version = Build.VERSION.RELEASE;
        app_version = BuildConfig.VERSION_NAME;
        connection = new ConnectionDetector(LoginActivity.this);


        if (!CheckingPermissionIsEnabledOrNot()) {
            RequestMultiplePermission();
        }

        if (checkPlayServices()) {

            // gcmRegID = getRegistrationId(this);

            gcmRegID = session.getGcmID();

            if (gcmRegID.isEmpty()) {
                // storeRegistrationId(this, gcmRegID);
                new getGCMRegId().execute();
            }

        } else {
            Log.i("TAG", "No valid Google Play Services APK found.");
        }
    }


//    private void underLineTextview() {
//
//        TextView textView = findViewById(R.id.textView);
//        TextView txt_termslink = findViewById(R.id.txt_termslink);
//        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        txt_termslink.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        textView.setText("Designed & Developed by Procialize");
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String url = "https://www.procialize.net";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
////                finish();
//            }
//        });
//        txt_termslink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String url = ApiConstant.imgURL + "eula.html";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
////                finish();
//            }
//        });
//    }

    private void initializeView() {


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        final TextInputEditText Etemail, Etpassword;
        final CheckBox chk_box;
        final TextInputLayout inputLayoutemail;
        final TextView text_forgotPswd;

//        headerlogoIv = findViewById(R.id.headerlogoIv);
//        Util.logomethod(this,headerlogoIv);
        Etemail = findViewById(R.id.input_email);
        Etpassword = findViewById(R.id.input_password);
        progressBar2 = findViewById(R.id.progressBar2);
        text_forgotPswd = findViewById(R.id.text_forgotPswd);
        chk_box = findViewById(R.id.chk_box);
        text_forgotPswd.getPaint().setUnderlineText(true);
        linearLayout2 = findViewById(R.id.linearLayout2);
        rel2 = findViewById(R.id.rel2);
        imgBack = findViewById(R.id.imgBack);
        login_otp = findViewById(R.id.login_otp);
        otp_submit_btn = findViewById(R.id.otp_submit_btn);
        otp_resubmit_btn = findViewById(R.id.otp_resubmit_btn);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel2.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);

            }
        });


//        inputLayoutemail = findViewById(R.id.input_layout_email);
//        inputLayoutemail.setErrorEnabled(true);
//        inputLayoutpassword = findViewById(R.id.input_layout_password);
//        inputLayoutpassword.setErrorEnabled(true);

        text_forgotPswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPasswordDialog();
            }
        });

        loginbtn = findViewById(R.id.loginbtn);
        createaccbtn = findViewById(R.id.createaccbtn);

        createaccbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.procialize.net";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        Etemail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                //                    inputLayoutemail.setError(null);
//                    inputLayoutemail.setError("Please Enter Valid Email Id");
                emailbool = s.toString().matches(emailPattern) && s.length() > 0;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });


        Etpassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                //                    inputLayoutpassword.setError(null);
//                    inputLayoutpassword.setError("Please Enter Password");
                passwordbool = s.length() > 0;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Etemail.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else {

                    if (connection.isConnectingToInternet()) {
                        emailid = Etemail.getText().toString();
                        // password = Etpassword.getText().toString();
                        showProgress();
                        sendEventList(Etemail.getText().toString());
                    } else {
                        Toast.makeText(LoginActivity.this, "You are not connected to Internet for processing", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        otp_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String localOtp = login_otp.getText().toString();
                if (localOtp.equalsIgnoreCase(passwordOtp)) {
//                    Intent event = new Intent(getApplicationContext(), EventChooserActivity.class);
//                    event.putExtra("email", emailid);
//                    event.putExtra("mobile", mobilenumber);
//
//                    event.putExtra("password", passwordOtp);
//                    event.putExtra("accesstiken", accessToken);
//                    startActivity(event);
//                    finish();

                    sendLoginEvent(emailid, password, "1", gcmRegID, platform, device, os_version, app_version);
                } else {
                    login_otp.setText("");
                    Toast.makeText(LoginActivity.this, "Wrong OTP! Please try again", Toast.LENGTH_SHORT).show();

                }
            }
        });

        otp_resubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResendOTP(mobilenumber);
            }
        });


        crashlytics("Login", "");
        firbaseAnalytics(this, "Login", "");
        /*if (CheckingPermissionIsEnabledOrNot()) {
//            Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }

        // If, If permission is not enabled then else condition will execute.
        else {

            //Calling method to enable permission.
            RequestMultiplePermission();

        }
*/

    }

    public void sendEventList(String email) {
        mAPIService.EventListPost(email).enqueue(new Callback<EventListing>() {
            @Override
            public void onResponse(Call<EventListing> call, Response<EventListing> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<EventListing> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponse(Response<EventListing> response) {

        if (response.body().getStatus().equals("success")) {

            passwordOtp = response.body().getUserData().getPassword_key();
            accessToken = response.body().getUserData().getApiAccessToken();

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_EVENT_PROFILE_PATH, response.body().getProfile_pic_url_path());
            editor.putString(KEY_EVENT_LIST_LOGO_PATH, response.body().getEvent_logo_url_path());
            editor.commit();

            eventList = response.body().getUserEventList().get(0);


            try {
                File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/" + ApiConstant.folderName + "/" + "background.jpg");
                mypath.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }


            String eventid = eventList.getEventId();
            eventnamestr = eventList.getName();
            logoImg = eventList.getHeader_logo();
            colorActive = "#fab43a";
            background = eventList.getBackground_image();

            SharedPreferences prefspro = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String profilePath = prefspro.getString(KEY_EVENT_PROFILE_PATH, "");
            String logoPath = prefspro.getString(KEY_EVENT_LIST_LOGO_PATH, "");

            Log.d("event_id", eventid);

            url = logoPath/* ApiConstant.eventpic*/ + eventList.getBackground_image();
//            Glide.with(this).load(url)
//                    .apply(RequestOptions.skipMemoryCacheOf(true))
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
////                progressBar.setVisibility(View.GONE);
//                    e.printStackTrace();
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
////                progressBar.setVisibility(View.GONE);
//                    return false;
//                }
//            }).into(imageview);

            imgname = "background";
            for (int i = 0; i < response.body().getUserEventList().size(); i++) {
                PicassoTrustAll.getInstance(LoginActivity.this)
                        .load(logoPath + response.body().getUserEventList().get(i).getBackground_image())
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                try {
                                    String root = Environment.getExternalStorageDirectory().toString();
                                    File myDir = new File(root + "/" + ApiConstant.folderName);
//                                    Toast.makeText(EventChooserActivity.this,
//                                            "Download completed- check folder Procialize/Image",
//                                            Toast.LENGTH_SHORT).show();
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
                        });
            }
//            linearLayout2.setVisibility(View.GONE);
//            rel2.setVisibility(View.VISIBLE);
            passcodeDialog(LoginActivity.this);
        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    public void sendResendOTP(String mobile) {
        mAPIService.ResendOtp(mobile).enqueue(new Callback<EventListing>() {
            @Override
            public void onResponse(Call<EventListing> call, Response<EventListing> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    showResponseOTP(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<EventListing> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponseOTP(Response<EventListing> response) {

        if (response.body().getStatus().equals("success")) {


            if (!(response.body().getUserData().equals(null))) {

                try {

                    passwordOtp = response.body().getUserData().getPassword_key();
                    mobilenumber = response.body().getUserData().getMobile();
                    emailid = response.body().getUserData().getEmail();
                    accessToken = response.body().getUserData().getApiAccessToken();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            login_otp.setText("");


//            linearLayout2.setVisibility(View.GONE);
//            rel2.setVisibility(View.VISIBLE);
//            passcodeDialog(LoginActivity.this);

        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }


    public void showProgress() {
        progressBar2.setVisibility(View.VISIBLE);
        loginbtn.setEnabled(false);
    }

    public void dismissProgress() {
        if (progressBar2.getVisibility() == View.VISIBLE) {
            progressBar2.setVisibility(View.GONE);
        }
        loginbtn.setEnabled(true);
    }

    @Override
    protected void onResume() {
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CONTACTS);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SixthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]
                {READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE
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

                        Toast.makeText(LoginActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }

    public void otpLoginProcess() {
        login_otp.setVisibility(View.GONE);
        otp_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP = otp_submit_btn.getText().toString();
            }
        });

    }

    public void forgetPasswordDialog() {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.dialog_signin);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();

        Button submit = myDialog.findViewById(R.id.submit);
        final EditText edit_emailid = myDialog.findViewById(R.id.edit_emailid);
        ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_emailid.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                } else {

                    String email = edit_emailid.getText().toString().trim();
                    forgotPassword(email);
                }
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

    }

    public void forgotPassword(String email) {
        mAPIService.ForgotPassword(email).enqueue(new Callback<Forgot>() {
            @Override
            public void onResponse(Call<Forgot> call, Response<Forgot> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponseForgotP(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<Forgot> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponseForgotP(Response<Forgot> response) {

        if (response.body().getStatus().equals("success")) {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            myDialog.dismiss();

        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JzvdStd.releaseAllVideos();
        ActivityCompat.finishAffinity(LoginActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

    public void passcodeDialog(final Context activity) {
//        Log.d("Password Key", response.body().getUserData().getPassword_key().toString());
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }

        passcodeDialog = new Dialog(activity);
        passcodeDialog.setContentView(R.layout.otplayout);
        passcodeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        passcodeDialog.setCancelable(false);

        Button btn_login_otp = passcodeDialog.findViewById(R.id.btn_login_otp);
        Button btn_resend_otp = passcodeDialog.findViewById(R.id.btn_resend_otp);
        final EditText message_edt_send__dialog = passcodeDialog.findViewById(R.id.message_edt_send__dialog);
        ImageView img_cancel = passcodeDialog.findViewById(R.id.img_cancel);


        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeDialog.dismiss();
                passcodeDialog.cancel();
            }
        });

        btn_login_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String localOtp = message_edt_send__dialog.getText().toString();
                if (localOtp.equalsIgnoreCase(passwordOtp)) {
//                    Intent event = new Intent(getApplicationContext(), EventChooserActivity.class);
//                    event.putExtra("email", emailid);
//                    event.putExtra("mobile", mobilenumber);
//
//                    event.putExtra("password", passwordOtp);
//                    event.putExtra("accesstiken", accessToken);
//                    startActivity(event);
//                    finish();

                    sendLoginEvent(emailid, passwordOtp, "1", gcmRegID, platform, device, os_version, app_version);
                } else {
                    login_otp.setText("");
                    Toast.makeText(LoginActivity.this, "Wrong OTP! Please try again", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btn_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailid.isEmpty()) {
                    Toast.makeText(passcodeDialog.getContext(), "Enter Mobile No", Toast.LENGTH_SHORT).show();
                } else {
                    sendResendOTP(emailid);
                }
            }
        });

        passcodeDialog.show();

    }

    public void sendLoginEvent(String email, String password, final String eventid, String registration_id, String platform, String device, String os_version, String app_version) {
        mAPIService.LoginPost(/*email, password,*/ eventid, registration_id, platform, device, os_version, accessToken, app_version).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponseLogin(response, eventid);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponseLogin(Response<Login> response, String eventid) {

        if (response.body().getStatus().equals("success")) {


            SessionManager sessionManager = new SessionManager(this);

            String firstname = response.body().getUserData().getFirstName();
            String lastname = response.body().getUserData().getLastName();
            String email = response.body().getUserData().getEmail();
            String mobile = response.body().getUserData().getMobile();
            String designation = response.body().getUserData().getDesignation();
            String company = response.body().getUserData().getCompanyName();
            String token = response.body().getUserData().getApiAccessToken();
            String desc = response.body().getUserData().getDescription();
            String city = response.body().getUserData().getCity();
            String country = response.body().getUserData().getCountry();
            String pic = response.body().getUserData().getProfilePic();
            String id = response.body().getUserData().getAttendeeId();
            String attendee_status = response.body().getUserData().getAttendee_status();
            String exhibitor_id = response.body().getUserData().getExhibitor_id();
            String exhibitor_status = response.body().getUserData().getExhibitor_status();

            sessionManager.createLoginSession(firstname, lastname, email, mobile, company, designation, token, desc, city, country, pic, id, emailid, password, "1", attendee_status, exhibitor_id, exhibitor_status);
            SessionManager.saveSharedPreferencesEventList(response.body().getEventSettingList());
            SessionManager.saveSharedPreferencesMenuEventList(response.body().getEventMenuSettingList());

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_LOGIN, MODE_PRIVATE);
            String login = prefs.getString("loginfirst", "");

            SharedPreferences prefs2 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String profilePic = prefs2.getString(KEY_EVENT_PROFILE_PATH, "");
            String eventLogo = prefs2.getString(KEY_EVENT_LIST_LOGO_PATH, "");

           /* Intent event = new Intent(getApplicationContext(), EventChooserActivity.class);
            event.putExtra("email", emailid);
            event.putExtra("password", password);
            event.putExtra("accesstiken", response.body().getUserData().getApiAccessToken());
            startActivity(event);
            finish();*/

            if (!(response.body().getUserData().equals(null))) {

                try {

                    passwordOtp = response.body().getUserData().getPassword_key();
                    mobilenumber = response.body().getUserData().getMobile();
                    emailid = response.body().getUserData().getEmail();
                    accessToken = response.body().getUserData().getApiAccessToken();
                    Log.d("Password Key==>", passwordOtp);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (response.body().getUserData() != null) {
                SharedPreferences prefsbuddy = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor2 = prefsbuddy.edit();
                editor2.putString("buddy_tc_accepted", response.body().getUserData().getBuddy_accept_terms());
                editor2.commit();
            }

            SharedPreferences.Editor editor1 = getSharedPreferences(MY_PREFS_CATEGORY, MODE_PRIVATE).edit();
            editor1.putString("categorycnt", response.body().getCategory_count()).commit();
            editor1.apply();

            if (login.equalsIgnoreCase("1")) {
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("eventid", eventid).commit();
                editor.putString("eventnamestr", eventnamestr).commit();
                editor.putString("logoImg", logoImg).commit();
                editor.putString("colorActive", colorActive).commit();
                editor.putString("eventback", background).commit();
                editor.apply();

                Intent home = new Intent(getApplicationContext(), ProfileActivity.class);
                home.putExtra("back", eventid);
                startActivity(home);
                finish();
            } else {
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("eventid", eventid).commit();
                editor.putString("eventnamestr", eventnamestr).commit();
                editor.putString("logoImg", logoImg).commit();
                editor.putString("colorActive", colorActive).commit();
                editor.apply();


                Intent home = new Intent(getApplicationContext(), WelcomeVideo.class);
                home.putExtra("back", eventid);
                startActivity(home);
                finish();
            }

        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    private class getGCMRegId extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {


            try {
                //gcmRegID = gcmRegistrationHelper.GCMRegister(REG_ID);
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d("MYTAG", "This is your Firebase token" + token);

                gcmRegID = token;

                session.storeGcmID(gcmRegID);

                // storeRegistrationId(getApplicationContext(), gcmRegID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // session = new SessionManagement(getApplicationContext());
            // if (session.isLoggedIn()) {
            // Update GCM ID to Server
            // new updateGCMRegId().execute();
            // }

        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("TAG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }



    private void underLineTextview() {

        TextView textView = findViewById(R.id.text_powered);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setText("Powered By Procialize");

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://www.theeventapp.in/terms-of-use";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                finish();
            }
        });
    }
}
