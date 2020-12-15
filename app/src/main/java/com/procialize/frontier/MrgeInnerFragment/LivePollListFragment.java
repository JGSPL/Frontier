package com.procialize.frontier.MrgeInnerFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.frontier.Activity.PollDetailActivity;
import com.procialize.frontier.Adapter.PollNewAdapter;
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.GetterSetter.LivePollFetch;
import com.procialize.frontier.GetterSetter.LivePollList;
import com.procialize.frontier.GetterSetter.LivePollLogo;
import com.procialize.frontier.GetterSetter.LivePollOptionList;
import com.procialize.frontier.MergeMain.MrgeHomeActivity;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_LIVE_POLL_LOGO;
import static com.procialize.frontier.Session.ImagePathConstants.KEY_LIVE_POLL_LOGO_PATH;
import static com.procialize.frontier.Utility.Util.setNotification;
import static com.procialize.frontier.util.CommonFunction.crashlytics;
import static com.procialize.frontier.util.CommonFunction.firbaseAnalytics;

public class LivePollListFragment extends Fragment implements PollNewAdapter.PollAdapterListner{

    View rootView;
    SwipeRefreshLayout pollrefresh;
    ListView pollRv;
    ProgressBar progressBar;
    List<LivePollOptionList> optionLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    TextView emptyView;
    private APIService mAPIService;
    private ConnectionDetector cd;
    TextView empty, pullrefresh;
    LinearLayout linear;
     String token;
     ImageView iv_live_poll_logo;

    public static Activity activity;

    public LivePollListFragment(Activity activity) {
        this.activity=activity;
    }

    public static LivePollListFragment newInstance() {
        LivePollListFragment fragment = new LivePollListFragment(activity);
        return fragment;
    }

    public LivePollListFragment() {
        // doesn't do anything special
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_live_poll, container, false);

        try {
            setNotification(getActivity(),MrgeHomeActivity.tv_notification,MrgeHomeActivity.ll_notification_count);
        }catch (Exception e)
        {e.printStackTrace();}

        /*DialogLivePoll dialogLivePoll = new DialogLivePoll();
        dialogLivePoll.welcomeLivePollDialog(getActivity());*/

        cd = new ConnectionDetector(getContext());
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        pollrefresh = rootView.findViewById(R.id.pollrefresh);
        progressBar = rootView.findViewById(R.id.progressBar);
        linear = rootView.findViewById(R.id.linear);
        pullrefresh = rootView.findViewById(R.id.pullrefresh);
        pollRv = rootView.findViewById(R.id.pollRv);

        empty = rootView.findViewById(R.id.empty);
        iv_live_poll_logo = rootView.findViewById(R.id.iv_live_poll_logo);

        TextView header = rootView.findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));
        pullrefresh.setTextColor(Color.parseColor(colorActive));

       // RelativeLayout layoutTop = rootView.findViewById(R.id.layoutTop);
       // layoutTop.setBackgroundColor(Color.parseColor(colorActive));

        optionLists = new ArrayList<>();

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
         token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Live Poll",token);
        firbaseAnalytics(getContext(), "Live Poll", token);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        //  pollRv.setLayoutAnimation(animation);

        if (cd.isConnectingToInternet()) {
            fetchPoll(token, eventid);
        } else {

            Toast.makeText(getContext(), "No internet connection",
                    Toast.LENGTH_SHORT).show();

        }


        pollrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    fetchPoll(token, eventid);
                } else {
                    if (pollrefresh.isRefreshing()) {
                        pollrefresh.setRefreshing(false);
                    }
                    Toast.makeText(getContext(), "No internet connection",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        return rootView;
    }


    public void fetchPoll(String token, String eventid) {
        showProgress();
        mAPIService.LivePollFetch(token, eventid).enqueue(new Callback<LivePollFetch>() {
            @Override
            public void onResponse(Call<LivePollFetch> call, Response<LivePollFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (pollrefresh.isRefreshing()) {
                        pollrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {
                    if (pollrefresh.isRefreshing()) {
                        pollrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LivePollFetch> call, Throwable t) {
                Toast.makeText(getContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (pollrefresh.isRefreshing()) {
                    pollrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<LivePollFetch> response) {

        // specify an adapter (see also next example)

        optionLists = response.body().getLivePollOptionList();
        String logoPath = response.body().getLogo_url_path();

        LivePollLogo logo = response.body().getLive_poll_logo();
        try {
            String strAppLivePollLogo = logo.getApp_livepoll_logo();
            SharedPreferences prefs1 = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor =prefs1.edit();
           editor.putString(KEY_LIVE_POLL_LOGO_PATH,logoPath);
           editor.putString(KEY_LIVE_POLL_LOGO,strAppLivePollLogo);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //empty.setTextColor(Color.parseColor(colorActive));
        if (response.body().getLivePollOptionList().size() != 0) {

            empty.setVisibility(View.GONE);
            PollNewAdapter pollAdapter = new PollNewAdapter(getContext(), response.body().getLivePollList(), response.body().getLivePollOptionList(), LivePollListFragment.this);
            pollAdapter.notifyDataSetChanged();
            pollRv.setAdapter(pollAdapter);
        } else {
            empty.setTextColor(Color.parseColor(colorActive));

            empty.setVisibility(View.VISIBLE);
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
    public void onContactSelected(LivePollList pollList) {

        if (pollList.getReplied().equalsIgnoreCase("1")) {
            if (pollList.getShow_result().equalsIgnoreCase("1")) {
                Toast.makeText(getContext(), "You are already submit quiz.", Toast.LENGTH_SHORT).show();
            } else {
                Intent polldetail = new Intent(getContext(), PollDetailActivity.class);
                polldetail.putExtra("id", pollList.getId());
                polldetail.putExtra("question", pollList.getQuestion());
                polldetail.putExtra("replied", pollList.getReplied());
                polldetail.putExtra("show_result", pollList.getShow_result());
                polldetail.putExtra("optionlist", (Serializable) optionLists);
                startActivity(polldetail);
            }

        } else {
            Intent polldetail = new Intent(getContext(), PollDetailActivity.class);
            polldetail.putExtra("id", pollList.getId());
            polldetail.putExtra("question", pollList.getQuestion());
            polldetail.putExtra("replied", pollList.getReplied());
            polldetail.putExtra("optionlist", (Serializable) optionLists);
            polldetail.putExtra("show_result", pollList.getShow_result());
            startActivity(polldetail);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }
    @Override
    public void onResume() {
        super.onResume();

        if (cd.isConnectingToInternet()) {
            fetchPoll(token, eventid);
        } else {
            if (pollrefresh.isRefreshing()) {
                pollrefresh.setRefreshing(false);
            }
            Toast.makeText(getContext(), "No internet connection",
                    Toast.LENGTH_SHORT).show();

        }
    }

//    public void onBackpressed() {
//
//        Intent intent = new Intent(activity, MrgeHomeActivity.class);
//        activity.startActivity(intent);
//    }
}