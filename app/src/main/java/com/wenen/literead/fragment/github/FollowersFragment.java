package com.wenen.literead.fragment.github;


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
import com.wenen.literead.activity.github.UserDetailActivity;
import com.wenen.literead.adapter.github.GitHubFollowAdapter;
import com.wenen.literead.contract.fragment.github.FollowersContract;
import com.wenen.literead.fragment.BaseFragment;
import com.wenen.literead.presenter.fragment.github.FollowersPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FollowersFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, FollowersContract.View {
    @Bind(R.id.rcl_github_list)
    RecyclerView rclGithubList;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.fl_parent)
    FrameLayout flParent;
    private boolean isRefreshed = false;
    private boolean hasLoad;
    private GitHubFollowAdapter gitHubFollowAdapter;
    private List<Object> list = new ArrayList<>();
    private FollowersPresenter followersPresenter;

    public FollowersFragment() {
        // Required empty public constructor
    }

    public static FollowersFragment newInstance(int position) {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclGithubList.setLayoutManager(linearLayoutManager);
        gitHubFollowAdapter = new GitHubFollowAdapter(list,(UserDetailActivity)getActivity());
        rclGithubList.setAdapter(gitHubFollowAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);
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
        if (!hasLoad) {
            if (followersPresenter==null)
                followersPresenter=new FollowersPresenter(this);
            followersPresenter.getGitHubFollow(this);
        }
    }

    @Override
    public void onRefresh() {
        doRefresh();
    }

    private void doRefresh() {
         followersPresenter.getGitHubFollow( this);
    }

    private boolean shouldRefreshOnVisibilityChange(boolean isVisibleToUser) {
        return isVisibleToUser && !isRefreshed;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        followersPresenter = new FollowersPresenter(this);
        refreshIf(shouldRefreshOnVisibilityChange(isVisibleToUser));
    }

    private void refreshIf(boolean needRefresh) {
        if (needRefresh) {
            doRefresh();
        }
    }

    @Override
    public void showData(List<Object> list) {
        isRefreshed = true;
        hasLoad = true;
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
        gitHubFollowAdapter.updateList(list);
    }

    @Override
    public void showError(String e) {
        hasLoad = false;
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
        isRefreshed = true;
        Snackbar.make(rclGithubList, e.toString(), Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        followersPresenter=null;
    }
}

