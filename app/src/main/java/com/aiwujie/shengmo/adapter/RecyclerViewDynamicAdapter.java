package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.TopicHeaderData;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

/**
 * Created by 290243232 on 2016/11/23.
 */
public class RecyclerViewDynamicAdapter extends RecyclerView.Adapter<RecyclerViewDynamicAdapter.ViewHolder> {
    private List<TopicHeaderData.DataBean> mDatas;
    private Context context;
    private String[] colors={"#66ff0000","#66b73acb","#660000ff","#6618a153","#66f39700","#66ff00ff","#66ff0000","#66b73acb","#660000ff","#6618a153","#66f39700","#66ff00ff"};
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;

    public RecyclerViewDynamicAdapter(List<TopicHeaderData.DataBean> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }


    /**
     * ItemClick的回调接口
     *
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return new ViewHolder(mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_topic_tv, parent, false);
        return new ViewHolder(layout);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(getItemViewType(position) == TYPE_HEADER) return;
        if(holder instanceof ViewHolder) {
            holder.tvTop.setText("#"+mDatas.get((position-1)%mDatas.size()).getTitle()+"#");

            //如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null) {
                holder.tvTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(holder.itemView, (position-1)%mDatas.size());
                    }
                });
            }
        }

    }


    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTop;
        AutoRelativeLayout autoRelativeLayout;
        public ViewHolder(View arg0)
        {
            super(arg0);
            if(itemView == mHeaderView) return;
            tvTop= (TextView) arg0.findViewById(R.id.item_mygridview_topic_tv);
            autoRelativeLayout = arg0.findViewById(R.id.al_id);
        }

    }
}
