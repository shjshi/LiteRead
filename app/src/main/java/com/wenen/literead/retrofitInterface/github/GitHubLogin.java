package com.wenen.literead.retrofitInterface.github;

import com.wenen.literead.model.github.GithubLoginModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wen_en on 16/9/7.
 */
public interface GitHubLogin {
    @GET("{path}")
    Observable<GithubLoginModel> GithubLogin(
            @Path("path") String path,
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret
    );
}
