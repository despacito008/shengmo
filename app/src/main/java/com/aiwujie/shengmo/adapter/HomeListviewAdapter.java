package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class HomeListviewAdapter extends BaseAdapter {
    private Context context;
    private List<HomeNewListData.DataBean> list;
    private LayoutInflater inflater;
    private int retcode;
    //isVisivleTime为0的时候不显示右上角时间
    private int isVisivleTime;
    private Handler handler = new Handler();

    public HomeListviewAdapter(Context context, List<HomeNewListData.DataBean> list, int retcode, int isVisivleTime) {
        super();
        this.context = context;
        this.list = list;
        this.retcode = retcode;
        this.isVisivleTime = isVisivleTime;
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
            convertView = inflater.inflate(R.layout.item_near_new_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeNewListData.DataBean data = list.get(position);
        if (TextUtil.isEmpty(data.getHead_pic()) || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemHeadIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemHeadIcon, 0);
        }


        if (isVisivleTime != 0) {

            if ("隐身".equals(data.getLast_login_time())) {
                holder.ivNoLastTime.setVisibility(View.VISIBLE);
                holder.tvLastTime.setVisibility(View.GONE);
            } else {
                holder.ivNoLastTime.setVisibility(View.GONE);
                holder.tvLastTime.setVisibility(View.VISIBLE);
                holder.tvLastTime.setText(data.getLast_login_time());
            }

        } else {
            holder.ivNoLastTime.setVisibility(View.GONE);
            holder.tvLastTime.setVisibility(View.GONE);
        }

        if ("1".equals(data.getLogin_time_switch())) {
            holder.tvLastTime.setText("隐身");
        }

        if (retcode == 2000 && !data.getLat().equals("0.000000") && !data.getLng().equals("0.000000") && !"隐身".equals(data.getDistance())) {
            holder.tvDistance.setText(data.getDistance());
            holder.tvDistance.setVisibility(View.VISIBLE);
            holder.ivNoDistance.setVisibility(View.GONE);
        } else {
            holder.ivNoDistance.setVisibility(View.VISIBLE);
            holder.tvDistance.setVisibility(View.GONE);
        }

        if (data.getOnlinestate() == 0) {
            holder.viewOnlineState.setVisibility(View.GONE);
        } else {
            holder.viewOnlineState.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), holder.itemIdentityIcon);


        //是否实名认证
        if ("1".equals(data.getRealids())) {
            holder.ivMaterial.setVisibility(View.VISIBLE);
        } else {
            holder.ivMaterial.setVisibility(View.GONE);
        }
        //是否自拍认证
        if ("1".equals(data.getRealname())) {
            holder.ivSelfie.setVisibility(View.VISIBLE);
        } else {
            holder.ivSelfie.setVisibility(View.GONE);
        }
        //短视频认证
        if ("1".equals(data.getVideo_auth_status())) {
            holder.ivVideo.setVisibility(View.VISIBLE);
        } else {
            holder.ivVideo.setVisibility(View.GONE);
        }
        holder.tvLayoutUserNormalInfoAge.setText(data.getAge());

        if (data.getSex().equals("1")) {
            holder.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            holder.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.tvItemSex.setCompoundDrawables(drawable, null, null, null);
        } else if ("2".equals(data.getSex())) {
            holder.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            holder.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.tvItemSex.setCompoundDrawables(drawable, null, null, null);
        } else if ("3".equals(data.getSex())) {
            holder.llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            holder.ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            holder.tvItemSex.setCompoundDrawables(drawable, null, null, null);
        }
        if ("S".equals(data.getRole())) {
            holder.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            holder.tvLayoutUserNormalInfoRole.setText("斯");
        } else if ("M".equals(data.getRole())) {
            holder.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            holder.tvLayoutUserNormalInfoRole.setText("慕");
        } else if ("SM".equals(data.getRole())) {
            holder.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            holder.tvLayoutUserNormalInfoRole.setText("双");
        } else {
            holder.tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_other);
            holder.tvLayoutUserNormalInfoRole.setText(data.getRole());
        }
        holder.tvName.setText(data.getNickname());

        if ("1".equals(data.getLocation_city_switch())) {
            holder.tvAddress.setVisibility(View.GONE);
            holder.ivNoLocation.setVisibility(View.VISIBLE);
        } else {
            if (TextUtil.isEmpty(data.getCity()) && TextUtil.isEmpty(data.getProvince())) {
                holder.tvAddress.setVisibility(View.GONE);
                holder.ivNoLocation.setVisibility(View.VISIBLE);
            } else {
                holder.tvAddress.setVisibility(View.VISIBLE);
                holder.ivNoLocation.setVisibility(View.GONE);
                if (data.getProvince().equals(data.getCity())) {
                    holder.tvAddress.setText(data.getCity());
                } else {
                    holder.tvAddress.setText(data.getProvince() + " " + data.getCity());
                }
            }
        }


        if (!"0".equals(data.getCharm_val())) {
            holder.llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
            holder.tvLayoutUserNormalInfoCharm.setText(data.getCharm_val());
        } else {
            holder.llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
        }

        if (!"0".equals(data.getWealth_val())) {
            holder.llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
            holder.tvLayoutUserNormalInfoWealth.setText(data.getWealth_val());
        } else {
            holder.llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
        }

        if (TextUtil.isEmpty(data.getUser_level()) || "0".equals(data.getUser_level())) {
            holder.clAudienceLevel.setVisibility(View.GONE);
        } else {
            holder.clAudienceLevel.setVisibility(View.VISIBLE);
            holder.tvAudienceLevel.setText(data.getUser_level());
        }
        if (TextUtil.isEmpty(data.getAnchor_level()) || "0".equals(data.getAnchor_level())) {
            holder.clAnchorLevel.setVisibility(View.GONE);
        } else {
            holder.clAnchorLevel.setVisibility(View.VISIBLE);
            holder.tvAnchorLevel.setText(data.getAnchor_level());
        }

        return convertView;
    }


    static
    class ViewHolder {
        @BindView(R.id.item_head_icon)
        ImageView itemHeadIcon;
        @BindView(R.id.item_identity_icon)
        ImageView itemIdentityIcon;
        @BindView(R.id.view_online_state)
        View viewOnlineState;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_selfie)
        ImageView ivSelfie;
        @BindView(R.id.iv_video)
        ImageView ivVideo;
        @BindView(R.id.iv_material)
        ImageView ivMaterial;
        @BindView(R.id.tv_distance)
        TextView tvDistance;
        @BindView(R.id.iv_no_distance)
        ImageView ivNoDistance;
        @BindView(R.id.tv_last_time)
        TextView tvLastTime;
        @BindView(R.id.iv_no_last_time)
        ImageView ivNoLastTime;
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
        @BindView(R.id.iv_no_location)
        ImageView ivNoLocation;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.constraint_layout_anchor_level)
        ConstraintLayout clAnchorLevel;
        @BindView(R.id.tv_live_anchor_level)
        TextView tvAnchorLevel;
        @BindView(R.id.constraint_layout_audience_level)
        ConstraintLayout clAudienceLevel;
        @BindView(R.id.tv_live_audience_level)
        TextView tvAudienceLevel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


