package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.bean.Sfzrz_bean;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
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
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.wildma.idcardcamera.camera.IDCardCamera;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;
import static com.aiwujie.shengmo.http.HttpUrl.addClaimGroup;

public class SfzrzActivity extends AppCompatActivity implements OnItemClickListener,EasyPermissions.PermissionCallbacks {

    @BindView(R.id.iv_normal_title_back)
    ImageView ivNormalTitleBack;
    @BindView(R.id.tv_normal_title_title)
    TextView tvNormalTitleTitle;
    @BindView(R.id.iv_normal_title_more)
    ImageView ivNormalTitleMore;
    @BindView(R.id.et_zsxm)
    EditText etZsxm;
    @BindView(R.id.et_sfzh)
    EditText etSfzh;
    @BindView(R.id.fre_sfz_1)
    ImageView freSfz1;
    @BindView(R.id.fre_sfz_2)
    ImageView freSfz2;
    @BindView(R.id.fre_sfz_3)
    ImageView freSfz3;
    @BindView(R.id.msfz_tijiao)
    TextView msfzTijiao;
//    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
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
                    }
                    ToastUtil.show(getApplicationContext(), "????????????");
                    break;
            }
            super.handleMessage(msg);
        }
    };
    int ALBUM_REQUEST_CODE = 0;
    String card_z = "";
    String card_f = "";
    String card_hand = "";
    private String real_name = "";
    private String ids = "";
    private String state;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            if (requestCode == 3) {
                //Bitmap bm = BitmapUtils.ResizeBitmap(PhotoUploadUtils.decodeUriAsBitmap(imageUri, this),960);
                Bitmap bm = BitmapUtils.ResizeBitmap(BitmapUtils.orientation(mTempPhotoPath),960);
                if (bm == null) {
                    return;
                }
                freSfz3.setImageBitmap(bm);
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bm, null, null));
                addchuan(uri);
            } else {

            }
            return;
        }

        if (resultCode == IDCardCamera.RESULT_CODE) {
            //?????????????????????????????????
            final String path = IDCardCamera.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) { //???????????????
                    //mIvFront.setImageBitmap(BitmapFactory.decodeFile(path));
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    freSfz1.setImageBitmap(BitmapFactory.decodeFile(path));
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                    addchuan(uri);
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //???????????????
                    //mIvBack.setImageBitmap(BitmapFactory.decodeFile(path));
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    freSfz2.setImageBitmap(bitmap);
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                    addchuan(uri);
                }
            }
        }

        if (requestCode == 3) {
            Bitmap bm = BitmapUtils.ResizeBitmap(PhotoUploadUtils.decodeUriAsBitmap(imageUri, this),960);
            if (bm == null) {
                return;
            }
            freSfz3.setImageBitmap(bm);
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
            freSfz2.setImageURI(uri);
            addchuan(uri);
        } else if (requestCode == 6) {
            Uri uri = data.getData();
            freSfz3.setImageURI(uri);
            addchuan(uri);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card_auth);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        Intent intent = getIntent();
        state = intent.getStringExtra("state");
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
                    ToastUtil.show(SfzrzActivity.this,"?????????????????????");
                    return;
                }
                if (ids.equals("") || ids == null) {
                    ToastUtil.show(SfzrzActivity.this,"?????????????????????");
                    return;
                }
                if (TextUtil.isEmpty(card_z)) {
                    ToastUtil.show(SfzrzActivity.this,"??????????????????????????????????????????????????????");
                    return;
                }
                if (TextUtil.isEmpty(card_f)) {
                    ToastUtil.show(SfzrzActivity.this,"??????????????????????????????????????????????????????");
                    return;
                }
                if (TextUtil.isEmpty(card_hand)) {
                    ToastUtil.show(SfzrzActivity.this,"??????????????????????????????????????????????????????");
                    return;
                }
//                if (card_z.equals("") || card_f.equals("") || card_hand.equals("")) {
//                    ToastUtil.show(SfzrzActivity.this,"????????????????????????");
//                    return;
//                }
                sendredbao();
            }
        });


        freSfz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req = "1";
                ALBUM_REQUEST_CODE = 4;
                showSelectPic();
            }
        });
        freSfz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req = "2";
                ALBUM_REQUEST_CODE = 5;
                showSelectPic();
            }
        });
        freSfz3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req = "3";
                ALBUM_REQUEST_CODE = 6;
                showSelectPic();
            }
        });

        tvNormalTitleTitle.setText("????????????");
        ivNormalTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivNormalTitleMore.setVisibility(View.INVISIBLE);
    }

    private void showSelectPic() {
        new AlertView(null, null, "??????", null,
                new String[]{"??????", "??????????????????"},
                this, AlertView.Style.ActionSheet, this).show();
    }
    Uri imageUri;
    String mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "temp_photo.jpeg";
    public void takePhoto() {
        // ??????????????????????????????
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ???????????????????????????sd???????????????
        // ??????????????????????????? ???????????????????????????temp??? ????????????????????????????????????????????????????????????????????????????????????????????????
        // File.separator??????????????????????????? ????????????????????????
        // ???????????????????????????Uri??????    *****????????????????????????????????????2*****
        imageUri = Uri.fromFile(new File(mTempPhotoPath));
//        imageUri = FileProvider.getUriForFile(SfzrzActivity.this,
//                SfzrzActivity.this.getApplicationContext().getPackageName() +".my.provider",
//                new File(mTempPhotoPath));
        //???????????????MediaStore.EXTRA_OUTPUT????????????????????????????????????????????????????????????onActivityResult?????????????????????????????????????????????????????????????????????
//        //???????????????????????????????????????????????????????????????
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, 3);
    }

    public void addchuan(Uri crUri) {
        //Uri crUri = data.getData();
        //CropUtils.startUCrop(this, crUri, 4, 1, 1, 700, 700);
        if (crUri != null) {
            Bitmap photo = PhotoUploadUtils.decodeUriAsBitmap(crUri, this);
            LogUtil.d(photo.getByteCount());
            LogUtil.d(photo.getWidth());
            LogUtil.d(photo.getByteCount());
//                    Bitmap photo = extras.getParcelable("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    Bitmap bitmap=comp(photo);
            if (photo != null) {
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);// (0-100)????????????
                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(this.getResources(), photo);
                circleDrawable.getPaint().setAntiAlias(true);
                circleDrawable.setCircular(true);
            }
            // ???????????????
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
                    IDCardCamera.create(SfzrzActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                } else if (req.equals("2")) {
                    IDCardCamera.create(SfzrzActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_BACK);
                } else if (req.equals("3")) {
                    //???????????????????????????????????????
                    if (ContextCompat.checkSelfPermission(SfzrzActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SfzrzActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                3);
                    } else { //????????????????????????????????????????????????????????????????????????
                        takePhoto();
                    }
                }
                break;
            case 1:
                choosePhone();
                break;
        }
    }

    //??????
    public void choosePhone() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {//???????????????????????????
            //????????????????????????????????????????????????????????????
            //???????????????????????????
            //????????????????????????????????????
            EasyPermissions.requestPermissions(this, "??????????????????????????????:\n\n1.????????????????????????\n\n2.??????", MY_PERMISSIONS_REQUEST_CALL_PHONE2, perms);
        } else {
            choosePhoto();
        }
    }

//    /**
//     * ?????????????????????
//     */
//    void choosePhoto() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(intent, ALBUM_REQUEST_CODE);
//    }

    private static final String IMAGE_UNSPECIFIED = "image/*";
    /**
     * ?????????????????????
     */
    void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    //???????????????
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

                            ToastUtil.show(SfzrzActivity.this, msg);
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
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    //???????????????
    public void getrealidstate() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        final String imgpre = SharedPreferencesUtils.geParam(SfzrzActivity.this, "image_host", "");
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
                                    if (!SfzrzActivity.this.isFinishing()) {
                                        ImageLoader.loadImage(SfzrzActivity.this, imgpre + data.getCard_z(), freSfz1);
                                        ImageLoader.loadImage(SfzrzActivity.this, imgpre + data.getCard_z(), freSfz2);
                                        ImageLoader.loadImage(SfzrzActivity.this, imgpre + data.getCard_hand(), freSfz3);
                                    }
                                    card_z = data.getCard_z();
                                    card_f = data.getCard_f();
                                    card_hand = data.getCard_hand();
//                                    fre_sfz_2.setImageURI(NetPic() + data.getCard_f());
//                                    fre_sfz_3.setImageURI(NetPic() + data.getCard_hand());
                                    msfzTijiao.setText("????????????");
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
            ToastUtil.show(getApplicationContext(), "????????????,?????????????????????");
            return;
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(), "????????????,?????????????????????");
            return;
        }
    }
}
