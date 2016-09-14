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
import com.wenen.literead.contract.github.GitSearchContract;
import com.wenen.literead.model.github.GithubUser;
import com.wenen.literead.presenter.github.GitSearchPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/6.
 */
public class GitSearchActivity extends BaseActivity implements GitSearchContract.View {

    @Override
    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder {
        @Bind(R.id.toolbar)
        public Toolbar toolbar;
        @Bind(R.id.indeterminate_horizontal_progress_toolbar)
        public MaterialProgressBar indeterminateHorizontalProgressToolbar;
        @Bind(R.id.et_username)
        public AppCompatEditText etUsername;
        @Bind(R.id.btn_login)
        public AppCompatButton btnLogin;
        public String title;
        public String username;
        public Subscriber subscriber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.btn_login)
        public void gitLogin() {
            username = etUsername.getText().toString();
            if ("".equals(username))
                gitSearchPresenter.showSnackBar(indeterminateHorizontalProgressToolbar, "账户名为空!", null);
            else
                gitSearchPresenter.githubLogin();
        }
    }

    ViewHolder viewHolder;
    private GitSearchPresenter gitSearchPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_git_login, null, savedInstanceState);
        setContentView(getRootView());
        viewHolder = new ViewHolder(getRootView());
        if (GithubUser.getSingle().isAutoLogin()) {
            Intent intent = new Intent(this, UserDetailActivity.class);
            intent.putExtra("username", githubUser.getName());
            startActivity(intent);
            finish();
        }
        if (savedInstanceState == null) {
            viewHolder.title = getIntent().getStringExtra("title");
        } else
            viewHolder.title = savedInstanceState.getString("title");
        gitSearchPresenter = new GitSearchPresenter(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", viewHolder.title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewHolder.toolbar.setTitle(viewHolder.title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewHolder = null;
    }
}
