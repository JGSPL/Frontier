package com.procialize.mrgeApp20.Utility;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;

import androidx.multidex.MultiDex;


import com.procialize.mrgeApp20.GetterSetter.LivePollOptionList;
import com.procialize.mrgeApp20.GetterSetter.QuizOptionList;
import com.procialize.mrgeApp20.MainActivity;

import java.util.ArrayList;



//import android.support.text.emoji.EmojiCompat;
//import android.support.text.emoji.FontRequestEmojiCompatConfig;
//import android.support.text.emoji.bundled.BundledEmojiCompatConfig;

public class MyApplication extends Application {

    /*
     * private BeaconManager beaconManager;
     *
     * @Override public void onCreate() { super.onCreate();
     *
     * beaconManager = new BeaconManager(getApplicationContext()); }
     */

    //private static final String TAG = "BeaconReferenceApplication";

    private static final String TAG = "EmojiCompatApplication";
    /**
     * Change this to {@code false} when you want to use the downloadable Emoji font.
     */
    private static final boolean USE_BUNDLED_EMOJI = true;
    private static Context mContext;
    int likeFlag = 0;
    private MainActivity mMonitoringActivity;
    private String editImage = "";
    private String feedbackComment = "";
    private int totalStar = 0;
    private String attendeeId = "";
    private int ratedFlag = 0;
    private String postImagePath = "";
    private ArrayList<Integer> likeList = new ArrayList<Integer>();
    private ArrayList<QuizOptionList> quizOptionList = new ArrayList<QuizOptionList>();
    private ArrayList<LivePollOptionList> pollOptionList = new ArrayList<LivePollOptionList>();
    private String SpeakerId;
    private String speakerName;
    private String eventId = "";

    public static Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context mContext) {
        MyApplication.mContext = mContext;
    }

    public static Context getContext() {
        return mContext;
    }

    public MainActivity getmMonitoringActivity() {
        return mMonitoringActivity;
    }

    public void setmMonitoringActivity(MainActivity mMonitoringActivity) {
        this.mMonitoringActivity = mMonitoringActivity;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        setmContext(this);

//        Intercom.initialize(this, "android_sdk-fdc30b4ec446bfd61b996a485f02ae47a5ae9a26", "tlnk2xgd");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Context context = MyApplication.this.getApplicationContext();
        Intent errorActivity = new Intent("com.beacon.activity");// this has to
        // match
        // your
        // intent
        // filter
        errorActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 22, errorActivity, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
//        final EmojiCompat.Config config;
//        if (USE_BUNDLED_EMOJI) {
//            // Use the bundled font for EmojiCompat
//            config = new BundledEmojiCompatConfig(getApplicationContext());
//        } else {
//            // Use a downloadable font for EmojiCompat
//            final FontRequest fontRequest = new FontRequest(
//                    "com.google.android.gms.fonts",
//                    "com.google.android.gms",
//                    "Noto Color Emoji Compat",
//                    R.array.com_google_android_gms_fonts_certs);
//            config = new FontRequestEmojiCompatConfig(getApplicationContext(), fontRequest)
//                    .setReplaceAll(true)
//                    .registerInitCallback(new EmojiCompat.InitCallback() {
//                        @Override
//                        public void onInitialized() {
//                            Log.i(TAG, "EmojiCompat initialized");
//                        }
//
//                        @Override
//                        public void onFailed(@Nullable Throwable throwable) {
//                            Log.e(TAG, "EmojiCompat initialization failed", throwable);
//                        }
//                    });
//        }
//        EmojiCompat.init(config);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public int getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(int likeFlag) {
        this.likeFlag = likeFlag;
    }

    public String getEditImage() {
        return editImage;
    }

    public void setEditImage(String editImage) {
        this.editImage = editImage;
    }

    public String getFeedbackComment() {
        return feedbackComment;
    }

    public void setFeedbackComment(String feedbackComment) {
        this.feedbackComment = feedbackComment;
    }

    public int getTotalStar() {
        return totalStar;
    }

    public void setTotalStar(int totalStar) {
        this.totalStar = totalStar;
    }

    public int getRatedFlag() {
        return ratedFlag;
    }

    public void setRatedFlag(int ratedFlag) {
        this.ratedFlag = ratedFlag;
    }

    public String getPostImagePath() {
        return postImagePath;
    }

    public void setPostImagePath(String postImagePath) {
        this.postImagePath = postImagePath;
    }

    public ArrayList<Integer> getLikeList() {
        return likeList;
    }

    public void setLikeList(ArrayList<Integer> likeList) {
        this.likeList = likeList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public ArrayList<QuizOptionList> getQuizOptionList() {
        return quizOptionList;
    }

    public void setQuizOptionList(ArrayList<QuizOptionList> quizOptionList) {
        this.quizOptionList = quizOptionList;
    }

    public ArrayList<LivePollOptionList> getPollOptionList() {
        return pollOptionList;
    }

    public void setPollOptionList(ArrayList<LivePollOptionList> pollOptionList) {
        this.pollOptionList = pollOptionList;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public String getSpeakerId() {
        return SpeakerId;
    }

    public void setSpeakerId(String speakerId) {
        SpeakerId = speakerId;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }


}
