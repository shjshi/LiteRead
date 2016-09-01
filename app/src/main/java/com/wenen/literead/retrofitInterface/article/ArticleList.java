package com.wenen.literead.retrofitInterface.article;

import com.wenen.literead.model.article.ArticleListModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Wen_en on 16/8/30.
 */
public interface ArticleList {
    @GET("data/{Typepath}/{pageCount}/{page}")
    Observable<ArticleListModel> getArticleList(
            @Path("Typepath") String Typepath,
            @Path("pageCount") int pageCount,
            @Path("page") int page
    );
}
