package com.wenen.literead.fragment.image;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenen.literead.ImageLoaderConfig.ImageLoaderConfig;
import com.wenen.literead.R;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.fragment.BaseFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wen_en on 16/8/29.
 */
public class ImageDetailsFragment extends BaseFragment {
    @Bind(R.id.iv_image)
    ImageView ivImage;
    private ImageLoader imageLoader = ImageLoaderConfig.imageLoader;
    private DisplayImageOptions options = ImageLoaderConfig.options;
    private ImageLoadingListener animateFirstListener = ImageLoaderConfig.animateFirstListener;

    public ImageDetailsFragment() {
    }

    public static ImageDetailsFragment newInstance(ArrayList<String> list, int position, boolean b) {
        ImageDetailsFragment fragment = new ImageDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putStringArrayList("list", list);
        args.putBoolean("isNeadAddHead", b);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_detail, container, false);
        ButterKnife.bind(this, rootView);
        ArrayList<String> list;
        list = this.getArguments().getStringArrayList("list");
        if (list != null)
            if (list.get(this.getArguments().getInt("position")) != null) {
                if (this.getArguments().getBoolean("isNeadAddHead"))
                    imageLoader.displayImage(APIUrl.imgUrl + list.get(this.getArguments()
                            .getInt("position")), ivImage, options, animateFirstListener);
           else
                    imageLoader.displayImage(list.get(this.getArguments()
                            .getInt("position")), ivImage, options, animateFirstListener);
            }
        return rootView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
