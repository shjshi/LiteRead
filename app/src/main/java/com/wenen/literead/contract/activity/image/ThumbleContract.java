package com.wenen.literead.contract.activity.image;

import com.wenen.literead.contract.activity.BaseContract;
import com.wenen.literead.model.image.ImageModel;

import java.util.ArrayList;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface ThumbleContract {
    interface View extends BaseContract.View {
        void showData(ArrayList<ImageModel.ListEntity> listEntities);
    }

    interface Model {
    }

    interface Presenter {
        void getImage(final int id);
    }
}
