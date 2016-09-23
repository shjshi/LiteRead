package com.wenen.literead.presenter.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.wenen.literead.contract.image.ImageDetailContract;
import com.wenen.literead.presenter.BasePresenter;

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
    public static final int DOWNLOAD_SUCCESS = 0x123;
    public static final int DOWNLOAD_ERROR = 0x124;
    private ImageDetailContract.View view;

    public ImageDetailPresenter(ImageDetailContract.View view) {
        super(view);
        addTaskListener(view);
    }

    public ImageDetailPresenter addTaskListener(ImageDetailContract.View view) {
        this.view = view;
        return this;
    }

    @Override
    public void downLoadFile(final String url) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            URL myFileURL = null;
            Bitmap bitmap = null;

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    myFileURL = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) myFileURL
                            .openConnection();
                    connection.setConnectTimeout(6000);
                    connection.setDoInput(true);
                    connection.setUseCaches(true);
                    InputStream is = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    File file = new File(createPictureDir(), UUID
                            .randomUUID() + ".jpg");
                    if (file.exists()) {
                        file.delete();
                    }
                    try {
                        FileOutputStream fOut = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                        Message msg = new Message();
                        msg.obj = file.getAbsolutePath();
                        msg.what = DOWNLOAD_SUCCESS;
                        handler.sendMessage(msg);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.obj = e.toString();
                        msg.what = DOWNLOAD_ERROR;
                        handler.sendMessage(msg);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.obj = e.toString();
                        msg.what = DOWNLOAD_ERROR;
                        handler.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = e.toString();
                    msg.what = DOWNLOAD_ERROR;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = e.toString();
                    msg.what = DOWNLOAD_ERROR;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWNLOAD_SUCCESS:
                    view.showError("图片已保存至：" + msg.obj.toString(), null);
                    break;
                case DOWNLOAD_ERROR:
                    view.showError("图片保存失败：" + msg.obj.toString(), null);
                    break;
                default:
                    break;
            }
        }
    };

    public File createPictureDir() {
        File pictureDir = new File(PICTURE_DIR);
        if (!pictureDir.exists()) {
            pictureDir.mkdirs();
        }
        return pictureDir;
    }
}
