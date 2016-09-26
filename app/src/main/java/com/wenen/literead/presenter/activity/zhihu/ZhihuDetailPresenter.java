package com.wenen.literead.presenter.activity.zhihu;

import android.view.View;

import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.activity.zhihu.ZhihuDetailContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.zhihu.ZhihuDetailModel;
import com.wenen.literead.presenter.activity.BasePresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/14.
 */
public class ZhihuDetailPresenter extends BasePresenter implements ZhihuDetailContract.Presenter {
    private Subscriber subscriber;
    private Document document;
    private ZhihuDetailContract.View view;

    public ZhihuDetailPresenter(ZhihuDetailContract.View view) {
        super(view);
        this.view = view;
    }

    public ZhihuDetailPresenter addTaskListener(ZhihuDetailContract.View view) {

        return this;
    }

    @Override
    public void getZhihuDetail(final int id) {
        subscriber = new HttpSubscriber<ZhihuDetailModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onNext(ZhihuDetailModel zhihuDetailModel) {
                super.onNext(zhihuDetailModel);
                if (zhihuDetailModel.type == 0)
                    document = Jsoup.parse(zhihuDetailModel.body);
                else
                    view.showError("此为站外文章,无法获取详情", null);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showError(e.toString(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getZhihuDetail(id);
                    }
                });
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (document != null) {
                    view.showData(document);
                }
            }

        };
        HttpClient.getSingle(APIUrl.ZHIHU_BASE_URL).getZhihuDetail(id, subscriber);
    }



}
