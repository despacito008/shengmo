package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.bumptech.glide.Glide;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class HomePageUserGridAdapter extends RecyclerView.Adapter<HomePageUserGridAdapter.HomePageUserHolder> {
    Context context;
    ArrayList<HomeNewListData.DataBean> userList;


    public HomePageUserGridAdapter(Context context, ArrayList<HomeNewListData.DataBean> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public HomePageUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_item_homepage_user_grid, parent, false);
        return new HomePageUserHolder(view);
    }

    @Override
    public void onBindViewHolder(HomePageUserHolder holder, int position) {
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class HomePageUserHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_homepage_user_icon)
        ImageView ivItemHomepageUserIcon;
        @BindView(R.id.view_item_homepage_user_online)
        View viewItemHomepageUserOnline;
        @BindView(R.id.iv_item_homepage_user_level)
        ImageView ivItemHomepageUserLevel;
        @BindView(R.id.tv_item_homepage_user_city)
        TextView tvItemHomepageUserCity;
        @BindView(R.id.iv_layout_user_normal_info_sex)
        ImageView ivLayoutUserNormalInfoSex;
        @BindView(R.id.tv_layout_user_normal_info_age)
        TextView tvLayoutUserNormalInfoAge;
        @BindView(R.id.ll_layout_user_normal_info_sex_age)
        LinearLayout llLayoutUserNormalInfoSexAge;

        public HomePageUserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void display(int index) {
            HomeNewListData.DataBean data = userList.get(index);
            if (TextUtil.isEmpty(data.getHead_pic()) || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
                ivItemHomepageUserIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                ImageLoader.loadImage(context, data.getHead_pic(),ivItemHomepageUserIcon,R.mipmap.morentouxiang);
            }

//            if (isVisivleTime != 0) {
//                if ("隐身".equals(data.getLast_login_time())) {
//                    ivItemUserNormalInfoNoTime.setVisibility(View.VISIBLE);
//                    tvItemUserNormalInfoTime.setVisibility(View.GONE);
//                } else {
//                    ivItemUserNormalInfoNoTime.setVisibility(View.GONE);
//                    tvItemUserNormalInfoTime.setVisibility(View.VISIBLE);
//                    tvItemUserNormalInfoTime.setText(data.getLast_login_time());
//                }
//
//            } else {
//                ivItemUserNormalInfoNoTime.setVisibility(View.GONE);
//                tvItemUserNormalInfoTime.setVisibility(View.GONE);
//            }
//
//            if ("1".equals(data.getLogin_time_switch())) {
//                tvItemUserNormalInfoTime.setText("隐身");
//            }


            if (data.getOnlinestate() == 0) {
                viewItemHomepageUserOnline.setVisibility(View.GONE);
            } else {
                viewItemHomepageUserOnline.setVisibility(View.VISIBLE);
            }
            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), ivItemHomepageUserLevel);


            tvLayoutUserNormalInfoAge.setText(data.getAge());

            if (data.getSex().equals("1")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tvItemSex.setCompoundDrawables(drawable, null, null, null);
            } else if ("2".equals(data.getSex())) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tvItemSex.setCompoundDrawables(drawable, null, null, null);
            } else if ("3".equals(data.getSex())) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
//            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tvItemSex.setCompoundDrawables(drawable, null, null, null);
            }


            if ("1".equals(data.getLocation_city_switch())) {
                //tvLayoutUserNormalInfoCity.setVisibility(View.INVISIBLE);
                //ivItemUserNormalInfoNoAddress.setVisibility(View.VISIBLE);
                tvItemHomepageUserCity.setText("未知");
            } else {
                if (TextUtil.isEmpty(data.getCity()) && TextUtil.isEmpty(data.getProvince())) {
                    //tvLayoutUserNormalInfoCity.setVisibility(View.INVISIBLE);
                    //ivItemUserNormalInfoNoAddress.setVisibility(View.VISIBLE);
                    tvItemHomepageUserCity.setText("未知");
                } else {
                    tvItemHomepageUserCity.setVisibility(View.VISIBLE);
                    tvItemHomepageUserCity.setText(data.getCity());
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSimpleItemListener != null) {
                        onSimpleItemListener.onItemListener(index);
                    }
                }
            });
        }
    }

    OnSimpleItemListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }
}
