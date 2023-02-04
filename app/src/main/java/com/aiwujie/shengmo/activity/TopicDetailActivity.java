package com.aiwujie.shengmo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.newui.TopicDynamicActivity;
import com.aiwujie.shengmo.adapter.DynamicListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.bean.TopicDetailData;
import com.aiwujie.shengmo.customview.BindPhoneAlertDialog;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.eventbus.DynamicSxEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gyf.immersionbar.ImmersionBar;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class TopicDetailActivity extends AppCompatActivity implements AbsListView.OnScrollListener, View.OnTouchListener, View.OnClickListener {

    @BindView(R.id.mTopic_detail_return)
    ImageView mTopicDetailReturn;
    @BindView(R.id.mTopic_detail_progressbar)
    ProgressBar mTopicDetailProgressbar;
    @BindView(R.id.mTopic_detail_ll_title)
    PercentRelativeLayout mTopicDetailLlTitle;
    @BindView(R.id.mTopic_detail_ll_tabs)
    PercentLinearLayout mTopicDetailLlTabs;
    @BindView(R.id.mTopic_detail_framelayout)
    PercentFrameLayout mTopicDetailFramelayout;
    @BindView(R.id.mTopic_detail_tv01)
    TextView mTopicDetailTv01;
    @BindView(R.id.mTopic_detail_tv02)
    TextView mTopicDetailTv02;
    @BindView(R.id.mTopicDetail_joinTopic)
    ImageView mTopicDetailJoinTopic;
    @BindView(R.id.mTopicDetail_followTopic)
    ImageView mTopicDetailFollowTopic;
    @BindView(R.id.mTopicDetail_joinTopic_layout)
    PercentLinearLayout mTopicDetailFollowLayout;
    @BindView(R.id.mTopic_detail_listview)
    ListView mTopicDetailListview;
    @BindView(R.id.mFragment_dynamic_hot_top)
    ImageView mFragmentDynamicHotTop;
    @BindView(R.id.mFragmentDynamic_line)
    TextView mFragmentDynamicLine;
    @BindView(R.id.mFragmentDynamic_Sx)
    ImageView mFragmentDynamicSx;
    //判断是否可以发送动态
    private boolean isSend = false;
    private String toastStr;
    private String topicid;
    private String topictitle;
    String lastid = "";
    private Handler handler = new Handler();
    private int page = 0;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    List<DynamicListData.DataBean> dynamics = new ArrayList<>();
    private DynamicListviewAdapter dynamicAdapter;
    private ImageView mTopicDetailIvbg;
    private ImageView mTopicDetailIcon;
    private TextView mTopicDetailName;
    private TextView mTopicDetailContent;
    private TextView mTopicDetailTvVisit;
    private TextView mTopicDetailTvDynamic;
    private TextView mTopicDetailTvJoin;
    private int flag = 1;
    private AlertDialog dialog;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;
    private TextView seeOrNotTv01;
    private TextView seeOrNotTv02;
    private View bgNew,bgHot;
    private int dynamicRetcode;
    private String headpic;
    private int topicState;
    private boolean isFollowed = false;

    /**
     * 筛选
     */
    private PopupWindow mPopWindow;
    private String dynamicSex;
    private String dynamicSexual;
    //vip  1.为vip 0.不是vip
    private String vipflag;
    private String sex = "";
    private String sexual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
//        StatusBarUtil.showLightStatusBar(this,R.id.mTopic_detail_ll_title);
//        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
//        sex= (String) SharedPreferencesUtils.getParam(this,"dynamicSex","");
//        sexual = (String) SharedPreferencesUtils.getParam(this,"dynamicSexual","");
//        initView();
//        getTopicDetail();
//        //newDynamic();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                lastid = "";
//                getDynamicList(flag);
//            }
//        }, 300);
        gotoNew();
    }

    void gotoNew() {
        topicid = getIntent().getStringExtra("tid");
        topictitle = getIntent().getStringExtra("topictitle");
        topicState = getIntent().getIntExtra("topicState", -1);
        Intent intent = new Intent(TopicDetailActivity.this, TopicDynamicActivity.class);
        intent.putExtra("tid",topicid);
        intent.putExtra("topictitle", topictitle);
        intent.putExtra("topicState", topictitle);
        startActivity(intent);
        finish();
    }

    /**
     * 初始化View
     */
    private void initView() {
        topicid = getIntent().getStringExtra("tid");
        topictitle = getIntent().getStringExtra("topictitle");
        topicState = getIntent().getIntExtra("topicState", -1);
        mTopicDetailFollowLayout.animate().alpha(1).setStartDelay(200).start();
        mTopicDetailFramelayout.animate().alpha(1).setStartDelay(200).start();
        mTopicDetailListview.setFocusable(false);
        mTopicDetailTv01.setSelected(true);
        //下面设置悬浮和头顶部分内容
        final View header = View.inflate(this, R.layout.item_topic_listview_header_along, null);//头部内容,会隐藏的部分
        mTopicDetailListview.addHeaderView(header);//添加头部
        mTopicDetailIvbg = (ImageView) header.findViewById(R.id.mTopic_detail_ivbg);
        mTopicDetailIcon = (ImageView) header.findViewById(R.id.mTopic_detail_icon);
        mTopicDetailName = (TextView) header.findViewById(R.id.mTopic_detail_name);
        mTopicDetailContent = (TextView) header.findViewById(R.id.mTopic_detail_content);
        mTopicDetailTvVisit = (TextView) header.findViewById(R.id.mTopic_detail_tvVisit);
        mTopicDetailTvDynamic = (TextView) header.findViewById(R.id.mTopic_detail_tvDynamic);
        mTopicDetailTvJoin = (TextView) header.findViewById(R.id.mTopic_detail_tvJoin);
        //final View header2 = View.inflate(this, R.layout.item_topic_listview_header_see_or_not, null);//头部内容,一直显示的部分
        //mTopicDetailListview.addHeaderView(header2);//添加头部
        seeOrNotTv01 = (TextView) header.findViewById(R.id.item_topic_listview_header_tv01);
        seeOrNotTv02 = (TextView) header.findViewById(R.id.item_topic_listview_header_tv02);
        bgNew = header.findViewById(R.id.item_topic_listview_header_bg01);
        bgHot = header.findViewById(R.id.item_topic_listview_header_bg02);
        seeOrNotTv01.setSelected(true);
        seeOrNotTv01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDynamic();
            }
        });
        seeOrNotTv02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotDynamic();
            }
        });
        mTopicDetailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> ivlist = new ArrayList<>();
                ivlist.add(headpic);
                Intent intent = new Intent(TopicDetailActivity.this, ZoomActivity.class);
                intent.putExtra("pics", ivlist);
                intent.putExtra("position", 0);
                startActivity(intent);
            }
        });
        mTopicDetailListview.setOnScrollListener(this);
        mFragmentDynamicSx.setOnClickListener(this);

    }

    private void getTopicDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("tid", topicid);
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetTopicDetail, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TopicDetailData data = new Gson().fromJson(response, TopicDetailData.class);
                            isFollowed = data.getData().getIs_follow_topic().equals("1") ? true : false;
                            if (isFollowed)
                                mTopicDetailFollowTopic.setImageResource(R.mipmap.quxiaoguanzhu);
                            else
                                mTopicDetailFollowTopic.setImageResource(R.mipmap.guanzhuhuati);
                            String admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
                            if (data.getData().getIs_admin().equals("1")) {
                                if (admin.equals("1")) {
                                    mTopicDetailFollowLayout.setVisibility(View.VISIBLE);
                                    mTopicDetailListview.setOnTouchListener(TopicDetailActivity.this);
                                } else {
                                    mTopicDetailFollowLayout.setVisibility(View.GONE);
                                }
                            } else {
                                mTopicDetailListview.setOnTouchListener(TopicDetailActivity.this);
                            }
                            headpic = data.getData().getPic();
                            if (headpic.equals("") || headpic.equals(NetPic())) {
                                mTopicDetailIvbg.setImageResource(R.mipmap.default_error);
                                mTopicDetailIcon.setImageResource(R.mipmap.default_error);
                            } else {
                                GlideImgManager.glideLoader(TopicDetailActivity.this, data.getData().getPic(), R.mipmap.default_error, R.mipmap.default_error, mTopicDetailIvbg);
                                GlideImgManager.glideLoader(TopicDetailActivity.this, data.getData().getPic(), R.mipmap.default_error, R.mipmap.default_error, mTopicDetailIcon,1);
                            }
                            mTopicDetailName.setText("#" + data.getData().getTitle() + "#");
                            if ("".equals(topictitle) || null == topictitle) {
                                topictitle = data.getData().getTitle();
                            }
                            mTopicDetailContent.setText(data.getData().getIntroduce());
                            SpannableStringBuilder builder = new SpannableStringBuilder(data.getData().getReadtimes() + "来访");
                            ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#ffffff"));
                            builder.setSpan(purSpan, 0, data.getData().getReadtimes().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mTopicDetailTvVisit.setText(builder);
                            SpannableStringBuilder builder01 = new SpannableStringBuilder(data.getData().getDynamicnum() + "动态");
                            builder01.setSpan(purSpan, 0, data.getData().getDynamicnum().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mTopicDetailTvDynamic.setText(builder01);
                            SpannableStringBuilder builder02 = new SpannableStringBuilder(data.getData().getPartaketimes() + "参与");
                            builder02.setSpan(purSpan, 0, data.getData().getPartaketimes().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mTopicDetailTvJoin.setText(builder02);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void hotDynamic() {
        showdialog();
        seeOrNotTv02.setSelected(true);
        seeOrNotTv01.setSelected(false);
        mTopicDetailTv02.setSelected(true);
        mTopicDetailTv01.setSelected(false);
        seeOrNotTv01.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        seeOrNotTv02.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        mTopicDetailTv02.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        mTopicDetailTv01.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        seeOrNotTv01.setTypeface(Typeface.DEFAULT);
        seeOrNotTv02.setTypeface(Typeface.DEFAULT_BOLD);
        mTopicDetailTv01.setTypeface(Typeface.DEFAULT);
        mTopicDetailTv02.setTypeface(Typeface.DEFAULT_BOLD);
        bgHot.setVisibility(View.VISIBLE);
        bgNew.setVisibility(View.GONE);
        page = 0;
        dynamics.clear();
        dynamicAdapter.notifyDataSetChanged();
        flag = 0;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastid = "";
                getDynamicList(flag);
            }
        }, 300);
    }

    private void newDynamic() {
        showdialog();
        seeOrNotTv02.setSelected(false);
        seeOrNotTv01.setSelected(true);
        mTopicDetailTv02.setSelected(false);
        mTopicDetailTv01.setSelected(true);
        seeOrNotTv01.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        seeOrNotTv02.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        mTopicDetailTv02.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        mTopicDetailTv01.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        seeOrNotTv02.setTypeface(Typeface.DEFAULT);
        seeOrNotTv01.setTypeface(Typeface.DEFAULT_BOLD);
        mTopicDetailTv02.setTypeface(Typeface.DEFAULT);
        mTopicDetailTv01.setTypeface(Typeface.DEFAULT_BOLD);
        bgHot.setVisibility(View.GONE);
        bgNew.setVisibility(View.VISIBLE);
        page = 0;
        dynamics.clear();
        dynamicAdapter.notifyDataSetChanged();
        flag = 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastid = "";
                getDynamicList(flag);
            }
        }, 300);
    }

    @OnClick({R.id.mTopic_detail_tv01, R.id.mTopic_detail_tv02, R.id.mTopic_detail_return, R.id.mTopicDetail_joinTopic, R.id.mTopicDetail_followTopic, R.id.mFragment_dynamic_hot_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mTopic_detail_return:
                finish();
                break;
            case R.id.mTopicDetail_joinTopic:
                if (dynamicRetcode != 3001) {
                    if (isSend) {
                        Intent intent = new Intent(this, SendDynamicActivity.class);
                        intent.putExtra("topicid", topicid);
                        intent.putExtra("topictitle", topictitle);
                        intent.putExtra("topicState", topicState);
                        startActivity(intent);
                    } else {
                        ToastUtil.show(getApplicationContext(), toastStr);
                    }
                } else {
                    BindPhoneAlertDialog.bindAlertDialog(this, toastStr);
                }
                break;
            case R.id.mTopicDetail_followTopic:
                if (!isFollowed) {
                    followTopic();
                } else {
                    cancleFollowTopic();
                }
                break;
            case R.id.mTopic_detail_tv01:
                newDynamic();
                break;
            case R.id.mTopic_detail_tv02:
                hotDynamic();
                break;
            case R.id.mFragment_dynamic_hot_top:
                mTopicDetailListview.setSelection(0);
                mFragmentDynamicHotTop.setVisibility(View.GONE);
                break;
        }
    }

    private void showOption() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_dynamic_pop, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setFocusable(true);
        backgroundAlpha(0.5f);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOnDismissListener(new poponDismissListener());
        //mPopWindow.showAsDropDown(mFragmentDynamicLine);
        mPopWindow.showAsDropDown(mFragmentDynamicSx);
        this.mPopWindow.setAnimationStyle(R.style.AnimationPreview);
        TextView llNan = (TextView) contentView.findViewById(R.id.item_dynamic_pop_llNan);
        TextView llNv = (TextView) contentView.findViewById(R.id.item_dynamic_pop_llNv);
        TextView llNao = (TextView) contentView.findViewById(R.id.item_dynamic_pop_llCdts);
        TextView llAll = (TextView) contentView.findViewById(R.id.item_dynamic_pop_llAll);
        TextView tvSexual = (TextView) contentView.findViewById(R.id.item_dynamic_pop_tvSexual);
        PercentLinearLayout llSexual = (PercentLinearLayout) contentView.findViewById(R.id.item_dynamic_pop_llSexual);
        String sex = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "dynamicSex", "");
        String sexual = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "dynamicSexual", "");
        String dynamicSexual = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "dynamicSxSexual", "");
        vipflag = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
        if (sex.equals("") && sexual.equals("")) {
            llAll.setTextColor(Color.parseColor("#b73acb"));
        }
        if (sex.equals("1") && sexual.equals("")) {
            llNan.setTextColor(Color.parseColor("#b73acb"));
        }
        if (sex.equals("2") && sexual.equals("")) {
            llNv.setTextColor(Color.parseColor("#b73acb"));
        }
        if (sex.equals("3") && sexual.equals("")) {
            llNao.setTextColor(Color.parseColor("#b73acb"));
        }
        if (dynamicSexual.equals("1")) {
            tvSexual.setTextColor(Color.parseColor("#b73acb"));
        }
        llNan.setOnClickListener(this);
        llNv.setOnClickListener(this);
        llAll.setOnClickListener(this);
        llNao.setOnClickListener(this);
        llSexual.setOnClickListener(this);
    }



    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void dynamicSx(String dynamicSex, String dynamicSexual, boolean isOpen) {
        if (isOpen) {
            SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSwitch", "1");
            mFragmentDynamicSx.setImageResource(R.mipmap.shuaixuanlv);
        } else {
            SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSwitch", "0");
            mFragmentDynamicSx.setImageResource(R.mipmap.popshaixuan);
        }
        SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSex", dynamicSex);
        SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSexual", dynamicSexual);
        EventBus.getDefault().post(new DynamicSxEvent(dynamicSex, dynamicSexual));
        mPopWindow.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DynamicSxEvent data) {
        page = 0;
        sex = data.getSex();
        sexual = data.getSexual();
        dynamics.clear();
        if (dynamicAdapter != null) {
            dynamicAdapter.notifyDataSetChanged();
        }

        lastid = "";
        getDynamicList(flag);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_dynamic_pop_llAll:
                if (vipflag.equals("1")) {
                    dynamicSex = "";
                    dynamicSexual = "";
                    dynamicSx(dynamicSex, dynamicSexual, false);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSxSexual", "0");
                    mFragmentDynamicSx.setImageResource(R.mipmap.popshaixuan);
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
                break;
            case R.id.item_dynamic_pop_llNan:
                if (vipflag.equals("1")) {
                    dynamicSex = "1";
                    dynamicSexual = "";
                    dynamicSx(dynamicSex, dynamicSexual, true);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSxSexual", "0");
                    //设置沙漏为紫色
//                    mFragmentDynamicSx.setImageResource(R.mipmap.shaixuan_zise);
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
                break;
            case R.id.item_dynamic_pop_llNv:
                if (vipflag.equals("1")) {
                    dynamicSex = "2";
                    dynamicSexual = "";
                    dynamicSx(dynamicSex, dynamicSexual, true);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSxSexual", "0");
                    //设置沙漏为紫色
//                    mFragmentDynamicSx.setImageResource(R.mipmap.shaixuan_zise);
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
                break;
            case R.id.item_dynamic_pop_llCdts:
                if (vipflag.equals("1")) {
                    dynamicSex = "3";
                    dynamicSexual = "";
                    dynamicSx(dynamicSex, dynamicSexual, true);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSxSexual", "0");
                    //设置沙漏为紫色
//                    mFragmentDynamicSx.setImageResource(R.mipmap.shaixuan_zise);
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
                break;
            case R.id.item_dynamic_pop_llSexual:
                if (vipflag.equals("1")) {
                    dynamicSex = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "mydynamicSex", "");
                    dynamicSexual = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "mydynamicSexual", "");
                    if (!dynamicSex.equals("") && !dynamicSexual.equals("")) {
                        SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSxSexual", "1");
                        dynamicSx(dynamicSex, dynamicSexual, true);
                        //设置沙漏为紫色
//                        mFragmentDynamicSx.setImageResource(R.mipmap.shaixuan_zise);
                    } else {
                        ToastUtil.show(getApplicationContext(), "请您重新登录,才可以使用此功能...");
                    }
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
                break;
            case R.id.mFragmentDynamic_Sx:
                showOption();
                break;
        }
    }

    public void vipDialog(String toastStr) {
        new VipDialog(this, toastStr);
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    private void getJudgeDynamic() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.JudgeDynamicNewrd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(response);
                            dynamicRetcode = json.getInt("retcode");
                            switch (dynamicRetcode) {
                                case 2000:
                                    isSend = true;
                                    break;
                                case 3001:
                                    isSend = false;
                                    toastStr = json.getString("msg");
                                    break;
                                case 4003:
                                case 4004:
                                    isSend = false;
                                    toastStr = json.getString("msg");
                                    break;
                                case 5000:
                                    isSend = false;
                                    toastStr = json.getString("msg");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void followTopic() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("tid", topicid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.FollowTopic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(response);
                            dynamicRetcode = json.getInt("retcode");
                            switch (dynamicRetcode) {
                                case 2000:
                                    ToastUtil.show(TopicDetailActivity.this, "关注成功");
                                    mTopicDetailFollowTopic.setImageResource(R.mipmap.quxiaoguanzhu);
                                    isFollowed = true;
                                    break;
                                default:
                                    ToastUtil.show(TopicDetailActivity.this, json.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void cancleFollowTopic() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("tid", topicid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.CancleFollowTopic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(response);
                            dynamicRetcode = json.getInt("retcode");
                            switch (dynamicRetcode) {
                                case 2000:
                                    ToastUtil.show(TopicDetailActivity.this, "取消关注");
                                    mTopicDetailFollowTopic.setImageResource(R.mipmap.guanzhuhuati);
                                    isFollowed = false;
                                    break;
                                default:
                                    ToastUtil.show(TopicDetailActivity.this, json.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void getDynamicList(int flag) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        if (flag == 0) {
            map.put("type", "4");
            map.put("tid", topicid);
        } else {
            map.put("type", "1");
            map.put("tid", topicid);
        }
        map.put("page", page + "");
        map.put("loginuid", MyApp.uid);
        map.put("sex", sex);
        map.put("sexual", sexual);
        map.put("lastid", lastid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetDynamicListNewFive, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final DynamicListData listData = new Gson().fromJson(response, DynamicListData.class);
                            if (listData.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    isReresh = false;
                                    ToastUtil.show(getApplicationContext(), "没有更多");
                                } else {
                                    dynamicAdapter = new DynamicListviewAdapter(TopicDetailActivity.this, dynamics, 0,"1");
                                    mTopicDetailListview.setAdapter(dynamicAdapter);
                                }
                            } else {
                                isReresh = true;
                                if (page == 0) {
                                    dynamics.addAll(listData.getData());
                                    int retcode = listData.getRetcode();
                                    try {
                                        dynamicAdapter = new DynamicListviewAdapter(TopicDetailActivity.this, dynamics, retcode,"1");
                                        mTopicDetailListview.setAdapter(dynamicAdapter);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    dynamics.addAll(listData.getData());
                                    dynamicAdapter.notifyDataSetChanged();
                                }
                            }

                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        mTopicDetailProgressbar.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
                if (IsListviewSlideBottom.isListViewReachBottomEdge(view)) {
                    if (isReresh) {
                        if(dynamics.size() == 0) {
                            return;
                        }
                        page = page + 1;
                        lastid = dynamics.get(0).getDid();
                        getDynamicList(flag);
                    }
                } else {
                    int first = view.getFirstVisiblePosition();
                    int last = view.getLastVisiblePosition();
                    for (int j = last; j >= first; j--) {
                        if(j > 1 && dynamics.size() > j - 2 && !TextUtil.isEmpty(dynamics.get(j - 2).getPlayUrl().trim())) {
                            dynamicAdapter.tryToPlayVideo(view,j - first,dynamics.get(j - 2).getPlayUrl().trim());
                            return;
                        }
                    }
                }
                break;
        }
        if (mTopicDetailListview.getFirstVisiblePosition() == 0) {
            mFragmentDynamicHotTop.setVisibility(View.GONE);
            mFragmentDynamicHotTop.setAnimation(AnimationUtil.ViewToGone());
        } else {
            if (mFragmentDynamicHotTop.getVisibility() == View.GONE) {
                mFragmentDynamicHotTop.setVisibility(View.VISIBLE);
                mFragmentDynamicHotTop.setAnimation(AnimationUtil.ViewToVisible());
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem >= 1) {
            mTopicDetailLlTabs.setVisibility(View.VISIBLE);
        } else {
            mTopicDetailLlTabs.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if (y1 - y2 > 2) {
                if (mTopicDetailFollowLayout.getVisibility() == View.VISIBLE) {
                    mTopicDetailFollowLayout.setVisibility(View.GONE);
                    mTopicDetailFollowLayout.setAnimation(AnimationUtil.moveToViewBottom());
                }
            } else if (y2 - y1 > 2) {
                if (mTopicDetailFollowLayout.getVisibility() == View.GONE) {
                    mTopicDetailFollowLayout.setVisibility(View.VISIBLE);
                    mTopicDetailFollowLayout.setAnimation(AnimationUtil.moveToViewLocation());
                }
            }
//            else if(x1 - x2 > 50) {
//                Toast.makeText(this, "向左滑", Toast.LENGTH_SHORT).show();
//            } else if(x2 - x1 > 50) {
//                Toast.makeText(this, "向右滑", Toast.LENGTH_SHORT).show();
//            }
        }
        return false;
    }

    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ProgressBar bar = new ProgressBar(this);
        builder.setCancelable(true)
                .setView(bar);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
       // MyApp.uid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "uid", "0");
       // getJudgeDynamic();
    }


}
