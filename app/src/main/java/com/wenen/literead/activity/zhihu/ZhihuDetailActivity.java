package com.wenen.literead.activity.zhihu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thefinestartist.finestwebview.FinestWebView;
import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.R;
import com.wenen.literead.activity.BaseActivity;
import com.wenen.literead.activity.image.ImageDetailActivity;
import com.wenen.literead.contract.activity.zhihu.ZhihuDetailContract;
import com.wenen.literead.presenter.activity.zhihu.ZhihuDetailPresenter;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;
import com.zzhoujay.richtext.callback.OnImageClickListener;
import com.zzhoujay.richtext.callback.OnURLClickListener;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import immortalz.me.library.TransitionsHeleper;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


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
    @Bind(R.id.indeterminate_horizontal_progress_toolbar)
    MaterialProgressBar indeterminateHorizontalProgressToolbar;
    private int id;
    private String title;
    private String imgurl;
    private ZhihuDetailPresenter zhihuDetailPresenter;
    private String content;
    private MyOnImageClickListener myOnImageClickListener;
    private MyUrlClick myUrlClick;
    private ImageFixCallBack imageFixCallBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_zhihu_detail, null, savedInstanceState);
        setContentView(getRootView());
        ButterKnife.bind(this);
        imageFixCallBack = new ImageFixCallBack();
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
        getData();
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
    public void showData(Document document) {
        myOnImageClickListener = new MyOnImageClickListener();
        myUrlClick = new MyUrlClick();
        Log.e("document", document.outerHtml());
        Elements elements = document.select("h2.question-title");
        title = elements.first().text();
        if (!"".equals(title)) {
            tvZhihuDetailTitle.setText(title);
            if (document.select("img.avatar") != null && document.select("img.avatar").first() != null)
                ImageLoaderConfig.imageLoader.displayImage(document.select("img.avatar").first().attr("src"), ivAuthor,
                        ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
            if (document.select("span.author") != null && document.select("span.author").first() != null)
                tvAuthor.setText(document.select("span.author").first().text());
            if (document.select("div.content") != null && document.select("div.content").first() != null)
                content = document.select("div.content").first().html();
        } else {
            ivAuthor.setVisibility(View.GONE);
            tvAuthor.setVisibility(View.GONE);
            tvZhihuDetailTitle.setVisibility(View.GONE);
            content = document.outerHtml();
        }
        RichText.from(content).autoFix(false).fix(imageFixCallBack).async(true).clickable(true)
                .imageClick(myOnImageClickListener).urlClick(myUrlClick).imageLongClick(null).into(tvZhihuDetail);
    }

    private class ImageFixCallBack implements ImageFixCallback {
        @Override
        public void onFix(ImageHolder holder, boolean imageReady) {
            if (holder.getWidth() > 500 && holder.getHeight() > 500) {
                holder.setAutoFix(true);
            }
        }
    }

    private class MyUrlClick implements OnURLClickListener {
        /**
         * 超链接点击得回调方法
         *
         * @param url 点击得url
         * @return true：已处理，false：未处理（会进行默认处理）
         */
        @Override
        public boolean urlClicked(String url) {
            new FinestWebView.Builder(context).statusBarColorRes(R.color.colorPrimary)
                    .progressBarColorRes(R.color.colorPrimary).toolbarColorRes(R.color.colorPrimary).titleColorRes(R.color.white)
                    .menuColorRes(R.color.white).iconDefaultColorRes(R.color.white)
                    .show(url);
            return true;
        }
    }

    private class MyOnImageClickListener implements OnImageClickListener {
        /**
         * 图片被点击后的回调方法
         *
         * @param imageUrls 本篇富文本内容里的全部图片
         * @param position  点击处图片在imageUrls中的位置
         */
        @Override
        public void imageClicked(List<String> imageUrls, int position) {
            Intent intent = new Intent(context, ImageDetailActivity.class);
            intent.putStringArrayListExtra("listUrls", (ArrayList<String>) imageUrls);
            intent.putExtra("title", title);
            intent.putExtra("position", position);
            intent.putExtra("isNeadAddHead", false);
            startActivity(intent);
        }
    }

    @Override
    public void showError(String s, View.OnClickListener listener) {
        showSnackBar(indeterminateHorizontalProgressToolbar, s, listener);
    }

    @Override
    public void getData() {
        zhihuDetailPresenter = new ZhihuDetailPresenter(this);
        zhihuDetailPresenter.getZhihuDetail(id);
    }

    @Override
    public void addTaskListener() {
        zhihuDetailPresenter.addTaskListener(this);
    }

    @Override
    public MaterialProgressBar getProgressBar() {
        return indeterminateHorizontalProgressToolbar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        zhihuDetailPresenter = null;
        myOnImageClickListener = null;
        myUrlClick = null;
        imgurl = null;
        imageFixCallBack = null;
    }
}
