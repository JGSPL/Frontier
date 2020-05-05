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
/*

        if (tabLayout.getTabAt(0) != null) {
            if (tabLayout.getTabAt(0).getText().equals("News Feed")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(0).getText().equals("Agenda")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[1]);
            }
            else if (tabLayout.getTabAt(0).getText().equals("Attendee")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[2]);
            }
            else if (tabLayout.getTabAt(0).getText().equals("Speaker")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[3]);
            }
            else if (tabLayout.getTabAt(0).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[4]);
            }
            else if (tabLayout.getTabAt(0).getText().equals("General Info")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[5]);
            }
        }


        if (tabLayout.getTabAt(1) != null) {
            if (tabLayout.getTabAt(1).getText().equals("News Feed")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(1).getText().equals("Agenda")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(1).getText().equals("Attendee")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(1).getText().equals("Speaker")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(1).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[4]);
            } else if (tabLayout.getTabAt(1).getText().equals("General Info")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[5]);
            }
        }


        if (tabLayout.getTabAt(2) != null) {
            if (tabLayout.getTabAt(2).getText().equals("News Feed")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(2).getText().equals("Agenda")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(2).getText().equals("Attendee")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(2).getText().equals("Speaker")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(2).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[4]);
            } else if (tabLayout.getTabAt(2).getText().equals("General Info")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[5]);
            }
        }


        if (tabLayout.getTabAt(3) != null) {
            if (tabLayout.getTabAt(3).getText().equals("News Feed")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(3).getText().equals("Agenda")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(3).getText().equals("Attendee")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(3).getText().equals("Speaker")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(3).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[4]);
            } else if (tabLayout.getTabAt(3).getText().equals("General Info")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[5]);
            }
        }
        if (tabLayout.getTabAt(4) != null) {
            if (tabLayout.getTabAt(4).getText().equals("News Feed")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(4).getText().equals("Agenda")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(4).getText().equals("Attendee")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(4).getText().equals("Speaker")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(4).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[4]);
            } else if (tabLayout.getTabAt(4).getText().equals("General Info")) {
                tabLayout.getTabAt(4).setIcon(tabIcons[5]);
            }
        }
        if (tabLayout.getTabAt(5) != null) {
            if (tabLayout.getTabAt(5).getText().equals("News Feed")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(5).getText().equals("Agenda")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(5).getText().equals("Attendee")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(5).getText().equals("Speaker")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(5).getText().equals("Exhibitors")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[4]);
            } else if (tabLayout.getTabAt(5).getText().equals("General Info")) {
                tabLayout.getTabAt(5).setIcon(tabIcons[5]);
            }
        }
*/


        /*for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv=(TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
            ImageView imageView= (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
            if (i==0)
            {
                tv.setText("News Feed");
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_newsfeed) );
            }else if (i==1)
            {
                tv.setText("Agenda");
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_newsfeed) );
            }else if (i==2)
            {
                tv.setText("Attendees");

            }else if (i==3)
            {
                tv.setText("Speakers");
            }
            tabLayout.getTabAt(i).setCustomView(tv);

        }*/
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
