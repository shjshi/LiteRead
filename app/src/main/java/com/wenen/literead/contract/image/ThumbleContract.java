package com.wenen.literead.contract.image;

import com.wenen.literead.activity.image.ThumbleActivity;
import com.wenen.literead.contract.BaseContract;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface ThumbleContract {
    interface View extends BaseContract.View {
        ThumbleActivity.ViewHolder getViewHolder();
    }

    interface Model {
    }

    interface Presenter {
        void getImage(final int id);
    }
}
