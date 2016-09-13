package com.wenen.literead.presenter.image;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wenen.literead.R;
import com.wenen.literead.adapter.image.ImageDetailsAdapter;
import com.wenen.literead.api.APIUrl;
import com.wenen.literead.presenter.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ImageDetailActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.container)
    ViewPager container;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.indeterminate_horizontal_progress_toolbar)
    MaterialProgressBar indeterminateHorizontalProgressToolbar;

    private ImageDetailsAdapter mSectionsPagerAdapter;
    public ArrayList<String> listl;
    public String title;
    public int position;
    private int currentPage;

    private ViewPager mViewPager;
    public static final String PICTURE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/liteRead/pictures/";
    private static final int DOWNLOAD_SUCCESS = 0x123;
    private static final int DOWNLOAD_ERROR = 0x124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(R.layout.activity_image_detail, null, savedInstanceState);
        setContentView(getRootView());
        ButterKnife.bind(this);
        listl = getIntent().getStringArrayListExtra("listUrls");
        title = getIntent().getStringExtra("title");
        position = getIntent().getIntExtra("position", 0);
        currentPage = position;
        mSectionsPagerAdapter = new ImageDetailsAdapter(getSupportFragmentManager(), listl, title, position);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(position);

        mViewPager.setOffscreenPageLimit(listl.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImageDetailActivity.this.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProgressBarISvisible(indeterminateHorizontalProgressToolbar, true);
                downLoadFile(APIUrl.imgUrl + listl.get(position));
            }
        });
    }

    private void downLoadFile(final String url) {
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
            setProgressBarISvisible(indeterminateHorizontalProgressToolbar, false);
            switch (msg.what) {
                case DOWNLOAD_SUCCESS:
                    showSnackBar(indeterminateHorizontalProgressToolbar, "图片已保存至：" + msg.obj.toString(), null);
                    break;
                case DOWNLOAD_ERROR:
                    showSnackBar(indeterminateHorizontalProgressToolbar, "图片保存失败：" + msg.obj.toString(), null);
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

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
