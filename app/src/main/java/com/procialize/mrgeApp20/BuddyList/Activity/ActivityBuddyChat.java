package com.procialize.mrgeApp20.BuddyList.Activity;

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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.BuddyList.Adapter.LiveChatAdapterRecycler;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchChatList;
import com.procialize.mrgeApp20.BuddyList.DataModel.chat_list;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.KeyboardUtility;
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

public class ActivityBuddyChat extends AppCompatActivity {

    public static String chat_id = "0";
    public static String SpotChat = "";
    public static String chat_message = "";
    public LiveChatAdapterRecycler liveChatAdapter;
    ImageView iv_buddy_details, profileIV;
    TextView title, sub_title;
    String token;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    SwipeRefreshLayout qaRvrefresh;
    ProgressBar progressBar;
    RecyclerView qaRv;
    ImageView headerlogoIv;
    TextView txtEmpty, nmtxt, pullrefresh;
    LinearLayout linear;
    // EditText commentEt;
    ImageView commentBt;
    List<chat_list> chat_lists = new ArrayList<>();
    List<chat_list> chat_NewAdd = new ArrayList<>();
    String buddy_status,from="";
    String page = "0";
    int pageNO, pageNumber = 1;
    DBHelper procializeDB;
    SQLiteDatabase db;
    SpotChatReciever spotChatReciever;
    IntentFilter spotChatFilter;
    ConnectionDetector cd;
    EmojiconEditText commentEt;
    EmojiconTextView textView;
    ImageView emojiImageView;
    View rootView;
    EmojIconActions emojIcon;
    private String userId, chat_with_id, attendeeid, name, city, country, company, designation, description, totalrating, profile, mobile,email;
    private APIService mAPIService;
    boolean isRefreshing = false;
    boolean isComplete = false;
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
      //  toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
                //                 finish();
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
            email = getIntent().getExtras().getString("email");
            buddy_status = getIntent().getExtras().getString("buddy_status");
            from = getIntent().getExtras().getString("from");

        } catch (Exception e) {
            e.printStackTrace();
        }

        profileIV = findViewById(R.id.profileIV);
        if (profile != null) {
            SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String picPath =  prefs1.getString(KEY_ATTENDEE_PIC_PATH,"");
            Glide.with(this).load(/*ApiConstant.profilepic*/picPath + profile).circleCrop()
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
        if (designation != null) {
            sub_title.setText(designation);
        }

        if (city != null) {
            sub_title.setText(city);
        }

        if (designation != null && city != null) {
            sub_title.setText(designation + " - " + city);
        } else {
            sub_title.setText("");
        }


        procializeDB = new DBHelper(this);
        db = procializeDB.getWritableDatabase();

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
                attendeetail.putExtra("buddy_status", buddy_status);
                attendeetail.putExtra("email", email);

//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
                startActivity(attendeetail);
                finish();
            }
        });
        init();
    }

    void init() {
        progressBar = findViewById(R.id.progressBar);
        qaRvrefresh = findViewById(R.id.qaRvrefresh);
        qaRv = findViewById(R.id.qaRv);
        txtEmpty = findViewById(R.id.txtEmpty);
        //commentEt = findViewById(R.id.commentEt);
        commentBt = findViewById(R.id.commentBt);
        //commentBt.setColorFilter(Color.parseColor(colorActive));


        rootView = findViewById(R.id.root_view);
        commentEt = (EmojiconEditText) findViewById(R.id.commentEt);
        emojiImageView = (ImageView) findViewById(R.id.emoji_btn);
        mAPIService = ApiUtils.getBuddyAPIService();

        SessionManager sessionManager = new SessionManager(ActivityBuddyChat.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);
        userId = user.get(SessionManager.KEY_USER_ID);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);

        cd = new ConnectionDetector(this);

        if (cd.isConnectingToInternet()) {
            UserChatHistory(eventid, token, attendeeid, "1");
        } else {
            //-------------------Offline messages----------------------
            List<chat_list> chat_lists1 = procializeDB.getBuddyChat(chat_with_id, userId);
            setAdapter(chat_lists1);
            txtEmpty.setVisibility(View.GONE);
            //qaRv.smoothScrollToPosition(liveChatAdapter.getCount());
            qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());
        }

        try {
            spotChatReciever = new SpotChatReciever();
            spotChatFilter = new IntentFilter(ApiConstant.BROADCAST_ACTION_FOR_SPOT_ChatBuddy);
            LocalBroadcastManager.getInstance(this).registerReceiver(spotChatReciever, spotChatFilter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        emojIcon = new EmojIconActions(this, rootView, commentEt, emojiImageView);
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


        //----------------------Send Message-----------------------------------
        commentBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentEt.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(commentEt.getText().toString());
                    chat_message = msg;
                    PostChat(eventid, token, attendeeid, msg);
                    commentBt.setEnabled(false);
                    KeyboardUtility.hideSoftKeyboard(ActivityBuddyChat.this);

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
        //----------------------------------------------------------------------------

        //-------------Update unread message count to 0 in local db----------------------
        procializeDB.setBuddyChatUnreadMessageCountToZero(attendeeid);

        //------------------Refresh list(fetch old messages)
        qaRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // String chat_id = chat_lists.get(0).getId();
                pageNO = Integer.parseInt(page);
                isRefreshing = true;
                pageNumber = pageNumber + 1;
                if (pageNumber <= pageNO) {
                    if(!isComplete)
                    UserChatHistory(eventid, token, attendeeid, String.valueOf(pageNumber));
                } else {
                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    isComplete = true;
                    Toast.makeText(ActivityBuddyChat.this, "Chat loading complete", Toast.LENGTH_SHORT).show();
                    //pageNumber = 1;
                }
            }
        });
        //------------------------------------------------------------

        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this, token,
                eventid,
                ApiConstant.pageVisited,
                "43",
                "");
        getUserActivityReport.userActivityReport();
    }

    //------------------------send Message-------------------------------------
    private void PostChat(final String eventid, final String token, String budd_id, String msg) {
        mAPIService.LiveChatPostUser(eventid, token, budd_id, msg).enqueue(new Callback<FetchChatList>() {
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
                    Toast.makeText(ActivityBuddyChat.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchChatList> call, Throwable t) {
                Toast.makeText(ActivityBuddyChat.this, "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showResponsePost(Response<FetchChatList> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            // page = response.body().getData_pages();

            if (!(response.body().getChatList().isEmpty())) {

                /*for(int i=0;i<response.body().getChatList().size();i++) {
                    chat_lists.add(response.body().getChatList().get(i));
                }*/
            /*    chat_lists.clear();
                for (int i = 0; i < response.body().getChatList().size(); i++) {
                    chat_lists.add(response.body().getChatList().get(i));
                }
                Collections.reverse(chat_lists);
                pageNumber = 0;*/

                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                String strDate = sdfDate.format(now);
                try {
                    chat_list chat_list1 = new chat_list();
                    chat_list1.setStatus("1");
                    chat_list1.setId("0");
                    chat_list1.setSender_id(userId);
                    chat_list1.setReceiver_id(chat_with_id);
                    chat_list1.setMessage(chat_message);
                    chat_list1.setTimestamp(strDate);

                    chat_lists.add(chat_list1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (liveChatAdapter != null) {
                    liveChatAdapter.notifyDataSetChanged();
                    qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());
                } else {
                    setAdapter(chat_lists);
                }
                // qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());
              /*  liveChatAdapter = new LiveChatAdapterRecycler(ActivityBuddyChat.this, chat_lists, attendeeid);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ActivityBuddyChat.this);
                qaRv.setLayoutManager(mLayoutManager);
                qaRv.setItemAnimator(new DefaultItemAnimator());
                liveChatAdapter.notifyDataSetChanged();
                qaRv.setAdapter(liveChatAdapter);
                liveChatAdapter.notifyDataSetChanged();*/

                // setAdapter(chat_lists);
                // qaRv.scheduleLayoutAnimation();
                txtEmpty.setVisibility(View.GONE);
                // qaRv.smoothScrollToPosition(liveChatAdapter.getCount());


                procializeDB.deleteBuddyChat(chat_with_id, userId);
                procializeDB.insertBuddyChat(chat_lists, db);
            } else {
            }
        } else {
        }
    }
    //---------------------------------------------------------------------------

    private void UserChatHistoryRefresh(final String eventid, final String token, String budd_id, String pageNumber) {
        mAPIService.UserChathistory(eventid, token, budd_id, pageNumber).enqueue(new Callback<FetchChatList>() {
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

    public void showResponseRefresh(Response<FetchChatList> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            // page = response.body().getData_pages();

            if (!(response.body().getChatList().isEmpty())) {

                /*for(int i=0;i<response.body().getChatList().size();i++) {
                    chat_lists.add(response.body().getChatList().get(i));
                }*/
                chat_lists.clear();
                for (int i = 0; i < response.body().getChatList().size(); i++) {
                    chat_lists.add(response.body().getChatList().get(i));
                }
                Collections.reverse(chat_lists);
                pageNumber = 1;

                /*liveChatAdapter = new LiveChatAdapterRecycler(ActivityBuddyChat.this, chat_lists, attendeeid);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ActivityBuddyChat.this);
                qaRv.setLayoutManager(mLayoutManager);
                qaRv.setItemAnimator(new DefaultItemAnimator());
                liveChatAdapter.notifyDataSetChanged();
                qaRv.setAdapter(liveChatAdapter);
                liveChatAdapter.notifyDataSetChanged();
*/

                setAdapter(chat_lists);
                // qaRv.scheduleLayoutAnimation();
                txtEmpty.setVisibility(View.GONE);
                //  qaRv.smoothScrollToPosition(liveChatAdapter.getCount());
                qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());
                procializeDB.deleteBuddyChat(chat_with_id, userId);
                procializeDB.insertBuddyChat(chat_lists, db);
            } else {

            }
        } else {

        }
    }

    //---------------------------Chatting history(fetch old messages)------------------------------------------------
    private void UserChatHistory(final String eventid, final String token, String budd_id, String pageNumber) {
        mAPIService.UserChathistory(eventid, token, budd_id, pageNumber).enqueue(new Callback<FetchChatList>() {
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


                /*liveChatAdapter = new LiveChatAdapterRecycler(ActivityBuddyChat.this, chat_lists, attendeeid);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ActivityBuddyChat.this);
                qaRv.setLayoutManager(mLayoutManager);
                qaRv.setItemAnimator(new DefaultItemAnimator());
                liveChatAdapter.notifyDataSetChanged();
                qaRv.setAdapter(liveChatAdapter);
                liveChatAdapter.notifyDataSetChanged();*/


                // qaRv.scheduleLayoutAnimation();
                txtEmpty.setVisibility(View.GONE);
                //qaRv.smoothScrollToPosition(liveChatAdapter.getCount());
                if (isRefreshing) {
                    if (liveChatAdapter != null) {
                        liveChatAdapter.notifyDataSetChanged();
                    }
                } else {
                    setAdapter(chat_lists);
                    qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());
                }

                procializeDB.deleteBuddyChat(chat_with_id, userId);
                procializeDB.insertBuddyChat(chat_NewAdd, db);

            } else {

                //   txtEmpty.setVisibility(View.VISIBLE);
                //txtEmpty.setText("Start conversation \n with VIP Support Team");
            }
        } else {
            // Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }
    //------------------------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(spotChatReciever);
    }

    public void setAdapter(List<chat_list> chat_lists) {
        liveChatAdapter = new LiveChatAdapterRecycler(ActivityBuddyChat.this, chat_lists, attendeeid);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ActivityBuddyChat.this);
        qaRv.setLayoutManager(mLayoutManager);
        qaRv.setItemAnimator(new DefaultItemAnimator());
        qaRv.setAdapter(liveChatAdapter);
    }

    private class SpotChatReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*chat_id.replace("chat_","");
            String BuddyId = chat_id;*/
            /*String BuddyId = chat_id.replace("chat_", "");
            Log.d("service end", "service end");
            if (BuddyId.equalsIgnoreCase(attendeeid)) {
                if (SpotChat != null) {
                    if (SpotChat.equalsIgnoreCase("chat")) {

                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date now = new Date();
                        String strDate = sdfDate.format(now);
                        try {
                            chat_list chat_list1 = new chat_list();
                            chat_list1.setStatus("1");
                            chat_list1.setId(chat_id);
                            chat_list1.setSender_id(BuddyId);
                            chat_list1.setReceiver_id(userId);
                            chat_list1.setMessage(chat_message);
                            chat_list1.setTimestamp(strDate);

                            chat_lists.add(chat_list1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (liveChatAdapter != null) {
                            liveChatAdapter.notifyDataSetChanged();
                            qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());
                        } else {
                            setAdapter(chat_lists);
                        }
                        qaRv.smoothScrollToPosition(liveChatAdapter.getItemCount());
                        procializeDB.setBuddyChatUnreadMessageCountToZero(attendeeid);
                    }
                }
            }*/
            new getMessage().execute();

            procializeDB.setBuddyChatUnreadMessageCountToZero(attendeeid);
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
                String BuddyId = chat_id.replace("chat_", "");
                if (BuddyId.equalsIgnoreCase(attendeeid)) {
                    if (SpotChat != null) {
                        if (SpotChat.equalsIgnoreCase("chat")) {

                            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date now = new Date();
                            String strDate = sdfDate.format(now);
                            try {
                                chat_list chat_list1 = new chat_list();
                                chat_list1.setStatus("1");
                                chat_list1.setId(chat_id);
                                chat_list1.setSender_id(BuddyId);
                                chat_list1.setReceiver_id(userId);
                                chat_list1.setMessage(chat_message);
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
            startActivity(new Intent(ActivityBuddyChat.this, MrgeHomeActivity.class));
        }
        else
        {
            finish();
        }
    }
}