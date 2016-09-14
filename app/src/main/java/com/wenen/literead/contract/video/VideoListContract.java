package com.wenen.literead.contract.video;

import com.wenen.literead.activity.video.VideoListActivity;
import com.wenen.literead.contract.BaseContract;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface VideoListContract {
    interface View extends BaseContract.View {
        VideoListActivity.ViewHolder getViewHolder();
    }

    interface Model {
    }

    interface Presenter {
        void getVideoList();
    }
}
