package com.procialize.mrgeApp20.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.apache.http.HttpVersion.HTTP_1_1;

/**
 * Created by HP-PC on 01-05-2017.
 */

public class SelfiePost extends Activity {

    static final int REQUEST_TAKE_PHOTO = 1;
    Dialog imageChooserDialog;
    LinearLayout llPost;
    ImageView post_thumbnail;
    EditText editName;
    Button btnSubmit;
    TextView txtTitle;
    String postUrl;
    // Session Manager Class
    SessionManager session;
    String MY_PREFS_NAME = "ProcializeInfo";
    String name = "";
    String eventId, colorActive;
    ProgressBar progressbar;
    ImageView headerlogoIv;
    Uri imageUri;
    File file;
    String image_Path = "";
    private ApiConstant constant;
    private String accessToken, mCurrentPhotoPath;

    public static File createDirectoryAndSaveFile(Bitmap imageToSave) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/MyFolder/Images");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/MyFolder/Images/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/MyFolder/Images/"), System.currentTimeMillis() + ".jpg");
        if (file.exists()) {
            file.delete();
        }


        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 50, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;

    }

    private static OkHttpClient getUnsafeOkHttpClient() {

        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient client = new OkHttpClient();

        OkHttpClient.Builder builder = client.newBuilder();
        builder.sslSocketFactory(sslSocketFactory);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;


            }
        });

        return builder.build();

    }

    private static HttpResponse transformResponse(Response response) {
        BasicHttpResponse httpResponse = null;
        try {
            int code = 0;
            if (response != null) {
                code = response.code();


                try {
                    String message = response.message();
                    httpResponse = new BasicHttpResponse(HTTP_1_1, code, message);

                    ResponseBody body = response.body();
                    InputStreamEntity entity = new InputStreamEntity(body.byteStream(), body.contentLength());
                    httpResponse.setEntity(entity);

                    Headers headers = response.headers();
                    for (int i = 0, size = headers.size(); i < size; i++) {
                        String name = headers.name(i);
                        String value = headers.value(i);
                        httpResponse.addHeader(name, value);
                        if ("Content-Type".equalsIgnoreCase(name)) {
                            entity.setContentType(value);
                        } else if ("Content-Encoding".equalsIgnoreCase(name)) {
                            entity.setContentEncoding(value);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return httpResponse;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfie_post);
        llPost = findViewById(R.id.llData);
        post_thumbnail = findViewById(R.id.imgpreview);
        editName = findViewById(R.id.editTitle);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtTitle = findViewById(R.id.txtTitle);
        progressbar = findViewById(R.id.progressbar);


//        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/FuturaStd-Medium.ttf");
//
//        btnSubmit.setTypeface(typeFace);
//        txtTitle.setTypeface(typeFace);
//        editName.setTypeface(typeFace);


        constant = new ApiConstant();

        postUrl = ApiConstant.baseUrl + "PostSelfie";
        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);


        // apikey
        accessToken = user.get(SessionManager.KEY_TOKEN);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventId = prefs.getString("eventid", "");
        colorActive = prefs.getString("colorActive", "");

        btnSubmit.setBackgroundColor(Color.parseColor(colorActive));
        TextView header = findViewById(R.id.txtTitle);
        header.setTextColor(Color.parseColor(colorActive));


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectionDetector cd;


                cd = new ConnectionDetector(SelfiePost.this);

                if (cd.isConnectingToInternet()) {


                    // if (TextUtils.isEmpty(editName.getText().toString())) {
                    // Toast.makeText(SelfiePost.this, "Enter title",
                    //         Toast.LENGTH_SHORT).show();
                    //}
//                    if (TextUtils.isEmpty(image_Path))
//
//                    {
//                        Toast.makeText(SelfiePost.this, "Upload Selfie",
//                                Toast.LENGTH_SHORT).show();
//                    } else {
                    //name = editName.getText().toString();
                    name = StringEscapeUtils.escapeJava(editName.getText().toString());


                    new SubmitPostTask().execute();


//                    }
                } else {
                    Toast.makeText(getBaseContext(), "No Internet Connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


//        upload.setTypeface(typeFace);


//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
//        upload.setVisibility(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            /*if (SelfiePost.this.checkSelfPermission(
                    "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
                ActivityCompat.requestPermissions(SelfiePost.this, permissions, 0);
            } else {

                showAlert(R.array.selectType);

            }*/
            if (this.checkSelfPermission(
                    "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                ActivityCompat.requestPermissions(SelfiePost.this, permissions, 0);
            } else if (this.checkSelfPermission(
                    "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                ActivityCompat.requestPermissions(SelfiePost.this, permissions, 0);
            } else {

                showAlert(R.array.selectType);

            }

        } else {
            showAlert(R.array.selectType);

        }


    }

    public void showAlert(int res) {
        AlertDialog.Builder imageChooserDialog = new AlertDialog.Builder(this, R.style.MyDialogTheme);

        //  imageChooserDialog = new Dialog(this, R.style.MyDialogTheme);
//        imageChooserDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        imageChooserDialog.Buil
//
        imageChooserDialog.setCancelable(false);
        String[] pictureDialogItems = {
                "Select photo from gallery >",
                "Capture photo from camera >", "Cancel"};

        imageChooserDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openGallery(0);
                                break;
                            case 1:
                                cameraTask(1);
                                break;
                            case 2:
                                finish();
                                break;
                        }
                    }
                });
        imageChooserDialog.show();

//        new MaterialDialog.Builder(SelfiePost.this)
//                .title("Select Image")
//                .items(res)
//                .cancelable(false)
//                .titleColor(getResources().getColor(android.R.color.black))
//                .contentColor(getResources().getColor(android.R.color.black))
//                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
//                    @Override
//                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//
//                        selectType(which);
//
//                        return true; // allow selection
//
//
//                    }
//                })
//                .positiveText("CHOOSE")
//                .show();
    }
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        Log.e("Getpath", "Cool" + mCurrentPhotoPath);
//        return image;
//    }

    private void selectType(int pos) {
        if (pos == 0) {

            openGallery(pos);

        } else if (pos == 1) {

            cameraTask(pos);

        }

    }

    public void cameraTask(int imgPos) {


        Log.i("android", "startStorage");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(SelfiePost.this,
                        permissions, 1);
            } else {

                //startCamera();
                // captureImage();
                dispatchTakePictureIntent();

            }

        } else {
            //startCamera();
            //  captureImage();
            dispatchTakePictureIntent();

        }


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.procialize.eventsapp.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();

        /*
         * Apply our splash exit (fade in) and main entry (fade out)
         * animation transitions.
         */
//        overridePendingTransition(R.anim.pull_in_left,
//                R.anim.pull_out_right);
        // super.onBackPressed();
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void startCamera() {


        File direct = new File(Environment.getExternalStorageDirectory() + "/MyFolder/Images");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/MyFolder/Images/");
            wallpaperDirectory.mkdirs();
        }

        file = new File(new File("/sdcard/MyFolder/Images/"), System.currentTimeMillis() + ".jpg");
        imageUri = Uri.fromFile(file);


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                imageUri);


        startActivityForResult(takePictureIntent, 1);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                showAlert(R.array.selectType);


            } else {
                Toast.makeText(this, "No permission to read external storage.",
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
                Toast.makeText(this, "No permission to read external storage.",
                        Toast.LENGTH_SHORT).show();

            } else {
                //captureImage();

                dispatchTakePictureIntent();
            }


        }
        /*if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();

            } else {
                Toast.makeText(this, "No permission to capture photo.",
                        Toast.LENGTH_SHORT).show();

            }
        }*/
    }

    private void openGallery(int pos) {

        startStorage();


    }

    public void startStorage() {

        Log.i("android", "startStorage");


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {


            llPost.setVisibility(View.VISIBLE);

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            String compressedImagePath = compressImage(picturePath);
            image_Path = compressedImagePath;


            Glide.with(this).load(compressedImagePath).into(post_thumbnail);


            // post_thumbnail.setImageURI(Uri.fromFile(new File(image_Path)));
            //  PicassoTrustAll.getInstance(this).load(compressedImagePath).into(post_thumbnail);

            // PicassoTrustAll.getInstance(PostActivity.this).load(compressedImagePath)
            // .into(post_thumbnail);


            cursor.close();
        } else if (requestCode == 2 && resultCode == Activity.RESULT_CANCELED) {

            finish();


        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setpic2();
        } else if (requestCode == 100 && resultCode == RESULT_CANCELED
                && null != data) {


            finish();

        } else if (resultCode == RESULT_CANCELED) {
            finish();
//            Intent selfieIntent = new Intent(SelfiePost.this, SelfieContestActivity.class);
//            startActivity(selfieIntent);
//            finish();
        } else {
            finish();
        }

        /*if (requestCode == 1 && resultCode == RESULT_OK
                && null != data) {


            try {


                //PicassoTrustAll.getInstance(this).load(uri.toString()).into(post_thumbnail);

                llPost.setVisibility(View.VISIBLE);




                String compressedImagePath = compressImage(file.getAbsolutePath());


                image_Path = compressedImagePath;

                 *//*   File saveFile = createDirectoryAndSaveFile(decodeSampledBitmapFromResource(
                            file1.getAbsolutePath(),
                            720, 1280));*//*


                // String compressedImagePath = compressImage(saveFile.getAbsolutePath());
                Glide.with(this).load(compressedImagePath).into(post_thumbnail);

                // PicassoTrustAll.getInstance(this).load(photo).into(post_thumbnail);


                //   post_thumbnail.setImageBitmap(photo);


            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (requestCode == 1 && resultCode == RESULT_CANCELED
                && null != data) {


            finish();

        }
*/

    }

    private void setpic2() {

        post_thumbnail.setVisibility(View.VISIBLE);
        llPost.setVisibility(View.VISIBLE);

        //selfieSubmit.setVisibility(View.VISIBLE);
        //edtImagename.setVisibility(View.VISIBLE);


        String compressedImagePath = compressImage(mCurrentPhotoPath);
        //appDelegate.setPostImagePath(compressedImagePath);
        image_Path = compressedImagePath;


        Glide.with(this).load(compressedImagePath).into(post_thumbnail);


        Toast.makeText(SelfiePost.this, "Image selected",
                Toast.LENGTH_SHORT).show();

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

//        float maxHeight = 816.0f;
//        float maxWidth = 612.0f;

        float maxHeight = 600.0f;
        float maxWidth = 400.0f;

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
        } catch (Exception exception) {
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
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

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
        Cursor cursor = getContentResolver().query(contentUri, null, null,
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

    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
        progressbar.setIndeterminate(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Drawable wrapDrawable = DrawableCompat.wrap(progressbar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, Color.parseColor(colorActive));
            progressbar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
        }

    }


    public void dismissProgress() {

        if (progressbar.getVisibility() == View.VISIBLE) {
            progressbar.setVisibility(View.GONE);
        }

    }

    public class SubmitPostTask extends AsyncTask<String, String, JSONObject> {

        String json1 = "";
        InputStream is = null;
        JSONObject json = null;
        JSONObject status;


        String message = "";

        String error = "";
        String msg = "";

        String res = null;

        ProgressDialog pDialog;

        File sourceFile;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(SelfiePost.this,
//                    R.style.Base_Theme_AppCompat_Dialog_Alert);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
            // Showing progress dialog
            showProgress();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {


                //String picturePath = appDelegate.getEditImage();

                if (image_Path != null && !(image_Path.equalsIgnoreCase(""))) {
                    sourceFile = new File(image_Path);
                }


                //Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

                String filename = image_Path.substring(image_Path.lastIndexOf("/") + 1);


                OkHttpClient client = null;
                try {

                    URL url = new URL(postUrl);
                    //  SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(url);

                    client = getUnsafeOkHttpClient().newBuilder().build();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                MultipartBody.Builder builder = new MultipartBody.Builder();

                builder.setType(MultipartBody.FORM);
                if (image_Path != null && !(image_Path.equalsIgnoreCase(""))) {
                    builder.addFormDataPart("selfie_image", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile));
                }

                builder.addFormDataPart("api_access_token", accessToken);
                builder.addFormDataPart("event_id", eventId);
                builder.addFormDataPart("title", name);

                RequestBody requestBody = builder.build();


                Request request = new Request.Builder()
                        .url(postUrl)
                        .post(requestBody)
                        .build();

                HttpEntity httpEntity = null;
                Response response = null;

                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //res = response.body().string();
                httpEntity = transformResponse(response).getEntity();
                res = EntityUtils.toString(httpEntity);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                json = new JSONObject(res);
                error = json.getString("status");
                message = json.getString("msg");
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());

            } catch (Exception e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());

            }

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog
            dismissProgress();
            if (!TextUtils.isEmpty(message))
                Toast.makeText(SelfiePost.this, message,
                        Toast.LENGTH_SHORT).show();


            if (error.equalsIgnoreCase("success")) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", true);
                setResult(RESULT_OK, returnIntent);
                finish();

            } else {
                Intent returnIntent = getIntent();
                returnIntent.putExtra("result", false);
                setResult(RESULT_OK, returnIntent);
                finish();
            }



         /*   if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();


                finish();


            } else {
                Toast.makeText(SelfiePost.this, message, Toast.LENGTH_SHORT)
                        .show();
            }
*/

        }

    }

}
