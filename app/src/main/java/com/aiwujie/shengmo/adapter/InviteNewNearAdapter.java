package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class InviteNewNearAdapter extends BaseAdapter {
    private Context context;
    private List<HomeNewListData.DataBean> list;
    private LayoutInflater inflater;
    private int retcode;

    public InviteNewNearAdapter(Context context, List<HomeNewListData.DataBean> list, int retcode) {
        super();
        this.context = context;
        this.list = list;
        this.retcode = retcode;
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
            convertView = inflater.inflate(R.layout.item_listview_invite_near, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeNewListData.DataBean data = list.get(position);
        if (data.getHead_pic().equals("")) {
            holder.itemListviewInviteNearIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewInviteNearIcon, 0);
        }
        if (retcode == 2000) {
            holder.itemListviewInviteNearDistance.setVisibility(View.VISIBLE);
            holder.itemListviewInviteNearDistance.setText(data.getDistance());
            if (data.getLat().equals("0.000000") || data.getLng().equals("0.000000")) {
                holder.itemListviewInviteNearDistance.setVisibility(View.INVISIBLE);
            } else {
                holder.itemListviewInviteNearDistance.setVisibility(View.VISIBLE);
            }
        } else {
            holder.itemListviewInviteNearDistance.setVisibility(View.INVISIBLE);
        }

        holder.itemListviewInviteNearSex.setText(data.getAge());
        if (data.getOnlinestate() == 0) {
            holder.itemListviewInviteNearOnline.setVisibility(View.GONE);
        } else {
            holder.itemListviewInviteNearOnline.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context,data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(),data.getBkvip(),data.getBlvip(), holder.itemListviewInviteNearVip);

        if (data.getIs_hand().equals("0")) {
            holder.itemListviewInviteNearHongniang.setVisibility(View.INVISIBLE);
        } else {
            holder.itemListviewInviteNearHongniang.setVisibility(View.VISIBLE);
        }
        if (data.getRealname().equals("0")) {
            holder.itemListviewInviteNearShiming.setVisibility(View.GONE);
        } else {
            holder.itemListviewInviteNearShiming.setVisibility(View.VISIBLE);
        }

        if (data.getSex().equals("1")) {
            holder.itemListviewInviteNearSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewInviteNearSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("2")) {
            holder.itemListviewInviteNearSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewInviteNearSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("3")) {
            holder.itemListviewInviteNearSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewInviteNearSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getRole().equals("S")) {
            holder.itemListviewInviteNearRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemListviewInviteNearRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemListviewInviteNearRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemListviewInviteNearRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemListviewInviteNearRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemListviewInviteNearRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.itemListviewInviteNearRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemListviewInviteNearRole.setText("~");
        }
        holder.itemListviewInviteNearName.setText(data.getNickname());

        if(data.getCity().equals("")&&data.getProvince().equals("")){
            holder.itemListviewInviteNearSign.setText("(用户已隐藏位置)");
        }else {
            if (!data.getCity().equals("") && data.getCity() != null) {
                holder.itemListviewInviteNearSign.setText(data.getCity());
            } else {
                holder.itemListviewInviteNearSign.setText(data.getProvince());
            }
        }

        if (((ListView) parent).isItemChecked(position + 1)) {
            holder.itemListviewInviteNearFlag.setImageResource(R.mipmap.atxuanzhong);
        } else {
            holder.itemListviewInviteNearFlag.setImageResource(R.mipmap.atweixuanzhong);
        }
        if (!data.getCharm_val().equals("0")) {
            holder.itemInviteListviewLlBeautyCount.setVisibility(View.VISIBLE);
            holder.itemInviteListviewBeautyCount.setText(data.getCharm_val());
        } else {
            holder.itemInviteListviewLlBeautyCount.setVisibility(View.GONE);
        }

        if (!data.getWealth_val().equals("0")) {
            holder.itemInviteListviewLlRichCount.setVisibility(View.VISIBLE);
            holder.itemInviteListviewRichCount.setText(data.getWealth_val());
        } else {
            holder.itemInviteListviewLlRichCount.setVisibility(View.GONE);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_listview_invite_near_flag)
        ImageView itemListviewInviteNearFlag;
        @BindView(R.id.item_listview_invite_near_icon)
        ImageView itemListviewInviteNearIcon;
        @BindView(R.id.item_listview_invite_near_vip)
        ImageView itemListviewInviteNearVip;
        @BindView(R.id.item_listview_invite_near_hongniang)
        ImageView itemListviewInviteNearHongniang;
        @BindView(R.id.item_listview_invite_near_name)
        TextView itemListviewInviteNearName;
        @BindView(R.id.item_listview_invite_near_shiming)
        ImageView itemListviewInviteNearShiming;
        @BindView(R.id.item_listview_invite_near_online)
        ImageView itemListviewInviteNearOnline;
        @BindView(R.id.item_listview_invite_near_distance)
        TextView itemListviewInviteNearDistance;
        @BindView(R.id.item_listview_invite_near_time)
        TextView itemListviewInviteNearTime;
        @BindView(R.id.item_listview_invite_near_Sex)
        TextView itemListviewInviteNearSex;
        @BindView(R.id.item_listview_invite_near_role)
        TextView itemListviewInviteNearRole;
        @BindView(R.id.item_invite_listview_richCount)
        TextView itemInviteListviewRichCount;
        @BindView(R.id.item_invite_listview_ll_richCount)
        AutoLinearLayout itemInviteListviewLlRichCount;
        @BindView(R.id.item_invite_listview_beautyCount)
        TextView itemInviteListviewBeautyCount;
        @BindView(R.id.item_invite_listview_ll_beautyCount)
        AutoLinearLayout itemInviteListviewLlBeautyCount;
        @BindView(R.id.item_listview_invite_near_sign)
        TextView itemListviewInviteNearSign;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


