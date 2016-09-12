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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wenen.literead.R;
import com.wenen.literead.adapter.github.GitHubFollowAdapter;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.fragment.BaseFragment;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.github.GithubFollowModel;
import com.wenen.literead.model.github.GithubUser;
import com.wenen.literead.model.github.StartedModel;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class FollowersFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.rcl_github_list)
    RecyclerView rclGithubList;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.fl_parent)
    FrameLayout flParent;
    private int position;
    private boolean isRefreshed = false;
    private Subscriber subscriber;
    private boolean hasLoad;
    private String path;
    private GitHubFollowAdapter gitHubFollowAdapter;
    private List<Object> list = new ArrayList<>();

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
        gitHubFollowAdapter = new GitHubFollowAdapter(list);
        rclGithubList.setAdapter(gitHubFollowAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);

        return view;
    }

    private void getGitHubFollow() {
        subscriber = new HttpSubscriber<ResponseBody>() {
            @Override
            public void onNext(ResponseBody o) {
                super.onNext(o);
                String string = null;
                try {
                    string = o.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JsonParser parser = new JsonParser();
                JsonReader reader = new JsonReader(new StringReader(string));
                reader.setLenient(true);
                JsonArray jsonArray = parser.parse(reader).getAsJsonArray();
                Gson gson = new Gson();
                if (position == 0 || position == 1) {
                    for (JsonElement js : jsonArray
                            ) {
                        GithubFollowModel githubModel = gson.fromJson(js, GithubFollowModel.class);
                        list.add(githubModel);
                    }
                } else {
                    for (JsonElement js : jsonArray
                            ) {
                        StartedModel startedModel = gson.fromJson(js, StartedModel.class);
                        list.add(startedModel);
                    }
                }
            }


            @Override
            public void onError(Throwable e) {
                super.onError(e);
                hasLoad = false;
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
                isRefreshed = true;
                Snackbar.make(rclGithubList, e.toString(), Snackbar.LENGTH_INDEFINITE).show();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                isRefreshed = true;
                hasLoad = true;
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
                gitHubFollowAdapter.updateList(list);
            }
        };
        if (path == null && getArguments() != null) {
            position = getArguments().getInt("position");
            switch (position) {
                case 0:
                    path = "followers";
                    break;
                case 1:
                    path = "following";
                    break;
                case 2:
                    path = "starred";
                    break;
                case 3:
                    path = "repos";
                    break;
            }
        }
        HttpClient.getSingle(APIUrl.GITHUB_BASE_URL).getGitHubFollow(GithubUser.getSingle().getName(), path, subscriber);
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
            getGitHubFollow();
        }

    }

    @Override
    public void onRefresh() {
        doRefresh();
    }

    private void doRefresh() {
        getGitHubFollow();
    }

    private boolean shouldRefreshOnVisibilityChange(boolean isVisibleToUser) {
        return isVisibleToUser && !isRefreshed;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        refreshIf(shouldRefreshOnVisibilityChange(isVisibleToUser));
    }

    private void refreshIf(boolean needRefresh) {
        if (needRefresh) {
            doRefresh();
        }
    }
}

