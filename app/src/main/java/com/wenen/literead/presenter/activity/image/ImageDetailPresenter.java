package com.wenen.literead.presenter.activity.image;

import android.app.Activity;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;
import com.wenen.literead.contract.activity.image.ImageDetailContract;
import com.wenen.literead.presenter.activity.BasePresenter;
import java.io.File;
import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Wen_en on 16/9/14.
 */
public class ImageDetailPresenter extends BasePresenter implements ImageDetailContract.Presenter {
  public static final String PICTURE_DIR =
      Environment.getExternalStorageDirectory().getAbsolutePath() + "/liteRead/pictures/";
  private ImageDetailContract.View view;
  private long downloadId;

  public ImageDetailPresenter(ImageDetailContract.View view) {
    super(view);
    addTaskListener(view);
  }

  public ImageDetailPresenter addTaskListener(ImageDetailContract.View view) {
    this.view = view;
    return this;
  }

  @Override public void downLoadFile(String url) {
    DownloadManager down = (DownloadManager) ((Activity) view).getSystemService(DOWNLOAD_SERVICE);
    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
    request.setDestinationInExternalPublicDir(createPictureDir().getAbsolutePath(), url);
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
    request.setTitle("图片下载中");
    long downloadId = down.enqueue(request);
    setDownLoadId(downloadId);
  }

  private void setDownLoadId(long downloadId) {
    this.downloadId = downloadId;
  }

  public long getDownloadId() {
    return downloadId;
  }

  private File createPictureDir() {
    File pictureDir = new File(PICTURE_DIR);
    if (!pictureDir.exists()) {
      pictureDir.mkdirs();
    }
    return pictureDir;
  }
}
