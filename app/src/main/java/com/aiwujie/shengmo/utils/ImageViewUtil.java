package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.ImagePreviewActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageViewUtil {

    public static void previewImage(Context context,String... images) {
        List<ImageInfo> imageInfo = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            ImageInfo info = new ImageInfo();
            info.setThumbnailUrl(images[i]);
            info.setBigImageUrl(images[i]);
            imageInfo.add(info);
        }
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, 0);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
