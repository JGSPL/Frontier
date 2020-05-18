package com.procialize.mrgeApp20.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.mrgeApp20.Engagement.Adapter.SelfieAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.GetterSetter.ReportSelfie;
import com.procialize.mrgeApp20.GetterSetter.ReportSelfieHide;
import com.procialize.mrgeApp20.GetterSetter.SelfieLike;
import com.procialize.mrgeApp20.GetterSetter.SelfieList;
import com.procialize.mrgeApp20.GetterSetter.SelfieListFetch;
import com.procialize.mrgeApp20.InnerDrawerActivity.SelfieUploadActivity;
import com.procialize.mrgeApp20.Engagement.Activity.SwappingSelfieActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelfieContestNew extends AppCompatActivity implements SelfieAdapter.SelfieAdapterListner {

    Button uploadbtn;
    SwipeRefreshLayout selfiefeedrefresh;
    ProgressBar progressBar;
    GridView gridview;
    String token;
    SelfieAdapter selfieAdapter;
    BottomSheetDialog dialog;
    Dialog myDialog;
    List<SelfieList> selfieLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ImageView headerlogoIv;
    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_new);
        setContentView(R.layout.activity_selfie_contest);
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");


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

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);



        selfiefeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                SelfieListFetch(token, eventid);
            }
        });

        uploadbtn = findViewById(R.id.uploadbtn);
        selfiefeedrefresh = findViewById(R.id.selfiefeedrefresh);
        gridview = findViewById(R.id.gridview);
        selfieLists = new ArrayList<SelfieList>();
        SelfieListFetch(token, eventid);

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selfie = new Intent(SelfieContestNew.this, SelfieUploadActivity.class);
                startActivity(selfie);
            }
        });


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
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

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
//            Collections.reverse(selfieLists);
//            SelfieAdapter selfieAdapter = new SelfieAdapter(SelfieContestNew.this, selfieLists,this);
//            gridview.setAdapter(selfieAdapter);

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
    public void onContactSelected(SelfieList selfieList, ImageView imageView) {
        Intent imageview = new Intent(this, SwappingSelfieActivity.class);
        imageview.putExtra("url", selfieList.getFileName());
        imageview.putExtra("gallerylist", (Serializable) selfieLists);
        startActivity(imageview);
    }

    @Override
    public void onLikeListener(View v, SelfieList selfieList, int position, TextView countTv, ImageView likeIv) {
        if (selfieList.getLikeFlag().equals("1")) {
            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_like));
            SelfieLike(token, "1", selfieList.getId());

            try {

                int count = Integer.parseInt(selfieList.getTotalLikes());

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

            SelfieLike(token, "1", selfieList.getId());


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
    public void onMoreListner(View v, SelfieList selfieList, int position) {

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

                    Toast.makeText(SelfieContestNew.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportSelfieHide> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(SelfieContestNew.this, "Low network or no network", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(SelfieContestNew.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(SelfieContestNew.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportSelfie> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(SelfieContestNew.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportPostresponse(Response<ReportSelfie> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Toast.makeText(SelfieContestNew.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

            myDialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(SelfieContestNew.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
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

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etmsg.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    dialog.dismiss();
                    ReportSelfie("1", id, token, msg);
                } else {
                    Toast.makeText(SelfieContestNew.this, "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void SelfieLike(String token, String eventid, String id) {
        mAPIService.SelfieLike(token, eventid, id).enqueue(new Callback<SelfieLike>() {
            @Override
            public void onResponse(Call<SelfieLike> call, Response<SelfieLike> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    showLikeResponse(response);
                } else {

                    Toast.makeText(SelfieContestNew.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<SelfieLike> call, Throwable t) {
                Toast.makeText(SelfieContestNew.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void showLikeResponse(Response<SelfieLike> response) {

        if (response.body().getMessage().equalsIgnoreCase("success")) {
            SelfieListFetch(token, eventid);
            Toast.makeText(SelfieContestNew.this, response.message(), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(SelfieContestNew.this, response.message(), Toast.LENGTH_SHORT).show();
        }
    }

}
