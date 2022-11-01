package com.eohi.hx.ui.work.model

/**
 *@author: YH
 *@date: 2021/12/7
 *@desc: 库位信息实体类
 */
data class TransferKwModel(
    val kwh: String,//库位号
    val dwmc: String,//库位名称
    val ckh: String,//仓库号
    val ckmc: String,//仓库名称
    val sl: Double
)
