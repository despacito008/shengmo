package com.aiwujie.shengmo.kt.ui.fragment

import android.view.View
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.tim.ConversationFragment
import com.aiwujie.shengmo.tim.HighConversationFragment

/**
 * @program: newshengmo
 * @description:
 * @author: whl
 * @create: 2022-06-08 20:05
 **/
class HomePageHighConversationFragment  :LazyFragment() {

    lateinit var conversationFragment: HighConversationFragment
    override fun loadData() {

    }

    override fun getContentViewId(): Int {
        return  R.layout.fragment_high_conversation
    }

    override fun initView(rootView: View) {
        initConversationFragment()
    }

    private fun initConversationFragment() {
        conversationFragment = HighConversationFragment()
        childFragmentManager.beginTransaction().replace(R.id.fl_content, conversationFragment).commit()
    }
}