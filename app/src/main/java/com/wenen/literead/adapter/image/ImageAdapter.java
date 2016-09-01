package com.wenen.literead.adapter.image;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.R;
import com.wenen.literead.adapter.ClickResponseListener;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.model.image.ImageModel;
import com.wenen.literead.ui.ImageDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wen_en on 16/8/15.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private ArrayList<ImageModel.ListEntity> listEntities;
    private List<Integer> mHeights;
    private ArrayList<String> listUrls = new ArrayList<>();
    private String title;

    public ImageAdapter(ArrayList<ImageModel.ListEntity> listEntities, String title) {
        this.listEntities = listEntities;
        this.title = title;
        setHasStableIds(true);
    }

    public void getRandomHeight(ArrayList<ImageModel.ListEntity> listEntities) {
        mHeights = new ArrayList<>();
        for (int i = 0; i < listEntities.size(); i++) {
            mHeights.add((int) (350 + Math.random() * 150));
        }
    }

    public void setListEntities(ArrayList<ImageModel.ListEntity> listEntities) {
        this.listEntities = listEntities;
    }

    public void updateList(ArrayList<ImageModel.ListEntity> listEntities) {
        setListEntities(listEntities);
        for (ImageModel.ListEntity entity : listEntities) {
            listUrls.add(entity.src);
        }
        notifyDataSetChanged();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(itemView, new ClickResponseListener() {
            @Override
            public void onWholeClick(int position) {
                Intent intent = new Intent(context, ImageDetailActivity.class);
                intent.putStringArrayListExtra("listUrls", listUrls);
                intent.putExtra("title", title);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        if (!listEntities.isEmpty()) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = mHeights.get(position);
            holder.itemView.setLayoutParams(layoutParams);
        }
        ImageLoaderConfig.imageLoader.displayImage(APIUrl.imgUrl + listEntities.get(position).src, holder.imageView, ImageLoaderConfig.options, ImageLoaderConfig.animateFirstListener);
    }


    @Override
    public int getItemCount() {
        return listEntities.size();
    }

    @Override
    public long getItemId(int position) {
        return listEntities.get(position).hashCode();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ClickResponseListener clickResponseListener;
        private ImageView imageView;

        public ImageViewHolder(View itemView, ClickResponseListener clickResponseListener) {
            super(itemView);
            this.clickResponseListener = clickResponseListener;
            imageView = (ImageView) itemView.findViewById(R.id.iv_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickResponseListener.onWholeClick(getAdapterPosition());
        }

    }

}
