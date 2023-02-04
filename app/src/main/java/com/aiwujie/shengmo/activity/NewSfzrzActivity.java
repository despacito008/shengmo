package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.bean.Sfzrz_bean;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.util.NormalUtilKt;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.BitmapUtils;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wildma.idcardcamera.camera.IDCardCamera;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class NewSfzrzActivity extends AppCompatActivity implements OnItemClickListener, EasyPermissions.PermissionCallbacks {

    String headurl = "";
    String req = "0";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 152:
                    String s = (String) msg.obj;
                    Log.i("icon", s);
                    BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                    headurl = beanicon.getData();
                    if (req.equals("1")) {
                        card_z = headurl;
                    } else if (req.equals("2")) {
                        card_f = headurl;
                    } else if (req.equals("3")) {
                        card_hand = headurl;
                        Glide.with(NewSfzrzActivity.this).load(imgpre + headurl).into(freSfz1);
                    }
                    ToastUtil.show(getApplicationContext(), "上传完成");
                    break;
            }
            super.handleMessage(msg);
        }
    };
    int ALBUM_REQUEST_CODE = 0;
    String card_z = "";
    String card_f = "";
    String card_hand = "";
    @BindView(R.id.iv_normal_title_back)
    ImageView ivNormalTitleBack;
    @BindView(R.id.tv_normal_title_title)
    TextView tvNormalTitleTitle;
    @BindView(R.id.iv_normal_title_more)
    ImageView ivNormalTitleMore;
    @BindView(R.id.tv_normal_title_more)
    TextView tvNormalTitleMore;
    @BindView(R.id.layout_normal_titlebar)
    LinearLayout layoutNormalTitlebar;
    @BindView(R.id.et_zsxm)
    EditText etZsxm;
    @BindView(R.id.et_sfzh)
    EditText etSfzh;
    @BindView(R.id.fre_sfz_1)
    ImageView freSfz1;
    @BindView(R.id.tv_go_to_old_auth)
    TextView tvGoToOldAuth;
    @BindView(R.id.msfz_tijiao)
    TextView msfzTijiao;
    private String real_name = "";
    private String ids = "";
    private String state;
    private String imgpre;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            if (requestCode == 3) {
               // Bitmap bm = BitmapUtils.ResizeBitmap(PhotoUploadUtils.decodeUriAsBitmap(imageUri, this),960);
                Bitmap bm = BitmapUtils.ResizeBitmap(BitmapUtils.orientation(mTempPhotoPath), 960);
                if (bm == null) {
                    return;
                }
                //freSfz3.setImageBitmap(bm);
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bm, "temp_image_" + System.currentTimeMillis(), null));
                addchuan(uri);
            } else {

            }
            return;
        }

        if (resultCode == IDCardCamera.RESULT_CODE) {
            //获取图片路径，显示图片
            final String path = IDCardCamera.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) { //身份证正面
                    //mIvFront.setImageBitmap(BitmapFactory.decodeFile(path));
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    freSfz1.setImageBitmap(BitmapFactory.decodeFile(path));
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                    addchuan(uri);
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面
                    //mIvBack.setImageBitmap(BitmapFactory.decodeFile(path));
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    //freSfz2.setImageBitmap(bitmap);
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                    addchuan(uri);
                }
            }
        }

        if (requestCode == 3) {
            Bitmap bm = BitmapUtils.ResizeBitmap(PhotoUploadUtils.decodeUriAsBitmap(imageUri, this), 960);
            if (bm == null) {
                return;
            }
            //freSfz3.setImageBitmap(bm);
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bm, null, null));
            addchuan(uri);
//            if (bm == null) {
//                return;
//            }
//            freSfz3.setImageURI(imageUri);
//            addchuan(imageUri);
//            if (data.getExtras() == null) {
//                return;
//            }
//            Bundle bundle = data.getExtras();
//            Bitmap bitmap = bundle.getParcelable("data");
//            freSfz3.setImageBitmap(bitmap);
//            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
//            addchuan(uri);
        }
        if (data.getData() == null) {
            return;
        }
        if (requestCode == 4) {
            Uri uri = data.getData();
            freSfz1.setImageURI(uri);
            addchuan(uri);
        } else if (requestCode == 5) {
            Uri uri = data.getData();
           // freSfz2.setImageURI(uri);
            addchuan(uri);
        } else if (requestCode == 6) {
            Uri uri = data.getData();
           // freSfz3.setImageURI(uri);
            addchuan(uri);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card_auth_new);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        Intent intent = getIntent();
        state = intent.getStringExtra("state");
        imgpre = SharedPreferencesUtils.geParam(this, "image_host", "");
//        findViewById(R.id.msmrzCenter_return).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        getrealidstate();
        msfzTijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                real_name = etZsxm.getText().toString().trim();
                ids = etSfzh.getText().toString().trim();
                if (real_name.equals("") || real_name == null) {
                    ToastUtil.show(NewSfzrzActivity.this, "请填写真实姓名");
                    return;
                }
                if (ids.equals("") || ids == null) {
                    ToastUtil.show(NewSfzrzActivity.this, "请填写身份证号");
                    return;
                }
////                if (TextUtil.isEmpty(card_z)) {
////                    ToastUtil.show(NewSfzrzActivity.this, "未检测到身份证正面照，请点击重新上传");
////                    return;
////                }
////                if (TextUtil.isEmpty(card_f)) {
////                    ToastUtil.show(NewSfzrzActivity.this, "未检测到身份证反面照，请点击重新上传");
////                    return;
////                }
                if (TextUtil.isEmpty(card_hand)) {
                    ToastUtil.show(NewSfzrzActivity.this, "未检测到自拍照，请点击重新上传");
                    return;
                }
//                if (card_z.equals("") || card_f.equals("") || card_hand.equals("")) {
//                    ToastUtil.show(SfzrzActivity.this,"请上传身份证照片");
//                    return;
//                }
              //  sendredbao();
                doNewAuth();
            }
        });


        freSfz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req = "3";
                ALBUM_REQUEST_CODE = 6;
                showSelectPic();
            }
        });
//        freSfz2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                req = "2";
//                ALBUM_REQUEST_CODE = 5;
//                showSelectPic();
//            }
//        });
//        freSfz3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                req = "3";
//                ALBUM_REQUEST_CODE = 6;
//                showSelectPic();
//
//            }
//        });

        tvNormalTitleTitle.setText("身份认证");
        ivNormalTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivNormalTitleMore.setVisibility(View.INVISIBLE);


        tvGoToOldAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewSfzrzActivity.this,SfzrzActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showSelectPic() {
        new AlertView(null, null, "取消", null,
                new String[]{"拍照"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    Uri imageUri;
    String mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "temp_photo.jpeg";

    public void takePhoto() {
        // 跳转到系统的拍照界面
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定照片存储位置为sd卡本目录下
        // 这里设置为固定名字 这样就只会只有一张temp图 如果要所有中间图片都保存可以通过时间或者加其他东西设置图片的名称
        // File.separator为系统自带的分隔符 是一个固定的常量
        // 获取图片所在位置的Uri路径    *****这里为什么这么做参考问题2*****
        File tempFile = new File(mTempPhotoPath);
        if (tempFile.exists()) {
            tempFile.delete();
        }
        try{
            tempFile.createNewFile();
        } catch (IOException e) { }
        if (Build.VERSION.SDK_INT>=24) {
                    imageUri = FileProvider.getUriForFile(NewSfzrzActivity.this,
                            NewSfzrzActivity.this.getApplicationContext().getPackageName() +".fileProvider",
                tempFile);
        } else {
            imageUri = Uri.fromFile(tempFile);
        }

        //如果指定了MediaStore.EXTRA_OUTPUT，则所拍摄的图像将写入该路径，并且不会向onActivityResult提供任何数据。您可以从您指定的内容中读取图像。
//        //下面这句指定调用相机拍照后的照片存储的路径
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, 3);
    }

    public void addchuan(Uri crUri) {
        //Uri crUri = data.getData();
        //CropUtils.startUCrop(this, crUri, 4, 1, 1, 700, 700);
        ToastUtil.show(NewSfzrzActivity.this,"正在上传中，请稍候");
        if (crUri != null) {
            Bitmap photo = PhotoUploadUtils.decodeUriAsBitmap(crUri, this);
            LogUtil.d(photo.getByteCount());
            LogUtil.d(photo.getWidth());
            LogUtil.d(photo.getByteCount());
//                    Bitmap photo = extras.getParcelable("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    Bitmap bitmap=comp(photo);
            if (photo != null) {
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);// (0-100)压缩文件
                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(this.getResources(), photo);
                circleDrawable.getPaint().setAntiAlias(true);
                circleDrawable.setCircular(true);
            }
            // 获得字节流
            ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
            PhotoUploadTask put = new PhotoUploadTask(
                    NetPic() + "Api/Api/fileUpload"  //"http://59.110.28.150:888/Api/Api/fileUpload"
                    , is,
                    this, handler);
            put.start();
        }
    }

    @Override
    public void onItemClick(Object o, int position, String data) {
        switch (position) {
            case 0:
                if (req.equals("1")) {
                    IDCardCamera.create(NewSfzrzActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                } else if (req.equals("2")) {
                    IDCardCamera.create(NewSfzrzActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_BACK);
                } else if (req.equals("3")) {
                    //第二个参数是需要申请的权限
                    if (ContextCompat.checkSelfPermission(NewSfzrzActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(NewSfzrzActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                3);
                    } else { //权限已经被授予，在这里直接写要执行的相应方法即可
                        takePhoto();
                    }
                }
                break;
            case 1:
                choosePhone();
                break;
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

//    /**
//     * 从相册选取图片
//     */
//    void choosePhoto() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(intent, ALBUM_REQUEST_CODE);
//    }

    private static final String IMAGE_UNSPECIFIED = "image/*";

    /**
     * 从相册选取图片
     */
    void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    //身份证认证
    public void sendredbao() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("card_z", card_z);
        map.put("card_f", card_f);
        map.put("card_hand", card_hand);
        map.put("real_name", real_name);
        map.put("ids", ids);
        map.put("state", state);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.setrealidcard, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    final JSONObject object = new JSONObject(response);
                    final String msg = object.getString("msg");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            ToastUtil.show(NewSfzrzActivity.this, msg);
                        }
                    });
                    switch (object.getInt("retcode")) {
                        case 2000:

                            break;
                        case 2001:
                        case 3000:
                        case 3001:
                        case 4000:
                        case 4001:
                        case 4002:
                        case 4003:
                        case 4004:
                        case 4005:
                        case 4006:
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

    public void doNewAuth() {
        HttpHelper.getInstance().authNewIdCard(real_name, ids, card_hand, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data,String msg) {
                ToastUtil.show(NewSfzrzActivity.this,"认证成功");
                finish();
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(NewSfzrzActivity.this,msg);
                tvGoToOldAuth.setVisibility(View.VISIBLE);
            }
        });
    }

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;

    //身份证显示
    public void getrealidstate() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        final String imgpre = SharedPreferencesUtils.geParam(NewSfzrzActivity.this, "image_host", "");
        requestManager.post(HttpUrl.getrealidstate, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            Gson gson = new Gson();
                            Sfzrz_bean sfzrz_bean = gson.fromJson(response, Sfzrz_bean.class);
                            final Sfzrz_bean.DataBean data = sfzrz_bean.getData();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    etZsxm.setText("" + data.getReal_name());
                                    etSfzh.setText("" + data.getIds());
                                    //freSfz1.setImageURI(NetPic() + data.getCard_z());
                                    if (!NewSfzrzActivity.this.isFinishing()) {
                                        ImageLoader.loadImage(NewSfzrzActivity.this, imgpre + data.getCard_z(), freSfz1);
                                      //  ImageLoader.loadImage(NewSfzrzActivity.this, imgpre + data.getCard_z(), freSfz2);
                                     //   ImageLoader.loadImage(NewSfzrzActivity.this, imgpre + data.getCard_hand(), freSfz3);
                                    }
                                    card_z = data.getCard_z();
                                    card_f = data.getCard_f();
                                    card_hand = data.getCard_hand();
//                                    fre_sfz_2.setImageURI(NetPic() + data.getCard_f());
//                                    fre_sfz_3.setImageURI(NetPic() + data.getCard_hand());
                                    msfzTijiao.setText("重新认证");
                                }
                            });

                            break;
                        case 2001:
                            msfzTijiao.setText(getString(R.string.live_is_authing));
                        case 3000:
                        case 3001:
                        case 4000:
                        case 4001:
                        case 4002:
                        case 4003:
                        case 4004:
                        case 4005:
                        case 4006:
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
            ToastUtil.show(getApplicationContext(), "授权失败,请开启相机权限");
            return;
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(), "授权失败,请开启读写权限");
            return;
        }
    }
}
