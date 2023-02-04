package com.aiwujie.shengmo.timlive.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.timlive.adapter.GiftViewPagerAdapter;
import com.aiwujie.shengmo.timlive.bean.GiftCoinListInfo;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;

/**
 * 礼物星榜
 */
public class GiftViewPagerPop extends Dialog implements View.OnClickListener {
    private static final String TAG = GiftViewPagerPop.class.getSimpleName();
    private static final int LOAD_RANK_DATA = 0x1;
    private static final int LOAD_RANK_DATA_IS_NULL = 0x2;
    private TextView mFragmentLive_title_local;
    private TextView mFragmentLive_title_week;
    private TextView mFragmentLive_title_month;
    private ImageView mFragmentLive_local_dian;
    private ImageView mFragmentLive_week_dian;
    private ImageView mFragmentLive_month_dian;
    private LinearLayout noData;
    private Activity context;
    private RecyclerView recycler_label;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOAD_RANK_DATA:
                    GiftCoinListInfo giftListInfo = (GiftCoinListInfo) msg.obj;
                    if(giftListInfo != null && giftListInfo.getRetcode() == 2000){
                        recycler_label.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        GiftViewPagerAdapter adapter = new GiftViewPagerAdapter(context, giftListInfo.getData().getRankingList(), new GiftViewPagerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position, GiftCoinListInfo.DataBean.RankingListBean data) {
//                                if (onPopListener != null) {
//                                    onPopListener.doItemClick(data.getUid());
//                                }
                                Intent intent = new Intent(context, UserInfoActivity.class);
                                intent.putExtra("uid", data.getUid());
                                context.startActivity(intent);
                            }
                        });
                        recycler_label.setAdapter(adapter);
                        if(adapter != null){
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case LOAD_RANK_DATA_IS_NULL:
                    recycler_label.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    public GiftViewPagerPop(@NonNull Activity context, int themeResId,String anchor_id) {
        super(context, themeResId);
        this.context = context;
        MyApp.anchor_id = anchor_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_room_click_anchor_gift_list);
        //代码设置样式优先级高于style
        initParams();
        initView();
        initData(MyApp.anchor_id,"1"); //默认本场
    }

    private void initParams() {
        Window dialogWindow = getWindow();
        dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialogWindow.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_live_room_click_anchor));
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.mypopwindow_anim_style); //添加动画
        int screenW = UIUtil.getScreenWidth(context);
        int screenH = UIUtil.getScreenHeight(context) / 3 * 2;
        dialogWindow.setLayout(screenW,screenH);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        recycler_label = findViewById(R.id.recycler_label);
        recycler_label.setLayoutManager(new LinearLayoutManager(getContext()));
        noData = findViewById(R.id.no_data);
        mFragmentLive_title_local = findViewById(R.id.mFragmentLive_title_this_field); //本场
        mFragmentLive_title_week = findViewById(R.id.mFragmentLive_title_week_list); //周榜
        mFragmentLive_title_month = findViewById(R.id.mFragmentLive_title_monthly_list); //月榜
        mFragmentLive_local_dian = findViewById(R.id.mFragmentLive_local_dian);
        mFragmentLive_week_dian = findViewById(R.id.mFragmentLive_week_dian);
        mFragmentLive_month_dian = findViewById(R.id.mFragmentLive_month_dian);

        mFragmentLive_title_local.setSelected(true);
        mFragmentLive_title_local.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        TextPaint tp = mFragmentLive_title_local.getPaint();
        tp.setFakeBoldText(true);
        mFragmentLive_title_local.setVisibility(View.VISIBLE);
        mFragmentLive_title_local.setOnClickListener(this);
        mFragmentLive_title_month.setOnClickListener(this);
        mFragmentLive_title_week.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mFragmentLive_title_this_field:
                initData(MyApp.anchor_id,"1");
                chooseTab(mFragmentLive_local_dian,mFragmentLive_title_local);
                unChooseTab(mFragmentLive_week_dian,mFragmentLive_title_week);
                unChooseTab(mFragmentLive_month_dian,mFragmentLive_title_month);
                break;
            case R.id.mFragmentLive_title_week_list:
                initData(MyApp.anchor_id,"4");
                unChooseTab(mFragmentLive_local_dian,mFragmentLive_title_local);
                chooseTab(mFragmentLive_week_dian,mFragmentLive_title_week);
                unChooseTab(mFragmentLive_month_dian,mFragmentLive_title_month);
                break;
            case R.id.mFragmentLive_title_monthly_list:
                initData(MyApp.anchor_id,"2");
                unChooseTab(mFragmentLive_local_dian,mFragmentLive_title_local);
                unChooseTab(mFragmentLive_week_dian,mFragmentLive_title_week);
                chooseTab(mFragmentLive_month_dian,mFragmentLive_title_month);
                break;
        }
    }

    //初始化数据
    private void initData(String anchor_id, String type) {
        LiveHttpHelper.getInstance().getGiftRank(anchor_id, type, 0, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                LogUtil.e(data);
                GiftCoinListInfo giftListInfo = GsonUtil.GsonToBean(data, GiftCoinListInfo.class);
                if(giftListInfo.getData() != null && giftListInfo.getData().getRankingList() != null && giftListInfo.getData().getRankingList().size() > 0){
                    Message message = new Message();
                    message.what = LOAD_RANK_DATA;
                    message.obj = giftListInfo;
                    mHandler.sendMessage(message);
                }else{
                    mHandler.sendEmptyMessage(LOAD_RANK_DATA_IS_NULL);
                }
            }

            @Override
            public void onFail(String msg) {
                LogUtil.e(msg);
                mHandler.sendEmptyMessage(LOAD_RANK_DATA_IS_NULL);
            }
        });
    }

    public interface OnPopListener {
        void doItemClick(String uid);
    }

    OnPopListener onPopListener;

    public void setOnPopListener(OnPopListener onPopListener) {
        this.onPopListener = onPopListener;
    }

    void chooseTab(ImageView iv,TextView tv) {
        iv.setVisibility(View.VISIBLE);
        tv.setSelected(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        tv.getPaint().setFakeBoldText(true);
    }

    void unChooseTab(ImageView iv,TextView tv) {
        iv.setVisibility(View.GONE);
        tv.setSelected(false);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        tv.getPaint().setFakeBoldText(false);
    }
}
