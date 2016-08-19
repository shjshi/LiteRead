package com.wenen.literead.adapter.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.R;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.model.image.ImageListModel;
import com.wenen.literead.ui.ThumbleActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Wen_en on 16/8/15.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.CardViewHolder> {
    private ArrayList<ImageListModel.TngouEntity> list;

    public ImageListAdapter(ArrayList<ImageListModel.TngouEntity> list) {
        this.list = list;
        setHasStableIds(true);
    }

    public void setList(ArrayList<ImageListModel.TngouEntity> list) {
        this.list = list;
    }

    public void updateList(ArrayList<ImageListModel.TngouEntity> list) {
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
        return new CardViewHolder(itemView, new CardViewHolder.ClickResponseListener() {
            @Override
            public void onWholeClick(int position) {
                if (position != -1) {
                    Intent intent = new Intent(context, ThumbleActivity.class);
                    intent.putExtra("id", list.get(position).id);
                    intent.putExtra("title", list.get(position).title);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        // for (int i = 0; i < list.size(); i++) {
        Log.e("imageListModelAdapter", list.get(position).title);
        //}
        ImageLoaderConfig.imageLoader.displayImage(APIUrl.imgUrl + list.get(position).img, holder.thumbleImg, ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
        holder.thumbleCount.setText("照片数量:" + list.get(position).size + "");
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

        public interface ClickResponseListener {
            void onWholeClick(int position);

        }

    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
