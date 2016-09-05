package com.wenen.literead.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.wenen.literead.R;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.fragment.ImageListFragment;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.image.ImageTypeListModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    @Bind(R.id.indeterminate_horizontal_progress_toolbar)
    MaterialProgressBar indeterminateHorizontalProgressToolbar;
    private Subscriber subscriber;
    private ArrayList<String> titleList = new ArrayList<>();
    private MainPageViewAdapter mainPageViewAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        assert mainPagerTabs != null;
        assert mainPager != null;
        mainPagerTabs.setupWithViewPager(mainPager);
        if (savedInstanceState == null) {
            getIMGTypeList();
        } else {
            titleList = savedInstanceState.getStringArrayList("titleList");
            if (mainPager.getAdapter() == null) {
                mainPageViewAdapter = new MainPageViewAdapter(getSupportFragmentManager());
                mainPager.setAdapter(mainPageViewAdapter);
            } else
                mainPager.getAdapter().notifyDataSetChanged();
            mainPager.setOffscreenPageLimit(titleList.size());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putStringArrayList("titleList", titleList);
    }

    @Override
    protected void onResume() {
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_slideshow) {
            Intent intent = new Intent();
            intent.setClass(this, VideoListActivity.class);
            intent.putExtra("title", "直播");
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent();
            intent.setClass(this, ArticleListActivity.class);
            intent.putExtra("title", "Android开发");
            startActivity(intent);
        } else if (id == R.id.nav_zhihu) {
            Intent intent = new Intent();
            intent.setClass(this, ZhihuListActivity.class);
            intent.putExtra("title", getString(R.string.zhihu_daily));
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void getIMGTypeList() {
        subscriber = new HttpSubscriber<ImageTypeListModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                if (mainPager.getAdapter() == null) {
                    mainPageViewAdapter = new MainPageViewAdapter(getSupportFragmentManager());
                    mainPager.setAdapter(mainPageViewAdapter);
                } else
                    mainPager.getAdapter().notifyDataSetChanged();
                mainPager.setOffscreenPageLimit(titleList.size());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mainPagerTabs.setVisibility(View.GONE);
                showSnackBar(indeterminateHorizontalProgressToolbar, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainPagerTabs.setVisibility(View.VISIBLE);
                        getIMGTypeList();
                    }
                });

            }

            @Override
            public void onNext(ImageTypeListModel imageTypeListModel) {
                super.onNext(imageTypeListModel);
                if (imageTypeListModel.status) {
                    for (ImageTypeListModel.TngouEntity tnEntity : imageTypeListModel.tngou
                            ) {
                        Log.e("Title", tnEntity.title);
                        titleList.add(tnEntity.title);
                        ImageListFragment imageListFragment = new ImageListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", tnEntity.id);
                        imageListFragment.setArguments(bundle);
                        Log.e("id", tnEntity.id + "");
                        fragments.add(imageListFragment);
                    }
                } else
                    showSnackBar(indeterminateHorizontalProgressToolbar, null, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getIMGTypeList();
                        }
                    });
            }
        };
        HttpClient.getSingle(APIUrl.TIANGOU_IMG_URL).getIMGTypeList(subscriber);
    }
}
