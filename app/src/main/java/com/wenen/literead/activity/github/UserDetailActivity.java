package com.wenen.literead.activity.github;

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
import com.wenen.literead.contract.github.UserDetailContract;
import com.wenen.literead.fragment.github.FollowersFragment;
import com.wenen.literead.presenter.github.UserDetailPresenter;
import com.wenen.literead.view.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/7.
 */
public class UserDetailActivity extends BaseActivity implements UserDetailContract.View {

    @Override
    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder {
        @Bind(R.id.toolbar)
        public Toolbar toolbar;
        @Bind(R.id.indeterminate_horizontal_progress_toolbar)
        public MaterialProgressBar indeterminateHorizontalProgressToolbar;
        @Bind(R.id.iv_avatar)
        public CircleImageView ivAvatar;
        @Bind(R.id.tv_bio)
        public AppCompatTextView tvBio;
        @Bind(R.id.tv_name)
        public AppCompatTextView tvName;
        @Bind(R.id.tv_blog)
        public AppCompatTextView tvBlog;
        @Bind(R.id.tb_tab)
        public TabLayout tbTab;
        @Bind(R.id.vp_github)
        public ViewPager vpGithub;
        public GithubPageAdapter githubPageAdapter;
        public String[] titles = new String[]{"Followers", "Following", "Started", "Repo"};
        public Subscriber subscriber;
        public String username;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    ViewHolder viewHolder;
    private UserDetailPresenter userDetailPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_user_detail, null, savedInstanceState);
        setContentView(getRootView());
        viewHolder = new ViewHolder(getRootView());
        ButterKnife.bind(this);
        viewHolder.githubPageAdapter = new GithubPageAdapter(getSupportFragmentManager());
        userDetailPresenter = new UserDetailPresenter(this);
        if (savedInstanceState != null) {
            viewHolder.username = savedInstanceState.getString("username");
            userDetailPresenter.githubSearch();
        } else {
            if (getIntent().getStringExtra("username") != null) {
                viewHolder.username = getIntent().getStringExtra("username");
                userDetailPresenter.githubSearch();
            } else {
                ImageLoaderConfig.imageLoader.displayImage(userDetailPresenter.githubUser.getGithubLoginModel().avatar_url, viewHolder.ivAvatar,
                        ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
                if (githubUser.getGithubLoginModel().bio != null)
                    viewHolder.tvBio.setText("Bio:" + userDetailPresenter.githubUser.getGithubLoginModel().bio);
                viewHolder.tvName.setText(githubUser.getGithubLoginModel().name);
                if (githubUser.getGithubLoginModel().blog != null)
                    viewHolder.tvBlog.setText("Blog:" + userDetailPresenter.githubUser.getGithubLoginModel().blog);
            }
        }
        assert viewHolder.tbTab != null;
        assert viewHolder.vpGithub != null;
        viewHolder.tbTab.setupWithViewPager(viewHolder.vpGithub);
        viewHolder.githubPageAdapter = new GithubPageAdapter(getSupportFragmentManager());
        viewHolder.vpGithub.setAdapter(viewHolder.githubPageAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", viewHolder.username);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewHolder.toolbar.setTitle(getString(R.string.github));
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
            return viewHolder.titles[position];
        }
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewHolder = null;
    }
}
