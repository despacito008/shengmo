package com.aiwujie.shengmo.recycleradapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.LocationInfo;

import java.util.ArrayList;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * @author：zq 2021/4/12 15:36
 * 邮箱：80776234@qq.com
 */
public class HistoryAddressAdapter extends RecyclerView.Adapter<HistoryAddressAdapter.ViewHolder> {
    ArrayList<LocationInfo> locationInfoList;
    Context context;

    public HistoryAddressAdapter(Context context, ArrayList<LocationInfo> locationInfoList) {
        this.locationInfoList = locationInfoList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append("地址：" + (TextUtils.isEmpty(locationInfoList.get(position).getAddr()) ? "暂无" : locationInfoList.get(position).getAddr()));
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#000000"));
        spannableStringBuilder.setSpan(foregroundColorSpan, 3, spannableStringBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvAddress.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tvAddress.setText(spannableStringBuilder);
        holder.tvEnterTime.setText(locationInfoList.get(position).getAddtime());
        holder.tvOutTime.setText(locationInfoList.get(position).getEndtime());
    }


    @Override
    public int getItemCount() {

        return locationInfoList.size();


    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAddress, tvEnterTime, tvOutTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvEnterTime = itemView.findViewById(R.id.tv_enter_time);
            tvOutTime = itemView.findViewById(R.id.tv_out_time);

        }
    }


}
