package com.procialize.mrgeApp20.Engagement.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.procialize.mrgeApp20.Engagement.Adapter.SwipeImageSelfieAdapter;
import com.procialize.mrgeApp20.Engagement.Adapter.SwipepagerSelfieAdapter;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.CustomTools.PicassoTrustAll;
import com.procialize.mrgeApp20.DbHelper.ConnectionDetector;
import com.procialize.mrgeApp20.GetterSetter.SelfieLike;
import com.procialize.mrgeApp20.GetterSetter.SelfieList;
import com.procialize.mrgeApp20.GetterSetter.SelfieListFetch;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwappingSelfieActivity extends AppCompatActivity implements SwipeImageSelfieAdapter.SwipeImageSelfieAdapterListner {
    public int rvposition = 0;
    String name;
    List<SelfieList> firstLevelFilters;
    SwipeImageSelfieAdapter swipeImageAdapter;
    SwipepagerSelfieAdapter swipepagerAdapter;
    ViewPager pager;
    ImageView right, left, backIv;
    ImageView headerlogoIv;
    String colorActive, eventid, user_id, token;
    String MY_PREFS_NAME = "ProcializeInfo";
    String img = "";
    RecyclerView recyclerView;
    RelativeLayout relative;
    TextView tv_name, tv_like;
    ImageView likeIv;
    ProgressBar progressBar;
    int likePos;
    private ConnectionDetector cd;
    private APIService mAPIService;

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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swapping_selfiegallery);
        cd = new ConnectionDetector(this);
        name = getIntent().getExtras().getString("url");
        firstLevelFilters = (List<SelfieList>) getIntent().getExtras().getSerializable("gallerylist");
        mAPIService = ApiUtils.getAPIService();
        recyclerView = findViewById(R.id.listrecycler);
        pager = findViewById(R.id.pager);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        likeIv = findViewById(R.id.likeIv);
        backIv = findViewById(R.id.backIv);
        relative = findViewById(R.id.relative);
        tv_name = findViewById(R.id.tv_name);
        tv_like = findViewById(R.id.tv_like);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        progressBar = findViewById(R.id.progressBar);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        eventid = prefs.getString("eventid", "1");

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        user_id = user.get(SessionManager.KEY_ID);
        token = user.get(SessionManager.KEY_TOKEN);

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            // relative.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            //  relative.setBackgroundColor(Color.parseColor("#f1f1f1"));
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
        LinearLayout linLike = findViewById(R.id.lin_like);
        TextView savebtn = findViewById(R.id.savebtn);
        TextView sharebtn = findViewById(R.id.sharebtn);


        //  linsave.setBackgroundColor(Color.parseColor(colorActive));
        //  linMsg.setBackgroundColor(Color.parseColor(colorActive));
        // savebtn.setBackgroundColor(Color.parseColor(colorActive));
        // sharebtn.setBackgroundColor(Color.parseColor(colorActive));

        linLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePos = rvposition;
                if (firstLevelFilters.get(rvposition).getLikeFlag().equals("1")) {
                    likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                    likeIv.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);


                    SelfieLike(token, eventid, firstLevelFilters.get(rvposition).getId());

                    try {

                        int count = Integer.parseInt(firstLevelFilters.get(rvposition).getTotalLikes());

                        if (count > 0) {
                            count = count - 1;
                            tv_like.setText(count + " Likes");

                        } else {
                            tv_like.setText("0 Likes");
                        }
//                SelfieLike(token,"1",selfieList.getId());/**/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_active_like));
                    likeIv.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);
                    SelfieLike(token, eventid, firstLevelFilters.get(rvposition).getId());

                    try {
                        int count = Integer.parseInt(firstLevelFilters.get(rvposition).getTotalLikes());

                        count = count + 1;
                        tv_like.setText(count + " Likes");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


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
                                    .load(ApiConstant.selfieimage + firstLevelFilters.get(rvposition).getFileName())
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
                                .load(ApiConstant.selfieimage + firstLevelFilters.get(rvposition).getFileName())
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
                shareImage(ApiConstant.selfieimage + firstLevelFilters.get(rvposition).getFileName(), SwappingSelfieActivity.this);
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
                        tv_name.setText(StringEscapeUtils.unescapeJava(firstLevelFilters.get(position).getTitle()));
                        tv_like.setText(firstLevelFilters.get(position).getTotalLikes() + " Likes");
                        if (firstLevelFilters.get(position).getLikeFlag().equals("1")) {


                            likeIv.setImageResource(R.drawable.ic_active_like);
                            likeIv.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);
                        } else {
                            likeIv.setImageResource(R.drawable.ic_like);
                            likeIv.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

                        }
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
                        tv_name.setText(StringEscapeUtils.unescapeJava(firstLevelFilters.get(position).getTitle()));
                        tv_like.setText(firstLevelFilters.get(position).getTotalLikes() + " Likes");
                        if (firstLevelFilters.get(position).getLikeFlag().equals("1")) {
                            likeIv.setImageResource(R.drawable.ic_active_like);
                            likeIv.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);
                        } else {
                            likeIv.setImageResource(R.drawable.ic_like);
                            likeIv.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                        }
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
                tv_name.setText(StringEscapeUtils.unescapeJava(firstLevelFilters.get(position).getTitle()));
                tv_like.setText(firstLevelFilters.get(position).getTotalLikes() + " Likes");
                if (firstLevelFilters.get(position).getLikeFlag().equals("1")) {
                    likeIv.setImageResource(R.drawable.ic_active_like);
                    likeIv.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);
                } else {
                    likeIv.setImageResource(R.drawable.ic_like);
                    likeIv.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                }
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
        JzvdStd.releaseAllVideos();
    }

    public void SelfieLike(String token, String eventid, String id) {
        mAPIService.SelfieLike(token, eventid, id).enqueue(new Callback<SelfieLike>() {
            @Override
            public void onResponse(Call<SelfieLike> call, Response<SelfieLike> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    showLikeResponse(response);
                } else {
                    Toast.makeText(SwappingSelfieActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SelfieLike> call, Throwable t) {
                Toast.makeText(SwappingSelfieActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLikeResponse(Response<SelfieLike> response) {
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (firstLevelFilters.get(rvposition).getLikeFlag().equals("1")) {
                firstLevelFilters.get(rvposition).setLikeFlag("0");
                try {

                    int count = Integer.parseInt(firstLevelFilters.get(rvposition).getTotalLikes());

                    if (count > 0) {
                        count = count - 1;
                        firstLevelFilters.get(rvposition).setTotalLikes(count + "");
                    } else {
                        firstLevelFilters.get(rvposition).setTotalLikes("0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                firstLevelFilters.get(rvposition).setLikeFlag("1");
                try {

                    int count = Integer.parseInt(firstLevelFilters.get(rvposition).getTotalLikes());
                    if (count > 0) {
                        count = count + 1;
                        firstLevelFilters.get(rvposition).setTotalLikes(count + "");
                    } else {
                        firstLevelFilters.get(rvposition).setTotalLikes("0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            swipepagerAdapter.notifyDataSetChanged();
            swipeImageAdapter.notifyDataSetChanged();
            //  SelfieListFetch(token, eventid);
//            Toast.makeText(SelfieContestActivity.this,response.message(),Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(SelfieContestActivity.this,response.message(),Toast.LENGTH_SHORT).show();
        }
    }


    public void SelfieListFetch(String token, String eventid) {
        showProgress();
        mAPIService.SelfieListFetch(token, eventid).enqueue(new Callback<SelfieListFetch>() {
            @Override
            public void onResponse(Call<SelfieListFetch> call, Response<SelfieListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SelfieListFetch> call, Throwable t) {
                // Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
            }
        });
    }

    public void showResponse(Response<SelfieListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            firstLevelFilters.clear();
            firstLevelFilters = response.body().getSelfieList();
            swipeImageAdapter = new SwipeImageSelfieAdapter(this, firstLevelFilters, this);
            swipeImageAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(swipeImageAdapter);
            recyclerView.scheduleLayoutAnimation();


            swipepagerAdapter = new SwipepagerSelfieAdapter(this, firstLevelFilters);
            swipepagerAdapter.notifyDataSetChanged();
            pager.setAdapter(swipepagerAdapter);
            pager.scheduleLayoutAnimation();

            pager.setCurrentItem(likePos);
            recyclerView.scrollToPosition(likePos);
        } else {
            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

}