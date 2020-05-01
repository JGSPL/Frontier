package com.procialize.mrgeApp20.Background;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.NewsFeedPostMultimedia;
import com.procialize.mrgeApp20.GetterSetter.news_feed_media;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.procialize.mrgeApp20.Activity.PostActivity.transformResponse;
import static com.procialize.mrgeApp20.UnsafeOkHttpClient.getUnsafeOkHttpClient;


public class BackgroundService extends IntentService {

    public static boolean isIntentServiceRunning;
    // private NotificationManager nm;
    private final Calendar time = Calendar.getInstance();
    InputStream is = null;
    JSONObject json = null;
    JSONObject statusResponse;
    String message = "";
    String error = "";
    String msg = "";
    String result = "";
    File file1;
    int successfullUploadedMediaCount = 0;
    ArrayList<Integer> videoPositionArray = new ArrayList<Integer>();
    int startMsForVideoCutting = 0;//Start time for cutting video
    int endMsForVideoCutting = 120000;//End TIme for cutting video
    String TAG = "BackgroundService";
    Thread thread;
    List<news_feed_media> news_feed_mediaDB = new ArrayList<news_feed_media>();
    String is_completed = "0";
    String news_feed_id1="";
    private String media_file, media_file_thumb, api_access_token, event_id, status;
    private DBHelper dbHelper;
    private ArrayList<NewsFeedPostMultimedia> arrayListNewsFeedMultiMedia;
    private FFmpeg ffmpeg;
    private DBHelper procializeDB;
    private SQLiteDatabase db;


    public BackgroundService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        loadFFMpegBinary();
        procializeDB = new DBHelper(getApplicationContext());
        db = procializeDB.getWritableDatabase();

        arrayListNewsFeedMultiMedia = ( ArrayList<NewsFeedPostMultimedia> ) intent.getSerializableExtra("arrayListNewsFeedMultiMedia");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
       /* media_file = intent.getExtras().getString("media_file", "");
        media_file_thumb = intent.getExtras().getString("media_file_thumb", "");*/
        api_access_token = intent.getExtras().getString("api_access_token", "");
        event_id = intent.getExtras().getString("event_id", "");
        status = intent.getExtras().getString("status", "");

        dbHelper = new DBHelper(getApplicationContext());
        //doFileUpload();

        if (arrayListNewsFeedMultiMedia.size() > 0) {

            for (int j = 0; j < arrayListNewsFeedMultiMedia.size(); j++) {
                if (arrayListNewsFeedMultiMedia.get(j).getMedia_type().equalsIgnoreCase("video")) {
                    videoPositionArray.add(j);
                }
            }

            try {


                if (videoPositionArray.size() > 0) {
                    if (arrayListNewsFeedMultiMedia.get(videoPositionArray.get(0)).getMedia_type().equalsIgnoreCase("video")) {
                        String strPath = arrayListNewsFeedMultiMedia.get(videoPositionArray.get(0)).getMedia_file();
                        executeCutVideoCommand(startMsForVideoCutting, endMsForVideoCutting, Uri.parse(strPath), videoPositionArray.get(0));
                    } else {
                        media_file = arrayListNewsFeedMultiMedia.get(0).getMedia_file();
                        media_file_thumb = arrayListNewsFeedMultiMedia.get(0).getMedia_file_thumb();
                        uploadToServer(media_file, media_file_thumb, arrayListNewsFeedMultiMedia.get(0).getFolderUniqueId());
                    }
                } else {
                    media_file = arrayListNewsFeedMultiMedia.get(0).getMedia_file();
                    media_file_thumb = arrayListNewsFeedMultiMedia.get(0).getMedia_file_thumb();
                    uploadToServer(media_file, media_file_thumb, arrayListNewsFeedMultiMedia.get(0).getFolderUniqueId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Command for cutting video
     */
    private String executeCutVideoCommand(int startMs, int endMs, Uri selectedVideoUri, int position) {
      /*  File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES
        );
*/
        String root = Environment.getExternalStorageDirectory().toString();
        File moviesDir = new File(root + "/TheWeddingApp/Video");

        if (!moviesDir.exists()) {
            moviesDir.mkdirs();
        }
        String path = selectedVideoUri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            path = path.substring(cut + 1);
        }
        /*String filePrefix = "cut_video";
        String fileExtn = ".mp4";
        //String yourRealPath = getPath(PostNewActivity.this, selectedVideoUri);
        File dest = new File(moviesDir, filePrefix + fileExtn);
        int fileNo = 0;
        while (dest.exists()) {
            fileNo++;
            dest = new File(moviesDir, filePrefix + fileNo + fileExtn);

        }*/
        String fileExtn = ".mp4";
        String filePrefix = path.replace(".mp4", "");
        File dest = new File(moviesDir, filePrefix + fileExtn);


        int fileNo = 0;
        while (dest.exists()) {
            fileNo++;
            dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
        }
        //fileNo++;
        // File dest = new File(moviesDir, filePrefix + fileExtn);
        //Log.d(TAG, "startTrim: src: " + yourRealPath);
        Log.d(TAG, "startTrim: dest: " + dest.getAbsolutePath());
        Log.d(TAG, "startTrim: startMs: " + startMs);
        Log.d(TAG, "startTrim: endMs: " + endMs);
        String filePath = dest.getAbsolutePath();
       /* String[] complexCommand = {"-ss", "" + startMs / 1000,
                "-y", "-i", selectedVideoUri.toString(),
                "-preset", "ultrafast",
                "-t", "" + (endMs - startMs) / 1000,
                "-vcodec", "mpeg4",
                "-b:v", "2097152",
                "-b:a", "48000",
                "-ac", "2",
                "-ar", "22050",
                "-preset", "ultrafast",
                "-crf", "23",
                "-c:a", "copy",
                "-fs", "17000000",
                filePath,
                "-b", "512k"};*/

        String[] complexCommand = {
                "-y", "-i", selectedVideoUri.toString(),
                /*"-ss", "" + startMs / 1000,
                 "-t", "" + (endMs - startMs) / 1000,*/
                "-ss", "" + startMs / 1000,
                "-t", "" + (endMs - startMs) / 1000,
                "-vcodec", "mpeg4",
                "-b:v", "2097152",
                "-b:a", "48000",
                "-ac", "2",
                "-ar", "22050",
                "-preset", "ultrafast",
                "-crf", "51",
                // "-fs", "17000000",
                "-c:a", "copy",
                filePath,
                "-b", "512k"};

        execFFmpegBinary(complexCommand, filePath);

        return filePath;
    }

    /**
     * Executing ffmpeg binary
     */
    private String execFFmpegBinary(final String[] command, final String outputPath) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.d(TAG, "FAILED with output : " + s);
                }

                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "SUCCESS with output : " + s);
                    //progressDialog.setMessage("progress : " + s);
                    if (videoPositionArray.size() != 0) {
                        int pos = videoPositionArray.get(0);
                        String thumbPath = arrayListNewsFeedMultiMedia.get(pos).getMedia_file_thumb();
                        String originalPath = arrayListNewsFeedMultiMedia.get(pos).getMedia_file();

                        if (arrayListNewsFeedMultiMedia.get(pos).getMedia_type().equals("video")) {
                            arrayListNewsFeedMultiMedia.get(pos).setCompressedPath(outputPath);
                            dbHelper.getReadableDatabase();
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            dbHelper.updateMultimediaPath(originalPath, outputPath, arrayListNewsFeedMultiMedia.get(pos).getNews_feed_id(), db);

                            //resultList.set(pos, new SelectedImages(originalPath, outputPath, thumbPath, true));
                            videoPositionArray.remove(0);

                            if (videoPositionArray.size() != 0) {
                                executeCutVideoCommand(startMsForVideoCutting, endMsForVideoCutting, Uri.parse(arrayListNewsFeedMultiMedia.get(videoPositionArray.get(0)).getMedia_file()), videoPositionArray.get(0));
                            } else {
                                if (arrayListNewsFeedMultiMedia.get(0).getMedia_type().equalsIgnoreCase("video"))
                                    media_file = arrayListNewsFeedMultiMedia.get(0).getCompressedPath();
                                else
                                    media_file = arrayListNewsFeedMultiMedia.get(0).getMedia_file();



                                media_file_thumb = arrayListNewsFeedMultiMedia.get(0).getMedia_file_thumb();
                            }
                        } else {
                            media_file = arrayListNewsFeedMultiMedia.get(0).getMedia_file();
                            media_file_thumb = arrayListNewsFeedMultiMedia.get(0).getMedia_file_thumb();
                        }
                        thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    uploadToServer(media_file, media_file_thumb, arrayListNewsFeedMultiMedia.get(0).getFolderUniqueId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();

                    }
                }

                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                    //progressDialog.setMessage("progress : " + s);
                    Log.d(TAG, "progress : " + s);
                }

                @Override
                public void onStart() {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                    // progressDialog.setMessage("Processing...");
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command : ffmpeg " + command);
                    if (videoPositionArray.size() == 0) {
                    }
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
        return outputPath;
    }

    /**
     * Load FFmpeg binary
     */
    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                Log.d(TAG, "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(this);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    //showUnsupportedExceptionDialog();
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            //showUnsupportedExceptionDialog();
        } catch (Exception e) {
            Log.d(TAG, "EXception no controlada : " + e);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
       /* Toast.makeText(this, "Service created at " + time.getTime(),
                Toast.LENGTH_LONG).show();*/
        // showNotification();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isIntentServiceRunning = false;
        if (dbHelper.getCountOfNotUploadedMultiMedia() == 0) {
            File dir = new File(Environment.getExternalStorageDirectory() + "AlbumCache");
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        }
        // Cancel the persistent notification.
        // nm.cancel(R.string.service_started);
       /* Toast.makeText(this,
                media_file + " Service destroyed at " + time.getTime() + ";",
                Toast.LENGTH_LONG).show();*/


       /* Intent broadcastIntent = new Intent(Constants.BROADCAST_ACTION);
        // Attaching data to the intent
        //broadcastIntent.putExtra(Constants.EXTRA_CAPITAL, capital);
        // Sending the broadcast
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);*/


    }

    public void uploadToServer(String media_file, String media_file_thumb, String folderUniqueId) {

        if (media_file.contains("jpg") || media_file.contains("jpeg") || media_file.contains("png")) {
            try {
                Uri myUri = Uri.parse(media_file);

                File auxFile = new File(myUri.toString());
                ExifInterface exif = new ExifInterface(auxFile.getPath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                int angle = 0;

                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    angle = 90;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    angle = 180;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    angle = 270;
                }

                Matrix mat = new Matrix();
                mat.postRotate(angle);

                Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(auxFile), null, null);
                Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                correctBmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
                File f1 = new File(media_file);
                f1.createNewFile();
                FileOutputStream fo = new FileOutputStream(auxFile);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                Log.w("TAG", "-- Error in setting image");
            } catch (OutOfMemoryError oom) {
                Log.w("TAG", "-- OOM Error in setting image");
            }
        }
        //int countOfUploadedFilesForPerticularNewsFeedId = dbHelper.getCountOfUploadedMultiMediaForNewsFeedId(news_feed_id);
        int countOfUploadedFilesForPerticularNewsFeedId = dbHelper.getCountOfUploadedMultiMediaForFolderUniqueId(folderUniqueId);
        String news_feed_id = dbHelper.getNewsFeedIdFromFolderUniqueId(folderUniqueId);
        if (countOfUploadedFilesForPerticularNewsFeedId == 1) {
            is_completed = "1";
        } else {
            is_completed = "0";
        }

        File file = new File(media_file);
        if (media_file.contains("mp4")) {
            MediaMetadataRetriever m = new MediaMetadataRetriever();

            m.setDataSource(file.getAbsolutePath());

            Bitmap bit = m.getFrameAtTime();

            String filename = String.valueOf(System.currentTimeMillis()) + ".png";
            File sd = Environment.getExternalStorageDirectory();
            File dest = new File(sd, filename);
            Matrix mat = new Matrix();
            mat.postRotate(90);

            try {
                FileOutputStream out = new FileOutputStream(dest);
                bit.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            file1 = new File(dest.getAbsolutePath());
        }
        //Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());
        MediaType MEDIA_TYPE_PNG = null;
        MediaType MEDIA_TYPE_THUMB = null;
        if (media_file.contains(".png") || media_file.contains(".jpg") || media_file.contains(".jpeg")) {
            MEDIA_TYPE_PNG = MediaType.parse("image/*");
        } else if (media_file.contains(".mp4")) {
            MEDIA_TYPE_PNG = MediaType.parse("video/*");
            MEDIA_TYPE_THUMB = MediaType.parse("image/*");
        }

        // String filename = path.substring(path.lastIndexOf("/") + 1);
        String extension = media_file.substring(media_file.lastIndexOf(".") + 1);

        Date date = new Date();
        //getTime() returns current time in milliseconds
        long time = date.getTime();
        String filename11 = String.valueOf(time);
        OkHttpClient client = null;
        try {
            URL url = new URL(ApiConstant.baseUrl + "PostNewsFeedMultiple");//"PostNewsFeedMultiple");
            HttpURLConnection conn = ( HttpURLConnection ) url.openConnection();
            conn.setConnectTimeout(7000);
            client = getUnsafeOkHttpClient().newBuilder().build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MultipartBody.Builder builder = new MultipartBody.Builder();
//                postFeed(type, token, eventid, status, body);
        builder.setType(MultipartBody.FORM);
        if (media_file != null && !(media_file.equalsIgnoreCase(""))) {
            builder.addFormDataPart("media_file", file.getName() + "." + extension, RequestBody.create(MEDIA_TYPE_PNG, file));
        }

        if (media_file.contains(".mp4")) {
            if (media_file_thumb != null && !(media_file_thumb.equalsIgnoreCase(""))) {
                builder.addFormDataPart("media_file_thumb", file.getName() + ".png", RequestBody.create(MEDIA_TYPE_THUMB, file1));
            }
        }
        builder.addFormDataPart("api_access_token", api_access_token);
        builder.addFormDataPart("event_id", event_id);

        if(news_feed_id.equalsIgnoreCase("false")||news_feed_id.equalsIgnoreCase("")){
            builder.addFormDataPart("news_feed_id", news_feed_id1);
        }else{
            builder.addFormDataPart("news_feed_id", news_feed_id);
        }

        builder.addFormDataPart("status", status);
        builder.addFormDataPart("is_completed", is_completed);
        // builder.addFormDataPart("isDebug", "1");

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(ApiConstant.baseUrl + "PostNewsFeedMultiple")
                .post(requestBody)
                .build();

        HttpEntity httpEntity = null;
        okhttp3.Response response = null;

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //res = response.body().string();

        if (response != null) {
            httpEntity = transformResponse(response).getEntity();
            try {
                result = EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            json = new JSONObject(result);
            Log.d("response", result);
            try {
                error = json.getString("status");
                message = json.getString("msg");

                news_feed_id1 = json.getString("news_feed_id");
                if (news_feed_id1 == null || news_feed_id1.equalsIgnoreCase("false")) {
                    news_feed_id1 = "";
                }

                if (error.equalsIgnoreCase("success")) {
                    dbHelper.getReadableDatabase();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    dbHelper.updateNewsFeedId(news_feed_id1, folderUniqueId, db);
                    dbHelper.updateMultimediaInfo(media_file, news_feed_id1, db, media_file_thumb, folderUniqueId);
                    message = json.getString("msg");
                    getFileToUpload();
                } else {
                    dbHelper.getReadableDatabase();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    dbHelper.updateNewsFeedId(news_feed_id1, folderUniqueId, db);
                    dbHelper.updateMultimediaInfo(media_file, news_feed_id1, db, media_file_thumb, folderUniqueId);
                    getFileToUpload();
                }
            } catch (Exception e) {
                if (news_feed_id1 == null || news_feed_id1.equalsIgnoreCase("false")) {
                    news_feed_id1 = "";
                }
                dbHelper.getReadableDatabase();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                dbHelper.updateNewsFeedId(news_feed_id1, folderUniqueId, db);
                dbHelper.updateMultimediaInfo(media_file, news_feed_id1, db, media_file_thumb, folderUniqueId);
                Log.e("JSON Parser", "Error parsing data " + e.toString());
                getFileToUpload();
            }
        } catch (JSONException e) {
            if (news_feed_id1 == null || news_feed_id1.equalsIgnoreCase("false")) {
                news_feed_id1 = "";
            }
            dbHelper.getReadableDatabase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.updateNewsFeedId(news_feed_id1, folderUniqueId, db);
            dbHelper.updateMultimediaInfo(media_file, news_feed_id1, db, media_file_thumb, folderUniqueId);
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            getFileToUpload();
        }
        //return json.toString();
    }

    private String getPostDataString(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public void getFileToUpload() {
        successfullUploadedMediaCount = successfullUploadedMediaCount + 1;
        if (arrayListNewsFeedMultiMedia.size() > successfullUploadedMediaCount) {
            String media_file1 = "";
            String media_file_thumb1 = arrayListNewsFeedMultiMedia.get(successfullUploadedMediaCount).getMedia_file_thumb();
            String news_feed_id = arrayListNewsFeedMultiMedia.get(successfullUploadedMediaCount).getNews_feed_id();
            String folderUniqueId = arrayListNewsFeedMultiMedia.get(successfullUploadedMediaCount).getFolderUniqueId();

            if (arrayListNewsFeedMultiMedia.get(successfullUploadedMediaCount).getMedia_type().equalsIgnoreCase("video"))
                media_file1 = arrayListNewsFeedMultiMedia.get(successfullUploadedMediaCount).getCompressedPath();
            else
                media_file1 = arrayListNewsFeedMultiMedia.get(successfullUploadedMediaCount).getMedia_file();

            uploadToServer(media_file1, media_file_thumb1, folderUniqueId);
        } else {
            // Creating an intent for broadcastreceiver
            Intent broadcastIntent = new Intent(ApiConstant.BROADCAST_ACTION);
            // Attaching data to the intent
            //broadcastIntent.putExtra(Constants.EXTRA_CAPITAL, capital);
            // Sending the broadcast
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
        }
    }
}