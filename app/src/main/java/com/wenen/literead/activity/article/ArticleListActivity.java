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
import com.wenen.literead.contract.article.ArticleListContract;
import com.wenen.literead.model.article.ArticleListModel;
import com.wenen.literead.presenter.article.ArticleListPresenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/1.
 */
public class ArticleListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, ArticleListContract.View {

    @Override
    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder {
        @Bind(R.id.toolbar)
        public Toolbar toolbar;
        @Bind(R.id.indeterminate_horizontal_progress_toolbar)
        public MaterialProgressBar indeterminateHorizontalProgressToolbar;
        @Bind(R.id.rcl_article_list)
        public RecyclerView rclArticleList;
        @Bind(R.id.swipe_refresh_layout)
        public SwipeRefreshLayout swipeRefreshLayout;
        public Subscriber subscriber;
        public ArticleListAdapter adapter;
        public ArrayList<ArticleListModel.ResultsEntity> list = new ArrayList<>();
        public String title;
        public int page = 1;
        public boolean loadMore;
        public boolean isError;
        public String type = "Android";
        public int pagecount = 5;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    ViewHolder viewHolder;
    private ArticleListPresenter articleListPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_article_list, null, savedInstanceState);
        setContentView(getRootView());
        viewHolder = new ViewHolder(getRootView());
        if (savedInstanceState == null) {
            viewHolder.title = getIntent().getStringExtra("title");
            viewHolder.type = getIntent().getStringExtra("type");
            viewHolder.pagecount = 5;
            viewHolder.page = 1;

        } else {
            viewHolder.page = savedInstanceState.getInt("page", 1);
            viewHolder.title = savedInstanceState.getString("title");
            viewHolder.type = savedInstanceState.getString("type");
            viewHolder.pagecount = savedInstanceState.getInt("pagecount", 5);
        }
        viewHolder.swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewHolder.rclArticleList.setLayoutManager(linearLayoutManager);
        viewHolder.adapter = new ArticleListAdapter(viewHolder.list, viewHolder.isError);
        viewHolder.rclArticleList.setAdapter(viewHolder.adapter);
        articleListPresenter = new ArticleListPresenter(this);
        articleListPresenter.getArticleList(viewHolder.type, viewHolder.pagecount, viewHolder.page);
        viewHolder.rclArticleList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (viewHolder.adapter != null) {
                    if (viewHolder.adapter.needLoadMore() && !viewHolder.isError) {
                        viewHolder.page++;
                        articleListPresenter.getArticleList(viewHolder.type, viewHolder.pagecount, viewHolder.page);
                        viewHolder.loadMore = true;
                    }
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", viewHolder.title);
        outState.putString("type", viewHolder.type);
        outState.putInt("pagecount", viewHolder.pagecount);
        outState.putInt("page", viewHolder.page);
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewHolder.toolbar.setTitle(viewHolder.title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        viewHolder = null;
    }

    @Override
    public void onRefresh() {
        viewHolder.loadMore = false;
        viewHolder.page = 1;
        articleListPresenter.getArticleList(viewHolder.type, viewHolder.pagecount, viewHolder.page);
    }
}
