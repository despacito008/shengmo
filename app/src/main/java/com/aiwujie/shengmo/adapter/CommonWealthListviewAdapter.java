package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.CommonWealthNewsData;
import com.aiwujie.shengmo.utils.GlideImgManager;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/1/12.
 */
public class CommonWealthListviewAdapter extends BaseAdapter {
    private Context context;
    private List<CommonWealthNewsData.DataBean> list;
    private LayoutInflater inflater;

    public CommonWealthListviewAdapter(Context context, List<CommonWealthNewsData.DataBean> list) {
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
            convertView = inflater.inflate(R.layout.item_listview_common_wealth, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommonWealthNewsData.DataBean data=list.get(position);
        holder.itemListviewCommonWealthTv.setText(data.getTitle());
//        if (data.getPic().equals("") || data.getPic().equals("http://59.110.28.150:888/")) {
        if (TextUtil.isEmpty(data.getPic())|| NetPic().equals(data.getPic())) {
            holder.itemListviewCommonWealthIv.setImageResource(R.mipmap.default_error);
        } else {
            GlideImgManager.glideLoader(context, data.getPic(), R.mipmap.default_error, R.mipmap.default_error, holder.itemListviewCommonWealthIv);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_listview_common_wealth_tv)
        TextView itemListviewCommonWealthTv;
        @BindView(R.id.item_listview_common_wealth_iv)
        ImageView itemListviewCommonWealthIv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
