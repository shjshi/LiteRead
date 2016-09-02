package com.wenen.literead.http;

import android.text.TextUtils;

import com.litesuits.android.log.Log;
import com.litesuits.common.assist.Network;
import com.wenen.literead.LiteReadApplication;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.retrofitInterface.article.ArticleList;
import com.wenen.literead.retrofitInterface.image.IMG;
import com.wenen.literead.retrofitInterface.image.IMGThumbleList;
import com.wenen.literead.retrofitInterface.image.IMGTypeList;
import com.wenen.literead.retrofitInterface.video.Video;
import com.wenen.literead.web.GetWebObservable;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Wen_en on 16/8/12.
 */
public class HttpClient {
    private static final String TAG = "HttpClient";

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static String BASE_URL = "";
    private static Retrofit retrofit;
    private static OkHttpClient client;
    private File httpCacheDirectory;
    private int cacheSize;
    private Cache cache;

    private HttpClient() {
        httpCacheDirectory = new File(LiteReadApplication.mContext.getCacheDir(), "responses");
        cacheSize = 30 * 1024 * 1024; // 30 MiB
        cache = new Cache(httpCacheDirectory, cacheSize);
        client = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("HTTP"))
                .cache(cache).build();
    }

    private static class SingletonHolder {
        private static HttpClient INSTANCE = new HttpClient();
    }

    public static HttpClient getSingle(String url) {
        setBaseUrl(url);
        Log.e("url", BASE_URL);
        return SingletonHolder.INSTANCE;
    }

    private static void updateRetrofit() {
        retrofit = new Retrofit.Builder().client(client).
                addConverterFactory(GsonConverterFactory.create())//解析方法
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(getBaseUrl())//主机地址
                .build();
    }

    class LoggerInterceptor implements Interceptor {
        public static final String TAG = "OkHttp";
        private String tag;
        private boolean showResponse;

        public LoggerInterceptor(String tag, boolean showResponse) {
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            this.showResponse = showResponse;
            this.tag = tag;
        }

        public LoggerInterceptor(String tag) {
            this(tag, true);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            logForRequest(request);
            if (!Network.isConnected(LiteReadApplication.mContext)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Log.e("NoNetwork", "无网络");
            } else
                Log.e("NoNetwork", "有网络");
            Response response = chain.proceed(request);
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
            return logForResponse(response);
        }

        private Response logForResponse(Response response) {
            try {
                //===>response log
                Log.e(tag, "========response'log=======");
                Response.Builder builder = response.newBuilder();
                Response clone = builder.build();
                Log.e(tag, "url : " + clone.request().url());
                Log.e(tag, "code : " + clone.code());
                Log.e(tag, "protocol : " + clone.protocol());
                if (!TextUtils.isEmpty(clone.message()))
                    Log.e(tag, "message : " + clone.message());

                if (showResponse) {
                    ResponseBody body = clone.body();
                    if (body != null) {
                        MediaType mediaType = body.contentType();
                        if (mediaType != null) {
                            Log.e(tag, "responseBody's contentType : " + mediaType.toString());
                            if (isText(mediaType)) {
                                String resp = body.string();
                                Log.e(tag, "responseBody's content : " + resp);

                                body = ResponseBody.create(mediaType, resp);
                                return response.newBuilder().body(body).build();
                            } else {
                                Log.e(tag, "responseBody's content : " + body.string());
                            }
                        }
                    }
                }

                Log.e(tag, "========response'log=======end");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        private void logForRequest(Request request) {
            try {
                String url = request.url().toString();
                Headers headers = request.headers();
                Log.e(tag, "========request'log=======");
                Log.e(tag, "method : " + request.method());
                Log.e(tag, "url : " + url);
                Log.e(tag, "pamars:" + request.toString());
                if (headers != null && headers.size() > 0) {
                    Log.e(tag, "headers : " + headers.toString());
                }
                RequestBody requestBody = request.body();
                if (requestBody != null) {
                    MediaType mediaType = requestBody.contentType();
                    if (mediaType != null) {
                        Log.e(tag, "requestBody's contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            Log.e(tag, "requestBody's content : " + bodyToString(request));
                        } else {
                            Log.e(tag, "requestBody's content : " + bodyToString(request));
                        }
                    }
                }
                Log.e(tag, "========request'log=======end");
            } catch (Exception e) {
//            e.printStackTrace();
            }
        }

        private boolean isText(MediaType mediaType) {
            if (mediaType.type() != null && mediaType.type().equals("text")) {
                return true;
            }
            if (mediaType.subtype() != null) {
                if (mediaType.subtype().equals("json") ||
                        mediaType.subtype().equals("xml") ||
                        mediaType.subtype().equals("html") ||
                        mediaType.subtype().equals("webviewhtml")
                        )
                    return true;
            }
            return false;
        }

        private String bodyToString(final Request request) {
            try {
                final Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            } catch (final IOException e) {
                return "something error when show requestBody.";
            }
        }
    }

    /**
     * 获取天狗图片分类
     */
    public void getIMGTypeList(Subscriber<Object> subscriber) {
        updateRetrofit();
        IMGTypeList imgTypeList = retrofit.create(IMGTypeList.class);
        imgTypeList.getImageTypeList(APIUrl.apikey).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取相册列表
     *
     * @param id
     * @param page
     * @param subscriber
     */
    public void getIMGThumbleList(int id, int page,int rows, Subscriber<Object> subscriber) {
        updateRetrofit();
        IMGThumbleList imgThumbleList = retrofit.create(IMGThumbleList.class);
        imgThumbleList.getImgThumbleList(APIUrl.apikey, id, page,rows).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取相册图片详情
     *
     * @param id
     * @param subscriber
     */
    public void getImg(long id, Subscriber<Object> subscriber) {
        updateRetrofit();
        IMG img = retrofit.create(IMG.class);
        img.getImage(APIUrl.apikey, id).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * jsoup解析斗鱼网页 获取游戏列表
     *
     * @param url
     * @param subscriber
     */
    public void getVideoList(String url, Subscriber<Object> subscriber) {
        Log.e("wholeurl", BASE_URL + url);
        GetWebObservable.getInstance(BASE_URL + url).map(new Func1<Document, Element>() {
            @Override
            public Element call(Document document) {
                Element type = document.body();
                return type;
            }


        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    public void getVideo(String roomid, String aid, String cdn, String client_sys, String time, String auth, Subscriber<Object> subscriber) {
        updateRetrofit();
        Video video = retrofit.create(Video.class);
        video.getVideo(roomid, aid, cdn, client_sys, time, auth).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);

    }

    public void getArticleList(String TypePath, int PageCount, int page, Subscriber<Object> subscriber) {
        updateRetrofit();
        ArticleList articleList = retrofit.create(ArticleList.class);
        articleList.getArticleList(TypePath, PageCount, page).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
