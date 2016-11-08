package com.wenen.literead.presenter.activity.article;

import android.view.View;

import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.activity.article.ArticleListContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.article.ArticleListModel;
import com.wenen.literead.presenter.activity.BasePresenter;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/14.
 */
public class ArticleListPresenter extends BasePresenter implements ArticleListContract.Presenter {
    private ArticleListContract.View view;
    public ArticleListPresenter(ArticleListContract.View view) {
        super(view);
        this.view = view;
    }
    @Override
    public void getArticleList(final String typePath, final int pageCount, final int page) {
        subscriber = new HttpSubscriber<ArticleListModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showError(e.toString(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getArticleList(typePath, pageCount, page);
                    }
                });
            }

            @Override
            public void onNext(ArticleListModel articleListModel) {
                super.onNext(articleListModel);
                view.showData(articleListModel);
            }
        };
        HttpClient.getSingle(APIUrl.GANK_IO_URL).getArticleList(typePath, pageCount, page, subscriber);
    }
}
