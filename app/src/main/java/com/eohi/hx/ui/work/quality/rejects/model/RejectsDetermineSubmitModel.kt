package com.eohi.hx.ui.work.quality.rejects.model

data class RejectsDetermineSubmitModel(
    val SWH: String,
    val SWRQ: Any,
    val bhgzsl: Int,
    val blppdr: String,
    val blppdrid: String,
    val blppdsj: String,
    val bz: String,
    val cjr: String,
    val cjrid: String,
    val clbfList: List<Clbf>,
    val djr: String,
    val djrid: String,
    val gsh: String,
    val gxbh: String,
    val gxmc: String,
    val gxtxh: Int,
    val items: List<SubmitRejectsItem>,
    val jgdybh: String,
    val jgdymc: String,
    val lzkkh: String,
    val rwdh: String,
    val sbbh: String,
    val sbmc: String
)

data class SubmitRejectsItem(
    var blsl: Int,
    var blxx: String,
    var blxxbm: String,
    var blyy: String,
    var blyybm: String,
    var czfs: String,
    var scr: String,
    var scrid: String,
    var sm: String,
    var zrgxh: String,
    var zrgxm: String
)

data class Clbf(
    val clbfsl: Int,
    val clblxx: String,
    val clczfs: String,
    val clwph: String
)