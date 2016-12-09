package com.wenen.literead.adapter.github;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.thefinestartist.finestwebview.FinestWebView;
import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.R;
import com.wenen.literead.adapter.ClickResponseListener;
import com.wenen.literead.contract.activity.github.UserDetailContract;
import com.wenen.literead.model.github.GithubFollowModel;
import com.wenen.literead.model.github.StartedModel;
import com.wenen.literead.activity.github.UserDetailActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wen_en on 16/9/8.
 */
public class GitHubFollowAdapter extends RecyclerView.Adapter<GitHubFollowAdapter.ViewHolder> {
  private List<Object> list;
  private UserDetailContract.View githubView;
  private Context appLicationContext;
  private Context context;

  public GitHubFollowAdapter(List<Object> list, UserDetailContract.View view, Context context) {
    this.list = list;
    this.githubView = view;
    this.appLicationContext = context;
  }

  public void setList(List<Object> list) {
    this.list = list;
  }

  public void updateList(List<Object> list) {
    setList(list);
    notifyDataSetChanged();
  }

  @Override
  public GitHubFollowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    context = parent.getContext();
    final View view = LayoutInflater.from(context).inflate(R.layout.github_list_item, null, false);
    return new ViewHolder(view, new ClickResponseListener() {
      @Override public void onWholeClick(int position) {
        if (list.get(position) instanceof GithubFollowModel) {
          githubView.refreashData(((GithubFollowModel) list.get(position)).login);
        } else {
          new FinestWebView.Builder(appLicationContext).statusBarColorRes(R.color.colorPrimary)
              .progressBarColorRes(R.color.colorPrimary)
              .toolbarColorRes(R.color.colorPrimary)
              .titleColorRes(R.color.white)
              .menuColorRes(R.color.white)
              .iconDefaultColorRes(R.color.white)
              .show(((StartedModel) list.get(position)).html_url);
        }
      }
    });
  }

  @Override public void onBindViewHolder(GitHubFollowAdapter.ViewHolder holder, int position) {
    if (list.get(position) instanceof GithubFollowModel) {
      ImageLoaderConfig.imageLoader.displayImage(
          ((GithubFollowModel) list.get(position)).avatar_url, holder.ivAvatar,
          ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
      holder.tvFollowerName.setText(((GithubFollowModel) list.get(position)).login);
      holder.tvFollowerUrl.setText(((GithubFollowModel) list.get(position)).html_url);
    } else {
      ImageLoaderConfig.imageLoader.displayImage(
          ((StartedModel) list.get(position)).owner.avatar_url, holder.ivAvatar,
          ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
      holder.tvFollowerName.setText(((StartedModel) list.get(position)).full_name);
      holder.tvFollowerUrl.setText(((StartedModel) list.get(position)).html_url);
    }
  }

  @Override public int getItemCount() {
    if (list != null) {
      return list.size();
    } else {
      return 0;
    }
  }

  @Override public long getItemId(int position) {
    return list.get(position).hashCode();
  }

  static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.iv_avatar) ImageView ivAvatar;
    @Bind(R.id.tv_follower_name) AppCompatTextView tvFollowerName;
    @Bind(R.id.tv_follower_url) AppCompatTextView tvFollowerUrl;
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
  public void releaseContext() {
    appLicationContext = null;
    context=null;
  }
}
