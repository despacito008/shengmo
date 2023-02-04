package com.aiwujie.shengmo.kt.util.livedata

import android.arch.lifecycle.*
import android.os.Handler
import android.os.Looper
import android.support.annotation.MainThread
import java.lang.IllegalStateException
import java.util.concurrent.ConcurrentHashMap


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.util.livedata
 * @ClassName: UnFlowLiveData
 * @Author: xmf
 * @CreateDate: 2022/5/7 17:05
 * @Description:
 */
class UnFlowLiveData<T> {
    private var mMainHandler: Handler = Handler(Looper.getMainLooper())
    private var mValue: T? = null
    private var mObserverMap: ConcurrentHashMap<Observer<in T?>, MutableLiveData<T>>? = null
    constructor() {
        mObserverMap = ConcurrentHashMap()
    }


    @MainThread
    fun observerForever(observer: Observer<T?>) {
        checkMainThread("observerForever")
        val liveData = MutableLiveData<T>()
        liveData.observeForever(observer)
        mObserverMap?.put(observer,liveData)
    }

    @MainThread
    fun observe(owner: LifecycleOwner,observer: Observer<T?>) {
        val lifecycler = owner.lifecycle
        if (lifecycler.currentState == Lifecycle.State.DESTROYED) {
            return
        }
        lifecycler.addObserver(object :LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                mObserverMap?.remove(observer)
                lifecycler.removeObserver(this)
            }
        })
        val livedata = MutableLiveData<T>()
        livedata.observe(owner,observer)
        mObserverMap?.put(observer,livedata)
    }

    @MainThread
    fun removeObserver(observer: Observer<T?>) {
        checkMainThread("removeObserver")
        mObserverMap?.remove(observer)
    }

    fun getValue():T? {
        return mValue
    }

    fun clearValue() {
        mValue = null
    }

    fun setValue(value: T) {
        checkMainThread("setValue")
        mValue = value
        mObserverMap?.values?.forEach {
            it.value = value
        }
    }

    fun postValue(value: T) {
        mMainHandler.post {
            setValue(value)
        }
    }

    fun checkMainThread(methodName: String) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw IllegalStateException("UnFlowLiveData, Cannot invoke  $methodName  on a background thread")
        }
    }
}
