package com.aiwujie.shengmo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.EditPunishmentActivity;
import com.aiwujie.shengmo.bean.PunishmentBean;
import com.aiwujie.shengmo.utils.OnSimpleItemViewListener;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.ImagePreviewActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//违规记录adapter
public class GetOutOfLineAdapter extends RecyclerView.Adapter<GetOutOfLineAdapter.ViewHolder> {
    List<PunishmentBean.DataBean> list;
    Activity context;
    private  SimpleDateFormat dateformat;

    public GetOutOfLineAdapter(List<PunishmentBean.DataBean> list, Activity context) {
        this.list = list;
        this.context = context;
        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.get_out_of_line_item, parent, false);
        GetOutOfLineAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        switch (list.get(position).getType()) {
            case "1":
                holder.tvMesTitle.setText("封禁账号/" + ("0".equals(list.get(position).getBlockingalong()) ? "永久" : list.get(position).getBlockingalong() + "天"));
                break;
            case "2":
                holder.tvMesTitle.setText("封禁动态/" + ("0".equals(list.get(position).getBlockingalong()) ? "永久" : list.get(position).getBlockingalong() + "天"));
                break;
            case "3":
                holder.tvMesTitle.setText("禁言/" + ("0".equals(list.get(position).getBlockingalong()) ? "永久" : list.get(position).getBlockingalong() + "天"));
                break;
            case "4":
                holder.tvMesTitle.setText("封禁资料/" + ("0".equals(list.get(position).getBlockingalong()) ? "永久" : list.get(position).getBlockingalong() + "天"));
                break;
            case "5":
                holder.tvMesTitle.setText("封禁设备/" + ("0".equals(list.get(position).getBlockingalong()) ? "永久" : list.get(position).getBlockingalong() + "天"));
                break;
        }
        holder.tvTime.setText(dateformat.format(Long.parseLong(list.get(position).getAddtime())*1000));
        holder.tvMsgContent.setText(list.get(position).getBlockreason());
        final List<String> images = list.get(position).getImage();

        if (images == null || images.size() == 0) {
            holder.rlvImg.setVisibility(View.GONE);
        } else {
            holder.rlvImg.setVisibility(View.VISIBLE);
            PunishmentImgAdapter punishmentImgAdapter = new PunishmentImgAdapter(images, context);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 5);
            holder.rlvImg.setLayoutManager(gridLayoutManager);
            holder.rlvImg.setAdapter(punishmentImgAdapter);
            punishmentImgAdapter.setOnSimpleItemListener(new OnSimpleItemViewListener() {
                @Override
                public void onItemListener(View view) {
                    int index = holder.rlvImg.getChildAdapterPosition(view);
                    List<ImageInfo> imageInfo = new ArrayList<>();
                    for (int i = 0; i < images.size(); i++) {
                        ImageInfo info = new ImageInfo();
                        info.setThumbnailUrl(images.get(i));
                        info.setBigImageUrl(images.get(i));
                        imageInfo.add(info);
                    }
                    Intent intent = new Intent(context, ImagePreviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
                    bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PunishmentBean.DataBean punishmentInfo = list.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, EditPunishmentActivity.class);
                intent.putExtra("title", "1".equals(list.get(holder.getAdapterPosition()).getType()) ? "编辑封号信息" : "2".equals(list.get(holder.getAdapterPosition()).getType()) ? "编辑封禁动态" : "3".equals(list.get(holder.getAdapterPosition()).getType()) ? "编辑禁言信息" : "编辑封禁资料");
                intent.putExtra("info", punishmentInfo);
                context.startActivityForResult(intent, 6);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMesTitle, tvEdit, tvTime, tvMsgContent;
        private RecyclerView rlvImg;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMesTitle = itemView.findViewById(R.id.tv_mes_title);
            tvEdit = itemView.findViewById(R.id.tv_edit);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvMsgContent = itemView.findViewById(R.id.tv_msg_content);
            rlvImg = itemView.findViewById(R.id.rlv_img);


        }
    }


}
