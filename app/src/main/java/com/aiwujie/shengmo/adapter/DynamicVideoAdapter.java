package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class DynamicVideoAdapter extends RecyclerView.Adapter<DynamicVideoAdapter.DynamicHolder> {
    private Context context;
    private List<DynamicListData.DataBean> dynamicList;

    private String type;
    private RequestOptions options;
    public OnItemClickListener onItemClickListener;


    public DynamicVideoAdapter(Context context, List<DynamicListData.DataBean> dynamicList, String type) {
        this.context = context;
        this.dynamicList = dynamicList;
        this.type = type;

        options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        options.transform((new MultiTransformation(new CenterCrop(),new RoundedCorners(20))));
    }

    @Override
    public DynamicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dynamic_video, parent, false);
        return new DynamicHolder(view);
    }

    @Override
    public void onBindViewHolder(final DynamicHolder holder, int position) {

        if (1 == dynamicList.get(position).getFollow_state() || 3 == dynamicList.get(position).getFollow_state() || MyApp.uid.equals(dynamicList.get(position).getUid())) {
            holder.ivAttention.setVisibility(View.GONE);
        } else {
            holder.ivAttention.setVisibility(View.VISIBLE);
        }

        if ("1".equals(dynamicList.get(position).getLaudstate())) {
            holder.ivRedHeart.setImageResource(R.mipmap.icon_red_heart_checked);

        } else {

            holder.ivRedHeart.setImageResource(R.mipmap.icon_red_heart_unchecked);

        }
        Glide.with(context).load(dynamicList.get(position).getCoverUrl()).apply(options).into(holder.ivVideo);
        holder.tvHardCount.setText(dynamicList.get(position).getNickname());

        GlideImgManager.glideLoader(context, dynamicList.get(position).getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.ivHeadPortrait, 0);
        holder.tvTitle.setText(dynamicList.get(position).getContent().trim());
        holder.tvHardCount.setText(dynamicList.get(position).getLaudnum());


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return dynamicList.size();
    }

    class DynamicHolder extends RecyclerView.ViewHolder {
        ImageView ivVideo, ivAttention, ivRedHeart, ivHeadPortrait;
        TextView tvHardCount, tvTitle;

        public DynamicHolder(final View itemView) {
            super(itemView);
            ivVideo = itemView.findViewById(R.id.iv_video);
            ivHeadPortrait = itemView.findViewById(R.id.iv_head_portrait);
            ivAttention = itemView.findViewById(R.id.iv_attention);
            ivRedHeart = itemView.findViewById(R.id.iv_red_heart);
            tvHardCount = itemView.findViewById(R.id.tv_hard_count);
            tvTitle = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());

                    }
                }
            });


        }


    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }


}
