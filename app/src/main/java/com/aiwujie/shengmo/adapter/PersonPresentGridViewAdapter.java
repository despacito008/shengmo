package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.MyPresentData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 290243232 on 2017/6/20.
 */

public class PersonPresentGridViewAdapter extends BaseAdapter{
    private List<MyPresentData.DataBean.GiftArrBean> mDatas;
    private LayoutInflater inflater;

    private int[] presents = {R.mipmap.presentnew01, R.mipmap.presentnew02, R.mipmap.presentnew03, R.mipmap.presentnew04, R.mipmap.presentnew05,
            R.mipmap.presentnew06, R.mipmap.presentnew07, R.mipmap.presentnew08, R.mipmap.presentnew09,
            R.mipmap.presentnew10, R.mipmap.presentnew11, R.mipmap.presentnew12, R.mipmap.presentnew13,
            R.mipmap.presentnew14, R.mipmap.presentnew15, R.mipmap.presentnew16, R.mipmap.presentnew17,
            R.mipmap.presentnew18, R.mipmap.presentnew19, R.mipmap.presentnew20, R.mipmap.presentnew21,
            R.mipmap.presentnew22, R.mipmap.presentnew23, R.mipmap.presentnew24, R.mipmap.presentnew25,
            R.mipmap.presentnew26, R.mipmap.presentnew27,R.mipmap.presentnew28,R.mipmap.presentnew29,R.mipmap.presentnew30,
            R.mipmap.presentnew31,R.mipmap.presentnew32,R.mipmap.weizhiliwu,R.mipmap.weizhiliwu,R.mipmap.weizhiliwu,R.mipmap.weizhiliwu,R.mipmap.weizhiliwu};
    private String[] moneys = {"2魔豆", "6魔豆", "10魔豆", "38魔豆", "99魔豆", "88魔豆", "123魔豆", "166魔豆", "199魔豆", "520魔豆", "666魔豆", "250魔豆", "777魔豆", "888魔豆", "999魔豆", "1314魔豆", "1666魔豆", "1999魔豆", "666魔豆", "999魔豆", "1888魔豆", "2899魔豆", "3899魔豆", "6888魔豆", "9888魔豆", "52000魔豆", "99999魔豆", "1魔豆", "3魔豆", "5魔豆", "10魔豆", "8魔豆", "未知", "未知", "未知", "未知", "未知"};

    public PersonPresentGridViewAdapter(Context context, List<MyPresentData.DataBean.GiftArrBean> mDatas) {
        inflater = LayoutInflater.from(context);
        //按照魔豆排序大到小。
        Collections.sort(mDatas, new Comparator<MyPresentData.DataBean.GiftArrBean>(){
            @Override
            public int compare(MyPresentData.DataBean.GiftArrBean o1, MyPresentData.DataBean.GiftArrBean o2) {
             /*
             * int compare(Student o1, Student o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
                if(Integer.parseInt(moneys[Integer.parseInt(o1.getType())-10].substring(0,moneys[Integer.parseInt(o1.getType())-10].length()-2)) >Integer.parseInt(moneys[Integer.parseInt(o2.getType())-10].substring(0,moneys[Integer.parseInt(o2.getType())-10].length()-2))){
                    return -1;
                }
                if(Integer.parseInt(moneys[Integer.parseInt(o1.getType())-10].substring(0,moneys[Integer.parseInt(o1.getType())-10].length()-2)) ==Integer.parseInt(moneys[Integer.parseInt(o2.getType())-10].substring(0,moneys[Integer.parseInt(o2.getType())-10].length()-2))){
                    return 0;
                }
                return 1;
            }
        });
        this.mDatas = mDatas;

    }

    /**
     * 先判断数据集的大小是否足够显示满本页,如果够，则直接返回每一页显示的最大条目个数pageSize,如果不够，则有几项就返回几,(也就是最后一页的时候就显示剩余item)
     */
    @Override
    public int getCount() {
        return mDatas.size() ;

    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview_person_present, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.item_gridview_person_present_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize
         */
        MyPresentData.DataBean.GiftArrBean data=mDatas.get(position);
        viewHolder.iv.setImageResource(presents[Integer.parseInt(data.getType()) - 10]);
        return convertView;
    }


    class ViewHolder {
        public ImageView iv;
    }
}
