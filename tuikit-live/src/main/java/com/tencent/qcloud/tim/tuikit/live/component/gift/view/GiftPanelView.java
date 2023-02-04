package com.tencent.qcloud.tim.tuikit.live.component.gift.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfoBean;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter.GiftPanelAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter.GiftViewPagerAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter.NewGiftPanelAdapter;
import com.yhao.floatwindow.IFloatWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class GiftPanelView extends LinearLayout {
    private LinearLayout mLayoutRoot;
    private ViewPager mViewpager;
    private LinearLayout mDotsLayout;
    private Context mContext;
    private TextView tvEmpty;
    public GiftPanelView(Context context) {
        this(context,null);
    }

    public GiftPanelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GiftPanelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mLayoutRoot = (LinearLayout) inflate(context, R.layout.panel_layout_gift, this);
        mDotsLayout = mLayoutRoot.findViewById(R.id.dots_container);
        mViewpager = mLayoutRoot.findViewById(R.id.gift_panel_view_pager);
        tvEmpty = mLayoutRoot.findViewById(R.id.tv_panel_gift_empty);
    }
    List<View> mGiftViews;
    List<String> mChooseGiftIdList;

    public void initChooseId(List<String> idList) {
        this.mChooseGiftIdList = idList;
    }


    public void initGiftData(List<GiftInfoBean> giftInfoList) {
        mGiftBean = giftInfoList;
        mGiftViews = new ArrayList<>();
        int listSize = giftInfoList.size();
        if (listSize == 0) {
            showEmptyView();
        }
        int columns = 4;
        int rows = 2;
        int pageSize =  listSize % (columns * rows) == 0 ? listSize / (columns * rows) : listSize / (columns * rows) + 1;
        // 获取页数
        for (int i = 0; i < pageSize; i++) {
            mGiftViews.add(viewPagerItem(mContext, i, giftInfoList, columns, rows));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
            params.setMargins(10, 0, 10, 0);
            if (pageSize > 1) {
                mDotsLayout.addView(dotsItem(i), params);
            }
        }
        if (pageSize > 1) {
            mDotsLayout.setVisibility(View.VISIBLE);
        } else {
            mDotsLayout.setVisibility(View.INVISIBLE);
        }
        GiftViewPagerAdapter giftViewPagerAdapter = new GiftViewPagerAdapter(mGiftViews);
        mViewpager.setAdapter(giftViewPagerAdapter);
        mViewpager.addOnPageChangeListener(new PageChangeListener2(mDotsLayout, mGiftViews));
        mViewpager.setCurrentItem(0);
        if (pageSize > 1) {
            mDotsLayout.getChildAt(0).setSelected(true);
        }
    }

    List<GiftInfoBean> giftInfoList;
    /**
     * 礼物面板每一页的数据
     *
     * @param context
     * @param pageIndex    第几页
     * @param giftInfoList 礼物集合
     * @param columns      列数
     * @param rows         行数
     * @return
     */
    public View viewPagerItem(final Context context, final int pageIndex, List<GiftInfoBean> giftInfoList, int columns, int rows) {
        this.giftInfoList = giftInfoList;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.live_layout_gift_panel, null);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.chart_face_gv);
        GridLayoutManager girdLayoutManager = new GridLayoutManager(context, columns);
        recyclerView.setLayoutManager(girdLayoutManager);
        int maxPageItems = columns * rows;
        int startIndex = pageIndex * maxPageItems;
        int endIndex = Math.min(maxPageItems * (pageIndex + 1), giftInfoList.size());
        List<GiftInfoBean> subList = new ArrayList<>(giftInfoList.subList(startIndex, endIndex));
        NewGiftPanelAdapter mGvAdapter = new NewGiftPanelAdapter(recyclerView, pageIndex, subList, context, mChooseGiftIdList,updateGiftInfo);
        recyclerView.setAdapter(mGvAdapter);
        mGvAdapter.setOnItemClickListener(new NewGiftPanelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, GiftInfoBean giftInfo, int position, int index) {
//                mPosition = position;
//                mSelectGiftInfo = giftInfo;
//                mSelectGiftPageIndex = index;
//                if(onGiftControllerDelegate != null){
//                    onGiftControllerDelegate.onClickDataChanged(giftInfo,position);
//                    onGiftControllerDelegate.onPopDismiss();
//                }
                if (!mChooseGiftIdList.contains(giftInfo.getGift_id())) {
                    mChooseGiftIdList.clear();
                    mChooseGiftIdList.add(giftInfo.getGift_id());
                    refreshChooseState();
                    if (onGiftListener != null) {
                        onGiftListener.doGiftChoose(giftInfo);
                    }
                }
            }
        });
        return recyclerView;
    }

    /**
     * 礼物页切换时，底部小圆点
     *
     * @param position
     * @return
     */
    private ImageView dotsItem(int position) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.live_layout_gift_dot, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }

    /**
     * 礼物页改变时，dots效果也要跟着改变
     */
    class PageChangeListener2 implements ViewPager.OnPageChangeListener {
        LinearLayout mDotsLayout;
        List<View> mGiftViews;

        public PageChangeListener2(LinearLayout dotLayout, List<View> giftView) {
            mDotsLayout = dotLayout;
            mGiftViews = giftView;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(position).setSelected(true);
            refreshChooseState();
//            for (int i = 0; i < mGiftViews.size(); i++) {//清除选中，当礼物页面切换到另一个礼物页面
//                refreshChooseState();
//            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void refreshChooseState() {
        if (mGiftViews != null) {
            for (int i = 0; i < mGiftViews.size(); i++) {//清除选中
                RecyclerView view = (RecyclerView) mGiftViews.get(i);
                NewGiftPanelAdapter adapter = (NewGiftPanelAdapter) view.getAdapter();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }

    }

    GiftInfoBean updateGiftInfo;
    public void updateGiftNum(GiftInfoBean mGiftInfoBean) {
        updateGiftInfo = mGiftInfoBean;
        refreshChooseState();
    }


    public interface OnGiftListener {
        void doGiftChoose(GiftInfoBean giftInfoBean);
    }

    OnGiftListener onGiftListener;

    public void setOnGiftListener(OnGiftListener onGiftListener) {
        this.onGiftListener = onGiftListener;
    }

    List<GiftInfoBean> mGiftBean;
    public List<GiftInfoBean> getGiftBean() {
        return mGiftBean;
    }

    public void showEmptyView() {
        tvEmpty.setVisibility(View.VISIBLE);
    }

    public void hideEmptyView() {
        tvEmpty.setVisibility(View.GONE);
    }

}
