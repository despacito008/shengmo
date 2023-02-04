package com.aiwujie.shengmo.kt.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.BeanIcon
import com.aiwujie.shengmo.bean.HighAuthDataBean
import com.aiwujie.shengmo.bean.HighAuthInfoBean
import com.aiwujie.shengmo.kt.adapter.HighAuthAdapter
import com.aiwujie.shengmo.kt.adapter.`interface`.BaseHolderInterfce
import com.aiwujie.shengmo.kt.listener.UploadImgCallback
import com.aiwujie.shengmo.kt.ui.activity.HighEndAuthActivity
import com.aiwujie.shengmo.kt.ui.view.LoadingPop
import com.aiwujie.shengmo.kt.util.NormalConstant
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.kt.util.toStr
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.LogUtil
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import java.io.File
import java.util.concurrent.Executors

class HighEndAuthSkillFragment : LazyFragment() {
    lateinit var recycleView: RecyclerView
    lateinit var tvAdd: TextView
    lateinit var model: HighAuthDataBean
    lateinit var mList: ArrayList<HighAuthInfoBean.DataInfoBean>
    lateinit var mHighAuthAdapter: HighAuthAdapter


    var mLayoutPosition = 0
    override fun loadData() {
    }

    override fun getContentViewId(): Int {
        return R.layout.app_fragment_high_end_auth_wealth
    }


    override fun initView(rootView: View) {
        mList = ArrayList()

        recycleView = rootView.findViewById(R.id.recycleView)
        tvAdd = rootView.findViewById(R.id.tv_add)

        tvAdd.setOnClickListener {
            if (HighEndAuthActivity.IsTopUser != "1") {
                "请开通高端交友服务".showToast()
                return@setOnClickListener
            }
            addAdapterOneItem()
        }
        initRecycleViewAdapter()
        getAuthInfo()
    }


    private fun initRecycleViewAdapter() {
        mHighAuthAdapter = HighAuthAdapter(context, mList, "4")
        with(recycleView) {
            layoutManager = LinearLayoutManager(context)
            adapter = mHighAuthAdapter
        }
        mHighAuthAdapter.baseHolderInterfce = object : BaseHolderInterfce {

            override fun onPictureAdd(layoutPosition: Int) {
                mLayoutPosition = layoutPosition
                requestPermission()
            }

            override fun onDeleteAdapterItem(layoutPosition: Int) {
                mList.removeAt(layoutPosition)
                mHighAuthAdapter.notifyItemRemoved(layoutPosition)
                if (mList.size == 0) {
                    initAddOne()
                }

            }

            override fun onAdapterNotify() {
                getAuthInfo()
            }

            override fun onNinePictureItemClick(layoutPosition: Int, position: Int, models: ArrayList<String>, photoLayout: BGASortableNinePhotoLayout) {
                mLayoutPosition = layoutPosition
                val photoPickerPreviewIntent = BGAPhotoPickerPreviewActivity.IntentBuilder(context)
                        .previewPhotos(models) // 当前预览的图片路径集合
                        .selectedPhotos(models) // 当前已选中的图片路径集合
                        .maxChooseCount(photoLayout.maxItemCount - photoLayout.itemCount) // 图片选择张数的最大值
                        .currentPosition(position) // 当前预览图片的索引
                        .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                        .build()
                startActivityForResult(photoPickerPreviewIntent, NormalConstant.REQUEST_CODE_2)
            }

        }
    }

    private fun initAddOne() {
        val model: HighAuthInfoBean.DataInfoBean = HighAuthInfoBean().DataInfoBean()
        mList.add(model)
        mHighAuthAdapter.notifyItemChanged(0)
    }


    private fun getAuthInfo() {
        HttpHelper.getInstance().getHighAuthInfo("4", object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val model = GsonUtil.GsonToBean(data, HighAuthInfoBean::class.java)
                model?.data?.run {
                    if (!audit_data.isNullOrEmpty()) {
                        mList.clear()
                        mList.addAll(audit_data)

                        for (index in mList.indices) {
                            mList[index].imgList = audit_data[index].img
                        }
                        mHighAuthAdapter.notifyDataSetChanged()
                    } else {
                        initAddOne()
                    }


                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }


    private fun addAdapterOneItem() {
        if (mList.size >= HighEndAuthActivity.maxListItem) {
            "最多可添加三条认证".showToast()
            return
        }
        val model: HighAuthInfoBean.DataInfoBean = HighAuthInfoBean().DataInfoBean()
        mList.add(model)
        mHighAuthAdapter.notifyItemRangeInserted(mList.size, 1)
    }

    private fun requestPermission() {
        XXPermissions.with(this)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            openPhotoWrapper()
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        var toastStr = StringBuilder().append("权限:")
                        with(toastStr) {
                            if (permissions?.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) == true && permissions?.contains(Manifest.permission.CAMERA)) {
                                append("使用相册 ")
                            }
                            append("被拒绝")
                        }
                        if (never) {
                            toastStr.append("，请手动授予权限")
                            showPermissionTipDialog(toastStr.toString(), permissions)
                        } else {
                            toastStr.append("，无法使用功能")
                            toastStr.toString().showToast()
                        }
                    }
                })
    }

    fun openPhotoWrapper() {// 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        val photoLayout: BGASortableNinePhotoLayout = recycleView.findViewHolderForAdapterPosition(mLayoutPosition).itemView.findViewById(R.id.nine_layout_upload_photo)
        val takePhotoDir = File(Environment.getExternalStorageDirectory(), "Shengmo")
        val photoPickerIntent = BGAPhotoPickerActivity.IntentBuilder(context)
                .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                .maxChooseCount(photoLayout.maxItemCount - photoLayout.itemCount) // 图片选择张数的最大值
                .selectedPhotos(null) // 当前已选中的图片路径集合
                .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                .build()
        startActivityForResult(photoPickerIntent, NormalConstant.REQUEST_CODE_1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            NormalConstant.REQUEST_CODE_1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.v("uploadListSize", BGAPhotoPickerActivity.getSelectedPhotos(data).size.toString() + "")
                    val photoLayout: BGASortableNinePhotoLayout = recycleView.findViewHolderForAdapterPosition(mLayoutPosition).itemView.findViewById(R.id.nine_layout_upload_photo)
                    photoLayout.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data))


                }
            }
            NormalConstant.REQUEST_CODE_2 -> {
                val photoLayout: BGASortableNinePhotoLayout = recycleView.findViewHolderForAdapterPosition(mLayoutPosition).itemView.findViewById(R.id.nine_layout_upload_photo)
                photoLayout.data = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data)
            }

            XXPermissions.REQUEST_CODE -> {
                if (XXPermissions.isGranted(context, Manifest.permission.CAMERA) &&
                        XXPermissions.isGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    "权限授予成功，可以使用相册功能了".showToast()
                }
            }
        }

    }


    fun showPermissionTipDialog(info: String?, permissions: List<String?>?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("提示")
        builder.setMessage(info)
        builder.setPositiveButton("去授予") { dialog, which ->
            dialog.dismiss()
            XXPermissions.startPermissionActivity(context, permissions)
        }
        builder.setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
        val dialog = builder.create()
        if (!dialog.isShowing) {
            dialog.show()
        }


    }

    var loadingPop: LoadingPop? = null
    private fun showloadPop() {
        if (loadingPop == null) {
            loadingPop = LoadingPop(context, "正在提交...")
        }
        loadingPop?.showPopupWindow()
    }

    private fun dimissPop() {
        if (loadingPop != null) {
            loadingPop?.dismiss()
        }
    }
}