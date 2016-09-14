package com.wenen.literead.contract.image;

import com.wenen.literead.activity.image.ImageDetailActivity;
import com.wenen.literead.contract.BaseContract;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface ImageDetailContract {
    interface View extends BaseContract.View {
        ImageDetailActivity.ViewHolder getViewHolder();
    }

    interface Model {
    }

    interface Presenter {
        void downLoadFile(final String url);
    }
}
