package com.wenen.literead.presenter.activity.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.wenen.literead.contract.activity.image.ImageDetailContract;
import com.wenen.literead.presenter.activity.BasePresenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Wen_en on 16/9/14.
 */
public class ImageDetailPresenter extends BasePresenter implements ImageDetailContract.Presenter {
    public static final String PICTURE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/liteRead/pictures/";
    private ImageDetailContract.View view;
    private DownLoadAsyncTask downLoadAsyncTask;

    public ImageDetailPresenter(ImageDetailContract.View view) {
        super(view);
        addTaskListener(view);
    }

    public ImageDetailPresenter addTaskListener(ImageDetailContract.View view) {
        this.view = view;
        return this;
    }

    private class DownLoadAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                return downLoad(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                view.showError("图片保存失败：" + e.toString(), null);
                return "图片保存失败：" + e.toString();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            view.showError(s, null);
            downLoadAsyncTask = null;
        }
    }

    private String downLoad(String url) throws Exception {
        URL myFileURL=new URL(url);
        HttpURLConnection connection = (HttpURLConnection) myFileURL
                .openConnection();
        connection.setConnectTimeout(6000);
        connection.setDoInput(true);
        connection.setUseCaches(true);
        InputStream is = connection.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        is.close();
        File file = new File(createPictureDir(), UUID
                .randomUUID() + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fOut = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        fOut.flush();
        fOut.close();
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = null;
        view.showMsg("图片已保存至：" + file.getAbsolutePath());
        return "图片已保存至：" + file.getAbsolutePath();
    }

    @Override
    public void downLoadFile(String url) {
        downLoadAsyncTask = new DownLoadAsyncTask();
        downLoadAsyncTask.execute(url, null, null);
    }
    public File createPictureDir() {
        File pictureDir = new File(PICTURE_DIR);
        if (!pictureDir.exists()) {
            pictureDir.mkdirs();
        }
        return pictureDir;
    }
}
