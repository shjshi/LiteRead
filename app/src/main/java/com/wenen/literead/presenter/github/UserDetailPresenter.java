package com.wenen.literead.presenter.github;

import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.activity.github.UserDetailActivity;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.github.UserDetailContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.github.GithubLoginModel;
import com.wenen.literead.presenter.BasePresenter;

/**
 * Created by Wen_en on 16/9/14.
 */
public class UserDetailPresenter extends BasePresenter implements UserDetailContract.Presenter {
    UserDetailActivity.ViewHolder viewHolder;

    public UserDetailPresenter(UserDetailContract.View view) {
        super(view);
        viewHolder = view.getViewHolder();
    }

    @Override
    public void githubSearch() {
        viewHolder.subscriber = new HttpSubscriber<GithubLoginModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showSnackBar(indeterminateHorizontalProgressToolbar, e.toString(), null);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                ImageLoaderConfig.imageLoader.displayImage(githubUser.getGithubLoginModel().avatar_url, viewHolder.ivAvatar,
                        ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
                if (githubUser.getGithubLoginModel().bio != null)
                    viewHolder.tvBio.setText("Bio:" + githubUser.getGithubLoginModel().bio);
                viewHolder.tvName.setText(githubUser.getGithubLoginModel().name);
                if (githubUser.getGithubLoginModel().blog != null)
                    viewHolder.tvBlog.setText("Blog:" + githubUser.getGithubLoginModel().blog);
                assert viewHolder.tbTab != null;
                assert viewHolder.vpGithub != null;
                viewHolder.tbTab.setupWithViewPager(viewHolder.vpGithub);
                if (viewHolder.vpGithub != null)
                    viewHolder.vpGithub.setAdapter(viewHolder.githubPageAdapter);
            }

            @Override
            public void onNext(GithubLoginModel githubLoginModel) {
                super.onNext(githubLoginModel);
                updateGithubUserData(githubLoginModel);
                githubUser.setName(viewHolder.username);
            }
        };
        HttpClient.getSingle(APIUrl.GITHUB_BASE_URL).GithubLogin(viewHolder.username, APIUrl.GITHUB_CLIENT_ID,
                APIUrl.GITHUB_CECRET, viewHolder.subscriber);
    }
}
