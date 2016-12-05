package com.wenen.literead.presenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.activity.MainContract;
import com.wenen.literead.fragment.image.ImageListFragment;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.image.ImageTypeListModel;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/14.
 */
public class MainPresenter extends BasePresenter implements MainContract.Presenter {
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private MainContract.View view;

    public MainPresenter(MainContract.View view) {
        super(view);
        this.view = view;
    }

    public MainPresenter addTaskListener(MainContract.View view) {
        this.view = view;
        return this;
    }

    @Override
    public void getIMGTypeList() {
        subscriber = new HttpSubscriber<ImageTypeListModel>(context) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                view.showData(fragments, titleList);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showError(e.toString(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getIMGTypeList();
                    }
                });
            }

            @Override
            public void onNext(ImageTypeListModel imageTypeListModel) {
                super.onNext(imageTypeListModel);
                if (imageTypeListModel.status) {
                    for (ImageTypeListModel.TngouEntity tnEntity : imageTypeListModel.tngou
                            ) {
                        Log.e("Title", tnEntity.title);
                        titleList.add(tnEntity.title);
                        ImageListFragment imageListFragment = new ImageListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", tnEntity.id);
                        imageListFragment.setArguments(bundle);
                        Log.e("id", tnEntity.id + "");
                        fragments.add(imageListFragment);
                    }
                } else
                    view.showError(null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getIMGTypeList();
                        }
                    });
            }
        };
        HttpClient.getSingle(APIUrl.TIANGOU_IMG_URL).getIMGTypeList(subscriber);
    }
}
