package com.procialize.mrgeApp20.AttendeeChat;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.Activity.AttendeeDetailActivity;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.AttendeeChat.Adapter.AttendeeChatAdapter;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchChatList;
import com.procialize.mrgeApp20.BuddyList.DataModel.chat_list;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.util.GetUserActivityReport;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendeeChatActivity extends AppCompatActivity {

    private String attendeeid, name, city, country, company, designation, description, totalrating, profile, mobile,
            buddy_status;
    ImageView iv_buddy_details,profileIV;
    TextView title, sub_title;
    public AttendeeChatAdapter liveChatAdapter;
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
    List<chat_list> chat_NewAdd = new ArrayList<>();
    public static String SpotEventChat="";

    String page = "0";
    int pageNO,pageNumber = 1;

    SpotChatReciever spotChatReciever;
    IntentFilter spotChatFilter;
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
            buddy_status = getIntent().getExtras().getString("buddy_status");

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
                Intent attendeetail = new Intent(AttendeeChatActivity.this, AttendeeDetailChat.class);
                attendeetail.putExtra("id", attendeeid);
                attendeetail.putExtra("name", name);
                attendeetail.putExtra("city", city);
                attendeetail.putExtra("country", country);
                attendeetail.putExtra("company", company);
                attendeetail.putExtra("designation", designation);
                attendeetail.putExtra("description", description);
                attendeetail.putExtra("profile", profile);
                attendeetail.putExtra("mobile", mobile);
                attendeetail.putExtra("buddy_status", buddy_status);
               // attendeetail.putExtra("page_status", "ChatPage");

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

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(AttendeeChatActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);

        UserChatHistory(eventid,token,attendeeid,"1");

        try {
            spotChatReciever = new SpotChatReciever();
            spotChatFilter = new IntentFilter(ApiConstant.BROADCAST_ACTION_FOR_SPOT_ChatBuddy);
            LocalBroadcastManager.getInstance(this).registerReceiver(spotChatReciever, spotChatFilter);

        } catch (Exception e) {
            e.printStackTrace();
        }


        commentBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentEt.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(commentEt.getText().toString());
                    PostChat(eventid,token,attendeeid,msg);
                    commentBt.setEnabled(false);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AttendeeChatActivity.this);
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
                // String chat_id = chat_lists.get(0).getId();
                pageNO = Integer.parseInt(page);

                pageNumber= pageNumber+1;
                if(pageNumber<=pageNO){
                    UserChatHistory(eventid, token, attendeeid, String.valueOf(pageNumber));
                }else{
                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    Toast.makeText(AttendeeChatActivity.this, "Chat loading complete", Toast.LENGTH_SHORT).show();
                    pageNumber = 1;
                }
            }
        });

        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this, token,
                eventid,
                ApiConstant.pageVisited,
                "11",
                "");
        getUserActivityReport.userActivityReport();
    }

    private class SpotChatReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*chat_id.replace("chat_","");
            String BuddyId = chat_id;*/
            String BuddyId = chat_id.replace("eventchat_", "");
            Log.d("service end", "service end");
            if(BuddyId.equalsIgnoreCase(attendeeid)) {
                if (SpotEventChat != null) {
                    if (SpotEventChat.equalsIgnoreCase("Eventchat")) {
                        UserChatHistoryRefresh(eventid, token, attendeeid, "1");
                        SpotEventChat = "S";
                        pageNumber = 1;

                    }
                }
            }
        }
    }


    private void PostChat(final String eventid, final String token, String budd_id,String msg) {
        mAPIService.eventLiveChat(eventid,token,budd_id,msg).enqueue(new Callback<FetchChatList>() {
            @Override
            public void onResponse(Call<FetchChatList> call, Response<FetchChatList> response) {

                if (response.isSuccessful()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    commentEt.setText("");
                    commentBt.setEnabled(true);
                    showResponsePost(response);
                    // UserChatHistory(eventid,token,attendeeid,"1");

                } else {

                    Toast.makeText(AttendeeChatActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<FetchChatList> call, Throwable t) {
                Toast.makeText(AttendeeChatActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void UserChatHistoryRefresh(final String eventid, final String token, String budd_id,String msg) {
        mAPIService.EventChatHistory(eventid,token,budd_id,msg).enqueue(new Callback<FetchChatList>() {
            @Override
            public void onResponse(Call<FetchChatList> call, Response<FetchChatList> response) {

                if (response.isSuccessful()) {
                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    //WS  ZAXDFRTGHNB  Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();


                    // QAFetch(token, eventid);
                    showResponseRefresh(response);

                } else {

                    Toast.makeText(AttendeeChatActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<FetchChatList> call, Throwable t) {
                if (qaRvrefresh.isRefreshing()) {
                    qaRvrefresh.setRefreshing(false);
                }
                Toast.makeText(AttendeeChatActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void showResponseRefresh(Response<FetchChatList> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            // page = response.body().getData_pages();

            if (!(response.body().getChatList().isEmpty())) {

                /*for(int i=0;i<response.body().getChatList().size();i++) {
                    chat_lists.add(response.body().getChatList().get(i));
                }*/
                chat_lists.clear();
                for(int i=0;i<response.body().getChatList().size();i++) {
                    chat_lists.add(response.body().getChatList().get(i));
                }
                Collections.reverse(chat_lists);
                pageNumber = 1;


                liveChatAdapter = new AttendeeChatAdapter(AttendeeChatActivity.this, chat_lists,attendeeid);
                liveChatAdapter.notifyDataSetChanged();
                qaRv.setAdapter(liveChatAdapter);
                liveChatAdapter.notifyDataSetChanged();

                // qaRv.scheduleLayoutAnimation();
                txtEmpty.setVisibility(View.GONE);
                qaRv.smoothScrollToPosition(liveChatAdapter.getCount());

            } else {

            }

        } else {

        }
    }


    public void showResponsePost(Response<FetchChatList> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            // page = response.body().getData_pages();

            if (!(response.body().getChatList().isEmpty())) {

                /*for(int i=0;i<response.body().getChatList().size();i++) {
                    chat_lists.add(response.body().getChatList().get(i));
                }*/
                chat_lists.clear();
                for(int i=0;i<response.body().getChatList().size();i++) {
                    chat_lists.add(response.body().getChatList().get(i));
                }
                Collections.reverse(chat_lists);
                pageNumber = 1;


                liveChatAdapter = new AttendeeChatAdapter(AttendeeChatActivity.this, chat_lists,attendeeid);
                liveChatAdapter.notifyDataSetChanged();
                qaRv.setAdapter(liveChatAdapter);
                liveChatAdapter.notifyDataSetChanged();

                // qaRv.scheduleLayoutAnimation();
                txtEmpty.setVisibility(View.GONE);
                qaRv.smoothScrollToPosition(liveChatAdapter.getCount());

            } else {

            }

        } else {

        }
    }


    private void UserChatHistory(final String eventid, final String token, String budd_id,String msg) {
        mAPIService.EventChatHistory(eventid,token,budd_id,msg).enqueue(new Callback<FetchChatList>() {
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

                    Toast.makeText(AttendeeChatActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<FetchChatList> call, Throwable t) {
                if (qaRvrefresh.isRefreshing()) {
                    qaRvrefresh.setRefreshing(false);
                }
                Toast.makeText(AttendeeChatActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void showResponse(Response<FetchChatList> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            page = response.body().getData_pages();

            if (!(response.body().getChatList().isEmpty())) {
                List<chat_list> chat_listsNew = new ArrayList<>();

                chat_listsNew = response.body().getChatList();
                Collections.reverse(chat_listsNew);

                if(chat_lists.size()>0){
                    chat_lists.clear();
                }
                for(int i=0;i<chat_listsNew.size();i++) {
                    chat_lists.add(chat_listsNew.get(i));
                }
                if(chat_NewAdd.size()>0){
                    for(int i=0;i<chat_NewAdd.size();i++) {
                        chat_lists.add(chat_NewAdd.get(i));
                    }
                }



                if(chat_NewAdd.size()>0){
                    chat_NewAdd.clear();
                }
                for(int i=0;i<chat_lists.size();i++) {
                    chat_NewAdd.add(chat_lists.get(i));
                }


                liveChatAdapter = new AttendeeChatAdapter(AttendeeChatActivity.this, chat_lists,attendeeid);
                liveChatAdapter.notifyDataSetChanged();
                qaRv.setAdapter(liveChatAdapter);
                liveChatAdapter.notifyDataSetChanged();

                // qaRv.scheduleLayoutAnimation();
                txtEmpty.setVisibility(View.GONE);
                qaRv.smoothScrollToPosition(liveChatAdapter.getCount());




            } else {

            }



        } else {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(spotChatReciever);

    }
}