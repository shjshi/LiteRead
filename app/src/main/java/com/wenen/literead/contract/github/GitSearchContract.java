package com.wenen.literead.contract.github;

import com.wenen.literead.contract.BaseContract;
import com.wenen.literead.model.github.GithubLoginModel;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface GitSearchContract {
    interface View extends BaseContract.View {
        void showData(GithubLoginModel githubLoginModel);
    }

    interface Model {
    }

    interface Prestener {
        void githubLogin(String s);
    }
}
