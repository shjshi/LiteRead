package com.wenen.literead.presenter.zhihu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wenen.literead.R;
import com.wenen.literead.adapter.zhihu.ZhihuListAdapter;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.zhihu.ZhihuListModel;
import com.wenen.literead.presenter.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/5.
 */
public class ZhihuListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.indeterminate_horizontal_progress_toolbar)
    MaterialProgressBar indeterminateHorizontalProgressToolbar;
    @Bind(R.id.rcl_zhihu)
    RecyclerView rclZhihu;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private Subscriber subscriber;
    private ZhihuListAdapter zhihuListAdapter;
    private List<ZhihuListModel.TopStoriesEntity> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_zhihu_list, null, savedInstanceState);
        setContentView(getRootView());
        ButterKnife.bind(this);
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setOnRefreshListener(this);
        getZhihuList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclZhihu.setLayoutManager(linearLayoutManager);
        zhihuListAdapter = new ZhihuListAdapter(list);
        rclZhihu.setAdapter(zhihuListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(R.string.zhihu_daily);
    }

    private void getZhihuList() {
        subscriber = new HttpSubscriber<ZhihuListModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
                if (rclZhihu.getAdapter() != null)
                    zhihuListAdapter.updateList(list);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
                showSnackBar(indeterminateHorizontalProgressToolbar, e.toString(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getZhihuList();
                    }
                });
            }

            @Override
            public void onNext(ZhihuListModel zhihuListModel) {
                super.onNext(zhihuListModel);
                list = zhihuListModel.top_stories;
            }

        };
        HttpClient.getSingle(APIUrl.ZHIHU_BASE_URL).getZhihuList("latest", subscriber);
    }

    @Override
    public void onRefresh() {
        getZhihuList();
    }
}
