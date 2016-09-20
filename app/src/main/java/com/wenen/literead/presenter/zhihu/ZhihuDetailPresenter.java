package com.wenen.literead.presenter.zhihu;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thefinestartist.finestwebview.FinestWebView;
import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.R;
import com.wenen.literead.activity.image.ImageDetailActivity;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.zhihu.ZhihuDetailContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.zhihu.ZhihuDetailModel;
import com.wenen.literead.presenter.BasePresenter;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnImageClickListener;
import com.zzhoujay.richtext.callback.OnURLClickListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/14.
 */
public class ZhihuDetailPresenter extends BasePresenter implements ZhihuDetailContract.Presenter {
    private Subscriber subscriber;
    private Document document;
    private String content;
    private AppCompatTextView tvZhihuDetailTitle;
    private AppCompatTextView tvZhihuDetail;
    private TextView tvAuthor;
    private int id;
    private ImageView ivAuthor;
    private String title;

    public ZhihuDetailPresenter(ZhihuDetailContract.View view) {
        super(view);
        id = view.getId();
        tvAuthor = view.getAuthor();
        tvZhihuDetail = view.getZhihuDetailTxt();
        tvZhihuDetailTitle = view.getZhihuDetailTitle();
        ivAuthor = view.getIvAuthor();
    }

    @Override
    public void getZhihuDetail() {
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
                    RichText.from(content).async(true).clickable(true).imageClick(new OnImageClickListener() {
                        @Override
                        public void imageClicked(List<String> imageUrls, int position) {
                            Intent intent = new Intent(context, ImageDetailActivity.class);
                            intent.putStringArrayListExtra("listUrls", (ArrayList<String>) imageUrls);
                            intent.putExtra("title", title);
                            intent.putExtra("position", position);
                            intent.putExtra("isNeadAddHead", false);
                            context.startActivity(intent);

                        }
                    }).urlClick(new OnURLClickListener() {
                        @Override
                        public boolean urlClicked(String url) {
                            new FinestWebView.Builder(context).statusBarColorRes(R.color.colorPrimary)
                                    .progressBarColorRes(R.color.colorPrimary).toolbarColorRes(R.color.colorPrimary).titleColorRes(R.color.white)
                                    .menuColorRes(R.color.white).iconDefaultColorRes(R.color.white)
                                    .show(url);
                            return true;
                        }
                    }).into(tvZhihuDetail);
                }
            }

        };
        HttpClient.getSingle(APIUrl.ZHIHU_BASE_URL).getZhihuDetail(id, subscriber);
    }
}
