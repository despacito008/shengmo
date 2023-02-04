package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.CusGridviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.bean.OwnerMsgData;
import com.aiwujie.shengmo.bean.SecretStateData;
import com.aiwujie.shengmo.bean.SwitchMarkBean;
import com.aiwujie.shengmo.customview.MyGridview;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.customview.WheelView;
import com.aiwujie.shengmo.dao.DaoSession;
import com.aiwujie.shengmo.dao.SwitchMarkBeanDao;
import com.aiwujie.shengmo.eventbus.DynamicUpMediaEditDataEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.view.HeightAndWeightPop;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.CropUtils;
import com.aiwujie.shengmo.utils.FilterGroupUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.PhotoRemoteUtil;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yalantis.ucrop.UCrop;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.feezu.liuli.timeselector.TimeSelector;
import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
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
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;


//编辑个人信息
public class EditPersonMsgActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener, OnItemClickListener, EasyPermissions.PermissionCallbacks, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.mEdit_personmsg_return)
    ImageView mEditPersonmsgReturn;
    @BindView(R.id.mEdit_personmsg_Tijiao)
    TextView mEditPersonmsgTijiao;
    @BindView(R.id.editPerson_none)
    ImageView editPersonNone;
    @BindView(R.id.mEdit_personmsg_isOpen)
    TextView mEditPersonmsgIsOpen;
    @BindView(R.id.mEdit_personmsg_qianming)
    TextView mEditPersonmsgQianming;
    @BindView(R.id.mEdit_personmsg_birthday)
    TextView mEditPersonmsgBirthday;
    @BindView(R.id.mEdit_personmsg_heightAndweight)
    TextView mEditPersonmsgHeightAndweight;
    @BindView(R.id.mEdit_personmsg_nan)
    RadioButton mEditPersonmsgNan;
    @BindView(R.id.mEdit_personmsg_nv)
    RadioButton mEditPersonmsgNv;
    @BindView(R.id.mEdit_personmsg_yi)
    RadioButton mEditPersonmsgYi;
    @BindView(R.id.mEdit_personmsg_rgSex)
    RadioGroup mEditPersonmsgRgSex;
    @BindView(R.id.mEdit_personmsg_S)
    RadioButton mEditPersonmsgS;
    @BindView(R.id.mEdit_personmsg_M)
    RadioButton mEditPersonmsgM;
    @BindView(R.id.mEdit_personmsg_SM)
    RadioButton mEditPersonmsgSM;
    @BindView(R.id.mEdit_personmsg_lang)
    RadioButton mEditPersonmsgLang;
    @BindView(R.id.mEdit_personmsg_no)
    RadioButton mEditPersonmsgNo;
    @BindView(R.id.mEdit_personmsg_rgRole)
    RadioGroup mEditPersonmsgRgRole;
    @BindView(R.id.mEdit_personmsg_gvLong)
    MyGridview mEditPersonmsgGvLong;
    @BindView(R.id.mEdit_personmsg_shi)
    RadioButton mEditPersonmsgShi;
    @BindView(R.id.mEdit_personmsg_wu)
    RadioButton mEditPersonmsgWu;
    @BindView(R.id.mEdit_personmsg_rgShijian)
    RadioGroup mEditPersonmsgRgShijian;

    @BindView(R.id.mRegist_one_page_gay)
    RadioGroup mRegistOnePageRgGay;
    @BindView(R.id.mRegist_one_page_les)
    RadioGroup mRegistOnePageRgLes;
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

    @BindView(R.id.mEdit_personmsg_gvCulture)
    MyGridview mEditPersonmsgGvCulture;
    @BindView(R.id.mEdit_personmsg_gvMoney)
    MyGridview mEditPersonmsgGvMoney;
    @BindView(R.id.mEdit_personmsg_iv01)
    ImageView mEditPersonmsgIv01;
    @BindView(R.id.mEdit_personmsg_iv02)
    ImageView mEditPersonmsgIv02;
    @BindView(R.id.mEdit_personmsg_iv03)
    ImageView mEditPersonmsgIv03;
    @BindView(R.id.mEdit_personmsg_iv04)
    ImageView mEditPersonmsgIv04;
    @BindView(R.id.mEdit_personmsg_iv05)
    ImageView mEditPersonmsgIv05;
    @BindView(R.id.mEdit_personmsg_iv06)
    ImageView mEditPersonmsgIv06;
    @BindView(R.id.mEdit_personmsg_iv07)
    ImageView mEditPersonmsgIv07;
    @BindView(R.id.mEdit_personmsg_iv08)
    ImageView mEditPersonmsgIv08;
    @BindView(R.id.mEdit_personmsg_iv09)
    ImageView mEditPersonmsgIv09;
    @BindView(R.id.mEdit_personmsg_iv10)
    ImageView mEditPersonmsgIv10;
    @BindView(R.id.mEdit_personmsg_iv11)
    ImageView mEditPersonmsgIv11;
    @BindView(R.id.mEdit_personmsg_iv12)
    ImageView mEditPersonmsgIv12;
    @BindView(R.id.mEdit_personmsg_iv13)
    ImageView mEditPersonmsgIv13;
    @BindView(R.id.mEdit_personmsg_iv14)
    ImageView mEditPersonmsgIv14;
    @BindView(R.id.mEdit_personmsg_iv15)
    ImageView mEditPersonmsgIv15;
    @BindView(R.id.mEdit_personmsg_VipLl)
    PercentLinearLayout mEditPersonmsgVipLl;
    @BindView(R.id.mEdit_personmsg_layout)
    PercentLinearLayout mEditPersonmsgLayout;
    @BindView(R.id.mEdit_personmsg_llXc)
    PercentLinearLayout mEditPersonmsgLlXc;
    @BindView(R.id.mEdit_personmsg_nickname)
    TextView mEditPersonmsgNickname;
    @BindView(R.id.mEdit_personmsg_icon)
    ImageView mEditPersonmsgIcon;
    @BindView(R.id.mEdit_personmsg_qxNan)
    CheckBox mEditPersonmsgQxNan;
    @BindView(R.id.mEdit_personmsg_qxNv)
    CheckBox mEditPersonmsgQxNv;
    @BindView(R.id.mEdit_personmsg_qxShuang)
    CheckBox mEditPersonmsgQxShuang;
    @BindView(R.id.mEdit_personmsg_qingdu)
    CheckBox mEditPersonmsgQingdu;
    @BindView(R.id.mEdit_personmsg_zhongdu)
    CheckBox mEditPersonmsgZhongdu;
    @BindView(R.id.mEdit_personmsg_zhongdu3)
    CheckBox mEditPersonmsgZhongdu3;
    @BindView(R.id.mEdit_personmsg_Liaotian)
    CheckBox mEditPersonmsgLiaotian;
    @BindView(R.id.mEdit_personmsg_Xianshi)
    CheckBox mEditPersonmsgXianshi;
    @BindView(R.id.mEdit_personmsg_Jiehun)
    CheckBox mEditPersonmsgJiehun;
    //获取  控件   两个按钮  控制相册是否可见
    @BindView(R.id.mEdit_auth_btn01)
    Button mEdit_auth_btn01;
    @BindView(R.id.mEdit_auth_btn02)
    Button mEdit_auth_btn02;

    @BindView(R.id.gay_layout)
    PercentLinearLayout gayLayout;
    @BindView(R.id.les_layout)
    PercentLinearLayout lesLayout;

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

    @BindView(R.id.ninelayout_edit_photo)
    BGASortableNinePhotoLayout ninePhotoLayout;

    //////////////////相册标记
    private String photo_ruleflag;
    private List<String> times;
    private List<String> educations;
    private List<String> salary;
    private CusGridviewAdapter adapterLong;
    private CusGridviewAdapter adapterEdu;
    private CusGridviewAdapter adapterSal;
    private String headurl = "";
    private String name = "";
    private String introduce = "";
    private String birthday = "";
    private String heigthAndWeight = "";
    private String height = "";
    private String weight = "";
    private String sex = "";
    private String role = "";
    private String sexual = "";
    private String attribute = "";
    private String along = "";
    private String culture = "";
    private String monthly = "";
    private String experience = "";
    private String want = "";
    private String level = "";
    private ArrayList<String> heights;
    private ArrayList<String> weights;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private int picFlag;
    private SimpleDateFormat sdf;
    private String currentTime = "";
    private PopupWindow mPopWindow;
    private String picString = "";
    private String headurldele = "";
    private List<ImageView> imges;
    private ArrayList<String> picList = new ArrayList<>();
    private String photo_lock = "";
    private Uri cropUri;
    private File cropImage;
    //性取向集合
    private List<String> sexuals = new ArrayList<>();
    private List<String> upSexuals = new ArrayList<>();
    //想找集合
    private List<String> wants = new ArrayList<>();
    private List<String> upWants = new ArrayList<>();
    //程度集合
    private List<String> levels = new ArrayList<>();
    private List<String> upLevels = new ArrayList<>();
    //群组id集合
    private List<String> groupIds = new ArrayList<>();
    //删除图片集合
    private List<String> deleteUrl = new ArrayList<>();
    //onResume状态码
    private boolean isOnresume = true;
    //查看大图集合
    ArrayList<String> showPics;
//    //判断是否修改了头像 0没修改 1修改
//    private String head_charge_time="0";

    //判断是否修改了相册 0没修改 1修改
    private String photo_charge_time = "0";
    //修改前的昵称
    private String oldnickname = "";
    //修改前的签名
    private String oldintroduce = "";
    //修改前的生日
    private String oldbirthday = "";
    //是否可以修改昵称
    private String changeState = "";
    //传过来的uid
    private String uid;
    //当前用户的vip状态
    private String vip;
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            // Log.i("xmf3", headurl);
//            switch (msg.what) {
//                case 152:
//                    String s = (String) msg.obj;
//                    //Log.i("icon", s);
//                    BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
//                    if (beanicon.getRetcode() == 2000) {
//                        //修改头像成功删除原头像源文件
////                        deletePic();
//                        headurl = beanicon.getData();
//                        ImageLoader.loadCircleImage(EditPersonMsgActivity.this, imgpre + headurl, mEditPersonmsgIcon);
////                        if(picFlag == 10) {
////                            Log.i("xmf1",headurl);
////                            picList.add(imgpre + headurl);
////                            ArrayList<String> temp = new ArrayList<>();
////                            temp.add(imgpre + headurl);
////                            ninePhotoLayout.addMoreData(temp);
////                        }
//
////                        deleteIcon(headurldele);
////                        headurldele = HttpUrl.NetPic()+ headurl;
////                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(MyApp.uid, mEditPersonmsgNickname.getText().toString(), Uri.parse(HttpUrl.NetPic()+ headurl)));
////                        ToastUtil.show(getApplicationContext(), "头像上传成功");
////                        head_charge_time="1";
//                    } else {
//                        // Log.i("picurl", "handleMessage: " + beanicon.getData());
//                        // Log.i("xmf2", headurl);
//                        String imgurl = beanicon.getData();
//                        if (picFlag == 10) {
//                            //Log.i("xmf1", imgurl);
//                            picList.add(imgpre + imgurl);
//                            ArrayList<String> temp = new ArrayList<>();
//                            temp.add(imgpre + imgurl);
//                            ninePhotoLayout.addMoreData(temp);
//                        }
//
////                        if ((picFlag - 1) < picList.size()) {
//////                            picList.set(picFlag-1,beanicon.getData());
////                            String removePic = picList.remove(picFlag - 1);
////                            deleteUrl.add(removePic);
//////                            Log.i("picurl", "handleMessage1: "+picList.remove(picFlag - 1));
////                            picList.add(picFlag - 1, beanicon.getData());
//////                            deletePic(removePic);
////                        } else {
////                            picList.add(beanicon.getData());
////                        }
////                        photo_charge_time = "1";
//                    }
//                    ToastUtil.show(getApplicationContext(), "上传完成");
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
    private String photo_rule;
    private String imgpre;

    MyHandler handler = new MyHandler(this);
    private static class MyHandler extends Handler {
        private WeakReference<EditPersonMsgActivity> activityWeakReference;

        public MyHandler(EditPersonMsgActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            EditPersonMsgActivity activity = activityWeakReference.get();
            if (msg.what == PhotoUploadTask.CODE_UPLOAD_SUC) {
                String s = (String) msg.obj;
                BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                if (activity.picFlag == 0) { //头像
                    activity.headurl = beanicon.getData();
                    ImageLoader.loadCircleImage(activity,  activity.imgpre +  activity.headurl,  activity.mEditPersonmsgIcon);
                } else if (activity.picFlag == 10) { //相册
                    String imgurl = beanicon.getData();
                    activity.picList.add(activity.imgpre + imgurl);
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(activity.imgpre + imgurl);
                    activity.ninePhotoLayout.addMoreData(temp);
                }
                ToastUtil.show(activity, "上传完成");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person_msg);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setData();
        setAdapter();
        setListener();
//        getSecretSit();
//        getEditMsg();
    }


    private void setAdapter() {
        adapterLong = new CusGridviewAdapter(this, times);
        mEditPersonmsgGvLong.setAdapter(adapterLong);
        adapterEdu = new CusGridviewAdapter(this, educations);
        mEditPersonmsgGvCulture.setAdapter(adapterEdu);
        adapterSal = new CusGridviewAdapter(this, salary);
        mEditPersonmsgGvMoney.setAdapter(adapterSal);

    }

    private void setListener() {
        mEditPersonmsgRgSex.setOnCheckedChangeListener(this);
        mEditPersonmsgRgRole.setOnCheckedChangeListener(this);
//        mEditPersonmsgRgQuxiang.setOnCheckedChangeListener(this);
        mEditPersonmsgGvLong.setOnItemClickListener(this);
        mEditPersonmsgGvCulture.setOnItemClickListener(this);
        mEditPersonmsgGvMoney.setOnItemClickListener(this);
//        mEditPersonmsgRgChengdu.setOnCheckedChangeListener(this);
        mEditPersonmsgRgShijian.setOnCheckedChangeListener(this);
//        mEditPersonmsgRgWant.setOnCheckedChangeListener(this);
        mEditPersonmsgQxNan.setOnCheckedChangeListener(this);
        mEditPersonmsgQxNv.setOnCheckedChangeListener(this);
        mEditPersonmsgQxShuang.setOnCheckedChangeListener(this);
        mEditPersonmsgQingdu.setOnCheckedChangeListener(this);
        mEditPersonmsgZhongdu.setOnCheckedChangeListener(this);
        mEditPersonmsgZhongdu3.setOnCheckedChangeListener(this);
        mEditPersonmsgLiaotian.setOnCheckedChangeListener(this);
        mEditPersonmsgXianshi.setOnCheckedChangeListener(this);
        mEditPersonmsgJiehun.setOnCheckedChangeListener(this);
        mRegistOnePageRgGay.setOnCheckedChangeListener(this);
        mRegistOnePageRgLes.setOnCheckedChangeListener(this);

        ninePhotoLayout.setDelegate(new BGASortableNinePhotoLayout.Delegate() {
            @Override
            public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
                picFlag = 10;
                showSelectPic();
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
            }

            @Override
            public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
                // addOrDeletePic();
                picList.remove(position);
                ninePhotoLayout.removeItem(position);
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
            }

            @Override
            public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
                Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(EditPersonMsgActivity.this)
                        .previewPhotos(models) // 当前预览的图片路径集合
                        .selectedPhotos(models) // 当前已选中的图片路径集合
                        .maxChooseCount(ninePhotoLayout.getMaxItemCount()) // 图片选择张数的最大值
                        .currentPosition(position) // 当前预览图片的索引
                        .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                        .build();
                startActivityForResult(photoPickerPreviewIntent, 123);
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
            }

            @Override
            public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {
                picList.clear();
                picList.addAll(models);
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
            }
        });
    }

    private void setData() {
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        uid = getIntent().getStringExtra("otheruid");
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
        times = new ArrayList<>();
        times.add("1年及以下");
        times.add("2-3年");
        times.add("4-6年");
        times.add("7-10年");
        times.add("10-20年");
        times.add("20年及以上");
        educations = new ArrayList<>();
        educations.add("高中及以下");
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
        imges = new ArrayList<>();
        imges.add(mEditPersonmsgIv01);
        imges.add(mEditPersonmsgIv02);
        imges.add(mEditPersonmsgIv03);
        imges.add(mEditPersonmsgIv04);
        imges.add(mEditPersonmsgIv05);
        imges.add(mEditPersonmsgIv06);
        imges.add(mEditPersonmsgIv07);
        imges.add(mEditPersonmsgIv08);
        imges.add(mEditPersonmsgIv09);
        imges.add(mEditPersonmsgIv10);
        imges.add(mEditPersonmsgIv11);
        imges.add(mEditPersonmsgIv12);
        imges.add(mEditPersonmsgIv13);
        imges.add(mEditPersonmsgIv14);
        imges.add(mEditPersonmsgIv15);
        heights = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            heights.add(120 + i + "cm");
        }
        weights = new ArrayList<>();
        for (int i = 0; i < 171; i++) {
            weights.add(30 + i + "kg");
        }
        mEditPersonmsgGvCulture.setFocusable(false);
        mEditPersonmsgGvLong.setFocusable(false);
        mEditPersonmsgGvMoney.setFocusable(false);
        String admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
        if (admin.equals("1")) {
            mEditPersonmsgNan.setClickable(true);
            mEditPersonmsgNv.setClickable(true);
            mEditPersonmsgYi.setClickable(true);
        } else {
            mEditPersonmsgNan.setClickable(false);
            mEditPersonmsgNv.setClickable(false);
            mEditPersonmsgYi.setClickable(false);
        }
        imgpre = SharedPreferencesUtils.geParam(EditPersonMsgActivity.this, "image_host", "");
    }

    @OnClick({R.id.mEdit_personmsg_return, R.id.mEdit_personmsg_Tijiao, R.id.mEdit_personmsg_icon, R.id.mEdit_personmsg_birthday, R.id.mEdit_personmsg_heightAndweight, R.id.mEdit_personmsg_iv01,
            R.id.mEdit_personmsg_iv02, R.id.mEdit_personmsg_iv03, R.id.mEdit_personmsg_iv04, R.id.mEdit_personmsg_iv05, R.id.mEdit_personmsg_iv06, R.id.mEdit_personmsg_iv07,
            R.id.mEdit_personmsg_iv08, R.id.mEdit_personmsg_iv09, R.id.mEdit_personmsg_iv10, R.id.mEdit_personmsg_iv11, R.id.mEdit_personmsg_iv12, R.id.mEdit_personmsg_iv13,
            R.id.mEdit_personmsg_iv14, R.id.mEdit_personmsg_iv15, R.id.mEdit_personmsg_nickname, R.id.mEdit_personmsg_qianming, R.id.mEdit_personmsg_llXc, R.id.mEdit_auth_btn01, R.id.mEdit_auth_btn02,
            R.id.mEdit_personmsg_S, R.id.mEdit_personmsg_M, R.id.mEdit_personmsg_SM, R.id.mEdit_personmsg_lang, R.id.mEdit_personmsg_no, R.id.mEdit_personmsg_nan, R.id.mEdit_personmsg_nv, R.id.mEdit_personmsg_yi,
            R.id.mEdit_personmsg_shi, R.id.mEdit_personmsg_wu, R.id.mEdit_personmsg_qingdu, R.id.mEdit_personmsg_zhongdu, R.id.mEdit_personmsg_zhongdu3, R.id.mEdit_personmsg_Liaotian,
            R.id.mEdit_personmsg_Xianshi, R.id.mEdit_personmsg_Jiehun, R.id.mEdit_personmsg_qxNan,
            R.id.mEdit_personmsg_qxNv, R.id.mEdit_personmsg_qxShuang, R.id.mRgist_one_page_gong, R.id.mRgist_one_page_shou, R.id.mRgist_one_page_ban, R.id.mRgist_one_page_gay_m, R.id.mRgist_one_page_t, R.id.mRgist_one_page_p, R.id.mRgist_one_page_h, R.id.mRgist_one_page_les_m})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mEdit_personmsg_return:
//                finish();
                isSave();
                break;
            case R.id.mEdit_personmsg_Tijiao:
                isSave();
                break;
            case R.id.mEdit_personmsg_icon:
                picFlag = 0;
                showSelectPic();
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_nickname:
                //如果uid和MyApp.uid相等，说明是自己进入修改资料页，反之则是官方人员进入修改资料页。
                if (uid.equals(MyApp.uid)) {
                    if (!changeState.equals("1")) {
                        intent = new Intent(this, com.aiwujie.shengmo.kt.ui.activity.EditNameActivity.class);
                        intent.putExtra(IntentKey.NAME, name);
                        startActivityForResult(intent, 100);
                        mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                    } else {
                        ToastUtil.show("您的本月修改昵称次数已达上限（修改昵称次数/月：普通用户1次,会员3次）");
                    }
                } else {
                    intent = new Intent(this, com.aiwujie.shengmo.kt.ui.activity.EditNameActivity.class);
                    intent.putExtra(IntentKey.NAME, mEditPersonmsgNickname.getText().toString());
                    startActivityForResult(intent, 100);
                    mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                }
                break;
            case R.id.mEdit_personmsg_qianming:
                Intent intent1 = new Intent(this, EditIntroduceActivity.class);
                intent1.putExtra("introduce", introduce);
                startActivityForResult(intent1, 101);
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_birthday:
                // 参数说明：ResultHandler为选取时间后的回调 startDate，endDate为时间控件的可选起始时间和结束时间。
                TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        String times = time.substring(0, time.length() - 6);
//                        birthday = times;
                        mEditPersonmsgBirthday.setText(times);
//                        ToastUtil.show(getApplicationContext(),times);
                    }
                }, "1990-01-01 00:00", currentTime);
                timeSelector.setMode(TimeSelector.MODE.YMD);
                timeSelector.show();
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_heightAndweight:
                //showpopwindow();
                showHeightAndWeightPop();
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_iv01:   //点击第一张图片的时候
                picFlag = 1;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv02:
                picFlag = 2;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv03:
                picFlag = 3;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv04:
                picFlag = 4;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv05:
                picFlag = 5;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv06:
                picFlag = 6;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv07:
                picFlag = 7;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv08:
                picFlag = 8;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv09:
                picFlag = 9;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv10:
                picFlag = 10;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv11:
                picFlag = 11;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv12:
                picFlag = 12;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv13:
                picFlag = 13;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv14:
                picFlag = 14;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_iv15:
                picFlag = 15;
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);
                addOrDeletePic();
                break;
            case R.id.mEdit_personmsg_llXc:
                if (uid.equals(MyApp.uid)) {
                    if (SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "").equals("1")) {
                        intent = new Intent(this, PhotoAlbumActivity.class);
                        intent.putExtra("photo_lock", photo_lock);
                        startActivity(intent);
                        mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                    } else {
                        //ToastUtil.show(getApplicationContext(), "请您先开通会员...");
                        VipDialog vipDialog = new VipDialog(EditPersonMsgActivity.this, "需要开通会员才可以使用相册密码~");
                    }
                }
                break;
            case R.id.mEdit_auth_btn01:  //点击 时候  所有人可见
                mEdit_auth_btn01.setSelected(true);
                mEdit_auth_btn02.setSelected(false);
                //设置标记  上传接口用
                photo_ruleflag = "0";
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_auth_btn02:
                mEdit_auth_btn02.setSelected(true);
                mEdit_auth_btn01.setSelected(false);
                //设置标记  上传接口用
                photo_ruleflag = "1";  //1就是限制
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //角色
            case R.id.mEdit_personmsg_S://斯
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_M:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_SM:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_lang:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_no:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            //性别
            case R.id.mEdit_personmsg_nan:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_nv:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_yi:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            //实践
            case R.id.mEdit_personmsg_shi:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_wu:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            //程度
            case R.id.mEdit_personmsg_qingdu:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_zhongdu:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_zhongdu3:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            //我想找
            case R.id.mEdit_personmsg_Liaotian:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_Xianshi:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_Jiehun:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            //性取向
            case R.id.mEdit_personmsg_qxNan:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_qxNv:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_qxShuang:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mRgist_one_page_gong:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mRgist_one_page_shou:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mRgist_one_page_ban:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mRgist_one_page_gay_m:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mRgist_one_page_t:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mRgist_one_page_p:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mRgist_one_page_h:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mRgist_one_page_les_m:
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
        }
    }

    private void addOrDeletePic() {
        if (picList.size() >= picFlag) {
            showDeletePic();
        } else {
            showSelectPic();
        }
    }

    private void isSave() {

        if (mEditPersonmsgTijiao.getVisibility() == View.VISIBLE) {  //判断体提交按钮是否显示
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("确认修改吗?")
                    .setPositiveButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //确认修改
                            String sexsual = "";
                            String levelstr = "";
                            String wantstr = "";
                            if (upSexuals.size() != 0) {
                                Collections.sort(upSexuals);
                                for (int i = 0; i < upSexuals.size(); i++) {
                                    sexsual += upSexuals.get(i) + ",";
                                }
                                sexual = sexsual.substring(0, sexsual.length() - 1);
                            }
                            if (upLevels.size() != 0) {
                                Collections.sort(upLevels);
                                for (int i = 0; i < upLevels.size(); i++) {
                                    levelstr += upLevels.get(i) + ",";
                                }
                                level = levelstr.substring(0, levelstr.length() - 1);
                            }
                            if (upWants.size() != 0) {
                                Collections.sort(upWants);
                                for (int i = 0; i < upWants.size(); i++) {
                                    wantstr += upWants.get(i) + ",";
                                }
                                want = wantstr.substring(0, wantstr.length() - 1);
                            }
                            if (sexual != null && want != null) {
                                confirmEdit();
                            } else {
                                ToastUtil.show(getApplicationContext(), "请您将个人信息填写完整");
                            }
                        }
                    });
                }
            }).create().show();

        } else {
            finish();
        }
    }

    private void getSecretSit() {//获取当前隐私状态
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid == null ? MyApp.uid : uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetSecretSit, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("secretactivity", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SecretStateData data = new Gson().fromJson(response, SecretStateData.class);
                            photo_lock = data.getData().getPhoto_lock();
                            //相册状态
                            photo_rule = data.getData().getPhoto_rule();
                            if (photo_lock.equals("1")) {
                                mEditPersonmsgIsOpen.setText("未加密");
                            } else {
                                mEditPersonmsgIsOpen.setText("加密");
                            }
                            ////////////////////////////////
                            if (photo_rule.equals("1")) {//限制
                                photo_ruleflag = "1";
                                mEdit_auth_btn01.setSelected(false);
                                mEdit_auth_btn02.setSelected(true);
                            } else {//所有人可见
                                mEdit_auth_btn01.setSelected(true);
                                mEdit_auth_btn02.setSelected(false);
                                photo_ruleflag = "0";
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

    private void getEditMsg() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid == null ? MyApp.uid : uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetMineinfodetailNew, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("editpersonmsg", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            int retcode = obj.getInt("retcode");
                            String msg = obj.getString("msg");
                            if (retcode == 4003) {
                                showProhibitDialog(msg);
                            }
                            OwnerMsgData data = new Gson().fromJson(response, OwnerMsgData.class);
                            switch (data.getRetcode()) {
                                case 2000:
                                    OwnerMsgData.DataBean datas = data.getData();
                                    vip = datas.getVip();
                                    if (vip.equals("1") || "1".equals(datas.getSvip()) || "1".equals(datas.getIs_admin())) {
                                        mEditPersonmsgVipLl.setVisibility(View.VISIBLE);
                                        ninePhotoLayout.setMaxItemCount(15); //vip可以上传15张
                                    } else {
                                        ninePhotoLayout.setMaxItemCount(6); //普通用户上传6张
                                    }
                                    changeState = datas.getChangeState();
                                    headurldele = datas.getHead_pic();
                                    picList = new ArrayList<>();
                                    List<String> photos = datas.getPhoto();
                                    for (int i = 0; i < photos.size(); i++) {
                                        picList.add(imgpre + photos.get(i));
                                    }
                                    ninePhotoLayout.addMoreData(picList);
                                    try {
                                        GlideImgManager.glideLoader(EditPersonMsgActivity.this, headurldele, R.mipmap.morentouxiang, R.mipmap.morentouxiang, mEditPersonmsgIcon, 0);
//                                        for (int i = 0; i < picList.size(); i++) {
//                                            if (i < imges.size()) {
//                                                GlideImgManager.glideLoader(EditPersonMsgActivity.this, HttpUrl.NetPic() + datas.getPhoto().get(i), R.mipmap.default_error, R.mipmap.default_error, imges.get(i));
////                                            Glide.with(EditPersonMsgActivity.this)
////                                                    .load(HttpUrl.NetPic() + datas.getPhoto().get(i))
////                                                    .apply(new RequestOptions().placeholder(R.mipmap.default_error).error(R.mipmap.default_error).transform(new MultiTransformation(new CenterCrop(),new RoundedCorners(20))))
////                                                    .into(imges.get(i));
//                                            }
//                                        }
                                    } catch (IllegalArgumentException e) {
                                        e.printStackTrace();
                                    }
                                    name = datas.getNickname();
                                    oldnickname = name;
                                    mEditPersonmsgNickname.setText(name);
                                    mEditPersonmsgNickname.setClickable(true);
                                    introduce = datas.getIntroduce();
                                    oldintroduce = introduce;
                                    mEditPersonmsgQianming.setText(datas.getIntroduce());
                                    birthday = datas.getBirthday();
                                    oldbirthday = birthday;
                                    mEditPersonmsgBirthday.setText(datas.getBirthday());
                                    height = datas.getTall() + "cm";
                                    weight = datas.getWeight() + "kg";
                                    heigthAndWeight = height + "/" + weight;
                                    mEditPersonmsgHeightAndweight.setText(heigthAndWeight);
                                    if (datas.getSex().equals("1")) {
                                        sex = "1";
                                        mEditPersonmsgNan.setChecked(true);
                                    } else if (datas.getSex().equals("2")) {
                                        sex = "2";
                                        mEditPersonmsgNv.setChecked(true);
                                    } else if (datas.getSex().equals("3")) {
                                        sex = "3";
                                        mEditPersonmsgYi.setChecked(true);
                                    }

                                    if (datas.getRole().equals("S")) {
                                        role = "S";
                                        mEditPersonmsgS.setChecked(true);
                                    } else if (datas.getRole().equals("M")) {
                                        role = "M";
                                        mEditPersonmsgM.setChecked(true);
                                    } else if (datas.getRole().equals("SM")) {
                                        role = "SM";
                                        mEditPersonmsgSM.setChecked(true);
                                    } else if (datas.getRole().equals("~")) {
                                        role = "~";
                                        mEditPersonmsgLang.setChecked(true);
                                    } else if (datas.getRole().equals("-")) {
                                        role = "-";
                                        mEditPersonmsgNo.setChecked(true);
                                    }
                                    if (datas.getSexual().contains(",")) {
                                        for (String t : datas.getSexual().split(",")) {
                                            sexuals.add(t);
                                        }
                                    } else {
                                        sexuals.add(datas.getSexual());
                                    }
                                    for (int i = 0; i < sexuals.size(); i++) {
                                        if (sexuals.get(i).equals("1")) {
                                            mEditPersonmsgQxNan.setChecked(true);
                                        } else if (sexuals.get(i).equals("2")) {
                                            mEditPersonmsgQxNv.setChecked(true);
                                        } else if (sexuals.get(i).equals("3")) {
                                            mEditPersonmsgQxShuang.setChecked(true);
                                        }
                                    }
                                    along = datas.getAlong();
                                    adapterLong.setSelectIndex(Integer.parseInt(along) - 1);
                                    adapterLong.notifyDataSetChanged();
                                    culture = datas.getCulture();
                                    adapterEdu.setSelectIndex(Integer.parseInt(culture) - 1);
                                    adapterEdu.notifyDataSetChanged();
                                    monthly = datas.getMonthly();
                                    adapterSal.setSelectIndex(Integer.parseInt(monthly) - 1);
                                    adapterSal.notifyDataSetChanged();
                                    if (datas.getExperience().equals("1")) {
                                        experience = "1";
                                        mEditPersonmsgShi.setChecked(true);
                                    } else if (datas.getExperience().equals("2")) {
                                        experience = "2";
                                        mEditPersonmsgWu.setChecked(true);
                                    }
                                    if (datas.getLevel().contains(",")) {
                                        for (String t : datas.getLevel().split(",")) {
                                            levels.add(t);
                                        }
                                    } else {
                                        levels.add(datas.getLevel());
                                    }
                                    for (int i = 0; i < levels.size(); i++) {
                                        if (levels.get(i).equals("1")) {
                                            mEditPersonmsgQingdu.setChecked(true);
                                        } else if (levels.get(i).equals("2")) {
                                            mEditPersonmsgZhongdu.setChecked(true);
                                        } else if (levels.get(i).equals("3")) {
                                            mEditPersonmsgZhongdu3.setChecked(true);
                                        }
                                    }
                                    if (datas.getWant().contains(",")) {
                                        for (String t : datas.getWant().split(",")) {
                                            wants.add(t);
                                        }
                                    } else {
                                        wants.add(datas.getWant());
                                    }
                                    for (int i = 0; i < wants.size(); i++) {
                                        if (wants.get(i).equals("1")) {
                                            mEditPersonmsgLiaotian.setChecked(true);
                                        } else if (wants.get(i).equals("2")) {
                                            mEditPersonmsgXianshi.setChecked(true);
                                        } else if (wants.get(i).equals("3")) {
                                            mEditPersonmsgJiehun.setChecked(true);
                                        }
                                    }
                                    attribute = datas.getAttribute();
                                    if (sex == "1" && sexuals.contains("1")) {
                                        gayLayout.setVisibility(View.VISIBLE);
                                        lesLayout.setVisibility(View.GONE);
                                        switch (attribute) {
                                            case "1":
                                                mRgistOnePageGay1.setChecked(true);
                                                break;
                                            case "0":
                                                mRgistOnePageGay0.setChecked(true);
                                                break;
                                            case "0.5":
                                                mRgistOnePageGayH.setChecked(true);
                                                break;
                                            case "~":
                                                mRgistOnePageGayM.setChecked(true);
                                                break;
                                        }
                                    } else if (sex == "2" && sexuals.contains("2")) {
                                        gayLayout.setVisibility(View.GONE);
                                        lesLayout.setVisibility(View.VISIBLE);
                                        switch (attribute) {
                                            case "T":
                                                mRgistOnePageLes1.setChecked(true);
                                                break;
                                            case "P":
                                                mRgistOnePageLes0.setChecked(true);
                                                break;
                                            case "H":
                                                mRgistOnePageLesH.setChecked(true);
                                                break;
                                            case "~":
                                                mRgistOnePageLesM.setChecked(true);
                                                break;
                                        }
                                    }
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

    private void showProhibitDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(str).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deletePic(String removePic) {
        Map<String, String> map = new HashMap<>();
        map.put("filename", removePic);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.DeletePic, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
//                Log.i("DeleteSuccess", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < imges.size(); i++) {
                            imges.get(i).setImageResource(R.mipmap.tianjiatupian);
                        }
                        for (int i = 0; i < picList.size(); i++) {
                            if (i < imges.size()) {
                                GlideImgManager.glideLoader(EditPersonMsgActivity.this, HttpUrl.NetPic() + picList.get(i), R.mipmap.default_error, R.mipmap.default_error, imges.get(i));
                            }
                        }
                        mEditPersonmsgTijiao.setEnabled(true);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void deleteIcon(String removeIcon) {
        Map<String, String> map = new HashMap<>();
        map.put("filename", removeIcon);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.DeletePic, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
//                Log.i("DeleteSuccess", "onSuccess: " + response);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void confirmEdit() {//提交修改
        if (SafeCheckUtil.isActivityFinish(EditPersonMsgActivity.this)) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        if (height.contains("cm")) {
            height = height.substring(0, height.length() - 2);
        }
        if (weight.contains("kg")) {
            weight = weight.substring(0, weight.length() - 2);
        }
        if (!mEditPersonmsgQianming.getText().toString().equals(oldintroduce)) {
            map.put("introduce", mEditPersonmsgQianming.getText().toString());
        } else {
            map.put("introduce", "");
        }
        if (!photo_charge_time.equals("0")) {
            map.put("photo_charge_time", "1");
        }
        if (!oldnickname.equals(mEditPersonmsgNickname.getText().toString())) {
            map.put("nickname", mEditPersonmsgNickname.getText().toString());
        } else {
            map.put("nickname", "");
        }
        if (!oldbirthday.equals(mEditPersonmsgBirthday.getText().toString())) {
            map.put("birthday", mEditPersonmsgBirthday.getText().toString());
        } else {
            map.put("birthday", "");
        }
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        //判断头像的url为空
        map.put("head_pic", headurl);
        map.put("tall", height);
        map.put("weight", weight);
        map.put("role", role);
        map.put("sex", sex);
        map.put("sexual", sexual);
        map.put("want", want);
        if (!TextUtil.isEmpty(level)) {
            map.put("level", level);
        }
        map.put("along", along);
        if (!TextUtil.isEmpty(experience)) {
            map.put("experience", experience);
        }
        map.put("culture", culture);
        map.put("monthly", monthly);
        map.put("attribute", attribute);
        if (gayLayout.getVisibility() == View.VISIBLE || lesLayout.getVisibility() == View.VISIBLE) {
            if (TextUtil.isEmpty(attribute)) {
                ToastUtil.show(EditPersonMsgActivity.this,"请选择属性");
                return;
            }
        }
        map.put("photo_rule", photo_ruleflag);//设置相册是否可见
        picString = "";
        String tt = TextUtil.isEmpty(imgpre) ? "okokok" : imgpre;
        if (picList.size() != 0) {
            for (int i = 0; i < picList.size(); i++) {
                picString += picList.get(i).contains(tt) ? picList.get(i).split(tt)[1] + "," : picList.get(i) + ",";
            }
            if (!TextUtil.isEmpty(picString)) {
                picString = picString.substring(0, picString.length() - 1);
            }
        }
        map.put("photo", picString);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.EditInfo, map, new IRequestCallback() {//提交修改资料
            @Override
            public void onSuccess(final String response) {
                Log.i("EditPersonMsg", "onSuccess: " + response);
                if (SafeCheckUtil.isActivityFinish(EditPersonMsgActivity.this)) {
                    return;
                }
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    if (!headurl.equals("")) {
                                        deleteIcon(headurldele);
                                        //RongIM.getInstance().refreshUserInfoCache(new UserInfo(MyApp.uid, mEditPersonmsgNickname.getText().toString(), Uri.parse(HttpUrl.NetPic() + headurl)));
                                        //RongIM.getInstance().setCurrentUserInfo(new UserInfo(MyApp.uid, mEditPersonmsgNickname.getText().toString(), Uri.parse(HttpUrl.NetPic()  + headurl)));
                                    }
                                    if (deleteUrl.size() != 0) {
                                        for (int i = 0; i < deleteUrl.size(); i++) {
                                            deleteIcon(deleteUrl.get(i));
                                        }
                                    }
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mysex", sex);
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mysexual", sexual);
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSex", sexual);
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSexual", sex);
                                    /*群组筛选*/
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSex", sex);
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSexual", sexual);

                                    //更新性取向到本地数据库
                                    int index = (int) SharedPreferencesUtils.getParam(EditPersonMsgActivity.this,"current_user_index",-1);
                                    DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
                                    SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
                                    List<SwitchMarkBean>switchMarkBeans = switchMarkBeanDao.loadAll();
                                    if (index == -1) {
                                        for (int i = 0; i < switchMarkBeans.size(); i++) {
                                            if (switchMarkBeans.get(i).getUser_id().equals(MyApp.uid)) {
                                                SharedPreferencesUtils.setParam(EditPersonMsgActivity.this,"current_user_index",i);
                                                SwitchMarkBean switchMarkBean = switchMarkBeans.get(i);
                                                switchMarkBean.setSex(sex);
                                                switchMarkBean.setSexual(sexual);
                                                switchMarkBeanDao.insertOrReplace(switchMarkBean);
                                            }
                                        }
                                    } else {
                                        SwitchMarkBean switchMarkBean = switchMarkBeans.get(index);
                                        switchMarkBean.setSex(sex);
                                        switchMarkBean.setSexual(sexual);
                                        switchMarkBeanDao.insertOrReplace(switchMarkBean);
                                    }

                                    String whatSexual = FilterGroupUtils.isWhatSexual(sex, sexual);
                                    SharedPreferencesUtils.setParam(getApplicationContext(), "groupFlag", whatSexual);
                                }
                            }.start();
                            EventBus.getDefault().post(new DynamicUpMediaEditDataEvent(2));
                            EventBus.getDefault().post("editsuccess");
                            setResult(200);
                            finish();
                            break;
                        case 4003:
                            finish();
                            break;
                        case 4010:
                        case 4011:
                        case 6000:
                        default:
                            ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (SafeCheckUtil.isActivityFinish(EditPersonMsgActivity.this)) {
                    return;
                }
                ToastUtil.show(getApplicationContext(), throwable.getMessage());

            }
        });
    }


    private void showSelectPic() {
        requestPhotoPermission();
//        new AlertView(null, null, "取消", null,
//                new String[]{"拍照", "从相册中选择"},
//                this, AlertView.Style.ActionSheet, this).show();
    }

    private void showDeletePic() {
        new AlertView(null, null, "取消", null,
                new String[]{"拍照", "从相册中选择", "查看大图", "删除"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.mEdit_personmsg_gvLong:
                adapterLong.setSelectIndex(position);
                adapterLong.notifyDataSetChanged();
                along = position + 1 + "";
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_gvCulture:
                adapterEdu.setSelectIndex(position);
                adapterEdu.notifyDataSetChanged();
                culture = position + 1 + "";
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
            case R.id.mEdit_personmsg_gvMoney:
                adapterSal.setSelectIndex(position);
                adapterSal.notifyDataSetChanged();
                monthly = position + 1 + "";
                mEditPersonmsgTijiao.setVisibility(View.VISIBLE);//提交按钮可见
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.mEdit_personmsg_rgSex:
                switch (checkedId) {
                    case R.id.mEdit_personmsg_nan:
                        sex = "1";
                        lesLayout.setVisibility(View.GONE);
                        lesLayout.setSelected(false);
                        if ("1".equals(sex) && upSexuals.contains("1")) {
                            gayLayout.setVisibility(View.VISIBLE);
                        } else {
                            gayLayout.setVisibility(View.GONE);
                            gayLayout.setSelected(false);
                            if (gayLayout.getVisibility() == View.GONE && lesLayout.getVisibility() == View.GONE)
                                attribute = "";
                        }
                        break;
                    case R.id.mEdit_personmsg_nv:
                        sex = "2";
                        gayLayout.setVisibility(View.GONE);
                        gayLayout.setSelected(false);
                        if ("2".equals(sex) && upSexuals.contains("2")) {
                            lesLayout.setVisibility(View.VISIBLE);
                        } else {
                            lesLayout.setVisibility(View.GONE);
                            lesLayout.setSelected(false);
                            if (gayLayout.getVisibility() == View.GONE && lesLayout.getVisibility() == View.GONE)
                                attribute = "";
                        }
                        break;
                    case R.id.mEdit_personmsg_yi:
                        sex = "3";
                        break;
                }
            case R.id.mEdit_personmsg_rgRole:
                switch (checkedId) {
                    case R.id.mEdit_personmsg_S:
                        role = "S";
                        howLongLayout.setVisibility(View.VISIBLE);
                        haveTryLayout.setVisibility(View.VISIBLE);
                        levelLayout.setVisibility(View.VISIBLE);
                        mEditPersonmsgGvLong.setVisibility(View.VISIBLE);
                        haveTryBtnLayout.setVisibility(View.VISIBLE);
                        levelBtnLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.mEdit_personmsg_M:
                        role = "M";
                        howLongLayout.setVisibility(View.VISIBLE);
                        haveTryLayout.setVisibility(View.VISIBLE);
                        levelLayout.setVisibility(View.VISIBLE);
                        mEditPersonmsgGvLong.setVisibility(View.VISIBLE);
                        haveTryBtnLayout.setVisibility(View.VISIBLE);
                        levelBtnLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.mEdit_personmsg_SM:
                        role = "SM";
                        howLongLayout.setVisibility(View.VISIBLE);
                        haveTryLayout.setVisibility(View.VISIBLE);
                        levelLayout.setVisibility(View.VISIBLE);
                        mEditPersonmsgGvLong.setVisibility(View.VISIBLE);
                        haveTryBtnLayout.setVisibility(View.VISIBLE);
                        levelBtnLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.mEdit_personmsg_lang:
                        role = "~";
                        howLongLayout.setVisibility(View.VISIBLE);
                        haveTryLayout.setVisibility(View.VISIBLE);
                        levelLayout.setVisibility(View.VISIBLE);
                        mEditPersonmsgGvLong.setVisibility(View.VISIBLE);
                        haveTryBtnLayout.setVisibility(View.VISIBLE);
                        levelBtnLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.mEdit_personmsg_no:
                        role = "-";
                        howLongLayout.setVisibility(View.GONE);
                        haveTryLayout.setVisibility(View.GONE);
                        levelLayout.setVisibility(View.GONE);
                        mEditPersonmsgGvLong.setVisibility(View.GONE);
                        haveTryBtnLayout.setVisibility(View.GONE);
                        levelBtnLayout.setVisibility(View.GONE);
                        break;
                }

                break;

            case R.id.mEdit_personmsg_rgShijian://有无实践
                switch (checkedId) {
                    case R.id.mEdit_personmsg_shi:
                        experience = "1";
                        break;
                    case R.id.mEdit_personmsg_wu:
                        experience = "2";
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
                    case R.id.mRgist_one_page_gay_m:
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
        }
    }

    //拍照
    public void takePhone(View view) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", MY_PERMISSIONS_REQUEST_CALL_PHONE, perms);
        } else {
           // takePhoto();
            choosePhotoBySelector();
        }

    }

    //相册
    public void choosePhone(View view) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", MY_PERMISSIONS_REQUEST_CALL_PHONE2, perms);
        } else {
            choosePhoto();
        }
    }

    /**
     * 拍照
     */
    void takePhoto() {
        try {
            cropImage = new File(Environment.getExternalStorageDirectory(), "/" + UUID.randomUUID() + ".jpg");
            cropUri = Uri.fromFile(cropImage);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (Exception e) {

        }
    }

    /**
     * 从相册选取图片
     */
    void choosePhoto() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
            startActivityForResult(intent, ALBUM_REQUEST_CODE);
        } catch (Exception e) {

        }
    }

    @Override
    public void onItemClick(Object o, int position, String data) {
        switch (position) {
            case 0:
                takePhone(mEditPersonmsgIcon);
                break;
            case 1:
                choosePhone(mEditPersonmsgIcon);
                break;
            case 2:
                showPics = new ArrayList<>();
                for (int i = 0; i < picList.size(); i++) {
                    showPics.add(HttpUrl.NetPic() + picList.get(i));
                }
                Intent intent = new Intent(this, ZoomActivity.class);
                intent.putStringArrayListExtra("pics", showPics);
                intent.putExtra("position", picFlag - 1);
                startActivity(intent);
                break;
            case 3:
                //删除图片
                // String removePic = picList.remove(picFlag - 1);
                // deletePic(removePic);
                break;
        }
    }

    private void showpopwindow() {
        height = "175cm";
        weight = "60kg";
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        final int disheight = wm.getDefaultDisplay().getHeight();
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_height_and_weight_pop, null);
        WheelView wv = (WheelView) contentView.findViewById(R.id.wheelview_height);
        WheelView wv2 = (WheelView) contentView.findViewById(R.id.wheelview_weight);
        TextView cancel = (TextView) contentView.findViewById(R.id.tv_cancle);
        TextView confirm = (TextView) contentView.findViewById(R.id.tv_select);
        wv.setOffset(2);
        wv.setItems(heights);
        wv.setSeletion(heights.size() / 2 + 5);
        wv2.setOffset(2);
        wv2.setItems(weights);
        wv2.setSeletion(weights.size() / 2 - 55);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                height = item;
            }
        });
        wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                weight = item;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heigthAndWeight = height + "/" + weight;
                mEditPersonmsgHeightAndweight.setText(heigthAndWeight);
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
        mPopWindow.showAtLocation(mEditPersonmsgLayout, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        this.mPopWindow.setAnimationStyle(R.style.AnimationPreview);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            //性取向
            case R.id.mEdit_personmsg_qxNan:
                if (isChecked) {
                    upSexuals.add("1");
                    if (sex == "1") {
                        gayLayout.setVisibility(View.VISIBLE);
                        lesLayout.setVisibility(View.GONE);
                        switch (attribute) {
                            case "1":
                                mRgistOnePageGay1.setChecked(true);
                                break;
                            case "0":
                                mRgistOnePageGay0.setChecked(true);
                                break;
                            case "0.5":
                                mRgistOnePageGayH.setChecked(true);
                                break;
                            case "~":
                                mRgistOnePageGayM.setChecked(true);
                                break;
                        }
                    }
                } else {
                    upSexuals.remove("1");
                    gayLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.mEdit_personmsg_qxNv:
                if (isChecked) {
                    upSexuals.add("2");
                    if (sex == "2") {
                        gayLayout.setVisibility(View.GONE);
                        lesLayout.setVisibility(View.VISIBLE);
                        switch (attribute) {
                            case "T":
                                mRgistOnePageLes1.setChecked(true);
                                break;
                            case "P":
                                mRgistOnePageLes0.setChecked(true);
                                break;
                            case "H":
                                mRgistOnePageLesH.setChecked(true);
                                break;
                            case "~":
                                mRgistOnePageLesM.setChecked(true);
                                break;
                        }
                    }
                } else {
                    upSexuals.remove("2");
                    lesLayout.setVisibility(View.GONE);
                }

                break;
            case R.id.mEdit_personmsg_qxShuang:
                if (isChecked) {
                    upSexuals.add("3");
                } else {
                    upSexuals.remove("3");
                }
                break;
            //程度
            case R.id.mEdit_personmsg_qingdu:
                if (isChecked) {
                    upLevels.add("1");
                } else {
                    upLevels.remove("1");
                }
                break;
            case R.id.mEdit_personmsg_zhongdu:
                if (isChecked) {
                    upLevels.add("2");
                } else {
                    upLevels.remove("2");
                }
                break;
            case R.id.mEdit_personmsg_zhongdu3:
                if (isChecked) {
                    upLevels.add("3");
                } else {
                    upLevels.remove("3");
                }
                break;
            case R.id.mEdit_personmsg_Liaotian:
                if (isChecked) {
                    upWants.add("1");
                } else {
                    upWants.remove("1");
                }
                break;
            case R.id.mEdit_personmsg_Xianshi:
                if (isChecked) {
                    upWants.add("2");
                } else {
                    upWants.remove("2");
                }

                break;
            case R.id.mEdit_personmsg_Jiehun:
                if (isChecked) {
                    upWants.add("3");
                } else {
                    upWants.remove("3");
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

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isSave();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String message) {
        if (message.equals("setSecretSitSucc")) {
            getSecretSit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                Uri crUri = data.getData();
                CropUtils.startUCrop(this, crUri, CROP_REQUEST_CODE, 1, 1, 700, 700);
//                startCrop(crUri);
                break;
            case CAMERA_REQUEST_CODE:
                CropUtils.startUCrop(this, cropUri, CROP_REQUEST_CODE, 1, 1, 700, 700);
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
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
                        switch (picFlag) {
                            case 0:
                                //头像
                                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
                                circleDrawable.getPaint().setAntiAlias(true);
                                circleDrawable.setCircular(true);
                                mEditPersonmsgIcon.setImageDrawable(circleDrawable); //把图片显示在ImageView控件上
                                break;
                            case 10:
                                //相册

                                break;

                        }
                        ToastUtil.show(getApplicationContext(), "图片上传中,请稍后提交...");
                    }
                    if (picFlag == 0) {
                        // 获得字节流
                        ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
                        PhotoUploadTask put = new PhotoUploadTask(
                                NetPic() + "Api/Api/fileUpload"  //"http://59.110.28.150:888/Api/Api/fileUpload"
                                , is,
                                this, handler);
                        put.start();
                    } else {
                        // 获得字节流
                        ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
                        PhotoUploadTask put = new PhotoUploadTask(
                                NetPic() + "Api/Api/filePhoto"//  "http://59.110.28.150:888/Api/Api/filePhoto"
                                , is,
                                this, handler);
                        put.start();
                    }
                } else {
                    return;
                }
                break;
            case 100:
                try {
//                    name = data.getStringExtra("name");
                    mEditPersonmsgNickname.setText(data.getStringExtra("name"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 101:
                try {
//                    introduce = data.getStringExtra("introduce");
                    mEditPersonmsgQianming.setText(data.getStringExtra("introduce"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 10000:
                if (data != null) {
                    ToastUtil.show(EditPersonMsgActivity.this,"开始上传");
                    //获取选择器返回的数据
                    ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                    for (int i = 0; i < images.size(); i++) {
                        LogUtil.d(images.get(i));
                        uploadImage(images.get(i));
                        //sendMessageByPath(images.get(i));
                    }
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
            choosePhotoBySelector();
            //takePhoto();
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
            ToastUtil.show(getApplicationContext(), "授权失败,请开启相机权限");
            return;
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(), "授权失败,请开启读写权限");
            return;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isOnresume) {
            isOnresume = false;
            getSecretSit();
            getEditMsg();
//            getGroupId();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    void requestPhotoPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", MY_PERMISSIONS_REQUEST_CALL_PHONE, perms);
        } else {
            choosePhotoBySelector();
        }
    }

    void choosePhotoBySelector() {
        ImageSelector.builder()
                .useCamera(true)
                .setSingle(false)
                .setMaxSelectCount(picFlag == 0?1:ninePhotoLayout.getMaxItemCount() - ninePhotoLayout.getItemCount())
                .canPreview(true)
                .setCrop(true)
                .start(EditPersonMsgActivity.this,10000);
    }

    void uploadImage(String path) {
        Bitmap loadbitmap = BitmapFactory.decodeFile(path, getBitmapOption(2));
        Bitmap rotaBitmap = PhotoRemoteUtil.rotaingImageView(PhotoRemoteUtil.getBitmapDegree(path), loadbitmap);
        ByteArrayInputStream is = new ByteArrayInputStream(Bitmap2Bytes(rotaBitmap));
        PhotoUploadTask put = new PhotoUploadTask(
                NetPic() + "Api/Api/filePhoto"//  "http://59.110.28.150:888/Api/Api/filePhoto"
                , is,
                this, handler);
        put.start();
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public void showHeightAndWeightPop() {
        HeightAndWeightPop heightAndWeightPop = new HeightAndWeightPop(EditPersonMsgActivity.this);
        heightAndWeightPop.showPopupWindow();
        heightAndWeightPop.setOnHeightAndWeightListener(new HeightAndWeightPop.OnHeightAndWeightListener() {
            @Override
            public void doChooseComplete(@NotNull String h, @NotNull String w) {
                height = h;
                weight = w;
                heigthAndWeight = height + "/" + weight;
                mEditPersonmsgHeightAndweight.setText(heigthAndWeight);

            }
        });

    }

}
