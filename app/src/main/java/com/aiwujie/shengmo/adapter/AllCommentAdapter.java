package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.bean.AllCommentData;
import com.aiwujie.shengmo.customview.PercentLinearLayout;
import com.aiwujie.shengmo.utils.GlideImgManager;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/1/23.
 */
public class AllCommentAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<AllCommentData.DataBean> list;
    private LayoutInflater inflater;
    boolean isShowApply = false;
    int newMessageNum = -1;

    public AllCommentAdapter(Context context, List<AllCommentData.DataBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public AllCommentAdapter(Context context, List<AllCommentData.DataBean> list,int newMessageNum) {
        this.context = context;
        this.list = list;
        this.newMessageNum = newMessageNum;
        inflater = LayoutInflater.from(context);
        isShowApply = true;
    }

    public void setNewMessageNum(int num) {
        newMessageNum = num;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview_all_comment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AllCommentData.DataBean data = list.get(position);
        if(isShowApply) {
            holder.tvItemApply.setVisibility(View.VISIBLE);
            if(position < newMessageNum) {
                holder.tvItemNew.setVisibility(View.VISIBLE);
            } else {
                holder.tvItemNew.setVisibility(View.GONE);
            }
        } else {
            holder.tvItemApply.setVisibility(View.GONE);
        }
        if (list.get(position).getHead_pic().equals("") || list.get(position).getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemListviewAllcommentIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, list.get(position).getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewAllcommentIcon, 0);
        }
        holder.itemListviewAllcommentName.setText(data.getNickname());
        if (data.getOtheruid().equals("0")) {
            holder.itemListviewAllarguementContent.setText(data.getCcontent());
        } else {
            holder.itemListviewAllarguementContent.setText("回复  " + data.getOthernickname() + ":" + data.getCcontent());
        }
        holder.itemListviewAllcommentTime.setText(data.getAddtime());
        holder.itemListviewAllcommentIcon.setTag(position);
        holder.itemListviewAllcommentIcon.setOnClickListener(this);
        if (!TextUtil.isEmpty(data.getPic()) || !TextUtil.isEmpty(data.getCoverUrl())) {
            holder.itemListviewAllcommentDynamiciv.setVisibility(View.VISIBLE);
            holder.itemListviewAllcommentDynamiccontent.setVisibility(View.GONE);
            if (TextUtil.isEmpty(data.getPic())) {
                GlideImgManager.glideLoader(context, data.getCoverUrl(), R.mipmap.default_error, R.mipmap.default_error, holder.itemListviewAllcommentDynamiciv,1);
            } else {
                GlideImgManager.glideLoader(context, data.getPic(), R.mipmap.default_error, R.mipmap.default_error, holder.itemListviewAllcommentDynamiciv,1);
            }
        } else {
            holder.itemListviewAllcommentDynamiciv.setVisibility(View.GONE);
            holder.itemListviewAllcommentDynamiccontent.setVisibility(View.VISIBLE);
            holder.itemListviewAllcommentDynamiccontent.setText(data.getContent());
        }
        holder.llItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemCommentListener != null) {
                    onItemCommentListener.onItemLongClick(position);
                }
                return true;
            }
        });
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCommentListener != null) {
                    onItemCommentListener.onItemClick(position);
                }
            }
        });
        holder.tvItemApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemCommentListener != null) {
                    onItemCommentListener.onItemApply(position);
                }
            }
        });


        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        if (list.size() <= pos) {
            return;
        }
        Intent intent = new Intent(context, PesonInfoActivity.class);
        intent.putExtra("uid", list.get(pos).getUid());
        context.startActivity(intent);
    }

    static class ViewHolder {
        @BindView(R.id.item_listview_allcomment_icon)
        ImageView itemListviewAllcommentIcon;
        @BindView(R.id.item_listview_allcomment_vip)
        ImageView itemListviewAllcommentVip;
        @BindView(R.id.item_listview_allcomment_name)
        TextView itemListviewAllcommentName;
        @BindView(R.id.item_listview_allcomment_time)
        TextView itemListviewAllcommentTime;
        @BindView(R.id.item_listview_allarguement_content)
        TextView itemListviewAllarguementContent;
        @BindView(R.id.item_listview_allcomment_dynamiccontent)
        TextView itemListviewAllcommentDynamiccontent;
        @BindView(R.id.item_listview_allcomment_dynamiciv)
        ImageView itemListviewAllcommentDynamiciv;
        @BindView(R.id.ll_item_comment)
        LinearLayout llItem;
        @BindView(R.id.tv_item_all_comment_apply)
        TextView tvItemApply;
        @BindView(R.id.tv_item_all_comment_new)
        TextView tvItemNew;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemCommentListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
        void onItemApply(int position);
    }

    OnItemCommentListener onItemCommentListener;

    public void setOnCommentListener(OnItemCommentListener onCommentListener) {
        this.onItemCommentListener = onCommentListener;
    }
}
