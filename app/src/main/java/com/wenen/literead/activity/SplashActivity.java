package com.wenen.literead.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.wenen.literead.R;

import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        creatProgressDialog();
        postHandler.sendEmptyMessage(0x123);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
    Handler postHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    cancelProgressDialog();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                    finish();
                }
            }, 1000);
        }
    };
}
