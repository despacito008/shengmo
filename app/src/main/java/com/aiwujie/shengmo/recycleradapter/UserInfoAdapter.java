package com.aiwujie.shengmo.recycleradapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.bean.AccountDeviceData;

import java.util.List;

/**
 * @author：zq 2021/4/12 17:50
 * 邮箱：80776234@qq.com
 */
public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {
    List<AccountDeviceData.Data.DeviceInfo.DeviceUser> list;
    Context context;

    public UserInfoAdapter(Context context, List<AccountDeviceData.Data.DeviceInfo.DeviceUser> list) {
        this.list = list;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public UserInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_info,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserInfoAdapter.ViewHolder holder, int position) {
            holder.tvName.setText(list.get(position).getNickname());
            switch (list.get(position).getSex()){

            case "1":
                holder.tvName.setTextColor(Color.parseColor("#0000ff"));
                break;
            case "2":
                holder.tvName.setTextColor(Color.parseColor("#ff0000"));
                break;
            case "3":
                holder.tvName.setTextColor(Color.parseColor("#b73acb"));
                break;
            default:
                break;
        }
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(holder.getAdapterPosition()).getUid());
                context.startActivity(intent);
            }
        });
            holder.tvRegTime.setText(list.get(position).getReg_time());
            holder.tvNewLoginTime.setText(list.get(position).getNew_login_time());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvRegTime;
        private final TextView tvNewLoginTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvRegTime = itemView.findViewById(R.id.tv_reg_time);
            tvNewLoginTime = itemView.findViewById(R.id.tv_new_login_time);
        }
    }
}
