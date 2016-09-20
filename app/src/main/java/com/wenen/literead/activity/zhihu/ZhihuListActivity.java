package com.wenen.literead.activity.zhihu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.wenen.literead.R;
import com.wenen.literead.adapter.zhihu.ZhihuListAdapter;
import com.wenen.literead.contract.zhihu.ZhihuListContract;
import com.wenen.literead.presenter.zhihu.ZhihuListPresenter;
import com.wenen.literead.model.zhihu.ZhihuListModel;
import com.wenen.literead.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wen_en on 16/9/5.
 */
public class ZhihuListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, ZhihuListContract.View {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rcl_zhihu)
    RecyclerView rclZhihu;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ZhihuListAdapter zhihuListAdapter;
    private List<ZhihuListModel.StoriesEntity> list = new ArrayList<>();
    private ZhihuListPresenter zhihuListPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_zhihu_list, null, savedInstanceState);
        setContentView(getRootView());
        ButterKnife.bind(this);
        zhihuListAdapter = new ZhihuListAdapter(list);
        zhihuListPresenter = new ZhihuListPresenter(this);
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setOnRefreshListener(this);
        zhihuListPresenter.getZhihuList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclZhihu.setLayoutManager(linearLayoutManager);
        rclZhihu.setAdapter(zhihuListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(R.string.zhihu_daily);
    }


    @Override
    public void onRefresh() {
        zhihuListPresenter.getZhihuList();
    }

    @Override
    public SwipeRefreshLayout getSwipRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return rclZhihu;
    }

    @Override
    public ZhihuListAdapter getAdapter() {
        return zhihuListAdapter;
    }

    @Override
    public void upDateData(List<ZhihuListModel.StoriesEntity> list) {
        this.list = list;
    }
}
