package com.procialize.mrgeApp20.BuddyList.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.BuddyList.Adapter.LiveChatAdapter;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchChatList;
import com.procialize.mrgeApp20.BuddyList.DataModel.chat_list;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityBuddyChat extends AppCompatActivity {

    private String attendeeid, name, city, country, company, designation, description, totalrating, profile, mobile;
    ImageView iv_buddy_details,profileIV;
    TextView title, sub_title;
    public LiveChatAdapter liveChatAdapter;
    String token;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    SwipeRefreshLayout qaRvrefresh;
    ProgressBar progressBar;
    ListView qaRv;
    ImageView headerlogoIv;
    TextView txtEmpty, nmtxt,pullrefresh;
    private APIService mAPIService;
    LinearLayout linear;
    EditText commentEt;
    ImageView commentBt;
    public static String chat_id = "0";
    List<chat_list> chat_lists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        profileIV = findViewById(R.id.profileIV);
        if (profile != null) {
            Glide.with(this).load(ApiConstant.profilepic + profile).circleCrop()
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

        title = findViewById(R.id.title);
        sub_title = findViewById(R.id.sub_title);

        title.setText(name);
        sub_title.setText(designation + " - " + city);

        iv_buddy_details = findViewById(R.id.iv_buddy_details);
        iv_buddy_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendeetail = new Intent(ActivityBuddyChat.this, ActivityBuddyDetails.class);
                attendeetail.putExtra("id", attendeeid);
                attendeetail.putExtra("name", name);
                attendeetail.putExtra("city", city);
                attendeetail.putExtra("country", country);
                attendeetail.putExtra("company", company);
                attendeetail.putExtra("designation", designation);
                attendeetail.putExtra("description", description);
                attendeetail.putExtra("profile", profile);
                attendeetail.putExtra("mobile", mobile);
//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
                startActivity(attendeetail);
            }
        });
        init();
    }

    void init(){
        progressBar = findViewById(R.id.progressBar);
        qaRvrefresh = findViewById(R.id.qaRvrefresh);
         qaRv = findViewById(R.id.qaRv);
        txtEmpty = findViewById(R.id.txtEmpty);
        commentEt = findViewById(R.id.commentEt);
        commentBt = findViewById(R.id.commentBt);

        mAPIService = ApiUtils.getBuddyAPIService();

        SessionManager sessionManager = new SessionManager(ActivityBuddyChat.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);

        UserChatHistory(eventid,token,attendeeid,"0");


        commentBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentEt.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(commentEt.getText().toString());
                    PostChat(eventid,token,attendeeid,msg);
                    commentBt.setEnabled(false);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBuddyChat.this);
                    builder.setTitle("");
                    builder.setMessage("Please post a message");

                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
            }
        });

        qaRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String chat_id = chat_lists.get(0).getId();
                UserChatHistory(eventid,token,attendeeid,chat_id);
            }
        });
    }

    private void PostChat(final String eventid, final String token, String budd_id,String msg) {
        mAPIService.LiveChatPostUser(eventid,token,budd_id,msg).enqueue(new Callback<FetchChatList>() {
            @Override
            public void onResponse(Call<FetchChatList> call, Response<FetchChatList> response) {

                if (response.isSuccessful()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    commentEt.setText("");
                    commentBt.setEnabled(true);

                    UserChatHistory(eventid,token,attendeeid,"");

                } else {

                    Toast.makeText(ActivityBuddyChat.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<FetchChatList> call, Throwable t) {
                Toast.makeText(ActivityBuddyChat.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void UserChatHistory(final String eventid, final String token, String budd_id,String msg) {
        mAPIService.UserChathistory(eventid,token,budd_id,msg).enqueue(new Callback<FetchChatList>() {
            @Override
            public void onResponse(Call<FetchChatList> call, Response<FetchChatList> response) {

                if (response.isSuccessful()) {
                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    Log.i("hit", "post submitted to API." + response.body().toString());
                  //WS  ZAXDFRTGHNB  Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();


                    // QAFetch(token, eventid);
                    showResponse(response);

                } else {

                    Toast.makeText(ActivityBuddyChat.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<FetchChatList> call, Throwable t) {
                if (qaRvrefresh.isRefreshing()) {
                    qaRvrefresh.setRefreshing(false);
                }
                Toast.makeText(ActivityBuddyChat.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void showResponse(Response<FetchChatList> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {

            if (!(response.body().getChatList().isEmpty())) {
                //  txtEmpty.setVisibility(View.GONE);
                /*if(chat_lists.size() > 0)
                { */
                    for(int i=0;i<response.body().getChatList().size();i++) {
                        chat_lists.add(response.body().getChatList().get(i));
                    }
                /*}
                else
                {

                }*/

                        liveChatAdapter = new LiveChatAdapter(ActivityBuddyChat.this, chat_lists,attendeeid);
                liveChatAdapter.notifyDataSetChanged();
                qaRv.setAdapter(liveChatAdapter);
                liveChatAdapter.notifyDataSetChanged();

                // qaRv.scheduleLayoutAnimation();
                txtEmpty.setVisibility(View.GONE);
                qaRv.smoothScrollToPosition(liveChatAdapter.getCount());




            } else {
             //   txtEmpty.setVisibility(View.VISIBLE);

                
                    //txtEmpty.setText("Start conversation \n with VIP Support Team");


               

            }



        } else {
           // Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }


}
