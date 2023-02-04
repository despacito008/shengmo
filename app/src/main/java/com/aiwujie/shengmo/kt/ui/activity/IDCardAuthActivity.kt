package com.aiwujie.shengmo.kt.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.BeanIcon
import com.aiwujie.shengmo.kt.util.NormalConstant
import com.aiwujie.shengmo.kt.util.SpKey
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.*
import com.google.gson.Gson
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import java.io.File
import java.io.IOException


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: IDCardAuthActivity
 * @Author: xmf
 * @CreateDate: 2022/4/8 10:37
 * @Description: 身份证认证
 */
class IDCardAuthActivity : BaseActivity() {
    // 标题栏
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView
    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView

    private lateinit var etAuthName: EditText
    private lateinit var etAuthNum: EditText
    private lateinit var ivAuthPic: ImageView
    private lateinit var tvAuthCommit: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_id_card_auth)
        StatusBarUtil.showLightStatusBar(this)
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)
        tvTitle.text = "身份证认证"
        ivTitleBack.setOnClickListener { finish() }
        etAuthName = findViewById(R.id.et_id_card_auth_name)
        etAuthNum = findViewById(R.id.et_id_card_auth_num)
        ivAuthPic = findViewById(R.id.iv_id_card_auth)
        tvAuthCommit = findViewById(R.id.tv_id_card_auth_commit)
        tvAuthCommit.setOnClickListener {
            commitIdCardAuth()
        }
        ivAuthPic.setOnClickListener {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        XXPermissions.with(this)
                .permission(Manifest.permission.CAMERA)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            openCamera()
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        if (never) {
                            showCameraTipDialog()
                        } else {
                            "该功能需要使用 相机 和 存储权限，授予后才能正常使用".showToast()
                        }
                    }
                })
    }

    private var mTempPhotoPath = ""
    private var mImageUri: Uri? = null
    private fun openCamera() {
        mTempPhotoPath = Environment.getExternalStorageDirectory().toString() + File.separator + "temp_photo.jpeg"
        // 跳转到系统的拍照界面
        val intentToTakePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 指定照片存储位置为sd卡本目录下
        // 这里设置为固定名字 这样就只会只有一张temp图 如果要所有中间图片都保存可以通过时间或者加其他东西设置图片的名称
        // File.separator为系统自带的分隔符 是一个固定的常量
        // 获取图片所在位置的Uri路径    *****这里为什么这么做参考问题2*****
        val tempFile = File(mTempPhotoPath)
        if (tempFile.exists()) {
            tempFile.delete()
        }
        try {
            tempFile.createNewFile()
        } catch (e: IOException) {
        }
        mImageUri = if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(this, this.applicationContext.packageName + ".fileProvider", tempFile)
        } else {
            Uri.fromFile(tempFile)
        }
        //如果指定了MediaStore.EXTRA_OUTPUT，则所拍摄的图像将写入该路径，并且不会向onActivityResult提供任何数据。您可以从您指定的内容中读取图像。
        //下面这句指定调用相机拍照后的照片存储的路径
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
        startActivityForResult(intentToTakePhoto, NormalConstant.REQUEST_CODE_1)
    }

    private fun showCameraTipDialog() {
        val dialog = with(AlertDialog.Builder(this)) {
            setTitle("提示")
            setMessage("该功能需要使用 相机 和 存储 权限，授予后才能正常使用")
            setPositiveButton("去授予") { dialog, _ ->
                dialog.dismiss()
                XXPermissions.startPermissionActivity(this@IDCardAuthActivity, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            create()
        }
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NormalConstant.REQUEST_CODE_1) {
            if (data == null) {
                val bm = BitmapUtils.ResizeBitmap(BitmapUtils.orientation(mTempPhotoPath), 960)
                        ?: return
                val uri = Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, bm, "temp_image_" + System.currentTimeMillis(), null))
                uploadImage(uri)

            } else {
                val bm = BitmapUtils.ResizeBitmap(PhotoUploadUtils.decodeUriAsBitmap(mImageUri, this), 960)
                        ?: return
                val uri = Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, bm, null, null))
                uploadImage(uri)
            }
        }
    }

    private var mPicUrl = ""
    private fun uploadImage(uri: Uri) {
        "上传中，请稍后".showToast()
        val path = UriUtil.getRealPathFromURI(this, uri)
        HttpHelper.getInstance().uploadImage(path, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val beanIcon = Gson().fromJson(data, BeanIcon::class.java)
                beanIcon?.data?.run {
                    val imgPre = SharedPreferencesUtils.getParam(this@IDCardAuthActivity, SpKey.IMAGE_HOST, "") as String
                    mPicUrl = imgPre + this
                    ImageLoader.loadImage(this@IDCardAuthActivity, mPicUrl, ivAuthPic)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun commitIdCardAuth() {
        if (etAuthName.text.toString().isEmpty()) {
            "请填写真实姓名".showToast()
            return
        }
        if (etAuthNum.text.toString().isEmpty()) {
            "请填写身份证号".showToast()
            return
        }
        if (mPicUrl.isEmpty()) {
            "请上传自拍照片".showToast()
            return
        }
        HttpHelper.getInstance().authNewIdCard(etAuthName.text.toString(), etAuthNum.text.toString(), mPicUrl, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                finish()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }
}
