package com.wenen.literead.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wenen.literead.R;
import com.wenen.literead.adapter.article.ArticleListAdapter;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.http.HttpClient;
import com.wenen.literead.http.HttpSubscriber;
import com.wenen.literead.model.article.ArticleListModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

/**
 * Created by Wen_en on 16/9/1.
 */
public class ArticleListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.indeterminate_horizontal_progress_toolbar)
    MaterialProgressBar indeterminateHorizontalProgressToolbar;
    @Bind(R.id.rcl_article_list)
    RecyclerView rclArticleList;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private Subscriber subscriber;
    private ArticleListAdapter adapter;
    private ArrayList<ArticleListModel.ResultsEntity> list = new ArrayList<>();
    private String title;
    private int page = 1;
    private boolean loadMore;
    private boolean isError;
    private String type = "Android";
    private int pagecount = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        ButterKnife.bind(this);
        if (savedInstanceState == null)
            title = getIntent().getStringExtra("title");
        else
            title = savedInstanceState.getString("title");
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclArticleList.setLayoutManager(linearLayoutManager);
        adapter = new ArticleListAdapter(list, isError);
        rclArticleList.setAdapter(adapter);
        getArticleList(type, pagecount, page);
        rclArticleList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (adapter != null) {
                    if (adapter.needLoadMore() && !isError) {
                        page++;
                        getArticleList(type, pagecount, page);
                        loadMore = true;
                    }
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", title);
    }

    private void getArticleList(String typePath, int pageCount, int page) {
        subscriber = new HttpSubscriber<ArticleListModel>(indeterminateHorizontalProgressToolbar) {
            @Override
            public void onCompleted() {
                super.onCompleted();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                isError = false;
                adapter.updateModel(list, isError);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (loadMore) {
                    isError = true;
                    adapter.updateModel(list, isError);
                } else
                    showSnackBar(indeterminateHorizontalProgressToolbar, null, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getArticleList(type, pagecount, ArticleListActivity.this.page);
                        }
                    });
            }

            @Override
            public void onNext(ArticleListModel articleListModel) {
                super.onNext(articleListModel);
                if (!loadMore) {
                    list.clear();
                }
                for (ArticleListModel.ResultsEntity entity : articleListModel.results
                        ) {
                    list.add(entity);
                }
            }
        };
        HttpClient.getSingle(APIUrl.GANK_IO_URL).getArticleList(typePath, pageCount, page, subscriber);

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
    }

    @Override
    public void onRefresh() {
        loadMore = false;
        page = 1;
        getArticleList(type, pagecount, page);
    }
}
