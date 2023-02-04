package com.aiwujie.shengmo.adapter;

import android.content.Context;

import com.aiwujie.shengmo.eventbus.EditDynamicIndexEvent;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.NineGridViewAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by 290243232 on 2017/3/15.
 */

public class EditDynamicPicAdapter extends NineGridViewAdapter implements OnItemClickListener {
    private Context context;
    private List<ImageInfo> imageInfo;
    private int positions;

    public EditDynamicPicAdapter(Context context, List<ImageInfo> imageInfo) {
        super(context, imageInfo);
        this.context=context;
        this.imageInfo=imageInfo;
    }

    @Override
    protected void onImageItemClick(Context context, NineGridView nineGridView, int index, List<ImageInfo> imageInfo) {
        super.onImageItemClick(context, nineGridView, index, imageInfo);
//        ArrayList<String> imgs=new ArrayList<>();
//        for (int i=0;i<imageInfo.size();i++){
//            imgs.add(imageInfo.get(i).getBigImageUrl());
//        }
//        Intent intent = new Intent(context, ZoomActivity.class);
//        intent.putExtra("position", index);
//        intent.putStringArrayListExtra("pics", imgs);
//        context.startActivity(intent);
        positions=index;
        Operation();
    }

    private void Operation() {
        new AlertView(null, null, "取消", null,
                new String[]{"删除"},
                context, AlertView.Style.ActionSheet, this).show();
    }

    @Override
    public void onItemClick(Object o, int position,String data) {
        switch (position){
            case 0:
                EventBus.getDefault().post(new EditDynamicIndexEvent(positions));
                break;
        }
    }
}


