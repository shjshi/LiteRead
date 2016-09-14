package com.wenen.literead.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.MainContract;
import com.wenen.literead.fragment.image.ImageListFragment;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.image.ImageTypeListModel;
import com.wenen.literead.activity.MainActivity;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/14.
 */
public class MainPresenter extends BasePresenter implements MainContract.Presenter {
    private MainContract.View view;
    private ViewPager mainPager;
    private MainActivity.MainPageViewAdapter mainPageViewAdapter;
    private TabLayout mainPagerTabs;
    private Subscriber subscriber;
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Context context;

    public MainPresenter(MainContract.View view) {
        super(view);
        this.view = view;
        mainPager = view.getMainPager();
        mainPageViewAdapter = view.getPagerAdapter();
        mainPagerTabs = view.getPagerTabs();
        context = view.getContext();
    }


    @Override
    public void getIMGTypeList() {
        subscriber = new HttpSubscriber<ImageTypeListModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                view.updateData(fragments, titleList);
                if (mainPager.getAdapter() == null) {
                    mainPager.setAdapter(mainPageViewAdapter);
                } else
                    mainPager.getAdapter().notifyDataSetChanged();
                mainPager.setOffscreenPageLimit(titleList.size());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mainPagerTabs.setVisibility(View.GONE);
                showSnackBar(indeterminateHorizontalProgressToolbar, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainPagerTabs.setVisibility(View.VISIBLE);
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
                    showSnackBar(indeterminateHorizontalProgressToolbar, null, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getIMGTypeList();
                        }
                    });
            }
        };
        HttpClient.getSingle(APIUrl.TIANGOU_IMG_URL).getIMGTypeList(subscriber);
    }
}
