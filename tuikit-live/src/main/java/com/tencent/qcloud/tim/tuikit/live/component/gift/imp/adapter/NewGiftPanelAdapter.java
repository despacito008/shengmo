package com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfoBean;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.RecyclerViewController;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;

import java.util.List;

public class NewGiftPanelAdapter extends RecyclerView.Adapter<NewGiftPanelAdapter.ViewHolder> {
    private Context                mContext;
    private RecyclerView           mRecyclerView;
    private RecyclerViewController mRecyclerViewController;
    private int                    mPageIndex;
    private List<String>         mSelectGiftIdList;
    private List<GiftInfoBean>         mGiftInfoList;
    private OnItemClickListener    mOnItemClickListener;

    private GiftInfoBean updateGiftInfo;


    public NewGiftPanelAdapter(RecyclerView recyclerView, int pageIndex, List<GiftInfoBean> list,
                               Context context, List<String> selectGiftInfoList,GiftInfoBean giftInfoBean) {
        super();
        mRecyclerView = recyclerView;
        mGiftInfoList = list;
        mContext = context;
        mPageIndex = pageIndex;
        mSelectGiftIdList = selectGiftInfoList;
        updateGiftInfo = giftInfoBean;
        recyclerViewClickListener(list, mContext);
    }

    private void recyclerViewClickListener(final List<GiftInfoBean> list, Context mContext) {
        mRecyclerViewController = new RecyclerViewController(mContext, mRecyclerView);
        mRecyclerViewController.setOnItemClickListener(new RecyclerViewController.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                final GiftInfoBean giftModel = list.get(position);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, giftModel, position, mPageIndex);
                }
            }
        });
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.live_recycle_item_gift_panel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GiftInfoBean giftInfo = mGiftInfoList.get(position);
        GlideEngine.loadImage(holder.mImageGift, giftInfo.getGift_image());
        if("1".equals(giftInfo.getGift_type())){//免费礼物
            holder.mTextGiftName.setText(giftInfo.getGift_name() + "X" + giftInfo.getNum());
            String price =  !TextUtils.isEmpty(giftInfo.getGift_beans()) ? giftInfo.getGift_beans() : "";
            holder.mTextGiftPrice.setText(String.valueOf(Integer.valueOf(price)));
        }else {//计费礼物
            holder.mTextGiftName.setText(giftInfo.getGift_name());
            holder.mTextGiftPrice.setText(String.valueOf(giftInfo.getGift_beans()));
        }

        if (!TextUtils.isEmpty(giftInfo.getTip_url())) {
            holder.mIvTip.setVisibility(View.VISIBLE);
            GlideEngine.loadImage(holder.mIvTip, giftInfo.getTip_url());
        } else {
            holder.mIvTip.setVisibility(View.INVISIBLE);
        }

        if (isItemSelect(giftInfo.getGift_id()) ) {
            holder.mLayoutRootView.setBackgroundResource(R.drawable.live_gift_shape_normal);
        } else {
            holder.mLayoutRootView.setBackground(null);
        }

        if (updateGiftInfo != null && giftInfo.getGift_id().equals(updateGiftInfo.getGift_id()) && "1".equals(giftInfo.getGift_type())) {
            holder.mTextGiftName.setText(giftInfo.getGift_name() + "X" + updateGiftInfo.getNum());
        }
    }

    @Override
    public int getItemCount() {
        return mGiftInfoList.size();
    }

    public void clearSelection(int pageIndex) {
        if (mPageIndex != pageIndex) {
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayoutRootView;
        ImageView    mImageGift;
        TextView     mTextGiftName;
        TextView     mTextGiftPrice;
//        TextView     mTextSendBtn;
        ImageView    mIvTip;

        public ViewHolder(View view) {
            super(view);
            mLayoutRootView = (LinearLayout) view.findViewById(R.id.ll_gift_root);
            mImageGift      = (ImageView)    view.findViewById(R.id.iv_gift_icon);
            mTextGiftName   = (TextView)     view.findViewById(R.id.tv_gift_name);
            mTextGiftPrice  = (TextView)     view.findViewById(R.id.tv_gift_price);
//            mTextSendBtn    = (TextView)     view.findViewById(R.id.tv_send);
            mIvTip          = (ImageView)   view.findViewById(R.id.iv_gift_tip);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, GiftInfoBean giftInfo, int position, int pageIndex);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private boolean isItemSelect(String gid) {
        if (mSelectGiftIdList.size() > 0 && mSelectGiftIdList.get(0).equals(gid)) {
            return true;
        } else {
            return false;
        }
    }
}
