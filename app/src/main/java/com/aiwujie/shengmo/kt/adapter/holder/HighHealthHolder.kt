package com.aiwujie.shengmo.kt.adapter.holder

import android.annotation.SuppressLint
import android.content.Context
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
import org.feezu.liuli.timeselector.TimeSelector
import org.feezu.liuli.timeselector.Utils.TextUtil
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class HighHealthHolder(var view: View, var context: Context) : HighAuthBaseHolder(view) {
    private var photoSize: Int = 0
    var etName: EditText = view.findViewById(R.id.et_name)
    var tvTimes: TextView = view.findViewById(R.id.tv_times)
    var photolayout: BGASortableNinePhotoLayout = view.findViewById(R.id.nine_layout_upload_photo)
    private var tvSubmit: TextView = view.findViewById(R.id.tv_submit)
    private var tvDelete: TextView = view.findViewById(R.id.tv_delete)
    private var tvStatus: TextView = view.findViewById(R.id.tv_status)
    var delegate: BGASortableNinePhotoLayout.Delegate
    var mModel: HighAuthInfoBean.DataInfoBean = HighAuthInfoBean().DataInfoBean()

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
                if (it.toString() != mModel.name && !TextUtil.isEmpty(mModel.audit_id) && mModel.status != "0") {
                    tvSubmit.text = "重新提交"
                }
            }
        }

        tvTimes.addTextChangedListenerDsl {
            afterTextChanged {
                if (it.toString() != mModel.examin_time && !TextUtil.isEmpty(mModel.audit_id) && mModel.status != "0") {
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

        tvTimes.setOnClickListener {
            if (HighEndAuthActivity.IsTopUser != "1") {
                "请开通高端交友服务".showToast()
                return@setOnClickListener
            }
            showTimeDialog()
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

    private fun deleteAuth() {

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

    @SuppressLint("SimpleDateFormat")
    private fun showTimeDialog() {
        // 参数说明：ResultHandler为选取时间后的回调 startDate，endDate为时间控件的可选起始时间和结束时间。
        val timeSelector = TimeSelector(context, TimeSelector.ResultHandler { time ->
            val times = time.substring(0, time.length - 6)
            tvTimes.text = times
        }, "2000-01-01 00:00", SimpleDateFormat("yyyy-MM-dd  HH:mm").format(Date(System.currentTimeMillis())), -1)
        timeSelector.setMode(TimeSelector.MODE.YMD)
        timeSelector.show()
    }


    override fun layoutViews() {

    }

    @SuppressLint("SetTextI18n")
    override fun layoutDatas(mode: HighAuthInfoBean.DataInfoBean) {

        mModel = mode
        etName.setText(mode.name)
        tvTimes.text = mode.examin_time
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
                setCanNotEditAndNotClick(tvTimes)
                photolayout.setDelegate(null)
            } else {
                setCanEdit(etName)
                setCanEdit(tvTimes)
                photolayout.setDelegate(delegate)
            }
        } else {
            setCanNotEditAndClick(etName)
            setCanNotEditAndClick(tvTimes)
            photolayout.setDelegate(delegate)
        }


    }

    var mList: ArrayList<HighAuthInfoBean.DataInfoBean> = ArrayList()
    private fun submitInfo() {
        if (etName.text.isEmpty()) {
            "请填写认证名称".showToast()
            return
        }
        if (tvTimes.text.isEmpty()) {
            "请选择时间".showToast()
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
                map["audit_type"] = "2"
                map["audit_id"] = mModel.audit_id
                map["name"] = etName.text.toString().trim()
                map["examin_time"] = tvTimes.text.toString().trim()
                map["img"] = imgString
                HttpHelper.getInstance().submitHighAuthInfo(
                        map,
                        object : HttpCodeMsgListener {
                            override fun onSuccess(data: String?, msg: String?) {
                                dimissPop()
                                msg?.showToast()
                            }

                            override fun onFail(code: Int, msg: String?) {
                                dimissPop()
                                msg?.showToast()
                            }
                        }
                )
            }
        })


    }

    private fun uploadImg(imagesList: ArrayList<String>, uploadImgCallback: UploadImgCallback) {
        if (imagesList.size == 0) {
            uploadImgCallback.callback(imagesList)
            return
        }
        val uploadList: ArrayList<String> = ArrayList()
        var uploadCount = 0
        val singleThreadPool = Executors.newFixedThreadPool(5)
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