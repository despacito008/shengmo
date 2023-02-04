package com.aiwujie.shengmo.qnlive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.bumptech.glide.Glide;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;

import java.math.MathContext;
import java.util.List;

public class RequestLinkAdapter extends RecyclerView.Adapter<RequestLinkAdapter.RequestLinkHolder> {
    Context context;
    List<V2TIMUserFullInfo> userFullInfoList;

    public RequestLinkAdapter(Context context, List<V2TIMUserFullInfo> userFullInfoList) {
        this.context = context;
        this.userFullInfoList = userFullInfoList;
    }

    @Override
    public RequestLinkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View requestLinkView = LayoutInflater.from(context).inflate(R.layout.app_item_request_link,parent,false);
        return new RequestLinkHolder(requestLinkView);
    }

    @Override
    public void onBindViewHolder(RequestLinkHolder holder, int position) {
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return userFullInfoList.size();
    }

    class RequestLinkHolder extends RecyclerView.ViewHolder {
        ImageView ivItemIcon;
        TextView tvItemName,tvItemAgree,tvItemReject;
        public RequestLinkHolder(View itemView) {
            super(itemView);
            ivItemIcon = itemView.findViewById(R.id.iv_item_request_link_icon);
            tvItemName = itemView.findViewById(R.id.tv_item_request_link_name);
            tvItemAgree = itemView.findViewById(R.id.tv_item_request_link_agree);
            tvItemReject = itemView.findViewById(R.id.tv_item_request_link_reject);
        }
        public void display(int index) {
            Glide.with(context).load(userFullInfoList.get(index).getFaceUrl()).into(ivItemIcon);
            tvItemName.setText(userFullInfoList.get(index).getNickName());
            tvItemAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRequestLinkListener != null) {
                        onRequestLinkListener.doLinkAgree(userFullInfoList.get(index).getUserID());
                    }
                }
            });
            tvItemReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRequestLinkListener != null) {
                        onRequestLinkListener.doLinkReject(userFullInfoList.get(index).getUserID());
                    }
                }
            });
        }
    }

    public interface onRequestLinkListener {
        void doLinkAgree(String uid);
        void doLinkReject(String uid);
    }

    public onRequestLinkListener onRequestLinkListener;

    public void setOnRequestLinkListener(RequestLinkAdapter.onRequestLinkListener onRequestLinkListener) {
        this.onRequestLinkListener = onRequestLinkListener;
    }
}
