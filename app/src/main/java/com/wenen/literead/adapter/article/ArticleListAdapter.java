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
import com.wenen.literead.model.article.ArticleListModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wen_en on 16/9/1.
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {
  private ArrayList<ArticleListModel.ResultsEntity> list;

  public ArticleListAdapter(ArrayList<ArticleListModel.ResultsEntity> list) {
    this.list = list;
    setHasStableIds(true);
  }

  public void setModel(ArrayList<ArticleListModel.ResultsEntity> list) {
    this.list = list;
  }
  public void updateModel(ArrayList<ArticleListModel.ResultsEntity> list) {
    setModel(list);
    notifyDataSetChanged();
  }

  @Override
  public ArticleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final Context context = parent.getContext();
      View view = LayoutInflater.from(context).inflate(R.layout.article_list_item, null, false);
      return new ViewHolder(view, new ClickResponseListener() {
        @Override public void onWholeClick(int position) {
          new FinestWebView.Builder(context).statusBarColorRes(R.color.colorPrimary)
              .progressBarColorRes(R.color.colorPrimary)
              .toolbarColorRes(R.color.colorPrimary)
              .titleColorRes(R.color.white)
              .menuColorRes(R.color.white)
              .iconDefaultColorRes(R.color.white)
              .show(list.get(position).url);
        }
      });
  }

  @Override public void onBindViewHolder(ArticleListAdapter.ViewHolder holder, int position) {
    holder.tvTitle.setText(list.get(position).desc);
    holder.tvAuthor.setText(list.get(position).who);
    holder.tvTime.setText(list.get(position).createdAt);
  }

  @Override public int getItemCount() {
    if (!list.isEmpty()) {
      return list.size();
    } else {
      return 0;
    }
  }

  @Override public long getItemId(int position) {

      return list.get(position).hashCode();

  }

  static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.tv_title) AppCompatTextView tvTitle;
    @Bind(R.id.tv_author) AppCompatTextView tvAuthor;
    @Bind(R.id.tv_time) AppCompatTextView tvTime;
    ClickResponseListener clickResponseListener;

    ViewHolder(View view, ClickResponseListener clickResponseListener) {
      super(view);
      ButterKnife.bind(this, view);
      this.clickResponseListener = clickResponseListener;
      view.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      clickResponseListener.onWholeClick(getAdapterPosition());
    }
  }
}
