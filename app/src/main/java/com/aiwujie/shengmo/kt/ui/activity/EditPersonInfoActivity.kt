package com.aiwujie.shengmo.kt.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.EditIntroduceActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.BeanIcon
import com.aiwujie.shengmo.bean.OwnerMsgData
import com.aiwujie.shengmo.bean.SecretStateData
import com.aiwujie.shengmo.eventbus.DynamicUpMediaEditDataEvent
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.listener.UploadImgCallback
import com.aiwujie.shengmo.kt.ui.view.HeightAndWeightPop
import com.aiwujie.shengmo.kt.ui.view.HeightAndWeightPop.OnHeightAndWeightListener
import com.aiwujie.shengmo.kt.util.*
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.net.IRequestCallback
import com.aiwujie.shengmo.net.RequestFactory
import com.aiwujie.shengmo.utils.*
import com.donkingliang.imageselector.utils.ImageSelector
import com.donkingliang.labels.LabelsView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import kotlinx.android.synthetic.main.activity_edit_person_msg.*
import org.feezu.liuli.timeselector.TimeSelector
import org.feezu.liuli.timeselector.Utils.TextUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.lang.ref.WeakReference
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class EditPersonInfoActivity : BaseActivity() {


    lateinit var tvTitle: TextView
    lateinit var ivBack: ImageView
    lateinit var tvMore: TextView
    lateinit var ivMore: ImageView
    lateinit var sdf: SimpleDateFormat

    lateinit var tvName: TextView
    lateinit var tvSign: TextView
    lateinit var tvBirthday: TextView
    lateinit var tvHighWeigh: TextView
    lateinit var tvPhotoOpen: TextView

    lateinit var gayLayout: LinearLayout
    lateinit var lesLayout: LinearLayout
    lateinit var llVip: LinearLayout

    lateinit var ivIcon: ImageView
    lateinit var photoLayout: BGASortableNinePhotoLayout
    lateinit var mEdit_auth_btn01: Button
    lateinit var mEdit_auth_btn02: Button

    lateinit var sexLabel: LabelsView
    lateinit var sexToLabel: LabelsView
    lateinit var roleLabel: LabelsView
    lateinit var gayLabel: LabelsView
    lateinit var lesLabel: LabelsView
    lateinit var timesLabel: LabelsView
    lateinit var havenLabel: LabelsView
    lateinit var levelLabel: LabelsView
    lateinit var wantLabel: LabelsView
    lateinit var degreeLabel: LabelsView
    lateinit var moneyLabel: LabelsView

    lateinit var howLongLayout: LinearLayout
    lateinit var havetryLayout: LinearLayout
    lateinit var levelLayout: LinearLayout


    //传过来的uid
    private var uid: String? = null

    //当前用户的vip状态
    private val vip: String? = null
    private var photo_ruleflag: String? = null
    private var currentTime = ""
    private var photo_rule: String? = null
    private var imgpre: String? = null
    private var picString = ""
    private val REQUEST_CODE_CHOOSE_PHOTO = 1

    //删除图片集合
    private val deleteUrl: List<String> = java.util.ArrayList()

    //  这里是定义label数据
    private var localSexList = listOf<String>("男", "女", "CDTS")
    private var localRoleList = listOf<String>("斯", "慕", "双", "~", "非斯慕同好")
    private var localGayList = listOf<String>("1", "0", "0.5", "~")
    private var localLesList = listOf<String>("T", "P", "H", "~")
    private val localTimes = listOf("1年及以下", "2-3年", "4-6年", "7-10年", "10-20年", "20年及以上")
    private val localEducations = listOf("高中及以下", "大专", "本科", "双学士", "硕士", "博士", "博士后")
    private val localSalary = listOf("2千以下", "2千-5千", "5千-1万", "1万-2万", "2万-5万", "5万以上")
    private val localHavenList = listOf("有", "无")
    private val localLevelList = listOf("轻度", "中度", "重度")
    private val localWantList = listOf("聊天", "现实", "结婚")
    private var picFlag = 0

    // 这里是接收数据
    private var mNickname = ""
    private var headurldele: String? = ""
    private var mIntroduce: String? = ""
    private var mSex = "0"
    private var mRole = ""
    private var targetAttribute = ""
    private var mAttribute = ""
    private var mAlong = ""
    private var mExperience = ""
    private var mCulture = ""
    private var mMonthly = ""
    private var mWant = ""
    private var mLvel = ""
    private val heights: ArrayList<String> = ArrayList()
    private val weights: ArrayList<String> = ArrayList()
    private val levelList: ArrayList<String> = ArrayList()
    private val sexList: ArrayList<String> = ArrayList()
    private val wantList: ArrayList<String> = ArrayList()
    private var sexual = ""
    private var headurl = ""
    private var height = ""
    private var mWeight = ""
    private var picList = ArrayList<String>()
    private var heigthAndWeight = ""

    //是否可以修改昵称
    private val changeState = ""

    //程度集合
    private val upLevelList: ArrayList<String> = ArrayList()

    //性取向集合
    private val upSexList: ArrayList<String> = ArrayList()

    //想找集合
    private val upWantList: ArrayList<String> = ArrayList()
    val REQUEST_CODE_PHOTO_PREVIEW = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.showLightStatusBar(this)
        setContentView(R.layout.app_activity_edit_person_info)
        EventBus.getDefault().register(this)
        tvTitle = findViewById(R.id.tv_normal_title_title)
        tvMore = findViewById(R.id.tv_normal_title_more)
        ivBack = findViewById(R.id.iv_normal_title_back)
        ivMore = findViewById(R.id.iv_normal_title_more)
        tvSign = findViewById(R.id.mEdit_personmsg_qianming)
        tvName = findViewById(R.id.mEdit_personmsg_nickname)
        tvBirthday = findViewById(R.id.mEdit_personmsg_birthday)
        tvHighWeigh = findViewById(R.id.mEdit_personmsg_heightAndweight)
        gayLayout = findViewById(R.id.gay_layout)
        lesLayout = findViewById(R.id.les_layout)
        sexLabel = findViewById(R.id.sexLabel)
        sexToLabel = findViewById(R.id.sexToLabel)
        roleLabel = findViewById(R.id.roleLabel)
        gayLabel = findViewById(R.id.gayLabel)
        lesLabel = findViewById(R.id.lesLabel)
        timesLabel = findViewById(R.id.timesLabel)
        havenLabel = findViewById(R.id.haveLabel)
        levelLabel = findViewById(R.id.levelLabel)
        wantLabel = findViewById(R.id.wantLabel)
        degreeLabel = findViewById(R.id.degreeLabel)
        moneyLabel = findViewById(R.id.moneyLabel)
        ivIcon = findViewById(R.id.mEdit_personmsg_icon)
        photoLayout = findViewById(R.id.ninelayout_edit_photo)
        mEdit_auth_btn01 = findViewById(R.id.mEdit_auth_btn01)
        mEdit_auth_btn02 = findViewById(R.id.mEdit_auth_btn02)
        tvPhotoOpen = findViewById(R.id.mEdit_personmsg_isOpen)
        llVip = findViewById(R.id.mEdit_personmsg_VipLl)

        howLongLayout = findViewById(R.id.howlong_layout)
        havetryLayout = findViewById(R.id.havetry_layout)
        levelLayout = findViewById(R.id.level_layout)
        tvTitle.text = "修改资料"
        ivMore.visibility = View.GONE
        tvMore.visibility = View.VISIBLE
        tvMore.text = "提交"

        initData()
        setData()
        setListener()
        getPersonInfo()
        getSecretSit()
    }


    fun initData() {
        uid = intent.getStringExtra("otheruid")
        sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
//        currentTime=sdf.format(new Date());
        //        currentTime=sdf.format(new Date());
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val mYear = calendar[Calendar.YEAR]
        val mMonth = calendar[Calendar.MONTH]
        val mDay = calendar[Calendar.DAY_OF_MONTH]
        val mHour = calendar[Calendar.HOUR]
        val mMinute = calendar[Calendar.MINUTE]
        try {
            val date1: Date = sdf.parse((mYear - 18).toString() + "-" + (mMonth + 1) + "-" + mDay + " " + mHour + ":" + mMinute)
            currentTime = sdf.format(date1)
        } catch (e: ParseException) {
            e.printStackTrace()
        }


        for (i in 0..101) {
            heights.add((120 + i).toString() + "cm")
        }
        for (i in 0..170) {
            weights.add((30 + i).toString() + "kg")
        }
        imgpre = SharedPreferencesUtils.geParam(this, "image_host", "")

        photoLayout.setDelegate(object : BGASortableNinePhotoLayout.Delegate {
            override fun onClickAddNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, models: java.util.ArrayList<String>) {
                picFlag = 10
                showSelectPic()
            }

            override fun onClickDeleteNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, model: String, models: java.util.ArrayList<String>) {
                // addOrDeletePic();
                picList.removeAt(position)
                photoLayout.removeItem(position)
            }

            override fun onClickNinePhotoItem(sortableNinePhotoLayout: BGASortableNinePhotoLayout, view: View, position: Int, model: String, models: java.util.ArrayList<String>) {
                val photoPickerPreviewIntent = BGAPhotoPickerPreviewActivity.IntentBuilder(this@EditPersonInfoActivity)
                        .previewPhotos(models) // 当前预览的图片路径集合
                        .selectedPhotos(models) // 当前已选中的图片路径集合
                        .maxChooseCount(photoLayout.maxItemCount) // 图片选择张数的最大值
                        .currentPosition(position) // 当前预览图片的索引
                        .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                        .build()
                startActivityForResult(photoPickerPreviewIntent, REQUEST_CODE_PHOTO_PREVIEW)

            }

            override fun onNinePhotoItemExchanged(sortableNinePhotoLayout: BGASortableNinePhotoLayout, fromPosition: Int, toPosition: Int, models: ArrayList<String>) {
                models.let {
                    picList.clear()
                    picList.addAll(it)
                }
            }
        })
    }


    fun setListener() {

        tvMore.setOnClickListener {
            isSave()
        }
        ivBack.setOnClickListener {
            isSave()
        }

        ivIcon.setOnClickListener {
            picFlag = 0
            showSelectPic()
        }

        tvName.setOnClickListener {
            //如果uid和MyApp.uid相等，说明是自己进入修改资料页，反之则是官方人员进入修改资料页。
            if (uid == MyApp.uid) {
                if (changeState != "1") {
                    intent = Intent(this, EditNameActivity::class.java)
                    intent.putExtra(IntentKey.NAME, mNickname)
                    startActivityForResult(intent, NormalConstant.REQUEST_CODE_1)
                } else {
                    ToastUtil.show("您的本月修改昵称次数已达上限（修改昵称次数/月：普通用户1次,会员3次）")
                }
            } else {
                intent = Intent(this, EditNameActivity::class.java)
                intent.putExtra(IntentKey.NAME, tvName.text.toString())
                startActivityForResult(intent, NormalConstant.REQUEST_CODE_1)
            }
        }

        sexLabel.setOnLabelSelectChangeListener { _, _, isSelect, position ->
            if (isSelect) {
                if (position == 0) {
                    mSex = "1"
                    lesLayout.visibility = View.GONE
                    if (sexList.contains("1")) {
                        targetAttribute = "gay"
                        gayLayout.visibility = View.VISIBLE
                    } else {
                        gayLayout.visibility = View.GONE
                        if (gayLayout.visibility == View.GONE && lesLayout.visibility == View.GONE) targetAttribute = ""
                    }
                } else if (position == 1) {
                    mSex = "2"
                    gayLayout.visibility = View.GONE
                    if (sexList.contains("2")) {
                        targetAttribute = "les"
                        lesLayout.visibility = View.VISIBLE
                    } else {
                        lesLayout.visibility = View.GONE
                        if (gayLayout.visibility == View.GONE && lesLayout.visibility == View.GONE) targetAttribute = ""
                    }
                } else if (position == 2) {
                    gayLayout.visibility = View.GONE
                    lesLayout.visibility = View.GONE
                    targetAttribute = ""
                    mSex = "3"
                }
            }
        }

        sexToLabel.setOnLabelSelectChangeListener { _, _, isSelect, position ->
//            (position.toString() + isSelect).showToast()
            if (position == 0) {
                if (isSelect) {
                    if (!sexList.contains("1")) {
                        sexList.add("1")
                    }
                    if (mSex == "1") {
                        mAttribute = "gay"
                        gayLayout.visibility = View.VISIBLE
                        lesLayout.visibility = View.GONE
                        when (mAttribute) {
                            "1" -> gayLabel.setSelects(0)
                            "0" -> gayLabel.setSelects(1)
                            "0.5" -> gayLabel.setSelects(2)
                            "~" -> gayLabel.setSelects(3)
                        }
                    }
                } else {
                    sexList.remove("1")
                    gayLayout.visibility = View.GONE
                }
            } else if (position == 1) {

                if (isSelect) {
                    if (!sexList.contains("2")) {
                        upSexList.add("2")
                    }
                    if (mSex == "2") {
                        mAttribute = "les"
                        gayLayout.visibility = View.GONE
                        lesLayout.visibility = View.VISIBLE
                        when (mAttribute) {
                            "T" -> lesLabel.setSelects(0)
                            "P" -> lesLabel.setSelects(1)
                            "H" -> lesLabel.setSelects(2)
                            "~" -> lesLabel.setSelects(3)
                        }
                    }
                } else {
                    sexList.remove("2")
                    lesLayout.visibility = View.GONE
                }
            } else if (position == 2) {
                gayLayout.visibility = View.GONE
                lesLayout.visibility = View.GONE
                if (isSelect) {
                    if (!sexList.contains("3")) {
                        sexList.add("3")
                    }
                    mAttribute = ""
                } else {
                    sexList.add("3")
                }
            }
        }



        tvSign.setOnClickListener {
            val intent1 = Intent(this, EditIntroduceActivity::class.java)
            intent1.putExtra("introduce", mIntroduce)
            startActivityForResult(intent1, NormalConstant.REQUEST_CODE_2)
        }

        tvHighWeigh.setOnClickListener { showHeightAndWeightPop() }
        tvBirthday.setOnClickListener {
            // 参数说明：ResultHandler为选取时间后的回调 startDate，endDate为时间控件的可选起始时间和结束时间。
            val timeSelector = TimeSelector(this, TimeSelector.ResultHandler { time ->
                val times = time.substring(0, time.length - 6)
                //                        birthday = times;
                tvBirthday.text = times
                //                        ToastUtil.show(getApplicationContext(),times);
            }, "1950-01-01 00:00", currentTime)
            timeSelector.setMode(TimeSelector.MODE.YMD)
            timeSelector.show()
        }

        mEdit_auth_btn01.setOnClickListener {
            mEdit_auth_btn01.isSelected = true
            mEdit_auth_btn02.isSelected = false
            //设置标记  上传接口用
            photo_ruleflag = "0"
            tvMore.visibility = View.VISIBLE//提交按钮可见
        }

        mEdit_auth_btn02.setOnClickListener {
            mEdit_auth_btn02.isSelected = true
            mEdit_auth_btn01.isSelected = false
            //设置标记  上传接口用
            photo_ruleflag = "1"  //1就是限制
            tvMore.visibility = View.VISIBLE//提交按钮可见
        }


    }

    private fun getPersonInfo() {
        HttpHelper.getInstance().getEditPersonInfo(uid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val data: OwnerMsgData = Gson().fromJson<OwnerMsgData>(data, OwnerMsgData::class.java)
                data.data.run {
                    tvName.text = nickname
                    tvSign.text = this.introduce
                    tvBirthday.text = this.birthday
                    tvHighWeigh.text = this.tall + "cm" + "/" + this.weight + "kg"
                    mNickname = nickname
                    changeState = this.changeState
                    mWeight = weight
                    height = this.tall
                    headurldele = this.head_pic
                    mIntroduce = this.introduce
                    mSex = this.sex
                    mAttribute = this.attribute
                    mRole = this.role
                    mAlong = this.along
                    mExperience = this.experience
                    mCulture = this.culture
                    mMonthly = this.monthly

                    if (vip == "1" || "1" == svip || "1" == is_admin) {
                        llVip.visibility = View.VISIBLE
                        photoLayout.maxItemCount = 15 //vip可以上传15张
                    } else {
                        photoLayout.maxItemCount = 6 //普通用户上传6张
                    }

                    GlideImgManager.glideLoader(this@EditPersonInfoActivity, headurldele, R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivIcon, 0)
                    picList.clear()
                    for (str in this.photo) {
                        picList.add(imgpre + str)
                    }
                    photoLayout.addMoreData(picList)

                    if (this.sexual.contains(",")) {
                        for (str in this.sexual.split(",")) {
                            sexList.add(str)
                        }
                    } else {
                        if (!TextUtil.isEmpty(this.sexual)) {
                            sexList.add(this.sexual)
                        }
                    }

                    if (this.level.contains(",")) {
                        for (t in this.level.split(",".toRegex()).toTypedArray()) {
                            levelList.add(t)
                        }
                    } else {
                        if (!TextUtil.isEmpty(this.level)){
                            levelList.add(this.level)
                        }

                    }

                    if (this.want.contains(",")) {
                        for (t in this.want.split(",".toRegex()).toTypedArray()) {
                            wantList.add(t)
                        }
                    } else {
                        if (!TextUtil.isEmpty(this.want)) {
                            wantList.add(this.want)
                        }

                    }


                    if ((sex == "1" && sexList.contains("1"))) {
                        gayLayout.visibility = View.VISIBLE
                        lesLayout.visibility = View.GONE
                        targetAttribute = "gay"
                        when (attribute) {
                            "1" -> gayLabel.setSelects(0)
                            "0" -> gayLabel.setSelects(1)
                            "0.5" -> gayLabel.setSelects(2)
                            "~" -> gayLabel.setSelects(3)
                        }
                    } else if (sex == "2" && sexList.contains("2")) {
                        gayLayout.visibility = View.GONE
                        lesLayout.visibility = View.VISIBLE
                        targetAttribute = "les"
                        when (attribute) {
                            "T" -> lesLabel.setSelects(0)
                            "P" -> lesLabel.setSelects(1)
                            "H" -> lesLabel.setSelects(2)
                            "~" -> lesLabel.setSelects(3)

                        }
                    }
                    sexLabel.showSelect(sex)
                    roleLabel.run {
                        when (role) {
                            "S" -> setSelects(0)
                            "M" -> setSelects(1)
                            "SM" -> setSelects(2)
                            "~" -> setSelects(3)
                            "-" -> {
                                setSelects(4)
                                howLongLayout.visibility = View.GONE
                                havetryLayout.visibility = View.GONE
                                levelLayout.visibility = View.GONE
                            }

                        }
                    }
                    sexToLabel.showListSelect(sexList)
                    timesLabel.showSelect(along)
                    havenLabel.showSelect(experience)
                    levelLabel.showListSelect(levelList)
                    wantLabel.showListSelect(wantList)
                    degreeLabel.showSelect(culture)
                    moneyLabel.showSelect(monthly)


                }

            }

            override fun onFail(code: Int, msg: String?) {
                    msg?.showToast()
            }
        })
    }


    private fun showSelectPic() {
        requestPermission()

    }

    var handler = MyHandler(this@EditPersonInfoActivity)

    public class MyHandler(activity: EditPersonInfoActivity) : Handler() {
        val activityWeakReference: WeakReference<EditPersonInfoActivity>
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val activity = activityWeakReference.get()
            if (msg.what == PhotoUploadTask.CODE_UPLOAD_SUC) {
                val s = msg.obj as String
                val beanicon = Gson().fromJson(s, BeanIcon::class.java)
                if (activity!!.picFlag == 0) { //头像
                    activity.headurl = beanicon.data
                    ImageLoader.loadCircleImage(activity, activity.imgpre + activity.headurl, activity.ivIcon)
                } else if (activity.picFlag == 10) { //相册
                    val imgurl = beanicon.data
                    activity.picList.add(activity.imgpre + imgurl)
                    val temp = java.util.ArrayList<String>()
                    temp.add(activity.imgpre + imgurl)
                    activity.photoLayout.addMoreData(temp)
                }
                ToastUtil.show(activity, "上传完成")
            }
        }

        init {
            activityWeakReference = WeakReference(activity)
        }
    }


    private fun isSave() {
        if (tvMore.visibility == View.VISIBLE) {  //判断体提交按钮是否显示
            val builder = AlertDialog.Builder(this)
            builder.setMessage("确认修改吗?")
                    .setPositiveButton("否") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }.setNegativeButton("是") { _, _ ->
                        handler.post(Runnable { //确认修改

                            if (sexual != null && mWant != null) {
                                confirmEdit()
                            } else {
                                ToastUtil.show(applicationContext, "请您将个人信息填写完整")
                            }
                        })
                    }.create().show()
        } else {
            finish()
        }
    }

    private fun confirmEdit() { //提交修改
        if (SafeCheckUtil.isActivityFinish(this@EditPersonInfoActivity)) {
            return
        }
        var introduce = tvSign.text.toString().trim()
        var nickName = tvName.text.toString().trim()
        var birthday = tvBirthday.text.toString().trim()

        var sex = sexLabel.selectLabels.toStrAddOne()
        var sexual = sexToLabel.selectLabels.toStrAddOne()
        var attribute = targetAttribute.run {
            when (targetAttribute) {
                "gay" -> {
                    gayLabel.selectLabels.toRole(targetAttribute)
                }
                "les" -> {
                    lesLabel.selectLabels.toRole(targetAttribute)
                }
                else -> {
                    ""
                }
            }

        }
        var role = roleLabel.selectLabels.toRole("role")
        var along = timesLabel.selectLabels.toStrAddOne()
        var experience = havenLabel.selectLabels.toStrAddOne()
        var level = levelLabel.selectLabels.toStrAddOne()
        var want = wantLabel.selectLabels.toStrAddOne()
        var culture = degreeLabel.selectLabels.toStrAddOne()
        var monthly = moneyLabel.selectLabels.toStrAddOne()

        picString = ""
        val tt = if (TextUtil.isEmpty(imgpre)) "okokok" else imgpre!!
        if (picList.size != 0) {
            for (i in picList.indices) {
                picString += if (picList[i].contains(tt)) picList[i].split(tt.toRegex()).toTypedArray()[1] + "," else picList[i] + ","
            }
            if (!TextUtil.isEmpty(picString)) {
                picString = picString.substring(0, picString.length - 1)
            }
        }
        HttpHelper.getInstance().editPersonInfo(uid, introduce, "0", nickName,
                birthday, headurl, height, mWeight, role, sex, sexual, want, level, along, experience,
                culture, monthly, attribute as String?, photo_ruleflag, picString, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                if (headurl != "") {
                    deleteIcon(headurldele)
                }

                SharedPreferencesUtils.setParam(applicationContext, "mysex", sex)
                SharedPreferencesUtils.setParam(applicationContext, "mysexual", sexual)
                SharedPreferencesUtils.setParam(applicationContext, "mydynamicSex", sexual)
                SharedPreferencesUtils.setParam(applicationContext, "mydynamicSexual", sex)
                /*群组筛选*/
                /*群组筛选*/SharedPreferencesUtils.setParam(applicationContext, "mygroupSex", sex)
                SharedPreferencesUtils.setParam(applicationContext, "mygroupSexual", sexual)

                //更新性取向到本地数据库

                //更新性取向到本地数据库
                val index = SharedPreferencesUtils.getParam(this@EditPersonInfoActivity, "current_user_index", -1) as Int
                val daoSession = MyApp.getSwitchMarkBeanDao()
                val switchMarkBeanDao = daoSession.switchMarkBeanDao
                val switchMarkBeans = switchMarkBeanDao.loadAll()
                if (index == -1) {
                    for (i in switchMarkBeans.indices) {
                        if (switchMarkBeans[i].user_id == MyApp.uid) {
                            SharedPreferencesUtils.setParam(this@EditPersonInfoActivity, "current_user_index", i)
                            val switchMarkBean = switchMarkBeans[i]
                            switchMarkBean.sex = sex
                            switchMarkBean.sexual = sexual
                            switchMarkBeanDao.insertOrReplace(switchMarkBean)
                        }
                    }
                } else {
                    val switchMarkBean = switchMarkBeans[index]
                    switchMarkBean.sex = sex
                    switchMarkBean.sexual = sexual
                    switchMarkBeanDao.insertOrReplace(switchMarkBean)
                }

                val whatSexual = FilterGroupUtils.isWhatSexual(sex, sexual)
                SharedPreferencesUtils.setParam(applicationContext, "groupFlag", whatSexual)
                EventBus.getDefault().post(DynamicUpMediaEditDataEvent(2))
                EventBus.getDefault().post("editsuccess")
                setResult(200)
                finish()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }

        });
    }


    private fun deleteIcon(removeIcon: String?) {
        val map: MutableMap<String, String?> = HashMap()
        map["filename"] = removeIcon
        val manager = RequestFactory.getRequestManager()
        manager.post(HttpUrl.DeletePic, map, object : IRequestCallback {
            override fun onSuccess(response: String) {
//                Log.i("DeleteSuccess", "onSuccess: " + response);
            }

            override fun onFailure(throwable: Throwable) {}
        })
    }


    fun setData() {
        sexLabel.setLabels(localSexList)
        sexToLabel.setLabels(localSexList)
        roleLabel.setLabels(localRoleList)
        gayLabel.setLabels(localGayList)
        lesLabel.setLabels(localLesList)
        timesLabel.setLabels(localTimes)
        havenLabel.setLabels(localHavenList)
        levelLabel.setLabels(localLevelList)
        wantLabel.setLabels(localWantList)
        degreeLabel.setLabels(localEducations)
        moneyLabel.setLabels(localSalary)

        roleLabel.setOnLabelSelectChangeListener(object : LabelsView.OnLabelSelectChangeListener {
            override fun onLabelSelectChange(label: TextView?, data: Any?, isSelect: Boolean, position: Int) {
                if (position == 4) {
                    howLongLayout.visibility = View.GONE
                    havetryLayout.visibility = View.GONE
                    levelLayout.visibility = View.GONE
                } else {
                    howLongLayout.visibility = View.VISIBLE
                    havetryLayout.visibility = View.VISIBLE
                    levelLayout.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun showHeightAndWeightPop() {
        val heightAndWeightPop = HeightAndWeightPop(this)
        heightAndWeightPop.showPopupWindow()
        heightAndWeightPop.onHeightAndWeightListener = object : OnHeightAndWeightListener {
            override fun doChooseComplete(h: String, w: String) {
                height = h
                mWeight = w
                heigthAndWeight = "$height/$mWeight"
                tvHighWeigh.text = heigthAndWeight
            }
        }
    }


    private fun requestPermission() {
        XXPermissions.with(this)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            if (picFlag == 0) {
                                chooseHeaderIcon()
                            } else {
                                openPhotoWrapper()
                            }
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
        var maxValue = if (picFlag == 0) {
            1
        } else {
            photoLayout.maxItemCount - photoLayout.itemCount
        }
        val takePhotoDir = File(Environment.getExternalStorageDirectory(), "Shengmo")
        val photoPickerIntent = BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                .maxChooseCount(maxValue) // 图片选择张数的最大值
                .selectedPhotos(null) // 当前已选中的图片路径集合
                .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                .build()
        startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_PHOTO)
    }

    fun chooseHeaderIcon() {
        ImageSelector.builder()
                .useCamera(true)
                .setMaxSelectCount(1)
                .setCrop(true)
                .start(this, NormalConstant.REQUEST_CODE_3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CHOOSE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.v("uploadListSize", BGAPhotoPickerActivity.getSelectedPhotos(data).size.toString() + "")
                    if (picFlag == 0) {
                    } else {
                        photoLayout.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data))
                    }
                    uploadImage(BGAPhotoPickerActivity.getSelectedPhotos(data), object : UploadImgCallback {
                        override fun callback(list: ArrayList<String>) {
                            Log.v("UploadSuccess", list[0])
                            if (picFlag == 0) {
                                //头像
                                headurl = list[0]
                                ImageLoader.loadCircleImage(this@EditPersonInfoActivity, imgpre + headurl, ivIcon)
                            } else {

                                picList.addAll(list)
                            }
                        }
                    })
                }
            }
            REQUEST_CODE_PHOTO_PREVIEW -> {
                photoLayout.data = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data)
            }

            NormalConstant.REQUEST_CODE_1 -> {
                if (resultCode == NormalConstant.RESULT_CODE_OK) {
                    tvName.text = data?.getStringExtra("name")
                }

            }
            NormalConstant.REQUEST_CODE_2 -> {
                if (resultCode == NormalConstant.RESULT_CODE_OK) {
                    tvSign.text = data?.getStringExtra("introduce")
                }
            }

            NormalConstant.REQUEST_CODE_3 -> {
                if (data != null) {
                    //获取选择器返回的数据
                    val images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT)
                    uploadImage(images, object : UploadImgCallback {
                        override fun callback(list: ArrayList<String>) {
                            headurl = list[0]
                            ImageLoader.loadCircleImage(this@EditPersonInfoActivity, imgpre + headurl, ivIcon)
                        }
                    })
                }
            }

            XXPermissions.REQUEST_CODE
            -> {
                if (XXPermissions.isGranted(this, Manifest.permission.CAMERA) &&
                        XXPermissions.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    "权限授予成功，可以使用相册功能了".showToast()
                }
            }
        }

    }

    lateinit var uploadImgList: ArrayList<String>
    fun uploadImage(imagesList: ArrayList<String>?, uploadImgCallback: UploadImgCallback) {
        "图片上传中，请稍后".showToast()
        uploadImgList = ArrayList()
        var uploadCount = 0
        val singleThreadPool = Executors.newFixedThreadPool(5)
        for (str in imagesList!!) {
            singleThreadPool.execute {
                HttpHelper.getInstance().uploadImage(str, object : HttpCodeMsgListener {
                    override fun onSuccess(data: String?, msg: String?) {
                        val beanIcon = Gson().fromJson(data, BeanIcon::class.java)
                        beanIcon?.data?.run {
                            uploadCount++
                            Log.v("EditInfoHead", this)
                            uploadImgList.add(this)
                            if (uploadCount == imagesList.size) {
                                uploadImgCallback.callback(uploadImgList)
                            }
                        }
                    }

                    override fun onFail(code: Int, msg: String?) {
                        msg?.showToast()
                    }
                })
            }
        }


    }


    override fun onResume() {
        super.onResume()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun helloEventBus(message: String) {
        if (message == "setSecretSitSucc") {
            getSecretSit()
        }
    }

    private var photo_lock = ""
    private fun getSecretSit() { //获取当前隐私状态
        val map: MutableMap<String, String> = HashMap()
        map["uid"] = (if (uid == null) MyApp.uid else uid!!)!!
        val manager = RequestFactory.getRequestManager()
        manager.post(HttpUrl.GetSecretSit, map, object : IRequestCallback {
            override fun onSuccess(response: String) {
//                Log.i("secretactivity", "onSuccess: " + response);
                handler.post {
                    try {
                        val data = Gson().fromJson(response, SecretStateData::class.java)
                        photo_lock = data.data.photo_lock
                        //相册状态
                        photo_rule = data.data.photo_rule
                        if (photo_lock == "1") {
                            tvPhotoOpen.text = "未加密"
                        } else {
                            tvPhotoOpen.text = "加密"
                        }
                        ////////////////////////////////
                        if (photo_rule == "1") { //限制
                            photo_ruleflag = "1"
                            mEdit_auth_btn01.isSelected = false
                            mEdit_auth_btn02.isSelected = true
                        } else { //所有人可见
                            mEdit_auth_btn01.isSelected = true
                            mEdit_auth_btn02.isSelected = false
                            photo_ruleflag = "0"
                        }
                    } catch (e: JsonSyntaxException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(throwable: Throwable) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}


