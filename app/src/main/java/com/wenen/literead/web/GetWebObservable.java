package com.wenen.literead.web;


import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/8/17.
 */

public class GetWebObservable {
    public static rx.Observable getInstance(final String urls) {
        Log.d("webUrls", urls);
        final rx.Observable documentObservable = rx.Observable.create(new rx.Observable.OnSubscribe<Document>() {
            @Override
            public void call(Subscriber<? super Document> subscriber) {
                Document document = null;
                try {
                   document = Jsoup.connect(urls).get();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }catch (Exception e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }finally {
                    subscriber.onNext(document);
                    subscriber.onCompleted();
                }

            }
        });
        return documentObservable;
    }
}
