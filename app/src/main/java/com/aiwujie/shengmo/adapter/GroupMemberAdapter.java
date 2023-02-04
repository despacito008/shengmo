package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.InviteGroupMemberActivity;
import com.aiwujie.shengmo.bean.GroupInfoMemberData;
import com.aiwujie.shengmo.kt.ui.activity.GroupInviteActivity;
import com.aiwujie.shengmo.utils.BitmapUtils;
import com.aiwujie.shengmo.utils.GlideCircleTransform;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/1/17.
 */
public class GroupMemberAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<GroupInfoMemberData.DataBean> list;
    private LayoutInflater inflater;
    private String groupId;
    private String userPower;
    private String leaderLet = "1";

    public GroupMemberAdapter(Context context, List<GroupInfoMemberData.DataBean> list,String groupId,String userPower) {
        this.context = context;
        this.list = list;
        this.groupId=groupId;
        this.userPower=userPower;
        inflater = LayoutInflater.from(context);
    }

    public GroupMemberAdapter(Context context, List<GroupInfoMemberData.DataBean> list,String groupId,String userPower,String leaderLet) {
        this.context = context;
        this.list = list;
        this.groupId=groupId;
        this.userPower=userPower;
        this.leaderLet = leaderLet;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview_groupmember, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        GroupInfoMemberData.DataBean data=list.get(position);
        if(position != list.size() - 1 || position < 6) {
            if (data.getHead_pic().equals("") || data.equals(NetPic())) {
                Glide.with(context).load(R.mipmap.morentouxiang).override(BitmapUtils.dip2px(context,45)).into(holder.itemGridviewGroupIcon);
            } else {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.mipmap.morentouxiang);
                requestOptions.error(R.mipmap.morentouxiang);
                requestOptions.transform(new GlideCircleTransform());
                Glide.with(context).load(Uri.parse(data.getHead_pic())).override(BitmapUtils.dip2px(context,45)).apply(requestOptions).into(holder.itemGridviewGroupIcon);
            }
        } else {
            if ("1".equals(userPower) || "2".equals(userPower) || "3".equals(userPower)) {
                Glide.with(context).load(R.drawable.group_add).override(BitmapUtils.dip2px(context, 37)).into(holder.itemGridviewGroupIcon);
            } else {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.mipmap.morentouxiang);
                requestOptions.error(R.mipmap.morentouxiang);
                requestOptions.transform(new GlideCircleTransform());
                Glide.with(context).load(Uri.parse(data.getHead_pic())).override(BitmapUtils.dip2px(context,45)).apply(requestOptions).into(holder.itemGridviewGroupIcon);
            }
        }

        if(data.getState()!=null) {
            switch (data.getState()) {
                case "1":
                    holder.itemGridviewGroupFlag.setVisibility(View.INVISIBLE);
                    break;
                case "2":
                    holder.itemGridviewGroupFlag.setBackgroundResource(R.drawable.group_manager);
                    break;
                case "3":
                    holder.itemGridviewGroupFlag.setBackgroundResource(R.drawable.group_member_leader);
                    if (!leaderLet.equals("1")) {
                        holder.itemGridviewGroupFlag.setText("群主" + leaderLet);
                    } else {
                        holder.itemGridviewGroupFlag.setText("群主");
                    }
                    break;
            }
        }
        switch (userPower){
            case "1":
            case "2":
            case "3":
                if (position == list.size() - 1) {
                    holder.itemGridviewGroupIcon.setOnClickListener(this);
                }
                break;
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(context, GroupInviteActivity.class);
//        intent.putExtra("groupId",groupId);
//        context.startActivity(intent);
        if (onSimpleItemListener != null) {
            onSimpleItemListener.onItemListener(1);
        }
    }

    static class ViewHolder {
        @BindView(R.id.item_gridview_groupIcon)
        ImageView itemGridviewGroupIcon;
        @BindView(R.id.item_gridview_groupFlag)
        TextView itemGridviewGroupFlag;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    OnSimpleItemListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }
}
