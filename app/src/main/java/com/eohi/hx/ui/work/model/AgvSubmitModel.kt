package com.eohi.hx.ui.work.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/8 18:17
 * 公司号(gsh)，用户ID(userid)，AGV业务类型码(agvywlxm)，起点(qd)，
 * 终点(zd)，来源(ly)，来源单据号(lydjh)，来源单据行号(lydjhh)，产线编码(cxbm)
 */
data class AgvSubmitModel(
        var gsh:String,
        var userid:String,
        var agvywlxm: String,
        var qd:String,
        var zd:String,
        var ly:String,
        var lydjh:String,
        var lydjhh:Int,
        var cxbm:String
)

