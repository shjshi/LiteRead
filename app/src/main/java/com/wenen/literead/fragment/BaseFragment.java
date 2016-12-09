package com.wenen.literead.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import com.squareup.leakcanary.RefWatcher;
import com.wenen.literead.LiteReadApplication;
import com.wenen.literead.contract.activity.BaseContract;
import com.wenen.literead.presenter.activity.BasePresenter;

/**
 * Created by Wen_en on 16/8/16.
 */
public class BaseFragment extends Fragment implements BaseContract.View {
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void showError(String s, View.OnClickListener listener) {

  }

  @Override public void getData() {

  }

  @Override public void addTaskListener() {

  }

  @Override public Context getContext() {
    return super.getContext();
  }

  @Override public void cancelHttp() {
    new BasePresenter(this).cancelHttp();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    cancelHttp();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    RefWatcher refWatcher = LiteReadApplication.getRefWatcher(getActivity());
    if (refWatcher != null) refWatcher.watch(this);
  }
}
