package com.procialize.mrgeApp20.InnerDrawerActivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.procialize.mrgeApp20.Fragments.AgendaFolderFragment;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.Utility.Util;

import java.util.HashMap;

import static com.procialize.mrgeApp20.util.CommonFunction.crashlytics;
import static com.procialize.mrgeApp20.util.CommonFunction.firbaseAnalytics;


public class AgendaVacationActivity extends AppCompatActivity {
    LinearLayout linear_header;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_vacation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        linear_header = findViewById(R.id.linear_header);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Fragment fragment = new AgendaFolderFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.myFrame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();


        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        final String token = user.get(SessionManager.KEY_TOKEN);
        crashlytics("Agenda Vacation",token);
        firbaseAnalytics(this,"Agenda Vacation",token);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        linear_header.setBackgroundResource(R.color.transp);
        linear_header.setAlpha(00);
    }
}