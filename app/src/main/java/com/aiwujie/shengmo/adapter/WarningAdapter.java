package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.WarningListData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/5/11.
 */

public class WarningAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<WarningListData.DataBean> list;
    private boolean isShowFlag;

    public WarningAdapter(Context context, List<WarningListData.DataBean> list, boolean isShowFlag) {
        this.context = context;
        this.list = list;
        this.isShowFlag = isShowFlag;
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
            convertView = inflater.inflate(R.layout.item_listview_warning, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WarningListData.DataBean data = list.get(position);
//          判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), holder.itemListviewWarningVip);
        if (data.getRealname().equals("0")) {
            holder.itemListviewWarningShiming.setVisibility(View.GONE);
        } else {
            holder.itemListviewWarningShiming.setVisibility(View.VISIBLE);
        }

        if (data.getSex().equals("1")) {
            holder.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
        } else if (data.getSex().equals("2")) {
            holder.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
        } else if (data.getSex().equals("3")) {
            holder.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
        }
        if (data.getRole().equals("S")) {
            holder.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.tvLayoutUserNormalInfoRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.tvLayoutUserNormalInfoRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.tvLayoutUserNormalInfoRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.tvLayoutUserNormalInfoRole.setText("~");
        }
        holder.tvLayoutUserNormalInfoAge.setText(data.getAge());

        if (!data.getHead_pic().equals(NetPic()) || !data.getHead_pic().equals("")) {//"http://59.110.28.150:888/"
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewWarningIcon, 0);
        } else {
            holder.itemListviewWarningIcon.setImageResource(R.mipmap.morentouxiang);
        }
        holder.itemListviewWarningName.setText(data.getNickname());
        if (!data.getCharm_val().equals("0")) {
            holder.llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
            holder.tvLayoutUserNormalInfoCharm.setText(data.getCharm_val());
        } else {
            holder.llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
        }

        if (!data.getWealth_val().equals("0")) {
            holder.llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
            holder.tvLayoutUserNormalInfoWealth.setText(data.getWealth_val());
        } else {
            holder.llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
        }

        if (data.getOnlinestate() == 1) {
            holder.ivItemOnline.setVisibility(View.VISIBLE);
        } else {
            holder.ivItemOnline.setVisibility(View.GONE);
        }

        if (isShowFlag) {
            if (data.getDynamicstatus().equals("0")) {
                holder.itemListviewWarningBanDynamic.setVisibility(View.VISIBLE);
                holder.tvItemBanDynamic.setText("已禁动态-"+getBanTime(data.getDynamic_blockingalong()));
            } else {
                holder.itemListviewWarningBanDynamic.setVisibility(View.GONE);
            }
            if (data.getInfostatus().equals("0")) {
                holder.itemListviewWarningBanInfo.setVisibility(View.VISIBLE);
                holder.tvItemBanInfo.setText("已禁资料-"+getBanTime(data.getInfo_blockingalong()));
            } else {
                holder.itemListviewWarningBanInfo.setVisibility(View.GONE);
            }
            if (data.getChatstatus().equals("0")) {
                holder.itemListviewWarningBanChat.setVisibility(View.VISIBLE);
                holder.tvItemBanChat.setText("已禁聊天-"+getBanTime(data.getChat_blockingalong()));
            } else {
                holder.itemListviewWarningBanChat.setVisibility(View.GONE);
            }
//            if (data.getStatus().equals("0")) {
//                holder.itemListviewWarningBanUser.setVisibility(View.GONE);
//                //holder.tvItemBanAccount.setText("已禁账号-"+getBanTime(data.getChat_blockingalong()));
//            } else {
//                holder.itemListviewWarningBanUser.setVisibility(View.GONE);
//            }
//            if (data.getDevicestatus().equals("0")) {
//                holder.itemListviewWarningBanDevice.setVisibility(View.GONE);
//                //holder.tvItemBanDevice.setText("已禁设备-"+getBanTime(data.getDevice_blockingalong()));
//            } else {
//                holder.itemListviewWarningBanDevice.setVisibility(View.GONE);
//            }

            if ("1".equals(data.getProhibition_status())) {
                holder.llBanLive.setVisibility(View.VISIBLE);
                holder.tvBanLive.setText("已禁直播-" + getBanTime(data.getLive_chat_blockingalong()));
            } else {
                holder.llBanLive.setVisibility(View.GONE);
            }

            if ("0".equals(data.getLivestatus())) {
                holder.llBanWatchLive.setVisibility(View.VISIBLE);
                holder.tvBanWatchLive.setText("禁看直播-" + getBanTime(data.getWatchlive_blockingalong()));
            } else {
                holder.llBanWatchLive.setVisibility(View.GONE);
            }

        }

        holder.tvItemBanTime.setText("" + data.getHandletime());
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_listview_warning_icon)
        ImageView itemListviewWarningIcon;
        @BindView(R.id.item_listview_warning_vip)
        ImageView itemListviewWarningVip;
        @BindView(R.id.item_listview_warning_name)
        TextView itemListviewWarningName;
        @BindView(R.id.item_listview_warning_shiming)
        ImageView itemListviewWarningShiming;
        @BindView(R.id.iv_layout_user_normal_info_sex)
        ImageView ivLayoutUserNormalInfoSex;
        @BindView(R.id.tv_layout_user_normal_info_age)
        TextView tvLayoutUserNormalInfoAge;
        @BindView(R.id.ll_layout_user_normal_info_sex_age)
        LinearLayout llLayoutUserNormalInfoSexAge;
        @BindView(R.id.tv_layout_user_normal_info_role)
        TextView tvLayoutUserNormalInfoRole;
        @BindView(R.id.iv_layout_user_normal_info_wealth)
        ImageView ivLayoutUserNormalInfoWealth;
        @BindView(R.id.tv_layout_user_normal_info_wealth)
        TextView tvLayoutUserNormalInfoWealth;
        @BindView(R.id.ll_layout_user_normal_info_wealth)
        LinearLayout llLayoutUserNormalInfoWealth;
        @BindView(R.id.iv_layout_user_normal_info_charm)
        ImageView ivLayoutUserNormalInfoCharm;
        @BindView(R.id.tv_layout_user_normal_info_charm)
        TextView tvLayoutUserNormalInfoCharm;
        @BindView(R.id.ll_layout_user_normal_info_charm)
        LinearLayout llLayoutUserNormalInfoCharm;
        @BindView(R.id.item_listview_warning_ban_dynamic)
        AutoLinearLayout itemListviewWarningBanDynamic;
        @BindView(R.id.item_listview_warning_ban_info)
        AutoLinearLayout itemListviewWarningBanInfo;
        @BindView(R.id.item_listview_warning_ban_chat)
        AutoLinearLayout itemListviewWarningBanChat;
        @BindView(R.id.item_listview_warning_ban_user)
        AutoLinearLayout itemListviewWarningBanUser;
        @BindView(R.id.item_listview_warning_ban_device)
        AutoLinearLayout itemListviewWarningBanDevice;
        @BindView(R.id.item_listview_warning_online)
        ImageView ivItemOnline;
        @BindView(R.id.tv_item_ban_dynamic)
        TextView tvItemBanDynamic;
        @BindView(R.id.tv_item_ban_info)
        TextView tvItemBanInfo;
        @BindView(R.id.tv_item_ban_chat)
        TextView tvItemBanChat;
        @BindView(R.id.tv_item_ban_account)
        TextView tvItemBanAccount;
        @BindView(R.id.tv_item_ban_device)
        TextView tvItemBanDevice;
        @BindView(R.id.tv_user_info_ban_time)
        TextView tvItemBanTime;
        @BindView(R.id.item_listview_warning_ban_watch_live)
        LinearLayout llBanWatchLive;
        @BindView(R.id.item_listview_warning_ban_live)
        LinearLayout llBanLive;
        @BindView(R.id.tv_item_ban_live)
        TextView tvBanLive;
        @BindView(R.id.tv_item_ban_watch_live)
        TextView tvBanWatchLive;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private String getBanTime(String along) {
        switch (along) {
            case "1":
                return "1天";
            case "3":
                return("3天");
            case "7":
                return("1周");
            case "14":
                return("2周");
            case "30":
            case "31":
                return("1月");
            default:
                return("永久");
        }
    }
}
