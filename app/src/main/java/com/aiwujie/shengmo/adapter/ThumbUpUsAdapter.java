package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.bean.DynamicDetailBean;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.TimeUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThumbUpUsAdapter extends RecyclerView.Adapter<ThumbUpUsAdapter.PushTopUsHolder> {
    Context context;
    private List<DynamicDetailBean.DataBean> dataList;

    public ThumbUpUsAdapter(Context context, List<DynamicDetailBean.DataBean> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public PushTopUsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item_push_top_us, parent, false);
        return new PushTopUsHolder(view);
    }

    @Override
    public void onBindViewHolder(PushTopUsHolder holder, int position) {
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class PushTopUsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_layout_avatar_bg)
        View viewLayoutAvatarBg;
        @BindView(R.id.lottie_user_avatar_state)
        LottieAnimationView lottieUserAvatarState;
        @BindView(R.id.iv_user_avatar_state)
        ImageView ivUserAvatarState;
        @BindView(R.id.iv_user_avatar_icon)
        ImageView ivUserAvatarIcon;
        @BindView(R.id.iv_user_avatar_level)
        ImageView ivUserAvatarLevel;
        @BindView(R.id.iv_user_avatar_online)
        ImageView ivUserAvatarOnline;
        @BindView(R.id.layout_user_avatar)
        ConstraintLayout layoutUserAvatar;
        @BindView(R.id.tv_item_push_top_us_name)
        TextView tvItemPushTopUsName;
        @BindView(R.id.tv_item_push_top_us_time)
        TextView tvItemPushTopUsTime;
        @BindView(R.id.tv_item_push_top_us_info)
        TextView tvItemPushTopUsInfo;
        @BindView(R.id.tv_item_push_top_us_dynamic)
        TextView tvItemPushTopUsDynamic;
        @BindView(R.id.iv_item_push_top_us_dynamic)
        ImageView ivItemPushTopUsDynamic;
        public PushTopUsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void display(int index) {
            tvItemPushTopUsName.setText(dataList.get(index).getNickname());
            //Date date = new Date(Long.valueOf(dataList.get(index).getAddtime())*1000);
            //String timeFormatText = TimeUtil.getTimeFormatText(date);
            tvItemPushTopUsTime.setText(dataList.get(index).getAddtime());
            tvItemPushTopUsInfo.setText("赞了你的动态");
            ImageLoader.loadCircleImage(context,dataList.get(index).getHead_pic(),ivUserAvatarIcon,R.mipmap.morentouxiang);
            DynamicDetailBean.DataBean data = dataList.get(index);
            //UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), ivUserAvatarLevel);
            if (!TextUtil.isEmpty(dataList.get(index).getPic())) {
                tvItemPushTopUsDynamic.setText("");
                ivItemPushTopUsDynamic.setVisibility(View.VISIBLE);
                ImageLoader.loadImage(context,dataList.get(index).getPic(),ivItemPushTopUsDynamic);
            } else {
                tvItemPushTopUsDynamic.setText(dataList.get(index).getContent());
                ivItemPushTopUsDynamic.setVisibility(View.GONE);
            }
            ivUserAvatarIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("uid",dataList.get(index).getUid());
                    context.startActivity(intent);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSimpleItemListener != null) {
                        onSimpleItemListener.onItemListener(index);
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
