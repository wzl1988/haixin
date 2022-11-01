package com.eohi.hx.ui.main.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/1/11 10:36
 */
class InStorageModel {
    var orderId //Agv任务Id
            : String? = null
    var orderNo //入库单号
            : String? = null
    var locationId //五金仓库ID  非五金 locationpoint终点
            = 0
    var rCId //原因码Id
            = 0
    var orderTypeId //单据类型，入库单默认35
            = 0
    var fromSystem //来源系统
            : String? = null
    var businessType //业务类型
            : String? = null
    var barcode //外箱码
            : String? = null
    var createUserId //创建用户Id
            : String? = null
    var createUserName //创建用户名
            : String? = null
    var kWH //库位号
            : String? = null

}