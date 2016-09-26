package com.wenen.literead.contract.activity.video;

import android.support.v4.app.Fragment;

import com.wenen.literead.contract.activity.BaseContract;

import java.util.ArrayList;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface VideoListContract {
    interface View extends BaseContract.View {
        void showData(ArrayList<String> titleList,  ArrayList<Fragment> fragments);
    }

    interface Model {
    }

    interface Presenter {
        void getVideoList();
    }
}
