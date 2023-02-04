package com.tencent.qcloud.tim.tuikit.live.component.link.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.qcloud.tim.tuikit.live.R;

import java.util.List;

public class SpeakManagerAdapter extends RecyclerView.Adapter<SpeakManagerAdapter.SpeakManagerHolder> {
    Context context;
    List<V2TIMUserFullInfo> userList;
    public SpeakManagerAdapter(Context context,List<V2TIMUserFullInfo> userFullInfoList) {
        this.context = context;
        this.userList = userFullInfoList;
    }

    @NonNull
    @Override
    public SpeakManagerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SpeakManagerHolder(LayoutInflater.from(context).inflate(R.layout.live_item_speak_manager,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SpeakManagerHolder speakManagerHolder, int i) {
        speakManagerHolder.display(i);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class SpeakManagerHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName,tvClose;
        public SpeakManagerHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_item_speak_manager_icon);
            tvName = itemView.findViewById(R.id.tv_item_speak_manager_name);
            tvClose = itemView.findViewById(R.id.tv_item_speak_manager_close);
        }

        void display(final int index) {
            Glide.with(context).load(userList.get(index).getFaceUrl()).into(ivIcon);
            tvName.setText(userList.get(index).getNickName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemSpeakManagerListener != null) {
                        onItemSpeakManagerListener.doItemClick(userList.get(index).getUserID());
                    }
                }
            });
            tvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemSpeakManagerListener != null) {
                        onItemSpeakManagerListener.doItemClose(userList.get(index).getUserID());
                    }
                }
            });
        }
    }

    public interface OnItemSpeakManagerListener {
        void doItemClick(String uid);
        void doItemClose(String uid);
    }

    OnItemSpeakManagerListener onItemSpeakManagerListener;

    public void setOnItemSpeakManagerListener(OnItemSpeakManagerListener onItemSpeakManagerListener) {
        this.onItemSpeakManagerListener = onItemSpeakManagerListener;
    }
}
