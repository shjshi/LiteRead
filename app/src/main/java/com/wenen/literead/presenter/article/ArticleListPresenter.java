package com.wenen.literead.presenter.article;

import android.view.View;

import com.wenen.literead.activity.article.ArticleListActivity;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.article.ArticleListContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.article.ArticleListModel;
import com.wenen.literead.presenter.BasePresenter;

/**
 * Created by Wen_en on 16/9/14.
 */
public class ArticleListPresenter extends BasePresenter implements ArticleListContract.Presenter {
    private ArticleListActivity.ViewHolder viewHolder;

    public ArticleListPresenter(ArticleListContract.View view) {
        super(view);
        this.viewHolder = view.getViewHolder();
    }

    @Override
    public void getArticleList(String typePath, int pageCount, int page) {
        viewHolder.subscriber = new HttpSubscriber<ArticleListModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                if (viewHolder.swipeRefreshLayout != null) {
                    viewHolder.swipeRefreshLayout.setRefreshing(false);
                }
                viewHolder.isError = false;
                viewHolder.adapter.updateModel(viewHolder.list, viewHolder.isError);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (viewHolder.swipeRefreshLayout != null) {
                    viewHolder.swipeRefreshLayout.setRefreshing(false);
                }
                if (viewHolder.loadMore) {
                    viewHolder.isError = true;
                    viewHolder.adapter.updateModel(viewHolder.list, viewHolder.isError);
                } else
                    showSnackBar(indeterminateHorizontalProgressToolbar, null, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getArticleList(viewHolder.type, viewHolder.pagecount, viewHolder.page);
                        }
                    });
            }

            @Override
            public void onNext(ArticleListModel articleListModel) {
                super.onNext(articleListModel);
                if (!viewHolder.loadMore) {
                    viewHolder.list.clear();
                }
                for (ArticleListModel.ResultsEntity entity : articleListModel.results
                        ) {
                    viewHolder.list.add(entity);
                }
            }
        };
        HttpClient.getSingle(APIUrl.GANK_IO_URL).getArticleList(typePath, pageCount, page, viewHolder.subscriber);
    }
}
