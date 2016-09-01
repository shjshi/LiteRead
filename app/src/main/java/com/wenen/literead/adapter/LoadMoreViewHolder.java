package com.wenen.literead.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenen.literead.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Wen_en on 16/8/29.
 */
public class LoadMoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.iv_bug)
    public ImageView ivBug;
    @Bind(R.id.progressBar)
    public MaterialProgressBar progressBar;
    @Bind(R.id.tv_msg)
    public TextView tvMsg;
    private ClickResponseListener mClickResponseListener;

    public LoadMoreViewHolder(View itemView, ClickResponseListener clickResponseListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mClickResponseListener = clickResponseListener;
    }

    @Override
    public void onClick(View view) {
        mClickResponseListener.onWholeClick(getAdapterPosition());
    }
}
