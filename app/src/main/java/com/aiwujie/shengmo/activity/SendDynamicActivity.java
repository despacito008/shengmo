package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.Atbean;
import com.aiwujie.shengmo.bean.DynamicPicData;
import com.aiwujie.shengmo.bean.TopicHeaderData;
import com.aiwujie.shengmo.bean.TopicSevenData;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.customview.BindPhoneAlertDialog;
import com.aiwujie.shengmo.customview.BindSvipDialog;
import com.aiwujie.shengmo.customview.WordWrapView;
import com.aiwujie.shengmo.eventbus.SendDynamicSuccessEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.statistical.AtMemberActivity;
import com.aiwujie.shengmo.kt.ui.view.ScrollEditText;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.FixedGlideImageLoader;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.NineGridUtils;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.TextViewUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.aiwujie.shengmo.view.CirclePgBar;
import com.aiwujie.shengmo.zdyview.ATGroupSpan;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.aliyun.common.global.AliyunTag;
import com.aliyun.qupai.editor.AliyunIVodCompose;
import com.aliyun.qupai.editor.impl.AliyunVodCompose;
import com.aliyun.qupaiokhttp.HttpRequest;
import com.aliyun.qupaiokhttp.RequestParams;
import com.aliyun.qupaiokhttp.StringHttpRequestCallback;
import com.aliyun.svideo.base.Constants;
import com.aliyun.svideo.common.utils.NetWatchdogUtils;
import com.aliyun.svideo.common.utils.ThreadUtils;
import com.aliyun.svideo.crop.CropMediaActivity;
import com.aliyun.svideo.editor.publish.ComposeFactory;
import com.aliyun.svideo.editor.publish.ImageUploadCallbackBean;
import com.aliyun.svideo.editor.publish.RefreshVodVideoUploadAuth;
import com.aliyun.svideo.editor.publish.UploadActivity;
import com.aliyun.svideo.editor.publish.VodImageUploadAuth;
import com.aliyun.svideo.editor.publish.VodVideoUploadAuth;
import com.aliyun.svideo.recorder.activity.AlivcSvideoRecordActivity;
import com.aliyun.svideo.recorder.bean.AlivcRecordInputParam;
import com.aliyun.svideo.recorder.bean.RenderingMode;
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
import com.tencent.qcloud.tim.uikit.utils.FileUtil;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.imageloader.BGAImage;
import cn.bingoogolapple.photopicker.imageloader.BGAImageLoader;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import id.zelory.compressor.Compressor;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class SendDynamicActivity extends AppCompatActivity implements BGASortableNinePhotoLayout.Delegate, BGAOnRVItemClickListener, EasyPermissions.PermissionCallbacks, View.OnTouchListener {

    public static final String TAG = "SendDynamicActivity";

    @BindView(R.id.mSendDynamic_cancel)
    TextView mSendDynamicCancel;
    @BindView(R.id.mSendDynamic_send)
    TextView mSendDynamicSend;
    @BindView(R.id.mSendDynamic_content)
    EditText mSendDynamicContent;
    @BindView(R.id.mSendDynamic_textcount)
    TextView mSendDynamicTextcount;
    @BindView(R.id.mSendDynamic_wordwrapview)
    WordWrapView mSendDynamicWordwrapview;
    @BindView(R.id.mSendDynamic_add_photos)
    BGASortableNinePhotoLayout mSendDynamicAddPhotos;
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_PERMISSION_VIDEO_PICKER = 0;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    private static final int REQUEST_CODE_CROP_VIDEO = 3;
    private static final int REQUEST_CODE_CHOOSE_VIDEO = 10005;

    @BindView(R.id.mSendDynamic_count)
    TextView mSendDynamicCount;
    @BindView(R.id.mSendDynamic_at)
    LinearLayout mSendDynamicAt;
    @BindView(R.id.mSendDynamic_guifan)
    TextView mSendDynamicGuifan;
    /* @BindView(R.id.mSendDynamic_newTopic)
     TextView mSendDynamicNewTopic;*/
    @BindView(R.id.mSend_dynamic_layout)
    LinearLayout mSendDynamicLayout;
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
    @BindView(R.id.mSendDynamic_tvTopic)
    TextView mSendDynamicTvTopic;
    @BindView(R.id.mSendDynamic_ll_tvTopic)
    LinearLayout mSendDynamicLlTvTopic;
    @BindView(R.id.mSendDynamic_scrollview)
    ScrollView mSendDynamic_recommend;
    @BindView(R.id.mSendDynamic_topic_sort_00)
    TextView mSendDynamicTopicSort00;
    //@BindView(R.id.mSendDynamic_recommend)
    //CheckBox mSendDynamic_check;
    @BindView(R.id.mSecret_gz)
    CheckBox mSendDynamic_gz;

    @BindView(R.id.video_player_layout)
    RelativeLayout videoPlayerLayout;
    @BindView(R.id.video_cover_img)
    ImageView videoCoverImg;
    @BindView(R.id.delete_img)
    ImageView deleteImg;
    private int totalNum = 0;
    private int currentNum = 0;
    private int topstate = 0;
    List<String> images = new ArrayList<>();
    List<String> upUrls = new ArrayList<>();
    List<String> upSyUrls = new ArrayList<>();
    private String colors[] = {"#ff0000", "#b73acb", "#0000ff", "#18a153", "#f39700", "#ff00ff", "#00a0e9"};
    private int bgBorder[] = {R.drawable.bg_border_topic_1, R.drawable.bg_border_topic_2, R.drawable.bg_border_topic_3, R.drawable.bg_border_topic_4, R.drawable.bg_border_topic_5, R.drawable.bg_border_topic_6, R.drawable.bg_border_topic_7};
    private List<TopicSevenData> topicSevenDatas = new ArrayList<>();
    private List<TextView> huatis = new ArrayList<>();
    private AlertDialog dialog;
    private String pics = "";
    private String sypics = "";
    //违规禁止发送动态的吐司提示
    private String toastStr;
    //判断是否可以发送动态
    private boolean isSend = true;
    //@人的json
    private String atStr = "";
    private String atuname = "";
    private String topicId = "-1";
    private int dynamicRetcode;
    private int topicState = -1;
    String videoId;

    /**
     * 视频文件大小
     */
    private long videoSize;
    /**
     * 图片文件大小
     */
    private long imageSize;

    private String mSendDynamic_gz_flag = "0";//推荐按钮
    List<String> atuidlist = new ArrayList<>();
    List<String> atunamelist = new ArrayList<>();
    public static final String KEY_UPLOAD_VIDEO = "video_path";
    public static final String KEY_UPLOAD_THUMBNAIL = "video_thumbnail";
    public static final String KEY_UPLOAD_DESC = "video_desc";
    public static final String KEY_PARAM_VIDEO_RATIO = "key_param_video_ratio";
    private MyHandler handler = new MyHandler(this);
    private String mThumbnailPath;
    private String mDesc;
    private CirclePgBar bar;

    ConcurrentHashMap<Integer, ImageUrlBean> imageMap;

    class ImageUrlBean {
        private String smallUrl;
        private String bigUrl;

        public String getSmallUrl() {
            return smallUrl;
        }

        public void setSmallUrl(String smallUrl) {
            this.smallUrl = smallUrl;
        }

        public String getBigUrl() {
            return bigUrl;
        }

        public void setBigUrl(String bigUrl) {
            this.bigUrl = bigUrl;
        }
    }

    static class MyHandler extends Handler {
        WeakReference<SendDynamicActivity> activityWeakReference;

        private MyHandler(SendDynamicActivity sendDynamicActivity) {
            activityWeakReference = new WeakReference<>(sendDynamicActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            SendDynamicActivity sendDynamicActivity = activityWeakReference.get();
            switch (msg.what) {
                case 152:
                    String data = (String) msg.obj;
                    sendDynamicActivity.currentNum += 1;
                    try {
                        JSONObject obj = new JSONObject(data);
                        if (obj.getInt("retcode") == 2000) {
                            DynamicPicData beanIcon = new Gson().fromJson(data, DynamicPicData.class);
                            String picurl = beanIcon.getData().getSlimg();
                            String picsy = beanIcon.getData().getSyimg();
                            sendDynamicActivity.upSyUrls.add(picsy);
                            sendDynamicActivity.upUrls.add(picurl);
                            ImageUrlBean imageUrlBean = sendDynamicActivity.new ImageUrlBean();
                            imageUrlBean.setSmallUrl(beanIcon.getData().getSlimg());
                            imageUrlBean.setBigUrl(beanIcon.getData().getSyimg());
                            sendDynamicActivity.imageMap.put(msg.arg1, imageUrlBean);
                        } else {
                            sendDynamicActivity.dialog.dismiss();
                            ToastUtil.show(sendDynamicActivity, "网络异常或者图片过大,请稍后重试...");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    if (sendDynamicActivity.totalNum == sendDynamicActivity.currentNum) {
                        sendDynamicActivity.pics = "";
                        sendDynamicActivity.sypics = "";
//                        for (int i = 0; i < sendDynamicActivity.upUrls.size(); i++) {
//                            sendDynamicActivity.pics += sendDynamicActivity.upUrls.get(i) + ",";
//                            sendDynamicActivity.sypics += sendDynamicActivity.upSyUrls.get(i) + ",";
//                        }
                        List<Integer> values = new ArrayList(sendDynamicActivity.imageMap.keySet());
                        Collections.sort(values, new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return o1 - o2;
                            }
                        });
                        for (Integer index : values) {
                            sendDynamicActivity.pics += sendDynamicActivity.imageMap.get(index).smallUrl + ",";
                            sendDynamicActivity.sypics += sendDynamicActivity.imageMap.get(index).bigUrl + ",";
                        }
                        sendDynamicActivity.pics = sendDynamicActivity.pics.substring(0, sendDynamicActivity.pics.length() - 1);
                        sendDynamicActivity.sypics = sendDynamicActivity.sypics.substring(0, sendDynamicActivity.sypics.length() - 1);

                        /*SpannableString spannableString = new SpannableString(mSendDynamicContent.getText());
                        String s = convertSpannedToRichText(spannableString);*/
                        sendDynamicActivity.sendDynamic(sendDynamicActivity.mSendDynamicContent.getText().toString());
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_dynamic);
        ButterKnife.bind(this);
        mSendDynamic_gz = (CheckBox) findViewById(R.id.mSecret_gz);
        // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        EventBus.getDefault().register(this);
        setData();
//        getJudgeDynamic();
        isSVIP();  // 一进入页面判断是否是vip  判断是否推荐按钮
        if (getIntent().getStringExtra("video_id") != null) {
            videoUploadCallback(getIntent());
        }
        imageMap = new ConcurrentHashMap<>();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        videoUploadCallback(intent);
    }

    private void videoUploadCallback(Intent intent) {
        videoPlayerLayout.setVisibility(View.VISIBLE);
        mSendDynamicAddPhotos.setVisibility(View.GONE);

        mVideoPath = intent.getStringExtra(KEY_UPLOAD_VIDEO);
        mThumbnailPath = intent.getStringExtra(KEY_UPLOAD_THUMBNAIL);
        if (!TextUtil.isEmpty(mVideoPath)) {
            videoSize = new File(mVideoPath).length();
            imageSize = new File(mThumbnailPath).length();
        }
        mDesc = intent.getStringExtra(KEY_UPLOAD_DESC);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.error(R.drawable.rc_image_error);
        requestOptions.placeholder(R.drawable.rc_image_error);
        File file = new File(mThumbnailPath);
        Glide.with(this)
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

    public void isSVIP() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetUserPowerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, TAG);
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            VipAndVolunteerData data = new Gson().fromJson(response, VipAndVolunteerData.class);
                            String svip = data.getData().getSvip();
                            PrintLogUtils.log(svip, TAG);
                            if (svip.equals("0")) {
                                mSendDynamic_gz.setChecked(false);
                                mSendDynamic_gz_flag = "0";
                                mSendDynamic_gz.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        BindSvipDialog.bindAlertDialog(SendDynamicActivity.this, "SVIP才可以推荐");
                                        mSendDynamic_gz.setChecked(false);
                                    }
                                });
                            } else {
                                mSendDynamic_gz_flag = "1";

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSendDynamic_gz.setChecked(true);
                                    }
                                });
                                mSendDynamic_gz.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        boolean checked = mSendDynamic_gz.isChecked();
                                        if (checked) {
                                            mSendDynamic_gz_flag = "1";
                                        } else {
                                            mSendDynamic_gz_flag = "0";
                                        }
                                    }
                                });
                            }
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

    private void getJudgeDynamic() {
        HttpHelper.getInstance().getUserSendDynamicPermission(new HttpListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(msg);
                finish();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setData() {
        if (getIntent().getStringExtra("topictitle") != null) {
            mSendDynamicLlTvTopic.setVisibility(View.VISIBLE);
            mSendDynamicTvTopic.setText(getIntent().getStringExtra("topictitle"));
        }
        if (getIntent().getStringExtra("topicid") != null) {
            topicId = getIntent().getStringExtra("topicid");
        }
        topicState = getIntent().getIntExtra("topicState", -1);
        mSendDynamicAddPhotos.setMaxItemCount(9);
        mSendDynamicAddPhotos.setEditable(true);
        mSendDynamicAddPhotos.setPlusEnable(true);
        mSendDynamicAddPhotos.setSortable(true);
        // 设置拖拽排序控件的代理
        mSendDynamicAddPhotos.setDelegate(this);
        // 解决scrollView中嵌套EditText导致不能上下滑动的问题
        mSendDynamicContent.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mBottomFlag = false;
            }
            //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
            if (canVerticalScroll(mSendDynamicContent)) {
                if (!mBottomFlag) {
                    // 解决scrollView中嵌套EditText导致不能上下滑动的问题
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
            return false;
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mSendDynamicContent.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY == mOffsetHeight || scrollY == 0) {
                    //这里触发父布局或祖父布局的滑动事件
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    mBottomFlag = true;
                }
            });
        }
        mSendDynamicContent.addTextChangedListener(new EditTitleChangedListener());
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
        mSendDynamicContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(s.length() > start)) {
                    return;
                }
                if ('@' == s.charAt(start) && count == 1) {
                    //Intent intent = new Intent(SendDynamicActivity.this, AtFansActivity.class);
                    Intent intent = new Intent(SendDynamicActivity.this, AtMemberActivity.class);
                    startActivityForResult(intent, 100);
                    return;
                }

//                if ((s.charAt(start) == '@') && (s.charAt(start + count - 1) == ' ')) {
//                    if ('@' == s.charAt(start - 1)) {
//                        mSendDynamicContent.getText().delete(start - 1, start);
//                    }
//                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        mSendDynamicContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = mSendDynamicContent.getSelectionStart();

                ATSpan[] atSpans = mSendDynamicContent.getText().getSpans(0, mSendDynamicContent.getText().length(), ATSpan.class);
                int length = atSpans.length;

                if (0 == length) {
                    return;

                }

                for (ATSpan atSpan : atSpans) {
                    int start = mSendDynamicContent.getText().getSpanStart(atSpan);
                    int end = mSendDynamicContent.getText().getSpanEnd(atSpan);
                    if (selectionStart >= start && selectionStart <= end) {
                        mSendDynamicContent.setSelection(end);
                        return;
                    }
                }
            }
        });

        mSendDynamicContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {

                    int selectionStart = mSendDynamicContent.getSelectionStart();

                    ATSpan[] atSpans = mSendDynamicContent.getText().getSpans(0, mSendDynamicContent.getText().length(), ATSpan.class);
                    int length = atSpans.length;

                    if (0 == length) {
                        return false;
                    }

                    for (ATSpan atSpan : atSpans) {
                        int start = mSendDynamicContent.getText().getSpanStart(atSpan);
                        int end = mSendDynamicContent.getText().getSpanEnd(atSpan);
                        if (selectionStart >= start + 1 && selectionStart <= end) {
                            mSendDynamicContent.getText().delete(start + 1, end);
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
                                    final TextView textview = new TextView(SendDynamicActivity.this);
                                    TextViewUtil.setTextSizeWithDp(textview, 13);
                                    textview.setTextColor(Color.parseColor("#999999"));
                                    textview.setText("#" + topicSevenDatas.get(i).getTitle() + "#");
                                    textview.setBackgroundResource(bgBorder[i % 7]);
                                    mSendDynamicWordwrapview.addView(textview);
                                    final int j = i;
                                    textview.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mSendDynamicLlTvTopic.setVisibility(View.VISIBLE);
                                            mSendDynamicTvTopic.setText(topicSevenDatas.get(j).getTitle());
                                            topicId = topicSevenDatas.get(j).getTid();
//                                            mSendDynamicWordwrapview.removeAllViews();
//                                            for (int i = 0; i < huatis.size(); i++) {
//                                                huatis.get(i).setSelected(false);
//                                            }
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
                                    final TextView textview = new TextView(SendDynamicActivity.this);
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
                                                Intent intent = new Intent(SendDynamicActivity.this, TopicActivity.class);
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

    @OnClick({R.id.mSendDynamic_ll_tvTopic, R.id.mSendDynamic_cancel, R.id.mSendDynamic_send, R.id.mSendDynamic_add_photos, R.id.mSendDynamic_at, R.id.delete_img})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mSendDynamic_cancel:
                String trim = mSendDynamicContent.getText().toString().trim();
                if (trim.length() > 0) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("是否取消发布动态?").setPositiveButton("继续编辑", null).setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
                } else {
                    finish();
                }


                break;
            case R.id.mSendDynamic_send:
                if (dynamicRetcode != 3001) {
                    if (isSend) {

                        if (images.size() == 0) {

                            if (TextUtils.isEmpty(mThumbnailPath) && TextUtils.isEmpty(videoId) && TextUtils.isEmpty(mSendDynamicContent.getText().toString().trim())) {
                                ToastUtil.show(this, "动态内容不能为空");
                            } else if (!TextUtils.isEmpty(mThumbnailPath)) {
                                showdialog(1);
                                initNetWatchdog();
                            } else {
                                showdialog(2);
                                sendDynamic(mSendDynamicContent.getText().toString().trim());
                            }
                        } else {
                            showdialog(2);
                            totalNum = images.size();
                            upPics();
                        }
                    } else {
                        ToastUtil.show(getApplicationContext(), toastStr);
                    }
                } else {
                    BindPhoneAlertDialog.bindAlertDialog(this, toastStr);
                }
                break;
            case R.id.mSendDynamic_add_photos:
                if (dynamicRetcode != 3001) {
                    if (isSend) {
                        // 选择相册
                        setAdminOperation(new String[]{"选择相册", "拍视频", "选择视频"});
                    } else {
                        ToastUtil.show(getApplicationContext(), toastStr);
                    }
                } else {
                    BindPhoneAlertDialog.bindAlertDialog(this, toastStr);
                }
                break;
            case R.id.mSendDynamic_at:
                if (dynamicRetcode != 3001) {
                    if (isSend) {
//                        intent = new Intent(this, AtFansActivity.class);
                        intent = new Intent(this, AtMemberActivity.class);
                        startActivityForResult(intent, 100);
                    } else {
                        ToastUtil.show(getApplicationContext(), toastStr);
                    }
                } else {
                    BindPhoneAlertDialog.bindAlertDialog(this, toastStr);
                }
                break;

            case R.id.mSendDynamic_ll_tvTopic:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("是否删除话题标签?").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        topicId = "-1";
                        mSendDynamicLlTvTopic.setVisibility(View.GONE);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

                break;
            case R.id.delete_img:
                mThumbnailPath = "";
                mVideoPath = "";
                mThumbnailPath = "";
                mDesc = "";
                videoPlayerLayout.setVisibility(View.GONE);
                mSendDynamicAddPhotos.setVisibility(View.VISIBLE);
                break;
        }
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
                        mSendDynamicAddPhotos.setData(new ArrayList<String>());
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
                        Reference<SendDynamicActivity> reference = new WeakReference(SendDynamicActivity.this);
                        AlivcSvideoRecordActivity.startRecord(reference.get(), recordParam);
                        break;
                    case "选择视频":
                        choiceVideoWrapper();
                        // chooseVideo();
                        break;
                }
            }
        }).show();
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        String trim = mSendDynamicContent.getText().toString().trim();
        Constants.SDCardConstants.clearCacheDir(this.getApplicationContext());
        if (trim.length() > 0) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("是否取消发布动态?").setPositiveButton("继续编辑", null).setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).create().show();
        } else {
            finish();
        }

    }

    private void sendDynamic(String content) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("content", content);
        map.put("pic", pics);
        map.put("sypic", sypics);
        if (!TextUtil.isEmpty(videoId)) {
            map.put("videoId", videoId);
            if (mWidth == 0) {
                mWidth = getResources().getDisplayMetrics().widthPixels;
            }
            if (mHeight == 0) {
                mHeight = getResources().getDisplayMetrics().heightPixels;
            }
            map.put("width", mWidth + "");
            map.put("height", mHeight + "");
        }

        if (!TextUtil.isEmpty(mImageUrl)) {
            map.put("coverUrl", mImageUrl);
        }

        map.put("recommend", mSendDynamic_gz_flag);

        if (!topicId.equals("-1")) {
            map.put("tid", topicId);
        }
        String substring = "";
        String substring2 = "";
        atStr = "";
        atuname = "";
        for (int i = 0; i < atuidlist.size(); i++) {
            atStr += atuidlist.get(i) + ",";
            atuname += atunamelist.get(i) + ",";
        }
        if (atuidlist.size() > 0) {
            substring = atStr.substring(0, atStr.length() - 1);
            substring2 = atuname.substring(0, atuname.length() - 1);
        }
        map.put("atuid", substring);
        map.put("atuname", substring2);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SendDynamic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("senddynamic", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
//                                    SharedPreferencesUtils.setParam(getApplicationContext(),"dynamicTimeMillis",String.valueOf(System.currentTimeMillis()));
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    EventBus.getDefault().post(new SendDynamicSuccessEvent(0));
                                    finish();
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
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

    private void upPics() {
        //ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        ExecutorService singleThreadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < images.size(); i++) {
            final int j = i;
            singleThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap compressedImageBitmap = null;
                    try {
                        compressedImageBitmap = new Compressor(SendDynamicActivity.this).compressToBitmap(new File(images.get(j)));
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
                        PhotoUploadTask put = new PhotoUploadTask(NetPic() + "Api/Api/dynamicPicUpload", is, SendDynamicActivity.this, j, handler);//http://59.110.28.150:888/
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

    private void showdialog(int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (type == 1) {
            builder = new AlertDialog.Builder(this, R.style.Translucent_NoTitle);
            bar = new CirclePgBar(this);
            builder.setTitle("发布动态中,请稍后...")
                    .setCancelable(false)
                    .setView(bar);
        } else {

            ProgressBar bar = new ProgressBar(this);
            builder.setTitle("发布动态中,请稍后...")
                    .setCancelable(false)
                    .setView(bar);
        }

        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        if (dynamicRetcode != 3001) {
            if (isSend) {
                // 选择相册
                setAdminOperation(new String[]{"选择相册", "拍视频", "选择视频"});
            } else {
                ToastUtil.show(getApplicationContext(), toastStr);
            }
        } else {
            BindPhoneAlertDialog.bindAlertDialog(this, toastStr);
        }
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mSendDynamicAddPhotos.removeItem(position);
        images.remove(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {

        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(mSendDynamicAddPhotos.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, REQUEST_CODE_PHOTO_PREVIEW);
//        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mSendDynamicAddPhotos.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
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
            mSendDynamicAddPhotos.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));
            images.addAll(data.getStringArrayListExtra("EXTRA_SELECTED_PHOTOS"));
            Log.i("reportImages", "onActivityResult: " + data.getExtras());
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            mSendDynamicAddPhotos.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
        } else if (requestCode == 100) {
            try {
                // atStr = data.getStringExtra("atStr");
                String atSize = data.getStringExtra("atSize");
                mSendDynamicCount.setText("@ " + atSize + "个人");
                SpannableStringBuilder builder = new SpannableStringBuilder(mSendDynamicCount.getText().toString());
                ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#b73acb"));
                builder.setSpan(purSpan, 1, atSize.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mSendDynamicCount.setText(builder);

            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else if (requestCode == 188) {
            try {
                topicId = data.getStringExtra("topicid");
                topicState = data.getIntExtra("topicState", -1);
                mSendDynamicLlTvTopic.setVisibility(View.VISIBLE);
                mSendDynamicTvTopic.setText(data.getStringExtra("topictitle"));
                initTopic();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_CROP_VIDEO) {
//            AlivcCropOutputParam outputParam = (AlivcCropOutputParam)data.getSerializableExtra(AlivcCropOutputParam.RESULT_KEY_OUTPUT_PARAM);
//            if (outputParam == null) {
//                return;
//            }
//            String path = outputParam.getOutputPath();
//            long duration = outputParam.getDuration();
//            long startTime = outputParam.getStartTime();
//            AlivcEditInputParam editParam = new AlivcEditInputParam.Builder()
//                    .setGop(30)
//                    .setVideoCodec(VideoCodecs.H264_SOFT_FFMPEG)
//                    .setVideoQuality(VideoQuality.SSD)
//                    .setVideoMediaInfo(path, (int) duration)
//                    .setCanReplaceMusic(true)
//                    .setResolutionMode(AliyunSnapVideoParam.RESOLUTION_480P)
//                    .build();
//
//            EditorMediaActivity.startImport(SendDynamicActivity.this, editParam);
        } else if (requestCode == REQUEST_CODE_CHOOSE_VIDEO) {
            LogUtil.d(data.getData());
            String videoPath = FileUtil.getPathFromUri(data.getData());
            LogUtil.d(videoPath);
            mVideoPath = videoPath;
            startVideoUpload();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        choicePhotoWrapper();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_PERMISSION_VIDEO_PICKER) {
            Toast.makeText(this, "您拒绝了「视频选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "Shengmo");
//            if (!takePhotoDir.exists()) {
//                takePhotoDir.mkdirs();
//            }

            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(mSendDynamicAddPhotos.getMaxItemCount() - mSendDynamicAddPhotos.getItemCount()) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_PHOTO);
//            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mSendDynamicAddPhotos.getMaxItemCount() - mSendDynamicAddPhotos.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }


    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_VIDEO_PICKER)
    private void choiceVideoWrapper() {
        int min = 50;// 最小时长
        int max = 15000;// 最大时长
        int gop = 30;  // 关键帧间隔  建议 1-300  默认30
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(SendDynamicActivity.this, perms)) {
            upSyUrls.clear();
            upUrls.clear();
            mSendDynamicAddPhotos.setData(new ArrayList<String>());
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
            CropMediaActivity.startCropForResult(SendDynamicActivity.this, REQUEST_CODE_CROP_VIDEO, snapParam);
        } else {
            EasyPermissions.requestPermissions(this, "视频选择需要以下权限:\n\n1.访问设备上的照片", REQUEST_CODE_PERMISSION_VIDEO_PICKER, perms);
        }

    }

    void chooseVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimetypes = {"video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_VIDEO);
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }


    class EditTitleChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mSendDynamicTextcount.setText("" + s.length() + "/10000");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() == 10000) {
                ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.mSendDynamic_content:
                // 解决scrollView中嵌套EditText导致不能上下滑动的问题
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApp.uid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "uid", "0");
        //判断是否可以发送动态
        getJudgeDynamic();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gethaoyouliebiao(Message message) {
        if (message.arg2 == 11) {
            Atbean atbean = (Atbean) message.obj;

            List<Atbean.DataBean> dataBean = atbean.getDataBean();
            int select_index = mSendDynamicContent.getSelectionStart();
            if (select_index >= 0) {
                if (mSendDynamicContent.getEditableText().charAt(select_index - 1) == '@') {
                    mSendDynamicContent.getEditableText().delete(select_index - 1, select_index);
                    select_index--;
                }
            }
            for (int i = 0; i < dataBean.size(); i++) {
                String uid = dataBean.get(i).getUid();
                atuidlist.add(uid);
                atunamelist.add("@" + dataBean.get(i).getNickname() + " ");
                SpannableString span;
                span = new SpannableString("@" + dataBean.get(i).getNickname() + " ");
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
                if (select_index < 0 || select_index >= mSendDynamicContent.getText().length()) {
                    mSendDynamicContent.append(span);
                } else {
                    mSendDynamicContent.getText().insert(select_index, span);//光标所在位置插入文字
                    // mSendDynamicContent.append(span,select_index,select_index + span.length());

                }
                select_index = select_index + span.length();
                // mSendDynamicContent.append(span);
                // mSendDynamicContent.append(span,select_index,select_index + span.length());
            }
            //LogUtil.d("setSelction =  " + select_index );
            mSendDynamicContent.setSelection(mSendDynamicContent.getText().length());
//            if (mSendDynamicContent.getText().toString().equals("@")) {
//                mSendDynamicContent.setText(" ");
//            }
        }
    }


    private NetWatchdogUtils mWatchdog;
    private AliyunVodCompose mComposeClient;
    private boolean mIsUpload;
    private String mImageUrl;
    private String mVideoUrl;
    private int mWidth = 0, mHeight = 0;
    private String mVideoPath;

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
        HttpRequest.post(sp.getString("api_host", "http://cs.aiwujie.net/") + "Api/Dynamic/getImageUploadAddress",
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
                                        com.aliyun.common.utils.ToastUtil.showToast(SendDynamicActivity.this, getResources().getString(com.aliyun.svideo.editor.R.string.alivc_editor_publish_upload_param_error));
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
                                    com.aliyun.common.utils.ToastUtil.showToast(SendDynamicActivity.this, "Get image upload auth info failed");
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
                                com.aliyun.common.utils.ToastUtil.showToast(SendDynamicActivity
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
        HttpRequest.get(sp.getString("api_host", "http://cs.aiwujie.net/") + "Api/Dynamic/getVideoUploadAddress", params, new StringHttpRequestCallback() {
            @Override
            protected void onSuccess(String s) {
                super.onSuccess(s);
                VodVideoUploadAuth tokenInfo = VodVideoUploadAuth.getVideoTokenInfo(s);
                if (tokenInfo != null && mComposeClient != null) {
                    computeWidthAndHeight();
                    videoId = tokenInfo.getVideoId().toString();
                    mVideoUrl = tokenInfo.getUploadAddress();
                    int rv = mComposeClient.uploadVideoWithVod(mVideoPath, tokenInfo.getUploadAddress(), tokenInfo.getUploadAuth(), mUploadCallback);
                    if (rv < 0) {
                        Log.d(AliyunTag.TAG, "上传参数错误 video path : " + mVideoPath + " thumbnailk : " + mThumbnailPath);
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                com.aliyun.common.utils.ToastUtil.showToast(SendDynamicActivity.this, getResources().getString(com.aliyun.svideo.editor.R.string.alivc_editor_publish_upload_param_error));
                            }
                        });
                    } else {
                        mIsUpload = true;
                    }
                } else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            com.aliyun.common.utils.ToastUtil.showToast(SendDynamicActivity.this, "Get video upload auth failed");
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
                        com.aliyun.common.utils.ToastUtil.showToast(SendDynamicActivity.this, "Get video upload auth failed");
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
                    sendDynamic(mSendDynamicContent.getText().toString());
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
                    int progress = 0;
                    if (mComposeClient.getState() == AliyunVodCompose.AliyunComposeState.STATE_IMAGE_UPLOADING) {
                        progress = (int) ((uploadedSize * 100) / (totalSize + videoSize));
                    } else if (mComposeClient.getState() == AliyunVodCompose.AliyunComposeState.STATE_VIDEO_UPLOADING) {
                        progress = (int) (((uploadedSize + imageSize) * 100) / (totalSize + imageSize));
                    }
                    Log.i("zq", progress + "");
                    if (bar != null) {
                        bar.setmProgress(progress);
                    }

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
     *
     * @param videoId
     */
    private void refreshVideoUpload(String videoId) {
        SharedPreferences sp = getSharedPreferences("personData", Context.MODE_PRIVATE);
        RequestParams params = new RequestParams();
        params.addFormDataPart("videoId", videoId);
        HttpRequest.post(sp.getString("api_host", "http://cs.aiwujie.net/") + "Api/Dynamic/refreshVideoUploadAddress", params, new StringHttpRequestCallback() {
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
                                com.aliyun.common.utils.ToastUtil.showToast(SendDynamicActivity.this, getResources().getString(com.aliyun.svideo.editor.R.string.alivc_editor_publish_upload_param_error));
                            }
                        });
                    } else {
                        mIsUpload = true;
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            com.aliyun.common.utils.ToastUtil.showToast(SendDynamicActivity.this, "Get video upload auth failed");
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


    void computeWidthAndHeight() {
        Glide.with(SendDynamicActivity.this).asBitmap().load(mImageUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                mWidth = resource.getWidth();
                mHeight = resource.getHeight();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    int mOffsetHeight = 0;
    boolean mBottomFlag = false;

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        //int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        int scrollExtent = editText.getHeight() - editText.getTotalPaddingTop() - editText.getTotalPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;
        mOffsetHeight = scrollDifference;
        if (scrollDifference == 0) { //editText未填满
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
}
