package com.aiwujie.shengmo.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData


/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.viewmodel
 * @ClassName: GroupInviteShareViewModel
 * @Author: xmf
 * @CreateDate: 2022/5/9 10:43
 * @Description:
 */
class GroupingMemberShareViewModel(application: Application) : AndroidViewModel(application) {
    private val chooseIdLiveData: MutableLiveData<ArrayList<String>> by lazy { MutableLiveData<ArrayList<String>>() }


    fun initData(data: ArrayList<String>) {
        val value = ArrayList<String>()
        value.addAll(data)
        chooseIdLiveData.postValue(value)
    }

    fun addUser(item: String) {
        val data = chooseIdLiveData.value
        data?.run {
            add(item)
            //LogUtil.d("add user " + data.size)
            chooseIdLiveData.postValue(data)
        }
    }

    fun removeUser(item: String) {
        val data = chooseIdLiveData.value
        data?.run {
            remove(item)
            //LogUtil.d("remove user " + data.size)
            chooseIdLiveData.postValue(data)
        }
    }

    fun getLiveData(): MutableLiveData<ArrayList<String>> {
        return chooseIdLiveData
    }
}
