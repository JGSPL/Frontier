package com.procialize.mrgeApp20.AttendeeChat;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.procialize.mrgeApp20.AttendeeChat.Adapter.AttendeeChatAdapterRecycler;
import com.procialize.mrgeApp20.BuddyList.Adapter.LiveChatAdapterRecycler;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchChatList;
import com.procialize.mrgeApp20.BuddyList.DataModel.chat_list;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.util.GetUserActivityReport;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_ATTENDEE_PIC_PATH;

public class AttendeeChatActivity extends AppCompatActivity {

    private String chat_with_id,attendeeid, name, city, country, company, designation, description, totalrating, profile, mobile,
            buddy_status,from="";
    ImageView iv_buddy_details,profileIV;
    TextView title, sub_title;
    public AttendeeChatAdapterRecycler liveChatAdapter;
    String token,userId;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    SwipeRefreshLayout qaRvrefresh;
    ProgressBar progressBar;
    RecyclerView qaRv;
    ImageView headerlogoIv;
    TextView txtEmpty, nmtxt,pullrefresh;
    private APIService mAPIService;
    LinearLayout linear;
    //EditText commentEt;
    ImageView commentBt;
    public static String chat_id = "0";
    public static String attendee_chat_message = "";
    List<chat_list> chat_lists = new ArrayList<>();
    List<chat_list> chat_NewAdd = new ArrayList<>();
    public static String SpotEventChat="";

    String page = "0";
    int pageNO,pageNumber = 1;

    SpotChatReciever spotChatReciever;
    IntentFilter spotChatFilter;
    DBHelper procializeDB;
    SQLiteDatabase db;
    ConnectionDetector cd;

    EmojiconEditText commentEt;
    EmojiconTextView textView;
    ImageView emojiImageView;
    View rootView;
    EmojIconActions emojIcon;
    boolean isRefreshing = false;
    boolean isComplete = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
       // toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                ///finish();
            }
        });

        try {
            attendeeid = getIntent().getExtras().getString("id");
            chat_with_id = getIntent().getExtras().getString("id");
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
            from = getIntent().getExtras().getString("fromPage");

        } catch (Exception e) {
            e.printStackTrace();
        }

        profileIV = findViewById(R.id.profileIV);
        if (profile != null) {
            SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String picPath =  prefs1.getString(KEY_ATTENDEE_PIC_PATH,"");
            Glide.with(this).load(/*ApiConstant.profilepic */picPath+ profile).circleCrop()
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
        title.setTextColor(Color.parseColor(colorActive));
        sub_title = findViewById(R.id.sub_title);

        title.setText(name);
        if(designation!=null && city!=null) {
            sub_title.setText(designation + " - " + city);
            sub_title.setVisibility(View.VISIBLE);
        }else
        {sub_title.setVisibility(View.GONE);}

        procializeDB  = new DBHelper(this);
        db = procializeDB.getWritableDatabase();

        procializeDB.setAttendeeChatUnreadMessageCountToZero(attendeeid);

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
        //commentEt = findViewById(R.id.commentEt);
        commentBt = findViewById(R.id.commentBt);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        //commentBt.setColorFilter(Color.parseColor(colorActive));
        rootView = findViewById(R.id.root_view);
        commentEt = (EmojiconEditText) findViewById(R.id.commentEt);
        emojiImageView = (ImageView) findViewById(R.id.emoji_btn);
        mAPIService = ApiUtils.getAPIService();

       CardView myCardView1 = (CardView)findViewById(R.id.myCardView1);
       ImageView iv_background = findViewById(R.id.iv_background);
        iv_background.setBackgroundColor(Color.parseColor(colorActive));

        SessionManager sessionManager = new SessionManager(AttendeeChatActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);
        userId = user.get(SessionManager.KEY_USER_ID);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);

       // UserChatHistory(eventid,token,attendeeid,"1");
        cd = new ConnectionDetector(AttendeeChatActivity.this);

        if (cd.isConnectingToInternet()) {
            UserChatHistory(eventid, token, attendeeid, "1");
        } else {
            List<chat_list> chat_lists1 = procializeDB.getAttendeeChat(chat_with_id, userId);
            //Collections.reverse(chat_lists1);
            setAdapter(chat_lists1);

            // qaRv.scheduleLayoutAnimation();
            txtEmpty.setVisibility(View.GONE);
            qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());
        }

        try {
            spotChatReciever = new SpotChatReciever();
            spotChatFilter = new IntentFilter(ApiConstant.BROADCAST_ACTION_FOR_SPOT_ChatBuddy);
            LocalBroadcastManager.getInstance(this).registerReceiver(spotChatReciever, spotChatFilter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        emojIcon = new EmojIconActions(this, rootView,commentEt, emojiImageView);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                // Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                //  Log.e(TAG, "Keyboard closed");
            }
        });


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
                isRefreshing = true;
                pageNumber= pageNumber+1;
                if(pageNumber<=pageNO){

                    if (chat_lists.size() > 0) {
                        chat_lists.clear();
                    }
                    if(!isComplete)
                    UserChatHistory(eventid, token, attendeeid, String.valueOf(pageNumber));
                }else{
                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    isComplete = true;
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


                setAdapter(chat_lists);
                txtEmpty.setVisibility(View.GONE);
                qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());

                procializeDB.deleteAttendeeChat(chat_with_id,userId);
                procializeDB.insertAttendeeChat(chat_lists, db);
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

                if (liveChatAdapter != null) {
                    liveChatAdapter.notifyDataSetChanged();
                    qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());
                } else {
                    setAdapter(chat_lists);
                }

                txtEmpty.setVisibility(View.GONE);
                procializeDB.deleteAttendeeChat(chat_with_id,userId);
                procializeDB.insertAttendeeChat(chat_lists, db);

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

                if (chat_lists.size() > 0) {
                    chat_lists.clear();
                }
                for (int i = 0; i < chat_listsNew.size(); i++) {
                    chat_lists.add(chat_listsNew.get(i));
                }
                if (chat_NewAdd.size() > 0) {
                    for (int i = 0; i < chat_NewAdd.size(); i++) {
                        chat_lists.add(chat_NewAdd.get(i));
                    }
                }


                if (chat_NewAdd.size() > 0) {
                    chat_NewAdd.clear();
                }
                for (int i = 0; i < chat_lists.size(); i++) {
                    chat_NewAdd.add(chat_lists.get(i));
                }


                txtEmpty.setVisibility(View.GONE);
                if (isRefreshing) {
                    if (liveChatAdapter != null) {
                        liveChatAdapter.notifyDataSetChanged();
                    }
                } else {
                    setAdapter(chat_lists);
                    qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());
                }

                procializeDB.deleteAttendeeChat(chat_with_id,userId);
                procializeDB.insertAttendeeChat(chat_lists, db);
            } else {}
        } else {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(spotChatReciever);
    }

    public void setAdapter(List<chat_list> chat_lists) {
        liveChatAdapter = new AttendeeChatAdapterRecycler(AttendeeChatActivity.this, chat_lists, attendeeid);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AttendeeChatActivity.this);
        qaRv.setLayoutManager(mLayoutManager);
        qaRv.setItemAnimator(new DefaultItemAnimator());
        qaRv.setAdapter(liveChatAdapter);
    }

    private class SpotChatReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*chat_id.replace("chat_","");
            String BuddyId = chat_id;*/
            /*String BuddyId = chat_id.replace("eventchat_", "");
            Log.d("service end", "service end");
            if(BuddyId.equalsIgnoreCase(attendeeid)) {
                if (SpotEventChat != null) {
                    if (SpotEventChat.equalsIgnoreCase("Eventchat")) {
                       *//* new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                UserChatHistoryRefresh(eventid, token, attendeeid, "1");
                            }
                        }, 300);*//*

                        UserChatHistoryRefresh(eventid, token, attendeeid, "1");
                        SpotEventChat = "S";
                        pageNumber = 1;

                    }

                }
            }*/
            new getMessage().execute();
            procializeDB.setAttendeeChatUnreadMessageCountToZero(attendeeid);
        }
    }


    private class getMessage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... f_url) {
            try {
                String BuddyId = chat_id.replace("eventchat_", "");
                if (BuddyId.equalsIgnoreCase(attendeeid)) {
                    if (SpotEventChat != null) {
                        if (SpotEventChat.equalsIgnoreCase("eventchat")) {

                            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date now = new Date();
                            String strDate = sdfDate.format(now);
                            try {
                                chat_list chat_list1 = new chat_list();
                                chat_list1.setStatus("1");
                                chat_list1.setId(chat_id);
                                chat_list1.setSender_id(BuddyId);
                                chat_list1.setReceiver_id(userId);
                                chat_list1.setMessage(attendee_chat_message);
                                chat_list1.setTimestamp(strDate);

                                chat_lists.add(chat_list1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return "success";
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        @Override
        protected void onPostExecute(String message) {

            if (liveChatAdapter != null) {
                liveChatAdapter.notifyDataSetChanged();
                qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());
            } else {
                setAdapter(chat_lists);
            }
            qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());


        }
    }

    @Override
    public void onBackPressed() {
        if(from.equalsIgnoreCase("noti"))
        {
            startActivity(new Intent(AttendeeChatActivity.this,MrgeHomeActivity.class));
        }
        else
        {
            finish();
        }
    }
}