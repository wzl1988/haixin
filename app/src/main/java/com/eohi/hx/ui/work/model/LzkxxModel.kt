package com.eohi.hx.ui.work.model

data class LzkxxModel(
    val lzkkh: String, //流转卡号
    val wph: String, //物品号
    val wpmc: String, //物品名称
    val gg: String, //规格描述
    val th: String, //图号
    val khmc: String, //客户名称
    val lzkzt: String, //流转卡状态
    val jldw: String, //单位
    val sybzs: String,//编内数量
    val scsl: String,
    val bzs: String, //需求数量
    val rwdh: String,
    val cjyh: String,
    val cjyhid: String,
    val cjrq: String
)
data class LzkDetailModel(
    val bzs: Int,
    val dscsl: Int,
    val gg: String,
    val lzkkh: String,
    val rwdh: String,
    val scph: String,
    val scsl: Int,
    val TH: String,
    val wph: String,
    val wpmc: String,
    val zxbggx: String,
    val jgdybh:String,
    val jgdymc:String,
    val cjmc:String,
    val cjbh:String,
    val cjdd:String,
    val sapddh:String,
    val items: List<LzkItem>
)

data class LzkItem(
    val bgsj: String,
    val bzs: Double,
    val dqbznsl: Double,
    val dqgxljbgsl: Double,
    val gxh: String,
    val gxms: String,
    val scrid: String,
    val scrxm: String,
    val bfsl: Double,
    val blsl: Double,
    val ccsl: Double,
    val fxsl: Double,
    val hgsl: Double
)