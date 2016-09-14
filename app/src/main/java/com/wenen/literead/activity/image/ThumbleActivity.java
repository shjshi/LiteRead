package com.wenen.literead.activity.image;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wenen.literead.R;
import com.wenen.literead.activity.BaseActivity;
import com.wenen.literead.adapter.image.ImageAdapter;
import com.wenen.literead.contract.image.ThumbleContract;
import com.wenen.literead.model.image.ImageModel;
import com.wenen.literead.presenter.image.ThumblePresenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Subscriber;

public class ThumbleActivity extends BaseActivity implements ThumbleContract.View {


    @Override
    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder {
        @Bind(R.id.toolbar)
        public Toolbar toolbar;
        @Bind(R.id.indeterminate_horizontal_progress_toolbar)
        public MaterialProgressBar indeterminateHorizontalProgressToolbar;
        @Bind(R.id.rcl_image_list)
        public RecyclerView rclImageList;
        @Bind(R.id.nsv_parent)
        public NestedScrollView nsvParent;
        @Bind(R.id.fab)
        public FloatingActionButton fab;
        public int id;
        public String title;
        public Subscriber subscribers;
        public ArrayList<ImageModel.ListEntity> listEntities = new ArrayList<>();
        public ImageAdapter mAdapter;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private ThumblePresenter thumblePresenter;
    private ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_thumble, null, savedInstanceState);
        setContentView(getRootView());
        viewHolder = new ViewHolder(getRootView());
        thumblePresenter = new ThumblePresenter(this);
        ButterKnife.bind(this);
        viewHolder.title = getIntent().getStringExtra("title");
        viewHolder.id = getIntent().getIntExtra("id", 0);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        viewHolder.rclImageList.setLayoutManager(staggeredGridLayoutManager);
        viewHolder.mAdapter = new ImageAdapter(viewHolder.listEntities, viewHolder.title);
        viewHolder.rclImageList.setAdapter(viewHolder.mAdapter);
        viewHolder.mAdapter.getRandomHeight(viewHolder.listEntities);
        SpacesItemDecoration decoration = new SpacesItemDecoration(2, 10, true);
        viewHolder.rclImageList.addItemDecoration(decoration);
        viewHolder.rclImageList.setHasFixedSize(true);
        viewHolder.rclImageList.setItemAnimator(new DefaultItemAnimator());
        thumblePresenter.getImage(viewHolder.id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewHolder.toolbar.setTitle(viewHolder.title);
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public SpacesItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewHolder = null;
    }
}
