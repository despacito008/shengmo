package com.aiwujie.shengmo.kt.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.EditRedWomenJSActivity
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.RedwomenNewListAdapter
import com.aiwujie.shengmo.adapter.RedwomenNewListAdapter.OnRedwomenClickListener
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.BeanIcon
import com.aiwujie.shengmo.bean.HighUserModel
import com.aiwujie.shengmo.eventbus.AddHighUserEven
import com.aiwujie.shengmo.eventbus.RedWomenIntroData
import com.aiwujie.shengmo.eventbus.RedWomenJsEvent
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.listener.UploadImgCallback
import com.aiwujie.shengmo.kt.ui.view.HighAddUserPop
import com.aiwujie.shengmo.kt.ui.view.LoadingPop
import com.aiwujie.shengmo.kt.util.IntentKey
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.kt.util.toStr
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.net.IRequestCallback
import com.aiwujie.shengmo.net.RequestFactory
import com.aiwujie.shengmo.utils.GlideImgManager
import com.aiwujie.shengmo.utils.GsonUtil
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.bigkoo.alertview.AlertView
import com.google.gson.Gson
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.tencent.liteav.demo.beauty.utils.ResourceUtils
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView
import org.feezu.liuli.timeselector.Utils.TextUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.collections.set

class PushHighActivity : BaseActivity() {

    lateinit var tvTitle: TextView
    lateinit var ivBack: ImageView
    private lateinit var tvMore: TextView
    private lateinit var ivMore: ImageView
    lateinit var photoLayout: BGASortableNinePhotoLayout
    lateinit var radioGroup: RadioGroup
    private lateinit var tvDesc: TextView


    lateinit var ivIcon: CircleImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserSexAndAge: TextView
    private lateinit var tvUserCity: TextView
    private lateinit var ivCaichan: ImageView
    private lateinit var ivJiankang: ImageView
    private lateinit var ivXueli: ImageView
    private lateinit var ivJineng: ImageView
    private lateinit var ivQita: ImageView


    private lateinit var ivRealId: ImageView
    private lateinit var ivRealName: ImageView
    private lateinit var ivVidoeAuth: ImageView

    private lateinit var rbAll: RadioButton
    private lateinit var rbHigh: RadioButton
    private lateinit var rbSelf: RadioButton


    lateinit var llRedNone: LinearLayout
    lateinit var recycleView: RecyclerView
    lateinit var tvMatch: TextView
    private var redwomenNewAdapter: RedwomenNewListAdapter? = null
    private var redwomendata: RedWomenIntroData? = null


    private lateinit var tvRecommd: TextView
    private lateinit var llRedService: LinearLayout
    private lateinit var llRedMatch: LinearLayout

    private lateinit var constrainUserInfo: ConstraintLayout


    private val selectList: ArrayList<String> = ArrayList()
    private val REQUEST_CODE_PHOTO_PREVIEW = 2
    private val REQUEST_CODE_CHOOSE_PHOTO = 1

    private var topPhotoStates: String = ""

    private var model: HighUserModel.DataBean? = null
    private var uid: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_push_high)
        StatusBarUtil.showLightStatusBar(this)
        EventBus.getDefault().register(this)


        uid = intent.getStringExtra(IntentKey.UID)   //
        tvTitle = findViewById(R.id.tv_normal_title_title)
        tvMore = findViewById(R.id.tv_normal_title_more)
        ivBack = findViewById(R.id.iv_normal_title_back)
        ivMore = findViewById(R.id.iv_normal_title_more)
        tvTitle.text = "高端发布"
        ivMore.visibility = View.GONE
        tvMore.visibility = View.VISIBLE
        tvMore.text = "提交"




        ivBack.setOnClickListener { finish() }
        tvMore.setOnClickListener { submitInfo() }

        tvUserName = findViewById(R.id.tv_name)
        tvUserCity = findViewById(R.id.tv_city)
        tvUserSexAndAge = findViewById(R.id.tv_user_info_sex_and_age)
        ivIcon = findViewById(R.id.iv_item_high_end_auth_icon)
        ivCaichan = findViewById(R.id.iv_caichan)
        ivJiankang = findViewById(R.id.iv_jiankang)
        ivXueli = findViewById(R.id.iv_xueli)
        ivJineng = findViewById(R.id.iv_jineng)
        ivQita = findViewById(R.id.iv_qita)


        ivRealId = findViewById(R.id.iv_realId)
        ivRealName = findViewById(R.id.iv_realName)
        ivVidoeAuth = findViewById(R.id.iv_vidoeAuth)

        rbAll = findViewById(R.id.rb_all)
        rbHigh = findViewById(R.id.rb_high)
        rbSelf = findViewById(R.id.rb_self)

        tvDesc = findViewById(R.id.tv_desc)
        tvRecommd = findViewById(R.id.tv_red_desc)

        radioGroup = findViewById(R.id.radioGroup)

        llRedNone = findViewById(R.id.ll_redNone)
        recycleView = findViewById(R.id.recycleView)

        llRedService = findViewById(R.id.ll_red_service)
        llRedMatch = findViewById(R.id.ll_red_match)

        constrainUserInfo = findViewById(R.id.constarinUserInfo)
        tvMatch = findViewById(R.id.tv_match)

        photoLayout = findViewById(R.id.photo_layout_normal_deal)

        constrainUserInfo.setOnClickListener {
            startActivity(Intent(this, UserInfoActivity::class.java).putExtra(IntentKey.UID, model?.chat_id))
        }





        radioGroup.setOnCheckedChangeListener { cb, checkedId ->
            when (checkedId) {
                R.id.rb_all -> {
                    topPhotoStates = "1"
                    if (cb.isPressed) {
                        updatePhotoPermisson("1")
                    }

                }
                R.id.rb_high -> {
                    topPhotoStates = "2"
                    if (cb.isPressed) {
                        updatePhotoPermisson("2")
                    }

                }
                R.id.rb_self -> {
                    topPhotoStates = "3"
                    if (cb.isPressed) {
                        updatePhotoPermisson("3")
                    }

                }

            }
        }

        tvMatch.setOnClickListener {
            val pop = HighAddUserPop(this@PushHighActivity, "红娘牵线服务","请填写匹配用户ID",uid,"1")
            pop.showPopupWindow()

        }



        with(photoLayout) {
            maxItemCount = 9
            isEditable = true
            isPlusEnable = true
            isSortable = true
            setDelegate(object : BGASortableNinePhotoLayout.Delegate {
                override fun onClickAddNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout?, view: View?, position: Int, models: ArrayList<String>?) {
                    onAddPicture()
                }

                override fun onClickDeleteNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout?, view: View?, position: Int, model: String?, models: ArrayList<String>?) {
                    photoLayout.removeItem(position)
                }

                override fun onClickNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout?, view: View?, position: Int, model: String?, models: ArrayList<String>?) {
                    val photoPickerPreviewIntent = BGAPhotoPickerPreviewActivity.IntentBuilder(this@PushHighActivity)
                            .previewPhotos(models) // 当前预览的图片路径集合
                            .selectedPhotos(models) // 当前已选中的图片路径集合
                            .maxChooseCount(photoLayout.maxItemCount - photoLayout.itemCount) // 图片选择张数的最大值
                            .currentPosition(position) // 当前预览图片的索引
                            .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                            .build()
                    startActivityForResult(photoPickerPreviewIntent, REQUEST_CODE_PHOTO_PREVIEW)
                }

                override fun onNinePhotoItemExchanged(sortableNinePhotoLayout: BGASortableNinePhotoLayout?, fromPosition: Int, toPosition: Int, models: ArrayList<String>?) {
                }
            })
        }

        getDetailInfo()


    }

    private fun getDetailInfo() {
        HttpHelper.getInstance().getHighUserDetail(uid, "1", object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val userInfoBean = GsonUtil.GsonToBean(data, HighUserModel::class.java)
                userInfoBean?.data?.run {
                    showUserInfo(this)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })

    }

    private fun getTopMatchList() {
        HttpHelper.getInstance().getMatchList(uid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                redwomendata = Gson().fromJson(data, RedWomenIntroData::class.java)
                redwomendata?.data?.run {
                    if (this != null && this.size > 0) {
                        llRedNone.visibility = View.GONE
                        recycleView.visibility = View.VISIBLE
                        redwomenNewAdapter = RedwomenNewListAdapter(this@PushHighActivity, this, "0")
                        recycleView.layoutManager = LinearLayoutManager(this@PushHighActivity)
                        recycleView.adapter = redwomenNewAdapter
                        redwomenNewAdapter?.setRedwomenClick(object : OnRedwomenClickListener {
                            override fun onRedwomenClick(position: Int) {

                            }

                            override fun onRedwomenClick(position: Int, remarks: String) {
                                if (TextUtil.isEmpty(remarks)) {
                                    "请填写备注".showToast()
                                    return
                                }
                                editRemarks(remarks, redwomendata?.data!![position].mid, position)
                            }

                        })
                    } else {
                        llRedNone.visibility = View.VISIBLE
                        recycleView.visibility = View.GONE
                    }
                }

            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    private fun editRemarks(remarks: String, redid: String, position: Int) {
        val map: MutableMap<String, String> = HashMap()
        map["login_uid"] = MyApp.uid
        map["id"] = redid
        map["remarks"] = remarks
        val manager = RequestFactory.getRequestManager()
        manager.post(HttpUrl.EditRemarks, map, object : IRequestCallback {
            override fun onSuccess(response: String) {
                redwomendata?.data?.get(position)?.remarks = remarks
                redwomenNewAdapter?.notifyItemChanged(position, remarks)
                ToastUtil.show("添加成功！")
            }

            override fun onFailure(throwable: Throwable) {
                ToastUtil.show("添加失败！")

            }
        })
    }


    private fun updatePhotoPermisson(status: String) {
        HttpHelper.getInstance().updatePushHighPhoto(uid, status, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }


    fun showUserInfo(user: HighUserModel.DataBean) {
        model = HighUserModel().DataBean()
        model = user
        user.run {
            GlideImgManager.glideLoader(this@PushHighActivity, user_info.head_pic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivIcon)
            photoLayout.addMoreData(user.top_photo_arr)

            if (MyApp.uid == chat_id) {
                llRedService.visibility = View.GONE
                if (is_match == "1") {
                    llRedMatch.visibility = View.VISIBLE
                } else {
                    llRedMatch.visibility = View.GONE
                }

            } else {
                if (MyApp.isAdmin == "1") {
                    llRedService.visibility = View.VISIBLE
                    getTopMatchList()
                    if (is_match == "1"){
                        tvMatch.visibility =View.VISIBLE
                    }else {
                        tvMatch.visibility =View.GONE
                    }

                } else {
                    tvMatch.visibility =View.GONE
                    llRedService.visibility = View.GONE
                }

            }




            when (user.top_photo_auth) {
                "1" -> {
                    rbAll.isChecked = true
                }
                "2" -> {
                    rbHigh.isChecked = true
                }
                "3" -> {
                    rbSelf.isChecked = true
                }
            }

            tvUserName.text = serial_id
            tvUserCity.text = if (user_info.province == user_info.city) {
                user_info.province
            } else {
                "${user_info.province}  ${user_info.city}"
            }
            if (TextUtil.isEmpty(top_desc)) {
                tvDesc.hint = "请完善内心独白"
            } else {
                tvDesc.text = top_desc
            }


            if (!TextUtil.isEmpty(top_red_desc)) {
                tvRecommd.text = top_red_desc
            } else {
                tvRecommd.hint = "等待红娘为您撰写"
            }

            var drawable: Drawable
            when (user_info.sex) {
                "1" -> {
                    drawable = ResourceUtils.getResources().getDrawable(R.mipmap.nan)
                    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                    tvUserSexAndAge.setCompoundDrawables(drawable, null, null, null)
                    tvUserSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy)
                }
                "2" -> {
                    drawable = ResourceUtils.getResources().getDrawable(R.mipmap.nv)
                    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                    tvUserSexAndAge.setCompoundDrawables(drawable, null, null, null)
                    tvUserSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl)
                }
                else -> {
                    drawable = ResourceUtils.getResources().getDrawable(R.mipmap.san)
                    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                    tvUserSexAndAge.setCompoundDrawables(drawable, null, null, null)
                    tvUserSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts)
                }
            }
            tvUserSexAndAge.text = if (user_info.age.isEmpty()) "0" else user_info.age


            if (user_info.realname == "1") {
                ivRealId.visibility = View.VISIBLE
            } else {
                ivRealId.visibility = View.GONE
            }
            if (user_info.video_auth_status == "1") {
                ivVidoeAuth.visibility = View.VISIBLE
            } else {
                ivVidoeAuth.visibility = View.GONE
            }
            if (user_info.realids == "1") {
                ivRealName.visibility = View.VISIBLE
            } else {
                ivRealName.visibility = View.GONE
            }

            if (top_cc_status == "1") {
                ivCaichan.visibility = View.VISIBLE
            } else {
                ivCaichan.visibility = View.GONE
            }
            if (top_jk_status == "1") {
                ivJiankang.visibility = View.VISIBLE
            } else {
                ivJiankang.visibility = View.GONE
            }
            if (top_xl_status == "1") {
                ivXueli.visibility = View.VISIBLE
            } else {
                ivXueli.visibility = View.GONE
            }
            if (top_jn_status == "1") {
                ivJineng.visibility = View.VISIBLE
            } else {
                ivJineng.visibility = View.GONE
            }
            if (top_qt_status == "1") {
                ivQita.visibility = View.VISIBLE
            } else {
                ivQita.visibility = View.GONE
            }

        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun helloEventBus(event: RedWomenJsEvent) {
        redwomendata?.data?.get(event.position)?.remarks = event.redJs
        redwomenNewAdapter?.notifyDataSetChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun  postNotifyMatch(event:AddHighUserEven){
        getTopMatchList()
    }

    private fun submitInfo() {
        if (photoLayout.data.isEmpty()) {
            "请上传个人照片".showToast()
            return
        }
        uploadImg()

    }


    private fun showSelectAlertView() {
        AlertView(null, null, "取消", null, arrayOf("选择相册"), this, AlertView.Style.ActionSheet) { _, _, _ ->
            openPhotoWrapper()
        }.show()
    }

    private fun onAddPicture() {
        if (XXPermissions.isGranted(this, Manifest.permission.CAMERA)) {
            showSelectAlertView()
        } else {
            requestPermissions()
        }
    }

    fun openPhotoWrapper() {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        val takePhotoDir = File(Environment.getExternalStorageDirectory(), "Shengmo")
        val photoPickerIntent = BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                .maxChooseCount(photoLayout.maxItemCount - photoLayout.itemCount) // 图片选择张数的最大值
                .selectedPhotos(null) // 当前已选中的图片路径集合
                .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                .build()
        startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_PHOTO)
    }

    fun requestPermissions() {
        XXPermissions.with(this)
                .permission(Manifest.permission.CAMERA)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            openPhotoWrapper()
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        //super.onDenied(permissions, never)
                        val toastStr = StringBuilder().append("权限:")
                        with(toastStr) {
                            if (permissions?.contains(Manifest.permission.CAMERA) == true) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_CHOOSE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    photoLayout.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data))
                }
            }
            REQUEST_CODE_PHOTO_PREVIEW -> {
                photoLayout.data = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data)
            }
            XXPermissions.REQUEST_CODE -> {
                if (XXPermissions.isGranted(this, Manifest.permission.CAMERA) &&
                        XXPermissions.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    "权限授予成功，可以使用相册功能了".showToast()
                }
            }
        }
    }

    lateinit var uploadImgList: ArrayList<String>
    private fun uploadImg() {
        val uploadImgList: ArrayList<String> = ArrayList()
        val oldList: ArrayList<String> = photoLayout.data.filter {
            if (!it.startsWith("http")) {
                uploadImgList.add(it)
            }
            it.startsWith("http")
        } as java.util.ArrayList<String>
        showloadPop()
        uploadImg(uploadImgList, object : UploadImgCallback {
            override fun callback(list: java.util.ArrayList<String>) {
                oldList.addAll(list)
                val desc = tvDesc.text.toString().trim()
                val recommd = tvRecommd.text.toString().trim()
                val photoString = oldList.toStr()
                val used = if (MyApp.isAdmin == "1") {
                    uid
                } else {
                    ""
                }
                HttpHelper.getInstance().submitHighInfo(used, photoString, topPhotoStates, desc, recommd, object : HttpCodeMsgListener {
                    override fun onSuccess(data: String?, msg: String?) {
                        dimissPop()
                        finish()
                        msg?.showToast()

                    }

                    override fun onFail(code: Int, msg: String?) {
                        dimissPop()
                        msg?.showToast()
                    }
                })

            }
        })


    }

    private fun uploadImg(imagesList: java.util.ArrayList<String>, uploadImgCallback: UploadImgCallback) {
        val uploadList: java.util.ArrayList<String> = java.util.ArrayList()
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

    var loadingPop: LoadingPop? = null
    private fun showloadPop() {
        if (loadingPop == null) {
            loadingPop = LoadingPop(this, "正在提交...")
        }
        loadingPop?.showPopupWindow()
    }

    private fun dimissPop() {
        if (loadingPop != null) {
            loadingPop?.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun setCanNotEditAndClick(view: View) {
        view.isFocusable = false;
        view.isFocusableInTouchMode = false;
        view.setOnClickListener {
            "等待红娘为您撰写".showToast()
            return@setOnClickListener
        }
    }

}
