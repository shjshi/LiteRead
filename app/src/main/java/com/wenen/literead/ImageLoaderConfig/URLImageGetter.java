package com.wenen.literead.ImageLoaderConfig;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.NonViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by Wen_en on 16/9/5.
 */
public class URLImageGetter implements Html.ImageGetter{
    private String shopDeString;
    private TextView textView;
    Context context;
    private DisplayImageOptions options;
    public URLImageGetter(String shopDeString,Context context,TextView textView,DisplayImageOptions options) {
        this.shopDeString = shopDeString;
        this.context = context;
        this.textView = textView;
        this.options = options;
    }
    @Override
    public Drawable getDrawable(String source) {

        final URLDrawable urlDrawable = new URLDrawable();
        ImageSize imageSize = new ImageSize(480,320);
        NonViewAware nonViewAware = new NonViewAware(imageSize, ViewScaleType.CROP);
        ImageLoader.getInstance().displayImage(source,nonViewAware,options,new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                urlDrawable.bitmap = loadedImage;
                urlDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
                textView.invalidate();
                textView.setText(textView.getText());
            }
        });
        return urlDrawable;
    }
}
