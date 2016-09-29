package com.wenen.literead.activity.article;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wenen.literead.R;
import com.wenen.literead.activity.BaseActivity;
import com.wenen.literead.adapter.article.ArticleListAdapter;
import com.wenen.literead.contract.activity.article.ArticleListContract;
import com.wenen.literead.model.article.ArticleListModel;
import com.wenen.literead.presenter.activity.article.ArticleListPresenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Wen_en on 16/9/1.
 */
public class ArticleListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, ArticleListContract.View {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.indeterminate_horizontal_progress_toolbar)
    MaterialProgressBar indeterminateHorizontalProgressToolbar;
    @Bind(R.id.rcl_article_list)
    RecyclerView rclArticleList;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private ArticleListAdapter adapter;
    private ArrayList<ArticleListModel.ResultsEntity> list = new ArrayList<>();
    private String title;
    private int page = 1;
    private boolean loadMore;
    private boolean isError;
    private String type = "Android";
    private int pagecount = 5;

    private RecyclerViewOnScrollListener recyclerViewOnScrollListener;
    private ArticleListPresenter articleListPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_article_list, null, savedInstanceState);
        setContentView(getRootView());
        ButterKnife.bind(this);
        recyclerViewOnScrollListener = new RecyclerViewOnScrollListener();
        if (savedInstanceState == null) {
            title = getIntent().getStringExtra("title");
            type = getIntent().getStringExtra("type");
            pagecount = 5;
            page = 1;
        } else {
            page = savedInstanceState.getInt("page", 1);
            title = savedInstanceState.getString("title");
            type = savedInstanceState.getString("type");
            pagecount = savedInstanceState.getInt("pagecount", 5);
        }
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclArticleList.setLayoutManager(linearLayoutManager);
        adapter = new ArticleListAdapter(list, isError);
        rclArticleList.setAdapter(adapter);
        articleListPresenter = new ArticleListPresenter(this);
        getData();
        rclArticleList.addOnScrollListener(recyclerViewOnScrollListener);
    }
    private class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (adapter != null) {
                if (adapter.needLoadMore() && !isError) {
                    page++;
                    getData();
                    loadMore = true;
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", title);
        outState.putString("type", type);
        outState.putInt("pagecount", pagecount);
        outState.putInt("page", page);
    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        articleListPresenter = null;
        recyclerViewOnScrollListener = null;
    }

    @Override
    public void onRefresh() {
        loadMore = false;
        page = 1;
        getData();
    }

    @Override
    public void showData(ArticleListModel articleListModel) {
        if (!loadMore) {
            list.clear();
        }
        for (ArticleListModel.ResultsEntity entity : articleListModel.results
                ) {
            list.add(entity);
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        isError = false;
        adapter.updateModel(list, isError);
    }

    @Override
    public void showError(String s, View.OnClickListener listener) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (loadMore) {
            isError = true;
            adapter.updateModel(list, isError);
        } else
            showSnackBar(indeterminateHorizontalProgressToolbar, s, listener);
    }

    @Override
    public void getData() {
        articleListPresenter.getArticleList(type, pagecount, page);
    }

    @Override
    public void addTaskListener() {

    }

    @Override
    public MaterialProgressBar getProgressBar() {
        return indeterminateHorizontalProgressToolbar;
    }
}
