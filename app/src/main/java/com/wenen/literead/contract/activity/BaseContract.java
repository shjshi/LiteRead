package com.wenen.literead.contract.activity;

import android.content.Context;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface BaseContract {
    interface View {
        void showError(String s, android.view.View.OnClickListener listener);

        void getData();

        void addTaskListener();

        Context getContext();

        void cancelHttp();
    }

    interface Model {
    }

    interface Presenter {
        void cancelHttp();

    }
}
