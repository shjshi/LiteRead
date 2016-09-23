package com.wenen.literead.contract.article;

import com.wenen.literead.contract.BaseContract;
import com.wenen.literead.model.article.ArticleListModel;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface ArticleListContract {
    interface View extends BaseContract.View {
        void showData(ArticleListModel articleListModel);
    }

    interface Model {
    }

    interface Presenter {
        void getArticleList(String typePath, int pageCount, int page);
    }

}
