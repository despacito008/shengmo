package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.VipGoodsBean;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VipGoodsRecyclerAdapter extends RecyclerView.Adapter<VipGoodsRecyclerAdapter.VipGoodsHolder> {
    Context context;
    private List<VipGoodsBean.DataBean> vipGoodsList;
    int currentIndex = 0;

    public VipGoodsRecyclerAdapter(Context context, List<VipGoodsBean.DataBean> vipGoodsList) {
        this.context = context;
        this.vipGoodsList = vipGoodsList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<VipGoodsBean.DataBean> getVipGoodsList() {
        return vipGoodsList;
    }

    public void setVipGoodsList(List<VipGoodsBean.DataBean> vipGoodsList) {
        this.vipGoodsList = vipGoodsList;
    }

    @Override
    public VipGoodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vip_center_package, parent, false);
        return new VipGoodsHolder(view);
    }

    @Override
    public void onBindViewHolder(VipGoodsHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return vipGoodsList.size();
    }

    class VipGoodsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_item_vip_center_bg)
        View viewItemVipCenterBg;
        @BindView(R.id.tv_item_vip_center_name)
        TextView tvItemVipCenterName;
        @BindView(R.id.tv_item_vip_center_rmb)
        TextView tvItemVipCenterRmb;
        @BindView(R.id.tv_item_vip_center_price)
        TextView tvItemVipCenterPrice;
        @BindView(R.id.tv_item_vip_center_original_price)
        TextView tvItemVipCenterOriginalPrice;
        @BindView(R.id.tv_item_vip_center_info)
        TextView tvItemVipCenterInfo;
        @BindView(R.id.tv_item_vip_center_discount)
        TextView tvItemVipCenterDiscount;
        public VipGoodsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void setData(final int position) {
            if(currentIndex == position) {
                viewItemVipCenterBg.setBackgroundResource(R.drawable.bg_vip_center_item_choose);
            } else {
                viewItemVipCenterBg.setBackgroundResource(R.drawable.bg_vip_center_item_normal);
            }
            tvItemVipCenterName.setText(vipGoodsList.get(position).getVip_name());
            tvItemVipCenterInfo.setText(vipGoodsList.get(position).getVip_info());
            tvItemVipCenterPrice.setText(vipGoodsList.get(position).getVip_price());
            if(!vipGoodsList.get(position).getVip_price().equals(vipGoodsList.get(0).getVip_original_price())) {
                tvItemVipCenterOriginalPrice.setText("原价：" + vipGoodsList.get(position).getVip_original_price() + "元");
                tvItemVipCenterOriginalPrice.setVisibility(View.VISIBLE);
            } else {
                tvItemVipCenterOriginalPrice.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(vipGoodsList.get(position).getVip_discount())) {
                tvItemVipCenterDiscount.setText("优惠" + vipGoodsList.get(position).getVip_discount() + "%");
                tvItemVipCenterDiscount.setVisibility(View.VISIBLE);
            } else {
                tvItemVipCenterDiscount.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentIndex == position) {
                        return;
                    }
                    int temp = currentIndex;
                    currentIndex = position;
                    notifyItemChanged(currentIndex);
                    notifyItemChanged(temp);
                    if(onSimpleItemListener != null) {
                        onSimpleItemListener.onItemListener(position);
                    }
                }
            });
        }
    }

    OnSimpleItemListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }
}
