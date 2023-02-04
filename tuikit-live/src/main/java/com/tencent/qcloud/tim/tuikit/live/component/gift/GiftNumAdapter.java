package com.tencent.qcloud.tim.tuikit.live.component.gift;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.NormalGiftInfo;

import java.util.List;

public class GiftNumAdapter extends RecyclerView.Adapter<GiftNumAdapter.GiftNumHolder> {
    Context mContext;
    List<NormalGiftInfo.DataBean.AuspiciousBean> auspiciousBeanList;

    public GiftNumAdapter(Context mContext, List<NormalGiftInfo.DataBean.AuspiciousBean> auspiciousBeanList) {
        this.mContext = mContext;
        this.auspiciousBeanList = auspiciousBeanList;
    }

    @NonNull
    @Override
    public GiftNumHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GiftNumHolder(LayoutInflater.from(mContext).inflate(R.layout.live_item_gift_num, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GiftNumHolder giftNumHolder, int i) {
        giftNumHolder.display(i);
    }

    @Override
    public int getItemCount() {
        return auspiciousBeanList.size();
    }

    class GiftNumHolder extends RecyclerView.ViewHolder {
        TextView tvContent;

        public GiftNumHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_item_gift_num);
        }

        public void display(final int index) {
            tvContent.setText(String.valueOf(auspiciousBeanList.get(index).getNum()) + " " + auspiciousBeanList.get(index).getName());
            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemNumListener != null) {
                        onItemNumListener.doItemClick(auspiciousBeanList.get(index).getNum());
                    }
                }
            });
        }
    }

    public interface OnItemNumListener {
        void doItemClick(int num);
    }

    OnItemNumListener onItemNumListener;

    public void setOnItemNumListener(OnItemNumListener onItemNumListener) {
        this.onItemNumListener = onItemNumListener;
    }
}
