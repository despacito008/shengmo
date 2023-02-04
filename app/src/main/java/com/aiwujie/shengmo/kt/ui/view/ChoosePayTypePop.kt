package com.aiwujie.shengmo.kt.ui.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.bean.WalletData
import com.aiwujie.shengmo.kt.util.clickDelay
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.HttpCodeListener
import com.aiwujie.shengmo.net.HttpHelper
import com.aiwujie.shengmo.utils.GsonUtil
import razerdp.basepopup.BasePopupWindow

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.ui.view
 * @ClassName: ChoosePayTypPop
 * @Author: xmf
 * @CreateDate: 2022/5/25 9:41
 * @Description:
 */
class ChoosePayTypePop(context: Context, private val payBean:Int = 0) : BasePopupWindow(context) {

    private val tvBeans:TextView by lazy { findViewById<TextView>(R.id.tv_pay_by_beans) }
    private val llPayBeans:LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_pay_by_beans) }
    private val llPayWeChat:LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_pay_by_we_chat) }
    private val llPayAli:LinearLayout by lazy { findViewById<LinearLayout>(R.id.ll_pay_by_ali) }

    init {
        getWallet()

        llPayBeans.clickDelay {
            if (payBean > balance) {
                "魔豆余额不足".showToast()
                return@clickDelay
            }
            payTypeChooseListener?.doChoosePayType(1)
            dismiss()
        }
        llPayWeChat.clickDelay {
            payTypeChooseListener?.doChoosePayType(2)
            dismiss()
        }
        llPayAli.clickDelay {
            payTypeChooseListener?.doChoosePayType(3)
            dismiss()
        }
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.app_pop_choose_pay_type)
    }

    var balance = 0
    private fun getWallet() {
        HttpHelper.getInstance().getMyWallet(object : HttpCodeListener {
            override fun onSuccess(data: String?) {
                val walletData = GsonUtil.GsonToBean(data, WalletData::class.java)
                walletData?.data?.run {
                    balance = wallet.toInt()
                    tvBeans.text = "魔豆支付 (当前余额:$wallet)"
                }
            }

            override fun onFail(code: Int, msg: String?) {

            }
        })
    }

    interface OnPayTypeChooseListener {
        fun doChoosePayType(type:Int)
    }

    var payTypeChooseListener:OnPayTypeChooseListener? = null

}
