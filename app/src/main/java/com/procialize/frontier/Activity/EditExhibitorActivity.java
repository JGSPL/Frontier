package com.procialize.frontier.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.tabs.TabLayout;
import com.procialize.frontier.Exhibitor.Adapter.ExhibitorEditDocumentAdapter;
import com.procialize.frontier.ApiConstant.APIService;
import com.procialize.frontier.ApiConstant.ApiConstant;
import com.procialize.frontier.ApiConstant.ApiUtils;
import com.procialize.frontier.DbHelper.DBHelper;
import com.procialize.frontier.Fragments.EditDetailsFragment;
import com.procialize.frontier.Fragments.EditDocumentFragment;
import com.procialize.frontier.GetterSetter.ExhibitorList;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.MyApplication;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import retrofit2.Call;
import retrofit2.Callback;

import static org.apache.http.HttpVersion.HTTP_1_1;

public class EditExhibitorActivity extends AppCompatActivity {
    private ViewPager viewPager;
    TabLayout tabs;
    ProgressBar progressBar;
    Adapter adapter;
    SharedPreferences prefs;
    String MY_PREFS_NAME = "ProcializeInfo";
    String accesstoken, eventid, colorActive;
    Button btn_save;
    ImageView img_exebutor, img_exhebitor;
    RelativeLayout linear;
    ImageView img_click;
    private int REQUEST_TAKE_GALLERY = 1, REQUEST_TAKE_PHOTO = 2;
    String mCurrentPhotoPath;
    MyApplication appDelegate;
    EditText edit_exname;
    ProgressBar progress;
    String picturePath;
    File sourceFile;
    String postUrl;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    SessionManager session;
    HashMap<String, String> user;
    public String bid, bname;
    TextView customTab1, customTab2;
    APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exhibitor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Intent attendeetail = new Intent(EditExhibitorActivity.this, ExhibitorDetailActivity.class);
                attendeetail.putExtra("address", ExhibitorDetailActivity.address);
                attendeetail.putExtra("allowedMessage", ExhibitorDetailActivity.allowedMessage);
                attendeetail.putExtra("allowedsetupMeeting", ExhibitorDetailActivity.allowedsetupMeeting);
                attendeetail.putExtra("ExhiCatId", ExhibitorDetailActivity.ExhiCatId);
                attendeetail.putExtra("Exhi_id", ExhibitorDetailActivity.Exhi_id);
                attendeetail.putExtra("ExhiLogo", ExhibitorDetailActivity.ExhiLogo);
                attendeetail.putExtra("ExhiName", ExhibitorDetailActivity.ExhiName);
                attendeetail.putExtra("StallNum", ExhibitorDetailActivity.StallNum);
                attendeetail.putExtra("TileImage", ExhibitorDetailActivity.TileImage);
                attendeetail.putExtra("CatName", ExhibitorDetailActivity.CatName);
                startActivity(attendeetail);
                finish();
            }
        });

        dbHelper = new DBHelper(EditExhibitorActivity.this);

        procializeDB = new DBHelper(EditExhibitorActivity.this);
        db = procializeDB.getWritableDatabase();
        appDelegate = (MyApplication) getApplicationContext();
        appDelegate.setPostImagePath("");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        btn_save = (Button) findViewById(R.id.btn_save);
        img_exebutor = (ImageView) findViewById(R.id.img_exebutor);
        img_exhebitor = (ImageView) findViewById(R.id.img_exhebitor);
        linear = (RelativeLayout) findViewById(R.id.linear);
        img_click = (ImageView) findViewById(R.id.img_click);
        edit_exname = (EditText) findViewById(R.id.edit_exname);
        progress = (ProgressBar) findViewById(R.id.progress);
        edit_exname.setText(ExhibitorDetailActivity.ExhiName);
        postUrl = ApiConstant.baseUrl + "EditExhibitor";
        session = new SessionManager(this);
        user = session.getUserDetails();
        mAPIService = ApiUtils.getAPIService();
        // token
        accesstoken = user.get(SessionManager.KEY_TOKEN);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        try {

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/"+ApiConstant.folderName+"/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));

        }

        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabs = (TabLayout) findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setTabMode(TabLayout.GRAVITY_CENTER);
        setupTabLayout();
        viewPager.setCurrentItem(0);
        tabs.getTabAt(0).getCustomView().setBackgroundColor(Color.parseColor(colorActive));
        customTab1.setTextColor(Color.parseColor("#ffffff"));
        customTab2.setTextColor(Color.parseColor("#000000"));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabs.getTabCount(); i++) {
                    if (i == position) {
                        tabs.getTabAt(i).getCustomView().setBackgroundColor(Color.parseColor(colorActive));
//                        tabs.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                    } else {
                        tabs.getTabAt(i).getCustomView().setBackgroundColor(Color.parseColor("#ffffff"));
//                        tabs.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    }
                }

                if (tabs.getSelectedTabPosition() == 0) {
                    customTab1.setTextColor(Color.parseColor("#ffffff"));
                    customTab2.setTextColor(Color.parseColor("#000000"));

                } else if (tabs.getSelectedTabPosition() == 1) {
                    customTab2.setTextColor(Color.parseColor("#ffffff"));
                    customTab1.setTextColor(Color.parseColor("#000000"));

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


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
        btn_save.setBackground(sd);
        btn_save.setTextColor(Color.parseColor(colorActive));

        if (ExhibitorDetailActivity.ExhiLogo != null) {


            Glide.with(this).load(ApiConstant.exhiilogo + ExhibitorDetailActivity.ExhiLogo)
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    // holder.progressBar.setVisibility(View.GONE);
                    img_exebutor.setImageResource(R.drawable.ex_logo);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    //  holder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(img_exebutor);

        }

        if (ExhibitorDetailActivity.TileImage != null) {


            Glide.with(this).load(ApiConstant.exhiilogo + ExhibitorDetailActivity.TileImage)
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    // holder.progressBar.setVisibility(View.GONE);
                    img_exhebitor.setImageResource(R.drawable.tile);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    //  holder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(img_exhebitor);

        }

        img_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (EditExhibitorActivity.this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                        final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                        ActivityCompat.requestPermissions(EditExhibitorActivity.this, permissions, 0);
                    } else if (EditExhibitorActivity.this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                        final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                        ActivityCompat.requestPermissions(EditExhibitorActivity.this, permissions, 0);
                    } else {

                        showAlert(R.array.selectType);

                    }

                } else {
                    showAlert(R.array.selectType);

                }
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picturePath = appDelegate.getPostImagePath();

                try {
                    bid = ExhibitorEditDocumentAdapter.getBrocherId();
                    bname = ExhibitorEditDocumentAdapter.getBrocherName();
                } catch (Exception e) {
                    bid = "";
                    bname = "";
                }

                if (bid == null) {
                    bid = "";
                }

                if (bname == null) {
                    bname = "";
                }

                new SubmitPostTask().execute();

            }
        });

    }

    private void setupTabLayout() {

        customTab1 = (TextView) LayoutInflater.from(EditExhibitorActivity.this)
                .inflate(R.layout.custom_tab_layout, null);
        customTab2 = (TextView) LayoutInflater.from(EditExhibitorActivity.this)
                .inflate(R.layout.custom_tab_layout, null);

        customTab1.setText("Details");
        tabs.getTabAt(0).setCustomView(customTab1);
        customTab2.setText("Documents");
        tabs.getTabAt(1).setCustomView(customTab2);


    }

    public void showAlert(int res) {

        new MaterialDialog.Builder(EditExhibitorActivity.this)
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

            cameraTask();

        } else {
            finish();
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

    private static HttpResponse transformResponse(Response response) {

        BasicHttpResponse httpResponse = null;
        try {
            int code = 0;
            if (response != null)
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return httpResponse;
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

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            showProgress();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {


                if (picturePath != null && !(picturePath.equalsIgnoreCase(""))) {
                    sourceFile = new File(picturePath);
                }


                //Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

                String filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);


                OkHttpClient client = null;
                try {

                    URL url = new URL(postUrl);
                    // SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(url);


                    client = getUnsafeOkHttpClient().newBuilder().build();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                MultipartBody.Builder builder = new MultipartBody.Builder();

                builder.setType(MultipartBody.FORM);
                if (picturePath != null && !(picturePath.equalsIgnoreCase(""))) {
                    builder.addFormDataPart("tile_image", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile));
                }

                builder.addFormDataPart("api_access_token", accesstoken);
                builder.addFormDataPart("event_id", eventid);

                builder.addFormDataPart("exhibitor_id", ExhibitorDetailActivity.Exhi_id);
                builder.addFormDataPart("exhibitor_name", edit_exname.getText().toString());
                builder.addFormDataPart("stall_number", EditDetailsFragment.edit_stall.getText().toString());
                builder.addFormDataPart("address", EditDetailsFragment.edit_address_detail.getText().toString());
                builder.addFormDataPart("brochure_id", bid);
                builder.addFormDataPart("brochure_title", bname);

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

            } catch (IOException e) {
                e.printStackTrace();
            }

            // try parse the string to a JSON object
            try {
                json = new JSONObject(res);
                error = json.getString("status");
                message = json.getString("msg");
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());

            } catch (NullPointerException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());

            }


            return json;

        }

        //
        @Override
        protected void onPostExecute(JSONObject result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog
            dismissProgress();

            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();

                Toast.makeText(EditExhibitorActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
                sendExhiList(eventid, accesstoken);

            } else {
                Toast.makeText(EditExhibitorActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }


        }

    }


    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    public void dismissProgress() {
        progress.setVisibility(View.GONE);
    }


    public void cameraTask() {


        Log.i("android", "startStorage");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(EditExhibitorActivity.this,
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
                        "com.procialize.eventsapp.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
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


    private void setupViewPager(ViewPager viewPager) {

        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new EditDetailsFragment(), "Details");
        adapter.addFragment(new EditDocumentFragment(), "Documents");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

        float maxHeight = 400f;
        float maxWidth = 1000f;
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
        final int height = 400;
        final int width = 1000;
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


    private void setpic2() {


        String compressedImagePath = compressImage(mCurrentPhotoPath);
        appDelegate.setPostImagePath(compressedImagePath);

        Glide.with(this).load(compressedImagePath).into(img_exhebitor);


        Toast.makeText(EditExhibitorActivity.this, "Image selected",
                Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {

                if (requestCode == REQUEST_TAKE_PHOTO) {
                    setpic2();
                }
                if (requestCode == REQUEST_TAKE_GALLERY && resultCode == RESULT_OK && data != null) {


                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);

                    String compressedImagePath = compressImage(picturePath);
                    appDelegate.setPostImagePath(compressedImagePath);

                    // PicassoTrustAll.getInstance(this).load(compressedImagePath).into(post_thumbnail);
                    Glide.with(this).load(compressedImagePath).into(img_exhebitor);


                    // PicassoTrustAll.getInstance(PostActivity.this).load(compressedImagePath)
                    // .into(post_thumbnail);

                    Toast.makeText(EditExhibitorActivity.this, "Image selected",
                            Toast.LENGTH_SHORT).show();

                    cursor.close();
//            }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public void sendExhiList(String event_id, String token) {
        mAPIService.ExhibitorFetch(event_id, token).enqueue(new Callback<ExhibitorList>() {
            @Override
            public void onResponse(Call<ExhibitorList> call, retrofit2.Response<ExhibitorList> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        Intent main = new Intent(EditExhibitorActivity.this, LoginActivity.class);
                        startActivity(main);
                        finish();

                    } else {
                        showResponse(response);
                    }
                } else {
                    dismissProgress();
                    Toast.makeText(EditExhibitorActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<ExhibitorList> call, Throwable t) {
                Toast.makeText(EditExhibitorActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponse(retrofit2.Response<ExhibitorList> response) {

        for (int i = 0; i < response.body().getExhibitorDataList().size(); i++) {
            if (response.body().getExhibitorDataList().get(i).getExhibitor_id().equalsIgnoreCase(ExhibitorDetailActivity.Exhi_id)) {
                Intent attendeetail = new Intent(EditExhibitorActivity.this, ExhibitorDetailActivity.class);
                attendeetail.putExtra("address", response.body().getExhibitorDataList().get(i).getAddress());
                attendeetail.putExtra("allowedMessage", response.body().getExhibitorDataList().get(i).getAllowed_message());
                attendeetail.putExtra("allowedsetupMeeting", response.body().getExhibitorDataList().get(i).getAllowed_setup_meeting());
                attendeetail.putExtra("ExhiCatId", response.body().getExhibitorDataList().get(i).getExhibitor_category_id());
                attendeetail.putExtra("Exhi_id", response.body().getExhibitorDataList().get(i).getExhibitor_id());
                attendeetail.putExtra("ExhiLogo", response.body().getExhibitorDataList().get(i).getLogo());
                attendeetail.putExtra("ExhiName", response.body().getExhibitorDataList().get(i).getName());
                attendeetail.putExtra("StallNum", response.body().getExhibitorDataList().get(i).getStall_number());
                attendeetail.putExtra("TileImage", response.body().getExhibitorDataList().get(i).getTile_image());
                attendeetail.putExtra("CatName", ExhibitorDetailActivity.CatName);
                startActivity(attendeetail);
                finish();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
