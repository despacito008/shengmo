package com.tencent.qcloud.tim.tuikit.live.component.gift.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftNumAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.NormalGiftInfo;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;

import java.util.Collections;
import java.util.List;


public class GiftNumSelectionView extends PopupWindow {
    public GiftNumSelectionView(@NonNull Context context) {
        this(context,null);
    }

    public GiftNumSelectionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    private View mLayoutRoot;
    private Context mContext;
    RecyclerView rvItem;
    public GiftNumSelectionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mLayoutRoot = LayoutInflater.from(context).inflate(R.layout.live_layout_gift_num_selection,null);
        rvItem = mLayoutRoot.findViewById(R.id.rv_layout_gift_num_select);
        setContentView(mLayoutRoot);
    }

    public void initData(List<NormalGiftInfo.DataBean.AuspiciousBean> auspiciousBeanList) {
        GiftNumAdapter numAdapter = new GiftNumAdapter(mContext,auspiciousBeanList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvItem.setAdapter(numAdapter);
        rvItem.setLayoutManager(layoutManager);
        numAdapter.setOnItemNumListener(new GiftNumAdapter.OnItemNumListener() {
            @Override
            public void doItemClick(int num) {
                if (onNumSelectListener != null) {
                    onNumSelectListener.doNumSelect(num);
                    dismiss();
                }
            }
        });
    }

    /**
     * 显示控件
     * @param tv
     */
    public void show(View tv) {
        if(isShowing()) dismiss();
        int offY = 245; //距离底部的高度
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(mLayoutRoot);
        if(!UIUtil.hasNavBar(mContext)){
            offY = offY- UIUtil.getNavigationBarHeight(mContext);
        }
        setOutsideTouchable(true);
        showAtLocation(tv, Gravity.RIGHT| Gravity.BOTTOM,35,offY);
    }

    public interface OnNumSelectListener {
        void doNumSelect(int num);
    }
    OnNumSelectListener onNumSelectListener;
    public void setOnNumSelectListener(OnNumSelectListener onNumSelectListener) {
        this.onNumSelectListener = onNumSelectListener;
    }
}
