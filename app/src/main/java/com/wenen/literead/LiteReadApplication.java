package com.wenen.literead;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.Bugly;
import com.wenen.literead.api.APIUrl;

/**
 * Created by Wen_en on 16/8/12.
 */
public class LiteReadApplication extends Application {
    public static Context mContext;
    private SharedPreferences sp;

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initImageLoader(getApplicationContext());
        Bugly.init(getApplicationContext(), APIUrl.BUGLY_APPID, false);
        sp = getSharedPreferences("appInfo", Context.MODE_PRIVATE);
    }
}
