package com.procialize.mrgeApp20.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExhibitorNotiFragment extends Fragment {

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
    ConnectionDetector cd;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    Adapter adapter;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_exhibitor_noti, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabs = (TabLayout) view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setTabMode(TabLayout.GRAVITY_CENTER);
        setupTabLayout();
        viewPager.setCurrentItem(0);
        tabs.getTabAt(0).getCustomView().setBackgroundColor(Color.parseColor("#5b5b5b"));
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
                        tabs.getTabAt(i).getCustomView().setBackgroundColor(Color.parseColor("#5b5b5b"));
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
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new MessageFragment(), "Message");
        adapter.addFragment(new MeetingScheduleFragment(), "Meeting Schedule");
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

        customTab1 = (TextView) LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_tab_layout, null);
        customTab2 = (TextView) LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_tab_layout, null);

        customTab1.setText("Message");
        tabs.getTabAt(0).setCustomView(customTab1);
        customTab2.setText("Meeting Schedule");
        tabs.getTabAt(1).setCustomView(customTab2);


    }

}
