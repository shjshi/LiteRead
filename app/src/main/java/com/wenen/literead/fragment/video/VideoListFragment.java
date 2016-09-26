package com.wenen.literead.fragment.video;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wenen.literead.R;
import com.wenen.literead.adapter.video.VideoListAdapter;
import com.wenen.literead.contract.fragment.video.VideoListContract;
import com.wenen.literead.fragment.BaseFragment;
import com.wenen.literead.model.video.VideoListModel;
import com.wenen.literead.presenter.fragment.video.VideoListPresenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, VideoListContract.View {
    @Bind(R.id.rcl_video_list)
    RecyclerView rclVideoList;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.fl_parent)
    FrameLayout flParent;
    private boolean isRefreshed = false;
    private VideoListAdapter mAdapter;
    private ArrayList<VideoListModel> listModels = new ArrayList<>();
    private VideoListPresenter videoListPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        videoListPresenter = new VideoListPresenter(this);
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

    }

    private void getVideoList() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        videoListPresenter.getVideoList(this);
    }

    @Override
    public void showData(ArrayList<VideoListModel> list) {
        isRefreshed = true;
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
        mAdapter.updateList(list);
    }

    @Override
    public void showError(String e) {
        Snackbar.make(rclVideoList, e, Snackbar.LENGTH_INDEFINITE).setAction("点击重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVideoList();
            }
        }).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoListPresenter = null;
    }
}
