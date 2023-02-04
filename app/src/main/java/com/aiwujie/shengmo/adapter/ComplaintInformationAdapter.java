package com.aiwujie.shengmo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.ActivityCommonWealth;
import com.aiwujie.shengmo.activity.EditPunishmentActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.bean.ComplaintInformatoinBean;
import com.aiwujie.shengmo.bean.PunishmentBean;
import com.aiwujie.shengmo.utils.DateUtils;
import com.aiwujie.shengmo.utils.OnSimpleItemViewListener;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.ImagePreviewActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//投诉被投诉记录adapter
public class ComplaintInformationAdapter extends RecyclerView.Adapter<ComplaintInformationAdapter.ViewHolder> {
    List<ComplaintInformatoinBean.ComplaintInformatoin> list;
    Activity context;
    String type;
    private final SimpleDateFormat dateformat;

    public ComplaintInformationAdapter(List<ComplaintInformatoinBean.ComplaintInformatoin> list,String type, Activity context) {
        this.list = list;
        this.type=type;
        this.context = context;
        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.complaint_information_item, parent, false);
        ComplaintInformationAdapter.ViewHolder  viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        switch (type){
            case "1":
                holder.tvComplaintType.setText("投诉/");
                holder.tvName.setText(list.get(position).getOthername());
                switch (list.get(position).getOthersex()){
                    case "1":
                        holder.tvName.setTextColor(Color.parseColor("#0000ff"));
                        break;
                    case "2":
                        holder.tvName.setTextColor(Color.parseColor("#ff0000"));
                        break;
                    case "3":
                        holder.tvName.setTextColor(Color.parseColor("#b73acb"));
                        break;
                    default:
                        break;
                }

                break;
            case "2":
                holder.tvComplaintType.setText("被投诉/");
                holder.tvName.setText(list.get(position).getNickname());
                if(list.get(position).getSex() == null) {
                    return;
                }
                switch (list.get(position).getSex()){
                    case "1":
                        holder.tvName.setTextColor(Color.parseColor("#0000ff"));
                        break;
                    case "2":
                        holder.tvName.setTextColor(Color.parseColor("#ff0000"));
                        break;
                    case "3":
                        holder.tvName.setTextColor(Color.parseColor("#b73acb"));
                        break;
                    default:
                        break;
                }

                break;
            default:
                break;

        }

        holder.tvResult.setText(list.get(position).getManageway_info());
//        switch (list.get(position).getManageway()) {
//            case "0" :
//                holder.tvResult.setText("处理结果： 未处理");
//                break;
//            case "1" :
//                holder.tvResult.setText("处理结果： 普通处理");
//                break;
//            case "2" :
//                holder.tvResult.setText("处理结果： 警告被举报人");
//                break;
//            case "3" :
//                holder.tvResult.setText("处理结果： 无效举报");
//                break;
//        }

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(type)){
                    Intent   intent = new Intent(context, PesonInfoActivity.class);
                    intent.putExtra("uid", list.get(holder.getAdapterPosition()).getOtheruid());
                    context.startActivity(intent);
                }else {
                    Intent   intent = new Intent(context, PesonInfoActivity.class);
                    intent.putExtra("uid", list.get(holder.getAdapterPosition()).getUid());
                    context.startActivity(intent);
                }


            }
        });


        holder.tvTime.setText(dateformat.format(Long.parseLong(list.get(position).getAddtime())*1000));
        holder.tvMsgContent.setText(list.get(position).getReport());
        final List<String> images = list.get(position).getImage();

        if(images == null || images.size() == 0) {
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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvComplaintType,tvName,tvTime,tvMsgContent,tvResult;
        private RecyclerView rlvImg;

        public ViewHolder(View itemView) {
            super(itemView);
            tvComplaintType=  itemView.findViewById(R.id.tv_complaint_type);
            tvName= itemView.findViewById(R.id.tv_name);
            tvTime=  itemView.findViewById(R.id.tv_time);
            tvMsgContent= itemView.findViewById(R.id.tv_msg_content);
            rlvImg= itemView.findViewById(R.id.rlv_img);
            tvResult = itemView.findViewById(R.id.tv_item_result);
        }
    }


}
