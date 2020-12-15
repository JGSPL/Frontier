package com.procialize.frontier.Utility;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.procialize.frontier.Activity.PuzzletwoActivity;
import com.procialize.frontier.GetterSetter.PuzzlePiece;
import com.procialize.frontier.R;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

public class TouchListenerNew implements View.OnTouchListener {
    private float xDelta;
    private float yDelta;
    private PuzzletwoActivity activity;
    private static MediaPlayer sPlayer;
    private final static int PLAY_SOUND = 0;
    private final static int APPLAUSE_SOUND = 1;
    private static AssetFileDescriptor sPlaySoundDescriptor;
    private static AssetFileDescriptor sApplauseSoundDescriptor;

    public TouchListenerNew(PuzzletwoActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getRawX();
        float y = motionEvent.getRawY();
        final double tolerance = sqrt(pow(view.getWidth(), 2) + pow(view.getHeight(), 2)) / 10;

        PuzzlePiece piece = (PuzzlePiece) view;
        if (!piece.canMove) {
            return true;
        }

        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xDelta = x - lParams.leftMargin;
                yDelta = y - lParams.topMargin;
                piece.bringToFront();
                break;
            case MotionEvent.ACTION_MOVE:
                lParams.leftMargin = (int) (x - xDelta);
                lParams.topMargin = (int) (y - yDelta);
                view.setLayoutParams(lParams);
                break;
            case MotionEvent.ACTION_UP:
                int xDiff = abs(piece.xCoord - lParams.leftMargin);
                int yDiff = abs(piece.yCoord - lParams.topMargin);
                if (xDiff <= tolerance && yDiff <= tolerance) {
                    lParams.leftMargin = piece.xCoord;
                    lParams.topMargin = piece.yCoord;
                    piece.setLayoutParams(lParams);
                    piece.canMove = false;
                    sendViewToBack(piece);
                    playSound(activity, R.raw.woohoo,
                            PLAY_SOUND, false);
                    activity.checkGameOver();

                }
                break;
        }


        return true;
    }

    public void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    public static void playSound(Context context, int soundResId,
                                 int soundType, final boolean needLoop) {
        AssetFileDescriptor soundFileDescriptor = null;
        if (context != null) {
            try {
                if (sPlayer == null) {
                    sPlayer = new MediaPlayer();
                }
                if (soundResId != 0) {
                    sPlaySoundDescriptor = context.getResources()
                            .openRawResourceFd(soundResId);
                }
                if (sApplauseSoundDescriptor == null) {
                    sApplauseSoundDescriptor = context.getResources()
                            .openRawResourceFd(R.raw.applause);
                }

                switch (soundType) {
                    case PLAY_SOUND:
                        soundFileDescriptor = sPlaySoundDescriptor;
                        break;
                    case APPLAUSE_SOUND:
                        soundFileDescriptor = sApplauseSoundDescriptor;
                        break;
                }
                sPlayer.reset();
                sPlayer.setDataSource(soundFileDescriptor.getFileDescriptor(),
                        soundFileDescriptor.getStartOffset(),
                        soundFileDescriptor.getDeclaredLength());
                sPlayer.prepare();
                sPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer player) {
                        player.seekTo(0);
                        player.start();
                    }
                });

                sPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer player) {
                        if (needLoop) {
                            player.seekTo(0);
                            player.start();
                        }
                    }
                });
            } catch (Exception e) {
            }
        }
    }
}