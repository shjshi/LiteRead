package com.wenen.literead.retrofitInterface.zhihu;

import com.wenen.literead.model.zhihu.ZhihuListModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Wen_en on 16/9/5.
 */
public interface ZhihuList {
    @GET("{path}")
    Observable<ZhihuListModel> getZhihuList(
            @Path("path") String path
    );
}
