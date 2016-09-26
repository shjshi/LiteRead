package com.wenen.literead.presenter.fragment.image;

import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.fragment.image.ImageListContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.model.image.ImageListModel;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/26.
 */

public class ImageListPresenter implements ImageListContract.Presenter {
    private Subscriber subscriber;
    private ImageListContract.View view;
    private ArrayList<ImageListModel.TngouEntity> list = new ArrayList<>();

    public ImageListPresenter(ImageListContract.View view) {
        this.view = view;
    }

    @Override
    public void getImgThumbleList(int id, int page, int rows, final boolean loadMore) {
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
                if (!loadMore)
                    list.clear();
                for (ImageListModel.TngouEntity tnEntity : imageListModel.tngou
                        ) {
                    list.add(tnEntity);
                }
            }
        };
        HttpClient.getSingle(APIUrl.TIANGOU_IMG_URL).getIMGThumbleList(id, page, rows, subscriber);
    }
}
