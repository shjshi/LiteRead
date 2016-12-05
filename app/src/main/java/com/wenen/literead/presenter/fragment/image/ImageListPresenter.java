package com.wenen.literead.presenter.fragment.image;

import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.fragment.image.ImageListContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.model.image.ImageListModel;
import com.wenen.literead.presenter.activity.BasePresenter;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/26.
 */

public class ImageListPresenter extends BasePresenter implements ImageListContract.Presenter {
    private ImageListContract.View view;
    private ArrayList<ImageListModel.TngouEntity> list = new ArrayList<>();

    public ImageListPresenter(ImageListContract.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public void getImgThumbleList(int id, int page, int rows) {
        subscriber = new Subscriber<ImageListModel>() {
            @Override
            public void onCompleted() {
                view.showData(list);
            }

            @Override
            public void onError(Throwable e) {
                view.shoeError(e.toString());
            }

            @Override
            public void onNext(ImageListModel imageListModel) {

                for (ImageListModel.TngouEntity tnEntity : imageListModel.tngou
                        ) {
                    list.add(tnEntity);
                }
            }
        };
        HttpClient.getSingle(APIUrl.TIANGOU_IMG_URL).getIMGThumbleList(id, page, rows, subscriber);
    }
}
