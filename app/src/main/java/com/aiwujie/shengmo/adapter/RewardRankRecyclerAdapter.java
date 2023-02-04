package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class RewardRankRecyclerAdapter extends RecyclerView.Adapter<RewardRankRecyclerAdapter.RewardRankHolder> {
    Context context;
    private List<RankBerewardData.DataBean> rankList;
    private int type = 1;

    public RewardRankRecyclerAdapter(Context context, List<RankBerewardData.DataBean> rankList,int type) {
        this.context = context;
        this.rankList = rankList;
        this.type = type;
    }

    @Override
    public RewardRankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reward_ranking, parent, false);
        return new RewardRankHolder(view);
    }

    @Override
    public void onBindViewHolder(RewardRankHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }

    class RewardRankHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_reward_ranking_index)
        TextView tvItemRewardRankingIndex;
        @BindView(R.id.iv_item_reward_ranking_icon)
        ImageView ivItemRewardRankingIcon;
        @BindView(R.id.iv_item_reward_ranking_identity)
        ImageView ivItemRewardRankingIdentity;
        @BindView(R.id.tv_item_reward_ranking_name)
        TextView tvItemRewardRankingName;
        @BindView(R.id.iv_item_reward_ranking_sex)
        ImageView ivItemRewardRankingSex;
        @BindView(R.id.tv_item_reward_ranking_age)
        TextView tvItemRewardRankingAge;
        @BindView(R.id.ll_item_reward_ranking_sex)
        LinearLayout llItemRewardRankingSex;
        @BindView(R.id.tv_item_reward_ranking_role)
        TextView tvItemRewardRankingRole;
        @BindView(R.id.tv_item_reward_ranking_wealth)
        TextView tvItemRewardRankingWealth;
        @BindView(R.id.ll_item_reward_ranking_wealth)
        LinearLayout llItemRewardRankingWealth;
        @BindView(R.id.tv_item_reward_ranking_charm)
        TextView tvItemRewardRankingCharm;
        @BindView(R.id.ll_item_reward_ranking_charm)
        LinearLayout llItemRewardRankingCharm;
        @BindView(R.id.ll_item_reward_ranking_info)
        LinearLayout llItemRewardRankingInfo;
        @BindView(R.id.tv_item_reward_ranking_count)
        TextView tvItemRewardRankingCount;
        @BindView(R.id.ll_item_reward_ranking_count)
        LinearLayout llItemRewardRankingCount;
        @BindView(R.id.iv_item_reward_ranking_favour)
        ImageView ivItemRewardRankingFavour;
        @BindView(R.id.tv_item_reward_ranking_favour)
        TextView tvItemRewardRankingFavour;
        @BindView(R.id.tv_item_reward_ranking_getOrSend)
        TextView tvRewardRankingGetOrSend;
        public RewardRankHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            RankBerewardData.DataBean data = rankList.get(position);
            //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), ivItemRewardRankingIdentity);

//            if (data.getRealname().equals("0")) {
//                itemListviewRanklistShiming.setVisibility(View.GONE);
//            } else {
//                itemListviewRanklistShiming.setVisibility(View.VISIBLE);
//            }

            if (data.getSex().equals("1")) {
                llItemRewardRankingSex.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                ivItemRewardRankingSex.setImageResource(R.mipmap.nan);
            } else if (data.getSex().equals("2")) {
                llItemRewardRankingSex.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                ivItemRewardRankingSex.setImageResource(R.mipmap.nv);
            } else if (data.getSex().equals("3")) {
                llItemRewardRankingSex.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                ivItemRewardRankingSex.setImageResource(R.mipmap.san);
            }
            if (data.getRole().equals("S")) {
                tvItemRewardRankingRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
                tvItemRewardRankingRole.setText("斯");
            } else if (data.getRole().equals("M")) {
                tvItemRewardRankingRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
                tvItemRewardRankingRole.setText("慕");
            } else if (data.getRole().equals("SM")) {
                tvItemRewardRankingRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
                tvItemRewardRankingRole.setText("双");
            } else if (data.getRole().equals("~")) {
                tvItemRewardRankingRole.setBackgroundResource(R.drawable.bg_user_info_sex_other);
                tvItemRewardRankingRole.setText("~");
            }
            tvItemRewardRankingAge.setText(data.getAge());
            tvItemRewardRankingIndex.setText(position + 4 + "");
            if (!data.getHead_pic().equals(NetPic()) || !data.getHead_pic().equals("")) {//"http://59.110.28.150:888/"
                GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivItemRewardRankingIcon, 2);
            } else {
                ivItemRewardRankingIcon.setImageResource(R.mipmap.morentouxiang);
            }
            tvItemRewardRankingName.setText(data.getNickname());

            if(TextUtil.isEmpty(data.getAllamount())) {
                llItemRewardRankingCount.setVisibility(View.INVISIBLE);
            } else {
                llItemRewardRankingCount.setVisibility(View.VISIBLE);
                tvItemRewardRankingCount.setText(data.getAllamount());
            }
            if(type == 1) {
                llItemRewardRankingWealth.setVisibility(View.VISIBLE);
                llItemRewardRankingCharm.setVisibility(View.GONE);
                tvItemRewardRankingWealth.setText(data.getWealth_val());
                tvRewardRankingGetOrSend.setText("送出");
                tvItemRewardRankingFavour.setBackgroundResource(R.drawable.bg_round_red);
                tvItemRewardRankingFavour.setText("最爱");
            } else {
                llItemRewardRankingWealth.setVisibility(View.GONE);
                llItemRewardRankingCharm.setVisibility(View.VISIBLE);
                tvItemRewardRankingCharm.setText(data.getWealth_val());
                tvRewardRankingGetOrSend.setText("收到");
                tvItemRewardRankingFavour.setBackgroundResource(R.drawable.bg_round_purple);
                tvItemRewardRankingFavour.setText("豪粉");
            }
            if (data.getRewardeduserinfo() != null) {
                if (!data.getHead_pic().equals(NetPic()) || !data.getHead_pic().equals("")) {//"http://59.110.28.150:888/"
                    GlideImgManager.glideLoader(context, data.getRewardeduserinfo().getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivItemRewardRankingFavour, 0);
                } else {
                    ivItemRewardRankingFavour.setImageResource(R.mipmap.morentouxiang);
                }
                ivItemRewardRankingFavour.setVisibility(View.VISIBLE);
                final String fuid = data.getRewardeduserinfo().getFuid();
                ivItemRewardRankingFavour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UserInfoActivity.class);
                        intent.putExtra("uid",fuid);
                        context.startActivity(intent);
                    }
                });
                ivItemRewardRankingFavour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UserInfoActivity.class);
                        intent.putExtra("uid",fuid);
                        context.startActivity(intent);
                    }
                });
            } else {
                ivItemRewardRankingFavour.setVisibility(View.GONE);
                tvItemRewardRankingFavour.setVisibility(View.GONE);
            }
            final String uid = data.getUid();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("uid",uid);
                    context.startActivity(intent);
                }
            });

        }
    }
}
