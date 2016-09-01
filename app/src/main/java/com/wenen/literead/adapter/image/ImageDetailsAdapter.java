package com.wenen.literead.adapter.image;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wenen.literead.fragment.ImageDetailsFragment;

import java.util.ArrayList;

/**
 * Created by Wen_en on 16/8/29.
 */
public class ImageDetailsAdapter extends FragmentPagerAdapter {
    ArrayList<String> list;
    String title;
    int position;

    public ImageDetailsAdapter(FragmentManager fm, ArrayList<String> list, String title, int position) {
        super(fm);
        this.list = list;
        this.title = title;
        this.position = position;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageDetailsFragment.newInstance(list, position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
