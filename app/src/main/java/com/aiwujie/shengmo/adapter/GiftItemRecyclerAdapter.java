package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.MyPresentData;
import com.bumptech.glide.Glide;

import java.util.List;

public class GiftItemRecyclerAdapter extends RecyclerView.Adapter<GiftItemRecyclerAdapter.GiftItemHolder> {
    Context context;
    private List<MyPresentData.DataBean.GiftArrBean> mDatas;
    private int type = 0;

    public GiftItemRecyclerAdapter(Context context, List<MyPresentData.DataBean.GiftArrBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    public GiftItemRecyclerAdapter(Context context, List<MyPresentData.DataBean.GiftArrBean> mDatas,int type) {
        this.context = context;
        this.mDatas = mDatas;
        this.type = 1;
    }

    @Override
    public GiftItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_gift_arr,parent,false);
        return new GiftItemHolder(view);
    }

    @Override
    public void onBindViewHolder(GiftItemHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class GiftItemHolder extends RecyclerView.ViewHolder {
        TextView tvItemNum,tvItemName,tvItemValue;
        ImageView ivItemIcon;
        View viewItemBg;

        public GiftItemHolder(View itemView) {
            super(itemView);
            tvItemNum = itemView.findViewById(R.id.tv_item_gift_arr_num);
            tvItemName = itemView.findViewById(R.id.tv_item_gift_arr_name);
            tvItemValue = itemView.findViewById(R.id.tv_item_gift_arr_value);
            ivItemIcon = itemView.findViewById(R.id.iv_item_gift_arr_icon);
            viewItemBg = itemView.findViewById(R.id.view_item_gift_arr);
        }

        public void setData(int position) {
            MyPresentData.DataBean.GiftArrBean data = mDatas.get(position);
            tvItemName.setText(data.getGift_name());
            Glide.with(context).load(data.getGift_img()).error(R.mipmap.weizhiliwu).into(ivItemIcon);
            String numStr = "x" + data.getNum();
            AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(12, true);//设置前面的字体大小
            SpannableStringBuilder builder = new SpannableStringBuilder(numStr);
            builder.setSpan(sizeSpan,1,data.getNum().length()+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvItemNum.setText(builder);
            tvItemValue.setText(data.getGift_sum() + " 银魔豆");
            if(type != 0) {
                viewItemBg.setBackgroundResource(R.drawable.ic_item_person_present_bg);
            }
        }
    }
}
