package com.wenen.literead.contract.fragment.video;

import android.support.v4.app.Fragment;

import com.wenen.literead.contract.activity.BaseContract;
import com.wenen.literead.model.video.VideoListModel;

import java.util.ArrayList;

/**
 * Created by Wen_en on 16/9/26.
 */

public interface VideoListContract {
    interface View extends BaseContract.View {
        void showData(ArrayList<VideoListModel> list);

        void showError(String e);
    }

    interface Model {
    }

    interface Presenter {
        void getVideoList(Fragment fragment);
    }
}
