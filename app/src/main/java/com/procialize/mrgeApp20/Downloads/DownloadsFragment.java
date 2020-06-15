package com.procialize.mrgeApp20.Downloads;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.procialize.mrgeApp20.Activity.PdfViewerActivity;
import com.procialize.mrgeApp20.Downloads.Activity.DownloadPdfActivity;
import com.procialize.mrgeApp20.Downloads.Adapter.DocumentsGridAdapter;
import com.procialize.mrgeApp20.Downloads.Adapter.DocumentsListAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.GetterSetter.DocumentList;
import com.procialize.mrgeApp20.GetterSetter.DocumentsListFetch;
import com.procialize.mrgeApp20.MergeMain.MrgeHomeActivity;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.util.GetUserActivityReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Utility.Util.setNotification;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class DownloadsFragment extends Fragment implements DocumentsListAdapter.DocumentsAdapterListner,DocumentsGridAdapter.DocumentsAdapterListner,View.OnClickListener{


    SwipeRefreshLayout docRvrefresh;
    RecyclerView docRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, logoImg, colorActive;
    ImageView headerlogoIv;
    RelativeLayout linear;
    TextView msg;
    View rootView;
    List<DocumentList> documentsList = new ArrayList<>();
    private APIService mAPIService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_documents, container, false);
        //setHasOptionsMenu(true);
        MrgeHomeActivity.grid_image_view.setImageDrawable(getResources().getDrawable(R.drawable.active_grid_view));
        MrgeHomeActivity.list_image_view.setImageDrawable(getResources().getDrawable(R.drawable.inactive_list_view));

        initView(rootView);
        return rootView;
    }


    public void initView(View rootView) {

        try {
            setNotification(getActivity(),MrgeHomeActivity.tv_notification,MrgeHomeActivity.ll_notification_count);
        }catch (Exception e)
        {e.printStackTrace();}

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        logoImg = prefs.getString("logoImg", "");
        colorActive = prefs.getString("colorActive", "");

        docRv = rootView.findViewById(R.id.docRv);

        TextView header = rootView.findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));

        docRvrefresh = rootView.findViewById(R.id.docRvrefresh);
        progressBar = rootView.findViewById(R.id.progressBar);
        linear = rootView.findViewById(R.id.linear);
        msg = rootView.findViewById(R.id.msg);

        MrgeHomeActivity.grid_image_view.setOnClickListener(this);
        MrgeHomeActivity.list_image_view.setOnClickListener(this);

        mAPIService = ApiUtils.getAPIService();
        SessionManager sessionManager = new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Documents", token);
        firbaseAnalytics(getActivity(), "Documents", token);
        // use a linear layout manager

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
        //docRv.setLayoutAnimation(animation);

        fetchDocuments(token, eventid);

        docRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDocuments(token, eventid);
            }
        });

        /*GetUserActivityReport getUserActivityReport = new GetUserActivityReport(getActivity(), token,
                eventid,
                ApiConstant.pageVisited,
                "23",
                "");
        getUserActivityReport.userActivityReport();*/
    }

    public void fetchDocuments(String token, String eventid) {
        showProgress();
        mAPIService.DocumentsListFetch(token, eventid).enqueue(new Callback<DocumentsListFetch>() {
            @Override
            public void onResponse(Call<DocumentsListFetch> call, Response<DocumentsListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    dismissProgress();
                    if (docRvrefresh.isRefreshing()) {
                        docRvrefresh.setRefreshing(false);
                    }
                    showResponse(response);
                } else {

                    if (docRvrefresh.isRefreshing()) {
                        docRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DocumentsListFetch> call, Throwable t) {
                Toast.makeText(getActivity(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (docRvrefresh.isRefreshing()) {
                    docRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<DocumentsListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getDocumentList().isEmpty())) {
                 documentsList = response.body().getDocumentList();
                DocumentsGridAdapter docAdapterGrid = new DocumentsGridAdapter(getActivity(),documentsList, this);
                docRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                docAdapterGrid.notifyDataSetChanged();
                docRv.setAdapter(docAdapterGrid);
                docRv.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);

            } else {

                docRv.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(DocumentsActivity.this, EmptyViewActivity.class);
//                startActivity(intent);
//                finish();
            }
        } else {
            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
    public void onContactSelected(DocumentList document) {
        Intent pdfview = new Intent(getActivity(), DownloadPdfActivity.class);
        pdfview.putExtra("url", "https://docs.google.com/gview?embedded=true&url=" + ApiConstant.imgURL + "uploads/documents/" + document.getFileName());
        pdfview.putExtra("url1", ApiConstant.imgURL + "uploads/documents/" + document.getFileName());
        pdfview.putExtra("doc_name",  document.getTitle());
        pdfview.putExtra("page_id",  "24");
        pdfview.putExtra("file_id",  document.getId());
        startActivity(pdfview);
    }

    @Override
    public void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.grid_image_view:
                if(documentsList.size() > 0) {
                    MrgeHomeActivity.grid_image_view.setImageDrawable(getResources().getDrawable(R.drawable.active_grid_view));
                    MrgeHomeActivity.list_image_view.setImageDrawable(getResources().getDrawable(R.drawable.inactive_list_view));
                    DocumentsGridAdapter docAdapterGrid = new DocumentsGridAdapter(getActivity(), documentsList, this);
                    docRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    docAdapterGrid.notifyDataSetChanged();
                    docRv.setAdapter(docAdapterGrid);
                    docRv.setVisibility(View.VISIBLE);
                    msg.setVisibility(View.GONE);
                }
                break;
            case R.id.list_image_view:
                if(documentsList.size() > 0) {
                    MrgeHomeActivity.grid_image_view.setImageDrawable(getResources().getDrawable(R.drawable.inactive_grid_view));
                    MrgeHomeActivity.list_image_view.setImageDrawable(getResources().getDrawable(R.drawable.active_list_view));
                    DocumentsListAdapter docAdapter = new DocumentsListAdapter(getActivity(), documentsList, this);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    docRv.setLayoutManager(mLayoutManager);
                    docAdapter.notifyDataSetChanged();
                    docRv.setAdapter(docAdapter);
                    docRv.setVisibility(View.VISIBLE);
                    msg.setVisibility(View.GONE);
                }
                break;
        }
    }
}