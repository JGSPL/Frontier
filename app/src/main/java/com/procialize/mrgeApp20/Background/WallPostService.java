package com.procialize.mrgeApp20.Background;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.FetchFeed;
import com.procialize.mrgeApp20.GetterSetter.NewsFeedList;
import com.procialize.mrgeApp20.GetterSetter.news_feed_media;
import com.procialize.mrgeApp20.Session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.ApiConstant.ApiConstant.pageSize;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_NEWSFEED_PATH;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_NEWSFEED_PROFILE_PATH;
import static com.procialize.mrgeApp20.Session.SessionManager.MY_PREFS_NAME;

public class WallPostService extends IntentService {

    Bitmap bitmap_image;
    List<news_feed_media> news_feed_mediaDB = new ArrayList<news_feed_media>();
    String token;
    HashMap<String, String> user;
    SessionManager sessionManager;
    String eventid,colorActive;
    private List<NewsFeedList> newsfeedList = new ArrayList<>();
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private APIService mAPIService;
    private List<NewsFeedList> newsfeedsDBList;
    String MY_PREFS_NAME = "ProcializeInfo";
    public WallPostService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        cd = new ConnectionDetector(getApplicationContext());
        procializeDB = new DBHelper(getApplicationContext());
        db = procializeDB.getWritableDatabase();
        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();
        token = user.get(SessionManager.KEY_TOKEN);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        if (token != null && !token.equals("")) {
            if (cd.isConnectingToInternet()) {
                fetchFeed(token, eventid);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent(ApiConstant.BROADCAST_ACTION_BUZZ_FEED);
        // Attaching data to the intent
        // broadcastIntent.putExtra(Constants.EXTRA_CAPITAL, capital);
        // Sending the broadcast
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
    }

    public void fetchFeed(String token, String eventid) {
        procializeDB.getReadableDatabase();

        mAPIService.FeedFetchPost(token, eventid,"1",pageSize).enqueue(new Callback<FetchFeed>() {
            @Override
            public void onResponse(Call<FetchFeed> call, Response<FetchFeed> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        sessionManager.logoutUser();
                    } else {
                        showResponse(response);
                    }
                }
            }

            @Override
            public void onFailure(Call<FetchFeed> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
            }
        });
    }

    public void showResponse(Response<FetchFeed> response) {

        try {
            Log.d("Newseed", response.toString());
            newsfeedList = response.body().getNewsFeedList();
            procializeDB.clearNewsFeedTable();
            procializeDB.clearBuzzMediaFeedTable();
            procializeDB.insertNEwsFeedInfo(response.body().getNewsFeedList(), db);
            newsfeedsDBList = response.body().getNewsFeedList();

            String newsFeedUrlPath = response.body().getNews_feed_url_path();
            String profilePicUrlPath = response.body().getProfile_pic_url_path();

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_NEWSFEED_PATH, newsFeedUrlPath);
            editor.putString(KEY_NEWSFEED_PROFILE_PATH, profilePicUrlPath);
            editor.commit();

            SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String newsFeedPath = prefs1.getString(KEY_NEWSFEED_PATH, "");
            String newsFeedProfilePath = prefs1.getString(KEY_NEWSFEED_PROFILE_PATH, "");

            if (newsfeedsDBList != null) {
                for (int i = 0; i < newsfeedsDBList.size(); i++) {
                    news_feed_mediaDB = new ArrayList<>();
                    if (newsfeedsDBList.get(i).getNews_feed_media().size() > 0) {
                        for (int j = 0; j < newsfeedsDBList.get(i).getNews_feed_media().size(); j++) {
                            news_feed_media nb_media = new news_feed_media();

                            nb_media.setNews_feed_id(newsfeedsDBList.get(i).getNews_feed_media().get(j).getNews_feed_id());
                            nb_media.setMedia_type(newsfeedsDBList.get(i).getNews_feed_media().get(j).getMedia_type());
                            nb_media.setMediaFile(newsfeedsDBList.get(i).getNews_feed_media().get(j).getMediaFile());
                            nb_media.setThumb_image(newsfeedsDBList.get(i).getNews_feed_media().get(j).getThumb_image());
                            nb_media.setWidth(newsfeedsDBList.get(i).getNews_feed_media().get(j).getWidth());
                            nb_media.setHeight(newsfeedsDBList.get(i).getNews_feed_media().get(j).getHeight());

                            ImageView imageView = new ImageView(getApplicationContext());
                            if (newsfeedsDBList.get(i).getNews_feed_media().get(j).getMedia_type().equalsIgnoreCase("image")) {
                                ImageLoader imageLoader = new ImageLoader(getApplicationContext());
                                imageLoader.DisplayImage(newsFeedPath/*ApiConstant.newsfeedwall*/ + newsfeedsDBList.get(i).getNews_feed_media().get(j).getMediaFile(),imageView);
                               /* Bitmap original = getBitmapFromURL(ApiConstant.newsfeedwall + newsfeedsDBList.get(i).getNews_feed_media().get(j).getMediaFile());
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                original.compress(Bitmap.CompressFormat.PNG, 30, out);
                                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                                bitmap_image = decoded;*/
                            } else {
                                ImageLoader imageLoader = new ImageLoader(getApplicationContext());
                                imageLoader.DisplayImage(newsFeedPath/*ApiConstant.newsfeedwall*/ + newsfeedsDBList.get(i).getNews_feed_media().get(j).getThumb_image(),imageView);
                               /* Bitmap original = getBitmapFromURL(ApiConstant.newsfeedwall + newsfeedsDBList.get(i).getNews_feed_media().get(j).getThumb_image());
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                original.compress(Bitmap.CompressFormat.PNG, 30, out);
                                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                                bitmap_image = decoded;*/
                            }

                            /*if (bitmap_image != null) {
                                byte[] data = getBitmapAsByteArray(bitmap_image); // this is a function
                                nb_media.setMedia_image(data);
                            }*/
                            news_feed_mediaDB.add(nb_media);
                        }
                        procializeDB.insertBuzzMediaInfo(news_feed_mediaDB, db);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}