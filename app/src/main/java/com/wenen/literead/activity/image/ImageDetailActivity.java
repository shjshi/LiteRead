package com.wenen.literead.activity.image;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wenen.literead.R;
import com.wenen.literead.activity.BaseActivity;
import com.wenen.literead.adapter.image.ImageDetailsAdapter;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.image.ImageDetailContract;
import com.wenen.literead.presenter.image.ImageDetailPresenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ImageDetailActivity extends BaseActivity implements ImageDetailContract.View {


    @Override
    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder {
        @Bind(R.id.toolbar)
        public Toolbar toolbar;
        @Bind(R.id.container)
        public ViewPager container;
        @Bind(R.id.fab)
        public FloatingActionButton fab;
        @Bind(R.id.indeterminate_horizontal_progress_toolbar)
        public MaterialProgressBar indeterminateHorizontalProgressToolbar;

        public ImageDetailsAdapter mSectionsPagerAdapter;
        public ArrayList<String> listl;
        public String title;
        public int position;
        public int currentPage;
        public ViewPager mViewPager;
        public boolean b;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private ViewHolder viewHolder;
    private ImageDetailPresenter imageDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_image_detail, null, savedInstanceState);
        setContentView(getRootView());
        viewHolder = new ViewHolder(getRootView());
        imageDetailPresenter = new ImageDetailPresenter(this);
        viewHolder.listl = getIntent().getStringArrayListExtra("listUrls");
        viewHolder.title = getIntent().getStringExtra("title");
        viewHolder.position = getIntent().getIntExtra("position", 0);
        viewHolder.b = getIntent().getBooleanExtra("isNeadAddHead", true);
        viewHolder.currentPage = viewHolder.position;
        viewHolder.mSectionsPagerAdapter = new ImageDetailsAdapter(getSupportFragmentManager(), viewHolder.listl,
                viewHolder.title, viewHolder.position, viewHolder.b);
        viewHolder.mViewPager = (ViewPager) findViewById(R.id.container);
        viewHolder.mViewPager.setAdapter(viewHolder.mSectionsPagerAdapter);
        viewHolder.mViewPager.setCurrentItem(viewHolder.position);

        viewHolder.mViewPager.setOffscreenPageLimit(viewHolder.listl.size());
        viewHolder.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewHolder.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewHolder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDetailPresenter.setProgressBarISvisible(viewHolder.indeterminateHorizontalProgressToolbar, true);
                imageDetailPresenter.downLoadFile(APIUrl.imgUrl + viewHolder.listl.get(viewHolder.position));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewHolder.toolbar.setTitle(viewHolder.title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewHolder = null;
    }
}
