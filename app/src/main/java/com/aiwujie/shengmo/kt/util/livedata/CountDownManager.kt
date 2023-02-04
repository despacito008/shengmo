package com.aiwujie.shengmo.kt.util.livedata

import android.os.CountDownTimer

object CountDownManager {
    private var mRemainSecond:Long = 10L
    private var mTimer:CountDownTimer? = null
    private var mListener = arrayListOf<CountDataChangeListener>()

    fun startCount(remainSecond:Long? = 10L) {
        mRemainSecond = remainSecond!!
        mTimer = object :CountDownTimer(remainSecond * 1000,1000) {
            override fun onTick(p0: Long) {
                mRemainSecond--
                dispatchMessage("剩余： $mRemainSecond 秒")
            }

            override fun onFinish() {
                dispatchMessage("倒计时结束")
            }
        }
        mTimer?.start()
    }

    fun cancelCount() {
        if (mTimer != null) {
            mTimer!!.cancel()
            mListener.clear()
        }
    }

    private fun dispatchMessage(msg: String) {
        mListener.forEach { it.onChange(msg) }
    }

     fun setListener(listener: CountDataChangeListener) {
        mListener.add(listener)
    }

     fun removeListener(listener: CountDataChangeListener) {
        mListener.remove(listener)
    }

}

interface CountDataChangeListener {
    fun onChange(msg:String)
}