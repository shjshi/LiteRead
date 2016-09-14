package com.wenen.literead.contract.zhihu;

import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenen.literead.contract.BaseContract;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface ZhihuDetailContract {
    interface View extends BaseContract.View {
        AppCompatTextView getZhihuDetailTitle();

        TextView getAuthor();

        AppCompatTextView getZhihuDetailTxt();

        ImageView getZhihuImage();

        ImageView getIvAuthor();

        int getId();
    }

    interface Model {
    }

    interface Presenter {
        void getZhihuDetail();
    }
}
