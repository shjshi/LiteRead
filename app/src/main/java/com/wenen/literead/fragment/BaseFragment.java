package com.wenen.literead.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wenen.literead.R;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Wen_en on 16/8/16.
 */
public class BaseFragment extends Fragment {
    private View indicatorView;
    private View failureView;
    private TextView failureText;
    private MaterialProgressBar indicator;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        indicatorView = getActivity().getLayoutInflater().inflate(R.layout.layout_indicator, null);
        failureView = indicatorView.findViewById(R.id.ll_failure_hint);
        failureText = (TextView) indicatorView
                .findViewById(R.id.tv_failure_hint);
        indicator = (MaterialProgressBar) indicatorView
                .findViewById(R.id.indeterminate_progress_small_library);
    }
    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
    /**
     * 取消全屏滚动进度条，显示失败信息并绑定点击监听器
     *
     * @param failureInfo
     * @param clickListener
     */
    public void showFailureInfo(String failureInfo,
                                View.OnClickListener clickListener) {
        failureView.setVisibility(View.VISIBLE);
        failureView.setOnClickListener(clickListener);
        failureText.setText(failureInfo);
        indicator.clearAnimation();
        indicator.setVisibility(View.GONE);
    }

    /**
     * 取消全屏滚动进度条，显示失败信息并绑定点击监听器
     *
     * @param failureInfoId
     * @param clickListener
     */
    public void showFailureInfo(int failureInfoId, View.OnClickListener clickListener) {
        failureView.setVisibility(View.VISIBLE);
        failureView.setOnClickListener(clickListener);
        failureText.setText(failureInfoId);
        indicator.clearAnimation();
        indicator.setVisibility(View.GONE);
    }

    /**
     * 取消全屏滚动进度条
     *
     * @param parentID
     */
    public void dismissIndicatorWrapper(int parentID) {
        indicator.clearAnimation();
        View parent = getActivity().findViewById(parentID);
        if (parent != null)
            ((ViewGroup) parent.getParent()).removeView(indicatorView);
        parent.setVisibility(View.VISIBLE);
    }

    /**
     * 显示全屏滚动进度条
     *
     * @param parentID
     */
    public void showIndicatorWrapper(int parentID) {
        if (failureView.getVisibility() == View.VISIBLE) {
            failureView.setVisibility(View.GONE);
            indicator.setVisibility(View.VISIBLE);
        }
        View parent = getActivity().findViewById(parentID);
        final ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        if (((ViewGroup) parent.getParent()).findViewById(R.id.rl_indicator) == null) {
            ((ViewGroup) parent.getParent()).addView(indicatorView, lp);
        }
        parent.setVisibility(View.INVISIBLE);
    }

}
