package com.wenen.literead.contract;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface BaseContract {
    interface View {
        void showError(String s, android.view.View.OnClickListener listener);

        void getData();

        void addTaskListener();

        MaterialProgressBar getProgressBar();
    }

    interface Model {
    }

    interface Presenter {


    }
}
