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
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;

public class DownloadsFragment extends Fragment implements DocumentsListAdapter.DocumentsAdapterListner,DocumentsGridAdapter.DocumentsAdapterListner {


    SwipeRefreshLayout docRvrefresh;
    RecyclerView docRv;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, logoImg, colorActive;
    ImageView headerlogoIv;
    RelativeLayout linear;
    TextView msg;
    View rootView;
    List<DocumentList> documentsList;
    private APIService mAPIService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_documents, container, false);
        setHasOptionsMenu(true);
        initView(rootView);
        return rootView;
    }

    public void initView(View rootView) {

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
        //   docRv.setLayoutAnimation(animation);


        fetchDocuments(token, eventid);

        docRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDocuments(token, eventid);
            }
        });

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

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
        startActivity(pdfview);
    }

    @Override
    public void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.navmenudownloads, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grid:
               /* int colorInt = Color.parseColor(colorActive);
                ColorStateList csl = ColorStateList.valueOf(colorInt);
                item.setIconTintList(csl);*/

                DocumentsGridAdapter docAdapterGrid = new DocumentsGridAdapter(getActivity(),documentsList, this);
                docRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                docAdapterGrid.notifyDataSetChanged();
                docRv.setAdapter(docAdapterGrid);
                docRv.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);

                break;
            case R.id.action_list:
               /* int colorInt2 = Color.parseColor(colorActive);
                ColorStateList csl2 = ColorStateList.valueOf(colorInt2);
                item.setIconTintList(csl2);*/



                DocumentsListAdapter docAdapter = new DocumentsListAdapter(getActivity(),documentsList, this);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                docRv.setLayoutManager(mLayoutManager);
                docAdapter.notifyDataSetChanged();
                docRv.setAdapter(docAdapter);
                docRv.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
