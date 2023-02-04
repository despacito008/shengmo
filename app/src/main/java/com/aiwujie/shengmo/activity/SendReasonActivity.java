package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.Atbean;
import com.aiwujie.shengmo.bean.DynamicPicData;
import com.aiwujie.shengmo.customview.BindPhoneAlertDialog;
import com.aiwujie.shengmo.eventbus.RecorderEvent;
import com.aiwujie.shengmo.eventbus.SendDynamicSuccessEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.statistical.AtMemberActivity;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import id.zelory.compressor.Compressor;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class SendReasonActivity extends AppCompatActivity implements BGASortableNinePhotoLayout.Delegate, BGAOnRVItemClickListener, EasyPermissions.PermissionCallbacks, View.OnTouchListener {

    public static final String TAG = "SendDynamicActivity";

    @BindView(R.id.mSendDynamic_cancel)
    TextView mSendDynamicCancel;
    @BindView(R.id.mSendDynamic_send)
    TextView mSendDynamicSend;
    @BindView(R.id.mSendDynamic_content)
    EditText mSendDynamicContent;
    @BindView(R.id.mSendDynamic_textcount)
    TextView mSendDynamicTextcount;
//    @BindView(R.id.mSendDynamic_wordwrapview)
//    WordWrapView mSendDynamicWordwrapview;
    @BindView(R.id.mSendDynamic_add_photos)
    BGASortableNinePhotoLayout mSendDynamicAddPhotos;
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    private static final int REQUEST_CODE_CROP_VIDEO=3;

    @BindView(R.id.mSendDynamic_count)
    TextView mSendDynamicCount;

    @BindView(R.id.mSendDynamic_recommend)
    CheckBox mSendDynamic_recommend;

    private String method="",days,uid="";



    private int totalNum = 0;
    private int currentNum = 0;
    private int topstate = 0;
    List<String> images = new ArrayList<>();
    List<String> upUrls = new ArrayList<>();
    List<String> upSyUrls = new ArrayList<>();
    private String colors[] = {"#ff0000", "#b73acb", "#0000ff", "#18a153", "#f39700", "#ff00ff", "#00a0e9"};
//    private List<TopicSevenData> topicSevenDatas = new ArrayList<>();
//    private List<TextView> huatis = new ArrayList<>();
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
//    private int topicState = -1;
    private int videoW=0,videoH=0;
    private String mSendDynamic_gz_flag = "0";//推荐按钮
    List<String> atuidlist = new ArrayList<>();
    List<String> atunamelist = new ArrayList<>();

    private String videoId = "", coverUrl = "";
    private MyHandler handler = new MyHandler(this);

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 152:
//                    String data = (String) msg.obj;
//                    currentNum += 1;
//                    try {
//                        JSONObject obj = new JSONObject(data);
//                        if (obj.getInt("retcode") == 2000) {
//                            DynamicPicData beanIcon = new Gson().fromJson(data, DynamicPicData.class);
//                            String picurl = beanIcon.getData().getSlimg();
//                            String picsy = beanIcon.getData().getSyimg();
//                            upSyUrls.add(picsy);
//                            upUrls.add(picurl);
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
//                        pics = "";
//                        sypics = "";
//                        for (int i = 0; i < upUrls.size(); i++) {
//                            pics += upUrls.get(i) + ",";
//                            sypics += upSyUrls.get(i) + ",";
//                        }
//                        pics = pics.substring(0, pics.length() - 1);
//                        sypics = sypics.substring(0, sypics.length() - 1);
//                        /*SpannableString spannableString = new SpannableString(mSendDynamicContent.getText());
//                        String s = convertSpannedToRichText(spannableString);*/
////                        sendDynamic(mSendDynamicContent.getText().toString());
////                        if(days!=null)
////                            adminBanOperation(method,days);
////                        else
////                            changeDeviceStatus(method);
//                        if (method.equals("forbid")) {
//                            changeDeviceStatus(method);
//                        } else {
//                            adminBanOperation(method,days);
//                        }
//                    }
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };

    static class MyHandler extends Handler {
        WeakReference<SendReasonActivity> activityWeakReference;
        public MyHandler(SendReasonActivity sendReasonActivity) {
            activityWeakReference = new WeakReference<>(sendReasonActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SendReasonActivity sendReasonActivity = activityWeakReference.get();
            switch (msg.what) {
                case 152:
                    String data = (String) msg.obj;
                    sendReasonActivity.currentNum += 1;
                    try {
                        JSONObject obj = new JSONObject(data);
                        if (obj.getInt("retcode") == 2000) {
                            DynamicPicData beanIcon = new Gson().fromJson(data, DynamicPicData.class);
                            String picurl = beanIcon.getData().getSlimg();
                            String picsy = beanIcon.getData().getSyimg();
                            sendReasonActivity.upSyUrls.add(picsy);
                            sendReasonActivity.upUrls.add(picurl);
                        } else {
                            sendReasonActivity.dialog.dismiss();
                            ToastUtil.show(sendReasonActivity, "网络异常或者图片过大,请稍后重试...");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    if (sendReasonActivity.totalNum == sendReasonActivity.currentNum) {
                        sendReasonActivity.pics = "";
                        sendReasonActivity.sypics = "";
                        for (int i = 0; i < sendReasonActivity.upUrls.size(); i++) {
                            sendReasonActivity.pics += sendReasonActivity.upUrls.get(i) + ",";
                            sendReasonActivity.sypics += sendReasonActivity.upSyUrls.get(i) + ",";
                        }
                        sendReasonActivity.pics = sendReasonActivity.pics.substring(0, sendReasonActivity.pics.length() - 1);
                        sendReasonActivity.sypics = sendReasonActivity.sypics.substring(0, sendReasonActivity.sypics.length() - 1);
                        if (sendReasonActivity.method.equals("forbid")) {
                            sendReasonActivity.changeDeviceStatus(sendReasonActivity.method);
                        } else {
                            sendReasonActivity.adminBanOperation(sendReasonActivity.method,sendReasonActivity.days);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_reason);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        EventBus.getDefault().register(this);
        setData();
//        getJudgeDynamic();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void adminBanOperation(String method, String days) {
        Map<String, String> map = new HashMap<>();
        map.put("method", method);
        map.put("uid", uid);
        map.put("content", mSendDynamicContent.getText().toString());
        map.put("pic", pics);
        map.put("sypic", sypics);
        map.put("login_uid", MyApp.uid);
        map.put("blockingalong", days);
        map.put("is_show",mSendDynamic_recommend.isChecked()?"1":"0");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ChangeAllUserStatus, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                dialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    EventBus.getDefault().post(new RecorderEvent(1));
                                    setResult(200);
                                    finish();
                                case 3000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
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
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                dialog.dismiss();
            }
        });
    }

    private void changeDeviceStatus(String method) {
        Map<String, String> map = new HashMap<>();
        map.put("method", method);
        map.put("uid", uid);
        map.put("content", mSendDynamicContent.getText().toString());
        map.put("pic", pics);
        map.put("sypic", sypics);
        map.put("blockingalong",days);
        map.put("login_uid", MyApp.uid);
        map.put("is_show",mSendDynamic_recommend.isChecked()?"1":"0");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ChangeDeviceStatus, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                dialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    EventBus.getDefault().post(new RecorderEvent(1));
                                    EventBus.getDefault().post(new SendDynamicSuccessEvent(0));
                                    ToastUtil.show(SendReasonActivity.this,"封禁成功");
                                    setResult(200);
                                    finish();
                                case 3000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
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
                dialog.dismiss();
            }
        });
    }

    private void setData() {

//        mSendDynamicGuifan.setMovementMethod(LinkMovementMethod.getInstance());
//        TextMoreClickUtils clickUtils=new TextMoreClickUtils(this,"0",handler);
//        mSendDynamicGuifan.setText(clickUtils.addClickablePart("认证会员、VIP会员","","发布的动态将出现在广场中"), TextView.BufferType.SPANNABLE);
        uid=getIntent().getStringExtra("uid");
        method = getIntent().getStringExtra("method");
        days=getIntent().getStringExtra("days");
        mSendDynamicAddPhotos.setMaxItemCount(9);
        mSendDynamicAddPhotos.setEditable(true);
        mSendDynamicAddPhotos.setPlusEnable(true);
        mSendDynamicAddPhotos.setSortable(true);
        // 设置拖拽排序控件的代理
        mSendDynamicAddPhotos.setDelegate(this);
        // 解决scrollView中嵌套EditText导致不能上下滑动的问题
        mSendDynamicContent.setOnTouchListener(this);
        mSendDynamicContent.addTextChangedListener(new EditTitleChangedListener());

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
//                    Intent intent = new Intent(SendReasonActivity.this, AtFansActivity.class);
                    Intent intent = new Intent(SendReasonActivity.this, AtMemberActivity.class);
                    startActivityForResult(intent, 100);
                    return;
                }

                if ((s.charAt(start) == '@') && (s.charAt(start + count - 1) == ' ')) {
                    if ('@' == s.charAt(start - 1)) {
                        mSendDynamicContent.getText().delete(start - 1, start);
                    }
                }

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





    @OnClick({ R.id.mSendDynamic_cancel, R.id.mSendDynamic_send, R.id.mSendDynamic_add_photos, R.id.mSendDynamic_at})
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
//                        String timeMilliss = String.valueOf(System.currentTimeMillis());
//                        String millis = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "dynamicTimeMillis", "0");
//                        if ((Long.parseLong(timeMilliss) - Long.parseLong(millis)) > (60 * 3 * 1000)) {

                        if (images.size() == 0) {
                            /*SpannableString spannableString = new SpannableString(mSendDynamicContent.getText());
                            String s = convertSpannedToRichText(spannableString);*/
                            if (coverUrl.length() == 0 && videoId.length() == 0 && mSendDynamicContent.getText().toString().length() == 0)
                                ToastUtil.show(this, "内容不能为空");
                            else {
                                showdialog();
//                                sendDynamic(mSendDynamicContent.getText().toString());
//                                if(days!=null)
//                                    adminBanOperation(method,days);
//                                else
//                                    changeDeviceStatus(method);
                                if (method.equals("forbid")) {
                                    changeDeviceStatus(method);
                                } else {
                                    adminBanOperation(method,days);
                                }
                            }
                        } else {
                            showdialog();
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
                        setAdminOperation(new String[]{"选择相册"});
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
//            case R.id.mSendDynamic_guifan:
//                intent = new Intent(this, VipWebActivity.class);
//                intent.putExtra("title", "图文规范");
//                intent.putExtra("path", HttpUrl.PicTextHtml);
//                startActivity(intent);
//                break;
         /*   case R.id.mSendDynamic_newTopic:
                intent = new Intent(this, SendTopicActivity.class);
                startActivity(intent);
                finish();
                break;*/

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
                }
            }
        }).show();
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
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

    }


    private void upPics() {
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < images.size(); i++) {
            final int j = i;
            singleThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap compressedImageBitmap = null;
                    try {
                        compressedImageBitmap = new Compressor(SendReasonActivity.this).compressToBitmap(new File(images.get(j)));
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
                        PhotoUploadTask put = new PhotoUploadTask(NetPic() + "Api/Api/dynamicPicUpload", is, SendReasonActivity.this, handler);//http://59.110.28.150:888/
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

    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ProgressBar bar = new ProgressBar(this);
        builder.setTitle("提交中,请稍后...")
                .setCancelable(false)
                .setView(bar);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        if (dynamicRetcode != 3001) {
            if (isSend) {
                // 选择相册
                setAdminOperation(new String[]{"选择相册"});
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

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gethaoyouliebiao(Message message) {
        if (message.arg2 == 11) {
            Atbean atbean = (Atbean) message.obj;
            if (mSendDynamicContent.getText().toString().equals("@")) {
                mSendDynamicContent.setText(" ");
            }
            List<Atbean.DataBean> dataBean = atbean.getDataBean();
            for (int i = 0; i < dataBean.size(); i++) {
                String uid = dataBean.get(i).getUid();
                atuidlist.add(uid);
                atunamelist.add("@" + dataBean.get(i).getNickname() + " ");
                //atStr+=uid+",";
                //atuname+="@"+dataBean.get(i).getNickname()+" ,";
//                ATSpan atSpan = new ATSpan(uid);
                SpannableString span = new SpannableString("@" + dataBean.get(i).getNickname() + " ");
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
//                span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mSendDynamicContent.append(span);
            }
            mSendDynamicContent.setSelection(mSendDynamicContent.getText().length());
        }
    }

}
