package com.wenen.literead.http;

import android.view.View;

import com.litesuits.android.log.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/2.
 */
public class HttpSubscriber<T> extends Subscriber<T> {
   private View view;

    public HttpSubscriber() {
    }

    public HttpSubscriber(View view) {
        this.view = view;
        setProgressBarISvisible(view, true);
    }

    @Override
    public void onCompleted() {
        setProgressBarISvisible(view, false);
    }


    @Override
    public void onError(Throwable e) {
        setProgressBarISvisible(view, false);
        if (e instanceof SocketTimeoutException) {
            Log.e(e.toString());
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            Log.e(httpException.code() + "");
            Log.e(httpException.message() + "");
            if (httpException.response() != null && httpException.response().errorBody() != null) {
                try {
                    Log.e(httpException.response().message());
                    String bodyStr = httpException.response().errorBody().string();
                    Log.e(bodyStr);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onNext(T t) {
    }

    public void setProgressBarISvisible(View view, boolean iSvisible) {
        if (view != null)
            if (iSvisible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
    }
}
