package com.wenen.literead.presenter.zhihu;

import android.view.View;

import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.zhihu.ZhihuListContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.zhihu.ZhihuListModel;
import com.wenen.literead.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/14.
 */
public class ZhihuListPresenter extends BasePresenter implements ZhihuListContract.Prestener {
    private Subscriber subscriber;
    private List<ZhihuListModel.StoriesEntity> list = new ArrayList<>();
    ZhihuListContract.View view;

    public ZhihuListPresenter(ZhihuListContract.View view) {
        super(view);
this.view=view;
    }

    public ZhihuListPresenter addTaskListener(ZhihuListContract.View view) {
        this.view = view;
        return this;
    }

    @Override
    public void getZhihuList() {
        subscriber = new HttpSubscriber<ZhihuListModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                view.showData(list);
//
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showError(e.toString(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getZhihuList();
                    }
                });
            }

            @Override
            public void onNext(ZhihuListModel zhihuListModel) {
                super.onNext(zhihuListModel);
                list = zhihuListModel.stories;
            }

        };
        HttpClient.getSingle(APIUrl.ZHIHU_BASE_URL).getZhihuList("latest", subscriber);
    }
}
