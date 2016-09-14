package com.wenen.literead.adapter.video;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.R;
import com.wenen.literead.adapter.ClickResponseListener;
import com.wenen.literead.model.video.VideoListModel;
import com.wenen.literead.activity.video.VideoPlayerActivity;

import java.util.ArrayList;

/**
 * Created by Wen_en on 16/8/18.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.CardViewHolder> {
    private ArrayList<VideoListModel> list;

    public VideoListAdapter(ArrayList<VideoListModel> list) {
        this.list = list;
        setHasStableIds(true);
    }

    public void setList(ArrayList<VideoListModel> list) {
        this.list = list;
    }

    public void updateList(ArrayList<VideoListModel> list) {
        setList(list);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).hashCode();
    }

    @Override
    public CardViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.image_list_item, parent, false);
        return new CardViewHolder(itemView, new ClickResponseListener() {
            @Override
            public void onWholeClick(int position) {
                if (position != -1) {
                    Intent intent = new Intent();
                    intent.putExtra("roomId", list.get(position).roomUrl.split("/")[1]);
                    intent.setClass(context, VideoPlayerActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Log.e("imageListModelAdapter", list.get(position).title);
        ImageLoaderConfig.imageLoader.displayImage(list.get(position).imageUrl, holder.thumbleImg, ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
        holder.thumbleCount.setText(list.get(position).author);
        holder.thumbleTitle.setText(list.get(position).title);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView thumbleImg;
        public TextView thumbleTitle;
        public TextView thumbleCount;
        private ClickResponseListener mClickResponseListener;

        public CardViewHolder(View itemView, ClickResponseListener clickResponseListener) {
            super(itemView);
            this.mClickResponseListener = clickResponseListener;
            thumbleImg = (ImageView) itemView.findViewById(R.id.iv_thumble_img);
            thumbleTitle = (TextView) itemView.findViewById(R.id.tv_thumble_title);
            thumbleCount = (TextView) itemView.findViewById(R.id.tv_thumble_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickResponseListener.onWholeClick(getAdapterPosition());
        }

    }


}
