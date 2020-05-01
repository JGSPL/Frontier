package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.mrgeApp20.Adapter.SelfieAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.GetterSetter.DeleteSelfie;
import com.procialize.mrgeApp20.GetterSetter.ReportSelfie;
import com.procialize.mrgeApp20.GetterSetter.ReportSelfieHide;
import com.procialize.mrgeApp20.GetterSetter.SelfieLike;
import com.procialize.mrgeApp20.GetterSetter.SelfieList;
import com.procialize.mrgeApp20.GetterSetter.SelfieListFetch;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class SelfieContestActivity extends AppCompatActivity implements SelfieAdapter.SelfieAdapterListner {

    Button uploadbtn;
    SwipeRefreshLayout selfiefeedrefresh;
    ProgressBar progressBar;
    RecyclerView selfierecycler;
    String token;
    SelfieAdapter selfieAdapter;
    BottomSheetDialog dialog;
    Dialog myDialog;
    List<SelfieList> selfieLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    String user_id, colorActive;
    ImageView headerlogoIv;
    TextView header, seldescription;
    private APIService mAPIService;
    LinearLayout linear;
    TextView pullrefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_contest);
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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
//                onBackPressed();
//                Intent intent = new Intent(SelfieContestActivity.this, EngagementActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        uploadbtn = findViewById(R.id.uploadbtn);
        linear = findViewById(R.id.linear);
        selfiefeedrefresh = findViewById(R.id.selfiefeedrefresh);
        selfierecycler = findViewById(R.id.selfierecycler);
        header = findViewById(R.id.title);
        pullrefresh = findViewById(R.id.pullrefresh);
        seldescription = findViewById(R.id.seldescription);
        header.setTextColor(Color.parseColor(colorActive));
        pullrefresh.setTextColor(Color.parseColor(colorActive));
        uploadbtn.setBackgroundColor(Color.parseColor(colorActive));

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
        selfieLists = new ArrayList<>();

        int columns = 2;
        selfierecycler.setLayoutManager(new GridLayoutManager(this, columns));

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        selfierecycler.setLayoutAnimation(animation);


        progressBar = findViewById(R.id.progressBar);

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();


        user_id = user.get(SessionManager.KEY_ID);
        // token
        token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Selfie Contest",user.get(SessionManager.KEY_TOKEN));
        firbaseAnalytics(this, "Selfie Contest", token);
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selfie = new Intent(SelfieContestActivity.this, SelfieUploadActivity.class);
                startActivity(selfie);
                finish();
            }
        });
        selfiefeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                SelfieListFetch(token, eventid);
            }
        });

        SelfieListFetch(token, eventid);

    }

    public void SelfieListFetch(String token, String eventid) {
        showProgress();
        mAPIService.SelfieListFetch(token, eventid).enqueue(new Callback<SelfieListFetch>() {
            @Override
            public void onResponse(Call<SelfieListFetch> call, Response<SelfieListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (selfiefeedrefresh.isRefreshing()) {
                        selfiefeedrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {
                    if (selfiefeedrefresh.isRefreshing()) {
                        selfiefeedrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SelfieListFetch> call, Throwable t) {
                // Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (selfiefeedrefresh.isRefreshing()) {
                    selfiefeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<SelfieListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {

            selfieLists = response.body().getSelfieList();

            selfieAdapter = new SelfieAdapter(this, response.body().getSelfieList(), this);
            selfieAdapter.notifyDataSetChanged();
            selfierecycler.setAdapter(selfieAdapter);
            selfierecycler.scheduleLayoutAnimation();

            try {
                if (!(response.body().getSelfie_title().equalsIgnoreCase(null))) {
                    header.setText(response.body().getSelfie_title());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!(response.body().getSelfie_description().equalsIgnoreCase(null))) {
                    seldescription.setText(response.body().getSelfie_description());
                    seldescription.setVisibility(View.VISIBLE);

                } else {
                    seldescription.setVisibility(View.GONE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();

        }
    }

    public void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onContactSelected(SelfieList selfieList, ImageView ivProfile) {


        Intent imageview = new Intent(this, SwappingSelfieActivity.class);
        imageview.putExtra("url", selfieList.getFileName());
        imageview.putExtra("gallerylist", (Serializable) selfieLists);
        startActivity(imageview);


//        imageview.putExtra("url", ApiConstant.selfievideo + selfieList.getFileName());
//        ActivityOptionsCompat options = ActivityOptionsCompat.
//                makeSceneTransitionAnimation(this, (View)ivProfile, "profile");
//        startActivity(imageview,options.toBundle());
    }

    @Override
    public void onLikeListener(View v, SelfieList selfieList, int position, TextView countTv, ImageView likeIv) {

        if (selfieList.getLikeFlag().equals("1")) {
            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_like));
            likeIv.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);


            SelfieLike(token, eventid, selfieList.getId());

            try {

                int count = Integer.parseInt(selfieList.getTotalLikes());

                if (count > 0) {
                    count = count - 1;
                    countTv.setText(count + "");

                } else {
                    countTv.setText("0");
                }
//                SelfieLike(token,"1",selfieList.getId());/**/
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_afterlike));
            likeIv.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);
            SelfieLike(token, eventid, selfieList.getId());


            try {

                int count = Integer.parseInt(selfieList.getTotalLikes());


                count = count + 1;
                countTv.setText(count + "");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onMoreListner(View v, final SelfieList selfieList, final int position) {

        dialog = new BottomSheetDialog(this);

        dialog.setContentView(R.layout.botomcontestdialouge);


        TextView reportTv = dialog.findViewById(R.id.reportTv);
        TextView hideTv = dialog.findViewById(R.id.hideTv);
        TextView deleteTv = dialog.findViewById(R.id.deleteTv);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);

        reportTv.setText("Report Selfie");
        deleteTv.setText("Delete this Selfie");
        hideTv.setText("Hide Selfie");

        if (selfieList.getAttendee_id().equalsIgnoreCase(user_id)) {
            deleteTv.setVisibility(View.VISIBLE);
            hideTv.setVisibility(View.GONE);
            reportTv.setVisibility(View.GONE);
        } else {
            deleteTv.setVisibility(View.GONE);
            hideTv.setVisibility(View.VISIBLE);
            reportTv.setVisibility(View.VISIBLE);
        }


        hideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportSelfieHide(eventid, selfieList.getId(), token, position);
            }
        });
        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteSelfie(eventid, selfieList.getId(), token, position);
            }
        });


        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge(selfieList.getId());
                dialog.dismiss();
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void SelfieLike(String token, String eventid, String id) {
        mAPIService.SelfieLike(token, eventid, id).enqueue(new Callback<SelfieLike>() {
            @Override
            public void onResponse(Call<SelfieLike> call, Response<SelfieLike> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    showLikeResponse(response);
                } else {

                    Toast.makeText(SelfieContestActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<SelfieLike> call, Throwable t) {
                Toast.makeText(SelfieContestActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void showLikeResponse(Response<SelfieLike> response) {

        if (response.body().getStatus().equalsIgnoreCase("success")) {
            SelfieListFetch(token, eventid);
//            Toast.makeText(SelfieContestActivity.this,response.message(),Toast.LENGTH_SHORT).show();

        } else {
//            Toast.makeText(SelfieContestActivity.this,response.message(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        SelfieListFetch(token, eventid);
        super.onResume();
    }


    public void ReportSelfieHide(String eventid, String selfieid, String token, final int position) {
//        showProgress();
        mAPIService.ReportSelfieHide(token, eventid, selfieid).enqueue(new Callback<ReportSelfieHide>() {
            @Override
            public void onResponse(Call<ReportSelfieHide> call, Response<ReportSelfieHide> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportPostHideresponse(response, position);
                } else {
//                    dismissProgress();

                    Toast.makeText(SelfieContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportSelfieHide> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(SelfieContestActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    public void DeleteSelfie(String eventid, String selfieid, String token, final int position) {
//        showProgress();
        mAPIService.DeleteSelfie(token, eventid, selfieid).enqueue(new Callback<DeleteSelfie>() {
            @Override
            public void onResponse(Call<DeleteSelfie> call, Response<DeleteSelfie> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    DeleteSelfieResponse(response, position);
                } else {
//                    dismissProgress();

                    Toast.makeText(SelfieContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteSelfie> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(SelfieContestActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }


    private void ReportPostHideresponse(Response<ReportSelfieHide> response, int position) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

            selfieAdapter.selfieList.remove(position);
            selfieAdapter.notifyItemRemoved(position);
            dialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(SelfieContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void DeleteSelfieResponse(Response<DeleteSelfie> response, int position) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

            selfieAdapter.selfieList.remove(position);
            selfieAdapter.notifyItemRemoved(position);
            dialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(SelfieContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    public void ReportSelfie(String eventid, String feedid, String token, String text) {
//        showProgress();
        mAPIService.ReportSelfie(token, eventid, feedid, text).enqueue(new Callback<ReportSelfie>() {
            @Override
            public void onResponse(Call<ReportSelfie> call, Response<ReportSelfie> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportPostresponse(response);
                } else {
//                    dismissProgress();

                    Toast.makeText(SelfieContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportSelfie> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(SelfieContestActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportPostresponse(Response<ReportSelfie> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Toast.makeText(SelfieContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

            myDialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(SelfieContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            myDialog.dismiss();
        }
    }


    private void showratedialouge(final String id) {

        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);
        ratebtn.setText("Report User");

        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);
        final TextView title = myDialog.findViewById(R.id.title);
        final ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);
        title.setText("Report USer");

        nametv.setText("To " + "Admin");

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
        imgCancel.setOnClickListener(new View.OnClickListener() {
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
                    dialog.dismiss();
                    ReportSelfie(eventid, id, token, msg);
                } else {
                    Toast.makeText(SelfieContestActivity.this, "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
