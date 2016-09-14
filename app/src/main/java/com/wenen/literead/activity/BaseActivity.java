package com.wenen.literead.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.wenen.literead.R;
import com.wenen.literead.contract.BaseContract;
import com.wenen.literead.elegate.IDelegate;
import com.wenen.literead.model.github.GithubUser;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Wen_en on 16/8/16.
 */
public class BaseActivity extends AppCompatActivity implements IDelegate, BaseContract.View {
    public static GithubUser githubUser;
    public View view;
    private Toolbar toolbar;
    public IDelegate iDelegate;
    private boolean b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iDelegate = this;
        githubUser = GithubUser.getSingle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!b) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        }
    }


    @Override
    public void create(int layoutId, ViewGroup v, Bundle b) {
        view = getLayoutInflater().inflate(layoutId, v, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);
    }

    @Override
    public View getRootView() {
        return view;
    }

    @Override
    public void canTSetToolBar(boolean b) {
        this.b = b;
    }

    @Override
    public MaterialProgressBar getProgressBar() {
        return (MaterialProgressBar) view.findViewById(R.id.indeterminate_horizontal_progress_toolbar);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public GithubUser getGitHubUser() {
        return githubUser;
    }
}
