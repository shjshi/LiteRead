package com.wenen.literead.presenter.zhihu;

import android.os.Build;
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
import com.wenen.literead.presenter.BaseActivity;

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
       create(R.layout.activity_zhihu_detail, getLayoutInflater(), null, savedInstanceState);
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
        ImageLoaderConfig.imageLoader.displayImage(imgurl, ivImageView,
                ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
    }

    private void getZhihuDetail() {
        subscriber = new HttpSubscriber<ZhihuDetailModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onNext(ZhihuDetailModel zhihuDetailModel) {
                super.onNext(zhihuDetailModel);
                if (zhihuDetailModel.type == 0)
                    document = Jsoup.parse(zhihuDetailModel.body);
                else
                    showSnackBar(indeterminateHorizontalProgressToolbar, "此为站外文章,无法获取详情", null);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showSnackBar(indeterminateHorizontalProgressToolbar, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getZhihuDetail();
                    }
                });
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (document != null) {
                    Log.e("document", document.outerHtml());
                    Elements elements = document.select("h2.question-title");
                    tvZhihuDetailTitle.setText(elements.first().text());
                    if (document.select("img.avatar") != null && document.select("img.avatar").first() != null)
                        ImageLoaderConfig.imageLoader.displayImage(document.select("img.avatar").first().attr("src"), ivAuthor,
                                ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
                    if (document.select("span.author") != null && document.select("span.author").first() != null)
                        tvAuthor.setText(document.select("span.author").first().text());
                    if (document.select("div.content") != null && document.select("div.content").first() != null)
                        content = document.select("div.content").first().html();
                    if (Build.VERSION.SDK_INT < 24) {
                        tvZhihuDetail.setText(Html.fromHtml(content, new URLImageGetter(content,
                                ZhihuDetailActivity.this, tvZhihuDetail, ImageLoaderConfig.options), null));
                    } else
                        tvZhihuDetail.setText(Html.fromHtml(content, 0, new URLImageGetter(content,
                                ZhihuDetailActivity.this, tvZhihuDetail, ImageLoaderConfig.options), null));
                }
            }
        };
        HttpClient.getSingle(APIUrl.ZHIHU_BASE_URL).getZhihuDetail(id, subscriber);
    }
}
