package com.procialize.mrgeApp20.BuddyList.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.R;

public class ActivityBuddyChat extends AppCompatActivity {

    String attendeeid, name, city, country, company, designation, description, totalrating, profile, mobile;
    ImageView iv_buddy_details,profileIV;
    TextView title, sub_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });

        try {
            attendeeid = getIntent().getExtras().getString("id");
            name = getIntent().getExtras().getString("name");
            city = getIntent().getExtras().getString("city");
            country = getIntent().getExtras().getString("country");
            company = getIntent().getExtras().getString("company");
            designation = getIntent().getExtras().getString("designation");
            description = getIntent().getExtras().getString("description");
            totalrating = getIntent().getExtras().getString("totalrating");
            profile = getIntent().getExtras().getString("profile");
            mobile = getIntent().getExtras().getString("mobile");
        } catch (Exception e) {
            e.printStackTrace();
        }

        profileIV = findViewById(R.id.profileIV);
        if (profile != null) {
            Glide.with(this).load(ApiConstant.profilepic + profile).circleCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            profileIV.setImageResource(R.drawable.profilepic_placeholder);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(profileIV);
        } else {
        }

        title = findViewById(R.id.title);
        sub_title = findViewById(R.id.sub_title);

        title.setText(name);
        sub_title.setText(designation + " - " + city);

        iv_buddy_details = findViewById(R.id.iv_buddy_details);
        iv_buddy_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendeetail = new Intent(ActivityBuddyChat.this, ActivityBuddyDetails.class);
                attendeetail.putExtra("id", attendeeid);
                attendeetail.putExtra("name", name);
                attendeetail.putExtra("city", city);
                attendeetail.putExtra("country", country);
                attendeetail.putExtra("company", company);
                attendeetail.putExtra("designation", designation);
                attendeetail.putExtra("description", description);
                attendeetail.putExtra("profile", profile);
                attendeetail.putExtra("mobile", mobile);
//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
                startActivity(attendeetail);
            }
        });
    }
}
