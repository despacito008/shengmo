package com.aiwujie.shengmo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.bumptech.glide.Glide;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.ImagePreviewActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2020/6/2.
 */

public class BeizhuImgAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<String> pictures=new ArrayList<String>();

    public BeizhuImgAdapter(ArrayList<String> pictures, Context context) {
        super();
        this.context = context;
        this.pictures = pictures;
    }

    @Override
    public int getCount() {

        if (null != pictures) {
            return pictures.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        return pictures.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            // 获得容器
            convertView = LayoutInflater.from(this.context).inflate(R.layout.item_sqimg_show, null);

            // 初始化组件
            viewHolder.image = (ImageView) convertView.findViewById(R.id.pic_img);
            // 给converHolder附加一个对象
            convertView.setTag(viewHolder);
        } else {
            // 取得converHolder附加的对象
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(pictures.get(position)).into(viewHolder.image);
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                for (int i = 0; i < pictures.size(); i++) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(pictures.get(i));
                    info.setBigImageUrl(pictures.get(i));
                    imageInfo.add(info);
                }
                Intent intent = new Intent(context, ImagePreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO,  imageInfo);
                bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, position);
                intent.putExtras(bundle);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
            }
        });


        return convertView;
    }

    class ViewHolder {
        public ImageView image;
    }


}
