package com.aiwujie.shengmo.recycleradapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.activity.VipWebActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.WealthQuestionData;
import com.aiwujie.shengmo.http.HttpUrl;

import java.util.List;


public class MoreNewsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<WealthQuestionData.DataBean> mEntityList;

    public MoreNewsAdapter(Context context, List<WealthQuestionData.DataBean> entityList){
        this.mContext = context;
        this.mEntityList = entityList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gridview_two_common_wealth1, parent, false);
        return new DemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final WealthQuestionData.DataBean entity = mEntityList.get(position);
        ((DemoViewHolder)holder).mText.setText(entity.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VipWebActivity.class);
                if ( entity.getUrl_type().equals("0")){

                    intent.putExtra("title", "圣魔");
                    if (!entity.getUrl().equals("")) {
                        intent.putExtra("path", entity.getUrl());
                    } else {
//                intent.putExtra("path", "http://hao.shengmo.org:888/Home/Info/news/id/" + wealthQuestionData.getData().get(position).getId());
                        intent.putExtra("path", HttpUrl.NetPic()+ "Home/Info/news/id/" + entity.getId());

                    }
                }else  if (entity.getUrl_type().equals("1")){

                    intent = new Intent(mContext, DynamicDetailActivity.class);
                    intent.putExtra("uid", MyApp.uid);
                    intent.putExtra("did", entity.getUrl());
                    intent.putExtra("pos", 1);
                    intent.putExtra("showwhat", 1);

                }else  if (entity.getUrl_type().equals("2")){

                    intent = new Intent(mContext, TopicDetailActivity.class);
                    intent.putExtra("tid", entity.getUrl());
                    //intent.putExtra("topictitle", bannerTitle.get(position));

                }else  if ( entity.getUrl_type().equals("3")){

                    intent = new Intent(mContext, PesonInfoActivity.class);
                    intent.putExtra("uid", entity.getUrl());
                }
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEntityList.size();
    }

    private class DemoViewHolder extends RecyclerView.ViewHolder{

        private TextView mText;

        public DemoViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.item_gridview_two_common_wealth_tv);
        }
    }
}