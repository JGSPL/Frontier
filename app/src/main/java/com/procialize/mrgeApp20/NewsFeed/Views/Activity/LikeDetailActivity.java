package com.procialize.mrgeApp20.NewsFeed.Views.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.Activity.AttendeeDetailActivity;
import com.procialize.mrgeApp20.Adapter.LikeAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;

import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.LikeListing;
import com.procialize.mrgeApp20.GetterSetter.news_feed_media;
import com.procialize.mrgeApp20.NewsFeed.Views.Adapter.SwipeMultimediaAdapter;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LikeDetailActivity extends AppCompatActivity {
    String newsfeedid;
    private APIService mAPIService;
    String token;
    HashMap<String, String> user;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    LikeAdapter likeAdapter;
    List<AttendeeList> attendeeLists;
    RecyclerView like_list;
    ImageView  profileIV, feedimageIv, playicon;
    ProgressBar progressView, feedprogress;
    TextView nameTv, designationTv, dateTv, companyTv, headingTv;
    JzvdStd videoplayer;
    String fname, lname, name, company, designation, heading, date, Likes, Likeflag, Comments, profileurl, noti_profileurl, feedurl, flag, type, feedid, apikey, thumbImg, videourl, noti_type;
    float p1 = 0;
    String colorActive;
    LinearLayout relative;
    TextView testdata;
    String substring;
    private List<AttendeeList> attendeeDBList;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    List<AttendeeList> customers = null;
    private SQLiteDatabase db;
    CardView card_view;
    ViewPager vp_slider;
    LinearLayout ll_dots;
    ArrayList<news_feed_media> myList;
    TextView tv_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_detail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

       // headerlogoIv = findViewById(R.id.headerlogoIv);
        profileIV = findViewById(R.id.profileIV);
        feedimageIv = findViewById(R.id.feedimageIv);
        playicon = findViewById(R.id.playicon);
        tv_header = findViewById(R.id.tv_header);

      /*  Util.logomethod(this, headerlogoIv);*/

        progressView = findViewById(R.id.progressView);
        feedprogress = findViewById(R.id.feedprogress);
        videoplayer = findViewById(R.id.videoplayer);
        nameTv = findViewById(R.id.nameTv);
        nameTv.setTextColor(Color.parseColor(colorActive));
        companyTv = findViewById(R.id.companyTv);
        dateTv = findViewById(R.id.dateTv);
        designationTv = findViewById(R.id.designationTv);
        headingTv = findViewById(R.id.headingTv);
        like_list = findViewById(R.id.like_list);
        relative = findViewById(R.id.relative);
        testdata = findViewById(R.id.testdata);
        card_view = findViewById(R.id.card_view);
        ll_dots = findViewById(R.id.ll_dots);
        vp_slider = findViewById(R.id.vp_slider);
        procializeDB = new DBHelper(LikeDetailActivity.this);
        db = procializeDB.getReadableDatabase();
        dbHelper = new DBHelper(LikeDetailActivity.this);
        customers = new ArrayList<AttendeeList>();

        procializeDB.getReadableDatabase();
        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            relative.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            relative.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        Intent intent = getIntent();
        try {
            if (intent != null) {
                lname = intent.getStringExtra("lname");
                fname = intent.getStringExtra("fname");
                if (fname == null) {
                    fname = "";
                }

                if (lname == null) {
                    lname = "";
                }
                company = intent.getStringExtra("company");
                designation = intent.getStringExtra("designation");
                heading = intent.getStringExtra("heading");
                date = intent.getStringExtra("date");
                if (intent.getStringExtra("Likes") == null) {
                    Likes = "1";
                } else {
                    Likes = intent.getStringExtra("Likes");
                }

                if (intent.getStringExtra("Comments") == null) {
                    Comments = "1";
                } else {
                    Comments = intent.getStringExtra("Comments");
                }

                try {
                    myList = (ArrayList<news_feed_media>) getIntent().getSerializableExtra("media_list");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Likeflag = intent.getStringExtra("Likeflag");
                noti_type = intent.getStringExtra("noti_type");

                if (noti_type.equalsIgnoreCase("Notification")) {
                    noti_profileurl = intent.getStringExtra("profilepic");
                } else if (noti_type.equalsIgnoreCase("Wall_Post")) {
                    profileurl = intent.getStringExtra("profilepic");
                }

                type = intent.getStringExtra("type");


                feedid = intent.getStringExtra("feedid");

                p1 = intent.getFloatExtra("AspectRatio", (float) 0.000);

                if (type.equalsIgnoreCase("Image")) {
                    feedurl = intent.getStringExtra("url");

                    Log.e("feedurl", feedurl);

                } else if (type.equalsIgnoreCase("Gif")) {
                    feedurl = intent.getStringExtra("url");

                    Log.e("feedurl", feedurl);

                } else if (type.equalsIgnoreCase("Video")) {
                    thumbImg = intent.getStringExtra("thumbImg");
                    videourl = intent.getStringExtra("videourl");
                }
                Log.e("p", p1 + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        nameTv.setText(fname + " " + lname);
        companyTv.setText(company);
        designationTv.setText(designation);
        testdata.setText(StringEscapeUtils.unescapeJava(heading));

        final SpannableStringBuilder stringBuilder = new SpannableStringBuilder(testdata.getText());
        if (heading != null) {
            headingTv.setVisibility(View.VISIBLE);
            int flag = 0;
            for (int i = 0; i < stringBuilder.length(); i++) {
                String sample = stringBuilder.toString();
                if ((stringBuilder.charAt(i) == '<')) {
                    try {
                        String text = "<";
                        String text1 = ">";

                        if (flag == 0) {
                            int start = sample.indexOf(text, i);
                            int end = sample.indexOf(text1, i);

                            Log.v("Indexes of", "Start : " + start + "," + end);
                            try {
                                substring = sample.substring(start, end + 1);
                                Log.v("String names: ", substring);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            if (substring.contains("<")) {
                                if (sample.contains(substring)) {
                                    substring = substring.replace("<", "");
                                    substring = substring.replace(">", "");
                                    int index = substring.indexOf("^");
//                                    substring = substring.replace("^", "");
                                    final String attendeeid = substring.substring(0, index);
                                    substring = substring.substring(index + 1, substring.length());


                                    stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    stringBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                                    stringBuilder.setSpan(new ClickableSpan() {
                                        @Override
                                        public void onClick(View widget) {
                                            attendeeDBList = dbHelper.getAttendeeDetailsId(attendeeid);
                                            Intent intent = new Intent(LikeDetailActivity.this, com.procialize.mrgeApp20.Activity.AttendeeDetailActivity.class);
                                            intent.putExtra("id", attendeeDBList.get(0).getAttendeeId());
                                            intent.putExtra("name", attendeeDBList.get(0).getFirstName() + " " + attendeeDBList.get(0).getLastName());
                                            intent.putExtra("city", attendeeDBList.get(0).getCity());
                                            intent.putExtra("country", attendeeDBList.get(0).getCountry());
                                            intent.putExtra("company", attendeeDBList.get(0).getCompanyName());
                                            intent.putExtra("designation", attendeeDBList.get(0).getDesignation());
                                            intent.putExtra("description", attendeeDBList.get(0).getDescription());
                                            intent.putExtra("profile", attendeeDBList.get(0).getProfilePic());
                                            startActivity(intent);
                                        }
                                    }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    stringBuilder.replace(start, end + 1, substring);
                                    testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                    headingTv.setMovementMethod(LinkMovementMethod.getInstance());
                                    headingTv.setText(stringBuilder);
                                    flag = 1;
//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        attendees.setComment(substring);
                                }
                            }
                        } else {

                            int start = sample.indexOf(text, i);
                            int end = sample.indexOf(text1, i);

                            Log.v("Indexes of", "Start : " + start + "," + end);
                            try {
                                substring = sample.substring(start, end + 1);
                                Log.v("String names: ", substring);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (substring.contains("<")) {
                                if (sample.contains(substring)) {
                                    substring = substring.replace("<", "");
                                    substring = substring.replace(">", "");
                                    int index = substring.indexOf("^");
//                                    substring = substring.replace("^", "");
                                    final String attendeeid = substring.substring(0, index);
                                    substring = substring.substring(index + 1, substring.length());


                                    stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    stringBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    stringBuilder.setSpan(new ClickableSpan() {
                                        @Override
                                        public void onClick(View widget) {
                                            attendeeDBList = dbHelper.getAttendeeDetailsId(attendeeid);
                                            Intent intent = new Intent(LikeDetailActivity.this, AttendeeDetailActivity.class);
                                            intent.putExtra("id", attendeeDBList.get(0).getAttendeeId());
                                            intent.putExtra("name", attendeeDBList.get(0).getFirstName() + " " + attendeeDBList.get(0).getLastName());
                                            intent.putExtra("city", attendeeDBList.get(0).getCity());
                                            intent.putExtra("country", attendeeDBList.get(0).getCountry());
                                            intent.putExtra("company", attendeeDBList.get(0).getCompanyName());
                                            intent.putExtra("designation", attendeeDBList.get(0).getDesignation());
                                            intent.putExtra("description", attendeeDBList.get(0).getDescription());
                                            intent.putExtra("profile", attendeeDBList.get(0).getProfilePic());
                                            startActivity(intent);
                                        }
                                    }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    stringBuilder.replace(start, end + 1, substring);
                                    testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                    headingTv.setMovementMethod(LinkMovementMethod.getInstance());

                                    headingTv.setText(stringBuilder);


//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        attendees.setComment(substring);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            headingTv.setText(stringBuilder);
        } else {
            headingTv.setVisibility(View.GONE);
        }


        if (noti_type.equalsIgnoreCase("Notification")) {

            if (noti_profileurl != null) {

                Glide.with(this).load(ApiConstant.profilepic + noti_profileurl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        profileIV.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(profileIV);
            } else {
                progressView.setVisibility(View.GONE);
            }

        } else if (noti_type.equalsIgnoreCase("Wall_Post")) {
            if (profileurl != null) {

                Glide.with(this).load(profileurl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        profileIV.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(profileIV);
            } else {
                progressView.setVisibility(View.GONE);
            }
        }

        if (myList != null) {
            if (myList.size() >= 1) {

                card_view.setVisibility(View.VISIBLE);
                feedimageIv.setVisibility(View.GONE);
                videoplayer.setVisibility(View.GONE);
                playicon.setVisibility(View.GONE);
                final ArrayList<String> imagesSelectednew = new ArrayList<>();
                final ArrayList<String> imagesSelectednew1 = new ArrayList<>();
                final ImageView[] ivArrayDotsPager;
                for (int i = 0; i < myList.size(); i++) {
                    imagesSelectednew.add(ApiConstant.newsfeedwall + myList.get(i).getMediaFile());
                    if (myList.get(i).getMediaFile().contains("mp4")) {
                        imagesSelectednew1.add(ApiConstant.newsfeedwall + myList.get(i).getThumb_image());
                    } else {
                        imagesSelectednew1.add("");
                    }
                }

                SwipeMultimediaAdapter swipepagerAdapter = new SwipeMultimediaAdapter(LikeDetailActivity.this, imagesSelectednew, imagesSelectednew1,myList);
                vp_slider.setAdapter(swipepagerAdapter);
                swipepagerAdapter.notifyDataSetChanged();

                if (imagesSelectednew.size() > 1) {
                    ivArrayDotsPager = new ImageView[imagesSelectednew.size()];
                    setupPagerIndidcatorDots(0, ll_dots, imagesSelectednew.size());
                    vp_slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            JzvdStd.releaseAllVideos();
                        }

                        @Override
                        public void onPageSelected(int position1) {
                            JzvdStd.releaseAllVideos();
                            setupPagerIndidcatorDots(position1, ll_dots, imagesSelectednew.size());
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                            JzvdStd.releaseAllVideos();
                        }
                    });
                }
            }
        } else if (type.equals("Image")) {
            //photo

            if (feedurl != null) {
                feedimageIv.setVisibility(View.VISIBLE);
                videoplayer.setVisibility(View.GONE);
                playicon.setVisibility(View.GONE);
                Glide.with(this).load(feedurl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        feedprogress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        feedprogress.setVisibility(View.GONE);
                        return false;
                    }
                }).into(feedimageIv).onLoadStarted(getDrawable(R.drawable.gallery_placeholder));
            } else {
                feedprogress.setVisibility(View.GONE);
            }

        } else if (type.equals("Gif")) {
            //photo

            if (feedurl != null) {
                feedimageIv.setVisibility(View.VISIBLE);
                videoplayer.setVisibility(View.GONE);
                playicon.setVisibility(View.GONE);
                feedprogress.setVisibility(View.GONE);
                Glide.with(videoplayer).load(feedurl).into(feedimageIv);

            }
        } else if (type.equals("Video")) {
            //video

            if (videourl != null) {
                feedimageIv.setVisibility(View.GONE);
                videoplayer.setVisibility(View.VISIBLE);
                playicon.setVisibility(View.GONE);
              /*  videoplayer.setUp(videourl
                        , JzvdStd.SCREEN_WINDOW_NORMAL, "");*/

                videoplayer.setUp(videourl,""
                        , JzvdStd.SCREEN_NORMAL);

                Glide.with(LikeDetailActivity.this).load(videourl).into(videoplayer.thumbImageView);


                feedprogress.setVisibility(View.GONE);
            }

        } else if (type.equals("Status")) {
            feedimageIv.setVisibility(View.GONE);
            feedprogress.setVisibility(View.GONE);
        }

        mAPIService = ApiUtils.getAPIService();


        SessionManager sessionManager = new SessionManager(LikeDetailActivity.this);

        user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);

        mAPIService.postLikeUserList(token, feedid, eventid).enqueue(new Callback<LikeListing>() {
            @Override
            public void onResponse(Call<LikeListing> call, Response<LikeListing> response) {
//                Log.d("","LikeListResp"+response.body());
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    showPostLikeListresponse(response);
                } else {
//                    dismissProgress();
                    Toast.makeText(LikeDetailActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LikeListing> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
//                dismissProgress();
                Toast.makeText(LikeDetailActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void showPostLikeListresponse(Response<LikeListing> response) {

        if (response.body().getStatus().equalsIgnoreCase("success")) {

            Log.e("post", "success");
            attendeeLists = new ArrayList<>();

            attendeeLists = response.body().getAttendeeList();
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            likeAdapter = new LikeAdapter(LikeDetailActivity.this, attendeeLists);
            like_list.setLayoutManager(layoutManager);
            likeAdapter.notifyDataSetChanged();
            like_list.setAdapter(likeAdapter);

            tv_header.setText(attendeeLists.size() +" Likes");
//            like_list.scheduleLayoutAnimation();

        } else {
            Log.e("post", "fail");
            Log.e("list", response.body().getAttendeeList().size() + "");
//            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onContactSelected(AttendeeList attendeeList) {
//
//    }

    private void setupPagerIndidcatorDots(int currentPage, LinearLayout ll_dots, int size) {

        TextView[] dots = new TextView[size];
        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(LikeDetailActivity.this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(Color.parseColor("#343434"));
            ll_dots.addView(dots[i]);
        }

        try {
            if (dots.length > 0) {
                if (dots.length != currentPage) {
                    dots[currentPage].setTextColor(Color.parseColor("#A2A2A2"));
                }
            }
        } catch (Exception e) {

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JzvdStd.releaseAllVideos();
    }
}