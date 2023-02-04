package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.FollowMsgData;
import com.aiwujie.shengmo.utils.GlideImgManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/1/18.
 */
public class FollowMsgAdapter extends BaseAdapter  {
    private Context context;
    private List<FollowMsgData.DataBean> list;
    private LayoutInflater inflater;

    public FollowMsgAdapter(Context context, List<FollowMsgData.DataBean> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview_follow_msg, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FollowMsgData.DataBean data = list.get(position);
        if (list.get(position).getHead_pic().equals("") || list.get(position).getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemListviewFollowMsgIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, list.get(position).getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewFollowMsgIcon, 0);
        }
        holder.itemListviewFollowMsgName.setText(data.getNickname());
        holder.itemListviewFollowMsgTime.setText(data.getAddtime());
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_listview_follow_msg_icon)
        ImageView itemListviewFollowMsgIcon;
        @BindView(R.id.item_listview_follow_msg_name)
        TextView itemListviewFollowMsgName;
        @BindView(R.id.item_listview_follow_msg_time)
        TextView itemListviewFollowMsgTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
