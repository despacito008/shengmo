package com.aiwujie.shengmo.qnlive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.qiniu.droid.rtc.QNSurfaceView;
import com.qiniu.droid.rtc.QNTrackInfo;

import java.util.HashMap;
import java.util.List;

public class QNRemoteViewAdapter extends RecyclerView.Adapter<QNRemoteViewAdapter.RemoteHolder>{
    Context context;
    List<List<QNTrackInfo>> remoteStreamList;

    public QNRemoteViewAdapter(Context context, List<List<QNTrackInfo>>remoteStreamList) {
        this.context = context;
        this.remoteStreamList = remoteStreamList;
    }
    boolean isAnchor = true;
    public QNRemoteViewAdapter(Context context, List<List<QNTrackInfo>>remoteStreamList,boolean isAnchor) {
        this.context = context;
        this.remoteStreamList = remoteStreamList;
        this.isAnchor = isAnchor;
    }

    @Override
    public RemoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RemoteHolder(LayoutInflater.from(context).inflate(R.layout.app_item_qn_remote_view,parent,false));
    }

    @Override
    public void onBindViewHolder(RemoteHolder holder, int position) {
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return remoteStreamList.size();
    }

    class RemoteHolder extends RecyclerView.ViewHolder {
        QNSurfaceView remoteSurfaceView;
        ImageView ivItemClose;
        public RemoteHolder(View itemView) {
            super(itemView);
            remoteSurfaceView = itemView.findViewById(R.id.qn_item_surface_view);
            ivItemClose = itemView.findViewById(R.id.iv_item_remote_view_close);
            if (isAnchor) {
                ivItemClose.setVisibility(View.VISIBLE);
            } else {
                ivItemClose.setVisibility(View.INVISIBLE);
            }
        }

        void display(int index) {
            ivItemClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRemoteViewListener != null) {
                        onRemoteViewListener.doRemoteClose(index);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRemoteViewListener != null) {
                        onRemoteViewListener.doRemoteClick(index);
                    }
                }
            });
            if (onRemoteViewListener != null) {
                for (QNTrackInfo qnTrackInfo: remoteStreamList.get(index)) {
                     if (qnTrackInfo.isVideo()) {
                         onRemoteViewListener.doRemoteRender(remoteSurfaceView,qnTrackInfo);
                     }
                }
            }
        }
    }

    public interface OnRemoteViewListener {
        void doRemoteClose(int index);
        void doRemoteClick(int index);
        void doRemoteRender(QNSurfaceView surfaceView,QNTrackInfo qnTrackInfo);
    }

    OnRemoteViewListener onRemoteViewListener;

    public void setOnRemoteViewListener(OnRemoteViewListener onRemoteViewListener) {
        this.onRemoteViewListener = onRemoteViewListener;
    }
}
