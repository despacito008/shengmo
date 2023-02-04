package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.PunishmentBean;
import com.aiwujie.shengmo.utils.DateUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.OnSimpleItemViewListener;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/5/11.
 */

public class PunishmentAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<PunishmentBean.DataBean> list;
    private boolean isShowFlag;

    public PunishmentAdapter(Context context, List<PunishmentBean.DataBean> list) {
        this.context = context;
        this.list = list;
        this.isShowFlag = SharedPreferencesUtils.getParam(context, "admin", "0").equals("1");
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_punishment_publicity, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PunishmentBean.DataBean data = list.get(position);
//          判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), "-1", data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), holder.itemListviewWarningVip);
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
        if (TextUtil.isEmpty(data.getComnum()) || "0".equals(data.getComnum())) {
            holder.tvPunishmentComment.setText("去评论");
        } else {
            holder.tvPunishmentComment.setText(data.getComnum());
        }
        if (!data.getHead_pic().equals(NetPic()) || !data.getHead_pic().equals("")) {//"http://59.110.28.150:888/"
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewWarningIcon, 0);
        } else {
            holder.itemListviewWarningIcon.setImageResource(R.mipmap.morentouxiang);
        }
        holder.itemListviewWarningName.setText(data.getNickname());
        if (!data.getCharm_val_new().equals("0")) {
            holder.llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
            holder.tvLayoutUserNormalInfoCharm.setText(data.getCharm_val_new());
        } else {
            holder.llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
        }

        if (!data.getWealth_val_new().equals("0")) {
            holder.llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
            holder.tvLayoutUserNormalInfoWealth.setText(data.getWealth_val_new());
        } else {
            holder.llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
        }

        if(data.getOnlinestate() == 1) {
            holder.ivItemOnline.setVisibility(View.VISIBLE);
        } else {
            holder.ivItemOnline.setVisibility(View.INVISIBLE);
        }

        switch (data.getType()) {
            case "1":
                holder.tvPunishmentType.setText("已禁账号");
                break;
            case "2":
                holder.tvPunishmentType.setText("已禁动态");
                break;
            case "3":
                holder.tvPunishmentType.setText("已禁聊天");
                break;
            case "4":
                holder.tvPunishmentType.setText("已禁资料");
                break;
            case "66":
                holder.tvPunishmentType.setText("禁看直播");
                break;
            default:
                holder.tvPunishmentType.setText("已禁设备");
                break;
        }

        switch (data.getBlockingalong()) {
            case "1":
                holder.tvPunishmentTime.setText("1天");
                break;
            case "3":
                holder.tvPunishmentTime.setText("3天");
                break;
            case "7":
                holder.tvPunishmentTime.setText("1周");
                break;
            case "14":
                holder.tvPunishmentTime.setText("2周");
                break;
            case "30":
            case "31":
                holder.tvPunishmentTime.setText("1月");
                break;
            default:
                holder.tvPunishmentTime.setText("永久");
                break;
        }
        try {
            holder.tvPunishmentDate.setText(DateUtils.getParseTime(Long.parseLong(data.getAddtime())));
        } catch (Exception e) {

        }
        holder.tvPunishmentReason.setText(data.getBlockreason());
        final List<String> images = data.getImage();

        if (images == null || images.size() == 0) {
            holder.rvPunishmentImg.setVisibility(View.GONE);
        } else {
            holder.rvPunishmentImg.setVisibility(View.VISIBLE);
            PunishmentImgAdapter punishmentImgAdapter = new PunishmentImgAdapter(images, context);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 5);
            holder.rvPunishmentImg.setLayoutManager(gridLayoutManager);
            holder.rvPunishmentImg.setAdapter(punishmentImgAdapter);
            punishmentImgAdapter.setOnSimpleItemListener(new OnSimpleItemViewListener() {
                @Override
                public void onItemListener(View view) {
                    int index = holder.rvPunishmentImg.getChildAdapterPosition(view);
                    List<ImageInfo> imageInfo = new ArrayList<>();
                    for (int i = 0; i < images.size(); i++) {
                        ImageInfo info = new ImageInfo();
                        info.setThumbnailUrl(images.get(i));
                        info.setBigImageUrl(images.get(i));
                        imageInfo.add(info);
                    }
                    Intent intent = new Intent(context, ImagePreviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
                    bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

        if (isShowFlag) {
            holder.tvPunishmentEdit.setVisibility(View.VISIBLE);
        } else {
            holder.tvPunishmentEdit.setVisibility(View.GONE);
        }

        holder.tvPunishmentEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPunishmentListener != null) {
                    onPunishmentListener.onItemEdit(position);
                }
            }
        });

        holder.llItemPunishment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPunishmentListener != null) {
                    onPunishmentListener.onItemClick(position);
                }
            }
        });


        return convertView;
    }




    public interface OnPunishmentListener {
        void onItemEdit(int position);

        void onItemClick(int position);
    }

    OnPunishmentListener onPunishmentListener;

    public void setOnPunishmentListener(OnPunishmentListener onPunishmentListener) {
        this.onPunishmentListener = onPunishmentListener;
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
        @BindView(R.id.tv_punishment_type)
        TextView tvPunishmentType;
        @BindView(R.id.tv_punishment_time)
        TextView tvPunishmentTime;
        @BindView(R.id.item_listview_warning_ban_dynamic)
        LinearLayout itemListviewWarningBanDynamic;
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
        @BindView(R.id.tv_punishment_date)
        TextView tvPunishmentDate;
        @BindView(R.id.tv_punishment_edit)
        TextView tvPunishmentEdit;
        @BindView(R.id.tv_punishment_reason)
        TextView tvPunishmentReason;
        @BindView(R.id.rv_punishment_img)
        RecyclerView rvPunishmentImg;
        @BindView(R.id.ll_item_punishment)
        LinearLayout llItemPunishment;
        @BindView(R.id.tv_punishment_common)
        TextView tvPunishmentComment;
        @BindView(R.id.item_listview_warning_online)
        ImageView ivItemOnline;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
