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
import com.wenen.literead.animation.ZoomOutPageTransformer;
import com.wenen.literead.contract.activity.video.VideoListContract;
import com.wenen.literead.presenter.activity.video.VideoListPresenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

public class VideoListActivity extends BaseActivity implements VideoListContract.View {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_pager_tabs)
    TabLayout mainPagerTabs;
    @Bind(R.id.indeterminate_horizontal_progress_toolbar)
    MaterialProgressBar indeterminateHorizontalProgressToolbar;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.main_pager)
    ViewPager mainPager;
    private Subscriber subscriber;
    private ArrayList<String> titleList = new ArrayList<>();
    private MainPageViewAdapter mainPageViewAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String title;


    private VideoListPresenter videoListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_video_list, null, savedInstanceState);
        setContentView(getRootView());
        ButterKnife.bind(this);
        mainPageViewAdapter = new MainPageViewAdapter(getSupportFragmentManager());
        videoListPresenter = new VideoListPresenter(this);
        assert mainPagerTabs != null;
        assert mainPager != null;
        mainPagerTabs.setupWithViewPager(mainPager);
        if (savedInstanceState == null) {
            getData();
        } else {
            titleList = savedInstanceState.getStringArrayList("list");
            if (mainPager.getAdapter() == null) {
                mainPager.setAdapter(mainPageViewAdapter);
            } else
                mainPager.getAdapter().notifyDataSetChanged();
            mainPager.setOffscreenPageLimit(titleList.size());
        }
        mainPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("list", titleList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);
    }


    private class MainPageViewAdapter extends FragmentStatePagerAdapter {

        public MainPageViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("fragments", position + fragments.get(position).getArguments().getString("url"));
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return titleList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoListPresenter = null;
    }


    @Override
    public void showData(ArrayList<String> titleList, ArrayList<Fragment> fragments) {
        this.titleList = titleList;
        this.fragments = fragments;
        if (mainPager.getAdapter() == null) {
            mainPager.setAdapter(mainPageViewAdapter);
        } else
            mainPager.getAdapter().notifyDataSetChanged();
        mainPager.setOffscreenPageLimit(titleList.size());
    }

    @Override
    public void showError(String s, View.OnClickListener listener) {
        showSnackBar(indeterminateHorizontalProgressToolbar, s, listener);
    }

    @Override
    public void getData() {
        videoListPresenter.getVideoList();
    }

    @Override
    public void addTaskListener() {
        videoListPresenter.addTaskListener(this);
    }

    @Override
    public MaterialProgressBar getProgressBar() {
        return indeterminateHorizontalProgressToolbar;
    }

}
