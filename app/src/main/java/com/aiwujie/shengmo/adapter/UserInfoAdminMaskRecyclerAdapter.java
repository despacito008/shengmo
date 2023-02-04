package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by 290243232 on 2016/11/23.
 */
public class UserInfoAdminMaskRecyclerAdapter extends RecyclerView.Adapter<UserInfoAdminMaskRecyclerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<String> mDatas;
    private Context context;
    //who为0的时候是看自己的相册 1为其他人
    private int who;

    public UserInfoAdminMaskRecyclerAdapter(List<String> mDatas, Context context, int who) {
        this.mDatas = mDatas;
        this.context = context;
        this.who = who;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * ItemClick的回调接口
     *
     * @author zhy
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_personmsg_user_info_mask,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.item_personMsg_iv);
        viewHolder.mSuo = (ImageView) view.findViewById(R.id.item_personMsg_suo);
        AutoUtils.auto(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            if (who == 1) {
                RequestOptions requestOptions = new RequestOptions();
//                                    requestOptions.placeholder(R.drawable.rc_image_error);
//                requestOptions.error(R.drawable.rc_image_error);
                requestOptions.transform((new MultiTransformation(new CenterCrop(),new RoundedCorners(20),new BlurTransformation( 40))));
                Glide.with(context).load(mDatas.get(position))
                        .apply(requestOptions)
                        .into(holder.mImg);
            } else if (who == 2) {
                RequestOptions requestOptions = new RequestOptions();
//                                    requestOptions.placeholder(R.drawable.rc_image_error);
//                requestOptions.error(R.drawable.rc_image_error);
                requestOptions.transform((new MultiTransformation(new CenterCrop(),new RoundedCorners(20),new BlurTransformation( 40))));
                Glide.with(context).load(mDatas.get(position))
                        .apply(requestOptions)
                        .into(holder.mImg);
            } else {
               // GlideImgManager.glideLoader(context, mDatas.get(position), R.mipmap.default_error, R.mipmap.default_error, holder.mImg);
                RequestOptions requestOptions = new RequestOptions();
//                                    requestOptions.placeholder(R.drawable.rc_image_error);
//                requestOptions.error(R.drawable.rc_image_error);
                requestOptions.transform((new MultiTransformation(new CenterCrop(),new RoundedCorners(20))));
                Glide.with(context).load(mDatas.get(position))
                        .apply(requestOptions)
                        .into(holder.mImg);
            }
            holder.mSuo.setVisibility(View.GONE);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
//        holder.mImg.setImageResource(R.mipmap.ic_launcher);
        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg;
        ImageView mSuo;
    }
}
