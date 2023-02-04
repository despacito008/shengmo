package com.aiwujie.shengmo.kt.bean

data class NormalResultBean<T:Any> (
    val `data`: T,
    val msg: String,
    val retcode: Int
)