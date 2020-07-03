package com.procialize.mrgeApp20.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.procialize.mrgeApp20.Activity.ExhibitorDetailActivity;
import com.procialize.mrgeApp20.Exhibitor.Adapter.ExhibitorEditDocumentAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.BuildConfig;
import com.procialize.mrgeApp20.CustomTools.ImagePath_MarshMallow;
import com.procialize.mrgeApp20.CustomTools.ProgressRequestBodyImage;
import com.procialize.mrgeApp20.CustomTools.ProgressRequestBodyVideo;
import com.procialize.mrgeApp20.DbHelper.DBHelper;
import com.procialize.mrgeApp20.GetterSetter.AddExhibitorBrochure;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorBrochureList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.MyApplication;
import com.procialize.mrgeApp20.Utility.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.mrgeApp20.Utility.ScalingUtilities.getPath;

public class EditDocumentFragment extends Fragment implements ExhibitorEditDocumentAdapter.MyTravelAdapterListner, ProgressRequestBodyImage.UploadCallbacks, ProgressRequestBodyVideo.UploadCallbacks {

    LinearLayout linear_edit_doc;
    public static final String DOCUMENTS_DIR = "documents";
    SharedPreferences prefs;
    String MY_PREFS_NAME = "ProcializeInfo";
    String accesstoken, eventid, colorActive;
    ImageView img_add;
    Button btn_add;
    private String pathToStoredVideo;
    private Uri uri;
    ImageView imgTvel;
    RecyclerView recycler_doc;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    List<ExhibitorBrochureList> ExhibitorBrochureList = new ArrayList<>();
    ExhibitorEditDocumentAdapter exhibitorEditDocumentAdapter;
    Button btn_upload;
    Dialog myDialog;
    private int REQUEST_TAKE_GALLERY = 1, REQUEST_TAKE_PHOTO = 2, REQUEST_VIDEO_CAPTURE = 300, REQUEST_TAKE_GALLERY_VIDEO = 3, REQUEST_TAKE_PDF = 4;
    String mCurrentPhotoPath;
    MyApplication appDelegate;
    String userChoosenTask;
    String type;
    Dialog dialog;
    File file;
    APIService mAPIService;
    SessionManager sessionManager;
    private String selectedImagePath;
    ProgressBar progrss;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_edit_details,
                container, false);

        linear_edit_doc = rootView.findViewById(R.id.linear_edit_doc);
        img_add = rootView.findViewById(R.id.img_add);
        btn_add = rootView.findViewById(R.id.btn_add);
        recycler_doc = rootView.findViewById(R.id.recycler_doc);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_doc.setLayoutManager(mLayoutManager);
        prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        ShapeDrawable sd = new ShapeDrawable();
        // Specify the shape of ShapeDrawable
        sd.setShape(new RectShape());
        // Specify the border color of shape
        sd.getPaint().setColor(Color.parseColor(colorActive));
        // Set the border width
        sd.getPaint().setStrokeWidth(10f);
        // Specify the style is a Stroke
        sd.getPaint().setStyle(Paint.Style.STROKE);
        // Finally, add the drawable background to TextView
        linear_edit_doc.setBackground(sd);
        btn_add.setTextColor(Color.parseColor(colorActive));
        appDelegate = (MyApplication) getActivity().getApplicationContext();

        int colorInt = Color.parseColor(colorActive);
        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(img_add.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        img_add.setImageDrawable(drawable);
        dbHelper = new DBHelper(getActivity());
        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getWritableDatabase();
        ExhibitorBrochureList.clear();
        ExhibitorBrochureList = dbHelper.getBrochureList(ExhibitorDetailActivity.Exhi_id);
        exhibitorEditDocumentAdapter = new ExhibitorEditDocumentAdapter(getActivity(), ExhibitorBrochureList, new EditDocumentFragment());
        exhibitorEditDocumentAdapter.notifyDataSetChanged();
        recycler_doc.setAdapter(exhibitorEditDocumentAdapter);
        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        accesstoken = user.get(SessionManager.KEY_TOKEN);

        linear_edit_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge();
            }
        });
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge();
            }
        });


        return rootView;
    }

    @Override
    public void onContactSelected(com.procialize.mrgeApp20.GetterSetter.ExhibitorBrochureList travel) {

    }

    private void showratedialouge() {

        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.dialog_add_presentation);
        myDialog.setCancelable(false);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();

        LinearLayout diatitle = myDialog.findViewById(R.id.diatitle);

        diatitle.setBackgroundColor(Color.parseColor(colorActive));

        final TextView nametv = myDialog.findViewById(R.id.nametv);
        final EditText edit_doc_name = myDialog.findViewById(R.id.edit_doc_name);
        btn_upload = myDialog.findViewById(R.id.btn_upload);
        progrss = myDialog.findViewById(R.id.progrss);

        final ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);
        imgTvel  = myDialog.findViewById(R.id.imgTvel);
//        nametv.setTextColor(Color.parseColor(colorActive));
        btn_upload.setBackgroundColor(Color.parseColor(colorActive));

        imgTvel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showAlert(R.array.selectType);
                dialog = onCreateDialogSingleChoice();
                dialog.show();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
                type = "";
                appDelegate.setPostImagePath("");
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit_doc_name.getText().toString();
                ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                try {
                    if (name.isEmpty()) {
                        Toast.makeText(getActivity(), "Enter name of file", Toast.LENGTH_SHORT).show();
                    } else if (type.equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Select File first", Toast.LENGTH_SHORT).show();
                    } else {
                        if (type.equals("image")) {
                            if (appDelegate.getPostImagePath() != null
                                    && appDelegate.getPostImagePath().length() > 0) {
                                System.out
                                        .println("Post Image URL  inside SubmitPostTask :"
                                                + appDelegate.getPostImagePath());

                                appDelegate.setPostImagePath("");

                                RequestBody type_ = RequestBody.create(MediaType.parse("text/plain"), type);
                                RequestBody token = RequestBody.create(MediaType.parse("text/plain"), accesstoken);
                                RequestBody event_id = RequestBody.create(MediaType.parse("text/plain"), eventid);
                                RequestBody ex_id = RequestBody.create(MediaType.parse("text/plain"), ExhibitorDetailActivity.Exhi_id);
                                RequestBody status = RequestBody.create(MediaType.parse("text/plain"), StringEscapeUtils.escapeJava(name));
                                MultipartBody.Part body = null;


                                if (file != null) {
//                            showProgress();

                                    ProgressRequestBodyImage reqFile = new ProgressRequestBodyImage(file, new EditDocumentFragment());
                                    body = MultipartBody.Part.createFormData("brochure_file_name", file.getName(), reqFile);
                                    progrss.setVisibility(View.VISIBLE);
                                    btn_upload.setEnabled(false);
                                    btn_upload.setClickable(false);
                                    postFeed(type_, token, event_id, ex_id, status, body);

                                } else {
                                    Toast.makeText(getActivity(), "Please Select any Image", Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                Toast.makeText(getActivity(), "Please Select any Image", Toast.LENGTH_SHORT).show();
//                                getActivity().finish();

                            }
                        } else if (type.equalsIgnoreCase("video")) {
                            RequestBody type_ = RequestBody.create(MediaType.parse("text/plain"), type);
                            RequestBody token = RequestBody.create(MediaType.parse("text/plain"), accesstoken);
                            RequestBody event_id = RequestBody.create(MediaType.parse("text/plain"), eventid);
                            RequestBody ex_id = RequestBody.create(MediaType.parse("text/plain"), ExhibitorDetailActivity.Exhi_id);
                            RequestBody status = RequestBody.create(MediaType.parse("text/plain"), StringEscapeUtils.escapeJava(name));
                            MultipartBody.Part body = null;
                            MultipartBody.Part body1 = null;
                            appDelegate.setPostImagePath("");

                            if (file != null) {

                                MediaMetadataRetriever m = new MediaMetadataRetriever();

                                m.setDataSource(file.getAbsolutePath());

                                Bitmap bit = m.getFrameAtTime();
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


                                ProgressRequestBodyVideo reqFile = new ProgressRequestBodyVideo(file, new EditDocumentFragment());
                                body = MultipartBody.Part.createFormData("brochure_file_name", file.getName(), reqFile);

                                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file1);
                                body1 = MultipartBody.Part.createFormData("media_file_thumb", file1.getName(), requestFile);

                                progrss.setVisibility(View.VISIBLE);
                                btn_upload.setEnabled(false);
                                btn_upload.setClickable(false);
                                postFeed(type_, token, event_id, ex_id, status, body);
                            } else {
                                Toast.makeText(getActivity(), "Please Select any Video", Toast.LENGTH_SHORT).show();

                            }

                        } else if (type.equalsIgnoreCase("pdf")) {


                            RequestBody type_ = RequestBody.create(MediaType.parse("text/plain"), type);
                            RequestBody token = RequestBody.create(MediaType.parse("text/plain"), accesstoken);
                            RequestBody event_id = RequestBody.create(MediaType.parse("text/plain"), eventid);
                            RequestBody ex_id = RequestBody.create(MediaType.parse("text/plain"), ExhibitorDetailActivity.Exhi_id);
                            RequestBody status = RequestBody.create(MediaType.parse("text/plain"), StringEscapeUtils.escapeJava(name));
                            MultipartBody.Part body = null;

                            appDelegate.setPostImagePath("");
                            if (file != null) {
                                RequestBody PDFreq = RequestBody.create(MediaType.parse("application/pdf"), file);
                                body = MultipartBody.Part.createFormData("brochure_file_name", file.getName(), PDFreq);

                                progrss.setVisibility(View.VISIBLE);
                                btn_upload.setEnabled(false);
                                btn_upload.setClickable(false);
                                postFeed(type_, token, event_id, ex_id, status, body);
                            } else {
                                Toast.makeText(getActivity(), "Please Select any pdf", Toast.LENGTH_SHORT).show();
//                            getActivity().finish();


                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Select File first", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public Dialog onCreateDialogSingleChoice() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence[] array = {"Image", "Video", "PDF"};

        builder.setTitle("Select Your Choice")

                .setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            type = "image";
                            showAlert(R.array.selectType);
                            dialog.dismiss();
                        } else if (which == 1) {
                            type = "video";
                            selectVideo();
                            dialog.dismiss();
                        } else if (which == 2) {
                            type = "pdf";
                            pdfgalleryIntent();
                            dialog.dismiss();
                        }
                    }
                });


        return builder.create();
    }

    public void postFeed(RequestBody filetype, RequestBody api_access_token, RequestBody eventid, RequestBody ex_id, RequestBody status, MultipartBody.Part fbody) {
        mAPIService.AddExhibitorBrochure(filetype, api_access_token, eventid, ex_id, status, fbody).enqueue(new Callback<AddExhibitorBrochure>() {
            @Override
            public void onResponse(Call<AddExhibitorBrochure> call, Response<AddExhibitorBrochure> response) {
                Log.i("hit", "post submitted to API." + response.body().toString());
                if (response.isSuccessful()) {
                    progrss.setVisibility(View.GONE);
                    btn_upload.setEnabled(true);
                    btn_upload.setClickable(true);
                    appDelegate.setPostImagePath("");
                    type = "";
                    myDialog.dismiss();
                    ExhibitorBrochureList.clear();
                    ExhibitorBrochureList = response.body().getExhibitor_brochure_list();
                    dbHelper.clearEXbrocherTable();
                    dbHelper.insertExBrocherList(ExhibitorBrochureList, db);

                    ExhibitorBrochureList.clear();
                    ExhibitorBrochureList = dbHelper.getBrochureList(ExhibitorDetailActivity.Exhi_id);
                    exhibitorEditDocumentAdapter = new ExhibitorEditDocumentAdapter(getActivity(), ExhibitorBrochureList, new EditDocumentFragment());
                    exhibitorEditDocumentAdapter.notifyDataSetChanged();
                    recycler_doc.setAdapter(exhibitorEditDocumentAdapter);

                } else {
                    myDialog.dismiss();
                    progrss.setVisibility(View.GONE);
                    type = "";
                    btn_upload.setEnabled(true);
                    btn_upload.setClickable(true);
                    appDelegate.setPostImagePath("");
//                    dismissProgress();
                    Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddExhibitorBrochure> call, Throwable t) {
                myDialog.dismiss();
                progrss.setVisibility(View.GONE);
                btn_upload.setEnabled(true);
                btn_upload.setClickable(true);
                appDelegate.setPostImagePath("");
                type = "";
                Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
//                dismissProgress();
            }
        });
    }

    private void selectVideo() {
        final CharSequence[] items = {"Take Video", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

//                recorderTask(1);

                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        videogalleryIntent();

                } else if (items[item].equals("Take Video")) {
                    userChoosenTask = "Take Video";
                    if (result) {
                        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
//                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //0 means low & 1 means high
                        if (videoCaptureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                        }
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();

                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void setpic2() {

        String compressedImagePath = compressImage(mCurrentPhotoPath);
        appDelegate.setPostImagePath(compressedImagePath);
//        file = new File(mCurrentPhotoPath);
        Toast.makeText(getActivity(), "Image selected",
                Toast.LENGTH_SHORT).show();

    }

    private void videogalleryIntent() {


        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO);
    }

    private void pdfgalleryIntent() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), REQUEST_TAKE_PDF);

    }


    public void showAlert(int res) {

        new MaterialDialog.Builder(getActivity())
                .title("Select Image")
                .items(res)
                .titleColor(getResources().getColor(android.R.color.black))
                .contentColor(getResources().getColor(android.R.color.black))
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        selectType(which);

                        return true; // allow selection


                    }
                })
                .positiveText("CHOOSE")
                .cancelable(false)
                .show();
    }

    private void selectType(int pos) {
        if (pos == 0) {

            openGallery(pos);

        } else if (pos == 1) {

            cameraTask(pos);

        } else {
            new MaterialDialog.Builder(getActivity()).cancelable(true);
        }

    }

    private void openGallery(int pos) {
        startStorage();
    }

    public void startStorage() {
        Log.i("android", "startStorage");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_TAKE_GALLERY);
    }

    public void cameraTask(int imgPos) {


        Log.i("android", "startStorage");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // Android M Permission check
            if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(getActivity(),
                        permissions, 1);
            } else {

                //startCamera();
                //  captureImage();
                dispatchTakePictureIntent();

            }

        } else {
            //startCamera();
            // captureImage();
            dispatchTakePictureIntent();

        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            // Create the File where the photo should go
            file = null;
            try {
                file = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        BuildConfig.APPLICATION_ID+".android.fileprovider",
                        file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_GALLERY && resultCode == getActivity().RESULT_OK && data != null) {
            imgTvel.setImageResource(R.drawable.selecteddoc);
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            String compressedImagePath = compressImage(picturePath);
            appDelegate.setPostImagePath(compressedImagePath);
            file = new File(compressedImagePath);
            Toast.makeText(getActivity(), "Image selected",
                    Toast.LENGTH_SHORT).show();

            cursor.close();
//            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK) {
            imgTvel.setImageResource(R.drawable.selecteddoc);
            setpic2();

            // setPic();
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
            imgTvel.setImageResource(R.drawable.selecteddoc);
            uri = data.getData();
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


            if (Build.VERSION.SDK_INT > 22)
                pathToStoredVideo = ImagePath_MarshMallow.getPath(getActivity(), uri);
            else
                //else we will get path directly
                pathToStoredVideo = uri.getPath();
            Log.d("video", "Recorded Video Path " + pathToStoredVideo);
            //Store the video to your server
            file = new File(pathToStoredVideo);


            uri = data.getData();


            try {
                if (uri != null) {

                    MediaPlayer mp = MediaPlayer.create(getActivity(), uri);
                    int duration = mp.getDuration();
                    mp.release();

                    if ((duration / 1000) > 15) {
                        // Show Your Messages
                        Toast.makeText(getActivity(), "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();

                    } else {

                        if (Build.VERSION.SDK_INT > 22) {
                            pathToStoredVideo = ImagePath_MarshMallow.getPath(getActivity(), uri);
                            Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                        } else {
                            //else we will get path directly
                            pathToStoredVideo = uri.getPath();

                            Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                        }
                        file = new File(pathToStoredVideo);

                    }
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
            imgTvel.setImageResource(R.drawable.selecteddoc);

            Uri selectedImageUri = data.getData();

            // OI FILE Manager
            selectedImagePath = selectedImageUri.getPath();

            // MEDIA GALLERY
            selectedImagePath = getPath(getActivity(), selectedImageUri);
            uri = selectedImageUri;
            if (selectedImagePath != null) {


                try {
                    if (uri != null) {

                        MediaPlayer mp = MediaPlayer.create(getActivity(), uri);
                        int duration = mp.getDuration();
                        mp.release();

                        if ((duration / 1000) > 15) {
                            // Show Your Messages
                            Toast.makeText(getActivity(), "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();

                        } else {

                            if (Build.VERSION.SDK_INT > 22) {
                                pathToStoredVideo = ImagePath_MarshMallow.getPath(getActivity(), uri);
                                Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                            } else {
                                //else we will get path directly
                                pathToStoredVideo = uri.getPath();

                                Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                            }
                            file = new File(pathToStoredVideo);

                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        } else if (requestCode == REQUEST_TAKE_PDF && resultCode == getActivity().RESULT_OK && data != null) {
            imgTvel.setImageResource(R.drawable.selecteddoc);
            uri = data.getData();
//            String PDFPATH = FilePath.getPath(getActivity(), uri);
//            file = new File(PDFPATH);
            String fileName = getFileName(getActivity(), uri);
            File cacheDir = getDocumentCacheDir(getActivity());
            file = generateFileName(fileName, cacheDir);
            String destinationPath = null;
            if (file != null) {
                destinationPath = file.getAbsolutePath();
                saveFileFromUri(getActivity(), uri, destinationPath);
            }

        }
    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
//        logDir(context.getCacheDir());
//        logDir(dir);

        return dir;
    }

    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            //Log.w(TAG, e);
            return null;
        }

        //logDir(directory);

        return file;
    }

    public static String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            String path = getPath(context, uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        float maxHeight = 916.0f;
        float maxWidth = 712.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();

        try {
            out = new FileOutputStream(filename);

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null,
                null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                showAlert(R.array.selectType);


            } else {
                Toast.makeText(getActivity(), "No permission to read external storage.",
                        Toast.LENGTH_SHORT).show();

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {

            int permGranted = PackageManager.PERMISSION_GRANTED;

            Boolean permissionRequired = false;

            for (int perm : grantResults) {

                if (perm != permGranted) {
                    permissionRequired = true;
                }
            }

            //if permission is still required
            if (permissionRequired) {
                Toast.makeText(getActivity(), "No permission to capture storage.",
                        Toast.LENGTH_SHORT).show();

            } else {
                //captureImage();

                dispatchTakePictureIntent();
            }
/*if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();

            } else {
                Toast.makeText(this, "No permission to capture photo.",
                        Toast.LENGTH_SHORT).show();

            }*/
        }
    }


    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }
}
