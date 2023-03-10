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
import org.feezu.liuli.timeselector.Utils.TextUtil
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.HashMap
import kotlin.collections.set

class HighSkillHolder(var view: View,var context: Context) : HighAuthBaseHolder(view) {
    private var etSkillName: EditText = view.findViewById(R.id.et_skillName)
    private var etSkillId: EditText = view.findViewById(R.id.et_skillId)
    private var etSkillDesc: EditText = view.findViewById(R.id.et_SkillDesc)

    var photolayout: BGASortableNinePhotoLayout = view.findViewById(R.id.nine_layout_upload_photo)
    private var tvSubmit: TextView = view.findViewById(R.id.tv_submit)
    var delegate: BGASortableNinePhotoLayout.Delegate
    private var tvDelete: TextView = view.findViewById(R.id.tv_delete)
    private var tvStatus: TextView = view.findViewById(R.id.tv_status)
    var mList: ArrayList<HighAuthInfoBean.DataInfoBean> = ArrayList()
    var mModel: HighAuthInfoBean.DataInfoBean = HighAuthInfoBean().DataInfoBean()
    private var photoSize = 0
    init {

        delegate = object : BGASortableNinePhotoLayout.Delegate {
            override fun onClickAddNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, models: ArrayList<String>) {
                if (HighEndAuthActivity.IsTopUser != "1") {
                    "???????????????????????????".showToast()
                    return
                }
                baseHolderContentInterface?.onAddItem(layoutPosition)
            }

            override fun onClickDeleteNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, model: String, models: ArrayList<String>) {
                photolayout.removeItem(position)
                tvSubmit.text ="????????????"
            }

            override fun onClickNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, model: String, models: ArrayList<String>) {
                baseHolderContentInterface?.onNineClickItem(layoutPosition, position, models, sortableNinePhotoLayout)
                if (models.size != photoSize){
                    tvSubmit.text ="????????????"
                }
            }

            override fun onNinePhotoItemExchanged(sortableNinePhotoLayout: BGASortableNinePhotoLayout, fromPosition: Int, toPosition: Int, models: ArrayList<String>) {

            }
        }
        photolayout.setDelegate(delegate)



        etSkillName.addTextChangedListenerDsl {
            afterTextChanged {
                if (it.toString() !=  mModel.name && !TextUtil.isEmpty(mModel.audit_id) && mModel.status != "0"){
                    tvSubmit.text="????????????"
                }
            }
        }
        etSkillId.addTextChangedListenerDsl {
            afterTextChanged {
                if (it.toString() !=  mModel.cert_num && !TextUtil.isEmpty(mModel.audit_id) && mModel.status != "0"){
                    tvSubmit.text="????????????"
                }
            }
        }
        etSkillDesc.addTextChangedListenerDsl {
            afterTextChanged {
                if (it.toString() !=  mModel.desc&& !TextUtil.isEmpty(mModel.audit_id) && mModel.status != "0"){
                    tvSubmit.text="????????????"
                }
            }
        }
        tvDelete.setOnClickListener {
            if(HighEndAuthActivity.IsTopUser != "1"){
                "???????????????????????????".showToast()
                return@setOnClickListener
            }
            if(mModel.status == "0"){
                "???????????????,????????????".showToast()
                return@setOnClickListener
            }
            deleteAuth()
        }

        tvSubmit.setOnClickListener {
            if (FastClickUtil.isFastClick()) {
                return@setOnClickListener
            }
            if(HighEndAuthActivity.IsTopUser != "1"){
                "???????????????????????????".showToast()
                return@setOnClickListener
            }

            submitInfo()
        }
    }


    override fun layoutViews() {

    }

    @SuppressLint("SetTextI18n")
    override fun layoutDatas(mode: HighAuthInfoBean.DataInfoBean) {

        mModel =mode
        etSkillName.setText(mode.name)
        etSkillId.setText(mode.cert_num)
        etSkillDesc.setText(mode.desc)

        photolayout.data.clear()
        photolayout.data=mode.img
        tvSubmit.text ="????????????"
        if (mode.img != null ){
            photoSize = mode.img.size
        }

        if( !TextUtil.isEmpty(mode.status_string) ){
            tvStatus.visibility =View.VISIBLE
            tvStatus.text ="????????????:${mode.status_string}"
        }else {
            tvStatus.visibility =View.GONE
        }

        if (HighEndAuthActivity.IsTopUser == "1") {
            if (mode.status == "0") {
                setCanNotEditAndNotClick(etSkillName)
                setCanNotEditAndNotClick(etSkillDesc)
                setCanNotEditAndNotClick(etSkillId)
                photolayout.setDelegate(null)
            } else {
                setCanEdit(etSkillName)
                setCanEdit(etSkillDesc)
                setCanEdit(etSkillId)
                photolayout.setDelegate(delegate)
            }
        }else {
            setCanNotEditAndClick(etSkillName)
            setCanNotEditAndClick(etSkillDesc)
            setCanNotEditAndClick(etSkillId)
            photolayout.setDelegate(delegate)
        }

    }

    private fun deleteAuth() {

        showDeleteTip()

    }


    private fun  showDeleteTip(){
        val normalTipsPop = NormalTipsPop.Builder(context)
                .setTitle("??????")
                .setInfo("???????????????????")
                .setCancelStr("??????")
                .setConfirmStr("??????")
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
    private fun submitInfo() {
        if (etSkillName.text.isEmpty()) {
            "?????????????????????".showToast()
            return
        }
        if (etSkillId.text.isEmpty()) {
            "???????????????".showToast()
            return
        }
        if (etSkillDesc.text.isEmpty()) {
            "???????????????".showToast()
            return
        }
        if (photolayout.data.size == 0) {
            "???????????????".showToast()
            return
        }
        if (photolayout.data.size == 0) {
            "???????????????".showToast()
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
                val map :HashMap<String,String> = HashMap()
                map["audit_type"] = "4"
                map["audit_id"] =  mList[layoutPosition].audit_id
                map["name"] = mList[layoutPosition].name
                map["desc"] = mList[layoutPosition].desc
                map["cert_num"] = mList[layoutPosition].cert_num
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
            loadingPop = LoadingPop(context, "????????????...")
        }
        loadingPop?.showPopupWindow()
    }

    private fun dimissPop() {
        if (loadingPop != null) {
            loadingPop?.dismiss()
        }
    }

}