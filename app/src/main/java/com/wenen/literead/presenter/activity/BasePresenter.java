package com.wenen.literead.presenter.activity;

import android.util.Log;

import com.wenen.literead.contract.activity.BaseContract;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/14.
 */
public class BasePresenter implements BaseContract.Presenter {
    public MaterialProgressBar indeterminateHorizontalProgressToolbar;
    public Subscriber subscriber;
    public BasePresenter(BaseContract.View view) {
        indeterminateHorizontalProgressToolbar = view.getProgressBar();
    }

    @Override
    public void cancelHttp() {
        if (subscriber!=null){
            Log.e("BasePresenter", "cancelHttp: "+"success" );
             subscriber.unsubscribe();}
    }
}
