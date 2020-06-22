package com.procialize.mrgeApp20.NewsFeed.Views.Fragment;


import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.procialize.mrgeApp20.Activity.LoginActivity;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.Background.BackgroundService;
import com.procialize.mrgeApp20.BuildConfig;
import com.procialize.mrgeApp20.CustomTools.MyJzvdStd;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.Analytic;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.GetterSetter.DeletePost;
import com.procialize.mrgeApp20.GetterSetter.FetchAttendee;
import com.procialize.mrgeApp20.GetterSetter.FetchFeed;
import com.procialize.mrgeApp20.GetterSetter.LikePost;
import com.procialize.mrgeApp20.GetterSetter.NewsFeedList;
import com.procialize.mrgeApp20.GetterSetter.NewsFeedPostMultimedia;
import com.procialize.mrgeApp20.GetterSetter.ReportPost;
import com.procialize.mrgeApp20.GetterSetter.ReportPostHide;
import com.procialize.mrgeApp20.GetterSetter.ReportUser;
import com.procialize.mrgeApp20.GetterSetter.ReportUserHide;
import com.procialize.mrgeApp20.GetterSetter.news_feed_media;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.NewsFeed.Views.Activity.CommentActivity;
import com.procialize.mrgeApp20.NewsFeed.Views.Activity.ImageViewActivity;
import com.procialize.mrgeApp20.NewsFeed.Views.Activity.LikeDetailActivity;
import com.procialize.mrgeApp20.NewsFeed.Views.Activity.PostNewActivity;
import com.procialize.mrgeApp20.NewsFeed.Views.Adapter.NewsFeedAdapterRecycler;
import com.procialize.mrgeApp20.NewsFeed.Views.RecyclerItemTouchHelper;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.util.GetUserActivityReport;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.NewsFeed.Views.Adapter.NewsFeedAdapterRecycler.swipableAdapterPosition;
import static com.procialize.mrgeApp20.Session.SessionManager.MY_PREFS_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewsFeed extends Fragment implements View.OnClickListener, NewsFeedAdapterRecycler.FeedAdapterListner, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    public static SwipeRefreshLayout newsfeedrefresh;
    public static List<NewsFeedList> newsfeedList;
    HashMap<String, String> user;
    BottomSheetDialog dialog;
    //ProgressBar progressbar;
    View rootView;
    LinearLayout ll_mind;
    SQLiteDatabase db;
    ArrayList<NewsFeedPostMultimedia> newsFeedPostMultimediaList;
    SessionManager session;
    String token, eventid, colorActive;
    TextView tv_uploading;
    NewsFeedAdapterRecycler feedAdapter;
    RecyclerView feedrecycler;
    String reaction_type;
    RelativeLayout relative;
    SessionManager sessionManager;
    List<news_feed_media> news_feed_mediaDB = new ArrayList<news_feed_media>();
    String strPath;
    BackgroundReceiver mReceiver;
    IntentFilter mFilter;
    ImageView profileIV;
    ProgressBar progressView;
    private DBHelper procializeDB;
    private List<NewsFeedList> newsfeedsDBList;
    private APIService mAPIService;
    private ConnectionDetector cd;
    private List<news_feed_media> news_feed_media;
    private List<AttendeeList> attendeeList;
    Dialog myDialog;

    public FragmentNewsFeed() {
        // Required empty public constructor
    }

    static public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
//            bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".android.fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void shareImage(final String data, String url, final Context context) {
        final Dialog dialog = new Dialog(context);
        Picasso.with(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                dialog.dismiss();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_SUBJECT, "Shared via MRGE app");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));

                context.startActivity(Intent.createChooser(i, "Shared via MRGE app"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                dialog.dismiss();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {


                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_progress);
                dialog.show();

                //Toast.makeText(context, "Please wait for image download", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);
        setHasOptionsMenu(false);
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // setHasOptionsMenu(false);
    }

    private void initView() {
        session = new SessionManager(getActivity());
        user = session.getUserDetails();
        token = user.get(SessionManager.KEY_TOKEN);

        profileIV = rootView.findViewById(R.id.profilestatus);
        progressView = rootView.findViewById(R.id.progressView);

        HashMap<String, String> user = session.getUserDetails();
        String profilepic = user.get(SessionManager.KEY_PIC);
        if (profilepic != null) {
            Glide.with(this).load(ApiConstant.profilepic + profilepic).circleCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                            progressView.setVisibility(View.GONE);
                            profileIV.setImageResource(R.drawable.profilepic_placeholder);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressView.setVisibility(View.GONE);
                            profileIV.setImageResource(R.drawable.profilepic_placeholder);
                            return false;
                        }
                    }).into(profileIV);
        } else {
            profileIV.setImageResource(R.drawable.profilepic_placeholder);
            progressView.setVisibility(View.GONE);
        }
        mReceiver = new BackgroundReceiver();
        // Creating an IntentFilter with action
        mFilter = new IntentFilter(ApiConstant.BROADCAST_ACTION);
        // Registering BroadcastReceiver with this activity for the intent filter
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, mFilter);

        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();
        //progressbar = rootView.findViewById(R.id.progressbar);
        cd = new ConnectionDetector(getActivity());


        mAPIService = ApiUtils.getAPIService();

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        ll_mind = rootView.findViewById(R.id.ll_mind);
        ll_mind.setOnClickListener(this);
        String colorActive = prefs.getString("colorActive", "");

        newsfeedrefresh = rootView.findViewById(R.id.newsfeedrefresh);
        feedrecycler = rootView.findViewById(R.id.recycler_view);
        relative = rootView.findViewById(R.id.relative);

        tv_uploading = rootView.findViewById(R.id.tv_uploading);
        try {
            procializeDB = new DBHelper(getActivity());
            db = procializeDB.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getDataFromLocalDB();

        newsFeedPostMultimediaList = procializeDB.getNotUploadedMultiMedia();
        // insertMediaToLocalDb();

        List<news_feed_media> mediafeedDetails = procializeDB.getBuzzMediaFeedDetails();
        List<news_feed_media> mediafeedDetails1 = mediafeedDetails;
        //ArrayList<NewsFeedPostMultimedia> newsFeedPostMultimediaList = dbHelper.getNotUploadedMultiMedia();

        if (newsFeedPostMultimediaList.size() > 0) {
            Intent intent = new Intent(getActivity(), BackgroundService.class);
            PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, intent, PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent == null) {
                // progressbarForSubmit.setVisibility(View.VISIBLE);
                tv_uploading.setVisibility(View.VISIBLE);
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(1000); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                tv_uploading.startAnimation(anim);
            } /*else {
                // progressbarForSubmit.setVisibility(View.GONE);    // "service is already running!";
                tv_uploading.setVisibility(View.GONE);    // "service is already running!";
            }*/

            intent.putExtra("arrayListNewsFeedMultiMedia", newsFeedPostMultimediaList);
            intent.putExtra("api_access_token", token);
            intent.putExtra("event_id", eventid);
            intent.putExtra("status", "");
            getActivity().startService(intent);
        } else {
            tv_uploading.clearAnimation();
            tv_uploading.setVisibility(View.GONE);
            if (procializeDB.getCountOfNotUploadedMultiMedia() == 0) {
                File dir = new File(Environment.getExternalStorageDirectory() + "/AlbumCache");
                if (dir.isDirectory()) {
                    String[] children = dir.list();
                    if (children != null) {
                        for (int i = 0; i < children.length; i++) {
                            new File(dir, children[i]).delete();
                        }
                    }
                }
            }
        }

        if (cd.isConnectingToInternet()) {
            fetchFeed(token, eventid);
        } else {
            Toast.makeText(getContext(), "No Internet Connection..!!", Toast.LENGTH_SHORT).show();
            if (newsfeedrefresh.isRefreshing()) {
                newsfeedrefresh.setRefreshing(false);
            }
        }

        mAPIService.AttendeeFetchPost(token, eventid).enqueue(new Callback<FetchAttendee>() {
            @Override
            public void onResponse(Call<FetchAttendee> call, Response<FetchAttendee> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        sessionManager.logoutUser();
                        Intent main = new Intent(getContext(), LoginActivity.class);
                        startActivity(main);
                        getActivity().finish();
                    } else {
                        showResponseAttendee(response);
                    }
                } else {

                    //Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAttendee> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                //  Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });

        newsfeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //fetchFeed(token,eventid);
                if (cd.isConnectingToInternet()) {
                    fetchFeed(token, eventid);
                } else {
                    Toast.makeText(getContext(), "No Internet Connection..!!", Toast.LENGTH_SHORT).show();
                    if (newsfeedrefresh.isRefreshing()) {
                        newsfeedrefresh.setRefreshing(false);
                    }
                }
            }
        });

        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(getActivity(), token,
                eventid,
                ApiConstant.pageVisited,
                "5",
                "");
        getUserActivityReport.userActivityReport();
    }

    public void showResponseAttendee(Response<FetchAttendee> response) {

        // specify an adapter (see also next example)
        attendeeList = response.body().getAttendeeList();
        procializeDB.clearAttendeesTable();
        procializeDB.insertAttendeesInfo(attendeeList, db);
        //attendeesDBList = dbHelper.getAttendeeDetails();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_mind:
                Intent postview = new Intent(getActivity(), PostNewActivity.class);
                startActivity(postview);
                break;
        }
    }

    public void getDataFromLocalDB() {
        db = procializeDB.getReadableDatabase();
        // news_feed_mediaDBB = procializeDB.getBuzzMediaFeedDetails();
        //newsfeedsDBList = dbHelper.getBuzzFeedDetails();
        newsfeedsDBList = procializeDB.getNewsFeedDetails();

//        if (newsfeedsDBList.size() > 0) {
//            post_layout.setVisibility(View.GONE);
//        } else {
//            post_layout.setVisibility(View.VISIBLE);
//        }

        if (newsfeedsDBList.size() == 0) {
//            NewsFeedList newsFeedList = new NewsFeedList();
//            newsfeedsDBList.add(newsFeedList);
            //buzzDBAdapter = new BuzzDBAdapter(getActivity(), newsfeedsDBList, BuzzFragment.this, false);
            setAdapter(newsfeedsDBList);
        } else {
            NewsFeedList newsFeedList = new NewsFeedList();
            newsfeedsDBList.add(newsFeedList);
            setAdapter(newsfeedsDBList);

        }

        if (newsfeedrefresh.isRefreshing()) {
            newsfeedrefresh.setRefreshing(false);
        }
    }

    public void setAdapter(List<NewsFeedList> newsfeedsList) {
        //Parcelable state = feedrecycler.onSaveInstanceState();

        feedAdapter = new NewsFeedAdapterRecycler(getActivity(), newsfeedsList, FragmentNewsFeed.this, true, relative);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        feedrecycler.setLayoutManager(mLayoutManager);
        feedrecycler.setItemAnimator(new DefaultItemAnimator());
        feedrecycler.setAdapter(feedAdapter);
//        feedrecycler.setSelection(mCurrentX);
        //feedrecycler.onRestoreInstanceState(state);
//        feedrecycler.scheduleLayoutAnimation();

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param

/*
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this, newsfeedsList);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(feedrecycler);*/
    }


    @Override
    public void onContactSelected(NewsFeedList feed, ImageView imageView, int position) {
        List<news_feed_media> news_feed_mediaC = new ArrayList<news_feed_media>();
        news_feed_mediaC = feed.getNews_feed_media();
        if (news_feed_mediaC.size() == 1) {
            if (news_feed_mediaC.get(0).getMedia_type().equals("Image")) {
                Intent imageview = new Intent(getContext(), ImageViewActivity.class);
                imageview.putExtra("url", ApiConstant.newsfeedwall + news_feed_mediaC.get(0).getMediaFile());
                startActivity(imageview);
            }
        }
    }

    @Override
    public void likeTvViewOnClick(View v, NewsFeedList feed, int position, ImageView likeimage, TextView liketext) {

        int count = Integer.parseInt(feed.getTotalLikes());

        Drawable drawables = likeimage.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawables).getBitmap();

        Bitmap bitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_like)).getBitmap();


//        if(!drawables[2].equals(R.drawable.ic_like)){
        if (bitmap != bitmap2) {
            reaction_type = "";
            feed.setLikeFlag("");
            likeimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
//            likeimage.setBackgroundResource(R.drawable.ic_like);
            if (cd.isConnectingToInternet()) {
                PostLike(reaction_type, eventid, feed.getNewsFeedId(), token);
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            try {

                if (count > 0) {
                    count = count - 1;
                    feed.setTotalLikes(String.valueOf(count));

                    if (count == 1) {
                        liketext.setText(count + " Like");
                    } else {
                        liketext.setText(count + " Likes");
                    }

                    feed.setTotalLikes(String.valueOf(count));

                } else {
                    liketext.setText("0" + " Likes");
                    feed.setTotalLikes("0");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            feed.setLikeFlag("1");
            likeimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_afterlike));
            //setTextViewDrawableColor(likeimage, colorActive);
            reaction_type = "0";
//            likeimage.setBackgroundResource(R.drawable.ic_afterlike);
            if (cd.isConnectingToInternet()) {
                PostLike(reaction_type, eventid, feed.getNewsFeedId(), token);
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

            try {

                count = count + 1;
                if (count == 1) {
                    liketext.setText(count + " Like");
                } else {
                    liketext.setText(count + " Likes");
                }

                feed.setTotalLikes(String.valueOf(count));

                feed.setTotalLikes(String.valueOf(count));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // newsfeedList.set(position, feed);


    }

    @Override
    public void commentTvViewOnClick(View v, NewsFeedList feed, int position) {


        float width = Float.parseFloat(feed.getWidth());
        float height = Float.parseFloat(feed.getHeight());

        float p1 = height / width;
        int feed_pos = position;

        //feedrecycler.setSelection(feed_pos);
        feedrecycler.smoothScrollToPosition(feed_pos);
        Intent comment = new Intent(getContext(), CommentActivity.class);

        comment.putExtra("fname", feed.getFirstName());
        comment.putExtra("lname", feed.getLastName());
        comment.putExtra("company", feed.getCompanyName());
        comment.putExtra("designation", feed.getDesignation());

        comment.putExtra("heading", feed.getPostStatus());
        comment.putExtra("date", feed.getPostDate());
        comment.putExtra("Likes", feed.getTotalLikes());
        comment.putExtra("Likeflag", feed.getLikeFlag());
        comment.putExtra("Comments", feed.getTotalComments());
        comment.putExtra("profilepic", ApiConstant.profilepic + feed.getProfilePic());
        comment.putExtra("type", feed.getType());
        comment.putExtra("feedid", feed.getNewsFeedId());

        comment.putExtra("AspectRatio", p1);
        comment.putExtra("noti_type", "Wall_Post");

        news_feed_media = feed.getNews_feed_media();
       // if (news_feed_media.size() >= 1) {
            comment.putExtra("media_list", (Serializable) news_feed_media);
        /*} else if (news_feed_media.size() > 0) {
            comment.putExtra("type", news_feed_media.get(0).getMedia_type());

            width = Float.parseFloat(news_feed_media.get(0).getWidth());
            height = Float.parseFloat(news_feed_media.get(0).getHeight());
            if (news_feed_media.get(0).getMedia_type().equalsIgnoreCase("Image")) {
                comment.putExtra("url", ApiConstant.newsfeedwall + news_feed_media.get(0).getMediaFile());
            } else if (news_feed_media.get(0).getMedia_type().equalsIgnoreCase("Video")) {
                comment.putExtra("videourl", ApiConstant.newsfeedwall + news_feed_media.get(0).getMediaFile());
                comment.putExtra("thumbImg", ApiConstant.newsfeedwall + news_feed_media.get(0).getThumb_image());
            }
    }else {
            comment.putExtra("type", "status");
        }*/
        comment.putExtra("flag", "noti");
        startActivity(comment);

    }

    @Override
    public void shareTvFollowOnClick(View v, NewsFeedList feedList, ImageView imageView, int position) {
        final List<news_feed_media> newsFeedMedia = feedList.getNews_feed_media();
        if (feedList != null) {
            if (cd.isConnectingToInternet()) {
                if (newsFeedMedia.size() > 0) {

                    if (newsFeedMedia.size() < swipableAdapterPosition) {
                        swipableAdapterPosition = 0;
                    }
                    if (newsFeedMedia.get(swipableAdapterPosition).getMedia_type().equals("Video")) {
                        boolean isPresentFile = false;
                        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/" + ApiConstant.folderName);
                        if (dir.isDirectory()) {
                            String[] children = dir.list();
                            for (int i = 0; i < children.length; i++) {
                                String filename = children[i].toString();
                                if (newsFeedMedia.get(swipableAdapterPosition).getMediaFile().equals(filename)) {
                                    isPresentFile = true;
                                }
                            }
                        }

                        if (!isPresentFile) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Download and Share");
                            builder.setMessage("Video will be share only after download,\nDo you want to continue for download and share?");
                            builder.setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            new DownloadFile().execute(ApiConstant.newsfeedwall + newsFeedMedia.get(swipableAdapterPosition).getMediaFile());
                                        }
                                    });
                            builder.show();

                        } else if (isPresentFile) {
                            String folder = Environment.getExternalStorageDirectory().toString() + "/" + ApiConstant.folderName + "/";
                            //Create androiddeft folder if it does not exist
                            File directory = new File(folder);
                            if (!directory.exists()) {
                                directory.mkdirs();
                            }
                            strPath = folder + newsFeedMedia.get(swipableAdapterPosition).getMediaFile();
                              /*              ContentValues content = new ContentValues(4);
                                            content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                                                    System.currentTimeMillis() / 1000);
                                            content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                                            content.put(MediaStore.Video.Media.DATA, strPath);
                                            ContentResolver resolver = getActivity().getContentResolver();
                                            Uri uri =strPath; resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);*/
                            Uri contentUri = FileProvider.getUriForFile(getActivity(),
                                    BuildConfig.APPLICATION_ID + ".android.fileprovider", new File(strPath));

                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("video/*");
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared via MRGE app");
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, "");
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                            startActivity(Intent.createChooser(sharingIntent, "Shared via MRGE app"));
                        }
                    } else {
                        shareImage(feedList.getPostDate() + "\n" + feedList.getPostStatus(), ApiConstant.newsfeedwall + newsFeedMedia.get(swipableAdapterPosition).getMediaFile(), getContext());
                    }
                } else {
                    shareTextUrl(feedList.getPostDate() + "\n" + feedList.getPostStatus(), StringEscapeUtils.unescapeJava(feedList.getPostStatus()));
                }
            }
        }
    }

    private void shareTextUrl(String data, String url) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Shared via MRGE app");
        share.putExtra(Intent.EXTRA_TEXT, url);

        startActivity(Intent.createChooser(share, "Shared via MRGE app!"));
    }

    @Override
    public void moreTvFollowOnClick(View v, final NewsFeedList feed, final int position) {
        dialog = new BottomSheetDialog(getActivity());

        dialog.setContentView(R.layout.botomfeeddialouge);


        TextView reportTv = dialog.findViewById(R.id.reportTv);
        TextView hideTv = dialog.findViewById(R.id.hideTv);
        TextView deleteTv = dialog.findViewById(R.id.deleteTv);
        TextView reportuserTv = dialog.findViewById(R.id.reportuserTv);
        TextView blockuserTv = dialog.findViewById(R.id.blockuserTv);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);
        TextView editIV = dialog.findViewById(R.id.editIV);


        if (user.get(SessionManager.ATTENDEE_STATUS).equalsIgnoreCase("1")) {
            if (user.get(SessionManager.KEY_ID).equalsIgnoreCase(feed.getAttendeeId())) {
                deleteTv.setVisibility(View.VISIBLE);
                //editIV.setVisibility(View.VISIBLE);
                hideTv.setVisibility(View.GONE);
                reportTv.setVisibility(View.GONE);
                reportuserTv.setVisibility(View.GONE);
                blockuserTv.setVisibility(View.GONE);

                if (feed.getType().equalsIgnoreCase("Video")) {
                    editIV.setVisibility(View.GONE);

                } else {
                    editIV.setVisibility(View.GONE);

                }
            } else {
                deleteTv.setVisibility(View.VISIBLE);
                //editIV.setVisibility(View.VISIBLE);
                hideTv.setVisibility(View.GONE);
                reportTv.setVisibility(View.GONE);
                reportuserTv.setVisibility(View.GONE);
                blockuserTv.setVisibility(View.GONE);
                editIV.setVisibility(View.GONE);
            }

        } else {
            if (user.get(SessionManager.KEY_ID).equalsIgnoreCase(feed.getAttendeeId())) {
                deleteTv.setVisibility(View.VISIBLE);
                //editIV.setVisibility(View.VISIBLE);
                hideTv.setVisibility(View.GONE);
                reportTv.setVisibility(View.GONE);
                reportuserTv.setVisibility(View.GONE);
                blockuserTv.setVisibility(View.GONE);

                if (feed.getType().equalsIgnoreCase("Video")) {
                    editIV.setVisibility(View.GONE);

                } else {
                    editIV.setVisibility(View.GONE);

                }

            } else {
                deleteTv.setVisibility(View.GONE);
                editIV.setVisibility(View.GONE);
                hideTv.setVisibility(View.VISIBLE);
                reportTv.setVisibility(View.VISIBLE);
                reportuserTv.setVisibility(View.VISIBLE);
                blockuserTv.setVisibility(View.GONE);
            }

        }
        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDelete(eventid, feed.getNewsFeedId(), token, position);
            }
        });


        hideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportPostHide(eventid, feed.getNewsFeedId(), token, position);
            }
        });

        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge("reportPost", feed.getNewsFeedId());
            }
        });


        reportuserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge("reportUser", feed.getAttendeeId());
                //ReportUser(eventid, feed.getAttendeeId(), token);

            }
        });

        blockuserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReportUserHide(eventid, feed.getAttendeeId(), token);

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
    public void moreLikeListOnClick(View v, NewsFeedList feed, int position) {
        float width = Float.parseFloat(feed.getWidth());
        float height = Float.parseFloat(feed.getHeight());

        float p1 = height / width;

        Intent intent = new Intent(getActivity(), LikeDetailActivity.class);
        intent.putExtra("fname", feed.getFirstName());
        intent.putExtra("lname", feed.getLastName());
        intent.putExtra("company", feed.getCompanyName());
        intent.putExtra("designation", feed.getDesignation());

        intent.putExtra("heading", feed.getPostStatus());
        intent.putExtra("date", feed.getPostDate());
        intent.putExtra("Likes", feed.getTotalLikes());
        intent.putExtra("Likeflag", feed.getLikeFlag());
        intent.putExtra("Comments", feed.getTotalComments());
        intent.putExtra("profilepic", ApiConstant.profilepic + feed.getProfilePic());
//        intent.putExtra("type", feed.getType());
        intent.putExtra("feedid", feed.getNewsFeedId());
        intent.putExtra("AspectRatio", p1);
        intent.putExtra("noti_type", "Wall_Post");

        news_feed_media = feed.getNews_feed_media();
        if (news_feed_media.size() >= 1) {
            intent.putExtra("media_list", (Serializable) news_feed_media);
        } else if (news_feed_media.size() > 0) {
            intent.putExtra("type", news_feed_media.get(0).getMedia_type());
            if (news_feed_media.get(0).getMedia_type().equalsIgnoreCase("Image")) {
                intent.putExtra("url", ApiConstant.newsfeedwall + news_feed_media.get(0).getMediaFile());
            } else if (news_feed_media.get(0).getMedia_type().equalsIgnoreCase("Video")) {
                intent.putExtra("videourl", ApiConstant.newsfeedwall + news_feed_media.get(0).getMediaFile());
                intent.putExtra("thumbImg", ApiConstant.newsfeedwall + news_feed_media.get(0).getThumb_image());
            }
        } else {
            intent.putExtra("type", "status");
        }
        intent.putExtra("flag", "noti");
        startActivity(intent);

    }

    @Override
    public void FeedEditOnClick(View v, NewsFeedList feed, int position) {

    }

    public void PostLike(String reaction_type, String eventid, String feedid, String token) {
//        showProgress();
        mAPIService.postLike(eventid, feedid, token).enqueue(new Callback<LikePost>() {
            @Override
            public void onResponse(Call<LikePost> call, Response<LikePost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showPostlikeresponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LikePost> call, Throwable t) {
                Toast.makeText(getActivity(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    private void showPostlikeresponse(Response<LikePost> response) {

        if (response.body().getStatus().equals("Success")) {
            Log.e("post", "success");

            /*if (cd.isConnectingToInternet()) {
                fetchFeed(token, eventid);
            }*/
//            fetchFeed(token, eventid);
        } else {
            Log.e("post", "fail");
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchFeed(String token, String eventid) {

        /// showProgress();

        mAPIService.FeedFetchPost(token, eventid).enqueue(new Callback<FetchFeed>() {
            @Override
            public void onResponse(Call<FetchFeed> call, Response<FetchFeed> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (newsfeedrefresh.isRefreshing()) {
                        newsfeedrefresh.setRefreshing(false);
                    }

                    dismissProgress();
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        sessionManager.logoutUser();
                        Intent main = new Intent(getContext(), MrgeHomeActivity.class);
                        startActivity(main);
                        getActivity().finish();

                    } else {
                        showResponse(response);
                    }
                } else {

                    dismissProgress();
                    if (newsfeedrefresh.isRefreshing()) {
                        newsfeedrefresh.setRefreshing(false);
                    }
                    //Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchFeed> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                //  Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (newsfeedrefresh.isRefreshing()) {
                    newsfeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void dismissProgress() {

       /* if (progressbar.getVisibility() == View.VISIBLE) {
            progressbar.setVisibility(View.GONE);
        }*/
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showResponse(Response<FetchFeed> response) {

        if (response.isSuccessful()) {
            Log.i("hit", "post submitted to API." + response.body().toString());
            // for (int i = 0; i < response.body().getLive_steaming_info().size(); i++) {
            MrgeHomeActivity.zoom_meeting_id = response.body().getLive_steaming_info().getZoom_meeting_id();
            MrgeHomeActivity.zoom_password = response.body().getLive_steaming_info().getZoom_password();
            MrgeHomeActivity.zoom_status = response.body().getLive_steaming_info().getZoom_status();

            MrgeHomeActivity.zoom_time = response.body().getLive_steaming_info().getZoom_datetime();
            if(MrgeHomeActivity.youTubeApiLists.size()>0){
                MrgeHomeActivity.youTubeApiLists.clear();
            }

             if (response.body().getYoutube_info().size() > 0) {
                MrgeHomeActivity. youTubeApiLists = response.body().getYoutube_info();
                if(MrgeHomeActivity.youTubeApiLists.size()>0) {

                    if (MrgeHomeActivity.youTubeApiLists.get(0).getStream_status().equalsIgnoreCase("1")) {
                        MrgeHomeActivity.linStream.setBackgroundColor(Color.parseColor(colorActive));
                        MrgeHomeActivity.txt_streaming.setBackgroundColor(Color.parseColor(colorActive));
                        MrgeHomeActivity.img_stream.setBackgroundColor(Color.parseColor(colorActive));

                        MrgeHomeActivity.txt_streaming.setText("Live Streaming! Tap to view ");
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(500); //You can manage the blinking time with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        MrgeHomeActivity.img_stream.startAnimation(anim);

                        //-------------------------------------
                        MrgeHomeActivity.linChange.setBackgroundColor(Color.parseColor(colorActive));

                        MrgeHomeActivity.img_view.setBackgroundColor(Color.parseColor(colorActive));
                        MrgeHomeActivity.txt_change.setBackgroundColor(Color.parseColor(colorActive));

                        MrgeHomeActivity.linStream.setBackgroundColor(Color.parseColor(colorActive));

                        MrgeHomeActivity.txt_change.setText("Change View");
                        MrgeHomeActivity.img_view.startAnimation(anim);
                        //-----------------------------------
                    } else {
                        MrgeHomeActivity.linChange.setBackgroundColor(Color.parseColor("#686868"));
                        MrgeHomeActivity.img_view.setBackgroundColor(Color.parseColor("#686868"));
                        MrgeHomeActivity.txt_change.setBackgroundColor(Color.parseColor("#686868"));

                        MrgeHomeActivity.linStream.setBackgroundColor(Color.parseColor("#686868"));
                        MrgeHomeActivity.txt_change.setBackgroundColor(Color.parseColor("#686868"));
                        MrgeHomeActivity.img_view.setBackgroundColor(Color.parseColor("#686868"));
                        // linear_livestream.setBackgroundColor(Color.parseColor("#686868"));
                        MrgeHomeActivity.txt_change.setText("Change View");
                    }
                }else {
                    MrgeHomeActivity.linChange.setBackgroundColor(Color.parseColor("#686868"));
                    MrgeHomeActivity.img_view.setBackgroundColor(Color.parseColor("#686868"));
                    MrgeHomeActivity.txt_change.setBackgroundColor(Color.parseColor("#686868"));

                    MrgeHomeActivity.linStream.setBackgroundColor(Color.parseColor("#686868"));
                    MrgeHomeActivity.txt_change.setBackgroundColor(Color.parseColor("#686868"));
                    MrgeHomeActivity.img_view.setBackgroundColor(Color.parseColor("#686868"));
                    // linear_livestream.setBackgroundColor(Color.parseColor("#686868"));
                    MrgeHomeActivity.txt_change.setText("Change View");
                }
            }else { MrgeHomeActivity.linStream.setBackgroundColor(Color.parseColor("#686868"));
                 MrgeHomeActivity.txt_streaming.setBackgroundColor(Color.parseColor("#686868"));
                 MrgeHomeActivity.img_stream.setBackgroundColor(Color.parseColor("#686868"));
                 MrgeHomeActivity.txt_streaming.setText("Nothing Streaming Currently");
                 Animation anim = new AlphaAnimation(0.0f, 0.0f);
                 anim.setDuration(0); //You can manage the blinking time with this parameter
                 anim.setStartOffset(0);
                // anim.setRepeatMode(Animation.REVERSE);
                // anim.setRepeatCount(Animation.INFINITE);
                 MrgeHomeActivity.img_stream.startAnimation(anim);
                 MrgeHomeActivity.linChange.setBackgroundColor(Color.parseColor("#686868"));
                 MrgeHomeActivity.img_view.setBackgroundColor(Color.parseColor("#686868"));
                 MrgeHomeActivity.txt_change.setBackgroundColor(Color.parseColor("#686868"));
                 MrgeHomeActivity.img_view.startAnimation(anim);
                 MrgeHomeActivity.linear_livestream.setVisibility(View.VISIBLE);
                 MrgeHomeActivity.linear_layout.setVisibility(View.GONE);

             }

            //}
            if ( MrgeHomeActivity.zoom_status.equalsIgnoreCase("1")) {
//                            countDownzoom();


                MrgeHomeActivity.linzoom.setBackgroundColor(Color.parseColor(colorActive));
                MrgeHomeActivity.txt_zoom.setBackgroundColor(Color.parseColor(colorActive));
                MrgeHomeActivity.img_zoom.setBackgroundColor(Color.parseColor(colorActive));

                MrgeHomeActivity.txt_zoom.setText("Participate via Video");
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(500); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                MrgeHomeActivity.img_zoom.startAnimation(anim);
            } else {
                       /* linChange.setBackgroundColor(Color.parseColor("#686868"));
                        img_view.setBackgroundColor(Color.parseColor("#686868"));
                        txt_change.setBackgroundColor(Color.parseColor("#686868"));
*/
                MrgeHomeActivity.linzoom.setBackgroundColor(Color.parseColor("#686868"));
                MrgeHomeActivity.txt_zoom.setBackgroundColor(Color.parseColor("#686868"));
                MrgeHomeActivity.img_zoom.setBackgroundColor(Color.parseColor("#686868"));
                // linear_livestream.setBackgroundColor(Color.parseColor("#686868"));
                MrgeHomeActivity.txt_zoom.setText("Participate via Video");
            }
        }

        try {
            Log.d("Newseed", response.toString());
            newsfeedList = response.body().getNewsFeedList();
//            if (newsfeedList.size() > 0) {
//                post_layout.setVisibility(View.GONE);
//            } else {
//                post_layout.setVisibility(View.VISIBLE);
//            }
            if(response.body().getNewsFeedList().size()>0) {

                news_feed_mediaDB = response.body().getNewsFeedList().get(0).getNews_feed_media();
            }

        /*    List<news_feed_list> newsFeedLists = new ArrayList<>();
            newsFeedLists = response.body().getNews_feed_list();*/

            if (newsfeedList.size() > 0) {
                setAdapter(newsfeedList);
            }

            feedrecycler.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                    JzvdStd.goOnPlayOnPause();
                    try {
                        MyJzvdStd.releaseAllVideos();
                        JzvdStd.releaseAllVideos();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            saveFeedToDb(response);
            SubmitAnalytics(token, eventid, "", "", "newsfeed");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {
                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());
                } else {
                    // Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
                //Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveFeedToDb(Response<FetchFeed> response) {
        newsfeedList = response.body().getNewsFeedList();
        procializeDB.clearNewsFeedTable();
        procializeDB.clearBuzzMediaFeedTable();
        procializeDB.insertNEwsFeedInfo(response.body().getNewsFeedList(), db);
        newsfeedsDBList = response.body().getNewsFeedList();
        if (newsfeedsDBList != null) {
            for (int i = 0; i < newsfeedsDBList.size(); i++) {
                news_feed_mediaDB = new ArrayList<>();
                if (newsfeedsDBList.get(i).getNews_feed_media().size() > 0) {
                    for (int j = 0; j < newsfeedsDBList.get(i).getNews_feed_media().size(); j++) {
                        news_feed_media nb_media = new news_feed_media();

                        nb_media.setNews_feed_id(newsfeedsDBList.get(i).getNews_feed_media().get(j).getNews_feed_id());
                        nb_media.setMedia_type(newsfeedsDBList.get(i).getNews_feed_media().get(j).getMedia_type());
                        nb_media.setMediaFile(newsfeedsDBList.get(i).getNews_feed_media().get(j).getMediaFile());
                        nb_media.setThumb_image(newsfeedsDBList.get(i).getNews_feed_media().get(j).getThumb_image());
                        nb_media.setWidth(newsfeedsDBList.get(i).getNews_feed_media().get(j).getWidth());
                        nb_media.setHeight(newsfeedsDBList.get(i).getNews_feed_media().get(j).getHeight());
                        nb_media.setMedia_id(newsfeedsDBList.get(i).getNews_feed_media().get(j).getMedia_id());

                        news_feed_mediaDB.add(nb_media);
                    }
                    procializeDB.insertBuzzMediaInfo(news_feed_mediaDB, db);
                }
            }
        }
    }


    public void PostDelete(String eventid, String feedid, String token, final int position) {
//        showProgress();
        mAPIService.DeletePost(token, eventid, feedid).enqueue(new Callback<DeletePost>() {
            @Override
            public void onResponse(Call<DeletePost> call, Response<DeletePost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    DeletePostresponse(response, position);
                } else {
//                    dismissProgress();
                    //Toast.makeText(getContext(),response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeletePost> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getContext(), "Unable to Delete please try again later", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void DeletePostresponse(Response<DeletePost> response, int position) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

            feedAdapter.feedLists.remove(position);
            // remove the item from recycler view
            // feedAdapter.removeItem(viewHolder.getAdapterPosition());
            feedAdapter.notifyItemRemoved(position);
            //  feedAdapter.notifyDataSetChanged();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            Log.e("post", "fail");
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    public void ReportPostHide(String eventid, String feedid, String token, final int position) {
//        showProgress();
        mAPIService.ReportPostHide(token, eventid, feedid).enqueue(new Callback<ReportPostHide>() {
            @Override
            public void onResponse(Call<ReportPostHide> call, Response<ReportPostHide> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportPostHideresponse(response, position);
                } else {
//                    dismissProgress();
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    // Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportPostHide> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getContext(), "Unable to Delete please try again later", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportPostHideresponse(Response<ReportPostHide> response, int position) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

            feedAdapter.feedLists.remove(position);
//            feedAdapter.notifyItemRemoved(position);
            feedAdapter.notifyDataSetChanged();
            dialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    public void ReportPost(String eventid, String feedid, String token, String text) {
//        showProgress();
        mAPIService.ReportPost(token, eventid, feedid, text).enqueue(new Callback<ReportPost>() {
            @Override
            public void onResponse(Call<ReportPost> call, Response<ReportPost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportPostresponse(response);
                } else {
//                    dismissProgress();
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportPost> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getContext(), "Unable to Delete please try again later", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportPostresponse(Response<ReportPost> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

             myDialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
              myDialog.dismiss();
        }
    }

    public void ReportUser(String eventid, String target_attendee_id, String token, String text) {
//        showProgress();
        mAPIService.ReportUser(token, eventid, target_attendee_id, text).enqueue(new Callback<ReportUser>() {
            @Override
            public void onResponse(Call<ReportUser> call, Response<ReportUser> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportUserresponse(response);
                } else {
//                    dismissProgress();
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportUser> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getContext(), "Unable to Delete please try again later", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportUserresponse(Response<ReportUser> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

           myDialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
             myDialog.dismiss();
        }
    }

    public void ReportUserHide(String eventid, String target_attendee_id, String token) {
//        showProgress();
        mAPIService.ReportUserHide(token, eventid, target_attendee_id).enqueue(new Callback<ReportUserHide>() {
            @Override
            public void onResponse(Call<ReportUserHide> call, Response<ReportUserHide> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportUserHideresponse(response);
                } else {
//                    dismissProgress();

                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportUserHide> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getContext(), "Unable to Delete please try again later", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportUserHideresponse(Response<ReportUserHide> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            fetchFeed(token, eventid);


        } else {
            Log.e("post", "fail");
            dialog.dismiss();
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position, List<NewsFeedList> newsfeedsList) {
        if (user.get(SessionManager.ATTENDEE_STATUS).equalsIgnoreCase("1")) {
            if (viewHolder instanceof NewsFeedAdapterRecycler.MyViewHolder) {
                int pos = viewHolder.getAdapterPosition();

                String newsfeedId = newsfeedsList.get(pos).getNewsFeedId();
                String newsfeedId1 = newsfeedId;
                //Log.e("newsfeedId==>", newsfeedId);
                PostDelete(eventid, newsfeedId, token, position);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.navmenudownloads, menu);
        menu.removeGroup(R.id.downloads);
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(getActivity());
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());
                //Append timestamp to file name
                //fileName = timestamp + "_" + fileName;
                //External directory path to save file
                //folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";
                String folder = Environment.getExternalStorageDirectory().toString() + "/" + ApiConstant.folderName + "/";


                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                strPath = folder + fileName;
                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("ImageMultipleActivity", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Download completed- check folder " + ApiConstant.folderName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
          /*  Toast.makeText(getActivity(),
                    message, Toast.LENGTH_LONG).show();*/

            Uri contentUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".android.fileprovider", new File(strPath));
/*
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("video/*");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT,"");
            // share.putExtra(Intent.EXTRA_TEXT, ApiConstant.newsfeedwall + newsFeedMedia.get(swipableAdapterPosition).getMediaFile());
            share.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(share, "Share link!"));*/

            ContentValues content = new ContentValues(4);
            content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                    System.currentTimeMillis() / 1000);
            content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            content.put(MediaStore.Video.Media.DATA, strPath);

            ContentResolver resolver = getActivity().getContentResolver();
            Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("video/*");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared via MRGE app");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent, "Shared via MRGE app"));

        }
    }

    private class BackgroundReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // progressbarForSubmit.setVisibility(View.GONE);
            Log.d("service end", "service end");
            fetchFeed(token, eventid);
            tv_uploading.clearAnimation();
            tv_uploading.setVisibility(View.GONE);
            Toast.makeText(context, "Post uploaded successfully..!!", Toast.LENGTH_SHORT).show();
            // progressbarForSubmit.setProgress(Integer.parseInt(String.valueOf(progress)));
            /* mTvCapital.setText("Capital : " + capital);*/
        }
    }

    private void showratedialouge(final String from, final String id) {

        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();

        TextView title = myDialog.findViewById(R.id.title);
        if (from.equalsIgnoreCase("reportPost")) {
            title.setText("Report Post");
        } else if (from.equalsIgnoreCase("reportUser")) {
            title.setText("Report User");

        }


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);
        ratebtn.setText("Send");

        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);

        nametv.setText("To " + "Admin");

        ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

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
                    if (from.equalsIgnoreCase("reportPost")) {
                        ReportPost(eventid, id, token, msg);
                    } else if (from.equalsIgnoreCase("reportUser")) {
                        ReportUser(eventid, id, token, msg);
                    }
                } else {
                    Toast.makeText(getActivity(), "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}