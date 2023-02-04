package com.aliyun.svideo.recorder.view.effects.filter.animfilter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.aliyun.svideo.record.R;
import com.aliyun.svideo.sdk.external.struct.effect.EffectFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 特效滤镜view的封装
 */
public class AnimFilterLoadingView extends FrameLayout {
    private AnimFilterAdapter mAnimFilterAdapter;
    private List<String> mDataList = new ArrayList<>();
    private OnAnimFilterItemClickListener mOnAnimFilterItemClickListener;

    public AnimFilterLoadingView(@NonNull Context context) {
        this(context, null);
    }

    public AnimFilterLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimFilterLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.alivc_recorder_view_anim_filter, this, true);
        RecyclerView recyclerView = view.findViewById(R.id.alivc_filter_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAnimFilterAdapter = new AnimFilterAdapter(getContext(), mDataList);

        recyclerView.setAdapter(mAnimFilterAdapter);
        // item点击事件
        mAnimFilterAdapter.setOnItemClickListener(new OnAnimFilterItemClickListener() {
            @Override
            public void onItemClick(EffectFilter effectInfo, int index) {
                if (mOnAnimFilterItemClickListener != null) {
                    mOnAnimFilterItemClickListener.onItemClick(effectInfo, index);
                }
            }

        });
    }

    public void addData(List<String> alivcFilterInfos) {
        mDataList.addAll(alivcFilterInfos);
        mAnimFilterAdapter.notifyDataSetChanged();
    }

    public void setOnAnimFilterListItemClickListener(OnAnimFilterItemClickListener listener) {
        this.mOnAnimFilterItemClickListener = listener;
    }


    public void setFilterPosition(int filterPosition) {
        mAnimFilterAdapter.setSelectedPos(filterPosition);
    }
}
