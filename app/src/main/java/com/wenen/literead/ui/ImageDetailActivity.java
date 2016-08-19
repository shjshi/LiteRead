package com.wenen.literead.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wenen.literead.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageDetailActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.container)
    ViewPager container;
    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public ArrayList<String> listl;
    public String title;
    public int position;
    private int currentPage;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public ImageDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        listl = getIntent().getStringArrayListExtra("listUrls");
        title = getIntent().getStringExtra("title");
        position = getIntent().getIntExtra("position", 0);
        currentPage = position;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), listl, title, position);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.setOffscreenPageLimit(listl.size());
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

    public static class PlaceholderFragment extends Fragment {
        @Bind(R.id.iv_image)
        ImageView ivImage;
        private ImageLoader imageLoader = ImageLoader.getInstance();
        private DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.noimage)
                .showImageOnFail(R.mipmap.noimage)
                .showImageForEmptyUri(R.mipmap.lks_for_blank_url)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private static String imgUrl = "http://tnfs.tngou.net/img";

        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(ArrayList<String> list, int position) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            args.putStringArrayList("list", list);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_image_detail, container, false);
            ButterKnife.bind(this, rootView);
            imageLoader.displayImage(imgUrl + this.getArguments().getStringArrayList("list").get(this.getArguments().getInt("position")), ivImage, options, animateFirstListener);
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        ArrayList<String> list;
        String title;
        int position;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<String> list, String title, int position) {
            super(fm);
            this.list = list;
            this.title = title;
            this.position = position;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(list, position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
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
