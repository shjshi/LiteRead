package com.wenen.literead.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.wenen.literead.R;
import com.wenen.literead.contract.activity.BaseContract;
import com.wenen.literead.elegate.IDelegate;
import com.wenen.literead.model.github.GithubLoginModel;
import com.wenen.literead.model.github.GithubUser;
import com.wenen.literead.presenter.activity.BasePresenter;

/**
 * Created by Wen_en on 16/8/16.
 */
public class BaseActivity extends AppCompatActivity implements IDelegate, BaseContract.View {
  public static GithubUser githubUser;
  public View view;
  private Toolbar toolbar;
  public IDelegate iDelegate;
  private boolean b;
  public Context context;
  private ProgressDialog progressDialog;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    iDelegate = this;
    githubUser = GithubUser.getSingle();
    context = this;
  }

  @Override protected void onResume() {
    super.onResume();
    if (toolbar != null) {
      if (!b) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            onBackPressed();
          }
        });
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
      }
    }
  }

  @Override public void create(int layoutId, ViewGroup v, Bundle b) {
    view = getLayoutInflater().inflate(layoutId, v, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    if (toolbar != null) setSupportActionBar(toolbar);
  }

  @Override public View getRootView() {
    return view;
  }

  @Override public void canTSetToolBar(boolean b) {
    this.b = b;
  }

  public void showSnackBar(@NonNull View view, String msg, View.OnClickListener onClickListener) {
    if (msg == null) msg = "数据加载失败";
    if (onClickListener != null) {
      Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
          .setAction("点击重试", onClickListener)
          .show();
    } else {
      Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
  }

  public void creatProgressDialog() {
    if (progressDialog == null) {
      progressDialog = ProgressDialog.show(context, "提示", "正在加载中...");
    }
  }

  public void creatProgressDialog(@NonNull String msg) {
    if (progressDialog == null) {
      progressDialog = ProgressDialog.show(context, "提示", msg);
    }
  }

  public void cancelProgressDialog() {
    if (progressDialog != null) progressDialog.dismiss();
  }

  public void updateGithubUserData(GithubLoginModel githubLoginModel) {
    githubUser.setGithubLoginModel(githubLoginModel);
  }

  @Override protected void onDestroy() {
    cancelHttp();
    super.onDestroy();
    context = null;
    if (progressDialog != null) progressDialog.dismiss();
    progressDialog = null;
  }

  @Override public void showError(String s, View.OnClickListener listener) {

  }

  @Override public void getData() {

  }

  @Override public void addTaskListener() {

  }

  @Override public Context getContext() {
    return context;
  }

  @Override public void cancelHttp() {
    new BasePresenter(this).cancelHttp();
  }
}
