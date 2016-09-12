package com.wenen.literead.http;

import android.text.TextUtils;

import com.litesuits.android.log.Log;
import com.litesuits.common.assist.Network;
import com.wenen.literead.LiteReadApplication;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Wen_en on 16/9/12.
 */
public class HttpInterceptor implements Interceptor {
    public static final String TAG = "OkHttp";
    private String tag;
    private boolean showResponse;

    public HttpInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.showResponse = showResponse;
        this.tag = tag;
    }

    public HttpInterceptor(String tag) {
        this(tag, true);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
//            if (original.url().toString().indexOf("https://api.github.com/users/") != -1) {
//                original.newBuilder().put(RequestBody.create(JSON,
//                        APIUrl.GITHUB_CLIENT_ID));
//            }
        if (!Network.isConnected(LiteReadApplication.mContext)) {
            original = original.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Log.e("NoNetwork", "无网络");
        } else
            Log.e("NoNetwork", "有网络");
        Response response = chain.proceed(original);
        if (Network.isConnected(LiteReadApplication.mContext)) {
            int maxAge = 60 * 60; // read from cache for 1 minute
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return response;
    }
}
