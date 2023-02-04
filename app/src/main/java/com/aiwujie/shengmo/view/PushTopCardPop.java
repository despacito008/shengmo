package com.aiwujie.shengmo.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.Ejection_Activity;
import com.aiwujie.shengmo.activity.HuojianfeiActivity;
import com.aiwujie.shengmo.adapter.PushTopRecyclerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.EjectionBean;
import com.aiwujie.shengmo.bean.EjectionshengyuBean;
import com.aiwujie.shengmo.bean.PushTopBean;
import com.aiwujie.shengmo.bean.TopcardyesBean;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.TextViewUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

public class PushTopCardPop extends BasePopupWindow {
    Context context;
    int balance = 0;
    int topCardNum = 1;
    @BindView(R.id.view_pop_push_top_bg)
    View viewPopPushTopBg;
    @BindView(R.id.tv_pop_push_top_name)
    TextView tvPopPushTopName;
    @BindView(R.id.tv_pop_push_top_balance)
    TextView tvPopPushTopBalance;
    @BindView(R.id.et_pop_push_top_num)
    EditText etPopPushTopNum;
    @BindView(R.id.tv_pop_push_top_charm)
    TextView tvPopPushTopCharm;
    @BindView(R.id.tv_pop_push_top_time_tips)
    TextView tvPopPushTopTimeTips;
    @BindView(R.id.rv_pop_push_top)
    RecyclerView rvPopPushTop;
    @BindView(R.id.tv_pop_push_top_confirm)
    TextView tvPopPushTopConfirm;
    @BindView(R.id.iv_pop_push_top_open)
    ImageView ivPopPushTopOpen;
    @BindView(R.id.ll_pop_push_top_open)
    LinearLayout llPopPushTopOpen;
    @BindView(R.id.iv_pop_push_top_rocket)
    ImageView ivPopPushTopRocket;
    @BindView(R.id.ll_pop_push_top_rocket)
    LinearLayout llPopPushTopRocket;
    private PushTopRecyclerAdapter topCardAdapter;
    private List<PushTopBean> pushTopBeanList;
    private String interval;
    private String dalaba = "1";
    private String did;
    private int pos;

    public PushTopCardPop(Context context,String did,int pos) {
        super(context);
        this.context = context;
        this.did = did;
        this.pos = pos;
        initView();
        initData();
        initListener();
    }

    @Override
    public View onCreateContentView() {
        View view =  createPopupById(R.layout.layout_pop_push_top);
        ButterKnife.bind(this,view);
        return view;
    }

    void initView() {
//        rvPushTop = findViewById(R.id.rv_pop_push_top);
//        tvBalance = findViewById(R.id.tv_pop_push_top_balance);
//        tvCharm = findViewById(R.id.tv_pop_push_top_charm);
//        tvConfirm = findViewById(R.id.tv_pop_push_top_confirm);
//        etNum = findViewById(R.id.et_pop_push_top_num);
    }

    void initData() {
        getusertopcardinfo();
        pushTopBeanList = new ArrayList<>();
        pushTopBeanList.add(new PushTopBean("5分钟", "300"));
        pushTopBeanList.add(new PushTopBean("10分钟", "600"));
        pushTopBeanList.add(new PushTopBean("15分钟", "900"));
        pushTopBeanList.add(new PushTopBean("20分钟", "1200"));
        pushTopBeanList.add(new PushTopBean("30分钟", "1800"));
        pushTopBeanList.add(new PushTopBean("1小时", "1"));
        pushTopBeanList.add(new PushTopBean("2小时", "7200"));
        pushTopBeanList.add(new PushTopBean("3小时", "3"));
        pushTopBeanList.add(new PushTopBean("6小时", "6"));
        pushTopBeanList.add(new PushTopBean("12小时", "12"));
        pushTopBeanList.add(new PushTopBean("24小时", "24"));
        topCardAdapter = new PushTopRecyclerAdapter(context, pushTopBeanList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        rvPopPushTop.setAdapter(topCardAdapter);
        rvPopPushTop.setLayoutManager(gridLayoutManager);
        updateCharmStr();
        interval = "0";
    }


    //推顶卡余额
    public void getusertopcardinfo() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getTopcardPageInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Gson gson = new Gson();
                EjectionshengyuBean ejectionBean = gson.fromJson(response, EjectionshengyuBean.class);
                balance = Integer.parseInt(ejectionBean.getData().getWallet_topcard());
                String balanceStr = "剩余 " + balance + " 张推顶卡";
                TextViewUtil.setSpannedColorText(tvPopPushTopBalance, balanceStr, 3, 3 + String.valueOf(balance).length(), Color.parseColor("#86a5ff"));
            }


            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    void initListener() {
        etPopPushTopNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtil.isEmpty(s.toString())) {
                    topCardNum = 0;
                } else {
                    topCardNum = Integer.parseInt(s.toString());
                }
                updateCharmStr();
                updateRocket();
                if(topCardNum < 2) {
                    topCardAdapter.changeItem(-1);
                    interval = "0";
                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        topCardAdapter.setOnSimpleItemListener(new OnSimpleItemListener() {

            @Override
            public void onItemListener(int position) {
//                if(topCardNum < 2) {
//                    ToastUtil.show(context,"两张及以上推顶卡可选择推顶间隔时长");
//                } else {
//                    interval = pushTopBeanList.get(position).getInterval();
//                    topCardAdapter.changeItem(position);
//                }
                interval = pushTopBeanList.get(position).getInterval();
                topCardAdapter.changeItem(position);
            }
        });

        llPopPushTopOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dalaba.equals("1")){
                    ivPopPushTopOpen.setImageResource(R.mipmap.yuandiantaohui);
                    dalaba = "2";
                }else {
                    ivPopPushTopOpen.setImageResource(R.mipmap.yuandiantaozi);
                    dalaba = "1";
                }
            }
        });

        tvPopPushTopConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userTopCard();
            }
        });

    }

    void updateCharmStr() {
        int charmNum = topCardNum * 100;
        String charmStr = charmNum + " 魅力值";
        TextViewUtil.setSpannedColorText(tvPopPushTopCharm, charmStr, 0, 0 + String.valueOf(charmNum).length(), Color.parseColor("#86a5ff"));
    }

    void updateRocket() {
        if (topCardNum < 2) {
            ivPopPushTopRocket.setVisibility(View.VISIBLE);
            llPopPushTopRocket.setVisibility(View.GONE);
        } else if (topCardNum == 2) {
            ivPopPushTopRocket.setVisibility(View.GONE);
            llPopPushTopRocket.setVisibility(View.VISIBLE);
        } else {
            ivPopPushTopRocket.setVisibility(View.VISIBLE);
            llPopPushTopRocket.setVisibility(View.VISIBLE);
        }
    }

    void userTopCard() {
        if(balance == 0){
            Intent intent = new Intent(context, Ejection_Activity.class);
            context.startActivity(intent);
            dismiss();
            return;
        }
        if(topCardNum < 1) {
            return;
        }
        if (topCardNum == 1) {
            interval = "0";
        }
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        map.put("num", String.valueOf(topCardNum));
        map.put("interval",interval);
        map.put("dalaba",dalaba);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.useTopcard, map, new IRequestCallback(){

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            Gson gson = new Gson();
                            EjectionBean ejectionBean = gson.fromJson(response, EjectionBean.class);
                            //Toast.makeText(context, ""+ejectionBean.getMsg(), Toast.LENGTH_SHORT).show();
                            if(ejectionBean.getRetcode()==2000){
                                EventBus.getDefault().post(new TopcardyesBean(pos,did));
                                Intent intent = new Intent(context, HuojianfeiActivity.class);
                                intent.putExtra("feishu",Integer.valueOf(topCardNum));
                                if(onPushTopCardSuccessListener != null) {
                                    if ("0".equals(interval)) {
                                        onPushTopCardSuccessListener.onPushTopCardSuc(topCardNum,topCardNum);
                                    } else {
                                        onPushTopCardSuccessListener.onPushTopCardSuc(1,topCardNum);
                                    }
                                }
                                context.startActivity(intent);
                                dismiss();
                            }
                            break;
                        case 2001:
                        case 3000:
                        case 3001:
                        case 4000:
                        case 4001:
                        case 4002:
                        case 4003:
                        case 4004:
                        case 4005:
                        case 4006:
                            ToastUtil.show(context,object.optString("msg"));
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
                            break;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public interface OnPushTopCardSuccessListener {
        void onPushTopCardSuc(int num,int allNum);
    }

    OnPushTopCardSuccessListener onPushTopCardSuccessListener;

    public void setOnPushTopCardSuccessListener(OnPushTopCardSuccessListener onPushTopCardSuccessListener) {
        this.onPushTopCardSuccessListener = onPushTopCardSuccessListener;
    }
}
