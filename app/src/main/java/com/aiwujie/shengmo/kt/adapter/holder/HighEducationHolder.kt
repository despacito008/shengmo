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
import com.aiwujie.shengmo.kt.ui.view.DegreePop
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

class HighEducationHolder(var view: View, var context: Context) : HighAuthBaseHolder(view) {
    var etSchoolName: EditText = view.findViewById(R.id.et_schoolName)
    var tvCultureName: TextView = view.findViewById(R.id.tv_cultureName)
    var etProjectName: EditText = view.findViewById(R.id.et_projectName)
    var tvStartTime: TextView = view.findViewById(R.id.tv_startTime)
    var tvEndTime: TextView = view.findViewById(R.id.tv_endTime)
    var photolayout: BGASortableNinePhotoLayout = view.findViewById(R.id.nine_layout_upload_photo)
    private var tvSubmit: TextView = view.findViewById(R.id.tv_submit)
    private var tvDelete: TextView = view.findViewById(R.id.tv_delete)
    private var tvStatus: TextView = view.findViewById(R.id.tv_status)
    var delegate: BGASortableNinePhotoLayout.Delegate
    var mList: ArrayList<HighAuthInfoBean.DataInfoBean> = ArrayList()
    var mModel: HighAuthInfoBean.DataInfoBean = HighAuthInfoBean().DataInfoBean()
    private var photoSize = 0
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
                tvSubmit.text ="重新提交"
            }

            override fun onClickNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, model: String, models: ArrayList<String>) {
                baseHolderContentInterface?.onNineClickItem(layoutPosition, position, models, sortableNinePhotoLayout)
                if (models.size != photoSize){

                    tvSubmit.text ="重新提交"
                }
            }

            override fun onNinePhotoItemExchanged(sortableNinePhotoLayout: BGASortableNinePhotoLayout, fromPosition: Int, toPosition: Int, models: ArrayList<String>) {

            }
        }
        photolayout.setDelegate(delegate)

        etSchoolName.addTextChangedListenerDsl {
            afterTextChanged {
                if (it.toString() !=  mModel.school_name && !TextUtil.isEmpty(mModel.audit_id) && mModel.status != "0"){
                    tvSubmit.text="重新提交"
                }
            }
        }
        etProjectName.addTextChangedListenerDsl {
            afterTextChanged {
                if (it.toString() !=  mModel.profession_name && !TextUtil.isEmpty(mModel.audit_id) && mModel.status != "0"){
                    tvSubmit.text="重新提交"
                }
            }
        }

        tvCultureName.addTextChangedListenerDsl {
            afterTextChanged {
                if (it.toString() !=  mModel.education_name){
                    this@HighEducationHolder.tvSubmit.text="重新提交"
                }
            }
        }
        tvStartTime.addTextChangedListenerDsl {
            afterTextChanged {
                if (it.toString() !=  mModel.admission_time){
                    this@HighEducationHolder.tvSubmit.text="重新提交"
                }
            }
        }
        tvEndTime.addTextChangedListenerDsl {
            afterTextChanged {
                if (it.toString() !=  mModel.graduation_time){
                    this@HighEducationHolder.tvSubmit.text="重新提交"
                }
            }
        }

        etProjectName.setOnClickListener {
            if(HighEndAuthActivity.IsTopUser != "1"){
                "请开通高端交友服务".showToast()
                return@setOnClickListener
            }
        }

        tvSubmit.setOnClickListener {
            if (FastClickUtil.isFastClick()) {
                return@setOnClickListener
            }
            if(HighEndAuthActivity.IsTopUser != "1"){
                "请开通高端交友服务".showToast()
                return@setOnClickListener
            }
            submitInfo()
        }

        tvCultureName.setOnClickListener {
            showDegree()
        }

        tvStartTime.setOnClickListener {
            showTimeDialog("start")
        }

        tvEndTime.setOnClickListener {
            showTimeDialog("end")
        }
        tvDelete.setOnClickListener {
            if(HighEndAuthActivity.IsTopUser != "1"){
                "请开通高端交友服务".showToast()
                return@setOnClickListener
            }
            if(mModel.status == "0"){
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
        mModel =mode
        etSchoolName.setText(mode.school_name)

        tvCultureName.text = mode.education_name

        etProjectName.setText(mode.profession_name)

        tvStartTime.text = mode.admission_time
        tvEndTime.text = mode.graduation_time

        photolayout.data = mode.img
        tvSubmit.text ="提交审核"
        if (mode.img != null) {
            photoSize =  mode.img.size
        }
        if( !TextUtil.isEmpty(mode.status_string) ){
            tvStatus.visibility =View.VISIBLE
            tvStatus.text ="审核状态:${mode.status_string}"
        }else {
            tvStatus.visibility =View.GONE
        }

        if (HighEndAuthActivity.IsTopUser == "1") {
            if (mode.status == "0") {
                setCanNotEditAndNotClick(etProjectName)
                setCanNotEditAndNotClick(etSchoolName)
                setCanNotEditAndNotClick(tvCultureName)
                setCanNotEditAndNotClick(tvStartTime)
                setCanNotEditAndNotClick(tvEndTime)
                photolayout.setDelegate(null)
            } else {
                setCanEdit(etProjectName)
                setCanEdit(etSchoolName)
                setCanEdit(tvCultureName)
                setCanEdit(tvStartTime)
                setCanEdit(tvEndTime)
                photolayout.setDelegate(delegate)
            }
        }else {
            setCanNotEditAndClick(etProjectName)
            setCanNotEditAndClick(etSchoolName)
            setCanNotEditAndClick(tvCultureName)
            setCanNotEditAndClick(tvStartTime)
            setCanNotEditAndClick(tvEndTime)
            photolayout.setDelegate(delegate)
        }
    }

    private fun deleteAuth() {

        showDeleteTip()

    }


    private fun  showDeleteTip(){
        val normalTipsPop = NormalTipsPop.Builder(context)
                .setTitle("提示")
                .setInfo("是否删除认证?")
                .setCancelStr("取消")
                .setConfirmStr("确定")
                .build()
        normalTipsPop.setOnPopClickListener(object : NormalTipsPop.OnPopClickListener {
            override fun cancelClick() {  normalTipsPop.dismiss()}
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
        HttpHelper.getInstance().deleteHighAuth(mModel.audit_id,object :HttpCodeMsgListener{
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
    private fun showTimeDialog(str: String) {
        // 参数说明：ResultHandler为选取时间后的回调 startDate，endDate为时间控件的可选起始时间和结束时间。
        val timeSelector = TimeSelector(context, TimeSelector.ResultHandler { time ->
            val times = time.substring(0, time.length - 6)
            when (str) {
                "start" -> {
                    tvStartTime.text = times
                }
                "end" -> {
                    tvEndTime.text = times
                }
            }
        }, "2000-01-01 00:00" ,SimpleDateFormat("yyyy-MM-dd  HH:mm").format(Date(System.currentTimeMillis())))
        timeSelector.setMode(TimeSelector.MODE.YMD)
        timeSelector.show()
    }

    private fun showDegree() {
        val pop = DegreePop(context)
        pop.showPopupWindow()
        pop.onDegreeListener = object : DegreePop.OnDegreeListener {
            override fun onComplete(str: String?) {
                tvCultureName.text = str
            }
        }

    }


    private fun submitInfo() {
        if (etSchoolName.text.isEmpty()) {
            "请填写学校名称".showToast()
            return
        }
        if (tvCultureName.text.isEmpty()) {
            "请填写学历".showToast()
            return
        }
        if (etProjectName.text.isEmpty()) {
            "请填写专业".showToast()
            return
        }
        if (tvStartTime.text.isEmpty()) {
            "请填写入学时间".showToast()
            return
        }
        if (tvEndTime.text.isEmpty()) {
            "请填写毕业时间".showToast()
            return
        }
        if (photolayout.data.isEmpty()) {
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
                map["audit_type"] = "3"
                map["audit_id"] = mModel.audit_id
                map["school_name"] = etSchoolName.text.toString().trim()
                map["education_name"] = tvCultureName.text.toString().trim()
                map["profession_name"] =etProjectName.text.toString().trim()
                map["admission_time"] = tvStartTime.text.toString().trim()
                map["graduation_time"] = tvEndTime.text.toString().trim()
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
        if (imagesList.size == 0){
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