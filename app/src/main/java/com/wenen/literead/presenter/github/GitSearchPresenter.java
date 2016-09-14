package com.wenen.literead.presenter.github;

import android.app.Activity;
import android.content.Intent;

import com.wenen.literead.activity.github.GitSearchActivity;
import com.wenen.literead.activity.github.UserDetailActivity;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.github.GitSearchContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.github.GithubLoginModel;
import com.wenen.literead.presenter.BasePresenter;

/**
 * Created by Wen_en on 16/9/14.
 */
public class GitSearchPresenter extends BasePresenter implements GitSearchContract.Prestener {
    private GitSearchActivity.ViewHolder viewHolder;

    public GitSearchPresenter(GitSearchContract.View view) {
        super(view);
        this.viewHolder = view.getViewHolder();
    }

    @Override
    public void githubLogin() {
        viewHolder.subscriber = new HttpSubscriber<GithubLoginModel>(viewHolder.indeterminateHorizontalProgressToolbar) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showSnackBar(viewHolder.indeterminateHorizontalProgressToolbar, e.toString(), null);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();

            }

            @Override
            public void onNext(GithubLoginModel githubLoginModel) {
                super.onNext(githubLoginModel);
                updateGithubUserData(githubLoginModel);
                githubUser.setName(viewHolder.username);
                githubUser.setAutoLogin(true);
                context.startActivity(new Intent(context, UserDetailActivity.class));
                ((Activity) context).finish();
            }
        };
        HttpClient.getSingle(APIUrl.GITHUB_BASE_URL).GithubLogin(viewHolder.username,
                APIUrl.GITHUB_CLIENT_ID, APIUrl.GITHUB_CECRET, viewHolder.subscriber);
    }
}
