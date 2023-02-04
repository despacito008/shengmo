package com.aiwujie.shengmo.recycleradapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.GrouplistActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.bean.FriendGroupListBean;
import com.aiwujie.shengmo.kt.ui.activity.GroupingSettingActivity;

import java.util.List;


public class LabellistAdapter2 extends RecyclerView.Adapter {

    private Context mContext;
    private List<FriendGroupListBean.DataBean> mEntityList;

    public LabellistAdapter2(Context context, List<FriendGroupListBean.DataBean> entityList){
        this.mContext = context;
        this.mEntityList = entityList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.app_item_group_label, parent, false);
        return new DemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final FriendGroupListBean.DataBean entity = mEntityList.get(position);
        ((DemoViewHolder)holder).mText.setText(entity.getFgname()+" ("+mEntityList.get(position).getNum()+")");
        ((DemoViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(mContext, GrouplistActivity.class);
                Intent intent = new Intent(mContext, GroupingSettingActivity.class);
                intent.putExtra("groupname",mEntityList.get(position).getFgname());
                intent.putExtra("fgid",mEntityList.get(position).getId());
                mContext.startActivity(intent);
            }
        });
        ((DemoViewHolder)holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onLabelListener != null) {
                    onLabelListener.doLabelLongClick(position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEntityList.size();
    }

    private class DemoViewHolder extends RecyclerView.ViewHolder{

        private TextView mText;

        public DemoViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.item_gridview_two_common_wealth_tv);
        }
    }

    public interface OnLabelListener {
        void doLabelLongClick(int index);
    }
    OnLabelListener onLabelListener;

    public void setOnLabelListener(OnLabelListener onLabelListener) {
        this.onLabelListener = onLabelListener;
    }
}