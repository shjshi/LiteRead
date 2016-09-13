package com.wenen.literead.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Wen_en on 16/9/13.
 */
public interface IDelegate {
    void create(int layoutId, ViewGroup v, Bundle b);

    View getRootView();

    void canTSetToolBar(boolean b);
}
