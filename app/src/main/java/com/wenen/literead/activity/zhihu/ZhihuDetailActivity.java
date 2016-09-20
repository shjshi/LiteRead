package com.wenen.literead.activity.zhihu;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.R;
import com.wenen.literead.activity.BaseActivity;
import com.wenen.literead.contract.zhihu.ZhihuDetailContract;
import com.wenen.literead.presenter.zhihu.ZhihuDetailPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ZhihuDetailActivity extends BaseActivity implements ZhihuDetailContract.View {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.tv_zhihu_detail_title)
    AppCompatTextView tvZhihuDetailTitle;
    @Bind(R.id.iv_author)
    ImageView ivAuthor;
    @Bind(R.id.tv_author)
    TextView tvAuthor;
    @Bind(R.id.tv_zhihu_detail)
    AppCompatTextView tvZhihuDetail;
    @Bind(R.id.iv_imageView)
    ImageView ivImageView;
    private int id;
    private String title;
    private String imgurl;
    private ZhihuDetailPresenter zhihuDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_zhihu_detail, null, savedInstanceState);
        setContentView(getRootView());
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            id = getIntent().getIntExtra("id", 0);
            title = getIntent().getStringExtra("title");
            imgurl = getIntent().getStringExtra("imgUrl");
        } else {
            imgurl = savedInstanceState.getString("imgUrl");
            id = savedInstanceState.getInt("id");
            title = savedInstanceState.getString("title");
        }
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            }
        });
        zhihuDetailPresenter = new ZhihuDetailPresenter(this);
        zhihuDetailPresenter.getZhihuDetail();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", title);
        outState.putInt("id", id);
        outState.putString("imgUrl", imgurl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("title", title);
        toolbarLayout.setTitle(title);
        ImageLoaderConfig.imageLoader.displayImage(imgurl, ivImageView,
                ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
    }


    @Override
    public AppCompatTextView getZhihuDetailTitle() {
        return tvZhihuDetailTitle;
    }

    @Override
    public TextView getAuthor() {
        return tvAuthor;
    }

    @Override
    public AppCompatTextView getZhihuDetailTxt() {
        return tvZhihuDetail;
    }

    @Override
    public ImageView getZhihuImage() {
        return ivImageView;
    }

    @Override
    public ImageView getIvAuthor() {
        return ivAuthor;
    }

    @Override
    public int getId() {
        return id;
    }
}
