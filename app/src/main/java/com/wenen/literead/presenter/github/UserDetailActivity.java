package com.wenen.literead.presenter.github;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.R;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.fragment.github.FollowersFragment;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.github.GithubLoginModel;
import com.wenen.literead.presenter.BaseActivity;
import com.wenen.literead.view.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/7.
 */
public class UserDetailActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.indeterminate_horizontal_progress_toolbar)
    MaterialProgressBar indeterminateHorizontalProgressToolbar;
    @Bind(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @Bind(R.id.tv_bio)
    AppCompatTextView tvBio;
    @Bind(R.id.tv_name)
    AppCompatTextView tvName;
    @Bind(R.id.tv_blog)
    AppCompatTextView tvBlog;
    @Bind(R.id.tb_tab)
    TabLayout tbTab;
    @Bind(R.id.vp_github)
    ViewPager vpGithub;
    private GithubPageAdapter githubPageAdapter;
    private String[] titles = new String[]{"Followers", "Following", "Started", "Repo"};
    private Subscriber subscriber;
    private String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_user_detail, getLayoutInflater(), null, savedInstanceState);
        setContentView(getRootView());
        ButterKnife.bind(this);
        if (savedInstanceState!=null){
            username = savedInstanceState.getString("username");
            githubSearch();
        }else {
            if (getIntent().getStringExtra("username") != null){
                username = getIntent().getStringExtra("username");
                githubSearch();
            }else {
                ImageLoaderConfig.imageLoader.displayImage(githubUser.getGithubLoginModel().avatar_url, ivAvatar,
                        ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
                if (githubUser.getGithubLoginModel().bio != null)
                    tvBio.setText("Bio:" + githubUser.getGithubLoginModel().bio);
                tvName.setText(githubUser.getGithubLoginModel().name);
                if (githubUser.getGithubLoginModel().blog != null)
                    tvBlog.setText("Blog:" + githubUser.getGithubLoginModel().blog);
            }
        }
        assert tbTab != null;
        assert vpGithub != null;
        tbTab.setupWithViewPager(vpGithub);
        githubPageAdapter = new GithubPageAdapter(getSupportFragmentManager());
        vpGithub.setAdapter(githubPageAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", username);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(getString(R.string.github));
    }

    class GithubPageAdapter extends FragmentStatePagerAdapter {

        public GithubPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FollowersFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private void githubSearch() {
        subscriber = new HttpSubscriber<GithubLoginModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showSnackBar(indeterminateHorizontalProgressToolbar, e.toString(), null);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                ImageLoaderConfig.imageLoader.displayImage(githubUser.getGithubLoginModel().avatar_url, ivAvatar,
                        ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
                if (githubUser.getGithubLoginModel().bio != null)
                    tvBio.setText("Bio:" + githubUser.getGithubLoginModel().bio);
                tvName.setText(githubUser.getGithubLoginModel().name);
                if (githubUser.getGithubLoginModel().blog != null)
                    tvBlog.setText("Blog:" + githubUser.getGithubLoginModel().blog);
                assert tbTab != null;
                assert vpGithub != null;
                tbTab.setupWithViewPager(vpGithub);
                githubPageAdapter = new GithubPageAdapter(getSupportFragmentManager());
                if (vpGithub != null)
                    vpGithub.setAdapter(githubPageAdapter);
            }

            @Override
            public void onNext(GithubLoginModel githubLoginModel) {
                super.onNext(githubLoginModel);
                updateGithubUserData(githubLoginModel);
                githubUser.setName(username);
            }
        };
        HttpClient.getSingle(APIUrl.GITHUB_BASE_URL).GithubLogin(username, APIUrl.GITHUB_CLIENT_ID, APIUrl.GITHUB_CECRET, subscriber);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log_out:
                githubUser.setAutoLogin(false);
                startActivity(new Intent(this, GitSearchActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.git_user_detail, menu);
        return true;
    }
}
