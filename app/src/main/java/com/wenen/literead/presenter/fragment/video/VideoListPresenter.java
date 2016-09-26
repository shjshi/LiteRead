package com.wenen.literead.presenter.fragment.video;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.fragment.video.VideoListContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.model.video.VideoListModel;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/26.
 */

public class VideoListPresenter implements VideoListContract.Presenter {
    private Subscriber subscriber;
    private ArrayList<VideoListModel> listModels = new ArrayList<>();
    private String url;
    private VideoListContract.View view;

    public VideoListPresenter(VideoListContract.View view) {
        this.view = view;
    }

    @Override
    public void getVideoList(Fragment fragment) {
        subscriber = new Subscriber<Element>() {
            @Override
            public void onCompleted() {
                view.showData(listModels);
            }

            @Override
            public void onError(Throwable e) {
                view.showError(e.toString());
            }

            @Override
            public void onNext(Element type) {
                Elements elements = type.select("div.mes");
                for (Element element : elements) {
                    VideoListModel videoListModel = new VideoListModel();
                    Log.e("author", element.select("p").select("span.dy-name").first().text());
                    videoListModel.author = element.select("p").select("span.dy-name").first().text();
                    Log.e("roomUrl", element.parent().select("a").first().attr("href"));
                    videoListModel.roomUrl = element.parent().select("a").first().attr("href");
                    videoListModel.imageUrl = element.parent().select("a").first().select("img").first().attr("data-original");
                    Log.e("imageUrl", element.parent().select("a").first().select("img").first().attr("data-original"));
                    Elements mesTit = element.select("div.mes-tit");
                    for (Element mesElement : mesTit) {
                        videoListModel.title = mesElement.select("h3").first().text();
                        Log.e("mesElement", mesElement.select("h3").first().text());
                        listModels.add(videoListModel);
                    }
                }
            }
        };
        if (url == null)
            url = fragment.getArguments().getString("url");
        HttpClient.getSingle(APIUrl.DOUYU_BASE_URL).
                getVideoList(url, subscriber);
    }
}
