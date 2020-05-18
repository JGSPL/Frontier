package com.procialize.mrgeApp20.MrgeInnerFragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.mrgeApp20.Adapter.QADirectAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.GetterSetter.DirectQuestion;
import com.procialize.mrgeApp20.R;

import static android.content.Context.MODE_PRIVATE;

public class QnADirectFragment extends Fragment implements QADirectAdapter.QADirectAdapterListner{

    View rootView;
    public Button postbtn;
    public QADirectAdapter qaAttendeeAdapter;
    Dialog myDialog;
    String token;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    SwipeRefreshLayout qaRvrefresh;
    ProgressBar progressBar;
    RecyclerView qaRv;
    ImageView headerlogoIv;
    RelativeLayout linUpper;
    TextView txtEmpty, nmtxt,pullrefresh;
    private APIService mAPIService;
    LinearLayout linear;


    public QnADirectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_qadirect, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        return rootView;
    }

    @Override
    public void onLikeListener(View v, DirectQuestion question, int position, TextView count, ImageView likeIv) {

    }
}
