package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.PresentData;

import java.util.List;

/**
 * Created by 290243232 on 2017/6/20.
 */

public class PresentGridViewAdapter extends BaseAdapter{
    private List<PresentData> mDatas;
    private LayoutInflater inflater;
    /**
     * 页数下标,从0开始(当前是第几页)
     */
    private int curIndex;
    /**
     * 每一页显示的个数
     */
    private int pageSize;

    public PresentGridViewAdapter(Context context, List<PresentData> mDatas, int curIndex, int pageSize) {
        inflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.curIndex = curIndex;
        this.pageSize = pageSize;
    }

    /**
     * 先判断数据集的大小是否足够显示满本页,如果够，则直接返回每一页显示的最大条目个数pageSize,如果不够，则有几项就返回几,(也就是最后一页的时候就显示剩余item)
     */
    @Override
    public int getCount() {
        return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);

    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview_present_new, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.item_gridview_present_name);
            viewHolder.tvMoney = (TextView) convertView.findViewById(R.id.item_gridview_present_dou);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.item_gridview_present_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize
         */
        int pos = position + curIndex * pageSize;
        viewHolder.tvName.setText(mDatas.get(pos).getName());
        viewHolder.tvMoney.setText(mDatas.get(pos).getMoney());
        viewHolder.iv.setImageResource(mDatas.get(pos).getIconRes());
        return convertView;
    }


    class ViewHolder {
        public TextView tvName;
        public TextView tvMoney;
        public ImageView iv;
    }
}
