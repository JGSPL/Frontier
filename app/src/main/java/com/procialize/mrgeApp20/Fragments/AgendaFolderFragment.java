package com.procialize.mrgeApp20.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;


import com.procialize.mrgeApp20.Adapter.SwipeAgendaImageAdapter;
import com.procialize.mrgeApp20.Adapter.SwipepagerAgendaImage;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.CustomTools.AutoScrollViewPager;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.Agenda;
import com.procialize.mrgeApp20.GetterSetter.AgendaFolder;
import com.procialize.mrgeApp20.GetterSetter.AgendaMediaList;
import com.procialize.mrgeApp20.GetterSetter.AgendaVacationList;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Utility;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class AgendaFolderFragment extends Fragment implements SwipeAgendaImageAdapter.SwipeAgendaImageAdapterListner {
    public static AutoScrollViewPager mViewPager;
    static LinearLayout tab_text;
    static String colorActive;
    String url = "";
    String email = "";
    ArrayList<String> eventDates = new ArrayList<String>();
    ArrayList<String> eventtempDates = new ArrayList<String>();
    Date d, d1;
    String updateAgendaUrl = "";
    ProgressDialog pDialog;
    SessionManager session;
    ApiConstant constant;
    String api_access_token;
    ConnectionDetector cd;
    SwipepagerAgendaImage swipepagerAdapter;
    int page;
    Handler handler;
    Runnable runnable;
    String dayFromDate;
    LinearLayout sliderDotspanel;
    String eventid;
    String MY_PREFS_NAME = "ProcializeInfo";
    String token;
    TextView txtname;
    WebView webView;
    SharedPreferences prefs;
    TextView text;
    // Declare Variable
    private FragmentTabHost agendaTabHost;
    // Database Helper
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private List<AgendaVacationList> agendaList = new ArrayList<AgendaVacationList>();
    private List<AgendaVacationList> agendaDBList;
    private List<AgendaMediaList> agendaFolderDBList;
    private DBHelper dbHelper;
    private APIService mAPIService;
    private List<AgendaMediaList> agendaFolderList = new ArrayList<AgendaMediaList>();
    private ArrayList<AgendaMediaList> agendaFolderImageList = new ArrayList<AgendaMediaList>();
    private String imgTemp = "0";
    private TabWidget tabWidget;
    private int dotscount;
    private ImageView[] dots;
    SwipeRefreshLayout swipe_refresh;
    LinearLayout linear;

    private static View createTabView(final Context context, final String text, final String text1) {
        View view = LayoutInflater.from(context).inflate(R.layout.agenda_tabs_bg, null);

        LinearLayout linTab = view.findViewById(R.id.linTab);

        TextView tv = view.findViewById(R.id.tabsText);
        TextView tv1 = view.findViewById(R.id.tabsecondText);
        tab_text = view.findViewById(R.id.tab_text);
        tv.setText(text + " " + text1);
//        tv1.setText(text1);
//        linTab.setSelected(true);
       /* if (linTab.isSelected() == true) {
            linTab.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            linTab.setBackgroundColor(Color.parseColor(colorActive));
        }*/


        linTab.setBackgroundResource(R.drawable.tab_selector);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.agenda_folder_fragment,
                container, false);
        // Toast.makeText(getActivity(), "Inside Agenda Fragment",
        // Toast.LENGTH_LONG).show();
        System.out.println("Inside Agenda Fragment");

        try {
            setNotification(getActivity(), MrgeHomeActivity.tv_notification,MrgeHomeActivity.ll_notification_count);
        }catch (Exception e)
        {e.printStackTrace();}

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Agenda Folder",user.get(SessionManager.KEY_TOKEN));
        firbaseAnalytics(getActivity(), "Agenda Folder", token);
        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getReadableDatabase();
//
//		// Connection Detector Reference
        cd = new ConnectionDetector(getActivity().getApplicationContext());


        constant = new ApiConstant();
        handler = new Handler();
        session = new SessionManager(getActivity().getApplicationContext());
//		api_access_token = session.getAccessToken();
//		eventId = session.getEventId();
        imgTemp = "0";

        // Create FragmentTabHost
        //agendaTabHost = new FragmentTabHost(getActivity());
        agendaTabHost = rootView.findViewById(android.R.id.tabhost);
        tabWidget = rootView.findViewById(android.R.id.tabs);
        agendaTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        txtname = rootView.findViewById(R.id.txtname);
        linear = rootView.findViewById(R.id.linear);
        text = rootView.findViewById(R.id.text);
        text.setTextColor(Color.parseColor(colorActive));
        txtname.setTextColor(Color.parseColor(colorActive));
//        webView = rootView.findViewById(R.id.webView);

        // // Locate fragment1.xml to create FragmentTabHost
        //agendaTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabcontent);
        updateAgendaUrl = ApiConstant.INDEPENDENT_AGENDA;
//
//        for(int i=0;i<agendaTabHost.getTabWidget().getChildCount();i++)
//            agendaTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFF")); //unselected
//
//        if(agendaTabHost.getCurrentTab()==0)
//            agendaTabHost.getTabWidget().getChildAt(agendaTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor(colorActive)); //1st tab selected
//        else
//            agendaTabHost.getTabWidget().getChildAt(agendaTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FFFFFF")); //2nd tab selected
//
//
//        agendaTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String tabId) {
//                for(int i=0;i<agendaTabHost.getTabWidget().getChildCount();i++)
//                    agendaTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFF")); //unselected
//
//                if(agendaTabHost.getCurrentTab()==0)
//                    agendaTabHost.getTabWidget().getChildAt(agendaTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor(colorActive)); //1st tab selected
//                else
//                    agendaTabHost.getTabWidget().getChildAt(agendaTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FFFFFF")); //2nd tab selected
//
//            }
//        });

//        setTabColor(agendaTabHost);

//        dbHelper = new DBHelper(getActivity());
//        agendaDBList = new ArrayList<AgendaVacationList>();
//        agendaFolderDBList = new ArrayList<AgendaMediaList>();
//        agendaDBList = dbHelper.getAgendaFolderList();
//        agendaFolderDBList = dbHelper.getAgendaMediaList();
//        initview();

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));

        }
        if (cd.isConnectingToInternet()) {

            // Initialize Update Agenda URL

            fetchAgenda(token, eventid);
        } else {
            dbHelper = new DBHelper(getActivity());
            agendaDBList = new ArrayList<AgendaVacationList>();
            agendaFolderDBList = new ArrayList<AgendaMediaList>();
            agendaDBList = dbHelper.getAgendaFolderList();
            agendaFolderDBList = dbHelper.getAgendaMediaList();


            initview();

        }

//        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                agendaTabHost.clearAllTabs();
//                fetchAgenda(token, eventid);
//            }
//        });
//

//		agendaTabHost.getTabWidget().setDividerDrawable(R.drawable.vertical_divider);

        return rootView;

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager = getActivity().findViewById(R.id.pagerimage);
        sliderDotspanel = getActivity().findViewById(R.id.SliderDots);

		/*mViewPager.startAutoScroll();
		mViewPager.setInterval(5000);
		mViewPager.setStopScrollWhenTouch(true);
		mViewPager.setCycle(true);*/

    }

	/*@Override
	public void onResume() {
		super.onResume();
		if (cd.isConnectingToInternet()) {

			// Initialize Update Agenda URL
			updateAgendaUrl = constant.WEBSERVICE_URL + constant.WEBSERVICE_FOLDER
					+ constant.INDEPENDENT_AGENDA;

			new GetUpdateAgenda().execute();
		}
	}*/

    // private void setupTab(final View view, final String tag) {
    // View tabview = createTabView(agendaTabHost.getContext(), tag);
    // TabSpec setContent = agendaTabHost.newTabSpec(tag).setIndicator(tabview)
    // .setContent(new TabContentFactory() {
    // public View createTabContent(String tag) {
    // return view;
    // }
    // });
    // agendaTabHost.addTab(setContent);
    // }

    // Detach FragmentTabHost
    @Override
    public void onDetach() {
        super.onDetach();

        try {

            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void initview() {


        String tempDate = "";
        if (eventtempDates.size() > 0) {
            eventtempDates.clear();
        }

        for (int i = 0; i < agendaDBList.size(); i++) {

            String eventdate = "";
            eventdate = agendaDBList.get(i).getFolder_name();


            if (!tempDate.equalsIgnoreCase(eventdate)) {
                tempDate = eventdate;

                eventDates.add(tempDate);
            }
        }

        try {
            txtname.setText(agendaDBList.get(0).getSession_name());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        webView.loadData(agendaDBList.get(0).getSession_description(), "text/html", null);

		/*int cnt= 0;
		for(int i=0;i<eventtempDates.size();i++){
			for(int j=i+1;j<eventtempDates.size();j++){
				if(eventtempDates.get(i).equals(eventtempDates.get(j))){
					cnt+=1;
				}
			}
			if(cnt<1){
				eventDates.add(eventtempDates.get(i));
			}
			cnt=0;
		}*/

        for (int j = 0; j < eventDates.size(); j++) {

            Bundle sessionDate = new Bundle();
            sessionDate.putString("sessionDate", eventDates.get(j));

            //String tag = "Tab" + j;
            String tag = eventDates.get(j);


            String formate1 = ApiConstant.dateformat;
            String formate2 = ApiConstant.dateformat1;
            String formate3 = ApiConstant.dateformat2;
            String formate4 = ApiConstant.dateformat3;
            String formate5 = ApiConstant.dateformat4;

            DateFormat outputFormat = new SimpleDateFormat("dd");
            SimpleDateFormat formatter = null;

            if (Utility.isValidFormat(formate1, eventDates.get(j), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat);
            } else if (Utility.isValidFormat(formate2, eventDates.get(j), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat1);
            } else if (Utility.isValidFormat(formate3, eventDates.get(j), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat2);
            } else if (Utility.isValidFormat(formate4, eventDates.get(j), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat3);
            } else if (Utility.isValidFormat(formate5, eventDates.get(j), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat4);
            }

            try {
                d = formatter.parse(eventDates.get(j));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                String outputDateStr = outputFormat.format(d);


// SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                Date date;
                try {
                    date = formatter.parse(eventDates.get(j));
//DateFormat dayFormate=new SimpleDateFormat("EEE");
                    DateFormat dayFormate = new SimpleDateFormat("MMM");

                    dayFromDate = dayFormate.format(date);
                    Log.d("asd", "----------:: " + dayFromDate);

                } catch (ParseException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // Create Session Tabs
                try {
                    agendaTabHost.addTab(
                            agendaTabHost.newTabSpec(tag)
                                    .setIndicator(createTabView(getActivity(), outputDateStr, dayFromDate)),
                            AgendaWebFragment.class, sessionDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (imgTemp.equalsIgnoreCase("0")) {
                if (agendaFolderImageList.size() > 0) {
                    agendaFolderImageList.clear();
                }
//                for (int i = 0; i < agendaDBList.size(); i++) {


                String imagedate = agendaDBList.get(0).getFolder_name();
                String session = agendaDBList.get(0).getSessionId();
                if (imagedate.equalsIgnoreCase(eventDates.get(0))) {
                    for (int k = 0; k < agendaFolderDBList.size(); k++) {
                        if (session.equalsIgnoreCase(agendaFolderDBList.get(k).getSession_vacation_id()))
                            agendaFolderImageList.add(agendaFolderDBList.get(k));
                    }
                }

//                }


                try {
                    swipepagerAdapter = new SwipepagerAgendaImage(getActivity(), agendaFolderImageList);
                    mViewPager.setAdapter(swipepagerAdapter);
                    swipepagerAdapter.notifyDataSetChanged();
                    //mViewPager.setOffscreenPageLimit(0);
                    sliderDotspanel.removeAllViews();

                    dotscount = agendaFolderImageList.size();
                    dots = new ImageView[dotscount];

                    for (int i = 0; i < dotscount; i++) {

                        dots[i] = new ImageView(getActivity());
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        params.setMargins(8, 0, 8, 0);

                        sliderDotspanel.addView(dots[i], params);

                    }

                    dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));
                }catch (Exception e){

                }


                try {


                    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {

                            for (int i = 0; i < dotscount; i++) {
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                            }

                            dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    imgTemp = "1";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


        // Temp
        if (eventDates.size() == 0) {

            Bundle sessionDate = new Bundle();
            sessionDate.putString("sessionDate", "");

            // Create Session Tabs
            agendaTabHost.addTab(
                    agendaTabHost.newTabSpec("Tab 0")
                            .setIndicator(createTabView(agendaTabHost.getContext(), "No Agenda Available", "")),
                    AgendaListFragment.class, sessionDate);

        }


        for (int i = 0; i < tabWidget.getChildCount(); i++) {
//            tabWidget.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.activetab));
        }

        // set the active tab
//        tabWidget.getChildAt(agendaTabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.activetab));

//        for (int i = 0; i < agendaTabHost.getTabWidget().getChildCount(); i++) {
//            // mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FF0000")); // unselected
//            tab_text = (LinearLayout) agendaTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_text); //Unselected Tabs
//            tab_text.setBackgroundColor(Color.parseColor("#f15a2b"));
//        }

        agendaTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                JzvdStd.releaseAllVideos();
                for (int i = 0; i < tabWidget.getChildCount(); i++) {
//                    tabWidget.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.activetab));

                }

                // set the active tab
//                tabWidget.getChildAt(agendaTabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.activetab));
//                tab_text = (LinearLayout) agendaTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_text); //Unselected Tabs
//                tab_text.setBackgroundColor(Color.parseColor("#80000000"));

                if (agendaFolderImageList.size() > 0) {
                    agendaFolderImageList.clear();
                }
                sliderDotspanel.removeAllViews();

//                for (int i=0;i<agendaDBList.size();i++){

//                }

                for (int i = 0; i < agendaDBList.size(); i++) {


                    String imagedate = agendaDBList.get(i).getFolder_name();
                    String sessionId = agendaDBList.get(i).getSessionId();
                    if (imagedate.equalsIgnoreCase(tabId)) {
                        txtname.setText(agendaDBList.get(i).getSession_name());
//                        webView.loadData(agendaDBList.get(i).getSession_description(), "text/html", null);
//                        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
                        for (int j = 0; j < agendaFolderDBList.size(); j++) {
                            if (sessionId.equalsIgnoreCase(agendaFolderDBList.get(j).getSession_vacation_id()))
                                agendaFolderImageList.add(agendaFolderDBList.get(j));
                        }
                    }

                }

                swipepagerAdapter = new SwipepagerAgendaImage(getActivity(), agendaFolderImageList);
                mViewPager.setAdapter(swipepagerAdapter);
                //mViewPager.setOffscreenPageLimit(0);


                swipepagerAdapter.notifyDataSetChanged();

                try {

                    if (SwipepagerAgendaImage.videoplayer != null) {
                       /* if (SwipepagerAgendaImage.videoplayer.isCurrentPlay()) {
                            SwipepagerAgendaImage.videoplayer.release();
                        }*/

                    }
                   // SwipepagerAgendaImage.videoplayer.release();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                dotscount = agendaFolderImageList.size();
                dots = new ImageView[dotscount];

                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(getActivity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(8, 0, 8, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));


                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        for (int i = 0; i < dotscount; i++) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                        }

                        dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        try {

                            if (SwipepagerAgendaImage.videoplayer != null) {
                               /* if (SwipepagerAgendaImage.videoplayer.isCurrentPlay()) {
                                    SwipepagerAgendaImage.videoplayer.release();
                                }*/

                            }
                           // SwipepagerAgendaImage.videoplayer.release();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });


                //Toast.makeText(getActivity(), tabId, Toast.LENGTH_SHORT).show();

            }
        });
    }

    // Remove FragmentTabHost
    @Override
    public void onDestroyView() {

        agendaTabHost = null;
        eventDates.clear();
        eventtempDates.clear();
        super.onDestroyView();

    }

    @Override
    public void onContactSelected(AgendaFolder filtergallerylists) {

    }

//	private class GetUpdateAgenda extends AsyncTask<Void, Void, Void> {
//
//		JSONObject jsonObj = null;
//		String status = "";
//		String statusMsg = "";
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//
//			// Showing Progress Dialog9820325285
//			pDialog = new ProgressDialog(getActivity(), R.style.LoaderTheme);
//			pDialog.setCancelable(false);
//			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//			pDialog.show();
//		}
//
//		@Override
//		protected Void doInBackground(Void... arg0) {
//
//			// Setting Thread Priority
//			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
//			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);
//
//			// Creating service handler class instance
//			ServiceHandler sh = new ServiceHandler();
//			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
//
//			nameValuePair.add(new BasicNameValuePair("api_access_token", api_access_token));
//
//			nameValuePair.add(new BasicNameValuePair("event_id", eventId));
//
//			// nameValuePair.add(new BasicNameValuePair("password", password));
//
//			// Making a request to url and getting response
//			String jsonStr = sh.makeServiceCall(updateAgendaUrl, ServiceHandler.POST, nameValuePair);
//
//			Log.d("Response: ", "> " + jsonStr);
//
//			if (jsonStr != null) {
//				try {
//
//					jsonObj = new JSONObject(jsonStr);
//					status = jsonObj.getString("status");
//					statusMsg = jsonObj.getString("msg");
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//
//			if (status.equalsIgnoreCase("success")) {
//
//				// Agenda Parser
//				agendaParser = new AgendaParser();
//				agendaList = agendaParser.Agenda_Parser(jsonStr);
//
//				//AgendaFolder Parser
//				agendaFolderParser = new AgendaFolderParser();
//				agendaFolderList = agendaFolderParser.Agenda_Folder_Parser(jsonStr);
//
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
//
//			// // Dismiss the progress dialog
//			 if (pDialog.isShowing())
//			 pDialog.dismiss();
//
//			// Database Operations
//
//
//			if (status.equalsIgnoreCase("success")) {
//
//				procializeDB.clearAgendaTable();
//				procializeDB.clearAgendaFolderTable();
//
//				procializeDB.insertAgendaData(agendaList, db);
//				procializeDB.insertAgendaFolderData(agendaFolderList,db);
//
//				dbHelper = new DBHelper(getActivity());
//				agendaDBList = new ArrayList<Agenda>();
//				agendaFolderDBList = new ArrayList<AgendaFolder>();
//				agendaDBList = dbHelper.getAgendaList();
//				agendaFolderDBList = dbHelper.getAgendaFolderList();
//
//
//
//			} else if (status.equalsIgnoreCase("error")) {
//
//				if (pDialog != null) {
//					pDialog.dismiss();
//					pDialog = null;
//				}
//
//
//
//			}
//
//		}
//	}

    public void fetchAgenda(String token, String eventid) {
//		progressBar.setVisibility(View.VISIBLE);
        mAPIService.AgendaFetchVacation(token, eventid).enqueue(new Callback<Agenda>() {
            @Override
            public void onResponse(Call<Agenda> call, Response<Agenda> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//					progressBar.setVisibility(View.GONE);

//                    if (swipe_refresh.isRefreshing()) {
//                        swipe_refresh.setRefreshing(false);
//                    }
                    showResponse(response);
                } else {
//					progressBar.setVisibility(View.GONE);
//                    if (swipe_refresh.isRefreshing()) {
//                        swipe_refresh.setRefreshing(false);
//                    }
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Agenda> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");

//                txtname.setText("Coming Soon..");
//                txtname.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);
//				progressBar.setVisibility(View.GONE);
//                if (swipe_refresh.isRefreshing()) {
//                    swipe_refresh.setRefreshing(false);
//                }
                //  Toast.makeText(getContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showResponse(Response<Agenda> response) {

        procializeDB.clearAgendaVacationTable();
        procializeDB.clearAgendaFolerTable();

        agendaList = response.body().getAgenda_vacation_list();
        agendaFolderList = response.body().getAgenda_vacation_media_list();
        procializeDB.insertAgendaVacationInfo(agendaList, db);
        procializeDB.insertAgendaMediaInfo(agendaFolderList, db);

        dbHelper = new DBHelper(getActivity());
        agendaDBList = new ArrayList<AgendaVacationList>();
        agendaFolderDBList = new ArrayList<AgendaMediaList>();
        agendaDBList = dbHelper.getAgendaFolderList();
        agendaFolderDBList = dbHelper.getAgendaMediaList();


        initview();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }
//    public void setTabColor(TabHost tabhost) {
//
//        for(int i=0;i<agendaTabHost.getTabWidget().getChildCount();i++)
//            agendaTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFF")); //unselected
//
//        if(tabhost.getCurrentTab()==0)
//            agendaTabHost.getTabWidget().getChildAt(agendaTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor(colorActive)); //1st tab selected
//        else
//            agendaTabHost.getTabWidget().getChildAt(agendaTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FFFFFF")); //2nd tab selected
//    }

}
