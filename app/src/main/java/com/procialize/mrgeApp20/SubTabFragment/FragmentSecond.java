package com.procialize.mrgeApp20.SubTabFragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cuberto.flashytabbarandroid.TabFlashyAnimatorWithTitle;
import com.google.android.material.tabs.TabLayout;
import com.procialize.mrgeApp20.CustomTools.CustomViewPager;
import com.procialize.mrgeApp20.Fragments.AgendaFragment;
import com.procialize.mrgeApp20.Fragments.AttendeeFragment;
import com.procialize.mrgeApp20.Fragments.GeneralInfo;
import com.procialize.mrgeApp20.Fragments.WallFragment_POST;
import com.procialize.mrgeApp20.GetterSetter.EventMenuSettingList;
import com.procialize.mrgeApp20.GetterSetter.EventSettingList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSecond extends Fragment {

    View rootView;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, eventnamestr, logoImg, colorActive, eventback, accesstoken;
    HashMap<String, String> user;
    SessionManager session;
    List<EventSettingList> eventSettingLists;
    List<EventMenuSettingList> eventMenuSettingLists;
    CustomViewPager viewpagerSecond;
    private TabFlashyAnimatorWithTitle tabFlashyAnimator;
    private TabLayout tabLayout;

    public FragmentSecond() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_second, container, false);
        // Inflate the layout for this fragment
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "");
        eventnamestr = prefs.getString("eventnamestr", "");
        logoImg = prefs.getString("logoImg", "");
        colorActive = prefs.getString("colorActive", "");
        eventback = prefs.getString("eventback", "");
        // token
        session = new SessionManager(getActivity());
        user = session.getUserDetails();
        accesstoken = user.get(SessionManager.KEY_TOKEN);
        initView();
        return rootView;
    }

    public void initView() {
        viewpagerSecond = rootView.findViewById(R.id.viewpagerSecond);
        // activity_spring_indicator_indicator_default = findViewById(R.id.activity_spring_indicator_indicator_default);

        viewpagerSecond.setPagingEnabled(false);
        setupViewPager(viewpagerSecond);
        tabLayout = rootView.findViewById(R.id.tabsSecond);

        //   subtabLayout = findViewById(R.id.Subtabs);
        tabLayout.setupWithViewPager(viewpagerSecond);
        tabFlashyAnimator = new TabFlashyAnimatorWithTitle(tabLayout);
        setupTabIcons();
        tabLayout.setTabTextColors(Color.parseColor("#4D4D4D"), Color.parseColor(colorActive));


    }

    private void setupTabIcons() {
        tabFlashyAnimator.addTabItem("Event Info", R.drawable.ic_newsfeed);
        tabFlashyAnimator.addTabItem("Participant",
                R.drawable.ic_agenda);
        tabFlashyAnimator.addTabItem("Schedule",
                R.drawable.ic_attendee);
        tabFlashyAnimator.addTabItem("Emergency",
                R.drawable.general_info);
        tabFlashyAnimator.highLightTab(0);
        viewpagerSecond.addOnPageChangeListener(tabFlashyAnimator);
    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new WallFragment_POST(), "Event Info");
        adapter.addFragment(new AgendaFragment(), "Participant");
        adapter.addFragment(new AttendeeFragment(), "Schedule");
        adapter.addFragment(new GeneralInfo(), "Emergency");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
