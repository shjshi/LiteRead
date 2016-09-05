package com.wenen.literead.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.ImageLoaderConfig.URLImageGetter;
import com.wenen.literead.R;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.zhihu.ZhihuDetailModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

public class ZhihuDetailActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.indeterminate_horizontal_progress_toolbar)
    MaterialProgressBar indeterminateHorizontalProgressToolbar;
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
    private Subscriber subscriber;
    private int id;
    private Document document;
    private String content;
    private String title;
    private String imgurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            id = getIntent().getIntExtra("id", 0);
            title = getIntent().getStringExtra("title");
            imgurl = getIntent().getStringExtra("imgUrl");
        } else {
            imgurl = savedInstanceState.getString("imgUrl");
            id = savedInstanceState.getInt("id");
            title = savedInstanceState.getString("title");
        }
        getZhihuDetail();
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
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        ImageLoaderConfig.imageLoader.displayImage(imgurl, ivImageView,
                ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
    }

    private void getZhihuDetail() {
        subscriber = new HttpSubscriber<ZhihuDetailModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onNext(ZhihuDetailModel zhihuDetailModel) {
                super.onNext(zhihuDetailModel);
                document = Jsoup.parse(zhihuDetailModel.body);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                Log.e("document", document.outerHtml());
                Elements elements = document.select("h2.question-title");
                ImageLoaderConfig.imageLoader.displayImage(document.select("img.avatar").first().text(), ivAuthor,
                        ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
                tvZhihuDetailTitle.setText(elements.first().text());
                ImageLoaderConfig.imageLoader.displayImage(document.select("img.avatar").first().attr("src"), ivAuthor, ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
                tvAuthor.setText(document.select("span.author").first().text());
                content = document.select("div.content").first().html();
                tvZhihuDetail.setText(Html.fromHtml(content, new URLImageGetter(content, ZhihuDetailActivity.this, tvZhihuDetail, ImageLoaderConfig.options), null));
            }
        };
        HttpClient.getSingle(APIUrl.ZHIHU_BASE_URL).getZhihuDetail(id, subscriber);
    }


}
