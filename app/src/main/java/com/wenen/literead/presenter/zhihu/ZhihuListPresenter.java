package com.wenen.literead.presenter.zhihu;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wenen.literead.adapter.zhihu.ZhihuListAdapter;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.zhihu.ZhihuListContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.zhihu.ZhihuListModel;
import com.wenen.literead.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/14.
 */
public class ZhihuListPresenter extends BasePresenter implements ZhihuListContract.Prestener {
    private Subscriber subscriber;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rclZhihu;
    private ZhihuListAdapter zhihuListAdapter;
    private List<ZhihuListModel.TopStoriesEntity> list = new ArrayList<>();

    public ZhihuListPresenter(ZhihuListContract.View view) {
        super(view);
        swipeRefreshLayout = view.getSwipRefreshLayout();
        rclZhihu = view.getRecyclerView();
        zhihuListAdapter = view.getAdapter();
    }

    @Override
    public void getZhihuList() {
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
}
