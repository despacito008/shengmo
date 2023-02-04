package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.kt.ui.activity.MapActivity;
import com.aiwujie.shengmo.utils.CropUtils;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class CreateGroupActivity extends AppCompatActivity implements OnItemClickListener, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.mCreate_group_return)
    ImageView mCreateGroupReturn;
    @BindView(R.id.mCreate_group_next)
    TextView mCreateGroupNext;
    @BindView(R.id.mCreate_group_name)
    EditText mCreateGroupName;
    @BindView(R.id.mCreate_group_introduce)
    EditText mCreateGroupIntroduce;
    @BindView(R.id.mCreate_group_textcount)
    TextView mCreateGroupTextcount;
    public static CreateGroupActivity instance;
    @BindView(R.id.mCreate_group_icon)
    ImageView mCreateGroupIcon;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private String headurl = "";
    private Uri cropUri;
    private File cropImage;
    private String[] minganText = {"奴"};
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 152:
                    String s = (String) msg.obj;
                    Log.i("icon", s);
                    BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                    headurl = beanicon.getData();
                    ToastUtil.show(getApplicationContext(), "头像上传成功");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        instance = this;
        mCreateGroupIntroduce.addTextChangedListener(new EditTitleChangedListener());
    }

    @OnClick({R.id.mCreate_group_return, R.id.mCreate_group_next, R.id.mCreate_group_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mCreate_group_return:
                finish();
                break;
            case R.id.mCreate_group_next:
                createNext();
                break;
            case R.id.mCreate_group_icon:
                showSelectPic();
                break;
        }
    }

    private void createNext() {
        String groupName = mCreateGroupName.getText().toString().trim();
        String groupIntroduce = mCreateGroupIntroduce.getText().toString();
        if (!groupName.equals("") && !groupName.equals("") && !headurl.equals("")) {
            //判断昵称中是否包含空格
            if (groupName.indexOf(" ") == -1) {

                for (int i = 0; i < minganText.length; i++) {
                    if (groupName.indexOf(minganText[i]) != -1) {
                        ToastUtil.show(getApplicationContext(), "很抱歉，您的昵称包含有禁止使用的字，请重试~");
                        return;
                    }
                    if (groupIntroduce.indexOf(minganText[i]) != -1) {
                        ToastUtil.show(getApplicationContext(), "很抱歉，您的群介绍包含有禁止使用的字，请重试~");
                        return;
                    }
                }
//                Intent intent = new Intent(this, MapActivity.class);
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("mapflag", "2");
                intent.putExtra("headurl", SharedPreferencesUtils.getParam(getApplicationContext(),"image_host","") +headurl);
                intent.putExtra("groupname", mCreateGroupName.getText().toString().trim());
                intent.putExtra("introduce", mCreateGroupIntroduce.getText().toString().trim());
                startActivity(intent);
            } else {
                ToastUtil.show(getApplicationContext(), "群昵称中不能有空格...");
            }
        } else {
            ToastUtil.show(getApplicationContext(), "请输入完整群信息");
        }
    }

    private void showSelectPic() {
        new AlertView(null, null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                this, AlertView.Style.ActionSheet, this).show();
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

    //相册
    public void choosePhone(View view) {
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
    public void onItemClick(Object o, int position, String data) {
        switch (position) {
            case 0:
                takePhone(mCreateGroupIcon);
                break;
            case 1:
                choosePhone(mCreateGroupIcon);
                break;
        }
    }

    class EditTitleChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mCreateGroupTextcount.setText("(" + s.length() + "/256)");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() == 256) {
                ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                Log.i("registonepageactivity", "onActivityResult: " + data);
                if (data == null) {
                    return;
                }
                Uri crUri = data.getData();
                CropUtils.startUCrop(this, crUri, CROP_REQUEST_CODE, 1, 1, 750, 750);
//                startCrop(crUri);
                break;
            case CAMERA_REQUEST_CODE:
//                startCrop(cropUri);
                CropUtils.startUCrop(this, cropUri, CROP_REQUEST_CODE, 1, 1, 750, 750);
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
                        photo.compress(Bitmap.CompressFormat.JPEG, 80, stream);// (0-100)压缩文件
                        //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
//                        mRgistOnePageIcon.setImageBitmap(photo); //把图片显示在ImageView控件上
                        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
                        circleDrawable.getPaint().setAntiAlias(true);
                        circleDrawable.setCircular(true);
                        mCreateGroupIcon.setImageDrawable(circleDrawable);
                        ToastUtil.show(getApplicationContext(), "头像上传中,请稍后...");
                    }
                    // 获得字节流
                    ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
                    PhotoUploadTask put = new PhotoUploadTask(
                            NetPic() + "Api/Api/fileUpload"//"http://59.110.28.150:888/Api/Api/fileUpload
                            , is,
                            this, handler);
                    put.start();
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
            ToastUtil.show(getApplicationContext(),"授权失败,请开启相机权限");
            return;
        }
        if(perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(),"授权失败,请开启读写权限");
            return;
        }
    }

}
