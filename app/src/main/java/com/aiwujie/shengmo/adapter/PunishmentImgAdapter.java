package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.BitmapUtils;
import com.aiwujie.shengmo.utils.GlideImageLoader;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.OnSimpleItemViewListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class PunishmentImgAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String> imgList;
    Context context;

    public PunishmentImgAdapter(List<String> imgList, Context context) {
        this.imgList = imgList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_punishment_img,parent,false);
        return new imgHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((imgHolder)holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    class imgHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        public imgHolder(final View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_item_punishment_img);
            ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onSimpleItemListener != null) {
                        onSimpleItemListener.onItemListener(itemView);
                    }
                }
            });
        }

        public void setData(int position) {
            Glide.with(context)
                    .load(imgList.get(position))
                    .apply(new RequestOptions().placeholder(R.drawable.default_error).error(R.drawable.default_error).transform(new MultiTransformation(new CenterCrop(),new RoundedCorners(BitmapUtils.dip2pix(context,3)))))
                    .into(ivImg);
        }
    }

    OnSimpleItemViewListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemViewListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }
}
