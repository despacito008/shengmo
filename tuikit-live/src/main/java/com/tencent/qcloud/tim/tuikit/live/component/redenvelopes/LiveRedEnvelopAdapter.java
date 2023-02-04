package com.tencent.qcloud.tim.tuikit.live.component.redenvelopes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRedEnvelopesBean;

import java.util.List;

public class LiveRedEnvelopAdapter extends RecyclerView.Adapter<LiveRedEnvelopAdapter.RedEnvelopHolder> {
    Context context;
    List<LiveRedEnvelopesBean.DataBean> envelopesList;

    public LiveRedEnvelopAdapter(Context context, List<LiveRedEnvelopesBean.DataBean> envelopesList) {
        this.context = context;
        this.envelopesList = envelopesList;
    }

    @NonNull
    @Override
    public RedEnvelopHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RedEnvelopHolder(LayoutInflater.from(context).inflate(R.layout.live_item_red_envelopes,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RedEnvelopHolder redEnvelopHolder, int i) {
        redEnvelopHolder.display(i);
    }

    @Override
    public int getItemCount() {
        return envelopesList.size();
    }

    class RedEnvelopHolder extends RecyclerView.ViewHolder {
        ImageView ivItemIcon;
        FrameLayout flContent;
        public RedEnvelopHolder(@NonNull View itemView) {
            super(itemView);
            ivItemIcon = itemView.findViewById(R.id.civ_live_red_envelopes_icon);
            flContent = itemView.findViewById(R.id.fl_live_red_envelopes);
        }

        public void display(final int index) {
            Glide.with(context).load(envelopesList.get(index).getHead_pic()).into(ivItemIcon);
            if ("1".equals(envelopesList.get(index).getState())) {
                flContent.setAlpha(1f);
            } else {
                flContent.setAlpha(0.5f);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRedEnvelopesListener != null) {
                        onRedEnvelopesListener.doRedEnvelopesClick(index,"0".equals(envelopesList.get(index).getState()));
                    }
                }
            });
        }
    }

    public interface OnRedEnvelopesListener {
        void doRedEnvelopesClick(int index,boolean isReceive);
    }
    OnRedEnvelopesListener onRedEnvelopesListener;
    public void setOnRedEnvelopesListener(OnRedEnvelopesListener onRedEnvelopesListener) {
        this.onRedEnvelopesListener = onRedEnvelopesListener;
    }
}
