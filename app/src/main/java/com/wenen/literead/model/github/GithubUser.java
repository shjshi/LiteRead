package com.wenen.literead.model.github;

import android.content.Context;
import android.content.SharedPreferences;

import com.wenen.literead.LiteReadApplication;

/**
 * Created by Wen_en on 16/9/8.
 */
public class GithubUser {
    private String username;
    private boolean autoLogin;
    private static SharedPreferences sp;

    public boolean isAutoLogin() {
        return sp.getBoolean("autoLogin", false);
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
        sp.edit().putBoolean("autoLogin", autoLogin).commit();
    }

    public String getName() {
        return sp.getString("username", "");
    }

    public void setName(String name) {
        this.username = name;
        sp.edit().putString("username", name).commit();
    }


    private GithubUser() {
    }

    private static class SingletonHolder {
        private static GithubUser INSTANCE = new GithubUser();
    }

    public static GithubUser getSingle() {
        sp = LiteReadApplication.mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        return SingletonHolder.INSTANCE;
    }


    public GithubLoginModel getGithubLoginModel() {
        return githubLoginModel;
    }

    public void setGithubLoginModel(GithubLoginModel githubLoginModel) {
        this.githubLoginModel = githubLoginModel;
    }

    private GithubLoginModel githubLoginModel;
}
