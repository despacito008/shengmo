package com.aiwujie.shengmo.kt.bean

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.kt.bean
 * @ClassName: NormalShareBean
 * @Author: xmf
 * @CreateDate: 2022/4/9 11:57
 * @Description:
 */
data class NormalShareBean (
        val type:Int = 0,
        val id:String = "",
        val title:String,
        val info:String,
        val content:String,
        val linkUrl:String,
        val imgUrl:String
        )
