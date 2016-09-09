package com.wenen.literead.ui.github;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.R;
import com.wenen.literead.fragment.github.FollowersFragment;
import com.wenen.literead.ui.BaseActivity;
import com.wenen.literead.view.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Wen_en on 16/9/7.
 */
public class UserDetail extends BaseActivity {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);
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
        if (savedInstanceState != null) {

        } else {
            githubPageAdapter = new GithubPageAdapter(getSupportFragmentManager());
            vpGithub.setAdapter(githubPageAdapter);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(getString(R.string.github));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
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
}
