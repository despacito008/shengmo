package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.MyPresentData;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserInfoGiftRecyclerAdapter extends RecyclerView.Adapter<UserInfoGiftRecyclerAdapter.GiftHolder> {
    private List<MyPresentData.DataBean.GiftArrBean> mDatas;
    private Context context;
    private int[] presents = {R.mipmap.presentnew01, R.mipmap.presentnew02, R.mipmap.presentnew03, R.mipmap.presentnew04, R.mipmap.presentnew05,
            R.mipmap.presentnew06, R.mipmap.presentnew07, R.mipmap.presentnew08, R.mipmap.presentnew09,
            R.mipmap.presentnew10, R.mipmap.presentnew11, R.mipmap.presentnew12, R.mipmap.presentnew13,
            R.mipmap.presentnew14, R.mipmap.presentnew15, R.mipmap.presentnew16, R.mipmap.presentnew17,
            R.mipmap.presentnew18, R.mipmap.presentnew19, R.mipmap.presentnew20, R.mipmap.presentnew21,
            R.mipmap.presentnew22, R.mipmap.presentnew23, R.mipmap.presentnew24, R.mipmap.presentnew25,
            R.mipmap.presentnew26, R.mipmap.presentnew27,R.mipmap.presentnew28,R.mipmap.presentnew29,R.mipmap.presentnew30,
            R.mipmap.presentnew31,R.mipmap.presentnew32,R.mipmap.weizhiliwu,R.mipmap.weizhiliwu,R.mipmap.weizhiliwu,R.mipmap.weizhiliwu,R.mipmap.weizhiliwu};
    private String[] moneys = {"2魔豆", "6魔豆", "10魔豆", "38魔豆", "99魔豆", "88魔豆", "123魔豆", "166魔豆", "199魔豆", "520魔豆", "666魔豆", "250魔豆", "777魔豆", "888魔豆", "999魔豆", "1314魔豆", "1666魔豆", "1999魔豆", "666魔豆", "999魔豆", "1888魔豆", "2899魔豆", "3899魔豆", "6888魔豆", "9888魔豆", "52000魔豆", "99999魔豆", "1魔豆", "3魔豆", "5魔豆", "10魔豆", "8魔豆", "未知", "未知", "未知", "未知", "未知"};

    public UserInfoGiftRecyclerAdapter(Context context, List<MyPresentData.DataBean.GiftArrBean> mDatas) {
        this.context = context;
        //按照魔豆排序大到小。
//        Collections.sort(mDatas, new Comparator<MyPresentData.DataBean.GiftArrBean>(){
//            @Override
//            public int compare(MyPresentData.DataBean.GiftArrBean o1, MyPresentData.DataBean.GiftArrBean o2) {
////                if (Integer.parseInt(o1.getType()) < 10 || Integer.parseInt(o2.getType()) < 10)  {
////                    return 0;
////                }
////                /*
////                 * int compare(Student o1, Student o2) 返回一个基本类型的整型，
////                 * 返回负数表示：o1 小于o2，
////                 * 返回0 表示：o1和o2相等，
////                 * 返回正数表示：o1大于o2。
////                 */
////                if(Integer.parseInt(moneys[Integer.parseInt(o1.getType())-10].substring(0,moneys[Integer.parseInt(o1.getType())-10].length()-2)) >Integer.parseInt(moneys[Integer.parseInt(o2.getType())-10].substring(0,moneys[Integer.parseInt(o2.getType())-10].length()-2))){
////                    return -1;
////                }
////                if(Integer.parseInt(moneys[Integer.parseInt(o1.getType())-10].substring(0,moneys[Integer.parseInt(o1.getType())-10].length()-2)) ==Integer.parseInt(moneys[Integer.parseInt(o2.getType())-10].substring(0,moneys[Integer.parseInt(o2.getType())-10].length()-2))){
////                    return 0;
////                }
//                return 1;
//            }
//        });
        this.mDatas = mDatas;
    }


    @Override
    public GiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_person_present, parent, false);
        return new GiftHolder(view);
    }

    @Override
    public void onBindViewHolder(GiftHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class GiftHolder extends RecyclerView.ViewHolder {
        ImageView ivItem;
        public GiftHolder(View itemView) {
            super(itemView);
            ivItem = itemView.findViewById(R.id.item_gridview_person_present_iv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onSimpleItemListener != null) {
                        onSimpleItemListener.onItemListener(0);
                    }
                }
            });
        }
        public void setData(int position) {
            MyPresentData.DataBean.GiftArrBean data=mDatas.get(position);
//            if (Integer.parseInt(data.getType()) >= 10) {
//                ivItem.setImageResource(presents[Integer.parseInt(data.getType()) - 10]);
//            }
            Glide.with(context).load(data.getGift_img()).error(R.mipmap.weizhiliwu).into(ivItem);
        }
    }
    OnSimpleItemListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }
}
