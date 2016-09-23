package com.wenen.literead.contract.zhihu;

import com.wenen.literead.contract.BaseContract;
import com.wenen.literead.model.zhihu.ZhihuListModel;

import java.util.List;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface ZhihuListContract {
    interface View extends BaseContract.View {
        void upDateData(List<ZhihuListModel.StoriesEntity> list);
void showData(List<ZhihuListModel.StoriesEntity> list);
    }


    interface Model {
    }


    interface Prestener {
        void getZhihuList();
    }


}
