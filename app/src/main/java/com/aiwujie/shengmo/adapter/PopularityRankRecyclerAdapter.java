package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.bean.RankBerewardData;
import com.aiwujie.shengmo.bean.RankPopularityData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class PopularityRankRecyclerAdapter extends RecyclerView.Adapter<PopularityRankRecyclerAdapter.PopularityRankHolder> {
    Context context;
    private List<RankPopularityData.DataBean> rankList;
    int type = 1;
    public PopularityRankRecyclerAdapter(Context context, List<RankPopularityData.DataBean> rankList) {
        this.context = context;
        this.rankList = rankList;
    }

    public PopularityRankRecyclerAdapter(Context context, List<RankPopularityData.DataBean> rankList,int type) {
        this.context = context;
        this.rankList = rankList;
        this.type = 2;
    }

    @Override
    public PopularityRankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popularity_ranking, parent, false);
        return new PopularityRankHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularityRankHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }

    class PopularityRankHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_popularity_ranking_index)
        TextView tvItemPopularityRankingIndex;
        @BindView(R.id.iv_item_popularity_ranking_icon)
        ImageView ivItemPopularityRankingIcon;
        @BindView(R.id.iv_item_popularity_ranking_identity)
        ImageView ivItemPopularityRankingIdentity;
        @BindView(R.id.tv_item_popularity_ranking_name)
        TextView tvItemPopularityRankingName;
        @BindView(R.id.iv_item_popularity_ranking_sex)
        ImageView ivItemPopularityRankingSex;
        @BindView(R.id.tv_item_popularity_ranking_age)
        TextView tvItemPopularityRankingAge;
        @BindView(R.id.ll_item_popularity_ranking_sex)
        LinearLayout llItemPopularityRankingSex;
        @BindView(R.id.tv_item_popularity_ranking_role)
        TextView tvItemPopularityRankingRole;
        @BindView(R.id.tv_item_popularity_ranking_wealth)
        TextView tvItemPopularityRankingWealth;
        @BindView(R.id.ll_item_popularity_ranking_wealth)
        LinearLayout llItemPopularityRankingWealth;
        @BindView(R.id.tv_item_popularity_ranking_charm)
        TextView tvItemPopularityRankingCharm;
        @BindView(R.id.ll_item_popularity_ranking_charm)
        LinearLayout llItemPopularityRankingCharm;
        @BindView(R.id.ll_item_popularity_ranking_info)
        LinearLayout llItemPopularityRankingInfo;
        @BindView(R.id.tv_item_popularity_ranking_num)
        TextView tvItemPopularityRankingNum;
        @BindView(R.id.tv_item_popularity_ranking_num_txt)
        TextView tvItemPopularityRankingNumTxt;
        public PopularityRankHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            RankPopularityData.DataBean data = rankList.get(position);
            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), ivItemPopularityRankingIdentity);

//            if (data.getRealname().equals("0")) {
//                itemListviewRanklistShiming.setVisibility(View.GONE);
//            } else {
//                itemListviewRanklistShiming.setVisibility(View.VISIBLE);
//            }

            if (data.getSex().equals("1")) {
                llItemPopularityRankingSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
                ivItemPopularityRankingSex.setImageResource(R.mipmap.nan);
            } else if (data.getSex().equals("2")) {
                llItemPopularityRankingSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
                ivItemPopularityRankingSex.setImageResource(R.mipmap.nv);
            } else if (data.getSex().equals("3")) {
                llItemPopularityRankingSex.setBackgroundResource(R.drawable.item_sex_san_bg);
                ivItemPopularityRankingSex.setImageResource(R.mipmap.san);
            }
            if (data.getRole().equals("S")) {
                tvItemPopularityRankingRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
                tvItemPopularityRankingRole.setText("斯");
            } else if (data.getRole().equals("M")) {
                tvItemPopularityRankingRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                tvItemPopularityRankingRole.setText("慕");
            } else if (data.getRole().equals("SM")) {
                tvItemPopularityRankingRole.setBackgroundResource(R.drawable.item_sex_san_bg);
                tvItemPopularityRankingRole.setText("双");
            } else if (data.getRole().equals("~")) {
                tvItemPopularityRankingRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                tvItemPopularityRankingRole.setText("~");
            } else {
                tvItemPopularityRankingRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                tvItemPopularityRankingRole.setText("-");
            }
            tvItemPopularityRankingAge.setText(data.getAge());
            tvItemPopularityRankingIndex.setText(position + 4 + "");
            if (!data.getHead_pic().equals(NetPic()) || !data.getHead_pic().equals("")) {//"http://59.110.28.150:888/"
                GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivItemPopularityRankingIcon, 2);
            } else {
                ivItemPopularityRankingIcon.setImageResource(R.mipmap.morentouxiang);
            }
            tvItemPopularityRankingName.setText(data.getNickname());
            tvItemPopularityRankingNum.setText(data.getAllnum());
            if(type == 1) {
                tvItemPopularityRankingNum.setTextColor(context.getResources().getColor(R.color.girlColor));
                tvItemPopularityRankingNumTxt.setTextColor(context.getResources().getColor(R.color.girlColor));
            } else {
                tvItemPopularityRankingNum.setTextColor(Color.parseColor("#0ecdcb"));
                tvItemPopularityRankingNumTxt.setTextColor(Color.parseColor("#0ecdcb"));
            }
            llItemPopularityRankingWealth.setVisibility(View.VISIBLE);
            llItemPopularityRankingCharm.setVisibility(View.VISIBLE);
            tvItemPopularityRankingWealth.setText(data.getWealth_val());
            tvItemPopularityRankingCharm.setText(data.getCharm_val());
            final String uid = data.getUid();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("uid", uid);
                    context.startActivity(intent);
                }
            });

        }
    }
}
