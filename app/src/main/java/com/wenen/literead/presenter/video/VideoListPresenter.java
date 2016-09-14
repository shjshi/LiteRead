package com.wenen.literead.presenter.video;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wenen.literead.activity.video.VideoListActivity;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.video.VideoListContract;
import com.wenen.literead.fragment.video.VideoListFragment;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.presenter.BasePresenter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Wen_en on 16/9/14.
 */
public class VideoListPresenter extends BasePresenter implements VideoListContract.Presenter {
    VideoListActivity.ViewHolder viewHolder;

    public VideoListPresenter(VideoListContract.View view) {
        super(view);
        this.viewHolder = view.getViewHolder();
    }

    @Override
    public void getVideoList() {
        viewHolder.subscriber = new HttpSubscriber<Element>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                if (viewHolder.mainPager.getAdapter() == null) {
                    viewHolder.mainPager.setAdapter(viewHolder.mainPageViewAdapter);
                } else
                    viewHolder.mainPager.getAdapter().notifyDataSetChanged();
                viewHolder.mainPager.setOffscreenPageLimit( viewHolder.titleList.size());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (e != null)
                    Log.e("next", e.toString());
                viewHolder.mainPagerTabs.setVisibility(View.GONE);
                showSnackBar(viewHolder.mainPager, "数据获取失败!", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.mainPagerTabs.setVisibility(View.VISIBLE);
                        getVideoList();
                    }
                });
            }

            @Override
            public void onNext(Element type) {
                super.onNext(type);
                Elements elements = type.select("a.btn");
                for (Element element : elements) {
                    viewHolder.titleList.add(element.text());
                    VideoListFragment videoListFragment = new VideoListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", element.attr("href"));
                    videoListFragment.setArguments(bundle);
                    viewHolder.fragments.add(videoListFragment);
                }
            }
        }
        ;
        HttpClient.getSingle(APIUrl.DOUYU_BASE_URL).
                getVideoList("/directory", viewHolder.subscriber);
    }
}
