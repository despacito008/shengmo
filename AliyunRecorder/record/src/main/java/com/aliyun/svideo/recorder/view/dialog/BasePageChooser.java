package com.aliyun.svideo.recorder.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aliyun.svideo.record.R;
import com.aliyun.svideo.base.widget.PagerSlidingTabStrip;

import java.util.List;

public abstract class BasePageChooser extends BaseChooser {
    private static final String ARG_FRAGMENT = "fragments";
    private ViewPager mViewPager;
    protected List<Fragment> mPageList ;
    private PagerSlidingTabStrip mTabPageIndicator;

    private int[] tabIcons;
    private LinearLayout llBlank;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alivc_recorder_dialog_chooser_base, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTabPageIndicator = (PagerSlidingTabStrip) view.findViewById(R.id.alivc_dialog_indicator);
        mViewPager = (ViewPager)view.findViewById(R.id.alivc_dialog_container);
        llBlank = view.findViewById(R.id.ll_blank);


        mTabPageIndicator.setTextColorResource(R.color.aliyun_svideo_tab_text_color_selector);
        mTabPageIndicator.setTabViewId(R.layout.aliyun_svideo_layout_tab_top);

        if (mPageList == null) {
            mPageList = createPagerFragmentList();
        }
        mViewPager.setOffscreenPageLimit(mPageList.size());
        mViewPager.setAdapter(new DialogPageAdapter(getChildFragmentManager(), mPageList));
        mTabPageIndicator.setViewPager(mViewPager);
    }

    ViewPager.SimpleOnPageChangeListener simpleOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if (onUpdatePageSelectedListener != null) {
                onUpdatePageSelectedListener.onPageSelected(position);
            }
        }
    };
    /**
     * ?????????ViewPager?????????fragment
     *
     */
    public abstract List<Fragment>   createPagerFragmentList();

    @Override
    public void onStart() {
        super.onStart();

        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(simpleOnPageChangeListener);
        }

        llBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    ??????crash:java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
                    ??????:after onSaveInstanceState invoke commit,??? show ????????? commit ??????
                    fragment is added and its state has already been saved???
                    Any operations that would change saved state should not be performed if this method returns true
                */
//                if (isStateSaved()) {
//                    return ;
//                }
                dismiss();
            }
        });
    }

    /**
     * tab????????????
     */
    private OnUpdatePageSelectedListener onUpdatePageSelectedListener;
    public interface OnUpdatePageSelectedListener {
        void onPageSelected(int position);
    }

    public void setOnUpdatePageSelectedListener(
        OnUpdatePageSelectedListener onUpdatePageSelectedListener) {
        this.onUpdatePageSelectedListener = onUpdatePageSelectedListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(simpleOnPageChangeListener);
        }
    }
}
