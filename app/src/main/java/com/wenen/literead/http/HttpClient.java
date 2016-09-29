package com.wenen.literead.http;

import android.util.Log;

import com.wenen.literead.LiteReadApplication;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.retrofitInterface.article.ArticleList;
import com.wenen.literead.retrofitInterface.github.GitHubLogin;
import com.wenen.literead.retrofitInterface.github.GithubFollow;
import com.wenen.literead.retrofitInterface.image.IMG;
import com.wenen.literead.retrofitInterface.image.IMGThumbleList;
import com.wenen.literead.retrofitInterface.image.IMGTypeList;
import com.wenen.literead.retrofitInterface.video.Video;
import com.wenen.literead.retrofitInterface.zhihu.ZhihuDetail;
import com.wenen.literead.retrofitInterface.zhihu.ZhihuList;
import com.wenen.literead.web.GetWebObservable;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
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
                .addInterceptor(new LoggerInterceptor(TAG)).addNetworkInterceptor(new HttpInterceptor(TAG))
                .cache(cache).build();
    }

    private static class SingletonHolder {
        private static HttpClient INSTANCE = new HttpClient();
    }

    public static HttpClient getSingle(String url) {
        BASE_URL=url;
        Log.e(TAG, BASE_URL);
        return SingletonHolder.INSTANCE;
    }

    private static void updateRetrofit() {
        retrofit = new Retrofit.Builder().client(client).
                addConverterFactory(GsonConverterFactory.create())//解析方法
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)//主机地址
                .build();
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
    public void getIMGThumbleList(int id, int page, int rows, Subscriber<Object> subscriber) {
        updateRetrofit();
        IMGThumbleList imgThumbleList = retrofit.create(IMGThumbleList.class);
        imgThumbleList.getImgThumbleList(APIUrl.apikey, id, page, rows).subscribeOn(Schedulers.io())
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
    @SuppressWarnings("unchecked")
    public void getVideoList(String url, Subscriber<Object> subscriber) {
        Log.e(TAG, BASE_URL + url);
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

    public void getZhihuList(String path, Subscriber<Object> subscriber) {
        updateRetrofit();
        ZhihuList zhihuList = retrofit.create(ZhihuList.class);
        zhihuList.getZhihuList(path).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    public void getZhihuDetail(int path, Subscriber<Object> subscriber) {
        updateRetrofit();
        ZhihuDetail zhihuDetail = retrofit.create(ZhihuDetail.class);
        zhihuDetail.getZhihuDetail(path).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    public void GithubLogin(String path, String client_id, String client_secret, Subscriber<Object> subscriber) {
        updateRetrofit();
        GitHubLogin gitHubLogin = retrofit.create(GitHubLogin.class);
        gitHubLogin.GithubLogin(path, client_id, client_secret).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);

    }

    public void getGitHubFollow(String name, String path, String client_id, String client_secret, Subscriber<Object> subscriber) {
        updateRetrofit();
        GithubFollow githubFollow = retrofit.create(GithubFollow.class);
        githubFollow.getGitHubFollowing(name, path, client_id, client_secret).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
