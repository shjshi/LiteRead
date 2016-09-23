package com.wenen.literead.contract.zhihu;

import com.wenen.literead.contract.BaseContract;

import org.jsoup.nodes.Document;

/**
 * Created by Wen_en on 16/9/14.
 */
public interface ZhihuDetailContract {
    interface View extends BaseContract.View {
        void showData(Document document);

        void showError(String s, android.view.View.OnClickListener listener);

        void getData();

        void addTaskListener();
    }

    interface Model {
    }

    interface Presenter {
        void getZhihuDetail(int id);

    }
}
