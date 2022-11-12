package com.eohi.hx.ui.work.quality.rejects.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/11/8 10:29
 */
data class RejectsListModel(
    val CJRQ: String,
    val CJYH: String,
    val GXSM: String,
    val PDBJ: Any,
    val SCLZKKH: String,
    val SCRWDH: String,
    val SWH: String,
    val SWRQ: String,
    val WPH: String,
    val ZRRXM: String,
    val ZRRGH:String,
    val BZS: Int,
    val detail: ArrayList<SubmitRejectsItem>,
    val djlsh: Int,
    val gxh: String,
    val wpmc: String,
    val jgdymc:String,
    val jgdybh:String,
    val djr:String,
    val djrid:String,
    val cjbh:String,
    val cjmc:String,
    val SBBH:String,
    val SBMC:String,
    val gg:String
)