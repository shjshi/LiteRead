package com.wenen.literead.presenter.activity.video;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.activity.video.VideoListContract;
import com.wenen.literead.fragment.video.VideoListFragment;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.presenter.activity.BasePresenter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/14.
 */
public class VideoListPresenter extends BasePresenter implements VideoListContract.Presenter {
    private VideoListContract.View view;
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public VideoListPresenter(VideoListContract.View view) {
        super(view);
        this.view=view;
    }

    public VideoListPresenter addTaskListener(VideoListContract.View view) {
        this.view = view;
        return this;
    }

    @Override
    public void getVideoList() {
        subscriber = new HttpSubscriber<Element>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                view.showData(titleList, fragments);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (e != null)
                    Log.e("next", e.toString());
                view.showError("数据获取失败!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getVideoList();
                    }
                });
            }

            @Override
            public void onNext(Element type) {
                super.onNext(type);
                Elements elements = type.select("a.btn");
                for (Element element : elements) {
                    titleList.add(element.text());
                    VideoListFragment videoListFragment = new VideoListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", element.attr("href"));
                    videoListFragment.setArguments(bundle);
                    fragments.add(videoListFragment);
                }
            }
        }
        ;
        HttpClient.getSingle(APIUrl.DOUYU_BASE_URL).
                getVideoList("/directory", subscriber);
    }
}
