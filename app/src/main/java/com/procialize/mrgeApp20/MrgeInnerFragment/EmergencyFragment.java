package com.procialize.mrgeApp20.MrgeInnerFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.GetterSetter.Contact;
import com.procialize.mrgeApp20.GetterSetter.ContactListFetch;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class EmergencyFragment extends Fragment {

    View rootView;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    ProgressBar progressBar;
    List<Contact> contactList;
    WebView mywebview;
    private APIService mAPIService;
    SwipeRefreshLayout webrefresher;
    ConnectionDetector cd;
    LinearLayout linear;
    TextView headertxt;

    public EmergencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_emergemcy, container, false);

        try {
            setNotification(getActivity(),MrgeHomeActivity.tv_notification,MrgeHomeActivity.ll_notification_count);
        }catch (Exception e)
        {e.printStackTrace();}

        MrgeHomeActivity.headerlogoIv.setVisibility(View.GONE);
        MrgeHomeActivity.txtMainHeader.setVisibility(View.VISIBLE);

        //Util.logomethodwithText(getContext(), MrgeHomeActivity.txtMainHeader, "Event Info");
        cd = new ConnectionDetector(getContext());
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);


        headerlogoIv = rootView.findViewById(R.id.headerlogoIv);
        Util.logomethod(getContext(), headerlogoIv);


        mywebview = rootView.findViewById(R.id.webView);
        webrefresher = rootView.findViewById(R.id.webrefresher);
        linear = rootView.findViewById(R.id.linear);
        headertxt = rootView.findViewById(R.id.headertxt);
        mywebview.setBackgroundColor(Color.TRANSPARENT);
        headertxt.setTextColor(Color.parseColor(colorActive));

       /* try {

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));

        }*/

        WebSettings settings = mywebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        // mywebview.loadUrl("https://www.procialize.info/contact_us.html");

        if (cd.isConnectingToInternet()) {
            fetchContact(eventid, token);
        } else {
            Toast.makeText(getContext(), "Device not connected to internet", Toast.LENGTH_SHORT).show();
        }

        crashlytics("Contact Us",token);
        firbaseAnalytics(getContext(),"Contact Us",token);
        webrefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (cd.isConnectingToInternet()) {
                    fetchContact(eventid, token);
                } else {
                    if (webrefresher.isRefreshing()) {
                        webrefresher.setRefreshing(false);
                    }
                    Toast.makeText(getContext(), "Device not connected to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rootView;
    }
    public void fetchContact(String eventid, String token) {
        showProgress();
        mAPIService.ContactListFetch(eventid, token).enqueue(new Callback<ContactListFetch>() {
            @Override
            public void onResponse(Call<ContactListFetch> call, Response<ContactListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    dismissProgress();
                    if (webrefresher.isRefreshing()) {
                        webrefresher.setRefreshing(false);
                    }
                    showResponse(response);
                } else {

                    if (webrefresher.isRefreshing()) {
                        webrefresher.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ContactListFetch> call, Throwable t) {
                Toast.makeText(getContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                if (webrefresher.isRefreshing()) {
                    webrefresher.setRefreshing(false);
                }
                dismissProgress();

            }
        });
    }

    public void showResponse(Response<ContactListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getContactList().isEmpty())) {
                contactList = response.body().getContactList();
                String contactstr = contactList.get(0).getContent();
                String cssEditor = response.body().getCssEditor();


                mywebview.clearCache(true);
                mywebview.loadData(cssEditor + contactstr, "text/html", "UTF-8");
                mywebview.setWebViewClient(new CustomWebViewClient());

            }
            /* else {

                setContentView(R.layout.activity_empty_view);
                ImageView imageView = findViewById(R.id.back);
                TextView text_empty = findViewById(R.id.text_empty);
                text_empty.setText("Contacts not available");
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });


            }*/

        } else {
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    private class CustomWebViewClient extends WebViewClient {

        public void onPageFinished(WebView view, String url) {

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                view.clearCache(true);
                view.reload();
                return true;
            } else if (url.startsWith("mailto:")) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                startActivity(intent);
                view.clearCache(true);
                view.reload();
                return true;
            }

            view.clearCache(true);
            view.loadUrl(url);
            return true;
        }
    }

    public void showProgress() {
       /* if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }*/
    }

    public void dismissProgress() {
       /* if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void onResume() {
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }



}