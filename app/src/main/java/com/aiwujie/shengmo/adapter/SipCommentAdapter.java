package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.DynamicListData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/1/12.
 */
public class SipCommentAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<DynamicListData.DataBean.ComArrBean> list;
    private LayoutInflater inflater;

    public SipCommentAdapter(Context context, List<DynamicListData.DataBean.ComArrBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            convertView = inflater.inflate(R.layout.item_listview_sipcomment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DynamicListData.DataBean.ComArrBean data = list.get(position);
        holder.itemListviewSipcommentName.setText(data.getNickname()+":");
        if(data.getOtheruid().equals("0")) {
            holder.itemListviewSipcommentContent.setText(data.getContent());
        }else{
            holder.itemListviewSipcommentContent.setText("回复  "+data.getOthernickname()+":"+data.getContent());
        }

        holder.itemListviewSipcommentName.setTag(position);
        holder.itemListviewSipcommentName.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos= (int) v.getTag();

    }

    static class ViewHolder {
        @BindView(R.id.item_listview_sipcomment_name)
        TextView itemListviewSipcommentName;
        @BindView(R.id.item_listview_sipcomment_content)
        TextView itemListviewSipcommentContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
