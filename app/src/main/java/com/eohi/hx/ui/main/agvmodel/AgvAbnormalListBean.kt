package com.eohi.hx.ui.main.agvmodel

import java.io.Serializable

/**
 *@author: YH
 *@date: 2022/1/13
 *@desc:agv异常处理列表实体类
 */
data class AgvAbnormalListBean(
    val ROWNU: Int,
    val rwid: String,
    val rwzt: String,
    val ywlxm: String,
    val ywlx: String,
    val ch: String,
    val qd: String,
    val zd: String,
    val cjrid: String,
    val cjsj: String,
    val ZHZXSJ: String
) :Serializable
