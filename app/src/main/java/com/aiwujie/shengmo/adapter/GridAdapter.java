package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.GlideImgManager;

import java.util.List;

/**
 * Created by 290243232 on 2016/12/8.
 */
public class GridAdapter extends BaseAdapter {
    private List<String> listUrls;
    private Context context;
    private LayoutInflater inflater;
    public GridAdapter(List<String> listUrls,Context context) {
        this.context=context;
        this.listUrls = listUrls;
//			listUrls.remove(listUrls.size()-1);
        inflater = LayoutInflater.from(context);
    }

    public int getCount(){
        return  listUrls.size();
    }
    @Override
    public String getItem(int position) {
        return listUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_image, parent,false);
            holder.image = (ImageView) convertView.findViewById(R.id.item_dynamic_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final String path=listUrls.get(position);
        GlideImgManager.glideLoader(context, path, R.mipmap.default_error, R.mipmap.default_error, holder.image);
        return convertView;
    }
    class ViewHolder {
        ImageView image;
    }

}
