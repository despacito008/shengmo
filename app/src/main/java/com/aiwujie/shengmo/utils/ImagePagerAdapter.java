package com.aiwujie.shengmo.utils;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    private Context context;
    private List<ImageView> strDrawables;

    public ImagePagerAdapter(Context context, List<ImageView> strDrawables) {
        this.context=context;
        this.strDrawables=strDrawables;
    }

    @Override
    public int getCount() {
        return strDrawables.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        ImageView imageView = strDrawables.get(position);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}

