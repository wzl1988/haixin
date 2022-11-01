package com.eohi.hx.ui.work.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/13 17:13
 */
data class PickingWpxxModel(
    val ckh: String,
    val fzsl: Double,
    val gg: String,
    val kwh: String,
    val ph: String,
    val sl: Double,
    val th: String,
    val tmh: String,
    val wph: String,
    val wpmc: String,
    val fzjldw:String,
    val erpscdd:String?=null, //sap工单号
    var khwph:String? =null//客户物品号

)