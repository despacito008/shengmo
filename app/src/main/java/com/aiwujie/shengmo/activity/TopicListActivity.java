package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.DynamicRecyclerAdapter;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicListActivity extends AppCompatActivity {
    @BindView(R.id.rv_topic_list)
    RecyclerView rvTopicList;
    @BindView(R.id.app_bar_top_list)
    AppBarLayout appBarTopList;
    @BindView(R.id.srl_top_list)
    SmartRefreshLayout srlTopList;
    @BindView(R.id.coord_topic_list)
    CoordinatorLayout coordTopicList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        ButterKnife.bind(this);
        getData();
    }


//    private void getTopicDetail() {
//        Map<String, String> map = new HashMap<>();
//        map.put("tid", topicid);
//        map.put("uid", MyApp.uid);
//        IRequestManager manager = RequestFactory.getRequestManager();
//        manager.post(HttpUrl.GetTopicDetail, map, new IRequestCallback() {
//            @Override
//            public void onSuccess(final String response) {
//
//                        try {
//                            TopicDetailData data = new Gson().fromJson(response, TopicDetailData.class);
//                            isFollowed = data.getData().getIs_follow_topic().equals("1") ? true : false;
//                            if (isFollowed)
//                                mTopicDetailFollowTopic.setImageResource(R.mipmap.quxiaoguanzhu);
//                            else
//                                mTopicDetailFollowTopic.setImageResource(R.mipmap.guanzhuhuati);
//                            String admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
//                            if (data.getData().getIs_admin().equals("1")) {
//                                if (admin.equals("1")) {
//                                    mTopicDetailFollowLayout.setVisibility(View.VISIBLE);
//                                    mTopicDetailListview.setOnTouchListener(TopicDetailActivity.this);
//                                } else {
//                                    mTopicDetailFollowLayout.setVisibility(View.GONE);
//                                }
//                            } else {
//                                mTopicDetailListview.setOnTouchListener(TopicDetailActivity.this);
//                            }
//                            headpic = data.getData().getPic();
////                            if (headpic.equals("") || headpic.equals("http://59.110.28.150:888/")) {
//                            if (headpic.equals("") || headpic.equals(NetPic())) {
//                                mTopicDetailIvbg.setImageResource(R.mipmap.default_error);
//                                mTopicDetailIcon.setImageResource(R.mipmap.default_error);
//                            } else {
//                                GlideImgManager.glideLoader(TopicDetailActivity.this, data.getData().getPic(), R.mipmap.default_error, R.mipmap.default_error, mTopicDetailIvbg);
//                                GlideImgManager.glideLoader(TopicDetailActivity.this, data.getData().getPic(), R.mipmap.default_error, R.mipmap.default_error, mTopicDetailIcon);
//                            }
//                            mTopicDetailName.setText("#" + data.getData().getTitle() + "#");
//                            if ("".equals(topictitle) || null == topictitle) {
//                                topictitle = data.getData().getTitle();
//                            }
//                            mTopicDetailContent.setText(data.getData().getIntroduce());
//                            SpannableStringBuilder builder = new SpannableStringBuilder(data.getData().getReadtimes() + "来访");
//                            ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
//                            builder.setSpan(purSpan, 0, data.getData().getReadtimes().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            mTopicDetailTvVisit.setText(builder);
//                            SpannableStringBuilder builder01 = new SpannableStringBuilder(data.getData().getDynamicnum() + "动态");
//                            builder01.setSpan(purSpan, 0, data.getData().getDynamicnum().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            mTopicDetailTvDynamic.setText(builder01);
//                            SpannableStringBuilder builder02 = new SpannableStringBuilder(data.getData().getPartaketimes() + "参与");
//                            builder02.setSpan(purSpan, 0, data.getData().getPartaketimes().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            mTopicDetailTvJoin.setText(builder02);
//                        } catch (JsonSyntaxException e) {
//                            e.printStackTrace();
//                        }
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//
//            }
//        });
//    }


    void getData() {
        String response = SharedPreferencesUtils.getParam(TopicListActivity.this, "tempData", "").toString();
        final DynamicListData listData = new Gson().fromJson(response, DynamicListData.class);
        List<DynamicListData.DataBean> dynamics = listData.getData();
        int retcode = 2000;
        DynamicRecyclerAdapter dynamicRecyclerAdapter = new DynamicRecyclerAdapter(TopicListActivity.this, dynamics, retcode, "2");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TopicListActivity.this);
        rvTopicList.setAdapter(dynamicRecyclerAdapter);
        rvTopicList.setLayoutManager(linearLayoutManager);


    }
}
