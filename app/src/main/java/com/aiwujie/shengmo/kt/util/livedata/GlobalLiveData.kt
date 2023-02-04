package com.aiwujie.shengmo.kt.util.livedata

import android.arch.lifecycle.LiveData
import com.aiwujie.shengmo.utils.LogUtil


/**
 * @ProjectName: newSamer
 * @Package: com.example.newsamer.livedata
 * @ClassName: GlobalLiveData
 * @Author: xmf
 * @CreateDate: 2022/5/6 14:22
 * @Description:
 */
class GlobalLiveData: LiveData<String>() {
    companion object {
        val instance: GlobalLiveData by lazy { GlobalLiveData() }
        //fun getInstance() = instance
        //fun getInstance() = if (Companion::instance.isInitialized) instance else GlobalLiveData()
    }

    private val mListener = object : CountDataChangeListener {
        override fun onChange(msg: String) {
            LogUtil.d("post value $msg")
            postValue(msg)
        }
    }

    fun startCount(remainSecond:Long? = 10L) {
        CountDownManager.startCount(remainSecond)
    }

    fun cancelCount() {
        CountDownManager.cancelCount()
    }

    override fun onActive() {
       // LogUtil.d("onActive")
        CountDownManager.setListener(mListener)
    }

    override fun onInactive() {
        super.onInactive()
       // LogUtil.d("onInactive")
        CountDownManager.removeListener(mListener)
    }
}
