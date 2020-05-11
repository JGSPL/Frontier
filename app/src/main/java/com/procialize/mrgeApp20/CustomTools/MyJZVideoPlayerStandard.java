package com.procialize.mrgeApp20.CustomTools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.procialize.mrgeApp20.R;

import cn.jzvd.JzvdStd;

/**
 * 这里可以监听到视频播放的生命周期和播放状态
 * 所有关于视频的逻辑都应该写在这里
 * Created by Nathen on 2017/7/2.
 */
public class MyJZVideoPlayerStandard extends JzvdStd {


    public MyJZVideoPlayerStandard(Context context) {
        super(context);
    }

    public MyJZVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
    }

    @Override
    public void onClick(View v) {

        try {
            super.onClick(v);
            int i = v.getId();
            if (i == R.id.fullscreen) {

            } else if (i == R.id.back) {
                MyJZVideoPlayerStandard.goOnPlayOnPause();
            } else if (i == R.id.back_tiny) {
                MyJZVideoPlayerStandard.goOnPlayOnPause();
            }
        } catch (Exception e) {
            startVideo();
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_layout_jzplayer;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @Override
    public void startVideo() {
        super.startVideo();
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
    }

    @Override
    public void onStateError() {
        super.onStateError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }


}
