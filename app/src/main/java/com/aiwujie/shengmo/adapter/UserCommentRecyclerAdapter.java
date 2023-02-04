package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.bean.AllCommentData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.TextViewUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/1/23.
 */
public class UserCommentRecyclerAdapter extends RecyclerView.Adapter<UserCommentRecyclerAdapter.CommentHolder> {
    private Context context;
    private List<AllCommentData.DataBean> list;
    private LayoutInflater inflater;
    boolean isShowApply = false;
    private Fragment fragment;

    public UserCommentRecyclerAdapter(Context context, List<AllCommentData.DataBean> list,Fragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    public UserCommentRecyclerAdapter(Context context, List<AllCommentData.DataBean> list, int tag) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        isShowApply = true;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listview_all_comment,parent,false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
       holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void onClick(View v) {
        int pos = (int) v.getTag();
        Intent intent = new Intent(context, PesonInfoActivity.class);
        intent.putExtra("uid", list.get(pos).getUid());
        context.startActivity(intent);
    }


    class CommentHolder extends RecyclerView.ViewHolder {
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

        public CommentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final int position) {
            final AllCommentData.DataBean data = list.get(position);
            if(isShowApply) {
                tvItemApply.setVisibility(View.VISIBLE);
            } else {
                tvItemApply.setVisibility(View.GONE);
            }
            if (list.get(position).getHead_pic().equals("") || list.get(position).getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
                itemListviewAllcommentIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                //GlideImgManager.glideLoader(context, list.get(position).getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, itemListviewAllcommentIcon, 0);
                ImageLoader.loadCircleImage(fragment,list.get(position).getHead_pic(),itemListviewAllcommentIcon,R.mipmap.morentouxiang);
            }
            itemListviewAllcommentName.setText(data.getNickname());
            if (data.getOtheruid().equals("0")) {
                itemListviewAllarguementContent.setText(data.getCcontent());
            } else {
                String content = "回复 " + data.getOthernickname() + ":" + data.getCcontent();
                TextViewUtil.setSpannedColorText(itemListviewAllarguementContent,content,3,data.getOthernickname().length() + 3, Color.parseColor("#db57f3"));
            }
            itemListviewAllcommentTime.setText(data.getAddtime());
            itemListviewAllcommentIcon.setTag(position);
            itemListviewAllcommentIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SafeCheckUtil.isIndexOurOfBounds(list,position)) {
                        return;
                    }
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("uid", list.get(position).getUid());
                    context.startActivity(intent);
                }
            });

            if (!TextUtil.isEmpty(data.getPic()) || !TextUtil.isEmpty(data.getCoverUrl())) {
                itemListviewAllcommentDynamiciv.setVisibility(View.VISIBLE);
                itemListviewAllcommentDynamiccontent.setVisibility(View.GONE);
                if (TextUtil.isEmpty(data.getPic())) {
                    //GlideImgManager.glideLoader(context, data.getCoverUrl(), R.mipmap.default_error, R.mipmap.default_error, itemListviewAllcommentDynamiciv,1);
                    ImageLoader.loadCircleImage(fragment,list.get(position).getCoverUrl(),itemListviewAllcommentDynamiciv,R.mipmap.morentouxiang);
                } else {
                    //GlideImgManager.glideLoader(context, data.getPic(), R.mipmap.default_error, R.mipmap.default_error, itemListviewAllcommentDynamiciv,1);
                    ImageLoader.loadCircleImage(fragment,list.get(position).getPic(),itemListviewAllcommentDynamiciv,R.mipmap.morentouxiang);
                }
            } else {
                itemListviewAllcommentDynamiciv.setVisibility(View.GONE);
                itemListviewAllcommentDynamiccontent.setVisibility(View.VISIBLE);
                itemListviewAllcommentDynamiccontent.setText(data.getContent());
            }
            llItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemCommentListener != null) {
                        onItemCommentListener.onItemLongClick(position);
                    }
                    return true;
                }
            });
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemCommentListener != null) {
                        onItemCommentListener.onItemClick(position);
                    }
                }
            });
            tvItemApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemCommentListener != null) {
                        onItemCommentListener.onItemApply(position);
                    }
                }
            });
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
