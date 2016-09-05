package com.wenen.literead.retrofitInterface.zhihu;

import com.wenen.literead.model.zhihu.ZhihuDetailModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Wen_en on 16/9/5.
 */
public interface ZhihuDetail {
    @GET("{path}")
    Observable<ZhihuDetailModel> getZhihuDetail(
            @Path("path") int path
    );
}
