package com.wenen.literead.retrofitInterface.video;

import com.wenen.literead.model.video.VideoModel;

import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wen_en on 16/8/19.
 */
public interface Video {
    @POST("{roomid}")
    Observable<VideoModel> getVideo(
            @Path("roomid") String roomid,
            @Query("aid") String aid,
            @Query("cdn") String cdn,
            @Query("client_sys") String client_sys,
            @Query("time") String time,
            @Query("auth") String auth
    );

}
