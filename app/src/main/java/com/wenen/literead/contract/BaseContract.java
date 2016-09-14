package com.wenen.literead.contract;

import android.content.Context;
import android.support.annotation.NonNull;

import com.wenen.literead.model.github.GithubLoginModel;
import com.wenen.literead.model.github.GithubUser;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface BaseContract {
    interface View {
        MaterialProgressBar getProgressBar();
        Context getContext();
        GithubUser getGitHubUser();
    }

    interface Model {
    }

    interface Presenter {
        void showSnackBar(@NonNull android.view.View view, String msg,
                          android.view.View.OnClickListener onClickListener);
        void setProgressBarISvisible(android.view.View view, boolean iSvisible);
        void updateGithubUserData(GithubLoginModel githubLoginModel);
    }
}
