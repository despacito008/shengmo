package com.aiwujie.shengmo.bean

data class LargeSvgaBean(
        val `data`: List<LargeSvgaDataBean>,
        val msg: String,
        val retcode: Int
)

data class LargeSvgaDataBean(
    val gift_id: String,
    val gift_name: String,
    val gift_svgaurl: String
)