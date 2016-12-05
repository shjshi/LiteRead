package com.wenen.literead.presenter.activity.image;

import android.util.Log;
import android.view.View;

import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.activity.image.ThumbleContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.image.ImageModel;
import com.wenen.literead.presenter.activity.BasePresenter;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/14.
 */
public class ThumblePresenter extends BasePresenter implements ThumbleContract.Presenter {
    private ThumbleContract.View view;
    ArrayList<ImageModel.ListEntity> listEntities = new ArrayList<>();

    public ThumblePresenter(ThumbleContract.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public void getImage(final int id) {
        subscriber = new HttpSubscriber<ImageModel>(context) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                view.showData(listEntities);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showError(e.toString(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getImage(id);
                    }
                });
            }

            @Override
            public void onNext(ImageModel imageModel) {
                super.onNext(imageModel);
                listEntities.clear();
                for (ImageModel.ListEntity listEntitie : imageModel.list
                        ) {
                    listEntities.add(listEntitie);
                    Log.e("list", listEntitie.src);
                }
            }
        };
        HttpClient.getSingle(APIUrl.TIANGOU_IMG_URL).getImg(id, subscriber);
    }
}
