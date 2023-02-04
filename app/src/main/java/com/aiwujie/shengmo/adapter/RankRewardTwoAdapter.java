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
import com.aiwujie.shengmo.bean.RankRegalTowData;
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

public class RankRewardTwoAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private List<RankRegalTowData.DataBean> list;

    public RankRewardTwoAdapter(Context context, List<RankRegalTowData.DataBean> list) {
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
            convertView = inflater.inflate(R.layout.item_listview_ranklist_two, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RankRegalTowData.DataBean data = list.get(position);
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context,data.getHead_pic(), data.getFuid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(),data.getBkvip(),data.getBlvip(), holder.itemListviewRanklistTwoVip);

        if (data.getRealname().equals("0")) {
            holder.itemListviewRanklistTwoShiming.setVisibility(View.GONE);
        } else {
            holder.itemListviewRanklistTwoShiming.setVisibility(View.VISIBLE);
        }

        if (data.getSex().equals("1")) {
            holder.itemListviewRanklistTwoSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewRanklistTwoSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("2")) {
            holder.itemListviewRanklistTwoSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewRanklistTwoSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("3")) {
            holder.itemListviewRanklistTwoSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewRanklistTwoSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getRole().equals("S")) {
            holder.itemListviewRanklistTwoRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemListviewRanklistTwoRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemListviewRanklistTwoRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemListviewRanklistTwoRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemListviewRanklistTwoRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemListviewRanklistTwoRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.itemListviewRanklistTwoRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemListviewRanklistTwoRole.setText("~");
        }
        holder.itemListviewRanklistTwoSex.setText(data.getAge());
        holder.itemListviewRanklistTwoNumber.setText(position + 1 + "");
        if (!data.getHead_pic().equals(NetPic()) || !data.getHead_pic().equals("")) {//"http://59.110.28.150:888/"
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewRanklistTwoIcon, 0);
        } else {
            holder.itemListviewRanklistTwoIcon.setImageResource(R.mipmap.morentouxiang);
        }
        holder.itemListviewRanklistTwoName.setText(data.getNickname());
        holder.itemListviewRanklistTwoCount.setText(data.getAllamount());
        holder.itemListviewRanklistTwoCaifu.setText(data.getCharm_val());
        holder.itemListviewRanklistTwoFirstIcon.setImageResource(R.mipmap.haofenicon);
        if (data.getRewarduserinfo()!= null) {
            if (!data.getHead_pic().equals(NetPic()) || !data.getHead_pic().equals("")) {//"http://59.110.28.150:888/"
                GlideImgManager.glideLoader(context, data.getRewarduserinfo().getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewRanklistTwoIcon01, 0);
            } else {
                holder.itemListviewRanklistTwoIcon.setImageResource(R.mipmap.morentouxiang);
            }
            holder.itemListviewRanklistTwoIcon01.setVisibility(View.VISIBLE);
        }
        holder.itemListviewRanklistTwoIcon.setTag(position);
        holder.itemListviewRanklistTwoIcon.setOnClickListener(this);
        holder.itemListviewRanklistTwoIcon01.setTag(position);
        holder.itemListviewRanklistTwoIcon01.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        Intent intent;
        switch (v.getId()) {
            case R.id.item_listview_ranklist_two_icon:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getFuid());
                context.startActivity(intent);
                break;
            case R.id.item_listview_ranklist_two_icon01:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getRewarduserinfo().getUid());
                context.startActivity(intent);
                break;

        }
    }

    static class ViewHolder {
        @BindView(R.id.item_listview_ranklist_two_number)
        TextView itemListviewRanklistTwoNumber;
        @BindView(R.id.item_listview_ranklist_two_icon)
        ImageView itemListviewRanklistTwoIcon;
        @BindView(R.id.item_listview_ranklist_two_vip)
        ImageView itemListviewRanklistTwoVip;
        @BindView(R.id.item_listview_ranklist_two_name)
        TextView itemListviewRanklistTwoName;
        @BindView(R.id.item_listview_ranklist_two_shiming)
        ImageView itemListviewRanklistTwoShiming;
        @BindView(R.id.item_listview_ranklist_two_Sex)
        TextView itemListviewRanklistTwoSex;
        @BindView(R.id.item_listview_ranklist_two_role)
        TextView itemListviewRanklistTwoRole;
        @BindView(R.id.item_listview_ranklist_two_caifu)
        TextView itemListviewRanklistTwoCaifu;
        @BindView(R.id.item_listview_ranklist_two_count)
        TextView itemListviewRanklistTwoCount;
        @BindView(R.id.item_listview_ranklist_two_firstIcon)
        ImageView itemListviewRanklistTwoFirstIcon;
        @BindView(R.id.item_listview_ranklist_two_icon01)
        ImageView itemListviewRanklistTwoIcon01;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
