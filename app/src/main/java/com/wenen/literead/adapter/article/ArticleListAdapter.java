package com.wenen.literead.adapter.article;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thefinestartist.finestwebview.FinestWebView;
import com.wenen.literead.R;
import com.wenen.literead.adapter.ClickResponseListener;
import com.wenen.literead.adapter.LoadMoreViewHolder;
import com.wenen.literead.model.article.ArticleListModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wen_en on 16/9/1.
 */
public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ArticleListModel.ResultsEntity> list;
    private static final int LOAD_MORE = 0;
    private static final int NO_LOAD_MORE = 1;
    private boolean needLoadMore;
    private boolean isError;

    public ArticleListAdapter(ArrayList<ArticleListModel.ResultsEntity> list, boolean isError) {
        this.list = list;
        this.isError = isError;
        setHasStableIds(true);
    }

    public void setModel(ArrayList<ArticleListModel.ResultsEntity> list, boolean isError) {
        this.list = list;
        this.isError = isError;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() > 1 && position == getItemCount() - 1) {
            needLoadMore = true;
            return LOAD_MORE;
        } else {
            needLoadMore = false;
            return NO_LOAD_MORE;
        }
    }

    public void updateModel(ArrayList<ArticleListModel.ResultsEntity> list, boolean isError) {
        setModel(list, isError);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        if (viewType != LOAD_MORE) {
            View view = LayoutInflater.from(context).inflate(R.layout.article_list_item, null, false);
            return new ViewHolder(view, new ClickResponseListener() {
                @Override
                public void onWholeClick(int position) {
                    new FinestWebView.Builder(context).statusBarColorRes(R.color.colorPrimary)
                            .progressBarColorRes(R.color.colorPrimary).toolbarColorRes(R.color.colorPrimary).titleColorRes(R.color.white)
                            .menuColorRes(R.color.white).iconDefaultColorRes(R.color.white)
                            .show(list.get(position).url);
                }
            });
        } else {
            View itemView = LayoutInflater.from(context).inflate(R.layout.load_more_view, parent, false);
            return new LoadMoreViewHolder(itemView, new ClickResponseListener() {
                @Override
                public void onWholeClick(int position) {

                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder && getItemCount() > 1) {
            ((ViewHolder) holder).tvTitle.setText(list.get(position).desc);
            ((ViewHolder) holder).tvAuthor.setText(list.get(position).who);
            ((ViewHolder) holder).tvTime.setText(list.get(position).createdAt);
        }
        if (holder instanceof LoadMoreViewHolder) {
            if (isError) {
                ((LoadMoreViewHolder) holder).tvMsg.setText("加载失败!!!");
                ((LoadMoreViewHolder) holder).progressBar.setVisibility(View.GONE);
                ((LoadMoreViewHolder) holder).ivBug.setVisibility(View.VISIBLE);
            } else {
                ((LoadMoreViewHolder) holder).tvMsg.setText("正在加载中...");
                ((LoadMoreViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                ((LoadMoreViewHolder) holder).ivBug.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        if (!list.isEmpty())
            return list.size() + 1;
        else
            return 0;
    }

    @Override
    public long getItemId(int position) {
        if (position == getItemCount() - 1)
            return position;
        else
            return list.get(position).hashCode();

    }


    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.tv_title)
        AppCompatTextView tvTitle;
        @Bind(R.id.tv_author)
        AppCompatTextView tvAuthor;
        @Bind(R.id.tv_time)
        AppCompatTextView tvTime;
        ClickResponseListener clickResponseListener;

        ViewHolder(View view, ClickResponseListener clickResponseListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.clickResponseListener = clickResponseListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickResponseListener.onWholeClick(getAdapterPosition());
        }
    }

    public boolean needLoadMore() {
        return needLoadMore;
    }

    public void setIsLoadMore(boolean needLoadMore) {
        this.needLoadMore = needLoadMore;
    }


}
