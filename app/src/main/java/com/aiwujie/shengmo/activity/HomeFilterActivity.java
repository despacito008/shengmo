package com.aiwujie.shengmo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.FilterData;
import com.aiwujie.shengmo.bean.PersonData;
import com.aiwujie.shengmo.customview.WheelView;
import com.aiwujie.shengmo.eventbus.HomeFilterStateEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFilterActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.mHome_filter_return)
    ImageView mHomeFilterReturn;
    @BindView(R.id.mHome_filter_name)
    TextView mHomeFilterName;
    @BindView(R.id.mHome_filter_wancheng)
    TextView mHomeFilterWancheng;
    @BindView(R.id.mHome_filter_rbZong)
    CheckBox mHomeFilterRbZong;
    @BindView(R.id.mHome_filter_rbOnline)
    CheckBox mHomeFilterRbOnline;
    @BindView(R.id.mHome_filter_age)
    TextView mHomeFilterAge;
    //    @BindView(R.id.mHome_filter_nan)
//    RadioButton mHomeFilterNan;
//    @BindView(R.id.mHome_filter_nv)
//    RadioButton mHomeFilterNv;
//    @BindView(R.id.mHome_filter_yi)
//    RadioButton mHomeFilterYi;
    @BindView(R.id.mHome_filter_rgSex)
    RadioGroup mHomeFilterRgSex;
    //    @BindView(R.id.mHome_filter_yixing)
//    RadioButton mHomeFilterYixing;
//    @BindView(R.id.mHome_filter_tongxing)
//    RadioButton mHomeFilterTongxing;
//    @BindView(R.id.mHome_filter_shuangxing)
//    RadioButton mHomeFilterShuangxing;
    @BindView(R.id.mHome_filter_rgQuxiang)
    RadioGroup mHomeFilterRgQuxiang;
    //    @BindView(R.id.mHome_filter_s)
//    RadioButton mHomeFilterS;
//    @BindView(R.id.mHome_filter_m)
//    RadioButton mHomeFilterM;
//    @BindView(R.id.mHome_filter_sm)
//    RadioButton mHomeFilterSm;
    @BindView(R.id.mHome_filter_rgJue)
    RadioGroup mHomeFilterRgJue;
    @BindView(R.id.mHome_filter_vip)
    ImageView mHomeFilterVip;
    @BindView(R.id.mHome_filter_rbGaoji)
    CheckBox mHomeFilterRbGaoji;
    @BindView(R.id.mHome_filter_culture)
    TextView mHomeFilterCulture;
    @BindView(R.id.mHome_filter_money)
    TextView mHomeFilterMoney;
    @BindView(R.id.mHome_filter_xiangzhao)
    TextView mHome_filter_xiangzhao;
    @BindView(R.id.mHome_filter_rbRenzheng)
    CheckBox mHomeFilterRbRenzheng;
    @BindView(R.id.home_filter_layout)
    PercentLinearLayout homeFilterLayout;
    @BindView(R.id.mHome_filter_sexbuxian)
    TextView mHomeFilterSexbuxian;
    @BindView(R.id.mHome_filter_nan)
    CheckBox mHomeFilterNan;
    @BindView(R.id.mHome_filter_nv)
    CheckBox mHomeFilterNv;
    @BindView(R.id.mHome_filter_yi)
    CheckBox mHomeFilterYi;
    @BindView(R.id.mHome_filter_quxiangbuxian)
    TextView mHomeFilterQuxiangbuxian;
    @BindView(R.id.mHome_filter_yixing)
    CheckBox mHomeFilterYixing;
    @BindView(R.id.mHome_filter_tongxing)
    CheckBox mHomeFilterTongxing;
    @BindView(R.id.mHome_filter_shuangxing)
    CheckBox mHomeFilterShuangxing;
    @BindView(R.id.mHome_filter_juesebuxian)
    TextView mHomeFilterJuesebuxian;
    @BindView(R.id.mHome_filter_s)
    CheckBox mHomeFilterS;
    @BindView(R.id.mHome_filter_m)
    CheckBox mHomeFilterM;
    @BindView(R.id.mHome_filter_sm)
    CheckBox mHomeFilterSm;
    @BindView(R.id.mHome_filter_lang)
    CheckBox mHomeFilterLang;
    //    @BindView(R.id.mHome_filter_sexbuxian)
//    RadioButton mHomeFilterSexbuxian;
//    @BindView(R.id.mHome_filter_quxiangbuxian)
//    RadioButton mHomeFilterQuxiangbuxian;
//    @BindView(R.id.mHome_filter_juesebuxian)
//    RadioButton mHomeFilterJuesebuxian;
    private ArrayList<String> ages;
    private ArrayList<String> cultures;
    private ArrayList<String> xiangzhao;
    private ArrayList<String> moneys;
    private String minAge = "不限";
    private String maxAge = "22岁";
    private PopupWindow mPopWindow;
    private String upAge = "0,22";
    private String showAge;
    private String upCulture = "2";
    private String culture = "大专";
    private String xzya = "聊天";
    private String upxzya = "1";
    private String showMoney = "2千-5千";
    private String upMoney = "2";
    private String ckZong = "0";
    private String isline = "";
    private String sex = "";
    private String quxiang = "";
    private String role = "";
    private String ckVip = "";
    private String ckAuthen = "";
    Handler handler = new Handler();
    private String headpic;
    private List<CheckBox> sexs;
    private List<String> upsexs = new ArrayList<>();
    private List<CheckBox> sexuals;
    private List<String> upsexuals = new ArrayList<>();
    private List<CheckBox> roles;
    private List<String> uproles = new ArrayList<>();
    boolean isVip = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_filter);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        setData();
        setListener();
        getOwnerMsg();
    }

    private void getOwnerMsg() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.OwnerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PersonData data = new Gson().fromJson(response, PersonData.class);
                            headpic = data.getData().getHead_pic();
                        }catch (JsonSyntaxException e){
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

    private void setData() {
        ages = new ArrayList<>();
        ages.add("不限");
        for (int i = 0; i < 64; i++) {
            ages.add(17 + i + "岁");
        }
        xiangzhao = new ArrayList<>();
        xiangzhao.add("不限");
        xiangzhao.add("聊天");
        xiangzhao.add("现实");
        xiangzhao.add("结婚");
        cultures = new ArrayList<>();
        cultures.add("不限");
        cultures.add("高中及以下");
        cultures.add("大专");
        cultures.add("本科");
        cultures.add("双学士");
        cultures.add("硕士");
        cultures.add("博士");
        cultures.add("博士后");
        moneys = new ArrayList<>();
        moneys.add("不限");
        moneys.add("2千以下");
        moneys.add("2千-5千");
        moneys.add("5千-1万");
        moneys.add("1万-2万");
        moneys.add("2万-5万");
        moneys.add("5万以上");
        sexs = new ArrayList<>();
        sexs.add(mHomeFilterNan);
        sexs.add(mHomeFilterNv);
        sexs.add(mHomeFilterYi);
        sexuals = new ArrayList<>();
        sexuals.add(mHomeFilterYixing);
        sexuals.add(mHomeFilterTongxing);
        sexuals.add(mHomeFilterShuangxing);
        roles = new ArrayList<>();
        roles.add(mHomeFilterS);
        roles.add(mHomeFilterM);
        roles.add(mHomeFilterSm);
        roles.add(mHomeFilterLang);
        setState();
    }

    private void setState() {
        if (SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "").equals("1")) {
            mHomeFilterVip.setImageResource(R.mipmap.vip);
        } else {
            mHomeFilterVip.setImageResource(R.mipmap.gaojisousuohui);
            mHomeFilterRbGaoji.setChecked(false);
            isVip = false;
        }
        if (SharedPreferencesUtils.getParam(getApplicationContext(), "filterZong", "").equals("1")) {
            ckZong = "1";
            mHomeFilterRbZong.setChecked(true);
        }
        ckVip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "ckvip", "");
        if (ckVip.equals("1")) {
            mHome_filter_xiangzhao.setClickable(true);
            mHomeFilterRbGaoji.setChecked(true);
            isVip = true;
            mHomeFilterCulture.setClickable(true);
            mHomeFilterMoney.setClickable(true);
            mHomeFilterRbRenzheng.setClickable(true);
            mHomeFilterAge.setClickable(true);
        } else {
            mHome_filter_xiangzhao.setClickable(false);
            mHomeFilterRbGaoji.setChecked(false);
            isVip = false;
            mHomeFilterCulture.setClickable(false);
            mHomeFilterMoney.setClickable(false);
            mHomeFilterRbRenzheng.setClickable(false);
            mHomeFilterAge.setClickable(false);
        }
        if (SharedPreferencesUtils.getParam(getApplicationContext(), "filterLine", "").equals("1")) {
            isline = "1";
            mHomeFilterRbOnline.setChecked(true);
        }
        showAge = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterShowAge", "");
        upAge = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterUpAge", "");
        mHomeFilterAge.setText(showAge);
        sex = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterSex", "");
//        List<String> upsexs=new ArrayList<>();
        if (sex.contains(",")) {
            for (String t : sex.split(",")) {
                upsexs.add(t);
            }
        } else {
            upsexs.add(sex);
        }
        for (int i = 0; i < upsexs.size(); i++) {
            if (upsexs.get(i).equals("0")) {
                mHomeFilterSexbuxian.setSelected(true);
            } else if (upsexs.get(i).equals("1")) {
                mHomeFilterNan.setChecked(true);
            } else if (upsexs.get(i).equals("2")) {
                mHomeFilterNv.setChecked(true);
            } else if (upsexs.get(i).equals("3")) {
                mHomeFilterYi.setChecked(true);
            }
        }
        Log.i("", "setState: " + sex);
        quxiang = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterQx", "");
//        List<String> upsexuals=new ArrayList<>();
        if (quxiang.contains(",")) {
            for (String t : quxiang.split(",")) {
                upsexuals.add(t);
            }
        } else {
            upsexuals.add(quxiang);
        }
        for (int i = 0; i < upsexuals.size(); i++) {
            if (upsexuals.get(i).equals("0")) {
                mHomeFilterQuxiangbuxian.setSelected(true);
            } else if (upsexuals.get(i).equals("1")) {
                mHomeFilterYixing.setChecked(true);
            } else if (upsexuals.get(i).equals("2")) {
                mHomeFilterTongxing.setChecked(true);
            } else if (upsexuals.get(i).equals("3")) {
                mHomeFilterShuangxing.setChecked(true);
            }
        }
        Log.i("quxiang", "setState: " + quxiang);
        role = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterRole", "");
//        List<String> uproles=new ArrayList<>();
        if (role.contains(",")) {
            for (String t : role.split(",")) {
                uproles.add(t);
            }
        } else {
            uproles.add(role);
        }
        for (int i = 0; i < uproles.size(); i++) {
            if (uproles.get(i).equals("0")) {
                mHomeFilterJuesebuxian.setSelected(true);
            } else if (uproles.get(i).equals("S")) {
                mHomeFilterS.setChecked(true);
            } else if (uproles.get(i).equals("M")) {
                mHomeFilterM.setChecked(true);
            } else if (uproles.get(i).equals("SM")) {
                mHomeFilterSm.setChecked(true);
            } else if(uproles.get(i).equals("~")){
                mHomeFilterLang.setChecked(true);
            }
        }
        upxzya = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterUpxzya", "");
        xzya = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterxzya", "");
        mHome_filter_xiangzhao.setText(xzya);
        upCulture = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterUpCulture", "");
        culture = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterCulture", "");
        mHomeFilterCulture.setText(culture);
        upMoney = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterUpMoney", "");
        showMoney = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterShowMoney", "");
        mHomeFilterMoney.setText(showMoney);
        ckAuthen = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterAuthen", "");
        if (ckAuthen.equals("1")) {
            mHomeFilterRbRenzheng.setChecked(true);
        } else {
            mHomeFilterRbRenzheng.setChecked(false);
        }
        if (!mHomeFilterRbZong.isChecked()) {
            setViewEnableF();
        } else {
            setViewEnableT();
        }

    }

    private void setViewEnableF() {
        mHome_filter_xiangzhao.setClickable(false);
        mHomeFilterRbOnline.setClickable(false);
        mHomeFilterAge.setClickable(false);
        mHomeFilterRbGaoji.setClickable(false);
        isVip = false;
        mHomeFilterCulture.setClickable(false);
        mHomeFilterMoney.setClickable(false);
        mHomeFilterRbRenzheng.setClickable(false);
        mHomeFilterJuesebuxian.setClickable(false);
        mHomeFilterQuxiangbuxian.setClickable(false);
        mHomeFilterSexbuxian.setClickable(false);
        mHomeFilterYixing.setClickable(false);
        mHomeFilterTongxing.setClickable(false);
        mHomeFilterShuangxing.setClickable(false);
        mHomeFilterS.setClickable(false);
        mHomeFilterM.setClickable(false);
        mHomeFilterSm.setClickable(false);
        mHomeFilterLang.setClickable(false);
        mHomeFilterNan.setClickable(false);
        mHomeFilterNv.setClickable(false);
        mHomeFilterYi.setClickable(false);
        //mHomeFilterRbGaoji.setChecked(false);
        mHomeFilterRbRenzheng.setChecked(false);
        mHomeFilterRbOnline.setChecked(false);
        mHomeFilterAge.setText("");
        mHomeFilterCulture.setText("");
        mHomeFilterMoney.setText("");
        mHomeFilterSexbuxian.setSelected(true);
        mHomeFilterQuxiangbuxian.setSelected(true);
        mHomeFilterJuesebuxian.setSelected(true);
        for (int i = 0; i < sexs.size(); i++) {
            sexs.get(i).setChecked(false);
        }
        for (int i = 0; i < sexuals.size(); i++) {
            sexuals.get(i).setChecked(false);
        }
        for (int i = 0; i < roles.size(); i++) {
            roles.get(i).setChecked(false);
        }
    }

    private void setViewEnableT() {
//        mHomeFilterRbOnline.setClickable(true);
//        mHomeFilterAge.setClickable(true);
        mHomeFilterYixing.setClickable(true);
        mHomeFilterTongxing.setClickable(true);
        mHomeFilterShuangxing.setClickable(true);
        mHomeFilterS.setClickable(true);
        mHomeFilterM.setClickable(true);
        mHomeFilterSm.setClickable(true);
        mHomeFilterLang.setClickable(true);
        mHomeFilterNan.setClickable(true);
        mHomeFilterNv.setClickable(true);
        mHomeFilterYi.setClickable(true);
        mHomeFilterJuesebuxian.setClickable(true);
        mHomeFilterQuxiangbuxian.setClickable(true);
        mHomeFilterSexbuxian.setClickable(true);
        mHomeFilterRbGaoji.setClickable(true);
        isVip = true;
        if (SharedPreferencesUtils.getParam(getApplicationContext(), "ckvip", "").equals("1") && isVip) {
            mHomeFilterCulture.setClickable(true);
            mHomeFilterMoney.setClickable(true);
            mHomeFilterRbRenzheng.setClickable(true);
            mHomeFilterAge.setClickable(true);
            mHomeFilterRbOnline.setClickable(true);
            mHome_filter_xiangzhao.setClickable(true);
        }
    }

    private void setListener() {
        mHomeFilterRbZong.setOnCheckedChangeListener(this);
        mHomeFilterRbGaoji.setOnCheckedChangeListener(this);
        mHomeFilterRbOnline.setOnCheckedChangeListener(this);
        mHomeFilterRbRenzheng.setOnCheckedChangeListener(this);
        for (int i = 0; i < sexs.size(); i++) {
            sexs.get(i).setOnCheckedChangeListener(this);
            sexuals.get(i).setOnCheckedChangeListener(this);
        }
        for (int i = 0; i < roles.size(); i++) {
            roles.get(i).setOnCheckedChangeListener(this);
        }

    }

    @OnClick({R.id.mHome_filter_return, R.id.mHome_filter_wancheng, R.id.mHome_filter_age, R.id.mHome_filter_culture, R.id.mHome_filter_xiangzhao, R.id.mHome_filter_money, R.id.mHome_filter_sexbuxian, R.id.mHome_filter_quxiangbuxian, R.id.mHome_filter_juesebuxian})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mHome_filter_return:
                finish();
                break;
            case R.id.mHome_filter_wancheng:
                upSxData();
                break;
            case R.id.mHome_filter_age:
                minAge = "不限";
                maxAge = "22岁";
                upAge = "0,22";
                showpopwindow();
                break;
            case R.id.mHome_filter_culture:
                culture = "大专";
                upCulture = "2";
                showPop(mHomeFilterCulture, cultures);
                break;
            case R.id.mHome_filter_money:
                showMoney = "2千到5千";
                upMoney = "2";
                showPop(mHomeFilterMoney, moneys);
                break;
            case R.id.mHome_filter_xiangzhao:
                //showMoney = "2千到5千";
                //upMoney = "2";
                xzya="聊天";
                upxzya="1";
                showPop2(mHome_filter_xiangzhao, xiangzhao);
                break;
            case R.id.mHome_filter_sexbuxian:
                for (int i = 0; i < sexs.size(); i++) {
                    sexs.get(i).setChecked(false);
                }
                mHomeFilterSexbuxian.setSelected(true);
                upsexs.clear();
                upsexs.add("");
                break;
            case R.id.mHome_filter_quxiangbuxian:
                for (int i = 0; i < sexuals.size(); i++) {
                    sexuals.get(i).setChecked(false);
                }
                mHomeFilterQuxiangbuxian.setSelected(true);
                upsexuals.clear();
                upsexuals.add("");
                break;
            case R.id.mHome_filter_juesebuxian:
                for (int i = 0; i < roles.size(); i++) {
                    roles.get(i).setChecked(false);
                }
                mHomeFilterJuesebuxian.setSelected(true);
                uproles.clear();
                uproles.add("");
                break;
        }
    }

    private void upSxData() {
        if (ckZong.equals("1")) {
            if (upsexs.size() != 0) {
                String sexstr = "";
                Collections.sort(upsexs);
                for (int i = 0; i < upsexs.size(); i++) {
                    sexstr += upsexs.get(i) + ",";
                }
                sex = sexstr.substring(0, sexstr.length() - 1);
            } else {
                sex = "";
            }
            if (upsexuals.size() != 0) {
                String quxiangstr = "";
                Collections.sort(upsexuals);
                for (int i = 0; i < upsexuals.size(); i++) {
                    quxiangstr += upsexuals.get(i) + ",";
                }
                quxiang = quxiangstr.substring(0, quxiangstr.length() - 1);
            } else {
                quxiang = "";
            }
            if (uproles.size() != 0) {
                String rolestr = "";
                Collections.sort(uproles);
                for (int i = 0; i < uproles.size(); i++) {
                    rolestr += uproles.get(i) + ",";
                }
                role = rolestr.substring(0, rolestr.length() - 1);
            } else {
                role = "";
            }
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterZong", ckZong);
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterSex", sex);
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterQx", quxiang);
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterRole", role);
            if (isVip) {
//                ckVip = "1";
                SharedPreferencesUtils.setParam(getApplicationContext(), "ckvip", ckVip);
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpxzya", upxzya);
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterxzya", xzya);
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterCulture", culture);
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpCulture", upCulture);
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterShowMoney", showMoney);
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpMoney", upMoney);
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterAuthen", ckAuthen);
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterLine", isline);
                if (mHomeFilterAge.getText().toString().equals("")) {
                    upAge = "";
                }
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterShowAge", showAge);
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpAge", upAge);
            } else {
                ckVip = "0";
                SharedPreferencesUtils.setParam(getApplicationContext(), "ckvip", ckVip);
                upCulture = "";
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterCulture", "");
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpCulture", upCulture);
                upMoney = "";
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterShowMoney", "");
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpMoney", upMoney);
                ckAuthen = "";
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterAuthen", ckAuthen);
                isline = "";
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterLine", isline);
                upAge = "";
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterShowAge", "");
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpAge", upAge);
                upxzya="";
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterxzya", "");
                SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpxzya", upxzya);

            }
        } else {
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterxzya", "");
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterShowAge", "");
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterCulture", "");
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterShowMoney", "");
            ckZong = "0";
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterZong", ckZong);
            sex = "";
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterSex", sex);
            quxiang = "";
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterQx", quxiang);
            role = "";
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterRole", role);
            isline = "";
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterLine", isline);
            upAge = "";
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpAge", upAge);
            upxzya="";
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpxzya", upxzya);
//            if (mHomeFilterRbGaoji.isChecked()) {
//                ckVip = "1";
//            } else {
//                ckVip = "0";
//            }
            ckVip = "0";
            SharedPreferencesUtils.setParam(getApplicationContext(), "ckvip", ckVip);
            upCulture = "";
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpCulture", upCulture);
            upMoney = "";
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpMoney", upMoney);
            ckAuthen = "";
            SharedPreferencesUtils.setParam(getApplicationContext(), "filterAuthen", ckAuthen);
        }
        //附近筛选取向未选中
        SharedPreferencesUtils.setParam(getApplicationContext(),"nearSexual","0");
        EventBus.getDefault().post(new HomeFilterStateEvent());
        EventBus.getDefault().post(new FilterData(isline, ckAuthen, upAge, sex, quxiang, role, upCulture, upMoney,upxzya));
        finish();
//        Log.i("shaixuan", "upSxData: " + sex + "--" + quxiang + "--" + role + "--" + upAge + "--" + upCulture + "--" + upMoney + "--" + isline + "--" + ckAuthen);
    }

    private void showPop(final TextView textView, List<String> list) {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        final int disheight = wm.getDefaultDisplay().getHeight();
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_culture_money_pop, null);
        WheelView wv = (WheelView) contentView.findViewById(R.id.wheelview_culture_money);
        TextView cancel = (TextView) contentView.findViewById(R.id.tv_cancle);
        TextView confirm = (TextView) contentView.findViewById(R.id.tv_select);
        wv.setOffset(2);
        wv.setItems(list);
        wv.setSeletion(2);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                if (textView == mHomeFilterCulture) {
                    culture = item;
                    upCulture = selectedIndex - 2 + "";
                    Log.i("selectage", "onSelected: " + upCulture);
                } else if (textView == mHomeFilterMoney) {
                    showMoney = item;
                    upMoney = selectedIndex - 2 + "";
                    Log.i("selectage", "onSelected: " + upMoney);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView == mHomeFilterCulture) {
                    culture = "大专";
                    upCulture = "2";
                } else if (textView == mHomeFilterMoney) {
                    showMoney = "2千-5千";
                    upMoney = "2";
                }
                mPopWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView == mHomeFilterCulture) {
                    mHomeFilterCulture.setText(culture);
                } else if (textView == mHomeFilterMoney) {
                    mHomeFilterMoney.setText(showMoney);
                }
                mPopWindow.dismiss();
            }
        });
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(disheight / (5 / 2));
        mPopWindow.setFocusable(true);
        backgroundAlpha(0.5f);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(homeFilterLayout, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        this.mPopWindow.setAnimationStyle(R.style.AnimationPreview);
    }

    private void showPop2(final TextView textView, List<String> list) {//想找pop
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        final int disheight = wm.getDefaultDisplay().getHeight();
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_culture_money_pop, null);
        WheelView wv = (WheelView) contentView.findViewById(R.id.wheelview_culture_money);
        TextView cancel = (TextView) contentView.findViewById(R.id.tv_cancle);
        TextView confirm = (TextView) contentView.findViewById(R.id.tv_select);
        wv.setOffset(2);
        wv.setItems(list);
        wv.setSeletion(1);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                if (textView == mHome_filter_xiangzhao) {
                    xzya = item;
                    upxzya = selectedIndex - 2 + "";
                    Log.i("selectage", "onSelected: " + upxzya);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView == mHome_filter_xiangzhao) {
                    xzya = "聊天";
                    upxzya = "1";
                }
                mPopWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView == mHome_filter_xiangzhao) {
                    mHome_filter_xiangzhao.setText(xzya);
                }
                mPopWindow.dismiss();
            }
        });
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(disheight / (5 / 2));
        mPopWindow.setFocusable(true);
        backgroundAlpha(0.5f);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(homeFilterLayout, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        this.mPopWindow.setAnimationStyle(R.style.AnimationPreview);
    }


    private void showpopwindow() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        final int disheight = wm.getDefaultDisplay().getHeight();
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_age_pop, null);
        WheelView wv = (WheelView) contentView.findViewById(R.id.wheelview_minAge);
        WheelView wv2 = (WheelView) contentView.findViewById(R.id.wheelview_maxAge);
        TextView cancel = (TextView) contentView.findViewById(R.id.tv_cancle);
        TextView confirm = (TextView) contentView.findViewById(R.id.tv_select);
        wv.setOffset(2);
        wv.setItems(ages);
        wv.setSeletion(0);
        wv2.setOffset(2);
        wv2.setItems(ages);
        wv2.setSeletion(ages.size() / 2 - 26);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                minAge = item;
                Log.i("selectage", "onSelected: " + minAge);
            }
        });
        wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                maxAge = item;
                Log.i("selectage", "onSelected: " + maxAge);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upAge = "0,22";
                showAge = "不限-22岁";
                mHomeFilterAge.setText("不限-22岁");
                mPopWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minAge.equals("不限")) {
                    minAge = "0岁";
                }
                if (maxAge.equals("不限")) {
                    maxAge = "1000岁";
                }
                int xAge = Integer.parseInt(minAge.replace("岁", ""));
                int dAge = Integer.parseInt(maxAge.replace("岁", ""));
                if (xAge > dAge) {
                    ToastUtil.show(getApplicationContext(), "请正确选择年龄");
                } else {
                    minAge = xAge + "";
                    maxAge = dAge + "岁";

                    mPopWindow.dismiss();
                }
                if (xAge == 0) {
                    minAge = "不限";
                }
                if (dAge == 1000) {
                    maxAge = "不限";
                }
                if (minAge.equals("不限") && maxAge.equals("不限")) {
                    showAge = "不限";
                } else {
                    showAge = minAge + "-" + maxAge;
                }
                upAge = xAge + "," + dAge;
                Log.i("homefilterage", "onClick: " + upAge);
                mHomeFilterAge.setText(showAge.trim());

            }
        });
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(disheight / (5 / 2));
        mPopWindow.setFocusable(true);
        backgroundAlpha(0.5f);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(homeFilterLayout, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        this.mPopWindow.setAnimationStyle(R.style.AnimationPreview);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.mHome_filter_rbZong:
                if (!isChecked) {
                    ckZong = "0";
                    setViewEnableF();
                } else {
                    ckZong = "1";
                    setViewEnableT();
                }
                break;
            case R.id.mHome_filter_rbGaoji:
                if (SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0").equals("0")) {
                    VipDialog();
                   // mHomeFilterRbGaoji.setChecked(false);
                    isVip = false;
                } else {
                    if (!isChecked) {
                        ckVip = "0";
                        mHomeFilterCulture.setClickable(false);
                        mHomeFilterMoney.setClickable(false);
                        mHomeFilterRbRenzheng.setClickable(false);
                        mHomeFilterAge.setClickable(false);
                        mHome_filter_xiangzhao.setClickable(false);
                        mHomeFilterRbOnline.setClickable(false);
                        mHomeFilterRbRenzheng.setChecked(false);
                        mHomeFilterRbOnline.setChecked(false);
                        mHome_filter_xiangzhao.setText("");
                        mHomeFilterAge.setText("");
                        mHomeFilterCulture.setText("");
                        mHomeFilterMoney.setText("");
                        upAge="";
                        showAge="";
                        culture="";
                        upCulture="";
                        showMoney = "";
                        upMoney = "";
                        ckAuthen = "";
                        isline = "";
                        xzya ="";
                        upxzya ="";
                    } else {
                        ckVip = "1";
                        mHome_filter_xiangzhao.setClickable(true);
                        mHomeFilterCulture.setClickable(true);
                        mHomeFilterMoney.setClickable(true);
                        mHomeFilterRbRenzheng.setClickable(true);
                        mHomeFilterAge.setClickable(true);
                        mHomeFilterRbOnline.setClickable(true);
                    }
                }
                break;
            case R.id.mHome_filter_rbOnline:
                if (isChecked) {
                    isline = "1";
                } else {
                    isline = "";
                }
                break;
            case R.id.mHome_filter_rbRenzheng:
                if (isChecked) {
                    ckAuthen = "1";
                } else {
                    ckAuthen = "";
                }
                break;
            case R.id.mHome_filter_nan:
                mHomeFilterSexbuxian.setSelected(false);
                if (isChecked) {
                    upsexs.add("1");
                } else {
                    upsexs.remove("1");
                }
                if (upsexs.contains("")) {
                    upsexs.remove("");
                }
                checkListIsChecked(1);
                break;
            case R.id.mHome_filter_nv:
                mHomeFilterSexbuxian.setSelected(false);
                if (isChecked) {
                    upsexs.add("2");
                } else {
                    upsexs.remove("2");
                }
                if (upsexs.contains("")) {
                    upsexs.remove("");
                }
                checkListIsChecked(1);
                break;
            case R.id.mHome_filter_yi:
                mHomeFilterSexbuxian.setSelected(false);
                if (isChecked) {
                    upsexs.add("3");
                } else {
                    upsexs.remove("3");
                }
                if (upsexs.contains("")) {
                    upsexs.remove("");
                }
                checkListIsChecked(1);
                break;
            case R.id.mHome_filter_yixing:
                mHomeFilterQuxiangbuxian.setSelected(false);
                if (isChecked) {
                    upsexuals.add("1");
                } else {
                    upsexuals.remove("1");
                }
                if (upsexuals.contains("")) {
                    upsexuals.remove("");
                }
                checkListIsChecked(2);
                break;
            case R.id.mHome_filter_tongxing:
                mHomeFilterQuxiangbuxian.setSelected(false);
                if (isChecked) {
                    upsexuals.add("2");
                } else {
                    upsexuals.remove("2");
                }
                if (upsexuals.contains("")) {
                    upsexuals.remove("");
                }
                checkListIsChecked(2);
                break;
            case R.id.mHome_filter_shuangxing:
                mHomeFilterQuxiangbuxian.setSelected(false);
                if (isChecked) {
                    upsexuals.add("3");
                } else {
                    upsexuals.remove("3");
                }
                if (upsexuals.contains("")) {
                    upsexuals.remove("");
                }
                checkListIsChecked(2);
                break;
            case R.id.mHome_filter_s:
                mHomeFilterJuesebuxian.setSelected(false);
                if (isChecked) {
                    uproles.add("S");
                } else {
                    uproles.remove("S");
                }
                if (uproles.contains("")) {
                    uproles.remove("");
                }
                checkListIsChecked(3);
                break;
            case R.id.mHome_filter_m:
                mHomeFilterJuesebuxian.setSelected(false);
                if (isChecked) {
                    uproles.add("M");
                } else {
                    uproles.remove("M");
                }
                if (uproles.contains("")) {
                    uproles.remove("");
                }
                checkListIsChecked(3);
                break;
            case R.id.mHome_filter_sm:
                mHomeFilterJuesebuxian.setSelected(false);
                if (isChecked) {
                    uproles.add("SM");
                } else {
                    uproles.remove("SM");
                }
                if (uproles.contains("")) {
                    uproles.remove("");
                }
                checkListIsChecked(3);
                break;
            case R.id.mHome_filter_lang:
                mHomeFilterJuesebuxian.setSelected(false);
                if (isChecked) {
                    uproles.add("~");
                } else {
                    uproles.remove("~");
                }
                if (uproles.contains("0")) {
                    uproles.remove("");
                }
                checkListIsChecked(3);
                break;
        }
    }

    public void checkListIsChecked(int flag) {
        switch (flag) {
            case 1:
                if (upsexs.size() == 0) {
                    for (int i = 0; i < sexs.size(); i++) {
                        sexs.get(i).setChecked(false);
                    }
                    mHomeFilterSexbuxian.setSelected(true);
                    upsexs.clear();
                    upsexs.add("");
                }
                break;
            case 2:
                if (upsexuals.size() == 0) {
                    for (int i = 0; i < sexuals.size(); i++) {
                        sexuals.get(i).setChecked(false);
                    }
                    mHomeFilterQuxiangbuxian.setSelected(true);
                    upsexuals.clear();
                    upsexuals.add("");
                }
                break;
            case 3:
                if (uproles.size() == 0) {
                    for (int i = 0; i < roles.size(); i++) {
                        roles.get(i).setChecked(false);
                    }
                    mHomeFilterJuesebuxian.setSelected(true);
                    uproles.clear();
                    uproles.add("");
                }
                break;
        }

    }

    private void VipDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_open_vip_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        TextView cancel = (TextView) window.findViewById(R.id.item_open_vip_cancel);
        TextView buy = (TextView) window.findViewById(R.id.item_open_vip_buy);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeFilterActivity.this, VipCenterActivity.class);
                intent.putExtra("headpic", headpic);
                intent.putExtra("uid",MyApp.uid);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
    }

//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        if (group == mHomeFilterRgSex) {
//            switch (checkedId) {
//                case R.id.mHome_filter_sexbuxian:
//                    sex = "0";
//                    break;
//                case R.id.mHome_filter_nan:
//                    sex = "1";
//                    break;
//                case R.id.mHome_filter_nv:
//                    sex = "2";
//                    break;
//                case R.id.mHome_filter_yi:
//                    sex = "3";
//                    break;
//            }
//        }
//        if (group == mHomeFilterRgQuxiang) {
//            switch (checkedId) {
//                case R.id.mHome_filter_quxiangbuxian:
//                    quxiang = "0";
//                    break;
//                case R.id.mHome_filter_yixing:
//                    quxiang = "1";
//                    break;
//                case R.id.mHome_filter_tongxing:
//                    quxiang = "2";
//                    break;
//                case R.id.mHome_filter_shuangxing:
//                    quxiang = "3";
//                    break;
//            }
//        }
//        if (group == mHomeFilterRgJue) {
//            switch (checkedId) {
//                case R.id.mHome_filter_juesebuxian:
//                    role = "0";
//                    break;
//                case R.id.mHome_filter_s:
//                    role = "S";
//                    break;
//                case R.id.mHome_filter_m:
//                    role = "M";
//                    break;
//                case R.id.mHome_filter_sm:
//                    role = "SM";
//                    break;
//            }
//        }
//    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(String message) {
        if (message.equals("kaitongsuccess")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
