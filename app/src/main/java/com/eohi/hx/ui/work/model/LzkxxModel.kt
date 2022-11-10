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
    val BZS: Int,
    val DSCSL: Int,
    val GG: String,
    val LZKKH: String,
    val RWDH: String,
    val SCPH: String,
    val SCSL: Int,
    val TH: String,
    val WPH: String,
    val WPMC: String,
    val ZXBGGX: String,
    val jgdybh:String,
    val jgdymc:String,
    val items: List<LzkItem>
)

data class LzkItem(
    val BGSJ: String,
    val BZS: Double,
    val DQBZNSL: Double,
    val DQGXLJBGSL: Double,
    val GXH: String,
    val GXMS: String,
    val SCRID: String,
    val SCRXM: String,
    val bfsl: Double,
    val blsl: Double,
    val ccsl: Double,
    val fxsl: Double,
    val hgsl: Double
)