package com.eohi.hx.ui.work.equipment.model

data class EquipmentCheck(
    val BJBH: String,
    val BJMC: String,
    val CJSBBH: String,
    val CSH: String,
    val CSMC: String,
    val DQSZWZ: String,
    val GSH: String,
    val GSMC: String,
    val JLLX: String,
    val QYBH: String,
    val QYMC: String,
    val SBBM: String,
    val SBMC: String,
    val SBXH: String,
    val WZBH: String,
    val WZMC: String,
    val barcode: String,
    val lastdjno: String,
    val lastdjtime: String,
    val lastdjtype: String,
    val lastwbno: String,
    val lastwbtime: String,
    val lastwbtype: String,
    val item:ArrayList<EquipmentParts>
)