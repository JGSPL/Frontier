package com.procialize.frontier.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.Gallery.Video.Activity.VideoFirstLevelActivity;
import com.procialize.frontier.Gallery.Video.Adapter.VideoAdapter;
import com.procialize.frontier.GetterSetter.FirstLevelFilter;
import com.procialize.frontier.GetterSetter.VideoFetchListFetch;
import com.procialize.frontier.GetterSetter.VideoFolderList;
import com.procialize.frontier.GetterSetter.VideoList;
import com.procialize.frontier.InnerDrawerActivity.ExoVideoActivity;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;
import com.procialize.frontier.util.GetUserActivityReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.frontier.Session.ImagePathConstants.KEY_FOLDER_VIDEO_PATH;
import static com.procialize.frontier.util.CommonFunction.crashlytics;
import static com.procialize.frontier.util.CommonFunction.firbaseAnalytics;

public class VideoGallery extends AppCompatActivity implements VideoAdapter.VideoAdapterListner {

    private static List<VideoList> videoLists = new ArrayList<>();
    private static List<VideoFolderList> folderLists = new ArrayList<>();
    SwipeRefreshLayout videoRvrefresh;
    RecyclerView videoRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    private APIService mAPIService;
    RelativeLayout linear;
    TextView msg_txt, pullrefresh;
    View rootView;
    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        SharedPreferences prefs = VideoGallery.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        cd = new ConnectionDetector(VideoGallery.this);

        videoRv = findViewById(R.id.videoRv);
        progressBar = findViewById(R.id.progressBar);
        videoRvrefresh = findViewById(R.id.videoRvrefresh);
        TextView header = findViewById(R.id.title);
        linear = findViewById(R.id.linear);
        msg_txt = findViewById(R.id.msg_txt);
        pullrefresh = findViewById(R.id.pullrefresh);
//        header.setTextColor(Color.parseColor(colorActive));
//        msg_txt.setTextColor(Color.parseColor(colorActive));
//        pullrefresh.setTextColor(Color.parseColor(colorActive));
        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(VideoGallery.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);

        crashlytics("Video", token);
        firbaseAnalytics(VideoGallery.this, "Video", token);
        // use a linear layout manager
        int columns = 2;
        videoRv.setLayoutManager(new GridLayoutManager(VideoGallery.this, columns));

//        int resId = R.anim.layout_animation_slide_right;
//        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(VideoGallery.this, resId);
        // videoRv.setLayoutAnimation(animation);

        if (cd.isConnectingToInternet()) {
            fetchVideo(token, eventid);
        } else {
            if (videoRvrefresh.isRefreshing()) {
                videoRvrefresh.setRefreshing(false);
            }

        }

        videoRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {

                    if (cd.isConnectingToInternet()) {
                        fetchVideo(token, eventid);
                    } else {
                        if (videoRvrefresh.isRefreshing()) {
                            videoRvrefresh.setRefreshing(false);
                        }

                    }
                }
            }
        });

        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(VideoGallery.this, token,
                eventid,
                ApiConstant.pageVisited,
                "21",
                "");
        getUserActivityReport.userActivityReport();
        //--------------------------------------------------------------------------------------
    }


    public void fetchVideo(String token, String eventid) {
        showProgress();
        mAPIService.VideoFetchListFetch(token, eventid).enqueue(new Callback<VideoFetchListFetch>() {
            @Override
            public void onResponse(Call<VideoFetchListFetch> call, Response<VideoFetchListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (videoRvrefresh.isRefreshing()) {
                        videoRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();

                    showResponse(response);
                } else {
                    if (videoRvrefresh.isRefreshing()) {
                        videoRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(VideoGallery.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoFetchListFetch> call, Throwable t) {
                Toast.makeText(VideoGallery.this, "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (videoRvrefresh.isRefreshing()) {
                    videoRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<VideoFetchListFetch> response) {


        // specify an adapter (see also next example)
        if (response.body().getVideoList().size() != 0) {
            videoRv.setVisibility(View.VISIBLE);
            msg_txt.setVisibility(View.GONE);
            if (videoLists.size() > 0) {
                videoLists.clear();
            }
            if (folderLists.size() > 0) {
                folderLists.clear();
            }
            videoLists = response.body().getVideoList();
            folderLists = response.body().getVideoFolderList();
            String folderVideoUrlPath = response.body().getFolder_video_url_path();

            SharedPreferences prefs = VideoGallery.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(KEY_FOLDER_VIDEO_PATH, folderVideoUrlPath);
            edit.commit();

            List<FirstLevelFilter> filtergallerylists = new ArrayList<>();
            Log.d("Video Response", String.valueOf(response.body().getVideoFolderList()));


            if (response.body().getVideoList().size() != 0 || response.body().getVideoFolderList().size() != 0) {


                String picPath = prefs.getString(KEY_FOLDER_VIDEO_PATH, "");

                if (response.body().getVideoFolderList().size() != 0) {
                    for (int i = 0; i < response.body().getVideoFolderList().size(); i++) {
                        if (response.body().getVideoFolderList().get(i).getFolderName() != null || !response.body().getVideoFolderList().get(i).getFolderName().equalsIgnoreCase("null")) {
                            FirstLevelFilter firstLevelFilter = new FirstLevelFilter();

                            if (!response.body().getVideoFolderList().get(i).getFolderName().contains("/")) {
                                firstLevelFilter.setFolderName(response.body().getVideoFolderList().get(i).getFolderName());
                                firstLevelFilter.setTitle(response.body().getVideoFolderList().get(i).getFolderName());
                                firstLevelFilter.setFolder_id(response.body().getVideoFolderList().get(i).getFolder_id());
                                firstLevelFilter.setFileName(/*ApiConstant.folderimage*/ picPath + response.body().getVideoFolderList().get(i).getFolderImage());

                                filtergallerylists.add(firstLevelFilter);
                            }
                        }
                    }
                }

                for (int i = 0; i < response.body().getVideoList().size(); i++) {
                    if (response.body().getVideoList().get(i).getFolderName() == null ||
                            response.body().getVideoList().get(i).getFolderName().equalsIgnoreCase("null")) {
                        FirstLevelFilter firstLevelFilter = new FirstLevelFilter();

                        firstLevelFilter.setTitle(response.body().getVideoList().get(i).getTitle());
                        firstLevelFilter.setFileName(response.body().getVideoList().get(i).getVideoUrl());
                        firstLevelFilter.setFolder_id(response.body().getVideoList().get(i).getFolder_id());
                        firstLevelFilter.setFolderName(response.body().getVideoList().get(i).getFolderName());

                        filtergallerylists.add(firstLevelFilter);
                    }
                }
                VideoAdapter videoAdapter = new VideoAdapter(VideoGallery.this, filtergallerylists, this, videoLists);
                videoAdapter.notifyDataSetChanged();
                videoRv.setAdapter(videoAdapter);

            } else {
                Toast.makeText(VideoGallery.this, "No Video Available", Toast.LENGTH_SHORT).show();
            }
        } else {
            videoRv.setVisibility(View.GONE);
            msg_txt.setVisibility(View.VISIBLE);

//            setContentView(R.layout.activity_empty_view);
//            ImageView imageView = findViewById(R.id.back);
//            TextView text_empty = findViewById(R.id.text_empty);
//            text_empty.setText("Video's not available");
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
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
    public void onContactSelected(FirstLevelFilter firstLevelFilter) {


        if (firstLevelFilter.getFolderName() == null || firstLevelFilter.getFolderName().equalsIgnoreCase("null")) {
            Boolean flagUrl = firstLevelFilter.getFileName()
                    .contains("youtu");

            // Check for Internet Connection

            if (flagUrl) {

                String videoUrl = firstLevelFilter.getFileName();

//                    String videoId = videoUrl.substring(videoUrl
//                            .lastIndexOf("=") + 1);

//                    String url =videoUrl.substring(videoUrl
//                            .lastIndexOf("&index") + 0);

                String[] parts = videoUrl.split("=");
                String part1 = parts[0]; // 004
                String videoId = parts[0]; // 034556


                String[] parts1 = videoId.split("&index");

                String url = parts1[0];


                String[] parts2 = videoId.split("&list");


                String url2 = parts2[0];

                Log.e("video", firstLevelFilter.getFileName());

                try {
//                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( videoUrl));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(videoUrl));
                    startActivity(webIntent);
//                    try {
//                        startActivity(appIntent);
//                    } catch (ActivityNotFoundException ex) {
//                        startActivity(webIntent);
//                    }

                } catch (ActivityNotFoundException e) {

                    // youtube is not installed.Will be opened in other
                    // available apps
                    Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                    videoIntent.setDataAndType(
                            Uri.parse(firstLevelFilter.getFileName()),
                            "video/*");
                    startActivity(videoIntent);
                }

            } else {

                /*Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                videoIntent.setDataAndType(
                        Uri.parse(firstLevelFilter.getFileName()), "video*//*");
                startActivity(videoIntent);*/
                Intent edit = new Intent(VideoGallery.this, ExoVideoActivity.class);
                edit.putExtra("videoUrl", firstLevelFilter.getFileName());
                edit.putExtra("title", firstLevelFilter.getTitle());
                edit.putExtra("page", "videoMain");
                startActivity(edit);
            }

//            String CurrentString =firstLevelFilter.getFileName();
//            String[] separated = CurrentString.split("v=");
//            String videoId = separated[1];
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+videoId));
//            intent.putExtra("VIDEO_ID", videoId);
//            startActivity(intent);

//            Intent view = new Intent(getApplicationContext(), VideoViewActivity.class);
//            view.putExtra("url", firstLevelFilter.getFileName());
//
//            startActivity(view);
        } else {

            if (videoLists.size() == 0) {

                Toast.makeText(VideoGallery.this, "Folder is Empty", Toast.LENGTH_SHORT).show();

            } else {
                String foldername = firstLevelFilter.getFolderName();

                Intent intent = new Intent(VideoGallery.this, VideoFirstLevelActivity.class);
                intent.putExtra("foldername", foldername);
//                intent.putExtra("videolist", (Serializable) videoLists);
//                intent.putExtra("folderlist", (Serializable) folderLists);

                startActivity(intent);
            }


        }
    }

    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JzvdStd.releaseAllVideos();
    }

}
