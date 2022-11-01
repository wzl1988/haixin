package com.eohi.hx.ui.work.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/6 17:40
 */
data class InstorageModel(
        val `data`: List<Data>,
        val name: String,
        var swh: String,
        val userid: String,
        val ywlxm: String,
        val gsh:String,
        val tckh:String,
        var tkwh:String,
        var lljhh:String,
        var cgddh:String,
        var fckh:String,
        var fkwh:String,
        var fhtzdh:String,
        var gdh:String
)

data class Data(
    val fsl: String?,
    val tmh: String?,
    val zsl: String?
)