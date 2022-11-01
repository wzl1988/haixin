package com.eohi.hx.ui.work.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/8 16:14
 */
class ProductionSubmitModel {
    var cardno:String?=null
    var produceuserid :String?=null
    var zrr:String?= null
    var scbz:String? =null
    var producetype:String? =null
    var gxno:String? =null
    var sl:Int = 0
    var equno:String? =null
    var jgdybh:String?= null
    var userid:String? =null
    var wlbs:String?=null
    var isfinish:String?= null
    var boxno:String? =null
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




