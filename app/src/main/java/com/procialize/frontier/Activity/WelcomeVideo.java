package com.procialize.frontier.Activity;

import androidx.appcompat.app.AppCompatActivity;
import com.procialize.frontier.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class WelcomeVideo extends AppCompatActivity {

    WebView web_video;
    ImageView img_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_video);

        web_video=findViewById(R.id.web_video);
        img_cancel=findViewById(R.id.img_cancel);

        web_video.loadUrl("https://www.procialize.live/frontier/uploads/welcome.mp4");

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplicationContext(), ProfileActivity.class);
                home.putExtra("back", "1");
                startActivity(home);
                finish();
            }
        });
    }
}