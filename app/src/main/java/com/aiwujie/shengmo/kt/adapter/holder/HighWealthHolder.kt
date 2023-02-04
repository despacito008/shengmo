package com.aiwujie.shengmo.kt.adapter.holder

import android.annotation.SuppressLint
import android.content.Context
import android.text.method.KeyListener
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.BeanIcon
import com.aiwujie.shengmo.bean.HighAuthInfoBean
import com.aiwujie.shengmo.kt.listener.UploadImgCallback
import com.aiwujie.shengmo.kt.ui.activity.HighEndAuthActivity
import com.aiwujie.shengmo.kt.ui.view.LoadingPop
import com.aiwujie.shengmo.kt.util.addTextChangedListenerDsl
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.kt.util.toStr
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.view.NormalTipsPop
import com.aliyun.svideo.common.utils.FastClickUtil
import com.google.gson.Gson
import org.feezu.liuli.timeselector.Utils.TextUtil
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.HashMap

class HighWealthHolder(var view: View, var context: Context) : HighAuthBaseHolder(view) {
    var etName: EditText = view.findViewById(R.id.et_name)
    var etDesc: EditText = view.findViewById(R.id.et_desc)
    var photolayout: BGASortableNinePhotoLayout = view.findViewById(R.id.nine_layout_upload_photo)
    private var tvSubmit: TextView = view.findViewById(R.id.tv_submit)
    private var tvDelete: TextView = view.findViewById(R.id.tv_delete)
    private var tvStatus: TextView = view.findViewById(R.id.tv_status)
    var delegate: BGASortableNinePhotoLayout.Delegate
    var mList: ArrayList<HighAuthInfoBean.DataInfoBean> = ArrayList()
    var mModel: HighAuthInfoBean.DataInfoBean = HighAuthInfoBean().DataInfoBean()
    var photoSize = 0

    init {

        delegate = object : BGASortableNinePhotoLayout.Delegate {
            override fun onClickAddNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, models: ArrayList<String>) {
                if (HighEndAuthActivity.IsTopUser != "1") {
                    "请开通高端交友服务".showToast()
                    return
                }
                baseHolderContentInterface?.onAddItem(layoutPosition)
            }

            override fun onClickDeleteNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, model: String, models: ArrayList<String>) {
                photolayout.removeItem(position)
                tvSubmit.text = "重新提交"
            }

            override fun onClickNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, model: String, models: ArrayList<String>) {
                baseHolderContentInterface?.onNineClickItem(layoutPosition, position, models, sortableNinePhotoLayout)
                if (models.size != photoSize) {
                    tvSubmit.text = "重新提交"
                }
            }

            override fun onNinePhotoItemExchanged(sortableNinePhotoLayout: BGASortableNinePhotoLayout, fromPosition: Int, toPosition: Int, models: ArrayList<String>) {

            }
        }
        photolayout.setDelegate(delegate)



        etName.addTextChangedListenerDsl {
            afterTextChanged {
                if (it.toString() != mModel.name && !TextUtil.isEmpty(mModel.audit_id) && mModel.status != "0" ) {
                    tvSubmit.text = "重新提交"
                }
            }
        }
        etDesc.addTextChangedListenerDsl {
            afterTextChanged {
                if (it.toString() != mModel.desc  && !TextUtil.isEmpty(mModel.audit_id) && mModel.status != "0") {
                    tvSubmit.text = "重新提交"
                }
            }
        }

        tvSubmit.setOnClickListener {
            if (FastClickUtil.isFastClick()) {
                return@setOnClickListener
            }
            if (HighEndAuthActivity.IsTopUser != "1") {
                "请开通高端交友服务".showToast()
                return@setOnClickListener
            }
            submitInfo()
        }
        tvDelete.setOnClickListener {
            if (HighEndAuthActivity.IsTopUser != "1") {
                "请开通高端交友服务".showToast()
                return@setOnClickListener
            }
            if (mModel.status == "0") {
                "正在审核中,请勿删除".showToast()
                return@setOnClickListener
            }
            deleteAuth()
        }
    }


    override fun layoutViews() {

    }


    @SuppressLint("SetTextI18n")
    override fun layoutDatas(mode: HighAuthInfoBean.DataInfoBean) {
        mModel = mode
        etDesc.setText(mode.desc)
        etName.setText(mode.name)
        photolayout.data.clear()
        photolayout.data = mode.img
        tvSubmit.text = "提交审核"
        if (mode.img != null) {
            photoSize = mode.img.size
        }
        if (!TextUtil.isEmpty(mode.status_string)) {
            tvStatus.visibility = View.VISIBLE
            tvStatus.text = "审核状态:${mode.status_string}"
        } else {
            tvStatus.visibility = View.GONE
        }




        if (HighEndAuthActivity.IsTopUser == "1") {
            if (mode.status == "0") {
                setCanNotEditAndNotClick(etName)
                setCanNotEditAndNotClick(etDesc)
                photolayout.setDelegate(null)
            } else {
                setCanEdit(etName)
                setCanEdit(etDesc)
                photolayout.setDelegate(delegate)
            }
        }else {
            setCanNotEditAndClick(etName)
            setCanNotEditAndClick(etDesc)
            photolayout.setDelegate(delegate)
        }


    }

    private fun deleteAuth() {
        if (mModel.status == "0") {
            "正在审核中,请勿删除".showToast()
            return
        }
        showDeleteTip()
    }



    private fun showDeleteTip() {
        val normalTipsPop = NormalTipsPop.Builder(context)
                .setTitle("提示")
                .setInfo("是否删除认证?")
                .setCancelStr("取消")
                .setConfirmStr("确定")
                .build()
        normalTipsPop.setOnPopClickListener(object : NormalTipsPop.OnPopClickListener {
            override fun cancelClick() {
                normalTipsPop.dismiss()
            }

            override fun confirmClick() {
                if (mModel.audit_id != null) {
                    deleteAuthInfo()
                } else {
                    baseHolderContentInterface?.onDeleteItem(layoutPosition)
                }
                normalTipsPop.dismiss()
            }
        })
        normalTipsPop.showPopupWindow()
    }

    private fun deleteAuthInfo() {
        HttpHelper.getInstance().deleteHighAuth(mModel.audit_id, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                baseHolderContentInterface?.onDeleteItem(layoutPosition)
                msg?.showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })

    }


    private fun submitInfo() {
        if (etName.text.isEmpty()) {
            "请填写认证名称".showToast()
            return
        }
        if (etDesc.text.isEmpty()) {
            "请填写描述".showToast()
            return
        }
        if (photolayout.data.size == 0) {
            "请上传图片".showToast()
            return
        }
        val uploadImgList: ArrayList<String> = ArrayList()
        val oldList: ArrayList<String> = photolayout.data.filter {
            if (!it.startsWith("http")) {
                uploadImgList.add(it)
            }
            it.startsWith("http")
        } as ArrayList<String>
        showloadPop()
        uploadImg(uploadImgList, object : UploadImgCallback {
            override fun callback(list: ArrayList<String>) {
                oldList.addAll(list)
                val imgString = oldList.toStr()
                val map: HashMap<String, String?> = HashMap()
                map["audit_type"] = "1"
                map["audit_id"] = mModel.audit_id
                map["name"] = etName.text.toString().trim()
                map["desc"] = etDesc.text.toString().trim()
                map["img"] = imgString
                HttpHelper.getInstance().submitHighAuthInfo(
                        map,
                        object : HttpCodeMsgListener {
                            override fun onSuccess(data: String?, msg: String?) {
                                baseHolderContentInterface?.onAdapterNotify()
                                dimissPop()
                                msg?.showToast()
                            }

                            override fun onFail(code: Int, msg: String?) {
                                baseHolderContentInterface?.onAdapterNotify()
                                dimissPop()
                                msg?.showToast()
                            }
                        }
                )
            }
        })


    }

    private fun uploadImg(imagesList: ArrayList<String>, uploadImgCallback: UploadImgCallback) {
        val uploadList: ArrayList<String> = ArrayList()
        var uploadCount = 0
        val singleThreadPool = Executors.newFixedThreadPool(5)

        if (imagesList.size == 0) {
            uploadImgCallback.callback(imagesList)
            return
        }
        for (str in imagesList) {
            singleThreadPool.execute {
                HttpHelper.getInstance().uploadImage(str, object : HttpCodeMsgListener {
                    override fun onSuccess(data: String?, msg: String?) {
                        val beanIcon = Gson().fromJson(data, BeanIcon::class.java)
                        beanIcon?.data?.run {
                            uploadCount++
                            uploadList.add(this)
                            Log.v("EditInfoHead", this)
                            if (uploadCount == imagesList.size) {
                                uploadImgCallback.callback(uploadList)
                            }

                        }
                    }

                    override fun onFail(code: Int, msg: String?) {
                        uploadCount++
                        uploadList.add("failed${uploadCount}")
                        msg?.showToast()
                    }
                })
            }
        }

    }

    private var loadingPop: LoadingPop? = null
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