package com.procialize.mrgeApp20.Engagement.Activity;

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

import com.procialize.mrgeApp20.Adapter.VideoContestAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.GetterSetter.Analytic;
import com.procialize.mrgeApp20.GetterSetter.ReportVideoContest;
import com.procialize.mrgeApp20.GetterSetter.ReportVideoContestHide;
import com.procialize.mrgeApp20.GetterSetter.VideoContest;
import com.procialize.mrgeApp20.GetterSetter.VideoContestLikes;
import com.procialize.mrgeApp20.GetterSetter.VideoContestListFetch;
import com.procialize.mrgeApp20.InnerDrawerActivity.ExoVideoActivity;
import com.procialize.mrgeApp20.InnerDrawerActivity.VideoContestUploadActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class VideoContestActivity extends AppCompatActivity implements VideoContestAdapter.VideoContestAdapterListner {

    Button uploadbtn;
    SwipeRefreshLayout videofeedrefresh;
    ProgressBar progressBar;
    RecyclerView videorecycler;
    String token;
    VideoContestAdapter videoAdapter;
    BottomSheetDialog dialog;
    Dialog myDialog;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    String user_id, colorActive;
    ImageView headerlogoIv;
    TextView header, seldescription;
    private APIService mAPIService;
    LinearLayout liner;
    TextView pullrefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_contest);

        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "");
        colorActive = prefs.getString("colorActive", "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        uploadbtn = findViewById(R.id.uploadbtn);
        videofeedrefresh = findViewById(R.id.videofeedrefresh);
        videorecycler = findViewById(R.id.videorecycler);
        header = findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));
        seldescription = findViewById(R.id.seldescription);
        liner = findViewById(R.id.liner);
        pullrefresh = findViewById(R.id.pullrefresh);
        pullrefresh.setTextColor(Color.parseColor(colorActive));

        int columns = 2;
        videorecycler.setLayoutManager(new GridLayoutManager(this, columns));

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        videorecycler.setLayoutAnimation(animation);


        progressBar = findViewById(R.id.progressBar);


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();
        user_id = user.get(SessionManager.KEY_ID);
        token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Video Contest",user.get(SessionManager.KEY_TOKEN));
        firbaseAnalytics(this, "Video Contest",token);
        uploadbtn.setBackgroundColor(Color.parseColor(colorActive));

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent videoupload = new Intent(VideoContestActivity.this, VideoContestUploadActivity.class);
                startActivity(videoupload);
                finish();
            }
        });

        videofeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                SelfieListFetch(token, eventid);
            }
        });

        SelfieListFetch(token, eventid);


        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            liner.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            liner.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------
    }

    public void SelfieListFetch(String token, String eventid) {
        showProgress();
        mAPIService.VideoContestListFetch(token, eventid).enqueue(new Callback<VideoContestListFetch>() {
            @Override
            public void onResponse(Call<VideoContestListFetch> call, Response<VideoContestListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (videofeedrefresh.isRefreshing()) {
                        videofeedrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {
                    if (videofeedrefresh.isRefreshing()) {
                        videofeedrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoContestListFetch> call, Throwable t) {
                //  Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
                if (videofeedrefresh.isRefreshing()) {
                    videofeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<VideoContestListFetch> response) {
        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            try {
                if (!(response.body().getVideo_title().equalsIgnoreCase(null))) {
                    header.setText(response.body().getVideo_title());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!(response.body().getVideo_description().equalsIgnoreCase(null))) {
                    seldescription.setText(response.body().getVideo_description());
                    //seldescription.setVisibility(View.VISIBLE);
                } else {
                    seldescription.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            videoAdapter = new VideoContestAdapter(this, response.body().getVideoContest(), this);
            videoAdapter.notifyDataSetChanged();
            videorecycler.setAdapter(videoAdapter);
            videorecycler.scheduleLayoutAnimation();
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
    public void onContactSelected(List<VideoContest> videoContest, ImageView ivProfile) {

        //SubmitAnalytics(token, eventid, "", "", "videocontest", videoContest.getTitle());

        Intent intent = new Intent(VideoContestActivity.this, SwappingVideoActivity.class);
        intent.putExtra("gallerylist",(Serializable) videoContest);
       /* intent.putExtra("videoUrl", videoContest.getFileName());
        intent.putExtra("title", videoContest.getTitle());
        intent.putExtra("page", "contest");*/
        startActivity(intent);
    }


    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type, String id) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type, id).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());
                } else {
                    // Toast.makeText(GeneralInfoActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
                // Toast.makeText(GeneralInfoActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLikeListener(View v, VideoContest videoContest, int position, TextView countTv, ImageView likeIv) {

        if (videoContest.getLikeFlag().equals("1")) {
            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_like));
            likeIv.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
            VideoContestLike(token, eventid, videoContest.getId());
            try {
                int count = Integer.parseInt(videoContest.getTotalLikes());

                if (count > 0) {
                    count = count - 1;
                    countTv.setText(count + "");

                } else {
                    countTv.setText("0");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_afterlike));
            likeIv.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
            VideoContestLike(token, eventid, videoContest.getId());


            try {

                int count = Integer.parseInt(videoContest.getTotalLikes());


                count = count + 1;
                countTv.setText(count + "");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onMoreListner(View v, final VideoContest videoContest, final int position) {
        dialog = new BottomSheetDialog(this);

        dialog.setContentView(R.layout.botomcontestdialouge);


        TextView reportTv = dialog.findViewById(R.id.reportTv);
        TextView hideTv = dialog.findViewById(R.id.hideTv);
        TextView deleteTv = dialog.findViewById(R.id.deleteTv);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);

        deleteTv.setVisibility(View.GONE);

        reportTv.setText("Report Video");
        hideTv.setText("Hide Video");
        deleteTv.setText("Detete this Video");

        if (videoContest.getAttendeeId().equalsIgnoreCase(user_id)) {
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
                ReportVideoContestHide(eventid, videoContest.getId(), token, position);
            }
        });

        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteVideo(eventid, videoContest.getId(), token, position);
            }
        });


        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge(videoContest.getId(), videoContest.getFirstName());
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

    @Override
    public void onShareListner(View v, VideoContest videoContest, int position) {
        shareTextUrl(videoContest.getTitle(), ApiConstant.selfievideo + videoContest.getFileName());
    }


    public void VideoContestLike(String token, String eventid, String id) {
        mAPIService.VideoContestLikes(token, eventid, id).enqueue(new Callback<VideoContestLikes>() {
            @Override
            public void onResponse(Call<VideoContestLikes> call, Response<VideoContestLikes> response) {

                if (response.body().getStatus().equalsIgnoreCase("Success")) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    showLikeResponse(response);
                } else {

//                    Toast.makeText(VideoContestActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<VideoContestLikes> call, Throwable t) {
                Toast.makeText(VideoContestActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showLikeResponse(Response<VideoContestLikes> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {
            SelfieListFetch(token, eventid);
//            Toast.makeText(VideoContestActivity.this,response.message(),Toast.LENGTH_SHORT).show();

        } else {
//            Toast.makeText(VideoContestActivity.this,response.message(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        SelfieListFetch(token, eventid);

        super.onResume();
    }

    public void ReportVideoContestHide(String eventid, String selfieid, String token, final int position) {
//        showProgress();
        mAPIService.ReportVideoContestHide(token, eventid, selfieid).enqueue(new Callback<ReportVideoContestHide>() {
            @Override
            public void onResponse(Call<ReportVideoContestHide> call, Response<ReportVideoContestHide> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportPostHideresponse(response, position);
                } else {
//                    dismissProgress();

                    Toast.makeText(VideoContestActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportVideoContestHide> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(VideoContestActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    public void DeleteVideo(String eventid, String selfieid, String token, final int position) {
//        showProgress();
        mAPIService.DeleteVideoContest(token, eventid, selfieid).enqueue(new Callback<ReportVideoContestHide>() {
            @Override
            public void onResponse(Call<ReportVideoContestHide> call, Response<ReportVideoContestHide> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportPostHideresponse(response, position);
                } else {
//                    dismissProgress();

                    Toast.makeText(VideoContestActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportVideoContestHide> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(VideoContestActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportPostHideresponse(Response<ReportVideoContestHide> response, int position) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

            videoAdapter.videoContestList.remove(position);
            videoAdapter.notifyItemRemoved(position);
            dialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(VideoContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }


    public void ReportVideoContest(String eventid, String feedid, String token, String text) {
//        showProgress();
        mAPIService.ReportVideoContest(token, eventid, feedid, text).enqueue(new Callback<ReportVideoContest>() {
            @Override
            public void onResponse(Call<ReportVideoContest> call, Response<ReportVideoContest> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportPostresponse(response);
                } else {
//                    dismissProgress();

                    Toast.makeText(VideoContestActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportVideoContest> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(VideoContestActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportPostresponse(Response<ReportVideoContest> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Toast.makeText(VideoContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

            myDialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(VideoContestActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            myDialog.dismiss();
        }
    }

    private void showratedialouge(final String id, String name) {

        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();

        LinearLayout diatitle = myDialog.findViewById(R.id.diatitle);

        diatitle.setBackgroundColor(Color.parseColor(colorActive));


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);
        ratebtn.setText("Send");

        ratebtn.setBackgroundColor(Color.parseColor(colorActive));

        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);
        final TextView title = myDialog.findViewById(R.id.title);
        final ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);

        title.setText("Report Video");

        nametv.setText("To " + name);
        nametv.setTextColor(Color.parseColor(colorActive));

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
                    ReportVideoContest(eventid, id, token, msg);
                } else {

                    Toast.makeText(VideoContestActivity.this, "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void shareTextUrl(String data, String url) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, data);
        share.putExtra(Intent.EXTRA_TEXT, url);

        startActivity(Intent.createChooser(share, "Share link!"));
    }


    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }

}
