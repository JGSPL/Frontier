package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.Fragments.ExhibitorNotiFragment;
import com.procialize.mrgeApp20.Fragments.NotificationFragment;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;


public class NotificationExhibitorActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    ImageView headerlogoIv;
    SharedPreferences prefs;
    HashMap<String, String> user;
    String MY_PREFS_NAME = "ProcializeInfo";
    SessionManager session;
    private APIService mAPIService;
    String accesstoken, eventid, colorActive, exhibitor_status, exhibitor_id;
    private ViewPager viewPager;
    TabLayout tabs;
    TextView customTab1, customTab2;
    Adapter adapter;
    ConnectionDetector cd;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_exhibitor);
        relativeLayout = findViewById(R.id.relative);
        try {
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            relativeLayout.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            relativeLayout.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dbHelper = new DBHelper(NotificationExhibitorActivity.this);

        procializeDB = new DBHelper(NotificationExhibitorActivity.this);
        db = procializeDB.getWritableDatabase();
        mAPIService = ApiUtils.getAPIService();
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        session = new SessionManager(this);
        user = session.getUserDetails();
        // token
        accesstoken = user.get(SessionManager.KEY_TOKEN);
        exhibitor_id = user.get(SessionManager.EXHIBITOR_ID);
        exhibitor_status = user.get(SessionManager.EXHIBITOR_STATUS);


        crashlytics("Exhibitor Notification", accesstoken);
        firbaseAnalytics(this, "Exhibitor Notification", accesstoken);;

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabs = (TabLayout) findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setTabMode(TabLayout.GRAVITY_CENTER);
        setupTabLayout();
        viewPager.setCurrentItem(0);
        tabs.getTabAt(0).getCustomView().setBackgroundColor(Color.parseColor(colorActive));
        customTab1.setTextColor(Color.parseColor("#ffffff"));
        customTab2.setTextColor(Color.parseColor("#000000"));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabs.getTabCount(); i++) {
                    if (i == position) {
                        tabs.getTabAt(i).getCustomView().setBackgroundColor(Color.parseColor(colorActive));
//                        tabs.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                    } else {
                        tabs.getTabAt(i).getCustomView().setBackgroundColor(Color.parseColor("#ffffff"));
//                        tabs.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    }
                }

                if (tabs.getSelectedTabPosition() == 0) {
                    customTab1.setTextColor(Color.parseColor("#ffffff"));
                    customTab2.setTextColor(Color.parseColor("#000000"));

                } else if (tabs.getSelectedTabPosition() == 1) {
                    customTab2.setTextColor(Color.parseColor("#ffffff"));
                    customTab1.setTextColor(Color.parseColor("#000000"));

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });



    }



    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new NotificationFragment(), "Event Notifications");
        adapter.addFragment(new ExhibitorNotiFragment(), "Exhibitor Notifications");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupTabLayout() {

        customTab1 = (TextView) LayoutInflater.from(NotificationExhibitorActivity.this)
                .inflate(R.layout.custom_tab_layout, null);
        customTab2 = (TextView) LayoutInflater.from(NotificationExhibitorActivity.this)
                .inflate(R.layout.custom_tab_layout, null);

        customTab1.setText("Event Notifications");
        tabs.getTabAt(0).setCustomView(customTab1);
        customTab2.setText("Exhibitor Notifications");
        tabs.getTabAt(1).setCustomView(customTab2);


    }
}
