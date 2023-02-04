package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter.GiftPanelAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.tencent.liteav.demo.beauty.utils.ResourceUtils.getResources;

public class GiftController {
    private int mPosition;
    private List<GiftInfo> mSelectGiftInfoList;
    private int            mSelectGiftPageIndex = -1;
    private GiftInfo       mSelectGiftInfo;
    private GiftPanelAdapter mGvAdapter;


    public GiftController() {
        mSelectGiftInfoList = new ArrayList<>();
    }

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
    public View viewPagerItem(final Context context, final int pageIndex, List<GiftInfo> giftInfoList, int columns, int rows) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.live_layout_gift_panel, null);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.chart_face_gv);
        GridLayoutManager girdLayoutManager = new GridLayoutManager(context, columns);
        recyclerView.setLayoutManager(girdLayoutManager);
        List<GiftInfo> subList = new ArrayList<>();
        int maxPageItems = columns * rows;
        int startIndex = pageIndex * maxPageItems;
        int endIndex = maxPageItems * (pageIndex + 1) > giftInfoList.size() ?
                giftInfoList.size() : maxPageItems * (pageIndex + 1);
        subList.addAll(giftInfoList.subList(startIndex, endIndex));
        GiftPanelAdapter mGvAdapter = new GiftPanelAdapter(recyclerView, pageIndex, subList, context, mSelectGiftInfoList);
        recyclerView.setAdapter(mGvAdapter);
        mGvAdapter.setOnItemClickListener(new GiftPanelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, GiftInfo giftInfo, int position, int index) {
                mPosition = position;
                mSelectGiftInfo = giftInfo;
                mSelectGiftPageIndex = index;
                if(onGiftControllerDelegate != null){
                    onGiftControllerDelegate.onClickDataChanged(giftInfo,position);
                    onGiftControllerDelegate.onPopDismiss();
                }
            }
        });
        return recyclerView;
    }

    public int getSelectPageIndex() {
        return mSelectGiftPageIndex;
    }

    public GiftInfo getSelectGiftInfo() {
        return mSelectGiftInfo;
    }

    public void clearSelect() {
        mSelectGiftInfo = null;
        if (mGvAdapter != null) {
            mGvAdapter.clearSelectStateWithRefresh();
        }
    }

    public interface GiftClickListener {
        void onClick(int position, GiftInfo giftInfo);
    }

    private GiftClickListener giftClickListener;

    public void setGiftClickListener(GiftClickListener listener) {
        giftClickListener = listener;
    }

    /**
     * 根据礼物数量以及GridView设置的行数和列数计算Pager数量
     * @return
     */
    public int getPagerCount(int listSize, int columns, int rows) {
        return listSize % (columns * rows) == 0 ? listSize / (columns * rows) : listSize / (columns * rows) + 1;
    }

    public void notifyItem(int changedNo,GiftInfo changedInfo){
        mSelectGiftInfo.num = String.valueOf(changedNo);

        if (mGvAdapter != null) {
            mGvAdapter.notifyItemChanged(mPosition);
        }
    }

    public interface OnGiftControllerDelegate{
        void onClickDataChanged(GiftInfo giftInfo,int position);
        void onPopDismiss();
    }

    public void setOnGiftControllerDelegate(OnGiftControllerDelegate onGiftControllerDelegate) {
        this.onGiftControllerDelegate = onGiftControllerDelegate;
    }

    private OnGiftControllerDelegate onGiftControllerDelegate;
}
