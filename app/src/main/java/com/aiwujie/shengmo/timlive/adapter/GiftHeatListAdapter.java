package com.aiwujie.shengmo.timlive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.timlive.bean.GiftRankingListInfo;
import com.aiwujie.shengmo.timlive.view.GiftRankingListPop;
import com.aiwujie.shengmo.utils.ClickUtils;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 热度适配器
 */
public class GiftHeatListAdapter extends RecyclerView.Adapter<GiftHeatListAdapter.ViewHolder>{
    private final List<GiftRankingListInfo.DataBean.RankingListBean> mList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public GiftHeatListAdapter(Context context, List<GiftRankingListInfo.DataBean.RankingListBean> mList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = mList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_live_frag_gift_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mContext,position, mList.get(position), onItemClickListener);
    }

    private void initItemIcon(TextView ivItemLocal,int position,int ranking) {
        switch (ranking){
            case 1:
                ivItemLocal.setBackgroundResource(R.mipmap.gift_one);
                ivItemLocal.setText("");
                break;
            case 2:
                ivItemLocal.setBackgroundResource(R.mipmap.gift_two);
                ivItemLocal.setText("");
                break;
            case 3:
                ivItemLocal.setBackgroundResource(R.mipmap.gift_three);
                ivItemLocal.setText("");
                break;
            default:
                ivItemLocal.setText("\t" + ranking + "");
                ivItemLocal.setBackgroundResource(R.color.transparent);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_local)
        TextView ivItemLocal;
        @BindView(R.id.iv_item_live_user_icon)
        ImageView iv_icon;
        @BindView(R.id.iv_item_live_user_identity)
        ImageView itemIdentityIcon;
        @BindView(R.id.tv_item_live_user_name)
        TextView tv_name;
        @BindView(R.id.ll_layout_user_normal_info_sex_age)
        LinearLayout llLayoutUserNormalInfoSexAge;
        @BindView(R.id.iv_layout_user_normal_info_sex)
        ImageView ivLayoutUserNormalInfoSex;
        @BindView(R.id.tv_layout_user_normal_info_age)
        TextView tvUserNormalInfoSexAge;
        @BindView(R.id.tv_layout_user_normal_info_role)
        TextView tvLayoutUserNormalInfoRole;
        @BindView(R.id.tv_layout_user_normal_info_wealth)
        TextView tvLayoutUserNormalInfoWealth;
        @BindView(R.id.iv_layout_user_normal_info_charm)
        ImageView ivLayoutUserNormalInfoCharm;
        @BindView(R.id.tv_layout_user_normal_info_charm)
        TextView tvLayoutUserNormalInfoCharm;
        @BindView(R.id.ll_layout_user_normal_info_charm)
        LinearLayout llLayoutUserNormalInfoCharm;
        @BindView(R.id.ll_layout_user_normal_info_wealth)
        LinearLayout llLayoutUserNormalInfoWealth;
        @BindView(R.id.tv_current_beans)
        TextView tvItemLiveBeansCoin;
        @BindView(R.id.tv_item_live_user_offline)
        TextView tvLiveUserOffline;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, int pos, final GiftRankingListInfo.DataBean.RankingListBean data, final OnItemClickListener listener) {
            if(data == null) return;
            ivItemLocal.setVisibility(View.VISIBLE);
            initItemIcon(ivItemLocal,pos,data.getRanking());
            //String coins = context.getString(R.string.live_head_backward_coins, !TextUtil.isEmpty(data.getCoin() + "") ? data.getCoin() + "" : "");
            tvItemLiveBeansCoin.setText(data.getRankinginfo());
            tv_name.setText(!TextUtils.isEmpty(data.getNickname()) ? data.getNickname() : "");
            tvUserNormalInfoSexAge.setText(!TextUtil.isEmpty(data.getAge()) ? data.getAge() : "");

            if ("1".equals(data.getCharm_val_switch())) {
                llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoCharm.setText("密");
            } else {
                if ("0".equals(data.getCharm_val_new())) {
                    llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
                } else {
                    llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
                    tvLayoutUserNormalInfoCharm.setText(data.getCharm_val_new());
                }
            }

            if ("1".equals(data.getWealth_val_switch())) {
                llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoWealth.setText("密");
            } else {
                if ("0".equals(data.getWealth_val_new())) {
                    llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
                } else {
                    llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
                    tvLayoutUserNormalInfoWealth.setText(data.getWealth_val_new());
                }
            }


            if (data.getSex().equals("1")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
            } else if (data.getSex().equals("2")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
            } else if (data.getSex().equals("3")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
            }
            if (data.getRole().equals("S")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                tvLayoutUserNormalInfoRole.setText("斯");
            } else if (data.getRole().equals("M")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                tvLayoutUserNormalInfoRole.setText("慕");
            } else if (data.getRole().equals("SM")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                tvLayoutUserNormalInfoRole.setText("双");
            } else if (data.getRole().equals("~")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_other);
                tvLayoutUserNormalInfoRole.setText("~");
            } else {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_other);
                tvLayoutUserNormalInfoRole.setText("-");
            }

            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(),  data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), itemIdentityIcon);


            if (!TextUtils.isEmpty(data.getHead_pic())) {
                //GlideEngine.loadImage(iv_icon, data.getHead_pic(), 0, 100);
                GlideEngine.loadCircleImage(iv_icon, data.getHead_pic());
            } else {
                GlideEngine.loadImage(iv_icon, data.getHead_pic(), R.drawable.live_room_default_cover, 100);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ClickUtils.isFastClick(v.getId())) {
                        listener.onItemClick(getLayoutPosition(), data);
                    }
                }
            });

            if ("1".equals(data.getIs_live())) {
                tvLiveUserOffline.setVisibility(View.INVISIBLE);
            } else {
                tvLiveUserOffline.setVisibility(View.VISIBLE);
            }

        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position, GiftRankingListInfo.DataBean.RankingListBean data);
    }
}