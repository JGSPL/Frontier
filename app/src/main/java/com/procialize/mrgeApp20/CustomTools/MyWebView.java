package com.procialize.mrgeApp20.CustomTools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebView;

/**
 * Created by Naushad on 12/11/2017.
 */

public class MyWebView extends WebView {

    public MyWebView(Context context) {
        super(context);
        initView(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    private void initView(Context context) {
        // i am not sure with these inflater lines
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // you should not use a new instance of MyWebView here
        // MyWebView view = (MyWebView) inflater.inflate(R.layout.custom_webview, this);
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setDomStorageEnabled(true);

    }
}