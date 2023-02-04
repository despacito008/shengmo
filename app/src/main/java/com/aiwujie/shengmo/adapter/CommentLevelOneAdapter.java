package com.aiwujie.shengmo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.CommentDetailActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.CommentData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.LinkMovementMethodOverride;
import com.aiwujie.shengmo.utils.OnSimpleItemViewListener;
import com.aiwujie.shengmo.zdyview.ATGroupSpan;
import com.aiwujie.shengmo.zdyview.ATHighSpan;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.bumptech.glide.Glide;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class CommentLevelOneAdapter extends RecyclerView.Adapter<CommentLevelOneAdapter.ViewHolder> {
    private Context context;
    private List<CommentData.DataBean> list;

    private int type = -1;
    private String did;

    public CommentLevelOneAdapter(Context context, List<CommentData.DataBean> list,String did) {
        this.context = context;
        this.list = list;
        this.did = did;
    }


    public CommentLevelOneAdapter(Context context, List<CommentData.DataBean> list,String did,int type) {
        this.context = context;
        this.list = list;
        this.did = did;
        this.type = 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_level_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        GlideImgManager.glideLoader(MyApp.getInstance(), list.get(position).getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.ivCommentLevelIcon,0);
        holder.ivCommentLevelVip.setVisibility(View.VISIBLE);
        if ("1".equals(list.get(position).getIs_admin())) {
            holder.ivCommentLevelVip.setImageResource(R.drawable.user_manager);//黑V
        } else if ("1".equals(list.get(position).getSvipannual())) {
            holder.ivCommentLevelVip.setImageResource(R.drawable.user_svip_year);
        } else if ("1".equals(list.get(position).getSvip())) {
            holder.ivCommentLevelVip.setImageResource(R.drawable.user_svip);
        } else if ("1".equals(list.get(position).getVipannual())) {
            holder.ivCommentLevelVip.setImageResource(R.drawable.user_vip_year);
        } else if ("1".equals(list.get(position).getVip())) {
            holder.ivCommentLevelVip.setImageResource(R.drawable.user_vip);
        } else {
            holder.ivCommentLevelVip.setVisibility(View.INVISIBLE);
            //holder.ivCommentLevelVip.setImageResource(R.mipmap.gaojisousuohui);
        }

        holder.tvCommentLevel_name.setText(list.get(position).getNickname());
        holder.tvCommentLevelTime.setText(list.get(position).getSendtime());
        if(type != - 1) {
            holder.tvCommentLevelContent.setTextColor(context.getResources().getColor(R.color.titleBlack));
            holder.tvCommentLevelContent.setBackgroundResource(R.drawable.item_normal_bg);
            holder.rvChildComment.setBackgroundResource(R.drawable.bg_round_gray);
            holder.tvCommentLevel_name.setTextColor(context.getResources().getColor(R.color.darkGray));
            holder.tvCommentLevelTime.setTextColor(context.getResources().getColor(R.color.lightGray));
            holder.tvItemThumbUpNum.setTextColor(context.getResources().getColor(R.color.darkGray));
        }
        holder.tvCommentLevelContent.setText(list.get(position).getContent());


        if ("0".equals(list.get(position).getOtheruid())) {
            SpannableString spanableInfo = new SpannableString(list.get(position).getContent());
            if (!"".equals(list.get(position).getAtuname())&&null!= list.get(position).getAtuname() && !"".equals(list.get(position).getAtuid())&&null!= list.get(position).getAtuid()){
                holder.tvCommentLevelContent.setOnTouchListener(new LinkMovementMethodOverride());
                String[] split = list.get(position).getAtuid().split(",");
                String[] split1 = list.get(position).getAtuname().split(",");
                for (int i = 0; i <split1.length; i++) {
                    if (split1[i].trim().contains("*")){
                        split1[i] =  split1[i].trim().replace("*","\\*");
                    }
                    String patten = Pattern.quote(split1[i].trim());
                        Pattern compile = Pattern.compile(patten);
                    Matcher matcher = compile.matcher(list.get(position).getContent());
                    while (matcher.find()){
                        int start = matcher.start();
                        if (split1[i].trim().contains("*")){
                            split1[i] =  split1[i].trim().replace("\\","");
                        }
                        if (!TextUtil.isEmpty(split[i]) && start >= 0) {
                            if (split1[i].startsWith("@[群]")) {
                                spanableInfo.setSpan(new ATGroupSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            } else if (split1[i].startsWith("@[高端]")) {
                                spanableInfo.setSpan(new ATHighSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            else {
                                spanableInfo.setSpan(new ATSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                       // spanableInfo.setSpan(new ATSpan(split[i]),start,start+split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            holder.tvCommentLevelContent.setText(spanableInfo);
        } else {
//            holder.itemListviewArguementContent.setText("回复 " +data.getOthernickname()+ ": " + data.getContent());
            SpannableStringBuilder builder = new SpannableStringBuilder("回复 " + list.get(position).getOthernickname() + ": " + list.get(position).getContent());
            ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#b73acb"));
            builder.setSpan(purSpan, 3, list.get(position).getOthernickname().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (!"".equals(list.get(position).getAtuname())&&null!= list.get(position).getAtuname() && !"".equals(list.get(position).getAtuid())&&null!= list.get(position).getAtuid()){
                holder.tvCommentLevelContent.setOnTouchListener(new LinkMovementMethodOverride());
                String[] split = list.get(position).getAtuid().split(",");
                String[] split1 = list.get(position).getAtuname().split(",");
                for (int i = 0; i <split1.length; i++) {
                    if (split1[i].trim().contains("*")){
                        split1[i] =  split1[i].trim().replace("*","\\*");
                    }
                    String patten = Pattern.quote(split1[i].trim());
                        Pattern compile = Pattern.compile(patten);
                    Matcher matcher = compile.matcher("回复 " + list.get(position).getOthernickname() + ": " + list.get(position).getContent());
                    while (matcher.find()){
                        int start = matcher.start();
                        if (split1[i].trim().contains("*")){
                            split1[i] =  split1[i].trim().replace("\\","");
                        }
                        if (!TextUtil.isEmpty(split[i]) && start >= 0) {
                            if (split1[i].startsWith("@[群]")) {
                                builder.setSpan(new ATGroupSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }  if (split1[i].startsWith("@[高端]")) {
                                builder.setSpan(new ATHighSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            else {
                                builder.setSpan(new ATSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                       // builder.setSpan(new ATSpan(split[i]),start,start+split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            holder.tvCommentLevelContent.setText(builder);

        }



        List<CommentData.DataBean.SubsetcommentBean> subsetcomment = list.get(position).getSubsetcomment();
        if(subsetcomment == null || subsetcomment.size() == 0) {
            holder.rvChildComment.setVisibility(View.GONE);
        } else {
            holder.rvChildComment.setVisibility(View.VISIBLE);
            ChildCommentAdapter childCommentAdapter = new ChildCommentAdapter(context,subsetcomment,Integer.parseInt(list.get(position).getTwolevelcommentnum()),type);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            holder.rvChildComment.setAdapter(childCommentAdapter);
            holder.rvChildComment.setLayoutManager(linearLayoutManager);
            childCommentAdapter.setOnSimpleItemViewListener(new OnSimpleItemViewListener() {
                @Override
                public void onItemListener(View view) {
                    Intent intent = new Intent(context, CommentDetailActivity.class);
                    intent.putExtra("cmid",list.get(position).getCmid());
                    intent.putExtra("did",did);
                    intent.putExtra("position",position);
                    context.startActivity(intent);
                }
            });
        }

        if("1".equals(list.get(position).getIs_like())) {
            Glide.with(context).load(R.drawable.user_thumb_up_done).into(holder.ivCommentLevelDianzan);
            holder.tvItemThumbUpNum.setTextColor(context.getResources().getColor(R.color.thumbOrange));
        } else {
            Glide.with(context).load(R.drawable.user_thumb_up).into(holder.ivCommentLevelDianzan);
            holder.tvItemThumbUpNum.setTextColor(context.getResources().getColor(R.color.normalGray));
        }

        if("0".equals(list.get(position).getLikenum())) {
            holder.tvItemThumbUpNum.setVisibility(View.GONE);
        } else {
            holder.tvItemThumbUpNum.setVisibility(View.VISIBLE);
            holder.tvItemThumbUpNum.setText(list.get(position).getLikenum());
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCommentLevelIcon, ivCommentLevelVip, ivCommentLevelDianzan;
        TextView tvCommentLevel_name, tvCommentLevelTime, tvCommentLevelContent,tvItemThumbUpNum;
        RecyclerView rvChildComment;
        ConstraintLayout clItem;
        public ViewHolder(final View itemView) {
            super(itemView);
            ivCommentLevelIcon = itemView.findViewById(R.id.iv_comment_level_icon);
            ivCommentLevelVip = itemView.findViewById(R.id.iv_comment_level_vip);
            tvCommentLevel_name = itemView.findViewById(R.id.tv_comment_level_name);
            tvCommentLevelTime = itemView.findViewById(R.id.tv_comment_level_time);
            tvCommentLevelContent = itemView.findViewById(R.id.tv_comment_level_content);
            ivCommentLevelDianzan = itemView.findViewById(R.id.iv_comment_level_dianzan);

            rvChildComment = itemView.findViewById(R.id.rv_item_child_comment);
            tvItemThumbUpNum = itemView.findViewById(R.id.tv_item_comment_level_dian_zan_num);
            clItem = itemView.findViewById(R.id.cl_item_level_comment);
            ivCommentLevelDianzan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onCommentClickListener != null) {
                        onCommentClickListener.onItemThumbUp(itemView);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onCommentClickListener != null) {
                        onCommentClickListener.onItemClick(itemView);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onCommentClickListener != null) {
                        onCommentClickListener.onItemLongClick(itemView);
                    }
                    return false;
                }
            });
            ivCommentLevelIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onCommentClickListener != null) {
                        onCommentClickListener.onItemHeadViewClick(itemView);
                    }

                }
            });

        }
    }


    public interface OnCommentClickListener {
        void onItemClick(View view);
        void onItemThumbUp(View view);
        void onItemHeadViewClick(View view);
        void onItemLongClick(View view);
    }

    OnCommentClickListener onCommentClickListener;

    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }


}
