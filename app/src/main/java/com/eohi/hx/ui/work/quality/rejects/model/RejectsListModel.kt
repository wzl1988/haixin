package com.eohi.hx.ui.work.quality.rejects.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/11/8 10:29
 */
data class RejectsListModel(
    val cjrq: String,
    val CJYH: String,
    val gxsm: String,
    val pdbj: Any,
    val sclzkkh: String,
    val scrwdh: String,
    val swh: String,
    val swrq: String,
    val wph: String,
    val zrrxm: String,
    val zrrgh:String,
    val bzs: Int,
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
    val sbbh:String,
    val sbmc:String,
    val gg:String
)