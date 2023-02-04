package com.aiwujie.shengmo.kt.ui.activity.statistical

import android.content.Intent
import android.os.Message
import android.support.v4.app.Fragment
import android.view.View
import com.aiwujie.shengmo.bean.Atbean
import com.aiwujie.shengmo.kt.ui.fragment.HomePageHighFragment
import com.aiwujie.shengmo.kt.ui.fragment.atmember.AtMemberConversationFragment
import com.aiwujie.shengmo.kt.ui.fragment.atmember.AtMemberGroupFragment
import com.aiwujie.shengmo.kt.ui.fragment.atmember.AtMemberHighFragment
import com.aiwujie.shengmo.kt.ui.fragment.atmember.AtMemberRelationShipFragment
import com.aiwujie.shengmo.kt.util.showToast
import org.greenrobot.eventbus.EventBus

class AtMemberActivity : StatisticalDetailActivity() {
    private lateinit var conversationFragment: AtMemberConversationFragment
    private lateinit var attentionFragment: AtMemberRelationShipFragment
    private lateinit var fansFragment: AtMemberRelationShipFragment
    private lateinit var groupFragment: AtMemberGroupFragment
    private lateinit var homePageHighFragment: AtMemberHighFragment
    override fun getFragmentList(): List<Fragment> {
        conversationFragment = AtMemberConversationFragment()
        attentionFragment = AtMemberRelationShipFragment.newInstance(0)
        fansFragment = AtMemberRelationShipFragment.newInstance(1)
        groupFragment = AtMemberGroupFragment()
        homePageHighFragment = AtMemberHighFragment.newInstance()
        return arrayListOf(conversationFragment, attentionFragment, fansFragment, groupFragment,homePageHighFragment)
    }

    override fun getTitleList(): List<String> {
        return arrayListOf("聊天", "关注", "粉丝", "群组","高端")
//        return arrayListOf("聊天", "关注", "粉丝", "群组")
    }

    override fun getPageTitle(): String {
        return "提醒谁看"
    }

    lateinit var atBeanList: ArrayList<Atbean.DataBean>
    override fun initViewComplete() {
        super.initViewComplete()
        ivNormalTitleMore.visibility = View.GONE
        tvNormalTitleMore.visibility = View.VISIBLE
        tvNormalTitleMore.text = "完成"
        atBeanList = ArrayList()
        tvNormalTitleMore.setOnClickListener {
            if (atBeanList.size == 0) {
                "请至少选择一位好友或群组".showToast()
            } else {
                atMember()
            }
        }
    }

    private fun atMember() {
        val atbean = Atbean()
        atbean.dataBean = atBeanList
        val obtain = Message.obtain()
        if ("commentDetail" == intent.getStringExtra("type")) {
            obtain.arg2 = 10001
        } else {
            obtain.arg2 = 11
        }
        obtain.obj = atbean
        EventBus.getDefault().post(obtain)
        var uidstr = ""
        var unamestr = ""
        val ids = StringBuffer()
        val names = StringBuffer()
        atBeanList.forEach {
            ids.append(it.uid).append(",")
            names.append(it.nickname).append(",")
        }
        if (ids.isNotEmpty()) {
            uidstr = ids.toString().substring(0, ids.toString().length)
            unamestr = names.toString().substring(0, names.toString().length)
        }

        val intent = Intent()
        intent.putExtra("atStr", uidstr)
        intent.putExtra("atNameStr", unamestr)
        intent.putExtra("atSize", atBeanList.size.toString() + "")
        setResult(100, intent)
        finish()

    }



    fun refresh(type: Int) {
        when (type) {
            1 -> {
                attentionFragment.refresh()
                fansFragment.refresh()
                groupFragment.refresh()
            }
            2 -> {
                conversationFragment.refresh()
                fansFragment.refresh()
            }
            3 -> {
                conversationFragment.refresh()
                attentionFragment.refresh()
            }
            4 -> {
                conversationFragment.refresh()
            }
        }
    }

    fun removeAtUser(uid: String) {
        var currentIndex = 0
        for (i in atBeanList.indices) {
            if (atBeanList[i].uid == uid) {
                currentIndex = i
            }
        }
        atBeanList.removeAt(currentIndex)
    }

}