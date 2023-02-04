package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.eventbus.PhotoRzEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.CropUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class PhotoAuthenticationActivity extends AppCompatActivity implements OnItemClickListener, EasyPermissions.PermissionCallbacks {


    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    @BindView(R.id.iv_normal_title_back)
    ImageView ivNormalTitleBack;
    @BindView(R.id.tv_normal_title_title)
    TextView tvNormalTitleTitle;
    @BindView(R.id.iv_normal_title_more)
    ImageView ivNormalTitleMore;
    @BindView(R.id.mPhoto_auth_pic)
    ImageView mPhotoAuthPic;
    @BindView(R.id.mPhoto_auth_btn01)
    TextView mPhotoAuthBtn01;
    @BindView(R.id.mPhoto_auth_btn02)
    TextView mPhotoAuthBtn02;
    @BindView(R.id.mPhoto_auth_tijiao)
    TextView mPhotoAuthTijiao;
    private Uri cropUri;
    private File cropImage;
    private String headurl = "";
    private String isOpenPhoto = "0";
    private String card_face;
    private int retcode;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 152:
                    String s = (String) msg.obj;
                    Log.i("icon", s);
                    BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                    headurl = beanicon.getData();
                    ToastUtil.show(getApplicationContext(), "上传完成");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_auth);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        Intent intent = getIntent();
        retcode = intent.getIntExtra("retcode", -1);
        switch (retcode) {
            case 2000:
                isOpenPhoto = intent.getStringExtra("status");
                card_face = intent.getStringExtra("card_face");
                ImageLoader.loadImage(this,card_face,mPhotoAuthPic);
//                mPhotoAuthTijiao.setText("重新认证");
                break;
            case 2002:
//                mPhotoAuthTijiao.setText("提交审核");
                break;
        }

        tvNormalTitleTitle.setText("自拍认证");
        ivNormalTitleMore.setVisibility(View.INVISIBLE);
        ivNormalTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (isOpenPhoto.equals("0")) {
            mPhotoAuthBtn01.setSelected(true);
            mPhotoAuthBtn02.setSelected(false);
            mPhotoAuthBtn01.setClickable(false);
        } else {
            mPhotoAuthBtn01.setSelected(false);
            mPhotoAuthBtn02.setSelected(true);
            mPhotoAuthBtn02.setClickable(false);
        }
    }

    @OnClick({R.id.mPhoto_auth_pic, R.id.mPhoto_auth_tijiao, R.id.mPhoto_auth_btn01, R.id.mPhoto_auth_btn02})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mPhoto_auth_pic:
                showSelectPic();
                break;
            case R.id.mPhoto_auth_tijiao:
                mPhotoAuthTijiao.setClickable(false);
                if (!headurl.equals("")) {
                    if (retcode == 2002) {
                        tijiaoAuth();
                    } else if (retcode == 2000) {
                        //重新认证
                        replaceidcard();
                    }
                } else {
                    ToastUtil.show(getApplicationContext(), "请上传认证照片...");
                    mPhotoAuthTijiao.setClickable(true);
                }
                break;
            case R.id.mPhoto_auth_btn01:
                if (retcode == 2002) {
                    ToastUtil.show(getApplicationContext(), "认证通过后才可设置权限哦~");
                } else {
                    mPhotoAuthBtn01.setSelected(true);
                    mPhotoAuthBtn02.setSelected(false);
                    mPhotoAuthBtn01.setClickable(false);
                    mPhotoAuthBtn02.setClickable(true);
                    setRealnameState("nobody");
                }
                break;
            case R.id.mPhoto_auth_btn02:
                if (retcode == 2002) {
                    ToastUtil.show(getApplicationContext(), "认证通过后才可设置权限哦~");
                } else {
                    mPhotoAuthBtn01.setSelected(false);
                    mPhotoAuthBtn02.setSelected(true);
                    mPhotoAuthBtn01.setClickable(true);
                    mPhotoAuthBtn02.setClickable(false);
                    setRealnameState("vip");
                }
                break;
        }
    }


    private void setRealnameState(String status) {
        Map<String, String> map = new HashMap<>();
        map.put("status", status);
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetRealnameState, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("setrealnamestate", "onSuccess: " + response);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }



    private void tijiaoAuth() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("card_face", headurl);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Setidcard, map, new IRequestCallback() {
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
                                    EventBus.getDefault().post("uploadSuccess");
                                    EventBus.getDefault().post(new PhotoRzEvent());
                                    finish();
                                    break;
                                case 4001:
                                case 4003:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                            }
                            mPhotoAuthTijiao.setClickable(true);
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

    private void replaceidcard() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("card_replace", headurl);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Replaceidcard, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("photoresume", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    EventBus.getDefault().post("uploadSuccess");
                                    EventBus.getDefault().post(new PhotoRzEvent());
                                    finish();
                                    break;
                                case 3000:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                            }
                            mPhotoAuthTijiao.setClickable(true);
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


    private void showSelectPic() {
        new AlertView(null, null, "取消", null,
                new String[]{"拍照"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    @Override
    public void onItemClick(Object o, int position, String data) {
        switch (position) {
            case 0:
                takePhone(mPhotoAuthPic);
                break;
//            case 1:
//                choosePhone(mPhotoAuthPic);
//                break;
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
            takePhoto();
        }

    }
//    //相册
//    public void choosePhone(View view){
//        String[] perms = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
//            //第二个参数是被拒绝后再次申请该权限的解释
//            //第三个参数是请求码
//            //第四个参数是要申请的权限
//            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", MY_PERMISSIONS_REQUEST_CALL_PHONE2, perms);
//        }
//        else {
//            choosePhoto();
//        }
//    }

    /**
     * 拍照
     */
    void takePhoto() {
        cropImage = new File(Environment.getExternalStorageDirectory(), "/" + UUID.randomUUID() + ".jpg");
        cropUri = Uri.fromFile(cropImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1); // 调用前置摄像头
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册选取图片
     */
    void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
//                if (data== null) {
//                    return;
//                }
//                Uri crUri=data.getData();
//                startCrop(crUri);
                break;
            case CAMERA_REQUEST_CODE:
//                if (data.getData() == null||data.getExtras()==null) {
//                    return;
//                }
//                cropImage = new File(Environment.getExternalStorageDirectory(), "/temp.jpg");
//                cropUri = Uri.fromFile(cropImage);
                CropUtils.startUCrop(this, cropUri, CROP_REQUEST_CODE, 1, 1, 700, 700);
//                startCrop(cropUri);
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
                        //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
                        mPhotoAuthPic.setImageBitmap(photo); //把图片显示在ImageView控件上
                    }
                    ToastUtil.show(getApplicationContext(), "认证照片上传中,请稍后...");
//                     获得字节流
                    ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
                    PhotoUploadTask put = new PhotoUploadTask(
                            HttpUrl.NetPic() + "Api/Api/fileUpload", is,
                            this, handler);
                    put.start();
//                    Map<String,Object> map=new HashMap<>();
//                    try {
//                        map.put("file",new File(new URI(resultUri.toString())));
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                    OkhttpUpload okhttpload=new OkhttpUpload(this);
//                    okhttpload.uploadImgAndParameter(map,"http://59.110.28.150:888/Api/Api/fileUpload");
                } else {
                    return;
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
            ToastUtil.show(PhotoAuthenticationActivity.this, "授权失败,请开启相机权限");
            return;
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(PhotoAuthenticationActivity.this, "授权失败,请开启读写权限");
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void selectItem(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.purple_main));
        textView.setBackgroundResource(R.drawable.bg_vip_center_item_choose);
    }

    public void unSelectItem(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.lightGray));
        textView.setBackgroundResource(R.drawable.bg_vip_center_item_normal);
    }

}
