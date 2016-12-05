package com.wenen.literead.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.wenen.literead.R;
import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/2.
 */
public class HttpSubscriber<T> extends Subscriber<T> {
    private Context context;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private View view;
    private static final String TAG = "HttpSubscriber";

    public HttpSubscriber() {
    }
    public HttpSubscriber(Context context) {
        this.context = context;
        creatProgressDialog();
    }

    @Override
    public void onCompleted() {
        cancelProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        cancelProgressDialog();
        if (e instanceof SocketTimeoutException) {
            Log.e(TAG, e.toString());
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            Log.e(TAG, httpException.code() + "");
            Log.e(TAG, httpException.message() + "");
            if (httpException.response() != null && httpException.response().errorBody() != null) {
                try {
                    Log.e(TAG, httpException.response().message());
                    String bodyStr = httpException.response().errorBody().string();
                    Log.e(TAG, bodyStr);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onNext(T t) {
    }

    public void creatProgressDialog() {
        if (progressDialog == null) {
          progressDialog=ProgressDialog.show(context,"提示","正在加载中...");
        }
    }

    public void cancelProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
        if (alertDialog!=null)alertDialog.dismiss();
    }
}
