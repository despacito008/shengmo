package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import id.zelory.compressor.Compressor;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ReportActivity extends AppCompatActivity implements BGASortableNinePhotoLayout.Delegate, EasyPermissions.PermissionCallbacks, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.mReport_return)
    ImageView mReportReturn;
    @BindView(R.id.mReport_rb01)
    RadioButton mReportRb01;
    @BindView(R.id.mReport_rb02)
    RadioButton mReportRb02;
    @BindView(R.id.mReport_rb03)
    RadioButton mReportRb03;
    @BindView(R.id.mReport_rb04)
    RadioButton mReportRb04;
    @BindView(R.id.mReport_rb05)
    RadioButton mReportRb05;
    @BindView(R.id.mRepor_fujia)
    TextView mReporFujia;
    @BindView(R.id.mRepor_photocount)
    TextView mReporPhotocount;
    @BindView(R.id.mRepor_ll_upload)
    PercentLinearLayout mReporLlUpload;
    @BindView(R.id.mReport_add_photos)
    BGASortableNinePhotoLayout mReportAddPhotos;
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    List<String> images = new ArrayList<>();
    List<String> upUrls = new ArrayList<>();
    @BindView(R.id.mReport_rg)
    RadioGroup mReportRg;
    @BindView(R.id.mReport_tijiao)
    Button mReportTijiao;
    private String otherid;
    private String state = "广告骚扰";
    private String type = "0";
    private int totalNum = 0;
    private int currentNum = 0;
    private AlertDialog dialog;
    private String pics = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 152:
                    currentNum += 1;
                    String s = (String) msg.obj;
                    Log.i("icon", s);
                    BeanIcon beanIcon = new Gson().fromJson(s, BeanIcon.class);
                    String picurl = beanIcon.getData();
                    upUrls.add(picurl);
                    Log.i("senddynamic", "handleMessage: " + totalNum + "," + currentNum);
                    if (totalNum == currentNum) {
                        pics = "";
                        for (int i = 0; i < images.size(); i++) {
                            pics += upUrls.get(i) + ",";
                        }
                        pics = pics.substring(0, pics.length() - 1);
                        Log.i("senddynamic", "handleMessage2: " + totalNum + "," + currentNum);
                        sendReport();
                    }

            }
            super.handleMessage(msg);
        }
    };
    private Display display;
    private int source_type = 0;

    public static void start(Context context,String uid) {
        Intent intent = new Intent(context,ReportActivity.class);
        intent.putExtra("uid",uid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        source_type = getIntent().getIntExtra("source_type",0);
        setData();
        setListener();
    }

    private void setData() {
        WindowManager windowManager = getWindowManager();
        display = windowManager.getDefaultDisplay();
        otherid = getIntent().getStringExtra("uid");
        mReportAddPhotos.setMaxItemCount(6);
        mReportAddPhotos.setEditable(true);
        mReportAddPhotos.setPlusEnable(false);
        mReportAddPhotos.setSortable(true);
        // 设置拖拽排序控件的代理
        mReportAddPhotos.setDelegate(this);
    }

    private void setListener() {
        mReportRg.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.mReport_return, R.id.mRepor_fujia, R.id.mRepor_ll_upload, R.id.mReport_tijiao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mReport_return:
                finish();
                break;
            case R.id.mRepor_fujia:
                Intent intent = new Intent(this, AdditionalActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.mRepor_ll_upload:
                if (mReporPhotocount.getText().toString().equals("6/6")) {
                    ToastUtil.show(getApplicationContext(), "最多只能上传6张图片");
                } else {
                    choicePhotoWrapper();
                }
                break;
            case R.id.mReport_tijiao:
                Log.i("reportactivity", "onClick: " + images.size());
                if (images.size() != 0&&!"".equals(mReporFujia.getText().toString())) {
                    totalNum = images.size();
                    showdialog();
                    upPics();
                }else{
                    ToastUtil.show(getApplicationContext(),"请填写完整举报信息...");
                }
                break;
        }
    }

    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ProgressBar bar = new ProgressBar(this);
        builder.setTitle("上传图片中,请稍后...")
                .setCancelable(false)
                .setView(bar);
        dialog = builder.create();
        dialog.show();
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
                        compressedImageBitmap = new Compressor(ReportActivity.this).compressToBitmap(new File(images.get(j)));
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
                        PhotoUploadTask put = new PhotoUploadTask(HttpUrl.NetPic()+"Api/Api/fileUpload", is, ReportActivity.this, handler);
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

    private void sendReport() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("otheruid", otherid);
        map.put("state", state);
        map.put("report", mReporFujia.getText().toString());
        map.put("type", type);
        map.put("images", pics);
        map.put("source_type",String.valueOf(source_type));
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Report, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    finish();
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
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

            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mReport_rb01:
                state = "广告骚扰";
                type = "0";
                break;
            case R.id.mReport_rb02:
                state = "政治敏感";
                type = "0";
                break;
            case R.id.mReport_rb03:
                state = "散布色情内容";
                type = "0";
                break;
            case R.id.mReport_rb04:
                state = "涉毒";
                type = "0";
                break;
            case R.id.mReport_rb05:
                state = "其它";
                type = "1";
                Intent intent = new Intent(this, AdditionalActivity.class);
                startActivityForResult(intent, 200);
                break;
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mReportAddPhotos.removeItem(position);
        mReporPhotocount.setText(mReportAddPhotos.getItemCount() + "/6");
        images.remove(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(mReportAddPhotos.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, REQUEST_CODE_PHOTO_PREVIEW);
//        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mReportAddPhotos.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            mReportAddPhotos.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));
            mReporPhotocount.setText(mReportAddPhotos.getItemCount() + "/6");
            images.addAll(data.getStringArrayListExtra("EXTRA_SELECTED_PHOTOS"));
            Log.i("reportImages", "onActivityResult: " + data.getStringArrayListExtra("EXTRA_SELECTED_PHOTOS").toString());
//            String pics=data.getExtras().toString();
//            images = Arrays.asList(pics.split(","));
//            for(String s : images){
//                Log.i("reportImages", "onActivityResult: "+s);
//            }

        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            mReportAddPhotos.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
        }
        if (requestCode == 200) {
            try {
                mReporFujia.setText(data.getStringExtra("addition"));
            } catch (Exception e) {
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
            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(mReportAddPhotos.getMaxItemCount() - mReportAddPhotos.getItemCount()) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_PHOTO);
//            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mReportAddPhotos.getMaxItemCount() - mReportAddPhotos.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }

}
