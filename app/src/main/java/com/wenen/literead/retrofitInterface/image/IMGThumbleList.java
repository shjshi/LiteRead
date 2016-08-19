package com.wenen.literead.retrofitInterface.image;

import com.wenen.literead.model.image.ImageListModel;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wen_en on 16/8/15.
 */
public interface IMGThumbleList {
    @GET("list")
    Observable<ImageListModel> getImgThumbleList(
            @Header("apikey") String apikey,
            @Query("id") int id,
            @Query("page") int page);
}
