package com.wenen.literead.presenter.activity.github;

import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.activity.github.GitSearchContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.github.GithubLoginModel;
import com.wenen.literead.presenter.activity.BasePresenter;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/14.
 */
public class GitSearchPresenter extends BasePresenter implements GitSearchContract.Prestener {
    private GitSearchContract.View view;
    public GitSearchPresenter(GitSearchContract.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public void githubLogin(String s) {
        subscriber = new HttpSubscriber<GithubLoginModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showError(e.toString(), null);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();

            }

            @Override
            public void onNext(GithubLoginModel githubLoginModel) {
                super.onNext(githubLoginModel);
                view.showData(githubLoginModel);
            }
        };
        HttpClient.getSingle(APIUrl.GITHUB_BASE_URL).GithubLogin(s,
                APIUrl.GITHUB_CLIENT_ID, APIUrl.GITHUB_CECRET,subscriber);
    }
}
