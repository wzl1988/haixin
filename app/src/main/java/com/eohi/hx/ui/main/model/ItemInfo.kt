package com.eohi.hx.ui.main.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/1/10 16:30
 * 物品信息
 */
class ItemInfo {
    /**
     * PurchaseOrderNo : 111111
     * BIRId : 1
     * BarCode : 11112222
     * ALDId : 1
     * ItemId : 55
     * ItemNo : 21307844
     * ItemName : F517EVR后支架（表面处理）（10026701）（4800 180-P02 013A）
     * Spec : “”
     * Unit : PCS
     * Quantity : 12
     * QTId : 163
     * QualityTaskNo : ZJ00000002
     * CreateTime : 2021-01-10 16:28:26
     * CreateUserId : 1
     * CreateUserName : admin
     */

    var CGDDH: String? = null
    var GGMS:  String? = null
    var WPMC:  String? = null
    var ZDW:  String? = null
    var ZSL:  String? = null
    var wph:  String? = null
    var tmh:String? = null //条码号
    var cktype:Int = 0//仓库类型
    var ckh:String? =null
    var kwh:String? =null
    var FSL :String? =null
    var fckh:String? =null
    var fkwh:String? =null
    var fhtzdh:String?=null //发货通知单号
    var tzhsl:String? = null //调整后数量
    var khh:String?= null//客户号
    var yym:String?= null//原因码
    var gdh:String? =null //工单号
    var khwph:String? =null //客户物品号
    var scrwdh:String? =null //生产任务单号
    var cxh:String?= null //产线


    val fdw: String?= null //辅助单位
    val fsl: Double?= null //辅助数量
    val ph: String?=null //批号
    var ERPSCDDH:String?=null //sap工单号

}