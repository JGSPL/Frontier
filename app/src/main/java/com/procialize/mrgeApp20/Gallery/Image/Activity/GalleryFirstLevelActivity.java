package com.procialize.mrgeApp20.Gallery.Image.Activity;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.procialize.mrgeApp20.Gallery.Image.Adapter.GalleryFirstLevelAdapter;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.GetterSetter.FirstLevelFilter;
import com.procialize.mrgeApp20.GetterSetter.FolderList;
import com.procialize.mrgeApp20.GetterSetter.GalleryList;
import com.procialize.mrgeApp20.InnerDrawerActivity.SwappingGalleryActivity;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.util.GetUserActivityReport;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;

import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_FOLDER_IMAGE_PATH;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;

public class GalleryFirstLevelActivity extends AppCompatActivity implements GalleryFirstLevelAdapter.GalleryFirstLevelAdapterListener {


    public static String foldername;
    RecyclerView galleryRv;
    TextView tvname, msg_txt;
    List<GalleryList> galleryLists;
    List<FolderList> folderLists;
    List<FirstLevelFilter> filtergallerylists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive,picPath;
    ImageView headerlogoIv;
    RelativeLayout linear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_first_level);

        galleryLists = new ArrayList<>();
        folderLists = new ArrayList<>();
        filtergallerylists = new ArrayList<>();

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
        linear = findViewById(R.id.linear);
        Util.logomethod(this, headerlogoIv);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        picPath = prefs.getString(KEY_FOLDER_IMAGE_PATH, "");


        foldername = getIntent().getExtras().getString("foldername");
        galleryLists = (List<GalleryList>) getIntent().getExtras().getSerializable("gallerylist");
        folderLists = (List<FolderList>) getIntent().getExtras().getSerializable("folderlist");

        /*Util.logomethodwithText( this, true,foldername, MrgeHomeActivity.txtMainHeader,MrgeHomeActivity.headerlogoIv);*/
        TextView title = findViewById(R.id.title);
        title.setText(foldername);
        try {
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ApiConstant.folderName+"/"+ "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        galleryRv = findViewById(R.id.galleryRv);
        tvname = findViewById(R.id.tvname);
        msg_txt = findViewById(R.id.msg_txt);
        tvname.setTextColor(Color.parseColor(colorActive));

        // use a linear layout manager
        int columns = 2;
        galleryRv.setLayoutManager(new GridLayoutManager(this, columns));

        if (foldername.contains("/")) {
            String[] parts = foldername.split("/");
            String part1 = parts[0];
            String part2 = parts[1];
            tvname.setText(part2);
        } else {
            tvname.setText(foldername);
        }

        if (folderLists.size() != 0 && galleryLists.size() != 0) {
            if (folderLists.size() != 0) {
                for (int i = 0; i < folderLists.size(); i++) {
                    if (folderLists.get(i).getFolderName() != null) {
                        if (folderLists.get(i).getFolderName().contains(foldername + "/")) {
                            FirstLevelFilter firstLevelFilter = new FirstLevelFilter();

                            firstLevelFilter.setTitle(folderLists.get(i).getFolderName());
                            firstLevelFilter.setFolderName(folderLists.get(i).getFolderName());
                            firstLevelFilter.setFileName(/*ApiConstant.folderimage */picPath+ folderLists.get(i).getFolderImage());
                            filtergallerylists.add(firstLevelFilter);
                        }
                    }
                }
            }

            if (galleryLists.size() != 0) {
                for (int i = 0; i < galleryLists.size(); i++) {
                    if (galleryLists.get(i).getFolderName() != null) {
                        if (galleryLists.get(i).getFolderName().equals(foldername)) {
                            FirstLevelFilter firstLevelFilter = new FirstLevelFilter();

                            firstLevelFilter.setTitle(galleryLists.get(i).getTitle());
                            firstLevelFilter.setFolderName(galleryLists.get(i).getFolderName());
                            firstLevelFilter.setFolder_id(galleryLists.get(i).getFolder_id());
                            firstLevelFilter.setId(galleryLists.get(i).getId());
                            firstLevelFilter.setFileName(/*ApiConstant.galleryimage*/picPath + galleryLists.get(i).getFileName());
                            filtergallerylists.add(firstLevelFilter);
                        }
                    }
                }
            }
            GalleryFirstLevelAdapter galleryAdapter = new GalleryFirstLevelAdapter(this, filtergallerylists, this);
            galleryAdapter.notifyDataSetChanged();
            galleryRv.setAdapter(galleryAdapter);
        } else {
            msg_txt.setVisibility(View.VISIBLE);
            galleryRv.setVisibility(View.GONE);
        }

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this,tv_notification,ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------
        SessionManager sessionManager = new SessionManager(GalleryFirstLevelActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        final String token = user.get(SessionManager.KEY_TOKEN);
        //--------------------------------------------------------------------------------------
        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this,token,
                eventid,
                ApiConstant.pageVisited,
                "18",
                "");
        getUserActivityReport.userActivityReport();
        //--------------------------------------------------------------------------------------
    }

    @Override
    public void onContactSelected(FirstLevelFilter filtergallerylists, List<FirstLevelFilter> firstLevelFilterList) {


        if (filtergallerylists.getFolderName() == null || filtergallerylists.getFolderName().equalsIgnoreCase("null")
                || filtergallerylists.getFolderName().equalsIgnoreCase(foldername)) {

            List<FirstLevelFilter> newfilterlist = new ArrayList<>();

            for (int i = 0; i < firstLevelFilterList.size(); i++) {
                if (firstLevelFilterList.get(i).getFolderName() != null) {
                    if (firstLevelFilterList.get(i).getFolderName().equalsIgnoreCase(foldername)) {
                        newfilterlist.add(firstLevelFilterList.get(i));
                    }
                }
            }


            if (filtergallerylists.getFolderName().equalsIgnoreCase(filtergallerylists.getTitle())) {
                String foldername = filtergallerylists.getFolderName();

                Intent intent = new Intent(getApplicationContext(), GalleryFirstLevelActivity.class);
                intent.putExtra("foldername", foldername);
                intent.putExtra("gallerylist", (Serializable) galleryLists);
                intent.putExtra("folderlist", (Serializable) folderLists);

                startActivity(intent);
            } else {
                Intent view = new Intent(getApplicationContext(), SwappingGalleryActivity.class);
                view.putExtra("url", filtergallerylists.getFileName());
                view.putExtra("foldername", filtergallerylists.getFolderName());

                view.putExtra("gallerylist", (Serializable) newfilterlist);
                startActivity(view);
            }
        } else {

            String foldername = filtergallerylists.getFolderName();

            if (foldername.contains("/")) {
                Intent intent = new Intent(getApplicationContext(), GalleryFirstLevelActivity.class);
                intent.putExtra("foldername", foldername);
                intent.putExtra("gallerylist", (Serializable) galleryLists);
                intent.putExtra("folderlist", (Serializable) folderLists);
                startActivity(intent);
            } else {
                List<FirstLevelFilter> newfilterlist = new ArrayList<>();

                for (int i = 0; i < firstLevelFilterList.size(); i++) {
                    if (firstLevelFilterList.get(i).getFolderName() != null) {
                        if (firstLevelFilterList.get(i).getFolderName().equalsIgnoreCase(foldername)) {
                            newfilterlist.add(firstLevelFilterList.get(i));
                        }
                    }
                }
                Intent view = new Intent(getApplicationContext(), SwappingGalleryActivity.class);
                view.putExtra("url", filtergallerylists.getFileName());
                view.putExtra("gallerylist", (Serializable) newfilterlist);
                startActivity(view);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();

    }
}
