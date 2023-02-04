package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.GroupData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchGroupResultAdapter extends RecyclerView.Adapter<SearchGroupResultAdapter.SearchGroupResultHolder> {
    Context context;
    List<GroupData.DataBean> groupList;
    int retcode;

    public SearchGroupResultAdapter(Context context, List<GroupData.DataBean> groupList,int retcode) {
        this.context = context;
        this.groupList = groupList;
        this.retcode = retcode;
    }

    @Override
    public SearchGroupResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listview_group, parent, false);
        return new SearchGroupResultHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchGroupResultHolder holder, int position) {
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class SearchGroupResultHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_group_listview_icon)
        ImageView itemGroupListviewIcon;
        @BindView(R.id.item_group_listview_vip)
        ImageView itemGroupListviewVip;
        @BindView(R.id.item_group_listview_name)
        TextView itemGroupListviewName;
        @BindView(R.id.item_group_listview_sign)
        TextView itemGroupListviewSign;
        @BindView(R.id.item_group_listview_peoplecount)
        TextView itemGroupListviewPeoplecount;
        @BindView(R.id.item_group_listview_distance)
        TextView itemGroupListviewDistance;
        @BindView(R.id.item_group_listview_claim)
        TextView itemGroupListviewClaim;
        public SearchGroupResultHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        void display(int index) {
            GroupData.DataBean data = groupList.get(index);
            if (retcode == 2000) {
                itemGroupListviewDistance.setVisibility(View.VISIBLE);
                itemGroupListviewDistance.setText(data.getDistance() + "km");
            } else {
                itemGroupListviewDistance.setVisibility(View.INVISIBLE);
            }
            if (data.getGroup_pic().equals("http:\\/\\/aiwujie.com.cn\\/")) {//"http:\\/\\/59.110.28.150:888\\/"
                itemGroupListviewIcon.setImageResource(R.mipmap.qunmorentouxiang);
            } else {
                GlideImgManager.glideLoader(context, data.getGroup_pic(), R.mipmap.qunmorentouxiang, R.mipmap.qunmorentouxiang, itemGroupListviewIcon, 0);
            }
            itemGroupListviewName.setText(data.getGroupname());
            itemGroupListviewPeoplecount.setText(data.getMember() + "人");
            if (!data.getIntroduce().equals("")) {
                itemGroupListviewSign.setText(data.getIntroduce());
            } else {
                itemGroupListviewSign.setText("(该群未填写群介绍)");
            }
            if ("1".equals(data.getIs_claim())) {
                itemGroupListviewClaim.setVisibility(View.VISIBLE);
            } else {
                itemGroupListviewClaim.setVisibility(View.GONE);
            }
            if(data.getGroup_num().length()<=5){
                itemGroupListviewVip.setVisibility(View.VISIBLE);
            }else{
                itemGroupListviewVip.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSimpleItemListener != null) {
                        onSimpleItemListener.onItemListener(index);
                    }
                }
            });
        }
    }
    OnSimpleItemListener onSimpleItemListener;
    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }
}
