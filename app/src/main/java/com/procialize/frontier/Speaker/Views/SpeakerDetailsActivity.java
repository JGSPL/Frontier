package com.procialize.frontier.Speaker.Views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.Downloads.Activity.DownloadPdfActivity;
import com.procialize.frontier.GetterSetter.Analytic;
import com.procialize.frontier.GetterSetter.EventSettingList;
import com.procialize.frontier.Speaker.Models.PdfList;
import com.procialize.frontier.GetterSetter.RatingSpeakerPost;
import com.procialize.frontier.InnerDrawerActivity.NotificationActivity;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Speaker.Views.Adapter.PdfListAdapter;
import com.procialize.frontier.Utility.Util;
import com.procialize.frontier.util.GetUserActivityReport;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.frontier.Session.ImagePathConstants.KEY_ATTENDEE_PIC_PATH;
import static com.procialize.frontier.Utility.Util.setNotification;
import static com.procialize.frontier.Utility.Utility.setgradientDrawable;

public class SpeakerDetailsActivity extends AppCompatActivity implements PdfListAdapter.PdfListAdapterListner {

    String speakerid, city, country, company, designation, description, totalrating, name, profile, mobile,pdf_file_path;
    List<PdfList> pdf_list;
    TextView tvdesc, tvname, tvcompany, tvdesignation, tvcity, speakertitle, tvmobile;
    ImageView profileIV;
    Button ratebtn;
    APIService mAPIService;
    SessionManager sessionManager;
    String apikey, attendeeid;
    float ratingval;
    Dialog myDialog;
    String speaker_rating, speaker_designation, speaker_company, speaker_location, speaker_mobile;
    List<EventSettingList> eventSettingLists;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    View viewthree, viewtwo, viewone, viewfive, viewfour;
    RelativeLayout ratinglayout, layoutTop;
    RatingBar ratingbar;
    ImageView headerlogoIv;
    Typeface typeface;
    RelativeLayout linear;
    RecyclerView rv_pdf_list;
    PdfListAdapter pdfListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_details);
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


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

       // toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);


        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        typeface = Typeface.createFromAsset(getAssets(),
                "DINPro-Regular.ttf");

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(SpeakerDetailsActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);
        attendeeid = user.get(SessionManager.KEY_ID);

        eventSettingLists = SessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

        try {
            speakerid = getIntent().getExtras().getString("id");
            name = getIntent().getExtras().getString("name");
            city = getIntent().getExtras().getString("city");
            country = getIntent().getExtras().getString("country");
            company = getIntent().getExtras().getString("company");
            designation = getIntent().getExtras().getString("designation");
            description = getIntent().getExtras().getString("description");
            totalrating = getIntent().getExtras().getString("totalrating");
            profile = getIntent().getExtras().getString("profile");
            mobile = getIntent().getExtras().getString("mobile");
            pdf_file_path = getIntent().getExtras().getString("pdf_file_path");
            pdf_list =(List<PdfList>) getIntent().getExtras().getSerializable("pdf_list");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SubmitAnalytics(apikey, eventid, "", "", "speakerDetail");
        tvname = findViewById(R.id.tvname);
        tvdesc = findViewById(R.id.tvdesc);
        tvdesc.setMovementMethod(new ScrollingMovementMethod());
        tvdesc.setTypeface(typeface);
        tvcompany = findViewById(R.id.tvcompany);
        tvdesignation = findViewById(R.id.tvdesignation);
        tvmobile = findViewById(R.id.tvmobile);
        speakertitle = findViewById(R.id.speakertitle);
        tvcity = findViewById(R.id.tvcity);
        profileIV = findViewById(R.id.profileIV);
        viewone = findViewById(R.id.viewone);
        viewtwo = findViewById(R.id.viewtwo);
        viewfive = findViewById(R.id.viewfive);
        viewthree = findViewById(R.id.viewthree);
        viewfour = findViewById(R.id.viewfour);
        ratinglayout = findViewById(R.id.ratinglayout);
        ratingbar = findViewById(R.id.ratingbar);
        layoutTop = findViewById(R.id.layoutTop);
        linear = findViewById(R.id.linear);
        rv_pdf_list = findViewById(R.id.rv_pdf_list);
        ratebtn = findViewById(R.id.ratebtn);

        LinearLayout rate2 = findViewById(R.id.rate2);
        rate2.setBackgroundColor(Color.parseColor(colorActive));
        ratebtn.setTextColor(Color.parseColor(colorActive));

        progressBar = findViewById(R.id.progressBar);

        ImageView notificationlogoIv = findViewById(R.id.notificationlogoIv);
        notificationlogoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(main);
                finish();
            }
        });


        pdfListAdapter = new PdfListAdapter(this,pdf_list,this);
        rv_pdf_list.setLayoutManager(new GridLayoutManager(this, 2));
        rv_pdf_list.setAdapter(pdfListAdapter);


        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
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
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

      //  tvname.setTextColor(Color.parseColor(colorActive));
        speakertitle.setTextColor(Color.parseColor(colorActive));

        GradientDrawable shape = setgradientDrawable(5, colorActive);

       // ratebtn.setBackground(shape);

       // layoutTop.setBackgroundColor(Color.parseColor(colorActive));
        LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#fbc375"),
                PorterDuff.Mode.SRC_ATOP);


        if (name != null) {
            if (name.equalsIgnoreCase("N A")) {
                tvname.setVisibility(View.GONE);
                viewone.setVisibility(View.GONE);
            } else {
                tvname.setText(name);
            }
        } else {
            tvname.setVisibility(View.GONE);
            viewone.setVisibility(View.GONE);
        }

        if (description != null) {
            if (description.equalsIgnoreCase("")) {
                tvdesc.setVisibility(View.GONE);
                viewfive.setVisibility(View.GONE);
            } else if (description.equalsIgnoreCase(" ")) {
                tvdesc.setVisibility(View.GONE);
                viewfive.setVisibility(View.GONE);
            } else {
                tvdesc.setText(description);
            }
        } else {
            tvdesc.setVisibility(View.GONE);
            viewfive.setVisibility(View.GONE);
        }

        if (company != null && speaker_company.equalsIgnoreCase("1")) {
            if (company.equalsIgnoreCase("")) {
                tvcompany.setVisibility(View.GONE);
                viewthree.setVisibility(View.GONE);
            } else {
                tvcompany.setText(company);
                viewtwo.setVisibility(View.VISIBLE);
            }

        } else {
            tvcompany.setVisibility(View.GONE);
            viewthree.setVisibility(View.GONE);
        }

        if (designation != null && speaker_designation.equalsIgnoreCase("1")) {
            if (designation.equalsIgnoreCase("")) {
                tvdesignation.setVisibility(View.GONE);
                viewtwo.setVisibility(View.GONE);
//                viewone.setVisibility(View.GONE);
            } else {
                tvdesignation.setText(designation);
            }

        } else {
            tvdesignation.setVisibility(View.GONE);
            viewtwo.setVisibility(View.GONE);
//            viewone.setVisibility(View.GONE);
        }

        if (city != null && speaker_location.equalsIgnoreCase("1")) {
            if (city.equalsIgnoreCase("")) {
                tvcity.setVisibility(View.GONE);
                viewfour.setVisibility(View.GONE);
            } else {
                tvcity.setText(city);
            }

        } else {
            tvcity.setVisibility(View.GONE);
            viewfour.setVisibility(View.GONE);
        }
        tvmobile.setVisibility(View.GONE);
        viewfive.setVisibility(View.GONE);
        /*if (mobile != null && speaker_mobile.equalsIgnoreCase("1")) {
            if (mobile.equalsIgnoreCase("")) {


            } else {
                tvmobile.setText(mobile);

            }

        } else {
            tvmobile.setVisibility(View.GONE);
            viewfive.setVisibility(View.GONE);

        }*/
        if (profile != null) {
            SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String picPath =  prefs1.getString(KEY_ATTENDEE_PIC_PATH,"");
            Glide.with(this).load(/*ApiConstant.profilepic*/picPath + profile).listener(new RequestListener<Drawable>() {
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
        if (speakerid.equalsIgnoreCase(attendeeid)) {
            ratebtn.setVisibility(View.GONE);
            ratinglayout.setVisibility(View.GONE);
        } else {
            ratebtn.setVisibility(View.VISIBLE);
            ratinglayout.setVisibility(View.VISIBLE);
        }

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingval = rating;
            }
        });


        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingval > 0) {
                    PostRate(eventid, String.valueOf(ratingval), apikey, speakerid, "");
                } else {
                    Toast.makeText(SpeakerDetailsActivity.this, "Please rate on a scale of 1-5 stars", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this,tv_notification,ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------

        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this, apikey,
                eventid,
                ApiConstant.pageVisited,
                "13",
                "");
        getUserActivityReport.userActivityReport();
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {
        for (int i = 0; i < eventSettingLists.size(); i++) {

           /* if (eventSettingLists.get(i).getFieldName().equals("speaker_rating")) {
                speaker_rating = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("speaker_designation")) {
                speaker_designation = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("speaker_company")) {
                speaker_company = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("speaker_location")) {
                speaker_location = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("speaker_mobile")) {
                speaker_mobile = eventSettingLists.get(i).getFieldValue();
            }*/
            if(eventSettingLists.get(i).getSub_menuList()!=null) {
                if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                    for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                        if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_rating")) {
                            speaker_rating = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                        }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_designation")) {
                            speaker_designation = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                        }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_company")) {
                            speaker_company = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                        }else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_location")) {
                            speaker_location = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                        }/*else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("speaker_mobile")) {
                            speaker_mobile = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                        }*/

                    }
                }
            }


        }
    }

    private void showratedialouge() {

        myDialog = new Dialog(SpeakerDetailsActivity.this);
        myDialog.setContentView(R.layout.dialog_rate_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);

        RatingBar ratingBar = myDialog.findViewById(R.id.ratingbar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingval = rating;
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
                if (ratingval > 0) {
                    PostRate(eventid, String.valueOf(ratingval), apikey, speakerid, "");
                } else {
                    Toast.makeText(SpeakerDetailsActivity.this, "Please rate on a scale of 1-5 stars", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void PostRate(String eventid, String rating, String token, String speakerid, String comment) {
        final ProgressDialog progressDialog = new ProgressDialog(SpeakerDetailsActivity.this);
        progressDialog.show();
//        showProgress();
        mAPIService.RatingSpeakerPost(token, eventid, speakerid, rating, comment).enqueue(new Callback<RatingSpeakerPost>() {
            @Override
            public void onResponse(Call<RatingSpeakerPost> call, Response<RatingSpeakerPost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressDialog.dismiss();
                    ratingbar.setRating(0F);
//                    dismissProgress();
                    DeletePostresponse(response);
                } else {
//                    dismissProgress();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RatingSpeakerPost> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void DeletePostresponse(Response<RatingSpeakerPost> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");
            SubmitAnalytics(apikey, eventid, "", "", "rating");
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
        //   overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

//                    Toast.makeText(SpeakerDetailsActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
//                Toast.makeText(SpeakerDetailsActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void imagealert() {
        final Dialog dialog = new Dialog(SpeakerDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
                .setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setContentView(R.layout.imagepopulayout);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
//        String imgae = dbManager.GetimageUrl(datamodel.get(position).getProdcutid());

        SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String picPath =  prefs1.getString(KEY_ATTENDEE_PIC_PATH,"");
        String imageUrl = /*ApiConstant.profilepic*/picPath + profile;
        Picasso.with(SpeakerDetailsActivity.this).load(imageUrl).into(image);
        dialog.show();
    }


    @Override
    public void onContactSelected(PdfList pdfList) {
        Intent pdfview = new Intent(SpeakerDetailsActivity.this, DownloadPdfActivity.class);
        pdfview.putExtra("url", "https://docs.google.com/gview?embedded=true&url=" + pdf_file_path + pdfList.getPdf_file());
        pdfview.putExtra("url1", pdf_file_path + pdfList.getPdf_file());
        pdfview.putExtra("doc_name",  pdfList.getPdf_name());
        pdfview.putExtra("page_id",  "49");
        pdfview.putExtra("file_id",  pdfList.getId());
        startActivity(pdfview);
    }
}