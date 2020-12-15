package com.procialize.frontier.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.DbHelper.ConnectionDetector;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PuzzleActivity extends AppCompatActivity implements View.OnTouchListener {
    private String accessToken, event_id;
    String MY_PREFS_NAME = "ProcializeInfo";

    private final static int PLAY_SOUND = 0;
    private final static int APPLAUSE_SOUND = 1;
    private static MediaPlayer sPlayer;
    private static AssetFileDescriptor sPlaySoundDescriptor;
    private static AssetFileDescriptor sApplauseSoundDescriptor;
    SessionManager sessionManager;
    long time;
    //  RelativeLayout
    //@BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    //@BindView(R.id.iv_original_image)
    ImageView ivOriginalImage;
    // @BindView(R.id.iv_part_1)
    ImageView ivPart1;
    // @BindView(R.id.iv_part_2)
    ImageView ivPart2;
    int count = 0;
    //@BindView(R.id.iv_part_3)
    ImageView ivPart3;
    // @BindView(R.id.iv_part_4)
    ImageView ivPart4;
    // @BindView(R.id.iv_part_5)
    ImageView ivPart5;
    // @BindView(R.id.iv_part_6)
    ImageView ivPart6;
    // @BindView(R.id.iv_part_7)
    ImageView ivPart7;
    // @BindView(R.id.iv_part_8)
    ImageView ivPart8;
    // @BindView(R.id.iv_part_9)
    ImageView ivPart9;
    //    @BindView(R.id.iv_part_10)
    ImageView ivPart10;
    //    @BindView(R.id.iv_part_11)
    ImageView ivPart11;
    //    @BindView(R.id.iv_part_12)
    ImageView ivPart12;
    //    @BindView(R.id.iv_puzzle_result_image)
    ImageView ivResultImage;
    //    @BindView(R.id.timer_view)
    TextView timerValue;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private int[] deltaX, deltaY;
    private Rect[] maskRect = new Rect[12];
    private Point[] points = new Point[12];
    private Bitmap original;
    private SoundPool mSoundPool;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int hours = mins / 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText(
                    String.format("%02d", mins) + ":"
                            + String.format("%02d", secs)
            );
            customHandler.postDelayed(this, 0);
        }

    };

    /**
     * This method used to play a sound.
     *
     * @param context
     * @param soundType
     * @param needLoop
     */

    String sperate_url = "";


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

    /**
     * Method to stop the sound.
     */
    public static void stopSound() {
        if (sPlayer != null) {
            sPlayer.reset();
        }
    }

    String image_name = "";

    private ApiConstant constant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);


        //ButterKnife.bind(this);

        rlRoot = findViewById(R.id.rl_root);
        ivOriginalImage = findViewById(R.id.iv_original_image);
        ivPart1 = findViewById(R.id.iv_part_1);
        ivPart2 = findViewById(R.id.iv_part_2);
        ivPart3 = findViewById(R.id.iv_part_3);
        ivPart4 = findViewById(R.id.iv_part_4);
        ivPart5 = findViewById(R.id.iv_part_5);
        ivPart6 = findViewById(R.id.iv_part_6);
        ivPart7 = findViewById(R.id.iv_part_7);
        ivPart8 = findViewById(R.id.iv_part_8);
        ivPart9 = findViewById(R.id.iv_part_9);
        ivPart10 = findViewById(R.id.iv_part_10);
        ivPart11 = findViewById(R.id.iv_part_11);
        ivPart12 = findViewById(R.id.iv_part_12);
        ivResultImage = findViewById(R.id.iv_puzzle_result_image);
        timerValue = findViewById(R.id.timer_view);

        rlRoot.setOnTouchListener(this);
        ivOriginalImage.setOnTouchListener(this);
        ivPart1.setOnTouchListener(this);
        ivPart2.setOnTouchListener(this);
        ivPart3.setOnTouchListener(this);
        ivPart4.setOnTouchListener(this);
        ivPart5.setOnTouchListener(this);
        ivPart6.setOnTouchListener(this);
        ivPart7.setOnTouchListener(this);
        ivPart8.setOnTouchListener(this);
        ivPart9.setOnTouchListener(this);
        ivPart10.setOnTouchListener(this);
        ivPart11.setOnTouchListener(this);
        ivPart12.setOnTouchListener(this);
        ivResultImage.setOnTouchListener(this);
        timerValue.setOnTouchListener(this);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        accessToken = user.get(SessionManager.KEY_TOKEN);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        event_id = prefs.getString("eventid", "1");

        image_name = getIntent().getExtras().getString("image_name");

        constant = new ApiConstant();

        sperate_url = "";//constant.baseUrl + constant.WEBSERVICE_FOLDER + constant.JIGSAW_PUZZLE;

        BitmapFactory.Options options = new BitmapFactory.Options();
        //int imageResource = getIntent().getIntExtra("original_image", R.drawable.contest_data);
        int imageResource = getIntent().getIntExtra("original_image", R.drawable.contest_data);


        original = BitmapFactory.decodeResource(getResources(),
                imageResource, options);
        ivOriginalImage.setImageResource(imageResource);
        ivResultImage.setImageResource(imageResource);
        original = Bitmap.createScaledBitmap(original, getPix(320), getPix(240)
                , false);
        deltaX = new int[12];
        deltaY = new int[12];

        int position = 0;
        Point screenSize = null;
        int initialX = 220;
        int initialY = 9;
        points[position] = new Point(getPix(initialX), getPix(initialY));
        maskRect[position] = new Rect(getPix(2), getPix(2), getPix(114), getPix(105));

        position = 1;
        points[position] = new Point(getPix(initialX + 64), points[0].y);
        maskRect[position] = new Rect(getPix(2), getPix(107), getPix(114),
                getPix(182));

        position = 2;
        points[position] = new Point(getPix(initialX + 126), points[1].y);
        maskRect[position] = new Rect(getPix(116), getPix(107), getPix(228),
                getPix(210));

        position = 3;
        points[position] = new Point(getPix(initialX + 184), points[2].y);
        maskRect[position] = new Rect(getPix(230), getPix(107), getPix(312),
                getPix(210));

        position = 4;
        points[position] = new Point(getPix(initialX + 2), getPix(initialY + 58));
        maskRect[position] = new Rect(getPix(314), getPix(107), getPix(396),
                getPix(192));

        position = 5;
        points[position] = new Point(getPix(initialX + 36), getPix(initialY + 36));
        maskRect[position] = new Rect(getPix(398), getPix(107), getPix(510),
                getPix(238));

        position = 6;
        points[position] = new Point(getPix(initialX + 103), getPix(initialY + 58));
        maskRect[position] = new Rect(getPix(2), getPix(240), getPix(145),
                getPix(315));

        position = 7;
        points[position] = new Point(getPix(initialX + 188), getPix(initialY + 58));
        maskRect[position] = new Rect(getPix(147), getPix(240), getPix(229),
                getPix(315));

        position = 8;
        points[position] = new Point(getPix(initialX + 2), getPix(initialY + 94));
        maskRect[position] = new Rect(getPix(231), getPix(240), getPix(343),
                getPix(343));

        position = 9;
        points[position] = new Point(getPix(initialX + 64), getPix(initialY + 116));
        maskRect[position] = new Rect(getPix(116), getPix(2), getPix(228),
                getPix(77));

        position = 10;
        points[position] = new Point(getPix(initialX + 126), getPix(initialY + 94));
        maskRect[position] = new Rect(getPix(230), getPix(2), getPix(342),
                getPix(105));

        position = 11;
        points[position] = new Point(getPix(initialX + 188), getPix(initialY + 94));
        maskRect[position] = new Rect(getPix(344), getPix(2), getPix(426),
                getPix(105));

        generateOutput(ivPart1, 0);
        generateOutput(ivPart2, 1);
        generateOutput(ivPart3, 2);
        generateOutput(ivPart4, 3);
        generateOutput(ivPart5, 4);
        generateOutput(ivPart6, 5);
        generateOutput(ivPart7, 6);
        generateOutput(ivPart8, 7);
        generateOutput(ivPart9, 8);
        generateOutput(ivPart10, 9);
        generateOutput(ivPart11, 10);
        generateOutput(ivPart12, 11);
        time = 0;
        startTime = SystemClock.uptimeMillis();
    }

    /* @OnTouch({R.id.iv_part_1, R.id.iv_part_2, R.id.iv_part_3, R.id.iv_part_4,
             R.id.iv_part_5, R.id.iv_part_6, R.id.iv_part_7, R.id.iv_part_8, R.id.iv_part_9,
             R.id.iv_part_10, R.id.iv_part_11, R.id.iv_part_12})*/
    public boolean onTouch(View v, MotionEvent event) {
        int position = 0;
        switch (v.getId()) {
            case R.id.iv_part_1:
                position = 0;
                break;
            case R.id.iv_part_2:
                position = 1;
                break;
            case R.id.iv_part_3:
                position = 2;
                break;
            case R.id.iv_part_4:
                position = 3;
                break;
            case R.id.iv_part_5:
                position = 4;
                break;
            case R.id.iv_part_6:
                position = 5;
                break;
            case R.id.iv_part_7:
                position = 6;
                break;
            case R.id.iv_part_8:
                position = 7;
                break;
            case R.id.iv_part_9:
                position = 8;
                break;
            case R.id.iv_part_10:
                position = 9;
                break;
            case R.id.iv_part_11:
                position = 10;
                break;
            case R.id.iv_part_12:
                position = 11;
                break;
        }
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                deltaX[position] = X - params.leftMargin;
                deltaY[position] = Y - params.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                lParams.leftMargin = X - deltaX[position];
                lParams.topMargin = Y - deltaY[position];
                v.setLayoutParams(lParams);
                checkCollision(v, position);
                rlRoot.invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    @Override
    protected void onPause() {
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        super.onPause();
    }

    private void checkCollision(View view, int position) {
        Rect rect2 = new Rect(view.getLeft(), view.getTop(), view.getLeft() + getPix(10)
                , view.getTop() + getPix(10));
        int extraSpace = getPix(25);
        int tileWidth = getPix(82);
        int tileHeight = getPix(80);
        int col = position % 4;
        int row = position / 4;
        int xSrc = points[0].x + (position % 4 * tileWidth);
        int ySrc = points[0].y + (position / 4 * tileHeight);
        if (position == 5 || position == 8 || position == 10 || position == 11) {
            ySrc -= (extraSpace);
        }
        if (position == 5 || position == 6) {
            xSrc -= (extraSpace);
        }
        Rect parentRect = new Rect(xSrc, ySrc, xSrc + getPix(10), ySrc + getPix(10));
        if (parentRect.intersect(rect2)) {
            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                    view.getLayoutParams();
            lParams.leftMargin = xSrc - getPix(col * 3) - getPix(2);
            lParams.topMargin = ySrc - getPix(row * 5);
            view.setLayoutParams(lParams);

            view.setOnTouchListener(null);
            rlRoot.invalidate();
            count++;
            if (count == 11) {
                playSound(this, R.raw.woohoo,
                        PLAY_SOUND, false);
            } else if (count == 12) {
                Toast.makeText(this, "Congrats You completed the game. in " + (updatedTime / 1000)
                        + " seconds", Toast.LENGTH_SHORT).show();
                //ivOriginalImage.setVisibility(View.GONE);
                ivResultImage.setVisibility(View.VISIBLE);
                playSound(this, R.raw.congrats,
                        APPLAUSE_SOUND, false);
                customHandler.removeCallbacks(updateTimerThread);
                // submitScore();


                ConnectionDetector cd;


                cd = new ConnectionDetector(PuzzleActivity.this);


                if (cd.isConnectingToInternet()) {

                    new sendScore().execute();

                } else {
                    Toast.makeText(getBaseContext(), "No Internet Connection",
                            Toast.LENGTH_SHORT).show();
                }


            } else {
                playSound(this, R.raw.tf_notification,
                        PLAY_SOUND, false);
            }
        }
    }

    private void generateOutput(ImageView imageView, int position) {
        Bitmap smallBitmap = Bitmap.createBitmap(original);
        int extraSpace = getPix(30);
        int tileWidth = getPix(82);
        int tileHeight = getPix(80);
        int col = position % 4;
        int xSrc = 2 + (position % 4 * tileWidth);
        int ySrc = 2 + (position / 4 * tileHeight);
        Rect srcRect = new Rect(xSrc - (position == 5 || position == 6 ? extraSpace : 0), ySrc
                - (position == 5 || position == 8 || position == 11 || position == 10 ? extraSpace : 0)
                , xSrc + tileWidth + (col == 3 || position == 4 || position == 5 ? 0 : extraSpace),
                ySrc + tileHeight
                        + (position == 0 || position == 2 || position == 3 || position == 5 ? extraSpace : 0)
        );
        Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.pieces_4x3);
        try {
            mask = Bitmap.createBitmap(mask, maskRect[position].left, maskRect[position].top,
                    maskRect[position].width(), maskRect[position].height());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(smallBitmap, srcRect, new Rect(0, 0, srcRect.width(),
                srcRect.height()), null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        imageView.setImageBitmap(result);

        Random r = new Random();
        int x = r.nextInt(220);
        int y = r.nextInt(300 - 100) + 100;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.setMargins(getPix(x), getPix(y), 0, 0);
        imageView.setLayoutParams(params);
    }

    public int getPix(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopSound();
    }

/*    private void submitScore() {


        RestClient.getInstance().getWebServices().submitScore(event_id,
                accessToken, image_name, (int) (updatedTime / 1000))
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("success")) {
                                Toast.makeText(PuzzleActivity.this, "Score submitted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PuzzleActivity.this, response.body().getMsg(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PuzzleActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        }


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PuzzleActivity.this.finish();
                            }
                        }, 1500);
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Toast.makeText(PuzzleActivity.this, t.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                        *//*PuzzleActivity.this.finish();*//*
                    }
                });
    }*/


    private class sendScore extends AsyncTask<Void, Void, Void> {

        JSONObject jsonObj = null;


        String status = "", msg = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Setting Thread Priority
            android.os.Process
                    .setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            android.os.Process
                    .setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            nameValuePair.add(new BasicNameValuePair("api_access_token",
                    accessToken));
            nameValuePair.add(new BasicNameValuePair("event_id", event_id));
            nameValuePair.add(new BasicNameValuePair("image_name", image_name));

            nameValuePair.add(new BasicNameValuePair("solved_time", String.valueOf(updatedTime / 1000)));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(sperate_url,
                    ServiceHandler.POST, nameValuePair);

            Log.i("android", "response is " + jsonStr);


            if (jsonStr != null) {
                try {
                    jsonObj = new JSONObject(jsonStr);


                    status = jsonObj.getString("status");

                    msg = jsonObj.getString("msg");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (status.equals("success")) {
                Toast.makeText(PuzzleActivity.this, "Score submitted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PuzzleActivity.this, msg,
                        Toast.LENGTH_SHORT).show();
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    PuzzleActivity.this.finish();
                }
            }, 1500);

        }
    }


}
