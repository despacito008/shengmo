package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.CommentDetailActivity;
import com.aiwujie.shengmo.bean.CommentDetailBean;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.LinkMovementMethodOverride;
import com.aiwujie.shengmo.zdyview.ATGroupSpan;
import com.aiwujie.shengmo.zdyview.ATHighSpan;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;


import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CommentDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<CommentDetailBean.DataBean.TwolevelcommentBean> twolevelcomment;

    public CommentDetailAdapter(Context context, List<CommentDetailBean.DataBean.TwolevelcommentBean> twolevelcomment) {
        this.context = context;
        this.twolevelcomment = twolevelcomment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment_detail, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CommentHolder) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return twolevelcomment.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_comment_detail_head)
        ImageView ivItemCommentDetailHead;
        @BindView(R.id.iv_item_comment_detail_sign)
        ImageView ivItemCommentDetailSign;
        @BindView(R.id.iv_item_comment_detail_thumb_up)
        ImageView ivItemCommentDetailThumbUp;
        @BindView(R.id.tv_item_comment_detail_thumb_up)
        TextView tvItemCommentDetailThumbUp;
        @BindView(R.id.tv_item_comment_detail_name)
        TextView tvItemCommentDetailName;
        @BindView(R.id.tv_item_comment_detail_date)
        TextView tvItemCommentDetailDate;
        @BindView(R.id.tv_item_comment_detail_content)
        TextView tvItemCommentDetailContent;

        public CommentHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivItemCommentDetailThumbUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCommentClickListener != null) {
                        onCommentClickListener.onItemThumbUp(itemView);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCommentClickListener != null) {
                        onCommentClickListener.onItemClick(itemView);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onCommentClickListener != null) {
                        onCommentClickListener.onItemLongClick(itemView);
                    }
                    return false;
                }
            });
            ivItemCommentDetailHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCommentClickListener != null) {
                        onCommentClickListener.onItemHeadViewClick(itemView);
                    }
                }
            });

        }

        public void setData(int position) {
            tvItemCommentDetailName.setText(twolevelcomment.get(position).getNickname());
            tvItemCommentDetailDate.setText(twolevelcomment.get(position).getSendtime());
            tvItemCommentDetailThumbUp.setText(twolevelcomment.get(position).getLikenum());
            CommentDetailBean.DataBean.TwolevelcommentBean data = twolevelcomment.get(position);
            if (data.getOtheruid().equals("0")) {
                SpannableString spanableInfo = new SpannableString(data.getContent());
                if (!"".equals(data.getAtuname()) && null != data.getAtuname() && !"".equals(data.getAtuid()) && null != data.getAtuid()) {
                    tvItemCommentDetailContent.setOnTouchListener(new LinkMovementMethodOverride());
                    String[] split = data.getAtuid().split(",");
                    String[] split1 = data.getAtuname().split(",");
                    for (int i = 0; i < split1.length; i++) {
                        if (split1[i].trim().contains("*")) {
                            split1[i] = split1[i].trim().replace("*", "\\*");
                        }
                        String patten = Pattern.quote(split1[i].trim());
                        Pattern compile = Pattern.compile(patten);
                        Matcher matcher = compile.matcher(data.getContent());
                        while (matcher.find()) {
                            int start = matcher.start();
                            if (split1[i].trim().contains("*")) {
                                split1[i] = split1[i].trim().replace("\\", "");
                            }
                            if (!TextUtil.isEmpty(split[i]) && start >= 0) {
                                if (split1[i].startsWith("@[群]")) {
                                    spanableInfo.setSpan(new ATGroupSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                } else if (split1[i].startsWith("@[高端]")) {
                                    spanableInfo.setSpan(new ATHighSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                } else {
                                    spanableInfo.setSpan(new ATSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                            //spanableInfo.setSpan(new ATSpan(split[i]),start,start+split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
                tvItemCommentDetailContent.setText(spanableInfo);
            } else {
                String contentStr = "回复 " + data.getOthernickname() + ": " + data.getContent();
                SpannableStringBuilder builder = new SpannableStringBuilder(contentStr);
                ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#b73acb"));
                builder.setSpan(purSpan, 3, data.getOthernickname().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (!"".equals(data.getAtuname()) && null != data.getAtuname() && !"".equals(data.getAtuid()) && null != data.getAtuid()) {
                    tvItemCommentDetailContent.setOnTouchListener(new LinkMovementMethodOverride());
                    String[] split = data.getAtuid().split(",");
                    String[] split1 = data.getAtuname().split(",");
                    for (int i = 0; i < split1.length; i++) {
                        if (split1[i].trim().contains("*")) {
                            split1[i] = split1[i].trim().replace("*", "\\*");
                        }
                        String patten = Pattern.quote(split1[i].trim());
                        Pattern compile = Pattern.compile(patten);
                        Matcher matcher = compile.matcher(contentStr);
                        while (matcher.find()) {
                            int start = matcher.start();
                            if (split1[i].trim().contains("*")) {
                                split1[i] = split1[i].trim().replace("\\", "");
                            }
                            if (!TextUtil.isEmpty(split[i]) && start >= 0) {
                                if (split1[i].startsWith("@[群]")) {
                                    builder.setSpan(new ATGroupSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                } else if (split1[i].startsWith("@[高端]")) {
                                    builder.setSpan(new ATHighSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                } else {
                                    builder.setSpan(new ATSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                            //builder.setSpan(new ATSpan(split[i]),start,start+split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }

                tvItemCommentDetailContent.setText(builder);
            }

            //  判断显示标识，官方管理>年svip>svip>年vip>vip
            ivItemCommentDetailSign.setVisibility(View.VISIBLE);
            CommentDetailBean.DataBean.TwolevelcommentBean twoLevelCommentBean = twolevelcomment.get(position);
            if (twoLevelCommentBean.getIs_admin().equals("1")) {
                Glide.with(context).load(R.drawable.user_manager).into(ivItemCommentDetailSign);
            } else if (twoLevelCommentBean.getSvipannual().equals("1")) {
                Glide.with(context).load(R.drawable.user_svip_year).into(ivItemCommentDetailSign);
            } else if (twoLevelCommentBean.getSvip().equals("1")) {
                Glide.with(context).load(R.drawable.user_svip).into(ivItemCommentDetailSign);
            } else if (twoLevelCommentBean.getVipannual().equals("1")) {
                Glide.with(context).load(R.drawable.user_vip_year).into(ivItemCommentDetailSign);
            } else if (twoLevelCommentBean.getVip().equals("1")) {
                Glide.with(context).load(R.drawable.user_vip).into(ivItemCommentDetailSign);
            } else {
                ivItemCommentDetailSign.setVisibility(View.INVISIBLE);
            }

            ImageLoader.loadCircleImage(context, twolevelcomment.get(position).getHead_pic(), ivItemCommentDetailHead, R.mipmap.morentouxiang);

            if ("1".equals(twolevelcomment.get(position).getIs_like())) {
                Glide.with(context).load(R.drawable.thumb_up_done).into(ivItemCommentDetailThumbUp);
            } else {
                Glide.with(context).load(R.drawable.thumb_up_normal).into(ivItemCommentDetailThumbUp);
            }

            if ("0".equals(twolevelcomment.get(position).getLikenum())) {
                tvItemCommentDetailThumbUp.setVisibility(View.INVISIBLE);
            } else {
                tvItemCommentDetailThumbUp.setVisibility(View.VISIBLE);
                tvItemCommentDetailThumbUp.setText(twolevelcomment.get(position).getLikenum());

            }
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
