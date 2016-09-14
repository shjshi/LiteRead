package com.wenen.literead.contract.article;

import com.wenen.literead.activity.article.ArticleListActivity;
import com.wenen.literead.contract.BaseContract;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface ArticleListContract {
    interface View extends BaseContract.View {
        ArticleListActivity.ViewHolder getViewHolder();
    }

    interface Model {
    }

    interface Presenter {
        void getArticleList(String typePath, int pageCount, int page);
    }

}
