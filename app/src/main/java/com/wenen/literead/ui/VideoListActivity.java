package com.wenen.literead.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.wenen.literead.R;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.fragment.VideoListFragment;
import com.wenen.literead.http.HttpClient;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

public class VideoListActivity extends BaseActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        assert mainPagerTabs != null;
        assert mainPager != null;
        mainPagerTabs.setupWithViewPager(mainPager);
        if (savedInstanceState == null) {
            getVideoList();
        } else {
            titleList = savedInstanceState.getStringArrayList("list");
            if (mainPager.getAdapter() == null) {
                mainPageViewAdapter = new MainPageViewAdapter(getSupportFragmentManager());
                mainPager.setAdapter(mainPageViewAdapter);
            } else
                mainPager.getAdapter().notifyDataSetChanged();
            mainPager.setOffscreenPageLimit(titleList.size());
            setProgressBarISvisible(indeterminateHorizontalProgressToolbar,false);
        }
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

    private void getVideoList() {
        setProgressBarISvisible(indeterminateHorizontalProgressToolbar,true);
        subscriber = new Subscriber<Element>() {
            @Override
            public void onCompleted() {
                if (mainPager.getAdapter() == null) {
                    mainPageViewAdapter = new MainPageViewAdapter(getSupportFragmentManager());
                    mainPager.setAdapter(mainPageViewAdapter);
                } else
                    mainPager.getAdapter().notifyDataSetChanged();
                mainPager.setOffscreenPageLimit(titleList.size());
                setProgressBarISvisible(indeterminateHorizontalProgressToolbar,false);
            }

            @Override
            public void onError(Throwable e) {
                if (e != null)
                    Log.e("next", e.toString());
                setProgressBarISvisible(indeterminateHorizontalProgressToolbar,false);
                mainPagerTabs.setVisibility(View.GONE);
                Snackbar.make(mainPager, "数据获取失败!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("点击重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mainPagerTabs.setVisibility(View.VISIBLE);
                                getVideoList();
                            }
                        }).show();

            }

            @Override
            public void onNext(Element type) {
                Log.e("next##############", "next#######");
                Elements elements = type.select("a.btn");
                for (Element element : elements) {
                    titleList.add(element.text());
                    VideoListFragment videoListFragment = new VideoListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", element.attr("href"));
                    videoListFragment.setArguments(bundle);
                    fragments.add(videoListFragment);
                }
            }
        }
        ;
        HttpClient.getSingle(APIUrl.DOUYU_BASE_URL).
                getVideoList("/directory", subscriber);
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

}
