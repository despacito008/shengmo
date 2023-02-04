package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.BankListData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/1/9.
 */
public class BankListAdapter extends BaseAdapter {
    private Context context;
    private List<BankListData.DataBean> list;
    private LayoutInflater inflater;

    public BankListAdapter(Context context, List<BankListData.DataBean> list) {
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
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.item_listview_bankcard, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.itemListviewBankcardName.setText("姓名: "+list.get(position).getRealname());
        holder.itemListviewBankcardBankname.setText("提现名称: "+list.get(position).getBankname());
        holder.itemListviewBankcardCard.setText("账号: "+list.get(position).getBankcard());
        if ("1".equals(list.get(position).getPay_status())) {
            holder.ivBankType.setImageResource(R.drawable.ic_withdraw_ali_pay);
        } else {
            holder.ivBankType.setImageResource(R.mipmap.yinlian);
        }
//        if ("1".equals(list.get(position).getBank_status())) {
//            holder.ivBankIsDefault.setVisibility(View.VISIBLE);
//        } else {
//            holder.ivBankIsDefault.setVisibility(View.GONE);
//        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_listview_bankcard_name)
        TextView itemListviewBankcardName;
        @BindView(R.id.item_listview_bankcard_bankname)
        TextView itemListviewBankcardBankname;
        @BindView(R.id.item_listview_bankcard_card)
        TextView itemListviewBankcardCard;
        @BindView(R.id.iv_bank_type)
        ImageView ivBankType;
        @BindView(R.id.iv_bank_is_default)
        ImageView ivBankIsDefault;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
