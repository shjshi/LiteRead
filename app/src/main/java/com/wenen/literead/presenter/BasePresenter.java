package com.wenen.literead.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.wenen.literead.contract.BaseContract;
import com.wenen.literead.model.github.GithubLoginModel;
import com.wenen.literead.model.github.GithubUser;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Wen_en on 16/9/14.
 */
public class BasePresenter implements BaseContract.Presenter {
    public MaterialProgressBar indeterminateHorizontalProgressToolbar;
    private BaseContract.View view;
    public Context context;
    public GithubUser githubUser;

    public BasePresenter(BaseContract.View view) {
        this.view = view;
        githubUser = view.getGitHubUser();
        context = view.getContext();
        indeterminateHorizontalProgressToolbar = view.getProgressBar();
    }

    @Override
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

    @Override
    public void setProgressBarISvisible(View view, boolean iSvisible) {
        if (iSvisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateGithubUserData(GithubLoginModel githubLoginModel) {
        githubUser.setGithubLoginModel(githubLoginModel);
    }
}
