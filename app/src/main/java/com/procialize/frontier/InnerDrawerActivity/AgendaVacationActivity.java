package com.procialize.frontier.InnerDrawerActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.procialize.frontier.Fragments.AgendaFolderFragment;
import com.procialize.frontier.R;
import com.procialize.frontier.Session.SessionManager;
import com.procialize.frontier.Utility.Util;

import java.util.HashMap;

import static com.procialize.frontier.util.CommonFunction.crashlytics;
import static com.procialize.frontier.util.CommonFunction.firbaseAnalytics;


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
        Fragment fragment = new AgendaFolderFragment(AgendaVacationActivity.this);
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