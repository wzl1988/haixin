package com.eohi.hx.ui.main.agvmodel

/**
 *@author: YH
 *@date: 2022/1/13
 *@desc: agv异常处理提交实体类
 */
data class AgvAbnormalPostBean(
    val rwid: String,
    val nowdw: String,
    val clfs: String//异常状态7,3对应数字1，5重发对应数字2，取消对应数字3
)
