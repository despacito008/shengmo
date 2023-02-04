package com.aiwujie.shengmo.kt.ui.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.kt.bean.NormalShareBean
import com.aiwujie.shengmo.kt.ui.activity.statistical.ShareUserActivity
import com.aiwujie.shengmo.kt.util.alphaBackground
import com.aiwujie.shengmo.kt.util.clickDelay
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.utils.AppIsInstallUtils
import com.aiwujie.shengmo.utils.QqShareManager
import com.aiwujie.shengmo.utils.WechatShareManager
import com.aiwujie.shengmo.utils.WeiboShareManager
import razerdp.basepopup.BasePopupWindow

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.view
 * @ClassName: NormalSharePop
 * @Author: xmf
 * @CreateDate: 2022/4/9 11:14
 * @Description:
 */
class NormalSharePop(context: Context) : BasePopupWindow(context) {
    private var llShareTop: LinearLayout = findViewById(R.id.ll_share_in_app)
    private var llShareSM: LinearLayout = findViewById(R.id.ll_share_sm)
    private var llShareWeChat: LinearLayout = findViewById(R.id.ll_share_we_chat)
    private var llShareFriendCircle: LinearLayout = findViewById(R.id.ll_share_friend_circle)
    private var llShareQQ: LinearLayout = findViewById(R.id.ll_share_qq)
    private var llShareQQZone: LinearLayout = findViewById(R.id.ll_share_qq_zone)
    private var llShareWeiBo: LinearLayout = findViewById(R.id.ll_share_wei_bo)
    private var tvShareCancel: TextView = findViewById(R.id.tv_share_cancel)
    private lateinit var mContext: Context
    var mTitle = ""
    var mContent = ""
    var mLinkUrl = ""
    var mImgUrl = ""
    lateinit var mShareBean:NormalShareBean
    constructor(context: Context, shareBean: NormalShareBean,showTop:Boolean = false) : this(context) {
        mContext = context
        mShareBean = shareBean
        mTitle = shareBean.title
        mContent = shareBean.content
        mLinkUrl = shareBean.linkUrl
        mImgUrl = shareBean.imgUrl
        popupGravity = Gravity.BOTTOM
        if (showTop) {llShareTop.visibility = View.VISIBLE}
        llShareSM.clickDelay {
            when (shareBean.type) {
                1,2,4 -> {
                    shareUser()
                }
            }
        }
        llShareWeChat.clickDelay {
            shareToWeChat(0)
        }
        llShareFriendCircle.clickDelay {
            shareToWeChat(1)
        }
        llShareQQ.clickDelay {
            shareToQQ(0)
        }
        llShareQQZone.clickDelay {
            shareToQQ(1)
        }
        llShareWeiBo.clickDelay {
            shareToWeiBo()
        }
        tvShareCancel.clickDelay {
            dismiss()
        }
    }

    private fun shareToWeChat(type: Int) {
        if (AppIsInstallUtils.isWeChatAvailable(mContext)) {
            val shareManger = WechatShareManager.getInstance(mContext)
            val shareContent = shareManger.getShareContentWebpag(mTitle, mContent, mLinkUrl, mImgUrl)
            shareManger.shareByWebchat(shareContent, type, mImgUrl)
        } else {
            "您没有安装微信".showToast()
        }
    }

    private fun shareToQQ(type: Int) {
        if (AppIsInstallUtils.isQQClientAvailable(mContext)) {
            val shareManger = QqShareManager.getInstance(mContext)
            when (type) {
                0 -> {
                    shareManger.shareToQQ(mLinkUrl, mTitle, mContent, mImgUrl)
                }
                1 -> {
                    shareManger.shareToQZone(mLinkUrl, mTitle, mContent, mImgUrl)
                }
            }
        } else {
            "您没有安装QQ".showToast()
        }
    }

    private fun shareToWeiBo() {
        if (AppIsInstallUtils.isWeiBoAvailable(mContext)) {
            val shareManger = WeiboShareManager.getInstance(mContext)
            shareManger.WeiboShare(mContext,mTitle + "\n" + mContent,mLinkUrl,mImgUrl)
        } else {
            "您没有安装微博".showToast()
        }
    }

    private fun shareUser() {
        val intent = Intent(mContext,ShareUserActivity::class.java)
        with(intent) {
            putExtra("id",mShareBean.id)
            putExtra("leixing",mShareBean.type)
            putExtra("content",mShareBean.info)
            putExtra("pic",mImgUrl)
            mContext.startActivity(this)
        }
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_share)
    }
}
