package com.procialize.mrgeApp20.CustomTools;

import android.widget.AbsListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.mrgeApp20.R;

import cn.jzvd.JzvdStd;

public abstract class EndlessRecyclerOnScrollListener extends
        RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class
            .getSimpleName();
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;
    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_FLING;

    public EndlessRecyclerOnScrollListener(
            LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading
                && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;

            JzvdStd videoView = recyclerView.findViewById(R.id.videoplayer);
            try {
                if (mScrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                    /*if (videoView != null) {
                        if (videoView.isCurrentPlay()) {
                            videoView.onStatePause();
                        }
                        videoView.release();
                        videoView = null;
                    }*/
                }
                // videoView.release();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void onLoadMore(int current_page);
}
