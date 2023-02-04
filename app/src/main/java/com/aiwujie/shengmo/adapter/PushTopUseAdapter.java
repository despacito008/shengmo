package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.DynamicDetailBean;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.TimeUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PushTopUseAdapter extends RecyclerView.Adapter<PushTopUseAdapter.PushTopUsHolder> {
    Context context;
    private List<DynamicDetailBean.DataBean> dataList;

    public PushTopUseAdapter(Context context, List<DynamicDetailBean.DataBean> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public PushTopUsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item_push_top_use, parent, false);
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
            tvItemPushTopUsName.setText("使用了" + dataList.get(index).getSum() + "张推顶卡");
            Date date = new Date(Long.valueOf(dataList.get(index).getAddtime())*1000);
            //String timeFormatText = TimeUtil.getTimeFormatText(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeFormatText = simpleDateFormat.format(date);
            tvItemPushTopUsInfo.setText("推顶时间 " + timeFormatText);
            tvItemPushTopUsTime.setText("推顶间隔 " + dataList.get(index).getInterval());
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
