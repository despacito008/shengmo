package com.tencent.qcloud.tim.tuikit.live.component.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.qcloud.tim.tuikit.live.R;

import java.util.List;

public class LiveManagerAdapter extends RecyclerView.Adapter<LiveManagerAdapter.LiveManagerHolder>   {
    Context                 mContext;
    List<V2TIMUserFullInfo> v2TIMUserFullInfoList;

    public LiveManagerAdapter(Context mContext, List<V2TIMUserFullInfo> v2TIMUserFullInfoList) {
        this.mContext = mContext;
        this.v2TIMUserFullInfoList = v2TIMUserFullInfoList;
    }

    @NonNull
    @Override
    public LiveManagerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LiveManagerHolder(LayoutInflater.from(mContext).inflate(R.layout.live_item_live_manager,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LiveManagerHolder liveManagerHolder, int i) {
            liveManagerHolder.display(i);
    }

    @Override
    public int getItemCount() {
        return v2TIMUserFullInfoList.size();
    }

    class LiveManagerHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public LiveManagerHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_item_live_manager);
        }
        void display(final int index) {
            Glide.with(mContext).load(v2TIMUserFullInfoList.get(index).getFaceUrl()).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onLiveManagerListener != null) {
                        onLiveManagerListener.onLiveManagerClick(v2TIMUserFullInfoList.get(index).getUserID());
                    }
                }
            });
        }
    }

    interface OnLiveManagerListener {
        void onLiveManagerClick(String uid);
    }

    OnLiveManagerListener onLiveManagerListener;

    public void setOnLiveManagerListener(OnLiveManagerListener onLiveManagerListener) {
        this.onLiveManagerListener = onLiveManagerListener;
    }
}
