package com.wenen.literead.presenter.activity;

import android.content.Context;
import android.util.Log;

import com.wenen.literead.contract.activity.BaseContract;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/14.
 */
public class BasePresenter implements BaseContract.Presenter {
   public Context context;
    public Subscriber subscriber;
    public BasePresenter(BaseContract.View view) {
        context = view.getContext();
    }
    @Override
    public void cancelHttp() {
        if (subscriber!=null){
            Log.e("BasePresenter", "cancelHttp: "+"success" );
             subscriber.unsubscribe();}
    }
}
