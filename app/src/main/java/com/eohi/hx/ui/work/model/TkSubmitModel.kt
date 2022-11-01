package com.eohi.hx.ui.work.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/28 15:10
 */
data class TkSubmitModel(
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

    val dhtzdh: String,
    val khh: String,
    val yym: String,

)

