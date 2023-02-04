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
import android.widget.EditText;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.bean.RedWomenStep2Data;
import com.aiwujie.shengmo.eventbus.RedWomenMatchStateEvent;
import com.aiwujie.shengmo.eventbus.RedwomenPhotoEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.CropUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yalantis.ucrop.UCrop;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class RedWomenEditMsgActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, OnItemClickListener {

    @BindView(R.id.mRedwomen_edit_msg_return)
    ImageView mRedwomenEditMsgReturn;
    @BindView(R.id.mRedwomen_edit_msg_iv01)
    ImageView mRedwomenEditMsgIv01;
    @BindView(R.id.mRedwomen_edit_msg_iv02)
    ImageView mRedwomenEditMsgIv02;
    @BindView(R.id.mRedwomen_edit_msg_iv03)
    ImageView mRedwomenEditMsgIv03;
    @BindView(R.id.mRedwomen_edit_msg_iv04)
    ImageView mRedwomenEditMsgIv04;
    @BindView(R.id.mRedwomen_edit_msg_iv05)
    ImageView mRedwomenEditMsgIv05;
    @BindView(R.id.mRedwomen_edit_msg_iv06)
    ImageView mRedwomenEditMsgIv06;
    @BindView(R.id.mRedwomen_edit_msg_et)
    EditText mRedwomenEditMsgEt;
    @BindView(R.id.mRedwomen_edit_msg_tj)
    ImageView mRedwomenEditMsgTj;
    @BindView(R.id.mFragmentmRedwomen_edit_msg_switch_01)
    ImageView mFragmentmRedwomenEditMsgSwitch01;
    @BindView(R.id.mFragment_mRedwomen_edit_msg_switch_ll01)
    PercentLinearLayout mFragmentMRedwomenEditMsgSwitchLl01;
    @BindView(R.id.mFragmentmRedwomen_edit_msg_switch_02)
    ImageView mFragmentmRedwomenEditMsgSwitch02;
    @BindView(R.id.mFragmentmRedwomen_edit_msg_switch_ll02)
    PercentLinearLayout mFragmentmRedwomenEditMsgSwitchLl02;
    @BindView(R.id.mFragmentmRedwomen_edit_msg_switch_03)
    ImageView mFragmentmRedwomenEditMsgSwitch03;
    @BindView(R.id.mFragmentmRedwomen_edit_msg_switch_ll03)
    PercentLinearLayout mFragmentmRedwomenEditMsgSwitchLl03;
    @BindView(R.id.mRedwomen_edit_msg_switch_ll)
    PercentLinearLayout mRedwomenEditMsgSwitchLl;
    private int picFlag;
    private Uri cropUri;
    private File cropImage;
    //删除图片集合
    private List<String> deleteUrl = new ArrayList<>();
    //图片地址集合
    private List<String> picList = new ArrayList<>();
    //imageview集合
    private List<ImageView> imges;
    //查看大图集合
    ArrayList<String> showPics;
    private List<ImageView> imageViews;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private String uid;
    private String admin;
    //区分是不是第一次修改资料
    private int editFlag;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 152:
                    String s = (String) msg.obj;
                    Log.i("icon", s);
                    BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
//                    Log.i("picurl", "handleMessage: " + beanicon.getData());
                    if ((picFlag - 1) < picList.size()) {
                        String removePic = picList.remove(picFlag - 1);
                        deleteUrl.add(removePic);
                        picList.add(picFlag - 1, beanicon.getData());
                    } else {
                        picList.add(beanicon.getData());
                    }
                    ToastUtil.show(getApplicationContext(), "上传完成");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_women_edit_msg);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        ButterKnife.bind(this);
        setData();
        getStep2Info();
    }

    private void setData() {
        imges = new ArrayList<>();
        imges.add(mRedwomenEditMsgIv01);
        imges.add(mRedwomenEditMsgIv02);
        imges.add(mRedwomenEditMsgIv03);
        imges.add(mRedwomenEditMsgIv04);
        imges.add(mRedwomenEditMsgIv05);
        imges.add(mRedwomenEditMsgIv06);
        imageViews=new ArrayList<>();
        imageViews.add(mFragmentmRedwomenEditMsgSwitch01);
        imageViews.add(mFragmentmRedwomenEditMsgSwitch02);
        imageViews.add(mFragmentmRedwomenEditMsgSwitch03);
        uid = getIntent().getStringExtra("uid");
        editFlag = getIntent().getIntExtra("editFlag", -1);
        admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
    }

    @OnClick({R.id.mRedwomen_edit_msg_return, R.id.mRedwomen_edit_msg_iv01, R.id.mRedwomen_edit_msg_iv02, R.id.mRedwomen_edit_msg_iv03, R.id.mRedwomen_edit_msg_iv04, R.id.mRedwomen_edit_msg_iv05, R.id.mRedwomen_edit_msg_iv06, R.id.mRedwomen_edit_msg_tj,R.id.mFragment_mRedwomen_edit_msg_switch_ll01, R.id.mFragmentmRedwomen_edit_msg_switch_ll02, R.id.mFragmentmRedwomen_edit_msg_switch_ll03})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mRedwomen_edit_msg_return:
                finish();
                break;
            case R.id.mRedwomen_edit_msg_iv01:
                picFlag = 1;
                addOrDeletePic();
                break;
            case R.id.mRedwomen_edit_msg_iv02:
                picFlag = 2;
                addOrDeletePic();
                break;
            case R.id.mRedwomen_edit_msg_iv03:
                picFlag = 3;
                addOrDeletePic();
                break;
            case R.id.mRedwomen_edit_msg_iv04:
                picFlag = 4;
                addOrDeletePic();
                break;
            case R.id.mRedwomen_edit_msg_iv05:
                picFlag = 5;
                addOrDeletePic();
                break;
            case R.id.mRedwomen_edit_msg_iv06:
                picFlag = 6;
                addOrDeletePic();
                break;
            case R.id.mRedwomen_edit_msg_tj:
                editStep2Info();
                break;
            case R.id.mFragment_mRedwomen_edit_msg_switch_ll01:
                setImageViewsSelect(0);
                setPhotoLock("0");
                break;
            case R.id.mFragmentmRedwomen_edit_msg_switch_ll02:
                setImageViewsSelect(1);
                setPhotoLock("1");
                break;
            case R.id.mFragmentmRedwomen_edit_msg_switch_ll03:
                setImageViewsSelect(2);
                setPhotoLock("2");
                break;

        }

    }

    private void setPhotoLock(final String method) {
        Map<String, String> map = new HashMap<>();
        map.put("method", method);
        map.put("uid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetPhotoLock, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject obj=new JSONObject(response);
                    switch (obj.getInt("retcode")){
                        case 2000:
                            EventBus.getDefault().post(new RedwomenPhotoEvent(Integer.parseInt(method)));
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

    private void addOrDeletePic() {
        if (picList.size() >= picFlag) {
            showDeletePic();
        } else {
            showSelectPic();
        }
    }

    private void editStep2Info() {
        String picString = "";
        if (picList.size() != 0) {
            for (int i = 0; i < picList.size(); i++) {
                picString += picList.get(i) + ",";
            }
            picString = picString.substring(0, picString.length() - 1);
        }
        if (picString.contains(HttpUrl.NetPic() )) {
            picString = picString.replaceAll(HttpUrl.NetPic() , "");
        }
        String httpurl;
        Map<String, String> map = new HashMap<>();
        if (admin.equals("1")) {
            map.put("login_uid", MyApp.uid);
            httpurl = HttpUrl.AdminEditStep2Info;
        } else {
            if (editFlag == 1) {
                httpurl = HttpUrl.EditStep2Info;
            } else {
                httpurl = HttpUrl.UpdateStep2Info;
            }
        }
        map.put("uid", uid);
        map.put("match_introduce", mRedwomenEditMsgEt.getText().toString());
        map.put("match_photo", picString);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(httpurl, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    EventBus.getDefault().post(new RedWomenMatchStateEvent());
                                    finish();
                                    break;
                                case 3000:
                                case 3001:
                                case 3002:
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

            }
        });
    }

    private void getStep2Info() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("loginuid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetStep2Info, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RedWomenStep2Data data = new Gson().fromJson(response, RedWomenStep2Data.class);
                            RedWomenStep2Data.DataBean datas = data.getData();
                            picList = datas.getMatch_photo();
                            try {
                                for (int i = 0; i < picList.size(); i++) {
                                    GlideImgManager.glideLoader(RedWomenEditMsgActivity.this, datas.getMatch_photo().get(i), R.mipmap.default_error, R.mipmap.default_error, imges.get(i));
                                }
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                            //设置开关
                            setImageViewsSelect(Integer.parseInt(datas.getMatch_photo_lock()));
                            mRedwomenEditMsgEt.setText(datas.getMatch_introduce());
                            mRedwomenEditMsgEt.setSelection(datas.getMatch_introduce().length());
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

    private void setImageViewsSelect(int currentFlag){
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setImageResource(R.mipmap.pursewxz);
        }
        imageViews.get(currentFlag).setImageResource(R.mipmap.pursexz);
    }

    private void showSelectPic() {
        new AlertView(null, null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void showDeletePic() {
        new AlertView(null, null, "取消", null,
                new String[]{"拍照", "从相册中选择", "查看大图", "删除"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    //拍照
    public void takePhone() {
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

    //相册
    public void choosePhone() {
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
    public void onItemClick(Object o, int position,String data) {
        switch (position) {
            case 0:
                takePhone();
                break;
            case 1:
                choosePhone();
                break;
            case 2:
                showPics = new ArrayList<>();
                for (int i = 0; i < picList.size(); i++) {
                    showPics.add(picList.get(i));
                }
                Intent intent = new Intent(this, ZoomActivity.class);
                intent.putStringArrayListExtra("pics", showPics);
                intent.putExtra("position", picFlag - 1);
                startActivity(intent);
                break;
            case 3:
                //删除图片
                String removePic = picList.remove(picFlag - 1);
                deletePic(removePic);
                break;
        }
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
                            if (picList.get(i).contains(HttpUrl.NetPic() )) {
                                GlideImgManager.glideLoader(RedWomenEditMsgActivity.this, picList.get(i), R.mipmap.default_error, R.mipmap.default_error, imges.get(i));
                            } else {
                                GlideImgManager.glideLoader(RedWomenEditMsgActivity.this, HttpUrl.NetPic()  + picList.get(i), R.mipmap.default_error, R.mipmap.default_error, imges.get(i));
                            }
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
                        //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
                        switch (picFlag) {
                            case 1:
                                mRedwomenEditMsgIv01.setImageBitmap(photo);
                                break;
                            case 2:
                                mRedwomenEditMsgIv02.setImageBitmap(photo);
                                break;
                            case 3:
                                mRedwomenEditMsgIv03.setImageBitmap(photo);
                                break;
                            case 4:
                                mRedwomenEditMsgIv04.setImageBitmap(photo);
                                break;
                            case 5:
                                mRedwomenEditMsgIv05.setImageBitmap(photo);
                                break;
                            case 6:
                                mRedwomenEditMsgIv06.setImageBitmap(photo);
                                break;
                        }
                        ToastUtil.show(getApplicationContext(), "图片上传中,请稍后提交...");
                    }
                    // 获得字节流
                    ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
                    PhotoUploadTask put = new PhotoUploadTask(
//                            "http://59.110.28.150:888/Api/Api/filePhoto"
                            NetPic()+"Api/Api/filePhoto"
                            , is,
                            this, handler);
                    put.start();
                }
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
            if(!perms.contains(Manifest.permission.CAMERA)) {
                return;
            }
            if(!perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }
            takePhoto();
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            if(!perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }
            choosePhoto();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if(perms.contains(Manifest.permission.CAMERA)) {
            ToastUtil.show(getApplicationContext(),"授权失败,请开启相机权限");
            return;
        }
        if(perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(),"授权失败,请开启读写权限");
            return;
        }
    }

}
