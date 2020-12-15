package com.procialize.frontier.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.Gallery.Image.Activity.GalleryFirstLevelActivity;
import com.procialize.frontier.Gallery.Image.Adapter.GalleryAdapter;
import com.procialize.frontier.GetterSetter.FirstLevelFilter;
import com.procialize.frontier.GetterSetter.FolderList;
import com.procialize.frontier.GetterSetter.GalleryList;
import com.procialize.frontier.GetterSetter.GalleryListFetch;
import com.procialize.frontier.InnerDrawerActivity.SwappingGalleryActivity;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.frontier.Session.ImagePathConstants.KEY_FOLDER_IMAGE_PATH;
import static com.procialize.frontier.util.CommonFunction.crashlytics;
import static com.procialize.frontier.util.CommonFunction.firbaseAnalytics;

public class FrontierGallery extends AppCompatActivity implements GalleryAdapter.GalleryAdapterListner{

    private static List<GalleryList> galleryLists = new ArrayList<>();
    private static List<FolderList> folderLists = new ArrayList<>();
    SwipeRefreshLayout galleryRvrefresh;
    RecyclerView galleryRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo",picPath="";
    String eventid, colorActive;
    ImageView headerlogoIv;
    RelativeLayout linear;
    TextView msg_txt, pullrefresh;
    View rootView;
    private APIService mAPIService;
    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontier_gallery);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        galleryRv = findViewById(R.id.galleryRv);
        galleryRvrefresh = findViewById(R.id.galleryRvrefresh);
        progressBar = findViewById(R.id.progressBar);
        linear = findViewById(R.id.linear);
        msg_txt = findViewById(R.id.msg_txt);
        pullrefresh = findViewById(R.id.pullrefresh);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

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


//        TextView header = findViewById(R.id.title);
//        header.setTextColor(Color.parseColor(colorActive));
//        pullrefresh.setTextColor(Color.parseColor(colorActive));
//        msg_txt.setTextColor(Color.parseColor(colorActive));


        mAPIService = ApiUtils.getAPIService();
        cd = new ConnectionDetector(FrontierGallery.this);

        SessionManager sessionManager = new SessionManager(FrontierGallery.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Gallery", token);
        firbaseAnalytics(FrontierGallery.this, "Gallery", token);
        // use a linear layout manager
        int columns = 2;
        galleryRv.setLayoutManager(new GridLayoutManager(FrontierGallery.this, columns));

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(FrontierGallery.this, resId);
        //galleryRv.setLayoutAnimation(animation);


        if(cd.isConnectingToInternet()) {
            fetchGallery(token, eventid);
        }else{
            if (galleryRvrefresh.isRefreshing()) {
                galleryRvrefresh.setRefreshing(false);
            }

        }

        galleryRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(cd.isConnectingToInternet()) {
                    fetchGallery(token, eventid);
                }else{
                    if (galleryRvrefresh.isRefreshing()) {
                        galleryRvrefresh.setRefreshing(false);
                    }

                }
            }
        });

    }

    public void fetchGallery(String token, String eventid) {
        showProgress();
        mAPIService.GalleryListFetch(token, eventid).enqueue(new Callback<GalleryListFetch>() {
            @Override
            public void onResponse(Call<GalleryListFetch> call, Response<GalleryListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (galleryRvrefresh.isRefreshing()) {
                        galleryRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {
                    if (galleryRvrefresh.isRefreshing()) {
                        galleryRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(FrontierGallery.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GalleryListFetch> call, Throwable t) {
                Toast.makeText(FrontierGallery.this, "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (galleryRvrefresh.isRefreshing()) {
                    galleryRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<GalleryListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {

            if(galleryLists.size()>0){
                galleryLists.clear();
            }
            if(folderLists.size()>0){
                folderLists.clear();
            }
            galleryLists = response.body().getGalleryList();
            folderLists = response.body().getFolderList();
            String folderImageUrlPath = response.body().getFolder_image_url_path();

            SharedPreferences prefs = FrontierGallery.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(KEY_FOLDER_IMAGE_PATH,folderImageUrlPath);
            edit.commit();
            List<FirstLevelFilter> filtergallerylists = new ArrayList<>();
            picPath = prefs.getString(KEY_FOLDER_IMAGE_PATH,"");
            if (response.body().getGalleryList().size() != 0 || response.body().getFolderList().size() != 0) {

                if (response.body().getFolderList().size() != 0) {
                    for (int i = 0; i < response.body().getFolderList().size(); i++) {
                        if (response.body().getFolderList().get(i).getFolderName() != null || !response.body().getFolderList().get(i).getFolderName().equalsIgnoreCase("null")) {
                            FirstLevelFilter firstLevelFilter = new FirstLevelFilter();

                            if (!response.body().getFolderList().get(i).getFolderName().contains("/")) {
                                firstLevelFilter.setFolderName(response.body().getFolderList().get(i).getFolderName());
                                firstLevelFilter.setTitle(response.body().getFolderList().get(i).getFolderName());
                                firstLevelFilter.setFolder_id(response.body().getFolderList().get(i).getFolder_id());
                                firstLevelFilter.setFileName(/*ApiConstant.galleryimage*/picPath + response.body().getFolderList().get(i).getFolderImage());

                                filtergallerylists.add(firstLevelFilter);
                            }
                        }
                    }
                }

                for (int i = 0; i < response.body().getGalleryList().size(); i++) {
                    if (response.body().getGalleryList().get(i).getFolderName() == null || response.body().getGalleryList().get(i).getFolderName().equalsIgnoreCase("null")) {
                        FirstLevelFilter firstLevelFilter = new FirstLevelFilter();

                        firstLevelFilter.setTitle(response.body().getGalleryList().get(i).getTitle());
                        firstLevelFilter.setFileName(/*ApiConstant.galleryimage*/picPath + response.body().getGalleryList().get(i).getFileName());
                        firstLevelFilter.setFolderName(response.body().getGalleryList().get(i).getFolderName());
                        firstLevelFilter.setFolder_id(response.body().getGalleryList().get(i).getFolder_id());

                        filtergallerylists.add(firstLevelFilter);
                    }
                }

                if (!filtergallerylists.isEmpty()) {
                    GalleryAdapter galleryAdapter = new GalleryAdapter(FrontierGallery.this, filtergallerylists, this,galleryLists);
                    galleryAdapter.notifyDataSetChanged();
                    galleryRv.setAdapter(galleryAdapter);
                    galleryRv.scheduleLayoutAnimation();
                } else {
                    galleryRv.setVisibility(View.GONE);
                    msg_txt.setVisibility(View.VISIBLE);
                }
            } else {
                galleryRv.setVisibility(View.GONE);
                msg_txt.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(FrontierGallery.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
    public void onContactSelected(FirstLevelFilter filtergallerylists, List<FirstLevelFilter> firstLevelFilterList) {

        if (filtergallerylists.getFolderName() == null || filtergallerylists.getFolderName().equalsIgnoreCase("null")) {

            List<FirstLevelFilter> newfilterlist = new ArrayList<>();

            for (int i = 0; i < firstLevelFilterList.size(); i++) {
                if (firstLevelFilterList.get(i).getFolderName() == null) {
                    newfilterlist.add(firstLevelFilterList.get(i));
                } else if (firstLevelFilterList.get(i).getFolderName().equalsIgnoreCase("null")) {
                    newfilterlist.add(firstLevelFilterList.get(i));
                }
            }
            Intent view = new Intent(FrontierGallery.this, SwappingGalleryActivity.class);
            view.putExtra("foldername", filtergallerylists.getFolderName());

            view.putExtra("url", filtergallerylists.getFileName());
            view.putExtra("gallerylist", (Serializable) newfilterlist);
            startActivity(view);
        } else {

            String foldername = filtergallerylists.getFolderName();

            Intent intent = new Intent(FrontierGallery.this, GalleryFirstLevelActivity.class);
            intent.putExtra("foldername", foldername);
            intent.putExtra("gallerylist", (Serializable) galleryLists);
            intent.putExtra("folderlist", (Serializable) folderLists);

            startActivity(intent);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }
}