package com.wenen.literead.contract.github;

import com.wenen.literead.activity.github.UserDetailActivity;
import com.wenen.literead.contract.BaseContract;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface UserDetailContract {
    interface View extends BaseContract.View {
        UserDetailActivity.ViewHolder getViewHolder();
    }

    interface Model {
    }

    interface Presenter {
        void githubSearch();
    }
}
