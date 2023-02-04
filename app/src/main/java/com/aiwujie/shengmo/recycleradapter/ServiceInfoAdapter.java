package com.aiwujie.shengmo.recycleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.RedwomenMarkerData;
import com.aiwujie.shengmo.utils.GlideImgManager;

import java.util.ArrayList;
import java.util.List;

public class ServiceInfoAdapter extends RecyclerView.Adapter<ServiceInfoAdapter.ViewHolder>  {
    private OnItemClickListener mOnItemClickListener = null;
    private Context context;
    private List<RedwomenMarkerData.DataBean> list ;

    public ServiceInfoAdapter(Context context, List<RedwomenMarkerData.DataBean> list) {
        this.context = context;

        this.list = list;
    }

    @Override
    public ServiceInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_info,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceInfoAdapter.ViewHolder holder, int position) {

        if (list.get(position).getHead_pic().equals("") || list.get(position).getHead_pic().equals("http://59.110.28.150:888/")) {
            holder.iv_service_head.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, list.get(position).getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang,   holder.iv_service_head, 0);
        }

        holder.tv_service_name.setText(list.get(position).getNickname());

        if ("1".equals(list.get(position).getIs_admin())) {
            holder.ivVipFlag.setImageResource(R.drawable.user_manager);//黑V
        }else if("1".equals(list.get(position).getSvipannual())){
            holder.ivVipFlag.setImageResource(R.drawable.user_svip_year);
        }else if("1".equals(list.get(position).getSvip())){
            holder.ivVipFlag.setImageResource(R.drawable.user_svip);
        }else if("1".equals(list.get(position).getVipannual())){
            holder.ivVipFlag.setImageResource(R.drawable.user_vip_year);
         }else if("1".equals(list.get(position).getVip())){
            holder.ivVipFlag.setImageResource(R.drawable.user_vip);
    }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_service_head,ivVipFlag;
        TextView tv_service_name;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_service_head = itemView.findViewById(R.id.iv_service_head);
            tv_service_name=itemView.findViewById(R.id.tv_service_name);
            ivVipFlag=   itemView.findViewById(R.id.iv_vip_flag);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(v,getPosition());
        }
    }

      public interface OnItemClickListener {
        void onItemClick(View view ,int position);
    }
}


