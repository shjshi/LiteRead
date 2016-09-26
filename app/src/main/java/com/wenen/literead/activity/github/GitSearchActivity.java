package com.wenen.literead.activity.github;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wenen.literead.R;
import com.wenen.literead.activity.BaseActivity;
import com.wenen.literead.contract.activity.github.GitSearchContract;
import com.wenen.literead.model.github.GithubLoginModel;
import com.wenen.literead.model.github.GithubUser;
import com.wenen.literead.presenter.activity.github.GitSearchPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Wen_en on 16/9/6.
 */
public class GitSearchActivity extends BaseActivity implements GitSearchContract.View {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.indeterminate_horizontal_progress_toolbar)
    MaterialProgressBar indeterminateHorizontalProgressToolbar;
    @Bind(R.id.et_username)
    AppCompatEditText etUsername;
    @Bind(R.id.btn_login)
    AppCompatButton btnLogin;
    private String title;
    private String username;

    @OnClick(R.id.btn_login)
    public void gitLogin() {
        username = etUsername.getText().toString();
        if ("".equals(username))
            showSnackBar(indeterminateHorizontalProgressToolbar, "账户名为空!", null);
        else
            getData();
    }

    private GitSearchPresenter gitSearchPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_git_login, null, savedInstanceState);
        setContentView(getRootView());
        ButterKnife.bind(this);
        if (GithubUser.getSingle().isAutoLogin()) {
            Intent intent = new Intent(this, UserDetailActivity.class);
            intent.putExtra("username", githubUser.getName());
            startActivity(intent);
            finish();
        }
        if (savedInstanceState == null) {
            title = getIntent().getStringExtra("title");
        } else
            title = savedInstanceState.getString("title");
        gitSearchPresenter = new GitSearchPresenter(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gitSearchPresenter=null;
    }

    @Override
    public void showData(GithubLoginModel githubLoginModel) {
        updateGithubUserData(githubLoginModel);
        githubUser.setName(username);
        githubUser.setAutoLogin(true);
        startActivity(new Intent(context, UserDetailActivity.class));
        finish();
    }

    @Override
    public void showError(String s, View.OnClickListener listener) {
        showSnackBar(indeterminateHorizontalProgressToolbar, s, listener);
    }

    @Override
    public void getData() {
        gitSearchPresenter.githubLogin(username);
    }

    @Override
    public void addTaskListener() {

    }

    @Override
    public MaterialProgressBar getProgressBar() {
        return indeterminateHorizontalProgressToolbar;
    }
}
