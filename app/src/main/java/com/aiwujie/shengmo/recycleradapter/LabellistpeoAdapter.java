package com.aiwujie.shengmo.recycleradapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;


public class LabellistpeoAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<HomeNewListData.DataBean> mEntityList;

    public LabellistpeoAdapter(Context context, List<HomeNewListData.DataBean> entityList){
        this.mContext = context;
        this.mEntityList = entityList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_near_listview, parent, false);
        return new DemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final HomeNewListData.DataBean data = mEntityList.get(position);
        if (TextUtil.isEmpty(data.getHead_pic()) || NetPic().equals(data.getHead_pic())) {//"http://59.110.28.150:888/"
            ((DemoViewHolder)holder).itemNearListviewIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(mContext, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ((DemoViewHolder)holder).itemNearListviewIcon, 0);
        }

            ((DemoViewHolder)holder).itemNearListviewTime.setVisibility(View.GONE);
            ((DemoViewHolder)holder).itemNearListviewDistance.setVisibility(View.VISIBLE);
            ((DemoViewHolder)holder).itemNearListviewDistance.setText(data.getDistance() + "");
            if ("0.000000".equals(data.getLat()) || "0.000000".equals(data.getLng())) {
                ((DemoViewHolder)holder).itemNearListviewDistance.setVisibility(View.INVISIBLE);
            } else {
                ((DemoViewHolder)holder).itemNearListviewDistance.setVisibility(View.VISIBLE);
            }

        ((DemoViewHolder)holder).itemNearListviewSex.setText(data.getAge());
        if (data.getOnlinestate() == 0) {
            ((DemoViewHolder)holder).itemNearListviewOnline.setVisibility(View.GONE);
        } else {
            ((DemoViewHolder)holder).itemNearListviewOnline.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(mContext, data.getHead_pic(),data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(),data.getBkvip(),data.getBlvip(), ((DemoViewHolder)holder).itemNearListviewVip);

        if ("0".equals(data.getIs_hand())) {
            ((DemoViewHolder)holder).itemNearListviewHongniang.setVisibility(View.INVISIBLE);
        } else {
            ((DemoViewHolder)holder).itemNearListviewHongniang.setVisibility(View.VISIBLE);
        }
        if ("0".equals(data.getRealname())) {
            ((DemoViewHolder)holder).itemNearListviewShiming.setVisibility(View.GONE);
        } else {
            ((DemoViewHolder)holder).itemNearListviewShiming.setVisibility(View.VISIBLE);
        }

        if ("1".equals(data.getSex())) {
            ((DemoViewHolder)holder).itemNearListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((DemoViewHolder)holder).itemNearListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if ("2".equals(data.getSex())) {
            ((DemoViewHolder)holder).itemNearListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((DemoViewHolder)holder).itemNearListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if ("3".equals(data.getSex())) {
            ((DemoViewHolder)holder).itemNearListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((DemoViewHolder)holder).itemNearListviewSex.setCompoundDrawables(drawable, null, null, null);
        }
        if ("S".equals(data.getRole())) {
            ((DemoViewHolder)holder).itemNearListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            ((DemoViewHolder)holder).itemNearListviewRole.setText("斯");
        } else if ("M".equals(data.getRole())) {
            ((DemoViewHolder)holder).itemNearListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            ((DemoViewHolder)holder).itemNearListviewRole.setText("慕");
        } else if ("SM".equals(data.getRole())) {
            ((DemoViewHolder)holder).itemNearListviewRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            ((DemoViewHolder)holder).itemNearListviewRole.setText("双");
        } else if ("~".equals(data.getRole())) {
            ((DemoViewHolder)holder).itemNearListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            ((DemoViewHolder)holder).itemNearListviewRole.setText("~");
        }
        ((DemoViewHolder)holder).itemNearListviewName.setText(data.getNickname());

        if ("1".equals(data.getLocation_city_switch())){
            ((DemoViewHolder)holder).itemNearListviewSign.setText("隐身");
        }else {
            if (TextUtil.isEmpty(data.getCity()) &&TextUtil.isEmpty( data.getProvince())) {
                ((DemoViewHolder)holder).itemNearListviewSign.setText("(用户已隐藏位置)");
            } else {
                if (data.getProvince().equals(data.getCity())) {
                    ((DemoViewHolder)holder).itemNearListviewSign.setText(data.getCity());
                } else {
                    ((DemoViewHolder)holder).itemNearListviewSign.setText(data.getProvince() + " " + data.getCity());
                }
            }
        }



        if (!"0".equals(data.getCharm_val())) {
            ((DemoViewHolder)holder).itemNearListviewLlBeautyCount.setVisibility(View.VISIBLE);
            ((DemoViewHolder)holder).itemNearListviewBeautyCount.setText(data.getCharm_val());
        } else {
            ((DemoViewHolder)holder).itemNearListviewLlBeautyCount.setVisibility(View.GONE);
        }

        if (!"0".equals(data.getWealth_val())) {
            ((DemoViewHolder)holder).itemNearListviewLlRichCount.setVisibility(View.VISIBLE);
            ((DemoViewHolder)holder).itemNearListviewRichCount.setText(data.getWealth_val());
        } else {
            ((DemoViewHolder)holder).itemNearListviewLlRichCount.setVisibility(View.GONE);
        }

        ((DemoViewHolder)holder).itemNearListviewShiming.setTag(position);
        ((DemoViewHolder)holder).itemNearListviewShiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(mContext, PhotoRzActivity.class);
                mContext.startActivity(intent);
            }
        });

        ((DemoViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PesonInfoActivity.class);
                intent.putExtra("uid", data.getUid());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mEntityList.size();
    }

    private class DemoViewHolder extends RecyclerView.ViewHolder{

        ImageView itemNearListviewIcon;
        ImageView itemNearListviewVip;
        ImageView itemNearListviewHongniang;
        TextView itemNearListviewFlag;
        TextView itemNearListviewName;
        ImageView itemNearListviewShiming;
        ImageView itemNearListviewOnline;
        TextView itemNearListviewDistance;
        TextView itemNearListviewTime;
        TextView itemNearListviewSex;
        TextView itemNearListviewRole;
        TextView itemNearListviewRichCount;
        AutoLinearLayout itemNearListviewLlRichCount;
        TextView itemNearListviewBeautyCount;
        AutoLinearLayout itemNearListviewLlBeautyCount;
        ImageView itemNearListviewBanSomeOne;
        TextView itemNearListviewSign;

        public DemoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemNearListviewIcon = itemView.findViewById(R.id.item_near_listview_icon);
            itemNearListviewVip = itemView.findViewById(R.id.item_near_listview_vip);
            itemNearListviewHongniang = itemView.findViewById(R.id.item_near_listview_hongniang);
            itemNearListviewFlag = itemView.findViewById(R.id.item_near_listview_flag);
            itemNearListviewName = itemView.findViewById(R.id.item_near_listview_name);
            itemNearListviewShiming = itemView.findViewById(R.id.item_near_listview_shiming);
            itemNearListviewOnline = itemView.findViewById(R.id.item_near_listview_online);
            itemNearListviewDistance = itemView.findViewById(R.id.item_near_listview_distance);
            itemNearListviewTime = itemView.findViewById(R.id.item_near_listview_time);
            itemNearListviewSex = itemView.findViewById(R.id.item_near_listview_Sex);
            itemNearListviewRole = itemView.findViewById(R.id.item_near_listview_role);
            itemNearListviewRichCount = itemView.findViewById(R.id.item_near_listview_richCount);
            itemNearListviewLlRichCount = itemView.findViewById(R.id.item_near_listview_ll_richCount);
            itemNearListviewBeautyCount = itemView.findViewById(R.id.item_near_listview_beautyCount);
            itemNearListviewLlBeautyCount = itemView.findViewById(R.id.item_near_listview_ll_beautyCount);
            itemNearListviewBanSomeOne = itemView.findViewById(R.id.item_near_listview_banSomeOne);
            itemNearListviewSign = itemView.findViewById(R.id.item_near_listview_sign);
        }
    }
}