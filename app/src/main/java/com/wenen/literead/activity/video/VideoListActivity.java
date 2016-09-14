package com.wenen.literead.activity.video;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.wenen.literead.R;
import com.wenen.literead.activity.BaseActivity;
import com.wenen.literead.contract.video.VideoListContract;
import com.wenen.literead.presenter.video.VideoListPresenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

public class VideoListActivity extends BaseActivity implements VideoListContract.View {

    @Override
    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder {
        @Bind(R.id.toolbar)
        public Toolbar toolbar;
        @Bind(R.id.main_pager_tabs)
        public TabLayout mainPagerTabs;
        @Bind(R.id.indeterminate_horizontal_progress_toolbar)
        public MaterialProgressBar indeterminateHorizontalProgressToolbar;
        @Bind(R.id.app_bar)
        public AppBarLayout appBar;
        @Bind(R.id.main_pager)
        public ViewPager mainPager;
        public Subscriber subscriber;
        public ArrayList<String> titleList = new ArrayList<>();
        public MainPageViewAdapter mainPageViewAdapter;
        public ArrayList<Fragment> fragments = new ArrayList<>();
        public String title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private ViewHolder viewHolder;
    private VideoListPresenter videoListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_video_list, null, savedInstanceState);
        setContentView(getRootView());
        viewHolder = new ViewHolder(getRootView());
        viewHolder.mainPageViewAdapter = new MainPageViewAdapter(getSupportFragmentManager());
        videoListPresenter = new VideoListPresenter(this);
        assert viewHolder.mainPagerTabs != null;
        assert viewHolder.mainPager != null;
        viewHolder.mainPagerTabs.setupWithViewPager(viewHolder.mainPager);
        if (savedInstanceState == null) {
            videoListPresenter.getVideoList();
        } else {
            viewHolder.titleList = savedInstanceState.getStringArrayList("list");
            if (viewHolder.mainPager.getAdapter() == null) {

                viewHolder.mainPager.setAdapter(viewHolder.mainPageViewAdapter);
            } else
                viewHolder.mainPager.getAdapter().notifyDataSetChanged();
            viewHolder.mainPager.setOffscreenPageLimit(viewHolder.titleList.size());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("list", viewHolder.titleList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewHolder.title = getIntent().getStringExtra("title");
        viewHolder.toolbar.setTitle(viewHolder.title);
    }


    private class MainPageViewAdapter extends FragmentStatePagerAdapter {

        public MainPageViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("fragments", position + viewHolder.fragments.get(position).getArguments().getString("url"));
            return viewHolder.fragments.get(position);

        }

        @Override
        public int getCount() {
            return viewHolder.titleList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return viewHolder.titleList.get(position);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewHolder = null;
    }
}
