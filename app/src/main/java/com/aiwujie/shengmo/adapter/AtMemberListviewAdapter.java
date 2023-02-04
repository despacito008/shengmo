package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.AtGroupMemberData;
import com.aiwujie.shengmo.utils.GlideImgManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class AtMemberListviewAdapter extends BaseAdapter{
    private Context context;
    private List<AtGroupMemberData.DataBean> list;
    private LayoutInflater inflater;
    private Handler handler = new Handler();

    public AtMemberListviewAdapter(Context context, List<AtGroupMemberData.DataBean> list) {
        super();
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.item_listview_atgroup_member, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AtGroupMemberData.DataBean data = list.get(position);
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemAtGroupMemberIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemAtGroupMemberIcon, 0);
        }
        if (!"".equals(data.getCardname())&&null!=data.getCardname()){
            holder.itemAtGroupMemberName.setText(data.getCardname());
        }else {
            holder.itemAtGroupMemberName.setText(data.getNickname());
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_at_group_member_icon)
        ImageView itemAtGroupMemberIcon;
        @BindView(R.id.item_at_group_member_name)
        TextView itemAtGroupMemberName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


