package com.wenen.literead.contract.fragment.image;

import com.wenen.literead.contract.activity.BaseContract;
import com.wenen.literead.model.image.ImageListModel;

import java.util.ArrayList;

/**
 * Created by Wen_en on 16/9/26.
 */

public interface ImageListContract {
    interface View extends BaseContract.View{
        void showData(ArrayList<ImageListModel.TngouEntity> list);

        void shoeError(String e);
    }

    interface Model {
    }

    interface Presenter {
        void getImgThumbleList(int id, int page, int rows);
    }
}
