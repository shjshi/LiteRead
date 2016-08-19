package com.wenen.literead.retrofitInterface.image;

import com.wenen.literead.model.image.ImageModel;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wen_en on 16/8/15.
 */
public interface IMG {
    @GET("show")
    Observable<ImageModel> getImage(
            @Header("apikey") String apikey,
            @Query("id") long id
    );
}
