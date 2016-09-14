package com.wenen.literead.presenter.image;

import android.util.Log;
import android.view.View;

import com.wenen.literead.activity.image.ThumbleActivity;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.image.ThumbleContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.image.ImageModel;
import com.wenen.literead.presenter.BasePresenter;

/**
 * Created by Wen_en on 16/9/14.
 */
public class ThumblePresenter extends BasePresenter implements ThumbleContract.Presenter {
    private ThumbleActivity.ViewHolder viewHolder;

    public ThumblePresenter(ThumbleContract.View view) {
        super(view);
        viewHolder = view.getViewHolder();
    }

    @Override
    public void getImage(int id) {
        viewHolder.subscribers = new HttpSubscriber<ImageModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                viewHolder.mAdapter.getRandomHeight(viewHolder.listEntities);
                viewHolder.mAdapter.updateList(viewHolder.listEntities);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showSnackBar(indeterminateHorizontalProgressToolbar, e.toString(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getImage(viewHolder.id);
                    }
                });
            }

            @Override
            public void onNext(ImageModel imageModel) {
                super.onNext(imageModel);
                viewHolder.listEntities.clear();
                for (ImageModel.ListEntity listEntitie : imageModel.list
                        ) {
                    viewHolder.listEntities.add(listEntitie);
                    Log.e("list", listEntitie.src);
                }
            }
        };
        HttpClient.getSingle(APIUrl.TIANGOU_IMG_URL).getImg(id, viewHolder.subscribers);
    }
}
