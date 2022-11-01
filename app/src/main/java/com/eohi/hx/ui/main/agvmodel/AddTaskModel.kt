package com.eohi.hx.ui.main.agvmodel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2020/12/16 14:25
 * 添加任务
 */
class AddTaskModel {
    /**
     * areaId : 5
     * modelProcessCode : chuku
     * priority : 6
     * fromSystem : MES
     * orderId : 3215435
     * taskOrderDetail : [{"material":[{"materielNum ":"3284 345 ","materielName ":"康王 356 ml ","materielAccount ":"100"}],"taskPath":"货架区 34,工作台 22","shelfNumber":"80000032","storageNum":"库位 38,库位 69","actionParam":{"workStationHeight":"","materielBoxWidth":""}}]
     */
    var areaId = 0
    var modelProcessCode: String? = null
    var priority = 0
    var fromSystem: String? = null
    var orderId: String? = null
    var taskOrderDetail: List<TaskOrderDetailBean>? = null

    class TaskOrderDetailBean {
        /**
         * material : [{"materielNum ":"3284 345 ","materielName ":"康王 356 ml ","materielAccount ":"100"}]
         * taskPath : 货架区 34,工作台 22
         * shelfNumber : 80000032
         * storageNum : 库位 38,库位 69
         * actionParam : {"workStationHeight":"","materielBoxWidth":""}
         */
        var taskPath: String? = null
        var shelfNumber: String? = null
        var storageNum: String? = null
        var actionParam: ActionParamBean? = null
        var material: List<MaterialBean>? = null

        class ActionParamBean {
            /**
             * workStationHeight :
             * materielBoxWidth :
             */
            var workStationHeight: String? = null
            var materielBoxWidth: String? = null

        }

        class MaterialBean {
            /**
             * materielNum  : 3284 345
             * materielName  : 康王 356 ml
             * materielAccount  : 100
             */
            var materielNum: String? = null
            var materielName: String? = null
            var materielAccount: String? = null

        }
    }
}