package com.aiwujie.shengmo.kt.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class LazyFragment : Fragment() {
    var isViewCreate = false
    var isUIVisible = false

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreate = true
        initView(view!!)
        lazyLoad()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isUIVisible = true
            lazyLoad()
        } else {
            isUIVisible = false
        }
    }

    fun lazyLoad() {
        if (isViewCreate && isUIVisible) {
            loadData()
            isViewCreate = false
            isUIVisible = false
        }
    }

    abstract fun loadData()

    abstract fun getContentViewId():Int

    abstract fun initView(rootView:View)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getContentViewId(), container, false)
    }





}