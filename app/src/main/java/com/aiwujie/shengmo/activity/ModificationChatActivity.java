package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.CropUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
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

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class ModificationChatActivity extends AppCompatActivity implements OnItemClickListener, EasyPermissions.PermissionCallbacks{

    @BindView(R.id.mEditGroupInfo_return)
    ImageView mEditGroupInfoReturn;
    @BindView(R.id.editPerson_none)
    ImageView editPersonNone;
    @BindView(R.id.mEditGroupInfo_nickname)
    TextView mEditGroupInfoNickname;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    @BindView(R.id.mEditGroupInfo_icon)
    ImageView mEditGroupInfoIcon;
    private Uri cropUri;
    private File cropImage;
    private String headurl = "";
    private String groupId;
    private String name;
    private String groupicon;
    private Uri cropEndUri;
    private String oldname;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 152:
                    String s = (String) msg.obj;
                    Log.i("icon", s);
                    BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                    headurl = beanicon.getData();
                    ToastUtil.show(getApplicationContext(), "????????????");
                    deleteIcon(groupicon);
                    groupicon = NetPic() + headurl;
//                    RongIM.getInstance().refreshGroupInfoCache(new Group(groupId, mEditGroupInfoNickname.getText().toString().trim(), Uri.parse(HttpUrl.NetPic()()+ headurl)));
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_chat);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        oldname = getIntent().getStringExtra("name");
        mEditGroupInfoNickname.setText(oldname);
        groupId = getIntent().getStringExtra("chatRoomId");
        groupicon = getIntent().getStringExtra("chatroompic");
        if (groupicon.equals(NetPic())) {//"http://59.110.28.150:888/"
            mEditGroupInfoIcon.setImageResource(R.mipmap.qunmorentouxiang);
        } else {
            GlideImgManager.glideLoader(ModificationChatActivity.this, groupicon, R.mipmap.qunmorentouxiang, R.mipmap.qunmorentouxiang, mEditGroupInfoIcon, 0);
        }
    }

    @OnClick({R.id.mEditGroupInfo_return, R.id.mEditGroupInfo_icon, R.id.mEditGroupInfo_nickname})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mEditGroupInfo_return:
                finish();
                break;
            case R.id.mEditGroupInfo_icon:
                showSelectPic();
                break;
            case R.id.mEditGroupInfo_nickname:
                Intent intent = new Intent(this, EditChatroomNameActivity.class);
                intent.putExtra("name", oldname);
                startActivityForResult(intent, 100);
                break;
        }
    }

    private void deleteIcon(String removeIcon) {
        Map<String, String> map = new HashMap<>();
        map.put("filename", removeIcon);
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

    private void showSelectPic() {
        new AlertView(null, null, "??????", null,
                new String[]{"??????", "??????????????????"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    @Override
    public void onItemClick(Object o, int position,String data) {
        switch (position) {
            case 0:
                takePhone(mEditGroupInfoIcon);
                break;
            case 1:
                choosePhone(mEditGroupInfoIcon);
                break;
        }
    }

    //??????
    public void takePhone(View view) {
        String[] perms = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
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
    public void choosePhone(View view) {
        String[] perms = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
        Intent intent = new Intent(Intent.ACTION_PICK, null);
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
//                startCrop(crUri);
                CropUtils.startUCrop(this, crUri, CROP_REQUEST_CODE, 1, 1, 300, 300);
                break;
            case CAMERA_REQUEST_CODE:
//                if (data == null) {
//                    return;
//                }
//                cropImage = new File(Environment.getExternalStorageDirectory(), "/temp.jpg");
//                cropUri = Uri.fromFile(cropImage);
//                startCrop(cropUri);
                CropUtils.startUCrop(this, cropUri, CROP_REQUEST_CODE, 1, 1, 300, 300);
                break;
            case CROP_REQUEST_CODE:
                Uri resultUri = null;
                try {
                    resultUri = UCrop.getOutput(data);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
//                if (data == null) {
//                    return;
//                }
//                Uri extras = data.getData();
//                Bundle bundle=data.getExtras();
//                Log.i("fanhuiuri", "onActivityResult: "+extras);
//                if (extras != null||bundle!=null) {
                if (resultUri != null) {
                    Bitmap photo = PhotoUploadUtils.decodeUriAsBitmap(resultUri, this);
//                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    Bitmap bitmap=comp(photo);
                    if (photo != null) {
                        photo.compress(Bitmap.CompressFormat.JPEG, 80, stream);// (0-100)????????????
                        //???????????????Bitmap?????????sd????????????????????????http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
                        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
                        circleDrawable.getPaint().setAntiAlias(true);
                        circleDrawable.setCircular(true);
                        mEditGroupInfoIcon.setImageDrawable(circleDrawable); //??????????????????ImageView?????????
                        ToastUtil.show(getApplicationContext(), "?????????????????????,?????????...");
                    }
                    // ???????????????
                    ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
                    PhotoUploadTask put = new PhotoUploadTask(
//                            "http://59.110.28.150:888/Api/Api/fileUpload"
                            NetPic() + "Api/Api/fileUpload"

                            , is,
                            this, handler);
                    put.start();
                }
                break;
            case 100:
                try {
                    name = data.getStringExtra("name");
                    mEditGroupInfoNickname.setText(name);
                } catch (Exception e) {
                    e.printStackTrace();
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
            ToastUtil.show(getApplicationContext(),"????????????,?????????????????????");
            return;
        }
        if(perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(),"????????????,?????????????????????");
            return;
        }
    }

    private void ediheadurlInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("roomid",groupId);
        map.put("pic",headurl);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.editChatPic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                            EventBus.getDefault().post("liaotianshixinxishuaxin");
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

    private void edieditChatNameInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("roomid",groupId);
        map.put("name",mEditGroupInfoNickname.getText().toString().trim());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.editChatName, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    EventBus.getDefault().post("liaotianshixinxishuaxin");
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
    protected void onDestroy() {
        super.onDestroy();
        if (!headurl.equals("")) {
            ediheadurlInfo();
        }
        if (!oldname.equals(mEditGroupInfoNickname.getText().toString().trim())) {
            if (!mEditGroupInfoNickname.getText().toString().trim().equals("")){
                edieditChatNameInfo();
            }
        }
    }

}
