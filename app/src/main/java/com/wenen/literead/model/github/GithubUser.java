package com.wenen.literead.model.github;

/**
 * Created by Wen_en on 16/9/8.
 */
public class GithubUser {
    public static GithubUser single;
    private String username;

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    private boolean autoLogin;

    private GithubUser() {
    }

    private static class SingletonHolder {
        private static GithubUser INSTANCE = new GithubUser();
    }

    public static GithubUser getSingle() {
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
