package com.eohi.hx.ui.work.model

/**
 *@author: YH
 *@date: 2021/12/7
 *@desc: 仓库物品实体类
 */
data class TransferKwWpModel(
    val kwh: String,//库位号
    val wph: String,//物品号
    val wpmc: String,//物品名称
    val gg: String,//规格
    val ph: String,//批号
    val sl: String//数量
)
