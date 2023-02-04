package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.EditDynamicPicAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicPicData;
import com.aiwujie.shengmo.bean.SetMarkNameData;
import com.aiwujie.shengmo.eventbus.EditDynamicIndexEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import id.zelory.compressor.Compressor;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class EditGuanliBeizhuActivity extends AppCompatActivity implements BGASortableNinePhotoLayout.Delegate, EasyPermissions.PermissionCallbacks, BGAOnRVItemClickListener, View.OnTouchListener {


    @BindView(R.id.mEdit_name_return)
    ImageView mEditNameReturn;
    @BindView(R.id.mEdit_name_Tijiao)
    TextView mEditNameTijiao;
    @BindView(R.id.mEdit_name_edittext)
    EditText mEditNameEdittext;
    @BindView(R.id.mEdit_name_count)
    TextView mEditNameCount;
    @BindView(R.id.mBeizhu_add_photos)
    BGASortableNinePhotoLayout mBeizhuAddPhotos;
    @BindView(R.id.mBeizhu_nineGrid)
    NineGridView mBeizhuNineGrid;
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;

    List<String> images = new ArrayList<>();
    List<String> upUrls = new ArrayList<>();
    List<String> upSyUrls = new ArrayList<>();
    ArrayList<String> sypic = new ArrayList<>();

    EditDynamicPicAdapter editBeizhuPicAdapter;

    private AlertDialog dialog;
    private String pics = "";
    private String sypics = "";

    private int totalNum = 0;
    private int currentNum = 0;
    private String fuid;
    private String etContent;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 152:
                    String data = (String) msg.obj;
                    currentNum += 1;
                    try {
                        JSONObject obj = new JSONObject(data);
                        if (obj.getInt("retcode") == 2000) {
                            DynamicPicData beanIcon = new Gson().fromJson(data, DynamicPicData.class);
                            String picurl = beanIcon.getData().getSlimg();
                            String picsy = beanIcon.getData().getSyimg();
                            upSyUrls.add(picsy);
                            upUrls.add(picurl);
                        } else {
                            dialog.dismiss();
                            ToastUtil.show(getApplication(), "网络异常或者图片过大,请稍后重试...");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    if (totalNum == currentNum) {
                        pics = "";
                        sypics = "";
                        for (int i = 0; i < upUrls.size(); i++) {
                            pics += upUrls.get(i) + ",";
                            sypics += upSyUrls.get(i) + ",";
                        }
                        pics = pics.substring(0, pics.length() - 1);
                        sypics = sypics.substring(0, sypics.length() - 1);
                        /*SpannableString spannableString = new SpannableString(mSendDynamicContent.getText());
                        String s = convertSpannedToRichText(spannableString);*/
                        setmarkname();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_guanlibeizhu);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        Intent intent = getIntent();
        EventBus.getDefault().register(this);
        Intent intent1 = getIntent();
        fuid = intent1.getStringExtra("fuid");
        init();

    }

    private void init() {
        sypic = (ArrayList<String>) getIntent().getSerializableExtra("sypic");
        if(sypic==null)
            sypic=new ArrayList<>();
        if (sypic.size() != 0) {
            mBeizhuNineGrid.setVisibility(View.VISIBLE);
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for (int i = 0; i < sypic.size(); i++) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(sypic.get(i));
                if (sypic.size() != 0) {
                    info.setBigImageUrl(sypic.get(i));
                } else {
                    info.setBigImageUrl(sypic.get(i));
                }
                imageInfo.add(info);
            }
            editBeizhuPicAdapter = new EditDynamicPicAdapter(this, imageInfo);
            mBeizhuNineGrid.setAdapter(editBeizhuPicAdapter);
        } else {
            mBeizhuNineGrid.setVisibility(View.GONE);
        }
        mBeizhuAddPhotos.setMaxItemCount(9-sypic.size());
        mBeizhuAddPhotos.setEditable(true);
        mBeizhuAddPhotos.setPlusEnable(true);
        mBeizhuAddPhotos.setSortable(true);
        // 设置拖拽排序控件的代理
        mBeizhuAddPhotos.setDelegate(this);
        mEditNameEdittext.setOnTouchListener(this);
        mEditNameEdittext.setText(getIntent().getStringExtra("name"));
        mEditNameEdittext.addTextChangedListener(new EditTitleChangedListener());
        mEditNameEdittext.setSelection(mEditNameEdittext.length());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void editDyEventBus(EditDynamicIndexEvent event) {
        String slpic = sypic.remove(event.getIndex());
//        String sypic = sypics.remove(event.getIndex());
        if (sypic.size() != 0) {
            mBeizhuNineGrid.setVisibility(View.VISIBLE);
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for (int i = 0; i < sypic.size(); i++) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(sypic.get(i));
                if (sypic.size() != 0) {
                    info.setBigImageUrl(sypic.get(i));
                } else {
                    info.setBigImageUrl(sypic.get(i));
                }
                imageInfo.add(info);
            }
            editBeizhuPicAdapter = new EditDynamicPicAdapter(this, imageInfo);
            mBeizhuNineGrid.setAdapter(editBeizhuPicAdapter);
        } else {
            mBeizhuNineGrid.setVisibility(View.GONE);
        }
        mBeizhuAddPhotos.setMaxItemCount(9 - sypic.size());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.mEdit_name_return, R.id.mEdit_name_Tijiao,R.id.mBeizhu_add_photos})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mEdit_name_return:
                finish();
                break;
            case R.id.mEdit_name_Tijiao:
                etContent = mEditNameEdittext.getText().toString().trim();
                if (images.size() == 0) {
                            /*SpannableString spannableString = new SpannableString(mSendDynamicContent.getText());
                            String s = convertSpannedToRichText(spannableString);*/
                    if (pics.length() == 0 && sypics.length() == 0 && mEditNameEdittext.getText().toString().length() == 0)
                        ToastUtil.show(this, "内容不能为空");
                    else {
                        showdialog();
                        setmarkname();
                    }
                } else {
                    showdialog();
                    totalNum = images.size();
                    upPics();
                }
                break;
            case R.id.mBeizhu_add_photos:
                // 选择相册
                setAdminOperation(new String[]{"选择相册"});
                break;
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
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.mEdit_name_edittext:
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
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        // 选择相册
        setAdminOperation(new String[]{"选择相册"});
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mBeizhuAddPhotos.removeItem(position);
        images.remove(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(mBeizhuAddPhotos.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            mBeizhuAddPhotos.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));
            images.addAll(data.getStringArrayListExtra("EXTRA_SELECTED_PHOTOS"));
            Log.i("reportImages", "onActivityResult: " + data.getExtras());
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            mBeizhuAddPhotos.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
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
                    .maxChooseCount(mBeizhuAddPhotos.getMaxItemCount() - mBeizhuAddPhotos.getItemCount()) // 图片选择张数的最大值
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
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    class EditTitleChangedListener implements TextWatcher {
        private CharSequence temp = "";//监听前的文本

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEditNameCount.setText("(" + s.length() + "/1000)");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() >= 1000) {
                ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
            }
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
                        compressedImageBitmap = new Compressor(EditGuanliBeizhuActivity.this).compressToBitmap(new File(images.get(j)));
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
                        PhotoUploadTask put = new PhotoUploadTask(NetPic() + "Api/Api/dynamicPicUpload", is, EditGuanliBeizhuActivity.this, handler);//http://59.110.28.150:888/
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

    public void setmarkname() {
        String temp="";
        for(String url:sypic){
            temp=temp+url.replace("http://image.aiwujie.com.cn/","")+",";
        }
        if(temp.length()>5)
            sypics=temp+sypics;
        Map<String, String> map = new HashMap<>();
        map.put("loginid", MyApp.uid);
        map.put("uid", fuid);
        map.put("content", etContent);
//        map.put("pic", pics);
        map.put("sypic", sypics);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.editAdminmrak, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    final SetMarkNameData data = new Gson().fromJson(response, SetMarkNameData.class);
                    JSONObject object = new JSONObject(response);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            ToastUtil.show(EditGuanliBeizhuActivity.this, "" + data.getMsg());
                            setResult(200);
                            finish();
                        }
                    });


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

    ;
}
