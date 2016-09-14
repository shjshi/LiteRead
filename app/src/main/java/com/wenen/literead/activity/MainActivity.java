package com.wenen.literead.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wenen.literead.R;
import com.wenen.literead.contract.MainContract;
import com.wenen.literead.presenter.MainPresenter;
import com.wenen.literead.activity.article.ArticleListActivity;
import com.wenen.literead.activity.github.GitSearchActivity;
import com.wenen.literead.activity.video.VideoListActivity;
import com.wenen.literead.activity.zhihu.ZhihuListActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_pager_tabs)
    TabLayout mainPagerTabs;
    @Bind(R.id.main_pager)
    ViewPager mainPager;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();
    private MainPageViewAdapter mainPageViewAdapter;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_main, null, savedInstanceState);
        setContentView(getRootView());
        ButterKnife.bind(this);
        mainPageViewAdapter = new MainPageViewAdapter(getSupportFragmentManager());
        mainPresenter = new MainPresenter(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        assert mainPagerTabs != null;
        assert mainPager != null;
        mainPagerTabs.setupWithViewPager(mainPager);
        if (savedInstanceState == null) {
            mainPresenter.getIMGTypeList();
        } else {
            titleList = savedInstanceState.getStringArrayList("titleList");
            if (mainPager.getAdapter() == null) {
                mainPager.setAdapter(mainPageViewAdapter);
            } else
                mainPager.getAdapter().notifyDataSetChanged();
            mainPager.setOffscreenPageLimit(titleList.size());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("titleList", titleList);
    }

    @Override
    protected void onResume() {
        canTSetToolBar(true);
        super.onResume();
        toolbar.setTitle(getString(R.string.app_name));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.nav_video:
                intent.setClass(this, VideoListActivity.class);
                intent.putExtra("title", getString(R.string.video));
                break;
            case R.id.nav_android:
                intent.setClass(this, ArticleListActivity.class);
                intent.putExtra("title", getString(R.string.android_dev));
                intent.putExtra("type", "Android");
                break;
            case R.id.nav_zhihu:
                intent.setClass(this, ZhihuListActivity.class);
                intent.putExtra("title", getString(R.string.zhihu_daily));
                break;
            case R.id.nav_git:
                intent.setClass(this, GitSearchActivity.class);
                intent.putExtra("title", getString(R.string.github));
                break;
            case R.id.nav_ios:
                intent.setClass(this, ArticleListActivity.class);
                intent.putExtra("title", getString(R.string.ios_develop));
                intent.putExtra("type", "iOS");
                break;
        }
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public ViewPager getMainPager() {
        return mainPager;
    }

    @Override
    public MainPageViewAdapter getPagerAdapter() {
        return mainPageViewAdapter;
    }

    @Override
    public TabLayout getPagerTabs() {
        return mainPagerTabs;
    }

    @Override
    public void updateData(ArrayList<Fragment> fragments, ArrayList<String> titleList) {
        this.fragments = fragments;
        this.titleList = titleList;
    }


    public class MainPageViewAdapter extends FragmentStatePagerAdapter {

        public MainPageViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
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

    }
}
