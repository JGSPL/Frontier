package com.procialize.mrgeApp20.MrgeInnerFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.mrgeApp20.Adapter.QADirectAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DialogQnA.DialogQnADirect;
import com.procialize.mrgeApp20.GetterSetter.DirectQuestion;
import com.procialize.mrgeApp20.GetterSetter.QADirectFetch;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.Utility.Utility.setgradientDrawable;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

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

        try {
            setNotification(getActivity(), MrgeHomeActivity.tv_notification,MrgeHomeActivity.ll_notification_count);
        }catch (Exception e)
        {e.printStackTrace();}

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        init();
        /*DialogQnADirect dialogQna = new DialogQnADirect();
        dialogQna.welcomeQnADialog(getContext());*/
        return rootView;
    }
    
    void init(){
        progressBar = rootView.findViewById(R.id.progressBar);
        qaRvrefresh = rootView.findViewById(R.id.qaRvrefresh);
        postbtn = rootView.findViewById(R.id.postbtn);
        qaRv = rootView.findViewById(R.id.qaRv);
        linUpper = rootView.findViewById(R.id.linUpper);
        txtEmpty = rootView.findViewById(R.id.txtEmpty);
        nmtxt = rootView.findViewById(R.id.nmtxt);
        linear = rootView.findViewById(R.id.linear);
        pullrefresh = rootView.findViewById(R.id.pullrefresh);

        GradientDrawable shape = setgradientDrawable(5, colorActive);
        postbtn.setBackground(shape);

       
       
        nmtxt.setTextColor(Color.parseColor(colorActive));
        pullrefresh.setTextColor(Color.parseColor(colorActive));
        txtEmpty.setTextColor(Color.parseColor(colorActive));

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("QA Direct",token);
        firbaseAnalytics(getContext(),"QA Direct",token);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        qaRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        // qaRv.setLayoutAnimation(animation);

        QAFetch(token, eventid);


        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge();
            }
        });

        qaRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                QAFetch(token, eventid);
            }
        });
    }
    public void QAFetch(String token, String eventid) {
        showProgress();
        mAPIService.QADirectFetch(token, eventid).enqueue(new Callback<QADirectFetch>() {
            @Override
            public void onResponse(Call<QADirectFetch> call, Response<QADirectFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {
                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QADirectFetch> call, Throwable t) {
                Toast.makeText(getContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (qaRvrefresh.isRefreshing()) {
                    qaRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<QADirectFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {

            if (myDialog != null) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }
            }
            if (!(response.body().getQa_question().isEmpty())) {
                //  txtEmpty.setVisibility(View.GONE);
                //linUpper.setBackground(getResources().getDrawable(R.drawable.close_icon));

                qaAttendeeAdapter = new QADirectAdapter(getContext(), response.body().getQa_question(), this);
                qaAttendeeAdapter.notifyDataSetChanged();
                qaRv.setAdapter(qaAttendeeAdapter);
                qaRv.scheduleLayoutAnimation();
                txtEmpty.setVisibility(View.GONE);
            } else {
                txtEmpty.setVisibility(View.VISIBLE);
                //linUpper.setBackground(getResources().getDrawable(R.drawable.noqna));

                qaAttendeeAdapter = new QADirectAdapter(getContext(), response.body().getQa_question(), this);
                qaAttendeeAdapter.notifyDataSetChanged();
                qaRv.setAdapter(qaAttendeeAdapter);
                qaRv.scheduleLayoutAnimation();


            }
            
        } else {
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

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

    private void showratedialouge() {

        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.dialouge_msg_layout);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        myDialog.setCancelable(false);
        myDialog.show();
        LinearLayout diatitle = myDialog.findViewById(R.id.diatitle);

        diatitle.setBackgroundColor(Color.parseColor(colorActive));


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);

        cancelbtn.setBackgroundColor(Color.parseColor(colorActive));
        ratebtn.setBackgroundColor(Color.parseColor(colorActive));

        ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);

        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);
        final TextView title = myDialog.findViewById(R.id.title);
        nametv.setVisibility(View.GONE);
        title.setText("Post Question");

        nametv.setTextColor(Color.parseColor(colorActive));


        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 250 - s.length();
                counttv.setText(count + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etmsg.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    PostQuetion(token, eventid, msg);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("");
                    builder.setMessage("Please post a question");

                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                    //  Toast.makeText(getContext(), "Please post a question", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void PostQuetion(final String token, final String eventid, String msg) {
        mAPIService.QADirectPost(token, eventid, msg).enqueue(new Callback<QADirectFetch>() {
            @Override
            public void onResponse(Call<QADirectFetch> call, Response<QADirectFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    if (myDialog != null) {
                        if (myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                    }
                    QAFetch(token, eventid);

                } else {

                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<QADirectFetch> call, Throwable t) {
                Toast.makeText(getContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onLikeListener(View v, DirectQuestion question, int position, TextView countTv, ImageView likeIv) {
        if (question.getLike_flag().equals("1")) {


            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_like));
            try {

                int count = Integer.parseInt(question.getTotal_likes());

                if (count > 0) {
                    count = count - 1;
                    countTv.setText(count + " Likes");

                } else {
                    countTv.setText("0" + " Likes");
                }

//            QAFetch(token,eventid);
                QALike(token, eventid, question.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_afterlike));
            likeIv.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);

            QALike(token, eventid, question.getId());


            try {

                int count = Integer.parseInt(question.getTotal_likes());


                count = count + 1;
                countTv.setText(count + " Likes");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void QALike(String token, String eventid, String questionid) {
        mAPIService.QADirectLike(token, eventid, questionid).enqueue(new Callback<QADirectFetch>() {
            @Override
            public void onResponse(Call<QADirectFetch> call, Response<QADirectFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().getMsg());


                    showLikeResponse(response);
                } else {

                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<QADirectFetch> call, Throwable t) {
                Toast.makeText(getContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showLikeResponse(Response<QADirectFetch> response) {

        if (response.body().getStatus().equalsIgnoreCase("success")) {
//            Toast.makeText(QASpeakerActivity.this,response.message(),Toast.LENGTH_SHORT).show();
//            ArrayList<DirectQuestion> speakerQuestionLists = new ArrayList<>();

            if (!(response.body().getQa_question().isEmpty())) {
                txtEmpty.setVisibility(View.GONE);
                //linUpper.setBackground(getResources().getDrawable(R.drawable.close_icon));

                qaAttendeeAdapter = new QADirectAdapter(getContext(), response.body().getQa_question(), this);
                qaAttendeeAdapter.notifyDataSetChanged();
                qaRv.setAdapter(qaAttendeeAdapter);
                qaRv.scheduleLayoutAnimation();
            } else {
                txtEmpty.setVisibility(View.VISIBLE);
                // linUpper.setBackground(getResources().getDrawable(R.drawable.qnadi));

                qaAttendeeAdapter = new QADirectAdapter(getContext(), response.body().getQa_question(), this);
                qaAttendeeAdapter.notifyDataSetChanged();
                qaRv.setAdapter(qaAttendeeAdapter);
                qaRv.scheduleLayoutAnimation();


            }

           /* qaAttendeeAdapter = new QADirectAdapter(getContext(), response.body().getQa_question(), this);
            qaAttendeeAdapter.notifyDataSetChanged();
            qaRv.setAdapter(qaAttendeeAdapter);
            qaRv.scheduleLayoutAnimation();*/
        } else {
            Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
        }
    }
   
}
