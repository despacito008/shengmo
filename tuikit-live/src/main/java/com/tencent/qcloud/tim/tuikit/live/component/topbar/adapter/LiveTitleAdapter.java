package com.tencent.qcloud.tim.tuikit.live.component.topbar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;

import java.util.List;

public class LiveTitleAdapter extends RecyclerView.Adapter<LiveTitleAdapter.LiveTitleHolder> {
    Context context;
    List<String> mTitleList;

    public LiveTitleAdapter(Context context, List<String> mTitleList) {
        this.context = context;
        this.mTitleList = mTitleList;
    }

    @NonNull
    @Override
    public LiveTitleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LiveTitleHolder(LayoutInflater.from(context).inflate(R.layout.app_item_live_title,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LiveTitleHolder liveTitleHolder, int i) {
        liveTitleHolder.display(i);
    }

    @Override
    public int getItemCount() {
        return mTitleList.size();
    }

    class LiveTitleHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public LiveTitleHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_item_live_title);
        }

        public void display(int index) {
            textView.setText(mTitleList.get(index));
        }

    }
}
