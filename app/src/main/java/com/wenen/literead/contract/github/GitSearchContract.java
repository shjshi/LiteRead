package com.wenen.literead.contract.github;

import com.wenen.literead.activity.github.GitSearchActivity;
import com.wenen.literead.contract.BaseContract;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface GitSearchContract {
    interface View extends BaseContract.View{
GitSearchActivity.ViewHolder getViewHolder();
    }

    interface Model {
    }

    interface Prestener {
        void githubLogin();
    }
}
