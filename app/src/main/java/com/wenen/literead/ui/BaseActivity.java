package com.wenen.literead.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wenen.literead.R;

/**
 * Created by Wen_en on 16/8/16.
 */
public class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void showSnackBar(@NonNull View view, String msg, View.OnClickListener onClickListener) {
        if (msg == null)
            msg = "数据加载失败";
        if (onClickListener != null)
            Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
                    .setAction("点击重试", onClickListener).show();
        else
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                    .show();
    }

    public void setProgressBarISvisible(View view, boolean iSvisible) {
        if (iSvisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
