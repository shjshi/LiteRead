package com.wenen.literead.activity.github;

import android.animation.ObjectAnimator;
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
import android.view.View;

import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.R;
import com.wenen.literead.activity.BaseActivity;
import com.wenen.literead.contract.activity.github.UserDetailContract;
import com.wenen.literead.fragment.github.FollowersFragment;
import com.wenen.literead.model.github.GithubLoginModel;
import com.wenen.literead.presenter.activity.github.UserDetailPresenter;
import com.wenen.literead.view.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Wen_en on 16/9/7.
 */
public class UserDetailActivity extends BaseActivity implements UserDetailContract.View {
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
    private String username;
    private ObjectAnimator objectAnimator;

    private UserDetailPresenter userDetailPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_user_detail, null, savedInstanceState);
        setContentView(getRootView());
        ButterKnife.bind(this);
        githubPageAdapter = new GithubPageAdapter(getSupportFragmentManager());
        userDetailPresenter = new UserDetailPresenter(this);
        if (savedInstanceState != null) {
            username = savedInstanceState.getString("username");
            getData();
        } else {
            if (getIntent().getStringExtra("username") != null) {
                username = getIntent().getStringExtra("username");
                getData();
            } else {
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

    @Override
    public void showData(GithubLoginModel githubLoginModel) {
        updateGithubUserData(githubLoginModel);
        githubUser.setName(username);
        ImageLoaderConfig.imageLoader.displayImage(githubUser.getGithubLoginModel().avatar_url, ivAvatar,
                ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
        objectAnimator = ObjectAnimator.ofFloat(ivAvatar, "rotationY", 0, -360);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
        if (githubUser.getGithubLoginModel().bio != null)
            tvBio.setText("Bio:" + githubUser.getGithubLoginModel().bio);
        tvName.setText(githubUser.getGithubLoginModel().name);
        if (githubUser.getGithubLoginModel().blog != null)
            tvBlog.setText("Blog:" + githubUser.getGithubLoginModel().blog);
        assert tbTab != null;
        assert vpGithub != null;
        tbTab.setupWithViewPager(vpGithub);
        if (vpGithub != null)
            vpGithub.setAdapter(githubPageAdapter);
    }

    @Override
    public void refreashData(String username) {
        this.username=username;
        userDetailPresenter.githubSearch(username);
    }

    @Override
    public void showError(String s, View.OnClickListener listener) {
        showSnackBar(indeterminateHorizontalProgressToolbar, s, listener);
    }

    @Override
    public void getData() {
        userDetailPresenter.githubSearch(username);
    }

    @Override
    public void addTaskListener() {
    }

    @Override
    public MaterialProgressBar getProgressBar() {
        return indeterminateHorizontalProgressToolbar;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log_out:
                githubUser.setAutoLogin(false);
                Intent intent=new Intent();
                intent.putExtra("title",getString(R.string.github));
                intent.setClass(this, GitSearchActivity.class);
                startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userDetailPresenter = null;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            objectAnimator = null;
        }

    }
}
