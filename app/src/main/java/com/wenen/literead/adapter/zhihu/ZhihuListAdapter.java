package com.wenen.literead.adapter.zhihu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.R;
import com.wenen.literead.adapter.ClickResponseListener;
import com.wenen.literead.model.zhihu.ZhihuListModel;
import com.wenen.literead.presenter.zhihu.ZhihuDetailActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wen_en on 16/9/5.
 */
public class ZhihuListAdapter extends RecyclerView.Adapter<ZhihuListAdapter.ViewHolder> {
    private List<ZhihuListModel.TopStoriesEntity> list;

    public ZhihuListAdapter(List<ZhihuListModel.TopStoriesEntity> list) {
        this.list = list;
        setHasStableIds(true);
    }

    private void setList(List<ZhihuListModel.TopStoriesEntity> list) {
        this.list = list;
    }

    public void updateList(List<ZhihuListModel.TopStoriesEntity> list) {
        setList(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.zhihu_list_item, null, false);
        return new ViewHolder(view, new ClickResponseListener() {
            @Override
            public void onWholeClick(int position) {
                Intent intent = new Intent();
                intent.setClass(context, ZhihuDetailActivity.class);
                intent.putExtra("id", list.get(position).id);
                intent.putExtra("title", list.get(position).title);
                intent.putExtra("imgUrl",list.get(position).image);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoaderConfig.imageLoader.displayImage(list.get(position).image, holder.ivZhihuImg,
                ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
        holder.tvZhihuTitle.setText(list.get(position).title);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).hashCode();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.iv_zhihu_img)
        ImageView ivZhihuImg;
        @Bind(R.id.tv_zhihu_title)
        TextView tvZhihuTitle;
        @Bind(R.id.news_list_card_view)
        CardView newsListCardView;
        private ClickResponseListener clickResponseListener;

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
}
