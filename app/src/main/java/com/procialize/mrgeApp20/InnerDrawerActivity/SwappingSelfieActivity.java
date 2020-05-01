package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.mrgeApp20.Adapter.SwipeImageSelfieAdapter;
import com.procialize.mrgeApp20.Adapter.SwipepagerSelfieAdapter;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.CustomTools.PicassoTrustAll;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.GetterSetter.SelfieList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Utility.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.jzvd.JZVideoPlayer;

public class SwappingSelfieActivity extends AppCompatActivity implements SwipeImageSelfieAdapter.SwipeImageSelfieAdapterListner {
    public int rvposition = 0;
    String name;
    List<SelfieList> firstLevelFilters;
    SwipeImageSelfieAdapter swipeImageAdapter;
    SwipepagerSelfieAdapter swipepagerAdapter;
    ViewPager pager;
    ImageView right, left, backIv;
    ImageView headerlogoIv;
    String colorActive;
    String MY_PREFS_NAME = "ProcializeInfo";
    String img = "";
    RecyclerView recyclerView;
    private ConnectionDetector cd;
    RelativeLayout relative;

    static public void shareImage(String url, final Context context) {
        Picasso.with(context).load(url).into(new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));
                context.startActivity(Intent.createChooser(i, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    static public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
//            bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(context, "com.procialize.eventsapp.android.fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swapping_selfiegallery);
        cd=new ConnectionDetector(this);
        name = getIntent().getExtras().getString("url");
        firstLevelFilters = (List<SelfieList>) getIntent().getExtras().getSerializable("gallerylist");

        recyclerView = findViewById(R.id.listrecycler);
        pager = findViewById(R.id.pager);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        backIv = findViewById(R.id.backIv);
        relative = findViewById(R.id.relative);

        headerlogoIv = findViewById(R.id.headerlogoIv);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

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
        Util.logomethod(this, headerlogoIv);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);

        swipeImageAdapter = new SwipeImageSelfieAdapter(this, firstLevelFilters, this);
        swipeImageAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(swipeImageAdapter);
        recyclerView.scheduleLayoutAnimation();


        swipepagerAdapter = new SwipepagerSelfieAdapter(this, firstLevelFilters);
        swipepagerAdapter.notifyDataSetChanged();
        pager.setAdapter(swipepagerAdapter);
        pager.scheduleLayoutAnimation();


        LinearLayout linMsg = findViewById(R.id.linMsg);
        LinearLayout linsave = findViewById(R.id.linsave);
        TextView savebtn = findViewById(R.id.savebtn);
        TextView sharebtn = findViewById(R.id.sharebtn);

        linsave.setBackgroundColor(Color.parseColor(colorActive));
        linMsg.setBackgroundColor(Color.parseColor(colorActive));
        savebtn.setBackgroundColor(Color.parseColor(colorActive));
        sharebtn.setBackgroundColor(Color.parseColor(colorActive));


        linMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {

                    try {
                        img = firstLevelFilters.get(rvposition).getFileName();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (SwappingSelfieActivity.this.checkSelfPermission(
                                "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions(SwappingSelfieActivity.this, permissions, 0);
                        } else if (SwappingSelfieActivity.this.checkSelfPermission(
                                "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions(SwappingSelfieActivity.this, permissions, 0);
                        } else {

                            // new myAsyncTask().execute();
                            PicassoTrustAll.getInstance(SwappingSelfieActivity.this)
                                    .load(ApiConstant.selfieimage+firstLevelFilters.get(rvposition).getFileName())
                                    .into(new com.squareup.picasso.Target() {
                                              @Override
                                              public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                  try {
                                                      String root = Environment.getExternalStorageDirectory().toString();
                                                      File myDir = new File(root + "/Procialize");

                                                      if (!myDir.exists()) {
                                                          myDir.mkdirs();
                                                      }
                                                      Toast.makeText(SwappingSelfieActivity.this,
                                                              "Download completed- check folder Procialize/Image",
                                                              Toast.LENGTH_SHORT).show();
                                                      String name = img + ".jpg";
                                                      myDir = new File(myDir, name);
                                                      FileOutputStream out = new FileOutputStream(myDir);
                                                      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                                      out.flush();
                                                      out.close();
                                                  } catch (Exception e) {
                                                      // some action
                                                  }
                                              }

                                              @Override
                                              public void onBitmapFailed(Drawable errorDrawable) {
                                              }

                                              @Override
                                              public void onPrepareLoad(Drawable placeHolderDrawable) {
                                              }
                                          }
                                    );

                        }


                    } else {
                        //new myAsyncTask().execute();
                        PicassoTrustAll.getInstance(SwappingSelfieActivity.this)
                                .load(ApiConstant.selfieimage+firstLevelFilters.get(rvposition).getFileName())
                                .into(new com.squareup.picasso.Target() {
                                          @Override
                                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                              try {
                                                  String root = Environment.getExternalStorageDirectory().toString();
                                                  File myDir = new File(root + "/Procialize");

                                                  if (!myDir.exists()) {
                                                      myDir.mkdirs();
                                                  }
                                                  Toast.makeText(SwappingSelfieActivity.this,
                                                          "Download completed- check folder Procialize/Image",
                                                          Toast.LENGTH_SHORT).show();
                                                  String name = img + ".jpg";
                                                  myDir = new File(myDir, name);

                                                  FileOutputStream out = new FileOutputStream(myDir);
                                                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                                  out.flush();
                                                  out.close();
                                              } catch (Exception e) {
                                                  // some action
                                              }
                                          }

                                          @Override
                                          public void onBitmapFailed(Drawable errorDrawable) {
                                          }

                                          @Override
                                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                                          }
                                      }
                                );

                    }


                } else {
                    Toast.makeText(SwappingSelfieActivity.this, "No Internet Connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        linsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(ApiConstant.selfieimage+firstLevelFilters.get(rvposition).getFileName(), SwappingSelfieActivity.this);
            }
        });


        indexset(name);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    int position = pager.getCurrentItem() + 1;

                    if (position <= firstLevelFilters.size()) {
                        pager.setCurrentItem(position);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    int position = pager.getCurrentItem() - 1;

                    if (position >= 0) {
                        pager.setCurrentItem(position);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                recyclerView.scrollToPosition(position);
                rvposition = position;

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void indexset(String name) {
        for (int j = 0; j < firstLevelFilters.size(); j++) {
            if (firstLevelFilters.get(j).getFileName().equalsIgnoreCase(name)) {
                pager.setCurrentItem(j);
            }
        }
    }

    @Override
    public void onContactSelected(SelfieList filtergallerylists) {
        indexset(filtergallerylists.getFileName());

    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
