package com.aiwujie.shengmo.kt.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.*
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity
import com.aiwujie.shengmo.activity.newui.VipMemberCenterActivity
import com.aiwujie.shengmo.activity.user.UserInfoActivity
import com.aiwujie.shengmo.adapter.GroupMemberAdapter
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.base.BaseActivity
import com.aiwujie.shengmo.bean.GroupInfoData
import com.aiwujie.shengmo.bean.GroupInfoMemberData
import com.aiwujie.shengmo.customview.MyGridview
import com.aiwujie.shengmo.eventbus.FinishConversationEvent
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.bean.NormalShareBean
import com.aiwujie.shengmo.kt.ui.view.GroupBuyTicketPop
import com.aiwujie.shengmo.kt.ui.view.NormalSharePop
import com.aiwujie.shengmo.kt.util.*
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpCodeMsgListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.net.HttpListener
import com.aiwujie.shengmo.tim.chat.ChatActivity
import com.aiwujie.shengmo.tim.utils.Constants
import com.aiwujie.shengmo.utils.*
import com.aiwujie.shengmo.view.GroupClaimPop
import com.aiwujie.shengmo.view.NormalTipsPop
import com.bigkoo.alertview.AlertView
import com.hb.dialog.myDialog.MyAlertInputDialog
import com.tencent.imsdk.v2.*
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import org.greenrobot.eventbus.EventBus
import java.util.*


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.activity
 * @ClassName: GroupDetailActivity
 * @Author: xmf
 * @CreateDate: 2022/4/13 10:06
 * @Description:
 */
class GroupDetailActivity : BaseActivity() {
    private lateinit var llGroupInfo: LinearLayout
    private lateinit var llMember: LinearLayout
    private lateinit var llAddress: LinearLayout
    private lateinit var llIntroduce: LinearLayout
    private lateinit var llAtAll: LinearLayout
    private lateinit var llVoice: LinearLayout
    private lateinit var llAutoPass: LinearLayout
    private lateinit var llNameCard: LinearLayout
    private lateinit var llClearMember: LinearLayout
    private lateinit var llClearChat: LinearLayout
    private lateinit var llSetManager: LinearLayout
    private lateinit var btOperate: Button
    private lateinit var btChat: Button

    private lateinit var ivLogo: ImageView
    private lateinit var ivLevel: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvNumber: TextView
    private lateinit var tvCreateTime: TextView
    private lateinit var ivGroupInfo: ImageView
    private lateinit var tvGroupAddress: TextView
    private lateinit var ivAddress: ImageView
    private lateinit var tvIntroduce: TextView
    private lateinit var ivIntroduce: ImageView
    private lateinit var cbVoice: CheckBox
    private lateinit var cbAutoPass: CheckBox
    private lateinit var tvNameCard: TextView
    private lateinit var tvManagerCount: TextView
    private lateinit var llClaim: LinearLayout
    private lateinit var tvClaimRule: TextView
    private lateinit var gvMember: MyGridview
    private lateinit var tvMemberCount: TextView

    // 标题栏
    private lateinit var tvTitle: TextView
    private lateinit var tvTitleRight: TextView
    private lateinit var ivTitleRight: ImageView
    private lateinit var ivTitleBack: ImageView
    var inviteState = 0
    var gid = ""
    var imgGid = ""
    var mFansClub = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_group_info)
        StatusBarUtil.showLightStatusBar(this)
        initView()
        gid = intent.getStringExtra(IntentKey.GROUP_ID)
        inviteState = intent.getIntExtra(IntentKey.GROUP_INVITE, 0)
        val formChat = intent.getBooleanExtra(IntentKey.FROM_CHAT, false)
        if (formChat) {
            btChat.visibility = View.GONE
        }
        getGroupInfo()
        initListener()
    }

    companion object {
        fun start(context: Context, gid: String, state: Int, fromChat: Boolean) {
            val intent = Intent(context, GroupDetailActivity::class.java)
            intent.putExtra(IntentKey.GROUP_ID, gid)
            intent.putExtra(IntentKey.GROUP_INVITE, state)
            intent.putExtra(IntentKey.FROM_CHAT, fromChat)
            context.startActivity(intent)
        }
    }

    private fun initView() {
        llGroupInfo = findViewById(R.id.ll_group_info)
        llMember = findViewById(R.id.ll_group_info_member)
        llAddress = findViewById(R.id.ll_group_info_address)
        llIntroduce = findViewById(R.id.ll_group_info_introduce)
        llAtAll = findViewById(R.id.ll_group_info_at_all)
        llVoice = findViewById(R.id.ll_group_info_voice)
        llAutoPass = findViewById(R.id.ll_group_info_auto_pass)
        llNameCard = findViewById(R.id.ll_group_info_name_card)
        llClearMember = findViewById(R.id.ll_group_info_clear_member)
        llClearChat = findViewById(R.id.ll_group_info_clear_chat)
        llSetManager = findViewById(R.id.ll_group_info_set_manager)
        btOperate = findViewById(R.id.bt_group_info_operate)
        btChat = findViewById(R.id.bt_group_info_start_chat)
        ivLogo = findViewById(R.id.iv_group_info_logo)
        ivLevel = findViewById(R.id.iv_group_info_level)
        tvName = findViewById(R.id.tv_group_info_name)
        tvNumber = findViewById(R.id.tv_group_info_id)
        tvCreateTime = findViewById(R.id.tv_group_info_time)
        ivGroupInfo = findViewById(R.id.iv_group_info)
        llClaim = findViewById(R.id.ll_group_info_strive)
        tvClaimRule = findViewById(R.id.tv_group_info_claim_rule)
        gvMember = findViewById(R.id.grid_view_group_info_member)
        tvMemberCount = findViewById(R.id.tv_group_info_member_count)
        tvIntroduce = findViewById(R.id.tv_group_info_introduce)
        ivIntroduce = findViewById(R.id.iv_group_info_introduce)
        cbVoice = findViewById(R.id.cb_group_info_voice)
        cbAutoPass = findViewById(R.id.cb_group_info_auto_pass)
        tvNameCard = findViewById(R.id.tv_group_info_name_card)
        tvManagerCount = findViewById(R.id.tv_group_info_manager_count)
        tvGroupAddress = findViewById(R.id.tv_group_info_address)
        ivAddress = findViewById(R.id.iv_group_info_address)
        tvTitle = findViewById(R.id.tv_normal_title_title)
        ivTitleBack = findViewById(R.id.iv_normal_title_back)
        ivTitleRight = findViewById(R.id.iv_normal_title_more)
        tvTitleRight = findViewById(R.id.tv_normal_title_more)
        ivTitleRight.visibility = View.VISIBLE
        tvTitle.text = "群组信息"
        ivTitleBack.setOnClickListener { finish() }
    }

    var role = -1
    var groupName = ""
    var groupIcon = ""
    var groupInfo: GroupInfoData.DataBean? = null
    private fun getGroupInfo() {
        HttpHelper.getInstance().getGroupInfo(gid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val groupData = GsonUtil.GsonToBean(data, GroupInfoData::class.java)
                groupData?.data?.run {
                    groupInfo = this
                    ImageLoader.loadCircleImage(this@GroupDetailActivity, group_pic, ivLogo)
                    groupName = groupname
                    groupIcon = group_pic
                    tvName.text = groupName
                    tvNumber.text = "群号：$group_num"
                    tvCreateTime.text = "创建：$create_time"
                    llClaim.visibility = if (is_claim == "1") View.VISIBLE else View.GONE
                    tvClaimRule.visibility = if (is_claim == "1") View.VISIBLE else View.GONE
                    tvGroupAddress.text = "$province $city"
                    tvMemberCount.text = "($member/$max_member)"
                    tvManagerCount.text = "$manager$managerStr"
                    tvIntroduce.text = introduce
                    tvNameCard.text = cardname
                    role = userpower.toInt()
                    mFansClub = is_fanclub == "1"
                    ivLevel.visibility = if (group_num.length <= 5) View.VISIBLE else View.GONE
                    when (role) {
                        -1 -> {
                            if (inviteState == 1) {
                                btChat.text = "同意加入群"
                            } else {
                                btChat.text = "申请加入群"
                            }
                        }
                        0 -> {
                            btChat.text = "已提交申请"
                        }
                        1 -> {
                            showMemberView()
                        }
                        2 -> {
                            showManageView()
                        }
                        3 -> {
                            showOwnerView()
                        }
                    }
                    if (mFansClub) {
                        llAutoPass.visibility = View.GONE
                        tvName.setTextColor(ContextCompat.getColor(this@GroupDetailActivity,R.color.cdtColor))
                    }
                    this@GroupDetailActivity.gid = this.gid
                    imgGid = im_gid
                    getGroupMemberInfo(group_leader_let)
                }
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun showMemberView() {
        getMessageReceiveState()
        llNameCard.visibility = View.VISIBLE
        llVoice.visibility = View.VISIBLE
        llClearChat.visibility = View.VISIBLE
        btOperate.visibility = View.VISIBLE
        btChat.text = "开始聊天"
        btOperate.text = "退出群组"
    }

    private fun showManageView() {
        showMemberView()
        llAtAll.visibility = View.VISIBLE
        llAutoPass.visibility = View.VISIBLE
        llClearMember.visibility = View.VISIBLE
        ivGroupInfo.visibility = View.VISIBLE
        ivIntroduce.visibility = View.VISIBLE
    }

    private fun showOwnerView() {
        showManageView()
        llSetManager.visibility = View.VISIBLE
        btOperate.text = "解散群组"
        ivAddress.visibility = View.VISIBLE
        if (mFansClub) { //粉丝群不可解散
            btOperate.visibility = View.GONE
        }
    }

    private fun isGroupManager(): Boolean {
        return role == 2 || role == 3;
    }

    private fun isGroupOwner(): Boolean {
        return role == 3;
    }

    private fun initListener() {
        ivTitleRight.setOnClickListener {
            showRightMenu()
        }
        btChat.setOnClickListener {
            when (role) {
                -1 -> {
                    if (mFansClub) {
                        showJoinFansGroupPop()
                    } else {
                        if (inviteState == 1) {
                            agreeJoinGroup()
                        } else {
                            requestJoinGroup()
                        }
                    }
                }
                0 -> {
                    requestJoinGroup()
                }
                else -> {
                    startGroupChat()
                }
            }
        }
        llGroupInfo.setOnClickListener {
            if (isGroupManager()) {
                gotoEditGroupInfo()
            }
        }
        llMember.setOnClickListener {
            gotoMemberActivity(1)
        }
        llSetManager.setOnClickListener {
            gotoMemberActivity(2)
        }
        llIntroduce.setOnClickListener {
            if (isGroupManager()) {
                gotoEditGroupIntroduce()
            }
        }
        llAddress.setOnClickListener {
            if (isGroupManager()) {
                gotoEditGroupAddress()
            }
        }
        llAtAll.setOnClickListener {
            if (isGroupManager()) {
                atAllMember()
            }
        }
        llClearMember.setOnClickListener {
            if (isGroupManager()) {
                showClearMemberDialog()
            }
        }
        llNameCard.setOnClickListener {
            showEditNameCardDialog()
        }
        llClearChat.setOnClickListener {
            showClearChatDialog()
        }
        btOperate.setOnClickListener {
            when (role) {
                1, 2 -> {
                    showQuitOrDissolutionGroup(1)
                }
                3 -> {
                    showQuitOrDissolutionGroup(2)
                }
            }
        }

        cbAutoPass.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isPressed) {
                setGroupAutoPassState()
            }
        }

        cbVoice.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isPressed) {
                setGroupMessageState()
            }
        }

        llClaim.setOnClickListener {
            getClaimState()
        }

        tvClaimRule.setOnClickListener {
            gotoRulePage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            NormalConstant.REQUEST_CODE_1, NormalConstant.REQUEST_CODE_2 -> {
                if (resultCode == NormalConstant.RESULT_CODE_OK) {
                    getGroupInfo()
                }
            }
            NormalConstant.REQUEST_CODE_4 -> {
                if (resultCode == NormalConstant.RESULT_CODE_OK) {
                    if (data?.getStringExtra(IntentKey.INTRODUCE)?.isNotEmpty() == true) {
                        groupInfo?.introduce = data.getStringExtra(IntentKey.INTRODUCE)
                        tvIntroduce.text = groupInfo?.introduce
                    }
                }
            }
        }
    }

    private fun requestJoinGroup() {
        val intent = Intent(this, VerificationActivity::class.java)
        intent.putExtra(IntentKey.GROUP_ID, gid)
        startActivityForResult(intent, NormalConstant.REQUEST_CODE_1)
    }

    private fun agreeJoinGroup() {
        HttpHelper.getInstance().joinGroup(gid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                llGroupInfo.postDelayed({ finish() }, 200)
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun startGroupChat() {
        if (IntentKey.MUTE_STATE.getSpValue() == "0") {
            "您因违规被系统禁用聊天功能，如有疑问请与客服联系！".showToast()
            return
        }
        val chatInfo = ChatInfo()
        chatInfo.type = V2TIMConversation.V2TIM_GROUP
        chatInfo.id = imgGid
        chatInfo.chatName = groupName
        intent = Intent(MyApp.instance(), ChatActivity::class.java)
        intent.putExtra(Constants.CHAT_INFO, chatInfo)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        MyApp.instance().startActivity(intent)
    }

    private fun getGroupMemberInfo(leader: String) {
        HttpHelper.getInstance().getGroupMemberBaseList(gid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                val memberData = GsonUtil.GsonToBean(data, GroupInfoMemberData::class.java)
                memberData?.data?.run {
                    val memberAdapter = GroupMemberAdapter(this@GroupDetailActivity, this.take(6), gid, role.toString(), leader)
                    gvMember.adapter = memberAdapter
                    gvMember.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        UserInfoActivity.start(this@GroupDetailActivity, this[position].uid)
                    }
                    (gvMember.adapter as GroupMemberAdapter).setOnSimpleItemListener {
                        if (mFansClub) {
                            //showJoinFansGroupPop()
                            "粉丝群暂不支持主动拉人".showToast()
                        } else {
                            val intent = Intent(this@GroupDetailActivity, GroupInviteActivity::class.java)
                            intent.putExtra("groupId", gid)
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    private fun gotoEditGroupInfo() {
        val intent = Intent(this, EditGroupActivity::class.java)
        intent.putExtra(IntentKey.GROUP_ID, gid)
        intent.putExtra(IntentKey.NAME, groupName)
        intent.putExtra(IntentKey.GROUP_ICON, groupIcon)
        intent.putExtra(IntentKey.GROUP_TYPE, if (mFansClub) "2" else "1")
        startActivityForResult(intent, NormalConstant.REQUEST_CODE_2)
    }

    private fun gotoMemberActivity(flag: Int) {
        if (role == 1 || role == 2 || role == 3 || MyApp.isAdmin == "1") {
            val intent = Intent(this, GroupMemberActivity::class.java)
            intent.putExtra(IntentKey.GROUP_ID, gid)
            intent.putExtra(IntentKey.STATE, role.toString())
            intent.putExtra(IntentKey.FLAG, flag.toString())
            startActivityForResult(intent, NormalConstant.REQUEST_CODE_3)
        } else {
            "非群成员，不能查看群成员列表".showToast()
        }
    }

    private fun gotoEditGroupIntroduce() {
        val intent = Intent(this, GroupIntroActivity::class.java)
        intent.putExtra(IntentKey.GROUP_ID, gid)
        intent.putExtra(IntentKey.INTRODUCE, groupInfo?.introduce)
        startActivityForResult(intent, NormalConstant.REQUEST_CODE_4)
    }

    private fun gotoEditGroupAddress() {
        val intent = Intent(this, ChangeGroupLocationActivity::class.java)
        with(intent) {
            putExtra("province", groupInfo?.province)
            putExtra("city", groupInfo?.city)
            putExtra("groupId", gid)
            putExtra("lat", groupInfo?.lat)
            putExtra("lng", groupInfo?.lng)
            startActivityForResult(intent, NormalConstant.REQUEST_CODE_1)
        }
    }

    private fun atAllMember() {
        SendMetionAllMsgActivity.start(this, gid)
    }

    private fun showClearMemberDialog() {
        AlertView(null, null, "取消", null, arrayOf("清理7天未登录", "清理15天未登录", "清理31天未登录"),
                this, AlertView.Style.ActionSheet) { _, position, _ ->
            when (position) {
                0 -> clearGroupMember("7")
                1 -> clearGroupMember("15")
                2 -> clearGroupMember("31")
            }
        }.show()
    }

    private fun clearGroupMember(day: String) {
        HttpHelper.getInstance().clearGroupMember(gid, day, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                getGroupInfo()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun showEditNameCardDialog() {
        val nameCardDialog = MyAlertInputDialog(this).builder()
                .setMsg("设置群名片")
                .setEditText("")
        nameCardDialog.setNegativeButton("取消") { nameCardDialog.dismiss() }
                .setPositiveButton("确认") {
                    if (nameCardDialog.result.length > 10) {
                        "群名片限十字以内".showToast()
                        return@setPositiveButton
                    }
                    editGroupNickName(nameCardDialog.result)
                    nameCardDialog.dismiss()
                }
        nameCardDialog.show()
    }

    private fun editGroupNickName(name: String) {
        HttpHelper.getInstance().editGroupUserNickName(gid, MyApp.uid, name, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                tvNameCard.text = name
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun showClearChatDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("提示").setMessage("确认清空记录吗？").setNegativeButton("确认") { dialog, _ ->
            V2TIMManager.getMessageManager().clearGroupHistoryMessage(imgGid, object : V2TIMCallback {
                override fun onSuccess() {
                    dialog.dismiss()
                    "清理成功".showToast()
                    EventBus.getDefault().post(FinishConversationEvent())
                    finish()
                }

                override fun onError(code: Int, desc: String) {
                    dialog.dismiss()
                    "网络异常,请稍后重试".showToast()
                }
            })
        }.setPositiveButton("取消") { dialog, _ -> dialog.dismiss() }.create().show()
    }

    private fun showQuitOrDissolutionGroup(type: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(if (type == 1) "确认退出吗?" else "解散群组后不可恢复，确定解散吗？")
                .setPositiveButton("否") { dialog, _ -> dialog.dismiss() }
                .setNegativeButton("是") { dialog, _ -> dialog.dismiss(); if (type == 1) quitGroup() else dissolutionGroup() }
                .create().show()
    }

    private fun quitGroup() {
        HttpHelper.getInstance().applyQuitGroup(gid, object : HttpListener {
            override fun onSuccess(data: String) {
                quitTimGroup()
            }

            override fun onFail(msg: String) {
                msg.showToast()
            }
        })
    }

    private fun quitTimGroup() {
        V2TIMManager.getInstance().quitGroup(gid, object : V2TIMCallback {
            override fun onError(code: Int, desc: String?) {
                desc?.showToast()
            }

            override fun onSuccess() {
                ToastUtil.show(applicationContext, "成功退出群组")
                V2TIMManager.getMessageManager().clearGroupHistoryMessage(imgGid, null)
                val intent = Intent(this@GroupDetailActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun dissolutionGroup() {
        HttpHelper.getInstance().dissolutionGroup(gid, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
                V2TIMManager.getMessageManager().clearGroupHistoryMessage(imgGid, null)
                val intent = Intent(this@GroupDetailActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun setGroupAutoPassState() {
        HttpHelper.getInstance().setGroupAutoCheck(gid, if (cbAutoPass.isChecked) "1" else "0", object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                msg?.showToast()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
                cbAutoPass.isChecked = !cbAutoPass.isChecked
            }
        })
    }

    private fun getMessageReceiveState() {
        V2TIMManager.getGroupManager().getGroupsInfo(listOf(imgGid), object : V2TIMValueCallback<List<V2TIMGroupInfoResult>> {
            override fun onSuccess(v2TIMGroupInfoResults: List<V2TIMGroupInfoResult>) {
                for (v2TIMGroupInfo in v2TIMGroupInfoResults) {
                    if (v2TIMGroupInfo.groupInfo.groupID == gid) {
                        when (v2TIMGroupInfo.groupInfo.recvOpt) {
                            V2TIMMessage.V2TIM_NOT_RECEIVE_MESSAGE -> {
                                cbVoice.isChecked = false
                            }
                            V2TIMMessage.V2TIM_RECEIVE_MESSAGE -> {
                                cbVoice.isChecked = true
                            }
                            V2TIMMessage.V2TIM_RECEIVE_NOT_NOTIFY_MESSAGE -> {
                                cbVoice.isChecked = false
                            }
                        }
                    }
                }
            }

            override fun onError(code: Int, desc: String) {}
        })

    }

    private fun setGroupMessageState() {
        if (cbVoice.isChecked) {
            V2TIMManager.getMessageManager().setGroupReceiveMessageOpt(imgGid, V2TIMMessage.V2TIM_RECEIVE_MESSAGE, null)
        } else {
            V2TIMManager.getMessageManager().setGroupReceiveMessageOpt(imgGid, V2TIMMessage.V2TIM_RECEIVE_NOT_NOTIFY_MESSAGE, null)
        }
    }

    private fun showRightMenu() {
        AlertView(null, null, "取消", null, arrayOf("分享", "举报"),
                this, AlertView.Style.ActionSheet) { _, position, _ ->
            when (position) {
                0 -> {
                    showSharePop()
                }
                1 -> {
                    gotoReportActivity()
                }
            }
        }.show()
    }

    private fun showSharePop() {
        val normalShareBean = NormalShareBean(3, "",
                "来自圣魔的群组",
                "邀请你加入群组 $groupName",
                "邀请你加入群组 $groupName",
                HttpUrl.NetPic() + HttpUrl.ShareGroupDetail + gid,
                groupInfo?.group_pic
                        ?: SpKey.IMAGE_HOST.getSpValue("http://image.aiwujie.com.cn/") + "Uploads/logo.png")
        val normalSharePop = NormalSharePop(this, normalShareBean)
        normalSharePop.showPopupWindow()
    }

    private fun gotoReportActivity() {
        ReportActivity.start(this, groupInfo?.uid)
    }

    private fun getClaimState() {
        HttpHelper.getInstance().getClaimState(gid, object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                showClaimPop()
            }

            override fun onFail(code: Int, msg: String?) {
                when (code) {
                    4002 -> {

                    }
                    4003 -> {

                    }
                    else -> {
                        msg?.showToast()
                    }
                }
            }
        })
    }

    private fun showVipPop(msg: String?) {
        val normalTipsPop = NormalTipsPop.Builder(this)
                .setTitle("权限不足")
                .setInfo(msg)
                .setCancelStr("取消")
                .setConfirmStr("去开通")
                .build()
        normalTipsPop.showPopupWindow()
        normalTipsPop.setOnPopClickListener(object : NormalTipsPop.OnPopClickListener {
            override fun cancelClick() {
                normalTipsPop.dismiss()
            }

            override fun confirmClick() {
                normalTipsPop.dismiss()
                val intent = Intent(this@GroupDetailActivity, VipMemberCenterActivity::class.java)
                val headpic = SharedPreferencesUtils.getParam(this@GroupDetailActivity, "headurl", "") as String
                intent.putExtra("headpic", headpic)
                intent.putExtra("uid", MyApp.uid)
                startActivity(intent)
            }
        })
    }

    private fun showBindPhonePop(msg: String?) {
        val normalTipsPop = NormalTipsPop.Builder(this)
                .setTitle("绑定手机号")
                .setInfo(msg)
                .setCancelStr("取消")
                .setConfirmStr("去绑定")
                .build()
        normalTipsPop.showPopupWindow()
        normalTipsPop.setOnPopClickListener(object : NormalTipsPop.OnPopClickListener {
            override fun cancelClick() {
                normalTipsPop.dismiss()
            }

            override fun confirmClick() {
                normalTipsPop.dismiss()
                val intent = Intent(this@GroupDetailActivity, BindingMobileActivity::class.java)
                intent.putExtra("neworchange", "new")
                startActivity(intent)
            }
        })
    }

    fun showClaimPop() {
        val groupClaimPop = GroupClaimPop(this)
        groupClaimPop.showPopupWindow()
        groupClaimPop.setNormalContentListener { content -> addClaim(groupClaimPop, content) }
    }

    private fun addClaim(groupClaimPop: GroupClaimPop, reason: String?) {
        HttpHelper.getInstance().addClaimGroup(gid, reason, object : HttpCodeMsgListener {
            override fun onSuccess(data: String?, msg: String?) {
                groupClaimPop.dismiss()
            }

            override fun onFail(code: Int, msg: String?) {
                msg?.showToast()
            }
        })
    }

    private fun gotoRulePage() {
        BannerWebActivity.start(this, HttpUrl.NetPic() + "Home/Info/Shengmosimu/id/20", "圣魔")
    }

    private fun showJoinFansGroupPop() {
        val buyTickPop = GroupBuyTicketPop(this,gid,"")
        buyTickPop.showPopupWindow()
        buyTickPop.onTicketPopListener = object :GroupBuyTicketPop.OnTicketPopListener {
            override fun doPopBuySuc() {

            }

            override fun doPopBuyFail(msg: String?) {
                msg?.showToast()
            }

            override fun doPopDismiss() {

            }

        }
    }
}
