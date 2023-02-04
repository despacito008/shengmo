package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.CusGridviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.customview.MyGridview;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.HomePageActivity;
import com.aiwujie.shengmo.utils.FilterGroupUtils;
import com.aiwujie.shengmo.utils.GetDeviceIdUtils;
import com.aiwujie.shengmo.utils.JsonObjectUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.SystemUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.VersionUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistTwoPageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.mRgist_two_page_return)
    ImageView mRgistTwoPageReturn;
    @BindView(R.id.item_title_name)
    TextView itemTitleName;
    @BindView(R.id.mRegist_two_page_gvLong)
    MyGridview mRegistTwoPageGvLong;
    @BindView(R.id.mRgist_two_page_shi)
    RadioButton mRgistTwoPageShi;
    @BindView(R.id.mRgist_two_page_meiyou)
    RadioButton mRgistTwoPageMeiyou;
    @BindView(R.id.mRegist_two_page_rgShijian)
    RadioGroup mRegistTwoPageRgShijian;
    @BindView(R.id.mRegist_two_page_gvYuexin)
    MyGridview mRegistTwoPageGvYuexin;
    @BindView(R.id.mRegist_two_page_erIntro)
    EditText mRegistTwoPageErIntro;
    @BindView(R.id.mRgist_two_page_next)
    Button mRgistTwoPageNext;
    @BindView(R.id.mRgist_two_page_qingdu)
    CheckBox mRgistTwoPageQingdu;
    @BindView(R.id.mRgist_two_page_zhongdu)
    CheckBox mRgistTwoPageZhongdu;
    @BindView(R.id.mRgist_two_page_zhongdu3)
    CheckBox mRgistTwoPageZhongdu3;
    @BindView(R.id.mRgist_two_page_liaotian)
    CheckBox mRgistTwoPageLiaotian;
    @BindView(R.id.mRgist_two_page_xianshi)
    CheckBox mRgistTwoPageXianshi;
    @BindView(R.id.mRgist_two_page_jiehun)
    CheckBox mRgistTwoPageJiehun;
    @BindView(R.id.mRegist_two_page_gvXueli)
    MyGridview mRegistTwoPageGvXueli;

    @BindView(R.id.howlong_layout)
    PercentLinearLayout howLongLayout;
    @BindView(R.id.havetry_layout)
    PercentLinearLayout haveTryLayout;
    @BindView(R.id.level_layout)
    PercentLinearLayout levelLayout;
    @BindView(R.id.havetry_btn_layout)
    PercentLinearLayout haveTryBtnLayout;
    @BindView(R.id.level_btn_layout)
    PercentLinearLayout levelBtnLayout;

    //    @BindView(R.id.mRgist_two_page_wantJunke)
//    RadioButton mRgistTwoPageWantJunke;
    private List<String> times;
    private List<String> educations;
    private List<String> salary;
    private CusGridviewAdapter adapterLong;
    private CusGridviewAdapter adapterEdu;
    private CusGridviewAdapter adapterSal;
    private String userinfoObj;
    private String basicObj;
    private String along;
    private String experience;
    private String level;
    private String want;
    private String culture;
    private String monthly;
    private String introduce;
    private JSONObject userinfo;
    private JSONObject basic;
    private JSONObject location;
    private JSONObject registObj;
    private Handler handler = new Handler();
    private List<String> levels = new ArrayList<>();
    private List<String> wants = new ArrayList<>();
    private int isFilter;
    private String sxSex;
    private String sxSexual;
    private String sn;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_two_page);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        sn = GetDeviceIdUtils.getSN(this);
        setData();
        setAdapter();
        setListener();
    }

    private void setData() {
        intent = getIntent();
        isFilter = intent.getIntExtra("isFilter", -1);
        if (isFilter == 1) {
            sxSex = intent.getStringExtra("sex");
            sxSexual = intent.getStringExtra("sexual");
        } else {
            sxSex = intent.getStringExtra("sex");
            sxSexual = intent.getStringExtra("sexual");
        }
        if ("-".equals(intent.getStringExtra("role"))) {
            howLongLayout.setVisibility(View.GONE);
            haveTryLayout.setVisibility(View.GONE);
            levelLayout.setVisibility(View.GONE);
            mRegistTwoPageGvLong.setVisibility(View.GONE);
            haveTryBtnLayout.setVisibility(View.GONE);
            levelBtnLayout.setVisibility(View.GONE);
        }
        userinfoObj = intent.getStringExtra("userinfoObj");
        basicObj = intent.getStringExtra("basicObj");
        basic = JsonObjectUtil.parse(basicObj);
        location = new JSONObject();
        userinfo = JsonObjectUtil.parse(userinfoObj);
        times = new ArrayList<>();
        times.add("1年以下");
        times.add("2-3年");
        times.add("4-6年");
        times.add("7-10年");
        times.add("10-20年");
        times.add("20年以上");
        educations = new ArrayList<>();
        educations.add("高中以下");
        educations.add("大专");
        educations.add("本科");
        educations.add("双学士");
        educations.add("硕士");
        educations.add("博士");
        educations.add("博士后");
        salary = new ArrayList<>();
        salary.add("2千以下");
        salary.add("2千-5千");
        salary.add("5千-1万");
        salary.add("1万-2万");
        salary.add("2万-5万");
        salary.add("5万以上");

    }

    private void setAdapter() {
        adapterLong = new CusGridviewAdapter(this, times);
        mRegistTwoPageGvLong.setAdapter(adapterLong);
        adapterEdu = new CusGridviewAdapter(this, educations);
        mRegistTwoPageGvXueli.setAdapter(adapterEdu);
        adapterSal = new CusGridviewAdapter(this, salary);
        mRegistTwoPageGvYuexin.setAdapter(adapterSal);
    }

    private void setListener() {
        mRegistTwoPageGvLong.setOnItemClickListener(this);
        mRegistTwoPageGvXueli.setOnItemClickListener(this);
        mRegistTwoPageGvYuexin.setOnItemClickListener(this);
        mRegistTwoPageRgShijian.setOnCheckedChangeListener(this);
        mRgistTwoPageQingdu.setOnCheckedChangeListener(this);
        mRgistTwoPageZhongdu.setOnCheckedChangeListener(this);
        mRgistTwoPageZhongdu3.setOnCheckedChangeListener(this);
        mRgistTwoPageLiaotian.setOnCheckedChangeListener(this);
        mRgistTwoPageXianshi.setOnCheckedChangeListener(this);
        mRgistTwoPageJiehun.setOnCheckedChangeListener(this);


    }

    @OnClick({R.id.mRgist_two_page_return, R.id.mRgist_two_page_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mRgist_two_page_return:
                finish();
                break;
            case R.id.mRgist_two_page_next:
                nextRegist();
                break;
        }
    }

    private void nextRegist() {
        introduce = mRegistTwoPageErIntro.getText().toString().trim();
        String levelstr = "";
        String wantstr = "";
        if (levels.size() != 0) {
            Collections.sort(levels);
            for (int i = 0; i < levels.size(); i++) {
                levelstr += levels.get(i) + ",";
            }
            level = levelstr.substring(0, levelstr.length() - 1);
        }
        if (wants.size() != 0) {
            Collections.sort(wants);
            for (int i = 0; i < wants.size(); i++) {
                wantstr += wants.get(i) + ",";
            }
            want = wantstr.substring(0, wantstr.length() - 1);
        }
        if (!"-".equals(intent.getStringExtra("role"))) {  //如果不是非斯慕同好
            if (TextUtil.isEmpty(along)) {
                ToastUtil.show(getApplicationContext(), "接触时间不能为空·...");
                return;
            }
            if (TextUtil.isEmpty(experience)) {
                ToastUtil.show(getApplicationContext(), "有无实践不能为空·...");
                return;
            }
            if (TextUtil.isEmpty(level)) {
                ToastUtil.show(getApplicationContext(), "程度不能为空·...");
                return;
            }
        }
        if (TextUtil.isEmpty(want)) {
            ToastUtil.show(getApplicationContext(), "我想找不能为空·...");
            return;
        }
        if (TextUtil.isEmpty(culture)) {
            ToastUtil.show(getApplicationContext(), "学历不能为空·...");
            return;
        }
        if (TextUtil.isEmpty(monthly)) {
            ToastUtil.show(getApplicationContext(), "月薪不能为空·...");
            return;
        }
//        if (TextUtil.isEmpty(introduce)) {
//            ToastUtil.show(getApplicationContext(), "自我介绍不能为空·...");
//            return;
//        }

        mRgistTwoPageNext.setEnabled(false);
        regist();

//        if (TextUtil.isEmpty(along)
//                || TextUtil.isEmpty(experience)
//                || TextUtil.isEmpty(level)
//                || TextUtil.isEmpty(want)
//                || TextUtil.isEmpty(culture)
//                || TextUtil.isEmpty(monthly)
//                || TextUtil.isEmpty(introduce)) {
//            ToastUtil.show(getApplicationContext(), "请填写完整注册信息·...");
//        } else {
//            mRgistTwoPageNext.setEnabled(false);
//            regist();
//        }
    }

    private void regist() {
        try {
            userinfo.put("along", along);
            userinfo.put("experience", experience);
            userinfo.put("level", level);
            userinfo.put("want", want);
            userinfo.put("culture", culture);
            userinfo.put("monthly", monthly);
            userinfo.put("introduce", introduce);
            userinfo.put("new_device_token", sn);
            userinfo.put("new_device_brand", SystemUtil.getDeviceBrand() + "-" + SystemUtil.getSystemModel());
            userinfo.put("new_device_version", SystemUtil.getSystemVersion());
            try {
                userinfo.put("new_device_appversion", VersionUtils.getVersion(getApplicationContext()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            location.put("lat", MyApp.lat);
            location.put("lng", MyApp.lng);
            location.put("addr", MyApp.address);
            location.put("city", MyApp.city);
            location.put("province", MyApp.province);
            registObj = new JSONObject();
            registObj.put("basic", basic);
            registObj.put("userinfo", userinfo);
            registObj.put("location", location);
            if (!TextUtil.isEmpty(getIntent().getStringExtra("invite_code"))) {
                registObj.put("register_invite_code",getIntent().getStringExtra("invite_code"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new RegistTask().execute(new RegistRequest(registObj.toString()));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.mRegist_two_page_gvLong:
                adapterLong.setSelectIndex(position);
                adapterLong.notifyDataSetChanged();
                along = position + 1 + "";
//                ToastUtil.show(getApplicationContext(),times.get(position));
                break;
            case R.id.mRegist_two_page_gvXueli:
                adapterEdu.setSelectIndex(position);
                adapterEdu.notifyDataSetChanged();
                culture = position + 1 + "";
//                ToastUtil.show(getApplicationContext(),educations.get(position));
                break;
            case R.id.mRegist_two_page_gvYuexin:
                adapterSal.setSelectIndex(position);
                adapterSal.notifyDataSetChanged();
                monthly = position + 1 + "";
//                ToastUtil.show(getApplicationContext(),salary.get(position));
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.mRgist_two_page_qingdu:
                if (isChecked) {
                    levels.add("1");
                } else {
                    levels.remove("1");
                }
                break;
            case R.id.mRgist_two_page_zhongdu:
                if (isChecked) {
                    levels.add("2");
                } else {
                    levels.remove("2");
                }
                break;
            case R.id.mRgist_two_page_zhongdu3:
                if (isChecked) {
                    levels.add("3");
                } else {
                    levels.remove("3");
                }
                break;
            case R.id.mRgist_two_page_liaotian:
                if (isChecked) {
                    wants.add("1");
                } else {
                    wants.remove("1");
                }
                break;
            case R.id.mRgist_two_page_xianshi:
                if (isChecked) {
                    wants.add("2");
                } else {
                    wants.remove("2");
                }
                break;
            case R.id.mRgist_two_page_jiehun:
                if (isChecked) {
                    wants.add("3");
                } else {
                    wants.remove("3");
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mRgist_two_page_shi:
                experience = "1";
                break;
            case R.id.mRgist_two_page_meiyou:
                experience = "2";
                break;
        }
    }

    class RegistTask extends AsyncTask<RegistRequest, Void, String> {

        @Override
        protected String doInBackground(RegistRequest... params) {
            RegistRequest paymentRequest = params[0];
            String data = null;
            String json = paymentRequest.registObjectStr;
            try {
                data = postJson(HttpUrl.Registernewrd, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if (null == data) {
                return;
            }
            try {
                JSONObject obj = new JSONObject(data);
                switch (obj.getInt("retcode")) {

                    case 2000:
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        JSONObject obj1 = obj.getJSONObject("data");
                        MyApp.uid = obj1.getString("uid");
                        MyApp.token = obj1.getString("t_sign");
                        SharedPreferencesUtils.setParam(getApplicationContext(),"t_sign", MyApp.token);
                        String url_token = obj1.getString("token");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "uid", MyApp.uid);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "token", MyApp.token);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "url_token", url_token);
                        //String filterFlag=FilterGroupUtils.isWhatSexual2(sxSex,sxSexual);
//
//                        if (sxSex.equals("3") && sxSexual.equals("3")) {
//                            SharedPreferencesUtils.setParam(getApplicationContext(), "filterSex", sxSex); //性取向筛选字段
//                            SharedPreferencesUtils.setParam(getApplicationContext(), "filterZong", "1");
//                            SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSex", sxSex);
//                            SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSwitch", "1");
//                        } else {
                            SharedPreferencesUtils.setParam(getApplicationContext(), "filterSex", sxSex);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "filterQx", sxSexual);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "filterZong", "1");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSex", sxSex);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSexual", sxSexual);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSwitch", "1");
//                        }

                        String filterGroupFlag = FilterGroupUtils.isWhatSexual(sxSex, sxSexual);
                        if (filterGroupFlag.equals("1")) {
                            SharedPreferencesUtils.setParam(getApplicationContext(), "groupSwitch", "0");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "groupFlag", "0");
                        } else {
                            SharedPreferencesUtils.setParam(getApplicationContext(), "groupSwitch", "1");
                            switch (filterGroupFlag) {
                                case "2":
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "groupFlag", "2");
                                    break;
                                case "3":
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "groupFlag", "3");
                                    break;
                                case "4":
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "groupFlag", "4");
                                    break;
                            }
                        }
                        SharedPreferencesUtils.setParam(getApplicationContext(), "mysex", sxSex);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "mysexual", sxSexual);
                        /*动态筛选*/
                        SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSex", sxSexual);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSexual", sxSex);
                        /*群组筛选*/
                        SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSex", sxSex);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSexual", sxSexual);

                        //Intent intent = new Intent(RegistTwoPageActivity.this, MainActivity.class);
                        Intent intent = new Intent(RegistTwoPageActivity.this, HomePageActivity.class);
                        intent.putExtra("gotoByRegister",1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    case 50001:
                    case 50002:
                        EventBus.getDefault().post(new TokenFailureEvent());
                        break;
                    default:
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        mRgistTwoPageNext.setEnabled(true);
                        break;
                }

            } catch (JSONException e) {
                mRgistTwoPageNext.setEnabled(true);
                e.printStackTrace();
            }
        }
    }

    private String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(HttpUrl.NetPic() + url).addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", "")).post(body).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    class RegistRequest {
        String registObjectStr;

        public RegistRequest(String registObjectStr) {
            this.registObjectStr = registObjectStr;
        }
    }

}
