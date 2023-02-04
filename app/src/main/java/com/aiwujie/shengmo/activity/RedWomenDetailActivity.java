package com.aiwujie.shengmo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.RedwomenPersonData;
import com.aiwujie.shengmo.customview.MyscrollView;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.GlideImageLoader;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RedWomenDetailActivity extends AppCompatActivity implements MyscrollView.ScrollListener {

    @BindView(R.id.mRedWomen_detail_return)
    ImageView mRedWomenDetailReturn;
    @BindView(R.id.mRedWomen_detail_title)
    TextView mRedWomenDetailTitle;
    @BindView(R.id.mRedWomen_detail_ll_title)
    PercentRelativeLayout mRedWomenDetailLlTitle;
    @BindView(R.id.mRedWomen_detail_banner)
    Banner mRedWomenDetailBanner;
    @BindView(R.id.mRedWomen_detail_name)
    TextView mRedWomenDetailName;
    @BindView(R.id.mRedWomen_detail_renzheng)
    ImageView mRedWomenDetailRenzheng;
    @BindView(R.id.mRedWomen_detail_Sex)
    TextView mRedWomenDetailSex;
    @BindView(R.id.mRedWomen_detail_role)
    TextView mRedWomenDetailRole;
    @BindView(R.id.mRedWomen_detail_city)
    TextView mRedWomenDetailCity;
    @BindView(R.id.mRedWomen_detail_age)
    TextView mRedWomenDetailAge;
    @BindView(R.id.mRedWomen_detail_star)
    TextView mRedWomenDetailStar;
    @BindView(R.id.mRedWomen_detail_height)
    TextView mRedWomenDetailHeight;
    @BindView(R.id.mRedWomen_detail_weight)
    TextView mRedWomenDetailWeight;
    @BindView(R.id.mRedWomen_detail_culture)
    TextView mRedWomenDetailCulture;
    @BindView(R.id.mRedWomen_detail_money)
    TextView mRedWomenDetailMoney;
    @BindView(R.id.mRedWomen_detail_smRole)
    TextView mRedWomenDetailSmRole;
    @BindView(R.id.mRedWomen_detail_long)
    TextView mRedWomenDetailLong;
    @BindView(R.id.mRedWomen_detail_experience)
    TextView mRedWomenDetailExperience;
    @BindView(R.id.mRedWomen_detail_level)
    TextView mRedWomenDetailLevel;
    @BindView(R.id.mRedWomen_detail_sexual)
    TextView mRedWomenDetailSexual;
    @BindView(R.id.mRedWomen_detail_want)
    TextView mRedWomenDetailWant;
    @BindView(R.id.mRedWomen_detail_heartTalk)
    TextView mRedWomenDetailHeartTalk;
    @BindView(R.id.mRedWomen_detail_redwomenTalk)
    TextView mRedWomenDetailRedwomenTalk;
    @BindView(R.id.mRedWomen_detail_redwomenChat)
    ImageView mRedWomenDetailRedwomenChat;
    @BindView(R.id.activity_red_women_detail)
    PercentRelativeLayout activityRedWomenDetail;
    @BindView(R.id.mRedWomen_detail_myScrollview)
    MyscrollView mRedWomenDetailMyScrollview;
    @BindView(R.id.mRedWomen_detail_admin_center)
    TextView mRedWomenDetailAdminCenter;
    /**
     * ScrollView正在向上滑动
     */
    public static final int SCROLL_UP = 0x01;

    /**
     * ScrollView正在向下滑动
     */
    public static final int SCROLL_DOWN = 0x10;
    private String uid;
    private Handler handler = new Handler();
    //照片集合
    private ArrayList<String> photos = new ArrayList<>();
    //简介集合
    private ArrayList<TextView> datatvs = new ArrayList<>();
    private String match_state;
    private String admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_women_detail);
        X_SystemBarUI.initSystemBar(this, R.color.purple_main);
        ButterKnife.bind(this);
        setData();
        setListener();
        getRedwomenPersonDetail();
    }


    private void setData() {
        uid = getIntent().getStringExtra("uid");
        //设置轮播时间
        mRedWomenDetailBanner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        mRedWomenDetailBanner.setIndicatorGravity(BannerConfig.CENTER);
        datatvs.add(mRedWomenDetailAge);
        datatvs.add(mRedWomenDetailStar);
        datatvs.add(mRedWomenDetailHeight);
        datatvs.add(mRedWomenDetailWeight);
        datatvs.add(mRedWomenDetailCulture);
        datatvs.add(mRedWomenDetailMoney);
        datatvs.add(mRedWomenDetailSmRole);
        datatvs.add(mRedWomenDetailLong);
        datatvs.add(mRedWomenDetailExperience);
        datatvs.add(mRedWomenDetailLevel);
        datatvs.add(mRedWomenDetailSexual);
        datatvs.add(mRedWomenDetailWant);
        //自己是不是牵线会员  0、不是牵线会员
        match_state = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "match_state", "0");
        admin= (String) SharedPreferencesUtils.getParam(getApplicationContext(),"admin","0");
        if(admin.equals("1")){
            mRedWomenDetailAdminCenter.setVisibility(View.VISIBLE);
        }
    }

    private void setListener() {
        mRedWomenDetailMyScrollview.setScrollListener(this);
//        mRedWomenDetailBanner.setOnBannerListener(this);
    }

    @OnClick({R.id.mRedWomen_detail_return, R.id.mRedWomen_detail_redwomenChat,R.id.mRedWomen_detail_admin_center})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mRedWomen_detail_return:
                finish();
                break;
            case R.id.mRedWomen_detail_redwomenChat:
                intent=new Intent(this,RedwomenPersonCenterActivity.class);
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
                break;
            case R.id.mRedWomen_detail_admin_center:
                intent=new Intent(this,RedwomenPersonCenterActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
                break;
        }
    }

    private void getRedwomenPersonDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetMatchUserInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("redwomenBeauti", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    mRedWomenDetailMyScrollview.setVisibility(View.VISIBLE);
                                    mRedWomenDetailRedwomenChat.setVisibility(View.VISIBLE);
                                    RedwomenPersonData data = new Gson().fromJson(response, RedwomenPersonData.class);
                                    photos = data.getData().getMatch_photo();
//                                    nickname=data.getData().getNickname();
                                    mRedWomenDetailTitle.setText(data.getData().getMatch_num());
                                    mRedWomenDetailName.setText(data.getData().getMatch_num());
                                    if (match_state.equals("0")&&admin.equals("0")) {
                                        if (data.getData().getMatch_photo_lock().equals("0")) {
                                            //设置图片加载器
                                            mRedWomenDetailBanner.setImageLoader(new GlideImageLoader());
                                        } else {
                                            //设置图片加载器(模糊)
                                            mRedWomenDetailBanner.setImageLoader(new GlideImageLoader(true));
                                        }
                                    } else {
                                        if(data.getData().getMatch_photo_lock().equals("2")){
                                            if(admin.equals("0")){
                                                //设置图片加载器(模糊)
                                                mRedWomenDetailBanner.setImageLoader(new GlideImageLoader(true));
                                            }else{
                                                //设置图片加载器
                                                mRedWomenDetailBanner.setImageLoader(new GlideImageLoader());
                                            }
                                        }else{
                                            //设置图片加载器
                                            mRedWomenDetailBanner.setImageLoader(new GlideImageLoader());
                                        }
                                    }
                                    //设置图片集合
                                    mRedWomenDetailBanner.setImages(photos);
                                    mRedWomenDetailBanner.start();
                                    if (data.getData().getCity().equals("")&&data.getData().getProvince().equals("")) {
                                        mRedWomenDetailCity.setText("未知");
                                    } else {
                                        if(!data.getData().getCity().equals("")) {
                                            mRedWomenDetailCity.setText(data.getData().getCity());
                                        }else{
                                            mRedWomenDetailCity.setText(data.getData().getProvince());
                                        }
                                    }
                                    mRedWomenDetailSex.setText(data.getData().getAge());
                                    if (data.getData().getSex().equals("1")) {
                                        mRedWomenDetailSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
                                        Drawable drawable = getResources().getDrawable(R.mipmap.nan);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        mRedWomenDetailSex.setCompoundDrawables(drawable, null, null, null);
                                        for (int i = 0; i < datatvs.size(); i++) {
                                            datatvs.get(i).setTextColor(Color.parseColor("#FFFFFF"));
                                            datatvs.get(i).setBackgroundResource(R.drawable.item_blue_rectangle_bg);
                                        }
                                    } else if (data.getData().getSex().equals("2")) {
                                        mRedWomenDetailSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
                                        Drawable drawable = getResources().getDrawable(R.mipmap.nv);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        mRedWomenDetailSex.setCompoundDrawables(drawable, null, null, null);
                                        for (int i = 0; i < datatvs.size(); i++) {
                                            datatvs.get(i).setTextColor(Color.parseColor("#FFFFFF"));
                                            datatvs.get(i).setBackgroundResource(R.drawable.item_pink_rectangle_bg);
                                        }
                                    } else {
                                        mRedWomenDetailSex.setBackgroundResource(R.drawable.item_sex_san_bg);
                                        Drawable drawable = getResources().getDrawable(R.mipmap.san);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        mRedWomenDetailSex.setCompoundDrawables(drawable, null, null, null);
                                        for (int i = 0; i < datatvs.size(); i++) {
                                            datatvs.get(i).setTextColor(Color.parseColor("#FFFFFF"));
                                            datatvs.get(i).setBackgroundResource(R.drawable.item_purple_rectangle_bg);
                                        }
                                    }
                                    if (data.getData().getRole().equals("S")) {
                                        mRedWomenDetailRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
                                        mRedWomenDetailRole.setText("斯");
                                        mRedWomenDetailSmRole.setText("角色: 斯");
                                    } else if (data.getData().getRole().equals("M")) {
                                        mRedWomenDetailRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                                        mRedWomenDetailRole.setText("慕");
                                        mRedWomenDetailSmRole.setText("角色: 慕");
                                    } else if (data.getData().getRole().equals("SM")) {
                                        mRedWomenDetailRole.setBackgroundResource(R.drawable.item_sex_san_bg);
                                        mRedWomenDetailRole.setText("双");
                                        mRedWomenDetailSmRole.setText("角色: 双");
                                    } else if (data.getData().getRole().equals("~")) {
                                        mRedWomenDetailRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                                        mRedWomenDetailRole.setText("~");
                                        mRedWomenDetailSmRole.setText("角色: ~");
                                    }
                                    if(data.getData().getRealname().equals("0")){
                                        mRedWomenDetailRenzheng.setVisibility(View.GONE);
                                    }else{
                                        mRedWomenDetailRenzheng.setVisibility(View.VISIBLE);
                                    }
                                    mRedWomenDetailAge.setText("年龄: " + data.getData().getAge() + "岁");
                                    mRedWomenDetailStar.setText("星座: " + data.getData().getStarchar());
                                    mRedWomenDetailHeight.setText("身高: " + data.getData().getTall() + "cm");
                                    mRedWomenDetailWeight.setText("体重: " + data.getData().getWeight() + "kg");
                                    mRedWomenDetailCulture.setText("学历: " + data.getData().getCulture());
                                    mRedWomenDetailMoney.setText("月薪: " + data.getData().getMonthly());
                                    mRedWomenDetailLong.setText("接触: " + data.getData().getAlong());
                                    mRedWomenDetailExperience.setText("实践: " + data.getData().getExperience());
                                    mRedWomenDetailLevel.setText("程度: " + data.getData().getLevel());
                                    mRedWomenDetailSexual.setText("取向: " + data.getData().getSexual());
                                    mRedWomenDetailWant.setText("想找: " + data.getData().getWant());
                                    mRedWomenDetailHeartTalk.setText(data.getData().getMatch_introduce());
                                    if(data.getData().getMatch_makerintroduce().equals("")){
                                        mRedWomenDetailRedwomenTalk.setText("暂无红娘荐语...");
                                    }else {
                                        mRedWomenDetailRedwomenTalk.setText(data.getData().getMatch_makerintroduce());
                                    }
                                    break;
                                case 4001:
                                    mRedWomenDetailTitle.setText("昵称牵线会员可见");
                                    mRedWomenDetailMyScrollview.setVisibility(View.GONE);
                                    redWomenDialog(object.getString("msg"),true);
                                    break;
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
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

    private void redWomenDialog(String str, final boolean isCloseCurrentAc) {
        View view = View.inflate(this, R.layout.redwomen_dialog, null);
        final AlertDialog startDialog = new AlertDialog.Builder(this).create();
        startDialog.setView(view);
        startDialog.show();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Window window = startDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = dm.widthPixels * 85 / 100;//宽高可设置具体大小
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        startDialog.getWindow().setAttributes(lp);
        startDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        startDialog.setCancelable(false);
        TextView tvContent = (TextView) view.findViewById(R.id.redwomen_dialog_content);
        TextView tvConfirm = (TextView) view.findViewById(R.id.redwomen_dialog_confirm);
        tvContent.setText(str);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog.dismiss();
                if(isCloseCurrentAc) {
                    finish();
                }
            }
        });

    }

    @Override
    public void scrollOritention(int oritention) {
        if (oritention == SCROLL_UP) {
            if (mRedWomenDetailRedwomenChat.getVisibility() == View.VISIBLE) {
                mRedWomenDetailRedwomenChat.setVisibility(View.GONE);
                mRedWomenDetailRedwomenChat.setAnimation(AnimationUtil.moveToViewBottom());
            }
        } else if (oritention == SCROLL_DOWN) {
            if (mRedWomenDetailRedwomenChat.getVisibility() == View.GONE) {
                mRedWomenDetailRedwomenChat.setVisibility(View.VISIBLE);
                mRedWomenDetailRedwomenChat.setAnimation(AnimationUtil.moveToViewLocation());
            }
        }
    }

//    @Override
//    public void OnBannerClick(int position) {
//        Intent intent = new Intent(this, ZoomActivity.class);
//        intent.putExtra("pics", photos);
//        intent.putExtra("position", position);
//        startActivity(intent);
//    }


    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        mRedWomenDetailBanner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        mRedWomenDetailBanner.stopAutoPlay();
    }

}
