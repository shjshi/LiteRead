package com.wenen.literead.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenen.literead.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends Activity {

    @Bind(R.id.fullscreen_content)
    TextView fullscreenContent;
    @Bind(R.id.dummy_button)
    Button dummyButton;
    @Bind(R.id.fullscreen_content_controls)
    LinearLayout fullscreenContentControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        postHandler.sendEmptyMessage(0x123);
    }

    Handler postHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                    finish();
                }
            }, 3000);
        }
    };
}
