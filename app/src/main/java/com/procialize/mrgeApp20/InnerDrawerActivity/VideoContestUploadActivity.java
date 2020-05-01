package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.CustomTools.CircleDisplay;
import com.procialize.mrgeApp20.CustomTools.ProgressRequestBodyImage;
import com.procialize.mrgeApp20.CustomTools.ProgressRequestBodyVideo;
import com.procialize.mrgeApp20.GetterSetter.PostVideoSelfie;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.ScalingUtilities;
import com.procialize.mrgeApp20.Utility.Util;
import com.procialize.mrgeApp20.Utility.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoContestUploadActivity extends AppCompatActivity implements ProgressRequestBodyImage.UploadCallbacks, ProgressRequestBodyVideo.UploadCallbacks {

    private static final String SERVER_PATH = "";
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    public static File videoFile;
    Uri capturedImageUri;
    ImageView imgPlay;
    Button btnSubmit;
    File file = null;
    LinearLayout llData;
    SessionManager sessionManager;
    String apikey;
    TextInputEditText editTitle;
    APIService mAPIService;
    CircleDisplay progressbar;
    String userChoosenTask;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventId, colorActive;
    ImageView headerlogoIv;
    VideoView video_view;
    String videoUrl;
    private Uri uri;
    private String pathToStoredVideo;
    private ImageView displayRecordedVideo;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    RelativeLayout relative;

    public static File createDirectoryAndSaveFile(Bitmap imageToSave) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/MyFolder/Images");

        direct.mkdir();

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/MyFolder/Images/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(direct, System.currentTimeMillis() + ".jpg");

//        if (file.exists()) {
//            file.delete();
//        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;

    }

    private void selectVideo() {
        final CharSequence[] items = {"Take Video", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(VideoContestUploadActivity.this);

                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        videogalleryIntent();

                } else if (items[item].equals("Take Video")) {
                    if (result) {
                        userChoosenTask = "Take Video";

                        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
//                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //0 means low & 1 means high
                        if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                        }
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    finish();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void videogalleryIntent() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/*"});
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            if (data.getData() != null) {
                llData.setVisibility(View.VISIBLE);

                displayRecordedVideo.setVisibility(View.VISIBLE);

                if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
                    uri = data.getData();
                    MediaPlayer mp = MediaPlayer.create(this, uri);
                    int duration = mp.getDuration();
                    mp.release();

                    if ((duration / 1000) > 15) {
                        // Show Your Messages
                        Toast.makeText(VideoContestUploadActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {

                        imgPlay.setVisibility(View.VISIBLE);

                        pathToStoredVideo = getRealPathFromURIPathVideo(uri, VideoContestUploadActivity.this);
                        Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                        //Store the video to your server
                        file = new File(pathToStoredVideo);

                        Bitmap b = ThumbnailUtils.createVideoThumbnail(pathToStoredVideo, MediaStore.Video.Thumbnails.MINI_KIND);
                        displayRecordedVideo.setImageBitmap(b);
                        video_view.setVideoPath(pathToStoredVideo);

                    }
                } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_FILE) {
                    uri = data.getData();
//                displayRecordedVideo.setVideoURI(uri);
//                displayRecordedVideo.start();
                    imgPlay.setVisibility(View.VISIBLE);
                    ArrayList<String> supportedMedia = new ArrayList<String>();

                    supportedMedia.add(".mp4");
                    supportedMedia.add(".mov");
                    supportedMedia.add(".3gp");


                    videoUrl = ScalingUtilities.getPath(VideoContestUploadActivity.this, data.getData());
//                    pathToStoredVideo = getRealPathFromURIPathVideo(uri, VideoContestUploadActivity.this);
                    videoFile = new File(videoUrl);
                    file = new File(videoUrl);

                    String fileExtnesion = videoUrl.substring(videoUrl.lastIndexOf("."));

                    if (supportedMedia.contains(fileExtnesion)) {


                        long file_size = Integer.parseInt(String.valueOf(videoFile.length()));
//                long fileMb = AudioPost.bytesToMeg(file_size);


                        //if (fileMb >= 16)
                        // Toast.makeText(VideoPost.this, "Upload a video not more than 15 MB in size",
                        //        Toast.LENGTH_SHORT).show();

                        //  else {
                        try {
                            MediaPlayer mplayer = new MediaPlayer();
                            mplayer.reset();
                            mplayer.setDataSource(videoUrl);
                            mplayer.prepare();

                            long totalFileDuration = mplayer.getDuration();
                            Log.i("android", "data is " + totalFileDuration);

                            int sec = (int) ((totalFileDuration / (1000)));

                            Log.i("android", "data is " + sec);

                            Bitmap b = ThumbnailUtils.createVideoThumbnail(videoUrl, MediaStore.Video.Thumbnails.MINI_KIND);
                            displayRecordedVideo.setImageBitmap(b);
                            video_view.setVideoPath(pathToStoredVideo);

                            file = videoFile;

                            if (sec > 15) {
                                Toast.makeText(VideoContestUploadActivity.this, "Select an video not more than 15 seconds",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {

                                llData.setVisibility(View.VISIBLE);
                                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                mediaMetadataRetriever.setDataSource(videoUrl);

                                Uri video = Uri.parse(videoUrl);

                                pathToStoredVideo = getRealPathFromURIPathVideo(video, VideoContestUploadActivity.this);
                                Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                //Store the video to your server
                                // videoview.setMediaController(mediacontrolle);

                                Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

                                displayRecordedVideo.setImageBitmap(bitmap);
                                imgPlay.setVisibility(View.VISIBLE);
                                video_view.setVideoPath(pathToStoredVideo);

                                Toast.makeText(VideoContestUploadActivity.this, "Video selected",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {


                            Log.i("android", "exception is " + e.getLocalizedMessage() + " " + e.getStackTrace());

                        }


                    } else {


                        Toast.makeText(VideoContestUploadActivity.this, "Only .mp4,.mov,.3gp File formats allowed ", Toast.LENGTH_SHORT).show();

                    }
                }
            } else {
                finish();
            }

//            }

        } else {
            finish();
        }

    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private String getRealPathFromURIPathVideo(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    public void PostVideoContest(RequestBody api_access_token, RequestBody eventid, RequestBody status, MultipartBody.Part fbody, MultipartBody.Part thumb) {
        mAPIService.PostVideoContest(api_access_token, eventid, status, thumb, fbody).enqueue(new Callback<PostVideoSelfie>() {
            @Override
            public void onResponse(Call<PostVideoSelfie> call, Response<PostVideoSelfie> response) {
                btnSubmit.setEnabled(true);
                btnSubmit.setClickable(true);
                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    editTitle.setEnabled(true);
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostVideoSelfie> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Log.e("hit", t.getMessage());
                editTitle.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponse(Response<PostVideoSelfie> response) {

        if (response.body().getStatus().equals("success")) {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VideoContestUploadActivity.this, VideoContestActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
        progressbar.setProgress(0);
        progressbar.setMaxValue(100);
        progressbar.setProgressColor(Color.parseColor(colorActive));
        progressbar.setText(String.valueOf(0));
        progressbar.setTextColor(Color.parseColor(colorActive));
        progressbar.setSuffix("%");
        progressbar.setPrefix("");

    }

    public void dismissProgress() {

        if (progressbar.getVisibility() == View.VISIBLE) {
            progressbar.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventId = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);


        displayRecordedVideo = findViewById(R.id.videopreview);
        btnSubmit = findViewById(R.id.btnSubmit);
        video_view = findViewById(R.id.video_view);

        displayRecordedVideo.setVisibility(View.VISIBLE);
        video_view.setVisibility(View.GONE);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video_view);
        video_view.setMediaController(mediaController);


        btnSubmit.setBackgroundColor(Color.parseColor(colorActive));
        editTitle = findViewById(R.id.editTitle);
        imgPlay = findViewById(R.id.imgPlay);
        progressbar = findViewById(R.id.progressbar);
        relative = findViewById(R.id.relative);
        sessionManager = new SessionManager(this);

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            relative.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            relative.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
        TextView header = findViewById(R.id.txtTitle);
        header.setTextColor(Color.parseColor(colorActive));


        mAPIService = ApiUtils.getAPIService();

        llData = findViewById(R.id.llData);
        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);


        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgPlay.setVisibility(View.GONE);
                displayRecordedVideo.setVisibility(View.GONE);
                video_view.setVisibility(View.VISIBLE);

                video_view.setVideoPath(file.getAbsolutePath());
                video_view.start();


            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = editTitle.getText().toString();
                editTitle.setEnabled(false);
                showProgress();

                MediaMetadataRetriever m = new MediaMetadataRetriever();
                m.setDataSource(file.getAbsolutePath());
                Bitmap bit = m.getFrameAtTime();


                video_view.pause();

                RequestBody token = RequestBody.create(MediaType.parse("text/plain"), apikey);
                RequestBody eventid = RequestBody.create(MediaType.parse("text/plain"), eventId);

                RequestBody status = RequestBody.create(MediaType.parse("text/plain"), StringEscapeUtils.escapeJava(data));
                MultipartBody.Part body = null;
                MultipartBody.Part body1 = null;

                String filename = String.valueOf(System.currentTimeMillis()) + ".png";
                File sd = Environment.getExternalStorageDirectory();
                File dest = new File(sd, filename);

                try {
                    FileOutputStream out = new FileOutputStream(dest);
                    bit.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                File file1 = new File(dest.getAbsolutePath());

                ProgressRequestBodyVideo reqFile = new ProgressRequestBodyVideo(file, VideoContestUploadActivity.this);
                body = MultipartBody.Part.createFormData("video_file", file.getName(), reqFile);

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file1);
                body1 = MultipartBody.Part.createFormData("thumb_name", file1.getName(), requestFile);

                btnSubmit.setEnabled(false);
                btnSubmit.setClickable(false);
                PostVideoContest(token, eventid, status, body, body1);
//                }
            }
        });


        selectVideo();
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(VideoContestUploadActivity.this, VideoContestActivity.class);
//        startActivity(intent);
        finish();
    }

    @Override
    public void onProgressUpdate(int percentage) {

        progressbar.setProgress(percentage);
        progressbar.setText(String.valueOf(percentage));
    }

    @Override
    public void onError() {
        dismissProgress();

    }

    @Override
    public void onFinish() {
        dismissProgress();
    }
}
