package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Contacts;
import android.provider.ContactsContract;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.GetterSetter.QRPost;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;
//import ezvcard.Ezvcard;
//import ezvcard.VCard;

public class QRScanActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    public static final int RequestPermissionCode = 8;
    //    private ScrollView contactll;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "QRCScanner-MainActivity";

    TextInputEditText edit_username_edit, edit_first_name_edit, edit_designation_edit, edit_company_name_edit, edit_city_edit, edit_mobile_edit, edit_email_edit;
    Button save_btn_qr;
    String name, number, designation, company, city, email, lname, fname;
    QRCodeReaderView qrCodeReaderView;
    ImageView headerlogoIv;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    TextView txt_heading;
    LinearLayout linear;
    private APIService mAPIService;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_qrscan);
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        mAPIService = ApiUtils.getAPIService();

        if (!CheckingPermissionIsEnabledOrNot()) {
            RequestMultiplePermission();
        }
//        contactll = findViewById(R.id.contactll);

        edit_username_edit = findViewById(R.id.edit_username_edit);
        edit_first_name_edit = findViewById(R.id.edit_first_name_edit);
        edit_designation_edit = findViewById(R.id.edit_designation_edit);
        edit_company_name_edit = findViewById(R.id.edit_company_name_edit);
        edit_city_edit = findViewById(R.id.edit_city_edit);
        edit_mobile_edit = findViewById(R.id.edit_mobile_edit);
        edit_email_edit = findViewById(R.id.edit_email_edit);
        txt_heading = findViewById(R.id.txt_heading);
        linear = findViewById(R.id.linear);

        save_btn_qr = findViewById(R.id.save_btn_qr);

        try {
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }


        qrCodeReaderView = ( QRCodeReaderView ) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        qrCodeReaderView.setVisibility(View.VISIBLE);
//        contactll.setVisibility(View.GONE);


        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);

        crashlytics("QR Scan",token);
        firbaseAnalytics(this, "QR Scan", token);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        int colorInt = Color.parseColor(colorActive);
        save_btn_qr.setBackgroundColor(colorInt);
        txt_heading.setTextColor(colorInt);
        save_btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_username_edit = findViewById(R.id.edit_username_edit);
                edit_first_name_edit = findViewById(R.id.edit_first_name_edit);
                edit_designation_edit = findViewById(R.id.edit_designation_edit);
                edit_company_name_edit = findViewById(R.id.edit_company_name_edit);
                edit_city_edit = findViewById(R.id.edit_city_edit);
                edit_mobile_edit = findViewById(R.id.edit_mobile_edit);
                edit_email_edit = findViewById(R.id.edit_email_edit);

                try {
                    QRScanPost(token, eventid, name, lname, edit_mobile_edit.getText().toString(), email);


                    if (number.isEmpty()) {
                        Intent addContactIntent = new Intent(Contacts.Intents.Insert.ACTION, Contacts.People.CONTENT_URI);
                        addContactIntent.putExtra(Contacts.Intents.Insert.NAME, name);
                        addContactIntent.putExtra(Contacts.Intents.Insert.COMPANY, company);
                        addContactIntent.putExtra(Contacts.Intents.Insert.JOB_TITLE, designation);
                        addContactIntent.putExtra(Contacts.Intents.Insert.EMAIL, email);
                        startActivity(addContactIntent);
                    } else {
                        Intent intent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, Uri.parse("tel:" + number));
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
                        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, designation);
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                        intent.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
//            }
        });
    }


    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        Log.e("text", text);

        String parts[] = text.split("\\r?\\n");
        Log.e("text", parts[0]);
        if (parts.length == 6) {

            name = parts[0].substring(0, (parts[0].length()));
            lname = parts[1].substring(0, (parts[1].length()));
            designation = parts[2].substring(0, (parts[2].length()));
            email = parts[3].substring(0, (parts[3].length()));
            number = parts[4].substring(0, (parts[4].length()));
            company = parts[5].substring(0, (parts[5].length()));

            edit_username_edit.setText(name);
            edit_first_name_edit.setText(lname);
            edit_designation_edit.setText(designation);
            edit_company_name_edit.setText(company);
            edit_mobile_edit.setText(number);
            edit_city_edit.setText(city);
            edit_email_edit.setText(email);

            if (number == null) {
                edit_mobile_edit.setEnabled(true);
            } else {
                edit_mobile_edit.setEnabled(false);
            }
        } else if (parts.length == 7) {

            name = parts[0].substring(0, (parts[0].length()));
            lname = parts[1].substring(0, (parts[1].length()));
            designation = parts[2].substring(0, (parts[2].length()));
            email = parts[3].substring(0, (parts[3].length()));
            number = parts[4].substring(0, (parts[4].length()));
            company = parts[5].substring(0, (parts[5].length()));
            city = parts[6].substring(0, (parts[6].length()));


            edit_username_edit.setText(name);
            edit_first_name_edit.setText(lname);
//            edit_first_name_edit.setText(fname + " " + lname);
            edit_designation_edit.setText(designation);
            edit_company_name_edit.setText(company);
            edit_mobile_edit.setText(number);
            edit_city_edit.setText(city);
            edit_email_edit.setText(email);

            if (number == null) {
                edit_mobile_edit.setEnabled(true);
            } else {
                edit_mobile_edit.setEnabled(false);
            }

        } else if (parts.length == 8) {

            name = parts[0].substring(0, (parts[0].length()));
            lname = parts[1].substring(0, (parts[1].length()));
            designation = parts[2].substring(0, (parts[2].length()));
            email = parts[3].substring(0, (parts[3].length()));
            number = parts[4].substring(0, (parts[4].length()));
            company = parts[5].substring(0, (parts[5].length()));
            city = parts[6].substring(0, (parts[6].length()));


            edit_username_edit.setText(name);
            edit_first_name_edit.setText(lname);
            edit_designation_edit.setText(designation);
            edit_company_name_edit.setText(company);
            edit_mobile_edit.setText(number);
            edit_city_edit.setText(city);
            edit_email_edit.setText(email);


            if (number == null) {
                edit_mobile_edit.setEnabled(true);
            } else {
                edit_mobile_edit.setEnabled(false);
            }

        } else if (parts.length == 9) {

            name = parts[0].substring(0, (parts[0].length()));
            lname = parts[1].substring(0, (parts[1].length()));
            designation = parts[2].substring(0, (parts[2].length()));
            email = parts[3].substring(0, (parts[3].length()));
            number = parts[4].substring(0, (parts[4].length()));
            company = parts[5].substring(0, (parts[5].length()));
            city = parts[6].substring(0, (parts[6].length()));


            edit_username_edit.setText(name);
            edit_first_name_edit.setText(lname);
            edit_designation_edit.setText(designation);
            edit_company_name_edit.setText(company);
            edit_mobile_edit.setText(number);
            edit_city_edit.setText(city);
            edit_email_edit.setText(email);

            if (number == null) {
                edit_mobile_edit.setEnabled(true);
            } else {
                edit_mobile_edit.setEnabled(false);
            }

        } else {
            try {
                name = parts[0].substring(0, (parts[0].length()));
            } catch (Exception e) {
                name = "";
            }

            try {
                lname = parts[1].substring(0, (parts[1].length()));
            } catch (Exception e) {
                lname = "";
            }

            try {
                designation = parts[2].substring(0, (parts[2].length()));
            } catch (Exception e) {
                designation = "";
            }

            try {
                email = parts[3].substring(0, (parts[3].length()));
            } catch (Exception e) {
                email = "";
            }

            try {
                number = parts[4].substring(0, (parts[4].length()));
            } catch (Exception e) {
                number = "";
            }

            try {
                company = parts[5].substring(0, (parts[5].length()));
            } catch (Exception e) {
                company = "";
            }

            try {
                city = parts[6].substring(0, (parts[6].length()));
            } catch (Exception e) {
                city = "";
            }


            edit_username_edit.setText(name);
            edit_first_name_edit.setText(lname);
            edit_designation_edit.setText(designation);
            edit_company_name_edit.setText(company);
            edit_mobile_edit.setText(number);
            edit_city_edit.setText(city);
            edit_email_edit.setText(email);

            if (number == null) {
                edit_mobile_edit.setEnabled(true);
            } else {
                edit_mobile_edit.setEnabled(false);
            }
        }

        qrCodeReaderView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
        qrCodeReaderView.stopCamera();
    }

    public void QRScanPost(String token, String eventid, String first_name, String last_name, String contact_number, String email) {

        mAPIService.QRScanDataPost(token, eventid, first_name, last_name, contact_number, email).enqueue(new Callback<QRPost>() {
            @Override
            public void onResponse(Call<QRPost> call, Response<QRPost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    showResponse(response);
                } else {

                    Toast.makeText(QRScanActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QRPost> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");

                Toast.makeText(QRScanActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showResponse(Response<QRPost> response) {
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            /*Toast.makeText(QRScanActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(QRScanActivity.this, QRScanActivity.class);
            startActivity(intent);*/
        } else {

        }

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode != Activity.RESULT_OK) {
//            Log.d(LOGTAG, "COULD NOT GET A GOOD RESULT.");
//            if (data == null)
//                return;
//            //Getting the passed result
//            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
//            if (result != null) {
//                Log.e("result", result);
//                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//                alertDialog.setTitle("Scan Error");
//                alertDialog.setMessage("QR Code could not be scanned");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.show();
//            }
//            return;
//
//        }
//        if (requestCode == REQUEST_CODE_QR_SCAN) {
//            if (data == null)
//                return;
//            //Getting the passed result
//            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
//
////            Log.e("result", result);
//
////            VCard vcard = Ezvcard.parse(result).first();
////            Log.e("result",vcard+"");
//
//            if (result != null) {
//                if (contactll.getVisibility() == View.GONE) {
//                    contactll.setVisibility(View.VISIBLE);
//
////                try {
////                    if (vcard!=null) {
////                        edit_username_edit.setText(vcard.getStructuredNames().get(0).getFamily());
////                        edit_first_name_edit.setText(vcard.getStructuredNames().get(0).getFamily());
////                        edit_designation_edit.setText("");
////                        edit_company_name_edit.setText(vcard.getOrganization().getValues().get(0));
////                        edit_city_edit.setText(vcard.getAddresses().get(0).getPoBox());
////                        edit_mobile_edit.setText(vcard.getTelephoneNumbers().get(0).getText());
////
////                        name = vcard.getStructuredNames().get(0).getFamily();
////                        number = vcard.getTelephoneNumbers().get(0).getText();
////                    }else
////                    {
//                    String lines[] = result.split("\\r?\\n");
//
//                    String username = lines[2].split(":")[1];
//                    String company = lines[3].split(":")[1];
//                    String designation = lines[4].split(":")[1];
//                    String mobile = lines[5].split(":")[1];
//                    String email = lines[6].split(":")[1];
//                    String city = lines[7].split(":")[1];
//
//                    name = username;
//                    number = mobile;
//                    edit_first_name_edit.setText(username);
//                    edit_designation_edit.setText(designation);
//                    edit_company_name_edit.setText(company);
//                    edit_city_edit.setText(city);
//                    edit_mobile_edit.setText(mobile);
//                    edit_email_edit.setText(email);
//
////                        Log.e("lines",lines[0]);
////                        Log.e("lines",lines[1]);
////                        Log.e("lines",lines[2]);
////                    }
////
////                }catch (Exception e)
////                {
////                    e.printStackTrace();
////                }
//                }
////            Log.d(LOGTAG,"Have scan result in your app activity :"+ result);
////            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
////            alertDialog.setTitle("Scan result");
////            alertDialog.setMessage(result);
////            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
////                    new DialogInterface.OnClickListener() {
////                        public void onClick(DialogInterface dialog, int which) {
////                            dialog.dismiss();
////                        }
////                    });
////            alertDialog.show();
//
//            }else
//            {
//                finish();
//            }
//        }
//    }

    public void addToContactList(Context context, String strDisplayName, String strNumber, String email, String des, String company,
                                 String city) throws Exception {

        ArrayList<ContentProviderOperation> cntProOper = new ArrayList<>();
        int contactIndex = cntProOper.size();//ContactSize
        ContentResolver contactHelper = context.getContentResolver();

        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)//Step1
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        //Display name will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step2
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, strDisplayName) // Name of the contact
                .build());

//        for (String s : strNumber) {
//            //Mobile number will be inserted in ContactsContract.Data table
//            cntProOper.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)//Step 3
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
//                    .withValue(android.provider.ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, s) // Number to be added
//                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
//        }

        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, strNumber) // Number to be added
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, des) // Number to be added
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK).build());

        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company) // Number to be added
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK).build());

        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.SipAddress.DATA, city) // Number to be added
                .withValue(ContactsContract.CommonDataKinds.SipAddress.TYPE, ContactsContract.CommonDataKinds.SipAddress.SIP_ADDRESS).build());

        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email) // Number to be added
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK).build());


        ContentProviderResult[] s = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper); //apply above data insertion into contacts list

        for (ContentProviderResult r : s) {
            Log.i("hey", "addToContactList: " + r.uri);
        }

        Toast.makeText(this, "Contact Save Successfully", Toast.LENGTH_SHORT).show();
        finish();


    }



/*
    public void addToContactList(Context context, String strDisplayName, String strNumber) throws Exception {

        ArrayList<ContentProviderOperation> cntProOper = new ArrayList<>();
        int contactIndex = cntProOper.size();//ContactSize
        ContentResolver contactHelper = context.getContentResolver();

        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)//Step1
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        //Display name will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)//Step2
                .withValueBackReference(android.provider.ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(android.provider.ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, strDisplayName) // Name of the contact
                .build());

//        for (String s : strNumber) {
//            //Mobile number will be inserted in ContactsContract.Data table
//            cntProOper.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)//Step 3
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
//                    .withValue(android.provider.ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, s) // Number to be added
//                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
//        }

        cntProOper.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)//Step 3
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(android.provider.ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, strNumber) // Number to be added
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

        ContentProviderResult[] s = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper); //apply above data insertion into contacts list

        for (ContentProviderResult r : s) {
            Log.i("hey", "addToContactList: " + r.uri);
        }

        Toast.makeText(this, "Contact Save Successfully", Toast.LENGTH_SHORT).show();
        finish();


    }
*/

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CONTACTS);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(QRScanActivity.this, new String[]
                {
                        WRITE_CONTACTS,
                        READ_CONTACTS,
                        CAMERA
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
                    boolean camerapermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (readContactPermission && writeContactpermjission && camerapermission) {
//                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(QRScanActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }


}