package com.wenen.literead.presenter.zhihu;

import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.ImageLoaderConfig.URLImageGetter;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.zhihu.ZhihuDetailContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.zhihu.ZhihuDetailModel;
import com.wenen.literead.presenter.BasePresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
                    tvZhihuDetailTitle.setText(elements.first().text());
                    if (document.select("img.avatar") != null && document.select("img.avatar").first() != null)
                        ImageLoaderConfig.imageLoader.displayImage(document.select("img.avatar").first().attr("src"), ivAuthor,
                                ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
                    if (document.select("span.author") != null && document.select("span.author").first() != null)
                        tvAuthor.setText(document.select("span.author").first().text());
                    if (document.select("div.content") != null && document.select("div.content").first() != null)
                        content = document.select("div.content").first().html();
                    if (Build.VERSION.SDK_INT < 24) {
                        tvZhihuDetail.setText(Html.fromHtml(content, new URLImageGetter(content,context
                                , tvZhihuDetail, ImageLoaderConfig.options), null));
                    } else
                        tvZhihuDetail.setText(Html.fromHtml(content, 0, new URLImageGetter(content,
                                context, tvZhihuDetail, ImageLoaderConfig.options), null));
                }
            }
        };
        HttpClient.getSingle(APIUrl.ZHIHU_BASE_URL).getZhihuDetail(id, subscriber);
    }
}
