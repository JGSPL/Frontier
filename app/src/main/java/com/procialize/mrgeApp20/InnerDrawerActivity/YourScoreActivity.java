package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Utility.Util;

import java.io.File;

public class YourScoreActivity extends AppCompatActivity {
    TextView txt_count, questionTv, txt_title;
    Button btn_ok;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    LinearLayout linear;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_score);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");


        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        txt_count = findViewById(R.id.txt_count);
        questionTv = findViewById(R.id.questionTv);
        btn_ok = findViewById(R.id.btn_ok);
        linear = findViewById(R.id.linear);
        txt_title = findViewById(R.id.txt_title);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        btn_ok.setBackgroundColor(Color.parseColor(colorActive));
        txt_count.setTextColor(Color.parseColor(colorActive));

//        Typeface typeface = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
//        txt_count.setTypeface(typeface);
        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
        final Intent intent = getIntent();
        String folderName = intent.getStringExtra("folderName");
        String correnctcount = intent.getStringExtra("Answers");
        String totalcount = intent.getStringExtra("TotalQue");

        questionTv.setText(folderName);
        txt_count.setText(correnctcount + "/" + totalcount);
        questionTv.setBackgroundColor(Color.parseColor(colorActive));
        txt_title.setTextColor(Color.parseColor(colorActive));
        QuizActivity.submitflag = false;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(YourScoreActivity.this, FolderQuizActivity.class);
                startActivity(intent1);
                finish();
            }
        });


    }
}
