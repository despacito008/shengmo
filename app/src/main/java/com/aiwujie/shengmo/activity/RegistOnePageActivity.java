package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.base.BaseActivity;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.customview.WheelView;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.view.HeightAndWeightPop;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.CropUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UriUtil;
import com.aiwujie.shengmo.view.LineWrapRadioGroup;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.yalantis.ucrop.UCrop;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.feezu.liuli.timeselector.TimeSelector;
import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;
import static com.aiwujie.shengmo.http.HttpUrl.checkInviteCode;

public class RegistOnePageActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, OnItemClickListener, EasyPermissions.PermissionCallbacks, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.mRgist_one_page_return)
    ImageView mRgistOnePageReturn;
    @BindView(R.id.item_title_name)
    TextView itemTitleName;
    @BindView(R.id.mRgist_one_page_icon)
    ImageView mRgistOnePageIcon;
    @BindView(R.id.mRgist_one_page_name)
    EditText mRgistOnePageName;
    @BindView(R.id.mRgist_one_page_nan)
    RadioButton mRgistOnePageNan;
    @BindView(R.id.mRgist_one_page_nv)
    RadioButton mRgistOnePageNv;
    @BindView(R.id.mRgist_one_page_yi)
    RadioButton mRgistOnePageYi;
    @BindView(R.id.mRgist_one_page_gong)
    RadioButton mRgistOnePageGay1;
    @BindView(R.id.mRgist_one_page_shou)
    RadioButton mRgistOnePageGay0;
    @BindView(R.id.mRgist_one_page_ban)
    RadioButton mRgistOnePageGayH;
    @BindView(R.id.mRgist_one_page_gay_m)
    RadioButton mRgistOnePageGayM;
    @BindView(R.id.mRgist_one_page_t)
    RadioButton mRgistOnePageLes1;
    @BindView(R.id.mRgist_one_page_p)
    RadioButton mRgistOnePageLes0;
    @BindView(R.id.mRgist_one_page_h)
    RadioButton mRgistOnePageLesH;
    @BindView(R.id.mRgist_one_page_les_m)
    RadioButton mRgistOnePageLesM;

    @BindView(R.id.mRegist_one_page_birthday)
    TextView mRegistOnePageBirthday;
    @BindView(R.id.mRegist_one_page_height)
    TextView mRegistOnePageHeight;
    //    @BindView(R.id.mRegist_one_page_weight)
//    TextView mRegistOnePageWeight;
    @BindView(R.id.mRgist_one_page_s)
    RadioButton mRgistOnePageS;
    @BindView(R.id.mRgist_one_page_m)
    RadioButton mRgistOnePageM;
    @BindView(R.id.mRgist_one_page_sm)
    RadioButton mRgistOnePageSm;
    @BindView(R.id.mRgist_one_page_lang)
    RadioButton mRgistOnePageLang;
    //    @BindView(R.id.mRgist_one_page_yixing)
//    RadioButton mRgistOnePageYixing;
//    @BindView(R.id.mRgist_one_page_tongxing)
//    RadioButton mRgistOnePageTongxing;
//    @BindView(R.id.mRgist_one_page_shuangxing)
//    RadioButton mRgistOnePageShuangxing;
    @BindView(R.id.mRgist_one_page_next)
    Button mRgistOnePageNext;
    @BindView(R.id.mRegist_one_page_rgSex)
    RadioGroup mRegistOnePageRgSex;
    @BindView(R.id.mRegist_one_page_rgJue)
    LineWrapRadioGroup mRegistOnePageRgJue;
    @BindView(R.id.mRegist_one_page_gay)
    RadioGroup mRegistOnePageRgGay;
    @BindView(R.id.mRegist_one_page_les)
    RadioGroup mRegistOnePageRgLes;
    @BindView(R.id.mRgist_one_page_no)
    RadioButton mRegistOnePageRgNo;
    //    @BindView(R.id.mRegist_one_page_rgQuxiang)
//    RadioGroup mRegistOnePageRgQuxiang;
    @BindView(R.id.mRegist_one_page_view)
    LinearLayout mView;
    @BindView(R.id.mRgist_one_page_yixing)
    CheckBox mRgistOnePageYixing;
    @BindView(R.id.mRgist_one_page_tongxing)
    CheckBox mRgistOnePageTongxing;
    @BindView(R.id.mRgist_one_page_shuangxing)
    CheckBox mRgistOnePageShuangxing;
    @BindView(R.id.gay_layout)
    PercentLinearLayout gayLayout;
    @BindView(R.id.les_layout)
    PercentLinearLayout lesLayout;
    @BindView(R.id.mRegist_invite_code)
    EditText mRegistInviteCode;

    private TimeSelector timeSelector;
    private String currentTime;
    private SimpleDateFormat sdf;
    private ArrayList<String> heights;
    private ArrayList<String> weights;
    private PopupWindow mPopWindow;
    private String height = "175cm";
    private String weight = "60kg";
    private String headurl;
    private String nickname;
    private String sex;
    private String birthday;
    private String heightAndweight;
    private String role;
    private String sexOrientation;
    private String attribute;
    private JSONObject userinfoObj;
    private String basicObj;
    private JSONObject basicObject;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private Uri cropUri;
    private File cropImage;
    private String channel;
    private String tempNickname;
    private List<String> sexOritations = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 152:
                    String s = (String) msg.obj;
                    Log.i("icon", s);
                    BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
//                    headurl = "http://59.110.28.150:888/" + beanicon.getData();
                    //headurl = NetPic() + beanicon.getData();
                    headurl = HttpUrl.getImagePath(beanicon.getData());
                    ToastUtil.show(getApplicationContext(), "头像上传成功");
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private String inviteCode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_one_page);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
//        instance=this;
        setData();
        setListener();
        Intent intent = getIntent();
        int loginmode = intent.getIntExtra("loginmode", -1);
        //0-通过邮箱和手机注册  1-第三方登录注册

        if (loginmode == 0) {
            basicObj = intent.getStringExtra("basicObj");
            inviteCode = intent.getStringExtra("invite_code");
            //mRegistInviteCode.setVisibility(View.GONE);
        } else if (loginmode == 1) {
            String openid = intent.getStringExtra("openid");
            nickname = intent.getStringExtra("nickname");
            headurl = intent.getStringExtra("headurl");
            channel = intent.getStringExtra("channel");
            //mRgistOnePageName.setText(nickname);
            GlideImgManager.glideLoader(this, headurl, R.mipmap.morentouxiang, R.mipmap.morentouxiang, mRgistOnePageIcon, 0);
            basicObject = new JSONObject();
            try {
                basicObject.put("openid", openid);
                basicObject.put("channel", channel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            basicObj = basicObject.toString();
            mRegistInviteCode.setVisibility(View.VISIBLE);
        }
        Log.i("registonepage", "onCreate: " + basicObj);
        //隐藏
      //  mRegistInviteCode.setVisibility(View.GONE);

    }

    private void setData() {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        currentTime=sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR);
        int mMinute = calendar.get(Calendar.MINUTE);
        try {
            Date date1 = sdf.parse((mYear - 18) + "-" + (mMonth + 1) + "-" + (mDay) + " " + mHour + ":" + mMinute);
            currentTime = sdf.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        heights = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            heights.add(120 + i + "cm");
        }
        weights = new ArrayList<>();
        for (int i = 0; i < 171; i++) {
            weights.add(30 + i + "kg");
        }
    }

    private void setListener() {
        mRegistOnePageRgSex.setOnCheckedChangeListener(this);
        mRegistOnePageRgJue.setOnCheckedChangeListener(this);
        mRgistOnePageYixing.setOnCheckedChangeListener(this);
        mRgistOnePageTongxing.setOnCheckedChangeListener(this);
        mRgistOnePageShuangxing.setOnCheckedChangeListener(this);
        mRegistOnePageRgGay.setOnCheckedChangeListener(this);
        mRegistOnePageRgLes.setOnCheckedChangeListener(this);
//        mRegistOnePageRgQuxiang.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.mRgist_one_page_return, R.id.mRgist_one_page_icon, R.id.mRegist_one_page_birthday, R.id.mRegist_one_page_height, R.id.mRgist_one_page_next})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mRgist_one_page_return:
                finish();
                break;
            case R.id.mRgist_one_page_icon:
                showSelectPic();
                break;
            case R.id.mRegist_one_page_birthday:
                // 参数说明：ResultHandler为选取时间后的回调 startDate，endDate为时间控件的可选起始时间和结束时间。
                timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        String times = time.substring(0, time.length() - 6);
                        mRegistOnePageBirthday.setText(times);
//                        ToastUtil.show(getApplicationContext(),times);
                    }
                }, "1900-01-01 00:00", currentTime);
                timeSelector.setMode(TimeSelector.MODE.YMD);
                timeSelector.show();
                break;
            case R.id.mRegist_one_page_height:
                showpopwindow();
                break;
//            case R.id.mRegist_one_page_weight:
//                break;
            case R.id.mRgist_one_page_next:
                //进入下一步注册
                registNext();
                break;
        }
    }

    private void registNext() {
        nickname = mRgistOnePageName.getText().toString().trim();
        birthday = mRegistOnePageBirthday.getText().toString().trim();
        heightAndweight = mRegistOnePageHeight.getText().toString().trim();
        String sexsual = "";
        if (sexOritations.size() != 0) {
            Collections.sort(sexOritations);
            for (int i = 0; i < sexOritations.size(); i++) {
                sexsual += sexOritations.get(i) + ",";
            }
            sexOrientation = sexsual.substring(0, sexsual.length() - 1);
        }
        if (TextUtil.isEmpty(headurl)) {
            ToastUtil.show(RegistOnePageActivity.this, "请上传头像");
            return;
        }
        if (TextUtil.isEmpty(nickname)) {
            ToastUtil.show(RegistOnePageActivity.this, "请输入昵称");
            return;
        }
        if (TextUtil.isEmpty(sex)) {
            ToastUtil.show(RegistOnePageActivity.this, "请选择性别");
            return;
        }
        if (TextUtil.isEmpty(birthday)) {
            ToastUtil.show(RegistOnePageActivity.this, "请填写生日");
            return;
        }
        if (TextUtil.isEmpty(heightAndweight)) {
            ToastUtil.show(RegistOnePageActivity.this, "请填写身高体重");
            return;
        }
        if (TextUtil.isEmpty(role)) {
            ToastUtil.show(RegistOnePageActivity.this, "请选择角色");
            return;
        }
        if (TextUtil.isEmpty(sexOrientation)) {
            ToastUtil.show(RegistOnePageActivity.this, "请选择取向");
            return;
        }

        if (TextUtil.isEmpty(headurl) || TextUtil.isEmpty(nickname) || TextUtil.isEmpty(sex) || TextUtil.isEmpty(birthday) || TextUtil.isEmpty(heightAndweight) || TextUtil.isEmpty(role) || TextUtil.isEmpty(sexOrientation)) {
            ToastUtil.show(getApplicationContext(), "请完善注册信息...");
        } else if (TextUtil.isEmpty(attribute) && (gayLayout.getVisibility() == View.VISIBLE || lesLayout.getVisibility() == View.VISIBLE)) {
            ToastUtil.show(getApplicationContext(), "请选择属性...");
        } else {
            //判断昵称中是否包含空格
            if (nickname.indexOf(" ") == -1) {
                if (!nickname.equals(tempNickname)) {
                        if (mRegistInviteCode.getVisibility() == View.GONE || TextUtil.isEmpty(mRegistInviteCode.getText().toString())) {
                            chargeSecond();
                        } else {
                            checkInviteCode();
                        }
                    } else {
                        ToastUtil.show(getApplicationContext(), "请您修改注册信息后再提交");
                    }
            } else {
                ToastUtil.show(getApplicationContext(), "昵称中不能有空格...");
            }
        }
    }

    private void chargeSecond() {
        Map<String, String> map = new HashMap<>();
        map.put("nickname", nickname);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ChargeSecond, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    userinfoJson();
                                    Intent intent = new Intent(RegistOnePageActivity.this, RegistTwoPageActivity.class);
                                    intent.putExtra("basicObj", basicObj);
                                    intent.putExtra("userinfoObj", userinfoObj.toString());
                                    intent.putExtra("role", role);
                                    intent.putExtra("invite_code",mRegistInviteCode.getVisibility() == View.VISIBLE ? mRegistInviteCode.getText().toString() : inviteCode);
                                    //判断是否进入首页直接筛选，1为筛选同志，2为筛选拉拉
                                    registFilter(intent);
                                    startActivity(intent);
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                                default:
                                    tempNickname = mRgistOnePageName.getText().toString().trim();
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
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

    private void registFilter(Intent intent) {
        intent.putExtra("sex", sex);
        intent.putExtra("sexual", sexOrientation);
//        if (sex.equals(sexOrientation)) {
//            intent.putExtra("isFilter", 1);
////            if(sex.equals("1")) {
//            intent.putExtra("sex", sex);
//            intent.putExtra("sexual", sexOrientation);
////            }else if(sex.equals("2")){
////                intent.putExtra("sex", sex);
////                intent.putExtra("sexual", sexOrientation);
////            }else{
////                intent.putExtra("sex",sex);
////                intent.putExtra("sexual",sexOrientation);
////            }
//        } else {
//            intent.putExtra("isFilter", 2);
//            intent.putExtra("sex", sex);
//            intent.putExtra("sexual", sexOrientation);
////            if (sex.equals("1")) {
////                if (sexOrientation.equals("3")) {
////                    intent.putExtra("sex", sex);
////                    intent.putExtra("sexual", sexOrientation);
////                }
////                if (sexOrientation.equals("1,3")) {
////                    intent.putExtra("sex", sex);
////                    intent.putExtra("sexual", sexOrientation);
////                }
////            } else {
////                intent.putExtra("sex", sex);
////                intent.putExtra("sexual", sexOrientation);
////            }
////            if (sex.equals("2")) {
////                if (sexOrientation.equals("2")) {
////                    intent.putExtra("sex", sex);
////                    intent.putExtra("sexual", sexOrientation);
////                }
////                if (sexOrientation.equals("2,3")) {
////                    intent.putExtra("sex", sex);
////                    intent.putExtra("sexual", sexOrientation);
////                }
////            } else {
////                intent.putExtra("sex", sex);
////                intent.putExtra("sexual", sexOrientation);
////            }
//        }
    }

    private void showpopwindow() {
        showHeightAndWeightPop();
    }

    public void showHeightAndWeightPop() {
        HeightAndWeightPop heightAndWeightPop = new HeightAndWeightPop(RegistOnePageActivity.this);
        heightAndWeightPop.showPopupWindow();
        heightAndWeightPop.setOnHeightAndWeightListener(new HeightAndWeightPop.OnHeightAndWeightListener() {
            @Override
            public void doChooseComplete(@NotNull String h, @NotNull String w) {
                height = h;
                weight = w;
                heightAndweight = height + "/" + weight;
                mRegistOnePageHeight.setText(heightAndweight);

            }
        });

    }

    private void showSelectPic() {
        new AlertView(null, null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.mRegist_one_page_rgSex:
                switch (checkedId) {
                    case R.id.mRgist_one_page_nan:
                        sex = "1";
                        lesLayout.setVisibility(View.GONE);
                        lesLayout.setSelected(false);
                        if ("1".equals(sex) && sexOritations.contains("1")) {
                            mRegistOnePageRgGay.clearCheck();
                            gayLayout.setVisibility(View.VISIBLE);
                        } else {
                            gayLayout.setVisibility(View.GONE);
                            gayLayout.setSelected(false);
                            if (gayLayout.getVisibility() == View.GONE && lesLayout.getVisibility() == View.GONE)
                                attribute = "";
                        }
                        break;
                    case R.id.mRgist_one_page_nv:
                        sex = "2";
                        gayLayout.setVisibility(View.GONE);
                        gayLayout.setSelected(false);
                        if ("2".equals(sex) && sexOritations.contains("2")) {
                            mRegistOnePageRgLes.clearCheck();
                            attribute = "";
                            lesLayout.setVisibility(View.VISIBLE);
                        } else {
                            lesLayout.setVisibility(View.GONE);
                            lesLayout.setSelected(false);
                            if (gayLayout.getVisibility() == View.GONE && lesLayout.getVisibility() == View.GONE)
                                attribute = "";
                        }
                        break;
                    case R.id.mRgist_one_page_yi:
                        sex = "3";
                        lesLayout.setVisibility(View.GONE);
                        gayLayout.setVisibility(View.GONE);
                        gayLayout.setSelected(false);
                        lesLayout.setSelected(false);
                        attribute = "";

                        break;
                }
                break;
            case R.id.mRegist_one_page_rgJue:
                switch (checkedId) {
                    case R.id.mRgist_one_page_s:
                        role = "S";
                        break;
                    case R.id.mRgist_one_page_m:
                        role = "M";
                        break;
                    case R.id.mRgist_one_page_sm:
                        role = "SM";
                        break;
                    case R.id.mRgist_one_page_lang:
                        role = "~";
                        break;
                    case R.id.mRgist_one_page_no:
                        role = "-";
                        break;
                }
                break;
            case R.id.mRegist_one_page_gay:
                switch (checkedId) {
                    case R.id.mRgist_one_page_gong:
                        attribute = "1";
                        break;
                    case R.id.mRgist_one_page_shou:
                        attribute = "0";
                        break;
                    case R.id.mRgist_one_page_ban:
                        attribute = "0.5";
                        break;
                    case R.id.mRgist_one_page_m:
                        attribute = "~";
                        break;
                }
                break;
            case R.id.mRegist_one_page_les:
                switch (checkedId) {
                    case R.id.mRgist_one_page_t:
                        attribute = "T";
                        break;
                    case R.id.mRgist_one_page_p:
                        attribute = "P";
                        break;
                    case R.id.mRgist_one_page_h:
                        attribute = "H";
                        break;
                    case R.id.mRgist_one_page_les_m:
                        attribute = "~";
                        break;
                }
                break;
//            case R.id.mRegist_one_page_rgQuxiang:
//                switch (checkedId){
//                    case R.id.mRgist_one_page_yixing:
//                        sexOrientation="1";
//                        break;
//                    case R.id.mRgist_one_page_tongxing:
//                        sexOrientation="2";
//                        break;
//                    case R.id.mRgist_one_page_shuangxing:
//                        sexOrientation="3";
//                        break;
//                }
//                break;
        }
    }

    //选择照片的点击事件
    @Override
    public void onItemClick(Object o, int position, String data) {
        switch (position) {
            case 0:
                takePhone(mRgistOnePageIcon);
                break;
            case 1:
                choosePhone(mRgistOnePageIcon);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.mRgist_one_page_yixing:
                if (isChecked) {
                    sexOritations.add("1");
//                    sexOrientation = "1";
                    if ("1".equals(sex)) {
                        mRegistOnePageRgGay.clearCheck();
                        attribute = "";
                        gayLayout.setVisibility(View.VISIBLE);
                        lesLayout.setVisibility(View.GONE);
                    } else {
                        gayLayout.setVisibility(View.GONE);
                        gayLayout.setSelected(false);
                        if (gayLayout.getVisibility() == View.GONE && lesLayout.getVisibility() == View.GONE) {
                            attribute = "";
                        }

                    }
                } else {
                    sexOritations.remove("1");
                    gayLayout.setVisibility(View.GONE);
                    gayLayout.setSelected(false);
                    if (gayLayout.getVisibility() == View.GONE && lesLayout.getVisibility() == View.GONE) {

                        attribute = "";

                    }
                }
                break;
            case R.id.mRgist_one_page_tongxing:
                if (isChecked) {
                    sexOritations.add("2");
//                    sexOrientation = "2";
                    if ("2".equals(sex)) {
                        mRegistOnePageRgLes.clearCheck();
                        attribute = "";
                        lesLayout.setVisibility(View.VISIBLE);
                        gayLayout.setVisibility(View.GONE);
                    } else {
                        lesLayout.setVisibility(View.GONE);
                        lesLayout.setSelected(false);
                        if (gayLayout.getVisibility() == View.GONE && lesLayout.getVisibility() == View.GONE) {
                            attribute = "";
                        }
                    }
                } else {
                    sexOritations.remove("2");
                    lesLayout.setVisibility(View.GONE);
                    lesLayout.setSelected(false);
                    if (gayLayout.getVisibility() == View.GONE && lesLayout.getVisibility() == View.GONE) {

                        attribute = "";
                    }
                }
                break;
            case R.id.mRgist_one_page_shuangxing:
                if (isChecked) {
                    sexOritations.add("3");
//                    sexOrientation = "3";
                } else {
                    sexOritations.remove("3");
                }
                break;
        }
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    private void userinfoJson() {
        userinfoObj = new JSONObject();
        try {
            if (height.contains("cm")) {
                height = height.substring(0, height.length() - 2);
            }
            if (weight.contains("kg")) {
                weight = weight.substring(0, weight.length() - 2);
            }
            userinfoObj.put("head_pic", headurl);
            userinfoObj.put("nickname", nickname);
            userinfoObj.put("birthday", birthday);
            userinfoObj.put("tall", height);
            userinfoObj.put("weight", weight);
            userinfoObj.put("role", role);
            userinfoObj.put("sex", sex);
            userinfoObj.put("attribute", attribute);
            userinfoObj.put("sexual", sexOrientation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //拍照
    public void takePhone(View view) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
//        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
//            //第二个参数是被拒绝后再次申请该权限的解释
//            //第三个参数是请求码
//            //第四个参数是要申请的权限
//            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", MY_PERMISSIONS_REQUEST_CALL_PHONE, perms);
//        } else {
//            takePhoto();
//        }
        XXPermissions.with(RegistOnePageActivity.this)
                .permission(perms)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            takePhoto();
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        ToastUtil.show("拍照需要以下权限:\n\n1.使用照相机\n\n2.储存图片");
                        if (never) {
                            XXPermissions.startPermissionActivity(RegistOnePageActivity.this);
                        }
                    }
                });

    }

    //相册
    public void choosePhone(View view) {
//        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
//            //第二个参数是被拒绝后再次申请该权限的解释
//            //第三个参数是请求码
//            //第四个参数是要申请的权限
//            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", MY_PERMISSIONS_REQUEST_CALL_PHONE2, perms);
//        } else {
//            choosePhoto();
//        }
        XXPermissions.with(RegistOnePageActivity.this)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        choosePhoto();
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        ToastUtil.show("图片选择需要以下权限:\n\n1.访问设备上的照片");
                        if (never) {
                            XXPermissions.startPermissionActivity(RegistOnePageActivity.this);
                        }
                    }
                });
    }

    /**
     * 拍照
     */
    void takePhoto() {
        cropImage = new File(Environment.getExternalStorageDirectory(), "/" + UUID.randomUUID() + ".jpg");
        cropUri = Uri.fromFile(cropImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册选取图片
     */
    void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                Log.i("registonepageactivity", "onActivityResult: " + data);
                if (data == null) {
                    return;
                }
                Uri crUri = data.getData();
                CropUtils.startUCrop(this, crUri, CROP_REQUEST_CODE, 1, 1, 750, 750);
                break;
            case CAMERA_REQUEST_CODE:
                CropUtils.startUCrop(this, cropUri, CROP_REQUEST_CODE, 1, 1, 750, 750);
                break;
            case CROP_REQUEST_CODE:
                Uri resultUri = null;
                try {
                    resultUri = UCrop.getOutput(data);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                if (resultUri != null) {
                    Bitmap photo = PhotoUploadUtils.decodeUriAsBitmap(resultUri, this);
//                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    Bitmap bitmap=comp(photo);
                    if (photo != null) {
                        photo.compress(Bitmap.CompressFormat.JPEG, 80, stream);// (0-100)压缩文件
                        //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
//                        mRgistOnePageIcon.setImageBitmap(photo); //把图片显示在ImageView控件上
                        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
                        circleDrawable.getPaint().setAntiAlias(true);
                        circleDrawable.setCircular(true);
                        mRgistOnePageIcon.setImageDrawable(circleDrawable);
                        ToastUtil.show(getApplicationContext(), "头像上传中,请稍后...");
                    }
                    // 获得字节流
                    ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
                    PhotoUploadTask put = new PhotoUploadTask(
//                            "http://59.110.28.150:888/Api/Api/fileUpload"
                            NetPic() + "Api/Api/fileUpload"
                            , is, this, handler);
                    put.start();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (!perms.contains(Manifest.permission.CAMERA)) {
                return;
            }
            if (!perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }
            takePhoto();
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            if (!perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }
            choosePhoto();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms.contains(Manifest.permission.CAMERA)) {
            ToastUtil.show(RegistOnePageActivity.this, "授权失败,请开启相机权限");
            return;
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(RegistOnePageActivity.this, "授权失败,请开启读写权限");
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void checkInviteCode() {
        HttpHelper.getInstance().checkInviteCode(mRegistInviteCode.getText().toString(), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                chargeSecond();
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(RegistOnePageActivity.this,msg);
            }
        });
    }
}

