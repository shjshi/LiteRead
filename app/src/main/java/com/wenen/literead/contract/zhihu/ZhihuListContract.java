package com.wenen.literead.contract.zhihu;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.wenen.literead.adapter.zhihu.ZhihuListAdapter;
import com.wenen.literead.contract.BaseContract;
import com.wenen.literead.model.zhihu.ZhihuListModel;

import java.util.List;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface ZhihuListContract {
    interface View extends BaseContract.View {
        SwipeRefreshLayout getSwipRefreshLayout();

        RecyclerView getRecyclerView();

        ZhihuListAdapter getAdapter();

        void upDateData(List<ZhihuListModel.TopStoriesEntity> list);
    }


    interface Model {
    }


    interface Prestener {
        void getZhihuList();
    }


}
