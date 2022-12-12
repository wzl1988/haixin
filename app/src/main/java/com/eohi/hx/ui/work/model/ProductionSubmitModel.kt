package com.eohi.hx.ui.work.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/8 16:14
 */
data class ProductionSubmitModel (
    var cardno: String? = null,
    var produceuserid: String? = null,
    var zrr: String? = null,
    var scbz: String? = null,
    var producetype: String? = null,
    var gxno: String? = null,
    var gxtxh:Int=0,
    var sl: Int = 0,
    var equno: String? = null,
    var jgdybh: String? = null,
    var userid: String? = null,
    var wlbs: String? = null,
    var isfinish: Int? = 0,
    var boxno: String? = null,
    var bc: String? = null,
    var dbxx: String? = null,
    var dbkss: Int = 0,
    var dbjss: Int = 0,
    var scrq: String? = null,
    var bz: String? = null, //备注
    var sfjj: String? = null,
    var rggs:Double =0.0,
    var tmh:String? =null
):Cloneable{
    public override fun clone(): Any {
        return super.clone()
    }
}



data class ClModel(
    var cardno:String,
    var clbzh:String,
    var clwph:String,
    var gxh:String
)


data class ProductionSubList(
    var proCardScdjAddList:ArrayList<ProductionSubmitModel>,
    var clList:ArrayList<ClModel>
)




