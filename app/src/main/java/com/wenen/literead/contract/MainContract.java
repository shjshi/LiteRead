package com.wenen.literead.contract;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.wenen.literead.activity.MainActivity;

import java.util.ArrayList;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface MainContract {
    interface View extends BaseContract.View {
        ViewPager getMainPager();

        MainActivity.MainPageViewAdapter getPagerAdapter();

        TabLayout getPagerTabs();

        void updateData( ArrayList<Fragment> fragments,ArrayList<String> titleList);

    }

    interface Model {
    }

    interface Presenter {
        void getIMGTypeList();
    }
}
