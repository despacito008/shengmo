package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.GrouplistActivity;
import com.aiwujie.shengmo.bean.GzFsHyListviewData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class InviteFollow2Adapter extends BaseAdapter {
    private Context context;
    private List<GzFsHyListviewData.DataBean> list;
    private LayoutInflater inflater;
    Handler handler = new Handler();
    private int retcode;
    List<String> inviteUids;

    public InviteFollow2Adapter(Context context, List<GzFsHyListviewData.DataBean> list,List<String> inviteUids) {
        super();
        this.inviteUids=inviteUids;
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
            convertView = inflater.inflate(R.layout.item_listview_invite_follow, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GzFsHyListviewData.DataBean data = list.get(position);
//        if (data.getUserInfo().getHead_pic().equals("") || data.getUserInfo().getHead_pic().equals("http://59.110.28.150:888/")) {
        if (data.getUserInfo().getHead_pic().equals("") || data.getUserInfo().getHead_pic().equals(NetPic())) {
            holder.itemListviewInviteFollowIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getUserInfo().getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewInviteFollowIcon, 0);
        }
        holder.itemListviewInviteFollowSex.setText(data.getUserInfo().getAge());
        if (data.getUserInfo().getOnlinestate() == 0) {
            holder.itemListviewInviteFollowOnline.setVisibility(View.GONE);
        } else {
            holder.itemListviewInviteFollowOnline.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context,data.getUserInfo().getHead_pic(), data.getUid(), data.getUserInfo().getIs_volunteer(), data.getUserInfo().getIs_admin(), data.getUserInfo().getSvipannual(), data.getUserInfo().getSvip(), data.getUserInfo().getVipannual(), data.getUserInfo().getVip(),data.getUserInfo().getBkvip(),data.getUserInfo().getBlvip(), holder.itemListviewInviteFollowVip);
    /*    if (data.getUserInfo().getIs_hand().equals("0")) {
            holder.itemListviewInviteFollowHongiang.setVisibility(View.INVISIBLE);
        } else {
            holder.itemListviewInviteFollowHongiang.setVisibility(View.VISIBLE);
        }*/
        if (data.getUserInfo().getRealname().equals("0")) {
            holder.itemListviewInviteFollowShiming.setVisibility(View.GONE);
        } else {
            holder.itemListviewInviteFollowShiming.setVisibility(View.VISIBLE);
        }

        if (data.getUserInfo().getSex().equals("1")) {
            holder.itemListviewInviteFollowSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewInviteFollowSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getUserInfo().getSex().equals("2")) {
            holder.itemListviewInviteFollowSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewInviteFollowSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getUserInfo().getSex().equals("3")) {
            holder.itemListviewInviteFollowSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewInviteFollowSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getUserInfo().getRole().equals("S")) {
            holder.itemListviewInviteFollowRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemListviewInviteFollowRole.setText("斯");
        } else if (data.getUserInfo().getRole().equals("M")) {
            holder.itemListviewInviteFollowRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemListviewInviteFollowRole.setText("慕");
        } else if (data.getUserInfo().getRole().equals("SM")) {
            holder.itemListviewInviteFollowRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemListviewInviteFollowRole.setText("双");
        } else if (data.getUserInfo().getRole().equals("~")) {
            holder.itemListviewInviteFollowRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemListviewInviteFollowRole.setText("~");
        }
        holder.itemListviewInviteFollowName.setText(data.getUserInfo().getNickname());
        if (data.getUserInfo().getLocation_city_switch().equals("1")){
            holder.itemListviewInviteFollowCity.setText("隐身");
        }else {
            if (!data.getUserInfo().getCity().equals("")) {
                holder.itemListviewInviteFollowCity.setText(data.getUserInfo().getCity());
            } else {
                holder.itemListviewInviteFollowCity.setText("(用户已隐藏位置)");
            }
        }


        if (data.isIscheck()){
            holder.itemListviewInviteFollowFlag.setImageResource(R.mipmap.atxuanzhong);
        }else {
            holder.itemListviewInviteFollowFlag.setImageResource(R.mipmap.atweixuanzhong);
        }
        if (inviteUids.contains(data.getUid())){
            data.setIscheck(true);
            holder.itemListviewInviteFollowFlag.setImageResource(R.mipmap.atxuanzhong);
        }else {
            data.setIscheck(false);
            holder.itemListviewInviteFollowFlag.setImageResource(R.mipmap.atweixuanzhong);
        }

        if (GrouplistActivity.yiyouhaoyou.contains(data.getUid())){
            holder.autoLinearLayout.setBackgroundResource(R.drawable.item_click_bg_selector2);
        }else {
            holder.autoLinearLayout.setBackgroundResource(R.drawable.item_click_bg_selector);
        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_listview_invite_follow_flag)
        ImageView itemListviewInviteFollowFlag;
        @BindView(R.id.item_listview_invite_follow_icon)
        ImageView itemListviewInviteFollowIcon;
        @BindView(R.id.item_listview_invite_follow_hongiang)
        ImageView itemListviewInviteFollowHongiang;
        @BindView(R.id.item_listview_invite_follow_vip)
        ImageView itemListviewInviteFollowVip;
        @BindView(R.id.item_listview_invite_follow_name)
        TextView itemListviewInviteFollowName;
        @BindView(R.id.item_listview_invite_follow_shiming)
        ImageView itemListviewInviteFollowShiming;
        @BindView(R.id.item_listview_invite_follow_online)
        ImageView itemListviewInviteFollowOnline;
        @BindView(R.id.item_listview_invite_follow_Sex)
        TextView itemListviewInviteFollowSex;
        @BindView(R.id.item_listview_invite_follow_role)
        TextView itemListviewInviteFollowRole;
        @BindView(R.id.item_listview_invite_follow_city)
        TextView itemListviewInviteFollowCity;
        @BindView(R.id.item_listview_invite_follow_time)
        TextView itemListviewInviteFollowTime;
        @BindView(R.id.auto_auto)
        AutoLinearLayout autoLinearLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


