package com.aiwujie.shengmo.timlive.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.tencent.qcloud.tim.tuikit.live.bean.NormalEventBean;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.timlive.adapter.GiftHeatListAdapter;
import com.aiwujie.shengmo.timlive.bean.GiftRankingListInfo;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 小时榜热度榜单
 */
public class GiftRankingListPop extends Dialog {
    @BindView(R.id.iv_item_local)
    TextView ivItemLocal;
    @BindView(R.id.iv_item_live_user_icon)
    ImageView iv_icon;
    @BindView(R.id.iv_item_live_user_identity)
    ImageView itemIdentityIcon;
    @BindView(R.id.tv_item_live_user_name)
    TextView tv_name;
    @BindView(R.id.ll_layout_user_normal_info_sex_age)
    LinearLayout llLayoutUserNormalInfoSexAge;
    @BindView(R.id.iv_layout_user_normal_info_sex)
    ImageView ivLayoutUserNormalInfoSex;
    @BindView(R.id.tv_layout_user_normal_info_age)
    TextView tvUserNormalInfoSexAge;
    @BindView(R.id.tv_layout_user_normal_info_role)
    TextView tvLayoutUserNormalInfoRole;
    @BindView(R.id.tv_layout_user_normal_info_wealth)
    TextView tvLayoutUserNormalInfoWealth;
    @BindView(R.id.iv_layout_user_normal_info_charm)
    ImageView ivLayoutUserNormalInfoCharm;
    @BindView(R.id.tv_layout_user_normal_info_charm)
    TextView tvLayoutUserNormalInfoCharm;
    @BindView(R.id.ll_layout_user_normal_info_charm)
    LinearLayout llLayoutUserNormalInfoCharm;
    @BindView(R.id.ll_layout_user_normal_info_wealth)
    LinearLayout llLayoutUserNormalInfoWealth;
    @BindView(R.id.tv_current_beans)
    TextView tvItemLiveBeansCoin;
    @BindView(R.id.cl_item_local)
    ConstraintLayout constraintLayout;

    private static final String TAG = GiftRankingListPop.class.getSimpleName();
    private static final int LOAD_RANK_DATA = 0x1;
    private static final int LOAD_RANK_DATA_IS_NULL = 0x2;
    @BindView(R.id.tv_item_live_user_send_gift)
    TextView tvItemLiveUserSendGift;
    private LinearLayout noData;
    private Activity context;
    private RecyclerView recycler_label;
    GiftHeatListAdapter.OnItemClickListener onItemClickListener = new GiftHeatListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, GiftRankingListInfo.DataBean.RankingListBean roomInfo) {
            //ToastUtil.show(context,position + " click");
            if ("1".equals(roomInfo.getIs_live())) {
                //ToastUtil.show(context,position + " 去直播间");
                NormalEventBean normalEventBean = new NormalEventBean();
                normalEventBean.setEventData1(roomInfo.getUid());
                normalEventBean.setEventData2(roomInfo.getRoom_id());
                LiveMethodEvent liveMethodEvent = new LiveMethodEvent(LiveEventConstant.GO_TO_OTHER_ANCHOR, "1", GsonUtil.getInstance().toJson(normalEventBean));
                EventBus.getDefault().post(liveMethodEvent);
                if (onPopListener != null) {
                    onPopListener.doPopTopAnchorClick(roomInfo.getUid(),roomInfo.getRoom_id());
                }
            } else {
                // ToastUtil.show(context,position + " 去个人主页");
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("uid", roomInfo.getUid());
                context.startActivity(intent);
            }
        }
    };
    Handler mHandler = new MyHandler(this);
//            new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case LOAD_RANK_DATA:
//                    GiftRankingListInfo giftRankingListInfo = (GiftRankingListInfo) msg.obj;
//                    //热度榜单列表
//                    if(giftRankingListInfo.getData() != null && giftRankingListInfo.getData().getHourRanking()!= null && giftRankingListInfo.getData().getHourRanking().size() > 0){
//                        recycler_label.setVisibility(View.VISIBLE);
//                        noData.setVisibility(View.GONE);
//                        GiftHeatListAdapter adapter = new GiftHeatListAdapter(context, giftRankingListInfo.getData().getHourRanking(), onItemClickListener);
//                        recycler_label.setAdapter(adapter);
//
//                    }
//                    //用户个人信息
//                    if(giftRankingListInfo.getData() != null && giftRankingListInfo.getData().getAnchorInfo() != null){
//                        bind(context,0, giftRankingListInfo.getData().getAnchorInfo(), onItemClickListener);
//                    }
//                    break;
//                case LOAD_RANK_DATA_IS_NULL:
//                    recycler_label.setVisibility(View.GONE);
//                    noData.setVisibility(View.VISIBLE);
//                    break;
//            }
//        }
//    };

    static class MyHandler extends Handler {
        WeakReference<GiftRankingListPop> weakReference;

        public MyHandler(GiftRankingListPop giftRankingListPop) {
            weakReference = new WeakReference<>(giftRankingListPop);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GiftRankingListPop giftRankingListPop = weakReference.get();
            switch (msg.what) {
                case LOAD_RANK_DATA:
                    GiftRankingListInfo giftRankingListInfo = (GiftRankingListInfo) msg.obj;
                    //热度榜单列表
                    if (giftRankingListInfo.getData() != null && giftRankingListInfo.getData().getHourRanking() != null && giftRankingListInfo.getData().getHourRanking().size() > 0) {
                        giftRankingListPop.recycler_label.setVisibility(View.VISIBLE);
                        giftRankingListPop.noData.setVisibility(View.GONE);
                        GiftHeatListAdapter adapter = new GiftHeatListAdapter(giftRankingListPop.context, giftRankingListInfo.getData().getHourRanking(), giftRankingListPop.onItemClickListener);
                        giftRankingListPop.recycler_label.setAdapter(adapter);

                    }
                    //用户个人信息
                    if (giftRankingListInfo.getData() != null && giftRankingListInfo.getData().getAnchorInfo() != null) {
                        giftRankingListPop.bind(giftRankingListPop.context, 0, giftRankingListInfo.getData().getAnchorInfo(), giftRankingListPop.onItemClickListener);
                    }
                    break;
                case LOAD_RANK_DATA_IS_NULL:
                    giftRankingListPop.recycler_label.setVisibility(View.GONE);
                    giftRankingListPop.noData.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public GiftRankingListPop(@NonNull Activity context, int themeResId, String anchor_id) {
        super(context, themeResId);
        this.context = context;
        MyApp.anchor_id = anchor_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_room_click_anchor_head_list);
        ButterKnife.bind(this);
        //代码设置样式优先级高于style
        initParams();
        initView();
        initData(MyApp.anchor_id, "3");
    }

    private void initParams() {
        Window dialogWindow = getWindow();
        dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.mypopwindow_anim_style); //添加动画
        dialogWindow.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_live_room_click_anchor));
        int screenW = UIUtil.getScreenWidth(context);
        int screenH = UIUtil.getScreenHeight(context) / 3 * 2;
        dialogWindow.setLayout(screenW, screenH);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        recycler_label = findViewById(R.id.recycler_label);
        recycler_label.setLayoutManager(new LinearLayoutManager(context));
        noData = findViewById(R.id.no_data);
    }


    //初始化数据
    private void initData(String anchor_id, String type) {
        LiveHttpHelper.getInstance().getHourRank(anchor_id, type, 0, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                LogUtil.e(data);
                GiftRankingListInfo giftRankingListInfo = GsonUtil.GsonToBean(data, GiftRankingListInfo.class);
                if (giftRankingListInfo != null && giftRankingListInfo.getRetcode() == 2000) {
                    Message message = new Message();
                    message.what = LOAD_RANK_DATA;
                    message.obj = giftRankingListInfo;
                    mHandler.sendMessage(message);
                } else {
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

    public void bind(Context context, final int pos, final GiftRankingListInfo.DataBean.RankingListBean data, final GiftHeatListAdapter.OnItemClickListener listener) {
        if (data == null) return;
        //ivItemLocal.setText("" + data.getRanking() + "");
        switch (data.getRanking()) {
            case 1:
                ivItemLocal.setBackgroundResource(R.mipmap.gift_one);
                break;
            case 2:
                ivItemLocal.setBackgroundResource(R.mipmap.gift_two);
                break;
            case 3:
                ivItemLocal.setBackgroundResource(R.mipmap.gift_three);
                break;
            case 0:
                ivItemLocal.setText("-");
                break;
            default:
                ivItemLocal.setText("\t" + data.getRanking() + "");
                break;
        }
        tv_name.setText(!TextUtils.isEmpty(data.getNickname()) ? data.getNickname() : "");
        tvUserNormalInfoSexAge.setText(!TextUtil.isEmpty(data.getAge()) ? data.getAge() : "");
        tvItemLiveUserSendGift.setVisibility(View.VISIBLE);
        if ("1".equals(data.getCharm_val_switch())) {
            llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
            tvLayoutUserNormalInfoCharm.setText("密");
        } else {
            if ("0".equals(data.getCharm_val_new())) {
                llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
            } else {
                llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoCharm.setText(data.getCharm_val_new());
            }
        }

        if ("1".equals(data.getWealth_val_switch())) {
            llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
            tvLayoutUserNormalInfoWealth.setText("密");
        } else {
            if ("0".equals(data.getWealth_val_new())) {
                llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
            } else {
                llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoWealth.setText(data.getWealth_val_new());
            }
        }

//        if (!data.getWealth_val_new().equals("0")) {
//            llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
//            tvLayoutUserNormalInfoWealth.setText(data.getWealth_val_new());
//        } else {
//            llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
//        }

        String coins = context.getString(R.string.live_heat_coins, !TextUtil.isEmpty(data.getCoin() + "") ? data.getCoin() + "" : "");
        tvItemLiveBeansCoin.setText(coins);

        if (data.getSex().equals("1")) {
            llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
        } else if (data.getSex().equals("2")) {
            llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
        } else if (data.getSex().equals("3")) {
            llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
        }
        if (data.getRole().equals("S")) {
            tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            tvLayoutUserNormalInfoRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            tvLayoutUserNormalInfoRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            tvLayoutUserNormalInfoRole.setText("双");
        } else if (data.getRole().equals("~")) {
            tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_other);
            tvLayoutUserNormalInfoRole.setText("~");
        } else {
            tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.bg_user_info_sex_other);
            tvLayoutUserNormalInfoRole.setText("-");
        }

        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), itemIdentityIcon);


        if (!TextUtils.isEmpty(data.getHead_pic())) {
            //GlideEngine.loadImage(iv_icon, data.getHead_pic(), 0, 100);
            GlideEngine.loadCircleImage(iv_icon, data.getHead_pic());
        } else {
            GlideEngine.loadImage(iv_icon, data.getHead_pic(), R.drawable.live_room_default_cover, 100);
        }

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!ClickUtils.isFastClick(v.getId())) {
//                    listener.onItemClick(pos, data);
//                }
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_GIFT_PANEL, "", ""));
                GiftRankingListPop.this.dismiss();

                if (onPopListener != null) {
                    onPopListener.doPopAnchorClick();
                }
            }
        });
    }

    public interface OnPopListener {
        void doPopAnchorClick();
        void doPopTopAnchorClick(String uid,String roomId);
    }

    OnPopListener onPopListener;

    public void setOnPopListener(OnPopListener onPopListener) {
        this.onPopListener = onPopListener;
    }
}
