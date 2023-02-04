package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.ListTopicData;
import com.aiwujie.shengmo.utils.GlideRoundTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/7/18.
 */

public class TopicAdapter extends BaseAdapter {
    private Context context;
    private List<ListTopicData.DataBean> list;
    private LayoutInflater inflater;
    private String colors[] = {"#ff0000", "#b73acb", "#0000ff", "#18a153", "#f39700", "#ff00ff", "#00a0e9"};

    public TopicAdapter(Context context, List<ListTopicData.DataBean> list) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview_topic_sort, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ListTopicData.DataBean data = list.get(position);
        holder.itemTopicSortName.setText("#" + data.getTitle() + "#");
        holder.itemTopicSortContent.setText(data.getIntroduce());
        holder.itemTopicSortPeopleCount.setText(data.getPartaketimes() + "人参与");
        holder.itemTopicSortDynamicCount.setText(data.getDynamicnum() + "动态");
        if (TextUtil.isEmpty(data.getPic()) || NetPic().equals(data.getPic())) {//"http://59.110.28.150:888/"
            holder.itemTopicSortIcon.setImageResource(R.mipmap.default_error);
        } else {
           // GlideImgManager.glideLoader(context, data.getPic(), R.mipmap.default_error, R.mipmap.default_error, holder.itemTopicSortIcon,1);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.default_error);
            requestOptions.error(R.mipmap.default_error);
            requestOptions.transform(new GlideRoundTransform(context,16));
            Glide.with(context).load(Uri.parse( data.getPic())).apply(requestOptions).into(holder.itemTopicSortIcon);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_topic_sort_icon)
        ImageView itemTopicSortIcon;
        @BindView(R.id.item_topic_sort_name)
        TextView itemTopicSortName;
        @BindView(R.id.item_topic_sort_content)
        TextView itemTopicSortContent;
        @BindView(R.id.item_topic_sort_peopleCount)
        TextView itemTopicSortPeopleCount;
        @BindView(R.id.item_topic_sort_dynamicCount)
        TextView itemTopicSortDynamicCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
