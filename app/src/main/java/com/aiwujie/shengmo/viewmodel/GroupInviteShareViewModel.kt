package com.aiwujie.shengmo.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.viewmodel
 * @ClassName: GroupInviteShareViewModel
 * @Author: xmf
 * @CreateDate: 2022/5/9 10:43
 * @Description:
 */
class GroupInviteShareViewModel(application: Application): AndroidViewModel(application) {
    private val chooseIdLiveData: MutableLiveData<ArrayList<String>> by lazy { MutableLiveData<ArrayList<String>>() }

    fun addUser(item:String) {
        chooseIdLiveData.value?.add(item)
    }

    fun removeUser(item:String) {
        chooseIdLiveData.value?.remove(item)
    }

    fun getLiveData():MutableLiveData<ArrayList<String>> {
        return chooseIdLiveData
    }
}
