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
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.CommentData;
import com.aiwujie.shengmo.utils.LinkMovementMethodOverride;
import com.aiwujie.shengmo.utils.OnSimpleItemViewListener;
import com.aiwujie.shengmo.zdyview.ATGroupSpan;
import com.aiwujie.shengmo.zdyview.ATHighSpan;
import com.aiwujie.shengmo.zdyview.ATSpan;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChildCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<CommentData.DataBean.SubsetcommentBean> subsetcomment;
    int nums;
    int type = 1;

    public ChildCommentAdapter(Context context, List<CommentData.DataBean.SubsetcommentBean> subsetcomment, int nums) {
        this.context = context;
        this.subsetcomment = subsetcomment;
        this.nums = nums;
    }

    public ChildCommentAdapter(Context context, List<CommentData.DataBean.SubsetcommentBean> subsetcomment, int nums,int type) {
        this.context = context;
        this.subsetcomment = subsetcomment;
        this.nums = nums;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_child_comment,parent,false);
        return new ChildCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ChildCommentHolder)holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return subsetcomment.size() > 2 ? 2 : subsetcomment.size();
    }

    class ChildCommentHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvContent,tvMore;

        public ChildCommentHolder(final View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_child_comment_name);
            tvContent = itemView.findViewById(R.id.tv_item_child_comment_content);
            tvMore = itemView.findViewById(R.id.tv_item_child_comment_more);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(onSimpleItemViewListener != null) {
                       onSimpleItemViewListener.onItemListener(itemView);
                   }
                }
            });
        }

        public void setData(int position) {
            tvName.setText(subsetcomment.get(position).getNickname());

            if(type == -1) {
                tvContent.setTextColor(context.getResources().getColor(R.color.white));
                tvMore.setTextColor(context.getResources().getColor(R.color.white));
            }


            CommentData.DataBean.SubsetcommentBean data = subsetcomment.get(position);
            if (data.getOtheruid().equals("0")) {
                SpannableString spanableInfo = new SpannableString(data.getContent());
                if (!"".equals(data.getAtuname())&&null!= data.getAtuname() && !"".equals(data.getAtuid())&&null!= data.getAtuid()){
                    tvContent.setOnTouchListener(new LinkMovementMethodOverride());
                    String[] split = data.getAtuid().split(",");
                    String[] split1 = data.getAtuname().split(",");
                    for (int i = 0; i <split1.length; i++) {
                        if (split1[i].trim().contains("*")){
                            split1[i] =  split1[i].trim().replace("*","\\*");
                        }
                        String patten = Pattern.quote(split1[i].trim());
                        Pattern compile = Pattern.compile(patten);
                        Matcher matcher = compile.matcher(data.getContent());
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
                            //spanableInfo.setSpan(new ATSpan(split[i]),start,start+split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
                tvContent.setText(spanableInfo);
            } else {
                String contentStr = data.getNickname() + "回复 " + data.getOthernickname() + ": " + data.getContent();
                SpannableStringBuilder builder = new SpannableStringBuilder(contentStr);
                ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#db57f3"));
                ForegroundColorSpan purSpan2 = new ForegroundColorSpan(Color.parseColor("#db57f3"));
                builder.setSpan(purSpan, 0, data.getNickname().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(purSpan2, data.getNickname().length() + 3, data.getNickname().length() + data.getOthernickname().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (!"".equals(data.getAtuname())&&null!= data.getAtuname() && !"".equals(data.getAtuid())&&null!= data.getAtuid()){
                    tvContent.setOnTouchListener(new LinkMovementMethodOverride());
                    String[] split = data.getAtuid().split(",");
                    String[] split1 = data.getAtuname().split(",");
                    for (int i = 0; i <split1.length; i++) {
                        if (split1[i].trim().contains("*")){
                            split1[i] =  split1[i].trim().replace("*","\\*");
                        }
                        String patten = Pattern.quote(split1[i].trim());
                        Pattern compile = Pattern.compile(patten);
                        Matcher matcher = compile.matcher(contentStr);
                        while (matcher.find()){
                            int start = matcher.start();
                            if (split1[i].trim().contains("*")){
                                split1[i] =  split1[i].trim().replace("\\","");
                            }
                            if (!TextUtil.isEmpty(split[i]) && start >= 0) {
                                if (split1[i].startsWith("@[群]")) {
                                    builder.setSpan(new ATGroupSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                                else if (split1[i].startsWith("@[高端]")) {
                                    builder.setSpan(new ATHighSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                                else {
                                    builder.setSpan(new ATSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                            //builder.setSpan(new ATSpan(split[i]),start,start+split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
                tvContent.setText(builder);
            }
            
            
            if(position == 1 && nums >= 3) {
                tvMore.setVisibility(View.VISIBLE);
                tvMore.setText("共有"+ nums + "条回复>");
            } else {
                tvMore.setVisibility(View.GONE);
            }
        }
    }

    OnSimpleItemViewListener onSimpleItemViewListener;

    public void setOnSimpleItemViewListener(OnSimpleItemViewListener onSimpleItemViewListener) {
        this.onSimpleItemViewListener = onSimpleItemViewListener;
    }
}
