package com.wenen.literead.retrofitInterface.image;

import com.wenen.literead.model.image.ImageTypeListModel;

import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by Wen_en on 16/8/12.
 */
public interface IMGTypeList {
    @GET("classify")
    Observable<ImageTypeListModel> getImageTypeList(
            @Header("apikey") String apikey
    );
}
