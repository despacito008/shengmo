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
import com.aiwujie.shengmo.bean.RankCharmData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/5/11.
 */

public class RankCharmAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private List<RankCharmData.DataBean> list;

    public RankCharmAdapter(Context context, List<RankCharmData.DataBean> list) {
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
            convertView = inflater.inflate(R.layout.item_listview_charm_ranklist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RankCharmData.DataBean data = list.get(position);
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.getHead_pic(),data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(),data.getBkvip(),data.getBlvip(), holder.itemListviewCharmVip);

        if (data.getRealname().equals("0")) {
            holder.itemListviewCharmShiming.setVisibility(View.GONE);
        } else {
            holder.itemListviewCharmShiming.setVisibility(View.VISIBLE);
        }

        if (data.getSex().equals("1")) {
            holder.itemListviewCharmSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewCharmSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("2")) {
            holder.itemListviewCharmSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewCharmSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("3")) {
            holder.itemListviewCharmSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewCharmSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getRole().equals("S")) {
            holder.itemListviewCharmRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemListviewCharmRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemListviewCharmRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemListviewCharmRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemListviewCharmRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemListviewCharmRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.itemListviewCharmRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemListviewCharmRole.setText("~");
        }
        holder.itemListviewCharmSex.setText(data.getAge());
        holder.itemListviewCharmNumber.setText(position + 1 + "");
        holder.itemListviewCharmMeili.setText(data.getCharm_val());
        if (!data.getHead_pic().equals(NetPic()) || !data.getHead_pic().equals("")) {//"http://59.110.28.150:888/"
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewCharmIcon, 0);
        } else {
            holder.itemListviewCharmIcon.setImageResource(R.mipmap.morentouxiang);
        }
        holder.itemListviewCharmName.setText(data.getNickname());
        holder.itemListviewCharmIcon.setTag(position);
        holder.itemListviewCharmIcon.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        Intent intent;
        switch (v.getId()) {
            case R.id.item_listview_charm_icon:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getUid());
                context.startActivity(intent);
                break;

        }
    }

    static class ViewHolder {
        @BindView(R.id.item_listview_charm_number)
        TextView itemListviewCharmNumber;
        @BindView(R.id.item_listview_charm_icon)
        ImageView itemListviewCharmIcon;
        @BindView(R.id.item_listview_charm_vip)
        ImageView itemListviewCharmVip;
        @BindView(R.id.item_listview_charm_name)
        TextView itemListviewCharmName;
        @BindView(R.id.item_listview_charm_shiming)
        ImageView itemListviewCharmShiming;
        @BindView(R.id.item_listview_charm_Sex)
        TextView itemListviewCharmSex;
        @BindView(R.id.item_listview_charm_role)
        TextView itemListviewCharmRole;
        @BindView(R.id.item_listview_charm_meili)
        TextView itemListviewCharmMeili;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
