package com.procialize.mrgeApp20.InnerDrawerActivity;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.mrgeApp20.Adapter.GalleryAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.GetterSetter.FirstLevelFilter;
import com.procialize.mrgeApp20.GetterSetter.FolderList;
import com.procialize.mrgeApp20.GetterSetter.GalleryList;
import com.procialize.mrgeApp20.GetterSetter.GalleryListFetch;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class GalleryActivity extends AppCompatActivity implements GalleryAdapter.GalleryAdapterListner {

    private static List<GalleryList> galleryLists;
    private static List<FolderList> folderLists;
    SwipeRefreshLayout galleryRvrefresh;
    RecyclerView galleryRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    private APIService mAPIService;
    RelativeLayout linear;
    TextView msg_txt, pullrefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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

        galleryRv = findViewById(R.id.galleryRv);
        galleryRvrefresh = findViewById(R.id.galleryRvrefresh);
        progressBar = findViewById(R.id.progressBar);
        linear = findViewById(R.id.linear);
        msg_txt = findViewById(R.id.msg_txt);
        pullrefresh = findViewById(R.id.pullrefresh);

        TextView header = findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));
        pullrefresh.setTextColor(Color.parseColor(colorActive));
        msg_txt.setTextColor(Color.parseColor(colorActive));


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Gallery",token);
        firbaseAnalytics(this, "Gallery", token);
        // use a linear layout manager
        int columns = 2;
        galleryRv.setLayoutManager(new GridLayoutManager(this, columns));

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        //galleryRv.setLayoutAnimation(animation);

        try {

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        fetchGallery(token, eventid);

        galleryRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchGallery(token, eventid);
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
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GalleryListFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

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

            galleryLists = response.body().getGalleryList();
            folderLists = response.body().getFolderList();


            List<FirstLevelFilter> filtergallerylists = new ArrayList<>();

            if (response.body().getGalleryList().size() != 0 || response.body().getFolderList().size() != 0) {

                if (response.body().getFolderList().size() != 0) {
                    for (int i = 0; i < response.body().getFolderList().size(); i++) {
                        if (response.body().getFolderList().get(i).getFolderName() != null || !response.body().getFolderList().get(i).getFolderName().equalsIgnoreCase("null")) {
                            FirstLevelFilter firstLevelFilter = new FirstLevelFilter();

                            if (!response.body().getFolderList().get(i).getFolderName().contains("/")) {
                                firstLevelFilter.setFolderName(response.body().getFolderList().get(i).getFolderName());
                                firstLevelFilter.setTitle(response.body().getFolderList().get(i).getFolderName());
                                firstLevelFilter.setFileName(ApiConstant.galleryimage + response.body().getFolderList().get(i).getFolderImage());

                                filtergallerylists.add(firstLevelFilter);
                            }
                        }
                    }
                }

                for (int i = 0; i < response.body().getGalleryList().size(); i++) {
                    if (response.body().getGalleryList().get(i).getFolderName() == null || response.body().getGalleryList().get(i).getFolderName().equalsIgnoreCase("null")) {
                        FirstLevelFilter firstLevelFilter = new FirstLevelFilter();

                        firstLevelFilter.setTitle(response.body().getGalleryList().get(i).getTitle());
                        firstLevelFilter.setFileName(ApiConstant.galleryimage + response.body().getGalleryList().get(i).getFileName());
                        firstLevelFilter.setFolderName(response.body().getGalleryList().get(i).getFolderName());

                        filtergallerylists.add(firstLevelFilter);
                    }
                }


                if (!filtergallerylists.isEmpty()) {
                    GalleryAdapter galleryAdapter = new GalleryAdapter(this, filtergallerylists, this);
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
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
//        } else {
//            setContentView(R.layout.activity_empty_view);
//            ImageView imageView = findViewById(R.id.back);
//            TextView text_empty = findViewById(R.id.text_empty);
//            text_empty.setText("Images not available");
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//        }
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
    protected void onResume() {
        super.onResume();
        //   overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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
            Intent view = new Intent(getApplicationContext(), SwappingGalleryActivity.class);
            view.putExtra("url", filtergallerylists.getFileName());
            view.putExtra("gallerylist", (Serializable) newfilterlist);
            startActivity(view);
        } else {

            String foldername = filtergallerylists.getFolderName();

            Intent intent = new Intent(getApplicationContext(), GalleryFirstLevelActivity.class);
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
