package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;

import com.aiwujie.shengmo.activity.ZoomActivity;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.NineGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 290243232 on 2017/3/15.
 */

public class OwnerDisplayAdapter extends NineGridViewAdapter {
    private Context context;
    private List<ImageInfo> imageInfo;
    public OwnerDisplayAdapter(Context context, List<ImageInfo> imageInfo) {
        super(context, imageInfo);
        this.context=context;
        this.imageInfo=imageInfo;
    }

    @Override
    protected void onImageItemClick(Context context, NineGridView nineGridView, int index, List<ImageInfo> imageInfo) {
        super.onImageItemClick(context, nineGridView, index, imageInfo);
        ArrayList<String> imgs=new ArrayList<>();
        for (int i=0;i<imageInfo.size();i++){
            imgs.add(imageInfo.get(i).getBigImageUrl());
        }
        Intent intent = new Intent(context, ZoomActivity.class);
        intent.putExtra("position", index);
        intent.putStringArrayListExtra("pics", imgs);
        context.startActivity(intent);
    }

}
