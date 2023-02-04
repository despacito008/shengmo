package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.service.autofill.DateTransformation;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.SecretStateData;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.popuwindow.SuozaidiPopupWindow;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.view.QCheckBox;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//隐私页面
public class SecretActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.mEdit_name_return)
    ImageView mEditNameReturn;
    @BindView(R.id.mSecret_punishList)
    TextView mSecret_punishList;
    @BindView(R.id.mSecret_gz)
    CheckBox mSecretGz;
    @BindView(R.id.mSecret_qz)
    CheckBox mSecretQz;
    @BindView(R.id.mSecret_blackList)
    TextView mSecretBlackList;
    @BindView(R.id.mSecret_lishchakan)
    TextView mSecret_lishchakan;
    @BindView(R.id.mSecret_isOpen)
    TextView mSecretIsOpen;
    Handler handler = new Handler();
    @BindView(R.id.mSecret_location)
    CheckBox mSecretLocation;
    @BindView(R.id.mSecret_city)
    CheckBox mSecret_city;
    @BindView(R.id.mSecret_login_time)
    CheckBox mSecretLoginTime;
    @BindView(R.id.mSecret_stealth)
    CheckBox mSecret_stealth;
    @BindView(R.id.cb_wealth_val_switch)
    QCheckBox cbWealthValSwitch;
    @BindView(R.id.cb_charm_val_switch)
    QCheckBox cbCharmValSwitch;
    @BindView(R.id.mSecret_fans)
    CheckBox mSecretFans;
    //    @BindView(R.id.mSecret_isOwner_See_PhotoAlbum)
//    TextView mSecretIsOwnerSeePhotoAlbum;
//    @BindView(R.id.mSecret_isOwner_See_Dynamic)
//    TextView mSecretIsOwnerSeeDynamic;
//    @BindView(R.id.mSecret_isOwner_See_Discuss)
//    TextView mSecretIsOwnerSeeDiscuss;
    private String follow_list_switch = "";
    private String fans_list_switch = "";
    private String group_list_switch;
    private String photo_lock;
    private String login_time_switch;
    //地理位置的开关,0为关闭,1为打开
    private int isOpenLocation;
    SuozaidiPopupWindow suozaidiPopupWindow;
    private Drawable mDrawableDuiHao;
    //private int flag = 0;

    //    mSecret_isOpenAlbumJurisdiction   相册查看权限
    @BindView(R.id.mSecret_isOpenAlbumJurisdiction)
    TextView mSecret_isOpenAlbumJurisdiction;
    private String photo_rule;
    @BindView(R.id.mSecret_isOpendynamicdiction)//动态
            TextView mSecret_isOpendynamicdiction;
    private String dynamic_rule;
    private String nickname_rule;
    private String comment_rule;
    private String live_rule;
    @BindView(R.id.mSecret_isOpendcommentsdiction)//评论
            TextView mSecret_isOpendcommentsdiction;

    @BindView(R.id.mSecret_isOpendLiveHistory)
            TextView mSecretLiveHistory;
    String positiontype = "";
    private String location_switch;
    private String location_city_switch;
    private Unbinder bind;

    private String charm_val_switch;
    private String wealth_val_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret);
        bind = ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        EventBus.getDefault().register(this);
        mSecretGz.setOnCheckedChangeListener(this);
        mSecretFans.setOnCheckedChangeListener(this);
        mSecretQz.setOnCheckedChangeListener(this);
        mSecretLocation.setOnCheckedChangeListener(this);
        mSecret_city.setOnCheckedChangeListener(this);
        mSecretLoginTime.setOnCheckedChangeListener(this);
        mSecret_stealth.setOnCheckedChangeListener(this);


        getSecretSit();
        getFollewingList("");
        /*isOpenLocation = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "isOpenLocation", 1);
        if (isOpenLocation == 0) {
            mSecretLocation.setChecked(false);
        } else {
            mSecretLocation.setChecked(true);
        }*/
        mDrawableDuiHao = getResources().getDrawable(R.mipmap.duigou_hui);
        suozaidiPopupWindow = new SuozaidiPopupWindow(this);


    }


    //每次进这个页面都走
    @Override
    protected void onResume() {
        super.onResume();

        //相册
        int albumflag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "albumjurisdiction_flag", -1);//相册查看
        switch (albumflag) {
            case 0://全部
                mSecret_isOpenAlbumJurisdiction.setText("所有人");
                break;
            case 1://限制
                mSecret_isOpenAlbumJurisdiction.setText("好友/会员可见");
                break;

        }
        //动态
        int alertflag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "dynamicdiction_flag", -1);
        switch (alertflag) {
            case 0://全部
                mSecret_isOpendynamicdiction.setText("所有人");
                break;
            case 1://限制
                mSecret_isOpendynamicdiction.setText("好友/会员可见");
                break;

        }
        //评论
        int commentsflag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "commentsdiction_flag", -1);
        switch (commentsflag) {
            case 0://全部
                mSecret_isOpendcommentsdiction.setText("所有人");
                break;
            case 1://限制
                mSecret_isOpendcommentsdiction.setText("好友/会员可见");
                break;

        }
        //历史昵称
        int nikenameflag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "nickname_rule_flag", -1);
        switch (nikenameflag) {
            case 0:
                mSecret_lishchakan.setText("SVIP可见");
                break;
            case 1:
                mSecret_lishchakan.setText("仅自己可见");
                break;

        }

        //评论
        int liveflag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "livediction_flag", -1);
        switch (liveflag) {
            case 0://全部
                mSecretLiveHistory.setText("所有人");
                break;
            case 1://限制
                mSecretLiveHistory.setText("好友/会员可见");
                break;

        }

    }

    private void getSecretSit() {//得到所有的状态
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetSecretSit, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("secretactivity", "onSuccess: " + response);
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            SecretStateData data = new Gson().fromJson(response, SecretStateData.class);
                            if (data.getData().getFollow_list_switch() != null) {
                                follow_list_switch = data.getData().getFollow_list_switch();
                                if (follow_list_switch.equals("0")) {
                                    mSecretGz.setChecked(true);
                                } else {
                                    mSecretGz.setChecked(false);
                                }

                                fans_list_switch = data.getData().getFans_list_switch();
                                if (fans_list_switch.equals("0")) {
                                    mSecretFans.setChecked(true);
                                } else {
                                    mSecretFans.setChecked(false);
                                }

                                wealth_val_switch = data.getData().getWealth_val_switch();
                                if (wealth_val_switch.equals("0")) {
                                    cbWealthValSwitch.setChecked(true);
                                } else {
                                    cbWealthValSwitch.setChecked(false);
                                }

                                charm_val_switch = data.getData().getCharm_val_switch();
                                if (charm_val_switch.equals("0")) {
                                    cbCharmValSwitch.setChecked(true);
                                } else {
                                    cbCharmValSwitch.setChecked(false);
                                }

                                group_list_switch = data.getData().getGroup_list_switch();
                                if (group_list_switch.equals("0")) {
                                    mSecretQz.setChecked(true);
                                } else {
                                    mSecretQz.setChecked(false);
                                }
                                login_time_switch = data.getData().getLogin_time_switch();
                                if (login_time_switch.equals("0")) {
                                    mSecretLoginTime.setChecked(true);
                                } else {
                                    mSecretLoginTime.setChecked(false);
                                }
                                location_switch = data.getData().getLocation_switch();
                                location_city_switch = data.getData().getLocation_city_switch();
                                if (location_switch.equals("0")) {
                                    mSecretLocation.setChecked(true);
                                } else {
                                    mSecretLocation.setChecked(false);
                                }
                                if (location_city_switch.equals("0")) {
                                    mSecret_city.setChecked(true);
                                } else {
                                    mSecret_city.setChecked(false);
                                }

                                mSecretBlackList.setText(data.getData().getBlack_limit());
                                photo_lock = data.getData().getPhoto_lock();
                                if (photo_lock.equals("1")) {
                                    mSecretIsOpen.setText("未加密");
                                } else {
                                    mSecretIsOpen.setText("加密");
                                }
                                //获取数据后设置相册查看权限
                                photo_rule = data.getData().getPhoto_rule();//0是全部   1是限制
                                if (photo_rule.equals("1")) {
                                    mSecret_isOpenAlbumJurisdiction.setText("好友/会员可见");
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "albumjurisdiction_flag", 1);
                                } else {
                                    mSecret_isOpenAlbumJurisdiction.setText("所有人");
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "albumjurisdiction_flag", 0);
                                }
                                //主页动态查看权限
                                dynamic_rule = data.getData().getDynamic_rule();
                                if (dynamic_rule.equals("1")) {
                                    mSecret_isOpendynamicdiction.setText("好友/会员可见");
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicdiction_flag", 1);
                                } else {
                                    mSecret_isOpendynamicdiction.setText("所有人");
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicdiction_flag", 0);
                                }

                                //历史昵称查看权限
                                nickname_rule = data.getData().getNickname_rule();
                                if ("1".equals(nickname_rule)) {
                                    mSecret_lishchakan.setText("仅自己可见");
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "nickname_rule_flag", 1);
                                } else {
                                    mSecret_lishchakan.setText("SVIP可见");
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "nickname_rule_flag", 0);
                                }
                                //评论
                                comment_rule = data.getData().getComment_rule();
                                if ("1".equals(comment_rule)) {
                                    mSecret_isOpendcommentsdiction.setText("好友/会员可见");
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "commentsdiction_flag", 1);
                                } else {
                                    mSecret_isOpendcommentsdiction.setText("所有人");
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "commentsdiction_flag", 0);
                                }

                                //直播记录
                                live_rule = data.getData().getLive_rule();
                                if ("1".equals(live_rule)) {
                                    mSecretLiveHistory.setText("好友/会员可见");
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "livediction_flag", 1);
                                } else {
                                    mSecretLiveHistory.setText("所有人");
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "livediction_flag", 0);
                                }

                            }

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

    //, R.id.mSecret_isOwner_See_PhotoAlbum, R.id.mSecret_isOwner_See_Dynamic, R.id.mSecret_isOwner_See_Discuss
    @OnClick({R.id.mSecret_punishList, R.id.mEdit_name_return, R.id.mSecret_lishchakan, R.id.mSecret_blackList, R.id.mSecret_isOpen,//
            R.id.mSecret_isOpenAlbumJurisdiction, R.id.mSecret_isOpendynamicdiction, R.id.mSecret_isOpendcommentsdiction,//
            R.id.cb_wealth_val_switch, R.id.cb_charm_val_switch,R.id.mSecret_isOpendLiveHistory})

    public void onClick(View view) {  //所有的点击事件
        switch (view.getId()) {


            case R.id.cb_wealth_val_switch:
                if (SharedPreferencesUtils.getParam(this, "svip", "").equals("0")) {
                    VipDialog vipDialog = new VipDialog(SecretActivity.this, "SVIP可关闭财富值");
                    return;
                }
                if ("0".equals(wealth_val_switch)) {
                    cbWealthValSwitch.setChecked(false);
                    wealth_val_switch = "1";
                } else {
                    wealth_val_switch = "0";
                    cbWealthValSwitch.setChecked(true);
                }
                break;
            case R.id.cb_charm_val_switch:
                if (SharedPreferencesUtils.getParam(this, "svip", "").equals("0")) {
                    VipDialog vipDialog = new VipDialog(SecretActivity.this, "SVIP可关闭魅力值");
                    return;
                }
                if ("0".equals(charm_val_switch)) {
                    cbCharmValSwitch.setChecked(false);
                    charm_val_switch = "1";
                } else {
                    charm_val_switch = "0";
                    cbCharmValSwitch.setChecked(true);
                }
                break;


            case R.id.mEdit_name_return:
                finish();
                break;
            case R.id.mSecret_punishList:

                break;
            case R.id.mSecret_blackList:
                Intent intent1 = new Intent(this, BlackListActivity.class);
                startActivity(intent1);
                break;
            case R.id.mSecret_lishchakan:
                if (SharedPreferencesUtils.getParam(this, "svip", "").equals("1")) {
                    Intent intent = new Intent(this, HistnameseeActivity.class);
                    startActivity(intent);
                } else {
                    //ToastUtil.show(getApplicationContext(), "请您先开通会员...");
                    VipDialog vipDialog = new VipDialog(SecretActivity.this, "需要开通svip才可以使用~");
                }
                break;
            case R.id.mSecret_isOpen:
                if (SharedPreferencesUtils.getParam(this, "vip", "").equals("1")) {
                    Intent intent = new Intent(this, PhotoAlbumActivity.class);
                    intent.putExtra("photo_lock", photo_lock);
                    startActivity(intent);
                } else {
                    //ToastUtil.show(getApplicationContext(), "请您先开通会员...");
                    VipDialog vipDialog = new VipDialog(SecretActivity.this, "需要开通会员才可以使用相册密码~");
                }
                break;
            case R.id.mSecret_isOpenAlbumJurisdiction:  //跳转到相册查看权限页面
//                Intent intent = new Intent(this, AlbumJurisdictionActivity.class);
//                startActivity(intent);
//                if (SharedPreferencesUtils.getParam(this, "vip", "").equals("1")) {
                Intent intent = new Intent(this, AlbumJurisdictionActivity.class);
//                    intent.putExtra("photo_lock", photo_lock);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getApplicationContext(), "请您先开通会员...");
//                }
                break;
            case R.id.mSecret_isOpendynamicdiction:  //跳转到动态查看权限页面
//                Intent intent = new Intent(this, AlbumJurisdictionActivity.class);
//                startActivity(intent);
//                if (SharedPreferencesUtils.getParam(this, "vip", "").equals("1")) {
                intent = new Intent(this, DynamicdictionActivity.class);
//                    intent.putExtra("photo_lock", photo_lock);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getApplicationContext(), "请您先开通会员...");
//                }
                break;
            case R.id.mSecret_isOpendcommentsdiction:  //跳转到评论查看权限页面
//                Intent intent = new Intent(this, AlbumJurisdictionActivity.class);
//                startActivity(intent);
//                if (SharedPreferencesUtils.getParam(this, "vip", "").equals("1")) {
                intent = new Intent(this, CommentsPermissionsActivity.class);
//                    intent.putExtra("photo_lock", photo_lock);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getApplicationContext(), "请您先开通会员...");
//                }
                break;
            case R.id.mSecret_isOpendLiveHistory:  //跳转到评论查看权限页面
//                Intent intent = new Intent(this, AlbumJurisdictionActivity.class);
//                startActivity(intent);
//                if (SharedPreferencesUtils.getParam(this, "vip", "").equals("1")) {
                intent = new Intent(this, LiveRuleSettingActivity.class);
//                    intent.putExtra("photo_lock", photo_lock);
                startActivity(intent);
//                } else {
//                    ToastUtil.show(getApplicationContext(), "请您先开通会员...");
//                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.mSecret_gz:
                if (isChecked) {
                    follow_list_switch = "0";
                } else {
                    follow_list_switch = "1";
                }
                break;
            case R.id.mSecret_fans:
                if (isChecked) {
                    fans_list_switch = "0";
                } else {
                    fans_list_switch = "1";
                }
                break;
            case R.id.mSecret_qz:
                if (isChecked) {
                    group_list_switch = "0";
                } else {
                    group_list_switch = "1";
                }
                break;
            case R.id.mSecret_location:
                if (isChecked) {
                    /*SharedPreferencesUtils.setParam(getApplicationContext(), "isOpenLocation", 1);*/
                    location_switch = "0";
                } else {
                   /* SharedPreferencesUtils.setParam(getApplicationContext(), "isOpenLocation", 0);
                    MyApp.lat = "";
                    MyApp.lng = "";
                    MyApp.city = "";
                    MyApp.address = "";
                    UpLocationUtils.LogintimeAndLocation();*/
                    location_switch = "1";
                }
                break;
            case R.id.mSecret_city:
                if (isChecked) {
                    location_city_switch = "0";
                } else {
                    location_city_switch = "1";
                }
                break;
            case R.id.mSecret_login_time:
                if (isChecked) {
                    login_time_switch = "0";
                } else {
                    login_time_switch = "1";
                }
                break;

            case R.id.mSecret_stealth:

                if (SharedPreferencesUtils.getParam(this, "svip", "").equals("1")) {
                    if (isChecked) {
                        getFollewingList("1");
                    } else {
                        getFollewingList("0");
                    }
                } else {
                    //ToastUtil.show(getApplicationContext(), "请您先开通会员...");
                    VipDialog vipDialog = new VipDialog(SecretActivity.this, "需要开通svip才可以使用~");
                    mSecret_stealth.setChecked(false);
                }

                break;


        }
    }

    private void setSecretSit() { //设置是否选中
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("follow_list_switch", follow_list_switch);
        map.put("fans_list_switch",fans_list_switch);
        map.put("group_list_switch", group_list_switch);
        map.put("photo_lock", photo_lock);
        map.put("login_time_switch", login_time_switch);
        map.put("location_city_switch", location_city_switch);
        map.put("location_switch", location_switch);
        map.put("wealth_val_switch", wealth_val_switch);
        map.put("charm_val_switch", charm_val_switch);
        //相册 动态  评论
        map.put("nickname_rule", SharedPreferencesUtils.getParam(getApplicationContext(), "nickname_rule_flag", -1) + "");
        map.put("photo_rule", SharedPreferencesUtils.getParam(getApplicationContext(), "albumjurisdiction_flag", -1) + "");
        map.put("dynamic_rule", SharedPreferencesUtils.getParam(getApplicationContext(), "dynamicdiction_flag", -1) + "");
        map.put("comment_rule", SharedPreferencesUtils.getParam(getApplicationContext(), "commentsdiction_flag", -1) + "");
        map.put("live_rule",SharedPreferencesUtils.getParam(getApplicationContext(), "livediction_flag", -1) + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetSecretSit, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("secretactivity", "onSuccess: " + response);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String message) {
        if (message.equals("setSecretSitSucc")) {
            getSecretSit();
        }
        if (message.equals("blackSuccess")) {
            getSecretSit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setSecretSit();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

//        mDrawableDuiHao.setBounds(0, 0, mDrawableDuiHao.getMinimumWidth(), mDrawableDuiHao.getMinimumHeight());//引入图片
//        if (radioGroup.getId() == R.id.rg_suozaidi) {//
//            switch (checkedId) {//通过点击。设置后方对勾那个图片
//                case R.id.rb_zonghe://
//                    ((RadioButton) radioGroup.getChildAt(1)).setCompoundDrawables(null, null, null, null);
//                    ((RadioButton) radioGroup.getChildAt(0)).setCompoundDrawables(null, null, mDrawableDuiHao, null);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            suozaidiPopupWindow.dismiss();
//                        }
//                    }, 500);
//                    if (flag == 1) {
//                        mSecretIsOwnerSeePhotoAlbum.setText("所有人可见");
//                    } else if (flag == 2) {
//                        mSecretIsOwnerSeeDynamic.setText("所有人可见");
//                    } else if (flag == 3) {
//                        mSecretIsOwnerSeeDiscuss.setText("所有人可见");
//                    }
//                    break;
//                case R.id.rb_zuijin://
//                    ((RadioButton) radioGroup.getChildAt(1)).setCompoundDrawables(null, null, mDrawableDuiHao, null);
//                    ((RadioButton) radioGroup.getChildAt(0)).setCompoundDrawables(null, null, null, null);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            suozaidiPopupWindow.dismiss();
//                        }
//                    }, 500);
//                    if (flag == 1) {
//                        mSecretIsOwnerSeePhotoAlbum.setText("好友/会员可见");
//                    } else if (flag == 2) {
//                        mSecretIsOwnerSeeDynamic.setText("好友/会员可见");
//                    } else if (flag == 3) {
//                        mSecretIsOwnerSeeDiscuss.setText("好友/会员可见");
//                    }
//                    break;
//            }
//        }
    }

    private void getFollewingList(String type) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        if (type.equals("")) {
        } else {
            map.put("type", type);
        }
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.hideStateSwitch, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentfollow", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    JSONObject data = object.getJSONObject("data");
                                    String code = data.getString("code");
                                    if ("1".equals(code)) {
                                        mSecret_stealth.setChecked(true);
                                    } else {
                                        mSecret_stealth.setChecked(false);
                                    }
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
}
