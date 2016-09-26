package com.wenen.literead.presenter.fragment.github;

import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.contract.fragment.github.FollowersContract;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.github.GithubFollowModel;
import com.wenen.literead.model.github.GithubUser;
import com.wenen.literead.model.github.StartedModel;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/26.
 */

public class FollowersPresenter implements FollowersContract.Presenter {
    private Subscriber subscriber;
    private List<Object> list = new ArrayList<>();
    private String path;
    private FollowersContract.View view;
    private int position;

    public FollowersPresenter(FollowersContract.View view) {
        this.view = view;
    }

    @Override
    public void getGitHubFollow(Fragment context) {
        subscriber = new HttpSubscriber<ResponseBody>() {
            @Override
            public void onNext(ResponseBody o) {
                super.onNext(o);
                String string = null;
                try {
                    string = o.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JsonParser parser = new JsonParser();
                JsonReader reader = new JsonReader(new StringReader(string));
                reader.setLenient(true);
                JsonArray jsonArray = parser.parse(reader).getAsJsonArray();
                Gson gson = new Gson();
                list.clear();
                if (position == 0 || position == 1) {
                    for (JsonElement js : jsonArray
                            ) {
                        GithubFollowModel githubModel = gson.fromJson(js, GithubFollowModel.class);
                        list.add(githubModel);
                    }
                } else {
                    for (JsonElement js : jsonArray
                            ) {
                        StartedModel startedModel = gson.fromJson(js, StartedModel.class);
                        list.add(startedModel);
                    }
                }
            }


            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showError(e.toString());
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                view.showData(list);
            }
        };
        if (path == null && context.getArguments() != null) {
            position = context.getArguments().getInt("position");
            switch (position) {
                case 0:
                    path = "followers";
                    break;
                case 1:
                    path = "following";
                    break;
                case 2:
                    path = "starred";
                    break;
                case 3:
                    path = "repos";
                    break;
            }
        }
        HttpClient.getSingle(APIUrl.GITHUB_BASE_URL).getGitHubFollow(GithubUser.getSingle().getName(),
                path, APIUrl.GITHUB_CLIENT_ID, APIUrl.GITHUB_CECRET, subscriber);
    }
}
