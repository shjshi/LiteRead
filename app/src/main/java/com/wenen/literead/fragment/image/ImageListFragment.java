package com.wenen.literead.fragment.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenen.literead.R;
import com.wenen.literead.adapter.image.ImageListAdapter;
import com.wenen.literead.contract.fragment.image.ImageListContract;
import com.wenen.literead.fragment.BaseFragment;
import com.wenen.literead.model.image.ImageListModel;
import com.wenen.literead.presenter.fragment.image.ImageListPresenter;

import com.wenen.literead.utils.EndlessRecyclerOnScrollListener;
import com.wenen.literead.utils.HeaderViewRecyclerAdapter;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wen_en on 16/8/12.
 */
public class ImageListFragment extends BaseFragment
    implements SwipeRefreshLayout.OnRefreshListener, ImageListContract.View {
  @Bind(R.id.rcl_image_list) RecyclerView rclImageList;
  @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  private boolean isRefreshed = false;
  /**
   * URL
   */
  private int id = 1;
  private int page = 1;
  private int rows = 5;
  private ArrayList<ImageListModel.TngouEntity> list = new ArrayList<>();
  private ImageListAdapter mAdapter;
  private ImageListPresenter imageListPresenter;
  private EndlessRecyclerOnScrollListener recyclerViewListener;
  private HeaderViewRecyclerAdapter headerViewRecyclerAdapter;
  private static final String TAG = "ImageListFragment";
  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      id = savedInstanceState.getInt("id");
      page = savedInstanceState.getInt("page");
      rows = savedInstanceState.getInt("rows");
    } else {
      id = getArguments().getInt("id");
    }
    page = 1;
    Log.e("id", id + "");
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("id", id);
    outState.putInt("page", page);
    outState.putInt("rows", rows);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_image_list, container, false);
    ButterKnife.bind(this, view);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerViewListener = new RecyclerViewListener(linearLayoutManager);
    rclImageList.setLayoutManager(linearLayoutManager);
    mAdapter = new ImageListAdapter(list);
    headerViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mAdapter);
    createLoadMoreView();
    rclImageList.setAdapter(headerViewRecyclerAdapter);
    swipeRefreshLayout.setOnRefreshListener(this);
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    rclImageList.addOnScrollListener(recyclerViewListener);
    return view;
  }

  private class RecyclerViewListener extends EndlessRecyclerOnScrollListener {

    public RecyclerViewListener(LinearLayoutManager linearLayoutManager) {
      super(linearLayoutManager);
    }

    @Override public void onLoadMore(int currentPage) {
      page = currentPage;
      Log.e(TAG, "onLoadMore: "+page );
      if (imageListPresenter!=null)
      imageListPresenter.getImgThumbleList(id, page, rows);
    }
  }
  private void createLoadMoreView() {
    View loadMoreView =
        LayoutInflater.from(getActivity()).inflate(R.layout.load_more_view, rclImageList, false);
    headerViewRecyclerAdapter.addFooterView(loadMoreView);
  }
  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onRefresh() {
    doRefresh();
  }

  private void refreshIf(boolean needRefresh) {
    if (needRefresh) {
      doRefresh();
    }
  }

  private void doRefresh() {
    page = 1;
    if (imageListPresenter!=null)
    imageListPresenter.getImgThumbleList(id, page, rows);
  }

  private boolean shouldRefreshOnVisibilityChange(boolean isVisibleToUser) {
    return isVisibleToUser && !isRefreshed;
  }

  @Override public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    imageListPresenter = new ImageListPresenter(this);
    refreshIf(shouldRefreshOnVisibilityChange(isVisibleToUser));
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void showData(ArrayList<ImageListModel.TngouEntity> list) {
    isRefreshed = true;
    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
    mAdapter.updateList(list);
  }

  @Override public void shoeError(String e) {
    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
    Snackbar.make(rclImageList, "数据加载失败", Snackbar.LENGTH_INDEFINITE)
        .setAction("点击重试", new View.OnClickListener() {
          @Override public void onClick(View view) {
            imageListPresenter.getImgThumbleList(id, page, rows);
          }
        });
  }

  @Override public void onDestroy() {
    super.onDestroy();
    imageListPresenter = null;
    recyclerViewListener = null;
  }
}
