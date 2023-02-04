package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.CropUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMGroupManager;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatManagerKit;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.EditGroupInfo;
import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class EditGroupActivity extends AppCompatActivity implements OnItemClickListener, EasyPermissions.PermissionCallbacks {

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
                    ToastUtil.show(getApplicationContext(), "上传完成");
                   // deleteIcon(groupicon);
                   // groupicon = NetPic() + headurl;
                    changeGroupIcon(HttpUrl.getImagePath(headurl));
//                    RongIM.getInstance().refreshGroupInfoCache(new Group(groupId, mEditGroupInfoNickname.getText().toString().trim(), Uri.parse(HttpUrl.NetPic()+ headurl)));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public static void start(Context context,String groupId,String name,String icon) {
        Intent intent = new Intent(context,EditGroupActivity.class);
        intent.putExtra(IntentKey.GROUP_ID,groupId);
        intent.putExtra(IntentKey.NAME,name);
        intent.putExtra(IntentKey.GROUP_ID,icon);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(EditGroupActivity.this);
        oldname = getIntent().getStringExtra("name");
        mEditGroupInfoNickname.setText(oldname);
        groupId = getIntent().getStringExtra("groupId");
        groupicon = getIntent().getStringExtra("groupicon");
        if (groupicon.equals(NetPic())) {//"http://59.110.28.150:888/"
            mEditGroupInfoIcon.setImageResource(R.mipmap.qunmorentouxiang);
        } else {
            GlideImgManager.glideLoader(EditGroupActivity.this, groupicon, R.mipmap.qunmorentouxiang, R.mipmap.qunmorentouxiang, mEditGroupInfoIcon, 0);
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
                if (!"2".equals(getIntent().getStringExtra(IntentKey.GROUP_TYPE))) {
                    Intent intent = new Intent(this, EditGroupNameActivity.class);
                    intent.putExtra("name", oldname);
                    startActivityForResult(intent, 100);
                } else {
                    ToastUtil.show("该群名称暂时不能编辑");
                }
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
        new AlertView(null, null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
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

    //拍照
    public void takePhone(View view) {
        String[] perms = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
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
    public void choosePhone(View view) {
        String[] perms = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
                        photo.compress(Bitmap.CompressFormat.JPEG, 80, stream);// (0-100)压缩文件
                        //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
                        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
                        circleDrawable.getPaint().setAntiAlias(true);
                        circleDrawable.setCircular(true);
                        mEditGroupInfoIcon.setImageDrawable(circleDrawable); //把图片显示在ImageView控件上
                        ToastUtil.show(getApplicationContext(), "头像正在上传中,请稍后...");
                    }
                    // 获得字节流
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
                    if (!oldname.equals(name)) {
                        changeGroupName(name);
                    }
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
            ToastUtil.show(EditGroupActivity.this,"授权失败,请开启相机权限");
            return;
        }
        if(perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(EditGroupActivity.this,"授权失败,请开启读写权限");
            return;
        }
    }

    private void editGroupInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid", groupId);
        map.put("groupname", mEditGroupInfoNickname.getText().toString().trim());
        if (!headurl.equals("")) {
            map.put("group_pic", headurl);
        }
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.EditGroupInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    String refreshIcon;
                                    EventBus.getDefault().post("editgroupsuccess");
                                    if (!headurl.equals("")) {
                                        refreshIcon = NetPic() + headurl;
                                    } else {
                                        refreshIcon = groupicon;
                                    }
                                    //RongIM.getInstance().refreshGroupInfoCache(new Group(groupId, mEditGroupInfoNickname.getText().toString().trim(), Uri.parse(refreshIcon)));
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
//        if (!headurl.equals("")) {
//            //editGroupInfo();
//            changeGroupIcon(HttpUrl.getImagePath(headurl));
//        } else {
//            if (!oldname.equals(mEditGroupInfoNickname.getText().toString().trim())) {
//                editGroupInfo();
//            }
//        }
    }

    void changeGroupIcon(final String url) {
        V2TIMManager.getGroupManager().getGroupsInfo(Arrays.asList(groupId), new V2TIMValueCallback<List<V2TIMGroupInfoResult>>() {
            @Override
            public void onSuccess(List<V2TIMGroupInfoResult> v2TIMGroupInfoResults) {
                for (V2TIMGroupInfoResult v2TIMGroupInfoResult : v2TIMGroupInfoResults) {
                    if (v2TIMGroupInfoResult.getGroupInfo().getGroupID().equals(groupId)) {
                        V2TIMGroupInfo groupInfo = v2TIMGroupInfoResult.getGroupInfo();
                        groupInfo.setFaceUrl(url);
                        V2TIMManager.getGroupManager().setGroupInfo(groupInfo, new V2TIMCallback() {
                            @Override
                            public void onSuccess() {
                                ToastUtil.show(EditGroupActivity.this,"修改头像成功");
                            }

                            @Override
                            public void onError(int code, String desc) {
                                ToastUtil.show(EditGroupActivity.this,"修改失败 ：" + code + desc);
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(int code, String desc) {

            }
        });
    }

    void changeGroupName(final String name) {
        V2TIMManager.getGroupManager().getGroupsInfo(Arrays.asList(groupId), new V2TIMValueCallback<List<V2TIMGroupInfoResult>>() {
            @Override
            public void onSuccess(List<V2TIMGroupInfoResult> v2TIMGroupInfoResults) {
                for (V2TIMGroupInfoResult v2TIMGroupInfoResult : v2TIMGroupInfoResults) {
                    if (v2TIMGroupInfoResult.getGroupInfo().getGroupID().equals(groupId)) {
                        V2TIMGroupInfo groupInfo = v2TIMGroupInfoResult.getGroupInfo();
                        groupInfo.setGroupName(name);
                        V2TIMManager.getGroupManager().setGroupInfo(groupInfo, new V2TIMCallback() {
                            @Override
                            public void onSuccess() {
                                ToastUtil.show(EditGroupActivity.this,"修改群名称成功");
                            }

                            @Override
                            public void onError(int code, String desc) {
                                ToastUtil.show(EditGroupActivity.this,"修改失败 ：" + code + desc);
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(int code, String desc) {

            }
        });


    }
}
