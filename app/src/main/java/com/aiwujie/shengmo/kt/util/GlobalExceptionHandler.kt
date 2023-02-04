package com.aiwujie.shengmo.kt.util

import android.content.Context
import android.content.Intent
import android.os.Process
import com.aiwujie.shengmo.activity.WelcomeActivity
import com.aiwujie.shengmo.kt.ui.activity.HomePageActivity
import com.aiwujie.shengmo.utils.FinishActivityManager
import com.aiwujie.shengmo.utils.LogUtil
import com.amap.api.mapcore.util.it

/**
 * @program: newshengmo
 * @description: 全局捕获异常
 * @author: whl
 * @create: 2022-05-07 09:25
 **/
class GlobalExceptionHandler  private  constructor():Thread.UncaughtExceptionHandler{

    private var mContext: Context? = null
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    companion object {

        private const val TAG = "GlobalExceptionHandler"

        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GlobalExceptionHandler()
        }
    }

    fun initExceptionHandler(context: Context?) {
        mContext = context
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        LogUtil.d(TAG,"ThreadName:"+t?.name+"Throwable:"+e?.message+"")
        //捕获到异常，则立即重启应用
        mContext?.runCatching {
//            val intent = Intent(mContext, WelcomeActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)

            FinishActivityManager.getManager().finishActivity(HomePageActivity::class.java)
            Process.killProcess(Process.myPid())
        }

    }
}