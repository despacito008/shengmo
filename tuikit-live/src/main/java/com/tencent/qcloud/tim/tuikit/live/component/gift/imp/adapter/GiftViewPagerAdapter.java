package com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;



import java.util.List;

public class GiftViewPagerAdapter extends PagerAdapter {

    private List<View> mViews;

    public GiftViewPagerAdapter(List<View> views) {
        this.mViews = views;
    }

    @Override
    public int getCount() {
        return mViews != null ? mViews.size() : 0;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup v = (ViewGroup) mViews.get(position).getParent();
        if (v != null) {
            v.removeView(mViews.get(position));
        } else {
            Log.i("Allen", String.valueOf(position + 1));
        }
        container.addView(mViews.get(position), 0);
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(position > mViews.size() - 1)return;
        container.removeView(mViews.get(position));
    }
}