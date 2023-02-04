package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.bean.TopCardListDataBean;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/1/9.
 */
public class TopCardListAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<TopCardListDataBean.DataBean> list;
    private LayoutInflater inflater;
    private int[] vips = {R.mipmap.vip, R.mipmap.vip, R.mipmap.vip, R.mipmap.vipnian};
    private int[] svips={R.mipmap.svip, R.mipmap.svip, R.mipmap.svip, R.mipmap.svipnian};
    public TopCardListAdapter(Context context, List<TopCardListDataBean.DataBean> list) {
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
            convertView = inflater.inflate(R.layout.item_listview_notice_topcard, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TopCardListDataBean.DataBean data = list.get(position);
        String vipContent = "";
            //holder.itemListviewNoticeAutoLlbg.setBackgroundResource(R.mipmap.songlibeijing);
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemListviewNoticeVipSendicon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewNoticeVipSendicon, 0);
        }
        if (data.getFhead_pic().equals("") || data.getFhead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemListviewNoticeVipOthericon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getFhead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewNoticeVipOthericon, 0);
        }
        holder.itemListviewNoticeVipSendname.setText(data.getNickname());
        holder.itemListviewNoticeVipReceivename.setText(data.getFnickname());
        holder.itemListviewNoticeVipName.setText(vipContent);
        holder.itemListviewNoticeVipSendtime.setText(data.getAddtime());
        holder.itemListviewNoticeVipSendicon.setTag(position);
        holder.itemListviewNoticeVipSendicon.setOnClickListener(this);
        holder.itemListviewNoticeVipOthericon.setTag(position);
        holder.itemListviewNoticeVipOthericon.setOnClickListener(this);
        holder.itemListviewNoticeVipIcon.setImageResource(R.drawable.ic_dynamic_detail_rocket);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        Intent intent;
        switch (v.getId()) {
            case R.id.item_listview_notice_vip_sendicon:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getUid());
                context.startActivity(intent);
                break;
            case R.id.item_listview_notice_vip_othericon:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getFuid());
                context.startActivity(intent);
                break;
        }
    }

    static class ViewHolder {
        @BindView(R.id.item_listview_notice_vip_sendname)
        TextView itemListviewNoticeVipSendname;
        @BindView(R.id.item_listview_notice_vip_receivename)
        TextView itemListviewNoticeVipReceivename;
        @BindView(R.id.item_listview_notice_vip_name)
        TextView itemListviewNoticeVipName;
        @BindView(R.id.item_listview_notice_vip_sendicon)
        ImageView itemListviewNoticeVipSendicon;
        @BindView(R.id.item_listview_notice_vip_icon)
        ImageView itemListviewNoticeVipIcon;
        @BindView(R.id.item_listview_notice_vip_othericon)
        ImageView itemListviewNoticeVipOthericon;
        @BindView(R.id.item_listview_notice_vip_sendtime)
        TextView itemListviewNoticeVipSendtime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
