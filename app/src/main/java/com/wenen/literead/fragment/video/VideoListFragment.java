package com.wenen.literead.fragment.video;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wenen.literead.R;
import com.wenen.literead.adapter.video.VideoListAdapter;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.fragment.BaseFragment;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.model.video.VideoListModel;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

public class VideoListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.rcl_video_list)
    RecyclerView rclVideoList;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.fl_parent)
    FrameLayout flParent;
    private String url;
    private Subscriber subscriber;
    private boolean isRefreshed = false;
    private ArrayList<VideoListModel> listModels = new ArrayList<>();
    private VideoListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            url = savedInstanceState.getString("url");
        } else {
            if (getArguments() != null) {
                url = getArguments().getString("url");
                Log.e("urllll", url + "");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("url", url);
    }

    @Override
    public void onRefresh() {
        doRefresh();
    }

    private void refreshIf(boolean needRefresh) {
        if (needRefresh) {
            doRefresh();
        }
    }

    private void doRefresh() {
        getVideoList();
    }

    private boolean shouldRefreshOnVisibilityChange(boolean isVisibleToUser) {
        return isVisibleToUser && !isRefreshed;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        refreshIf(shouldRefreshOnVisibilityChange(isVisibleToUser));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclVideoList.setLayoutManager(linearLayoutManager);
        mAdapter = new VideoListAdapter(listModels);
        rclVideoList.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getVideoList();
    }

    private void getVideoList() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        subscriber = new Subscriber<Element>() {
            @Override
            public void onCompleted() {
                isRefreshed = true;
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
                mAdapter.updateList(listModels);
            }

            @Override
            public void onError(Throwable e) {
                if (e != null)
                    Log.e("next", e.toString());
            }

            @Override
            public void onNext(Element type) {
                Elements elements = type.select("div.mes");
                for (Element element : elements) {
                    VideoListModel videoListModel = new VideoListModel();
                    Log.e("author", element.select("p").select("span.dy-name").first().text());
                    videoListModel.author = element.select("p").select("span.dy-name").first().text();
                    Log.e("roomUrl", element.parent().select("a").first().attr("href"));
                    videoListModel.roomUrl = element.parent().select("a").first().attr("href");
                    videoListModel.imageUrl = element.parent().select("a").first().select("img").first().attr("data-original");
                    Log.e("imageUrl", element.parent().select("a").first().select("img").first().attr("data-original"));
                    Elements mesTit = element.select("div.mes-tit");
                    for (Element mesElement : mesTit) {
                        videoListModel.title = mesElement.select("h3").first().text();
                        Log.e("mesElement", mesElement.select("h3").first().text());
                        listModels.add(videoListModel);
                    }
                }
            }
        };
        HttpClient.getSingle(APIUrl.DOUYU_BASE_URL).
                getVideoList(url, subscriber);

    }

}
