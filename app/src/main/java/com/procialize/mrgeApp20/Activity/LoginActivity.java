package com.procialize.mrgeApp20.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.CustomTools.Connectivity;
import com.procialize.mrgeApp20.GetterSetter.EventListing;
import com.procialize.mrgeApp20.GetterSetter.Forgot;
import com.procialize.mrgeApp20.R;

import cn.jzvd.JZVideoPlayerStandard;
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
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class LoginActivity extends AppCompatActivity {


    public static final int RequestPermissionCode = 8;
    Boolean emailbool = false, passwordbool = false;
    ProgressBar progressBar2;
    Button loginbtn, createaccbtn;
    String emailid, password;
    ImageView headerlogoIv;
    Dialog myDialog;
    private APIService mAPIService;
    CheckBox chk_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


        mAPIService = ApiUtils.getAPIService();
        Fabric.with(this, new Crashlytics());
        underLineTextview();
        initializeView();
    }


    private void underLineTextview() {

        TextView textView = findViewById(R.id.textView);
        TextView txt_termslink = findViewById(R.id.txt_termslink);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_termslink.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setText("Designed & Developed by Procialize");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://www.procialize.net";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
//                finish();
            }
        });
        txt_termslink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = ApiConstant.imgURL + "eula.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
//                finish();
            }
        });
    }

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
                    Toast.makeText(LoginActivity.this, "Enter Email Id", Toast.LENGTH_SHORT).show();
                } else if (Etpassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } /*else if (!(chk_box.isChecked())) {
                    Toast.makeText(LoginActivity.this, "Please accept terms & conditions", Toast.LENGTH_SHORT).show();
                } */else {
//                    inputLayoutpassword.setError(null);
//                    inputLayoutemail.setError(null);

                    if (Connectivity.isConnected(LoginActivity.this)) {
                        emailid = Etemail.getText().toString();
                        password = Etpassword.getText().toString();
                        showProgress();
                        sendEventList(Etemail.getText().toString(), Etpassword.getText().toString());
                    } else {
                        Toast.makeText(LoginActivity.this, "You are not connected to Internet for processing", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        crashlytics("Login","");
        firbaseAnalytics(this,"Login","");
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

    public void sendEventList(String email, String password) {
        mAPIService.EventListPost(email, password).enqueue(new Callback<EventListing>() {
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

            Intent event = new Intent(getApplicationContext(), EventChooserActivity.class);
            event.putExtra("email", emailid);
            event.putExtra("password", password);
            event.putExtra("accesstiken", response.body().getUserData().getApiAccessToken());
            startActivity(event);
            finish();

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
                {
                        CAMERA,
                        WRITE_CONTACTS,
                        READ_CONTACTS,
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE,
                        VIBRATE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readcontactpermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writecontactpermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean readstoragepermjission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean writestoragepermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean vibratepermission = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && readcontactpermission && writecontactpermission && readstoragepermjission && writestoragepermission && vibratepermission) {

//                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
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
        JZVideoPlayerStandard.releaseAllVideos();
        ActivityCompat.finishAffinity(LoginActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayerStandard.releaseAllVideos();
    }
}
