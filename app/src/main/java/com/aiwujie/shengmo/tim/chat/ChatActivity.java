package com.aiwujie.shengmo.tim.chat;

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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.LoginActivity;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.eventbus.FinishConversationEvent;
import com.aiwujie.shengmo.tim.helper.MessageSendHelper;
import com.aiwujie.shengmo.tim.utils.Constants;
import com.aiwujie.shengmo.tim.utils.DemoLog;
import com.aiwujie.shengmo.utils.CropUtils;
import com.aiwujie.shengmo.utils.FinishActivityManager;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;

import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class ChatActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatFragment mChatFragment;
    private ChatInfo mChatInfo;

    public Bundle saveBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        chat(getIntent());
//        ImmersionBar.with(this)
//                //.statusBarColor(R.color.chatWrite)     //???????????????????????????????????????
//               // .statusBarDarkFont(true)
//                .init();  //????????????????????????????????????????????????init();
        saveBundle = savedInstanceState;
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        EventBus.getDefault().register(this);
        FinishActivityManager.getManager().addActivity(this);
    }

    public Bundle getSaveBundle() {
        return saveBundle;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        DemoLog.i(TAG, "onNewIntent");
        super.onNewIntent(intent);
        chat(intent);
    }

    @Override
    protected void onResume() {
        DemoLog.i(TAG, "onResume");
        super.onResume();
    }

    private void chat(Intent intent) {
        final Bundle bundle = intent.getExtras();
        DemoLog.i(TAG, "bundle: " + bundle + " intent: " + intent);
        if (bundle == null) {
            Uri uri = intent.getData();
            if (uri != null) {
                // ???????????????????????????oppo scheme url??????
                Set<String> set = uri.getQueryParameterNames();
                if (set != null) {
                    for (String key : set) {
                        String value = uri.getQueryParameter(key);
                        DemoLog.i(TAG, "oppo push scheme url key: " + key + " value: " + value);
                    }
                }
            }
            startSplashActivity();
        } else {

            // ????????????????????????????????????oppo??????????????????????????????????????????????????????ChatActivity??????????????????ext??????
            String ext = bundle.getString("ext");
            DemoLog.i(TAG, "huawei push custom data ext: " + ext);

            Set<String> set = bundle.keySet();
            if (set != null) {
                for (String key : set) {
                    String value = bundle.getString(key);
                    DemoLog.i(TAG, "oppo push custom data key: " + key + " value: " + value);
                }
            }

            // ??????????????????????????????
            mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
            if (mChatInfo == null) {
                startSplashActivity();
                return;
            }

            mChatFragment = new ChatFragment();
            mChatFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, mChatFragment).commitAllowingStateLoss();

//            HttpHelper.getInstance().getAllRedBagStatus(mChatInfo.getId(), new HttpListener() {
//                @Override
//                public void onSuccess(String data) {
//                    mChatFragment = new ChatFragment();
//                    bundle.putString("redData", data);
//                    mChatFragment.setArguments(bundle);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, mChatFragment).commitAllowingStateLoss();
//                }
//
//                @Override
//                public void onFail(String msg) {
//
//                }
//            });


        }
    }



    private void startSplashActivity() {
        Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private Uri cropUri;
    private File cropImage;

    //??????
    public void takePhone() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(this, perms)) {//???????????????????????????
            //????????????????????????????????????????????????????????????
            //???????????????????????????
            //????????????????????????????????????
            EasyPermissions.requestPermissions(this, "??????????????????????????????:\n\n1.????????????????????????\n\n2.??????", MY_PERMISSIONS_REQUEST_CALL_PHONE, perms);
        } else {
            takePhoto();
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


    /**
     * ??????
     */
    void takePhoto() {
        cropImage = new File(Environment.getExternalStorageDirectory(), "/" + UUID.randomUUID() + ".jpg");
        cropUri = Uri.fromFile(cropImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * ?????????????????????
     */
    void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
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
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)????????????
//                        switch (picFlag) {
//                            case 0:
//                                //??????
//                                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
//                                circleDrawable.getPaint().setAntiAlias(true);
//                                circleDrawable.setCircular(true);
//                                mEditPersonmsgIcon.setImageDrawable(circleDrawable); //??????????????????ImageView?????????
//                                break;
//                            case 10:
//                                //??????
//
//                                break;
//
//                        }
//                        ToastUtil.show(getApplicationContext(), "???????????????,???????????????...");
                    }
                    if (true) {
                        // ???????????????
                        ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
                        PhotoUploadTask put = new PhotoUploadTask(
                                NetPic() + "Api/Api/fileUpload"  //"http://59.110.28.150:888/Api/Api/fileUpload"
                                , is,
                                this, handler);
                        put.start();
                    } else {
                        // ???????????????
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
            ToastUtil.show(getApplicationContext(), "????????????,?????????????????????");
            return;
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(), "????????????,?????????????????????");
            return;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 152:
                    String s = (String) msg.obj;
                    Log.i("icon", s);
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int retcode = jsonObject.optInt("retcode");
                        if(retcode != 2000) {
                            ToastUtil.show(getApplicationContext(), "????????????????????????????????????");
                            return;
                        }
                        BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                        MessageSendHelper.getInstance().sendFlashMessage(SharedPreferencesUtils.geParam(ChatActivity.this,"image_host","-") + beanicon.getData());
                        //  startActivity(new Intent(ChatActivity.this,TempActivity.class));
                        ToastUtil.show(getApplicationContext(), "????????????");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(FinishConversationEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        FinishActivityManager.getManager().finishActivity();
    }
}
