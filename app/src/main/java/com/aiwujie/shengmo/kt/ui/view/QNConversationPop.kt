package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.FollowMsgActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.bean.DialogStampData
import com.aiwujie.shengmo.customview.StampDialogNew
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.ui.activity.GroupMsgOperateActivity
import com.aiwujie.shengmo.net.IRequestCallback
import com.aiwujie.shengmo.net.RequestFactory
import com.aiwujie.shengmo.qnlive.activity.QNLiveRoomAnchorActivity
import com.aiwujie.shengmo.tim.chat.ChatActivity
import com.aiwujie.shengmo.tim.helper.ConversationLayoutHelper
import com.aiwujie.shengmo.tim.utils.Constants
import com.aiwujie.shengmo.timlive.ui.LiveRoomAnchorActivity
import com.aiwujie.shengmo.utils.LogUtil
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import com.aiwujie.shengmo.utils.ToastUtil
import com.google.gson.Gson
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo
import org.json.JSONException
import org.json.JSONObject
import razerdp.basepopup.BasePopupWindow
import java.util.*

class QNConversationPop(context: Context):BasePopupWindow(context) {

    private lateinit var mConversationLayout:ConversationLayout
    private lateinit var activity:AppCompatActivity
    init {
        //ToastUtil.show(context,"createRoom")

        // 从布局文件中获取会话列表面板
        mConversationLayout = findViewById(R.id.conversation_layout)
        // mMenu = new Menu(getActivity(), (TitleBarLayout) mConversationLayout.getTitleBar(), Menu.MENU_TYPE_CONVERSATION);
        // mMenu = new Menu(getActivity(), (TitleBarLayout) mConversationLayout.getTitleBar(), Menu.MENU_TYPE_CONVERSATION);
        LogUtil.d("获取会话列表")
        // 会话列表面板的默认UI和交互初始化
        mConversationLayout.initDefault(true)
        // 通过API设置ConversataonLayout各种属性的样例，开发者可以打开注释，体验效果
        activity = context as QNLiveRoomAnchorActivity
        ConversationLayoutHelper.customizeConversation(mConversationLayout, activity)

        mConversationLayout.conversationList.setOnItemClickListener {
            view, position, messageInfo ->

            //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
            if (position == 0) {
                return@setOnItemClickListener
            }

            val mTargetId: String = messageInfo.id

            if (mTargetId == "4") {
                // Intent intent1 = new Intent(getActivity(), GroupMsgOperationActivity.class);
                val intent1 = Intent(activity, GroupMsgOperateActivity::class.java)
                intent1.putExtra("targetId", mTargetId)
                activity.startActivity(intent1)
                V2TIMManager.getMessageManager().markC2CMessageAsRead(mTargetId, null)
                return@setOnItemClickListener
            }
            if (mTargetId == "9") {
                val intent2 = Intent(activity, FollowMsgActivity::class.java)
                activity.startActivity(intent2)
                V2TIMManager.getMessageManager().markC2CMessageAsRead(mTargetId, null)
                return@setOnItemClickListener
            }


            val isSpeak = SharedPreferencesUtils.getParam(activity.getApplicationContext(), "nospeak", "1") as String
            if (isSpeak == "0" && mTargetId.toInt() > 20) {
                ToastUtil.show(activity.applicationContext, "您因违规被系统禁用聊天功能，如有疑问请与客服联系！")
                return@setOnItemClickListener
            } else {
                if (messageInfo.isGroup) {
                    startChatGroupActivity(messageInfo)
                } else {
                    isOpenChat(messageInfo)
                }
            }
        }
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_conversation)
    }

    private fun isOpenChat(conversationInfo: ConversationInfo) {
        val map: MutableMap<String, String> = HashMap()
        map["uid"] = MyApp.uid
        map["otheruid"] = conversationInfo.id
        val manager = RequestFactory.getRequestManager()
        manager.post(HttpUrl.GetOpenChatRestrictAndInfo, map, object : IRequestCallback {
            override fun onSuccess(response: String) {
                Log.i("isopenchat", "onSuccess: $response")
                try {
                    val `object` = JSONObject(response)
                    val chatRetcode = `object`.getInt("retcode")
                    when (chatRetcode) {
                        2000, 2001 -> {
                            val obj = `object`.getJSONObject("data")
                            val obj1 = obj.getJSONObject("info")
                            startChatC2CActivity(conversationInfo, response)
                        }
                        3001 -> {
                            val data = Gson().fromJson(response, DialogStampData::class.java)
                            val datas = data.data
                            StampDialogNew(activity, conversationInfo.id, "nickname", datas.wallet_stamp, datas.basicstampX.toString() + "", datas.basicstampY.toString() + "", datas.basicstampZ.toString() + "", datas.sex)
                        }
                        4005 -> ToastUtil.show(activity, `object`.getString("msg"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(throwable: Throwable) {}
        })
    }

    private fun startChatC2CActivity(conversationInfo: ConversationInfo, userData: String) {
        val chatInfo = ChatInfo()
        chatInfo.type = if (conversationInfo.isGroup) V2TIMConversation.V2TIM_GROUP else V2TIMConversation.V2TIM_C2C
        chatInfo.id = conversationInfo.id
        chatInfo.chatName = conversationInfo.title
        chatInfo.draft = conversationInfo.draft
        val intent = Intent(MyApp.instance(), ChatActivity::class.java)
        intent.putExtra(Constants.CHAT_INFO, chatInfo)
        intent.putExtra("userInfo", userData)
        //        if (conversationInfo.getLastMessage() != null) {
//            long seq = conversationInfo.getLastMessage().getTimMessage().getSeq();
//            int unRead = conversationInfo.getUnRead();
//            if (unRead > 10) {
//                unRead = unRead > 999 ? 999 : unRead;
//                long unReadSeq = seq - unRead;
//                intent.putExtra(Constants.UNREAD_NUM, unRead);
//                intent.putExtra(Constants.UNREAD_SEQ, unReadSeq);
//            }
//        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        MyApp.instance().startActivity(intent)
    }

    private fun startChatGroupActivity(conversationInfo: ConversationInfo) {
        val chatInfo = ChatInfo()
        chatInfo.type = if (conversationInfo.isGroup) V2TIMConversation.V2TIM_GROUP else V2TIMConversation.V2TIM_C2C
        chatInfo.id = conversationInfo.id
        chatInfo.chatName = conversationInfo.title
        chatInfo.draft = conversationInfo.draft
        val intent = Intent(MyApp.instance(), ChatActivity::class.java)
        intent.putExtra(Constants.CHAT_INFO, chatInfo)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (conversationInfo.lastMessage != null) {
            val seq = conversationInfo.lastMessage.timMessage.seq
            var unRead = conversationInfo.unRead
            if (unRead > 10) {
                unRead = Math.min(unRead, 99999)
                val unReadSeq = seq - unRead + 1
                intent.putExtra(Constants.UNREAD_NUM, unRead)
                intent.putExtra(Constants.UNREAD_SEQ, unReadSeq)
            }
        }
        MyApp.instance().startActivity(intent)
    }
}