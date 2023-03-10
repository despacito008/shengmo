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
import org.feezu.liuli.timeselector.TimeSelector
import java.io.File
import java.util.concurrent.Executors

class HighEndAuthHealthFragment : LazyFragment() {

    lateinit var recycleView: RecyclerView
    lateinit var tvAdd: TextView
    lateinit var model: HighAuthDataBean
    lateinit var mList: ArrayList<HighAuthInfoBean.DataInfoBean>
    lateinit var mHighAuthAdapter: HighAuthAdapter


    var mLayoutPosition = 0
    override fun loadData() {
    }

    override fun getContentViewId(): Int {
        return R.layout.app_fragment_high_end_auth_health
    }


    override fun initView(rootView: View) {
        mList = ArrayList()
        recycleView = rootView.findViewById(R.id.recycleView)
        tvAdd = rootView.findViewById(R.id.tv_add)

        tvAdd.setOnClickListener {
            if (HighEndAuthActivity.IsTopUser != "1") {
                "???????????????????????????".showToast()
                return@setOnClickListener
            }
            addAdapterOneItem()
        }
        initRecycleViewAdapter()
        getAuthInfo()
    }


    private fun initRecycleViewAdapter() {
        mHighAuthAdapter = HighAuthAdapter(context, mList, "2")
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
                        .previewPhotos(models) // ?????????????????????????????????
                        .selectedPhotos(models) // ????????????????????????????????????
                        .maxChooseCount(photoLayout.maxItemCount - photoLayout.itemCount) // ??????????????????????????????
                        .currentPosition(position) // ???????????????????????????
                        .isFromTakePhoto(false) // ?????????????????????????????????
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
        HttpHelper.getInstance().getHighAuthInfo("2", object : HttpCodeMsgListener {
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
            "???????????????????????????".showToast()
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
                        var toastStr = StringBuilder().append("??????:")
                        with(toastStr) {
                            if (permissions?.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) == true && permissions?.contains(Manifest.permission.CAMERA)) {
                                append("???????????? ")
                            }
                            append("?????????")
                        }
                        if (never) {
                            toastStr.append("????????????????????????")
                            showPermissionTipDialog(toastStr.toString(), permissions)
                        } else {
                            toastStr.append("?????????????????????")
                            toastStr.toString().showToast()
                        }
                    }
                })
    }

    fun openPhotoWrapper() {// ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        val photoLayout: BGASortableNinePhotoLayout = recycleView.findViewHolderForAdapterPosition(mLayoutPosition).itemView.findViewById(R.id.nine_layout_upload_photo)
        val takePhotoDir = File(Environment.getExternalStorageDirectory(), "Shengmo")
        val photoPickerIntent = BGAPhotoPickerActivity.IntentBuilder(context)
                .cameraFileDir(takePhotoDir) // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                .maxChooseCount(photoLayout.maxItemCount - photoLayout.itemCount) // ??????????????????????????????
                .selectedPhotos(null) // ????????????????????????????????????
                .pauseOnScroll(false) // ???????????????????????????????????????
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
                    "????????????????????????????????????????????????".showToast()
                }
            }
        }

    }


    fun showPermissionTipDialog(info: String?, permissions: List<String?>?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("??????")
        builder.setMessage(info)
        builder.setPositiveButton("?????????") { dialog, _ ->
            dialog.dismiss()
            XXPermissions.startPermissionActivity(context, permissions)
        }
        builder.setNegativeButton("??????") { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        if (!dialog.isShowing) {
            dialog.show()
        }


    }

}