package com.wenen.literead.activity.image;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wenen.literead.R;
import com.wenen.literead.activity.BaseActivity;
import com.wenen.literead.adapter.image.ImageDetailsAdapter;
import com.wenen.literead.animation.ZoomOutPageTransformer;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.activity.image.ImageDetailContract;
import com.wenen.literead.presenter.activity.image.ImageDetailPresenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageDetailActivity extends BaseActivity implements ImageDetailContract.View {
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.container) ViewPager container;
  @Bind(R.id.fab) FloatingActionButton fab;
  private ImageDetailsAdapter mSectionsPagerAdapter;
  private ArrayList<String> listl;
  private String title;
  private int position;
  private int currentPage;
  private ViewPager mViewPager;
  private boolean b;

  private ViewPagerChangListener viewPagerChangListener;
  private ImageDetailPresenter imageDetailPresenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    create(R.layout.activity_image_detail, null, savedInstanceState);
    setContentView(getRootView());
    ButterKnife.bind(this);
    imageDetailPresenter = new ImageDetailPresenter(this);
    listl = getIntent().getStringArrayListExtra("listUrls");
    title = getIntent().getStringExtra("title");
    position = getIntent().getIntExtra("position", 0);
    b = getIntent().getBooleanExtra("isNeadAddHead", true);
    currentPage = position;
    mSectionsPagerAdapter =
        new ImageDetailsAdapter(getSupportFragmentManager(), listl, title, position, b);
    mViewPager = (ViewPager) findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);
    mViewPager.setCurrentItem(position);
    mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    mViewPager.setOffscreenPageLimit(listl.size());
    viewPagerChangListener = new ViewPagerChangListener();
    mViewPager.addOnPageChangeListener(viewPagerChangListener);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        getData();
      }
    });
  }

  @Override public void showMsg(String msg) {
    cancelProgressDialog();
    showSnackBar(toolbar, msg, null);
  }

  private class ViewPagerChangListener implements ViewPager.OnPageChangeListener {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override public void onPageSelected(int position) {
      ImageDetailActivity.this.position = position;
    }

    @Override public void onPageScrollStateChanged(int state) {

    }
  }

  @Override protected void onResume() {
    super.onResume();
    toolbar.setTitle(title);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    imageDetailPresenter.cancelAsyncTask();
    imageDetailPresenter = null;
    viewPagerChangListener = null;
  }

  @Override public void showError(String s, View.OnClickListener listener) {
    cancelProgressDialog();
    showSnackBar(toolbar, s, listener);
  }

  @Override public void getData() {
    creatProgressDialog("正在下载中...");
    imageDetailPresenter.downLoadFile(APIUrl.imgUrl + listl.get(position));
  }

  @Override public void addTaskListener() {
  }

}
