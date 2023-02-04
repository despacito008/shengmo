package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.bean.RankBerewardData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/5/11.
 */

public class RankRewardAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private List<RankBerewardData.DataBean> list;

    public RankRewardAdapter(Context context, List<RankBerewardData.DataBean> list) {
        this.context = context;
        this.list = list;
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
            convertView = inflater.inflate(R.layout.item_listview_ranklist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RankBerewardData.DataBean data = list.get(position);
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context,data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(),data.getBkvip(),data.getBlvip(), holder.itemListviewRanklistVip);

        if (data.getRealname().equals("0")) {
            holder.itemListviewRanklistShiming.setVisibility(View.GONE);
        } else {
            holder.itemListviewRanklistShiming.setVisibility(View.VISIBLE);
        }

        if (data.getSex().equals("1")) {
            holder.itemListviewRanklistSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewRanklistSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("2")) {
            holder.itemListviewRanklistSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewRanklistSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("3")) {
            holder.itemListviewRanklistSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewRanklistSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getRole().equals("S")) {
            holder.itemListviewRanklistRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemListviewRanklistRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemListviewRanklistRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemListviewRanklistRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemListviewRanklistRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemListviewRanklistRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.itemListviewRanklistRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemListviewRanklistRole.setText("~");
        }
        holder.itemListviewRanklistSex.setText(data.getAge());
        holder.itemListviewRanklistNumber.setText(position + 1 + "");
        if (!data.getHead_pic().equals(NetPic()) || !data.getHead_pic().equals("")) {//"http://59.110.28.150:888/"
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewRanklistIcon, 0);
        } else {
            holder.itemListviewRanklistIcon.setImageResource(R.mipmap.morentouxiang);
        }
        holder.itemListviewRanklistName.setText(data.getNickname());
        holder.itemListviewRanklistCount.setText(data.getAllamount());
        holder.itemListviewRanklistCaifu.setText(data.getWealth_val());
        holder.itemListviewRanklistFirstIcon.setImageResource(R.mipmap.zuiaiicon);
        if (data.getRewardeduserinfo() != null) {
            if (!data.getHead_pic().equals(NetPic()) || !data.getHead_pic().equals("")) {//"http://59.110.28.150:888/"
                GlideImgManager.glideLoader(context, data.getRewardeduserinfo().getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewRanklistIcon01, 0);
            } else {
                holder.itemListviewRanklistIcon.setImageResource(R.mipmap.morentouxiang);
            }
            holder.itemListviewRanklistIcon01.setVisibility(View.VISIBLE);
        }
        holder.itemListviewRanklistIcon.setTag(position);
        holder.itemListviewRanklistIcon.setOnClickListener(this);
        holder.itemListviewRanklistIcon01.setTag(position);
        holder.itemListviewRanklistIcon01.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        Intent intent;
        switch (v.getId()) {
            case R.id.item_listview_ranklist_icon:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getUid());
                context.startActivity(intent);
                break;
            case R.id.item_listview_ranklist_icon01:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getRewardeduserinfo().getFuid());
                context.startActivity(intent);
                break;

        }
    }

    static class ViewHolder {
        @BindView(R.id.item_listview_ranklist_number)
        TextView itemListviewRanklistNumber;
        @BindView(R.id.item_listview_ranklist_icon)
        ImageView itemListviewRanklistIcon;
        @BindView(R.id.item_listview_ranklist_vip)
        ImageView itemListviewRanklistVip;
        @BindView(R.id.item_listview_ranklist_name)
        TextView itemListviewRanklistName;
        @BindView(R.id.item_listview_ranklist_shiming)
        ImageView itemListviewRanklistShiming;
        @BindView(R.id.item_listview_ranklist_Sex)
        TextView itemListviewRanklistSex;
        @BindView(R.id.item_listview_ranklist_role)
        TextView itemListviewRanklistRole;
        @BindView(R.id.item_listview_ranklist_caifu)
        TextView itemListviewRanklistCaifu;
        @BindView(R.id.item_listview_ranklist_count)
        TextView itemListviewRanklistCount;
        @BindView(R.id.item_listview_ranklist_firstIcon)
        ImageView itemListviewRanklistFirstIcon;
        @BindView(R.id.item_listview_ranklist_icon01)
        ImageView itemListviewRanklistIcon01;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
