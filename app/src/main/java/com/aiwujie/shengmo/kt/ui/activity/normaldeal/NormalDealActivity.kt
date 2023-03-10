package com.aiwujie.shengmo.kt.ui.activity.normaldeal

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.BeanIcon
import com.aiwujie.shengmo.kt.ui.view.LimitEditLayout
import com.aiwujie.shengmo.kt.ui.view.UploadTipsPop
import com.aiwujie.shengmo.kt.util.doReplace
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.bigkoo.alertview.AlertView
import com.donkingliang.imageselector.utils.ImageSelector
import com.google.gson.Gson
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import java.io.File
import java.lang.StringBuilder
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.Comparator
import kotlin.collections.ArrayList

abstract class NormalDealActivity : BaseActivity() {
    lateinit var limitLayout: LimitEditLayout
    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView
    private lateinit var photoLayout: BGASortableNinePhotoLayout
    private lateinit var llIsPublic:LinearLayout
    lateinit var cbIsPublic: CheckBox
    private val REQUEST_CODE_PERMISSION_PHOTO_PICKER = 3
    private val REQUEST_CODE_CHOOSE_PHOTO = 1
    private val REQUEST_CODE_PHOTO_PREVIEW = 2
    lateinit var picList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_normal_deal)
        StatusBarUtil.showLightStatusBar(this)
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)
        ivTitleRight.visibility = View.GONE
        tvTitleRight.visibility = View.VISIBLE
        tvTitleRight.text = "??????"
        ivTitleBack.setOnClickListener {
            finish()
        }
        tvTitle.text = getPageTitle()
        picList = initPicList()
        limitLayout = findViewById(R.id.limit_edit_normal_deal)
        limitLayout.limitLength = 1000
        limitLayout.textHint = "?????????????????????"

        llIsPublic = findViewById(R.id.ll_normal_deal_is_public)
        cbIsPublic = findViewById(R.id.cb_normal_deal_is_public)
        llIsPublic.visibility = if (isShowCheckBox()) View.VISIBLE else View.GONE

        photoLayout = findViewById(R.id.photo_layout_normal_deal)
        photoLayout.addMoreData(picList)

        with(photoLayout) {
            maxItemCount = 9
            isEditable = true
            isPlusEnable = true
            isSortable = true
            setDelegate(object : BGASortableNinePhotoLayout.Delegate {
                override fun onClickAddNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout?, view: View?, position: Int, models: ArrayList<String>?) {
                    showSelectAlertView()
                }

                override fun onClickDeleteNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout?, view: View?, position: Int, model: String?, models: ArrayList<String>?) {
                    photoLayout.removeItem(position)
                    picList.removeAt(position)
                }

                override fun onClickNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout?, view: View?, position: Int, model: String?, models: ArrayList<String>?) {
                    val photoPickerPreviewIntent = BGAPhotoPickerPreviewActivity.IntentBuilder(this@NormalDealActivity)
                            .previewPhotos(models) // ?????????????????????????????????
                            .selectedPhotos(models) // ????????????????????????????????????
                            .maxChooseCount(photoLayout.maxItemCount) // ??????????????????????????????
                            .currentPosition(position) // ???????????????????????????
                            .isFromTakePhoto(false) // ?????????????????????????????????
                            .build()
                    startActivityForResult(photoPickerPreviewIntent, REQUEST_CODE_PHOTO_PREVIEW)
                }

                override fun onNinePhotoItemExchanged(sortableNinePhotoLayout: BGASortableNinePhotoLayout?, fromPosition: Int, toPosition: Int, models: ArrayList<String>?) {
                    models?.let {
                        picList.clear()
                        picList.addAll(it)
                    }
                }
            })
        }

        tvTitleRight.setOnClickListener {
            if (picList.size == 0) {
                doCommit()
            } else {
                uploadImage()
            }
        }
        initViewComplete()
    }

    private fun showSelectAlertView() {
        AlertView(null, null, "??????", null, arrayOf("????????????"), this, AlertView.Style.ActionSheet) { _, _, _ ->
            requestPermission()
        }.show()
    }

    private fun requestPermission() {
        XXPermissions.with(this)
                .permission(Manifest.permission.CAMERA)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(object:OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            openPhotoWrapper()
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        //super.onDenied(permissions, never)
                        var toastStr = StringBuilder().append("??????:")
                        with(toastStr) {
                            if (permissions?.contains(Manifest.permission.CAMERA) == true) {
                                append("???????????? ")
                            }
                            if (permissions?.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) == true) {
                                append("???????????? ")
                            }
                            append("?????????")
                        }
                        if (never) {
                            toastStr.append("????????????????????????")
                            showPermissionTipDialog(toastStr.toString(),permissions)
                        } else {
                            toastStr.append("?????????????????????")
                            toastStr.toString().showToast()
                        }
                    }
                })
    }

    fun openPhotoWrapper() {
        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        val takePhotoDir = File(Environment.getExternalStorageDirectory(), "Shengmo")
        val photoPickerIntent = BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(takePhotoDir) // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                .maxChooseCount(photoLayout.maxItemCount - photoLayout.itemCount) // ??????????????????????????????
                .selectedPhotos(null) // ????????????????????????????????????
                .pauseOnScroll(false) // ???????????????????????????????????????
                .build()
        startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_PHOTO)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CHOOSE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    photoLayout.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data))
                    picList.addAll(data!!.getStringArrayListExtra("EXTRA_SELECTED_PHOTOS"))
                }
            }
            REQUEST_CODE_PHOTO_PREVIEW -> {
                photoLayout.data = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data)
            }
            XXPermissions.REQUEST_CODE -> {
                if (XXPermissions.isGranted(this,Manifest.permission.CAMERA) &&
                        XXPermissions.isGranted(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    "????????????????????????????????????????????????".showToast()
                }
            }
        }
    }

    lateinit var imageMap: ConcurrentHashMap<String, String>
    lateinit var uploadList: ArrayList<String>
    lateinit var needUploadIndexList: ArrayList<Int>
    fun uploadImage() {
        uploadList = ArrayList()
        needUploadIndexList = ArrayList()
        imageMap = ConcurrentHashMap()
        val singleThreadPool = Executors.newFixedThreadPool(5)
        var uploadCount = 0
        val upList = picList.filterNot {
            if (!it.startsWith("http")) {
                needUploadIndexList.add(picList.indexOf(it))
            }
            it.startsWith("http")
        } as ArrayList<String>
        if (upList.size == 0 ) {
            doCommit()
            return
        }
        for (imgUrl in upList) {
            showUploadPop()
            singleThreadPool.execute {
                HttpHelper.getInstance().uploadImage(imgUrl, object : HttpCodeMsgListener {
                    override fun onSuccess(data: String?, msg: String?) {
                        uploadCount++
                        val beanIcon = Gson().fromJson(data, BeanIcon::class.java)
                        beanIcon?.data?.run {
                            imageMap[this] = imgUrl
                            uploadList.add(this)
                            uploadPop?.updateProcess(uploadCount)
                        }
                        if (uploadCount == upList.size) {
                            uploadList.sortWith(Comparator { o1, o2 ->
                                upList.indexOf(imageMap[o1]) - upList.indexOf(imageMap[o2])
                            })
                            uploadPop?.dismiss()
                            for ((tempIndex, i) in needUploadIndexList.withIndex()) {
                                picList.doReplace(i, uploadList[tempIndex])
                            }
                            doCommit()
                        }
                    }

                    override fun onFail(code: Int, msg: String?) {
                        uploadCount++
                        imageMap["fail${uploadCount}"] = imgUrl
                        uploadList.add("fail${uploadCount}")
                    }
                })
            }
        }
    }

    var uploadPop: UploadTipsPop? = null
    private fun showUploadPop() {
        if (uploadPop == null) {
            uploadPop = UploadTipsPop(this, 9)
            uploadPop?.showPopupWindow()
        }
    }

//    private fun showPermissionTipDialog(info:String,permissions: MutableList<String>?){
//        val dialog = with(AlertDialog.Builder(this)) {
//            setTitle("??????")
//            setMessage(info)
//            setPositiveButton("?????????"){ dialog, _ ->
//                dialog.dismiss()
//                XXPermissions.startPermissionActivity(this@NormalDealActivity,permissions)
//            }
//            setNegativeButton("??????"){ dialog, _ ->
//                dialog.dismiss()
//            }
//            create()
//        }
//        if (!dialog.isShowing) {
//            dialog.show()
//        }
//    }

    open fun initViewComplete() {}

    abstract fun getPageTitle(): String

    abstract fun isShowCheckBox(): Boolean

    abstract fun initPicList(): ArrayList<String>

    abstract fun doCommit()


}