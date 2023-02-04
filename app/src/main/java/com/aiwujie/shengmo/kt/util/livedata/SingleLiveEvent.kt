package com.aiwujie.shengmo.kt.util.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @ProjectName: newSamer
 * @Package: com.example.newsamer.livedata
 * @ClassName: SingleLiveEvent
 * @Author: xmf
 * @CreateDate: 2022/5/6 15:04
 * @Description:
 */
class SingleLiveEvent<T>: MutableLiveData<T>() {
    private val mPending: AtomicBoolean = AtomicBoolean(false)

//    @MainThread
//    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
//        super.observe(owner) {
//            t->
//            if (mPending.compareAndSet(true,false)) {
//                observer.onChanged(t)
//            } else {
//                Log.e("livedata","其实还是粘性")
//            }
//        }
//    }

    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        if (mPending.compareAndSet(true,false)) {
            super.observe(owner, observer)
        }
    }

    @MainThread
    override fun setValue(value: T) {
        mPending.set(true)
        super.setValue(value)
    }

//    @MainThread
//    fun call() {
//        value = null
//    }
}
