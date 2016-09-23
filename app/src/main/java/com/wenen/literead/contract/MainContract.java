package com.wenen.literead.contract;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface MainContract {
    interface View extends BaseContract.View {
       void showData( ArrayList<Fragment> fragments,ArrayList<String> titleList);


    }

    interface Model {
    }

    interface Presenter {
        void getIMGTypeList();
    }
}
