package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.EditDynamicPicAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.Atbean;
import com.aiwujie.shengmo.bean.DynamicDetailData;
import com.aiwujie.shengmo.bean.DynamicPicData;
import com.aiwujie.shengmo.bean.TopicHeaderData;
import com.aiwujie.shengmo.bean.TopicSevenData;
import com.aiwujie.shengmo.customview.BindPhoneAlertDialog;
import com.aiwujie.shengmo.customview.WordWrapView;
import com.aiwujie.shengmo.eventbus.DynamicDetailEvent;
import com.aiwujie.shengmo.eventbus.EditDynamicIndexEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.statistical.AtMemberActivity;
import com.aiwujie.shengmo.kt.util.NormalUtilKt;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.NineGridUtils;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.aiwujie.shengmo.zdyview.ATGroupSpan;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.aliyun.common.global.AliyunTag;
import com.aliyun.qupai.editor.AliyunIVodCompose;
import com.aliyun.qupai.editor.impl.AliyunVodCompose;
import com.aliyun.qupaiokhttp.HttpRequest;
import com.aliyun.qupaiokhttp.RequestParams;
import com.aliyun.qupaiokhttp.StringHttpRequestCallback;
import com.aliyun.svideo.common.utils.NetWatchdogUtils;
import com.aliyun.svideo.common.utils.ThreadUtils;
import com.aliyun.svideo.crop.CropMediaActivity;
import com.aliyun.svideo.editor.publish.ComposeFactory;
import com.aliyun.svideo.editor.publish.ImageUploadCallbackBean;
import com.aliyun.svideo.editor.publish.RefreshVodVideoUploadAuth;
import com.aliyun.svideo.editor.publish.VodImageUploadAuth;
import com.aliyun.svideo.editor.publish.VodVideoUploadAuth;
import com.aliyun.svideo.recorder.activity.AlivcSvideoRecordActivity;
import com.aliyun.svideo.recorder.bean.AlivcRecordInputParam;
import com.aliyun.svideo.recorder.bean.RenderingMode;
import com.aliyun.svideo.recorder.util.gles.GeneratedTexture;
import com.aliyun.svideo.sdk.external.struct.common.VideoQuality;
import com.aliyun.svideo.sdk.external.struct.encoder.VideoCodecs;
import com.aliyun.svideo.sdk.external.struct.snap.AliyunSnapVideoParam;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.sina.weibo.sdk.openapi.models.Group;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import id.zelory.compressor.Compressor;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

//编辑动态
// 2020 - 3 -17 区分 顾问 黑钻  和  管理员的权限
public class EditDynamicActivity extends AppCompatActivity implements BGASortableNinePhotoLayout.Delegate, EasyPermissions.PermissionCallbacks {
    public static final String TAG = "EditDynamicActivity";

    @BindView(R.id.mEditDynamic_return)
    ImageView mEditDynamicReturn;
    @BindView(R.id.mEditDynamic_confirm)
    TextView mEditDynamicConfirm;
    @BindView(R.id.mEditDynamic_icon)
    ImageView mEditDynamicIcon;
    @BindView(R.id.mEditDynamic_hongniang)
    ImageView mEditDynamicHongniang;
    @BindView(R.id.mEditDynamic_vip)
    ImageView mEditDynamicVip;
    @BindView(R.id.mEditDynamic_name)
    TextView mEditDynamicName;
    @BindView(R.id.mEditDynamic_shiming)
    ImageView mEditDynamicShiming;
    @BindView(R.id.mEditDynamic_online)
    ImageView mEditDynamicOnline;
    @BindView(R.id.mEditDynamic_liulancount)
    TextView mEditDynamicLiulancount;
    @BindView(R.id.mEditDynamic_Sex)
    TextView mEditDynamicSex;
    @BindView(R.id.mEditDynamic_role)
    TextView mEditDynamicRole;
    @BindView(R.id.mEditDynamic_richCount)
    TextView mEditDynamicRichCount;
    @BindView(R.id.mEditDynamic_ll_richCount)
    PercentLinearLayout mEditDynamicLlRichCount;
    @BindView(R.id.mEditDynamic_beautyCount)
    TextView mEditDynamicBeautyCount;
    @BindView(R.id.mEditDynamic_ll_beautyCount)
    PercentLinearLayout mEditDynamicLlBeautyCount;
    @BindView(R.id.mEditDynamic_distance)
    TextView mEditDynamicDistance;
    @BindView(R.id.mEditDynamic_time)
    TextView mEditDynamicTime;
    @BindView(R.id.mEditDynamic_header_layout)
    PercentLinearLayout mEditDynamicHeaderLayout;
    @BindView(R.id.mEditDynamic_intro)
    EditText mEditDynamicIntro;
    @BindView(R.id.mDynamic_nineGrid)
    NineGridView mDynamicNineGrid;
    @BindView(R.id.mEditDynamic_ScrollView)
    ScrollView mEditDynamicScrollView;
    @BindView(R.id.activity_edit_dynamic)
    PercentLinearLayout activityEditDynamic;
    @BindView(R.id.mEditDynamic_introCount)
    TextView mEditDynamicIntroCount;
    @BindView(R.id.mEditDynamic_add_photos)
    BGASortableNinePhotoLayout mEditDynamicAddPhotos;
    private String did;
    private String fvip;
    private String fsvipannual;
    private String fsvip;
    private String fadmin;
    private String fvolunteer;
    private String fvipannual;
    private String fuid;
    private String content;
    private String distance;
    private String sex;
    private String role;
    private String age;
    private String time;
    private String liulancount;
    private String shiming;
    private int isonline;
    private String name;
    private String headpic;
    private int retcode;
    private ArrayList<String> sypics;
    private ArrayList<String> slpics;
    private ArrayList<String> deletesypics = new ArrayList<>();
    private ArrayList<String> deleteslpics = new ArrayList<>();
    List<String> images = new ArrayList<>();
    List<String> upUrls = new ArrayList<>();
    List<String> upSyUrls = new ArrayList<>();
    private EditDynamicPicAdapter editDynamicPicAdapter;
    private String charmval;
    private String wealthwal;
    private int dynamicPos;
    private int dynamicRetcode;
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 4;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    private static final int REQUEST_CODE_CROP_VIDEO=3;

    public static final String KEY_UPLOAD_VIDEO = "video_path";
    public static final String KEY_UPLOAD_THUMBNAIL = "video_thumbnail";
    public static final String KEY_UPLOAD_DESC = "video_desc";
    public static final String KEY_PARAM_VIDEO_RATIO = "key_param_video_ratio";
    private AlertDialog dialog;
    //违规禁止发送动态的吐司提示
    private String toastStr;
    //判断是否可以发送动态
    private boolean isSend = false;
    private int currentNum = 0;
    private int totalNum = 0;
    private String newPics = "";
    private String newSyPics = "";
    ////////////////////////////////////////
    private List<TextView> huatis = new ArrayList<>();

    @BindView(R.id.mSendDynamic_topic_sort_00)
    TextView mSendDynamicTopicSort00;
    @BindView(R.id.mSendDynamic_topic_sort_01)
    TextView mSendDynamicTopicSort01;
    @BindView(R.id.mSendDynamic_topic_sort_02)
    TextView mSendDynamicTopicSort02;
    @BindView(R.id.mSendDynamic_topic_sort_03)
    TextView mSendDynamicTopicSort03;
    @BindView(R.id.mSendDynamic_topic_sort_04)
    TextView mSendDynamicTopicSort04;
    @BindView(R.id.mSendDynamic_topic_sort_05)
    TextView mSendDynamicTopicSort05;
    @BindView(R.id.mSendDynamic_topic_sort_06)
    TextView mSendDynamicTopicSort06;
    @BindView(R.id.mSendDynamic_topic_sort_07)
    TextView mSendDynamicTopicSort07;

    @BindView(R.id.video_player_layout)
    PercentRelativeLayout videoPlayerLayout;
    @BindView(R.id.video_cover_img)
    ImageView videoCoverImg;
    @BindView(R.id.delete_img)
    ImageView deleteImg;
    private static final int REQUEST_CODE_PERMISSION_VIDEO_PICKER = 0;
    private int topicState = -1;
    private List<TopicSevenData> topicSevenDatas = new ArrayList<>();
    private String colors[] = {"#ff0000", "#b73acb", "#0000ff", "#18a153", "#f39700", "#ff00ff", "#00a0e9"};
    @BindView(R.id.mSendDynamic_wordwrapview)
    WordWrapView mSendDynamicWordwrapview;
    @BindView(R.id.mSendDynamic_tvTopic)
    TextView mSendDynamicTvTopic;
    @BindView(R.id.mSendDynamic_ll_tvTopic)
    PercentLinearLayout mSendDynamicLlTvTopic;
    @BindView(R.id.mSendDynamic_newTopic)
    TextView mSendDynamicNewTopic;
    private int topstate = 0;
    private String topicId = "-1";  //默认话题id
    private String mImageUrl;
    private String mVideoUrl;
    private int mWidth=0,mHeight=0;
    private String mVideoPath;

    /**
     * 视频文件大小
     */
    private long videoSize;
    /**
     * 图片文件大小
     */
    private long imageSize;



//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    //删除图片
//                    deletePics();
//                    break;
//                case 152:
//                    String data = (String) msg.obj;
//                    Log.i("edittexticon", data);
//                    currentNum += 1;
//                    try {
//                        JSONObject obj = new JSONObject(data);
//                        if (obj.getInt("retcode") == 2000) {
//                            DynamicPicData beanIcon = new Gson().fromJson(data, DynamicPicData.class);
//                            String picurl = beanIcon.getData().getSlimg();
//                            String picsy = beanIcon.getData().getSyimg();
//                            upSyUrls.add(picsy);
//                            upUrls.add(picurl);
////                            Log.i("senddynamic", "handleMessage: " + picurl + "," + picsy);
//                        } else {
//                            dialog.dismiss();
//                            ToastUtil.show(getApplication(), "网络异常或者图片过大,请稍后重试...");
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (NullPointerException e) {
//                        e.printStackTrace();
//                    }
//                    if (totalNum == currentNum) {
//                        newPics = "";
//                        newSyPics = "";
//                        for (int i = 0; i < upUrls.size(); i++) {
//                            newPics += upUrls.get(i) + ",";
//                            newSyPics += upSyUrls.get(i) + ",";
//                        }
//                        newPics = newPics.substring(0, newPics.length() - 1);
//                        newSyPics = newSyPics.substring(0, newSyPics.length() - 1);
//                        editDynamic();
//                    }
//                    break;
//            }
//        }
//    };
    MyHandler handler = new MyHandler(this);
    static class MyHandler extends Handler {
        WeakReference<EditDynamicActivity> activityWeakReference;
        MyHandler(EditDynamicActivity editDynamicActivity) {
            activityWeakReference = new WeakReference<>(editDynamicActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            EditDynamicActivity editDynamicActivity = activityWeakReference.get();
            switch (msg.what) {
                case 0:
                    //删除图片
                    editDynamicActivity.deletePics();
                    break;
                case 152:
                    String data = (String) msg.obj;
                    Log.i("edittexticon", data);
                    editDynamicActivity.currentNum += 1;
                    try {
                        JSONObject obj = new JSONObject(data);
                        if (obj.getInt("retcode") == 2000) {
                            DynamicPicData beanIcon = new Gson().fromJson(data, DynamicPicData.class);
                            String picurl = beanIcon.getData().getSlimg();
                            String picsy = beanIcon.getData().getSyimg();
                            editDynamicActivity.upSyUrls.add(picsy);
                            editDynamicActivity.upUrls.add(picurl);
//                            Log.i("senddynamic", "handleMessage: " + picurl + "," + picsy);
                            editDynamicActivity.imageMap.put(msg.arg1,beanIcon.getData().getSyimg());
                        } else {
                            editDynamicActivity.dialog.dismiss();
                            ToastUtil.show( editDynamicActivity, "网络异常或者图片过大,请稍后重试...");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    if ( editDynamicActivity.totalNum ==  editDynamicActivity.currentNum) {
                        editDynamicActivity.newPics = "";
                        editDynamicActivity.newSyPics = "";
                        for (int i = 0; i <  editDynamicActivity.upUrls.size(); i++) {
                            editDynamicActivity.newPics +=  editDynamicActivity.upUrls.get(i) + ",";
                            editDynamicActivity.newSyPics +=  editDynamicActivity.upSyUrls.get(i) + ",";
                        }
                        editDynamicActivity.newPics =  editDynamicActivity.newPics.substring(0,  editDynamicActivity.newPics.length() - 1);
                        editDynamicActivity.newSyPics =  editDynamicActivity.newSyPics.substring(0,  editDynamicActivity.newSyPics.length() - 1);
                        editDynamicActivity.editDynamic();
                    }
                    break;
                case 123:
                    editDynamicActivity.mEditDynamicConfirm.setClickable(true);
                    break;
            }
        }
    }
    private String tid;
    private String topictitle;
    private String atuid;
    private String atuname;
    private String bkvip;
    private String blvip;
    private String blvip1;
    String width;
    String height;
    String playUrl;
    String coverUrl;
    String videoId;
    String newVideoId;//上传成功后视频id
    private String mThumbnailPath;
    private String mDesc;
    List<String> atuidlist = new ArrayList<>();
    List<String> atunamelist = new ArrayList<>();

    private String is_display;

    ConcurrentHashMap<Integer,String> imageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dynamic);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        getIntentData();
        imageMap = new ConcurrentHashMap<>();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        did = intent.getStringExtra("did");
        is_display = intent.getStringExtra("is_display");
        dynamicPos = intent.getIntExtra("dynamicPos", -1);
        getDynamicDetail();
        if(getIntent().getStringExtra("video_id")!=null)
            videoUploadCallback(getIntent());
    }

    private void setData() {
//        SpannableString spannableString = new SpannableString(content);
//        if (!"".equals(atuname) && null != atuname && !"".equals(atuid) && null != atuid) {
//            String[] split = atuid.split(",");
//            String[] split1 = atuname.split(",");
//            for (int i = 0; i < split1.length; i++) {
//                if (split1[i].trim().contains("*")) {
//                    split1[i] = split1[i].trim().replace("*", "\\*");
//                }
//                String patten = Pattern.quote(split1[i].trim());
//                        Pattern compile = Pattern.compile(patten);
//                Matcher matcher = compile.matcher(content);
//                while (matcher.find()) {
//                    int start = matcher.start();
//                    if (split1[i].trim().contains("*")) {
//                        split1[i] = split1[i].trim().replace("\\", "");
//                    }
//
//                    if (!TextUtil.isEmpty(split[i]) && start >= 0) {
//                        if (split1[i].startsWith("@[群]")) {
//                            spannableString.setSpan(new ATGroupSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        } else {
//                            spannableString.setSpan(new ATSpan(split[i]), start, start + split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        }
//                    }
//                }
//            }
//        }
        SpannableString spannableString = NormalUtilKt.toDynamicSpannable(content,atuname,atuid);
        mEditDynamicIntro.setText(spannableString);

        mEditDynamicIntro.setSelection(mEditDynamicIntro.getText().length());
        mEditDynamicIntro.addTextChangedListener(new EditTitleChangedListener());
        mEditDynamicIntroCount.setText(mEditDynamicIntro.getText().length() + "/10000");

        if (!TextUtil.isEmpty(videoId)){
            videoPlayerLayout.setVisibility(View.VISIBLE);
            mDynamicNineGrid.setVisibility(View.GONE);
            mEditDynamicAddPhotos.setVisibility(View.GONE);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.skipMemoryCache(true);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.error(R.drawable.rc_image_error);
            requestOptions.placeholder(R.drawable.rc_image_error);
            Glide.with(this)
                    .asBitmap()//制Glide返回一个Bitmap对象
                    .load(coverUrl)
                    .apply(requestOptions)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            int i = NineGridUtils.dp2px(300);
                            if (height > i) {
                                height = i;
                            }
                            videoCoverImg.setLayoutParams(NineGridUtils.setPara(videoCoverImg, width, height));
                            videoCoverImg.setImageBitmap(bitmap);
                        }

                    });
        }else {
            videoPlayerLayout.setVisibility(View.GONE);
        }


//        if (slpics.size() != 0) {
//            mDynamicNineGrid.setVisibility(View.VISIBLE);
//            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
//            for (int i = 0; i < slpics.size(); i++) {
//                ImageInfo info = new ImageInfo();
//                info.setThumbnailUrl(slpics.get(i));
//                if (sypics.size() != 0) {
//                    info.setBigImageUrl(sypics.get(i));
//                } else {
//                    info.setBigImageUrl(slpics.get(i));
//                }
//                imageInfo.add(info);
//            }
//            editDynamicPicAdapter = new EditDynamicPicAdapter(this, imageInfo);
//            mDynamicNineGrid.setAdapter(editDynamicPicAdapter);
//        } else {
//            mDynamicNineGrid.setVisibility(View.GONE);
//        }

        mEditDynamicAddPhotos.addMoreData(slpics);


        if (!"0".equals(charmval)) {
            mEditDynamicLlBeautyCount.setVisibility(View.VISIBLE);
            mEditDynamicBeautyCount.setText(charmval);
        } else {
            mEditDynamicLlBeautyCount.setVisibility(View.GONE);
        }
        if (!"0".equals(wealthwal)) {
            mEditDynamicLlRichCount.setVisibility(View.VISIBLE);
            mEditDynamicRichCount.setText(wealthwal);
        } else {
            mEditDynamicLlRichCount.setVisibility(View.GONE);
        }

        mEditDynamicAddPhotos.setMaxItemCount(9);
        mEditDynamicAddPhotos.setEditable(true);
        mEditDynamicAddPhotos.setPlusEnable(true);
        mEditDynamicAddPhotos.setSortable(true);
        // 设置拖拽排序控件的代理
        mEditDynamicAddPhotos.setDelegate(this);


        huatis.add(mSendDynamicTopicSort00);
        huatis.add(mSendDynamicTopicSort01);
        huatis.add(mSendDynamicTopicSort02);
        huatis.add(mSendDynamicTopicSort03);
        huatis.add(mSendDynamicTopicSort04);
        huatis.add(mSendDynamicTopicSort05);
        huatis.add(mSendDynamicTopicSort06);
        huatis.add(mSendDynamicTopicSort07);
        //初始化顶部话题
        initTopic();

        // 这里区分身份
        if ("限制".equals(is_display)) {
            //文字不可编辑状态
            mEditDynamicIntro.setFocusable(false);
            mEditDynamicIntro.setFocusableInTouchMode(false);
            mEditDynamicIntro.setVisibility(View.INVISIBLE);
            mDynamicNineGrid.setVisibility(View.GONE);
            mEditDynamicAddPhotos.setVisibility(View.GONE);
        }


        mEditDynamicIntro.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!(s.length() > start)) {
                    return;
                }
                if ('@' == s.charAt(start) && count == 1) {
//                    Intent intent = new Intent(EditDynamicActivity.this, AtFansActivity.class);
                    Intent intent = new Intent(EditDynamicActivity.this, AtMemberActivity.class);
                    startActivityForResult(intent, 100);
                    return;
                }

//                if ((s.charAt(start) == '@') && (s.charAt(start + count - 1) == ' ')) {
//                    if ('@' == s.charAt(start - 1)) {
//                        mEditDynamicIntro.getText().delete(start - 1, start);
//                    }
//                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        mEditDynamicIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = mEditDynamicIntro.getSelectionStart();

                ATSpan[] atSpans = mEditDynamicIntro.getText().getSpans(0, mEditDynamicIntro.getText().length(), ATSpan.class);
                int length = atSpans.length;

                if (0 == length) {
                    return;

                }

                for (ATSpan atSpan : atSpans) {
                    int start = mEditDynamicIntro.getText().getSpanStart(atSpan);
                    int end = mEditDynamicIntro.getText().getSpanEnd(atSpan);
                    if (selectionStart >= start && selectionStart <= end) {
                        mEditDynamicIntro.setSelection(end);
                        return;
                    }
                }
            }
        });

        mEditDynamicIntro.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {

                    int selectionStart = mEditDynamicIntro.getSelectionStart();

                    ATSpan[] atSpans = mEditDynamicIntro.getText().getSpans(0, mEditDynamicIntro.getText().length(), ATSpan.class);
                    int length = atSpans.length;

                    if (0 == length) {
                        return false;
                    }

                    for (ATSpan atSpan : atSpans) {
                        int start = mEditDynamicIntro.getText().getSpanStart(atSpan);
                        int end = mEditDynamicIntro.getText().getSpanEnd(atSpan);
                        if (selectionStart >= start + 1 && selectionStart <= end) {
                            mEditDynamicIntro.getText().delete(start + 1, end);
                            for (int i = 0; i < atuidlist.size(); i++) {
                                String value = atSpan.getValue();
                                if (atuidlist.get(i).equals(value)) {
                                    atuidlist.remove(i);
                                    atunamelist.remove(i);
                                    break;
                                }
                            }
                            return false;
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        videoUploadCallback(intent);
    }

    private void videoUploadCallback(Intent intent) {
        videoPlayerLayout.setVisibility(View.VISIBLE);
        mDynamicNineGrid.setVisibility(View.GONE);
        mEditDynamicAddPhotos.setVisibility(View.GONE);

        mVideoPath = intent.getStringExtra(KEY_UPLOAD_VIDEO);
        mThumbnailPath = intent.getStringExtra(KEY_UPLOAD_THUMBNAIL);
        if (!TextUtil.isEmpty(mVideoPath)){
            videoSize = new File(mVideoPath).length();
            imageSize = new File(mThumbnailPath).length();
        }
        mDesc = intent.getStringExtra(KEY_UPLOAD_DESC);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.rc_image_error);
        requestOptions.placeholder(R.drawable.rc_image_error);
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        File file= new File(mThumbnailPath);
        Glide.with(EditDynamicActivity.this)
                .asBitmap()//制Glide返回一个Bitmap对象
                .load(file)
                .apply(requestOptions)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int i = NineGridUtils.dp2px(300);
                        if (height > i) {
                            height = i;
                        }
                        videoCoverImg.setLayoutParams(NineGridUtils.setPara(videoCoverImg, width, height));
                        videoCoverImg.setImageBitmap(bitmap);
                    }

                });
    }

    private void initTopic() {
        if (topicState == -1) {
            mSendDynamicTopicSort00.setSelected(true);
            getTopicEight(0);
        } else {
            huatis.get(topicState).setSelected(true);
            getTopicEight(topicState);
        }

        for (int i = 0; i < huatis.size(); i++) {
            final int finalI = i;
            huatis.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (huatis.get(finalI).isSelected()) {
                        for (int j = 0; j < huatis.size(); j++) {
                            huatis.get(j).setSelected(false);
                        }
                        topicSevenDatas.clear();
                        mSendDynamicWordwrapview.removeAllViews();
//                        mSendDynamicLlTvTopic.setVisibility(View.GONE);
//                        topstate = -1;
//                        topicId = "-1";
                    } else {
                        for (int j = 0; j < huatis.size(); j++) {
                            huatis.get(j).setSelected(false);
                        }
                        topicSevenDatas.clear();
                        mSendDynamicWordwrapview.removeAllViews();
                        huatis.get(finalI).setSelected(true);
                        topstate = finalI;
                        getTopicEight(topstate);
                    }
                }
            });
        }
    }

    //得到二级列表的选项
    private void getTopicEight(final int pid) {
        Map<String, String> map = new HashMap<>();
        map.put("pid", pid + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetTopicEight, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("senddynamicgettopic", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TopicHeaderData data = new Gson().fromJson(response, TopicHeaderData.class);
                            if (data.getData().size() < 35) {
                                for (int i = 0; i < data.getData().size(); i++) {
                                    TopicSevenData sevenData = new TopicSevenData();
                                    sevenData.setTid(data.getData().get(i).getTid());
                                    sevenData.setTitle(data.getData().get(i).getTitle());
                                    topicSevenDatas.add(sevenData);
                                }
                                for (int i = 0; i < topicSevenDatas.size(); i++) {
                                    final TextView textview = new TextView(EditDynamicActivity.this);
                                    textview.setTextSize(14);
                                    textview.setTextColor(Color.parseColor(colors[i % 7]));
                                    textview.setText("#" + topicSevenDatas.get(i).getTitle() + "#");
                                    mSendDynamicWordwrapview.addView(textview);
                                    final int j = i;
                                    textview.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mSendDynamicLlTvTopic.setVisibility(View.VISIBLE);
                                            mSendDynamicTvTopic.setText(topicSevenDatas.get(j).getTitle());
                                            topicId = topicSevenDatas.get(j).getTid();
                                            mSendDynamicWordwrapview.removeAllViews();
                                            for (int i = 0; i < huatis.size(); i++) {
                                                huatis.get(i).setSelected(false);
                                            }
//                                            mSendDynamicContent.setFocusable(false);
//                                            mSendDynamicScrollview.smoothScrollTo(0, 0);
                                        }
                                    });
                                }
                            } else {
                                for (int i = 0; i < data.getData().size(); i++) {
                                    TopicSevenData sevenData = new TopicSevenData();
                                    sevenData.setTid(data.getData().get(i).getTid());
                                    sevenData.setTitle(data.getData().get(i).getTitle());
                                    topicSevenDatas.add(sevenData);
                                }
                                topicSevenDatas.add(new TopicSevenData("-1", "[更多]"));
                                for (int i = 0; i < topicSevenDatas.size(); i++) {
                                    final TextView textview = new TextView(EditDynamicActivity.this);
                                    textview.setTextSize(14);
                                    if (i == 35) {
                                        textview.setText(topicSevenDatas.get(i).getTitle());
                                    } else {
                                        textview.setTextColor(Color.parseColor(colors[i % 7]));
                                        textview.setText("#" + topicSevenDatas.get(i).getTitle() + "#");
                                    }
                                    mSendDynamicWordwrapview.addView(textview);
                                    final int j = i;
                                    textview.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            ImageSpanUtils.insertEditText(topicSevenDatas.get(j).getTitle(),SendDynamicActivity.this,mSendDynamicContent);
                                            if (j == 35) {
                                                Intent intent = new Intent(EditDynamicActivity.this, TopicActivity.class);
//                                                intent.putExtra("uid", MyApp.uid);
                                                intent.putExtra("topicflag", 1);
                                                intent.putExtra("pid", pid);
                                                startActivityForResult(intent, 188);
                                            } else {
                                                mSendDynamicLlTvTopic.setVisibility(View.VISIBLE);
                                                mSendDynamicTvTopic.setText(topicSevenDatas.get(j).getTitle());
                                                topicId = topicSevenDatas.get(j).getTid();
                                                mSendDynamicWordwrapview.removeAllViews();
                                                for (int i = 0; i < huatis.size(); i++) {
                                                    huatis.get(i).setSelected(false);
                                                }
//                                                mSendDynamicContent.setFocusable(false);
//                                                mSendDynamicScrollview.smoothScrollTo(0, 0);
                                            }
                                        }
                                    });
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

    @OnClick({R.id.mEditDynamic_return, R.id.mEditDynamic_confirm, R.id.mSendDynamic_newTopic,R.id.delete_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mEditDynamic_return:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("是否退出编辑?").setPositiveButton("继续编辑", null).setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
                break;
            case R.id.mEditDynamic_confirm://点击确定的时候
                mEditDynamicConfirm.setClickable(false);
                handler.sendEmptyMessageDelayed(123,2000);
                if (dynamicRetcode != 3001) {
                    if (isSend) {
                        if (TextUtils.isEmpty(mThumbnailPath) && TextUtils.isEmpty(videoId)&& TextUtils.isEmpty(mEditDynamicIntro.getText().toString().trim())&&images.size()==0&&slpics.size() == 0){
                            ToastUtil.show(this, "动态内容不能为空");
                            return;
                        }
                        showdialog();
                        if (images.size() != 0) {
                            totalNum = images.size();
                            upPics();
                        } else {
                            // 如果用户没有修改视频或者图片直接提交，如果用户修改了视频 直接调用上传视频接口 上传完视频之后调用修改接口
                            if (!TextUtils.isEmpty(mVideoPath)){
                                initNetWatchdog();
                            }else {
                            editDynamic();
                            }

                        }
                    } else {
                        ToastUtil.show(getApplicationContext(), toastStr);
                    }
                } else {
                    BindPhoneAlertDialog.bindAlertDialog(this, toastStr);
                }
                break;
            case R.id.mSendDynamic_newTopic:
                Intent intent = new Intent(this, SendTopicActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.delete_img:
                coverUrl="";
                videoId="";
                mVideoPath="";
                mThumbnailPath ="";
                mDesc ="";
                mWidth=0;
                mHeight=0;
                videoPlayerLayout.setVisibility(View.GONE);
                mDynamicNineGrid.setVisibility(View.VISIBLE);
                mEditDynamicAddPhotos.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("是否退出编辑?").setPositiveButton("继续编辑", null).setNegativeButton("放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();


    }

    private void deletePics() {
        for (int i = 0; i < deleteslpics.size(); i++) {
            deletePic(deleteslpics.get(i));
           // deletePic(deletesypics.get(i));
        }
    }

    private void deletePic(String removePic) {
        Map<String, String> map = new HashMap<>();
        map.put("filename", removePic);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.DeletePic, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("DeleteSuccess", "onSuccess: " + response);

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ProgressBar bar = new ProgressBar(this);
        builder.setTitle("修改动态中,请稍后...")
                .setCancelable(false)
                .setView(bar);
        dialog = builder.create();
        dialog.show();
    }

    private void upPics() {
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < images.size(); i++) {
            final int j = i;
            if (images.get(i).startsWith("http")) {
                imageMap.put(i,images.get(i));
                currentNum++;
                if (totalNum == currentNum) {
                    editDynamic();
                }
                continue;
            }
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap compressedImageBitmap = null;
                    try {
                        compressedImageBitmap = new Compressor(EditDynamicActivity.this).compressToBitmap(new File(images.get(j)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 字节数组输出流
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    // 将Bitmap对象转化为字节数组输出流
                    if (compressedImageBitmap != null) {
                        compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        // 获得字节流
                        ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
//                        PhotoUploadTask put = new PhotoUploadTask("http://59.110.28.150:888/Api/Api/dynamicPicUpload", is, EditDynamicActivity.this, handler);
                        PhotoUploadTask put = new PhotoUploadTask(NetPic() + "Api/Api/dynamicPicUpload", is, EditDynamicActivity.this,j, handler);//http://59.110.28.150:888/
                        put.start();
                    } else {
                        dialog.dismiss();
                         runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.show(getApplicationContext(), "图片不存在或者图片过大...");
                                        }
                                    });
                    }
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }


    private void editDynamic() {  //编辑动态的接口
        String slpicss = "";
        String sypicss = "";
//        if (slpics.size() != 0) {
//           String imagePath= SharedPreferencesUtils.geParam(EditDynamicActivity.this,"image_host","");
//
//            for (int i = 0; i < slpics.size(); i++) {
//                slpicss += slpics.get(i).replace("http://aiwujie.com.cn/", "").replace(imagePath, "") + ",";
//                sypicss += sypics.get(i).replace("http://aiwujie.com.cn/", "").replace(imagePath, "") + ",";
//            }
//            slpicss = slpicss.substring(0, slpicss.length() - 1);
//            sypicss = sypicss.substring(0, sypicss.length() - 1);
//        }

        if (imageMap.size() != 0) {
            List<Integer> values = new ArrayList(imageMap.keySet());
            Collections.sort(values, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1 - o2;
                }
            });
            String imagePath= SharedPreferencesUtils.geParam(EditDynamicActivity.this,"image_host","");
            for (Integer index : values) {
                if (!imageMap.get(index).startsWith("http")) {
                    slpicss += imagePath + imageMap.get(index) + ",";
                    sypicss += imagePath + imageMap.get(index) + ",";
                } else {
                    slpicss += imageMap.get(index) + ",";
                    sypicss +=  imageMap.get(index) + ",";
                }
            }
            slpicss = slpicss.substring(0, slpicss.length() - 1);
            sypicss = sypicss.substring(0, sypicss.length() - 1);
        }

//        if (upUrls.size() != 0) {
//            if (slpics.size() == 0) {
//                slpicss = newPics;
//                sypicss = newSyPics;
//            } else {
//                slpicss = slpicss + "," + newPics;
//                sypicss = sypicss + "," + newSyPics;
//            }
//        }

        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);

        map.put("tid", topicId);

        map.put("content", mEditDynamicIntro.getText().toString().trim());
        map.put("pic", slpicss);
        map.put("sypic", sypicss);
        if (!TextUtils.isEmpty(newVideoId)){
            map.put("videoId",newVideoId);
            map.put("width", mWidth+"");
            map.put("height", mHeight+"");
        }else if(!TextUtils.isEmpty(videoId)){
            map.put("videoId",videoId);
        }

        if (!TextUtil.isEmpty(mImageUrl)){
            map.put("coverUrl", mImageUrl);
        }
        String substring = "";
        String substring2 = "";
        atuid = "";
        atuname = "";
        for (int i = 0; i < atuidlist.size(); i++) {
            atuid += atuidlist.get(i) + ",";
            atuname += atunamelist.get(i) + ",";
        }
        if (atuidlist.size() > 0) {
            substring = atuid.substring(0, atuid.length() - 1);
            substring2 = atuname.substring(0, atuname.length() - 1);
        }
        map.put("atuid", substring);
        map.put("atuname", substring2);

        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.EditDynamic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("editdynamicsuccess", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                case 2001:
                                    if (deleteslpics.size() != 0) {
                                        handler.sendEmptyMessage(0);
                                    }
                                    EventBus.getDefault().post(new DynamicDetailEvent());
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    finish();
                                    break;
                                default:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                            }
                            dialog.dismiss();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void editDyEventBus(EditDynamicIndexEvent event) {
        String slpic = slpics.remove(event.getIndex());
        String sypic = sypics.remove(event.getIndex());
        if (slpics.size() != 0) {
            mDynamicNineGrid.setVisibility(View.VISIBLE);
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for (int i = 0; i < slpics.size(); i++) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(slpics.get(i));
                if (sypics.size() != 0) {
                    info.setBigImageUrl(sypics.get(i));
                } else {
                    info.setBigImageUrl(slpics.get(i));
                }
                imageInfo.add(info);
            }
            editDynamicPicAdapter = new EditDynamicPicAdapter(this, imageInfo);
            mDynamicNineGrid.setAdapter(editDynamicPicAdapter);
        } else {
            mDynamicNineGrid.setVisibility(View.GONE);
        }
        mEditDynamicAddPhotos.setMaxItemCount(9 - slpics.size());
        deleteslpics.add(slpic);
        deletesypics.add(sypic);
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        if (dynamicRetcode != 3001) {
            if (isSend) {
                // 选择相册
                if (slpics.size()!=0){
                    choicePhotoWrapper();
                }else {
                setAdminOperation(new String[]{"选择相册", "拍视频", "选择视频"});
                }

            } else {
                ToastUtil.show(getApplicationContext(), toastStr);
            }
        } else {
            BindPhoneAlertDialog.bindAlertDialog(this, toastStr);
        }
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mEditDynamicAddPhotos.removeItem(position);
        images.remove(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {

        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(mEditDynamicAddPhotos.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, REQUEST_CODE_PHOTO_PREVIEW);
//        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mEditDynamicAddPhotos.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {
        images.clear();
        images.addAll(models);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            mEditDynamicAddPhotos.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));
            images.addAll(data.getStringArrayListExtra("EXTRA_SELECTED_PHOTOS"));
            Log.i("reportImages", "onActivityResult: " + data.getExtras());
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            mEditDynamicAddPhotos.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "Shengmo");
            if (!takePhotoDir.exists()) {
                takePhotoDir.mkdirs();
            }
            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(mEditDynamicAddPhotos.getMaxItemCount() - mEditDynamicAddPhotos.getItemCount()) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_PHOTO);
//            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mEditDynamicAddPhotos.getMaxItemCount() - mEditDynamicAddPhotos.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }


    class EditTitleChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEditDynamicIntroCount.setText("" + s.length() + "/10000");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() == 10000) {
                ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApp.uid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "uid", "0");
        getJudgeDynamic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void gethaoyouliebiao(Message message) {
//        if (message.arg2 == 11) {
//            Atbean atbean = (Atbean) message.obj;
//            if (mEditDynamicIntro.getText().toString().equals("@")) {
//                mEditDynamicIntro.setText(" ");
//            }
//            List<Atbean.DataBean> dataBean = atbean.getDataBean();
//            for (int i = 0; i < dataBean.size(); i++) {
//                String uid = dataBean.get(i).getUid();
//                atuidlist.add(uid);
//                atunamelist.add("@" + dataBean.get(i).getNickname() + " ");
//                //atuid+=uid+",";
//                //atuname+="@"+dataBean.get(i).getNickname()+" ,";
//                ATSpan atSpan = new ATSpan(uid);
//                SpannableString span = new SpannableString("@" + dataBean.get(i).getNickname() + " ");
//                span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                mEditDynamicIntro.append(span);
//            }
//            mEditDynamicIntro.setSelection(mEditDynamicIntro.getText().length());
//
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gethaoyouliebiao(Message message) {
        if (message.arg2 == 11) {
            Atbean atbean = (Atbean) message.obj;

            List<Atbean.DataBean> dataBean = atbean.getDataBean();
            int select_index = mEditDynamicIntro.getSelectionStart();
            if (select_index >= 0) {
                if (mEditDynamicIntro.getEditableText().charAt(select_index-1) == '@') {
                    mEditDynamicIntro.getEditableText().delete(select_index-1,select_index);
                    select_index--;
                }
            }
            for (int i = 0; i < dataBean.size(); i++) {
                String uid = dataBean.get(i).getUid();
                atuidlist.add(uid);
                atunamelist.add("@" + dataBean.get(i).getNickname() + " ");
//                ATSpan atSpan = new ATSpan(uid);
                SpannableString span;
                span = new SpannableString("@" + dataBean.get(i).getNickname() + " ");
//                span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                //  whl  群组颜色
                if (dataBean.get(i).getNickname().contains("[群]")){
                    ATSpan atSpan = new ATSpan(uid,"群");
                    span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }else if (dataBean.get(i).getNickname().contains("[高端]")){
                    ATSpan atSpan = new ATSpan(uid,"高端");
                    span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }else {
                    ATSpan atSpan = new ATSpan(uid);
                    span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                if (select_index < 0 || select_index >= mEditDynamicIntro.getText().length() ){
                    mEditDynamicIntro.append(span);
                }else{
                    mEditDynamicIntro.getText().insert(select_index,span);//光标所在位置插入文字
                    // mSendDynamicContent.append(span,select_index,select_index + span.length());

                }
                select_index = select_index + span.length();
                // mSendDynamicContent.append(span);
                // mSendDynamicContent.append(span,select_index,select_index + span.length());
            }
            //LogUtil.d("setSelction =  " + select_index );
            mEditDynamicIntro.setSelection(mEditDynamicIntro.getText().length());
//            if (mSendDynamicContent.getText().toString().equals("@")) {
//                mSendDynamicContent.setText(" ");
//            }
        }
    }

    private void getDynamicDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("did", did);
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("aliyunImage", "1");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getDynamicdetailNewFiveEdit350, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("dynamicdetaildata", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            retcode = obj.getInt("retcode");
                            if (retcode == 2000 || retcode == 2001) {
                                final DynamicDetailData data = new Gson().fromJson(response, DynamicDetailData.class);
                                DynamicDetailData.DataBean datas = data.getData();
                                headpic = datas.getHead_pic();
                                name = datas.getNickname();
                                isonline = datas.getOnlinestate();
                                shiming = datas.getRealname();
                                fvip = datas.getVip();
                                charmval = datas.getCharm_val();
                                liulancount = datas.getReadtimes();
                                age = datas.getAge();
                                wealthwal = datas.getWealth_val();
                                fsvipannual = datas.getSvipannual();
                                topictitle = datas.getTopictitle();
                                fsvip = datas.getSvip();
                                fadmin = datas.getIs_admin();
                                fvolunteer = datas.getIs_volunteer();
                                fvipannual = datas.getVipannual();
                                tid = datas.getTid();
                                sex = datas.getSex();
                                role = datas.getRole();
                                time = datas.getAddtime();
                                content = datas.getContent();
                                sypics = datas.getSypic();
                                slpics = datas.getPic();
                                images = datas.getPic();
                                fuid = datas.getUid();
                                distance = datas.getDistance() + "";
                                atuid = datas.getAtuid();
                                atuname = datas.getAtuname();

                                playUrl= datas.getPlayUrl();
                                coverUrl= datas.getCoverUrl();
                                videoId= datas.getVideoId();
                                String[] split = atuid.split(",");
                                String[] split1 = atuname.split(",");
                                for (int i = 0; i < split.length; i++) {
                                    atuidlist.add(split[i]);
                                    atunamelist.add(split1[i]);
                                }
                                blvip = datas.getBlvip();
                                bkvip = datas.getBkvip();

                                if (!topictitle.equals("") && null != topictitle) {
                                    mSendDynamicLlTvTopic.setVisibility(View.VISIBLE);
                                    mSendDynamicTvTopic.setText(topictitle);
                                    topicId = tid;
                                    mSendDynamicWordwrapview.removeAllViews();
                                    for (int i = 0; i < huatis.size(); i++) {
                                        huatis.get(i).setSelected(false);
                                    }
                                }
                                setData();

                            } else if (retcode == 4002) {
                                ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                            }


                        } catch (Exception e) {
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


    public void setAdminOperation(String[] tabs) {
        new AlertView(null, null, "取消", null,
                tabs, this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                int min = 5000;// 最小时长
                int max = 15000;// 最大时长
                int gop = 30;  // 关键帧间隔  建议 1-300  默认30
                switch (data) {
                    case "选择相册":
                        choicePhotoWrapper();
                        break;
                    case "拍视频":
                        upSyUrls.clear();
                        upUrls.clear();
                        mEditDynamicAddPhotos.setData(new ArrayList<String>());
                        images.clear();
                        AlivcRecordInputParam recordParam = new AlivcRecordInputParam.Builder()
                                .setResolutionMode(AliyunSnapVideoParam.RESOLUTION_480P) //  480 540 720
                                .setRatioMode(AliyunSnapVideoParam.RATIO_MODE_9_16) //9_16 3_4
                                .setMaxDuration(max)
                                .setMinDuration(min)
                                .setVideoQuality(VideoQuality.SSD)
                                .setGop(gop)
                                .setVideoCodec(VideoCodecs.H264_SOFT_FFMPEG)
                                .setIsUseFlip(false)
                                .setVideoRenderingMode(RenderingMode.Race)
                                .build();
                        Reference<SendDynamicActivity> reference = new WeakReference(EditDynamicActivity.this);
                        AlivcSvideoRecordActivity.startRecord(reference.get(), recordParam,true);
                        break;
                    case "选择视频":
                        choiceVideoWrapper();
                        break;
                }
            }
        }).show();
    }

    @AfterPermissionGranted( REQUEST_CODE_PERMISSION_VIDEO_PICKER)
    private void choiceVideoWrapper() {
        int min = 5000;// 最小时长
        int max = 15000;// 最大时长
        int gop = 30;  // 关键帧间隔  建议 1-300  默认30
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(EditDynamicActivity.this, perms)) {
            upSyUrls.clear();
            upUrls.clear();
            mEditDynamicAddPhotos.setData(new ArrayList<String>());
            images.clear();
            AliyunSnapVideoParam snapParam = new AliyunSnapVideoParam.Builder()
                    .setResolutionMode(AliyunSnapVideoParam.RESOLUTION_480P) //  480 540 720
                    .setRatioMode(AliyunSnapVideoParam.RATIO_MODE_9_16) //9_16 3_4
                    .setMaxDuration(60000)
                    .setMinDuration(min)
                    .setMinCropDuration(min)
                    .setMinVideoDuration(min)
                    .setVideoQuality(VideoQuality.SSD)
                    .setGop(gop)
                    .setVideoCodec(VideoCodecs.H264_SOFT_FFMPEG)
                    .build();
            CropMediaActivity.startCropForResult(EditDynamicActivity.this,REQUEST_CODE_CROP_VIDEO,snapParam,true);
        } else {
            EasyPermissions.requestPermissions(this, "视频选择需要以下权限:\n\n1.访问设备上的照片", REQUEST_CODE_PERMISSION_VIDEO_PICKER, perms);
        }

    }




    private NetWatchdogUtils mWatchdog;
    private AliyunVodCompose mComposeClient;
    private boolean mIsUpload;

    //检查网络状态
    private void initNetWatchdog() {
        mComposeClient = ComposeFactory.INSTANCE.getAliyunVodCompose();
        mComposeClient.init(this);
        mWatchdog = new NetWatchdogUtils(this);
        mWatchdog.startWatch();
        mWatchdog.setNetChangeListener(new NetWatchdogUtils.NetChangeListener() {
            @Override
            public void onWifiTo4G() {
                Log.e(TAG, "onWifiTo4G");
            }

            @Override
            public void on4GToWifi() {
                Log.e(TAG, "on4GToWifi");

            }

            @Override
            public void onReNetConnected(boolean isReconnect) {
                AliyunIVodCompose.AliyunComposeState stateIdle = mComposeClient.getState();
                Log.e(TAG, "onReNetConnected state : " + stateIdle);
                if (mComposeClient != null && (stateIdle == AliyunIVodCompose.AliyunComposeState.STATE_IDLE || stateIdle == null)) {
                    startImageUpload();
                }
            }

            @Override
            public void onNetUnConnected() {
                Log.e(TAG, "onNetUnConnected");

            }
        });
    }


    /**
     * 上传图片
     */
    private void startImageUpload() {
        Log.e(TAG, "startImageUpload");
        SharedPreferences sp = getSharedPreferences("personData", Context.MODE_PRIVATE);
        RequestParams params = new RequestParams();
        params.addFormDataPart("imageType", "default");
        HttpRequest.post(sp.getString("api_host", "http://cs.aiwujie.net/")+"Api/Dynamic/getImageUploadAddress",
                params, new StringHttpRequestCallback() {
                    @Override
                    protected void onSuccess(String s) {
                        super.onSuccess(s);
                        Log.d(TAG, s);
                        VodImageUploadAuth tokenInfo = VodImageUploadAuth.getImageTokenInfo(s);
                        if (tokenInfo != null && mComposeClient != null) {
                            mImageUrl = tokenInfo.getImageURL();
                            int rv = mComposeClient.uploadImageWithVod(mThumbnailPath, tokenInfo.getUploadAddress(), tokenInfo.getUploadAuth(), mUploadCallback);
                            if (rv < 0) {
                                Log.d(AliyunTag.TAG, "上传参数错误 video path : " + mVideoPath + " thumbnailk : " + mThumbnailPath);
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        com.aliyun.common.utils.ToastUtil.showToast(EditDynamicActivity.this, getResources().getString(com.aliyun.svideo.editor.R.string.alivc_editor_publish_upload_param_error));
                                    }
                                });
                            } else {
                                mIsUpload = true;
                                ImageUploadCallbackBean imageUploadCallbackBean = new Gson().fromJson(s,
                                        ImageUploadCallbackBean.class);
                                if (imageUploadCallbackBean != null && "2000".equals(imageUploadCallbackBean.getCode())) {
                                    mImageUrl = imageUploadCallbackBean.getData().getImageURL();
                                }
                            }

                        } else {
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    com.aliyun.common.utils.ToastUtil.showToast(EditDynamicActivity.this, "Get image upload auth info failed");
                                }
                            });
                            Log.e(AliyunTag.TAG, "Get image upload auth info failed");
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, String msg) {
                        super.onFailure(errorCode, msg);
                        Log.e(AliyunTag.TAG, "Get image upload auth info failed, errorCode:" + errorCode + ", msg:" + msg);
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                com.aliyun.common.utils.ToastUtil.showToast(EditDynamicActivity
                                        .this, "Get image upload auth info failed");
                            }
                        });
                    }
                });
    }

    /**
     * 上传视频
     */
    private void startVideoUpload() {
        SharedPreferences sp = getSharedPreferences("personData", Context.MODE_PRIVATE);
        RequestParams params = new RequestParams();
        params.addFormDataPart("title", TextUtils.isEmpty(mDesc) ? "test video" : mDesc);
        params.addFormDataPart("fileName", mVideoPath.toString());
        params.addFormDataPart("coverURL", mImageUrl == null ? "" : mImageUrl);
        HttpRequest.get(sp.getString("api_host", "http://cs.aiwujie.net/")+"Api/Dynamic/getVideoUploadAddress", params, new StringHttpRequestCallback() {
            @Override
            protected void onSuccess(String s) {
                super.onSuccess(s);
                VodVideoUploadAuth tokenInfo = VodVideoUploadAuth.getVideoTokenInfo(s);
                if (tokenInfo != null && mComposeClient != null) {
                    mWidth = getResources().getDisplayMetrics().widthPixels;
                    mHeight= getResources().getDisplayMetrics().heightPixels;
                    newVideoId = tokenInfo.getVideoId().toString();
                    mVideoUrl = tokenInfo.getUploadAddress();
                    int rv = mComposeClient.uploadVideoWithVod(mVideoPath, tokenInfo.getUploadAddress(), tokenInfo.getUploadAuth(), mUploadCallback);
                    if (rv < 0) {
                        Log.d(AliyunTag.TAG, "上传参数错误 video path : " + mVideoPath + " thumbnailk : " + mThumbnailPath);
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                com.aliyun.common.utils.ToastUtil.showToast(EditDynamicActivity.this, getResources().getString(com.aliyun.svideo.editor.R.string.alivc_editor_publish_upload_param_error));
                            }
                        });
                    } else {
                        mIsUpload = true;

                    }

                } else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            com.aliyun.common.utils.ToastUtil.showToast(EditDynamicActivity.this, "Get video upload auth failed");
                        }
                    });
                    Log.e(AliyunTag.TAG, "Get video upload auth info failed");
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Log.e(AliyunTag.TAG, "Get video upload auth failed, errorCode:" + errorCode + ", msg:" + msg);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        com.aliyun.common.utils.ToastUtil.showToast(EditDynamicActivity.this, "Get video upload auth failed");
                    }
                });
            }
        });
    }

    private final AliyunVodCompose.AliyunIVodUploadCallBack mUploadCallback = new AliyunVodCompose.AliyunIVodUploadCallBack() {

        @Override
        public void onUploadSucceed() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mComposeClient != null && mComposeClient.getState() == AliyunIVodCompose.AliyunComposeState.STATE_IMAGE_UPLOADING) {
                        //如果是图片上传回调，继续视频上传
                        startVideoUpload();
                        return;
                    }
                    editDynamic();
                }
            });

        }

        @Override
        public void onUploadFailed(String code, String message) {
            Log.e(AliyunTag.TAG, "onUploadFailed, errorCode:" + code + ", msg:" + message);
        }

        @Override
        public void onUploadProgress(final long uploadedSize, final long totalSize) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mComposeClient == null) {

                        return;
                    }
                    int progress=0;
//                    if (mComposeClient.getState() == AliyunVodCompose.AliyunComposeState.STATE_IMAGE_UPLOADING) {
//                        progress = (int)((uploadedSize * 100) / (totalSize  + videoSize));
//                    } else if (mComposeClient.getState() == AliyunVodCompose.AliyunComposeState.STATE_VIDEO_UPLOADING) {
//                        progress = (int)(((uploadedSize + imageSize) * 100) / (totalSize + imageSize));
//                    }
//                    Log.i("zq",progress+"");
//                    if (bar!=null){
//                        bar.setmProgress(progress);
//                    }


                }
            });

        }

        @Override
        public void onUploadRetry(String code, String message) {

        }

        @Override
        public void onUploadRetryResume() {

        }

        @Override
        public void onUploadTokenExpired() {
            if (mComposeClient == null) {
                return;
            }
            if (mComposeClient.getState() == AliyunIVodCompose.AliyunComposeState.STATE_IMAGE_UPLOADING) {
                startImageUpload();
            } else if (mComposeClient.getState() == AliyunIVodCompose.AliyunComposeState.STATE_VIDEO_UPLOADING) {
                refreshVideoUpload(videoId);
            }
        }
    };

    /**
     * 刷新视频凭证
     * @param videoId
     */
    private void refreshVideoUpload(String videoId) {
        SharedPreferences sp = getSharedPreferences("personData", Context.MODE_PRIVATE);
        RequestParams params = new RequestParams();
        params.addFormDataPart("videoId", videoId);
        HttpRequest.post(sp.getString("api_host", "http://cs.aiwujie.net/")+"Api/Dynamic/refreshVideoUploadAddress", params, new StringHttpRequestCallback() {
            @Override
            protected void onSuccess(String s) {
                super.onSuccess(s);
                RefreshVodVideoUploadAuth tokenInfo = RefreshVodVideoUploadAuth.getReVideoTokenInfo(s);
                if (tokenInfo != null && mComposeClient != null) {
                    int rv = mComposeClient.refreshWithUploadAuth(tokenInfo.getUploadAuth());
                    if (rv < 0) {
                        Log.d(AliyunTag.TAG, "上传参数错误 video path : " + mVideoPath + " thumbnailk : " + mThumbnailPath);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                com.aliyun.common.utils.ToastUtil.showToast(EditDynamicActivity.this, getResources().getString(com.aliyun.svideo.editor.R.string.alivc_editor_publish_upload_param_error));
                            }
                        });
                    } else {
                        mIsUpload = true;
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            com.aliyun.common.utils.ToastUtil.showToast(EditDynamicActivity.this, "Get video upload auth failed");
                        }
                    });
                    Log.e(AliyunTag.TAG, "Get video upload auth info failed");
                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }
}
