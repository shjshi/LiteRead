package com.wenen.literead.contract.activity.github;

import com.wenen.literead.contract.activity.BaseContract;
import com.wenen.literead.model.github.GithubLoginModel;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface UserDetailContract {
    interface View extends BaseContract.View {
        void showData(GithubLoginModel githubLoginModel);
        void refreashData(String username);
    }

    interface Model {
    }

    interface Presenter {
        void githubSearch(String s);
    }
}
