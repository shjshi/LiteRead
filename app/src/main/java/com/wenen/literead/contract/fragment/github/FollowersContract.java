package com.wenen.literead.contract.fragment.github;

import android.support.v4.app.Fragment;

import com.wenen.literead.contract.activity.BaseContract;

import java.util.List;

/**
 * Created by Wen_en on 16/9/26.
 */

public interface FollowersContract {
    interface View extends BaseContract.View{
        void showData(List<Object> list);

        void showError(String e);
    }

    interface Model {
    }

    interface Presenter {
        void getGitHubFollow(Fragment context);
    }
}
