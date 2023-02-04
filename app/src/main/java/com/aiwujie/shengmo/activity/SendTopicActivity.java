package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.customview.WordWrapView;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
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

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class SendTopicActivity extends AppCompatActivity implements OnItemClickListener, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.mTopicDetail_return)
    ImageView mTopicDetailReturn;
    @BindView(R.id.mSend_topic_send)
    TextView mSendTopicSend;
    @BindView(R.id.mSend_topic_etTitle)
    EditText mSendTopicEtTitle;
    @BindView(R.id.mSend_topic_guifan)
    TextView mSendTopicGuifan;
    @BindView(R.id.mSend_topic_textcount)
    TextView mSendTopicTextcount;
    @BindView(R.id.mSend_topic_addpic)
    ImageView mSendTopicAddpic;
    @BindView(R.id.mSend_topic_wordwrapview)
    WordWrapView mSendTopicWordwrapview;
    @BindView(R.id.mSendTopic_topic_sort_01)
    TextView mSendTopicTopicSort01;
    @BindView(R.id.mSendTopic_topic_sort_02)
    TextView mSendTopicTopicSort02;
    @BindView(R.id.mSendTopic_topic_sort_03)
    TextView mSendTopicTopicSort03;
    @BindView(R.id.mSendTopic_topic_sort_04)
    TextView mSendTopicTopicSort04;
    @BindView(R.id.mSendTopic_topic_sort_05)
    TextView mSendTopicTopicSort05;
    @BindView(R.id.mSendTopic_topic_sort_06)
    TextView mSendTopicTopicSort06;
    @BindView(R.id.mSendTopic_topic_sort_07)
    TextView mSendTopicTopicSort07;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    @BindView(R.id.mSend_topic_content)
    EditText mSendTopicContent;
    private int[] strs = new int[]{R.string.Guandian, R.string.Xingqu, R.string.Meitu, R.string.Jiaoyou, R.string.Shenghuo, R.string.Qinggan, R.string.Huodong};
    private List<TextView> huatis = new ArrayList<>();
    private int topstate = 0;
    private File cropImage;
    private Uri cropUri;
    private String headurl;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 152:
                    String s = (String) msg.obj;
                    Log.i("icon", s);
                    BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                    if (beanicon.getRetcode() == 2000) {
                        headurl = beanicon.getData();
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
        setContentView(R.layout.activity_send_topic);
        ButterKnife.bind(this);
        setData();
    }

    private void setData() {
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        mSendTopicContent.addTextChangedListener(new EditTitleChangedListener());
        huatis.add(mSendTopicTopicSort01);
        huatis.add(mSendTopicTopicSort02);
        huatis.add(mSendTopicTopicSort03);
        huatis.add(mSendTopicTopicSort04);
        huatis.add(mSendTopicTopicSort05);
        huatis.add(mSendTopicTopicSort06);
        huatis.add(mSendTopicTopicSort07);
        for (int i = 0; i < huatis.size(); i++) {
            final int finalI = i;
            huatis.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (huatis.get(finalI).isSelected()) {
                        for (int j = 0; j < huatis.size(); j++) {
                            huatis.get(j).setSelected(false);
                        }
                        topstate = 0;
                    } else {
                        for (int j = 0; j < huatis.size(); j++) {
                            huatis.get(j).setSelected(false);
                        }
                        huatis.get(finalI).setSelected(true);
                        topstate = finalI + 1;
                    }
                }
            });
        }
//        for (int i = 0; i < strs.length; i++) {
//            final TextView textview = new TextView(this);
//            textview.setTextColor(Color.parseColor("#FFFFFF"));
//            textview.setTextSize(13);
//            textview.setBackgroundResource(R.drawable.item_uplevel_conner_hui_bg);
//            textview.setText(getResources().getText(strs[i]));
//            huatis.add(textview);
//            mSendTopicWordwrapview.addView(textview);
//        }
//        for (int i = 0; i < huatis.size(); i++) {
//            final int j = i;
//            huatis.get(j).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    for (int k = 0; k < huatis.size(); k++) {
//                        huatis.get(k).setBackgroundResource(R.drawable.item_uplevel_conner_hui_bg);
//                    }
//                    huatis.get(j).setBackgroundResource(R.drawable.item_uplevel_conner_bg);
//                    topstate = j + 1;
//                }
//            });
//        }
    }

    @OnClick({R.id.mTopicDetail_return, R.id.mSend_topic_send, R.id.mSend_topic_guifan, R.id.mSend_topic_addpic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mTopicDetail_return:
                finish();
                break;
            case R.id.mSend_topic_send:
                //创建话题
                if (topstate != 0) {
                    if (headurl != null) {
                        if (!mSendTopicEtTitle.getText().toString().equals("")) {
                            if (!mSendTopicContent.getText().toString().equals("")) {
                                if (mSendTopicContent.getText().toString().length() > 30) {
                                    mSendTopicSend.setClickable(false);
                                    sendTopic();
                                } else {
                                    ToastUtil.show(getApplicationContext(), "话题介绍不能少于30个字...");
                                }
                            } else {
                                ToastUtil.show(getApplicationContext(), "请输入话题介绍...");
                            }
                        } else {
                            ToastUtil.show(getApplicationContext(), "请输入话题标签...");
                        }
                    } else {
                        ToastUtil.show(getApplicationContext(), "请上传话题图片...");
                    }
                } else {
                    ToastUtil.show(getApplicationContext(), "请选择话题分类...");
                }
                break;
            case R.id.mSend_topic_guifan:
                Intent intent = new Intent(this, VipWebActivity.class);
                intent.putExtra("title", "图文规范");
                intent.putExtra("path", HttpUrl.NetPic()+HttpUrl.PicTextHtml);
                startActivity(intent);
                break;
            case R.id.mSend_topic_addpic:
                new AlertView(null, null, "取消", null,
                        new String[]{"拍照", "从相册中选择"},
                        this, AlertView.Style.ActionSheet, this).show();
                break;
        }
    }

    private void sendTopic() {
        JSONObject topicObj = new JSONObject();
        try {
            topicObj.put("pid", topstate + "");
            topicObj.put("uid", MyApp.uid);
            topicObj.put("title", mSendTopicEtTitle.getText().toString());
            topicObj.put("introduce", mSendTopicContent.getText().toString());
            topicObj.put("pic", headurl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new TopicTask().execute(new TopicRequest(topicObj.toString()));
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
        }
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
                        mSendTopicAddpic.setImageBitmap(photo);
                    }
                    ToastUtil.show(getApplicationContext(), "图片上传中,请稍后提交...");
                    ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
                    PhotoUploadTask put = new PhotoUploadTask(
                            NetPic()+"Api/Api/fileUpload", is,//"http://59.110.28.150:888/
                            this, handler);
                    put.start();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class EditTitleChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mSendTopicTextcount.setText("" + s.length() + "/256");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() == 256) {
                ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
            }
        }
    }

    class TopicTask extends AsyncTask<TopicRequest, Void, String> {

        @Override
        protected String doInBackground(TopicRequest... params) {
            TopicRequest topicRequest = params[0];
            String data = null;
            String json = topicRequest.topicObjectStr;
            try {
                data = postJson(HttpUrl.CreateTopic, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if (null == data) {
                return;
            }
            try {
                JSONObject obj = new JSONObject(data);
                switch (obj.getInt("retcode")) {
                    case 3000:
                    case 3001:
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        break;
                    case 2000:
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        finish();

                        break;
                    case 50001:
                    case 50002:
                        EventBus.getDefault().post(new TokenFailureEvent());
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSendTopicSend.setClickable(true);
        }
    }

    private String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(HttpUrl.NetPic()+url).addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(),"url_token","")).post(body).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    class TopicRequest {
        String topicObjectStr;

        public TopicRequest(String topicObjectStr) {
            this.topicObjectStr = topicObjectStr;
        }
    }
}
